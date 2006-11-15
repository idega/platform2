/**
 * NortekServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.campus.webservice.nortek.server;

public class NortekServiceServiceLocator extends org.apache.axis.client.Service implements is.idega.idegaweb.campus.webservice.nortek.server.NortekServiceService {

    public NortekServiceServiceLocator() {
    }


    public NortekServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public NortekServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for NortekService
    private java.lang.String NortekService_address = "http://www.campus.is/services/NortekService";

    public java.lang.String getNortekServiceAddress() {
        return NortekService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String NortekServiceWSDDServiceName = "NortekService";

    public java.lang.String getNortekServiceWSDDServiceName() {
        return NortekServiceWSDDServiceName;
    }

    public void setNortekServiceWSDDServiceName(java.lang.String name) {
        NortekServiceWSDDServiceName = name;
    }

    public is.idega.idegaweb.campus.webservice.nortek.server.NortekService getNortekService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(NortekService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getNortekService(endpoint);
    }

    public is.idega.idegaweb.campus.webservice.nortek.server.NortekService getNortekService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            is.idega.idegaweb.campus.webservice.nortek.server.NortekServiceSoapBindingStub _stub = new is.idega.idegaweb.campus.webservice.nortek.server.NortekServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getNortekServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setNortekServiceEndpointAddress(java.lang.String address) {
        NortekService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (is.idega.idegaweb.campus.webservice.nortek.server.NortekService.class.isAssignableFrom(serviceEndpointInterface)) {
                is.idega.idegaweb.campus.webservice.nortek.server.NortekServiceSoapBindingStub _stub = new is.idega.idegaweb.campus.webservice.nortek.server.NortekServiceSoapBindingStub(new java.net.URL(NortekService_address), this);
                _stub.setPortName(getNortekServiceWSDDServiceName());
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
        if ("NortekService".equals(inputPortName)) {
            return getNortekService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:nortek", "NortekServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:nortek", "NortekService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("NortekService".equals(portName)) {
            setNortekServiceEndpointAddress(address);
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
