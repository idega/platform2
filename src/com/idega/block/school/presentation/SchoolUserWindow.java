package com.idega.block.school.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Window;

/**
 * @author anders
 */
public class SchoolUserWindow extends Window {

	public SchoolUserWindow() {
		this.setWidth(600);
		this.setHeight(600);
		this.setScrollbar(true);
		this.setResizable(true);	
		this.setAllMargins(0);
	}

	/**
	 * @see com.idega.presentation.PresentationObject#main(IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		this.setParentToReload();
		SchoolUserEditor e = new SchoolUserEditor();
		e.setHideLogin(true);
		e.setHideBackButton(true);
		e.setAddCloseButton(true);
		add(e);
	}	
}
