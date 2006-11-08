/**
 * NetbokhaldEntry.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.server;

public class NetbokhaldEntry  implements java.io.Serializable {
    private double VATAmount;

    private java.lang.String VATKey;

    private java.lang.String accountingKey;

    private double amount;

    private java.lang.String customer;

    private int customerNumber;

    private java.util.Calendar dateOfEntry;

    private java.lang.String invoiceReceiver;

    private boolean isVAT;

    private java.lang.String reference;

    private java.lang.String text;

    public NetbokhaldEntry() {
    }

    public NetbokhaldEntry(
           double VATAmount,
           java.lang.String VATKey,
           java.lang.String accountingKey,
           double amount,
           java.lang.String customer,
           int customerNumber,
           java.util.Calendar dateOfEntry,
           java.lang.String invoiceReceiver,
           boolean isVAT,
           java.lang.String reference,
           java.lang.String text) {
           this.VATAmount = VATAmount;
           this.VATKey = VATKey;
           this.accountingKey = accountingKey;
           this.amount = amount;
           this.customer = customer;
           this.customerNumber = customerNumber;
           this.dateOfEntry = dateOfEntry;
           this.invoiceReceiver = invoiceReceiver;
           this.isVAT = isVAT;
           this.reference = reference;
           this.text = text;
    }


    /**
     * Gets the VATAmount value for this NetbokhaldEntry.
     * 
     * @return VATAmount
     */
    public double getVATAmount() {
        return VATAmount;
    }


    /**
     * Sets the VATAmount value for this NetbokhaldEntry.
     * 
     * @param VATAmount
     */
    public void setVATAmount(double VATAmount) {
        this.VATAmount = VATAmount;
    }


    /**
     * Gets the VATKey value for this NetbokhaldEntry.
     * 
     * @return VATKey
     */
    public java.lang.String getVATKey() {
        return VATKey;
    }


    /**
     * Sets the VATKey value for this NetbokhaldEntry.
     * 
     * @param VATKey
     */
    public void setVATKey(java.lang.String VATKey) {
        this.VATKey = VATKey;
    }


    /**
     * Gets the accountingKey value for this NetbokhaldEntry.
     * 
     * @return accountingKey
     */
    public java.lang.String getAccountingKey() {
        return accountingKey;
    }


    /**
     * Sets the accountingKey value for this NetbokhaldEntry.
     * 
     * @param accountingKey
     */
    public void setAccountingKey(java.lang.String accountingKey) {
        this.accountingKey = accountingKey;
    }


    /**
     * Gets the amount value for this NetbokhaldEntry.
     * 
     * @return amount
     */
    public double getAmount() {
        return amount;
    }


    /**
     * Sets the amount value for this NetbokhaldEntry.
     * 
     * @param amount
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }


    /**
     * Gets the customer value for this NetbokhaldEntry.
     * 
     * @return customer
     */
    public java.lang.String getCustomer() {
        return customer;
    }


    /**
     * Sets the customer value for this NetbokhaldEntry.
     * 
     * @param customer
     */
    public void setCustomer(java.lang.String customer) {
        this.customer = customer;
    }


    /**
     * Gets the customerNumber value for this NetbokhaldEntry.
     * 
     * @return customerNumber
     */
    public int getCustomerNumber() {
        return customerNumber;
    }


    /**
     * Sets the customerNumber value for this NetbokhaldEntry.
     * 
     * @param customerNumber
     */
    public void setCustomerNumber(int customerNumber) {
        this.customerNumber = customerNumber;
    }


    /**
     * Gets the dateOfEntry value for this NetbokhaldEntry.
     * 
     * @return dateOfEntry
     */
    public java.util.Calendar getDateOfEntry() {
        return dateOfEntry;
    }


    /**
     * Sets the dateOfEntry value for this NetbokhaldEntry.
     * 
     * @param dateOfEntry
     */
    public void setDateOfEntry(java.util.Calendar dateOfEntry) {
        this.dateOfEntry = dateOfEntry;
    }


    /**
     * Gets the invoiceReceiver value for this NetbokhaldEntry.
     * 
     * @return invoiceReceiver
     */
    public java.lang.String getInvoiceReceiver() {
        return invoiceReceiver;
    }


    /**
     * Sets the invoiceReceiver value for this NetbokhaldEntry.
     * 
     * @param invoiceReceiver
     */
    public void setInvoiceReceiver(java.lang.String invoiceReceiver) {
        this.invoiceReceiver = invoiceReceiver;
    }


    /**
     * Gets the isVAT value for this NetbokhaldEntry.
     * 
     * @return isVAT
     */
    public boolean isIsVAT() {
        return isVAT;
    }


    /**
     * Sets the isVAT value for this NetbokhaldEntry.
     * 
     * @param isVAT
     */
    public void setIsVAT(boolean isVAT) {
        this.isVAT = isVAT;
    }


    /**
     * Gets the reference value for this NetbokhaldEntry.
     * 
     * @return reference
     */
    public java.lang.String getReference() {
        return reference;
    }


    /**
     * Sets the reference value for this NetbokhaldEntry.
     * 
     * @param reference
     */
    public void setReference(java.lang.String reference) {
        this.reference = reference;
    }


    /**
     * Gets the text value for this NetbokhaldEntry.
     * 
     * @return text
     */
    public java.lang.String getText() {
        return text;
    }


    /**
     * Sets the text value for this NetbokhaldEntry.
     * 
     * @param text
     */
    public void setText(java.lang.String text) {
        this.text = text;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof NetbokhaldEntry)) return false;
        NetbokhaldEntry other = (NetbokhaldEntry) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.VATAmount == other.getVATAmount() &&
            ((this.VATKey==null && other.getVATKey()==null) || 
             (this.VATKey!=null &&
              this.VATKey.equals(other.getVATKey()))) &&
            ((this.accountingKey==null && other.getAccountingKey()==null) || 
             (this.accountingKey!=null &&
              this.accountingKey.equals(other.getAccountingKey()))) &&
            this.amount == other.getAmount() &&
            ((this.customer==null && other.getCustomer()==null) || 
             (this.customer!=null &&
              this.customer.equals(other.getCustomer()))) &&
            this.customerNumber == other.getCustomerNumber() &&
            ((this.dateOfEntry==null && other.getDateOfEntry()==null) || 
             (this.dateOfEntry!=null &&
              this.dateOfEntry.equals(other.getDateOfEntry()))) &&
            ((this.invoiceReceiver==null && other.getInvoiceReceiver()==null) || 
             (this.invoiceReceiver!=null &&
              this.invoiceReceiver.equals(other.getInvoiceReceiver()))) &&
            this.isVAT == other.isIsVAT() &&
            ((this.reference==null && other.getReference()==null) || 
             (this.reference!=null &&
              this.reference.equals(other.getReference()))) &&
            ((this.text==null && other.getText()==null) || 
             (this.text!=null &&
              this.text.equals(other.getText())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        _hashCode += new Double(getVATAmount()).hashCode();
        if (getVATKey() != null) {
            _hashCode += getVATKey().hashCode();
        }
        if (getAccountingKey() != null) {
            _hashCode += getAccountingKey().hashCode();
        }
        _hashCode += new Double(getAmount()).hashCode();
        if (getCustomer() != null) {
            _hashCode += getCustomer().hashCode();
        }
        _hashCode += getCustomerNumber();
        if (getDateOfEntry() != null) {
            _hashCode += getDateOfEntry().hashCode();
        }
        if (getInvoiceReceiver() != null) {
            _hashCode += getInvoiceReceiver().hashCode();
        }
        _hashCode += (isIsVAT() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getReference() != null) {
            _hashCode += getReference().hashCode();
        }
        if (getText() != null) {
            _hashCode += getText().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(NetbokhaldEntry.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:netbokhald", "NetbokhaldEntry"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("VATAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "VATAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("VATKey");
        elemField.setXmlName(new javax.xml.namespace.QName("", "VATKey"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accountingKey");
        elemField.setXmlName(new javax.xml.namespace.QName("", "accountingKey"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("amount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "amount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("customer");
        elemField.setXmlName(new javax.xml.namespace.QName("", "customer"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("customerNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("", "customerNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateOfEntry");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dateOfEntry"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("invoiceReceiver");
        elemField.setXmlName(new javax.xml.namespace.QName("", "invoiceReceiver"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isVAT");
        elemField.setXmlName(new javax.xml.namespace.QName("", "isVAT"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reference");
        elemField.setXmlName(new javax.xml.namespace.QName("", "reference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("text");
        elemField.setXmlName(new javax.xml.namespace.QName("", "text"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
