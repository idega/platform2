/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: Reikningur.java,v 1.1 2005/03/15 11:35:09 birna Exp $
 */

package is.idega.block.finance.business.li.sign_in_answer;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class Reikningur.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:09 $
 */
public class Reikningur implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _bankaNr
     */
    private java.lang.String _bankaNr;

    /**
     * Field _hb
     */
    private java.lang.String _hb;

    /**
     * Field _reikningsNr
     */
    private java.lang.String _reikningsNr;


      //----------------/
     //- Constructors -/
    //----------------/

    public Reikningur() {
        super();
    } //-- is.idega.block.finance.business.li.sign_in_answer.Reikningur()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'bankaNr'.
     * 
     * @return String
     * @return the value of field 'bankaNr'.
     */
    public java.lang.String getBankaNr()
    {
        return this._bankaNr;
    } //-- java.lang.String getBankaNr() 

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
     * Returns the value of field 'reikningsNr'.
     * 
     * @return String
     * @return the value of field 'reikningsNr'.
     */
    public java.lang.String getReikningsNr()
    {
        return this._reikningsNr;
    } //-- java.lang.String getReikningsNr() 

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
     * Sets the value of field 'bankaNr'.
     * 
     * @param bankaNr the value of field 'bankaNr'.
     */
    public void setBankaNr(java.lang.String bankaNr)
    {
        this._bankaNr = bankaNr;
    } //-- void setBankaNr(java.lang.String) 

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
     * Sets the value of field 'reikningsNr'.
     * 
     * @param reikningsNr the value of field 'reikningsNr'.
     */
    public void setReikningsNr(java.lang.String reikningsNr)
    {
        this._reikningsNr = reikningsNr;
    } //-- void setReikningsNr(java.lang.String) 

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
        return (is.idega.block.finance.business.li.sign_in_answer.Reikningur) Unmarshaller.unmarshal(is.idega.block.finance.business.li.sign_in_answer.Reikningur.class, reader);
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
