/*
 * Created on Aug 18, 2003
 *
 */
package se.idega.idegaweb.commune.accounting.userinfo.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Window;

/**
 * BruttoIncomeWindow
 * @author aron 
 * @version 1.0
 */

public class BruttoIncomeWindow extends Window {

	public BruttoIncomeWindow() {
		this.setWidth(600);
		this.setHeight(500);
		this.setScrollbar(true);
		this.setResizable(true);
		this.setAllMargins(0);
	}
	
	/**
	 * @see com.idega.presentation.PresentationObject#main(IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
	  BruttoIncomeEditor editor = new BruttoIncomeEditor();
	  //editor.setShowCloseButton(true);
	  add(editor);
	}
	
	public static String getUserIDParameterName(){
		return BruttoIncomeEditor.PRM_USER_ID;
	}
}
