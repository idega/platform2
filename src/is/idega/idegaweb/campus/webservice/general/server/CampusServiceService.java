/**
 * CampusServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.campus.webservice.general.server;

public interface CampusServiceService extends javax.xml.rpc.Service {
    public java.lang.String getCampusServiceAddress();

    public is.idega.idegaweb.campus.webservice.general.server.CampusService getCampusService() throws javax.xml.rpc.ServiceException;

    public is.idega.idegaweb.campus.webservice.general.server.CampusService getCampusService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
