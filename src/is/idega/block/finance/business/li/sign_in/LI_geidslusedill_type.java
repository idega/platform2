/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_geidslusedill_type.java,v 1.1 2005/03/15 11:35:07 birna Exp $
 */

package is.idega.block.finance.business.li.sign_in;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class LI_geidslusedill_type.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:07 $
 */
public class LI_geidslusedill_type implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _reikningur
     */
    private is.idega.block.finance.business.li.sign_in.Reikningur _reikningur;

    /**
     * Ef kröfupottur 62, kennitala greiðanda. Kröfupottur 66,
     * kennitala kröfuhafa
     */
    private java.lang.String _kennitala;

    /**
     * Field _gjalddagi
     */
    private org.exolab.castor.types.Date _gjalddagi;


      //----------------/
     //- Constructors -/
    //----------------/

    public LI_geidslusedill_type() {
        super();
    } //-- is.idega.block.finance.business.li.sign_in.LI_geidslusedill_type()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'gjalddagi'.
     * 
     * @return Date
     * @return the value of field 'gjalddagi'.
     */
    public org.exolab.castor.types.Date getGjalddagi()
    {
        return this._gjalddagi;
    } //-- org.exolab.castor.types.Date getGjalddagi() 

    /**
     * Returns the value of field 'kennitala'. The field
     * 'kennitala' has the following description: Ef kröfupottur
     * 62, kennitala greiðanda. Kröfupottur 66, kennitala
     * kröfuhafa
     * 
     * @return String
     * @return the value of field 'kennitala'.
     */
    public java.lang.String getKennitala()
    {
        return this._kennitala;
    } //-- java.lang.String getKennitala() 

    /**
     * Returns the value of field 'reikningur'.
     * 
     * @return Reikningur
     * @return the value of field 'reikningur'.
     */
    public is.idega.block.finance.business.li.sign_in.Reikningur getReikningur()
    {
        return this._reikningur;
    } //-- is.idega.block.finance.business.li.sign_in.Reikningur getReikningur() 

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
     * Sets the value of field 'gjalddagi'.
     * 
     * @param gjalddagi the value of field 'gjalddagi'.
     */
    public void setGjalddagi(org.exolab.castor.types.Date gjalddagi)
    {
        this._gjalddagi = gjalddagi;
    } //-- void setGjalddagi(org.exolab.castor.types.Date) 

    /**
     * Sets the value of field 'kennitala'. The field 'kennitala'
     * has the following description: Ef kröfupottur 62, kennitala
     * greiðanda. Kröfupottur 66, kennitala kröfuhafa
     * 
     * @param kennitala the value of field 'kennitala'.
     */
    public void setKennitala(java.lang.String kennitala)
    {
        this._kennitala = kennitala;
    } //-- void setKennitala(java.lang.String) 

    /**
     * Sets the value of field 'reikningur'.
     * 
     * @param reikningur the value of field 'reikningur'.
     */
    public void setReikningur(is.idega.block.finance.business.li.sign_in.Reikningur reikningur)
    {
        this._reikningur = reikningur;
    } //-- void setReikningur(is.idega.block.finance.business.li.sign_in.Reikningur) 

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
        return (is.idega.block.finance.business.li.sign_in.LI_geidslusedill_type) Unmarshaller.unmarshal(is.idega.block.finance.business.li.sign_in.LI_geidslusedill_type.class, reader);
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
