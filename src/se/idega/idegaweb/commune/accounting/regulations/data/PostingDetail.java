package se.idega.idegaweb.commune.accounting.regulations.data;

/**
 * @author Joakim
 * 
 * Object to be returned by the Regulation as a responce to a query for a specific 
 * row/instance in the regulation framework
 */
public class PostingDetail {
	private String term;
	private int amount;
	
	public PostingDetail(){
	}
	
	public PostingDetail(String t, int a){
		term = t;
		amount = a;
	}
	
	public int getAmount() {
		return amount;
	}

	public String getTerm() {
		return term;
	}

	public void setAmount(int i) {
		amount = i;
	}

	public void setTerm(String string) {
		term = string;
	}
}
