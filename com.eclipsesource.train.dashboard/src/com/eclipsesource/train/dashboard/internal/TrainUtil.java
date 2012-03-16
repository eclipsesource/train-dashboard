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
package com.eclipsesource.train.dashboard.internal;

import java.util.Date;
import java.util.List;

import com.eclipsesource.train.dashboard.model.Train;


public class TrainUtil {
  
  public static List<Train> getTrainsForDate( Date date ) {
    return FetchFactory.getTrains( date );
  }
}
