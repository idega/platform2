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

/**
 * @author palli
 */
public class CashierSubWindowTemplate extends Block {
	protected Group _club = null;
	protected Group _division = null;
	
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
	
	public Group getClub() {
		return _club;
	}
	
	public Group getDivision() {
		return _division;
	}
}
