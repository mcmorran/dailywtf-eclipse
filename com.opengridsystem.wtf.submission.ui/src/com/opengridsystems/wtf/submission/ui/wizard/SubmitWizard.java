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

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.springframework.jmx.access.InvocationFailureException;

import com.opengridsystems.wtf.submission.ui.Activator;
import com.thedailywtf.SubmitWTF;
import com.thedailywtf.SubmitWTFSoap;

public class SubmitWizard extends Wizard implements INewWizard {

	private URL WSDL_URL; 
	private SubmissionPage page;
	protected IStructuredSelection selection;	

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.setWindowTitle("Submit to The Daily WTF");
		this.selection = selection;
		try {
			WSDL_URL = new URL("http://thedailywtf.com/SubmitWTF.asmx?wsdl");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addPages() {
		Object  o = this.selection.getFirstElement();
		if (o!=null && o instanceof TextSelection){
			o = ((TextSelection)o).getText();
		}
		System.out.println(o.getClass().getName());
		this.page = new SubmissionPage("Submit your WTF", o.toString());
		this.page.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/wtf64padded.png"));
		this.page.setDescription("The Daily WTF code submission plugin for Eclipse provided by Open Grid Systems Ltd.");
		addPage(page);
	}


	@Override
	public boolean canFinish() {
		return page.isPageComplete();
	}

	@Override
	public boolean performFinish() {

		final String name = page.name.getText().trim();
		final String email = page.email.getText().trim();
		final String subject = page.subject.getText().trim();
		final String message = page.message.getText();
		final String code = page.code.getText();
		final boolean doNotPublish = page.publish.getSelection();

		ProgressMonitorDialog pmD = new ProgressMonitorDialog(this.getShell());
		IRunnableWithProgress loader = new IRunnableWithProgress() {

			public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
				SubmitWTF ws = new SubmitWTF(WSDL_URL);

				SubmitWTFSoap wsSoap = ws.getSubmitWTFSoap();
				try{
					String response = wsSoap.submit(name, email, subject, message, code, doNotPublish);
					if (response  == null || !response.equals(""))
						throw new InvocationTargetException(new Exception("Your WTF has not been submitted\n\nResponse: "+response));
				}catch (Exception ex){
					throw new InvocationTargetException(ex);
				}
			}

		};
		try{
			pmD.run(true, false, loader);
		}catch (InvocationTargetException ex){
			ErrorDialog.openError(getShell(), "Error", ex.getMessage(),new Status(Status.ERROR, Activator.PLUGIN_ID, ex.getMessage(), ex));
			return false;
		}catch (InterruptedException ex){
			ErrorDialog.openError(getShell(), "Error", ex.getMessage(),new Status(Status.ERROR, Activator.PLUGIN_ID, ex.getMessage(), ex));
			return false;
		}
		MessageDialog.openInformation(getShell(), "Submitted", "Your WTF has been submitted");
		return true;
	}

}
