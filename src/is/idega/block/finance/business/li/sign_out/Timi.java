/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: Timi.java,v 1.1 2005/03/15 11:35:08 birna Exp $
 */

package is.idega.block.finance.business.li.sign_out;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class Timi.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:08 $
 */
public class Timi implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _dagsMottekid
     */
    private java.util.Date _dagsMottekid;

    /**
     * Field _dagsSvarad
     */
    private java.util.Date _dagsSvarad;


      //----------------/
     //- Constructors -/
    //----------------/

    public Timi() {
        super();
    } //-- is.idega.block.finance.business.li.sign_out.Timi()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'dagsMottekid'.
     * 
     * @return Date
     * @return the value of field 'dagsMottekid'.
     */
    public java.util.Date getDagsMottekid()
    {
        return this._dagsMottekid;
    } //-- java.util.Date getDagsMottekid() 

    /**
     * Returns the value of field 'dagsSvarad'.
     * 
     * @return Date
     * @return the value of field 'dagsSvarad'.
     */
    public java.util.Date getDagsSvarad()
    {
        return this._dagsSvarad;
    } //-- java.util.Date getDagsSvarad() 

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
     * Sets the value of field 'dagsMottekid'.
     * 
     * @param dagsMottekid the value of field 'dagsMottekid'.
     */
    public void setDagsMottekid(java.util.Date dagsMottekid)
    {
        this._dagsMottekid = dagsMottekid;
    } //-- void setDagsMottekid(java.util.Date) 

    /**
     * Sets the value of field 'dagsSvarad'.
     * 
     * @param dagsSvarad the value of field 'dagsSvarad'.
     */
    public void setDagsSvarad(java.util.Date dagsSvarad)
    {
        this._dagsSvarad = dagsSvarad;
    } //-- void setDagsSvarad(java.util.Date) 

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
        return (is.idega.block.finance.business.li.sign_out.Timi) Unmarshaller.unmarshal(is.idega.block.finance.business.li.sign_out.Timi.class, reader);
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
