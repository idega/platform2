/*
 * Created on 24.5.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;

import com.idega.presentation.IWContext;

import is.idega.idegaweb.golf.templates.page.GolfWindow;


/**
 * Title: TournamentGroupsWindow
 * Description:
 * Copyright: Copyright (c) 2004
 * Company: idega Software
 * @author 2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */
public class TournamentGroupsWindow extends GolfWindow {

	/**
	 * 
	 */
	public TournamentGroupsWindow() {
		this("Tournament Admin",400,500);
	}

	/**
	 * @param name
	 * @param width
	 * @param height
	 */
	public TournamentGroupsWindow(String name, int width, int height) {
		super(name, width, height);
		this.setGolfClassToInstanciate(TournamentGroups.class);
	}

	public void main(IWContext iwc) throws Exception {
		this.setTitle(getResourceBundle(iwc).getLocalizedString("tournament.tournament_groups", "Tournament Groups"));
	}
}
