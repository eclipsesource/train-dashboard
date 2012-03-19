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


/**
 * The {@link DashboardAggregator} marks the entry point of the train API.
 */
public interface DashboardAggregator {

  /**
   * Creates a new {@link RailwayInfo} object for the given date. There will be only one {@link RailwayInfo} object
   * for each day except the info object for today. The object for today has a maximum age that can be configured
   * using the com.eclipsesource.train.dashboard.max.age system property.
   */
  RailwayInfo getInfoForDate( Date date );
  
}
