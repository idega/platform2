/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_IK_birtingarkerfi_type.java,v 1.1 2005/03/15 11:35:06 birna Exp $
 */

package is.idega.block.finance.business.li.claims;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class LI_IK_birtingarkerfi_type.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:06 $
 */
public class LI_IK_birtingarkerfi_type implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _tegund
     */
    private java.lang.String _tegund;

    /**
     * Field _slod
     */
    private java.lang.String _slod;


      //----------------/
     //- Constructors -/
    //----------------/

    public LI_IK_birtingarkerfi_type() {
        super();
    } //-- is.idega.block.finance.business.li.claims.LI_IK_birtingarkerfi_type()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'slod'.
     * 
     * @return String
     * @return the value of field 'slod'.
     */
    public java.lang.String getSlod()
    {
        return this._slod;
    } //-- java.lang.String getSlod() 

    /**
     * Returns the value of field 'tegund'.
     * 
     * @return String
     * @return the value of field 'tegund'.
     */
    public java.lang.String getTegund()
    {
        return this._tegund;
    } //-- java.lang.String getTegund() 

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
     * Sets the value of field 'slod'.
     * 
     * @param slod the value of field 'slod'.
     */
    public void setSlod(java.lang.String slod)
    {
        this._slod = slod;
    } //-- void setSlod(java.lang.String) 

    /**
     * Sets the value of field 'tegund'.
     * 
     * @param tegund the value of field 'tegund'.
     */
    public void setTegund(java.lang.String tegund)
    {
        this._tegund = tegund;
    } //-- void setTegund(java.lang.String) 

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
        return (is.idega.block.finance.business.li.claims.LI_IK_birtingarkerfi_type) Unmarshaller.unmarshal(is.idega.block.finance.business.li.claims.LI_IK_birtingarkerfi_type.class, reader);
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
