/*
 * $Id$
 *
 * Copyright (C) 2000-2003 Idega Software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega Software.
 * Use is subject to license terms.
 */
package com.idega.block.help.presentation;

import com.idega.block.help.data.HelpNode;
import com.idega.core.data.ICTreeNode;
import com.idega.idegaweb.help.presentation.*;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.TreeViewer;

/**
 * @author palli
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class HelpTreeViewer extends TreeViewer {
	public HelpTreeViewer() {
		super();
	}
	
	public static TreeViewer getTreeViewerInstance(ICTreeNode node, IWContext iwc) {
		HelpTreeViewer viewer = new HelpTreeViewer();
		viewer.setRootNode(node);
		return viewer;
	}
	
	public PresentationObject getObjectToAddToColumn(int colIndex, ICTreeNode node, IWContext iwc, boolean nodeIsOpen, boolean nodeHasChild, boolean isRootNode) {
		PresentationObject o = super.getObjectToAddToColumn(colIndex,node,iwc,nodeIsOpen,nodeHasChild,isRootNode);		
		if (node instanceof HelpNode && o instanceof Link) {
			Link l = (Link)o;
			l.addParameter(Help.HELP_BUNDLE, ((HelpNode) node).getBundleName());
			
			return l;
		}

		return o;
	}
}