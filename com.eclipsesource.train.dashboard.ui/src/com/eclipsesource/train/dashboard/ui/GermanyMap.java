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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;


public class GermanyMap extends Composite {

  private int width;
  private int height;
  
  public GermanyMap( Composite parent, int style ) {
    super( parent, style );
    createBackground();
  }

  private void createBackground() {
    final Shell shell = this.getShell();
    setBGImage();
    shell.addListener (SWT.Resize,  new Listener () {
      public void handleEvent (Event e) {
        setBGImage();
      }
    });
  }
  
  
  private void setBGImage() {
    int newWidth = this.getShell().getSize().x;
    int newHeight = this.getShell().getSize().y;
    if( newWidth > 0 && newHeight > 0  ) {
      width = newWidth;
      height = newHeight;
      // && ( width != newWidth || height != newHeight )
      ImageData imageData = new ImageData( GermanyMap.class
                                           .getResourceAsStream("/images/germany.png") );
      imageData = imageData.scaledTo( width, height );
      Image image =  new Image( this.getDisplay(), imageData );
      this.setBackgroundImage( image );
    }
  }
}
