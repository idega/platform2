/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_IK_timabil_type.java,v 1.1 2005/03/15 11:35:05 birna Exp $
 */

package is.idega.block.finance.business.li.claims;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * dagsFra -> dagsTil
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:05 $
 */
public class LI_IK_timabil_type implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _dags_fra
     */
    private org.exolab.castor.types.Date _dags_fra;

    /**
     * Field _dags_til
     */
    private org.exolab.castor.types.Date _dags_til;


      //----------------/
     //- Constructors -/
    //----------------/

    public LI_IK_timabil_type() {
        super();
    } //-- is.idega.block.finance.business.li.claims.LI_IK_timabil_type()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'dags_fra'.
     * 
     * @return Date
     * @return the value of field 'dags_fra'.
     */
    public org.exolab.castor.types.Date getDags_fra()
    {
        return this._dags_fra;
    } //-- org.exolab.castor.types.Date getDags_fra() 

    /**
     * Returns the value of field 'dags_til'.
     * 
     * @return Date
     * @return the value of field 'dags_til'.
     */
    public org.exolab.castor.types.Date getDags_til()
    {
        return this._dags_til;
    } //-- org.exolab.castor.types.Date getDags_til() 

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
     * Sets the value of field 'dags_fra'.
     * 
     * @param dags_fra the value of field 'dags_fra'.
     */
    public void setDags_fra(org.exolab.castor.types.Date dags_fra)
    {
        this._dags_fra = dags_fra;
    } //-- void setDags_fra(org.exolab.castor.types.Date) 

    /**
     * Sets the value of field 'dags_til'.
     * 
     * @param dags_til the value of field 'dags_til'.
     */
    public void setDags_til(org.exolab.castor.types.Date dags_til)
    {
        this._dags_til = dags_til;
    } //-- void setDags_til(org.exolab.castor.types.Date) 

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
        return (is.idega.block.finance.business.li.claims.LI_IK_timabil_type) Unmarshaller.unmarshal(is.idega.block.finance.business.li.claims.LI_IK_timabil_type.class, reader);
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
