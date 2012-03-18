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

import com.eclipsesource.train.dashboard.model.Train;


public class TrainUtil {
  
  private static final int MAX_AGE_OF_TODAYS_TRAINS_IN_MINUTES = 30;
  
  public static List<Train> getTrainsForDate( Date date ) {
    return FetchFactory.getTrains( date );
  }
  
  public static boolean isToOld( Date toUse ) {
    boolean result = false;
    TrainServiceKey key = new TrainServiceKey( toUse );
    Date today = new Date();
    if( key.equals( new TrainServiceKey( today ) ) ) {
      long toUseInMinutes = ( toUse.getTime() / 1000 ) / 60;
      long todayInMinutes = ( today.getTime() / 1000 ) / 60;
      result = ( todayInMinutes - toUseInMinutes ) > MAX_AGE_OF_TODAYS_TRAINS_IN_MINUTES; 
    }
    return result;
  }
  
  private TrainUtil() {
    // prevent instantiation
  }
}
