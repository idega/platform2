/*
 * $Id: ContractEditorWindow.java,v 1.1 2004/11/26 14:06:05 aron Exp $
 * Created on 24.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.presentation.admin;

import se.idega.idegaweb.commune.childcare.presentation.ChildCareWindow;

import com.idega.presentation.IWContext;

/**
 * 
 *  Last modified: $Date: 2004/11/26 14:06:05 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public class ContractEditorWindow extends ChildCareWindow{
    
    public ContractEditorWindow() {
		this.setWidth(400);
		this.setHeight(350);
		this.setScrollbar(true);
		this.setResizable(true);	
		this.setAllMargins(0);
	}

	/**
	 * @see com.idega.presentation.PresentationObject#main(IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
	    ContractEditor editor = new ContractEditor();
	    editor.setShowBackButton(false);
		add(editor);
	}

}
