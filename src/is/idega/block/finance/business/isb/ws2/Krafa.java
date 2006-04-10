/**
 * Krafa.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.block.finance.business.isb.ws2;

public class Krafa  implements java.io.Serializable {
    private java.lang.String faerslugerd;

    private java.lang.String kennitalaKrofuhafa;

    private java.util.Calendar gjalddagi;

    private java.util.Calendar nidurfellingardagur;

    private java.lang.String audkenni;

    private java.lang.String kennitalaGreidanda;

    private int bankanumer;

    private int hofudbok;

    private int krofunumer;

    private java.math.BigDecimal upphaed;

    private java.lang.String tilvisun;

    private java.lang.String sedilnumer;

    private java.lang.String vidskiptanumer;

    private java.util.Calendar eindagi;

    private java.math.BigDecimal tilkynningarOgGreidslugjald1;

    private java.math.BigDecimal tilkynningarOgGreidslugjald2;

    private java.math.BigDecimal vanskilagjald1;

    private java.math.BigDecimal vanskilagjald2;

    private int dagafjoldiVanskilagjalds1;

    private int dagafjoldiVanskilagjalds2;

    private java.lang.String vanskilagjaldsKodi;

    private java.math.BigDecimal annarKostnadur;

    private java.math.BigDecimal annarVanskilakostnadur;

    private java.math.BigDecimal drattavaxtaprosenta;

    private java.lang.String drattavaxtaregla;

    private java.lang.String drattavaxtastofnkodi;

    private java.lang.String gengistegund;

    private java.lang.String mynt;

    private int gengisbanki;

    private java.lang.String gengiskodi;

    private java.lang.String greidslukodi;

    private java.math.BigDecimal afslattur1;

    private java.math.BigDecimal afslattur2;

    private int dagafjoldiAfslattar1;

    private int dagafjoldiAfslattar2;

    private java.lang.String afslattarkodi;

    private java.lang.String innborgunarkodi;

    private java.lang.String birtingakodi;

    private java.lang.String vefslod;

    private java.lang.String nafnGreidanda1;

    private java.lang.String nafnGreidanda2;

    private java.lang.String heimiliGreidanda;

    private java.lang.String sveitarfelagGreidanda;

    private java.lang.String athugasemdalina1;

    private java.lang.String athugasemdalina2;

    private is.idega.block.finance.business.isb.ws2.Krofulidur[] hreyfingar;

    public Krafa() {
    }

    public Krafa(
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
           is.idega.block.finance.business.isb.ws2.Krofulidur[] hreyfingar) {
           this.faerslugerd = faerslugerd;
           this.kennitalaKrofuhafa = kennitalaKrofuhafa;
           this.gjalddagi = gjalddagi;
           this.nidurfellingardagur = nidurfellingardagur;
           this.audkenni = audkenni;
           this.kennitalaGreidanda = kennitalaGreidanda;
           this.bankanumer = bankanumer;
           this.hofudbok = hofudbok;
           this.krofunumer = krofunumer;
           this.upphaed = upphaed;
           this.tilvisun = tilvisun;
           this.sedilnumer = sedilnumer;
           this.vidskiptanumer = vidskiptanumer;
           this.eindagi = eindagi;
           this.tilkynningarOgGreidslugjald1 = tilkynningarOgGreidslugjald1;
           this.tilkynningarOgGreidslugjald2 = tilkynningarOgGreidslugjald2;
           this.vanskilagjald1 = vanskilagjald1;
           this.vanskilagjald2 = vanskilagjald2;
           this.dagafjoldiVanskilagjalds1 = dagafjoldiVanskilagjalds1;
           this.dagafjoldiVanskilagjalds2 = dagafjoldiVanskilagjalds2;
           this.vanskilagjaldsKodi = vanskilagjaldsKodi;
           this.annarKostnadur = annarKostnadur;
           this.annarVanskilakostnadur = annarVanskilakostnadur;
           this.drattavaxtaprosenta = drattavaxtaprosenta;
           this.drattavaxtaregla = drattavaxtaregla;
           this.drattavaxtastofnkodi = drattavaxtastofnkodi;
           this.gengistegund = gengistegund;
           this.mynt = mynt;
           this.gengisbanki = gengisbanki;
           this.gengiskodi = gengiskodi;
           this.greidslukodi = greidslukodi;
           this.afslattur1 = afslattur1;
           this.afslattur2 = afslattur2;
           this.dagafjoldiAfslattar1 = dagafjoldiAfslattar1;
           this.dagafjoldiAfslattar2 = dagafjoldiAfslattar2;
           this.afslattarkodi = afslattarkodi;
           this.innborgunarkodi = innborgunarkodi;
           this.birtingakodi = birtingakodi;
           this.vefslod = vefslod;
           this.nafnGreidanda1 = nafnGreidanda1;
           this.nafnGreidanda2 = nafnGreidanda2;
           this.heimiliGreidanda = heimiliGreidanda;
           this.sveitarfelagGreidanda = sveitarfelagGreidanda;
           this.athugasemdalina1 = athugasemdalina1;
           this.athugasemdalina2 = athugasemdalina2;
           this.hreyfingar = hreyfingar;
    }


    /**
     * Gets the faerslugerd value for this Krafa.
     * 
     * @return faerslugerd
     */
    public java.lang.String getFaerslugerd() {
        return faerslugerd;
    }


    /**
     * Sets the faerslugerd value for this Krafa.
     * 
     * @param faerslugerd
     */
    public void setFaerslugerd(java.lang.String faerslugerd) {
        this.faerslugerd = faerslugerd;
    }


    /**
     * Gets the kennitalaKrofuhafa value for this Krafa.
     * 
     * @return kennitalaKrofuhafa
     */
    public java.lang.String getKennitalaKrofuhafa() {
        return kennitalaKrofuhafa;
    }


    /**
     * Sets the kennitalaKrofuhafa value for this Krafa.
     * 
     * @param kennitalaKrofuhafa
     */
    public void setKennitalaKrofuhafa(java.lang.String kennitalaKrofuhafa) {
        this.kennitalaKrofuhafa = kennitalaKrofuhafa;
    }


    /**
     * Gets the gjalddagi value for this Krafa.
     * 
     * @return gjalddagi
     */
    public java.util.Calendar getGjalddagi() {
        return gjalddagi;
    }


    /**
     * Sets the gjalddagi value for this Krafa.
     * 
     * @param gjalddagi
     */
    public void setGjalddagi(java.util.Calendar gjalddagi) {
        this.gjalddagi = gjalddagi;
    }


    /**
     * Gets the nidurfellingardagur value for this Krafa.
     * 
     * @return nidurfellingardagur
     */
    public java.util.Calendar getNidurfellingardagur() {
        return nidurfellingardagur;
    }


    /**
     * Sets the nidurfellingardagur value for this Krafa.
     * 
     * @param nidurfellingardagur
     */
    public void setNidurfellingardagur(java.util.Calendar nidurfellingardagur) {
        this.nidurfellingardagur = nidurfellingardagur;
    }


    /**
     * Gets the audkenni value for this Krafa.
     * 
     * @return audkenni
     */
    public java.lang.String getAudkenni() {
        return audkenni;
    }


    /**
     * Sets the audkenni value for this Krafa.
     * 
     * @param audkenni
     */
    public void setAudkenni(java.lang.String audkenni) {
        this.audkenni = audkenni;
    }


    /**
     * Gets the kennitalaGreidanda value for this Krafa.
     * 
     * @return kennitalaGreidanda
     */
    public java.lang.String getKennitalaGreidanda() {
        return kennitalaGreidanda;
    }


    /**
     * Sets the kennitalaGreidanda value for this Krafa.
     * 
     * @param kennitalaGreidanda
     */
    public void setKennitalaGreidanda(java.lang.String kennitalaGreidanda) {
        this.kennitalaGreidanda = kennitalaGreidanda;
    }


    /**
     * Gets the bankanumer value for this Krafa.
     * 
     * @return bankanumer
     */
    public int getBankanumer() {
        return bankanumer;
    }


    /**
     * Sets the bankanumer value for this Krafa.
     * 
     * @param bankanumer
     */
    public void setBankanumer(int bankanumer) {
        this.bankanumer = bankanumer;
    }


    /**
     * Gets the hofudbok value for this Krafa.
     * 
     * @return hofudbok
     */
    public int getHofudbok() {
        return hofudbok;
    }


    /**
     * Sets the hofudbok value for this Krafa.
     * 
     * @param hofudbok
     */
    public void setHofudbok(int hofudbok) {
        this.hofudbok = hofudbok;
    }


    /**
     * Gets the krofunumer value for this Krafa.
     * 
     * @return krofunumer
     */
    public int getKrofunumer() {
        return krofunumer;
    }


    /**
     * Sets the krofunumer value for this Krafa.
     * 
     * @param krofunumer
     */
    public void setKrofunumer(int krofunumer) {
        this.krofunumer = krofunumer;
    }


    /**
     * Gets the upphaed value for this Krafa.
     * 
     * @return upphaed
     */
    public java.math.BigDecimal getUpphaed() {
        return upphaed;
    }


    /**
     * Sets the upphaed value for this Krafa.
     * 
     * @param upphaed
     */
    public void setUpphaed(java.math.BigDecimal upphaed) {
        this.upphaed = upphaed;
    }


    /**
     * Gets the tilvisun value for this Krafa.
     * 
     * @return tilvisun
     */
    public java.lang.String getTilvisun() {
        return tilvisun;
    }


    /**
     * Sets the tilvisun value for this Krafa.
     * 
     * @param tilvisun
     */
    public void setTilvisun(java.lang.String tilvisun) {
        this.tilvisun = tilvisun;
    }


    /**
     * Gets the sedilnumer value for this Krafa.
     * 
     * @return sedilnumer
     */
    public java.lang.String getSedilnumer() {
        return sedilnumer;
    }


    /**
     * Sets the sedilnumer value for this Krafa.
     * 
     * @param sedilnumer
     */
    public void setSedilnumer(java.lang.String sedilnumer) {
        this.sedilnumer = sedilnumer;
    }


    /**
     * Gets the vidskiptanumer value for this Krafa.
     * 
     * @return vidskiptanumer
     */
    public java.lang.String getVidskiptanumer() {
        return vidskiptanumer;
    }


    /**
     * Sets the vidskiptanumer value for this Krafa.
     * 
     * @param vidskiptanumer
     */
    public void setVidskiptanumer(java.lang.String vidskiptanumer) {
        this.vidskiptanumer = vidskiptanumer;
    }


    /**
     * Gets the eindagi value for this Krafa.
     * 
     * @return eindagi
     */
    public java.util.Calendar getEindagi() {
        return eindagi;
    }


    /**
     * Sets the eindagi value for this Krafa.
     * 
     * @param eindagi
     */
    public void setEindagi(java.util.Calendar eindagi) {
        this.eindagi = eindagi;
    }


    /**
     * Gets the tilkynningarOgGreidslugjald1 value for this Krafa.
     * 
     * @return tilkynningarOgGreidslugjald1
     */
    public java.math.BigDecimal getTilkynningarOgGreidslugjald1() {
        return tilkynningarOgGreidslugjald1;
    }


    /**
     * Sets the tilkynningarOgGreidslugjald1 value for this Krafa.
     * 
     * @param tilkynningarOgGreidslugjald1
     */
    public void setTilkynningarOgGreidslugjald1(java.math.BigDecimal tilkynningarOgGreidslugjald1) {
        this.tilkynningarOgGreidslugjald1 = tilkynningarOgGreidslugjald1;
    }


    /**
     * Gets the tilkynningarOgGreidslugjald2 value for this Krafa.
     * 
     * @return tilkynningarOgGreidslugjald2
     */
    public java.math.BigDecimal getTilkynningarOgGreidslugjald2() {
        return tilkynningarOgGreidslugjald2;
    }


    /**
     * Sets the tilkynningarOgGreidslugjald2 value for this Krafa.
     * 
     * @param tilkynningarOgGreidslugjald2
     */
    public void setTilkynningarOgGreidslugjald2(java.math.BigDecimal tilkynningarOgGreidslugjald2) {
        this.tilkynningarOgGreidslugjald2 = tilkynningarOgGreidslugjald2;
    }


    /**
     * Gets the vanskilagjald1 value for this Krafa.
     * 
     * @return vanskilagjald1
     */
    public java.math.BigDecimal getVanskilagjald1() {
        return vanskilagjald1;
    }


    /**
     * Sets the vanskilagjald1 value for this Krafa.
     * 
     * @param vanskilagjald1
     */
    public void setVanskilagjald1(java.math.BigDecimal vanskilagjald1) {
        this.vanskilagjald1 = vanskilagjald1;
    }


    /**
     * Gets the vanskilagjald2 value for this Krafa.
     * 
     * @return vanskilagjald2
     */
    public java.math.BigDecimal getVanskilagjald2() {
        return vanskilagjald2;
    }


    /**
     * Sets the vanskilagjald2 value for this Krafa.
     * 
     * @param vanskilagjald2
     */
    public void setVanskilagjald2(java.math.BigDecimal vanskilagjald2) {
        this.vanskilagjald2 = vanskilagjald2;
    }


    /**
     * Gets the dagafjoldiVanskilagjalds1 value for this Krafa.
     * 
     * @return dagafjoldiVanskilagjalds1
     */
    public int getDagafjoldiVanskilagjalds1() {
        return dagafjoldiVanskilagjalds1;
    }


    /**
     * Sets the dagafjoldiVanskilagjalds1 value for this Krafa.
     * 
     * @param dagafjoldiVanskilagjalds1
     */
    public void setDagafjoldiVanskilagjalds1(int dagafjoldiVanskilagjalds1) {
        this.dagafjoldiVanskilagjalds1 = dagafjoldiVanskilagjalds1;
    }


    /**
     * Gets the dagafjoldiVanskilagjalds2 value for this Krafa.
     * 
     * @return dagafjoldiVanskilagjalds2
     */
    public int getDagafjoldiVanskilagjalds2() {
        return dagafjoldiVanskilagjalds2;
    }


    /**
     * Sets the dagafjoldiVanskilagjalds2 value for this Krafa.
     * 
     * @param dagafjoldiVanskilagjalds2
     */
    public void setDagafjoldiVanskilagjalds2(int dagafjoldiVanskilagjalds2) {
        this.dagafjoldiVanskilagjalds2 = dagafjoldiVanskilagjalds2;
    }


    /**
     * Gets the vanskilagjaldsKodi value for this Krafa.
     * 
     * @return vanskilagjaldsKodi
     */
    public java.lang.String getVanskilagjaldsKodi() {
        return vanskilagjaldsKodi;
    }


    /**
     * Sets the vanskilagjaldsKodi value for this Krafa.
     * 
     * @param vanskilagjaldsKodi
     */
    public void setVanskilagjaldsKodi(java.lang.String vanskilagjaldsKodi) {
        this.vanskilagjaldsKodi = vanskilagjaldsKodi;
    }


    /**
     * Gets the annarKostnadur value for this Krafa.
     * 
     * @return annarKostnadur
     */
    public java.math.BigDecimal getAnnarKostnadur() {
        return annarKostnadur;
    }


    /**
     * Sets the annarKostnadur value for this Krafa.
     * 
     * @param annarKostnadur
     */
    public void setAnnarKostnadur(java.math.BigDecimal annarKostnadur) {
        this.annarKostnadur = annarKostnadur;
    }


    /**
     * Gets the annarVanskilakostnadur value for this Krafa.
     * 
     * @return annarVanskilakostnadur
     */
    public java.math.BigDecimal getAnnarVanskilakostnadur() {
        return annarVanskilakostnadur;
    }


    /**
     * Sets the annarVanskilakostnadur value for this Krafa.
     * 
     * @param annarVanskilakostnadur
     */
    public void setAnnarVanskilakostnadur(java.math.BigDecimal annarVanskilakostnadur) {
        this.annarVanskilakostnadur = annarVanskilakostnadur;
    }


    /**
     * Gets the drattavaxtaprosenta value for this Krafa.
     * 
     * @return drattavaxtaprosenta
     */
    public java.math.BigDecimal getDrattavaxtaprosenta() {
        return drattavaxtaprosenta;
    }


    /**
     * Sets the drattavaxtaprosenta value for this Krafa.
     * 
     * @param drattavaxtaprosenta
     */
    public void setDrattavaxtaprosenta(java.math.BigDecimal drattavaxtaprosenta) {
        this.drattavaxtaprosenta = drattavaxtaprosenta;
    }


    /**
     * Gets the drattavaxtaregla value for this Krafa.
     * 
     * @return drattavaxtaregla
     */
    public java.lang.String getDrattavaxtaregla() {
        return drattavaxtaregla;
    }


    /**
     * Sets the drattavaxtaregla value for this Krafa.
     * 
     * @param drattavaxtaregla
     */
    public void setDrattavaxtaregla(java.lang.String drattavaxtaregla) {
        this.drattavaxtaregla = drattavaxtaregla;
    }


    /**
     * Gets the drattavaxtastofnkodi value for this Krafa.
     * 
     * @return drattavaxtastofnkodi
     */
    public java.lang.String getDrattavaxtastofnkodi() {
        return drattavaxtastofnkodi;
    }


    /**
     * Sets the drattavaxtastofnkodi value for this Krafa.
     * 
     * @param drattavaxtastofnkodi
     */
    public void setDrattavaxtastofnkodi(java.lang.String drattavaxtastofnkodi) {
        this.drattavaxtastofnkodi = drattavaxtastofnkodi;
    }


    /**
     * Gets the gengistegund value for this Krafa.
     * 
     * @return gengistegund
     */
    public java.lang.String getGengistegund() {
        return gengistegund;
    }


    /**
     * Sets the gengistegund value for this Krafa.
     * 
     * @param gengistegund
     */
    public void setGengistegund(java.lang.String gengistegund) {
        this.gengistegund = gengistegund;
    }


    /**
     * Gets the mynt value for this Krafa.
     * 
     * @return mynt
     */
    public java.lang.String getMynt() {
        return mynt;
    }


    /**
     * Sets the mynt value for this Krafa.
     * 
     * @param mynt
     */
    public void setMynt(java.lang.String mynt) {
        this.mynt = mynt;
    }


    /**
     * Gets the gengisbanki value for this Krafa.
     * 
     * @return gengisbanki
     */
    public int getGengisbanki() {
        return gengisbanki;
    }


    /**
     * Sets the gengisbanki value for this Krafa.
     * 
     * @param gengisbanki
     */
    public void setGengisbanki(int gengisbanki) {
        this.gengisbanki = gengisbanki;
    }


    /**
     * Gets the gengiskodi value for this Krafa.
     * 
     * @return gengiskodi
     */
    public java.lang.String getGengiskodi() {
        return gengiskodi;
    }


    /**
     * Sets the gengiskodi value for this Krafa.
     * 
     * @param gengiskodi
     */
    public void setGengiskodi(java.lang.String gengiskodi) {
        this.gengiskodi = gengiskodi;
    }


    /**
     * Gets the greidslukodi value for this Krafa.
     * 
     * @return greidslukodi
     */
    public java.lang.String getGreidslukodi() {
        return greidslukodi;
    }


    /**
     * Sets the greidslukodi value for this Krafa.
     * 
     * @param greidslukodi
     */
    public void setGreidslukodi(java.lang.String greidslukodi) {
        this.greidslukodi = greidslukodi;
    }


    /**
     * Gets the afslattur1 value for this Krafa.
     * 
     * @return afslattur1
     */
    public java.math.BigDecimal getAfslattur1() {
        return afslattur1;
    }


    /**
     * Sets the afslattur1 value for this Krafa.
     * 
     * @param afslattur1
     */
    public void setAfslattur1(java.math.BigDecimal afslattur1) {
        this.afslattur1 = afslattur1;
    }


    /**
     * Gets the afslattur2 value for this Krafa.
     * 
     * @return afslattur2
     */
    public java.math.BigDecimal getAfslattur2() {
        return afslattur2;
    }


    /**
     * Sets the afslattur2 value for this Krafa.
     * 
     * @param afslattur2
     */
    public void setAfslattur2(java.math.BigDecimal afslattur2) {
        this.afslattur2 = afslattur2;
    }


    /**
     * Gets the dagafjoldiAfslattar1 value for this Krafa.
     * 
     * @return dagafjoldiAfslattar1
     */
    public int getDagafjoldiAfslattar1() {
        return dagafjoldiAfslattar1;
    }


    /**
     * Sets the dagafjoldiAfslattar1 value for this Krafa.
     * 
     * @param dagafjoldiAfslattar1
     */
    public void setDagafjoldiAfslattar1(int dagafjoldiAfslattar1) {
        this.dagafjoldiAfslattar1 = dagafjoldiAfslattar1;
    }


    /**
     * Gets the dagafjoldiAfslattar2 value for this Krafa.
     * 
     * @return dagafjoldiAfslattar2
     */
    public int getDagafjoldiAfslattar2() {
        return dagafjoldiAfslattar2;
    }


    /**
     * Sets the dagafjoldiAfslattar2 value for this Krafa.
     * 
     * @param dagafjoldiAfslattar2
     */
    public void setDagafjoldiAfslattar2(int dagafjoldiAfslattar2) {
        this.dagafjoldiAfslattar2 = dagafjoldiAfslattar2;
    }


    /**
     * Gets the afslattarkodi value for this Krafa.
     * 
     * @return afslattarkodi
     */
    public java.lang.String getAfslattarkodi() {
        return afslattarkodi;
    }


    /**
     * Sets the afslattarkodi value for this Krafa.
     * 
     * @param afslattarkodi
     */
    public void setAfslattarkodi(java.lang.String afslattarkodi) {
        this.afslattarkodi = afslattarkodi;
    }


    /**
     * Gets the innborgunarkodi value for this Krafa.
     * 
     * @return innborgunarkodi
     */
    public java.lang.String getInnborgunarkodi() {
        return innborgunarkodi;
    }


    /**
     * Sets the innborgunarkodi value for this Krafa.
     * 
     * @param innborgunarkodi
     */
    public void setInnborgunarkodi(java.lang.String innborgunarkodi) {
        this.innborgunarkodi = innborgunarkodi;
    }


    /**
     * Gets the birtingakodi value for this Krafa.
     * 
     * @return birtingakodi
     */
    public java.lang.String getBirtingakodi() {
        return birtingakodi;
    }


    /**
     * Sets the birtingakodi value for this Krafa.
     * 
     * @param birtingakodi
     */
    public void setBirtingakodi(java.lang.String birtingakodi) {
        this.birtingakodi = birtingakodi;
    }


    /**
     * Gets the vefslod value for this Krafa.
     * 
     * @return vefslod
     */
    public java.lang.String getVefslod() {
        return vefslod;
    }


    /**
     * Sets the vefslod value for this Krafa.
     * 
     * @param vefslod
     */
    public void setVefslod(java.lang.String vefslod) {
        this.vefslod = vefslod;
    }


    /**
     * Gets the nafnGreidanda1 value for this Krafa.
     * 
     * @return nafnGreidanda1
     */
    public java.lang.String getNafnGreidanda1() {
        return nafnGreidanda1;
    }


    /**
     * Sets the nafnGreidanda1 value for this Krafa.
     * 
     * @param nafnGreidanda1
     */
    public void setNafnGreidanda1(java.lang.String nafnGreidanda1) {
        this.nafnGreidanda1 = nafnGreidanda1;
    }


    /**
     * Gets the nafnGreidanda2 value for this Krafa.
     * 
     * @return nafnGreidanda2
     */
    public java.lang.String getNafnGreidanda2() {
        return nafnGreidanda2;
    }


    /**
     * Sets the nafnGreidanda2 value for this Krafa.
     * 
     * @param nafnGreidanda2
     */
    public void setNafnGreidanda2(java.lang.String nafnGreidanda2) {
        this.nafnGreidanda2 = nafnGreidanda2;
    }


    /**
     * Gets the heimiliGreidanda value for this Krafa.
     * 
     * @return heimiliGreidanda
     */
    public java.lang.String getHeimiliGreidanda() {
        return heimiliGreidanda;
    }


    /**
     * Sets the heimiliGreidanda value for this Krafa.
     * 
     * @param heimiliGreidanda
     */
    public void setHeimiliGreidanda(java.lang.String heimiliGreidanda) {
        this.heimiliGreidanda = heimiliGreidanda;
    }


    /**
     * Gets the sveitarfelagGreidanda value for this Krafa.
     * 
     * @return sveitarfelagGreidanda
     */
    public java.lang.String getSveitarfelagGreidanda() {
        return sveitarfelagGreidanda;
    }


    /**
     * Sets the sveitarfelagGreidanda value for this Krafa.
     * 
     * @param sveitarfelagGreidanda
     */
    public void setSveitarfelagGreidanda(java.lang.String sveitarfelagGreidanda) {
        this.sveitarfelagGreidanda = sveitarfelagGreidanda;
    }


    /**
     * Gets the athugasemdalina1 value for this Krafa.
     * 
     * @return athugasemdalina1
     */
    public java.lang.String getAthugasemdalina1() {
        return athugasemdalina1;
    }


    /**
     * Sets the athugasemdalina1 value for this Krafa.
     * 
     * @param athugasemdalina1
     */
    public void setAthugasemdalina1(java.lang.String athugasemdalina1) {
        this.athugasemdalina1 = athugasemdalina1;
    }


    /**
     * Gets the athugasemdalina2 value for this Krafa.
     * 
     * @return athugasemdalina2
     */
    public java.lang.String getAthugasemdalina2() {
        return athugasemdalina2;
    }


    /**
     * Sets the athugasemdalina2 value for this Krafa.
     * 
     * @param athugasemdalina2
     */
    public void setAthugasemdalina2(java.lang.String athugasemdalina2) {
        this.athugasemdalina2 = athugasemdalina2;
    }


    /**
     * Gets the hreyfingar value for this Krafa.
     * 
     * @return hreyfingar
     */
    public is.idega.block.finance.business.isb.ws2.Krofulidur[] getHreyfingar() {
        return hreyfingar;
    }


    /**
     * Sets the hreyfingar value for this Krafa.
     * 
     * @param hreyfingar
     */
    public void setHreyfingar(is.idega.block.finance.business.isb.ws2.Krofulidur[] hreyfingar) {
        this.hreyfingar = hreyfingar;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Krafa)) return false;
        Krafa other = (Krafa) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.faerslugerd==null && other.getFaerslugerd()==null) || 
             (this.faerslugerd!=null &&
              this.faerslugerd.equals(other.getFaerslugerd()))) &&
            ((this.kennitalaKrofuhafa==null && other.getKennitalaKrofuhafa()==null) || 
             (this.kennitalaKrofuhafa!=null &&
              this.kennitalaKrofuhafa.equals(other.getKennitalaKrofuhafa()))) &&
            ((this.gjalddagi==null && other.getGjalddagi()==null) || 
             (this.gjalddagi!=null &&
              this.gjalddagi.equals(other.getGjalddagi()))) &&
            ((this.nidurfellingardagur==null && other.getNidurfellingardagur()==null) || 
             (this.nidurfellingardagur!=null &&
              this.nidurfellingardagur.equals(other.getNidurfellingardagur()))) &&
            ((this.audkenni==null && other.getAudkenni()==null) || 
             (this.audkenni!=null &&
              this.audkenni.equals(other.getAudkenni()))) &&
            ((this.kennitalaGreidanda==null && other.getKennitalaGreidanda()==null) || 
             (this.kennitalaGreidanda!=null &&
              this.kennitalaGreidanda.equals(other.getKennitalaGreidanda()))) &&
            this.bankanumer == other.getBankanumer() &&
            this.hofudbok == other.getHofudbok() &&
            this.krofunumer == other.getKrofunumer() &&
            ((this.upphaed==null && other.getUpphaed()==null) || 
             (this.upphaed!=null &&
              this.upphaed.equals(other.getUpphaed()))) &&
            ((this.tilvisun==null && other.getTilvisun()==null) || 
             (this.tilvisun!=null &&
              this.tilvisun.equals(other.getTilvisun()))) &&
            ((this.sedilnumer==null && other.getSedilnumer()==null) || 
             (this.sedilnumer!=null &&
              this.sedilnumer.equals(other.getSedilnumer()))) &&
            ((this.vidskiptanumer==null && other.getVidskiptanumer()==null) || 
             (this.vidskiptanumer!=null &&
              this.vidskiptanumer.equals(other.getVidskiptanumer()))) &&
            ((this.eindagi==null && other.getEindagi()==null) || 
             (this.eindagi!=null &&
              this.eindagi.equals(other.getEindagi()))) &&
            ((this.tilkynningarOgGreidslugjald1==null && other.getTilkynningarOgGreidslugjald1()==null) || 
             (this.tilkynningarOgGreidslugjald1!=null &&
              this.tilkynningarOgGreidslugjald1.equals(other.getTilkynningarOgGreidslugjald1()))) &&
            ((this.tilkynningarOgGreidslugjald2==null && other.getTilkynningarOgGreidslugjald2()==null) || 
             (this.tilkynningarOgGreidslugjald2!=null &&
              this.tilkynningarOgGreidslugjald2.equals(other.getTilkynningarOgGreidslugjald2()))) &&
            ((this.vanskilagjald1==null && other.getVanskilagjald1()==null) || 
             (this.vanskilagjald1!=null &&
              this.vanskilagjald1.equals(other.getVanskilagjald1()))) &&
            ((this.vanskilagjald2==null && other.getVanskilagjald2()==null) || 
             (this.vanskilagjald2!=null &&
              this.vanskilagjald2.equals(other.getVanskilagjald2()))) &&
            this.dagafjoldiVanskilagjalds1 == other.getDagafjoldiVanskilagjalds1() &&
            this.dagafjoldiVanskilagjalds2 == other.getDagafjoldiVanskilagjalds2() &&
            ((this.vanskilagjaldsKodi==null && other.getVanskilagjaldsKodi()==null) || 
             (this.vanskilagjaldsKodi!=null &&
              this.vanskilagjaldsKodi.equals(other.getVanskilagjaldsKodi()))) &&
            ((this.annarKostnadur==null && other.getAnnarKostnadur()==null) || 
             (this.annarKostnadur!=null &&
              this.annarKostnadur.equals(other.getAnnarKostnadur()))) &&
            ((this.annarVanskilakostnadur==null && other.getAnnarVanskilakostnadur()==null) || 
             (this.annarVanskilakostnadur!=null &&
              this.annarVanskilakostnadur.equals(other.getAnnarVanskilakostnadur()))) &&
            ((this.drattavaxtaprosenta==null && other.getDrattavaxtaprosenta()==null) || 
             (this.drattavaxtaprosenta!=null &&
              this.drattavaxtaprosenta.equals(other.getDrattavaxtaprosenta()))) &&
            ((this.drattavaxtaregla==null && other.getDrattavaxtaregla()==null) || 
             (this.drattavaxtaregla!=null &&
              this.drattavaxtaregla.equals(other.getDrattavaxtaregla()))) &&
            ((this.drattavaxtastofnkodi==null && other.getDrattavaxtastofnkodi()==null) || 
             (this.drattavaxtastofnkodi!=null &&
              this.drattavaxtastofnkodi.equals(other.getDrattavaxtastofnkodi()))) &&
            ((this.gengistegund==null && other.getGengistegund()==null) || 
             (this.gengistegund!=null &&
              this.gengistegund.equals(other.getGengistegund()))) &&
            ((this.mynt==null && other.getMynt()==null) || 
             (this.mynt!=null &&
              this.mynt.equals(other.getMynt()))) &&
            this.gengisbanki == other.getGengisbanki() &&
            ((this.gengiskodi==null && other.getGengiskodi()==null) || 
             (this.gengiskodi!=null &&
              this.gengiskodi.equals(other.getGengiskodi()))) &&
            ((this.greidslukodi==null && other.getGreidslukodi()==null) || 
             (this.greidslukodi!=null &&
              this.greidslukodi.equals(other.getGreidslukodi()))) &&
            ((this.afslattur1==null && other.getAfslattur1()==null) || 
             (this.afslattur1!=null &&
              this.afslattur1.equals(other.getAfslattur1()))) &&
            ((this.afslattur2==null && other.getAfslattur2()==null) || 
             (this.afslattur2!=null &&
              this.afslattur2.equals(other.getAfslattur2()))) &&
            this.dagafjoldiAfslattar1 == other.getDagafjoldiAfslattar1() &&
            this.dagafjoldiAfslattar2 == other.getDagafjoldiAfslattar2() &&
            ((this.afslattarkodi==null && other.getAfslattarkodi()==null) || 
             (this.afslattarkodi!=null &&
              this.afslattarkodi.equals(other.getAfslattarkodi()))) &&
            ((this.innborgunarkodi==null && other.getInnborgunarkodi()==null) || 
             (this.innborgunarkodi!=null &&
              this.innborgunarkodi.equals(other.getInnborgunarkodi()))) &&
            ((this.birtingakodi==null && other.getBirtingakodi()==null) || 
             (this.birtingakodi!=null &&
              this.birtingakodi.equals(other.getBirtingakodi()))) &&
            ((this.vefslod==null && other.getVefslod()==null) || 
             (this.vefslod!=null &&
              this.vefslod.equals(other.getVefslod()))) &&
            ((this.nafnGreidanda1==null && other.getNafnGreidanda1()==null) || 
             (this.nafnGreidanda1!=null &&
              this.nafnGreidanda1.equals(other.getNafnGreidanda1()))) &&
            ((this.nafnGreidanda2==null && other.getNafnGreidanda2()==null) || 
             (this.nafnGreidanda2!=null &&
              this.nafnGreidanda2.equals(other.getNafnGreidanda2()))) &&
            ((this.heimiliGreidanda==null && other.getHeimiliGreidanda()==null) || 
             (this.heimiliGreidanda!=null &&
              this.heimiliGreidanda.equals(other.getHeimiliGreidanda()))) &&
            ((this.sveitarfelagGreidanda==null && other.getSveitarfelagGreidanda()==null) || 
             (this.sveitarfelagGreidanda!=null &&
              this.sveitarfelagGreidanda.equals(other.getSveitarfelagGreidanda()))) &&
            ((this.athugasemdalina1==null && other.getAthugasemdalina1()==null) || 
             (this.athugasemdalina1!=null &&
              this.athugasemdalina1.equals(other.getAthugasemdalina1()))) &&
            ((this.athugasemdalina2==null && other.getAthugasemdalina2()==null) || 
             (this.athugasemdalina2!=null &&
              this.athugasemdalina2.equals(other.getAthugasemdalina2()))) &&
            ((this.hreyfingar==null && other.getHreyfingar()==null) || 
             (this.hreyfingar!=null &&
              java.util.Arrays.equals(this.hreyfingar, other.getHreyfingar())));
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
        if (getFaerslugerd() != null) {
            _hashCode += getFaerslugerd().hashCode();
        }
        if (getKennitalaKrofuhafa() != null) {
            _hashCode += getKennitalaKrofuhafa().hashCode();
        }
        if (getGjalddagi() != null) {
            _hashCode += getGjalddagi().hashCode();
        }
        if (getNidurfellingardagur() != null) {
            _hashCode += getNidurfellingardagur().hashCode();
        }
        if (getAudkenni() != null) {
            _hashCode += getAudkenni().hashCode();
        }
        if (getKennitalaGreidanda() != null) {
            _hashCode += getKennitalaGreidanda().hashCode();
        }
        _hashCode += getBankanumer();
        _hashCode += getHofudbok();
        _hashCode += getKrofunumer();
        if (getUpphaed() != null) {
            _hashCode += getUpphaed().hashCode();
        }
        if (getTilvisun() != null) {
            _hashCode += getTilvisun().hashCode();
        }
        if (getSedilnumer() != null) {
            _hashCode += getSedilnumer().hashCode();
        }
        if (getVidskiptanumer() != null) {
            _hashCode += getVidskiptanumer().hashCode();
        }
        if (getEindagi() != null) {
            _hashCode += getEindagi().hashCode();
        }
        if (getTilkynningarOgGreidslugjald1() != null) {
            _hashCode += getTilkynningarOgGreidslugjald1().hashCode();
        }
        if (getTilkynningarOgGreidslugjald2() != null) {
            _hashCode += getTilkynningarOgGreidslugjald2().hashCode();
        }
        if (getVanskilagjald1() != null) {
            _hashCode += getVanskilagjald1().hashCode();
        }
        if (getVanskilagjald2() != null) {
            _hashCode += getVanskilagjald2().hashCode();
        }
        _hashCode += getDagafjoldiVanskilagjalds1();
        _hashCode += getDagafjoldiVanskilagjalds2();
        if (getVanskilagjaldsKodi() != null) {
            _hashCode += getVanskilagjaldsKodi().hashCode();
        }
        if (getAnnarKostnadur() != null) {
            _hashCode += getAnnarKostnadur().hashCode();
        }
        if (getAnnarVanskilakostnadur() != null) {
            _hashCode += getAnnarVanskilakostnadur().hashCode();
        }
        if (getDrattavaxtaprosenta() != null) {
            _hashCode += getDrattavaxtaprosenta().hashCode();
        }
        if (getDrattavaxtaregla() != null) {
            _hashCode += getDrattavaxtaregla().hashCode();
        }
        if (getDrattavaxtastofnkodi() != null) {
            _hashCode += getDrattavaxtastofnkodi().hashCode();
        }
        if (getGengistegund() != null) {
            _hashCode += getGengistegund().hashCode();
        }
        if (getMynt() != null) {
            _hashCode += getMynt().hashCode();
        }
        _hashCode += getGengisbanki();
        if (getGengiskodi() != null) {
            _hashCode += getGengiskodi().hashCode();
        }
        if (getGreidslukodi() != null) {
            _hashCode += getGreidslukodi().hashCode();
        }
        if (getAfslattur1() != null) {
            _hashCode += getAfslattur1().hashCode();
        }
        if (getAfslattur2() != null) {
            _hashCode += getAfslattur2().hashCode();
        }
        _hashCode += getDagafjoldiAfslattar1();
        _hashCode += getDagafjoldiAfslattar2();
        if (getAfslattarkodi() != null) {
            _hashCode += getAfslattarkodi().hashCode();
        }
        if (getInnborgunarkodi() != null) {
            _hashCode += getInnborgunarkodi().hashCode();
        }
        if (getBirtingakodi() != null) {
            _hashCode += getBirtingakodi().hashCode();
        }
        if (getVefslod() != null) {
            _hashCode += getVefslod().hashCode();
        }
        if (getNafnGreidanda1() != null) {
            _hashCode += getNafnGreidanda1().hashCode();
        }
        if (getNafnGreidanda2() != null) {
            _hashCode += getNafnGreidanda2().hashCode();
        }
        if (getHeimiliGreidanda() != null) {
            _hashCode += getHeimiliGreidanda().hashCode();
        }
        if (getSveitarfelagGreidanda() != null) {
            _hashCode += getSveitarfelagGreidanda().hashCode();
        }
        if (getAthugasemdalina1() != null) {
            _hashCode += getAthugasemdalina1().hashCode();
        }
        if (getAthugasemdalina2() != null) {
            _hashCode += getAthugasemdalina2().hashCode();
        }
        if (getHreyfingar() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getHreyfingar());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getHreyfingar(), i);
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
        new org.apache.axis.description.TypeDesc(Krafa.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ws.isb.is", "Krafa"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("faerslugerd");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Faerslugerd"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("kennitalaKrofuhafa");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "KennitalaKrofuhafa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gjalddagi");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Gjalddagi"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nidurfellingardagur");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Nidurfellingardagur"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("audkenni");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Audkenni"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("kennitalaGreidanda");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "KennitalaGreidanda"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bankanumer");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Bankanumer"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hofudbok");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Hofudbok"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("krofunumer");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Krofunumer"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("upphaed");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Upphaed"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tilvisun");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Tilvisun"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sedilnumer");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Sedilnumer"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vidskiptanumer");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Vidskiptanumer"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("eindagi");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Eindagi"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tilkynningarOgGreidslugjald1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "TilkynningarOgGreidslugjald1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tilkynningarOgGreidslugjald2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "TilkynningarOgGreidslugjald2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vanskilagjald1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Vanskilagjald1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vanskilagjald2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Vanskilagjald2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dagafjoldiVanskilagjalds1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "DagafjoldiVanskilagjalds1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dagafjoldiVanskilagjalds2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "DagafjoldiVanskilagjalds2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vanskilagjaldsKodi");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "VanskilagjaldsKodi"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("annarKostnadur");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "AnnarKostnadur"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("annarVanskilakostnadur");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "AnnarVanskilakostnadur"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("drattavaxtaprosenta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Drattavaxtaprosenta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("drattavaxtaregla");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Drattavaxtaregla"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("drattavaxtastofnkodi");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Drattavaxtastofnkodi"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gengistegund");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Gengistegund"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mynt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Mynt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gengisbanki");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Gengisbanki"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gengiskodi");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Gengiskodi"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("greidslukodi");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Greidslukodi"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("afslattur1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Afslattur1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("afslattur2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Afslattur2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dagafjoldiAfslattar1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "DagafjoldiAfslattar1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dagafjoldiAfslattar2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "DagafjoldiAfslattar2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("afslattarkodi");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Afslattarkodi"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("innborgunarkodi");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Innborgunarkodi"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("birtingakodi");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Birtingakodi"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vefslod");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Vefslod"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nafnGreidanda1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "NafnGreidanda1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nafnGreidanda2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "NafnGreidanda2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("heimiliGreidanda");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "HeimiliGreidanda"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sveitarfelagGreidanda");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "SveitarfelagGreidanda"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("athugasemdalina1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Athugasemdalina1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("athugasemdalina2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Athugasemdalina2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hreyfingar");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Hreyfingar"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ws.isb.is", "Krofulidur"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://ws.isb.is", "Krofulidur"));
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
