/**
 * UppreiknudKrafa.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package is.idega.block.finance.business.isb.ws;

public class UppreiknudKrafa  extends is.idega.block.finance.business.isb.ws.Krafa  implements java.io.Serializable {
    private java.math.BigDecimal eftirstodvar;
    private boolean skuldfaerist;
    private java.math.BigDecimal ogreittTilkynningargjald;
    private java.math.BigDecimal ogreiddirDrattarvextir;
    private java.math.BigDecimal uppaedTilGreidsluIDag;
    private java.math.BigDecimal tilkynningargjaldTilGreidsluIDag;
    private java.math.BigDecimal vanskilagjaldTilGreidsluIDag;
    private java.math.BigDecimal drattarvextirTilGreidsluIDag;
    private java.lang.String villubod;
    private is.idega.block.finance.business.isb.ws.StadaKrofu stada;
    private is.idega.block.finance.business.isb.ws.PrentunKrofu prentun;

    public UppreiknudKrafa() {
    }

    public java.math.BigDecimal getEftirstodvar() {
        return eftirstodvar;
    }

    public void setEftirstodvar(java.math.BigDecimal eftirstodvar) {
        this.eftirstodvar = eftirstodvar;
    }

    public boolean isSkuldfaerist() {
        return skuldfaerist;
    }

    public void setSkuldfaerist(boolean skuldfaerist) {
        this.skuldfaerist = skuldfaerist;
    }

    public java.math.BigDecimal getOgreittTilkynningargjald() {
        return ogreittTilkynningargjald;
    }

    public void setOgreittTilkynningargjald(java.math.BigDecimal ogreittTilkynningargjald) {
        this.ogreittTilkynningargjald = ogreittTilkynningargjald;
    }

    public java.math.BigDecimal getOgreiddirDrattarvextir() {
        return ogreiddirDrattarvextir;
    }

    public void setOgreiddirDrattarvextir(java.math.BigDecimal ogreiddirDrattarvextir) {
        this.ogreiddirDrattarvextir = ogreiddirDrattarvextir;
    }

    public java.math.BigDecimal getUppaedTilGreidsluIDag() {
        return uppaedTilGreidsluIDag;
    }

    public void setUppaedTilGreidsluIDag(java.math.BigDecimal uppaedTilGreidsluIDag) {
        this.uppaedTilGreidsluIDag = uppaedTilGreidsluIDag;
    }

    public java.math.BigDecimal getTilkynningargjaldTilGreidsluIDag() {
        return tilkynningargjaldTilGreidsluIDag;
    }

    public void setTilkynningargjaldTilGreidsluIDag(java.math.BigDecimal tilkynningargjaldTilGreidsluIDag) {
        this.tilkynningargjaldTilGreidsluIDag = tilkynningargjaldTilGreidsluIDag;
    }

    public java.math.BigDecimal getVanskilagjaldTilGreidsluIDag() {
        return vanskilagjaldTilGreidsluIDag;
    }

    public void setVanskilagjaldTilGreidsluIDag(java.math.BigDecimal vanskilagjaldTilGreidsluIDag) {
        this.vanskilagjaldTilGreidsluIDag = vanskilagjaldTilGreidsluIDag;
    }

    public java.math.BigDecimal getDrattarvextirTilGreidsluIDag() {
        return drattarvextirTilGreidsluIDag;
    }

    public void setDrattarvextirTilGreidsluIDag(java.math.BigDecimal drattarvextirTilGreidsluIDag) {
        this.drattarvextirTilGreidsluIDag = drattarvextirTilGreidsluIDag;
    }

    public java.lang.String getVillubod() {
        return villubod;
    }

    public void setVillubod(java.lang.String villubod) {
        this.villubod = villubod;
    }

    public is.idega.block.finance.business.isb.ws.StadaKrofu getStada() {
        return stada;
    }

    public void setStada(is.idega.block.finance.business.isb.ws.StadaKrofu stada) {
        this.stada = stada;
    }

    public is.idega.block.finance.business.isb.ws.PrentunKrofu getPrentun() {
        return prentun;
    }

    public void setPrentun(is.idega.block.finance.business.isb.ws.PrentunKrofu prentun) {
        this.prentun = prentun;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UppreiknudKrafa)) return false;
        UppreiknudKrafa other = (UppreiknudKrafa) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.eftirstodvar==null && other.getEftirstodvar()==null) || 
             (this.eftirstodvar!=null &&
              this.eftirstodvar.equals(other.getEftirstodvar()))) &&
            this.skuldfaerist == other.isSkuldfaerist() &&
            ((this.ogreittTilkynningargjald==null && other.getOgreittTilkynningargjald()==null) || 
             (this.ogreittTilkynningargjald!=null &&
              this.ogreittTilkynningargjald.equals(other.getOgreittTilkynningargjald()))) &&
            ((this.ogreiddirDrattarvextir==null && other.getOgreiddirDrattarvextir()==null) || 
             (this.ogreiddirDrattarvextir!=null &&
              this.ogreiddirDrattarvextir.equals(other.getOgreiddirDrattarvextir()))) &&
            ((this.uppaedTilGreidsluIDag==null && other.getUppaedTilGreidsluIDag()==null) || 
             (this.uppaedTilGreidsluIDag!=null &&
              this.uppaedTilGreidsluIDag.equals(other.getUppaedTilGreidsluIDag()))) &&
            ((this.tilkynningargjaldTilGreidsluIDag==null && other.getTilkynningargjaldTilGreidsluIDag()==null) || 
             (this.tilkynningargjaldTilGreidsluIDag!=null &&
              this.tilkynningargjaldTilGreidsluIDag.equals(other.getTilkynningargjaldTilGreidsluIDag()))) &&
            ((this.vanskilagjaldTilGreidsluIDag==null && other.getVanskilagjaldTilGreidsluIDag()==null) || 
             (this.vanskilagjaldTilGreidsluIDag!=null &&
              this.vanskilagjaldTilGreidsluIDag.equals(other.getVanskilagjaldTilGreidsluIDag()))) &&
            ((this.drattarvextirTilGreidsluIDag==null && other.getDrattarvextirTilGreidsluIDag()==null) || 
             (this.drattarvextirTilGreidsluIDag!=null &&
              this.drattarvextirTilGreidsluIDag.equals(other.getDrattarvextirTilGreidsluIDag()))) &&
            ((this.villubod==null && other.getVillubod()==null) || 
             (this.villubod!=null &&
              this.villubod.equals(other.getVillubod()))) &&
            ((this.stada==null && other.getStada()==null) || 
             (this.stada!=null &&
              this.stada.equals(other.getStada()))) &&
            ((this.prentun==null && other.getPrentun()==null) || 
             (this.prentun!=null &&
              this.prentun.equals(other.getPrentun())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getEftirstodvar() != null) {
            _hashCode += getEftirstodvar().hashCode();
        }
        _hashCode += new Boolean(isSkuldfaerist()).hashCode();
        if (getOgreittTilkynningargjald() != null) {
            _hashCode += getOgreittTilkynningargjald().hashCode();
        }
        if (getOgreiddirDrattarvextir() != null) {
            _hashCode += getOgreiddirDrattarvextir().hashCode();
        }
        if (getUppaedTilGreidsluIDag() != null) {
            _hashCode += getUppaedTilGreidsluIDag().hashCode();
        }
        if (getTilkynningargjaldTilGreidsluIDag() != null) {
            _hashCode += getTilkynningargjaldTilGreidsluIDag().hashCode();
        }
        if (getVanskilagjaldTilGreidsluIDag() != null) {
            _hashCode += getVanskilagjaldTilGreidsluIDag().hashCode();
        }
        if (getDrattarvextirTilGreidsluIDag() != null) {
            _hashCode += getDrattarvextirTilGreidsluIDag().hashCode();
        }
        if (getVillubod() != null) {
            _hashCode += getVillubod().hashCode();
        }
        if (getStada() != null) {
            _hashCode += getStada().hashCode();
        }
        if (getPrentun() != null) {
            _hashCode += getPrentun().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UppreiknudKrafa.class);

    static {
//        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ws.isb.is", "UppreiknudKrafa"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("eftirstodvar");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "ns1:Eftirstodvar"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("skuldfaerist");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "ns1:Skuldfaerist"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ogreittTilkynningargjald");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "ns1:OgreittTilkynningargjald"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ogreiddirDrattarvextir");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "ns1:OgreiddirDrattarvextir"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("uppaedTilGreidsluIDag");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "ns1:UppaedTilGreidsluIDag"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tilkynningargjaldTilGreidsluIDag");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "ns1:TilkynningargjaldTilGreidsluIDag"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vanskilagjaldTilGreidsluIDag");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "ns1:VanskilagjaldTilGreidsluIDag"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("drattarvextirTilGreidsluIDag");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "ns1:DrattarvextirTilGreidsluIDag"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("villubod");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "ns1:Villubod"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stada");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "ns1:Stada"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ws.isb.is", "StadaKrofu"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("prentun");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "ns1:Prentun"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ws.isb.is", "PrentunKrofu"));
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
