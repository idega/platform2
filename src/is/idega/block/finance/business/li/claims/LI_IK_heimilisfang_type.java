/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_IK_heimilisfang_type.java,v 1.1 2005/03/15 11:35:05 birna Exp $
 */

package is.idega.block.finance.business.li.claims;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class LI_IK_heimilisfang_type.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:05 $
 */
public class LI_IK_heimilisfang_type implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _nafn
     */
    private java.lang.String _nafn;

    /**
     * Field _nafn2
     */
    private java.lang.String _nafn2;

    /**
     * Field _heimili
     */
    private java.lang.String _heimili;

    /**
     * Field _sveitarfelag
     */
    private java.lang.String _sveitarfelag;

    /**
     * Field _simanumer
     */
    private java.lang.String _simanumer;


      //----------------/
     //- Constructors -/
    //----------------/

    public LI_IK_heimilisfang_type() {
        super();
    } //-- is.idega.block.finance.business.li.claims.LI_IK_heimilisfang_type()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'heimili'.
     * 
     * @return String
     * @return the value of field 'heimili'.
     */
    public java.lang.String getHeimili()
    {
        return this._heimili;
    } //-- java.lang.String getHeimili() 

    /**
     * Returns the value of field 'nafn'.
     * 
     * @return String
     * @return the value of field 'nafn'.
     */
    public java.lang.String getNafn()
    {
        return this._nafn;
    } //-- java.lang.String getNafn() 

    /**
     * Returns the value of field 'nafn2'.
     * 
     * @return String
     * @return the value of field 'nafn2'.
     */
    public java.lang.String getNafn2()
    {
        return this._nafn2;
    } //-- java.lang.String getNafn2() 

    /**
     * Returns the value of field 'simanumer'.
     * 
     * @return String
     * @return the value of field 'simanumer'.
     */
    public java.lang.String getSimanumer()
    {
        return this._simanumer;
    } //-- java.lang.String getSimanumer() 

    /**
     * Returns the value of field 'sveitarfelag'.
     * 
     * @return String
     * @return the value of field 'sveitarfelag'.
     */
    public java.lang.String getSveitarfelag()
    {
        return this._sveitarfelag;
    } //-- java.lang.String getSveitarfelag() 

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
     * Sets the value of field 'heimili'.
     * 
     * @param heimili the value of field 'heimili'.
     */
    public void setHeimili(java.lang.String heimili)
    {
        this._heimili = heimili;
    } //-- void setHeimili(java.lang.String) 

    /**
     * Sets the value of field 'nafn'.
     * 
     * @param nafn the value of field 'nafn'.
     */
    public void setNafn(java.lang.String nafn)
    {
        this._nafn = nafn;
    } //-- void setNafn(java.lang.String) 

    /**
     * Sets the value of field 'nafn2'.
     * 
     * @param nafn2 the value of field 'nafn2'.
     */
    public void setNafn2(java.lang.String nafn2)
    {
        this._nafn2 = nafn2;
    } //-- void setNafn2(java.lang.String) 

    /**
     * Sets the value of field 'simanumer'.
     * 
     * @param simanumer the value of field 'simanumer'.
     */
    public void setSimanumer(java.lang.String simanumer)
    {
        this._simanumer = simanumer;
    } //-- void setSimanumer(java.lang.String) 

    /**
     * Sets the value of field 'sveitarfelag'.
     * 
     * @param sveitarfelag the value of field 'sveitarfelag'.
     */
    public void setSveitarfelag(java.lang.String sveitarfelag)
    {
        this._sveitarfelag = sveitarfelag;
    } //-- void setSveitarfelag(java.lang.String) 

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
        return (is.idega.block.finance.business.li.claims.LI_IK_heimilisfang_type) Unmarshaller.unmarshal(is.idega.block.finance.business.li.claims.LI_IK_heimilisfang_type.class, reader);
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
