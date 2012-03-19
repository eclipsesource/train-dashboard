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

import java.util.List;

import com.eclipsesource.train.dashboard.model.Station;
import com.eclipsesource.train.dashboard.model.Train;


public interface RailwayInfo {
  
  /**
   * Returns all stations. Not date specific!
   */
  List<Station> getAllStations();

  /**
   * Returns a {@link Station} object for a given id. Not date specific!
   */
  Station getStationById( int id );

  /**
   * Returns all trains for the date used in {@link DashboardAggregator}.
   */
  List<Train> getAllTrains();

  /**
   * Returns a train by a given number for the date used in {@link DashboardAggregator}.
   */
  Train getTrainByNr( String trainNr );

  /**
   * Returns a {@link DelayInfo} object for the date used in {@link DashboardAggregator}.
   */
  DelayInfo getDelayInfo();
}
