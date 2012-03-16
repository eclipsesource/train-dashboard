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

import java.util.Date;
import java.util.List;

import com.eclipsesource.train.dashboard.DashboardAggregator;
import com.eclipsesource.train.dashboard.DelayInfo;
import com.eclipsesource.train.dashboard.model.Station;
import com.eclipsesource.train.dashboard.model.Train;


public class DashboardAggregatorImpl implements DashboardAggregator {
  
  
  private DelayInfo delayInfo;

  public DashboardAggregatorImpl() {
    delayInfo = new DelayInfoImpl();
  }

  public List<Station> getAllStations() {
    return FetchFactory.getStations();
  }
  
  public Station getStationById( int id ) {
    return DelayCalculationUtil.getStationById( id );
  }
  
  public List<Train> getTrainsForDate( Date date ) {
    return FetchFactory.getTrains( date );
  }
  
  public Train getTrainByNr( Date date, String trainNr ) {
    List<Train> trains = getTrainsForDate( date );
    for( Train train : trains ) {
      if( train.getTrainNr().equals( trainNr ) ) {
        return train;
      }
    }
    return null;
  }
  
  public DelayInfo getDelayInfo() {
    return delayInfo;
  }
  
}
