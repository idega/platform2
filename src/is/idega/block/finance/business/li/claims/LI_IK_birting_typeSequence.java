/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_IK_birting_typeSequence.java,v 1.1 2005/03/15 11:35:06 birna Exp $
 */

package is.idega.block.finance.business.li.claims;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class LI_IK_birting_typeSequence.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:06 $
 */
public class LI_IK_birting_typeSequence implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _athugasemdalina_1
     */
    private java.lang.String _athugasemdalina_1;

    /**
     * Field _athugasemdalina_2
     */
    private java.lang.Object _athugasemdalina_2;

    /**
     * Field _postfang_krofuhafa
     */
    private is.idega.block.finance.business.li.claims.Postfang_krofuhafa _postfang_krofuhafa;

    /**
     * Field _postfang_krofugreidanda
     */
    private is.idega.block.finance.business.li.claims.Postfang_krofugreidanda _postfang_krofugreidanda;

    /**
     * Field _hreyfingar
     */
    private is.idega.block.finance.business.li.claims.Hreyfingar _hreyfingar;


      //----------------/
     //- Constructors -/
    //----------------/

    public LI_IK_birting_typeSequence() {
        super();
    } //-- is.idega.block.finance.business.li.claims.LI_IK_birting_typeSequence()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'athugasemdalina_1'.
     * 
     * @return String
     * @return the value of field 'athugasemdalina_1'.
     */
    public java.lang.String getAthugasemdalina_1()
    {
        return this._athugasemdalina_1;
    } //-- java.lang.String getAthugasemdalina_1() 

    /**
     * Returns the value of field 'athugasemdalina_2'.
     * 
     * @return Object
     * @return the value of field 'athugasemdalina_2'.
     */
    public java.lang.Object getAthugasemdalina_2()
    {
        return this._athugasemdalina_2;
    } //-- java.lang.Object getAthugasemdalina_2() 

    /**
     * Returns the value of field 'hreyfingar'.
     * 
     * @return Hreyfingar
     * @return the value of field 'hreyfingar'.
     */
    public is.idega.block.finance.business.li.claims.Hreyfingar getHreyfingar()
    {
        return this._hreyfingar;
    } //-- is.idega.block.finance.business.li.claims.Hreyfingar getHreyfingar() 

    /**
     * Returns the value of field 'postfang_krofugreidanda'.
     * 
     * @return Postfang_krofugreidanda
     * @return the value of field 'postfang_krofugreidanda'.
     */
    public is.idega.block.finance.business.li.claims.Postfang_krofugreidanda getPostfang_krofugreidanda()
    {
        return this._postfang_krofugreidanda;
    } //-- is.idega.block.finance.business.li.claims.Postfang_krofugreidanda getPostfang_krofugreidanda() 

    /**
     * Returns the value of field 'postfang_krofuhafa'.
     * 
     * @return Postfang_krofuhafa
     * @return the value of field 'postfang_krofuhafa'.
     */
    public is.idega.block.finance.business.li.claims.Postfang_krofuhafa getPostfang_krofuhafa()
    {
        return this._postfang_krofuhafa;
    } //-- is.idega.block.finance.business.li.claims.Postfang_krofuhafa getPostfang_krofuhafa() 

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
     * Sets the value of field 'athugasemdalina_1'.
     * 
     * @param athugasemdalina_1 the value of field
     * 'athugasemdalina_1'.
     */
    public void setAthugasemdalina_1(java.lang.String athugasemdalina_1)
    {
        this._athugasemdalina_1 = athugasemdalina_1;
    } //-- void setAthugasemdalina_1(java.lang.String) 

    /**
     * Sets the value of field 'athugasemdalina_2'.
     * 
     * @param athugasemdalina_2 the value of field
     * 'athugasemdalina_2'.
     */
    public void setAthugasemdalina_2(java.lang.Object athugasemdalina_2)
    {
        this._athugasemdalina_2 = athugasemdalina_2;
    } //-- void setAthugasemdalina_2(java.lang.Object) 

    /**
     * Sets the value of field 'hreyfingar'.
     * 
     * @param hreyfingar the value of field 'hreyfingar'.
     */
    public void setHreyfingar(is.idega.block.finance.business.li.claims.Hreyfingar hreyfingar)
    {
        this._hreyfingar = hreyfingar;
    } //-- void setHreyfingar(is.idega.block.finance.business.li.claims.Hreyfingar) 

    /**
     * Sets the value of field 'postfang_krofugreidanda'.
     * 
     * @param postfang_krofugreidanda the value of field
     * 'postfang_krofugreidanda'.
     */
    public void setPostfang_krofugreidanda(is.idega.block.finance.business.li.claims.Postfang_krofugreidanda postfang_krofugreidanda)
    {
        this._postfang_krofugreidanda = postfang_krofugreidanda;
    } //-- void setPostfang_krofugreidanda(is.idega.block.finance.business.li.claims.Postfang_krofugreidanda) 

    /**
     * Sets the value of field 'postfang_krofuhafa'.
     * 
     * @param postfang_krofuhafa the value of field
     * 'postfang_krofuhafa'.
     */
    public void setPostfang_krofuhafa(is.idega.block.finance.business.li.claims.Postfang_krofuhafa postfang_krofuhafa)
    {
        this._postfang_krofuhafa = postfang_krofuhafa;
    } //-- void setPostfang_krofuhafa(is.idega.block.finance.business.li.claims.Postfang_krofuhafa) 

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
        return (is.idega.block.finance.business.li.claims.LI_IK_birting_typeSequence) Unmarshaller.unmarshal(is.idega.block.finance.business.li.claims.LI_IK_birting_typeSequence.class, reader);
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
