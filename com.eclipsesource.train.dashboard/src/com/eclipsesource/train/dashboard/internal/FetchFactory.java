/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.train.dashboard.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.eclipsesource.train.dashboard.model.Station;
import com.eclipsesource.train.dashboard.model.Train;


public class FetchFactory {
  
  private static final String REST_API_HOST = "http://zugmonitor.sueddeutsche.de/api";
  private static final String DATA_LOCATION_PROPERTY = "com.eclipsesource.train.dashboard.data.location";
  
  private static ConcurrentHashMap<TrainServiceKey, List<Train>> trainCache 
    = new ConcurrentHashMap<TrainServiceKey, List<Train>>();
  private static volatile List<Station> stations;
  
  public static List<Station> getStations() {
    if( stations == null ) {
      fetchSationsFromResource();
    }
    return stations;
  }

  private static void fetchSationsFromResource() {
    File dataFolder = getDataFolder();
    File stationsFile = new File( dataFolder.getPath() + File.separator + "stations.json" );
    if( !stationsFile.exists() ) {
      fetchFromWebservice( stationsFile, REST_API_HOST + "/stations" );
    }
    Deserializer deserializer = new Deserializer();
    try {
      stations = deserializer.getStations( new FileInputStream( stationsFile ) );
    } catch( FileNotFoundException shouldNotHappen ) {
      throw new IllegalStateException( "Could not read file " + stationsFile.getAbsolutePath() );
    }
  }
  
  public static List<Train> getTrains( Date date ) {
    TrainServiceKey key = new TrainServiceKey( date );
    List<Train> result = trainCache.get( key );
    File dataFolder = getDataFolder();
    File trainFile = new File( dataFolder.getPath() + File.separator + key.toString() + ".json" );
    if( trainFileIsToOld( trainFile, date ) || result == null ) {
      result = fetchTrainsFromResource( key, trainFile );
    }
    return result;
  }

  private static List<Train> fetchTrainsFromResource( TrainServiceKey key, File trainFile ) {
    List<Train> result = null;
    if( !trainFile.exists() ) {
      fetchFromWebservice( trainFile, REST_API_HOST + "/trains/" + key.toString() );
    }
    Deserializer deserializer = new Deserializer();
    try {
      result = deserializer.getTrains( new FileInputStream( trainFile ) );
      List<Train> putResult = trainCache.putIfAbsent( key, result );
      if( putResult != null ) {
        result = putResult;
      }
    } catch( FileNotFoundException shouldNotHappen ) {
      throw new IllegalStateException( "Could not read file " + trainFile.getAbsolutePath() );
    }
    return result;
  }

  private static File getDataFolder() {
    String dataLocationPath = getDataLocationPath();
    return ensureDataFolderExist( dataLocationPath );
  }

  private static boolean trainFileIsToOld( File trainFile, Date date ) {
    boolean result = false;
    Date trainFileDate = new Date( trainFile.lastModified() );
    if( TrainUtil.isToOld( date ) || TrainUtil.isToOld( trainFileDate ) ) {
      trainFile.delete();
      trainCache.remove( date );
      result = true;
    }
    return result;
  }

  private static String getDataLocationPath() {
    String dataLocationPath = System.getProperty( DATA_LOCATION_PROPERTY );
    if( dataLocationPath == null ) {
      throw new IllegalStateException( "No data location available. You need to set the " 
                                       + DATA_LOCATION_PROPERTY 
                                       + " property" );
    }
    return dataLocationPath;
  }

  private static File ensureDataFolderExist( String dataLocationPath ) {
    File dataFolder = new File( dataLocationPath );
    if( !dataFolder.exists() ) {
      dataFolder.mkdir();
    }
    if( !dataFolder.isDirectory() || !dataFolder.canWrite()) {
      throw new IllegalStateException( "Invalid data location" );
    }
    return dataFolder;
  }

  private static void fetchFromWebservice( File fileToWrite, String url ) {
    try {
      System.out.println( "Call REST service " + url  + " (" + new Date() + ")" );
      FileWriter fileWriter = new FileWriter( fileToWrite );
      String json = StreamReaderUtil.readStream( new URL( url ).openStream() );
      fileWriter.write( json );
      fileWriter.close();
    } catch( IOException shouldNotHappen ) {
      throw new IllegalStateException( "Could not write json file " + fileToWrite.getAbsolutePath(), shouldNotHappen );
    }
  }
}
