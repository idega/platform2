/*
 * Created on 15.4.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;



/**
 * Title: PrintingWindow
 * Description:
 * Copyright: Copyright (c) 2004
 * Company: idega Software
 * @author 2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */
public class PrintingWindow extends TournamentAdministratorWindow {

	/**
	 * 
	 */
	public PrintingWindow() {
		this("TournamentAdmin",850,600);
	}

	/**
	 * @param name
	 * @param width
	 * @param height
	 */
	public PrintingWindow(String name, int width, int height) {
		super(name, width, height);
		this.setGolfClassToInstanciate(Printing.class);
		setScrollbar(true);
	}

}
