/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: Krofur.java,v 1.1 2005/03/15 11:35:03 birna Exp $
 */

package is.idega.block.finance.business.li.claims;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Vector;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class Krofur.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:03 $
 */
public class Krofur implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _krafaList
     */
    private java.util.Vector _krafaList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Krofur() {
        super();
        _krafaList = new Vector();
    } //-- is.idega.block.finance.business.li.claims.Krofur()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addKrafa
     * 
     * 
     * 
     * @param vKrafa
     */
    public void addKrafa(is.idega.block.finance.business.li.claims.Krafa vKrafa)
        throws java.lang.IndexOutOfBoundsException
    {
        if (!(_krafaList.size() < 500)) {
            throw new IndexOutOfBoundsException();
        }
        _krafaList.addElement(vKrafa);
    } //-- void addKrafa(is.idega.block.finance.business.li.claims.Krafa) 

    /**
     * Method addKrafa
     * 
     * 
     * 
     * @param index
     * @param vKrafa
     */
    public void addKrafa(int index, is.idega.block.finance.business.li.claims.Krafa vKrafa)
        throws java.lang.IndexOutOfBoundsException
    {
        if (!(_krafaList.size() < 500)) {
            throw new IndexOutOfBoundsException();
        }
        _krafaList.insertElementAt(vKrafa, index);
    } //-- void addKrafa(int, is.idega.block.finance.business.li.claims.Krafa) 

    /**
     * Method enumerateKrafa
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateKrafa()
    {
        return _krafaList.elements();
    } //-- java.util.Enumeration enumerateKrafa() 

    /**
     * Method getKrafa
     * 
     * 
     * 
     * @param index
     * @return Krafa
     */
    public is.idega.block.finance.business.li.claims.Krafa getKrafa(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _krafaList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (is.idega.block.finance.business.li.claims.Krafa) _krafaList.elementAt(index);
    } //-- is.idega.block.finance.business.li.claims.Krafa getKrafa(int) 

    /**
     * Method getKrafa
     * 
     * 
     * 
     * @return Krafa
     */
    public is.idega.block.finance.business.li.claims.Krafa[] getKrafa()
    {
        int size = _krafaList.size();
        is.idega.block.finance.business.li.claims.Krafa[] mArray = new is.idega.block.finance.business.li.claims.Krafa[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (is.idega.block.finance.business.li.claims.Krafa) _krafaList.elementAt(index);
        }
        return mArray;
    } //-- is.idega.block.finance.business.li.claims.Krafa[] getKrafa() 

    /**
     * Method getKrafaCount
     * 
     * 
     * 
     * @return int
     */
    public int getKrafaCount()
    {
        return _krafaList.size();
    } //-- int getKrafaCount() 

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
     * Method removeAllKrafa
     * 
     */
    public void removeAllKrafa()
    {
        _krafaList.removeAllElements();
    } //-- void removeAllKrafa() 

    /**
     * Method removeKrafa
     * 
     * 
     * 
     * @param index
     * @return Krafa
     */
    public is.idega.block.finance.business.li.claims.Krafa removeKrafa(int index)
    {
        java.lang.Object obj = _krafaList.elementAt(index);
        _krafaList.removeElementAt(index);
        return (is.idega.block.finance.business.li.claims.Krafa) obj;
    } //-- is.idega.block.finance.business.li.claims.Krafa removeKrafa(int) 

    /**
     * Method setKrafa
     * 
     * 
     * 
     * @param index
     * @param vKrafa
     */
    public void setKrafa(int index, is.idega.block.finance.business.li.claims.Krafa vKrafa)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _krafaList.size())) {
            throw new IndexOutOfBoundsException();
        }
        if (!(index < 500)) {
            throw new IndexOutOfBoundsException();
        }
        _krafaList.setElementAt(vKrafa, index);
    } //-- void setKrafa(int, is.idega.block.finance.business.li.claims.Krafa) 

    /**
     * Method setKrafa
     * 
     * 
     * 
     * @param krafaArray
     */
    public void setKrafa(is.idega.block.finance.business.li.claims.Krafa[] krafaArray)
    {
        //-- copy array
        _krafaList.removeAllElements();
        for (int i = 0; i < krafaArray.length; i++) {
            _krafaList.addElement(krafaArray[i]);
        }
    } //-- void setKrafa(is.idega.block.finance.business.li.claims.Krafa) 

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
        return (is.idega.block.finance.business.li.claims.Krofur) Unmarshaller.unmarshal(is.idega.block.finance.business.li.claims.Krofur.class, reader);
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
