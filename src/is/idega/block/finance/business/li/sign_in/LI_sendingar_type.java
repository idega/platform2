/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_sendingar_type.java,v 1.1 2005/03/15 11:35:07 birna Exp $
 */

package is.idega.block.finance.business.li.sign_in;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Vector;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class LI_sendingar_type.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:07 $
 */
public class LI_sendingar_type implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _items
     */
    private java.util.Vector _items;


      //----------------/
     //- Constructors -/
    //----------------/

    public LI_sendingar_type() {
        super();
        _items = new Vector();
    } //-- is.idega.block.finance.business.li.sign_in.LI_sendingar_type()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addLI_sendingar_typeItem
     * 
     * 
     * 
     * @param vLI_sendingar_typeItem
     */
    public void addLI_sendingar_typeItem(is.idega.block.finance.business.li.sign_in.LI_sendingar_typeItem vLI_sendingar_typeItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _items.addElement(vLI_sendingar_typeItem);
    } //-- void addLI_sendingar_typeItem(is.idega.block.finance.business.li.sign_in.LI_sendingar_typeItem) 

    /**
     * Method addLI_sendingar_typeItem
     * 
     * 
     * 
     * @param index
     * @param vLI_sendingar_typeItem
     */
    public void addLI_sendingar_typeItem(int index, is.idega.block.finance.business.li.sign_in.LI_sendingar_typeItem vLI_sendingar_typeItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _items.insertElementAt(vLI_sendingar_typeItem, index);
    } //-- void addLI_sendingar_typeItem(int, is.idega.block.finance.business.li.sign_in.LI_sendingar_typeItem) 

    /**
     * Method enumerateLI_sendingar_typeItem
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateLI_sendingar_typeItem()
    {
        return _items.elements();
    } //-- java.util.Enumeration enumerateLI_sendingar_typeItem() 

    /**
     * Method getLI_sendingar_typeItem
     * 
     * 
     * 
     * @param index
     * @return LI_sendingar_typeItem
     */
    public is.idega.block.finance.business.li.sign_in.LI_sendingar_typeItem getLI_sendingar_typeItem(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _items.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (is.idega.block.finance.business.li.sign_in.LI_sendingar_typeItem) _items.elementAt(index);
    } //-- is.idega.block.finance.business.li.sign_in.LI_sendingar_typeItem getLI_sendingar_typeItem(int) 

    /**
     * Method getLI_sendingar_typeItem
     * 
     * 
     * 
     * @return LI_sendingar_typeItem
     */
    public is.idega.block.finance.business.li.sign_in.LI_sendingar_typeItem[] getLI_sendingar_typeItem()
    {
        int size = _items.size();
        is.idega.block.finance.business.li.sign_in.LI_sendingar_typeItem[] mArray = new is.idega.block.finance.business.li.sign_in.LI_sendingar_typeItem[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (is.idega.block.finance.business.li.sign_in.LI_sendingar_typeItem) _items.elementAt(index);
        }
        return mArray;
    } //-- is.idega.block.finance.business.li.sign_in.LI_sendingar_typeItem[] getLI_sendingar_typeItem() 

    /**
     * Method getLI_sendingar_typeItemCount
     * 
     * 
     * 
     * @return int
     */
    public int getLI_sendingar_typeItemCount()
    {
        return _items.size();
    } //-- int getLI_sendingar_typeItemCount() 

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
     * Method removeAllLI_sendingar_typeItem
     * 
     */
    public void removeAllLI_sendingar_typeItem()
    {
        _items.removeAllElements();
    } //-- void removeAllLI_sendingar_typeItem() 

    /**
     * Method removeLI_sendingar_typeItem
     * 
     * 
     * 
     * @param index
     * @return LI_sendingar_typeItem
     */
    public is.idega.block.finance.business.li.sign_in.LI_sendingar_typeItem removeLI_sendingar_typeItem(int index)
    {
        java.lang.Object obj = _items.elementAt(index);
        _items.removeElementAt(index);
        return (is.idega.block.finance.business.li.sign_in.LI_sendingar_typeItem) obj;
    } //-- is.idega.block.finance.business.li.sign_in.LI_sendingar_typeItem removeLI_sendingar_typeItem(int) 

    /**
     * Method setLI_sendingar_typeItem
     * 
     * 
     * 
     * @param index
     * @param vLI_sendingar_typeItem
     */
    public void setLI_sendingar_typeItem(int index, is.idega.block.finance.business.li.sign_in.LI_sendingar_typeItem vLI_sendingar_typeItem)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _items.size())) {
            throw new IndexOutOfBoundsException();
        }
        _items.setElementAt(vLI_sendingar_typeItem, index);
    } //-- void setLI_sendingar_typeItem(int, is.idega.block.finance.business.li.sign_in.LI_sendingar_typeItem) 

    /**
     * Method setLI_sendingar_typeItem
     * 
     * 
     * 
     * @param LI_sendingar_typeItemArray
     */
    public void setLI_sendingar_typeItem(is.idega.block.finance.business.li.sign_in.LI_sendingar_typeItem[] LI_sendingar_typeItemArray)
    {
        //-- copy array
        _items.removeAllElements();
        for (int i = 0; i < LI_sendingar_typeItemArray.length; i++) {
            _items.addElement(LI_sendingar_typeItemArray[i]);
        }
    } //-- void setLI_sendingar_typeItem(is.idega.block.finance.business.li.sign_in.LI_sendingar_typeItem) 

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
        return (is.idega.block.finance.business.li.sign_in.LI_sendingar_type) Unmarshaller.unmarshal(is.idega.block.finance.business.li.sign_in.LI_sendingar_type.class, reader);
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
