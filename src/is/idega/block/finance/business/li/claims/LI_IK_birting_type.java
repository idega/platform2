/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_IK_birting_type.java,v 1.1 2005/03/15 11:35:07 birna Exp $
 */

package is.idega.block.finance.business.li.claims;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class LI_IK_birting_type.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:07 $
 */
public class LI_IK_birting_type implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _tegund
     */
    private java.lang.String _tegund;

    /**
     * Field _LI_IK_birting_typeSequence
     */
    private is.idega.block.finance.business.li.claims.LI_IK_birting_typeSequence _LI_IK_birting_typeSequence;

    /**
     * Field _xmls
     */
    private java.lang.String _xmls;


      //----------------/
     //- Constructors -/
    //----------------/

    public LI_IK_birting_type() {
        super();
    } //-- is.idega.block.finance.business.li.claims.LI_IK_birting_type()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'LI_IK_birting_typeSequence'.
     * 
     * @return LI_IK_birting_typeSequence
     * @return the value of field 'LI_IK_birting_typeSequence'.
     */
    public is.idega.block.finance.business.li.claims.LI_IK_birting_typeSequence getLI_IK_birting_typeSequence()
    {
        return this._LI_IK_birting_typeSequence;
    } //-- is.idega.block.finance.business.li.claims.LI_IK_birting_typeSequence getLI_IK_birting_typeSequence() 

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
     * Returns the value of field 'xmls'.
     * 
     * @return String
     * @return the value of field 'xmls'.
     */
    public java.lang.String getXmls()
    {
        return this._xmls;
    } //-- java.lang.String getXmls() 

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
     * Sets the value of field 'LI_IK_birting_typeSequence'.
     * 
     * @param LI_IK_birting_typeSequence the value of field
     * 'LI_IK_birting_typeSequence'.
     */
    public void setLI_IK_birting_typeSequence(is.idega.block.finance.business.li.claims.LI_IK_birting_typeSequence LI_IK_birting_typeSequence)
    {
        this._LI_IK_birting_typeSequence = LI_IK_birting_typeSequence;
    } //-- void setLI_IK_birting_typeSequence(is.idega.block.finance.business.li.claims.LI_IK_birting_typeSequence) 

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
     * Sets the value of field 'xmls'.
     * 
     * @param xmls the value of field 'xmls'.
     */
    public void setXmls(java.lang.String xmls)
    {
        this._xmls = xmls;
    } //-- void setXmls(java.lang.String) 

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
        return (is.idega.block.finance.business.li.claims.LI_IK_birting_type) Unmarshaller.unmarshal(is.idega.block.finance.business.li.claims.LI_IK_birting_type.class, reader);
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
