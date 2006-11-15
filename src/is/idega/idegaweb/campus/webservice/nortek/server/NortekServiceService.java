/**
 * NortekServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.campus.webservice.nortek.server;

public interface NortekServiceService extends javax.xml.rpc.Service {
    public java.lang.String getNortekServiceAddress();

    public is.idega.idegaweb.campus.webservice.nortek.server.NortekService getNortekService() throws javax.xml.rpc.ServiceException;

    public is.idega.idegaweb.campus.webservice.nortek.server.NortekService getNortekService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
