/**
 * KrofurWSLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package is.idega.block.finance.business.isb.ws;

public class KrofurWSLocator extends org.apache.axis.client.Service implements is.idega.block.finance.business.isb.ws.KrofurWS {

    // Use to get a proxy class for KrofurWSSoap
    private final java.lang.String KrofurWSSoap_address = "https://ws-test.isb.is/adgerdirv1/krofur.asmx";

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

    public is.idega.block.finance.business.isb.ws.KrofurWSSoap getKrofurWSSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(KrofurWSSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getKrofurWSSoap(endpoint);
    }

    public is.idega.block.finance.business.isb.ws.KrofurWSSoap getKrofurWSSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
        	is.idega.block.finance.business.isb.ws.KrofurWSSoapStub _stub = new is.idega.block.finance.business.isb.ws.KrofurWSSoapStub(portAddress, this);
            _stub.setPortName(getKrofurWSSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (is.idega.block.finance.business.isb.ws.KrofurWSSoap.class.isAssignableFrom(serviceEndpointInterface)) {
            	is.idega.block.finance.business.isb.ws.KrofurWSSoapStub _stub = new is.idega.block.finance.business.isb.ws.KrofurWSSoapStub(new java.net.URL(KrofurWSSoap_address), this);
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
        String inputPortName = portName.getLocalPart();
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
            ports.add(new javax.xml.namespace.QName("KrofurWSSoap"));
        }
        return ports.iterator();
    }

}
