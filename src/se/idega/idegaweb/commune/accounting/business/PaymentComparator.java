package se.idega.idegaweb.commune.accounting.business;

import java.text.Collator;
import java.util.Comparator;

import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;

import com.idega.util.LocaleUtil;

/**
 * A class to compare a collection of PaymentRecords or PaymentHeaders objects.
 * @author Sigtryggur
 */
public class PaymentComparator implements Comparator {

	private Collator collator;
	private String compareString1;
	private String compareString2;
	/** 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	
	public int compare(Object o1, Object o2) {		
		collator = Collator.getInstance(LocaleUtil.getSwedishLocale());

		if (o1 instanceof PaymentHeader) {
			compareString1 = ((PaymentHeader) o1).getSchool().getName();
			compareString2 = ((PaymentHeader) o2).getSchool().getName();
		}
		else if (o1 instanceof PaymentRecord) {
			compareString1 = String.valueOf(((PaymentRecord) o1).getOrderId());
			compareString2 = String.valueOf(((PaymentRecord) o2).getOrderId());
		}
		return collator.compare(compareString1, compareString2);
	}
}
