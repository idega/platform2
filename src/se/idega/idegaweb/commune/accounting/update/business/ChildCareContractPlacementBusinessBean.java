/*
 * Created on 4.11.2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.commune.accounting.update.business;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.accounting.childcare.data.ChildCareApplicationHome;
import se.idega.idegaweb.commune.accounting.childcare.data.ChildCareContract;
import se.idega.idegaweb.commune.accounting.childcare.data.ChildCareContractHome;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.util.IWTimestamp;

/**
 * @author laddi
 */
public class ChildCareContractPlacementBusinessBean extends IBOServiceBean implements ChildCareContractPlacementBusiness {

	public void updateMissingPlacements() {
		System.out.println("[ContractSchoolPlacementUpdate]: Beginning updating applications...");
		System.out.println("-------------------------------------------------------------------");
		try {
			Collection applications = getChildCareApplicationHome().findApplicationsWithoutPlacing();
			int size = applications.size();
			int counter = 1;
			
			Iterator iter = applications.iterator();
			while (iter.hasNext()) {
				ChildCareApplication application = (ChildCareApplication) iter.next();
				System.out.println("[ContractSchoolPlacementUpdate]: Updating application " + counter++ + " of " + size + " (ID=" + application.getPrimaryKey() + ")");
				
				IWTimestamp fromDate = new IWTimestamp(application.getFromDate());
				Timestamp endDate = null;
				if (application.getRejectionDate() != null)
					endDate = new IWTimestamp(application.getRejectionDate()).getTimestamp();
				
				try {
					Collection contracts = getChildCareContractHome().findByApplication(((Integer)application.getPrimaryKey()).intValue());
					SchoolClass group = null;
					try {
						group = getSchoolBusiness().getSchoolClassHome().findOneBySchool(application.getProviderId());
					}
					catch (FinderException fe) {
						System.out.println("Found no group for provider = " + application.getProviderId());
						group = null;
					}
					
					if (group != null) {
						SchoolClassMember member = getSchoolBusiness().storeSchoolClassMember(application.getChildId(), ((Integer) group.getPrimaryKey()).intValue(), -1, group.getSchoolTypeId(), fromDate.getTimestamp(), endDate, -1, null);
						Iterator iterator = contracts.iterator();
						while (iterator.hasNext()) {
							ChildCareContract contract = (ChildCareContract) iterator.next();
							contract.setSchoolClassMember(member);
							contract.store();
						}
					}
				}
				catch (FinderException fe) {
					fe.printStackTrace();
				}
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			System.out.println("[ContractSchoolPlacementUpdate]: No applications found with missing placements.");
		}
		System.out.println("-------------------------------------------------------------------");
		System.out.println("[ContractSchoolPlacementUpdate]: Done updating applications........");
	}
	
	public SchoolBusiness getSchoolBusiness() {
		try {
			return (SchoolBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}
	
	public ChildCareApplicationHome getChildCareApplicationHome() {
		try {
			return (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
		}
		catch (IDOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	public ChildCareContractHome getChildCareContractHome() {
		try {
			return (ChildCareContractHome) IDOLookup.getHome(ChildCareContract.class);
		}
		catch (IDOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
}
