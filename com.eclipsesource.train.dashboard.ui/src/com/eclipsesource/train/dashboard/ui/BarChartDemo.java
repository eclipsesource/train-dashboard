package com.eclipsesource.train.dashboard.ui;

import java.util.Random;

import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.eclipsesource.train.dashboard.ui.chart.Bar;
import com.eclipsesource.train.dashboard.ui.chart.Chart;

public class BarChartDemo implements IEntryPoint {

  private Chart chart;

  public int createUI() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NO_TRIM );
    shell.setMaximized( true );
    shell.setLayout( new GridLayout() );
    shell.setBackground( display.getSystemColor( SWT.COLOR_BLACK ) );
    createContent( display, shell );
    shell.open();
    shell.setVisible( true );
    return 0;
  }

  private void createContent( Display display, Shell shell ) {
    createToolbar( shell );
    Composite parent = new Composite( shell, SWT.NONE );
    parent.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    parent.setBackground( display.getSystemColor( SWT.COLOR_BLACK ) );
    parent.setLayout( new FillLayout() );
    createChart( parent );
  }

  private void createChart( Composite parent ) {
    chart = new Chart( parent, SWT.NONE );
    Bar b1 = new Bar( 0, 100, 40, "40%", "Versp.", new Color( parent.getDisplay(), 20, 20, 80 ) );
    Bar b2 = new Bar( 0, 100, 100, "100%", "fast Pünktlich", new Color( parent.getDisplay(),
                                                                        200,
                                                                        120,
                                                                        120 ) );
    Bar b3 = new Bar( 0, 80, 66, "6", "Züge", new Color( parent.getDisplay(), 120, 120, 200 ) );
    Bar b4 = new Bar( 0, 100, 37, "37%", "Anschl. erreicht", new Color( parent.getDisplay(),
                                                                        200,
                                                                        200,
                                                                        200 ) );
    chart.addBar( b1 );
    chart.addBar( b2 );
    chart.addBar( b3 );
    chart.addBar( b4 );
    chart.layout();
  }

  private void createToolbar( Composite parent ) {
    ToolBar toolbar = new ToolBar( parent, SWT.NONE );
    toolbar.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );
    ToolItem item = new ToolItem( toolbar, SWT.NONE );
    item.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        Random rand = new Random( System.currentTimeMillis() );
        for( int i = 0; i < chart.getBarCount(); i++ ) {
          Bar bar = chart.getBar( i );
          double value = bar.getValue();
          bar.setValue( value * rand.nextDouble() + 20 );
          bar.setText( new Integer( new Double( bar.getValue() ).intValue() ).toString() + "%" );
        }
        chart.layout();
      }
    } );
    item.setText( "Change" );
  }
}
