/**
 * Beingreidslubeidni.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package is.idega.block.finance.business.isb.ws;

public class Beingreidslubeidni  implements java.io.Serializable {
    private java.lang.String textalykill;
    private java.lang.String vidskiptanumer;
    private java.lang.String kennitalaKrofuhafa;
    private java.lang.String kennitalaGreidanda;
    private int bankiKrofuhafa;
    private java.util.Calendar gildirFra;
    private java.util.Calendar gildirTil;
    private is.idega.block.finance.business.isb.ws.BeingreidslubeidniStada stada;
    private java.util.Calendar dagsetningStodu;

    public Beingreidslubeidni() {
    }

    public java.lang.String getTextalykill() {
        return textalykill;
    }

    public void setTextalykill(java.lang.String textalykill) {
        this.textalykill = textalykill;
    }

    public java.lang.String getVidskiptanumer() {
        return vidskiptanumer;
    }

    public void setVidskiptanumer(java.lang.String vidskiptanumer) {
        this.vidskiptanumer = vidskiptanumer;
    }

    public java.lang.String getKennitalaKrofuhafa() {
        return kennitalaKrofuhafa;
    }

    public void setKennitalaKrofuhafa(java.lang.String kennitalaKrofuhafa) {
        this.kennitalaKrofuhafa = kennitalaKrofuhafa;
    }

    public java.lang.String getKennitalaGreidanda() {
        return kennitalaGreidanda;
    }

    public void setKennitalaGreidanda(java.lang.String kennitalaGreidanda) {
        this.kennitalaGreidanda = kennitalaGreidanda;
    }

    public int getBankiKrofuhafa() {
        return bankiKrofuhafa;
    }

    public void setBankiKrofuhafa(int bankiKrofuhafa) {
        this.bankiKrofuhafa = bankiKrofuhafa;
    }

    public java.util.Calendar getGildirFra() {
        return gildirFra;
    }

    public void setGildirFra(java.util.Calendar gildirFra) {
        this.gildirFra = gildirFra;
    }

    public java.util.Calendar getGildirTil() {
        return gildirTil;
    }

    public void setGildirTil(java.util.Calendar gildirTil) {
        this.gildirTil = gildirTil;
    }

    public is.idega.block.finance.business.isb.ws.BeingreidslubeidniStada getStada() {
        return stada;
    }

    public void setStada(is.idega.block.finance.business.isb.ws.BeingreidslubeidniStada stada) {
        this.stada = stada;
    }

    public java.util.Calendar getDagsetningStodu() {
        return dagsetningStodu;
    }

    public void setDagsetningStodu(java.util.Calendar dagsetningStodu) {
        this.dagsetningStodu = dagsetningStodu;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Beingreidslubeidni)) return false;
        Beingreidslubeidni other = (Beingreidslubeidni) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.textalykill==null && other.getTextalykill()==null) || 
             (this.textalykill!=null &&
              this.textalykill.equals(other.getTextalykill()))) &&
            ((this.vidskiptanumer==null && other.getVidskiptanumer()==null) || 
             (this.vidskiptanumer!=null &&
              this.vidskiptanumer.equals(other.getVidskiptanumer()))) &&
            ((this.kennitalaKrofuhafa==null && other.getKennitalaKrofuhafa()==null) || 
             (this.kennitalaKrofuhafa!=null &&
              this.kennitalaKrofuhafa.equals(other.getKennitalaKrofuhafa()))) &&
            ((this.kennitalaGreidanda==null && other.getKennitalaGreidanda()==null) || 
             (this.kennitalaGreidanda!=null &&
              this.kennitalaGreidanda.equals(other.getKennitalaGreidanda()))) &&
            this.bankiKrofuhafa == other.getBankiKrofuhafa() &&
            ((this.gildirFra==null && other.getGildirFra()==null) || 
             (this.gildirFra!=null &&
              this.gildirFra.equals(other.getGildirFra()))) &&
            ((this.gildirTil==null && other.getGildirTil()==null) || 
             (this.gildirTil!=null &&
              this.gildirTil.equals(other.getGildirTil()))) &&
            ((this.stada==null && other.getStada()==null) || 
             (this.stada!=null &&
              this.stada.equals(other.getStada()))) &&
            ((this.dagsetningStodu==null && other.getDagsetningStodu()==null) || 
             (this.dagsetningStodu!=null &&
              this.dagsetningStodu.equals(other.getDagsetningStodu())));
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
        if (getTextalykill() != null) {
            _hashCode += getTextalykill().hashCode();
        }
        if (getVidskiptanumer() != null) {
            _hashCode += getVidskiptanumer().hashCode();
        }
        if (getKennitalaKrofuhafa() != null) {
            _hashCode += getKennitalaKrofuhafa().hashCode();
        }
        if (getKennitalaGreidanda() != null) {
            _hashCode += getKennitalaGreidanda().hashCode();
        }
        _hashCode += getBankiKrofuhafa();
        if (getGildirFra() != null) {
            _hashCode += getGildirFra().hashCode();
        }
        if (getGildirTil() != null) {
            _hashCode += getGildirTil().hashCode();
        }
        if (getStada() != null) {
            _hashCode += getStada().hashCode();
        }
        if (getDagsetningStodu() != null) {
            _hashCode += getDagsetningStodu().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Beingreidslubeidni.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ws.isb.is", "Beingreidslubeidni"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("textalykill");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Textalykill"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vidskiptanumer");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Vidskiptanumer"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("kennitalaKrofuhafa");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "KennitalaKrofuhafa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("kennitalaGreidanda");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "KennitalaGreidanda"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bankiKrofuhafa");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "BankiKrofuhafa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gildirFra");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "GildirFra"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gildirTil");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "GildirTil"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stada");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Stada"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ws.isb.is", "BeingreidslubeidniStada"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dagsetningStodu");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "DagsetningStodu"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
