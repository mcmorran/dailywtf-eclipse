/*******************************************************************************
 * Copyright (c) 2010 Alan McMorran & Open Grid Systems Ltd..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Alan McMorran & Open Grid Systems Ltd. - initial API and implementation
 ******************************************************************************/
package com.opengridsystems.wtf.submission.ui.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class SubmissionPage extends WizardPage implements Listener{

	protected Composite composite;
	protected Text name, email, subject, message, code;
	protected Button publish;
	
	protected String codeSnippet;
	
	
	protected SubmissionPage(String pageName, String codeSnippet) {
		super(pageName);
		setTitle(pageName);
		this.codeSnippet = codeSnippet;
	}

	@Override
	public void createControl(Composite parent) {
		composite = new Composite(parent, SWT.NONE);
		// create the desired layout for this wizard page
		GridLayout gl = new GridLayout(1, false);
		composite.setLayout(gl);
		composite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true));
		
		Composite iComp = new Composite(composite, SWT.NONE|SWT.NO_REDRAW_RESIZE);
		GridData iGD = new GridData(GridData.FILL_HORIZONTAL);
		iGD.verticalAlignment =SWT.TOP;
		iComp.setLayoutData(iGD);
		
		GridLayout iGl = new GridLayout(2, false);
		iComp.setLayout(iGl);
		
		Label nameLabel = new Label(iComp, SWT.NONE);
		nameLabel.setText("Your Name:");
		name = new Text(iComp, SWT.BORDER|SWT.SINGLE);
		name.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		name.addListener(SWT.KeyUp, this);
		
		Label emailLabel = new Label(iComp, SWT.NONE);
		emailLabel.setText("Your Email:");
		email = new Text(iComp, SWT.BORDER|SWT.SINGLE);
		email.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		email.addListener(SWT.KeyUp, this);

		Label subjectLabel = new Label(iComp, SWT.NONE);
		subjectLabel.setText("Subject:");
		subject = new Text(iComp, SWT.BORDER|SWT.SINGLE);
		subject.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		subject.addListener(SWT.KeyUp, this);
		
		Label messageLabel = new Label(composite, SWT.NONE);
		messageLabel.setText("Message:");
		
		GridData multiLineGD = new GridData(GridData.FILL_BOTH);
		//multiLineGD.heightHint = 180;
		multiLineGD.grabExcessVerticalSpace = true;
		
		message = new Text(composite, SWT.BORDER|SWT.MULTI|SWT.V_SCROLL|SWT.WRAP);
		message.setLayoutData(multiLineGD);
		message.addListener(SWT.KeyUp, this);
		
		Label codeLabel = new Label(composite, SWT.NONE);
		codeLabel.setText("Code Snippet:");
		code = new Text(composite, SWT.BORDER|SWT.MULTI|SWT.V_SCROLL|SWT.H_SCROLL);
		code.setLayoutData(multiLineGD);
		code.setText(codeSnippet);
		code.addListener(SWT.KeyUp, this);
		
		Label publishHeader = new Label(composite, SWT.NONE);
		Font f = publishHeader.getFont();
		FontData data = f.getFontData()[0];
		data.setStyle(data.getStyle() | SWT.BOLD);
		Font bold = new Font(publishHeader.getDisplay(), data);
		publishHeader.setText("Please Don't Publish");
		publishHeader.setFont(bold);		
		
		Composite jComp = new Composite(composite, SWT.NONE);		
		GridLayout jGl = new GridLayout(2, false);
		jComp.setLayout(jGl);

		publish = new Button(jComp, SWT.CHECK);
		
		Label publishLabel = new Label(jComp, SWT.NONE);
		publishLabel.setText("Even though I'm sure you'd do a fine job of anonymising, I just wanted\n" +
				"to be able say that I sent code to The Daily WTF. Yes I realise this\n" +
				"hardly counts, but it's better than wallowing in my own misery.");
		
		setControl(composite);
	}
	
	@Override
	public boolean isPageComplete() {
		return email!=null && email.getText()!=null && !email.getText().trim().equals("") &&
			name!=null && name.getText()!=null && !name.getText().trim().equals("") &&
			message!=null && message.getText()!=null && !message.getText().trim().equals("") &&
			subject!=null && subject.getText()!=null && !subject.getText().trim().equals("") &&
			code!=null && code.getText()!=null && !code.getText().trim().equals("");
	}

	@Override
	public void handleEvent(Event event) {
		setPageComplete(isPageComplete());
	}

}
