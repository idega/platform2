package se.idega.idegaweb.commune.accounting.regulations.data;

import se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry;

/**
 * Object to be returned by the Regulation as a responce to a query for a specific 
 * row/instance in the regulation framework
 * 
 * @author Joakim
 */
public class PostingDetail {
	private String term;
	private float amount;
	private float vat;
	private int vatRegulationID;
	private String ruleSpecType;
	
	public PostingDetail(){
	}

	//TODO (JJ) Have to change the regularInvoiceEntry to regularPaymentEntrry
	public PostingDetail(RegularInvoiceEntry regularPaymentEntry){
		setAmount(regularPaymentEntry.getAmount());
		setRuleSpecType(regularPaymentEntry.getRegSpecType().getRegSpecType());
		setTerm(regularPaymentEntry.getPlacing());
		setVat(regularPaymentEntry.getVAT());
		setVatRegulationID(regularPaymentEntry.getVatRegulationID());
	}
	
	public PostingDetail(String t, float a, float v, int vID, String rst){
		term = t;
		amount = a;
		vat = v;
		vatRegulationID = vID;
		ruleSpecType = rst;
	}
	
	public float getAmount() {
		return amount;
	}

	public String getTerm() {
		return term;
	}

	public void setAmount(float i) {
		amount = i;
	}

	public void setTerm(String string) {
		term = string;
	}

	public float getVat() {
		return vat;
	}

	public void setVat(float f) {
		vat = f;
	}

	public int getVatRegulationID() {
		return vatRegulationID;
	}

	public void setVatRegulationID(int i) {
		vatRegulationID = i;
	}

	public String getRuleSpecType() {
		return ruleSpecType;
	}

	public void setRuleSpecType(String i) {
		ruleSpecType = i;
	}

}
