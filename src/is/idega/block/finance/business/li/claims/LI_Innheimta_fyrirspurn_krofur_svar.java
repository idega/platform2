/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_Innheimta_fyrirspurn_krofur_svar.java,v 1.1 2005/03/15 11:35:06 birna Exp $
 */

package is.idega.block.finance.business.li.claims;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class LI_Innheimta_fyrirspurn_krofur_svar.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:06 $
 */
public class LI_Innheimta_fyrirspurn_krofur_svar implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _version
     */
    private java.math.BigDecimal _version;

    /**
     * Field _krofur
     */
    private is.idega.block.finance.business.li.claims.Krofur _krofur;

    /**
     * Field _villa
     */
    private is.idega.block.finance.business.li.claims.Villa _villa;


      //----------------/
     //- Constructors -/
    //----------------/

    public LI_Innheimta_fyrirspurn_krofur_svar() {
        super();
    } //-- is.idega.block.finance.business.li.claims.LI_Innheimta_fyrirspurn_krofur_svar()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'krofur'.
     * 
     * @return Krofur
     * @return the value of field 'krofur'.
     */
    public is.idega.block.finance.business.li.claims.Krofur getKrofur()
    {
        return this._krofur;
    } //-- is.idega.block.finance.business.li.claims.Krofur getKrofur() 

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
     * Returns the value of field 'villa'.
     * 
     * @return Villa
     * @return the value of field 'villa'.
     */
    public is.idega.block.finance.business.li.claims.Villa getVilla()
    {
        return this._villa;
    } //-- is.idega.block.finance.business.li.claims.Villa getVilla() 

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
     * Sets the value of field 'krofur'.
     * 
     * @param krofur the value of field 'krofur'.
     */
    public void setKrofur(is.idega.block.finance.business.li.claims.Krofur krofur)
    {
        this._krofur = krofur;
    } //-- void setKrofur(is.idega.block.finance.business.li.claims.Krofur) 

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
     * Sets the value of field 'villa'.
     * 
     * @param villa the value of field 'villa'.
     */
    public void setVilla(is.idega.block.finance.business.li.claims.Villa villa)
    {
        this._villa = villa;
    } //-- void setVilla(is.idega.block.finance.business.li.claims.Villa) 

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
        return (is.idega.block.finance.business.li.claims.LI_Innheimta_fyrirspurn_krofur_svar) Unmarshaller.unmarshal(is.idega.block.finance.business.li.claims.LI_Innheimta_fyrirspurn_krofur_svar.class, reader);
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
