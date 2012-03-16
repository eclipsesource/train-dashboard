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

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.eclipsesource.train.dashboard.DashboardAggregator;


public class Activator implements BundleActivator {
  
  private static DashboardAggregator aggregator;
  private ServiceReference<?> serviceReference;

  public void start( BundleContext context ) throws Exception {
    serviceReference = context.getServiceReference( DashboardAggregator.class.getName() );
    Object service = context.getService( serviceReference );
    if( service == null ) {
      throw new IllegalStateException( "Need DashboardAggregator service" );
    }
    setAggregator( ( DashboardAggregator )service );
  }

  private static void setAggregator( DashboardAggregator aggregator ) {
    Activator.aggregator = aggregator;
  }

  public static DashboardAggregator getAggregator() {
    return aggregator;
  }

  public void stop( BundleContext context ) throws Exception {
    context.ungetService( serviceReference );
  }
}
