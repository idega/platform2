/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_strengjategund_type.java,v 1.1 2005/03/15 11:35:03 birna Exp $
 */

package is.idega.block.finance.business.li.sign_in.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * Class LI_strengjategund_type.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:03 $
 */
public class LI_strengjategund_type implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The AHAFNAGJALDEYRIR type
     */
    public static final int AHAFNAGJALDEYRIR_TYPE = 0;

    /**
     * The instance of the AHAFNAGJALDEYRIR type
     */
    public static final LI_strengjategund_type AHAFNAGJALDEYRIR = new LI_strengjategund_type(AHAFNAGJALDEYRIR_TYPE, "AHAFNAGJALDEYRIR");

    /**
     * The LAUNAGREIDSLUR type
     */
    public static final int LAUNAGREIDSLUR_TYPE = 1;

    /**
     * The instance of the LAUNAGREIDSLUR type
     */
    public static final LI_strengjategund_type LAUNAGREIDSLUR = new LI_strengjategund_type(LAUNAGREIDSLUR_TYPE, "LAUNAGREIDSLUR");

    /**
     * The BIRTINGARKERFI_VIGOR type
     */
    public static final int BIRTINGARKERFI_VIGOR_TYPE = 2;

    /**
     * The instance of the BIRTINGARKERFI_VIGOR type
     */
    public static final LI_strengjategund_type BIRTINGARKERFI_VIGOR = new LI_strengjategund_type(BIRTINGARKERFI_VIGOR_TYPE, "BIRTINGARKERFI_VIGOR");

    /**
     * Field _memberTable
     */
    private static java.util.Hashtable _memberTable = init();

    /**
     * Field type
     */
    private int type = -1;

    /**
     * Field stringValue
     */
    private java.lang.String stringValue = null;


      //----------------/
     //- Constructors -/
    //----------------/

    private LI_strengjategund_type(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- is.idega.block.finance.business.li.sign_in.types.LI_strengjategund_type(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate
     * 
     * Returns an enumeration of all possible instances of
     * LI_strengjategund_type
     * 
     * @return Enumeration
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getType
     * 
     * Returns the type of this LI_strengjategund_type
     * 
     * @return int
     */
    public int getType()
    {
        return this.type;
    } //-- int getType() 

    /**
     * Method init
     * 
     * 
     * 
     * @return Hashtable
     */
    private static java.util.Hashtable init()
    {
        Hashtable members = new Hashtable();
        members.put("AHAFNAGJALDEYRIR", AHAFNAGJALDEYRIR);
        members.put("LAUNAGREIDSLUR", LAUNAGREIDSLUR);
        members.put("BIRTINGARKERFI_VIGOR", BIRTINGARKERFI_VIGOR);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method readResolve
     * 
     *  will be called during deserialization to replace the
     * deserialized object with the correct constant instance.
     * <br/>
     * 
     * @return Object
     */
    private java.lang.Object readResolve()
    {
        return valueOf(this.stringValue);
    } //-- java.lang.Object readResolve() 

    /**
     * Method toString
     * 
     * Returns the String representation of this
     * LI_strengjategund_type
     * 
     * @return String
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOf
     * 
     * Returns a new LI_strengjategund_type based on the given
     * String value.
     * 
     * @param string
     * @return LI_strengjategund_type
     */
    public static is.idega.block.finance.business.li.sign_in.types.LI_strengjategund_type valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid LI_strengjategund_type";
            throw new IllegalArgumentException(err);
        }
        return (LI_strengjategund_type) obj;
    } //-- is.idega.block.finance.business.li.sign_in.types.LI_strengjategund_type valueOf(java.lang.String) 

}
