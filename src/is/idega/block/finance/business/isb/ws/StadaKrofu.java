/**
 * StadaKrofu.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package is.idega.block.finance.business.isb.ws;

public class StadaKrofu implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected StadaKrofu(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _OGREIDD = "ÓGREIDD";
    public static final java.lang.String _GREIDD = "GREIDD";
    public static final java.lang.String _MILLINNHEIMTA = "MILLINNHEIMTA";
    public static final java.lang.String _LOGFRAEDIINNHEIMTA = "LÖGFRÆÐIINNHEIMTA";
    public static final java.lang.String _NIDURFELLD = "NIÐURFELLD";
    public static final java.lang.String _VILLA = "VILLA";
    public static final StadaKrofu OGREIDD = new StadaKrofu(_OGREIDD);
    public static final StadaKrofu GREIDD = new StadaKrofu(_GREIDD);
    public static final StadaKrofu MILLINNHEIMTA = new StadaKrofu(_MILLINNHEIMTA);
    public static final StadaKrofu LOGFRAEDIINNHEIMTA = new StadaKrofu(_LOGFRAEDIINNHEIMTA);
    public static final StadaKrofu NIDURFELLD = new StadaKrofu(_NIDURFELLD);
    public static final StadaKrofu VILLA = new StadaKrofu(_VILLA);
    public java.lang.String getValue() { return _value_;}
    public static StadaKrofu fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        StadaKrofu enum1 = (StadaKrofu)
            _table_.get(value);
        if (enum1==null) throw new java.lang.IllegalStateException();
        return enum1;
    }
    public static StadaKrofu fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
