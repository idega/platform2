/*
 * Created on 24.5.2004
 */
package is.idega.idegaweb.golf.clubs.presentation;

import com.idega.presentation.IWContext;

import is.idega.idegaweb.golf.templates.page.GolfWindow;


/**
 * Title: UnionCorrectWindow
 * Description:
 * Copyright: Copyright (c) 2004
 * Company: idega Software
 * @author 2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */
public class UnionCorrectWindow extends GolfWindow {

	/**
	 * 
	 */
	public UnionCorrectWindow() {
		this("TournamentAdmin",300,250);
	}

	/**
	 * @param name
	 * @param width
	 * @param height
	 */
	public UnionCorrectWindow(String name, int width, int height) {
		super(name, width, height);
		this.setGolfClassToInstanciate(UnionCorrect.class);
	}
	
	public void main(IWContext iwc) throws Exception {
		this.setTitle(getResourceBundle(iwc).getLocalizedString("tournament.verify_tournament", "Verify tournament"));
	}

}
