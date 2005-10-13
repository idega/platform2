/*
 * $Id: ManagementTypeBMPBean.java,v 1.2 2005/10/13 08:09:37 palli Exp $ Created on
 * Sep 7, 2005
 * 
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package se.idega.idegaweb.commune.accounting.regulations.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;
import com.idega.data.IDOQuery;

public class ManagementTypeBMPBean extends GenericEntity implements ManagementType {

	private static final String ENTITY_NAME = "cacc_management_type";

	private static final String COLUMN_MANAGEMENT_TYPE = "management_type";

	private static final String COMMUNE = "commune";

	private static final String NOT_COMMUNE = "not_commune";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void insertStartData() throws Exception {
		super.insertStartData();

		System.out.println("¤¤¤ Invoked " + ENTITY_NAME + ".insertStartData ()");

		ManagementTypeHome home = (ManagementTypeHome) IDOLookup.getHome(ManagementType.class);
		final String[] data = { COMMUNE, NOT_COMMUNE };
		for (int i = 0; i < data.length; i++) {
			ManagementType cbType = home.create();
			cbType.setManagementType(data[i]);
			cbType.store();
		}
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_MANAGEMENT_TYPE, "Management type", true, true, String.class);
	}

	public void setManagementType(String type) {
		setColumn(COLUMN_MANAGEMENT_TYPE, type);
	}

	public String getManagementType() {
		return getStringColumnValue(COLUMN_MANAGEMENT_TYPE);
	}

	public void setLocalizationKey(String type) {
		setColumn(COLUMN_MANAGEMENT_TYPE, type);
	}

	public String getLocalizationKey() {
		return getStringColumnValue(COLUMN_MANAGEMENT_TYPE);
	}

	public boolean isCommuneManagementType() {
		String type = getManagementType();
		if (type.equals(COMMUNE)) {
			return true;
		}
		
		return false;
	}
	
	public Collection ejbFindAll() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		return idoFindPKsBySQL(sql.toString());
	}
}