/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_kvittun_vidtakanda_type.java,v 1.1 2005/03/15 11:35:07 birna Exp $
 */

package is.idega.block.finance.business.li.sign_in;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class LI_kvittun_vidtakanda_type.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:07 $
 */
public class LI_kvittun_vidtakanda_type implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _kvittun
     */
    private is.idega.block.finance.business.li.sign_in.Kvittun _kvittun;


      //----------------/
     //- Constructors -/
    //----------------/

    public LI_kvittun_vidtakanda_type() {
        super();
    } //-- is.idega.block.finance.business.li.sign_in.LI_kvittun_vidtakanda_type()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'kvittun'.
     * 
     * @return Kvittun
     * @return the value of field 'kvittun'.
     */
    public is.idega.block.finance.business.li.sign_in.Kvittun getKvittun()
    {
        return this._kvittun;
    } //-- is.idega.block.finance.business.li.sign_in.Kvittun getKvittun() 

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
     * Sets the value of field 'kvittun'.
     * 
     * @param kvittun the value of field 'kvittun'.
     */
    public void setKvittun(is.idega.block.finance.business.li.sign_in.Kvittun kvittun)
    {
        this._kvittun = kvittun;
    } //-- void setKvittun(is.idega.block.finance.business.li.sign_in.Kvittun) 

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
        return (is.idega.block.finance.business.li.sign_in.LI_kvittun_vidtakanda_type) Unmarshaller.unmarshal(is.idega.block.finance.business.li.sign_in.LI_kvittun_vidtakanda_type.class, reader);
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
