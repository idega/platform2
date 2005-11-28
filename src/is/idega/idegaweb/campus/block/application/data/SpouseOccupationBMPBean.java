/*
 * $Id: SpouseOccupationBMPBean.java,v 1.4.4.1 2005/11/28 11:53:50 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.application.data;

import java.sql.SQLException;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;

/**
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class SpouseOccupationBMPBean extends GenericEntity
		implements SpouseOccupation{
	private static final String ENTITY_NAME = "cam_spouse_occ";

	private static final String COLUMN_DESCRIPTION = "description";

	private static final String COLUMN_LOC_KEY = "localization_key";
	
	public SpouseOccupationBMPBean() {
		super();
	}

	public SpouseOccupationBMPBean(int id) throws SQLException {
		super(id);
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_DESCRIPTION, "Description", true, true, String.class, 255);
		addAttribute(COLUMN_LOC_KEY, "Localization key", true, true, String.class, 255);
	}

	public String getEntityName() {
		return (ENTITY_NAME);
	}

	public String getDescriptionColumnName() {
		return (COLUMN_DESCRIPTION);
	}

	public String getName() {
		return (getDescription());
	}

	public String getDescription() {
		return getStringColumnValue(COLUMN_DESCRIPTION);
	}

	public void setDescription(String description) {
		setColumn(COLUMN_DESCRIPTION, description);
	}
	
	public void setLocalizationKey(String key) {
		setColumn(COLUMN_LOC_KEY, key);
	}

	public String getLocalizationKey() {
		return getStringColumnValue(COLUMN_LOC_KEY);
	}
	
	public java.util.Collection ejbFindAll() throws FinderException {
		return super.idoFindAllIDsBySQL();
	}
}