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
package com.eclipsesource.train.dashboard.ui;

import org.eclipse.rwt.application.Application;
import org.eclipse.rwt.application.ApplicationConfiguration;


public class DashboardConfiguration implements ApplicationConfiguration {

  public void configure( Application application ) {
    application.addEntryPoint( "/dashboard", EntryPoint.class, null );
    application.addEntryPoint( "/chart", BarChartDemo.class, null );
  }
}
