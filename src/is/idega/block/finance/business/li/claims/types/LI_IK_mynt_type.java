/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_IK_mynt_type.java,v 1.1 2005/03/15 11:35:02 birna Exp $
 */

package is.idega.block.finance.business.li.claims.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * Class LI_IK_mynt_type.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:02 $
 */
public class LI_IK_mynt_type implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The CAD type
     */
    public static final int CAD_TYPE = 0;

    /**
     * The instance of the CAD type
     */
    public static final LI_IK_mynt_type CAD = new LI_IK_mynt_type(CAD_TYPE, "CAD");

    /**
     * The CHF type
     */
    public static final int CHF_TYPE = 1;

    /**
     * The instance of the CHF type
     */
    public static final LI_IK_mynt_type CHF = new LI_IK_mynt_type(CHF_TYPE, "CHF");

    /**
     * The DKK type
     */
    public static final int DKK_TYPE = 2;

    /**
     * The instance of the DKK type
     */
    public static final LI_IK_mynt_type DKK = new LI_IK_mynt_type(DKK_TYPE, "DKK");

    /**
     * The NOK type
     */
    public static final int NOK_TYPE = 3;

    /**
     * The instance of the NOK type
     */
    public static final LI_IK_mynt_type NOK = new LI_IK_mynt_type(NOK_TYPE, "NOK");

    /**
     * The SEK type
     */
    public static final int SEK_TYPE = 4;

    /**
     * The instance of the SEK type
     */
    public static final LI_IK_mynt_type SEK = new LI_IK_mynt_type(SEK_TYPE, "SEK");

    /**
     * The EUR type
     */
    public static final int EUR_TYPE = 5;

    /**
     * The instance of the EUR type
     */
    public static final LI_IK_mynt_type EUR = new LI_IK_mynt_type(EUR_TYPE, "EUR");

    /**
     * The GBP type
     */
    public static final int GBP_TYPE = 6;

    /**
     * The instance of the GBP type
     */
    public static final LI_IK_mynt_type GBP = new LI_IK_mynt_type(GBP_TYPE, "GBP");

    /**
     * The ISK type
     */
    public static final int ISK_TYPE = 7;

    /**
     * The instance of the ISK type
     */
    public static final LI_IK_mynt_type ISK = new LI_IK_mynt_type(ISK_TYPE, "ISK");

    /**
     * The JPY type
     */
    public static final int JPY_TYPE = 8;

    /**
     * The instance of the JPY type
     */
    public static final LI_IK_mynt_type JPY = new LI_IK_mynt_type(JPY_TYPE, "JPY");

    /**
     * The USD type
     */
    public static final int USD_TYPE = 9;

    /**
     * The instance of the USD type
     */
    public static final LI_IK_mynt_type USD = new LI_IK_mynt_type(USD_TYPE, "USD");

    /**
     * The XDR type
     */
    public static final int XDR_TYPE = 10;

    /**
     * The instance of the XDR type
     */
    public static final LI_IK_mynt_type XDR = new LI_IK_mynt_type(XDR_TYPE, "XDR");

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

    private LI_IK_mynt_type(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- is.idega.block.finance.business.li.claims.types.LI_IK_mynt_type(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate
     * 
     * Returns an enumeration of all possible instances of
     * LI_IK_mynt_type
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
     * Returns the type of this LI_IK_mynt_type
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
        members.put("CAD", CAD);
        members.put("CHF", CHF);
        members.put("DKK", DKK);
        members.put("NOK", NOK);
        members.put("SEK", SEK);
        members.put("EUR", EUR);
        members.put("GBP", GBP);
        members.put("ISK", ISK);
        members.put("JPY", JPY);
        members.put("USD", USD);
        members.put("XDR", XDR);
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
     * Returns the String representation of this LI_IK_mynt_type
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
     * Returns a new LI_IK_mynt_type based on the given String
     * value.
     * 
     * @param string
     * @return LI_IK_mynt_type
     */
    public static is.idega.block.finance.business.li.claims.types.LI_IK_mynt_type valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid LI_IK_mynt_type";
            throw new IllegalArgumentException(err);
        }
        return (LI_IK_mynt_type) obj;
    } //-- is.idega.block.finance.business.li.claims.types.LI_IK_mynt_type valueOf(java.lang.String) 

}
