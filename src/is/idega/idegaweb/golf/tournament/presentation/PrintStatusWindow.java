/*
 * Created on 24.5.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.templates.page.GolfWindow;


/**
 * Title: PrintStatusWindow
 * Description:
 * Copyright: Copyright (c) 2004
 * Company: idega Software
 * @author 2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */
public class PrintStatusWindow extends GolfWindow {

	/**
	 * 
	 */
	public PrintStatusWindow() {
		this("TournamentAdmin",650,500);
	}

	/**
	 * @param name
	 * @param width
	 * @param height
	 */
	public PrintStatusWindow(String name, int width, int height) {
		super(name, width, height);
		this.setGolfClassToInstanciate(PrintStatus.class);
		this.setMenubar(true);
	    this.setResizable(true);
	}

}
