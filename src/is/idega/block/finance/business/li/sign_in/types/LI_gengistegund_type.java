/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_gengistegund_type.java,v 1.1 2005/03/15 11:35:03 birna Exp $
 */

package is.idega.block.finance.business.li.sign_in.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * Class LI_gengistegund_type.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:03 $
 */
public class LI_gengistegund_type implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The A type
     */
    public static final int A_TYPE = 0;

    /**
     * The instance of the A type
     */
    public static final LI_gengistegund_type A = new LI_gengistegund_type(A_TYPE, "A");

    /**
     * The T type
     */
    public static final int T_TYPE = 1;

    /**
     * The instance of the T type
     */
    public static final LI_gengistegund_type T = new LI_gengistegund_type(T_TYPE, "T");

    /**
     * The S type
     */
    public static final int S_TYPE = 2;

    /**
     * The instance of the S type
     */
    public static final LI_gengistegund_type S = new LI_gengistegund_type(S_TYPE, "S");

    /**
     * The Z type
     */
    public static final int Z_TYPE = 3;

    /**
     * The instance of the Z type
     */
    public static final LI_gengistegund_type Z = new LI_gengistegund_type(Z_TYPE, "Z");

    /**
     * The F type
     */
    public static final int F_TYPE = 4;

    /**
     * The instance of the F type
     */
    public static final LI_gengistegund_type F = new LI_gengistegund_type(F_TYPE, "F");

    /**
     * The L type
     */
    public static final int L_TYPE = 5;

    /**
     * The instance of the L type
     */
    public static final LI_gengistegund_type L = new LI_gengistegund_type(L_TYPE, "L");

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

    private LI_gengistegund_type(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- is.idega.block.finance.business.li.sign_in.types.LI_gengistegund_type(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate
     * 
     * Returns an enumeration of all possible instances of
     * LI_gengistegund_type
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
     * Returns the type of this LI_gengistegund_type
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
        members.put("A", A);
        members.put("T", T);
        members.put("S", S);
        members.put("Z", Z);
        members.put("F", F);
        members.put("L", L);
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
     * LI_gengistegund_type
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
     * Returns a new LI_gengistegund_type based on the given String
     * value.
     * 
     * @param string
     * @return LI_gengistegund_type
     */
    public static is.idega.block.finance.business.li.sign_in.types.LI_gengistegund_type valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid LI_gengistegund_type";
            throw new IllegalArgumentException(err);
        }
        return (LI_gengistegund_type) obj;
    } //-- is.idega.block.finance.business.li.sign_in.types.LI_gengistegund_type valueOf(java.lang.String) 

}
