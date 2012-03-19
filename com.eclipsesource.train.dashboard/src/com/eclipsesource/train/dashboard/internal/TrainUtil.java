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
  
  private static final String MAX_AGE_PROPERTY = "com.eclipsesource.train.dashboard.max.age";
  
  public static List<Train> getTrainsForDate( Date date ) {
    return FetchFactory.getTrains( date );
  }
  
  public static boolean isToOld( Date toUse ) {
    int maxAge = getMaxAge();
    boolean result = false;
    TrainServiceKey key = new TrainServiceKey( toUse );
    Date today = new Date();
    if( key.equals( new TrainServiceKey( today ) ) ) {
      long toUseInMinutes = ( toUse.getTime() / 1000 ) / 60;
      long todayInMinutes = ( today.getTime() / 1000 ) / 60;
      result = ( todayInMinutes - toUseInMinutes ) > maxAge; 
    }
    return result;
  }
  
  private static int getMaxAge() {
    String maxAgeString = System.getProperty( MAX_AGE_PROPERTY );
    if( maxAgeString != null) {
      return Integer.parseInt( maxAgeString );
    }
    throw new IllegalStateException( "The property " + MAX_AGE_PROPERTY + " needs to be set" );
  }

  private TrainUtil() {
    // prevent instantiation
  }
}
