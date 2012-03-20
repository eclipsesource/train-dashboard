/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.train.dashboard.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.eclipsesource.train.dashboard.DashboardAggregator;
import com.eclipsesource.train.dashboard.RailwayInfo;
import com.eclipsesource.train.dashboard.ui.chart.Bar;
import com.eclipsesource.train.dashboard.ui.chart.Chart;

public class EntryPoint implements IEntryPoint {

  public enum DataView {
    Overview("Overview"), AvrgDelay("\u00D8 Delay Time"), MaxDelay("Max. Delay"), Trains("Trains"), Stations(
        "Stations");

    private final String title;

    DataView( String title ) {
      this.title = title;
    }

    public String getTitle() {
      return title;
    }
  }
  
  protected Shell dataShell;
  private Date currentDate = new Date();
  private List<IInfoListener> infoListeners = new ArrayList<IInfoListener>();
  private static int HISTORY = -30;
  
  public int createUI() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NO_TRIM );
    shell.setBackground( shell.getDisplay().getSystemColor( SWT.COLOR_BLACK ) );
    GridLayoutFactory.fillDefaults().spacing( 2, 2 ).numColumns( 2 ).applyTo( shell );
    createDataArea( shell );
    createNavigationArea( shell );
    DashboardAggregator aggregator = Activator.getAggregator();
    RailwayInfo info = aggregator.getInfoForDate( new Date() );
    fireInfoEvent( info );
    shell.setMaximized( true );
    shell.open();
    return 0;
  }

  protected void addInfoListener( IInfoListener listener ) {
    infoListeners.add( listener );
  }

  private void fireInfoEvent( RailwayInfo info ) {
    for( IInfoListener listener : this.infoListeners ) {
      listener.newInfo( info );
    }
  }

  private void createDataArea( Shell shell ) {
    Composite result = new Composite( shell, SWT.NONE );
    GridLayoutFactory.fillDefaults().numColumns( 1 ).applyTo( result );
    GridDataFactory.fillDefaults().align( SWT.FILL, SWT.FILL ).grab( true, true ).applyTo( result );
    result.setBackground( result.getDisplay().getSystemColor( SWT.COLOR_BLACK ) );
    ToolBar toolBar = new ToolBar( result, SWT.NONE );
    GridDataFactory.fillDefaults().grab( true, false ).align( SWT.FILL, SWT.TOP ).applyTo( toolBar );
    ToolItem prevItem = new ToolItem( toolBar, SWT.NONE );
    prevItem.setText( "Previous" );
    prevItem.addSelectionListener( new SelectionAdapter() {
      @Override
      public void widgetSelected( SelectionEvent e ) {
        rollToInfo(-1);
      }
    } );
    new ToolItem( toolBar, SWT.SEPARATOR );
    ToolItem titleItem = new ToolItem( toolBar, SWT.NONE );
    titleItem.setData( WidgetUtil.CUSTOM_VARIANT, "TITLE" );
    titleItem.setText( "Train Dashboard" );
    new ToolItem( toolBar, SWT.SEPARATOR );
    ToolItem nextItem = new ToolItem( toolBar, SWT.NONE );
    nextItem.setText( "Next" );
    nextItem.addSelectionListener( new SelectionAdapter() {
      @Override
      public void widgetSelected( SelectionEvent e ) {
        rollToInfo(1);
      }
    } );
  }

  private void createNavigationArea( Shell shell ) {
    Composite result = new Composite( shell, SWT.NONE );
    GridLayoutFactory.fillDefaults().spacing( 2, 2 ).applyTo( result );
    GridDataFactory.fillDefaults()
      .hint( 128, SWT.DEFAULT )
      .minSize( 128, SWT.DEFAULT )
      .align( SWT.FILL, SWT.FILL )
      .grab( false, true )
      .applyTo( result );
    result.setBackground( result.getDisplay().getSystemColor( SWT.COLOR_BLACK ) );
    MouseAdapter button1Listener = new MouseAdapter() {

      @Override
      public void mouseUp( MouseEvent e ) {
        closeDataShell();
      }
    };
    final Composite button1 = createButton( result, DataView.Overview, button1Listener );
    button1.setBackground( new Color( result.getDisplay(), 39, 39, 39 ) );
    button1.addMouseListener( new MouseAdapter() {

      @Override
      public void mouseUp( MouseEvent e ) {
        closeDataShell();
      }
    } );
    MouseAdapter button2Listener = new MouseAdapter() {

      @Override
      public void mouseUp( MouseEvent e ) {
        closeDataShell();
        Display display = button1.getDisplay();
        dataShell = new Shell( display, SWT.NO_TRIM | SWT.ON_TOP );
        int height = button1.getDisplay().getBounds().height - 49;
        int width = button1.getDisplay().getBounds().width - 130;
        dataShell.setBounds( 0, 49, width, height );
        dataShell.setBackground( display.getSystemColor( SWT.COLOR_BLACK ) );
        dataShell.setLayout( new FillLayout() );
        createChart( dataShell );
        dataShell.open();
      }
    };
    Composite button2 = createButton( result, DataView.AvrgDelay, button2Listener );
    button2.setBackground( new Color( shell.getDisplay(), 55, 55, 55 ) );
    Composite button3 = createButton( result, DataView.MaxDelay, null );
    button3.setBackground( new Color( shell.getDisplay(), 39, 39, 39 ) );
    Composite button4 = createButton( result, DataView.Trains, null );
    button4.setBackground( new Color( shell.getDisplay(), 55, 55, 55 ) );
    Composite button5 = createButton( result, DataView.Stations, null );
    button5.setBackground( new Color( shell.getDisplay(), 39, 39, 39 ) );
  }

  private Composite createButton( Composite parent, final DataView dataView, MouseListener listener )
  {
    Composite result = new Composite( parent, SWT.NONE );
    GridDataFactory.fillDefaults().hint( 128, 128 ).applyTo( result );
    GridLayoutFactory.fillDefaults().applyTo( result );
    final Label lblDigits = new Label( result, SWT.NONE );
    GridDataFactory.fillDefaults()
      .align( SWT.CENTER, SWT.CENTER )
      .grab( true, true )
      .applyTo( lblDigits );
    Font font = FontDescriptor.createFrom( lblDigits.getFont() )
      .setHeight( 56 )
      .createFont( lblDigits.getDisplay() );
    lblDigits.setFont( font );
    lblDigits.setForeground( new Color( result.getDisplay(), 200, 0, 0 ) );
    lblDigits.setText( "---" );
    final Label lblTitle = new Label( result, SWT.CENTER );
    GridDataFactory.fillDefaults()
      .align( SWT.FILL, SWT.FILL )
      .grab( true, false )
      .applyTo( lblTitle );
    lblTitle.setForeground( new Color( result.getDisplay(), 200, 0, 0 ) );
    lblTitle.setText( dataView.getTitle() );
    if( listener != null ) {
      result.addMouseListener( listener );
      lblDigits.addMouseListener( listener );
      lblTitle.addMouseListener( listener );
    }
    addInfoListener( new IInfoListener() {

      public void newInfo( RailwayInfo info ) {
        switch( dataView ) {
          case Overview:
            lblDigits.setText( "---" );
            lblDigits.setForeground( new Color( lblDigits.getDisplay(), 0, 200, 200 ) );
            lblTitle.setForeground( new Color( lblDigits.getDisplay(), 0, 200, 200 ) );
          break;
          case AvrgDelay:
            int averageDelayMinutes = info.getDelayInfo().getAverageDelayMinutes();
            lblDigits.setText( String.valueOf( averageDelayMinutes ) );
            if( averageDelayMinutes >= 15 ) {
              lblDigits.setForeground( new Color( lblDigits.getDisplay(), 200, 0, 0 ) );
              lblTitle.setForeground( new Color( lblDigits.getDisplay(), 200, 0, 0 ) );
            } else {
              lblDigits.setForeground( new Color( lblDigits.getDisplay(), 0, 200, 0 ) );
              lblTitle.setForeground( new Color( lblDigits.getDisplay(), 0, 200, 0 ) );
            }
          break;
          case MaxDelay:
            int maximumDelayMinutes = info.getDelayInfo().getMaximumDelayMinutes();
            lblDigits.setText( String.valueOf( maximumDelayMinutes ) );
            if( maximumDelayMinutes >= 120 ) {
              lblDigits.setForeground( new Color( lblDigits.getDisplay(), 200, 0, 0 ) );
              lblTitle.setForeground( new Color( lblDigits.getDisplay(), 200, 0, 0 ) );
            } else {
              lblDigits.setForeground( new Color( lblDigits.getDisplay(), 0, 200, 0 ) );
              lblTitle.setForeground( new Color( lblDigits.getDisplay(), 0, 200, 0 ) );
            }
          break;
          case Trains:
            lblDigits.setText( String.valueOf( info.getAllTrains().size() ) );
            lblDigits.setForeground( new Color( lblDigits.getDisplay(), 0, 200, 200 ) );
            lblTitle.setForeground( new Color( lblDigits.getDisplay(), 0, 200, 200 ) );
          break;
          case Stations:
            lblDigits.setText( String.valueOf( info.getAllStations().size() ) );
            lblDigits.setForeground( new Color( lblDigits.getDisplay(), 0, 200, 200 ) );
            lblTitle.setForeground( new Color( lblDigits.getDisplay(), 0, 200, 200 ) );
          break;
          default:
            lblDigits.setText( "---" );
            lblDigits.setForeground( new Color( lblDigits.getDisplay(), 0, 200, 200 ) );
            lblTitle.setForeground( new Color( lblDigits.getDisplay(), 0, 200, 200 ) );
          break;
        }
        lblDigits.getParent().layout();
      }
    } );
    return result;
  }

  private void createChart( Composite parent ) {
    Chart chart = new Chart( parent, SWT.NONE );
    Bar b1 = new Bar( 0, 100, 40, "40%", "Versp.", new Color( parent.getDisplay(), 20, 20, 80 ) );
    Bar b2 = new Bar( 0, 100, 100, "100%", "fast Pünktlich", new Color( parent.getDisplay(),
                                                                        200,
                                                                        120,
                                                                        120 ) );
    Bar b3 = new Bar( 0, 80, 66, "6", "Züge", new Color( parent.getDisplay(), 120, 120, 200 ) );
    Bar b4 = new Bar( 0, 100, 37, "37%", "Anschl. erreicht", new Color( parent.getDisplay(),
                                                                        200,
                                                                        200,
                                                                        200 ) );
    chart.addBar( b1 );
    chart.addBar( b2 );
    chart.addBar( b3 );
    chart.addBar( b4 );
    chart.layout();
  }

  private void closeDataShell() {
    if( dataShell != null ) {
      if( dataShell.isDisposed() ) {
        dataShell = null;
      } else {
        dataShell.close();
      }
    }
  }

  private void rollToInfo(int delta) {
    Calendar calToday = Calendar.getInstance();
    calToday.setTime( new Date() );
    
    Calendar cal = Calendar.getInstance();
    cal.setTime( currentDate );
    cal.roll( Calendar.DATE, delta );
    if( cal.after( calToday ) ) {
      cal.add( Calendar.DATE, HISTORY );
    }
    this.currentDate = cal.getTime();
    DashboardAggregator aggregator = Activator.getAggregator();
    RailwayInfo info = aggregator.getInfoForDate( currentDate );
    fireInfoEvent( info );
  }
}
