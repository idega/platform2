/**
 * BuildingServiceSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.campus.webservice.building.server;

public class BuildingServiceSoapBindingSkeleton implements is.idega.idegaweb.campus.webservice.building.server.BuildingWSService, org.apache.axis.wsdl.Skeleton {
    private is.idega.idegaweb.campus.webservice.building.server.BuildingWSService impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
        };
        _oper = new org.apache.axis.description.OperationDesc("getComplexInfo", _params, new javax.xml.namespace.QName("", "getComplexInfoReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:buildingservice", "ArrayOfComplexInfo"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:buildingservice", "getComplexInfo"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getComplexInfo") == null) {
            _myOperations.put("getComplexInfo", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getComplexInfo")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:buildingservice", "ComplexInfo"), is.idega.idegaweb.campus.webservice.building.server.ComplexInfo.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getBuildingInfo", _params, new javax.xml.namespace.QName("", "getBuildingInfoReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:buildingservice", "ArrayOfBuildingInfo"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:buildingservice", "getBuildingInfo"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getBuildingInfo") == null) {
            _myOperations.put("getBuildingInfo", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getBuildingInfo")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:buildingservice", "BuildingInfo"), is.idega.idegaweb.campus.webservice.building.server.BuildingInfo.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getApartmentInfo", _params, new javax.xml.namespace.QName("", "getApartmentInfoReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:buildingservice", "ArrayOfApartmentInfo"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:buildingservice", "getApartmentInfo"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getApartmentInfo") == null) {
            _myOperations.put("getApartmentInfo", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getApartmentInfo")).add(_oper);
    }

    public BuildingServiceSoapBindingSkeleton() {
        this.impl = new is.idega.idegaweb.campus.webservice.building.server.BuildingServiceSoapBindingImpl();
    }

    public BuildingServiceSoapBindingSkeleton(is.idega.idegaweb.campus.webservice.building.server.BuildingWSService impl) {
        this.impl = impl;
    }
    public is.idega.idegaweb.campus.webservice.building.server.ComplexInfo[] getComplexInfo() throws java.rmi.RemoteException
    {
        is.idega.idegaweb.campus.webservice.building.server.ComplexInfo[] ret = impl.getComplexInfo();
        return ret;
    }

    public is.idega.idegaweb.campus.webservice.building.server.BuildingInfo[] getBuildingInfo(is.idega.idegaweb.campus.webservice.building.server.ComplexInfo in0) throws java.rmi.RemoteException
    {
        is.idega.idegaweb.campus.webservice.building.server.BuildingInfo[] ret = impl.getBuildingInfo(in0);
        return ret;
    }

    public is.idega.idegaweb.campus.webservice.building.server.ApartmentInfo[] getApartmentInfo(is.idega.idegaweb.campus.webservice.building.server.BuildingInfo in0) throws java.rmi.RemoteException
    {
        is.idega.idegaweb.campus.webservice.building.server.ApartmentInfo[] ret = impl.getApartmentInfo(in0);
        return ret;
    }

}
