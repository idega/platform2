/*
 * $Id$
 *
 * Copyright (C) 2000-2003 Idega Software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega Software.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.block.forum.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.Window;

/**
 * @author palli
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CommuneForumTopicFiles extends Window {
	public CommuneForumTopicFiles() {
		super("Files",200,200);
		setResizable(true);
	}
	
	public void main(IWContext iwc) {
		add("Here is a list of files:\n");
		Table t = new Table();
		t.add("File 1...",1,1);
		t.add("File 2...",1,2);
		t.add("File 3...",1,3);
		t.add("File 4...",1,4);
	}
}
