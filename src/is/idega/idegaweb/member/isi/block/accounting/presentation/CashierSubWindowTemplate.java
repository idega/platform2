/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation;

import com.idega.presentation.Block;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * @author palli
 */
public class CashierSubWindowTemplate extends Block {
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi.block.accounting";
	
	protected static final String ERROR_COULD_NOT_SAVE = "isi_acc_could_not_save";
	
	protected Group _club = null;
	protected Group _division = null;
	protected User _user = null;
	
	/**
	 * 
	 */
	public CashierSubWindowTemplate() {
		super();
	}

	public void setClub(Group club) {
		_club = club;
	}
	
	public void setDivision(Group division) {
		_division = division;
	}
	
	public void setUser(User user) {
		_user = user;
	}
	
	public Group getClub() {
		return _club;
	}
	
	public Group getDivision() {
		return _division;
	}
	
	public User getUser() {
		return _user;
	}
	
	
	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
}