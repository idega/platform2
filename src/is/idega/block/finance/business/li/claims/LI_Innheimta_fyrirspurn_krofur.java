/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_Innheimta_fyrirspurn_krofur.java,v 1.1 2005/03/15 11:35:03 birna Exp $
 */

package is.idega.block.finance.business.li.claims;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

/**
 * Class LI_Innheimta_fyrirspurn_krofur.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:03 $
 */
public class LI_Innheimta_fyrirspurn_krofur implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _version
     */
    private java.math.BigDecimal _version;

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
     * Field _gjalddagi_fra
     */
    private org.exolab.castor.types.Date _gjalddagi_fra;

    /**
     * Field _gjalddagi_til
     */
    private org.exolab.castor.types.Date _gjalddagi_til;

    /**
     * Field _audkenni
     */
    private java.lang.String _audkenni;

    /**
     * Field _hreyfingardagur_fra
     */
    private org.exolab.castor.types.Date _hreyfingardagur_fra;

    /**
     * Field _hreyfingardagur_til
     */
    private org.exolab.castor.types.Date _hreyfingardagur_til;

    /**
     * Field _astand
     */
    private is.idega.block.finance.business.li.claims.types.LI_IK_astand_type _astand;

    /**
     * Field _session_id
     */
    private java.lang.String _session_id;


      //----------------/
     //- Constructors -/
    //----------------/

    public LI_Innheimta_fyrirspurn_krofur() {
        super();
    } //-- is.idega.block.finance.business.li.claims.LI_Innheimta_fyrirspurn_krofur()


      //-----------/
     //- Methods -/
    //-----------/

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
     * Returns the value of field 'gjalddagi_fra'.
     * 
     * @return Date
     * @return the value of field 'gjalddagi_fra'.
     */
    public org.exolab.castor.types.Date getGjalddagi_fra()
    {
        return this._gjalddagi_fra;
    } //-- org.exolab.castor.types.Date getGjalddagi_fra() 

    /**
     * Returns the value of field 'gjalddagi_til'.
     * 
     * @return Date
     * @return the value of field 'gjalddagi_til'.
     */
    public org.exolab.castor.types.Date getGjalddagi_til()
    {
        return this._gjalddagi_til;
    } //-- org.exolab.castor.types.Date getGjalddagi_til() 

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
     * Returns the value of field 'hreyfingardagur_fra'.
     * 
     * @return Date
     * @return the value of field 'hreyfingardagur_fra'.
     */
    public org.exolab.castor.types.Date getHreyfingardagur_fra()
    {
        return this._hreyfingardagur_fra;
    } //-- org.exolab.castor.types.Date getHreyfingardagur_fra() 

    /**
     * Returns the value of field 'hreyfingardagur_til'.
     * 
     * @return Date
     * @return the value of field 'hreyfingardagur_til'.
     */
    public org.exolab.castor.types.Date getHreyfingardagur_til()
    {
        return this._hreyfingardagur_til;
    } //-- org.exolab.castor.types.Date getHreyfingardagur_til() 

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
     * Returns the value of field 'session_id'.
     * 
     * @return String
     * @return the value of field 'session_id'.
     */
    public java.lang.String getSession_id()
    {
        return this._session_id;
    } //-- java.lang.String getSession_id() 

    /**
     * Returns the value of field 'version'.
     * 
     * @return BigDecimal
     * @return the value of field 'version'.
     */
    public java.math.BigDecimal getVersion()
    {
        return this._version;
    } //-- java.math.BigDecimal getVersion() 

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
    public void marshal(java.io.Writer out, String noNamespaceSchemaLocation)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
    	try {
				Marshaller marshaller = new Marshaller(out);
				marshaller.setNoNamespaceSchemaLocation(noNamespaceSchemaLocation);
				marshaller.setSuppressXSIType(true);
				marshaller.marshal(this);
			}
			catch (MarshalException e) {
				e.printStackTrace();
			}
			catch (ValidationException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
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
     * Sets the value of field 'banki'.
     * 
     * @param banki the value of field 'banki'.
     */
    public void setBanki(java.lang.String banki)
    {
        this._banki = banki;
    } //-- void setBanki(java.lang.String) 

    /**
     * Sets the value of field 'gjalddagi_fra'.
     * 
     * @param gjalddagi_fra the value of field 'gjalddagi_fra'.
     */
    public void setGjalddagi_fra(org.exolab.castor.types.Date gjalddagi_fra)
    {
        this._gjalddagi_fra = gjalddagi_fra;
    } //-- void setGjalddagi_fra(org.exolab.castor.types.Date) 

    /**
     * Sets the value of field 'gjalddagi_til'.
     * 
     * @param gjalddagi_til the value of field 'gjalddagi_til'.
     */
    public void setGjalddagi_til(org.exolab.castor.types.Date gjalddagi_til)
    {
        this._gjalddagi_til = gjalddagi_til;
    } //-- void setGjalddagi_til(org.exolab.castor.types.Date) 

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
     * Sets the value of field 'hreyfingardagur_fra'.
     * 
     * @param hreyfingardagur_fra the value of field
     * 'hreyfingardagur_fra'.
     */
    public void setHreyfingardagur_fra(org.exolab.castor.types.Date hreyfingardagur_fra)
    {
        this._hreyfingardagur_fra = hreyfingardagur_fra;
    } //-- void setHreyfingardagur_fra(org.exolab.castor.types.Date) 

    /**
     * Sets the value of field 'hreyfingardagur_til'.
     * 
     * @param hreyfingardagur_til the value of field
     * 'hreyfingardagur_til'.
     */
    public void setHreyfingardagur_til(org.exolab.castor.types.Date hreyfingardagur_til)
    {
        this._hreyfingardagur_til = hreyfingardagur_til;
    } //-- void setHreyfingardagur_til(org.exolab.castor.types.Date) 

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
     * Sets the value of field 'session_id'.
     * 
     * @param session_id the value of field 'session_id'.
     */
    public void setSession_id(java.lang.String session_id)
    {
        this._session_id = session_id;
    } //-- void setSession_id(java.lang.String) 

    /**
     * Sets the value of field 'version'.
     * 
     * @param version the value of field 'version'.
     */
    public void setVersion(java.math.BigDecimal version)
    {
        this._version = version;
    } //-- void setVersion(java.math.BigDecimal) 

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
        return (is.idega.block.finance.business.li.claims.LI_Innheimta_fyrirspurn_krofur) Unmarshaller.unmarshal(is.idega.block.finance.business.li.claims.LI_Innheimta_fyrirspurn_krofur.class, reader);
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
