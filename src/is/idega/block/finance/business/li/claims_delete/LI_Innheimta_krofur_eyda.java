/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_Innheimta_krofur_eyda.java,v 1.1 2005/03/15 11:35:08 birna Exp $
 */

package is.idega.block.finance.business.li.claims_delete;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

/**
 * Class LI_Innheimta_krofur_eyda.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:08 $
 */
public class LI_Innheimta_krofur_eyda implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _version
     */
    private java.math.BigDecimal _version;

    /**
     * Field _session_id
     */
    private java.lang.String _session_id;

    /**
     * Field _krofur
     */
    private is.idega.block.finance.business.li.claims_delete.Krofur _krofur;


      //----------------/
     //- Constructors -/
    //----------------/

    public LI_Innheimta_krofur_eyda() {
        super();
    } //-- is.idega.block.finance.business.li.claims_delete.LI_Innheimta_krofur_eyda()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'krofur'.
     * 
     * @return Krofur
     * @return the value of field 'krofur'.
     */
    public is.idega.block.finance.business.li.claims_delete.Krofur getKrofur()
    {
        return this._krofur;
    } //-- is.idega.block.finance.business.li.claims_delete.Krofur getKrofur() 

    /**
     * Returns the value of field 'session_id'.
     * 
     * @return String
     * @return the value of field 'session_id'.
     */
    public java.lang.String getSession_id()
    {
        return this._session_id;
    } //-- java.lang.String getSession_id() 

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
    public void marshal(java.io.Writer out, String noNamespaceSchemaLocation)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
    	try {
				Marshaller marshaller = new Marshaller(out);
				marshaller.setNoNamespaceSchemaLocation(noNamespaceSchemaLocation);
				marshaller.setSuppressXSIType(true);
				marshaller.marshal(this);
			}
			catch (MarshalException e) {
				e.printStackTrace();
			}
			catch (ValidationException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
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
     * Sets the value of field 'krofur'.
     * 
     * @param krofur the value of field 'krofur'.
     */
    public void setKrofur(is.idega.block.finance.business.li.claims_delete.Krofur krofur)
    {
        this._krofur = krofur;
    } //-- void setKrofur(is.idega.block.finance.business.li.claims_delete.Krofur) 

    /**
     * Sets the value of field 'session_id'.
     * 
     * @param session_id the value of field 'session_id'.
     */
    public void setSession_id(java.lang.String session_id)
    {
        this._session_id = session_id;
    } //-- void setSession_id(java.lang.String) 

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
        return (is.idega.block.finance.business.li.claims_delete.LI_Innheimta_krofur_eyda) Unmarshaller.unmarshal(is.idega.block.finance.business.li.claims_delete.LI_Innheimta_krofur_eyda.class, reader);
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
