/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: GengisreglaType.java,v 1.1 2005/03/15 11:35:02 birna Exp $
 */

package is.idega.block.finance.business.li.claims.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * Class GengisreglaType.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:02 $
 */
public class GengisreglaType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The GJALDDAGAGENGI type
     */
    public static final int GJALDDAGAGENGI_TYPE = 0;

    /**
     * The instance of the GJALDDAGAGENGI type
     */
    public static final GengisreglaType GJALDDAGAGENGI = new GengisreglaType(GJALDDAGAGENGI_TYPE, "GJALDDAGAGENGI");

    /**
     * The GREIDSLUDAGSGENGI type
     */
    public static final int GREIDSLUDAGSGENGI_TYPE = 1;

    /**
     * The instance of the GREIDSLUDAGSGENGI type
     */
    public static final GengisreglaType GREIDSLUDAGSGENGI = new GengisreglaType(GREIDSLUDAGSGENGI_TYPE, "GREIDSLUDAGSGENGI");

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

    private GengisreglaType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- is.idega.block.finance.business.li.claims.types.GengisreglaType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate
     * 
     * Returns an enumeration of all possible instances of
     * GengisreglaType
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
     * Returns the type of this GengisreglaType
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
        members.put("GJALDDAGAGENGI", GJALDDAGAGENGI);
        members.put("GREIDSLUDAGSGENGI", GREIDSLUDAGSGENGI);
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
     * Returns the String representation of this GengisreglaType
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
     * Returns a new GengisreglaType based on the given String
     * value.
     * 
     * @param string
     * @return GengisreglaType
     */
    public static is.idega.block.finance.business.li.claims.types.GengisreglaType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid GengisreglaType";
            throw new IllegalArgumentException(err);
        }
        return (GengisreglaType) obj;
    } //-- is.idega.block.finance.business.li.claims.types.GengisreglaType valueOf(java.lang.String) 

}
