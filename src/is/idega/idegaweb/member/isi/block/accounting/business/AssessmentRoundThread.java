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
import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffType;
import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffTypeHome;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryHome;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

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

	private String tariff = null;

	private String skipList = null;

//	private double amount = 0.0d;

	public AssessmentRoundThread(AssessmentRound assessmentRound, IWApplicationContext iwac, String tariff,
			String skip) {
		round = assessmentRound;
		this.iwac = iwac;
		if (tariff != null) {
			this.tariff = tariff;
		}

/*		this.amount = 0.0d;
		try {
			this.amount = Double.parseDouble(amount);
		}
		catch (Exception e) {
		}*/

		this.skipList = skip;
	}

	public void run() {
		Group top = round.getGroup();
		Collection skip = new ArrayList();
		StringTokenizer tok = new StringTokenizer(skipList, ";");
		while (tok.hasMoreElements()) {
			String str = (String) tok.nextElement();
			skip.add(str);
		}

		try {
			ClubTariffType tariffType = ((ClubTariffTypeHome) IDOLookup.getHome(ClubTariffType.class)).findByPrimaryKey(new Integer(
					tariff));
			round.addTariffType(tariffType);
			assessGroup(top, round.getIncludeChildren(), tariffType, skip);
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

		IWTimestamp now = IWTimestamp.RightNow();
		round.setEndTime(now.getTimestamp());
		round.store();
	}

	private void assessGroup(Group group, boolean includeChildren, ClubTariffType tariffType, Collection skip) {
		if (!skip.contains(group.getGroupType())) {
			try {
				ClubTariff tariff = createTariffForGroup(group, tariffType);
				Collection users = getUserBusiness().getUsersInGroup(group);
				if (users != null && !users.isEmpty()) {
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
						entry.setAmount(round.getAmount());
						entry.setDateOfEntry(IWTimestamp.getTimestampRightNow());
						entry.setInfo(tariff.getText());
						//entry.setInfo("Test keyrsla");
						entry.setTariff(tariff);
						entry.setTariffType(tariffType);
						entry.setStatusCreated();
						entry.setTypeAssessment();
						entry.setEntryOpen(true);
						entry.setInsertedByUser(round.getExecutedBy());
						entry.store();
					}
				}
			}
			catch (IDOLookupException e) {
				e.printStackTrace();
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (CreateException e) {
				e.printStackTrace();
			}
		}

		if (includeChildren) {
			Iterator it = group.getChildrenIterator();
			if (it != null) {
				while (it.hasNext()) {
					Group child = (Group) it.next();
					assessGroup(child, includeChildren, tariffType, skip);
				}
			}
		}
	}

	private ClubTariff createTariffForGroup(Group group, ClubTariffType type) {
		try {
			StringBuffer name = new StringBuffer(type.getName());
			name.append(" - ");
			name.append(round.getName());
			return getAccountingBusiness().insertTariff(round.getClub(), round.getDivision(), group, type, name.toString(), round.getAmount(), round.getPeriodFrom(), round.getPeriodTo());
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
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