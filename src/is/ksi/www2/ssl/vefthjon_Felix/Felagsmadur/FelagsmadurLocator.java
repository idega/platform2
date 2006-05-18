/**
 * FelagsmadurLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package is.ksi.www2.ssl.vefthjon_Felix.Felagsmadur;

public class FelagsmadurLocator extends org.apache.axis.client.Service implements is.ksi.www2.ssl.vefthjon_Felix.Felagsmadur.Felagsmadur {

    public FelagsmadurLocator() {
    }


    public FelagsmadurLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public FelagsmadurLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for FelagsmadurSoap
    private java.lang.String FelagsmadurSoap_address = "http://www2.ksi.is/ssl/vefthjon_felix/Felagsmadur.asmx";

    public java.lang.String getFelagsmadurSoapAddress() {
        return FelagsmadurSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String FelagsmadurSoapWSDDServiceName = "FelagsmadurSoap";

    public java.lang.String getFelagsmadurSoapWSDDServiceName() {
        return FelagsmadurSoapWSDDServiceName;
    }

    public void setFelagsmadurSoapWSDDServiceName(java.lang.String name) {
        FelagsmadurSoapWSDDServiceName = name;
    }

    public is.ksi.www2.ssl.vefthjon_Felix.Felagsmadur.FelagsmadurSoap getFelagsmadurSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(FelagsmadurSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getFelagsmadurSoap(endpoint);
    }

    public is.ksi.www2.ssl.vefthjon_Felix.Felagsmadur.FelagsmadurSoap getFelagsmadurSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            is.ksi.www2.ssl.vefthjon_Felix.Felagsmadur.FelagsmadurSoapStub _stub = new is.ksi.www2.ssl.vefthjon_Felix.Felagsmadur.FelagsmadurSoapStub(portAddress, this);
            _stub.setPortName(getFelagsmadurSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setFelagsmadurSoapEndpointAddress(java.lang.String address) {
        FelagsmadurSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (is.ksi.www2.ssl.vefthjon_Felix.Felagsmadur.FelagsmadurSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                is.ksi.www2.ssl.vefthjon_Felix.Felagsmadur.FelagsmadurSoapStub _stub = new is.ksi.www2.ssl.vefthjon_Felix.Felagsmadur.FelagsmadurSoapStub(new java.net.URL(FelagsmadurSoap_address), this);
                _stub.setPortName(getFelagsmadurSoapWSDDServiceName());
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
        if ("FelagsmadurSoap".equals(inputPortName)) {
            return getFelagsmadurSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www2.ksi.is/ssl/vefthjon_Felix/Felagsmadur/", "Felagsmadur");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www2.ksi.is/ssl/vefthjon_Felix/Felagsmadur/", "FelagsmadurSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("FelagsmadurSoap".equals(portName)) {
            setFelagsmadurSoapEndpointAddress(address);
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
