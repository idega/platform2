/*
 * $Id$
 *
 * Copyright (C) 2000-2003 Idega Software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega Software.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.data;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;

/**
 * @author palli
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class UserTitlesBMPBean extends GenericEntity implements UserTitles {
	private final static String ENTITY_NAME = "isi_titles";

	protected final static String TITLE_KEY = "loc_title_key";
	protected final static String BOARD_TITLE = "board";

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(TITLE_KEY, "Key to localized title", true, true, String.class);
		addAttribute(BOARD_TITLE, "Is title a board title", true, true, Boolean.class);
	}

	public void setLocalizedTitleKey(String key) {
		setColumn(TITLE_KEY, key);
	}

	public void setIsBoardTitle(boolean boardTitle) {
		setColumn(BOARD_TITLE, boardTitle);
	}

	public String getLocalizedTitleKey() {
		return getStringColumnValue(TITLE_KEY);
	}

	public boolean getIsBoardTitle() {
		return getBooleanColumnValue(BOARD_TITLE, false);
	}

	public Collection ejbFindAllTitles() throws FinderException {
		return super.idoFindAllIDsBySQL();
	}

	public Collection ejbFindAllBoardTitle() throws FinderException, RemoteException {
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(getEntityName());
		sql.append(" where ");
		sql.append(BOARD_TITLE);
		sql.append(" = 'Y'");

		return this.idoFindIDsBySQL(sql.toString());
	}
}