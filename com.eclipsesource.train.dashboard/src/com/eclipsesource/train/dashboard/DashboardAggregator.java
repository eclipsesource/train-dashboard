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

import java.util.Date;
import java.util.List;

import com.eclipsesource.train.dashboard.model.Station;
import com.eclipsesource.train.dashboard.model.Train;



public interface DashboardAggregator {

  List<Station> getAllStations();

  Station getStationById( int id );

  List<Train> getTrainsForDate( Date date );

  Train getTrainByNr( Date date, String trainNr );

  DelayInfo getDelayInfo();
  
}
