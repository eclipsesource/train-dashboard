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

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;


public class DateDeserializer implements JsonDeserializer<Date> {
  
  private SimpleDateFormat format;

  public DateDeserializer() {
    format = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" );
  }

  public Date deserialize( JsonElement json, Type typeOfT, JsonDeserializationContext context )
    throws JsonParseException
  {
    String dateString = json.getAsJsonPrimitive().getAsString();
    try {
      return format.parse( dateString );
    } catch( ParseException e ) {
      throw new IllegalArgumentException( "Could not parse date " + dateString );
    }
  }
}
