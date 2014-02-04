/*******************************************************************************
 * Copyright (c) 2014 Angelo ZERR.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:      
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.angularjs.internal.ui.hyperlink;

import org.eclipse.angularjs.core.utils.HyperlinkUtils;
import org.eclipse.angularjs.internal.ui.AngularScopeHelper;
import org.eclipse.angularjs.internal.ui.AngularUIMessages;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IRegion;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;

import tern.angular.AngularType;
import tern.angular.protocol.definition.TernAngularDefinitionQuery;
import tern.eclipse.ide.core.IDETernProject;
import tern.eclipse.ide.core.scriptpath.ITernScriptPath;
import tern.eclipse.ide.ui.hyperlink.AbstractTernHyperlink;

/**
 * HTML angular element hyperlink.
 */
public class HTMLAngularHyperLink extends AbstractTernHyperlink {

	private final IDOMAttr attr;
	private final IFile file;
	private final String expression;
	private final AngularType angularType;

	public HTMLAngularHyperLink(IDOMAttr attr, IRegion region, IFile file,
			IDETernProject ternProject, String expression,
			AngularType angularType) {
		super(region, ternProject);
		this.attr = attr;
		this.file = file;
		this.expression = expression;
		this.angularType = angularType;
	}

	@Override
	public void open() {
		try {
			TernAngularDefinitionQuery query = new TernAngularDefinitionQuery(
					angularType);
			query.setExpression(expression);
			ITernScriptPath scriptPath = AngularScopeHelper.populateScope(
					attr.getOwnerElement(), file, angularType, query);
			if (scriptPath != null) {
				ternProject.request(query, query.getFiles(), scriptPath, this);
			} else {
				ternProject.request(query, query.getFiles(), attr, file, this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getHyperlinkText() {
		return AngularUIMessages.HTMLAngularHyperLink_text;
	}

	@Override
	public String getTypeLabel() {
		return AngularUIMessages.HTMLAngularHyperLink_typeLabel;
	}

}
