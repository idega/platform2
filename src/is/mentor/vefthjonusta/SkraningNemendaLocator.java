/**
 * SkraningNemendaLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.mentor.vefthjonusta;

public class SkraningNemendaLocator extends org.apache.axis.client.Service implements is.mentor.vefthjonusta.SkraningNemenda {

    public SkraningNemendaLocator() {
    }


    public SkraningNemendaLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SkraningNemendaLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SkraningNemendaSoap
    private java.lang.String SkraningNemendaSoap_address = "https://217.151.171.250/vefthjonusta/skraningNemenda/skraningNemenda.asmx";

    public java.lang.String getSkraningNemendaSoapAddress() {
        return SkraningNemendaSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SkraningNemendaSoapWSDDServiceName = "SkraningNemendaSoap";

    public java.lang.String getSkraningNemendaSoapWSDDServiceName() {
        return SkraningNemendaSoapWSDDServiceName;
    }

    public void setSkraningNemendaSoapWSDDServiceName(java.lang.String name) {
        SkraningNemendaSoapWSDDServiceName = name;
    }

    public is.mentor.vefthjonusta.SkraningNemendaSoap getSkraningNemendaSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SkraningNemendaSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSkraningNemendaSoap(endpoint);
    }

    public is.mentor.vefthjonusta.SkraningNemendaSoap getSkraningNemendaSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            is.mentor.vefthjonusta.SkraningNemendaSoapStub _stub = new is.mentor.vefthjonusta.SkraningNemendaSoapStub(portAddress, this);
            _stub.setPortName(getSkraningNemendaSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSkraningNemendaSoapEndpointAddress(java.lang.String address) {
        SkraningNemendaSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (is.mentor.vefthjonusta.SkraningNemendaSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                is.mentor.vefthjonusta.SkraningNemendaSoapStub _stub = new is.mentor.vefthjonusta.SkraningNemendaSoapStub(new java.net.URL(SkraningNemendaSoap_address), this);
                _stub.setPortName(getSkraningNemendaSoapWSDDServiceName());
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
        if ("SkraningNemendaSoap".equals(inputPortName)) {
            return getSkraningNemendaSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("https://mentor.is/vefthjonusta", "SkraningNemenda");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("https://mentor.is/vefthjonusta", "SkraningNemendaSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("SkraningNemendaSoap".equals(portName)) {
            setSkraningNemendaSoapEndpointAddress(address);
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
