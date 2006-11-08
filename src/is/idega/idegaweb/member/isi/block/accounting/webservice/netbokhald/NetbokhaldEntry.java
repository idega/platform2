package is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald;

import java.util.Date;

public class NetbokhaldEntry {
	private String customer = null;
	
	private String invoiceReceiver = null;
	
	private Date dateOfEntry = null;
	
	private String text = "";
	
	private String reference = "";
	
	private String accountingKey = null;
	
	private String vatKey = null;
	
	private double amount = 0.0d;
	
	private double vatAmount = 0.0d;
	
	private boolean isVat = false;
	
	private int customerNumber = -1;
	
	public NetbokhaldEntry() {
		
	}
	
	public void setCustomer(String ssn) {
		customer = ssn;
	}
	
	public String getCustomer() {
		return customer;
	}
	
	public void setInvoiceReceiver(String invoiceReceiver) {
		this.invoiceReceiver = invoiceReceiver;
	}
	
	public String getInvoiceReceiver() {
		return invoiceReceiver;
	}
	
	public void setDateOfEntry(Date dateOfEntry) {
		this.dateOfEntry = dateOfEntry;
	}
	
	public Date getDateOfEntry() {
		return dateOfEntry;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public void setReference(String reference) {
		this.reference = reference;
	}
	
	public String getReference() {
		return reference;
	}
	
	public void setAccountingKey(String accountingKey) {
		this.accountingKey = accountingKey;
	}
	
	public String getAccountingKey() {
		return accountingKey;
	}
	
	public void setVATKey(String key) {
		this.vatKey = key;
	}
	
	public String getVATKey() {
		return vatKey;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public void setVATAmount(double amount) {
		this.vatAmount = amount;
	}
	
	public double getVATAmount() {
		return vatAmount;
	}
	
	public void setIsVAT(boolean isVAT) {
		this.isVat = isVAT;
	}
	
	public boolean getIsVAT() {
		return isVat;
	}
	
	public void setCustomerNumber(int customerNumber) {
		this.customerNumber = customerNumber;
	}
	
	public int getCustomerNumber() {
		return customerNumber; 
	}
}