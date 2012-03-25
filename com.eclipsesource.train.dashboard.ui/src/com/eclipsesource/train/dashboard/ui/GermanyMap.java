/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.train.dashboard.ui;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.eclipsesource.train.dashboard.CoordinatesUtil;
import com.eclipsesource.train.dashboard.CoordinatesUtil.Coordinates;
import com.eclipsesource.train.dashboard.DelayInfo;
import com.eclipsesource.train.dashboard.Point;
import com.eclipsesource.train.dashboard.RailwayInfo;
import com.eclipsesource.train.dashboard.model.Station;

public class GermanyMap extends Composite {

  private MarkerPool markerPool = new MarkerPool();
  private RailwayInfo currentInfo;
  private static final Image markerImage;
  private static final ConcurrentHashMap<ScaledImageKey, Image> scaledImages;
  private static final ImageData gemranyMapData;
  
  static {
    markerImage = Graphics.getImage( "/images/marker_white.png", GermanyMap.class.getClassLoader() ); 
    gemranyMapData = new ImageData( GermanyMap.class.getResourceAsStream( "/images/germany.png" ) );
    scaledImages = new ConcurrentHashMap<ScaledImageKey, Image>();
  }
  
  public GermanyMap( Composite parent, int style ) {
    super( parent, style );
    createBackground();
  }

  private void createBackground() {
    setBGImage();
    this.addListener( SWT.Resize, new Listener() {
      public void handleEvent( Event e ) {
        setBGImage();
      }
    } );
  }

  private void setBGImage() {
    int newWidth = this.getSize().x;
    int newHeight = this.getSize().y;
    if( newWidth > 0 && newHeight > 0 ) {
      ScaledImageKey key = new ScaledImageKey( newWidth, newHeight );
      Image image = scaledImages.get( key );
      if( image == null ) {
        ImageData scaledData = gemranyMapData.scaledTo( newWidth, newHeight );
        image = new Image( this.getDisplay(), scaledData );
        Image putResult = scaledImages.putIfAbsent( key, image );
        if( putResult != null ) {
          image = putResult;
        }
      }
      this.setBackgroundImage( image );
      setInfo( currentInfo );
    }
  }

  public void setInfo( RailwayInfo info ) {
    this.currentInfo = info;
    List<Station> stationsSortedByDelayMinutes = info.getDelayInfo().getStationsSortedByDelayMinutes( 15 );
    for( Station station : stationsSortedByDelayMinutes ) {
      addMarker( station.getLat(), station.getLon(), station );
    }
    markerPool.clear();
  }

  public void addMarker( double longitude, double latitude, final Station station ) {
    Point location = CoordinatesUtil.transformToPoint( new Coordinates( longitude, latitude ), this.getSize().x, this.getSize().y, new Coordinates( 54.8, 6.0 ), new Coordinates( 47.3, 14.7 ) );
    Label marker = markerPool.getMarker();
    if( marker == null ) {
      marker = new Label( this, SWT.NONE);
      marker.setData( WidgetUtil.CUSTOM_VARIANT, "ANIMATED" );
      markerPool.addMarker( marker );
    }
    marker.setImage( markerImage );
    marker.setLocation( Math.max( 0, location.x -8), Math.max( 0, location.y -30 ) );
    marker.setSize( 32, 32 );
    marker.addListener( SWT.MouseDown, new Listener() {
      public void handleEvent( Event event ) {
        StringBuilder message = new StringBuilder();
        DelayInfo delayInfo = currentInfo.getDelayInfo();
        int stationId = station.getId();
        int delayAmountForStation = delayInfo.getDelayAmountForStation( stationId );
        int delayMinutesForStation = delayInfo.getDelayMinutesForStation( stationId );
        double delayPercentageForStation = delayInfo.getDelayPercentageForStation( stationId );
        message.append( "Delayed Trains: " );
        message.append( String.valueOf( Math.round( delayAmountForStation) ) );
        message.append( " (" );
        message.append( String.valueOf( Math.round( delayPercentageForStation) ) );
        message.append( "%)\n" );
        message.append( "Total Minutes Delayed: " );
        message.append( String.valueOf( Math.round( delayMinutesForStation) ) );
        Shell dialog = new Shell( getDisplay(), SWT.CLOSE | SWT.BORDER | SWT.TITLE | SWT.APPLICATION_MODAL );
        int width = 280;
        int height = 130;
        org.eclipse.swt.graphics.Point touchPoint = getDisplay().map( (Control)event.widget, null, event.x, event.y );
        int x = Math.max( 1, Math.min( touchPoint.x, getDisplay().getBounds().width -width -1) );
        int y = Math.max( 1, touchPoint.y -128);
        dialog.setBounds( x, y, width, height );
        dialog.setBackground( getDisplay().getSystemColor( SWT.COLOR_BLACK ) );
        dialog.setText( station.getName() );
        dialog.setLayout( new FillLayout() );
        Label messageLabel = new Label(dialog, SWT.WRAP );
        messageLabel.setText( message.toString() );
        messageLabel.setForeground( getDisplay().getSystemColor( SWT.COLOR_WHITE ) );
        dialog.open();
      }
    } );
  }
  
  @Override
  public void dispose() {
    markerPool.dispose();
    super.dispose();
  }
}
