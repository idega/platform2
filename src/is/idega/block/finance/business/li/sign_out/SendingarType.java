/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: SendingarType.java,v 1.1 2005/03/15 11:35:08 birna Exp $
 */

package is.idega.block.finance.business.li.sign_out;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class SendingarType.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:08 $
 */
public class SendingarType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _snigill
     */
    private is.idega.block.finance.business.li.sign_out.types.ErTilType _snigill;

    /**
     * Field _tolvupostur
     */
    private java.lang.String _tolvupostur;

    /**
     * Field _sms
     */
    private java.lang.String _sms;


      //----------------/
     //- Constructors -/
    //----------------/

    public SendingarType() {
        super();
    } //-- is.idega.block.finance.business.li.sign_out.SendingarType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'sms'.
     * 
     * @return String
     * @return the value of field 'sms'.
     */
    public java.lang.String getSms()
    {
        return this._sms;
    } //-- java.lang.String getSms() 

    /**
     * Returns the value of field 'snigill'.
     * 
     * @return ErTilType
     * @return the value of field 'snigill'.
     */
    public is.idega.block.finance.business.li.sign_out.types.ErTilType getSnigill()
    {
        return this._snigill;
    } //-- is.idega.block.finance.business.li.sign_out.types.ErTilType getSnigill() 

    /**
     * Returns the value of field 'tolvupostur'.
     * 
     * @return String
     * @return the value of field 'tolvupostur'.
     */
    public java.lang.String getTolvupostur()
    {
        return this._tolvupostur;
    } //-- java.lang.String getTolvupostur() 

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
     * Sets the value of field 'sms'.
     * 
     * @param sms the value of field 'sms'.
     */
    public void setSms(java.lang.String sms)
    {
        this._sms = sms;
    } //-- void setSms(java.lang.String) 

    /**
     * Sets the value of field 'snigill'.
     * 
     * @param snigill the value of field 'snigill'.
     */
    public void setSnigill(is.idega.block.finance.business.li.sign_out.types.ErTilType snigill)
    {
        this._snigill = snigill;
    } //-- void setSnigill(is.idega.block.finance.business.li.sign_out.types.ErTilType) 

    /**
     * Sets the value of field 'tolvupostur'.
     * 
     * @param tolvupostur the value of field 'tolvupostur'.
     */
    public void setTolvupostur(java.lang.String tolvupostur)
    {
        this._tolvupostur = tolvupostur;
    } //-- void setTolvupostur(java.lang.String) 

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
        return (is.idega.block.finance.business.li.sign_out.SendingarType) Unmarshaller.unmarshal(is.idega.block.finance.business.li.sign_out.SendingarType.class, reader);
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
