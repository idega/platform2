/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_reikningur_type.java,v 1.1 2005/03/15 11:35:07 birna Exp $
 */

package is.idega.block.finance.business.li.sign_in;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class LI_reikningur_type.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:07 $
 */
public class LI_reikningur_type implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _utibu
     */
    private java.lang.String _utibu;

    /**
     * Field _hb
     */
    private java.lang.String _hb;

    /**
     * Field _reikningsnr
     */
    private java.lang.String _reikningsnr;


      //----------------/
     //- Constructors -/
    //----------------/

    public LI_reikningur_type() {
        super();
    } //-- is.idega.block.finance.business.li.sign_in.LI_reikningur_type()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'hb'.
     * 
     * @return String
     * @return the value of field 'hb'.
     */
    public java.lang.String getHb()
    {
        return this._hb;
    } //-- java.lang.String getHb() 

    /**
     * Returns the value of field 'reikningsnr'.
     * 
     * @return String
     * @return the value of field 'reikningsnr'.
     */
    public java.lang.String getReikningsnr()
    {
        return this._reikningsnr;
    } //-- java.lang.String getReikningsnr() 

    /**
     * Returns the value of field 'utibu'.
     * 
     * @return String
     * @return the value of field 'utibu'.
     */
    public java.lang.String getUtibu()
    {
        return this._utibu;
    } //-- java.lang.String getUtibu() 

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
     * Sets the value of field 'hb'.
     * 
     * @param hb the value of field 'hb'.
     */
    public void setHb(java.lang.String hb)
    {
        this._hb = hb;
    } //-- void setHb(java.lang.String) 

    /**
     * Sets the value of field 'reikningsnr'.
     * 
     * @param reikningsnr the value of field 'reikningsnr'.
     */
    public void setReikningsnr(java.lang.String reikningsnr)
    {
        this._reikningsnr = reikningsnr;
    } //-- void setReikningsnr(java.lang.String) 

    /**
     * Sets the value of field 'utibu'.
     * 
     * @param utibu the value of field 'utibu'.
     */
    public void setUtibu(java.lang.String utibu)
    {
        this._utibu = utibu;
    } //-- void setUtibu(java.lang.String) 

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
        return (is.idega.block.finance.business.li.sign_in.LI_reikningur_type) Unmarshaller.unmarshal(is.idega.block.finance.business.li.sign_in.LI_reikningur_type.class, reader);
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
