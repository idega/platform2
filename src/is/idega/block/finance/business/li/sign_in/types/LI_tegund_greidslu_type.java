/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_tegund_greidslu_type.java,v 1.1 2005/03/15 11:35:03 birna Exp $
 */

package is.idega.block.finance.business.li.sign_in.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * Class LI_tegund_greidslu_type.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:03 $
 */
public class LI_tegund_greidslu_type implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The AB-GIRO type
     */
    public static final int AB_GIRO_TYPE = 0;

    /**
     * The instance of the AB-GIRO type
     */
    public static final LI_tegund_greidslu_type AB_GIRO = new LI_tegund_greidslu_type(AB_GIRO_TYPE, "AB-GIRO");

    /**
     * The C-GIRO type
     */
    public static final int C_GIRO_TYPE = 1;

    /**
     * The instance of the C-GIRO type
     */
    public static final LI_tegund_greidslu_type C_GIRO = new LI_tegund_greidslu_type(C_GIRO_TYPE, "C-GIRO");

    /**
     * The GREIDSLUSEDILL type
     */
    public static final int GREIDSLUSEDILL_TYPE = 2;

    /**
     * The instance of the GREIDSLUSEDILL type
     */
    public static final LI_tegund_greidslu_type GREIDSLUSEDILL = new LI_tegund_greidslu_type(GREIDSLUSEDILL_TYPE, "GREIDSLUSEDILL");

    /**
     * The MILLIFAERSLA type
     */
    public static final int MILLIFAERSLA_TYPE = 3;

    /**
     * The instance of the MILLIFAERSLA type
     */
    public static final LI_tegund_greidslu_type MILLIFAERSLA = new LI_tegund_greidslu_type(MILLIFAERSLA_TYPE, "MILLIFAERSLA");

    /**
     * The OTHEKKT_TEGUND type
     */
    public static final int OTHEKKT_TEGUND_TYPE = 4;

    /**
     * The instance of the OTHEKKT_TEGUND type
     */
    public static final LI_tegund_greidslu_type OTHEKKT_TEGUND = new LI_tegund_greidslu_type(OTHEKKT_TEGUND_TYPE, "OTHEKKT_TEGUND");

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

    private LI_tegund_greidslu_type(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- is.idega.block.finance.business.li.sign_in.types.LI_tegund_greidslu_type(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate
     * 
     * Returns an enumeration of all possible instances of
     * LI_tegund_greidslu_type
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
     * Returns the type of this LI_tegund_greidslu_type
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
        members.put("AB-GIRO", AB_GIRO);
        members.put("C-GIRO", C_GIRO);
        members.put("GREIDSLUSEDILL", GREIDSLUSEDILL);
        members.put("MILLIFAERSLA", MILLIFAERSLA);
        members.put("OTHEKKT_TEGUND", OTHEKKT_TEGUND);
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
     * LI_tegund_greidslu_type
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
     * Returns a new LI_tegund_greidslu_type based on the given
     * String value.
     * 
     * @param string
     * @return LI_tegund_greidslu_type
     */
    public static is.idega.block.finance.business.li.sign_in.types.LI_tegund_greidslu_type valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid LI_tegund_greidslu_type";
            throw new IllegalArgumentException(err);
        }
        return (LI_tegund_greidslu_type) obj;
    } //-- is.idega.block.finance.business.li.sign_in.types.LI_tegund_greidslu_type valueOf(java.lang.String) 

}
