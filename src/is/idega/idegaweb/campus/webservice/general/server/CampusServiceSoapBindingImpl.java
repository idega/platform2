/**
 * CampusServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.campus.webservice.general.server;

import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.webservice.general.business.CampusServiceBusiness;

import java.util.Collection;
import java.util.Iterator;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWMainApplication;
import com.idega.util.IWTimestamp;

public class CampusServiceSoapBindingImpl implements
		is.idega.idegaweb.campus.webservice.general.server.CampusService {
	public is.idega.idegaweb.campus.webservice.general.server.TenantInfo[] getTenantInfo(
			int in0) throws java.rmi.RemoteException {
		CampusServiceBusiness bus1 = (CampusServiceBusiness) IBOLookup
				.getServiceInstance(IWMainApplication
						.getDefaultIWApplicationContext(),
						CampusServiceBusiness.class);
		Collection col = bus1.getRentedContractsForComplex(in0);

		if (col != null) {
			TenantInfo info[] = new TenantInfo[col.size()];
			int i = 0;
			Iterator it = col.iterator();
			while (it.hasNext()) {
				Contract contract = (Contract) it.next();
				info[i] = new TenantInfo();
				info[i].setPersonalID(contract.getApplicant().getSSN());
				info[i].setBuildingName(contract.getApartment().getFloor().getBuilding().getName());
				info[i].setApartmentNumber(contract.getApartment().getName());
				if (contract.getDeliverTime() != null) {
					info[i].setMovedInDate(new IWTimestamp(contract.getDeliverTime()).getCalendar());
				} else {
					info[i].setMovedInDate(null);					
				}
				i++;
			}

			return info;
		}

		return null;
	}

}
