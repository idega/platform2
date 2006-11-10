/**
 * AccountingServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.member.isi.block.accounting.webservice.general.server;

import is.idega.idegaweb.member.isi.block.accounting.webservice.general.business.GeneralAccountingServiceBusiness;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWMainApplication;

public class AccountingServiceSoapBindingImpl
		implements
		is.idega.idegaweb.member.isi.block.accounting.webservice.general.server.AccountingService {
	public is.idega.idegaweb.member.isi.block.accounting.webservice.general.server.UserInfo getUser(
			java.lang.String in0) throws java.rmi.RemoteException {
		GeneralAccountingServiceBusiness bus1 = (GeneralAccountingServiceBusiness) IBOLookup
				.getServiceInstance(IWMainApplication
						.getDefaultIWApplicationContext(),
						GeneralAccountingServiceBusiness.class);

		return bus1.getUserInfo(in0);
	}
}