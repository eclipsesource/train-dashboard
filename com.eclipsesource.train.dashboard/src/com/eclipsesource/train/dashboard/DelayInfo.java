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


public interface DelayInfo {

  List<Station> getStationsSortedByDelayAmount( Date date );

  List<Station> getStationsSortedByDelayAmount( Date date, int maxSize );

  int getDelayAmountForStation( int stationId, Date date );

  List<Station> getStationsSortedByDelayMinutes( Date date );

  List<Station> getStationsSortedByDelayMinutes( Date date, int maxSize );

  int getDelayMinutesForStation( int stationId, Date date );

  List<Station> getStationsSortedByDelayPercentage( Date date );

  List<Station> getStationsSortedByDelayPercentage( Date date, int maxSize );

  double getDelayPercentageForStation( int stationId, Date date );

  int getAvarageDelayMinutes( Date date );

  int getMaximumDelayMinutes( Date date );

  int getDelayedTrainsAmount( Date date, int minDelay );

  double getDelayedTrainsPercentage( Date date, int minDelay );
}
