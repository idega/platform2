/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_IK_astand_type.java,v 1.1 2005/03/15 11:35:02 birna Exp $
 */

package is.idega.block.finance.business.li.claims.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * Class LI_IK_astand_type.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:02 $
 */
public class LI_IK_astand_type implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The ÓGREIDD type
     */
    public static final int VALUE_0_TYPE = 0;

    /**
     * The instance of the ÓGREIDD type
     */
    public static final LI_IK_astand_type VALUE_0 = new LI_IK_astand_type(VALUE_0_TYPE, "ÓGREIDD");

    /**
     * The GREIDD type
     */
    public static final int VALUE_1_TYPE = 1;

    /**
     * The instance of the GREIDD type
     */
    public static final LI_IK_astand_type VALUE_1 = new LI_IK_astand_type(VALUE_1_TYPE, "GREIDD");

    /**
     * The NIÐURFELLD type
     */
    public static final int VALUE_2_TYPE = 2;

    /**
     * The instance of the NIÐURFELLD type
     */
    public static final LI_IK_astand_type VALUE_2 = new LI_IK_astand_type(VALUE_2_TYPE, "NIÐURFELLD");

    /**
     * The MILLIINNHEIMTA type
     */
    public static final int VALUE_3_TYPE = 3;

    /**
     * The instance of the MILLIINNHEIMTA type
     */
    public static final LI_IK_astand_type VALUE_3 = new LI_IK_astand_type(VALUE_3_TYPE, "MILLIINNHEIMTA");

    /**
     * The LÖGFRÆÐIINNHEIMTA type
     */
    public static final int VALUE_4_TYPE = 4;

    /**
     * The instance of the LÖGFRÆÐIINNHEIMTA type
     */
    public static final LI_IK_astand_type VALUE_4 = new LI_IK_astand_type(VALUE_4_TYPE, "LÖGFRÆÐIINNHEIMTA");

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

    private LI_IK_astand_type(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- is.idega.block.finance.business.li.claims.types.LI_IK_astand_type(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate
     * 
     * Returns an enumeration of all possible instances of
     * LI_IK_astand_type
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
     * Returns the type of this LI_IK_astand_type
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
        members.put("ÓGREIDD", VALUE_0);
        members.put("GREIDD", VALUE_1);
        members.put("NIÐURFELLD", VALUE_2);
        members.put("MILLIINNHEIMTA", VALUE_3);
        members.put("LÖGFRÆÐIINNHEIMTA", VALUE_4);
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
     * Returns the String representation of this LI_IK_astand_type
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
     * Returns a new LI_IK_astand_type based on the given String
     * value.
     * 
     * @param string
     * @return LI_IK_astand_type
     */
    public static is.idega.block.finance.business.li.claims.types.LI_IK_astand_type valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid LI_IK_astand_type";
            throw new IllegalArgumentException(err);
        }
        return (LI_IK_astand_type) obj;
    } //-- is.idega.block.finance.business.li.claims.types.LI_IK_astand_type valueOf(java.lang.String) 

}
