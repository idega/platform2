/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_C_giro_type.java,v 1.1 2005/03/15 11:35:07 birna Exp $
 */

package is.idega.block.finance.business.li.sign_in;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class LI_C_giro_type.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:07 $
 */
public class LI_C_giro_type implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _reikningur
     */
    private is.idega.block.finance.business.li.sign_in.Reikningur _reikningur;

    /**
     * Field _kennitala
     */
    private java.lang.String _kennitala;

    /**
     * Field _sedilnumer
     */
    private java.lang.String _sedilnumer;


      //----------------/
     //- Constructors -/
    //----------------/

    public LI_C_giro_type() {
        super();
    } //-- is.idega.block.finance.business.li.sign_in.LI_C_giro_type()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'kennitala'.
     * 
     * @return String
     * @return the value of field 'kennitala'.
     */
    public java.lang.String getKennitala()
    {
        return this._kennitala;
    } //-- java.lang.String getKennitala() 

    /**
     * Returns the value of field 'reikningur'.
     * 
     * @return Reikningur
     * @return the value of field 'reikningur'.
     */
    public is.idega.block.finance.business.li.sign_in.Reikningur getReikningur()
    {
        return this._reikningur;
    } //-- is.idega.block.finance.business.li.sign_in.Reikningur getReikningur() 

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
     * Sets the value of field 'kennitala'.
     * 
     * @param kennitala the value of field 'kennitala'.
     */
    public void setKennitala(java.lang.String kennitala)
    {
        this._kennitala = kennitala;
    } //-- void setKennitala(java.lang.String) 

    /**
     * Sets the value of field 'reikningur'.
     * 
     * @param reikningur the value of field 'reikningur'.
     */
    public void setReikningur(is.idega.block.finance.business.li.sign_in.Reikningur reikningur)
    {
        this._reikningur = reikningur;
    } //-- void setReikningur(is.idega.block.finance.business.li.sign_in.Reikningur) 

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
        return (is.idega.block.finance.business.li.sign_in.LI_C_giro_type) Unmarshaller.unmarshal(is.idega.block.finance.business.li.sign_in.LI_C_giro_type.class, reader);
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
