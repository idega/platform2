package com.idega.block.building.data;

import com.idega.data.GenericEntity;

public class ApartmentDiaryStatusBMPBean extends GenericEntity implements
		ApartmentDiaryStatus {

	protected static final String ENTITY_NAME = "bu_aprt_diary_status";
	
	
	
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		// TODO Auto-generated method stub
	}
}