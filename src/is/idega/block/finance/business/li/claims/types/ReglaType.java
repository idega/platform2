/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: ReglaType.java,v 1.1 2005/03/15 11:35:02 birna Exp $
 */

package is.idega.block.finance.business.li.claims.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * Class ReglaType.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:02 $
 */
public class ReglaType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The 360/360 type
     */
    public static final int VALUE_0_TYPE = 0;

    /**
     * The instance of the 360/360 type
     */
    public static final ReglaType VALUE_0 = new ReglaType(VALUE_0_TYPE, "360/360");

    /**
     * The RAUN/360 type
     */
    public static final int VALUE_1_TYPE = 1;

    /**
     * The instance of the RAUN/360 type
     */
    public static final ReglaType VALUE_1 = new ReglaType(VALUE_1_TYPE, "RAUN/360");

    /**
     * The RAUN/RAUN type
     */
    public static final int VALUE_2_TYPE = 2;

    /**
     * The instance of the RAUN/RAUN type
     */
    public static final ReglaType VALUE_2 = new ReglaType(VALUE_2_TYPE, "RAUN/RAUN");

    /**
     * The EITT_PROSENT_A_DAG type
     */
    public static final int VALUE_3_TYPE = 3;

    /**
     * The instance of the EITT_PROSENT_A_DAG type
     */
    public static final ReglaType VALUE_3 = new ReglaType(VALUE_3_TYPE, "EITT_PROSENT_A_DAG");

    /**
     * The ENGIR_DRATTARVEXTIR type
     */
    public static final int VALUE_4_TYPE = 4;

    /**
     * The instance of the ENGIR_DRATTARVEXTIR type
     */
    public static final ReglaType VALUE_4 = new ReglaType(VALUE_4_TYPE, "ENGIR_DRATTARVEXTIR");

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

    private ReglaType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- is.idega.block.finance.business.li.claims.types.ReglaType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate
     * 
     * Returns an enumeration of all possible instances of
     * ReglaType
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
     * Returns the type of this ReglaType
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
        members.put("360/360", VALUE_0);
        members.put("RAUN/360", VALUE_1);
        members.put("RAUN/RAUN", VALUE_2);
        members.put("EITT_PROSENT_A_DAG", VALUE_3);
        members.put("ENGIR_DRATTARVEXTIR", VALUE_4);
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
     * Returns the String representation of this ReglaType
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
     * Returns a new ReglaType based on the given String value.
     * 
     * @param string
     * @return ReglaType
     */
    public static is.idega.block.finance.business.li.claims.types.ReglaType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid ReglaType";
            throw new IllegalArgumentException(err);
        }
        return (ReglaType) obj;
    } //-- is.idega.block.finance.business.li.claims.types.ReglaType valueOf(java.lang.String) 

}
