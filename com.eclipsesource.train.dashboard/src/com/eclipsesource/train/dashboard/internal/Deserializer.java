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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.eclipsesource.train.dashboard.model.Station;
import com.eclipsesource.train.dashboard.model.Train;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;


public class Deserializer {

  public List<Station> getStations( InputStream stream ) {
    JsonObject element = getParsedStream( stream );
    return createStationsList( element );
  }

  private List<Station> createStationsList( JsonObject element ) {
    List<Station> result = new ArrayList<Station>();
    Set<Entry<String, JsonElement>> entrySet = element.entrySet();
    Gson gson = new Gson();
    for( Entry<String, JsonElement> entry : entrySet ) {
      if( !entry.getKey().equals( "cities" ) ) {
        result.add( gson.fromJson( entry.getValue(), Station.class ) );
      }
    }
    return result;
  }

  public List<Train> getTrains( InputStream stream ) {
    JsonObject element = getParsedStream( stream );
    return createTrainList( element );
  }
  
  private List<Train> createTrainList( JsonObject element ) {
    List<Train> result = new ArrayList<Train>();
    Set<Entry<String, JsonElement>> entrySet = element.entrySet();
    Gson gson = createTrainGson();
    for( Entry<String, JsonElement> entry : entrySet ) {
      result.add( gson.fromJson( entry.getValue(), Train.class ) );
    }
    return result;
  }

  private Gson createTrainGson() {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter( Date.class, new DateDeserializer() );
    Gson gson = gsonBuilder.create();
    return gson;
  }

  private JsonObject getParsedStream( InputStream json ) {
    JsonParser parser = new JsonParser();
    JsonReader reader = new JsonReader( new InputStreamReader( json ) );
    JsonObject element = parser.parse( reader ).getAsJsonObject();
    return element;
  }
}
