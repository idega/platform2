package se.idega.idegaweb.commune.accounting.regulations.business;

import java.util.ArrayList;

/**
 * @author Joakim
 */
public class PaymentFlowConstant {
	public static final String IN = "flow.in";
	public static final String OUT = "flow.out";
	
	public ArrayList getAllPaymentFlow(){
		ArrayList ret = new ArrayList();
		ret.add(IN);
		ret.add(OUT);
		return ret;
	}
}
