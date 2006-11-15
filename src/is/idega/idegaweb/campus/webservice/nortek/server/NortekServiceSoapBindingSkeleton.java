/**
 * NortekServiceSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.campus.webservice.nortek.server;

public class NortekServiceSoapBindingSkeleton implements is.idega.idegaweb.campus.webservice.nortek.server.NortekService, org.apache.axis.wsdl.Skeleton {
    private is.idega.idegaweb.campus.webservice.nortek.server.NortekService impl;
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
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "cardSerialNumber"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("isCardValid", _params, new javax.xml.namespace.QName("", "isCardValidReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:nortek", "isCardValid"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("isCardValid") == null) {
            _myOperations.put("isCardValid", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("isCardValid")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "cardSerialNumber"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("banCard", _params, new javax.xml.namespace.QName("", "banCardReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:nortek", "banCard"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("banCard") == null) {
            _myOperations.put("banCard", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("banCard")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "cardSerialNumber"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "timestamp"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "amount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"), double.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("addAmountToCard", _params, new javax.xml.namespace.QName("", "addAmountToCardReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:nortek", "addAmountToCard"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("addAmountToCard") == null) {
            _myOperations.put("addAmountToCard", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("addAmountToCard")).add(_oper);
    }

    public NortekServiceSoapBindingSkeleton() {
        this.impl = new is.idega.idegaweb.campus.webservice.nortek.server.NortekServiceSoapBindingImpl();
    }

    public NortekServiceSoapBindingSkeleton(is.idega.idegaweb.campus.webservice.nortek.server.NortekService impl) {
        this.impl = impl;
    }
    public boolean isCardValid(java.lang.String cardSerialNumber) throws java.rmi.RemoteException
    {
        boolean ret = impl.isCardValid(cardSerialNumber);
        return ret;
    }

    public boolean banCard(java.lang.String cardSerialNumber) throws java.rmi.RemoteException
    {
        boolean ret = impl.banCard(cardSerialNumber);
        return ret;
    }

    public boolean addAmountToCard(java.lang.String cardSerialNumber, java.util.Calendar timestamp, double amount, java.lang.String terminal) throws java.rmi.RemoteException
    {
        boolean ret = impl.addAmountToCard(cardSerialNumber, timestamp, amount, terminal);
        return ret;
    }

}
