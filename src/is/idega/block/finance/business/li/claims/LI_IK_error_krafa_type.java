/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_IK_error_krafa_type.java,v 1.1 2005/03/15 11:35:05 birna Exp $
 */

package is.idega.block.finance.business.li.claims;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class LI_IK_error_krafa_type.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:05 $
 */
public class LI_IK_error_krafa_type implements java.io.Serializable {


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
     * Field _villunumer
     */
    private int _villunumer;

    /**
     * keeps track of state for field: _villunumer
     */
    private boolean _has_villunumer;

    /**
     * Field _villumelding
     */
    private java.lang.String _villumelding;


      //----------------/
     //- Constructors -/
    //----------------/

    public LI_IK_error_krafa_type() {
        super();
    } //-- is.idega.block.finance.business.li.claims.LI_IK_error_krafa_type()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteVillunumer
     * 
     */
    public void deleteVillunumer()
    {
        this._has_villunumer= false;
    } //-- void deleteVillunumer() 

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
     * Returns the value of field 'villumelding'.
     * 
     * @return String
     * @return the value of field 'villumelding'.
     */
    public java.lang.String getVillumelding()
    {
        return this._villumelding;
    } //-- java.lang.String getVillumelding() 

    /**
     * Returns the value of field 'villunumer'.
     * 
     * @return int
     * @return the value of field 'villunumer'.
     */
    public int getVillunumer()
    {
        return this._villunumer;
    } //-- int getVillunumer() 

    /**
     * Method hasVillunumer
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasVillunumer()
    {
        return this._has_villunumer;
    } //-- boolean hasVillunumer() 

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
     * Sets the value of field 'banki'.
     * 
     * @param banki the value of field 'banki'.
     */
    public void setBanki(java.lang.String banki)
    {
        this._banki = banki;
    } //-- void setBanki(java.lang.String) 

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
     * Sets the value of field 'hofudbok'.
     * 
     * @param hofudbok the value of field 'hofudbok'.
     */
    public void setHofudbok(java.lang.String hofudbok)
    {
        this._hofudbok = hofudbok;
    } //-- void setHofudbok(java.lang.String) 

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
     * Sets the value of field 'villumelding'.
     * 
     * @param villumelding the value of field 'villumelding'.
     */
    public void setVillumelding(java.lang.String villumelding)
    {
        this._villumelding = villumelding;
    } //-- void setVillumelding(java.lang.String) 

    /**
     * Sets the value of field 'villunumer'.
     * 
     * @param villunumer the value of field 'villunumer'.
     */
    public void setVillunumer(int villunumer)
    {
        this._villunumer = villunumer;
        this._has_villunumer = true;
    } //-- void setVillunumer(int) 

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
        return (is.idega.block.finance.business.li.claims.LI_IK_error_krafa_type) Unmarshaller.unmarshal(is.idega.block.finance.business.li.claims.LI_IK_error_krafa_type.class, reader);
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
