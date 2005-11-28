/*
<<<<<<< CurrentResidencyBMPBean.java
 * $Id: CurrentResidencyBMPBean.java,v 1.3.4.1 2005/11/28 11:53:32 palli Exp $
=======

 * $Id: CurrentResidencyBMPBean.java,v 1.3.4.1 2005/11/28 11:53:32 palli Exp $

>>>>>>> 1.2
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */

package is.idega.idegaweb.campus.block.application.data;


import java.sql.SQLException;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */

public class CurrentResidencyBMPBean
	extends GenericEntity
	implements CurrentResidency {

	private static final String ENTITY_NAME = "cam_curr_res";

	private static final String COLUMN_DESCRIPTION = "description";

	private static final String COLUMN_REQ_EXTRA_INFO = "requires_extra_info";
	
	private static final String COLUMN_LOC_KEY = "localization_key";

	public CurrentResidencyBMPBean() {
		super();
	}

	public CurrentResidencyBMPBean(int id) throws SQLException {
		super(id);
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_DESCRIPTION, "Description", true, true, String.class, 255);
		addAttribute(COLUMN_REQ_EXTRA_INFO, "Requires extra info", true, true, Boolean.class);
		addAttribute(COLUMN_LOC_KEY, "Localization key", true, true, String.class, 255);
	}

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public String getDescriptionColumnName() {
		return COLUMN_DESCRIPTION;
	}

	public String getRequiresExtranInfoColumnName() {
		return COLUMN_REQ_EXTRA_INFO;
	}
	public String getName() {
		return getDescription();
	}

	public String getDescription() {
		return getStringColumnValue(COLUMN_DESCRIPTION);
	}
	public void setDescription(String description) {
		setColumn(COLUMN_DESCRIPTION, description);
	}

	public boolean getRequiresExtraInfo() {
		return getBooleanColumnValue(COLUMN_REQ_EXTRA_INFO, false);
	}

	public void setRequiresExtranInfo(boolean extraInfo) {
		setColumn(COLUMN_REQ_EXTRA_INFO, extraInfo);
	}

	public void setLocalizationKey(String key) {
		setColumn(COLUMN_LOC_KEY, key);
	}

	public String getLocalizationKey() {
		return getStringColumnValue(COLUMN_LOC_KEY);
	}

	public java.util.Collection ejbFindAll() throws FinderException{
		return super.idoFindAllIDsBySQL();
	}
}