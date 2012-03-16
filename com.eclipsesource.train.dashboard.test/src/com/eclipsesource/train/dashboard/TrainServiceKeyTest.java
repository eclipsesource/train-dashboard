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

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.eclipsesource.train.dashboard.internal.TrainServiceKey;


public class TrainServiceKeyTest {
  
  
  @Test( expected = IllegalArgumentException.class )
  public void testFutureDates() {
    Date date = new Date();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime( date );
    calendar.add( Calendar.DAY_OF_MONTH, 1 );
    Date futureDate = calendar.getTime();
    
    new TrainServiceKey( futureDate );
  }
  
  
  @Test
  public void testToString() {
    TrainServiceKey key1 = new TrainServiceKey( new Date() );
    TrainServiceKey key2 = new TrainServiceKey( new Date() );
    
    assertEquals( key1.toString(), key2.toString() );
  }
}
