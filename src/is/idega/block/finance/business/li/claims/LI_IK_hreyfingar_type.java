/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_IK_hreyfingar_type.java,v 1.1 2005/03/15 11:35:06 birna Exp $
 */

package is.idega.block.finance.business.li.claims;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Vector;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class LI_IK_hreyfingar_type.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:06 $
 */
public class LI_IK_hreyfingar_type implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _hreyfingList
     */
    private java.util.Vector _hreyfingList;


      //----------------/
     //- Constructors -/
    //----------------/

    public LI_IK_hreyfingar_type() {
        super();
        _hreyfingList = new Vector();
    } //-- is.idega.block.finance.business.li.claims.LI_IK_hreyfingar_type()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addHreyfing
     * 
     * 
     * 
     * @param vHreyfing
     */
    public void addHreyfing(is.idega.block.finance.business.li.claims.Hreyfing vHreyfing)
        throws java.lang.IndexOutOfBoundsException
    {
        _hreyfingList.addElement(vHreyfing);
    } //-- void addHreyfing(is.idega.block.finance.business.li.claims.Hreyfing) 

    /**
     * Method addHreyfing
     * 
     * 
     * 
     * @param index
     * @param vHreyfing
     */
    public void addHreyfing(int index, is.idega.block.finance.business.li.claims.Hreyfing vHreyfing)
        throws java.lang.IndexOutOfBoundsException
    {
        _hreyfingList.insertElementAt(vHreyfing, index);
    } //-- void addHreyfing(int, is.idega.block.finance.business.li.claims.Hreyfing) 

    /**
     * Method enumerateHreyfing
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateHreyfing()
    {
        return _hreyfingList.elements();
    } //-- java.util.Enumeration enumerateHreyfing() 

    /**
     * Method getHreyfing
     * 
     * 
     * 
     * @param index
     * @return Hreyfing
     */
    public is.idega.block.finance.business.li.claims.Hreyfing getHreyfing(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _hreyfingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (is.idega.block.finance.business.li.claims.Hreyfing) _hreyfingList.elementAt(index);
    } //-- is.idega.block.finance.business.li.claims.Hreyfing getHreyfing(int) 

    /**
     * Method getHreyfing
     * 
     * 
     * 
     * @return Hreyfing
     */
    public is.idega.block.finance.business.li.claims.Hreyfing[] getHreyfing()
    {
        int size = _hreyfingList.size();
        is.idega.block.finance.business.li.claims.Hreyfing[] mArray = new is.idega.block.finance.business.li.claims.Hreyfing[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (is.idega.block.finance.business.li.claims.Hreyfing) _hreyfingList.elementAt(index);
        }
        return mArray;
    } //-- is.idega.block.finance.business.li.claims.Hreyfing[] getHreyfing() 

    /**
     * Method getHreyfingCount
     * 
     * 
     * 
     * @return int
     */
    public int getHreyfingCount()
    {
        return _hreyfingList.size();
    } //-- int getHreyfingCount() 

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
     * Method removeAllHreyfing
     * 
     */
    public void removeAllHreyfing()
    {
        _hreyfingList.removeAllElements();
    } //-- void removeAllHreyfing() 

    /**
     * Method removeHreyfing
     * 
     * 
     * 
     * @param index
     * @return Hreyfing
     */
    public is.idega.block.finance.business.li.claims.Hreyfing removeHreyfing(int index)
    {
        java.lang.Object obj = _hreyfingList.elementAt(index);
        _hreyfingList.removeElementAt(index);
        return (is.idega.block.finance.business.li.claims.Hreyfing) obj;
    } //-- is.idega.block.finance.business.li.claims.Hreyfing removeHreyfing(int) 

    /**
     * Method setHreyfing
     * 
     * 
     * 
     * @param index
     * @param vHreyfing
     */
    public void setHreyfing(int index, is.idega.block.finance.business.li.claims.Hreyfing vHreyfing)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _hreyfingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _hreyfingList.setElementAt(vHreyfing, index);
    } //-- void setHreyfing(int, is.idega.block.finance.business.li.claims.Hreyfing) 

    /**
     * Method setHreyfing
     * 
     * 
     * 
     * @param hreyfingArray
     */
    public void setHreyfing(is.idega.block.finance.business.li.claims.Hreyfing[] hreyfingArray)
    {
        //-- copy array
        _hreyfingList.removeAllElements();
        for (int i = 0; i < hreyfingArray.length; i++) {
            _hreyfingList.addElement(hreyfingArray[i]);
        }
    } //-- void setHreyfing(is.idega.block.finance.business.li.claims.Hreyfing) 

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
        return (is.idega.block.finance.business.li.claims.LI_IK_hreyfingar_type) Unmarshaller.unmarshal(is.idega.block.finance.business.li.claims.LI_IK_hreyfingar_type.class, reader);
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
