package se.idega.idegaweb.commune.accounting.regulations.data;

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
	private int ruleSpecType;
	
	public PostingDetail(){
	}
	
	public PostingDetail(String t, float a, float v, int vID, int rst){
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

	public int getRuleSpecType() {
		return ruleSpecType;
	}

	public void setRuleSpecType(int i) {
		ruleSpecType = i;
	}

}
