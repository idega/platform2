/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_timi_type.java,v 1.1 2005/03/15 11:35:07 birna Exp $
 */

package is.idega.block.finance.business.li.sign_in;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class LI_timi_type.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:07 $
 */
public class LI_timi_type implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _dags_mottekid
     */
    private java.util.Date _dags_mottekid;

    /**
     * Field _dags_svarad
     */
    private java.util.Date _dags_svarad;


      //----------------/
     //- Constructors -/
    //----------------/

    public LI_timi_type() {
        super();
    } //-- is.idega.block.finance.business.li.sign_in.LI_timi_type()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'dags_mottekid'.
     * 
     * @return Date
     * @return the value of field 'dags_mottekid'.
     */
    public java.util.Date getDags_mottekid()
    {
        return this._dags_mottekid;
    } //-- java.util.Date getDags_mottekid() 

    /**
     * Returns the value of field 'dags_svarad'.
     * 
     * @return Date
     * @return the value of field 'dags_svarad'.
     */
    public java.util.Date getDags_svarad()
    {
        return this._dags_svarad;
    } //-- java.util.Date getDags_svarad() 

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
     * Sets the value of field 'dags_mottekid'.
     * 
     * @param dags_mottekid the value of field 'dags_mottekid'.
     */
    public void setDags_mottekid(java.util.Date dags_mottekid)
    {
        this._dags_mottekid = dags_mottekid;
    } //-- void setDags_mottekid(java.util.Date) 

    /**
     * Sets the value of field 'dags_svarad'.
     * 
     * @param dags_svarad the value of field 'dags_svarad'.
     */
    public void setDags_svarad(java.util.Date dags_svarad)
    {
        this._dags_svarad = dags_svarad;
    } //-- void setDags_svarad(java.util.Date) 

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
        return (is.idega.block.finance.business.li.sign_in.LI_timi_type) Unmarshaller.unmarshal(is.idega.block.finance.business.li.sign_in.LI_timi_type.class, reader);
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
