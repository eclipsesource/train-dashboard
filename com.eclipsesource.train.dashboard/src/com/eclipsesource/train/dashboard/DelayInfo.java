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


public interface DelayInfo {

  List<Station> getStationsSortedByDelayAmount();

  List<Station> getStationsSortedByDelayAmount(int maxSize );

  int getDelayAmountForStation( int stationId );

  List<Station> getStationsSortedByDelayMinutes();

  List<Station> getStationsSortedByDelayMinutes( int maxSize );

  int getDelayMinutesForStation( int stationId );

  List<Station> getStationsSortedByDelayPercentage();

  List<Station> getStationsSortedByDelayPercentage( int maxSize );

  double getDelayPercentageForStation( int stationId );

  int getAvarageDelayMinutes();

  int getMaximumDelayMinutes();

  int getDelayedTrainsAmount(int minDelay );

  double getDelayedTrainsPercentage( int minDelay );
}
