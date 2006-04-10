/**
 * UppreiknudKrafa.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.block.finance.business.isb.ws2;

public class UppreiknudKrafa  extends is.idega.block.finance.business.isb.ws2.Krafa  implements java.io.Serializable {
    private java.math.BigDecimal eftirstodvar;

    private boolean skuldfaerist;

    private java.math.BigDecimal ogreittTilkynningargjald;

    private java.math.BigDecimal ogreiddirDrattarvextir;

    private java.math.BigDecimal uppaedTilGreidsluIDag;

    private java.math.BigDecimal tilkynningargjaldTilGreidsluIDag;

    private java.math.BigDecimal vanskilagjaldTilGreidsluIDag;

    private java.math.BigDecimal drattarvextirTilGreidsluIDag;

    private java.lang.String villubod;

    private is.idega.block.finance.business.isb.ws2.StadaKrofu stada;

    private is.idega.block.finance.business.isb.ws2.PrentunKrofu prentun;

    public UppreiknudKrafa() {
    }

    public UppreiknudKrafa(
           java.lang.String faerslugerd,
           java.lang.String kennitalaKrofuhafa,
           java.util.Calendar gjalddagi,
           java.util.Calendar nidurfellingardagur,
           java.lang.String audkenni,
           java.lang.String kennitalaGreidanda,
           int bankanumer,
           int hofudbok,
           int krofunumer,
           java.math.BigDecimal upphaed,
           java.lang.String tilvisun,
           java.lang.String sedilnumer,
           java.lang.String vidskiptanumer,
           java.util.Calendar eindagi,
           java.math.BigDecimal tilkynningarOgGreidslugjald1,
           java.math.BigDecimal tilkynningarOgGreidslugjald2,
           java.math.BigDecimal vanskilagjald1,
           java.math.BigDecimal vanskilagjald2,
           int dagafjoldiVanskilagjalds1,
           int dagafjoldiVanskilagjalds2,
           java.lang.String vanskilagjaldsKodi,
           java.math.BigDecimal annarKostnadur,
           java.math.BigDecimal annarVanskilakostnadur,
           java.math.BigDecimal drattavaxtaprosenta,
           java.lang.String drattavaxtaregla,
           java.lang.String drattavaxtastofnkodi,
           java.lang.String gengistegund,
           java.lang.String mynt,
           int gengisbanki,
           java.lang.String gengiskodi,
           java.lang.String greidslukodi,
           java.math.BigDecimal afslattur1,
           java.math.BigDecimal afslattur2,
           int dagafjoldiAfslattar1,
           int dagafjoldiAfslattar2,
           java.lang.String afslattarkodi,
           java.lang.String innborgunarkodi,
           java.lang.String birtingakodi,
           java.lang.String vefslod,
           java.lang.String nafnGreidanda1,
           java.lang.String nafnGreidanda2,
           java.lang.String heimiliGreidanda,
           java.lang.String sveitarfelagGreidanda,
           java.lang.String athugasemdalina1,
           java.lang.String athugasemdalina2,
           is.idega.block.finance.business.isb.ws2.Krofulidur[] hreyfingar,
           java.math.BigDecimal eftirstodvar,
           boolean skuldfaerist,
           java.math.BigDecimal ogreittTilkynningargjald,
           java.math.BigDecimal ogreiddirDrattarvextir,
           java.math.BigDecimal uppaedTilGreidsluIDag,
           java.math.BigDecimal tilkynningargjaldTilGreidsluIDag,
           java.math.BigDecimal vanskilagjaldTilGreidsluIDag,
           java.math.BigDecimal drattarvextirTilGreidsluIDag,
           java.lang.String villubod,
           is.idega.block.finance.business.isb.ws2.StadaKrofu stada,
           is.idega.block.finance.business.isb.ws2.PrentunKrofu prentun) {
        super(
            faerslugerd,
            kennitalaKrofuhafa,
            gjalddagi,
            nidurfellingardagur,
            audkenni,
            kennitalaGreidanda,
            bankanumer,
            hofudbok,
            krofunumer,
            upphaed,
            tilvisun,
            sedilnumer,
            vidskiptanumer,
            eindagi,
            tilkynningarOgGreidslugjald1,
            tilkynningarOgGreidslugjald2,
            vanskilagjald1,
            vanskilagjald2,
            dagafjoldiVanskilagjalds1,
            dagafjoldiVanskilagjalds2,
            vanskilagjaldsKodi,
            annarKostnadur,
            annarVanskilakostnadur,
            drattavaxtaprosenta,
            drattavaxtaregla,
            drattavaxtastofnkodi,
            gengistegund,
            mynt,
            gengisbanki,
            gengiskodi,
            greidslukodi,
            afslattur1,
            afslattur2,
            dagafjoldiAfslattar1,
            dagafjoldiAfslattar2,
            afslattarkodi,
            innborgunarkodi,
            birtingakodi,
            vefslod,
            nafnGreidanda1,
            nafnGreidanda2,
            heimiliGreidanda,
            sveitarfelagGreidanda,
            athugasemdalina1,
            athugasemdalina2,
            hreyfingar);
        this.eftirstodvar = eftirstodvar;
        this.skuldfaerist = skuldfaerist;
        this.ogreittTilkynningargjald = ogreittTilkynningargjald;
        this.ogreiddirDrattarvextir = ogreiddirDrattarvextir;
        this.uppaedTilGreidsluIDag = uppaedTilGreidsluIDag;
        this.tilkynningargjaldTilGreidsluIDag = tilkynningargjaldTilGreidsluIDag;
        this.vanskilagjaldTilGreidsluIDag = vanskilagjaldTilGreidsluIDag;
        this.drattarvextirTilGreidsluIDag = drattarvextirTilGreidsluIDag;
        this.villubod = villubod;
        this.stada = stada;
        this.prentun = prentun;
    }


    /**
     * Gets the eftirstodvar value for this UppreiknudKrafa.
     * 
     * @return eftirstodvar
     */
    public java.math.BigDecimal getEftirstodvar() {
        return eftirstodvar;
    }


    /**
     * Sets the eftirstodvar value for this UppreiknudKrafa.
     * 
     * @param eftirstodvar
     */
    public void setEftirstodvar(java.math.BigDecimal eftirstodvar) {
        this.eftirstodvar = eftirstodvar;
    }


    /**
     * Gets the skuldfaerist value for this UppreiknudKrafa.
     * 
     * @return skuldfaerist
     */
    public boolean isSkuldfaerist() {
        return skuldfaerist;
    }


    /**
     * Sets the skuldfaerist value for this UppreiknudKrafa.
     * 
     * @param skuldfaerist
     */
    public void setSkuldfaerist(boolean skuldfaerist) {
        this.skuldfaerist = skuldfaerist;
    }


    /**
     * Gets the ogreittTilkynningargjald value for this UppreiknudKrafa.
     * 
     * @return ogreittTilkynningargjald
     */
    public java.math.BigDecimal getOgreittTilkynningargjald() {
        return ogreittTilkynningargjald;
    }


    /**
     * Sets the ogreittTilkynningargjald value for this UppreiknudKrafa.
     * 
     * @param ogreittTilkynningargjald
     */
    public void setOgreittTilkynningargjald(java.math.BigDecimal ogreittTilkynningargjald) {
        this.ogreittTilkynningargjald = ogreittTilkynningargjald;
    }


    /**
     * Gets the ogreiddirDrattarvextir value for this UppreiknudKrafa.
     * 
     * @return ogreiddirDrattarvextir
     */
    public java.math.BigDecimal getOgreiddirDrattarvextir() {
        return ogreiddirDrattarvextir;
    }


    /**
     * Sets the ogreiddirDrattarvextir value for this UppreiknudKrafa.
     * 
     * @param ogreiddirDrattarvextir
     */
    public void setOgreiddirDrattarvextir(java.math.BigDecimal ogreiddirDrattarvextir) {
        this.ogreiddirDrattarvextir = ogreiddirDrattarvextir;
    }


    /**
     * Gets the uppaedTilGreidsluIDag value for this UppreiknudKrafa.
     * 
     * @return uppaedTilGreidsluIDag
     */
    public java.math.BigDecimal getUppaedTilGreidsluIDag() {
        return uppaedTilGreidsluIDag;
    }


    /**
     * Sets the uppaedTilGreidsluIDag value for this UppreiknudKrafa.
     * 
     * @param uppaedTilGreidsluIDag
     */
    public void setUppaedTilGreidsluIDag(java.math.BigDecimal uppaedTilGreidsluIDag) {
        this.uppaedTilGreidsluIDag = uppaedTilGreidsluIDag;
    }


    /**
     * Gets the tilkynningargjaldTilGreidsluIDag value for this UppreiknudKrafa.
     * 
     * @return tilkynningargjaldTilGreidsluIDag
     */
    public java.math.BigDecimal getTilkynningargjaldTilGreidsluIDag() {
        return tilkynningargjaldTilGreidsluIDag;
    }


    /**
     * Sets the tilkynningargjaldTilGreidsluIDag value for this UppreiknudKrafa.
     * 
     * @param tilkynningargjaldTilGreidsluIDag
     */
    public void setTilkynningargjaldTilGreidsluIDag(java.math.BigDecimal tilkynningargjaldTilGreidsluIDag) {
        this.tilkynningargjaldTilGreidsluIDag = tilkynningargjaldTilGreidsluIDag;
    }


    /**
     * Gets the vanskilagjaldTilGreidsluIDag value for this UppreiknudKrafa.
     * 
     * @return vanskilagjaldTilGreidsluIDag
     */
    public java.math.BigDecimal getVanskilagjaldTilGreidsluIDag() {
        return vanskilagjaldTilGreidsluIDag;
    }


    /**
     * Sets the vanskilagjaldTilGreidsluIDag value for this UppreiknudKrafa.
     * 
     * @param vanskilagjaldTilGreidsluIDag
     */
    public void setVanskilagjaldTilGreidsluIDag(java.math.BigDecimal vanskilagjaldTilGreidsluIDag) {
        this.vanskilagjaldTilGreidsluIDag = vanskilagjaldTilGreidsluIDag;
    }


    /**
     * Gets the drattarvextirTilGreidsluIDag value for this UppreiknudKrafa.
     * 
     * @return drattarvextirTilGreidsluIDag
     */
    public java.math.BigDecimal getDrattarvextirTilGreidsluIDag() {
        return drattarvextirTilGreidsluIDag;
    }


    /**
     * Sets the drattarvextirTilGreidsluIDag value for this UppreiknudKrafa.
     * 
     * @param drattarvextirTilGreidsluIDag
     */
    public void setDrattarvextirTilGreidsluIDag(java.math.BigDecimal drattarvextirTilGreidsluIDag) {
        this.drattarvextirTilGreidsluIDag = drattarvextirTilGreidsluIDag;
    }


    /**
     * Gets the villubod value for this UppreiknudKrafa.
     * 
     * @return villubod
     */
    public java.lang.String getVillubod() {
        return villubod;
    }


    /**
     * Sets the villubod value for this UppreiknudKrafa.
     * 
     * @param villubod
     */
    public void setVillubod(java.lang.String villubod) {
        this.villubod = villubod;
    }


    /**
     * Gets the stada value for this UppreiknudKrafa.
     * 
     * @return stada
     */
    public is.idega.block.finance.business.isb.ws2.StadaKrofu getStada() {
        return stada;
    }


    /**
     * Sets the stada value for this UppreiknudKrafa.
     * 
     * @param stada
     */
    public void setStada(is.idega.block.finance.business.isb.ws2.StadaKrofu stada) {
        this.stada = stada;
    }


    /**
     * Gets the prentun value for this UppreiknudKrafa.
     * 
     * @return prentun
     */
    public is.idega.block.finance.business.isb.ws2.PrentunKrofu getPrentun() {
        return prentun;
    }


    /**
     * Sets the prentun value for this UppreiknudKrafa.
     * 
     * @param prentun
     */
    public void setPrentun(is.idega.block.finance.business.isb.ws2.PrentunKrofu prentun) {
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
        _hashCode += (isSkuldfaerist() ? Boolean.TRUE : Boolean.FALSE).hashCode();
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
        new org.apache.axis.description.TypeDesc(UppreiknudKrafa.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ws.isb.is", "UppreiknudKrafa"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("eftirstodvar");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Eftirstodvar"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("skuldfaerist");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Skuldfaerist"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ogreittTilkynningargjald");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "OgreittTilkynningargjald"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ogreiddirDrattarvextir");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "OgreiddirDrattarvextir"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("uppaedTilGreidsluIDag");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "UppaedTilGreidsluIDag"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tilkynningargjaldTilGreidsluIDag");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "TilkynningargjaldTilGreidsluIDag"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vanskilagjaldTilGreidsluIDag");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "VanskilagjaldTilGreidsluIDag"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("drattarvextirTilGreidsluIDag");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "DrattarvextirTilGreidsluIDag"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("villubod");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Villubod"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stada");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Stada"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ws.isb.is", "StadaKrofu"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("prentun");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Prentun"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ws.isb.is", "PrentunKrofu"));
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
