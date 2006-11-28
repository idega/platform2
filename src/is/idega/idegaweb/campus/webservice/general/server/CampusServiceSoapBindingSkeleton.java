/**
 * CampusServiceSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.campus.webservice.general.server;

public class CampusServiceSoapBindingSkeleton implements is.idega.idegaweb.campus.webservice.general.server.CampusService, org.apache.axis.wsdl.Skeleton {
    private is.idega.idegaweb.campus.webservice.general.server.CampusService impl;
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
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getTenantInfo", _params, new javax.xml.namespace.QName("", "getTenantInfoReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:campus", "ArrayOfTenantInfo"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:campus", "getTenantInfo"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getTenantInfo") == null) {
            _myOperations.put("getTenantInfo", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getTenantInfo")).add(_oper);
    }

    public CampusServiceSoapBindingSkeleton() {
        this.impl = new is.idega.idegaweb.campus.webservice.general.server.CampusServiceSoapBindingImpl();
    }

    public CampusServiceSoapBindingSkeleton(is.idega.idegaweb.campus.webservice.general.server.CampusService impl) {
        this.impl = impl;
    }
    public is.idega.idegaweb.campus.webservice.general.server.TenantInfo[] getTenantInfo(int in0) throws java.rmi.RemoteException
    {
        is.idega.idegaweb.campus.webservice.general.server.TenantInfo[] ret = impl.getTenantInfo(in0);
        return ret;
    }

}
