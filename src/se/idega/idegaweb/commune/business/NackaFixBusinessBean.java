/*
 * $Id: NackaFixBusinessBean.java,v 1.2 2004/12/07 21:21:54 laddi Exp $
 * Created on 7.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.business;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.care.data.ChildCareContract;
import se.idega.idegaweb.commune.care.data.ChildCareContractHome;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolSeason;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.util.IWTimestamp;


/**
 * Last modified: $Date: 2004/12/07 21:21:54 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public class NackaFixBusinessBean extends IBOServiceBean  implements NackaFixBusiness{

	private SchoolBusiness getSchoolBusiness() {
		try {
			return (SchoolBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
	
	private SchoolClassMemberHome getSchoolClassMemberHome() {
		try {
			return (SchoolClassMemberHome) IDOLookup.getHome(SchoolClassMember.class);
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
	
	private ChildCareContractHome getChildCareContractHome() {
		try {
			return (ChildCareContractHome) IDOLookup.getHome(ChildCareContract.class);
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
	
	public void fixElementarySchoolPlacements() {
		try {
			Collection placements = getSchoolClassMemberHome().findAllByCategory(getSchoolBusiness().getCategoryElementarySchool());
			int size = placements.size();
			System.out.println("[FIX]: Updating removed date for "+size+" elementary school placements");
			
			int userID = -1;
			SchoolSeason previousSeason = null;
			SchoolClassMember previousPlacement = null;
			Iterator iter = placements.iterator();
			int row = 1;
			while (iter.hasNext()) {
				if (row % 100 == 0) {
					System.out.println("[FIX]: Updating entry number " + row + " of " + size);
				}
				SchoolClassMember element = (SchoolClassMember) iter.next();
				SchoolSeason season = element.getSchoolClass().getSchoolSeason();
				
				if (userID == element.getClassMemberId()) {
					if (previousPlacement != null && previousSeason != null && season.getPrimaryKey().equals(previousSeason.getPrimaryKey())) {
						IWTimestamp stamp = new IWTimestamp(element.getRegisterDate());
						stamp.addDays(-1);
						previousPlacement.setRemovedDate(stamp.getTimestamp());
						previousPlacement.store();
					}
				}
				
				IWTimestamp stamp = new IWTimestamp(season.getSchoolSeasonEnd());
				element.setRemovedDate(stamp.getTimestamp());
				element.store();
				
				userID = element.getClassMemberId();
				previousSeason = season;
				previousPlacement = element;
				row++;
			}
			System.out.println("[FIX]: Update done");
			System.out.println("-------------------------------------------------------");
		}
		catch (FinderException fe) {
			log(fe);
		}
		catch (RemoteException re) {
			log(re);
		}
	}
	
	public void fixChildCarePlacements() {
		try {
			Collection placements = getSchoolClassMemberHome().findAllByCategory(getSchoolBusiness().getCategoryChildcare());
			int size = placements.size();
			System.out.println("[FIX]: Updating removed date for "+size+" child care/after school care placements");
			
			Iterator iter = placements.iterator();
			int row = 1;
			while (iter.hasNext()) {
				if (row % 100 == 0) {
					System.out.println("[FIX]: Updating entry number " + row + " of " + size);
				}
				SchoolClassMember element = (SchoolClassMember) iter.next();
				Collection contracts = getChildCareContractHome().findAllBySchoolClassMember(element);
				if (contracts != null) {
					Iterator iterator = contracts.iterator();
					while (iterator.hasNext()) {
						ChildCareContract contract = (ChildCareContract) iterator.next();
						if (contract.getTerminatedDate() != null) {
							IWTimestamp stamp = new IWTimestamp(contract.getTerminatedDate());
							element.setRemovedDate(stamp.getTimestamp());
						}
						else {
							element.setRemovedDate(null);
						}
					}
					element.store();
				}
				row++;
			}
			System.out.println("[FIX]: Update done");
			System.out.println("-------------------------------------------------------");
		}
		catch (FinderException fe) {
			log(fe);
		}
		catch (RemoteException re) {
			log(re);
		}
	}
}