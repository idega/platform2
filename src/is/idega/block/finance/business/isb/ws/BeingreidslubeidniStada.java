/**
 * BeingreidslubeidniStada.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package is.idega.block.finance.business.isb.ws;

public class BeingreidslubeidniStada implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected BeingreidslubeidniStada(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _VIRK = "VIRK";
    public static final java.lang.String _OVIRK = "OVIRK";
    public static final BeingreidslubeidniStada VIRK = new BeingreidslubeidniStada(_VIRK);
    public static final BeingreidslubeidniStada OVIRK = new BeingreidslubeidniStada(_OVIRK);
    public java.lang.String getValue() { return _value_;}
    public static BeingreidslubeidniStada fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        BeingreidslubeidniStada enum1 = (BeingreidslubeidniStada)
            _table_.get(value);
        if (enum1==null) throw new java.lang.IllegalStateException();
        return enum1;
    }
    public static BeingreidslubeidniStada fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
