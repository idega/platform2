/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_astand_bokunar_type.java,v 1.1 2005/03/15 11:35:03 birna Exp $
 */

package is.idega.block.finance.business.li.sign_in.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * Class LI_astand_bokunar_type.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:03 $
 */
public class LI_astand_bokunar_type implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The MA_BOKA type
     */
    public static final int MA_BOKA_TYPE = 0;

    /**
     * The instance of the MA_BOKA type
     */
    public static final LI_astand_bokunar_type MA_BOKA = new LI_astand_bokunar_type(MA_BOKA_TYPE, "MA_BOKA");

    /**
     * The ER_AD_BOKA type
     */
    public static final int ER_AD_BOKA_TYPE = 1;

    /**
     * The instance of the ER_AD_BOKA type
     */
    public static final LI_astand_bokunar_type ER_AD_BOKA = new LI_astand_bokunar_type(ER_AD_BOKA_TYPE, "ER_AD_BOKA");

    /**
     * The BOKUD_AN_VILLU type
     */
    public static final int BOKUD_AN_VILLU_TYPE = 2;

    /**
     * The instance of the BOKUD_AN_VILLU type
     */
    public static final LI_astand_bokunar_type BOKUD_AN_VILLU = new LI_astand_bokunar_type(BOKUD_AN_VILLU_TYPE, "BOKUD_AN_VILLU");

    /**
     * The BOKUD_MED_VILLU type
     */
    public static final int BOKUD_MED_VILLU_TYPE = 3;

    /**
     * The instance of the BOKUD_MED_VILLU type
     */
    public static final LI_astand_bokunar_type BOKUD_MED_VILLU = new LI_astand_bokunar_type(BOKUD_MED_VILLU_TYPE, "BOKUD_MED_VILLU");

    /**
     * The NIDURFELLD type
     */
    public static final int NIDURFELLD_TYPE = 4;

    /**
     * The instance of the NIDURFELLD type
     */
    public static final LI_astand_bokunar_type NIDURFELLD = new LI_astand_bokunar_type(NIDURFELLD_TYPE, "NIDURFELLD");

    /**
     * The OTHEKKT_ASTAND type
     */
    public static final int OTHEKKT_ASTAND_TYPE = 5;

    /**
     * The instance of the OTHEKKT_ASTAND type
     */
    public static final LI_astand_bokunar_type OTHEKKT_ASTAND = new LI_astand_bokunar_type(OTHEKKT_ASTAND_TYPE, "OTHEKKT_ASTAND");

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

    private LI_astand_bokunar_type(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- is.idega.block.finance.business.li.sign_in.types.LI_astand_bokunar_type(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate
     * 
     * Returns an enumeration of all possible instances of
     * LI_astand_bokunar_type
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
     * Returns the type of this LI_astand_bokunar_type
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
        members.put("MA_BOKA", MA_BOKA);
        members.put("ER_AD_BOKA", ER_AD_BOKA);
        members.put("BOKUD_AN_VILLU", BOKUD_AN_VILLU);
        members.put("BOKUD_MED_VILLU", BOKUD_MED_VILLU);
        members.put("NIDURFELLD", NIDURFELLD);
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
     * LI_astand_bokunar_type
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
     * Returns a new LI_astand_bokunar_type based on the given
     * String value.
     * 
     * @param string
     * @return LI_astand_bokunar_type
     */
    public static is.idega.block.finance.business.li.sign_in.types.LI_astand_bokunar_type valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid LI_astand_bokunar_type";
            throw new IllegalArgumentException(err);
        }
        return (LI_astand_bokunar_type) obj;
    } //-- is.idega.block.finance.business.li.sign_in.types.LI_astand_bokunar_type valueOf(java.lang.String) 

}
