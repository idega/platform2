/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_IK_gengi_type.java,v 1.1 2005/03/15 11:35:04 birna Exp $
 */

package is.idega.block.finance.business.li.claims;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class LI_IK_gengi_type.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:04 $
 */
public class LI_IK_gengi_type implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _mynt
     */
    private is.idega.block.finance.business.li.claims.types.LI_IK_mynt_type _mynt;

    /**
     * Field _gengi
     */
    private java.math.BigDecimal _gengi;


      //----------------/
     //- Constructors -/
    //----------------/

    public LI_IK_gengi_type() {
        super();
    } //-- is.idega.block.finance.business.li.claims.LI_IK_gengi_type()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'gengi'.
     * 
     * @return BigDecimal
     * @return the value of field 'gengi'.
     */
    public java.math.BigDecimal getGengi()
    {
        return this._gengi;
    } //-- java.math.BigDecimal getGengi() 

    /**
     * Returns the value of field 'mynt'.
     * 
     * @return LI_IK_mynt_type
     * @return the value of field 'mynt'.
     */
    public is.idega.block.finance.business.li.claims.types.LI_IK_mynt_type getMynt()
    {
        return this._mynt;
    } //-- is.idega.block.finance.business.li.claims.types.LI_IK_mynt_type getMynt() 

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
     * Sets the value of field 'gengi'.
     * 
     * @param gengi the value of field 'gengi'.
     */
    public void setGengi(java.math.BigDecimal gengi)
    {
        this._gengi = gengi;
    } //-- void setGengi(java.math.BigDecimal) 

    /**
     * Sets the value of field 'mynt'.
     * 
     * @param mynt the value of field 'mynt'.
     */
    public void setMynt(is.idega.block.finance.business.li.claims.types.LI_IK_mynt_type mynt)
    {
        this._mynt = mynt;
    } //-- void setMynt(is.idega.block.finance.business.li.claims.types.LI_IK_mynt_type) 

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
        return (is.idega.block.finance.business.li.claims.LI_IK_gengi_type) Unmarshaller.unmarshal(is.idega.block.finance.business.li.claims.LI_IK_gengi_type.class, reader);
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
