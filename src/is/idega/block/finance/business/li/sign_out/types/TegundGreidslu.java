/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: TegundGreidslu.java,v 1.1 2005/03/15 11:35:08 birna Exp $
 */

package is.idega.block.finance.business.li.sign_out.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * Class TegundGreidslu.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:08 $
 */
public class TegundGreidslu implements java.io.Serializable {


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
    public static final TegundGreidslu AB_GIRO = new TegundGreidslu(AB_GIRO_TYPE, "AB-GIRO");

    /**
     * The C-GIRO type
     */
    public static final int C_GIRO_TYPE = 1;

    /**
     * The instance of the C-GIRO type
     */
    public static final TegundGreidslu C_GIRO = new TegundGreidslu(C_GIRO_TYPE, "C-GIRO");

    /**
     * The GREIDSLUSEDILL type
     */
    public static final int GREIDSLUSEDILL_TYPE = 2;

    /**
     * The instance of the GREIDSLUSEDILL type
     */
    public static final TegundGreidslu GREIDSLUSEDILL = new TegundGreidslu(GREIDSLUSEDILL_TYPE, "GREIDSLUSEDILL");

    /**
     * The MILLIFAERSLA type
     */
    public static final int MILLIFAERSLA_TYPE = 3;

    /**
     * The instance of the MILLIFAERSLA type
     */
    public static final TegundGreidslu MILLIFAERSLA = new TegundGreidslu(MILLIFAERSLA_TYPE, "MILLIFAERSLA");

    /**
     * The OTHEKKT_TEGUND type
     */
    public static final int OTHEKKT_TEGUND_TYPE = 4;

    /**
     * The instance of the OTHEKKT_TEGUND type
     */
    public static final TegundGreidslu OTHEKKT_TEGUND = new TegundGreidslu(OTHEKKT_TEGUND_TYPE, "OTHEKKT_TEGUND");

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

    private TegundGreidslu(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- is.idega.block.finance.business.li.sign_out.types.TegundGreidslu(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate
     * 
     * Returns an enumeration of all possible instances of
     * TegundGreidslu
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
     * Returns the type of this TegundGreidslu
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
     * Returns the String representation of this TegundGreidslu
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
     * Returns a new TegundGreidslu based on the given String
     * value.
     * 
     * @param string
     * @return TegundGreidslu
     */
    public static is.idega.block.finance.business.li.sign_out.types.TegundGreidslu valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid TegundGreidslu";
            throw new IllegalArgumentException(err);
        }
        return (TegundGreidslu) obj;
    } //-- is.idega.block.finance.business.li.sign_out.types.TegundGreidslu valueOf(java.lang.String) 

}
