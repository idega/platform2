/*
 * $Id:$
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.childcare.presentation;

import com.idega.block.media.business.MediaBusiness;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.presentation.ui.Window;

/**
 * This class does something very clever.....
 * 
 * @author <a href="palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ViewChildCareContract extends Window {
	public static String VIEW_CONTRACT_FILE = "view_contract_file_id";
	
	public ViewChildCareContract() {
		setResizable(true);
		setMenubar(true);
		keepFocus();
	}

	public void main(IWContext iwc) {
		String id = iwc.getParameter(VIEW_CONTRACT_FILE) ;
		int fileId = (id == null) ? -1 : Integer.parseInt(id);
		Page p = getParentPage();		
		
		if (fileId > 0) {
			String url = MediaBusiness.getMediaURL(fileId, iwc.getIWMainApplication());
			p.setToRedirect(url);
		}
		else {
			add("Could not create contract file");
		}
	}
}