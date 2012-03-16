package com.eclipsesource.train.dashboard.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;


public class StreamReaderUtil {
  
  public static String readStream( InputStream inputStream ) {
    Writer writer = new StringWriter();
    copyStream( inputStream, writer );
    return writer.toString();
  }
  
  private static void copyStream( InputStream inputStream, Writer writer ) {
    try {
      copyStreamInternal( inputStream, writer );
    } catch( IOException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }

  private static void copyStreamInternal( InputStream inputStream, Writer writer ) throws IOException {
    char[] buffer = new char[ 1024 ];
    try {
      Reader reader = new BufferedReader( new InputStreamReader( inputStream, "UTF-8" ) );
      int n;
      while( ( n = reader.read( buffer ) ) != -1 ) {
        writer.write( buffer, 0, n );
      }
    } finally {
      inputStream.close();
      writer.close();
    }
  }
  
  private StreamReaderUtil() {
    // prevent instantiation
  }
}