/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LIInnskraSvar.java,v 1.1 2005/03/15 11:35:08 birna Exp $
 */

package is.idega.block.finance.business.li.sign_in_answer;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class LIInnskraSvar.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:08 $
 */
public class LIInnskraSvar implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _version
     */
    private java.math.BigDecimal _version;

    /**
     * Field _timi
     */
    private is.idega.block.finance.business.li.sign_in_answer.Timi _timi;

    /**
     * Field _seta
     */
    private java.lang.String _seta;


      //----------------/
     //- Constructors -/
    //----------------/

    public LIInnskraSvar() {
        super();
    } //-- is.idega.block.finance.business.li.sign_in_answer.LIInnskraSvar()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'seta'.
     * 
     * @return String
     * @return the value of field 'seta'.
     */
    public java.lang.String getSeta()
    {
        return this._seta;
    } //-- java.lang.String getSeta() 

    /**
     * Returns the value of field 'timi'.
     * 
     * @return Timi
     * @return the value of field 'timi'.
     */
    public is.idega.block.finance.business.li.sign_in_answer.Timi getTimi()
    {
        return this._timi;
    } //-- is.idega.block.finance.business.li.sign_in_answer.Timi getTimi() 

    /**
     * Returns the value of field 'version'.
     * 
     * @return BigDecimal
     * @return the value of field 'version'.
     */
    public java.math.BigDecimal getVersion()
    {
        return this._version;
    } //-- java.math.BigDecimal getVersion() 

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
     * Sets the value of field 'seta'.
     * 
     * @param seta the value of field 'seta'.
     */
    public void setSeta(java.lang.String seta)
    {
        this._seta = seta;
    } //-- void setSeta(java.lang.String) 

    /**
     * Sets the value of field 'timi'.
     * 
     * @param timi the value of field 'timi'.
     */
    public void setTimi(is.idega.block.finance.business.li.sign_in_answer.Timi timi)
    {
        this._timi = timi;
    } //-- void setTimi(is.idega.block.finance.business.li.sign_in_answer.Timi) 

    /**
     * Sets the value of field 'version'.
     * 
     * @param version the value of field 'version'.
     */
    public void setVersion(java.math.BigDecimal version)
    {
        this._version = version;
    } //-- void setVersion(java.math.BigDecimal) 

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
        return (is.idega.block.finance.business.li.sign_in_answer.LIInnskraSvar) Unmarshaller.unmarshal(is.idega.block.finance.business.li.sign_in_answer.LIInnskraSvar.class, reader);
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
