/**
 * BuildingWSServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.campus.webservice.building.server;

public class BuildingWSServiceServiceLocator extends org.apache.axis.client.Service implements is.idega.idegaweb.campus.webservice.building.server.BuildingWSServiceService {

    public BuildingWSServiceServiceLocator() {
    }


    public BuildingWSServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public BuildingWSServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for BuildingService
    private java.lang.String BuildingService_address = "http://www.studentagardar.is/services/BuildingService";

    public java.lang.String getBuildingServiceAddress() {
        return BuildingService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String BuildingServiceWSDDServiceName = "BuildingService";

    public java.lang.String getBuildingServiceWSDDServiceName() {
        return BuildingServiceWSDDServiceName;
    }

    public void setBuildingServiceWSDDServiceName(java.lang.String name) {
        BuildingServiceWSDDServiceName = name;
    }

    public is.idega.idegaweb.campus.webservice.building.server.BuildingWSService getBuildingService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(BuildingService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getBuildingService(endpoint);
    }

    public is.idega.idegaweb.campus.webservice.building.server.BuildingWSService getBuildingService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            is.idega.idegaweb.campus.webservice.building.server.BuildingServiceSoapBindingStub _stub = new is.idega.idegaweb.campus.webservice.building.server.BuildingServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getBuildingServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setBuildingServiceEndpointAddress(java.lang.String address) {
        BuildingService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (is.idega.idegaweb.campus.webservice.building.server.BuildingWSService.class.isAssignableFrom(serviceEndpointInterface)) {
                is.idega.idegaweb.campus.webservice.building.server.BuildingServiceSoapBindingStub _stub = new is.idega.idegaweb.campus.webservice.building.server.BuildingServiceSoapBindingStub(new java.net.URL(BuildingService_address), this);
                _stub.setPortName(getBuildingServiceWSDDServiceName());
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
        if ("BuildingService".equals(inputPortName)) {
            return getBuildingService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:buildingservice", "BuildingWSServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:buildingservice", "BuildingService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("BuildingService".equals(portName)) {
            setBuildingServiceEndpointAddress(address);
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
