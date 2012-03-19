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
import java.util.concurrent.ConcurrentHashMap;

import com.eclipsesource.train.dashboard.DashboardAggregator;
import com.eclipsesource.train.dashboard.RailwayInfo;


// Exists only once because it's an OSGi service
public class DashboardAggregatorImpl implements DashboardAggregator {
  
  private ConcurrentHashMap<TrainServiceKey, RailwayInfo> infoCache;
  
  public DashboardAggregatorImpl() {
    infoCache = new ConcurrentHashMap<TrainServiceKey, RailwayInfo>();
  }

  public RailwayInfo getInfoForDate( Date date ) {
    TrainServiceKey key = new TrainServiceKey( date );
    RailwayInfo result = infoCache.get( key );
    if( result == null ) {
      result = createRailwayInfo( date, key );
    } else if( TrainUtil.isToOld( date ) ) {
      infoCache.remove( key );
      result = createRailwayInfo( date, key );
    }
    return result;
  }

  private RailwayInfo createRailwayInfo( Date date, TrainServiceKey key ) {
    RailwayInfo result = new RailwayInfoImpl( date );
    RailwayInfo putResult = infoCache.putIfAbsent( key, result );
    if( putResult != null ) {
      result = putResult;
    }
    return result;
  }
  
}
