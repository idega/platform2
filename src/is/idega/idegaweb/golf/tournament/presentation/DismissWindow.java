/*
 * Created on 1.6.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.templates.page.GolfWindow;

import com.idega.presentation.IWContext;


/**
 * Title: DismissWindow
 * Description:
 * Copyright: Copyright (c) 2004
 * Company: idega Software
 * @author 2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */
public class DismissWindow extends GolfWindow {

	/**
	 * 
	 */
	public DismissWindow() {
		this("TournamentAdmin",350,250);
	}

	/**
	 * @param name
	 * @param width
	 * @param height
	 */
	public DismissWindow(String name, int width, int height) {
		super(name, width, height);
		this.setGolfClassToInstanciate(Dismiss.class);
	}
	
	public void main(IWContext iwc) {
		setTitle(localize("tournament.dismiss", "Dismiss"));
	}

}