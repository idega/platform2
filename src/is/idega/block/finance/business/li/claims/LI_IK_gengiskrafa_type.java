/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_IK_gengiskrafa_type.java,v 1.1 2005/03/15 11:35:06 birna Exp $
 */

package is.idega.block.finance.business.li.claims;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class LI_IK_gengiskrafa_type.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:06 $
 */
public class LI_IK_gengiskrafa_type implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _gengistegund
     */
    private is.idega.block.finance.business.li.claims.types.GengistegundType _gengistegund;

    /**
     * Field _mynt
     */
    private is.idega.block.finance.business.li.claims.types.LI_IK_mynt_type _mynt;

    /**
     * Field _gengisbanki
     */
    private is.idega.block.finance.business.li.claims.types.GengisbankiType _gengisbanki;

    /**
     * Field _gengisregla
     */
    private is.idega.block.finance.business.li.claims.types.GengisreglaType _gengisregla;


      //----------------/
     //- Constructors -/
    //----------------/

    public LI_IK_gengiskrafa_type() {
        super();
    } //-- is.idega.block.finance.business.li.claims.LI_IK_gengiskrafa_type()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'gengisbanki'.
     * 
     * @return GengisbankiType
     * @return the value of field 'gengisbanki'.
     */
    public is.idega.block.finance.business.li.claims.types.GengisbankiType getGengisbanki()
    {
        return this._gengisbanki;
    } //-- is.idega.block.finance.business.li.claims.types.GengisbankiType getGengisbanki() 

    /**
     * Returns the value of field 'gengisregla'.
     * 
     * @return GengisreglaType
     * @return the value of field 'gengisregla'.
     */
    public is.idega.block.finance.business.li.claims.types.GengisreglaType getGengisregla()
    {
        return this._gengisregla;
    } //-- is.idega.block.finance.business.li.claims.types.GengisreglaType getGengisregla() 

    /**
     * Returns the value of field 'gengistegund'.
     * 
     * @return GengistegundType
     * @return the value of field 'gengistegund'.
     */
    public is.idega.block.finance.business.li.claims.types.GengistegundType getGengistegund()
    {
        return this._gengistegund;
    } //-- is.idega.block.finance.business.li.claims.types.GengistegundType getGengistegund() 

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
     * Sets the value of field 'gengisbanki'.
     * 
     * @param gengisbanki the value of field 'gengisbanki'.
     */
    public void setGengisbanki(is.idega.block.finance.business.li.claims.types.GengisbankiType gengisbanki)
    {
        this._gengisbanki = gengisbanki;
    } //-- void setGengisbanki(is.idega.block.finance.business.li.claims.types.GengisbankiType) 

    /**
     * Sets the value of field 'gengisregla'.
     * 
     * @param gengisregla the value of field 'gengisregla'.
     */
    public void setGengisregla(is.idega.block.finance.business.li.claims.types.GengisreglaType gengisregla)
    {
        this._gengisregla = gengisregla;
    } //-- void setGengisregla(is.idega.block.finance.business.li.claims.types.GengisreglaType) 

    /**
     * Sets the value of field 'gengistegund'.
     * 
     * @param gengistegund the value of field 'gengistegund'.
     */
    public void setGengistegund(is.idega.block.finance.business.li.claims.types.GengistegundType gengistegund)
    {
        this._gengistegund = gengistegund;
    } //-- void setGengistegund(is.idega.block.finance.business.li.claims.types.GengistegundType) 

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
        return (is.idega.block.finance.business.li.claims.LI_IK_gengiskrafa_type) Unmarshaller.unmarshal(is.idega.block.finance.business.li.claims.LI_IK_gengiskrafa_type.class, reader);
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
