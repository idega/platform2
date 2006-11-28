/**
 * CampusServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.campus.webservice.general.server;

public class CampusServiceServiceLocator extends org.apache.axis.client.Service implements is.idega.idegaweb.campus.webservice.general.server.CampusServiceService {

    public CampusServiceServiceLocator() {
    }


    public CampusServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CampusServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CampusService
    private java.lang.String CampusService_address = "http://www.studentagardar.is/services/CampusService";

    public java.lang.String getCampusServiceAddress() {
        return CampusService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CampusServiceWSDDServiceName = "CampusService";

    public java.lang.String getCampusServiceWSDDServiceName() {
        return CampusServiceWSDDServiceName;
    }

    public void setCampusServiceWSDDServiceName(java.lang.String name) {
        CampusServiceWSDDServiceName = name;
    }

    public is.idega.idegaweb.campus.webservice.general.server.CampusService getCampusService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CampusService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCampusService(endpoint);
    }

    public is.idega.idegaweb.campus.webservice.general.server.CampusService getCampusService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            is.idega.idegaweb.campus.webservice.general.server.CampusServiceSoapBindingStub _stub = new is.idega.idegaweb.campus.webservice.general.server.CampusServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getCampusServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCampusServiceEndpointAddress(java.lang.String address) {
        CampusService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (is.idega.idegaweb.campus.webservice.general.server.CampusService.class.isAssignableFrom(serviceEndpointInterface)) {
                is.idega.idegaweb.campus.webservice.general.server.CampusServiceSoapBindingStub _stub = new is.idega.idegaweb.campus.webservice.general.server.CampusServiceSoapBindingStub(new java.net.URL(CampusService_address), this);
                _stub.setPortName(getCampusServiceWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("CampusService".equals(inputPortName)) {
            return getCampusService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:campus", "CampusServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:campus", "CampusService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CampusService".equals(portName)) {
            setCampusServiceEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
