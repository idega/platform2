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
import is.idega.idegaweb.member.isi.block.accounting.data.UserCreditCard;
import is.idega.idegaweb.member.isi.block.accounting.data.UserCreditCardHome;
import is.idega.idegaweb.member.util.IWMemberConstants;

import java.rmi.RemoteException;
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
import javax.ejb.RemoveException;

import com.idega.block.datareport.util.ReportableCollection;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * A service bean for the accounting part of the isi member system.
 * 
 * @author palli
 */
public class AccountingBusinessBean extends IBOServiceBean implements AccountingBusiness {
	/**
	 * A method to start the assessment batch. Starts up a thread that executes the batch, creates a log entry and then exits.
	 * 
	 * @param name The name of the assessment batch.
	 * @param club The group representing the club the batch is being executed for.
	 * @param division The group representing the division the batch is being executed for. Can be null.
	 * @param groupId The id of the top level group the batch is being executed for.
	 * @param user The user executing the batch.
	 * @param includeChildren If true then the batch is executed recursively for the children of the top level group.
	 * @param tariffs A String array of the tariff types the batch is being executed for.
	 * @param paymentDate The last payment date to be put in the FinanceEntry.
	 * @param runOnDate The date the tariffs are being fetched on.
	 */
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

	public Collection findAllTariffByClubAndDivision(Group club, Group division) {
		try {
			return getClubTariffHome().findAllByClubAndDivision(club, division);
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
	
	public boolean insertTariff(Group club, Group division, String groupId, String typeId, String text, String amount, Date from, Date to, boolean applyToChildren, String skip) {
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

		return insertTariff(club, division, group, type, text, am, from, to, applyToChildren, skip, null);
	}

	public boolean insertTariff(Group club, Group division, Group group, ClubTariffType type, String text, float amount, Date from, Date to, boolean applyToChildren, String skipList, List skip) {
		if (skip == null || skip.isEmpty()) {
			skip = new ArrayList();
		
			StringTokenizer tok = new StringTokenizer(skipList, ";");
			while (tok.hasMoreElements()) {
				String str = (String) tok.nextElement();
				skip.add(str);
			}			
		}
		
		ClubTariff eTariff;
		try {
			if (!skip.contains(group.getGroupType())) {
				if (division == null) {
					division = findDivisionForGroup(group);
				}
				
				eTariff = getClubTariffHome().create();
				eTariff.setClub(club);
				eTariff.setDivision(division);
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
						boolean ret = insertTariff(club, division, child, type, text, amount, from, to, applyToChildren, skipList, skip);
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
	
	public Group findDivisionForGroup(Group group) {
		if (group == null) {
			return null;
		}

		if (group.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION)) {
			return group;
		}
		else if (group.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB)) {
			return null;
		}
		
		List parents = group.getParentGroups();
		if (parents != null && !parents.isEmpty()) {
			Iterator it = parents.iterator();
			while (it.hasNext()) {
				Group parent = (Group) it.next();
				
				Group div = findDivisionForGroup(parent);
				if (div != null) {
					return div;
				}
			}
		}
		
		return null;
	}
	
	public Group findClubForGroup(Group group) {
		if (group == null) {
			return null;
		}

		if (group.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB)) {
			return group;
		}
		
		List parents = group.getParentGroups();
		if (parents != null && !parents.isEmpty()) {
			Iterator it = parents.iterator();
			while (it.hasNext()) {
				Group parent = (Group) it.next();
				
				Group div = findClubForGroup(parent);
				if (div != null) {
					return div;
				}
			}
		}
		
		return null;
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

	public boolean insertCreditCard(Group club, String division, String type, String number, String expMonth, String expYear, User user) {
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

		return insertCreditCard(club, div, cType, number, expMonth, expYear, user);
	}

	public boolean insertCreditCard(Group club, Group division, CreditCardType type, String number, String expMonth, String expYear, User user) {
		try {
			UserCreditCard eCard = getUserCreditCardHome().create();
			eCard.setClub(club);
			eCard.setDivision(division);
			eCard.setCardType(type);
			eCard.setCardOwner(user);
			eCard.setCardNumber(number);
			eCard.setExpirationMonth(expMonth);
			eCard.setExpirationYear(expYear);
			
			eCard.store();

			return true;
		}
		catch (CreateException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean deleteCreditCards(String ids[]) {
		try {
			for (int i = 0; i < ids.length; i++) {
				Integer id = new Integer(ids[i]);
				UserCreditCard eCard = getUserCreditCardHome().findByPrimaryKey(id);
				eCard.setDeleted(true);
				eCard.store();
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
			if (division == null) {
				division = findDivisionForGroup(group);
			}
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

				//@TODO just get entries I'm allowed to delete!!!!
				Collection rec = getFinanceEntryHome().findAllByAssessmentRound(eRound);
				if (rec != null && !rec.isEmpty()) {
					Iterator it = rec.iterator();
					while (it.hasNext()) {
						FinanceEntry entry = (FinanceEntry) it.next();
						try {
							entry.remove();
						}
						catch (RemoveException e1) {
							e1.printStackTrace();
						}
					}
				}
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
	
	public Collection findAllUsersCreditCards(Group club, User user, Group division) {
		try {
			return getUserCreditCardHome().findAllByUser(club, division, user);
		}
		catch (FinderException e) {
		}

		return null;
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
			if (div == null) {
				div = findDivisionForGroup(group);
			}
			entry.setDivision(div);
			entry.setGroup(group);
			entry.setAmount(amount);
			entry.setDateOfEntry(IWTimestamp.getTimestampRightNow());
			if (info != null && !"".equals(info))
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

	private UserCreditCardHome getUserCreditCardHome() {
		try {
			return (UserCreditCardHome) IDOLookup.getHome(UserCreditCard.class);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/*
	 * Report A29.1 of the ISI Specs
	 */
	public ReportableCollection getPaymentStatusByLeaguesGroupsAndDateIntervalFiltering(
			Collection leaguesFilter,
			Collection regionalUnionsFilter)
	throws RemoteException {
		System.out.println("Magnað maður magnað");
		//initialize stuff
		/*int age = splitAge==null?DEFAULT_SPLIT_AGE:splitAge.intValue();
		initializeBundlesIfNeeded();*/
		ReportableCollection reportCollection = new ReportableCollection();
		/*Locale currentLocale = this.getUserContext().getCurrentLocale();
		
		//PARAMETES
		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				_iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale),"GMT"));
		
		//PARAMETERS that are also FIELDS
		//data from entity columns, can also be defined with an entity definition, see getClubMemberStatisticsForRegionalUnions method
		//The name you give the field/parameter must not contain spaces or special characters		
		ReportableField regionalUnionAbbreviation = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionAbbreviation.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionAbbreviation);
		
		//fake columns (data gotten by business methods)
		ReportableField leagueString = new ReportableField(FIELD_NAME_LEAGUE_NAME, String.class);
		leagueString.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_LEAGUE_INFO, "League"), currentLocale);
		reportCollection.addField(leagueString);
		
		ReportableField womenUnderAgeLimit = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE, Integer.class);
		womenUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAgeLimit);
		
		ReportableField womenOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
		
		ReportableField menUnderAgeLimit = new ReportableField(FIELD_NAME_MEN_UNDER_AGE, Integer.class);
		menUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_UNDER, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAgeLimit);
		
		ReportableField menOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL, "men "+age+"+"), currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
		
		ReportableField bothGendersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_UNDER, "all -"+age), currentLocale);
		reportCollection.addField(bothGendersUnderAge);
		
		ReportableField bothGendersEqualOverAge = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
		bothGendersEqualOverAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER, "all "+age+"+"), currentLocale);
		reportCollection.addField(bothGendersEqualOverAge);
		
		
		//Real data stuff
		//Gathering data
		//Get all the workreports (actually more than needed)
		//then for each get its leagues and the count for
		//each age and create a row and insert into an ordered map by league
		//then iterate the map and insert into the final report collection.
		Collection clubs = getWorkReportBusiness().getWorkReportsByYearRegionalUnionsAndClubs(year.intValue(), regionalUnionsFilter, null);
		Map workReportsByLeagues = new TreeMap();
		List leagueGroupIdList = getGroupIdListFromLeagueGroupCollection(year,leaguesFilter,false);
		
		//Iterating through workreports and creating report data 
		Iterator iter = clubs.iterator();
		while (iter.hasNext()) {
			//the club
			WorkReport report = (WorkReport) iter.next();
			String regionalUnionIdentifier = getRegionalUnionIdentifier(report);
			
			try {
				Collection leagues = report.getLeagues();
				Iterator iterator = leagues.iterator();
				while (iterator.hasNext()) {
					WorkReportGroup league = (WorkReportGroup) iterator.next();
					
					if (!leagueGroupIdList.contains(league.getGroupId())) {
						continue; //don't process this one, go to next
					}
					String leagueIdentifier = getLeagueIdentifier(league);
					
					//add the data
					Map regionalUnionStatsMapPerLeague = (Map) workReportsByLeagues.get(league.getPrimaryKey());
					if (regionalUnionStatsMapPerLeague == null){
						regionalUnionStatsMapPerLeague = new TreeMap();
					}
					
					//fetch the stats or initialize
					ReportableData regData = (ReportableData) regionalUnionStatsMapPerLeague.get(regionalUnionIdentifier);
					if(regData==null){//initialize
						regData = new ReportableData();
						regData.addData(regionalUnionAbbreviation, regionalUnionIdentifier );
						regData.addData(leagueString, leagueIdentifier);
						regData.addData(womenUnderAgeLimit, new Integer(0));
						regData.addData(womenOverOrEqualAgeLimit, new Integer(0));
						regData.addData(menUnderAgeLimit,new Integer(0));
						regData.addData(menOverOrEqualAgeLimit, new Integer(0));
					}
					
					//add to counts
					int womenUnder = getWorkReportBusiness().getCountOfFemalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(age, report, league);
					regData = addToIntegerCount(womenUnderAgeLimit, regData, womenUnder);
					
					int womenOver = getWorkReportBusiness().getCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age, report, league);
					regData = addToIntegerCount(womenOverOrEqualAgeLimit, regData, womenOver);
					
					int menUnder = getWorkReportBusiness().getCountOfMalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(age, report, league);				
					regData = addToIntegerCount(menUnderAgeLimit, regData, menUnder);
					
					int menOver = getWorkReportBusiness().getCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age, report, league);
					regData = addToIntegerCount(menOverOrEqualAgeLimit, regData, menOver);
					
					//put it back again
					regionalUnionStatsMapPerLeague.put(regionalUnionIdentifier,regData);
					//and into the other map
					workReportsByLeagues.put(league.getPrimaryKey(), regionalUnionStatsMapPerLeague);
					
				}
				
			}
			catch (IDOException e) {
				e.printStackTrace();
			}
			
		}
		
		// iterate through the ordered map and ordered lists and add to the final collection
		Iterator statsDataIter = workReportsByLeagues.keySet().iterator();
		while (statsDataIter.hasNext()) {
			
			Map regMap = (Map) workReportsByLeagues.get(statsDataIter.next());
			
			// don't forget to add the row to the collection
			reportCollection.addAll(regMap.values());
		}
		
		ReportableField[] sortFields = new ReportableField[] {leagueString, regionalUnionAbbreviation};
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);
		*/
		//finished return the collection
		return reportCollection;
	}
}