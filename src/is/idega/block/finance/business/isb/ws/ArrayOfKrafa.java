/**
 * ArrayOfKrafa.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package is.idega.block.finance.business.isb.ws;

public class ArrayOfKrafa  implements java.io.Serializable {
    private is.idega.block.finance.business.isb.ws.Krafa[] krafa;

    public ArrayOfKrafa() {
    }

    public is.idega.block.finance.business.isb.ws.Krafa[] getKrafa() {
        return krafa;
    }

    public void setKrafa(is.idega.block.finance.business.isb.ws.Krafa[] krafa) {
        this.krafa = krafa;
    }

    public is.idega.block.finance.business.isb.ws.Krafa getKrafa(int i) {
        return krafa[i];
    }

    public void setKrafa(int i, is.idega.block.finance.business.isb.ws.Krafa value) {
        this.krafa[i] = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArrayOfKrafa)) return false;
        ArrayOfKrafa other = (ArrayOfKrafa) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.krafa==null && other.getKrafa()==null) || 
             (this.krafa!=null &&
              java.util.Arrays.equals(this.krafa, other.getKrafa())));
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
        if (getKrafa() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getKrafa());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getKrafa(), i);
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
        new org.apache.axis.description.TypeDesc(ArrayOfKrafa.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfKrafa"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("krafa");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "ns1:Krafa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ws.isb.is", "Krafa"));
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
