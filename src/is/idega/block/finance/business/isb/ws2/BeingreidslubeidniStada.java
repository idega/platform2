/**
 * BeingreidslubeidniStada.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.block.finance.business.isb.ws2;

public class BeingreidslubeidniStada implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected BeingreidslubeidniStada(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _VIRK = "VIRK";
    public static final java.lang.String _OVIRK = "OVIRK";
    public static final BeingreidslubeidniStada VIRK = new BeingreidslubeidniStada(_VIRK);
    public static final BeingreidslubeidniStada OVIRK = new BeingreidslubeidniStada(_OVIRK);
    public java.lang.String getValue() { return _value_;}
    public static BeingreidslubeidniStada fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        BeingreidslubeidniStada enumeration = (BeingreidslubeidniStada)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static BeingreidslubeidniStada fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BeingreidslubeidniStada.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ws.isb.is", "BeingreidslubeidniStada"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
