/**
 * AstandKrofu.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package is.idega.block.finance.business.isb.ws;

public class AstandKrofu implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected AstandKrofu(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _OGREIDD = "&Oacute;GREIDD";//"ÓGREIDD";
    public static final java.lang.String _GREIDD = "GREIDD";
    public static final java.lang.String _NIDURFELLD = "NI&ETH;URFELLD";//"NIÐURFELLD";
    public static final java.lang.String _MILLINNHEIMTA = "MILLINNHEIMTA";
    public static final java.lang.String _LOGFRAEDIINNHEIMTA = "L&Ouml;GFR&AElig;&ETH;IINNHEIMTA";//"LÖGFRÆÐIINNHEIMTA";
    public static final java.lang.String _ALLAR_KROFUR = "ALLAR_KROFUR";
    public static final AstandKrofu OGREIDD = new AstandKrofu(_OGREIDD);
    public static final AstandKrofu GREIDD = new AstandKrofu(_GREIDD);
    public static final AstandKrofu NIDURFELLD = new AstandKrofu(_NIDURFELLD);
    public static final AstandKrofu MILLINNHEIMTA = new AstandKrofu(_MILLINNHEIMTA);
    public static final AstandKrofu LOGFRAEDIINNHEIMTA = new AstandKrofu(_LOGFRAEDIINNHEIMTA);
    public static final AstandKrofu ALLAR_KROFUR = new AstandKrofu(_ALLAR_KROFUR);
    public java.lang.String getValue() { return _value_;}
    public static AstandKrofu fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        AstandKrofu enum = (AstandKrofu)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static AstandKrofu fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
