/**
 * BuildingServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.campus.webservice.building.server;

import is.idega.idegaweb.campus.webservice.building.business.BuildingWSServiceBusiness;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWMainApplication;

public class BuildingServiceSoapBindingImpl implements is.idega.idegaweb.campus.webservice.building.server.BuildingWSService{
    public is.idega.idegaweb.campus.webservice.building.server.ComplexInfo[] getComplexInfo() throws java.rmi.RemoteException {
        return getBusiness().getComplexInfo();
    }

    public is.idega.idegaweb.campus.webservice.building.server.BuildingInfo[] getBuildingInfo(is.idega.idegaweb.campus.webservice.building.server.ComplexInfo in0) throws java.rmi.RemoteException {
        return getBusiness().getBuildingInfo(in0);
    }

    public is.idega.idegaweb.campus.webservice.building.server.ApartmentInfo[] getApartmentInfo(is.idega.idegaweb.campus.webservice.building.server.BuildingInfo in0) throws java.rmi.RemoteException {
        return getBusiness().getApartmentInfo(in0);
    }

    private BuildingWSServiceBusiness getBusiness() throws IBOLookupException {
    	BuildingWSServiceBusiness bus1 = (BuildingWSServiceBusiness) IBOLookup
		.getServiceInstance(IWMainApplication
				.getDefaultIWApplicationContext(),
				BuildingWSServiceBusiness.class);
    	
    	return bus1;
    }
}