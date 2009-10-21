/**
 * BuildingWSService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.campus.webservice.building.client;

public interface BuildingWSService extends java.rmi.Remote {
    public is.idega.idegaweb.campus.webservice.building.client.ComplexInfo[] getComplexInfo() throws java.rmi.RemoteException;
    public is.idega.idegaweb.campus.webservice.building.client.BuildingInfo[] getBuildingInfo(is.idega.idegaweb.campus.webservice.building.client.ComplexInfo in0) throws java.rmi.RemoteException;
    public is.idega.idegaweb.campus.webservice.building.client.ApartmentInfo[] getApartmentInfo(is.idega.idegaweb.campus.webservice.building.client.BuildingInfo in0) throws java.rmi.RemoteException;
}
