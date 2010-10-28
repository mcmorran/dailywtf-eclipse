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
package com.opengridsystems.wtf.submission.ui.popup.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWizard;

import com.opengridsystems.wtf.submission.ui.wizard.SubmitWizard;

public class SubmitAction implements IEditorActionDelegate {

	protected ISelection selection;
	protected IEditorPart editor;
	
	/**
	 * Constructor for Action1.
	 */
	public SubmitAction() {
		super();
	}
	
	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		IWorkbenchWindow window = this.editor.getSite().getWorkbenchWindow();
		final IWorkbenchPage activePage = window.getActivePage();
		if (activePage != null) {
			// Instantiates and initialises the wizard
			IWorkbenchWizard wizard = new SubmitWizard();
			IStructuredSelection selection;
			if (this.selection instanceof IStructuredSelection)
				selection = (IStructuredSelection)this.selection;
			else
				selection = new StructuredSelection(this.selection);
			wizard.init(window.getWorkbench(), selection);
				
			// Instantiates the wizard container with the wizard and opens it
			WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
			dialog.create();
			dialog.open();
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.editor = targetEditor;
	}

}
