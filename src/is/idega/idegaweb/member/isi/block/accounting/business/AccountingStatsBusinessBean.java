package is.idega.idegaweb.member.isi.block.accounting.business;

import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReport;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import com.idega.block.datareport.util.FieldsComparator;
import com.idega.block.datareport.util.ReportableCollection;
import com.idega.block.datareport.util.ReportableData;
import com.idega.block.datareport.util.ReportableField;
import com.idega.business.IBOLookup;
import com.idega.business.IBOSessionBean;
import com.idega.data.IDOException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

/**
 * @author Sigtryggur
 */
public class AccountingStatsBusinessBean extends IBOSessionBean implements AccountingStatsBusiness {
	
	private static final String LOCALIZED_DIVISION_NAME = "AccountingStatsBusiness.division_name";
	private static final String LOCALIZED_GROUP_NAME = "AccountingStatsBusiness.group_name";
	
	private static final String FIELD_DIVISION_NAME = "division_name";
	private static final String FIELD_GROUP_NAME = "group_name";
	
	private AccountingBusiness accountingBiz = null;
	private IWBundle _iwb = null;
	private IWResourceBundle _iwrb = null;
	private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi.block.accounting";
	
	private AccountingBusiness getAccountingBusiness() throws RemoteException {
		if (accountingBiz == null) {
			accountingBiz = (AccountingBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), AccountingBusiness.class);
		}	
		return accountingBiz;
	}
	
	private void initializeBundlesIfNeeded() {
		if (_iwb == null) {
			_iwb = this.getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
		}
		_iwrb = _iwb.getResourceBundle(this.getUserContext().getCurrentLocale());
	}
	
	/*
	 * Report A29.1 of the ISI Specs
	 */
	public ReportableCollection getPaymentStatusByLeaguesGroupsAndDateIntervalFiltering(
			Date dateFromFilter,
			Date dateToFilter,
			Collection leaguesFilter,
			Collection regionalUnionsFilter)
	throws RemoteException {
		//initialize stuff
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		
		//PARAMETES
		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
		 "accountingreport",
		 _iwrb.getLocalizedString("AccountingStatsBusiness.label", "Current date"),
		 "label",
		 TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale),"GMT"));
		 
		 //PARAMETERS that are also FIELDS
		 //data from entity columns, can also be defined with an entity definition, see getClubMemberStatisticsForRegionalUnions method
		 //The name you give the field/parameter must not contain spaces or special characters		
		 ReportableField divisionString = new ReportableField(FIELD_DIVISION_NAME, String.class);
		 divisionString.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_DIVISION_NAME, "Division"), currentLocale);
		 reportCollection.addField(divisionString);
		 
		 //fake columns (data gotten by business methods)
		 ReportableField groupString = new ReportableField(FIELD_GROUP_NAME, String.class);
		 groupString.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_GROUP_NAME, "Group"), currentLocale);
		 reportCollection.addField(groupString);
		 /* 
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
		 */
		 
		 
		 //Real data stuff
		 //Gathering data
		 //Get all the workreports (actually more than needed)
		 //then for each get its leagues and the count for
		 //each age and create a row and insert into an ordered map by league
		 //then iterate the map and insert into the final report collection.
		 Collection clubs = getAccountingBusiness().getFinanceEntriesByDateIntervalDivisionsandGroups(dateFromFilter, dateToFilter, regionalUnionsFilter, regionalUnionsFilter);
		 //List leagueGroupIdList = getGroupIdListFromLeagueGroupCollection(year, leaguesFilter, false);
		 Map financeEntriesByDivisions = new TreeMap();
		 
		 /*
		 //Iterating through workreports and creating report data 
		 Iterator iter = clubs.iterator();
		 while (iter.hasNext()) {
			 //the club
			 FinanceEntry financeEntry = (FinanceEntry) iter.next();
			 
			 String groupIdentifier = financeEntry.getGroup().getName();
			 String divisionIdentifier = financeEntry.getDivision().getName();
			 
			 try {
			 	Collection leagues = report.getLeagues();
			 	Iterator iterator = leagues.iterator();
			 	while (iterator.hasNext()) {
			 		WorkReportGroup league = (WorkReportGroup) iterator.next();
			 		
			 		if (!leagueGroupIdList.contains(league.getGroupId()) ) {
			 			continue; //don't process this one, go to next
			 		}
			 		//create a new ReportData for each row
			 		ReportableData data = new ReportableData();
			 		//					add the data to the correct fields/columns
			 		
			 		data.addData(clubName, cName);
			 		data.addData(regionalUnionAbbreviation, regUniIdentifier );
			 		//					get the stats
			 		//int playerCount = getWorkReportBusiness().getCountOfPlayersOfPlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(16, report, league);
			 		
			 		data.addData(womenUnderAgeLimit, new Integer(getWorkReportBusiness().getCountOfFemalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(age, report, league)));
			 		data.addData(womenOverOrEqualAgeLimit, new Integer(getWorkReportBusiness().getCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age, report, league)));
			 		data.addData(menUnderAgeLimit,new Integer(getWorkReportBusiness().getCountOfMalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(age, report, league)));
			 		data.addData(menOverOrEqualAgeLimit, new Integer(getWorkReportBusiness().getCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age, report, league)));
			 		
			 	String leagueText = getLeagueIdentifier(league);
			 		
			 		data.addData(leagueString, leagueText);
			 		
			 		
			 		List statsForLeague = (List) workReportsByLeagues.get(league.getPrimaryKey());
			 		if (statsForLeague == null)
			 			statsForLeague = new Vector();
			 		statsForLeague.add(data);
			 		workReportsByLeagues.put(league.getPrimaryKey(), statsForLeague);
			 	}
			 }
			 catch (IDOException e) {
			 	e.printStackTrace();
			 }
		 } 
		 // iterate through the ordered map and ordered lists and add to the final collection
		 Iterator statsDataIter = financeEntriesByDivisions.keySet().iterator();
		 while (statsDataIter.hasNext()) {
		 
		 Map regMap = (Map) financeEntriesByDivisions.get(statsDataIter.next());
		 
		 // don't forget to add the row to the collection
		 reportCollection.addAll(regMap.values());
		 }
	
		 ReportableField[] sortFields = new ReportableField[] {groupString, divisionString};
		 Comparator comparator = new FieldsComparator(sortFields);
		 Collections.sort(reportCollection, comparator);
		 */
		 //finished return the collection
		return reportCollection;
	}

}
