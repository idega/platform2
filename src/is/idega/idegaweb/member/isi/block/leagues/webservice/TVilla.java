/**
 * TVilla.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.member.isi.block.leagues.webservice;

public class TVilla  implements java.io.Serializable {
    private int iVilla;
    private java.lang.String sVilla_texti;

    public TVilla() {
    }

    public TVilla(
           int iVilla,
           java.lang.String sVilla_texti) {
           this.iVilla = iVilla;
           this.sVilla_texti = sVilla_texti;
    }


    /**
     * Gets the iVilla value for this TVilla.
     * 
     * @return iVilla
     */
    public int getIVilla() {
        return iVilla;
    }


    /**
     * Sets the iVilla value for this TVilla.
     * 
     * @param iVilla
     */
    public void setIVilla(int iVilla) {
        this.iVilla = iVilla;
    }


    /**
     * Gets the sVilla_texti value for this TVilla.
     * 
     * @return sVilla_texti
     */
    public java.lang.String getSVilla_texti() {
        return sVilla_texti;
    }


    /**
     * Sets the sVilla_texti value for this TVilla.
     * 
     * @param sVilla_texti
     */
    public void setSVilla_texti(java.lang.String sVilla_texti) {
        this.sVilla_texti = sVilla_texti;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TVilla)) return false;
        TVilla other = (TVilla) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.iVilla == other.getIVilla() &&
            ((this.sVilla_texti==null && other.getSVilla_texti()==null) || 
             (this.sVilla_texti!=null &&
              this.sVilla_texti.equals(other.getSVilla_texti())));
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
        _hashCode += getIVilla();
        if (getSVilla_texti() != null) {
            _hashCode += getSVilla_texti().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TVilla.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", "tVilla"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("IVilla");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "iVilla"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SVilla_texti");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "sVilla_texti"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
