/**
 * StadaKrofu.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.block.finance.business.isb.ws2;

public class StadaKrofu implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected StadaKrofu(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    /*public static final java.lang.String _ÓGREIDD = "ÓGREIDD";
    public static final java.lang.String _GREIDD = "GREIDD";
    public static final java.lang.String _MILLINNHEIMTA = "MILLINNHEIMTA";
    public static final java.lang.String _LÖGFRÆÐIINNHEIMTA = "LÖGFRÆÐIINNHEIMTA";
    public static final java.lang.String _NIÐURFELLD = "NIÐURFELLD";
    public static final java.lang.String _VILLA = "VILLA";*/
    public static final java.lang.String _OGREIDD = "&Oacute;GREIDD";//"ÓGREIDD";
    public static final java.lang.String _GREIDD = "GREIDD";
    public static final java.lang.String _NIDURFELLD = "NI&ETH;URFELLD";//"NIÐURFELLD";
    public static final java.lang.String _MILLINNHEIMTA = "MILLINNHEIMTA";
    public static final java.lang.String _LOGFRAEDIINNHEIMTA = "L&Ouml;GFR&AElig;&ETH;IINNHEIMTA";//"LÖGFRÆÐIINNHEIMTA";
    public static final java.lang.String _VILLA = "VILLA";

    public static final StadaKrofu OGREIDD = new StadaKrofu(_OGREIDD);
    public static final StadaKrofu GREIDD = new StadaKrofu(_GREIDD);
    public static final StadaKrofu MILLINNHEIMTA = new StadaKrofu(_MILLINNHEIMTA);
    public static final StadaKrofu LOGFRAEDIINNHEIMTA = new StadaKrofu(_LOGFRAEDIINNHEIMTA);
    public static final StadaKrofu NIDURFELLD = new StadaKrofu(_NIDURFELLD);
    public static final StadaKrofu VILLA = new StadaKrofu(_VILLA);
    public java.lang.String getValue() { return _value_;}
    public static StadaKrofu fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        StadaKrofu enumeration = (StadaKrofu)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static StadaKrofu fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(StadaKrofu.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ws.isb.is", "StadaKrofu"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
