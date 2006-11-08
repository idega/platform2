/**
 * NetbokhaldServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.server;

import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean;
import is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.business.NetbokhaldBusiness;

import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWMainApplication;
import com.idega.util.IWTimestamp;

public class NetbokhaldServiceSoapBindingImpl
		implements
		is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.server.NetbokhaldService {
	public is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.server.NetbokhaldEntry[] getEntries(
			java.lang.String in0, java.util.Calendar in1)
			throws java.rmi.RemoteException {
		NetbokhaldBusiness bus1 = (NetbokhaldBusiness) IBOLookup
				.getServiceInstance(IWMainApplication
						.getDefaultIWApplicationContext(),
						NetbokhaldBusiness.class);
		Collection col = bus1.getFinanceEntries(in0, in1.getTime());
		
		if (col != null) {
			NetbokhaldEntry entries[] = new NetbokhaldEntry[col.size()];
			int i = 0;
			Iterator it = col.iterator();
			while (it.hasNext()) {
				FinanceEntry entry = (FinanceEntry) it.next();
				entries[i] = new NetbokhaldEntry();
				entries[i].setAccountingKey("1234");
				if (entry.getType().equals(FinanceEntryBMPBean.TYPE_PAYMENT)) {
					entries[i].setAmount(-entry.getAmount());
				} else {
					entries[i].setAmount(entry.getAmount());					
				}
				entries[i].setCustomer(entry.getUser().getPersonalID());
				entries[i].setCustomerNumber(-1);
				GregorianCalendar cal = new GregorianCalendar();
				IWTimestamp stamp = new IWTimestamp(entry.getDateOfEntry());
				cal.setTime(stamp.getDate());
				entries[i].setDateOfEntry(cal);
				if (entry.getInvoiceReceiver() != null) {
					entries[i].setInvoiceReceiver(entry.getInvoiceReceiver().getInvoiceReceiver().getPersonalID());
					entries[i].setReference(entry.getInvoiceReceiver().getInvoiceReceiver().getPersonalID());
				}
				entries[i].setIsVAT(false);
				entries[i].setText(entry.getInfo());
				entries[i].setVATAmount(0.0d);
				entries[i].setVATKey("");
				i++;
			}
			
			return entries;
		}
		
		return null;
	}
}
