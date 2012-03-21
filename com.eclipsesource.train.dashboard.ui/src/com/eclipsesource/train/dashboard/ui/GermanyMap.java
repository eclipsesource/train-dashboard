/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.train.dashboard.ui;

import java.util.List;

import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.eclipsesource.train.dashboard.CoordinatesUtil;
import com.eclipsesource.train.dashboard.CoordinatesUtil.Coordinates;
import com.eclipsesource.train.dashboard.Point;
import com.eclipsesource.train.dashboard.RailwayInfo;
import com.eclipsesource.train.dashboard.model.Station;

public class GermanyMap extends Composite {

  private int width;
  private int height;
  private MarkerPool markerPool = new MarkerPool();
  private RailwayInfo currentInfo;

  public GermanyMap( Composite parent, int style ) {
    super( parent, style );
    createBackground();
  }

  private void createBackground() {
    final Shell shell = this.getShell();
    setBGImage();
    shell.addListener( SWT.Resize, new Listener() {

      public void handleEvent( Event e ) {
        setBGImage();
      }
    } );
  }

  private void setBGImage() {
    int newWidth = this.getSize().x;
    int newHeight = this.getSize().y;
    if( newWidth > 0 && newHeight > 0 ) {
      width = newWidth;
      height = newHeight;
      // && ( width != newWidth || height != newHeight )
      ImageData imageData = new ImageData( GermanyMap.class.getResourceAsStream( "/images/germany.png" ) );
      imageData = imageData.scaledTo( width, height );
      Image image = new Image( this.getDisplay(), imageData );
      this.setBackgroundImage( image );
      setInfo( currentInfo );
    }
  }

  public void setInfo( RailwayInfo info ) {
    this.currentInfo = info;
    List<Station> stationsSortedByDelayMinutes = info.getDelayInfo().getStationsSortedByDelayMinutes( 10 );
    for( Station station : stationsSortedByDelayMinutes ) {
      addMarker( station.getLat(), station.getLon(), station.getName() );
    }
    markerPool.clear();
  }

  public void addMarker( double longitude, double latitude, String text ) {
    Point location = CoordinatesUtil.transformToPoint( new Coordinates( longitude, latitude ), width, height, new Coordinates( 54.8, 6.0 ), new Coordinates( 47.3, 14.7 ) );
    Label l = markerPool.getMarker();
    if( l == null ) {
      l = new Label( this, SWT.NONE);
      l.setData( WidgetUtil.CUSTOM_VARIANT, "ANIMATED" );
      markerPool.addMarker( l );
    }
    l.setText( text );
    l.setLocation( location.x, location.y );
    l.setSize( l.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
  }
  
  @Override
  public void dispose() {
    markerPool.dispose();
    super.dispose();
  }
}
