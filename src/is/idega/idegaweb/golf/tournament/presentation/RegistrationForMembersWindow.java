/*
 * Created on 24.5.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;

import com.idega.presentation.IWContext;

import is.idega.idegaweb.golf.templates.page.GolfWindow;


/**
 * Title: RegistrationForMembersWindow
 * Description:
 * Copyright: Copyright (c) 2004
 * Company: idega Software
 * @author 2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */
public class RegistrationForMembersWindow extends GolfWindow {

	/**
	 * 
	 */
	public RegistrationForMembersWindow() {
		this("TournamentAdmin",700,600);
	}

	/**
	 * @param name
	 * @param width
	 * @param height
	 */
	public RegistrationForMembersWindow(String name, int width, int height) {
		super(name, width, height);
		this.setGolfClassToInstanciate(RegistrationForMembers.class);
		this.setResizable(true);
	}
	
	public void main(IWContext iwc) throws Exception {
		this.setTitle(getResourceBundle(iwc).getLocalizedString("tournament.registration_for_members", "Registration"));
	}

}
