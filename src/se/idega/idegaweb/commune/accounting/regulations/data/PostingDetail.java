package se.idega.idegaweb.commune.accounting.regulations.data;

import se.idega.idegaweb.commune.accounting.invoice.data.RegularPaymentEntry;

/**
 * Object to be returned by the Regulation as a responce to a query for a
 * specific row/instance in the regulation framework
 * 
 * @author Joakim
 */
public class PostingDetail {
	private String term;

	private float amount;

	private float vat = 0;

	private int vatRuleRegulationID = -1;

	private VATRegulation vatRegulation;

	private String ruleSpecType;

	private int orderID;

	private float vatAmount = 0;

	private Regulation reg = null;

	public PostingDetail() {
	}

	public PostingDetail(RegularPaymentEntry regularPaymentEntry) {
		setAmount(regularPaymentEntry.getAmount());
		if (regularPaymentEntry.getRegSpecType() != null) {
			setRuleSpecType(regularPaymentEntry.getRegSpecType()
					.getRegSpecType());
		}
		setTerm(regularPaymentEntry.getPlacing());
		// setVATPercent(regularPaymentEntry.getVATAmount());
		setVATAmount(regularPaymentEntry.getVATAmount());
		int vatRuleRegulationId = regularPaymentEntry.getVatRuleRegulationId();
		if (vatRuleRegulationId != -1) {
			setVatRuleRegulationId(vatRuleRegulationId);

		}
		setOrderID(999);
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

	public float getVATPercent() {
		return vat;
	}

	public void setVATPercent(float f) {
		vat = f;
	}

	public int getVatRuleRegulationId() {
		return vatRuleRegulationID;
	}

	public void setVatRuleRegulationId(int i) {
		vatRuleRegulationID = i;
	}

	public String getRuleSpecType() {
		return ruleSpecType;
	}

	public void setRuleSpecType(String i) {
		ruleSpecType = i;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int i) {
		orderID = i;
	}

	/**
	 * @return Returns the vatRegulation.
	 */
	public VATRegulation getVATRegulation() {
		return vatRegulation;
	}

	/**
	 * @param vatRegulation
	 *            The vatRegulation to set.
	 */
	public void setVATRegulation(VATRegulation vatRegulation) {
		this.vatRegulation = vatRegulation;
	}

	public float getVATPercentage() {
		return getVATPercent() / 100;
	}

	public float getVATAmount() {
		return vatAmount;
	}

	public void setVATAmount(float VATAmount) {
		vatAmount = VATAmount;
	}

	public void setRegulation(Regulation regulation) {
		this.reg = regulation;
	}

	public Regulation getRegulation() {
		return reg;
	}
}