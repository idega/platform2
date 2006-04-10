/**
 * Beingreidslubeidni.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.block.finance.business.isb.ws2;

public class Beingreidslubeidni  implements java.io.Serializable {
    private java.lang.String textalykill;

    private java.lang.String vidskiptanumer;

    private java.lang.String kennitalaKrofuhafa;

    private java.lang.String kennitalaGreidanda;

    private int bankiKrofuhafa;

    private java.util.Calendar gildirFra;

    private java.util.Calendar gildirTil;

    private is.idega.block.finance.business.isb.ws2.BeingreidslubeidniStada stada;

    private java.util.Calendar dagsetningStodu;

    public Beingreidslubeidni() {
    }

    public Beingreidslubeidni(
           java.lang.String textalykill,
           java.lang.String vidskiptanumer,
           java.lang.String kennitalaKrofuhafa,
           java.lang.String kennitalaGreidanda,
           int bankiKrofuhafa,
           java.util.Calendar gildirFra,
           java.util.Calendar gildirTil,
           is.idega.block.finance.business.isb.ws2.BeingreidslubeidniStada stada,
           java.util.Calendar dagsetningStodu) {
           this.textalykill = textalykill;
           this.vidskiptanumer = vidskiptanumer;
           this.kennitalaKrofuhafa = kennitalaKrofuhafa;
           this.kennitalaGreidanda = kennitalaGreidanda;
           this.bankiKrofuhafa = bankiKrofuhafa;
           this.gildirFra = gildirFra;
           this.gildirTil = gildirTil;
           this.stada = stada;
           this.dagsetningStodu = dagsetningStodu;
    }


    /**
     * Gets the textalykill value for this Beingreidslubeidni.
     * 
     * @return textalykill
     */
    public java.lang.String getTextalykill() {
        return textalykill;
    }


    /**
     * Sets the textalykill value for this Beingreidslubeidni.
     * 
     * @param textalykill
     */
    public void setTextalykill(java.lang.String textalykill) {
        this.textalykill = textalykill;
    }


    /**
     * Gets the vidskiptanumer value for this Beingreidslubeidni.
     * 
     * @return vidskiptanumer
     */
    public java.lang.String getVidskiptanumer() {
        return vidskiptanumer;
    }


    /**
     * Sets the vidskiptanumer value for this Beingreidslubeidni.
     * 
     * @param vidskiptanumer
     */
    public void setVidskiptanumer(java.lang.String vidskiptanumer) {
        this.vidskiptanumer = vidskiptanumer;
    }


    /**
     * Gets the kennitalaKrofuhafa value for this Beingreidslubeidni.
     * 
     * @return kennitalaKrofuhafa
     */
    public java.lang.String getKennitalaKrofuhafa() {
        return kennitalaKrofuhafa;
    }


    /**
     * Sets the kennitalaKrofuhafa value for this Beingreidslubeidni.
     * 
     * @param kennitalaKrofuhafa
     */
    public void setKennitalaKrofuhafa(java.lang.String kennitalaKrofuhafa) {
        this.kennitalaKrofuhafa = kennitalaKrofuhafa;
    }


    /**
     * Gets the kennitalaGreidanda value for this Beingreidslubeidni.
     * 
     * @return kennitalaGreidanda
     */
    public java.lang.String getKennitalaGreidanda() {
        return kennitalaGreidanda;
    }


    /**
     * Sets the kennitalaGreidanda value for this Beingreidslubeidni.
     * 
     * @param kennitalaGreidanda
     */
    public void setKennitalaGreidanda(java.lang.String kennitalaGreidanda) {
        this.kennitalaGreidanda = kennitalaGreidanda;
    }


    /**
     * Gets the bankiKrofuhafa value for this Beingreidslubeidni.
     * 
     * @return bankiKrofuhafa
     */
    public int getBankiKrofuhafa() {
        return bankiKrofuhafa;
    }


    /**
     * Sets the bankiKrofuhafa value for this Beingreidslubeidni.
     * 
     * @param bankiKrofuhafa
     */
    public void setBankiKrofuhafa(int bankiKrofuhafa) {
        this.bankiKrofuhafa = bankiKrofuhafa;
    }


    /**
     * Gets the gildirFra value for this Beingreidslubeidni.
     * 
     * @return gildirFra
     */
    public java.util.Calendar getGildirFra() {
        return gildirFra;
    }


    /**
     * Sets the gildirFra value for this Beingreidslubeidni.
     * 
     * @param gildirFra
     */
    public void setGildirFra(java.util.Calendar gildirFra) {
        this.gildirFra = gildirFra;
    }


    /**
     * Gets the gildirTil value for this Beingreidslubeidni.
     * 
     * @return gildirTil
     */
    public java.util.Calendar getGildirTil() {
        return gildirTil;
    }


    /**
     * Sets the gildirTil value for this Beingreidslubeidni.
     * 
     * @param gildirTil
     */
    public void setGildirTil(java.util.Calendar gildirTil) {
        this.gildirTil = gildirTil;
    }


    /**
     * Gets the stada value for this Beingreidslubeidni.
     * 
     * @return stada
     */
    public is.idega.block.finance.business.isb.ws2.BeingreidslubeidniStada getStada() {
        return stada;
    }


    /**
     * Sets the stada value for this Beingreidslubeidni.
     * 
     * @param stada
     */
    public void setStada(is.idega.block.finance.business.isb.ws2.BeingreidslubeidniStada stada) {
        this.stada = stada;
    }


    /**
     * Gets the dagsetningStodu value for this Beingreidslubeidni.
     * 
     * @return dagsetningStodu
     */
    public java.util.Calendar getDagsetningStodu() {
        return dagsetningStodu;
    }


    /**
     * Sets the dagsetningStodu value for this Beingreidslubeidni.
     * 
     * @param dagsetningStodu
     */
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
        new org.apache.axis.description.TypeDesc(Beingreidslubeidni.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ws.isb.is", "Beingreidslubeidni"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("textalykill");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Textalykill"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vidskiptanumer");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Vidskiptanumer"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("kennitalaKrofuhafa");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "KennitalaKrofuhafa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("kennitalaGreidanda");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "KennitalaGreidanda"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bankiKrofuhafa");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "BankiKrofuhafa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gildirFra");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "GildirFra"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gildirTil");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "GildirTil"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stada");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Stada"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ws.isb.is", "BeingreidslubeidniStada"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dagsetningStodu");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "DagsetningStodu"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
