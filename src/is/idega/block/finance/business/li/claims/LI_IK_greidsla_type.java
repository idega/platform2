/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_IK_greidsla_type.java,v 1.1 2005/03/15 11:35:07 birna Exp $
 */

package is.idega.block.finance.business.li.claims;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class LI_IK_greidsla_type.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:07 $
 */
public class LI_IK_greidsla_type implements java.io.Serializable {


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
     * Field _upphaed
     */
    private is.idega.block.finance.business.li.claims.Upphaed _upphaed;

    /**
     * Field _eindagi
     */
    private org.exolab.castor.types.Date _eindagi;

    /**
     * Field _audkenni
     */
    private java.lang.String _audkenni;

    /**
     * Field _tilvisunarnumer
     */
    private java.lang.String _tilvisunarnumer;

    /**
     * Field _textalykill
     */
    private java.lang.String _textalykill;

    /**
     * Field _innlausnarbanki
     */
    private java.lang.String _innlausnarbanki;

    /**
     * Field _hreyfingardagur
     */
    private org.exolab.castor.types.Date _hreyfingardagur;

    /**
     * Field _vaxtadagur
     */
    private org.exolab.castor.types.Date _vaxtadagur;

    /**
     * Field _innborgunarupphaed
     */
    private int _innborgunarupphaed;

    /**
     * keeps track of state for field: _innborgunarupphaed
     */
    private boolean _has_innborgunarupphaed;

    /**
     * Field _radstofudupphaed
     */
    private int _radstofudupphaed;

    /**
     * keeps track of state for field: _radstofudupphaed
     */
    private boolean _has_radstofudupphaed;

    /**
     * Field _heildarupphaed
     */
    private int _heildarupphaed;

    /**
     * keeps track of state for field: _heildarupphaed
     */
    private boolean _has_heildarupphaed;

    /**
     * Field _fjarmagnstekjuskattur
     */
    private int _fjarmagnstekjuskattur;

    /**
     * keeps track of state for field: _fjarmagnstekjuskattur
     */
    private boolean _has_fjarmagnstekjuskattur;

    /**
     * Field _sedilnumer
     */
    private java.lang.String _sedilnumer;

    /**
     * Field _vidskiptanumer
     */
    private java.lang.String _vidskiptanumer;

    /**
     * Field _tilkynningarupphaed
     */
    private int _tilkynningarupphaed;

    /**
     * keeps track of state for field: _tilkynningarupphaed
     */
    private boolean _has_tilkynningarupphaed;

    /**
     * Field _vanskilagjaldsupphaed
     */
    private java.math.BigDecimal _vanskilagjaldsupphaed;

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
     * Field _drattarvaxtaupphaed
     */
    private int _drattarvaxtaupphaed;

    /**
     * keeps track of state for field: _drattarvaxtaupphaed
     */
    private boolean _has_drattarvaxtaupphaed;

    /**
     * Field _afslattarupphaed
     */
    private java.math.BigDecimal _afslattarupphaed;

    /**
     * Field _gengi
     */
    private is.idega.block.finance.business.li.claims.Gengi _gengi;

    /**
     * Field _bunkanumer
     */
    private java.lang.String _bunkanumer;


      //----------------/
     //- Constructors -/
    //----------------/

    public LI_IK_greidsla_type() {
        super();
    } //-- is.idega.block.finance.business.li.claims.LI_IK_greidsla_type()


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
     * Method deleteDrattarvaxtaupphaed
     * 
     */
    public void deleteDrattarvaxtaupphaed()
    {
        this._has_drattarvaxtaupphaed= false;
    } //-- void deleteDrattarvaxtaupphaed() 

    /**
     * Method deleteFjarmagnstekjuskattur
     * 
     */
    public void deleteFjarmagnstekjuskattur()
    {
        this._has_fjarmagnstekjuskattur= false;
    } //-- void deleteFjarmagnstekjuskattur() 

    /**
     * Method deleteHeildarupphaed
     * 
     */
    public void deleteHeildarupphaed()
    {
        this._has_heildarupphaed= false;
    } //-- void deleteHeildarupphaed() 

    /**
     * Method deleteInnborgunarupphaed
     * 
     */
    public void deleteInnborgunarupphaed()
    {
        this._has_innborgunarupphaed= false;
    } //-- void deleteInnborgunarupphaed() 

    /**
     * Method deleteRadstofudupphaed
     * 
     */
    public void deleteRadstofudupphaed()
    {
        this._has_radstofudupphaed= false;
    } //-- void deleteRadstofudupphaed() 

    /**
     * Method deleteTilkynningarupphaed
     * 
     */
    public void deleteTilkynningarupphaed()
    {
        this._has_tilkynningarupphaed= false;
    } //-- void deleteTilkynningarupphaed() 

    /**
     * Returns the value of field 'afslattarupphaed'.
     * 
     * @return BigDecimal
     * @return the value of field 'afslattarupphaed'.
     */
    public java.math.BigDecimal getAfslattarupphaed()
    {
        return this._afslattarupphaed;
    } //-- java.math.BigDecimal getAfslattarupphaed() 

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
     * Returns the value of field 'bunkanumer'.
     * 
     * @return String
     * @return the value of field 'bunkanumer'.
     */
    public java.lang.String getBunkanumer()
    {
        return this._bunkanumer;
    } //-- java.lang.String getBunkanumer() 

    /**
     * Returns the value of field 'drattarvaxtaupphaed'.
     * 
     * @return int
     * @return the value of field 'drattarvaxtaupphaed'.
     */
    public int getDrattarvaxtaupphaed()
    {
        return this._drattarvaxtaupphaed;
    } //-- int getDrattarvaxtaupphaed() 

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
     * Returns the value of field 'fjarmagnstekjuskattur'.
     * 
     * @return int
     * @return the value of field 'fjarmagnstekjuskattur'.
     */
    public int getFjarmagnstekjuskattur()
    {
        return this._fjarmagnstekjuskattur;
    } //-- int getFjarmagnstekjuskattur() 

    /**
     * Returns the value of field 'gengi'.
     * 
     * @return Gengi
     * @return the value of field 'gengi'.
     */
    public is.idega.block.finance.business.li.claims.Gengi getGengi()
    {
        return this._gengi;
    } //-- is.idega.block.finance.business.li.claims.Gengi getGengi() 

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
     * Returns the value of field 'heildarupphaed'.
     * 
     * @return int
     * @return the value of field 'heildarupphaed'.
     */
    public int getHeildarupphaed()
    {
        return this._heildarupphaed;
    } //-- int getHeildarupphaed() 

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
     * Returns the value of field 'hreyfingardagur'.
     * 
     * @return Date
     * @return the value of field 'hreyfingardagur'.
     */
    public org.exolab.castor.types.Date getHreyfingardagur()
    {
        return this._hreyfingardagur;
    } //-- org.exolab.castor.types.Date getHreyfingardagur() 

    /**
     * Returns the value of field 'innborgunarupphaed'.
     * 
     * @return int
     * @return the value of field 'innborgunarupphaed'.
     */
    public int getInnborgunarupphaed()
    {
        return this._innborgunarupphaed;
    } //-- int getInnborgunarupphaed() 

    /**
     * Returns the value of field 'innlausnarbanki'.
     * 
     * @return String
     * @return the value of field 'innlausnarbanki'.
     */
    public java.lang.String getInnlausnarbanki()
    {
        return this._innlausnarbanki;
    } //-- java.lang.String getInnlausnarbanki() 

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
     * Returns the value of field 'radstofudupphaed'.
     * 
     * @return int
     * @return the value of field 'radstofudupphaed'.
     */
    public int getRadstofudupphaed()
    {
        return this._radstofudupphaed;
    } //-- int getRadstofudupphaed() 

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
     * Returns the value of field 'textalykill'.
     * 
     * @return String
     * @return the value of field 'textalykill'.
     */
    public java.lang.String getTextalykill()
    {
        return this._textalykill;
    } //-- java.lang.String getTextalykill() 

    /**
     * Returns the value of field 'tilkynningarupphaed'.
     * 
     * @return int
     * @return the value of field 'tilkynningarupphaed'.
     */
    public int getTilkynningarupphaed()
    {
        return this._tilkynningarupphaed;
    } //-- int getTilkynningarupphaed() 

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
     * Returns the value of field 'vanskilagjaldsupphaed'.
     * 
     * @return BigDecimal
     * @return the value of field 'vanskilagjaldsupphaed'.
     */
    public java.math.BigDecimal getVanskilagjaldsupphaed()
    {
        return this._vanskilagjaldsupphaed;
    } //-- java.math.BigDecimal getVanskilagjaldsupphaed() 

    /**
     * Returns the value of field 'vaxtadagur'.
     * 
     * @return Date
     * @return the value of field 'vaxtadagur'.
     */
    public org.exolab.castor.types.Date getVaxtadagur()
    {
        return this._vaxtadagur;
    } //-- org.exolab.castor.types.Date getVaxtadagur() 

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
     * Method hasDrattarvaxtaupphaed
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasDrattarvaxtaupphaed()
    {
        return this._has_drattarvaxtaupphaed;
    } //-- boolean hasDrattarvaxtaupphaed() 

    /**
     * Method hasFjarmagnstekjuskattur
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasFjarmagnstekjuskattur()
    {
        return this._has_fjarmagnstekjuskattur;
    } //-- boolean hasFjarmagnstekjuskattur() 

    /**
     * Method hasHeildarupphaed
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasHeildarupphaed()
    {
        return this._has_heildarupphaed;
    } //-- boolean hasHeildarupphaed() 

    /**
     * Method hasInnborgunarupphaed
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasInnborgunarupphaed()
    {
        return this._has_innborgunarupphaed;
    } //-- boolean hasInnborgunarupphaed() 

    /**
     * Method hasRadstofudupphaed
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasRadstofudupphaed()
    {
        return this._has_radstofudupphaed;
    } //-- boolean hasRadstofudupphaed() 

    /**
     * Method hasTilkynningarupphaed
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasTilkynningarupphaed()
    {
        return this._has_tilkynningarupphaed;
    } //-- boolean hasTilkynningarupphaed() 

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
     * Sets the value of field 'afslattarupphaed'.
     * 
     * @param afslattarupphaed the value of field 'afslattarupphaed'
     */
    public void setAfslattarupphaed(java.math.BigDecimal afslattarupphaed)
    {
        this._afslattarupphaed = afslattarupphaed;
    } //-- void setAfslattarupphaed(java.math.BigDecimal) 

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
     * Sets the value of field 'audkenni'.
     * 
     * @param audkenni the value of field 'audkenni'.
     */
    public void setAudkenni(java.lang.String audkenni)
    {
        this._audkenni = audkenni;
    } //-- void setAudkenni(java.lang.String) 

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
     * Sets the value of field 'bunkanumer'.
     * 
     * @param bunkanumer the value of field 'bunkanumer'.
     */
    public void setBunkanumer(java.lang.String bunkanumer)
    {
        this._bunkanumer = bunkanumer;
    } //-- void setBunkanumer(java.lang.String) 

    /**
     * Sets the value of field 'drattarvaxtaupphaed'.
     * 
     * @param drattarvaxtaupphaed the value of field
     * 'drattarvaxtaupphaed'.
     */
    public void setDrattarvaxtaupphaed(int drattarvaxtaupphaed)
    {
        this._drattarvaxtaupphaed = drattarvaxtaupphaed;
        this._has_drattarvaxtaupphaed = true;
    } //-- void setDrattarvaxtaupphaed(int) 

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
     * Sets the value of field 'fjarmagnstekjuskattur'.
     * 
     * @param fjarmagnstekjuskattur the value of field
     * 'fjarmagnstekjuskattur'.
     */
    public void setFjarmagnstekjuskattur(int fjarmagnstekjuskattur)
    {
        this._fjarmagnstekjuskattur = fjarmagnstekjuskattur;
        this._has_fjarmagnstekjuskattur = true;
    } //-- void setFjarmagnstekjuskattur(int) 

    /**
     * Sets the value of field 'gengi'.
     * 
     * @param gengi the value of field 'gengi'.
     */
    public void setGengi(is.idega.block.finance.business.li.claims.Gengi gengi)
    {
        this._gengi = gengi;
    } //-- void setGengi(is.idega.block.finance.business.li.claims.Gengi) 

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
     * Sets the value of field 'heildarupphaed'.
     * 
     * @param heildarupphaed the value of field 'heildarupphaed'.
     */
    public void setHeildarupphaed(int heildarupphaed)
    {
        this._heildarupphaed = heildarupphaed;
        this._has_heildarupphaed = true;
    } //-- void setHeildarupphaed(int) 

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
     * Sets the value of field 'hreyfingardagur'.
     * 
     * @param hreyfingardagur the value of field 'hreyfingardagur'.
     */
    public void setHreyfingardagur(org.exolab.castor.types.Date hreyfingardagur)
    {
        this._hreyfingardagur = hreyfingardagur;
    } //-- void setHreyfingardagur(org.exolab.castor.types.Date) 

    /**
     * Sets the value of field 'innborgunarupphaed'.
     * 
     * @param innborgunarupphaed the value of field
     * 'innborgunarupphaed'.
     */
    public void setInnborgunarupphaed(int innborgunarupphaed)
    {
        this._innborgunarupphaed = innborgunarupphaed;
        this._has_innborgunarupphaed = true;
    } //-- void setInnborgunarupphaed(int) 

    /**
     * Sets the value of field 'innlausnarbanki'.
     * 
     * @param innlausnarbanki the value of field 'innlausnarbanki'.
     */
    public void setInnlausnarbanki(java.lang.String innlausnarbanki)
    {
        this._innlausnarbanki = innlausnarbanki;
    } //-- void setInnlausnarbanki(java.lang.String) 

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
     * Sets the value of field 'numer'.
     * 
     * @param numer the value of field 'numer'.
     */
    public void setNumer(java.lang.String numer)
    {
        this._numer = numer;
    } //-- void setNumer(java.lang.String) 

    /**
     * Sets the value of field 'radstofudupphaed'.
     * 
     * @param radstofudupphaed the value of field 'radstofudupphaed'
     */
    public void setRadstofudupphaed(int radstofudupphaed)
    {
        this._radstofudupphaed = radstofudupphaed;
        this._has_radstofudupphaed = true;
    } //-- void setRadstofudupphaed(int) 

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
     * Sets the value of field 'textalykill'.
     * 
     * @param textalykill the value of field 'textalykill'.
     */
    public void setTextalykill(java.lang.String textalykill)
    {
        this._textalykill = textalykill;
    } //-- void setTextalykill(java.lang.String) 

    /**
     * Sets the value of field 'tilkynningarupphaed'.
     * 
     * @param tilkynningarupphaed the value of field
     * 'tilkynningarupphaed'.
     */
    public void setTilkynningarupphaed(int tilkynningarupphaed)
    {
        this._tilkynningarupphaed = tilkynningarupphaed;
        this._has_tilkynningarupphaed = true;
    } //-- void setTilkynningarupphaed(int) 

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
     * Sets the value of field 'vanskilagjaldsupphaed'.
     * 
     * @param vanskilagjaldsupphaed the value of field
     * 'vanskilagjaldsupphaed'.
     */
    public void setVanskilagjaldsupphaed(java.math.BigDecimal vanskilagjaldsupphaed)
    {
        this._vanskilagjaldsupphaed = vanskilagjaldsupphaed;
    } //-- void setVanskilagjaldsupphaed(java.math.BigDecimal) 

    /**
     * Sets the value of field 'vaxtadagur'.
     * 
     * @param vaxtadagur the value of field 'vaxtadagur'.
     */
    public void setVaxtadagur(org.exolab.castor.types.Date vaxtadagur)
    {
        this._vaxtadagur = vaxtadagur;
    } //-- void setVaxtadagur(org.exolab.castor.types.Date) 

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
        return (is.idega.block.finance.business.li.claims.LI_IK_greidsla_type) Unmarshaller.unmarshal(is.idega.block.finance.business.li.claims.LI_IK_greidsla_type.class, reader);
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
