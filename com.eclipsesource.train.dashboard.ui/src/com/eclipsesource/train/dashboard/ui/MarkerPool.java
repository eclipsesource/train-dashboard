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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;


public class MarkerPool {

  Set<Label> pool = new HashSet<Label>();
  Set<Label> used = new HashSet<Label>();
  
  public void addMarker( Label l ) {
    used.add( l );
  }

  public void dispose() {
    for( Label label : new ArrayList<Label>(pool) ) {
      if( !label.isDisposed() ) {
        label.dispose();
      }
      pool.remove( label );
    }
    for( Label label : new ArrayList<Label>(used) ) {
      if( !label.isDisposed() ) {
        label.dispose();
      }
      used.remove( label );
    }
  }

  public Label getMarker() {
    Iterator<Label> iterator = pool.iterator();
    if( iterator.hasNext() ) {
      Label result = iterator.next();
      used.add( result );
      pool.remove( result );
      Listener[] listeners = result.getListeners( SWT.MouseDown );
      for( Listener listener : listeners ) {
        result.removeListener( SWT.MouseDown, listener );
      }
      return result;
    }
    return null;
  }

  public void clear() {
    for( Label label : new ArrayList<Label>(pool) ) {
      if( !label.isDisposed() ) {
        label.dispose();
      }
      pool.remove( label );
    }
    pool.addAll( used );
    used.clear();
  }
}
