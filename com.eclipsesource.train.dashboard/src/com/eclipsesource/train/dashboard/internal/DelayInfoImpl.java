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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.eclipsesource.train.dashboard.DelayInfo;
import com.eclipsesource.train.dashboard.internal.DelayCalculationUtil.DelayAmountComparator;
import com.eclipsesource.train.dashboard.internal.DelayCalculationUtil.DelayMinutesComparator;
import com.eclipsesource.train.dashboard.internal.DelayCalculationUtil.DelayPercentageComparator;
import com.eclipsesource.train.dashboard.model.Station;
import com.eclipsesource.train.dashboard.model.StationInfo;
import com.eclipsesource.train.dashboard.model.Train;


public class DelayInfoImpl implements DelayInfo {
  
  public List<Station> getStationsSortedByDelayAmount( Date date ) {
    return getStationsSortedByDelayAmount( date, -1 );
  }
  
  public List<Station> getStationsSortedByDelayAmount( Date date, int maxSize ) {
    List<Station> result = new ArrayList<Station>();
    List<Train> trains = TrainUtil.getTrainsForDate( date );
    Map<Integer, List<Integer>> delayedStations = DelayCalculationUtil.createStationDelayMap( result, trains );
    Comparator<Station> comparator = new DelayAmountComparator( delayedStations );
    Collections.sort( result, comparator );
    return DelayCalculationUtil.reduceList( result, maxSize );
  }
  
  public int getDelayAmountForStation( int stationId, Date date ) {
    return DelayCalculationUtil.getDelayAmountForStation( stationId, date );
  }
  
  public List<Station> getStationsSortedByDelayMinutes( Date date ) {
    return getStationsSortedByDelayMinutes( date, -1 );
  }
  
  public List<Station> getStationsSortedByDelayMinutes( Date date, int maxSize ) {
    List<Station> result = new ArrayList<Station>();
    List<Train> trains = TrainUtil.getTrainsForDate( date );
    Map<Integer, List<Integer>> delayedStations = DelayCalculationUtil.createStationDelayMap( result, trains );
    Comparator<Station> comparator = new DelayMinutesComparator( delayedStations );
    Collections.sort( result, comparator );
    return DelayCalculationUtil.reduceList( result, maxSize );
  }
  
  public int getDelayMinutesForStation( int stationId, Date date ) {
    List<Train> trains = TrainUtil.getTrainsForDate( date );
    Map<Integer, List<Integer>> delayMap 
      = DelayCalculationUtil.createStationDelayMap( new ArrayList<Station>(), trains );
    List<Integer> list = delayMap.get( Integer.valueOf( stationId ) );
    return DelayCalculationUtil.calculateDelayMinutes( list );
  }
  
  public List<Station> getStationsSortedByDelayPercentage( Date date ) {
    return getStationsSortedByDelayPercentage( date, -1 );
  }
  
  public List<Station> getStationsSortedByDelayPercentage( Date date, int maxSize ) {
    List<Station> result = new ArrayList<Station>();
    List<Train> trains = TrainUtil.getTrainsForDate( date );
    DelayCalculationUtil.createStationDelayMap( result, trains );
    Comparator<Station> comparator = new DelayPercentageComparator( date );
    Collections.sort( result, comparator );
    return DelayCalculationUtil.reduceList( result, maxSize );
  }
  
  public double getDelayPercentageForStation( int stationId, Date date ) {
    int delayAmount = getDelayAmountForStation( stationId, date );
    List<Train> trains = DelayCalculationUtil.getTrainsForStation( stationId, date );
    return delayAmount / ( ( double )trains.size() / 100 );
  }
  
  public int getAvarageDelayMinutes( Date date ) {
    List<Train> trains = TrainUtil.getTrainsForDate( date );
    Map<Integer, List<Integer>> delays = DelayCalculationUtil.createStationDelayMap( new ArrayList<Station>(), trains );
    Set<Integer> keySet = delays.keySet();
    int delayNumber = 0;
    int delayAmount = 0;
    for( Iterator iterator = keySet.iterator(); iterator.hasNext(); ) {
      Integer key = ( Integer )iterator.next();
      List<Integer> list = delays.get( key );
      for( Integer delay : list ) {
        delayNumber++;
        delayAmount += delay.intValue();
      }
    }
    return delayAmount / delayNumber;
  }
  
  public int getMaximumDelayMinutes( Date date ) {
    List<Train> trains = TrainUtil.getTrainsForDate( date );
    Map<Integer, List<Integer>> delays = DelayCalculationUtil.createStationDelayMap( new ArrayList<Station>(), trains );
    Set<Integer> keySet = delays.keySet();
    int maxDelay = 0;
    for( Iterator iterator = keySet.iterator(); iterator.hasNext(); ) {
      Integer key = ( Integer )iterator.next();
      List<Integer> list = delays.get( key );
      for( Integer delay : list ) {
        if( delay.intValue() > maxDelay ) {
          maxDelay = delay.intValue();
        }
      }
    }
    return maxDelay;
  }
  
  public int getDelayedTrainsAmount( Date date, int minDelay ) {
    List<Train> trains = TrainUtil.getTrainsForDate( date );
    int delayCount = 0;
    for( Train train : trains ) {
      List<StationInfo> stations = train.getStations();
      for( StationInfo stationInfo : stations ) {
        if( stationInfo.getDelay() > 0 && stationInfo.getDelay() >= minDelay ) {
          delayCount++;
          break;
        }
      }
    }
    return delayCount;
  }
  
  public double getDelayedTrainsPercentage( Date date, int minDelay ) {
    int delayCount = getDelayedTrainsAmount( date, minDelay );
    return delayCount / ( ( double )TrainUtil.getTrainsForDate( date ).size() / 100 );
  }
}
