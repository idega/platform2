/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_IK_drattarvextir_type.java,v 1.1 2005/03/15 11:35:03 birna Exp $
 */

package is.idega.block.finance.business.li.claims;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class LI_IK_drattarvextir_type.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:03 $
 */
public class LI_IK_drattarvextir_type implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _prosent
     */
    private java.math.BigDecimal _prosent;

    /**
     * Field _vaxtastofn
     */
    private is.idega.block.finance.business.li.claims.types.LI_IK_vaxtastofnkodi_type _vaxtastofn;

    /**
     * Field _regla
     */
    private is.idega.block.finance.business.li.claims.types.ReglaType _regla;


      //----------------/
     //- Constructors -/
    //----------------/

    public LI_IK_drattarvextir_type() {
        super();
    } //-- is.idega.block.finance.business.li.claims.LI_IK_drattarvextir_type()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'prosent'.
     * 
     * @return BigDecimal
     * @return the value of field 'prosent'.
     */
    public java.math.BigDecimal getProsent()
    {
        return this._prosent;
    } //-- java.math.BigDecimal getProsent() 

    /**
     * Returns the value of field 'regla'.
     * 
     * @return ReglaType
     * @return the value of field 'regla'.
     */
    public is.idega.block.finance.business.li.claims.types.ReglaType getRegla()
    {
        return this._regla;
    } //-- is.idega.block.finance.business.li.claims.types.ReglaType getRegla() 

    /**
     * Returns the value of field 'vaxtastofn'.
     * 
     * @return LI_IK_vaxtastofnkodi_type
     * @return the value of field 'vaxtastofn'.
     */
    public is.idega.block.finance.business.li.claims.types.LI_IK_vaxtastofnkodi_type getVaxtastofn()
    {
        return this._vaxtastofn;
    } //-- is.idega.block.finance.business.li.claims.types.LI_IK_vaxtastofnkodi_type getVaxtastofn() 

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
     * Sets the value of field 'prosent'.
     * 
     * @param prosent the value of field 'prosent'.
     */
    public void setProsent(java.math.BigDecimal prosent)
    {
        this._prosent = prosent;
    } //-- void setProsent(java.math.BigDecimal) 

    /**
     * Sets the value of field 'regla'.
     * 
     * @param regla the value of field 'regla'.
     */
    public void setRegla(is.idega.block.finance.business.li.claims.types.ReglaType regla)
    {
        this._regla = regla;
    } //-- void setRegla(is.idega.block.finance.business.li.claims.types.ReglaType) 

    /**
     * Sets the value of field 'vaxtastofn'.
     * 
     * @param vaxtastofn the value of field 'vaxtastofn'.
     */
    public void setVaxtastofn(is.idega.block.finance.business.li.claims.types.LI_IK_vaxtastofnkodi_type vaxtastofn)
    {
        this._vaxtastofn = vaxtastofn;
    } //-- void setVaxtastofn(is.idega.block.finance.business.li.claims.types.LI_IK_vaxtastofnkodi_type) 

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
        return (is.idega.block.finance.business.li.claims.LI_IK_drattarvextir_type) Unmarshaller.unmarshal(is.idega.block.finance.business.li.claims.LI_IK_drattarvextir_type.class, reader);
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
