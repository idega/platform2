/*
 * Created on 24.5.2004
 */
package is.idega.idegaweb.golf.clubs.presentation;

import is.idega.idegaweb.golf.templates.page.GolfWindow;

import com.idega.presentation.IWContext;


/**
 * Title: MemberCorrectWindow
 * Description:
 * Copyright: Copyright (c) 2004
 * Company: idega Software
 * @author 2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */
public class MemberCorrectWindow extends GolfWindow {

	/**
	 * 
	 */
	public MemberCorrectWindow() {
		this("TournamentAdmin",300,300);
	}

	/**
	 * @param name
	 * @param width
	 * @param height
	 */
	public MemberCorrectWindow(String name, int width, int height) {
		super(name, width, height);
		this.setGolfClassToInstanciate(MemberCorrect.class);
	}
	
	public void main(IWContext iwc) throws Exception {
		this.setTitle(getResourceBundle(iwc).getLocalizedString("tournament.verify_tournament","Verify tournament"));
	}

}
