/**
 * NetbokhaldServiceSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.server;

public class NetbokhaldServiceSoapBindingSkeleton implements is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.server.NetbokhaldService, org.apache.axis.wsdl.Skeleton {
    private is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.server.NetbokhaldService impl;
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
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getEntries", _params, new javax.xml.namespace.QName("", "getEntriesReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:netbokhald", "ArrayOfNetbokhaldEntry"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:netbokhald", "getEntries"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getEntries") == null) {
            _myOperations.put("getEntries", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getEntries")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getEntries", _params, new javax.xml.namespace.QName("", "getEntriesReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:netbokhald", "ArrayOfNetbokhaldEntry"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:netbokhald", "getEntries"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getEntries") == null) {
            _myOperations.put("getEntries", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getEntries")).add(_oper);
    }

    public NetbokhaldServiceSoapBindingSkeleton() {
        this.impl = new is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.server.NetbokhaldServiceSoapBindingImpl();
    }

    public NetbokhaldServiceSoapBindingSkeleton(is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.server.NetbokhaldService impl) {
        this.impl = impl;
    }
    public is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.server.NetbokhaldEntry[] getEntries(java.lang.String in0, java.util.Calendar in1) throws java.rmi.RemoteException
    {
        is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.server.NetbokhaldEntry[] ret = impl.getEntries(in0, in1);
        return ret;
    }

    public is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.server.NetbokhaldEntry[] getEntries(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException
    {
        is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.server.NetbokhaldEntry[] ret = impl.getEntries(in0, in1);
        return ret;
    }

}
