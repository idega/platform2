/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
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

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author palli
 */
public class AccountingBusinessBean extends IBOServiceBean implements AccountingBusiness {
	public boolean doAssessment(String name, Group club, Group division, String groupId, User user, boolean useParent, boolean includeChildren) {
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
		
		Thread assRoundThread = new AssessmentRoundThread(name, club, division, group, null, user, useParent, includeChildren, getIWApplicationContext());
		
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
	
	public boolean insertTariff(Group club, String groupId, String typeId, String text, String amount, Date from, Date to) {
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
		catch(Exception e) {
		}
		
		return insertTariff(club,group,type,text,am,from,to);
	}
	
	public boolean insertTariff(Group club, Group group, ClubTariffType type, String text, float amount, Date from, Date to) {
		ClubTariff eTariff;
		try {
			eTariff = getClubTariffHome().create();
			eTariff.setClub(club);
			eTariff.setGroup(group);
			eTariff.setTariffType(type);
			eTariff.setText(text);
			eTariff.setAmount(amount);
			eTariff.setPeriodFrom(from);
			eTariff.setPeriodTo(to);
			
			eTariff.store();
			
			return true;
		}
		catch (CreateException e) {
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
		if (division != null) {
			try {
				GroupHome gHome = (GroupHome) IDOLookup.getHome(Group.class);
				div = gHome.findByPrimaryKey(new Integer(division));
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
		
		return insertCreditCardContract(club,div,contractNumber,cType);
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

	public Collection findAllAssessmentRoundByClubAndDivision(Group club, Group division) {
		try {
			return getAssessmentRoundHome().findAllByClubAndDivision(club, division);
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean insertAssessmentRound(String name, Group club, Group division, Group group, User user, Timestamp start, Timestamp end, boolean useParent, boolean includeChildren) {
		try {
			AssessmentRound round = getAssessmentRoundHome().create();
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
			round.setUseParentTariff(useParent);
			round.setIncludeChildren(includeChildren);
			
			round.store();
		}
		catch (CreateException e) {
			e.printStackTrace();
			
			return false;
		}
		
		return true;
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
	
	private ClubTariffHome getClubTariffHome() {
		try {
			return (ClubTariffHome) IDOLookup.getHome(ClubTariff.class);
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