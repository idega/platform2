/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_astand_reiknings_type.java,v 1.1 2005/03/15 11:35:03 birna Exp $
 */

package is.idega.block.finance.business.li.sign_in.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * Class LI_astand_reiknings_type.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:03 $
 */
public class LI_astand_reiknings_type implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The OPINN type
     */
    public static final int OPINN_TYPE = 0;

    /**
     * The instance of the OPINN type
     */
    public static final LI_astand_reiknings_type OPINN = new LI_astand_reiknings_type(OPINN_TYPE, "OPINN");

    /**
     * The LOKADUR type
     */
    public static final int LOKADUR_TYPE = 1;

    /**
     * The instance of the LOKADUR type
     */
    public static final LI_astand_reiknings_type LOKADUR = new LI_astand_reiknings_type(LOKADUR_TYPE, "LOKADUR");

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

    private LI_astand_reiknings_type(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- is.idega.block.finance.business.li.sign_in.types.LI_astand_reiknings_type(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate
     * 
     * Returns an enumeration of all possible instances of
     * LI_astand_reiknings_type
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
     * Returns the type of this LI_astand_reiknings_type
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
        members.put("OPINN", OPINN);
        members.put("LOKADUR", LOKADUR);
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
     * LI_astand_reiknings_type
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
     * Returns a new LI_astand_reiknings_type based on the given
     * String value.
     * 
     * @param string
     * @return LI_astand_reiknings_type
     */
    public static is.idega.block.finance.business.li.sign_in.types.LI_astand_reiknings_type valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid LI_astand_reiknings_type";
            throw new IllegalArgumentException(err);
        }
        return (LI_astand_reiknings_type) obj;
    } //-- is.idega.block.finance.business.li.sign_in.types.LI_astand_reiknings_type valueOf(java.lang.String) 

}
