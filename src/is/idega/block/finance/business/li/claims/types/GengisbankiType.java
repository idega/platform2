/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: GengisbankiType.java,v 1.1 2005/03/15 11:35:02 birna Exp $
 */

package is.idega.block.finance.business.li.claims.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * Class GengisbankiType.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:02 $
 */
public class GengisbankiType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The SEÐLABANKINN type
     */
    public static final int VALUE_0_TYPE = 0;

    /**
     * The instance of the SEÐLABANKINN type
     */
    public static final GengisbankiType VALUE_0 = new GengisbankiType(VALUE_0_TYPE, "SEÐLABANKINN");

    /**
     * The LANDSBANKINN type
     */
    public static final int VALUE_1_TYPE = 1;

    /**
     * The instance of the LANDSBANKINN type
     */
    public static final GengisbankiType VALUE_1 = new GengisbankiType(VALUE_1_TYPE, "LANDSBANKINN");

    /**
     * The BÚNAÐARBANKINN type
     */
    public static final int VALUE_2_TYPE = 2;

    /**
     * The instance of the BÚNAÐARBANKINN type
     */
    public static final GengisbankiType VALUE_2 = new GengisbankiType(VALUE_2_TYPE, "BÚNAÐARBANKINN");

    /**
     * The ÍSLANDSBANKI type
     */
    public static final int VALUE_3_TYPE = 3;

    /**
     * The instance of the ÍSLANDSBANKI type
     */
    public static final GengisbankiType VALUE_3 = new GengisbankiType(VALUE_3_TYPE, "ÍSLANDSBANKI");

    /**
     * The SPARISJÓÐIR type
     */
    public static final int VALUE_4_TYPE = 4;

    /**
     * The instance of the SPARISJÓÐIR type
     */
    public static final GengisbankiType VALUE_4 = new GengisbankiType(VALUE_4_TYPE, "SPARISJÓÐIR");

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

    private GengisbankiType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- is.idega.block.finance.business.li.claims.types.GengisbankiType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate
     * 
     * Returns an enumeration of all possible instances of
     * GengisbankiType
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
     * Returns the type of this GengisbankiType
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
        members.put("SEÐLABANKINN", VALUE_0);
        members.put("LANDSBANKINN", VALUE_1);
        members.put("BÚNAÐARBANKINN", VALUE_2);
        members.put("ÍSLANDSBANKI", VALUE_3);
        members.put("SPARISJÓÐIR", VALUE_4);
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
     * Returns the String representation of this GengisbankiType
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
     * Returns a new GengisbankiType based on the given String
     * value.
     * 
     * @param string
     * @return GengisbankiType
     */
    public static is.idega.block.finance.business.li.claims.types.GengisbankiType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid GengisbankiType";
            throw new IllegalArgumentException(err);
        }
        return (GengisbankiType) obj;
    } //-- is.idega.block.finance.business.li.claims.types.GengisbankiType valueOf(java.lang.String) 

}
