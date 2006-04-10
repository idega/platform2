/**
 * Greidsla.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.block.finance.business.isb.ws2;

public class Greidsla  implements java.io.Serializable {
    private int bankanumer;

    private int hofudbok;

    private int krofunumer;

    private java.util.Calendar gjalddagi;

    private java.lang.String kennitalaKrofuhafa;

    private java.lang.String kennitalaGreidanda;

    private java.lang.String vidskiptanumer;

    private java.math.BigDecimal upphaed;

    private java.util.Calendar eindagi;

    private java.lang.String audkenni;

    private java.lang.String tilvisun;

    private java.util.Calendar nidurfellingardagur;

    private java.lang.String textalykill;

    private int innlausnarbanki;

    private java.util.Calendar hreyfingardagur;

    private java.lang.String sedilnumer;

    private java.lang.String vanskilagjaldskodi;

    private java.math.BigDecimal vanskilagjald1;

    private java.math.BigDecimal vanskilagjald2;

    private int dagafjoldiVanskilagjalds1;

    private int dagafjoldiVanskilagjalds2;

    private java.lang.String afslattarkodi;

    private java.math.BigDecimal afslattur1;

    private java.math.BigDecimal afslattur2;

    private int dagafjoldiAfslattar1;

    private int dagafjoldiAfslattar2;

    private java.math.BigDecimal tilkynningarOgGreidslugjald1;

    private java.math.BigDecimal tilkynningarOgGreidslugjald2;

    private java.math.BigDecimal annarVanskilakostnadur;

    private java.lang.String innborgunarkodi;

    private java.lang.String greidslukodi;

    private java.lang.String drattavaxtastofnkodi;

    private java.lang.String drattavaxtaregla;

    private java.math.BigDecimal drattavaxtaprosenta;

    private java.lang.String gengistegund;

    private java.lang.String mynt;

    private int gengisbanki;

    private java.lang.String gengiskodi;

    private java.lang.String birtingarkodi;

    private java.util.Calendar vaxtadagsetning;

    private java.util.Calendar innborgunardagur;

    private java.math.BigDecimal innborgunarupphaed;

    private java.math.BigDecimal drattarvextir;

    private java.math.BigDecimal greiddUpphaed;

    private java.math.BigDecimal skattaUpphaed;

    private java.math.BigDecimal veitturAfslattur;

    private java.math.BigDecimal greittTilkynningargjald;

    private java.math.BigDecimal greittVanskilagjald;

    private java.math.BigDecimal greiddurAnnarVanskilakostnadur;

    private java.math.BigDecimal greiddurAnnarKostnadur;

    private java.math.BigDecimal ogreittTilkynningargjald;

    private java.lang.String bunkanumer;

    private java.math.BigDecimal annarKostnadur;

    public Greidsla() {
    }

    public Greidsla(
           int bankanumer,
           int hofudbok,
           int krofunumer,
           java.util.Calendar gjalddagi,
           java.lang.String kennitalaKrofuhafa,
           java.lang.String kennitalaGreidanda,
           java.lang.String vidskiptanumer,
           java.math.BigDecimal upphaed,
           java.util.Calendar eindagi,
           java.lang.String audkenni,
           java.lang.String tilvisun,
           java.util.Calendar nidurfellingardagur,
           java.lang.String textalykill,
           int innlausnarbanki,
           java.util.Calendar hreyfingardagur,
           java.lang.String sedilnumer,
           java.lang.String vanskilagjaldskodi,
           java.math.BigDecimal vanskilagjald1,
           java.math.BigDecimal vanskilagjald2,
           int dagafjoldiVanskilagjalds1,
           int dagafjoldiVanskilagjalds2,
           java.lang.String afslattarkodi,
           java.math.BigDecimal afslattur1,
           java.math.BigDecimal afslattur2,
           int dagafjoldiAfslattar1,
           int dagafjoldiAfslattar2,
           java.math.BigDecimal tilkynningarOgGreidslugjald1,
           java.math.BigDecimal tilkynningarOgGreidslugjald2,
           java.math.BigDecimal annarVanskilakostnadur,
           java.lang.String innborgunarkodi,
           java.lang.String greidslukodi,
           java.lang.String drattavaxtastofnkodi,
           java.lang.String drattavaxtaregla,
           java.math.BigDecimal drattavaxtaprosenta,
           java.lang.String gengistegund,
           java.lang.String mynt,
           int gengisbanki,
           java.lang.String gengiskodi,
           java.lang.String birtingarkodi,
           java.util.Calendar vaxtadagsetning,
           java.util.Calendar innborgunardagur,
           java.math.BigDecimal innborgunarupphaed,
           java.math.BigDecimal drattarvextir,
           java.math.BigDecimal greiddUpphaed,
           java.math.BigDecimal skattaUpphaed,
           java.math.BigDecimal veitturAfslattur,
           java.math.BigDecimal greittTilkynningargjald,
           java.math.BigDecimal greittVanskilagjald,
           java.math.BigDecimal greiddurAnnarVanskilakostnadur,
           java.math.BigDecimal greiddurAnnarKostnadur,
           java.math.BigDecimal ogreittTilkynningargjald,
           java.lang.String bunkanumer,
           java.math.BigDecimal annarKostnadur) {
           this.bankanumer = bankanumer;
           this.hofudbok = hofudbok;
           this.krofunumer = krofunumer;
           this.gjalddagi = gjalddagi;
           this.kennitalaKrofuhafa = kennitalaKrofuhafa;
           this.kennitalaGreidanda = kennitalaGreidanda;
           this.vidskiptanumer = vidskiptanumer;
           this.upphaed = upphaed;
           this.eindagi = eindagi;
           this.audkenni = audkenni;
           this.tilvisun = tilvisun;
           this.nidurfellingardagur = nidurfellingardagur;
           this.textalykill = textalykill;
           this.innlausnarbanki = innlausnarbanki;
           this.hreyfingardagur = hreyfingardagur;
           this.sedilnumer = sedilnumer;
           this.vanskilagjaldskodi = vanskilagjaldskodi;
           this.vanskilagjald1 = vanskilagjald1;
           this.vanskilagjald2 = vanskilagjald2;
           this.dagafjoldiVanskilagjalds1 = dagafjoldiVanskilagjalds1;
           this.dagafjoldiVanskilagjalds2 = dagafjoldiVanskilagjalds2;
           this.afslattarkodi = afslattarkodi;
           this.afslattur1 = afslattur1;
           this.afslattur2 = afslattur2;
           this.dagafjoldiAfslattar1 = dagafjoldiAfslattar1;
           this.dagafjoldiAfslattar2 = dagafjoldiAfslattar2;
           this.tilkynningarOgGreidslugjald1 = tilkynningarOgGreidslugjald1;
           this.tilkynningarOgGreidslugjald2 = tilkynningarOgGreidslugjald2;
           this.annarVanskilakostnadur = annarVanskilakostnadur;
           this.innborgunarkodi = innborgunarkodi;
           this.greidslukodi = greidslukodi;
           this.drattavaxtastofnkodi = drattavaxtastofnkodi;
           this.drattavaxtaregla = drattavaxtaregla;
           this.drattavaxtaprosenta = drattavaxtaprosenta;
           this.gengistegund = gengistegund;
           this.mynt = mynt;
           this.gengisbanki = gengisbanki;
           this.gengiskodi = gengiskodi;
           this.birtingarkodi = birtingarkodi;
           this.vaxtadagsetning = vaxtadagsetning;
           this.innborgunardagur = innborgunardagur;
           this.innborgunarupphaed = innborgunarupphaed;
           this.drattarvextir = drattarvextir;
           this.greiddUpphaed = greiddUpphaed;
           this.skattaUpphaed = skattaUpphaed;
           this.veitturAfslattur = veitturAfslattur;
           this.greittTilkynningargjald = greittTilkynningargjald;
           this.greittVanskilagjald = greittVanskilagjald;
           this.greiddurAnnarVanskilakostnadur = greiddurAnnarVanskilakostnadur;
           this.greiddurAnnarKostnadur = greiddurAnnarKostnadur;
           this.ogreittTilkynningargjald = ogreittTilkynningargjald;
           this.bunkanumer = bunkanumer;
           this.annarKostnadur = annarKostnadur;
    }


    /**
     * Gets the bankanumer value for this Greidsla.
     * 
     * @return bankanumer
     */
    public int getBankanumer() {
        return bankanumer;
    }


    /**
     * Sets the bankanumer value for this Greidsla.
     * 
     * @param bankanumer
     */
    public void setBankanumer(int bankanumer) {
        this.bankanumer = bankanumer;
    }


    /**
     * Gets the hofudbok value for this Greidsla.
     * 
     * @return hofudbok
     */
    public int getHofudbok() {
        return hofudbok;
    }


    /**
     * Sets the hofudbok value for this Greidsla.
     * 
     * @param hofudbok
     */
    public void setHofudbok(int hofudbok) {
        this.hofudbok = hofudbok;
    }


    /**
     * Gets the krofunumer value for this Greidsla.
     * 
     * @return krofunumer
     */
    public int getKrofunumer() {
        return krofunumer;
    }


    /**
     * Sets the krofunumer value for this Greidsla.
     * 
     * @param krofunumer
     */
    public void setKrofunumer(int krofunumer) {
        this.krofunumer = krofunumer;
    }


    /**
     * Gets the gjalddagi value for this Greidsla.
     * 
     * @return gjalddagi
     */
    public java.util.Calendar getGjalddagi() {
        return gjalddagi;
    }


    /**
     * Sets the gjalddagi value for this Greidsla.
     * 
     * @param gjalddagi
     */
    public void setGjalddagi(java.util.Calendar gjalddagi) {
        this.gjalddagi = gjalddagi;
    }


    /**
     * Gets the kennitalaKrofuhafa value for this Greidsla.
     * 
     * @return kennitalaKrofuhafa
     */
    public java.lang.String getKennitalaKrofuhafa() {
        return kennitalaKrofuhafa;
    }


    /**
     * Sets the kennitalaKrofuhafa value for this Greidsla.
     * 
     * @param kennitalaKrofuhafa
     */
    public void setKennitalaKrofuhafa(java.lang.String kennitalaKrofuhafa) {
        this.kennitalaKrofuhafa = kennitalaKrofuhafa;
    }


    /**
     * Gets the kennitalaGreidanda value for this Greidsla.
     * 
     * @return kennitalaGreidanda
     */
    public java.lang.String getKennitalaGreidanda() {
        return kennitalaGreidanda;
    }


    /**
     * Sets the kennitalaGreidanda value for this Greidsla.
     * 
     * @param kennitalaGreidanda
     */
    public void setKennitalaGreidanda(java.lang.String kennitalaGreidanda) {
        this.kennitalaGreidanda = kennitalaGreidanda;
    }


    /**
     * Gets the vidskiptanumer value for this Greidsla.
     * 
     * @return vidskiptanumer
     */
    public java.lang.String getVidskiptanumer() {
        return vidskiptanumer;
    }


    /**
     * Sets the vidskiptanumer value for this Greidsla.
     * 
     * @param vidskiptanumer
     */
    public void setVidskiptanumer(java.lang.String vidskiptanumer) {
        this.vidskiptanumer = vidskiptanumer;
    }


    /**
     * Gets the upphaed value for this Greidsla.
     * 
     * @return upphaed
     */
    public java.math.BigDecimal getUpphaed() {
        return upphaed;
    }


    /**
     * Sets the upphaed value for this Greidsla.
     * 
     * @param upphaed
     */
    public void setUpphaed(java.math.BigDecimal upphaed) {
        this.upphaed = upphaed;
    }


    /**
     * Gets the eindagi value for this Greidsla.
     * 
     * @return eindagi
     */
    public java.util.Calendar getEindagi() {
        return eindagi;
    }


    /**
     * Sets the eindagi value for this Greidsla.
     * 
     * @param eindagi
     */
    public void setEindagi(java.util.Calendar eindagi) {
        this.eindagi = eindagi;
    }


    /**
     * Gets the audkenni value for this Greidsla.
     * 
     * @return audkenni
     */
    public java.lang.String getAudkenni() {
        return audkenni;
    }


    /**
     * Sets the audkenni value for this Greidsla.
     * 
     * @param audkenni
     */
    public void setAudkenni(java.lang.String audkenni) {
        this.audkenni = audkenni;
    }


    /**
     * Gets the tilvisun value for this Greidsla.
     * 
     * @return tilvisun
     */
    public java.lang.String getTilvisun() {
        return tilvisun;
    }


    /**
     * Sets the tilvisun value for this Greidsla.
     * 
     * @param tilvisun
     */
    public void setTilvisun(java.lang.String tilvisun) {
        this.tilvisun = tilvisun;
    }


    /**
     * Gets the nidurfellingardagur value for this Greidsla.
     * 
     * @return nidurfellingardagur
     */
    public java.util.Calendar getNidurfellingardagur() {
        return nidurfellingardagur;
    }


    /**
     * Sets the nidurfellingardagur value for this Greidsla.
     * 
     * @param nidurfellingardagur
     */
    public void setNidurfellingardagur(java.util.Calendar nidurfellingardagur) {
        this.nidurfellingardagur = nidurfellingardagur;
    }


    /**
     * Gets the textalykill value for this Greidsla.
     * 
     * @return textalykill
     */
    public java.lang.String getTextalykill() {
        return textalykill;
    }


    /**
     * Sets the textalykill value for this Greidsla.
     * 
     * @param textalykill
     */
    public void setTextalykill(java.lang.String textalykill) {
        this.textalykill = textalykill;
    }


    /**
     * Gets the innlausnarbanki value for this Greidsla.
     * 
     * @return innlausnarbanki
     */
    public int getInnlausnarbanki() {
        return innlausnarbanki;
    }


    /**
     * Sets the innlausnarbanki value for this Greidsla.
     * 
     * @param innlausnarbanki
     */
    public void setInnlausnarbanki(int innlausnarbanki) {
        this.innlausnarbanki = innlausnarbanki;
    }


    /**
     * Gets the hreyfingardagur value for this Greidsla.
     * 
     * @return hreyfingardagur
     */
    public java.util.Calendar getHreyfingardagur() {
        return hreyfingardagur;
    }


    /**
     * Sets the hreyfingardagur value for this Greidsla.
     * 
     * @param hreyfingardagur
     */
    public void setHreyfingardagur(java.util.Calendar hreyfingardagur) {
        this.hreyfingardagur = hreyfingardagur;
    }


    /**
     * Gets the sedilnumer value for this Greidsla.
     * 
     * @return sedilnumer
     */
    public java.lang.String getSedilnumer() {
        return sedilnumer;
    }


    /**
     * Sets the sedilnumer value for this Greidsla.
     * 
     * @param sedilnumer
     */
    public void setSedilnumer(java.lang.String sedilnumer) {
        this.sedilnumer = sedilnumer;
    }


    /**
     * Gets the vanskilagjaldskodi value for this Greidsla.
     * 
     * @return vanskilagjaldskodi
     */
    public java.lang.String getVanskilagjaldskodi() {
        return vanskilagjaldskodi;
    }


    /**
     * Sets the vanskilagjaldskodi value for this Greidsla.
     * 
     * @param vanskilagjaldskodi
     */
    public void setVanskilagjaldskodi(java.lang.String vanskilagjaldskodi) {
        this.vanskilagjaldskodi = vanskilagjaldskodi;
    }


    /**
     * Gets the vanskilagjald1 value for this Greidsla.
     * 
     * @return vanskilagjald1
     */
    public java.math.BigDecimal getVanskilagjald1() {
        return vanskilagjald1;
    }


    /**
     * Sets the vanskilagjald1 value for this Greidsla.
     * 
     * @param vanskilagjald1
     */
    public void setVanskilagjald1(java.math.BigDecimal vanskilagjald1) {
        this.vanskilagjald1 = vanskilagjald1;
    }


    /**
     * Gets the vanskilagjald2 value for this Greidsla.
     * 
     * @return vanskilagjald2
     */
    public java.math.BigDecimal getVanskilagjald2() {
        return vanskilagjald2;
    }


    /**
     * Sets the vanskilagjald2 value for this Greidsla.
     * 
     * @param vanskilagjald2
     */
    public void setVanskilagjald2(java.math.BigDecimal vanskilagjald2) {
        this.vanskilagjald2 = vanskilagjald2;
    }


    /**
     * Gets the dagafjoldiVanskilagjalds1 value for this Greidsla.
     * 
     * @return dagafjoldiVanskilagjalds1
     */
    public int getDagafjoldiVanskilagjalds1() {
        return dagafjoldiVanskilagjalds1;
    }


    /**
     * Sets the dagafjoldiVanskilagjalds1 value for this Greidsla.
     * 
     * @param dagafjoldiVanskilagjalds1
     */
    public void setDagafjoldiVanskilagjalds1(int dagafjoldiVanskilagjalds1) {
        this.dagafjoldiVanskilagjalds1 = dagafjoldiVanskilagjalds1;
    }


    /**
     * Gets the dagafjoldiVanskilagjalds2 value for this Greidsla.
     * 
     * @return dagafjoldiVanskilagjalds2
     */
    public int getDagafjoldiVanskilagjalds2() {
        return dagafjoldiVanskilagjalds2;
    }


    /**
     * Sets the dagafjoldiVanskilagjalds2 value for this Greidsla.
     * 
     * @param dagafjoldiVanskilagjalds2
     */
    public void setDagafjoldiVanskilagjalds2(int dagafjoldiVanskilagjalds2) {
        this.dagafjoldiVanskilagjalds2 = dagafjoldiVanskilagjalds2;
    }


    /**
     * Gets the afslattarkodi value for this Greidsla.
     * 
     * @return afslattarkodi
     */
    public java.lang.String getAfslattarkodi() {
        return afslattarkodi;
    }


    /**
     * Sets the afslattarkodi value for this Greidsla.
     * 
     * @param afslattarkodi
     */
    public void setAfslattarkodi(java.lang.String afslattarkodi) {
        this.afslattarkodi = afslattarkodi;
    }


    /**
     * Gets the afslattur1 value for this Greidsla.
     * 
     * @return afslattur1
     */
    public java.math.BigDecimal getAfslattur1() {
        return afslattur1;
    }


    /**
     * Sets the afslattur1 value for this Greidsla.
     * 
     * @param afslattur1
     */
    public void setAfslattur1(java.math.BigDecimal afslattur1) {
        this.afslattur1 = afslattur1;
    }


    /**
     * Gets the afslattur2 value for this Greidsla.
     * 
     * @return afslattur2
     */
    public java.math.BigDecimal getAfslattur2() {
        return afslattur2;
    }


    /**
     * Sets the afslattur2 value for this Greidsla.
     * 
     * @param afslattur2
     */
    public void setAfslattur2(java.math.BigDecimal afslattur2) {
        this.afslattur2 = afslattur2;
    }


    /**
     * Gets the dagafjoldiAfslattar1 value for this Greidsla.
     * 
     * @return dagafjoldiAfslattar1
     */
    public int getDagafjoldiAfslattar1() {
        return dagafjoldiAfslattar1;
    }


    /**
     * Sets the dagafjoldiAfslattar1 value for this Greidsla.
     * 
     * @param dagafjoldiAfslattar1
     */
    public void setDagafjoldiAfslattar1(int dagafjoldiAfslattar1) {
        this.dagafjoldiAfslattar1 = dagafjoldiAfslattar1;
    }


    /**
     * Gets the dagafjoldiAfslattar2 value for this Greidsla.
     * 
     * @return dagafjoldiAfslattar2
     */
    public int getDagafjoldiAfslattar2() {
        return dagafjoldiAfslattar2;
    }


    /**
     * Sets the dagafjoldiAfslattar2 value for this Greidsla.
     * 
     * @param dagafjoldiAfslattar2
     */
    public void setDagafjoldiAfslattar2(int dagafjoldiAfslattar2) {
        this.dagafjoldiAfslattar2 = dagafjoldiAfslattar2;
    }


    /**
     * Gets the tilkynningarOgGreidslugjald1 value for this Greidsla.
     * 
     * @return tilkynningarOgGreidslugjald1
     */
    public java.math.BigDecimal getTilkynningarOgGreidslugjald1() {
        return tilkynningarOgGreidslugjald1;
    }


    /**
     * Sets the tilkynningarOgGreidslugjald1 value for this Greidsla.
     * 
     * @param tilkynningarOgGreidslugjald1
     */
    public void setTilkynningarOgGreidslugjald1(java.math.BigDecimal tilkynningarOgGreidslugjald1) {
        this.tilkynningarOgGreidslugjald1 = tilkynningarOgGreidslugjald1;
    }


    /**
     * Gets the tilkynningarOgGreidslugjald2 value for this Greidsla.
     * 
     * @return tilkynningarOgGreidslugjald2
     */
    public java.math.BigDecimal getTilkynningarOgGreidslugjald2() {
        return tilkynningarOgGreidslugjald2;
    }


    /**
     * Sets the tilkynningarOgGreidslugjald2 value for this Greidsla.
     * 
     * @param tilkynningarOgGreidslugjald2
     */
    public void setTilkynningarOgGreidslugjald2(java.math.BigDecimal tilkynningarOgGreidslugjald2) {
        this.tilkynningarOgGreidslugjald2 = tilkynningarOgGreidslugjald2;
    }


    /**
     * Gets the annarVanskilakostnadur value for this Greidsla.
     * 
     * @return annarVanskilakostnadur
     */
    public java.math.BigDecimal getAnnarVanskilakostnadur() {
        return annarVanskilakostnadur;
    }


    /**
     * Sets the annarVanskilakostnadur value for this Greidsla.
     * 
     * @param annarVanskilakostnadur
     */
    public void setAnnarVanskilakostnadur(java.math.BigDecimal annarVanskilakostnadur) {
        this.annarVanskilakostnadur = annarVanskilakostnadur;
    }


    /**
     * Gets the innborgunarkodi value for this Greidsla.
     * 
     * @return innborgunarkodi
     */
    public java.lang.String getInnborgunarkodi() {
        return innborgunarkodi;
    }


    /**
     * Sets the innborgunarkodi value for this Greidsla.
     * 
     * @param innborgunarkodi
     */
    public void setInnborgunarkodi(java.lang.String innborgunarkodi) {
        this.innborgunarkodi = innborgunarkodi;
    }


    /**
     * Gets the greidslukodi value for this Greidsla.
     * 
     * @return greidslukodi
     */
    public java.lang.String getGreidslukodi() {
        return greidslukodi;
    }


    /**
     * Sets the greidslukodi value for this Greidsla.
     * 
     * @param greidslukodi
     */
    public void setGreidslukodi(java.lang.String greidslukodi) {
        this.greidslukodi = greidslukodi;
    }


    /**
     * Gets the drattavaxtastofnkodi value for this Greidsla.
     * 
     * @return drattavaxtastofnkodi
     */
    public java.lang.String getDrattavaxtastofnkodi() {
        return drattavaxtastofnkodi;
    }


    /**
     * Sets the drattavaxtastofnkodi value for this Greidsla.
     * 
     * @param drattavaxtastofnkodi
     */
    public void setDrattavaxtastofnkodi(java.lang.String drattavaxtastofnkodi) {
        this.drattavaxtastofnkodi = drattavaxtastofnkodi;
    }


    /**
     * Gets the drattavaxtaregla value for this Greidsla.
     * 
     * @return drattavaxtaregla
     */
    public java.lang.String getDrattavaxtaregla() {
        return drattavaxtaregla;
    }


    /**
     * Sets the drattavaxtaregla value for this Greidsla.
     * 
     * @param drattavaxtaregla
     */
    public void setDrattavaxtaregla(java.lang.String drattavaxtaregla) {
        this.drattavaxtaregla = drattavaxtaregla;
    }


    /**
     * Gets the drattavaxtaprosenta value for this Greidsla.
     * 
     * @return drattavaxtaprosenta
     */
    public java.math.BigDecimal getDrattavaxtaprosenta() {
        return drattavaxtaprosenta;
    }


    /**
     * Sets the drattavaxtaprosenta value for this Greidsla.
     * 
     * @param drattavaxtaprosenta
     */
    public void setDrattavaxtaprosenta(java.math.BigDecimal drattavaxtaprosenta) {
        this.drattavaxtaprosenta = drattavaxtaprosenta;
    }


    /**
     * Gets the gengistegund value for this Greidsla.
     * 
     * @return gengistegund
     */
    public java.lang.String getGengistegund() {
        return gengistegund;
    }


    /**
     * Sets the gengistegund value for this Greidsla.
     * 
     * @param gengistegund
     */
    public void setGengistegund(java.lang.String gengistegund) {
        this.gengistegund = gengistegund;
    }


    /**
     * Gets the mynt value for this Greidsla.
     * 
     * @return mynt
     */
    public java.lang.String getMynt() {
        return mynt;
    }


    /**
     * Sets the mynt value for this Greidsla.
     * 
     * @param mynt
     */
    public void setMynt(java.lang.String mynt) {
        this.mynt = mynt;
    }


    /**
     * Gets the gengisbanki value for this Greidsla.
     * 
     * @return gengisbanki
     */
    public int getGengisbanki() {
        return gengisbanki;
    }


    /**
     * Sets the gengisbanki value for this Greidsla.
     * 
     * @param gengisbanki
     */
    public void setGengisbanki(int gengisbanki) {
        this.gengisbanki = gengisbanki;
    }


    /**
     * Gets the gengiskodi value for this Greidsla.
     * 
     * @return gengiskodi
     */
    public java.lang.String getGengiskodi() {
        return gengiskodi;
    }


    /**
     * Sets the gengiskodi value for this Greidsla.
     * 
     * @param gengiskodi
     */
    public void setGengiskodi(java.lang.String gengiskodi) {
        this.gengiskodi = gengiskodi;
    }


    /**
     * Gets the birtingarkodi value for this Greidsla.
     * 
     * @return birtingarkodi
     */
    public java.lang.String getBirtingarkodi() {
        return birtingarkodi;
    }


    /**
     * Sets the birtingarkodi value for this Greidsla.
     * 
     * @param birtingarkodi
     */
    public void setBirtingarkodi(java.lang.String birtingarkodi) {
        this.birtingarkodi = birtingarkodi;
    }


    /**
     * Gets the vaxtadagsetning value for this Greidsla.
     * 
     * @return vaxtadagsetning
     */
    public java.util.Calendar getVaxtadagsetning() {
        return vaxtadagsetning;
    }


    /**
     * Sets the vaxtadagsetning value for this Greidsla.
     * 
     * @param vaxtadagsetning
     */
    public void setVaxtadagsetning(java.util.Calendar vaxtadagsetning) {
        this.vaxtadagsetning = vaxtadagsetning;
    }


    /**
     * Gets the innborgunardagur value for this Greidsla.
     * 
     * @return innborgunardagur
     */
    public java.util.Calendar getInnborgunardagur() {
        return innborgunardagur;
    }


    /**
     * Sets the innborgunardagur value for this Greidsla.
     * 
     * @param innborgunardagur
     */
    public void setInnborgunardagur(java.util.Calendar innborgunardagur) {
        this.innborgunardagur = innborgunardagur;
    }


    /**
     * Gets the innborgunarupphaed value for this Greidsla.
     * 
     * @return innborgunarupphaed
     */
    public java.math.BigDecimal getInnborgunarupphaed() {
        return innborgunarupphaed;
    }


    /**
     * Sets the innborgunarupphaed value for this Greidsla.
     * 
     * @param innborgunarupphaed
     */
    public void setInnborgunarupphaed(java.math.BigDecimal innborgunarupphaed) {
        this.innborgunarupphaed = innborgunarupphaed;
    }


    /**
     * Gets the drattarvextir value for this Greidsla.
     * 
     * @return drattarvextir
     */
    public java.math.BigDecimal getDrattarvextir() {
        return drattarvextir;
    }


    /**
     * Sets the drattarvextir value for this Greidsla.
     * 
     * @param drattarvextir
     */
    public void setDrattarvextir(java.math.BigDecimal drattarvextir) {
        this.drattarvextir = drattarvextir;
    }


    /**
     * Gets the greiddUpphaed value for this Greidsla.
     * 
     * @return greiddUpphaed
     */
    public java.math.BigDecimal getGreiddUpphaed() {
        return greiddUpphaed;
    }


    /**
     * Sets the greiddUpphaed value for this Greidsla.
     * 
     * @param greiddUpphaed
     */
    public void setGreiddUpphaed(java.math.BigDecimal greiddUpphaed) {
        this.greiddUpphaed = greiddUpphaed;
    }


    /**
     * Gets the skattaUpphaed value for this Greidsla.
     * 
     * @return skattaUpphaed
     */
    public java.math.BigDecimal getSkattaUpphaed() {
        return skattaUpphaed;
    }


    /**
     * Sets the skattaUpphaed value for this Greidsla.
     * 
     * @param skattaUpphaed
     */
    public void setSkattaUpphaed(java.math.BigDecimal skattaUpphaed) {
        this.skattaUpphaed = skattaUpphaed;
    }


    /**
     * Gets the veitturAfslattur value for this Greidsla.
     * 
     * @return veitturAfslattur
     */
    public java.math.BigDecimal getVeitturAfslattur() {
        return veitturAfslattur;
    }


    /**
     * Sets the veitturAfslattur value for this Greidsla.
     * 
     * @param veitturAfslattur
     */
    public void setVeitturAfslattur(java.math.BigDecimal veitturAfslattur) {
        this.veitturAfslattur = veitturAfslattur;
    }


    /**
     * Gets the greittTilkynningargjald value for this Greidsla.
     * 
     * @return greittTilkynningargjald
     */
    public java.math.BigDecimal getGreittTilkynningargjald() {
        return greittTilkynningargjald;
    }


    /**
     * Sets the greittTilkynningargjald value for this Greidsla.
     * 
     * @param greittTilkynningargjald
     */
    public void setGreittTilkynningargjald(java.math.BigDecimal greittTilkynningargjald) {
        this.greittTilkynningargjald = greittTilkynningargjald;
    }


    /**
     * Gets the greittVanskilagjald value for this Greidsla.
     * 
     * @return greittVanskilagjald
     */
    public java.math.BigDecimal getGreittVanskilagjald() {
        return greittVanskilagjald;
    }


    /**
     * Sets the greittVanskilagjald value for this Greidsla.
     * 
     * @param greittVanskilagjald
     */
    public void setGreittVanskilagjald(java.math.BigDecimal greittVanskilagjald) {
        this.greittVanskilagjald = greittVanskilagjald;
    }


    /**
     * Gets the greiddurAnnarVanskilakostnadur value for this Greidsla.
     * 
     * @return greiddurAnnarVanskilakostnadur
     */
    public java.math.BigDecimal getGreiddurAnnarVanskilakostnadur() {
        return greiddurAnnarVanskilakostnadur;
    }


    /**
     * Sets the greiddurAnnarVanskilakostnadur value for this Greidsla.
     * 
     * @param greiddurAnnarVanskilakostnadur
     */
    public void setGreiddurAnnarVanskilakostnadur(java.math.BigDecimal greiddurAnnarVanskilakostnadur) {
        this.greiddurAnnarVanskilakostnadur = greiddurAnnarVanskilakostnadur;
    }


    /**
     * Gets the greiddurAnnarKostnadur value for this Greidsla.
     * 
     * @return greiddurAnnarKostnadur
     */
    public java.math.BigDecimal getGreiddurAnnarKostnadur() {
        return greiddurAnnarKostnadur;
    }


    /**
     * Sets the greiddurAnnarKostnadur value for this Greidsla.
     * 
     * @param greiddurAnnarKostnadur
     */
    public void setGreiddurAnnarKostnadur(java.math.BigDecimal greiddurAnnarKostnadur) {
        this.greiddurAnnarKostnadur = greiddurAnnarKostnadur;
    }


    /**
     * Gets the ogreittTilkynningargjald value for this Greidsla.
     * 
     * @return ogreittTilkynningargjald
     */
    public java.math.BigDecimal getOgreittTilkynningargjald() {
        return ogreittTilkynningargjald;
    }


    /**
     * Sets the ogreittTilkynningargjald value for this Greidsla.
     * 
     * @param ogreittTilkynningargjald
     */
    public void setOgreittTilkynningargjald(java.math.BigDecimal ogreittTilkynningargjald) {
        this.ogreittTilkynningargjald = ogreittTilkynningargjald;
    }


    /**
     * Gets the bunkanumer value for this Greidsla.
     * 
     * @return bunkanumer
     */
    public java.lang.String getBunkanumer() {
        return bunkanumer;
    }


    /**
     * Sets the bunkanumer value for this Greidsla.
     * 
     * @param bunkanumer
     */
    public void setBunkanumer(java.lang.String bunkanumer) {
        this.bunkanumer = bunkanumer;
    }


    /**
     * Gets the annarKostnadur value for this Greidsla.
     * 
     * @return annarKostnadur
     */
    public java.math.BigDecimal getAnnarKostnadur() {
        return annarKostnadur;
    }


    /**
     * Sets the annarKostnadur value for this Greidsla.
     * 
     * @param annarKostnadur
     */
    public void setAnnarKostnadur(java.math.BigDecimal annarKostnadur) {
        this.annarKostnadur = annarKostnadur;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Greidsla)) return false;
        Greidsla other = (Greidsla) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.bankanumer == other.getBankanumer() &&
            this.hofudbok == other.getHofudbok() &&
            this.krofunumer == other.getKrofunumer() &&
            ((this.gjalddagi==null && other.getGjalddagi()==null) || 
             (this.gjalddagi!=null &&
              this.gjalddagi.equals(other.getGjalddagi()))) &&
            ((this.kennitalaKrofuhafa==null && other.getKennitalaKrofuhafa()==null) || 
             (this.kennitalaKrofuhafa!=null &&
              this.kennitalaKrofuhafa.equals(other.getKennitalaKrofuhafa()))) &&
            ((this.kennitalaGreidanda==null && other.getKennitalaGreidanda()==null) || 
             (this.kennitalaGreidanda!=null &&
              this.kennitalaGreidanda.equals(other.getKennitalaGreidanda()))) &&
            ((this.vidskiptanumer==null && other.getVidskiptanumer()==null) || 
             (this.vidskiptanumer!=null &&
              this.vidskiptanumer.equals(other.getVidskiptanumer()))) &&
            ((this.upphaed==null && other.getUpphaed()==null) || 
             (this.upphaed!=null &&
              this.upphaed.equals(other.getUpphaed()))) &&
            ((this.eindagi==null && other.getEindagi()==null) || 
             (this.eindagi!=null &&
              this.eindagi.equals(other.getEindagi()))) &&
            ((this.audkenni==null && other.getAudkenni()==null) || 
             (this.audkenni!=null &&
              this.audkenni.equals(other.getAudkenni()))) &&
            ((this.tilvisun==null && other.getTilvisun()==null) || 
             (this.tilvisun!=null &&
              this.tilvisun.equals(other.getTilvisun()))) &&
            ((this.nidurfellingardagur==null && other.getNidurfellingardagur()==null) || 
             (this.nidurfellingardagur!=null &&
              this.nidurfellingardagur.equals(other.getNidurfellingardagur()))) &&
            ((this.textalykill==null && other.getTextalykill()==null) || 
             (this.textalykill!=null &&
              this.textalykill.equals(other.getTextalykill()))) &&
            this.innlausnarbanki == other.getInnlausnarbanki() &&
            ((this.hreyfingardagur==null && other.getHreyfingardagur()==null) || 
             (this.hreyfingardagur!=null &&
              this.hreyfingardagur.equals(other.getHreyfingardagur()))) &&
            ((this.sedilnumer==null && other.getSedilnumer()==null) || 
             (this.sedilnumer!=null &&
              this.sedilnumer.equals(other.getSedilnumer()))) &&
            ((this.vanskilagjaldskodi==null && other.getVanskilagjaldskodi()==null) || 
             (this.vanskilagjaldskodi!=null &&
              this.vanskilagjaldskodi.equals(other.getVanskilagjaldskodi()))) &&
            ((this.vanskilagjald1==null && other.getVanskilagjald1()==null) || 
             (this.vanskilagjald1!=null &&
              this.vanskilagjald1.equals(other.getVanskilagjald1()))) &&
            ((this.vanskilagjald2==null && other.getVanskilagjald2()==null) || 
             (this.vanskilagjald2!=null &&
              this.vanskilagjald2.equals(other.getVanskilagjald2()))) &&
            this.dagafjoldiVanskilagjalds1 == other.getDagafjoldiVanskilagjalds1() &&
            this.dagafjoldiVanskilagjalds2 == other.getDagafjoldiVanskilagjalds2() &&
            ((this.afslattarkodi==null && other.getAfslattarkodi()==null) || 
             (this.afslattarkodi!=null &&
              this.afslattarkodi.equals(other.getAfslattarkodi()))) &&
            ((this.afslattur1==null && other.getAfslattur1()==null) || 
             (this.afslattur1!=null &&
              this.afslattur1.equals(other.getAfslattur1()))) &&
            ((this.afslattur2==null && other.getAfslattur2()==null) || 
             (this.afslattur2!=null &&
              this.afslattur2.equals(other.getAfslattur2()))) &&
            this.dagafjoldiAfslattar1 == other.getDagafjoldiAfslattar1() &&
            this.dagafjoldiAfslattar2 == other.getDagafjoldiAfslattar2() &&
            ((this.tilkynningarOgGreidslugjald1==null && other.getTilkynningarOgGreidslugjald1()==null) || 
             (this.tilkynningarOgGreidslugjald1!=null &&
              this.tilkynningarOgGreidslugjald1.equals(other.getTilkynningarOgGreidslugjald1()))) &&
            ((this.tilkynningarOgGreidslugjald2==null && other.getTilkynningarOgGreidslugjald2()==null) || 
             (this.tilkynningarOgGreidslugjald2!=null &&
              this.tilkynningarOgGreidslugjald2.equals(other.getTilkynningarOgGreidslugjald2()))) &&
            ((this.annarVanskilakostnadur==null && other.getAnnarVanskilakostnadur()==null) || 
             (this.annarVanskilakostnadur!=null &&
              this.annarVanskilakostnadur.equals(other.getAnnarVanskilakostnadur()))) &&
            ((this.innborgunarkodi==null && other.getInnborgunarkodi()==null) || 
             (this.innborgunarkodi!=null &&
              this.innborgunarkodi.equals(other.getInnborgunarkodi()))) &&
            ((this.greidslukodi==null && other.getGreidslukodi()==null) || 
             (this.greidslukodi!=null &&
              this.greidslukodi.equals(other.getGreidslukodi()))) &&
            ((this.drattavaxtastofnkodi==null && other.getDrattavaxtastofnkodi()==null) || 
             (this.drattavaxtastofnkodi!=null &&
              this.drattavaxtastofnkodi.equals(other.getDrattavaxtastofnkodi()))) &&
            ((this.drattavaxtaregla==null && other.getDrattavaxtaregla()==null) || 
             (this.drattavaxtaregla!=null &&
              this.drattavaxtaregla.equals(other.getDrattavaxtaregla()))) &&
            ((this.drattavaxtaprosenta==null && other.getDrattavaxtaprosenta()==null) || 
             (this.drattavaxtaprosenta!=null &&
              this.drattavaxtaprosenta.equals(other.getDrattavaxtaprosenta()))) &&
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
            ((this.birtingarkodi==null && other.getBirtingarkodi()==null) || 
             (this.birtingarkodi!=null &&
              this.birtingarkodi.equals(other.getBirtingarkodi()))) &&
            ((this.vaxtadagsetning==null && other.getVaxtadagsetning()==null) || 
             (this.vaxtadagsetning!=null &&
              this.vaxtadagsetning.equals(other.getVaxtadagsetning()))) &&
            ((this.innborgunardagur==null && other.getInnborgunardagur()==null) || 
             (this.innborgunardagur!=null &&
              this.innborgunardagur.equals(other.getInnborgunardagur()))) &&
            ((this.innborgunarupphaed==null && other.getInnborgunarupphaed()==null) || 
             (this.innborgunarupphaed!=null &&
              this.innborgunarupphaed.equals(other.getInnborgunarupphaed()))) &&
            ((this.drattarvextir==null && other.getDrattarvextir()==null) || 
             (this.drattarvextir!=null &&
              this.drattarvextir.equals(other.getDrattarvextir()))) &&
            ((this.greiddUpphaed==null && other.getGreiddUpphaed()==null) || 
             (this.greiddUpphaed!=null &&
              this.greiddUpphaed.equals(other.getGreiddUpphaed()))) &&
            ((this.skattaUpphaed==null && other.getSkattaUpphaed()==null) || 
             (this.skattaUpphaed!=null &&
              this.skattaUpphaed.equals(other.getSkattaUpphaed()))) &&
            ((this.veitturAfslattur==null && other.getVeitturAfslattur()==null) || 
             (this.veitturAfslattur!=null &&
              this.veitturAfslattur.equals(other.getVeitturAfslattur()))) &&
            ((this.greittTilkynningargjald==null && other.getGreittTilkynningargjald()==null) || 
             (this.greittTilkynningargjald!=null &&
              this.greittTilkynningargjald.equals(other.getGreittTilkynningargjald()))) &&
            ((this.greittVanskilagjald==null && other.getGreittVanskilagjald()==null) || 
             (this.greittVanskilagjald!=null &&
              this.greittVanskilagjald.equals(other.getGreittVanskilagjald()))) &&
            ((this.greiddurAnnarVanskilakostnadur==null && other.getGreiddurAnnarVanskilakostnadur()==null) || 
             (this.greiddurAnnarVanskilakostnadur!=null &&
              this.greiddurAnnarVanskilakostnadur.equals(other.getGreiddurAnnarVanskilakostnadur()))) &&
            ((this.greiddurAnnarKostnadur==null && other.getGreiddurAnnarKostnadur()==null) || 
             (this.greiddurAnnarKostnadur!=null &&
              this.greiddurAnnarKostnadur.equals(other.getGreiddurAnnarKostnadur()))) &&
            ((this.ogreittTilkynningargjald==null && other.getOgreittTilkynningargjald()==null) || 
             (this.ogreittTilkynningargjald!=null &&
              this.ogreittTilkynningargjald.equals(other.getOgreittTilkynningargjald()))) &&
            ((this.bunkanumer==null && other.getBunkanumer()==null) || 
             (this.bunkanumer!=null &&
              this.bunkanumer.equals(other.getBunkanumer()))) &&
            ((this.annarKostnadur==null && other.getAnnarKostnadur()==null) || 
             (this.annarKostnadur!=null &&
              this.annarKostnadur.equals(other.getAnnarKostnadur())));
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
        _hashCode += getBankanumer();
        _hashCode += getHofudbok();
        _hashCode += getKrofunumer();
        if (getGjalddagi() != null) {
            _hashCode += getGjalddagi().hashCode();
        }
        if (getKennitalaKrofuhafa() != null) {
            _hashCode += getKennitalaKrofuhafa().hashCode();
        }
        if (getKennitalaGreidanda() != null) {
            _hashCode += getKennitalaGreidanda().hashCode();
        }
        if (getVidskiptanumer() != null) {
            _hashCode += getVidskiptanumer().hashCode();
        }
        if (getUpphaed() != null) {
            _hashCode += getUpphaed().hashCode();
        }
        if (getEindagi() != null) {
            _hashCode += getEindagi().hashCode();
        }
        if (getAudkenni() != null) {
            _hashCode += getAudkenni().hashCode();
        }
        if (getTilvisun() != null) {
            _hashCode += getTilvisun().hashCode();
        }
        if (getNidurfellingardagur() != null) {
            _hashCode += getNidurfellingardagur().hashCode();
        }
        if (getTextalykill() != null) {
            _hashCode += getTextalykill().hashCode();
        }
        _hashCode += getInnlausnarbanki();
        if (getHreyfingardagur() != null) {
            _hashCode += getHreyfingardagur().hashCode();
        }
        if (getSedilnumer() != null) {
            _hashCode += getSedilnumer().hashCode();
        }
        if (getVanskilagjaldskodi() != null) {
            _hashCode += getVanskilagjaldskodi().hashCode();
        }
        if (getVanskilagjald1() != null) {
            _hashCode += getVanskilagjald1().hashCode();
        }
        if (getVanskilagjald2() != null) {
            _hashCode += getVanskilagjald2().hashCode();
        }
        _hashCode += getDagafjoldiVanskilagjalds1();
        _hashCode += getDagafjoldiVanskilagjalds2();
        if (getAfslattarkodi() != null) {
            _hashCode += getAfslattarkodi().hashCode();
        }
        if (getAfslattur1() != null) {
            _hashCode += getAfslattur1().hashCode();
        }
        if (getAfslattur2() != null) {
            _hashCode += getAfslattur2().hashCode();
        }
        _hashCode += getDagafjoldiAfslattar1();
        _hashCode += getDagafjoldiAfslattar2();
        if (getTilkynningarOgGreidslugjald1() != null) {
            _hashCode += getTilkynningarOgGreidslugjald1().hashCode();
        }
        if (getTilkynningarOgGreidslugjald2() != null) {
            _hashCode += getTilkynningarOgGreidslugjald2().hashCode();
        }
        if (getAnnarVanskilakostnadur() != null) {
            _hashCode += getAnnarVanskilakostnadur().hashCode();
        }
        if (getInnborgunarkodi() != null) {
            _hashCode += getInnborgunarkodi().hashCode();
        }
        if (getGreidslukodi() != null) {
            _hashCode += getGreidslukodi().hashCode();
        }
        if (getDrattavaxtastofnkodi() != null) {
            _hashCode += getDrattavaxtastofnkodi().hashCode();
        }
        if (getDrattavaxtaregla() != null) {
            _hashCode += getDrattavaxtaregla().hashCode();
        }
        if (getDrattavaxtaprosenta() != null) {
            _hashCode += getDrattavaxtaprosenta().hashCode();
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
        if (getBirtingarkodi() != null) {
            _hashCode += getBirtingarkodi().hashCode();
        }
        if (getVaxtadagsetning() != null) {
            _hashCode += getVaxtadagsetning().hashCode();
        }
        if (getInnborgunardagur() != null) {
            _hashCode += getInnborgunardagur().hashCode();
        }
        if (getInnborgunarupphaed() != null) {
            _hashCode += getInnborgunarupphaed().hashCode();
        }
        if (getDrattarvextir() != null) {
            _hashCode += getDrattarvextir().hashCode();
        }
        if (getGreiddUpphaed() != null) {
            _hashCode += getGreiddUpphaed().hashCode();
        }
        if (getSkattaUpphaed() != null) {
            _hashCode += getSkattaUpphaed().hashCode();
        }
        if (getVeitturAfslattur() != null) {
            _hashCode += getVeitturAfslattur().hashCode();
        }
        if (getGreittTilkynningargjald() != null) {
            _hashCode += getGreittTilkynningargjald().hashCode();
        }
        if (getGreittVanskilagjald() != null) {
            _hashCode += getGreittVanskilagjald().hashCode();
        }
        if (getGreiddurAnnarVanskilakostnadur() != null) {
            _hashCode += getGreiddurAnnarVanskilakostnadur().hashCode();
        }
        if (getGreiddurAnnarKostnadur() != null) {
            _hashCode += getGreiddurAnnarKostnadur().hashCode();
        }
        if (getOgreittTilkynningargjald() != null) {
            _hashCode += getOgreittTilkynningargjald().hashCode();
        }
        if (getBunkanumer() != null) {
            _hashCode += getBunkanumer().hashCode();
        }
        if (getAnnarKostnadur() != null) {
            _hashCode += getAnnarKostnadur().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Greidsla.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ws.isb.is", "Greidsla"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
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
        elemField.setFieldName("gjalddagi");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Gjalddagi"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("kennitalaKrofuhafa");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "KennitalaKrofuhafa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("kennitalaGreidanda");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "KennitalaGreidanda"));
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
        elemField.setFieldName("upphaed");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Upphaed"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("eindagi");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Eindagi"));
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
        elemField.setFieldName("tilvisun");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Tilvisun"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nidurfellingardagur");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Nidurfellingardagur"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("textalykill");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Textalykill"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("innlausnarbanki");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Innlausnarbanki"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hreyfingardagur");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Hreyfingardagur"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
        elemField.setFieldName("vanskilagjaldskodi");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Vanskilagjaldskodi"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
        elemField.setFieldName("afslattarkodi");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Afslattarkodi"));
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
        elemField.setFieldName("annarVanskilakostnadur");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "AnnarVanskilakostnadur"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
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
        elemField.setFieldName("greidslukodi");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Greidslukodi"));
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
        elemField.setFieldName("drattavaxtaregla");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Drattavaxtaregla"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("drattavaxtaprosenta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Drattavaxtaprosenta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
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
        elemField.setFieldName("birtingarkodi");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Birtingarkodi"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vaxtadagsetning");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Vaxtadagsetning"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("innborgunardagur");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Innborgunardagur"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("innborgunarupphaed");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Innborgunarupphaed"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("drattarvextir");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Drattarvextir"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("greiddUpphaed");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "GreiddUpphaed"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("skattaUpphaed");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "SkattaUpphaed"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("veitturAfslattur");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "VeitturAfslattur"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("greittTilkynningargjald");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "GreittTilkynningargjald"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("greittVanskilagjald");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "GreittVanskilagjald"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("greiddurAnnarVanskilakostnadur");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "GreiddurAnnarVanskilakostnadur"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("greiddurAnnarKostnadur");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "GreiddurAnnarKostnadur"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ogreittTilkynningargjald");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "OgreittTilkynningargjald"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bunkanumer");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.isb.is", "Bunkanumer"));
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
