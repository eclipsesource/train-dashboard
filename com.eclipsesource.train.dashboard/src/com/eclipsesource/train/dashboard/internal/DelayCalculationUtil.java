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
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eclipsesource.train.dashboard.model.Station;
import com.eclipsesource.train.dashboard.model.StationInfo;
import com.eclipsesource.train.dashboard.model.Train;


public class DelayCalculationUtil {
  
  public static class DelayAmountComparator implements Comparator<Station> {
    
    private final Map<Integer, List<Integer>> delayedStations;

    public DelayAmountComparator( Map<Integer, List<Integer>> delayStations ) {
      this.delayedStations = delayStations;
    }

    public int compare( Station o1, Station o2 ) {
      List<Integer> o1Delays = delayedStations.get( new Integer( o1.getId() ) );
      List<Integer> o2Delays = delayedStations.get( new Integer( o2.getId() ) );
      if( o1Delays.size() > o2Delays.size() ) {
        return -1;
      } else if( o1Delays.size() < o2Delays.size() ) {
        return 1;
      } else {
        return 0;
      }
    }
    
  }
  
  public static class DelayMinutesComparator implements Comparator<Station> {
    
    private final Map<Integer, List<Integer>> delayedStations;
    
    public DelayMinutesComparator( Map<Integer, List<Integer>> delayStations ) {
      this.delayedStations = delayStations;
    }
    
    public int compare( Station o1, Station o2 ) {
      List<Integer> o1Delays = delayedStations.get( new Integer( o1.getId() ) );
      List<Integer> o2Delays = delayedStations.get( new Integer( o2.getId() ) );
      int o1DelayMinutes = calculateDelayMinutes( o1Delays );
      int o2DelayMinutes = calculateDelayMinutes( o2Delays );
      if( o1DelayMinutes > o2DelayMinutes ) {
        return -1;
      } else if( o1DelayMinutes < o2DelayMinutes ) {
        return 1;
      } else {
        return 0;
      }
    }

  }
  
  public static class DelayPercentageComparator implements Comparator<Station> {
    
    private final Date date;
    private Map<Integer, List<Train>> trainMap;
    private Map<Integer, Integer> delayMap;
    
    public DelayPercentageComparator( Date date ) {
      this.date = date;
      trainMap = new HashMap<Integer, List<Train>>();
      delayMap = new HashMap<Integer, Integer>();
    }
    
    public int compare( Station o1, Station o2 ) {
      List<Train> o1Trains = getStationTrains( o1 );
      List<Train> o2Trains = getStationTrains( o2 );
      int o1DelayAmount = getStationDelayAmount( o1 );
      int o2DelayAmount = getStationDelayAmount( o2 );
      if( !o1Trains.isEmpty() && o2Trains.isEmpty() ) {
        return -1;
      } else if( o1Trains.isEmpty() && !o2Trains.isEmpty() ) {
        return 1;
      }
      double o1Percentage = o1DelayAmount / ( ( double )o1Trains.size() / 100 ); 
      double o2Percentage = o2DelayAmount / ( ( double )o2Trains.size() / 100 ); 
      if( o1Percentage > o2Percentage ) {
        return -1;
      } else if( o1Percentage < o2Percentage ) {
        return 1;
      } else {
        return 0;
      }
    }

    private List<Train> getStationTrains( Station station ) {
      List<Train> trains = trainMap.get( Integer.valueOf( station.getId() ) );
      if( trains == null ) {
        trains = getTrainsForStation( station.getId(), date );
        trainMap.put( Integer.valueOf( station.getId() ), trains );
      }
      return trains;
    }

    private int getStationDelayAmount( Station station ) {
      int delayAmount = 0;
      if( delayMap.get( Integer.valueOf( station.getId() ) ) == null ) {
        delayAmount = getDelayAmountForStation( station.getId(), date );
        delayMap.put( Integer.valueOf( station.getId() ), Integer.valueOf( delayAmount ) );
      } else {
        delayAmount = delayMap.get( Integer.valueOf( station.getId() ) ).intValue();
      }
      return delayAmount;
    }
    
  }
  
  public static List<Train> getTrainsForStation( int stationId, Date date ) {
    List<Train> result = new ArrayList<Train>();
    List<Train> trains = TrainUtil.getTrainsForDate( date );
    for( Train train : trains ) {
      List<StationInfo> stations = train.getStations();
      for( StationInfo stationInfo : stations ) {
        if( stationInfo.getStationId() == stationId ) {
          result.add( train );
        }
      }
    }
    return result;
  }
  
  public static int getDelayAmountForStation( int stationId, Date date ) {
    List<Train> trains = TrainUtil.getTrainsForDate( date );
    Map<Integer, List<Integer>> delayMap 
      = DelayCalculationUtil.createStationDelayMap( new ArrayList<Station>(), trains );
    List<Integer> list = delayMap.get( Integer.valueOf( stationId ) );
    return list.size();
  }
  
  public static Map<Integer, List<Integer>> createStationDelayMap( List<Station> stationList,
                                                                   List<Train> trains )
  {
    final Map<Integer, List<Integer>> delayedStations = new HashMap<Integer, List<Integer>>();
    for( Train train : trains ) {
      List<StationInfo> stations = train.getStations();
      for( StationInfo stationInfo : stations ) {
        if( stationInfo.getDelay() > 0 ) {
          handleDelay( stationList, delayedStations, stationInfo );
        }
      }
      
    }
    return delayedStations;
  }

  private static void handleDelay( List<Station> stationList,
                                   final Map<Integer, List<Integer>> delayedStations,
                                   StationInfo stationInfo )
  {
    int stationId = stationInfo.getStationId();
    List<Integer> delays = delayedStations.get( Integer.valueOf( stationId ) );
    if( delays == null ) {
      stationList.add( getStationById( stationId ) );
      delays = new ArrayList<Integer>();
      delayedStations.put( Integer.valueOf( stationId ), delays );
    }
    delays.add( Integer.valueOf( stationInfo.getDelay() ) );
  }

  public static Station getStationById( int id ) {
    List<Station> allStations = FetchFactory.getStations();
    for( Station station : allStations ) {
      if( station.getId() == id ) {
        return station;
      }
    }
    return null;
  }
  
  public static <T> List<T> reduceList( List<T> list, int expectedSize ) {
    if( expectedSize < 0 ) {
      return new ArrayList<T>( list );
    }
    ArrayList<T> result = new ArrayList<T>();
    for( int i = 0; i < expectedSize; i++ ) {
      result.add( list.get( i ) );
    }
    return result;
  }
  
  public static int calculateDelayMinutes( List<Integer> delays ) {
    int result = 0;
    for( Integer integer : delays ) {
      result += integer.intValue();
    }
    return result;
  }
}
