/*
 * Created on 15.4.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;



/**
 * Title: TournamentListsWindow
 * Description:
 * Copyright: Copyright (c) 2004
 * Company: idega Software
 * @author 2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */
public class TournamentListsWindow extends TournamentAdministratorWindow {

	/**
	 * 
	 */
	public TournamentListsWindow() {
		this("TournamentAdmin",850,600);
	}

	/**
	 * @param name
	 * @param width
	 * @param height
	 */
	public TournamentListsWindow(String name, int width, int height) {
		super(name, width, height);
		this.setGolfClassToInstanciate(TournamentLists.class);
	}

}
