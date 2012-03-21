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

public class CoordinatesUtil {
  
  public static class Coordinates {
    
    public final double lat;
    public final double lon;

    public Coordinates( double lat, double lon ) {
      this.lat = lat;
      this.lon = lon;
    }
    
  }
  
  public static Point transformToPoint( Coordinates toMap, 
                                        int imgWidth, 
                                        int imgHeight, 
                                        Coordinates topLeft, 
                                        Coordinates bottomRight ) 
  {
    double distanceX = Math.abs( bottomRight.lat - topLeft.lat );
    double distanceY = Math.abs( topLeft.lon - bottomRight.lon );
    double latPercentage = Math.abs( topLeft.lat - toMap.lat ) / ( distanceX / 100 );
    double lonPercentage = Math.abs( toMap.lon - topLeft.lon ) / ( distanceY / 100 );
    int x = ( int )( ( imgWidth / 100.0 ) * lonPercentage );
    int y = ( int )( ( imgHeight / 100.0 ) * latPercentage );
    return new Point( x, y );
  }
  
  private CoordinatesUtil() {
    // prevent instantiation
  }
  
}
