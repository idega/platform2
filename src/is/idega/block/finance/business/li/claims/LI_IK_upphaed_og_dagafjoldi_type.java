/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_IK_upphaed_og_dagafjoldi_type.java,v 1.1 2005/03/15 11:35:03 birna Exp $
 */

package is.idega.block.finance.business.li.claims;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class LI_IK_upphaed_og_dagafjoldi_type.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:03 $
 */
public class LI_IK_upphaed_og_dagafjoldi_type implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * internal content storage
     */
    private java.math.BigDecimal _content;

    /**
     * Field _dagafjoldi
     */
    private int _dagafjoldi;

    /**
     * keeps track of state for field: _dagafjoldi
     */
    private boolean _has_dagafjoldi;


      //----------------/
     //- Constructors -/
    //----------------/

    public LI_IK_upphaed_og_dagafjoldi_type() {
        super();
    } //-- is.idega.block.finance.business.li.claims.LI_IK_upphaed_og_dagafjoldi_type()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteDagafjoldi
     * 
     */
    public void deleteDagafjoldi()
    {
        this._has_dagafjoldi= false;
    } //-- void deleteDagafjoldi() 

    /**
     * Returns the value of field 'content'. The field 'content'
     * has the following description: internal content storage
     * 
     * @return BigDecimal
     * @return the value of field 'content'.
     */
    public java.math.BigDecimal getContent()
    {
        return this._content;
    } //-- java.math.BigDecimal getContent() 

    /**
     * Returns the value of field 'dagafjoldi'.
     * 
     * @return int
     * @return the value of field 'dagafjoldi'.
     */
    public int getDagafjoldi()
    {
        return this._dagafjoldi;
    } //-- int getDagafjoldi() 

    /**
     * Method hasDagafjoldi
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasDagafjoldi()
    {
        return this._has_dagafjoldi;
    } //-- boolean hasDagafjoldi() 

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
     * Sets the value of field 'content'. The field 'content' has
     * the following description: internal content storage
     * 
     * @param content the value of field 'content'.
     */
    public void setContent(java.math.BigDecimal content)
    {
        this._content = content;
    } //-- void setContent(java.math.BigDecimal) 

    /**
     * Sets the value of field 'dagafjoldi'.
     * 
     * @param dagafjoldi the value of field 'dagafjoldi'.
     */
    public void setDagafjoldi(int dagafjoldi)
    {
        this._dagafjoldi = dagafjoldi;
        this._has_dagafjoldi = true;
    } //-- void setDagafjoldi(int) 

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
        return (is.idega.block.finance.business.li.claims.LI_IK_upphaed_og_dagafjoldi_type) Unmarshaller.unmarshal(is.idega.block.finance.business.li.claims.LI_IK_upphaed_og_dagafjoldi_type.class, reader);
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
