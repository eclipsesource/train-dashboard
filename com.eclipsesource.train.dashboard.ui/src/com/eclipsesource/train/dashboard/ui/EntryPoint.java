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

import java.util.Date;

import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.eclipsesource.train.dashboard.DashboardAggregator;
import com.eclipsesource.train.dashboard.RailwayInfo;


public class EntryPoint implements IEntryPoint {

  public int createUI() {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setLayout( new GridLayout( 1, false ) );
    
    DashboardAggregator aggregator = Activator.getAggregator();
    RailwayInfo info = aggregator.getInfoForDate( new Date() );
    
    Label label = new Label( shell, SWT.NONE );
    label.setLayoutData( new GridData( SWT.BEGINNING, SWT.CENTER, false, false ) );
    label.setText( "Maximum Delay Time (min) " + info.getDelayInfo().getMaximumDelayMinutes() );
    
    
    shell.setMaximized( true );
    shell.open();
    return 0;
  }
}
