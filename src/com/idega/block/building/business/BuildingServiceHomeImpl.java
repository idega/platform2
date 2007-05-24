package com.idega.block.building.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class BuildingServiceHomeImpl extends IBOHomeImpl implements
		BuildingServiceHome {
	public Class getBeanInterfaceClass() {
		return BuildingService.class;
	}

	public BuildingService create() throws CreateException {
		return (BuildingService) super.createIBO();
	}
}