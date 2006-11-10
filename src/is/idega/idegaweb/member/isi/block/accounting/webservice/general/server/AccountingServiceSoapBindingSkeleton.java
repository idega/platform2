/**
 * AccountingServiceSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.member.isi.block.accounting.webservice.general.server;

public class AccountingServiceSoapBindingSkeleton implements is.idega.idegaweb.member.isi.block.accounting.webservice.general.server.AccountingService, org.apache.axis.wsdl.Skeleton {
    private is.idega.idegaweb.member.isi.block.accounting.webservice.general.server.AccountingService impl;
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
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getUser", _params, new javax.xml.namespace.QName("", "getUserReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:accounting", "UserInfo"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:accounting", "getUser"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getUser") == null) {
            _myOperations.put("getUser", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getUser")).add(_oper);
    }

    public AccountingServiceSoapBindingSkeleton() {
        this.impl = new is.idega.idegaweb.member.isi.block.accounting.webservice.general.server.AccountingServiceSoapBindingImpl();
    }

    public AccountingServiceSoapBindingSkeleton(is.idega.idegaweb.member.isi.block.accounting.webservice.general.server.AccountingService impl) {
        this.impl = impl;
    }
    public is.idega.idegaweb.member.isi.block.accounting.webservice.general.server.UserInfo getUser(java.lang.String in0) throws java.rmi.RemoteException
    {
        is.idega.idegaweb.member.isi.block.accounting.webservice.general.server.UserInfo ret = impl.getUser(in0);
        return ret;
    }

}
