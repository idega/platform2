/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_IK_vanskilagjald_type.java,v 1.1 2005/03/15 11:35:07 birna Exp $
 */

package is.idega.block.finance.business.li.claims;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Vector;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class LI_IK_vanskilagjald_type.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:07 $
 */
public class LI_IK_vanskilagjald_type implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _vidmidun
     */
    private is.idega.block.finance.business.li.claims.types.LI_IK_vidmidun_dags_type _vidmidun;

    /**
     * Field _tegund_vanskilagjalds
     */
    private is.idega.block.finance.business.li.claims.types.LI_IK_tegund_afslattar_og_vanskilagjalds_type _tegund_vanskilagjalds;

    /**
     * Field _items
     */
    private java.util.Vector _items;


      //----------------/
     //- Constructors -/
    //----------------/

    public LI_IK_vanskilagjald_type() {
        super();
        _items = new Vector();
    } //-- is.idega.block.finance.business.li.claims.LI_IK_vanskilagjald_type()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addLI_IK_vanskilagjald_typeItem
     * 
     * 
     * 
     * @param vLI_IK_vanskilagjald_typeItem
     */
    public void addLI_IK_vanskilagjald_typeItem(is.idega.block.finance.business.li.claims.LI_IK_vanskilagjald_typeItem vLI_IK_vanskilagjald_typeItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _items.addElement(vLI_IK_vanskilagjald_typeItem);
    } //-- void addLI_IK_vanskilagjald_typeItem(is.idega.block.finance.business.li.claims.LI_IK_vanskilagjald_typeItem) 

    /**
     * Method addLI_IK_vanskilagjald_typeItem
     * 
     * 
     * 
     * @param index
     * @param vLI_IK_vanskilagjald_typeItem
     */
    public void addLI_IK_vanskilagjald_typeItem(int index, is.idega.block.finance.business.li.claims.LI_IK_vanskilagjald_typeItem vLI_IK_vanskilagjald_typeItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _items.insertElementAt(vLI_IK_vanskilagjald_typeItem, index);
    } //-- void addLI_IK_vanskilagjald_typeItem(int, is.idega.block.finance.business.li.claims.LI_IK_vanskilagjald_typeItem) 

    /**
     * Method enumerateLI_IK_vanskilagjald_typeItem
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateLI_IK_vanskilagjald_typeItem()
    {
        return _items.elements();
    } //-- java.util.Enumeration enumerateLI_IK_vanskilagjald_typeItem() 

    /**
     * Method getLI_IK_vanskilagjald_typeItem
     * 
     * 
     * 
     * @param index
     * @return LI_IK_vanskilagjald_typeItem
     */
    public is.idega.block.finance.business.li.claims.LI_IK_vanskilagjald_typeItem getLI_IK_vanskilagjald_typeItem(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _items.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (is.idega.block.finance.business.li.claims.LI_IK_vanskilagjald_typeItem) _items.elementAt(index);
    } //-- is.idega.block.finance.business.li.claims.LI_IK_vanskilagjald_typeItem getLI_IK_vanskilagjald_typeItem(int) 

    /**
     * Method getLI_IK_vanskilagjald_typeItem
     * 
     * 
     * 
     * @return LI_IK_vanskilagjald_typeItem
     */
    public is.idega.block.finance.business.li.claims.LI_IK_vanskilagjald_typeItem[] getLI_IK_vanskilagjald_typeItem()
    {
        int size = _items.size();
        is.idega.block.finance.business.li.claims.LI_IK_vanskilagjald_typeItem[] mArray = new is.idega.block.finance.business.li.claims.LI_IK_vanskilagjald_typeItem[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (is.idega.block.finance.business.li.claims.LI_IK_vanskilagjald_typeItem) _items.elementAt(index);
        }
        return mArray;
    } //-- is.idega.block.finance.business.li.claims.LI_IK_vanskilagjald_typeItem[] getLI_IK_vanskilagjald_typeItem() 

    /**
     * Method getLI_IK_vanskilagjald_typeItemCount
     * 
     * 
     * 
     * @return int
     */
    public int getLI_IK_vanskilagjald_typeItemCount()
    {
        return _items.size();
    } //-- int getLI_IK_vanskilagjald_typeItemCount() 

    /**
     * Returns the value of field 'tegund_vanskilagjalds'.
     * 
     * @return LI_IK_tegund_afslattar_og_vanskilagjalds_type
     * @return the value of field 'tegund_vanskilagjalds'.
     */
    public is.idega.block.finance.business.li.claims.types.LI_IK_tegund_afslattar_og_vanskilagjalds_type getTegund_vanskilagjalds()
    {
        return this._tegund_vanskilagjalds;
    } //-- is.idega.block.finance.business.li.claims.types.LI_IK_tegund_afslattar_og_vanskilagjalds_type getTegund_vanskilagjalds() 

    /**
     * Returns the value of field 'vidmidun'.
     * 
     * @return LI_IK_vidmidun_dags_type
     * @return the value of field 'vidmidun'.
     */
    public is.idega.block.finance.business.li.claims.types.LI_IK_vidmidun_dags_type getVidmidun()
    {
        return this._vidmidun;
    } //-- is.idega.block.finance.business.li.claims.types.LI_IK_vidmidun_dags_type getVidmidun() 

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
     * Method removeAllLI_IK_vanskilagjald_typeItem
     * 
     */
    public void removeAllLI_IK_vanskilagjald_typeItem()
    {
        _items.removeAllElements();
    } //-- void removeAllLI_IK_vanskilagjald_typeItem() 

    /**
     * Method removeLI_IK_vanskilagjald_typeItem
     * 
     * 
     * 
     * @param index
     * @return LI_IK_vanskilagjald_typeItem
     */
    public is.idega.block.finance.business.li.claims.LI_IK_vanskilagjald_typeItem removeLI_IK_vanskilagjald_typeItem(int index)
    {
        java.lang.Object obj = _items.elementAt(index);
        _items.removeElementAt(index);
        return (is.idega.block.finance.business.li.claims.LI_IK_vanskilagjald_typeItem) obj;
    } //-- is.idega.block.finance.business.li.claims.LI_IK_vanskilagjald_typeItem removeLI_IK_vanskilagjald_typeItem(int) 

    /**
     * Method setLI_IK_vanskilagjald_typeItem
     * 
     * 
     * 
     * @param index
     * @param vLI_IK_vanskilagjald_typeItem
     */
    public void setLI_IK_vanskilagjald_typeItem(int index, is.idega.block.finance.business.li.claims.LI_IK_vanskilagjald_typeItem vLI_IK_vanskilagjald_typeItem)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _items.size())) {
            throw new IndexOutOfBoundsException();
        }
        _items.setElementAt(vLI_IK_vanskilagjald_typeItem, index);
    } //-- void setLI_IK_vanskilagjald_typeItem(int, is.idega.block.finance.business.li.claims.LI_IK_vanskilagjald_typeItem) 

    /**
     * Method setLI_IK_vanskilagjald_typeItem
     * 
     * 
     * 
     * @param LI_IK_vanskilagjald_typeItemArray
     */
    public void setLI_IK_vanskilagjald_typeItem(is.idega.block.finance.business.li.claims.LI_IK_vanskilagjald_typeItem[] LI_IK_vanskilagjald_typeItemArray)
    {
        //-- copy array
        _items.removeAllElements();
        for (int i = 0; i < LI_IK_vanskilagjald_typeItemArray.length; i++) {
            _items.addElement(LI_IK_vanskilagjald_typeItemArray[i]);
        }
    } //-- void setLI_IK_vanskilagjald_typeItem(is.idega.block.finance.business.li.claims.LI_IK_vanskilagjald_typeItem) 

    /**
     * Sets the value of field 'tegund_vanskilagjalds'.
     * 
     * @param tegund_vanskilagjalds the value of field
     * 'tegund_vanskilagjalds'.
     */
    public void setTegund_vanskilagjalds(is.idega.block.finance.business.li.claims.types.LI_IK_tegund_afslattar_og_vanskilagjalds_type tegund_vanskilagjalds)
    {
        this._tegund_vanskilagjalds = tegund_vanskilagjalds;
    } //-- void setTegund_vanskilagjalds(is.idega.block.finance.business.li.claims.types.LI_IK_tegund_afslattar_og_vanskilagjalds_type) 

    /**
     * Sets the value of field 'vidmidun'.
     * 
     * @param vidmidun the value of field 'vidmidun'.
     */
    public void setVidmidun(is.idega.block.finance.business.li.claims.types.LI_IK_vidmidun_dags_type vidmidun)
    {
        this._vidmidun = vidmidun;
    } //-- void setVidmidun(is.idega.block.finance.business.li.claims.types.LI_IK_vidmidun_dags_type) 

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
        return (is.idega.block.finance.business.li.claims.LI_IK_vanskilagjald_type) Unmarshaller.unmarshal(is.idega.block.finance.business.li.claims.LI_IK_vanskilagjald_type.class, reader);
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
