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
package com.eclipsesource.train.dashboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import com.eclipsesource.train.dashboard.internal.Deserializer;
import com.eclipsesource.train.dashboard.model.Station;
import com.eclipsesource.train.dashboard.model.Train;


public class DeserializerTest {
  
  @Test
  public void testDeserializeStations() {
    InputStream stream = getClass().getResourceAsStream( "/stations.json" );
    Deserializer deserializer = new Deserializer();

    List<Station> stations = deserializer.getStations( stream );
    
    assertEquals( 595, stations.size() );
    for( Station station : stations ) {
      assertTrue( station.getId() > 0 );
      assertTrue( station.getLat() > 0 );
      assertTrue( station.getLon() > 0 );
      assertNotNull( station.getName() );
    }
  }
  
  @Test
  public void testDeserializeTrains() {
    InputStream stream = getClass().getResourceAsStream( "/2012-03-09.json" );
    Deserializer deserializer = new Deserializer();
    
    List<Train> trains = deserializer.getTrains( stream );
    
    assertEquals( 834, trains.size() );
    for( Train train : trains ) {
      assertNotNull( train.getStarted() );
      assertNotNull( train.getFinished() );
      assertNotNull( train.getNextRun() );
      assertNotNull( train.getStations() );
      assertNotNull( train.getStatus() );
      assertNotNull( train.getTrainNr() );
    }
  }
  
}
