/*
 * $Id: TabyHelpTreeWindow.java,v 1.1 2003/12/18 15:02:18 palli Exp $
 *
 * Copyright (C) 2000-2003 Idega Software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega Software.
 * Use is subject to license terms.
 */
package se.idega.block.help.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Window;

/**
 * @author palli
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TabyHelpTreeWindow extends Window {
	public TabyHelpTreeWindow() {
		super();
		setResizable(true);
		setScrollbar(true);
		setHeight(300);
		setWidth(600);
	}
	
	public void main(IWContext iwc) {
		add(new TabyHelpTree());
	}
}