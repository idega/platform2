package se.idega.idegaweb.commune.accounting.regulations.business;

import java.util.ArrayList;

/**
 * @author Joakim
 */
public class PaymentFlowConstant {
	public static final String IN = "cacc_payment_flow_type.in";
	public static final String OUT = "cacc_payment_flow_type.out";
	
	public ArrayList getAllPaymentFlow(){
		ArrayList ret = new ArrayList();
		ret.add(IN);
		ret.add(OUT);
		return ret;
	}
}
