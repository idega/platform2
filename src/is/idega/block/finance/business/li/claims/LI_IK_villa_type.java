/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_IK_villa_type.java,v 1.1 2005/03/15 11:35:05 birna Exp $
 */

package is.idega.block.finance.business.li.claims;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * almenn villumeðhöndlun, villunúmer og villutexti
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:05 $
 */
public class LI_IK_villa_type implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _villunumer
     */
    private int _villunumer;

    /**
     * keeps track of state for field: _villunumer
     */
    private boolean _has_villunumer;

    /**
     * Field _villumelding
     */
    private java.lang.String _villumelding;


      //----------------/
     //- Constructors -/
    //----------------/

    public LI_IK_villa_type() {
        super();
    } //-- is.idega.block.finance.business.li.claims.LI_IK_villa_type()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteVillunumer
     * 
     */
    public void deleteVillunumer()
    {
        this._has_villunumer= false;
    } //-- void deleteVillunumer() 

    /**
     * Returns the value of field 'villumelding'.
     * 
     * @return String
     * @return the value of field 'villumelding'.
     */
    public java.lang.String getVillumelding()
    {
        return this._villumelding;
    } //-- java.lang.String getVillumelding() 

    /**
     * Returns the value of field 'villunumer'.
     * 
     * @return int
     * @return the value of field 'villunumer'.
     */
    public int getVillunumer()
    {
        return this._villunumer;
    } //-- int getVillunumer() 

    /**
     * Method hasVillunumer
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasVillunumer()
    {
        return this._has_villunumer;
    } //-- boolean hasVillunumer() 

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
     * Sets the value of field 'villumelding'.
     * 
     * @param villumelding the value of field 'villumelding'.
     */
    public void setVillumelding(java.lang.String villumelding)
    {
        this._villumelding = villumelding;
    } //-- void setVillumelding(java.lang.String) 

    /**
     * Sets the value of field 'villunumer'.
     * 
     * @param villunumer the value of field 'villunumer'.
     */
    public void setVillunumer(int villunumer)
    {
        this._villunumer = villunumer;
        this._has_villunumer = true;
    } //-- void setVillunumer(int) 

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
        return (is.idega.block.finance.business.li.claims.LI_IK_villa_type) Unmarshaller.unmarshal(is.idega.block.finance.business.li.claims.LI_IK_villa_type.class, reader);
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
