/**
 * NetbokhaldServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.client;

public class NetbokhaldServiceServiceLocator extends org.apache.axis.client.Service implements is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.client.NetbokhaldServiceService {

    public NetbokhaldServiceServiceLocator() {
    }


    public NetbokhaldServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public NetbokhaldServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for NetbokhaldService
    private java.lang.String NetbokhaldService_address = "http://felixtest.sidan.is/services/NetbokhaldService";

    public java.lang.String getNetbokhaldServiceAddress() {
        return this.NetbokhaldService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String NetbokhaldServiceWSDDServiceName = "NetbokhaldService";

    public java.lang.String getNetbokhaldServiceWSDDServiceName() {
        return this.NetbokhaldServiceWSDDServiceName;
    }

    public void setNetbokhaldServiceWSDDServiceName(java.lang.String name) {
        this.NetbokhaldServiceWSDDServiceName = name;
    }

    public is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.client.NetbokhaldService getNetbokhaldService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(this.NetbokhaldService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getNetbokhaldService(endpoint);
    }

    public is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.client.NetbokhaldService getNetbokhaldService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.client.NetbokhaldServiceSoapBindingStub _stub = new is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.client.NetbokhaldServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getNetbokhaldServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setNetbokhaldServiceEndpointAddress(java.lang.String address) {
        this.NetbokhaldService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.client.NetbokhaldService.class.isAssignableFrom(serviceEndpointInterface)) {
                is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.client.NetbokhaldServiceSoapBindingStub _stub = new is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.client.NetbokhaldServiceSoapBindingStub(new java.net.URL(this.NetbokhaldService_address), this);
                _stub.setPortName(getNetbokhaldServiceWSDDServiceName());
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
        if ("NetbokhaldService".equals(inputPortName)) {
            return getNetbokhaldService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:netbokhald", "NetbokhaldServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (this.ports == null) {
            this.ports = new java.util.HashSet();
            this.ports.add(new javax.xml.namespace.QName("urn:netbokhald", "NetbokhaldService"));
        }
        return this.ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("NetbokhaldService".equals(portName)) {
            setNetbokhaldServiceEndpointAddress(address);
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
