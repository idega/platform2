/**
 * 
 */
package com.idega.block.building.business;



import com.idega.business.IBOHomeImpl;

/**
 * @author bluebottle
 *
 */
public class BuildingServiceHomeImpl extends IBOHomeImpl implements
		BuildingServiceHome {
	protected Class getBeanInterfaceClass() {
		return BuildingService.class;
	}

	public BuildingService create() throws javax.ejb.CreateException {
		return (BuildingService) super.createIBO();
	}

}
