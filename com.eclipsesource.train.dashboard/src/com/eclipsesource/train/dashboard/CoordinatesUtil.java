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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoordinatesUtil {
  
  public static final Coordinates DEFAULT_COODRINATES = new Coordinates( 0, 0 );
  
  private static Comparator<Coordinates> coordinatesComparator = new Comparator<Coordinates>() {
  
    public int compare( Coordinates c1, Coordinates c2 ) {
      int result = 0;
      if( !c1.equals( c2 ) ) {
        double latDifference = Math.abs( c1.lat - c2.lat );
        double lonDifference = Math.abs( c1.lon - c2.lon );
        if( latDifference > lonDifference ) {
          if( c1.lat > c2.lat ) {
            result = 1;
          } else {
            result = -1;
          }
        } else {
          if( c1.lon > c2.lon ) {
            result = 1;
          } else {
            result = -1;
          }
        }
      }
      return result;
    }
  };

  public static class Coordinates {
    
    public final double lat;
    public final double lon;

    public Coordinates( double lat, double lon ) {
      this.lat = lat;
      this.lon = lon;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      long temp;
      temp = Double.doubleToLongBits( lat );
      result = prime * result + ( int )( temp ^ ( temp >>> 32 ) );
      temp = Double.doubleToLongBits( lon );
      result = prime * result + ( int )( temp ^ ( temp >>> 32 ) );
      return result;
    }

    @Override
    public boolean equals( Object obj ) {
      if( this == obj )
        return true;
      if( obj == null )
        return false;
      if( getClass() != obj.getClass() )
        return false;
      Coordinates other = ( Coordinates )obj;
      if( Double.doubleToLongBits( lat ) != Double.doubleToLongBits( other.lat ) )
        return false;
      if( Double.doubleToLongBits( lon ) != Double.doubleToLongBits( other.lon ) )
        return false;
      return true;
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
  
  public static Map<Coordinates, Coordinates> mapToNearestCoordinates( List<Coordinates> from, List<Coordinates> to ) {
    List<Coordinates> fromCopy = new ArrayList<Coordinates>( from );
    List<Coordinates> toCopy = new ArrayList<Coordinates>( to );
    Collections.sort( fromCopy, coordinatesComparator );
    Collections.sort( toCopy, coordinatesComparator );
    Map<Coordinates, Coordinates> result = new HashMap<Coordinates, Coordinates>();
    for( int i = 0; i < toCopy.size(); i++ ) {
      Coordinates toMap = toCopy.get( i );
      if( i < fromCopy.size() ) {
        result.put( toMap, fromCopy.get( i ) );
      } else {
        result.put( toMap, DEFAULT_COODRINATES );
      }
    }
    return result;
  }

  private CoordinatesUtil() {
    // prevent instantiation
  }
  
}
