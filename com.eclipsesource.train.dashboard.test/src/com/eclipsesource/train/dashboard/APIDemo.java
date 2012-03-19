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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.eclipsesource.train.dashboard.internal.DashboardAggregatorImpl;
import com.eclipsesource.train.dashboard.internal.FetchFactory;
import com.eclipsesource.train.dashboard.model.Station;


public class APIDemo {
  
  public static void main( String[] args ) {
    FetchFactory.getStations();
    FetchFactory.getTrains( new Date() );
    DashboardAggregatorImpl impl = new DashboardAggregatorImpl();
    Date date = createDate();
    long start = System.currentTimeMillis();
    doActions( impl, date );
    doActions( impl, date );
    doActions( impl, date );
    doActions( impl, date );
    long stop = System.currentTimeMillis();
    System.out.println( "Duration: " + ( stop - start ) + "ms" );
  }

  private static Date createDate() {
    Calendar calendar = Calendar.getInstance();
    calendar.add( Calendar.DAY_OF_MONTH, 0 );
    Date date = calendar.getTime();
    return date;
  }

  private static void doActions( DashboardAggregator aggregator, Date date ) {
    RailwayInfo info = aggregator.getInfoForDate( date );
    System.out.println( "Information for " + date );
    System.out.println( "================================" );
    System.out.println( "Amount of Stations: " + info.getAllStations().size() );
    System.out.println( "Amount of Trains: " + info.getAllTrains().size() );
    DelayInfo delayInfo = info.getDelayInfo();
    printDelayInfo( date, delayInfo );
    System.out.println( "================================" );
  }

  private static void printDelayInfo( Date date, DelayInfo delayInfo ) {
    System.out.println( "Avarage Delay in minutes: " + delayInfo.getAverageDelayMinutes() );
    System.out.println( "Maximum Delay in minutes: " + delayInfo.getMaximumDelayMinutes() );
    System.out.println( "Trains with min. delay = 5min: " + delayInfo.getDelayedTrainsAmount( 5 ) );
    System.out.println( "Trains with min. delay = 10min: " + delayInfo.getDelayedTrainsAmount( 10 ) );
    System.out.println( "Trains with min. delay = 15min: " + delayInfo.getDelayedTrainsAmount( 15 ) );
    System.out.println( "Trains (%) with min. delay = 5min: " + delayInfo.getDelayedTrainsPercentage( 5 ) );
    System.out.println( "Trains (%) with min. delay = 10min: " + delayInfo.getDelayedTrainsPercentage( 10 ) );
    System.out.println( "Trains (%) with min. delay = 15min: " + delayInfo.getDelayedTrainsPercentage( 15 ) );
    System.out.println( "Stations delayed Trains sorted by amount:" );
    List<Station> stationsSortedByDelayAmount = delayInfo.getStationsSortedByDelayAmount( 10 );
    for( Station station : stationsSortedByDelayAmount ) {
      System.out.println( "  " + station.getName() + ", delays " 
                          + delayInfo.getDelayAmountForStation( station.getId() ) );
    }
    System.out.println( "Stations delayed Trains sorted by minutes: " );
    List<Station> stationsSortedByDelayMinutes = delayInfo.getStationsSortedByDelayMinutes( 10 );
    for( Station station : stationsSortedByDelayMinutes ) {
      System.out.println( "  " + station.getName() + ", delay minutes " 
                          + delayInfo.getDelayMinutesForStation( station.getId() ) );
    }
    System.out.println( "Stations delayed Trains sorted by percentage: " );
    List<Station> stationsSortedByDelayPercentage = delayInfo.getStationsSortedByDelayPercentage( 10 );
    for( Station station : stationsSortedByDelayPercentage ) {
      System.out.println( "  " + station.getName() + ", % of delayed trains " 
                          + delayInfo.getDelayPercentageForStation( station.getId() ) );
    }
  }
}
