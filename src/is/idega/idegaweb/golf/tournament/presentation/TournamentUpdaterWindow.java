/*
 * Created on 15.4.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;



/**
 * Title: TournamentUpdaterWindow
 * Description:
 * Copyright: Copyright (c) 2004
 * Company: idega Software
 * @author 2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */
public class TournamentUpdaterWindow extends TournamentAdministratorWindow {

	/**
	 * 
	 */
	public TournamentUpdaterWindow() {
		this("TournamentAdmin",850,600);
	}

	/**
	 * @param name
	 * @param width
	 * @param height
	 */
	public TournamentUpdaterWindow(String name, int width, int height) {
		super(name, width, height);
		this.setGolfClassToInstanciate(TournamentUpdater.class);
	}

}
