/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_IK_krafa_type.java,v 1.1 2005/03/15 11:35:03 birna Exp $
 */

package is.idega.block.finance.business.li.claims;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class LI_IK_krafa_type.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:03 $
 */
public class LI_IK_krafa_type implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _kt_krofuhafa
     */
    private java.lang.String _kt_krofuhafa;

    /**
     * Field _banki
     */
    private java.lang.String _banki;

    /**
     * Field _hofudbok
     */
    private java.lang.String _hofudbok;

    /**
     * Field _numer
     */
    private java.lang.String _numer;

    /**
     * Field _gjalddagi
     */
    private org.exolab.castor.types.Date _gjalddagi;

    /**
     * Field _kt_greidanda
     */
    private java.lang.String _kt_greidanda;

    /**
     * Field _nidurfellingardagur
     */
    private org.exolab.castor.types.Date _nidurfellingardagur;

    /**
     * Field _audkenni
     */
    private java.lang.String _audkenni;

    /**
     * Field _upphaed
     */
    private is.idega.block.finance.business.li.claims.Upphaed _upphaed;

    /**
     * Field _tilvisunarnumer
     */
    private java.lang.String _tilvisunarnumer;

    /**
     * Field _eindagi
     */
    private org.exolab.castor.types.Date _eindagi;

    /**
     * Field _sedilnumer
     */
    private java.lang.String _sedilnumer;

    /**
     * Field _vidskiptanumer
     */
    private java.lang.String _vidskiptanumer;

    /**
     * Field _tilkynningar_og_greidslugjald_1
     */
    private int _tilkynningar_og_greidslugjald_1;

    /**
     * keeps track of state for field:
     * _tilkynningar_og_greidslugjald_1
     */
    private boolean _has_tilkynningar_og_greidslugjald_1;

    /**
     * Field _tilkynningar_og_greidslugjald_2
     */
    private int _tilkynningar_og_greidslugjald_2;

    /**
     * keeps track of state for field:
     * _tilkynningar_og_greidslugjald_2
     */
    private boolean _has_tilkynningar_og_greidslugjald_2;

    /**
     * Field _vanskilagjald
     */
    private is.idega.block.finance.business.li.claims.Vanskilagjald _vanskilagjald;

    /**
     * Field _annar_kostnadur
     */
    private int _annar_kostnadur;

    /**
     * keeps track of state for field: _annar_kostnadur
     */
    private boolean _has_annar_kostnadur;

    /**
     * Field _annar_vanskila_kostnadur
     */
    private int _annar_vanskila_kostnadur;

    /**
     * keeps track of state for field: _annar_vanskila_kostnadur
     */
    private boolean _has_annar_vanskila_kostnadur;

    /**
     * Field _drattarvextir
     */
    private is.idega.block.finance.business.li.claims.Drattarvextir _drattarvextir;

    /**
     * Field _gengiskrafa
     */
    private is.idega.block.finance.business.li.claims.Gengiskrafa _gengiskrafa;

    /**
     * Field _greidsluregla
     */
    private is.idega.block.finance.business.li.claims.types.LI_IK_greidsluregla_type _greidsluregla;

    /**
     * Field _afslattur
     */
    private is.idega.block.finance.business.li.claims.Afslattur _afslattur;

    /**
     * Field _innborgunarregla
     */
    private is.idega.block.finance.business.li.claims.types.LI_IK_innborgunarregla_type _innborgunarregla;

    /**
     * Field _birtingarkerfi
     */
    private is.idega.block.finance.business.li.claims.Birtingarkerfi _birtingarkerfi;

    /**
     * Field _birting
     */
    private is.idega.block.finance.business.li.claims.Birting _birting;

    /**
     * Field _aunnin_kostnadur
     */
    private int _aunnin_kostnadur;

    /**
     * keeps track of state for field: _aunnin_kostnadur
     */
    private boolean _has_aunnin_kostnadur;

    /**
     * Field _astand
     */
    private is.idega.block.finance.business.li.claims.types.LI_IK_astand_type _astand;


      //----------------/
     //- Constructors -/
    //----------------/

    public LI_IK_krafa_type() {
        super();
    } //-- is.idega.block.finance.business.li.claims.LI_IK_krafa_type()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteAnnar_kostnadur
     * 
     */
    public void deleteAnnar_kostnadur()
    {
        this._has_annar_kostnadur= false;
    } //-- void deleteAnnar_kostnadur() 

    /**
     * Method deleteAnnar_vanskila_kostnadur
     * 
     */
    public void deleteAnnar_vanskila_kostnadur()
    {
        this._has_annar_vanskila_kostnadur= false;
    } //-- void deleteAnnar_vanskila_kostnadur() 

    /**
     * Method deleteAunnin_kostnadur
     * 
     */
    public void deleteAunnin_kostnadur()
    {
        this._has_aunnin_kostnadur= false;
    } //-- void deleteAunnin_kostnadur() 

    /**
     * Method deleteTilkynningar_og_greidslugjald_1
     * 
     */
    public void deleteTilkynningar_og_greidslugjald_1()
    {
        this._has_tilkynningar_og_greidslugjald_1= false;
    } //-- void deleteTilkynningar_og_greidslugjald_1() 

    /**
     * Method deleteTilkynningar_og_greidslugjald_2
     * 
     */
    public void deleteTilkynningar_og_greidslugjald_2()
    {
        this._has_tilkynningar_og_greidslugjald_2= false;
    } //-- void deleteTilkynningar_og_greidslugjald_2() 

    /**
     * Returns the value of field 'afslattur'.
     * 
     * @return Afslattur
     * @return the value of field 'afslattur'.
     */
    public is.idega.block.finance.business.li.claims.Afslattur getAfslattur()
    {
        return this._afslattur;
    } //-- is.idega.block.finance.business.li.claims.Afslattur getAfslattur() 

    /**
     * Returns the value of field 'annar_kostnadur'.
     * 
     * @return int
     * @return the value of field 'annar_kostnadur'.
     */
    public int getAnnar_kostnadur()
    {
        return this._annar_kostnadur;
    } //-- int getAnnar_kostnadur() 

    /**
     * Returns the value of field 'annar_vanskila_kostnadur'.
     * 
     * @return int
     * @return the value of field 'annar_vanskila_kostnadur'.
     */
    public int getAnnar_vanskila_kostnadur()
    {
        return this._annar_vanskila_kostnadur;
    } //-- int getAnnar_vanskila_kostnadur() 

    /**
     * Returns the value of field 'astand'.
     * 
     * @return LI_IK_astand_type
     * @return the value of field 'astand'.
     */
    public is.idega.block.finance.business.li.claims.types.LI_IK_astand_type getAstand()
    {
        return this._astand;
    } //-- is.idega.block.finance.business.li.claims.types.LI_IK_astand_type getAstand() 

    /**
     * Returns the value of field 'audkenni'.
     * 
     * @return String
     * @return the value of field 'audkenni'.
     */
    public java.lang.String getAudkenni()
    {
        return this._audkenni;
    } //-- java.lang.String getAudkenni() 

    /**
     * Returns the value of field 'aunnin_kostnadur'.
     * 
     * @return int
     * @return the value of field 'aunnin_kostnadur'.
     */
    public int getAunnin_kostnadur()
    {
        return this._aunnin_kostnadur;
    } //-- int getAunnin_kostnadur() 

    /**
     * Returns the value of field 'banki'.
     * 
     * @return String
     * @return the value of field 'banki'.
     */
    public java.lang.String getBanki()
    {
        return this._banki;
    } //-- java.lang.String getBanki() 

    /**
     * Returns the value of field 'birting'.
     * 
     * @return Birting
     * @return the value of field 'birting'.
     */
    public is.idega.block.finance.business.li.claims.Birting getBirting()
    {
        return this._birting;
    } //-- is.idega.block.finance.business.li.claims.Birting getBirting() 

    /**
     * Returns the value of field 'birtingarkerfi'.
     * 
     * @return Birtingarkerfi
     * @return the value of field 'birtingarkerfi'.
     */
    public is.idega.block.finance.business.li.claims.Birtingarkerfi getBirtingarkerfi()
    {
        return this._birtingarkerfi;
    } //-- is.idega.block.finance.business.li.claims.Birtingarkerfi getBirtingarkerfi() 

    /**
     * Returns the value of field 'drattarvextir'.
     * 
     * @return Drattarvextir
     * @return the value of field 'drattarvextir'.
     */
    public is.idega.block.finance.business.li.claims.Drattarvextir getDrattarvextir()
    {
        return this._drattarvextir;
    } //-- is.idega.block.finance.business.li.claims.Drattarvextir getDrattarvextir() 

    /**
     * Returns the value of field 'eindagi'.
     * 
     * @return Date
     * @return the value of field 'eindagi'.
     */
    public org.exolab.castor.types.Date getEindagi()
    {
        return this._eindagi;
    } //-- org.exolab.castor.types.Date getEindagi() 

    /**
     * Returns the value of field 'gengiskrafa'.
     * 
     * @return Gengiskrafa
     * @return the value of field 'gengiskrafa'.
     */
    public is.idega.block.finance.business.li.claims.Gengiskrafa getGengiskrafa()
    {
        return this._gengiskrafa;
    } //-- is.idega.block.finance.business.li.claims.Gengiskrafa getGengiskrafa() 

    /**
     * Returns the value of field 'gjalddagi'.
     * 
     * @return Date
     * @return the value of field 'gjalddagi'.
     */
    public org.exolab.castor.types.Date getGjalddagi()
    {
        return this._gjalddagi;
    } //-- org.exolab.castor.types.Date getGjalddagi() 

    /**
     * Returns the value of field 'greidsluregla'.
     * 
     * @return LI_IK_greidsluregla_type
     * @return the value of field 'greidsluregla'.
     */
    public is.idega.block.finance.business.li.claims.types.LI_IK_greidsluregla_type getGreidsluregla()
    {
        return this._greidsluregla;
    } //-- is.idega.block.finance.business.li.claims.types.LI_IK_greidsluregla_type getGreidsluregla() 

    /**
     * Returns the value of field 'hofudbok'.
     * 
     * @return String
     * @return the value of field 'hofudbok'.
     */
    public java.lang.String getHofudbok()
    {
        return this._hofudbok;
    } //-- java.lang.String getHofudbok() 

    /**
     * Returns the value of field 'innborgunarregla'.
     * 
     * @return LI_IK_innborgunarregla_type
     * @return the value of field 'innborgunarregla'.
     */
    public is.idega.block.finance.business.li.claims.types.LI_IK_innborgunarregla_type getInnborgunarregla()
    {
        return this._innborgunarregla;
    } //-- is.idega.block.finance.business.li.claims.types.LI_IK_innborgunarregla_type getInnborgunarregla() 

    /**
     * Returns the value of field 'kt_greidanda'.
     * 
     * @return String
     * @return the value of field 'kt_greidanda'.
     */
    public java.lang.String getKt_greidanda()
    {
        return this._kt_greidanda;
    } //-- java.lang.String getKt_greidanda() 

    /**
     * Returns the value of field 'kt_krofuhafa'.
     * 
     * @return String
     * @return the value of field 'kt_krofuhafa'.
     */
    public java.lang.String getKt_krofuhafa()
    {
        return this._kt_krofuhafa;
    } //-- java.lang.String getKt_krofuhafa() 

    /**
     * Returns the value of field 'nidurfellingardagur'.
     * 
     * @return Date
     * @return the value of field 'nidurfellingardagur'.
     */
    public org.exolab.castor.types.Date getNidurfellingardagur()
    {
        return this._nidurfellingardagur;
    } //-- org.exolab.castor.types.Date getNidurfellingardagur() 

    /**
     * Returns the value of field 'numer'.
     * 
     * @return String
     * @return the value of field 'numer'.
     */
    public java.lang.String getNumer()
    {
        return this._numer;
    } //-- java.lang.String getNumer() 

    /**
     * Returns the value of field 'sedilnumer'.
     * 
     * @return String
     * @return the value of field 'sedilnumer'.
     */
    public java.lang.String getSedilnumer()
    {
        return this._sedilnumer;
    } //-- java.lang.String getSedilnumer() 

    /**
     * Returns the value of field
     * 'tilkynningar_og_greidslugjald_1'.
     * 
     * @return int
     * @return the value of field 'tilkynningar_og_greidslugjald_1'.
     */
    public int getTilkynningar_og_greidslugjald_1()
    {
        return this._tilkynningar_og_greidslugjald_1;
    } //-- int getTilkynningar_og_greidslugjald_1() 

    /**
     * Returns the value of field
     * 'tilkynningar_og_greidslugjald_2'.
     * 
     * @return int
     * @return the value of field 'tilkynningar_og_greidslugjald_2'.
     */
    public int getTilkynningar_og_greidslugjald_2()
    {
        return this._tilkynningar_og_greidslugjald_2;
    } //-- int getTilkynningar_og_greidslugjald_2() 

    /**
     * Returns the value of field 'tilvisunarnumer'.
     * 
     * @return String
     * @return the value of field 'tilvisunarnumer'.
     */
    public java.lang.String getTilvisunarnumer()
    {
        return this._tilvisunarnumer;
    } //-- java.lang.String getTilvisunarnumer() 

    /**
     * Returns the value of field 'upphaed'.
     * 
     * @return Upphaed
     * @return the value of field 'upphaed'.
     */
    public is.idega.block.finance.business.li.claims.Upphaed getUpphaed()
    {
        return this._upphaed;
    } //-- is.idega.block.finance.business.li.claims.Upphaed getUpphaed() 

    /**
     * Returns the value of field 'vanskilagjald'.
     * 
     * @return Vanskilagjald
     * @return the value of field 'vanskilagjald'.
     */
    public is.idega.block.finance.business.li.claims.Vanskilagjald getVanskilagjald()
    {
        return this._vanskilagjald;
    } //-- is.idega.block.finance.business.li.claims.Vanskilagjald getVanskilagjald() 

    /**
     * Returns the value of field 'vidskiptanumer'.
     * 
     * @return String
     * @return the value of field 'vidskiptanumer'.
     */
    public java.lang.String getVidskiptanumer()
    {
        return this._vidskiptanumer;
    } //-- java.lang.String getVidskiptanumer() 

    /**
     * Method hasAnnar_kostnadur
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasAnnar_kostnadur()
    {
        return this._has_annar_kostnadur;
    } //-- boolean hasAnnar_kostnadur() 

    /**
     * Method hasAnnar_vanskila_kostnadur
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasAnnar_vanskila_kostnadur()
    {
        return this._has_annar_vanskila_kostnadur;
    } //-- boolean hasAnnar_vanskila_kostnadur() 

    /**
     * Method hasAunnin_kostnadur
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasAunnin_kostnadur()
    {
        return this._has_aunnin_kostnadur;
    } //-- boolean hasAunnin_kostnadur() 

    /**
     * Method hasTilkynningar_og_greidslugjald_1
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasTilkynningar_og_greidslugjald_1()
    {
        return this._has_tilkynningar_og_greidslugjald_1;
    } //-- boolean hasTilkynningar_og_greidslugjald_1() 

    /**
     * Method hasTilkynningar_og_greidslugjald_2
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasTilkynningar_og_greidslugjald_2()
    {
        return this._has_tilkynningar_og_greidslugjald_2;
    } //-- boolean hasTilkynningar_og_greidslugjald_2() 

    /**
     * Method isValid
     * 
     * 
     * 
     * @return boolean
     */
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * Method marshal
     * 
     * 
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * 
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'afslattur'.
     * 
     * @param afslattur the value of field 'afslattur'.
     */
    public void setAfslattur(is.idega.block.finance.business.li.claims.Afslattur afslattur)
    {
        this._afslattur = afslattur;
    } //-- void setAfslattur(is.idega.block.finance.business.li.claims.Afslattur) 

    /**
     * Sets the value of field 'annar_kostnadur'.
     * 
     * @param annar_kostnadur the value of field 'annar_kostnadur'.
     */
    public void setAnnar_kostnadur(int annar_kostnadur)
    {
        this._annar_kostnadur = annar_kostnadur;
        this._has_annar_kostnadur = true;
    } //-- void setAnnar_kostnadur(int) 

    /**
     * Sets the value of field 'annar_vanskila_kostnadur'.
     * 
     * @param annar_vanskila_kostnadur the value of field
     * 'annar_vanskila_kostnadur'.
     */
    public void setAnnar_vanskila_kostnadur(int annar_vanskila_kostnadur)
    {
        this._annar_vanskila_kostnadur = annar_vanskila_kostnadur;
        this._has_annar_vanskila_kostnadur = true;
    } //-- void setAnnar_vanskila_kostnadur(int) 

    /**
     * Sets the value of field 'astand'.
     * 
     * @param astand the value of field 'astand'.
     */
    public void setAstand(is.idega.block.finance.business.li.claims.types.LI_IK_astand_type astand)
    {
        this._astand = astand;
    } //-- void setAstand(is.idega.block.finance.business.li.claims.types.LI_IK_astand_type) 

    /**
     * Sets the value of field 'audkenni'.
     * 
     * @param audkenni the value of field 'audkenni'.
     */
    public void setAudkenni(java.lang.String audkenni)
    {
        this._audkenni = audkenni;
    } //-- void setAudkenni(java.lang.String) 

    /**
     * Sets the value of field 'aunnin_kostnadur'.
     * 
     * @param aunnin_kostnadur the value of field 'aunnin_kostnadur'
     */
    public void setAunnin_kostnadur(int aunnin_kostnadur)
    {
        this._aunnin_kostnadur = aunnin_kostnadur;
        this._has_aunnin_kostnadur = true;
    } //-- void setAunnin_kostnadur(int) 

    /**
     * Sets the value of field 'banki'.
     * 
     * @param banki the value of field 'banki'.
     */
    public void setBanki(java.lang.String banki)
    {
        this._banki = banki;
    } //-- void setBanki(java.lang.String) 

    /**
     * Sets the value of field 'birting'.
     * 
     * @param birting the value of field 'birting'.
     */
    public void setBirting(is.idega.block.finance.business.li.claims.Birting birting)
    {
        this._birting = birting;
    } //-- void setBirting(is.idega.block.finance.business.li.claims.Birting) 

    /**
     * Sets the value of field 'birtingarkerfi'.
     * 
     * @param birtingarkerfi the value of field 'birtingarkerfi'.
     */
    public void setBirtingarkerfi(is.idega.block.finance.business.li.claims.Birtingarkerfi birtingarkerfi)
    {
        this._birtingarkerfi = birtingarkerfi;
    } //-- void setBirtingarkerfi(is.idega.block.finance.business.li.claims.Birtingarkerfi) 

    /**
     * Sets the value of field 'drattarvextir'.
     * 
     * @param drattarvextir the value of field 'drattarvextir'.
     */
    public void setDrattarvextir(is.idega.block.finance.business.li.claims.Drattarvextir drattarvextir)
    {
        this._drattarvextir = drattarvextir;
    } //-- void setDrattarvextir(is.idega.block.finance.business.li.claims.Drattarvextir) 

    /**
     * Sets the value of field 'eindagi'.
     * 
     * @param eindagi the value of field 'eindagi'.
     */
    public void setEindagi(org.exolab.castor.types.Date eindagi)
    {
        this._eindagi = eindagi;
    } //-- void setEindagi(org.exolab.castor.types.Date) 

    /**
     * Sets the value of field 'gengiskrafa'.
     * 
     * @param gengiskrafa the value of field 'gengiskrafa'.
     */
    public void setGengiskrafa(is.idega.block.finance.business.li.claims.Gengiskrafa gengiskrafa)
    {
        this._gengiskrafa = gengiskrafa;
    } //-- void setGengiskrafa(is.idega.block.finance.business.li.claims.Gengiskrafa) 

    /**
     * Sets the value of field 'gjalddagi'.
     * 
     * @param gjalddagi the value of field 'gjalddagi'.
     */
    public void setGjalddagi(org.exolab.castor.types.Date gjalddagi)
    {
        this._gjalddagi = gjalddagi;
    } //-- void setGjalddagi(org.exolab.castor.types.Date) 

    /**
     * Sets the value of field 'greidsluregla'.
     * 
     * @param greidsluregla the value of field 'greidsluregla'.
     */
    public void setGreidsluregla(is.idega.block.finance.business.li.claims.types.LI_IK_greidsluregla_type greidsluregla)
    {
        this._greidsluregla = greidsluregla;
    } //-- void setGreidsluregla(is.idega.block.finance.business.li.claims.types.LI_IK_greidsluregla_type) 

    /**
     * Sets the value of field 'hofudbok'.
     * 
     * @param hofudbok the value of field 'hofudbok'.
     */
    public void setHofudbok(java.lang.String hofudbok)
    {
        this._hofudbok = hofudbok;
    } //-- void setHofudbok(java.lang.String) 

    /**
     * Sets the value of field 'innborgunarregla'.
     * 
     * @param innborgunarregla the value of field 'innborgunarregla'
     */
    public void setInnborgunarregla(is.idega.block.finance.business.li.claims.types.LI_IK_innborgunarregla_type innborgunarregla)
    {
        this._innborgunarregla = innborgunarregla;
    } //-- void setInnborgunarregla(is.idega.block.finance.business.li.claims.types.LI_IK_innborgunarregla_type) 

    /**
     * Sets the value of field 'kt_greidanda'.
     * 
     * @param kt_greidanda the value of field 'kt_greidanda'.
     */
    public void setKt_greidanda(java.lang.String kt_greidanda)
    {
        this._kt_greidanda = kt_greidanda;
    } //-- void setKt_greidanda(java.lang.String) 

    /**
     * Sets the value of field 'kt_krofuhafa'.
     * 
     * @param kt_krofuhafa the value of field 'kt_krofuhafa'.
     */
    public void setKt_krofuhafa(java.lang.String kt_krofuhafa)
    {
        this._kt_krofuhafa = kt_krofuhafa;
    } //-- void setKt_krofuhafa(java.lang.String) 

    /**
     * Sets the value of field 'nidurfellingardagur'.
     * 
     * @param nidurfellingardagur the value of field
     * 'nidurfellingardagur'.
     */
    public void setNidurfellingardagur(org.exolab.castor.types.Date nidurfellingardagur)
    {
        this._nidurfellingardagur = nidurfellingardagur;
    } //-- void setNidurfellingardagur(org.exolab.castor.types.Date) 

    /**
     * Sets the value of field 'numer'.
     * 
     * @param numer the value of field 'numer'.
     */
    public void setNumer(java.lang.String numer)
    {
        this._numer = numer;
    } //-- void setNumer(java.lang.String) 

    /**
     * Sets the value of field 'sedilnumer'.
     * 
     * @param sedilnumer the value of field 'sedilnumer'.
     */
    public void setSedilnumer(java.lang.String sedilnumer)
    {
        this._sedilnumer = sedilnumer;
    } //-- void setSedilnumer(java.lang.String) 

    /**
     * Sets the value of field 'tilkynningar_og_greidslugjald_1'.
     * 
     * @param tilkynningar_og_greidslugjald_1 the value of field
     * 'tilkynningar_og_greidslugjald_1'.
     */
    public void setTilkynningar_og_greidslugjald_1(int tilkynningar_og_greidslugjald_1)
    {
        this._tilkynningar_og_greidslugjald_1 = tilkynningar_og_greidslugjald_1;
        this._has_tilkynningar_og_greidslugjald_1 = true;
    } //-- void setTilkynningar_og_greidslugjald_1(int) 

    /**
     * Sets the value of field 'tilkynningar_og_greidslugjald_2'.
     * 
     * @param tilkynningar_og_greidslugjald_2 the value of field
     * 'tilkynningar_og_greidslugjald_2'.
     */
    public void setTilkynningar_og_greidslugjald_2(int tilkynningar_og_greidslugjald_2)
    {
        this._tilkynningar_og_greidslugjald_2 = tilkynningar_og_greidslugjald_2;
        this._has_tilkynningar_og_greidslugjald_2 = true;
    } //-- void setTilkynningar_og_greidslugjald_2(int) 

    /**
     * Sets the value of field 'tilvisunarnumer'.
     * 
     * @param tilvisunarnumer the value of field 'tilvisunarnumer'.
     */
    public void setTilvisunarnumer(java.lang.String tilvisunarnumer)
    {
        this._tilvisunarnumer = tilvisunarnumer;
    } //-- void setTilvisunarnumer(java.lang.String) 

    /**
     * Sets the value of field 'upphaed'.
     * 
     * @param upphaed the value of field 'upphaed'.
     */
    public void setUpphaed(is.idega.block.finance.business.li.claims.Upphaed upphaed)
    {
        this._upphaed = upphaed;
    } //-- void setUpphaed(is.idega.block.finance.business.li.claims.Upphaed) 

    /**
     * Sets the value of field 'vanskilagjald'.
     * 
     * @param vanskilagjald the value of field 'vanskilagjald'.
     */
    public void setVanskilagjald(is.idega.block.finance.business.li.claims.Vanskilagjald vanskilagjald)
    {
        this._vanskilagjald = vanskilagjald;
    } //-- void setVanskilagjald(is.idega.block.finance.business.li.claims.Vanskilagjald) 

    /**
     * Sets the value of field 'vidskiptanumer'.
     * 
     * @param vidskiptanumer the value of field 'vidskiptanumer'.
     */
    public void setVidskiptanumer(java.lang.String vidskiptanumer)
    {
        this._vidskiptanumer = vidskiptanumer;
    } //-- void setVidskiptanumer(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return Object
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (is.idega.block.finance.business.li.claims.LI_IK_krafa_type) Unmarshaller.unmarshal(is.idega.block.finance.business.li.claims.LI_IK_krafa_type.class, reader);
    } //-- java.lang.Object unmarshal(java.io.Reader) 

    /**
     * Method validate
     * 
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
