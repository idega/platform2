package com.idega.block.building.data;

import com.idega.data.GenericEntity;

public class ApartmentDiaryBMPBean extends GenericEntity implements
		ApartmentDiary {

	protected static final String ENTITY_NAME = "bu_aprt_diary";
	
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
	}
}