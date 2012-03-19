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
package com.eclipsesource.train.dashboard.ui.chart;

import org.eclipse.swt.graphics.Color;

public class Bar {

  private double minimum = 0.0;
  private double maximum = 0.0;
  private double value = 0.0;
  private String text = "";
  private String description = "";
  private Color color;

  public Bar( double minimum,
              double maximum,
              double value,
              String text,
              String description,
              Color color )
  {
    setMinimum( minimum );
    setMaximum( maximum );
    setValue( value );
    setText( text );
    setDescription( description );
    setColor( color );
  }

  public double getMinimum() {
    return minimum;
  }

  public void setMinimum( double minimum ) {
    this.minimum = minimum;
  }

  public double getMaximum() {
    return maximum;
  }

  public void setMaximum( double maximum ) {
    this.maximum = maximum;
  }

  public double getValue() {
    return value;
  }

  public void setValue( double value ) {
    if( value < minimum ) {
      this.value = minimum;
    } else if( value > maximum ) {
      this.value = maximum;
    } else {
      this.value = value;
    }
  }

  public String getText() {
    return text;
  }

  public void setText( String text ) {
    this.text = text;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription( String description ) {
    this.description = description;
  }

  public Color getColor() {
    return color;
  }

  public void setColor( Color color ) {
    this.color = color;
  }
}
