/**
 * ArrayOfKrofulidur.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package is.idega.block.finance.business.isb.ws;

public class ArrayOfKrofulidur  implements java.io.Serializable {
    private is.idega.block.finance.business.isb.ws.Krofulidur[] krofulidur;

    public ArrayOfKrofulidur() {
    }

    public is.idega.block.finance.business.isb.ws.Krofulidur[] getKrofulidur() {
        return krofulidur;
    }

    public void setKrofulidur(is.idega.block.finance.business.isb.ws.Krofulidur[] krofulidur) {
        this.krofulidur = krofulidur;
    }

    public is.idega.block.finance.business.isb.ws.Krofulidur getKrofulidur(int i) {
        return krofulidur[i];
    }

    public void setKrofulidur(int i, is.idega.block.finance.business.isb.ws.Krofulidur value) {
        this.krofulidur[i] = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArrayOfKrofulidur)) return false;
        ArrayOfKrofulidur other = (ArrayOfKrofulidur) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.krofulidur==null && other.getKrofulidur()==null) || 
             (this.krofulidur!=null &&
              java.util.Arrays.equals(this.krofulidur, other.getKrofulidur())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getKrofulidur() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getKrofulidur());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getKrofulidur(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ArrayOfKrofulidur.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfKrofulidur"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("krofulidur");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Krofulidur"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ws.isb.is", "Krofulidur"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}