/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_IK_tegund_afslattar_og_vanskilagjalds_type.java,v 1.1 2005/03/15 11:35:02 birna Exp $
 */

package is.idega.block.finance.business.li.claims.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * Class LI_IK_tegund_afslattar_og_vanskilagjalds_type.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:02 $
 */
public class LI_IK_tegund_afslattar_og_vanskilagjalds_type implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The UPPHÆÐ type
     */
    public static final int VALUE_0_TYPE = 0;

    /**
     * The instance of the UPPHÆÐ type
     */
    public static final LI_IK_tegund_afslattar_og_vanskilagjalds_type VALUE_0 = new LI_IK_tegund_afslattar_og_vanskilagjalds_type(VALUE_0_TYPE, "UPPHÆÐ");

    /**
     * The PRÓSENTA type
     */
    public static final int VALUE_1_TYPE = 1;

    /**
     * The instance of the PRÓSENTA type
     */
    public static final LI_IK_tegund_afslattar_og_vanskilagjalds_type VALUE_1 = new LI_IK_tegund_afslattar_og_vanskilagjalds_type(VALUE_1_TYPE, "PRÓSENTA");

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

    private LI_IK_tegund_afslattar_og_vanskilagjalds_type(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- is.idega.block.finance.business.li.claims.types.LI_IK_tegund_afslattar_og_vanskilagjalds_type(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate
     * 
     * Returns an enumeration of all possible instances of
     * LI_IK_tegund_afslattar_og_vanskilagjalds_type
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
     * Returns the type of this
     * LI_IK_tegund_afslattar_og_vanskilagjalds_type
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
        members.put("UPPHÆÐ", VALUE_0);
        members.put("PRÓSENTA", VALUE_1);
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
     * LI_IK_tegund_afslattar_og_vanskilagjalds_type
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
     * Returns a new LI_IK_tegund_afslattar_og_vanskilagjalds_type
     * based on the given String value.
     * 
     * @param string
     * @return LI_IK_tegund_afslattar_og_vanskilagjalds_type
     */
    public static is.idega.block.finance.business.li.claims.types.LI_IK_tegund_afslattar_og_vanskilagjalds_type valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid LI_IK_tegund_afslattar_og_vanskilagjalds_type";
            throw new IllegalArgumentException(err);
        }
        return (LI_IK_tegund_afslattar_og_vanskilagjalds_type) obj;
    } //-- is.idega.block.finance.business.li.claims.types.LI_IK_tegund_afslattar_og_vanskilagjalds_type valueOf(java.lang.String) 

}
