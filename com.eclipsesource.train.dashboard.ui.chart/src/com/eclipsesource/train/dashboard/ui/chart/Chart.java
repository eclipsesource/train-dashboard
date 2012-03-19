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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class Chart extends Composite {

	private List<Bar> bars = new ArrayList<Bar>();
	private List<Composite> barComposites = new ArrayList<Composite>();
	private List<Label> descriptions = new ArrayList<Label>();
	private List<Label> values = new ArrayList<Label>();
	
	private final Composite barComposite;
	private final Composite descriptionComposite;
	
	public Chart(Composite parent, int style) {
		super(parent, style);
		this.setLayout( new GridLayout() );
		barComposite = new Composite( this, SWT.NONE );
		barComposite.setLayout( new FormLayout() );
		barComposite.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
		descriptionComposite = new Composite( this, SWT.NONE );
		descriptionComposite.setLayout( new FormLayout() );
		descriptionComposite.setLayoutData( new GridData( SWT.FILL, SWT.BOTTOM, true, false ) );
	}
	
	public void addBar( Bar bar ) {
		bars.add(bar);
		Composite comp = new Composite( barComposite, SWT.NONE );
		comp.setBackground( bar.getColor() );
		comp.setData(WidgetUtil.CUSTOM_VARIANT, "ANIMATED");
		barComposites.add(comp);
		Label value = new Label( barComposite, SWT.CENTER );
		value.setData(WidgetUtil.CUSTOM_VARIANT, "ANIMATED");
		value.setForeground( this.getDisplay().getSystemColor( SWT.COLOR_WHITE ) );
		values.add(value);
		Label description = new Label( descriptionComposite, SWT.CENTER );
		description.setForeground( this.getDisplay().getSystemColor( SWT.COLOR_WHITE ) );
		description.setText(bar.getDescription());
		descriptions.add( description );
	}
	
	public Bar getBar( int index ) {
		return bars.get(index);
	}
	
	public int getBarCount() {
		return bars.size();
	}

	@Override
	public void layout() {
		int ticks = bars.size() * 3 + bars.size() - 1;
		double tickWidth = ( 100.0 / new Double( ticks ).doubleValue() );
		for (int i = 0; i < bars.size(); i++) {
			Bar bar = bars.get(i);
			int top = 0;
			double scale = bar.getMaximum() - bar.getMinimum();
			if( scale > 0.0 ) {
				top = new Double( bar.getValue() / scale * 90 ).intValue();
			}
			int left = new Double( i * 3 * tickWidth + i * tickWidth ).intValue();
			int right = new Double( left + ( 3* tickWidth ) ).intValue();
			FormData formdata = new FormData();
			formdata.bottom = new FormAttachment( 100 );
			formdata.top = new FormAttachment( 100 - top );
			formdata.left = new FormAttachment( left);
			formdata.right = new FormAttachment( right );
			barComposites.get( i ).setLayoutData(formdata);
			FormData lblFormdata = new FormData();
			lblFormdata.left = new FormAttachment( left );
			lblFormdata.right = new FormAttachment( right );
			lblFormdata.bottom = new FormAttachment( barComposites.get( i ), -5, SWT.TOP );
			Label value = values.get( i );
			value.setLayoutData( lblFormdata );
			value.setText( bar.getText() );
			FormData descFormdata = new FormData();
			descFormdata.left = new FormAttachment( left );
			descFormdata.right = new FormAttachment( right );
			descFormdata.bottom = new FormAttachment( 100 );
			descriptions.get( i ).setLayoutData( descFormdata );
		}
		barComposite.layout();
		super.layout();		
	}
	
}
