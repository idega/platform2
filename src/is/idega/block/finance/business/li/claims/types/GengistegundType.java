/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: GengistegundType.java,v 1.1 2005/03/15 11:35:02 birna Exp $
 */

package is.idega.block.finance.business.li.claims.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * Class GengistegundType.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:02 $
 */
public class GengistegundType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The ALMENNT_GENGI_VIÐSKIPTABANKA type
     */
    public static final int VALUE_0_TYPE = 0;

    /**
     * The instance of the ALMENNT_GENGI_VIÐSKIPTABANKA type
     */
    public static final GengistegundType VALUE_0 = new GengistegundType(VALUE_0_TYPE, "ALMENNT_GENGI_VIÐSKIPTABANKA");

    /**
     * The MIÐGENGI_SEÐLABANKA type
     */
    public static final int VALUE_1_TYPE = 1;

    /**
     * The instance of the MIÐGENGI_SEÐLABANKA type
     */
    public static final GengistegundType VALUE_1 = new GengistegundType(VALUE_1_TYPE, "MIÐGENGI_SEÐLABANKA");

    /**
     * The FUNDARGENGI_SEÐLABANKA type
     */
    public static final int VALUE_2_TYPE = 2;

    /**
     * The instance of the FUNDARGENGI_SEÐLABANKA type
     */
    public static final GengistegundType VALUE_2 = new GengistegundType(VALUE_2_TYPE, "FUNDARGENGI_SEÐLABANKA");

    /**
     * The SEÐLAGENGI_VIÐSKIPTABANKA type
     */
    public static final int VALUE_3_TYPE = 3;

    /**
     * The instance of the SEÐLAGENGI_VIÐSKIPTABANKA type
     */
    public static final GengistegundType VALUE_3 = new GengistegundType(VALUE_3_TYPE, "SEÐLAGENGI_VIÐSKIPTABANKA");

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

    private GengistegundType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- is.idega.block.finance.business.li.claims.types.GengistegundType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate
     * 
     * Returns an enumeration of all possible instances of
     * GengistegundType
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
     * Returns the type of this GengistegundType
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
        members.put("ALMENNT_GENGI_VIÐSKIPTABANKA", VALUE_0);
        members.put("MIÐGENGI_SEÐLABANKA", VALUE_1);
        members.put("FUNDARGENGI_SEÐLABANKA", VALUE_2);
        members.put("SEÐLAGENGI_VIÐSKIPTABANKA", VALUE_3);
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
     * Returns the String representation of this GengistegundType
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
     * Returns a new GengistegundType based on the given String
     * value.
     * 
     * @param string
     * @return GengistegundType
     */
    public static is.idega.block.finance.business.li.claims.types.GengistegundType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid GengistegundType";
            throw new IllegalArgumentException(err);
        }
        return (GengistegundType) obj;
    } //-- is.idega.block.finance.business.li.claims.types.GengistegundType valueOf(java.lang.String) 

}
