/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega software. Use is
 * subject to license terms.
 *  
 */
package is.idega.idegaweb.member.isi.block.accounting.business;

import is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRound;
import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariff;
import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffHome;
import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffType;
import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffTypeHome;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryHome;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author palli
 */
public class AssessmentRoundThread extends Thread {
	private AssessmentRound round = null;
	private IWApplicationContext iwac = null;
	private List tariffList = null;
	
	public AssessmentRoundThread(AssessmentRound assessmentRound, IWApplicationContext iwac, List tariffs) {
		round = assessmentRound;
		this.iwac = iwac;
		if (tariffs != null) {
			tariffList = new ArrayList();
			tariffList.addAll(tariffs);
		}
	}

	public void run() {
		Group top = round.getGroup();
		if (tariffList != null && !tariffList.isEmpty()) {
			Iterator it = tariffList.iterator();
			while (it.hasNext()) {
				String id = (String) it.next();
				try {
					ClubTariffType tariffType = ((ClubTariffTypeHome) IDOLookup.getHome(ClubTariffType.class)).findByPrimaryKey(new Integer(id));
			
					round.addTariffType(tariffType);
			
					assessGroup(top, round.getIncludeChildren(), tariffType);
				}
				catch (IDOLookupException e) {
					e.printStackTrace();
				}
				catch (NumberFormatException e) {
					e.printStackTrace();
				}
				catch (FinderException e) {
					e.printStackTrace();
				}
				catch (IDOAddRelationshipException e) {
					e.printStackTrace();
				}
			}
		}
		
		IWTimestamp now = IWTimestamp.RightNow();
		round.setEndTime(now.getTimestamp());
		round.store();
	}

	private void assessGroup(Group group, boolean includeChildren, ClubTariffType tariffType) {
		try {
			IWTimestamp runOnDate = null;
			if (round.getRunOnDate() != null) {
				runOnDate = new IWTimestamp(round.getRunOnDate());
			}
			Collection tariffs = ((ClubTariffHome) IDOLookup.getHome(ClubTariff.class)).findByGroupAndTariffType(group, tariffType, runOnDate);
			if (tariffs != null && !tariffs.isEmpty()) {
				Collection users = getUserBusiness().getUsersInGroup(group);
				if (users != null && !users.isEmpty()) {
					Iterator tariffIt = tariffs.iterator();
					while (tariffIt.hasNext()) {
						ClubTariff tariff = (ClubTariff) tariffIt.next();
						
						Iterator userIt = users.iterator();
						while (userIt.hasNext()) {
							User user = (User) userIt.next();
								
							FinanceEntry entry = getFinanceEntryHome().create();
							entry.setUser(user);
							entry.setAssessment(round);
							entry.setClub(round.getClub());
							Group division = round.getDivision();
							if (division == null) {
								division = getAccountingBusiness().findDivisionForGroup(group);
							}
							entry.setDivision(division);
							entry.setGroup(group);
							entry.setAmount(tariff.getAmount());
							entry.setDateOfEntry(IWTimestamp.getTimestampRightNow());
							entry.setInfo(tariff.getText());
							entry.setTariffID(((Integer)tariff.getPrimaryKey()).intValue());
							entry.setTariffTypeID(tariff.getTariffTypeId());
							entry.setStatusCreated();
							entry.setTypeAssessment();
							entry.setEntryOpen(true);
							entry.setInsertedByUser(round.getExecutedBy());
							entry.store();
						}
					}
				}
			}
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (CreateException e) {
			e.printStackTrace();
		}
		
		if (includeChildren) {
			Iterator it = group.getChildrenIterator();
			if (it != null) {
				while (it.hasNext()) {
					Group child = (Group) it.next();
					assessGroup(child, includeChildren, tariffType);
				}
			}
		}	
	}
	
	private UserBusiness getUserBusiness() {
		try {
			return (UserBusiness) IBOLookup.getServiceInstance(iwac, UserBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}	
	
	private AccountingBusiness getAccountingBusiness() {
		try {
			return (AccountingBusiness) IBOLookup.getServiceInstance(iwac, AccountingBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}	
	
	private FinanceEntryHome getFinanceEntryHome() {
		try {
			return (FinanceEntryHome) IDOLookup.getHome(FinanceEntry.class);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}

		return null;
	}
}