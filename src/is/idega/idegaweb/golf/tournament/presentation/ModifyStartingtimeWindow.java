/*
 * Created on 24.5.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.templates.page.GolfWindow;

import com.idega.presentation.IWContext;


/**
 * Title: ModifyStartingtimeWindow
 * Description:
 * Copyright: Copyright (c) 2004
 * Company: idega Software
 * @author 2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */
public class ModifyStartingtimeWindow extends GolfWindow {

	/**
	 * 
	 */
	public ModifyStartingtimeWindow() {
		this("TournamentAdmin",700,600);
	}

	/**
	 * @param name
	 * @param width
	 * @param height
	 */
	public ModifyStartingtimeWindow(String name, int width, int height) {
		super(name, width, height);
		this.setGolfClassToInstanciate(ModifyStartingtime.class);
		this.setResizable(true);
	}
	
	public void main(IWContext iwc) throws Exception {
		this.setTitle(getResourceBundle(iwc).getLocalizedString("tournament.edit_tee_times", "Edit tee times"));
	}

}
