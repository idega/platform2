/*
 * Created on Aug 14, 2003
 *
 */
package se.idega.idegaweb.commune.user.presentation;

import is.idega.idegaweb.member.presentation.UserEditor;

import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Window;

/**
 * UserEditorWindow
 * @author aron 
 * @version 1.0
 */

public class CitizenEditorWindow extends Window {

	public CitizenEditorWindow() {
		this.setWidth(500);
		this.setHeight(500);
		this.setScrollbar(true);
		this.setResizable(true);
		this.setAllMargins(0);
	}

	/**
	 * @see com.idega.presentation.PresentationObject#main(IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		CitizenEditor editor = new CitizenEditor();
		add(editor);
	}
	
	
	
	public static String getUserIDParameterName(){
		return UserEditor.getUserIDParameterName();
	}

}
