package se.idega.idegaweb.commune.accounting.business;

import java.text.Collator;
import java.util.Comparator;

import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord;

import com.idega.util.LocaleUtil;

/**
 * A class to compare a collection of InvoiceRecords or InvoiceHeaders objects.
 * @author Sigtryggur
 */
public class InvoiceComparator implements Comparator {

	private Collator collator;
	private String compareString1;
	private String compareString2;
	/** 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	
	public int compare(Object o1, Object o2) {		
		collator = Collator.getInstance(LocaleUtil.getSwedishLocale());

		if (o1 instanceof InvoiceHeader) {
		compareString1 = ((InvoiceRecord) o1).getProvider().getName();
		compareString2 = ((InvoiceRecord) o2).getProvider().getName();
		}
		else if (o1 instanceof InvoiceRecord){
			compareString1 = ((InvoiceRecord) o1).getInvoiceText();
			compareString2 = ((InvoiceRecord) o2).getInvoiceText();
		}
		return collator.compare(compareString1, compareString2);
	}
}
