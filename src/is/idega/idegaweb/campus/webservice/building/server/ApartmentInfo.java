/**
 * ApartmentInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.campus.webservice.building.server;

public class ApartmentInfo  implements java.io.Serializable {
    private java.lang.String category;

    private double collectiveFee;

    private double electricity;

    private java.lang.String floor;

    private boolean furnished;

    private boolean hasAttic;

    private boolean hasBathroom;

    private boolean hasKitchen;

    private boolean hasStorageroom;

    private boolean hasStudyroom;

    private double heat;

    private int id;

    private java.lang.String name;

    private int numberOfRooms;

    private double rent;

    private java.lang.String serialNumber;

    private double size;

    private java.lang.String subcategory;

    private java.lang.String type;

    private java.lang.String typeEnglishInfo;

    private java.lang.String typeInfo;

    private java.lang.String typeShortName;

    public ApartmentInfo() {
    }

    public ApartmentInfo(
           java.lang.String category,
           double collectiveFee,
           double electricity,
           java.lang.String floor,
           boolean furnished,
           boolean hasAttic,
           boolean hasBathroom,
           boolean hasKitchen,
           boolean hasStorageroom,
           boolean hasStudyroom,
           double heat,
           int id,
           java.lang.String name,
           int numberOfRooms,
           double rent,
           java.lang.String serialNumber,
           double size,
           java.lang.String subcategory,
           java.lang.String type,
           java.lang.String typeEnglishInfo,
           java.lang.String typeInfo,
           java.lang.String typeShortName) {
           this.category = category;
           this.collectiveFee = collectiveFee;
           this.electricity = electricity;
           this.floor = floor;
           this.furnished = furnished;
           this.hasAttic = hasAttic;
           this.hasBathroom = hasBathroom;
           this.hasKitchen = hasKitchen;
           this.hasStorageroom = hasStorageroom;
           this.hasStudyroom = hasStudyroom;
           this.heat = heat;
           this.id = id;
           this.name = name;
           this.numberOfRooms = numberOfRooms;
           this.rent = rent;
           this.serialNumber = serialNumber;
           this.size = size;
           this.subcategory = subcategory;
           this.type = type;
           this.typeEnglishInfo = typeEnglishInfo;
           this.typeInfo = typeInfo;
           this.typeShortName = typeShortName;
    }


    /**
     * Gets the category value for this ApartmentInfo.
     * 
     * @return category
     */
    public java.lang.String getCategory() {
        return category;
    }


    /**
     * Sets the category value for this ApartmentInfo.
     * 
     * @param category
     */
    public void setCategory(java.lang.String category) {
        this.category = category;
    }


    /**
     * Gets the collectiveFee value for this ApartmentInfo.
     * 
     * @return collectiveFee
     */
    public double getCollectiveFee() {
        return collectiveFee;
    }


    /**
     * Sets the collectiveFee value for this ApartmentInfo.
     * 
     * @param collectiveFee
     */
    public void setCollectiveFee(double collectiveFee) {
        this.collectiveFee = collectiveFee;
    }


    /**
     * Gets the electricity value for this ApartmentInfo.
     * 
     * @return electricity
     */
    public double getElectricity() {
        return electricity;
    }


    /**
     * Sets the electricity value for this ApartmentInfo.
     * 
     * @param electricity
     */
    public void setElectricity(double electricity) {
        this.electricity = electricity;
    }


    /**
     * Gets the floor value for this ApartmentInfo.
     * 
     * @return floor
     */
    public java.lang.String getFloor() {
        return floor;
    }


    /**
     * Sets the floor value for this ApartmentInfo.
     * 
     * @param floor
     */
    public void setFloor(java.lang.String floor) {
        this.floor = floor;
    }


    /**
     * Gets the furnished value for this ApartmentInfo.
     * 
     * @return furnished
     */
    public boolean isFurnished() {
        return furnished;
    }


    /**
     * Sets the furnished value for this ApartmentInfo.
     * 
     * @param furnished
     */
    public void setFurnished(boolean furnished) {
        this.furnished = furnished;
    }


    /**
     * Gets the hasAttic value for this ApartmentInfo.
     * 
     * @return hasAttic
     */
    public boolean isHasAttic() {
        return hasAttic;
    }


    /**
     * Sets the hasAttic value for this ApartmentInfo.
     * 
     * @param hasAttic
     */
    public void setHasAttic(boolean hasAttic) {
        this.hasAttic = hasAttic;
    }


    /**
     * Gets the hasBathroom value for this ApartmentInfo.
     * 
     * @return hasBathroom
     */
    public boolean isHasBathroom() {
        return hasBathroom;
    }


    /**
     * Sets the hasBathroom value for this ApartmentInfo.
     * 
     * @param hasBathroom
     */
    public void setHasBathroom(boolean hasBathroom) {
        this.hasBathroom = hasBathroom;
    }


    /**
     * Gets the hasKitchen value for this ApartmentInfo.
     * 
     * @return hasKitchen
     */
    public boolean isHasKitchen() {
        return hasKitchen;
    }


    /**
     * Sets the hasKitchen value for this ApartmentInfo.
     * 
     * @param hasKitchen
     */
    public void setHasKitchen(boolean hasKitchen) {
        this.hasKitchen = hasKitchen;
    }


    /**
     * Gets the hasStorageroom value for this ApartmentInfo.
     * 
     * @return hasStorageroom
     */
    public boolean isHasStorageroom() {
        return hasStorageroom;
    }


    /**
     * Sets the hasStorageroom value for this ApartmentInfo.
     * 
     * @param hasStorageroom
     */
    public void setHasStorageroom(boolean hasStorageroom) {
        this.hasStorageroom = hasStorageroom;
    }


    /**
     * Gets the hasStudyroom value for this ApartmentInfo.
     * 
     * @return hasStudyroom
     */
    public boolean isHasStudyroom() {
        return hasStudyroom;
    }


    /**
     * Sets the hasStudyroom value for this ApartmentInfo.
     * 
     * @param hasStudyroom
     */
    public void setHasStudyroom(boolean hasStudyroom) {
        this.hasStudyroom = hasStudyroom;
    }


    /**
     * Gets the heat value for this ApartmentInfo.
     * 
     * @return heat
     */
    public double getHeat() {
        return heat;
    }


    /**
     * Sets the heat value for this ApartmentInfo.
     * 
     * @param heat
     */
    public void setHeat(double heat) {
        this.heat = heat;
    }


    /**
     * Gets the id value for this ApartmentInfo.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }


    /**
     * Sets the id value for this ApartmentInfo.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Gets the name value for this ApartmentInfo.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this ApartmentInfo.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the numberOfRooms value for this ApartmentInfo.
     * 
     * @return numberOfRooms
     */
    public int getNumberOfRooms() {
        return numberOfRooms;
    }


    /**
     * Sets the numberOfRooms value for this ApartmentInfo.
     * 
     * @param numberOfRooms
     */
    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }


    /**
     * Gets the rent value for this ApartmentInfo.
     * 
     * @return rent
     */
    public double getRent() {
        return rent;
    }


    /**
     * Sets the rent value for this ApartmentInfo.
     * 
     * @param rent
     */
    public void setRent(double rent) {
        this.rent = rent;
    }


    /**
     * Gets the serialNumber value for this ApartmentInfo.
     * 
     * @return serialNumber
     */
    public java.lang.String getSerialNumber() {
        return serialNumber;
    }


    /**
     * Sets the serialNumber value for this ApartmentInfo.
     * 
     * @param serialNumber
     */
    public void setSerialNumber(java.lang.String serialNumber) {
        this.serialNumber = serialNumber;
    }


    /**
     * Gets the size value for this ApartmentInfo.
     * 
     * @return size
     */
    public double getSize() {
        return size;
    }


    /**
     * Sets the size value for this ApartmentInfo.
     * 
     * @param size
     */
    public void setSize(double size) {
        this.size = size;
    }


    /**
     * Gets the subcategory value for this ApartmentInfo.
     * 
     * @return subcategory
     */
    public java.lang.String getSubcategory() {
        return subcategory;
    }


    /**
     * Sets the subcategory value for this ApartmentInfo.
     * 
     * @param subcategory
     */
    public void setSubcategory(java.lang.String subcategory) {
        this.subcategory = subcategory;
    }


    /**
     * Gets the type value for this ApartmentInfo.
     * 
     * @return type
     */
    public java.lang.String getType() {
        return type;
    }


    /**
     * Sets the type value for this ApartmentInfo.
     * 
     * @param type
     */
    public void setType(java.lang.String type) {
        this.type = type;
    }


    /**
     * Gets the typeEnglishInfo value for this ApartmentInfo.
     * 
     * @return typeEnglishInfo
     */
    public java.lang.String getTypeEnglishInfo() {
        return typeEnglishInfo;
    }


    /**
     * Sets the typeEnglishInfo value for this ApartmentInfo.
     * 
     * @param typeEnglishInfo
     */
    public void setTypeEnglishInfo(java.lang.String typeEnglishInfo) {
        this.typeEnglishInfo = typeEnglishInfo;
    }


    /**
     * Gets the typeInfo value for this ApartmentInfo.
     * 
     * @return typeInfo
     */
    public java.lang.String getTypeInfo() {
        return typeInfo;
    }


    /**
     * Sets the typeInfo value for this ApartmentInfo.
     * 
     * @param typeInfo
     */
    public void setTypeInfo(java.lang.String typeInfo) {
        this.typeInfo = typeInfo;
    }


    /**
     * Gets the typeShortName value for this ApartmentInfo.
     * 
     * @return typeShortName
     */
    public java.lang.String getTypeShortName() {
        return typeShortName;
    }


    /**
     * Sets the typeShortName value for this ApartmentInfo.
     * 
     * @param typeShortName
     */
    public void setTypeShortName(java.lang.String typeShortName) {
        this.typeShortName = typeShortName;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ApartmentInfo)) return false;
        ApartmentInfo other = (ApartmentInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.category==null && other.getCategory()==null) || 
             (this.category!=null &&
              this.category.equals(other.getCategory()))) &&
            this.collectiveFee == other.getCollectiveFee() &&
            this.electricity == other.getElectricity() &&
            ((this.floor==null && other.getFloor()==null) || 
             (this.floor!=null &&
              this.floor.equals(other.getFloor()))) &&
            this.furnished == other.isFurnished() &&
            this.hasAttic == other.isHasAttic() &&
            this.hasBathroom == other.isHasBathroom() &&
            this.hasKitchen == other.isHasKitchen() &&
            this.hasStorageroom == other.isHasStorageroom() &&
            this.hasStudyroom == other.isHasStudyroom() &&
            this.heat == other.getHeat() &&
            this.id == other.getId() &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            this.numberOfRooms == other.getNumberOfRooms() &&
            this.rent == other.getRent() &&
            ((this.serialNumber==null && other.getSerialNumber()==null) || 
             (this.serialNumber!=null &&
              this.serialNumber.equals(other.getSerialNumber()))) &&
            this.size == other.getSize() &&
            ((this.subcategory==null && other.getSubcategory()==null) || 
             (this.subcategory!=null &&
              this.subcategory.equals(other.getSubcategory()))) &&
            ((this.type==null && other.getType()==null) || 
             (this.type!=null &&
              this.type.equals(other.getType()))) &&
            ((this.typeEnglishInfo==null && other.getTypeEnglishInfo()==null) || 
             (this.typeEnglishInfo!=null &&
              this.typeEnglishInfo.equals(other.getTypeEnglishInfo()))) &&
            ((this.typeInfo==null && other.getTypeInfo()==null) || 
             (this.typeInfo!=null &&
              this.typeInfo.equals(other.getTypeInfo()))) &&
            ((this.typeShortName==null && other.getTypeShortName()==null) || 
             (this.typeShortName!=null &&
              this.typeShortName.equals(other.getTypeShortName())));
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
        if (getCategory() != null) {
            _hashCode += getCategory().hashCode();
        }
        _hashCode += new Double(getCollectiveFee()).hashCode();
        _hashCode += new Double(getElectricity()).hashCode();
        if (getFloor() != null) {
            _hashCode += getFloor().hashCode();
        }
        _hashCode += (isFurnished() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isHasAttic() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isHasBathroom() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isHasKitchen() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isHasStorageroom() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isHasStudyroom() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += new Double(getHeat()).hashCode();
        _hashCode += getId();
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        _hashCode += getNumberOfRooms();
        _hashCode += new Double(getRent()).hashCode();
        if (getSerialNumber() != null) {
            _hashCode += getSerialNumber().hashCode();
        }
        _hashCode += new Double(getSize()).hashCode();
        if (getSubcategory() != null) {
            _hashCode += getSubcategory().hashCode();
        }
        if (getType() != null) {
            _hashCode += getType().hashCode();
        }
        if (getTypeEnglishInfo() != null) {
            _hashCode += getTypeEnglishInfo().hashCode();
        }
        if (getTypeInfo() != null) {
            _hashCode += getTypeInfo().hashCode();
        }
        if (getTypeShortName() != null) {
            _hashCode += getTypeShortName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ApartmentInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:buildingservice", "ApartmentInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("category");
        elemField.setXmlName(new javax.xml.namespace.QName("", "category"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("collectiveFee");
        elemField.setXmlName(new javax.xml.namespace.QName("", "collectiveFee"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("electricity");
        elemField.setXmlName(new javax.xml.namespace.QName("", "electricity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("floor");
        elemField.setXmlName(new javax.xml.namespace.QName("", "floor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("furnished");
        elemField.setXmlName(new javax.xml.namespace.QName("", "furnished"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hasAttic");
        elemField.setXmlName(new javax.xml.namespace.QName("", "hasAttic"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hasBathroom");
        elemField.setXmlName(new javax.xml.namespace.QName("", "hasBathroom"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hasKitchen");
        elemField.setXmlName(new javax.xml.namespace.QName("", "hasKitchen"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hasStorageroom");
        elemField.setXmlName(new javax.xml.namespace.QName("", "hasStorageroom"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hasStudyroom");
        elemField.setXmlName(new javax.xml.namespace.QName("", "hasStudyroom"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("heat");
        elemField.setXmlName(new javax.xml.namespace.QName("", "heat"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numberOfRooms");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numberOfRooms"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rent");
        elemField.setXmlName(new javax.xml.namespace.QName("", "rent"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serialNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("", "serialNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("size");
        elemField.setXmlName(new javax.xml.namespace.QName("", "size"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subcategory");
        elemField.setXmlName(new javax.xml.namespace.QName("", "subcategory"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type");
        elemField.setXmlName(new javax.xml.namespace.QName("", "type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("typeEnglishInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "typeEnglishInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("typeInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "typeInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("typeShortName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "typeShortName"));
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
