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

import java.awt.Point;


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
    double distanceX = bottomRight.lat - topLeft.lat;
    double distanceY = topLeft.lon - topLeft.lon;
    double latPercentage = toMap.lat / ( distanceX / 100 );
    double lonPercentage = toMap.lon / ( distanceY / 100 );
    int x = ( int )( ( imgWidth / 100 ) * latPercentage );
    int y = ( int )( ( imgHeight / 100 ) * lonPercentage );
    return new Point( x, y );
  }
  
  private CoordinatesUtil() {
    // prevent instantiation
  }
  
}
