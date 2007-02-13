/**
 * NetbokhaldServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.server;

import is.idega.idegaweb.member.isi.block.accounting.data.DiscountEntry;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean;
import is.idega.idegaweb.member.isi.block.accounting.netbokhald.business.NetbokhaldBusiness;
import is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldAccountingKeys;
import is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldAccountingKeysBMPBean;

import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;

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
		Map accountingKeys = bus1.getAccountingKeys(in0);
		Collection col2 = bus1.getDiscountEntries(in0, in1.getTime());

		return returnEntriesFromCollection(col, col2, accountingKeys);
	}

	public is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.server.NetbokhaldEntry[] getEntries(
			java.lang.String in0, java.lang.String in1)
			throws java.rmi.RemoteException {
		NetbokhaldBusiness bus1 = (NetbokhaldBusiness) IBOLookup
				.getServiceInstance(IWMainApplication
						.getDefaultIWApplicationContext(),
						NetbokhaldBusiness.class);
		Collection col = bus1.getFinanceEntries(in0, in1);
		Map accountingKeys = bus1.getAccountingKeys(in0);
		Collection col2 = bus1.getDiscountEntries(in0, in1);

		if (accountingKeys == null) {
			System.out.println("keys is null");
		}

		return returnEntriesFromCollection(col, col2, accountingKeys);
	}

	private is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.server.NetbokhaldEntry[] returnEntriesFromCollection(
			Collection col, Collection col2, Map accountingKeys) {
		try {
			if (col != null) {
				int size = col.size();
				if (col2 != null) {
					size += col2.size();
				}
				NetbokhaldEntry entries[] = new NetbokhaldEntry[size * 2];
				int i = 0;
				Iterator it = col.iterator();
				while (it.hasNext()) {
					FinanceEntry entry = (FinanceEntry) it.next();

					String key = "22100";
					String counterKey = "87180";
					if (entry.getType().equals(
							FinanceEntryBMPBean.TYPE_ASSESSMENT)
							|| entry.getType().equals(
									FinanceEntryBMPBean.TYPE_MANUAL)) {
						Map keys = (Map) accountingKeys
								.get(NetbokhaldAccountingKeysBMPBean.TYPE_ASSESSMENT);
						if (keys != null) {
							NetbokhaldAccountingKeys netbokhaldKey = (NetbokhaldAccountingKeys) keys
									.get(new Integer(entry.getTariffTypeID()));
							if (netbokhaldKey != null) {
								key = netbokhaldKey.getDebetKey();
								counterKey = netbokhaldKey.getCreditKey();
							}
						}
					} else if (entry.getType().equals(
							FinanceEntryBMPBean.TYPE_PAYMENT)) {
						Map keys = (Map) accountingKeys
								.get(NetbokhaldAccountingKeysBMPBean.TYPE_PAYMENT);
						if (keys != null) {
							NetbokhaldAccountingKeys netbokhaldKey = (NetbokhaldAccountingKeys) keys
									.get((Integer) entry.getPaymentType()
											.getPrimaryKey());
							if (netbokhaldKey != null) {
								key = netbokhaldKey.getDebetKey();
								counterKey = netbokhaldKey.getCreditKey();
							}
						}
					} else {
						key = "22100";
						counterKey = "87180";
					}

					if (key == null) {
						key = "22100";
					}

					if (counterKey == null) {
						counterKey = "87180";
					}

					entries[i] = new NetbokhaldEntry();
					entries[i].setAccountingKey(key);
						entries[i].setAmount(-entry.getAmount());
					entries[i].setCustomer(entry.getUser().getPersonalID());
					entries[i].setCustomerNumber(-1);
					GregorianCalendar cal = new GregorianCalendar();
					IWTimestamp stamp = new IWTimestamp(entry.getDateOfEntry());
					cal.setTime(stamp.getDate());
					entries[i].setDateOfEntry(cal);
					if (entry.getInvoiceReceiver() != null) {
						entries[i].setInvoiceReceiver(entry
								.getInvoiceReceiver().getInvoiceReceiver()
								.getPersonalID());
					} else {
						entries[i].setInvoiceReceiver(entry.getUser()
								.getPersonalID());
					}
					entries[i].setIsVAT(false);
					entries[i].setVATAmount(0.0d);
					entries[i].setVATKey("");
					entries[i]
							.setSerialNumber(entry.getPrimaryKey().toString());
					StringBuffer text = new StringBuffer();
					if (entry.getInfo() != null) {
						if (entry.getInfo().length() > 40) {
							text.append(entry.getInfo().substring(0, 40));
						} else {
							text.append(entry.getInfo());
						}
					}
					text.append(entry.getUser().getPersonalID());
					entries[i].setText(text.toString());
					i++;
					entries[i] = new NetbokhaldEntry();
					entries[i].setAccountingKey(counterKey);
					entries[i].setAmount(entry.getAmount());
					entries[i].setCustomer(entry.getUser().getPersonalID());
					entries[i].setCustomerNumber(-1);
					entries[i].setDateOfEntry(cal);
					if (entry.getInvoiceReceiver() != null) {
						entries[i].setInvoiceReceiver(entry
								.getInvoiceReceiver().getInvoiceReceiver()
								.getPersonalID());
					} else {
						entries[i].setInvoiceReceiver(entry.getUser()
								.getPersonalID());
					}
					entries[i].setIsVAT(false);
					entries[i].setText(entry.getInfo());
					entries[i].setVATAmount(0.0d);
					entries[i].setVATKey("");
					entries[i]
							.setSerialNumber(entry.getPrimaryKey().toString());
					i++;
				}

				if (col2 != null) {
					it = col2.iterator();
					while (it.hasNext()) {
						DiscountEntry discEntry = (DiscountEntry) it.next();
						FinanceEntry entry = discEntry.getFinanceEntry();
						
						String key = "22100";
						String counterKey = "87180";
						if (entry.getType().equals(
								FinanceEntryBMPBean.TYPE_ASSESSMENT)
								|| entry.getType().equals(
										FinanceEntryBMPBean.TYPE_MANUAL)) {
							Map keys = (Map) accountingKeys
									.get(NetbokhaldAccountingKeysBMPBean.TYPE_ASSESSMENT);
							if (keys != null) {
								NetbokhaldAccountingKeys netbokhaldKey = (NetbokhaldAccountingKeys) keys
										.get(new Integer(entry
												.getTariffTypeID()));
								if (netbokhaldKey != null) {
									key = netbokhaldKey.getDebetKey();
									counterKey = netbokhaldKey.getCreditKey();
								}
							}
						}  else {
							key = "22100";
							counterKey = "87180";
						}

						if (key == null) {
							key = "22100";
						}

						if (counterKey == null) {
							counterKey = "87180";
						}

						entries[i] = new NetbokhaldEntry();
						entries[i].setAccountingKey(key);
						entries[i].setAmount(entry.getDiscountAmount());
						entries[i].setCustomer(entry.getUser().getPersonalID());
						entries[i].setCustomerNumber(-1);
						GregorianCalendar cal = new GregorianCalendar();
						IWTimestamp stamp = new IWTimestamp(entry
								.getDateOfEntry());
						cal.setTime(stamp.getDate());
						entries[i].setDateOfEntry(cal);
						if (entry.getInvoiceReceiver() != null) {
							entries[i].setInvoiceReceiver(entry
									.getInvoiceReceiver().getInvoiceReceiver()
									.getPersonalID());
						} else {
							entries[i].setInvoiceReceiver(entry.getUser()
									.getPersonalID());
						}
						entries[i].setIsVAT(false);
						entries[i].setVATAmount(0.0d);
						entries[i].setVATKey("");
						entries[i].setSerialNumber(entry.getPrimaryKey()
								.toString());
						StringBuffer text = new StringBuffer();
						if (entry.getInfo() != null) {
							if (entry.getInfo().length() > 40) {
								text.append(entry.getInfo().substring(0, 40));
							} else {
								text.append(entry.getInfo());
							}
						}
						text.append(entry.getUser().getPersonalID());
						entries[i].setText(text.toString());
						i++;
						entries[i] = new NetbokhaldEntry();
						entries[i].setAccountingKey(counterKey);
							entries[i].setAmount(-entry.getDiscountAmount());
						entries[i].setCustomer(entry.getUser().getPersonalID());
						entries[i].setCustomerNumber(-1);
						entries[i].setDateOfEntry(cal);
						if (entry.getInvoiceReceiver() != null) {
							entries[i].setInvoiceReceiver(entry
									.getInvoiceReceiver().getInvoiceReceiver()
									.getPersonalID());
						} else {
							entries[i].setInvoiceReceiver(entry.getUser()
									.getPersonalID());
						}
						entries[i].setIsVAT(false);
						entries[i].setText(entry.getInfo());
						entries[i].setVATAmount(0.0d);
						entries[i].setVATKey("");
						entries[i].setSerialNumber(entry.getPrimaryKey()
								.toString());
						i++;
					}
				}

				return entries;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}