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

/**
 * All information is date specific. They are sticked to the date used in {@link DashboardAggregator}.
 */
public interface DelayInfo {

  /**
   * Returns all stations that have delayed trains sorted by the amount of delayed trains.
   */
  List<Station> getStationsSortedByDelayAmount();

  /**
   * Returns a given number of stations that have delayed trains sorted by the amount of delayed trains.
   */
  List<Station> getStationsSortedByDelayAmount(int maxSize );

  /**
   * Returns the sum of all delayed trains for a given station.
   */
  int getDelayAmountForStation( int stationId );

  /**
   * Returns all stations that have delayed trains sorted by the sum of delay minutes.
   */
  List<Station> getStationsSortedByDelayMinutes();

  /**
   * Returns a given number of stations that have delayed trains sorted by the sum of delay minutes.
   */
  List<Station> getStationsSortedByDelayMinutes( int maxSize );

  /**
   * Returns the sum of minutes for all delayed trains of a given station.
   */
  int getDelayMinutesForStation( int stationId );

  /**
   * Returns all stations that have delayed trains sorted by the percentage of delayed trains.
   * E.g. if a station has 10 trains a day and 3 are delayed, than it has a delay percentage of 30%.
   */
  List<Station> getStationsSortedByDelayPercentage();

  /**
   * Returns a given number of stations that have delayed trains sorted by the percentage of delayed trains.
   * E.g. if a station has 10 trains a day and 3 are delayed, than it has a delay percentage of 30%.
   */
  List<Station> getStationsSortedByDelayPercentage( int maxSize );

  /**
   * Returns the percentage of delays for a given station.
   * E.g. if a station has 10 trains a day and 3 are delayed, than it has a delay percentage of 30%.
   */
  double getDelayPercentageForStation( int stationId );

  /**
   * Returns the average delay time in minutes for all trains.
   */
  int getAverageDelayMinutes();

  /**
   * Returns the absolute maximum delay time of all trains.
   */
  int getMaximumDelayMinutes();

  /**
   * Returns the number of the delayed trains with a given minimum delay in minutes.
   */
  int getDelayedTrainsAmount(int minDelay );

  /**
   * Returns the percentage of delayed trains with a given minimum delay in minutes.
   */
  double getDelayedTrainsPercentage( int minDelay );

  /**
   * Returns all delayed trains for a given station.
   */
  List<Train> getDelayedTrainsForStation( int stationId );
}
