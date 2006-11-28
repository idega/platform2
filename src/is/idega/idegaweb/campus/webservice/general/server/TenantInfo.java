/**
 * TenantInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.campus.webservice.general.server;

public class TenantInfo  implements java.io.Serializable {
    private java.lang.String apartmentNumber;

    private java.lang.String buildingName;

    private java.util.Calendar movedInDate;

    private java.lang.String personalID;

    public TenantInfo() {
    }

    public TenantInfo(
           java.lang.String apartmentNumber,
           java.lang.String buildingName,
           java.util.Calendar movedInDate,
           java.lang.String personalID) {
           this.apartmentNumber = apartmentNumber;
           this.buildingName = buildingName;
           this.movedInDate = movedInDate;
           this.personalID = personalID;
    }


    /**
     * Gets the apartmentNumber value for this TenantInfo.
     * 
     * @return apartmentNumber
     */
    public java.lang.String getApartmentNumber() {
        return apartmentNumber;
    }


    /**
     * Sets the apartmentNumber value for this TenantInfo.
     * 
     * @param apartmentNumber
     */
    public void setApartmentNumber(java.lang.String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }


    /**
     * Gets the buildingName value for this TenantInfo.
     * 
     * @return buildingName
     */
    public java.lang.String getBuildingName() {
        return buildingName;
    }


    /**
     * Sets the buildingName value for this TenantInfo.
     * 
     * @param buildingName
     */
    public void setBuildingName(java.lang.String buildingName) {
        this.buildingName = buildingName;
    }


    /**
     * Gets the movedInDate value for this TenantInfo.
     * 
     * @return movedInDate
     */
    public java.util.Calendar getMovedInDate() {
        return movedInDate;
    }


    /**
     * Sets the movedInDate value for this TenantInfo.
     * 
     * @param movedInDate
     */
    public void setMovedInDate(java.util.Calendar movedInDate) {
        this.movedInDate = movedInDate;
    }


    /**
     * Gets the personalID value for this TenantInfo.
     * 
     * @return personalID
     */
    public java.lang.String getPersonalID() {
        return personalID;
    }


    /**
     * Sets the personalID value for this TenantInfo.
     * 
     * @param personalID
     */
    public void setPersonalID(java.lang.String personalID) {
        this.personalID = personalID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TenantInfo)) return false;
        TenantInfo other = (TenantInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.apartmentNumber==null && other.getApartmentNumber()==null) || 
             (this.apartmentNumber!=null &&
              this.apartmentNumber.equals(other.getApartmentNumber()))) &&
            ((this.buildingName==null && other.getBuildingName()==null) || 
             (this.buildingName!=null &&
              this.buildingName.equals(other.getBuildingName()))) &&
            ((this.movedInDate==null && other.getMovedInDate()==null) || 
             (this.movedInDate!=null &&
              this.movedInDate.equals(other.getMovedInDate()))) &&
            ((this.personalID==null && other.getPersonalID()==null) || 
             (this.personalID!=null &&
              this.personalID.equals(other.getPersonalID())));
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
        if (getApartmentNumber() != null) {
            _hashCode += getApartmentNumber().hashCode();
        }
        if (getBuildingName() != null) {
            _hashCode += getBuildingName().hashCode();
        }
        if (getMovedInDate() != null) {
            _hashCode += getMovedInDate().hashCode();
        }
        if (getPersonalID() != null) {
            _hashCode += getPersonalID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TenantInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:campus", "TenantInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("apartmentNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("", "apartmentNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("buildingName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "buildingName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("movedInDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "movedInDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("personalID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "personalID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
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
