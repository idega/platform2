/*
 * Created on 1.6.2004
 */
package is.idega.idegaweb.golf.handicap.presentation;

import com.idega.presentation.IWContext;

import is.idega.idegaweb.golf.templates.page.GolfWindow;


/**
 * Title: HandicapUtilityWindow
 * Description:
 * Copyright: Copyright (c) 2004
 * Company: idega Software
 * @author 2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */
public class HandicapUtilityWindow extends GolfWindow {

	/**
	 * 
	 */
	public HandicapUtilityWindow() {
		this("TournamentAdmin",350,200);
	}

	/**
	 * @param name
	 * @param width
	 * @param height
	 */
	public HandicapUtilityWindow(String name, int width, int height) {
		super(name, width, height);
		this.setGolfClassToInstanciate(HandicapUtility.class);
	}
	
	public void main(IWContext iwc) {
		String action = iwc.getParameter(HandicapUtility.PARAMETER_METHOD);
		if(action != null) {
			if(action.equals(String.valueOf(HandicapUtility.ACTION_CHANGE_TEES))) {
				setTitle(localize("tournament.change_tees", "Change tees"));
			} else if(action.equals(String.valueOf(HandicapUtility.ACTION_UPDATE_HANDICAP))) {
				setTitle(localize("tournament.update_handicap", "Update handicap"));
			}
		}
	}

}