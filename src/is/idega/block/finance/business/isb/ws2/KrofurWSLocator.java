/**
 * KrofurWSLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.block.finance.business.isb.ws2;

public class KrofurWSLocator extends org.apache.axis.client.Service implements is.idega.block.finance.business.isb.ws2.KrofurWS {

    public KrofurWSLocator() {
    }


    public KrofurWSLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public KrofurWSLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for KrofurWSSoap
    private java.lang.String KrofurWSSoap_address = "https://ws.isb.is/adgerdirv1/krofur.asmx";

    public java.lang.String getKrofurWSSoapAddress() {
        return KrofurWSSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String KrofurWSSoapWSDDServiceName = "KrofurWSSoap";

    public java.lang.String getKrofurWSSoapWSDDServiceName() {
        return KrofurWSSoapWSDDServiceName;
    }

    public void setKrofurWSSoapWSDDServiceName(java.lang.String name) {
        KrofurWSSoapWSDDServiceName = name;
    }

    public is.idega.block.finance.business.isb.ws2.KrofurWSSoap_PortType getKrofurWSSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(KrofurWSSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getKrofurWSSoap(endpoint);
    }

    public is.idega.block.finance.business.isb.ws2.KrofurWSSoap_PortType getKrofurWSSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            is.idega.block.finance.business.isb.ws2.KrofurWSSoap_BindingStub _stub = new is.idega.block.finance.business.isb.ws2.KrofurWSSoap_BindingStub(portAddress, this);
            _stub.setPortName(getKrofurWSSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setKrofurWSSoapEndpointAddress(java.lang.String address) {
        KrofurWSSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (is.idega.block.finance.business.isb.ws2.KrofurWSSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                is.idega.block.finance.business.isb.ws2.KrofurWSSoap_BindingStub _stub = new is.idega.block.finance.business.isb.ws2.KrofurWSSoap_BindingStub(new java.net.URL(KrofurWSSoap_address), this);
                _stub.setPortName(getKrofurWSSoapWSDDServiceName());
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
        if ("KrofurWSSoap".equals(inputPortName)) {
            return getKrofurWSSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ws.isb.is", "KrofurWS");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ws.isb.is", "KrofurWSSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("KrofurWSSoap".equals(portName)) {
            setKrofurWSSoapEndpointAddress(address);
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
