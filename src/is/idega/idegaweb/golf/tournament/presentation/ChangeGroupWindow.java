/*
 * Created on 1.6.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.templates.page.GolfWindow;

import com.idega.presentation.IWContext;


/**
 * Title: ChangeGroupWindow
 * Description:
 * Copyright: Copyright (c) 2004
 * Company: idega Software
 * @author 2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */
public class ChangeGroupWindow extends GolfWindow {

	/**
	 * 
	 */
	public ChangeGroupWindow() {
		this("TournamentAdmin",350,200);
	}

	/**
	 * @param name
	 * @param width
	 * @param height
	 */
	public ChangeGroupWindow(String name, int width, int height) {
		super(name, width, height);
		this.setGolfClassToInstanciate(ChangeGroup.class);
	}
	
	public void main(IWContext iwc) {
		setTitle(localize("tournament.change_group", "Change group"));
	}

}