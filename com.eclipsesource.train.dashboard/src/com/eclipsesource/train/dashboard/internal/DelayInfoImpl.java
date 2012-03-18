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
import java.util.HashMap;
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
  
  private static final int DEFAULT_VALUE = -10000;
  
  private final Date date;
  private final Map<Integer, List<Station>> delaysByAmount;
  private final Map<Integer, List<Station>> delaysByMinutes;
  private final Map<Integer, List<Station>> delaysByPercentage;
  private final Map<Integer, Integer> delayAmountForStations;
  private final Map<Integer, Integer> delayMinutesForStations;
  private final Map<Integer, Double> delayPercentageForStations;
  private final Map<Integer, Integer> delayedTrainAmounts;
  private final Map<Integer, Double> delayedTrainPercentages;
  private int avarageDelayMinutes;
  private int maximumDelayMinutes;

  public DelayInfoImpl( Date date ) {
    this.date = date;
    this.delaysByAmount = new HashMap<Integer, List<Station>>();
    this.delaysByMinutes = new HashMap<Integer, List<Station>>();
    this.delaysByPercentage = new HashMap<Integer, List<Station>>();
    this.delayAmountForStations = new HashMap<Integer, Integer>();
    this.delayMinutesForStations = new HashMap<Integer, Integer>();
    this.delayPercentageForStations = new HashMap<Integer, Double>();
    this.avarageDelayMinutes = DEFAULT_VALUE;
    this.maximumDelayMinutes = DEFAULT_VALUE;
    this.delayedTrainAmounts = new HashMap<Integer, Integer>();
    this.delayedTrainPercentages = new HashMap<Integer, Double>();
  }
  
  public List<Station> getStationsSortedByDelayAmount() {
    return getStationsSortedByDelayAmount( -1 );
  }
  
  public List<Station> getStationsSortedByDelayAmount( int maxSize ) {
    Integer key = Integer.valueOf( maxSize );
    List<Station> result = delaysByAmount.get( key );
    if( result == null ) {
      result = doGetStationsSortedByDelayAmount( maxSize, key );
    }
    return result;
  }

  private List<Station> doGetStationsSortedByDelayAmount( int maxSize, Integer key ) {
    List<Station> tmpList = new ArrayList<Station>();
    List<Train> trains = TrainUtil.getTrainsForDate( date );
    Map<Integer, List<Integer>> delayedStations = DelayCalculationUtil.createStationDelayMap( tmpList, trains );
    Comparator<Station> comparator = new DelayAmountComparator( delayedStations );
    Collections.sort( tmpList, comparator );
    List<Station> result = DelayCalculationUtil.reduceList( tmpList, maxSize );
    delaysByAmount.put( key, result );
    return result;
  }
  
  public int getDelayAmountForStation( int stationId ) {
    Integer key = Integer.valueOf( stationId );
    Integer result = delayAmountForStations.get( key );
    if( result == null ) {
      int tmpResult = DelayCalculationUtil.getDelayAmountForStation( stationId, date );
      result = Integer.valueOf( tmpResult );
      delayAmountForStations.put( key, result );
    }
    return result.intValue();
  }
  
  public List<Station> getStationsSortedByDelayMinutes() {
    return getStationsSortedByDelayMinutes( -1 );
  }
  
  public List<Station> getStationsSortedByDelayMinutes( int maxSize ) {
    Integer key = Integer.valueOf( maxSize );
    List<Station> result = delaysByMinutes.get( key );
    if( result == null ) {
      result = doGetStationsSortedByDelayMinutes( maxSize, key );
    }
    return result;
  }

  private List<Station> doGetStationsSortedByDelayMinutes( int maxSize, Integer key ) {
    List<Station> tmpResult = new ArrayList<Station>();
    List<Train> trains = TrainUtil.getTrainsForDate( date );
    Map<Integer, List<Integer>> delayedStations = DelayCalculationUtil.createStationDelayMap( tmpResult, trains );
    Comparator<Station> comparator = new DelayMinutesComparator( delayedStations );
    Collections.sort( tmpResult, comparator );
    List<Station> result = DelayCalculationUtil.reduceList( tmpResult, maxSize );
    delaysByMinutes.put( key, result );
    return result;
  }
  
  public int getDelayMinutesForStation( int stationId ) {
    Integer key = Integer.valueOf( stationId );
    Integer result = delayMinutesForStations.get( key );
    if( result == null ) {
      result = doGetDelayMinutesForStation( stationId, key );
    }
    return result.intValue();
  }

  private Integer doGetDelayMinutesForStation( int stationId, Integer key ) {
    List<Train> trains = TrainUtil.getTrainsForDate( date );
    Map<Integer, List<Integer>> delayMap 
      = DelayCalculationUtil.createStationDelayMap( new ArrayList<Station>(), trains );
    List<Integer> list = delayMap.get( Integer.valueOf( stationId ) );
    Integer result = Integer.valueOf( DelayCalculationUtil.calculateDelayMinutes( list ) );
    delayMinutesForStations.put( key, result );
    return result;
  }
  
  public List<Station> getStationsSortedByDelayPercentage() {
    return getStationsSortedByDelayPercentage( -1 );
  }
  
  public List<Station> getStationsSortedByDelayPercentage( int maxSize ) {
    Integer key = Integer.valueOf( maxSize );
    List<Station> result = delaysByPercentage.get( key );
    if( result == null ) {
      result = doGetStationsSortedByDelayPercentage( maxSize, key );
    }
    return result;
  }

  private List<Station> doGetStationsSortedByDelayPercentage( int maxSize, Integer key ) {
    List<Station> tmpResult = new ArrayList<Station>();
    List<Train> trains = TrainUtil.getTrainsForDate( date );
    DelayCalculationUtil.createStationDelayMap( tmpResult, trains );
    Comparator<Station> comparator = new DelayPercentageComparator( date );
    Collections.sort( tmpResult, comparator );
    List<Station> result = DelayCalculationUtil.reduceList( tmpResult, maxSize );
    delaysByPercentage.put( key, result );
    return result;
  }
  
  public double getDelayPercentageForStation( int stationId ) {
    Integer key = Integer.valueOf( stationId );
    Double result = delayPercentageForStations.get( key );
    if( result == null ) {
      int delayAmount = getDelayAmountForStation( stationId );
      List<Train> trains = DelayCalculationUtil.getTrainsForStation( stationId, date );
      result = Double.valueOf( delayAmount / ( ( double )trains.size() / 100 ) );
      delayPercentageForStations.put( key, result );
    }
    return result.doubleValue();
  }
  
  public int getAvarageDelayMinutes() {
    if( avarageDelayMinutes == DEFAULT_VALUE ) {
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
      avarageDelayMinutes = delayAmount / delayNumber;
    }
    return avarageDelayMinutes;
  }
  
  public int getMaximumDelayMinutes() {
    if( maximumDelayMinutes == DEFAULT_VALUE ) {
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
      maximumDelayMinutes = maxDelay;
    }
    return maximumDelayMinutes;
  }
  
  public int getDelayedTrainsAmount( int minDelay ) {
    Integer key = Integer.valueOf( minDelay );
    Integer result = delayedTrainAmounts.get( key );
    if( result == null ) {
      result = doGetDelayedTrainsAmount( minDelay, key );
    }
    return result.intValue();
  }

  private Integer doGetDelayedTrainsAmount( int minDelay, Integer key ) {
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
    Integer result = Integer.valueOf( delayCount );
    delayedTrainAmounts.put( key, result );
    return result;
  }
  
  public double getDelayedTrainsPercentage( int minDelay ) {
    Integer key = Integer.valueOf( minDelay );
    Double result = delayedTrainPercentages.get( key );
    if( result == null ) {
      int delayCount = getDelayedTrainsAmount( minDelay );
      result = Double.valueOf(  delayCount / ( ( double )TrainUtil.getTrainsForDate( date ).size() / 100 ) );
      delayedTrainPercentages.put( key, result );
    }
    return result.doubleValue();
  }
}
