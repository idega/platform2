/*
 * $Id: PostingParameterListViewer.java,v 1.2 2003/09/08 08:10:07 laddi Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.posting.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Window;

/**
 * @author Kelly
 *
 */
public class PostingParameterListViewer extends Window {

//	private final static String PARAM_ACTION = "action";

	public PostingParameterListViewer() {
		this.setWidth(640);
		this.setHeight(480);
		this.setScrollbar(true);
		this.setResizable(false);
	}

	/**
	 * @see com.idega.presentation.PresentationObject#main(IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		//String action = iwc.getParameter(PARAM_ACTION);
		
		PostingParameterListEditor editor = new PostingParameterListEditor();
		add(editor);
	}
	
}