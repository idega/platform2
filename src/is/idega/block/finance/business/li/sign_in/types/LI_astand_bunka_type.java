/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_astand_bunka_type.java,v 1.1 2005/03/15 11:35:03 birna Exp $
 */

package is.idega.block.finance.business.li.sign_in.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * Class LI_astand_bunka_type.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:03 $
 */
public class LI_astand_bunka_type implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The OBOKADUR type
     */
    public static final int OBOKADUR_TYPE = 0;

    /**
     * The instance of the OBOKADUR type
     */
    public static final LI_astand_bunka_type OBOKADUR = new LI_astand_bunka_type(OBOKADUR_TYPE, "OBOKADUR");

    /**
     * The BOKADUR type
     */
    public static final int BOKADUR_TYPE = 1;

    /**
     * The instance of the BOKADUR type
     */
    public static final LI_astand_bunka_type BOKADUR = new LI_astand_bunka_type(BOKADUR_TYPE, "BOKADUR");

    /**
     * The VILLA type
     */
    public static final int VILLA_TYPE = 2;

    /**
     * The instance of the VILLA type
     */
    public static final LI_astand_bunka_type VILLA = new LI_astand_bunka_type(VILLA_TYPE, "VILLA");

    /**
     * The EYTT type
     */
    public static final int EYTT_TYPE = 3;

    /**
     * The instance of the EYTT type
     */
    public static final LI_astand_bunka_type EYTT = new LI_astand_bunka_type(EYTT_TYPE, "EYTT");

    /**
     * The I_VINNSLU type
     */
    public static final int I_VINNSLU_TYPE = 4;

    /**
     * The instance of the I_VINNSLU type
     */
    public static final LI_astand_bunka_type I_VINNSLU = new LI_astand_bunka_type(I_VINNSLU_TYPE, "I_VINNSLU");

    /**
     * The OTHEKKT_ASTAND type
     */
    public static final int OTHEKKT_ASTAND_TYPE = 5;

    /**
     * The instance of the OTHEKKT_ASTAND type
     */
    public static final LI_astand_bunka_type OTHEKKT_ASTAND = new LI_astand_bunka_type(OTHEKKT_ASTAND_TYPE, "OTHEKKT_ASTAND");

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

    private LI_astand_bunka_type(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- is.idega.block.finance.business.li.sign_in.types.LI_astand_bunka_type(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate
     * 
     * Returns an enumeration of all possible instances of
     * LI_astand_bunka_type
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
     * Returns the type of this LI_astand_bunka_type
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
        members.put("OBOKADUR", OBOKADUR);
        members.put("BOKADUR", BOKADUR);
        members.put("VILLA", VILLA);
        members.put("EYTT", EYTT);
        members.put("I_VINNSLU", I_VINNSLU);
        members.put("OTHEKKT_ASTAND", OTHEKKT_ASTAND);
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
     * LI_astand_bunka_type
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
     * Returns a new LI_astand_bunka_type based on the given String
     * value.
     * 
     * @param string
     * @return LI_astand_bunka_type
     */
    public static is.idega.block.finance.business.li.sign_in.types.LI_astand_bunka_type valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid LI_astand_bunka_type";
            throw new IllegalArgumentException(err);
        }
        return (LI_astand_bunka_type) obj;
    } //-- is.idega.block.finance.business.li.sign_in.types.LI_astand_bunka_type valueOf(java.lang.String) 

}
