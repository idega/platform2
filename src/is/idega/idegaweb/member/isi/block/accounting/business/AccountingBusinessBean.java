/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega software. Use is
 * subject to license terms.
 *  
 */
package is.idega.idegaweb.member.isi.block.accounting.business;

import is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRound;
import is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundHome;
import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariff;
import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffHome;
import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffType;
import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffTypeHome;
import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContract;
import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractHome;
import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardType;
import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardTypeHome;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryHome;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.IWTimestamp;

/**
 * @author palli
 */
public class AccountingBusinessBean extends IBOServiceBean implements AccountingBusiness {
	public boolean doAssessment(String name, Group club, Group division, String groupId, User user, boolean includeChildren, String tariffs[], Timestamp paymentDate, Timestamp runOnDate) {
		Group group = null;
		if (groupId != null) {
			try {
				GroupHome gHome = (GroupHome) IDOLookup.getHome(Group.class);
				group = gHome.findByPrimaryKey(new Integer(groupId));
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
		}

		IWTimestamp now = IWTimestamp.RightNow();
		AssessmentRound round = insertAssessmentRound(name, club, division, group, user, now.getTimestamp(), null, includeChildren, paymentDate, runOnDate);

		Thread assRoundThread = new AssessmentRoundThread(round, getIWApplicationContext(), Arrays.asList(tariffs));
		assRoundThread.start();

		return true;
	}

	public Collection findAllTariffByClub(Group club) {
		try {
			return getClubTariffHome().findAllByClub(club);
		}
		catch (FinderException e) {
		}

		return null;
	}

	public Collection findAllValidTariffByGroup(Group group) {
		try {
			return getClubTariffHome().findAllValidByGroup(group);
		}
		catch (FinderException e) {
		}

		return null;
	}
	
	public Collection findAllValidTariffByGroup(String groupId) {
		Group group = null;
		if (groupId != null) {
			try {
				GroupHome gHome = (GroupHome) IDOLookup.getHome(Group.class);
				group = gHome.findByPrimaryKey(new Integer(groupId));
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
		}
		
		if (group != null) {
			return findAllValidTariffByGroup(group);
		}
		
		return null;
	}
	
	public boolean insertTariff(Group club, String groupId, String typeId, String text, String amount, Date from, Date to, boolean applyToChildren, String skip) {
		Group group = null;
		if (groupId != null) {
			try {
				GroupHome gHome = (GroupHome) IDOLookup.getHome(Group.class);
				group = gHome.findByPrimaryKey(new Integer(groupId));
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
		}

		ClubTariffType type = null;
		if (typeId != null) {
			try {
				type = getClubTariffTypeHome().findByPrimaryKey(new Integer(typeId));
			}
			catch (NumberFormatException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
		}

		float am = 0;
		try {
			am = Float.parseFloat(amount);
		}
		catch (Exception e) {
		}

		return insertTariff(club, group, type, text, am, from, to, applyToChildren, skip, null);
	}

	public boolean insertTariff(Group club, Group group, ClubTariffType type, String text, float amount, Date from, Date to, boolean applyToChildren, String skipList, List skip) {
		if (skip == null || skip.isEmpty()) {
			skip = new ArrayList();
		
			StringTokenizer tok = new StringTokenizer(skipList, ";");
			while (tok.hasMoreElements()) {
				String str = (String) tok.nextElement();
				skip.add(str);
			}
			
			Iterator it = skip.iterator();
			while (it.hasNext()) {
				System.out.println((String)it.next());
			}
		}
		
		ClubTariff eTariff;
		try {
			if (!skip.contains(group.getGroupType())) {
				eTariff = getClubTariffHome().create();
				eTariff.setClub(club);
				eTariff.setGroup(group);
				eTariff.setTariffType(type);
				eTariff.setText(text);
				eTariff.setAmount(amount);
				eTariff.setPeriodFrom(from);
				eTariff.setPeriodTo(to);
					
				eTariff.store();
			}

			if (applyToChildren) {
				Iterator children = group.getChildren();
				if (children != null) {
					while (children.hasNext()) {
						Group child = (Group) children.next();
						boolean ret = insertTariff(club, child, type, text, amount, from, to, applyToChildren, skipList, skip);
						if (!ret)
							return ret;
					}
				}
			}
			
			return true;
		}
		catch (CreateException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	public boolean deleteTariff(String ids[]) {
		try {
			for (int i = 0; i < ids.length; i++) {
				Integer id = new Integer(ids[i]);
				ClubTariff eTariff = getClubTariffHome().findByPrimaryKey(id);
				eTariff.setDeleted(true);
				eTariff.store();
			}

			return true;
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		catch (EJBException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	public Collection findAllTariffTypeByClub(Group club) {
		try {
			return getClubTariffTypeHome().findAllByClub(club);
		}
		catch (FinderException e) {
		}

		return null;
	}

	public boolean insertTariffType(String type, String name, String locKey, Group club) {
		try {
			ClubTariffType eType = getClubTariffTypeHome().create();
			eType.setTariffType(type);
			eType.setClub(club);
			eType.setName(name);
			eType.setLocalizedKey(locKey);

			eType.store();

			return true;
		}
		catch (CreateException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean deleteTariffType(String ids[]) {
		try {
			for (int i = 0; i < ids.length; i++) {
				Integer id = new Integer(ids[i]);
				ClubTariffType eType = getClubTariffTypeHome().findByPrimaryKey(id);
				eType.setDeleted(true);
				eType.store();
			}

			return true;
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		catch (EJBException e) {
			e.printStackTrace();
		}

		return false;
	}

	public Collection findAllCreditCardType() {
		try {
			return getCreditCardTypeHome().findAll();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return null;
	}

	public Collection findAllCreditCardContractByClub(Group club) {
		try {
			return getCreditCardContractHome().findAllByClub(club);
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return null;
	}

	public boolean insertCreditCardContract(Group club, String division, String contractNumber, String type) {
		Group div = null;
		if (division != null || !division.equals("-1")) {
			try {
				GroupHome gHome = (GroupHome) IDOLookup.getHome(Group.class);
				div = gHome.findByPrimaryKey(new Integer(division));
			}
			catch (IDOLookupException e) {
			}
			catch (NumberFormatException e) {
			}
			catch (FinderException e) {
			}
		}

		CreditCardType cType = null;
		if (type != null) {
			try {
				cType = getCreditCardTypeHome().findByPrimaryKey(new Integer(type));
			}
			catch (NumberFormatException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
		}

		return insertCreditCardContract(club, div, contractNumber, cType);
	}

	public boolean insertCreditCardContract(Group club, Group division, String contractNumber, CreditCardType type) {
		try {
			CreditCardContract eCont = getCreditCardContractHome().create();
			eCont.setClub(club);
			eCont.setDivision(division);
			eCont.setContractNumber(contractNumber);
			eCont.setCardType(type);

			eCont.store();

			return true;
		}
		catch (CreateException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean deleteContract(String ids[]) {
		try {
			for (int i = 0; i < ids.length; i++) {
				Integer id = new Integer(ids[i]);
				CreditCardContract eCont = getCreditCardContractHome().findByPrimaryKey(id);
				eCont.setDeleted(true);
				eCont.store();
			}

			return true;
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		catch (EJBException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	public Collection findAllAssessmentRoundByClubAndDivision(Group club, Group division) {
		try {
			return getAssessmentRoundHome().findAllByClubAndDivision(club, division);
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return null;
	}

	public AssessmentRound insertAssessmentRound(String name, Group club, Group division, Group group, User user, Timestamp start, Timestamp end, boolean includeChildren, Timestamp paymentDate, Timestamp runOnDate) {
		AssessmentRound round = null;
		try {
			round = getAssessmentRoundHome().create();
			round.setName(name);
			round.setClub(club);
			if (division != null)
				round.setDivision(division);
			if (group != null)
				round.setGroup(group);
			round.setExecutedBy(user);
			round.setStartTime(start);
			if (end != null)
				round.setEndTime(end);
			round.setIncludeChildren(includeChildren);
			round.setPaymentDate(paymentDate);
//			round.set

			round.store();
		}
		catch (CreateException e) {
			e.printStackTrace();

			return null;
		}

		return round;
	}
	
	public boolean deleteAssessmentRound(String ids[]) {
		try {
			for (int i = 0; i < ids.length; i++) {
				Integer id = new Integer(ids[i]);
				AssessmentRound eRound = getAssessmentRoundHome().findByPrimaryKey(id);
				eRound.setDeleted(true);
				eRound.store();
			}

			return true;
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		catch (EJBException e) {
			e.printStackTrace();
		}

		return false;
	}
	

	private ClubTariffTypeHome getClubTariffTypeHome() {
		try {
			return (ClubTariffTypeHome) IDOLookup.getHome(ClubTariffType.class);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}

		return null;
	}

	public boolean insertManualAssessment(Group club, Group div, User user, String groupId, String tariffId, String amount, String info, User currentUser) {
		Group group = null;
		if (groupId != null) {
			try {
				GroupHome gHome = (GroupHome) IDOLookup.getHome(Group.class);
				group = gHome.findByPrimaryKey(new Integer(groupId));
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
		}
		
		ClubTariff tariff = null;
		if (tariffId != null) {
			try {
				tariff = getClubTariffHome().findByPrimaryKey(new Integer(tariffId));
			}
			catch (NumberFormatException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
		}

		float am = 0;
		try {
			am = Float.parseFloat(amount);
		}
		catch (Exception e) {
		}

		return insertManualAssessment(club, div, user, group, tariff, am, info, currentUser);
	}

	public boolean insertManualAssessment(Group club, Group div, User user, Group group, ClubTariff tariff, float amount, String info, User currentUser) {
		try {
			FinanceEntry entry = getFinanceEntryHome().create();
			entry.setUser(user);
			entry.setClub(club);
			entry.setDivision(div);
			entry.setGroup(group);
			entry.setAmount(amount);
			entry.setDateOfEntry(IWTimestamp.getTimestampRightNow());
			if (info != null)
				entry.setInfo(info);
			else
				entry.setInfo(tariff.getText());
			entry.setStatusCreated();
			entry.setTypeManual();
			entry.setInsertedByUser(currentUser);
			entry.store();
			
			return true;
		}
		catch (CreateException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	private ClubTariffHome getClubTariffHome() {
		try {
			return (ClubTariffHome) IDOLookup.getHome(ClubTariff.class);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}

		return null;
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
	
	private CreditCardContractHome getCreditCardContractHome() {
		try {
			return (CreditCardContractHome) IDOLookup.getHome(CreditCardContract.class);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}

		return null;
	}

	private CreditCardTypeHome getCreditCardTypeHome() {
		try {
			return (CreditCardTypeHome) IDOLookup.getHome(CreditCardType.class);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}

		return null;
	}

	private AssessmentRoundHome getAssessmentRoundHome() {
		try {
			return (AssessmentRoundHome) IDOLookup.getHome(AssessmentRound.class);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}

		return null;
	}
}