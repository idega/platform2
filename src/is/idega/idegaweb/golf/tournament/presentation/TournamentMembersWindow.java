/*
 * Created on 23.5.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.templates.page.GolfWindow;


/**
 * Title: TournamentMembersWindow
 * Description:
 * Copyright: Copyright (c) 2004
 * Company: idega Software
 * @author 2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */
public class TournamentMembersWindow extends GolfWindow {

	/**
	 * 
	 */
	public TournamentMembersWindow() {
		this("TournamentAdmin",600,600);
	}

	/**
	 * @param name
	 * @param width
	 * @param height
	 */
	public TournamentMembersWindow(String name, int width, int height) {
		super(name, width, height);
		this.setGolfClassToInstanciate(TournamentMembers.class);
	    this.setResizable(true);
	    this.setMenubar(true);
	}

}
