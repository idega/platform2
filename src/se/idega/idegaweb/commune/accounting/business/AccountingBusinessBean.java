/*
 * Created on 9.9.2003
 */
package se.idega.idegaweb.commune.accounting.business;

import java.rmi.RemoteException;

import se.idega.idegaweb.commune.accounting.export.business.ExportBusiness;

import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;

/**
 * @author laddi
 */
public class AccountingBusinessBean extends IBOServiceBean {

	public ExportBusiness getExportBusiness() {
		try {
			return (ExportBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), ExportBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}
}