package se.idega.idegaweb.commune.accounting.regulations.data;

/**
 * @author Joakim
 * 
 * Object to be returned by the Regulation as a responce to a query for a specific 
 * row/instance in the regulation framework
 */
public class PostingDetail {
	private String term;
	private float amount;
	private float vat;
	private int vatRegulationID;
	
	public PostingDetail(){
	}
	
	public PostingDetail(String t, float a, float v, int vID){
		term = t;
		amount = a;
		vat = v;
		vatRegulationID = vID;
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

	public float getVatRegulationID() {
		return vatRegulationID;
	}

	public void setVatRegulationID(int i) {
		vatRegulationID = i;
	}

}
