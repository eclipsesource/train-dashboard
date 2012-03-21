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
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
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
import com.eclipsesource.train.dashboard.DelayInfo;
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
  private RailwayInfo currentInfo = null;
  private static int HISTORY = -30;
  
  public int createUI() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NO_TRIM );
    shell.setBackground( shell.getDisplay().getSystemColor( SWT.COLOR_BLACK ) );
    GridLayoutFactory.fillDefaults().spacing( 2, 2 ).numColumns( 2 ).applyTo( shell );
    createToolbar( shell );
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

  protected void removeInfoListener( IInfoListener listener ) {
    infoListeners.remove( listener );
  }

  private void fireInfoEvent( RailwayInfo info ) {
    this.currentInfo = info;
    for( IInfoListener listener : this.infoListeners ) {
      listener.newInfo( info );
    }
  }

  private void createDataArea( Shell shell ) {
    Composite result = new Composite( shell, SWT.NONE );
    GridLayoutFactory.fillDefaults().numColumns( 1 ).applyTo( result );
    GridDataFactory.fillDefaults().align( SWT.FILL, SWT.FILL ).grab( true, true ).applyTo( result );
    result.setBackground( result.getDisplay().getSystemColor( SWT.COLOR_BLACK ) );
  }

  private void createToolbar( Composite result ) {
    ToolBar toolBar = new ToolBar( result, SWT.NONE );
    GridDataFactory.fillDefaults().grab( true, false ).span( 2, 1 ).align( SWT.FILL, SWT.TOP ).applyTo( toolBar );
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
        createChart( dataShell, currentInfo );
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
        if( lblDigits.getText() == null || lblDigits.getText().equals( "0" ) ) {
          lblDigits.setText( "---" );
        }
        lblDigits.getParent().layout();
      }
    } );
    return result;
  }

  private void createChart( Composite parent, RailwayInfo info ) {
    DelayInfo delayInfo = info.getDelayInfo();
    final Chart chart = new Chart( parent, SWT.NONE );
    int delayedTrainsAmount5 = delayInfo.getDelayedTrainsAmount( 5 );
    int delayedTrainsAmount10 = delayInfo.getDelayedTrainsAmount( 10 );
    int delayedTrainsAmount15 = delayInfo.getDelayedTrainsAmount( 15 );
    int maxAmount = Math.max( delayedTrainsAmount5, Math.max( delayedTrainsAmount10, delayedTrainsAmount15 ) );
    maxAmount = Math.max( maxAmount, 400 );
    double delayedTrainsPercentage5 = delayInfo.getDelayedTrainsPercentage( 5 );
    double delayedTrainsPercentage10 = delayInfo.getDelayedTrainsPercentage( 10 );
    double delayedTrainsPercentage15 = delayInfo.getDelayedTrainsPercentage( 15 );

    Bar bar5m = new Bar( 0, maxAmount, delayedTrainsAmount5, String.valueOf( delayedTrainsAmount5 ), ">5min", new Color( parent.getDisplay(), 0, 200, 0 ) );
    Bar bar5p = new Bar( 0, 100, delayedTrainsPercentage5, String.valueOf( Math.round( delayedTrainsPercentage5 ) ) + "%", ">5min", new Color( parent.getDisplay(), 0, 200, 0 ) );
    Bar bar10m = new Bar( 0, maxAmount, delayedTrainsAmount10, String.valueOf( delayedTrainsAmount10 ), ">10min", new Color( parent.getDisplay(), 200, 200, 0 ) );
    Bar bar10p = new Bar( 0, 100, delayedTrainsPercentage10, String.valueOf( Math.round( delayedTrainsPercentage10 ) ) + "%", ">10min", new Color( parent.getDisplay(), 200, 200, 0 ) );
    Bar bar15m = new Bar( 0, maxAmount, delayedTrainsAmount15, String.valueOf( delayedTrainsAmount15 ), ">15min", new Color( parent.getDisplay(), 200, 0, 0 ) );
    Bar bar15p = new Bar( 0, 100, delayedTrainsPercentage15, String.valueOf( Math.round( delayedTrainsPercentage15 ) ) + "%", ">15min", new Color( parent.getDisplay(), 200, 0, 0 ) );

    chart.addBar( bar5m );
    chart.addBar( bar5p );
    chart.addBar( bar10m );
    chart.addBar( bar10p );
    chart.addBar( bar15m );
    chart.addBar( bar15p );

    chart.layout();
    
    final IInfoListener infoListener = new IInfoListener() {
      public void newInfo( RailwayInfo info ) {
        DelayInfo delayInfo = info.getDelayInfo();
        int delayedTrainsAmount5 = delayInfo.getDelayedTrainsAmount( 5 );
        int delayedTrainsAmount10 = delayInfo.getDelayedTrainsAmount( 10 );
        int delayedTrainsAmount15 = delayInfo.getDelayedTrainsAmount( 15 );
        int maxAmount = Math.max( delayedTrainsAmount5, Math.max( delayedTrainsAmount10, delayedTrainsAmount15 ) );
        maxAmount = Math.max( maxAmount, 400 );
        double delayedTrainsPercentage5 = delayInfo.getDelayedTrainsPercentage( 5 );
        double delayedTrainsPercentage10 = delayInfo.getDelayedTrainsPercentage( 10 );
        double delayedTrainsPercentage15 = delayInfo.getDelayedTrainsPercentage( 15 );
        
        chart.getBar( 0 ).setMaximum( maxAmount );
        chart.getBar( 0 ).setValue( delayedTrainsAmount5 );
        chart.getBar( 0 ).setText( String.valueOf( delayedTrainsAmount5 ) );
        chart.getBar( 1 ).setValue( delayedTrainsPercentage5 );
        chart.getBar( 1 ).setText( String.valueOf( Math.round( delayedTrainsPercentage5 ) ) + "%" );
        
        chart.getBar( 2 ).setMaximum( maxAmount );
        chart.getBar( 2 ).setValue( delayedTrainsAmount10 );
        chart.getBar( 2 ).setText( String.valueOf( delayedTrainsAmount10 ) );
        chart.getBar( 3 ).setValue( delayedTrainsPercentage10 );
        chart.getBar( 3 ).setText( String.valueOf( Math.round( delayedTrainsPercentage10 ) ) + "%" );
        
        chart.getBar( 4 ).setMaximum( maxAmount );
        chart.getBar( 4 ).setValue( delayedTrainsAmount15 );
        chart.getBar( 4 ).setText( String.valueOf( delayedTrainsAmount15 ) );
        chart.getBar( 5 ).setValue( delayedTrainsPercentage15 );
        chart.getBar( 5 ).setText( String.valueOf( Math.round( delayedTrainsPercentage15 ) ) + "%" );
        
        chart.layout();
      }
    };

    addInfoListener( infoListener );
    parent.addDisposeListener( new DisposeListener() {
      public void widgetDisposed( DisposeEvent event ) {
        removeInfoListener( infoListener );
      }
    } );
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
