package is.idega.idegaweb.member.isi.block.accounting.business;

import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReport;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.sql.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import com.idega.block.datareport.util.FieldsComparator;
import com.idega.block.datareport.util.ReportableCollection;
import com.idega.block.datareport.util.ReportableData;
import com.idega.block.datareport.util.ReportableField;
import com.idega.business.IBOLookup;
import com.idega.business.IBOSessionBean;
import com.idega.data.IDOException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

/**
 * @author Sigtryggur
 */
public class AccountingStatsBusinessBean extends IBOSessionBean implements AccountingStatsBusiness {
	
	private static final String LOCALIZED_LABEL = "AccountingStatsBusiness.label";
	private static final String LOCALIZED_DIVISION_NAME = "AccountingStatsBusiness.division_name";
	private static final String LOCALIZED_GROUP_NAME = "AccountingStatsBusiness.group_name";
	private static final String LOCALIZED_NAME = "AccountingStatsBusiness.name";
	private static final String LOCALIZED_PERSONAL_ID = "AccountingStatsBusiness.personal_id";
	private static final String LOCALIZED_AMOUNT = "AccountingStatsBusiness.amount";
	private static final String LOCALIZED_DATE_OF_ENTRY = "AccountingStatsBusiness.date_of_entry";
	private static final String LOCALIZED_AMOUNT_EQUALIZED = "AccountingStatsBusiness.amount_equalized";
	private static final String LOCALIZED_INFO = "AccountingStatsBusiness.info";
	
	private static final String FIELD_NAME_DIVISION_NAME = "division_name";
	private static final String FIELD_NAME_GROUP_NAME = "group_name";
	private static final String FIELD_NAME_NAME = "name";
	private static final String FIELD_NAME_PERSONAL_ID = "personal_id";
	private static final String FIELD_NAME_AMOUNT = "amount";
	private static final String FIELD_NAME_DATE_OF_ENTRY = "date_of_entry";
	private static final String FIELD_NAME_AMOUNT_EQUALIZED = "amount_equalized";
	private static final String FIELD_NAME_INFO = "info";
	
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
	public ReportableCollection getPaymentStatusByDivisionsGroupsAndDateIntervalFiltering(
			Date dateFromFilter,
			Date dateToFilter,
			Collection divisionsFilter,
			Collection groupsFilter)
	throws RemoteException {
		//initialize stuff
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		
		//PARAMETES
		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
		 "accountingreport", _iwrb.getLocalizedString(LOCALIZED_LABEL, "Current date"),
		 "label", TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale, IWTimestamp.LONG,IWTimestamp.SHORT),"GMT"));
		 
		 //PARAMETERS that are also FIELDS
		 //data from entity columns, can also be defined with an entity definition, see getClubMemberStatisticsForRegionalUnions method
		 //The name you give the field/parameter must not contain spaces or special characters		
		 ReportableField divisionField = new ReportableField(FIELD_NAME_DIVISION_NAME, String.class);
		 divisionField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_DIVISION_NAME, "Division"), currentLocale);
		 reportCollection.addField(divisionField);
		 
		 //fake columns (data gotten by business methods)
		 ReportableField groupField = new ReportableField(FIELD_NAME_GROUP_NAME, String.class);
		 groupField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_GROUP_NAME, "Group"), currentLocale);
		 reportCollection.addField(groupField);
		 
		 ReportableField nameField = new ReportableField(FIELD_NAME_NAME, String.class);
		 nameField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_NAME, "Name"), currentLocale);
		 reportCollection.addField(nameField);
		 
		 ReportableField personalIDField = new ReportableField(FIELD_NAME_PERSONAL_ID, String.class);
		 personalIDField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_PERSONAL_ID, "Personal id"),currentLocale);
		 reportCollection.addField(personalIDField);
		 
		 ReportableField amountField = new ReportableField(FIELD_NAME_AMOUNT, Double.class);
		 amountField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_AMOUNT, "Amount"), currentLocale);
		 reportCollection.addField(amountField);
		 
		 ReportableField entryDateField = new ReportableField(FIELD_NAME_DATE_OF_ENTRY, String.class);
		 entryDateField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_DATE_OF_ENTRY, "Date of entry"), currentLocale);
		 reportCollection.addField(entryDateField);
		 
		 ReportableField amountEqualizedField = new ReportableField(FIELD_NAME_AMOUNT_EQUALIZED, Double.class);
		 amountEqualizedField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_AMOUNT_EQUALIZED, "Amount equalized"), currentLocale);
		 reportCollection.addField(amountEqualizedField);
		 
		 ReportableField infoField = new ReportableField(FIELD_NAME_INFO, String.class);
		 infoField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_INFO, "Info"), currentLocale);
		 reportCollection.addField(infoField);
		 
		 //Real data stuff
		 //Gathering data
		 //Get all the workreports (actually more than needed)
		 //then for each get its leagues and the count for
		 //each age and create a row and insert into an ordered map by league
		 //then iterate the map and insert into the final report collection.
		 Collection finEntries = getAccountingBusiness().getFinanceEntriesByDateIntervalDivisionsAndGroups(dateFromFilter, dateToFilter, divisionsFilter, groupsFilter);
		 //List leagueGroupIdList = getGroupIdListFromLeagueGroupCollection(year, leaguesFilter, false);
		 Map financeEntriesByDivisions = new TreeMap();
		 
		 //Iterating through workreports and creating report data 
		 Iterator iter = finEntries.iterator();
		 while (iter.hasNext()) {
			 //the club
			 FinanceEntry financeEntry = (FinanceEntry) iter.next();
			 
			 String groupIdentifier = financeEntry.getGroup().getName();
			 String divisionIdentifier = financeEntry.getDivision().getName();
			 
			 
			 try {
			 	Group division = financeEntry.getDivision();	
			 		//if (!leagueGroupIdList.contains(league.getGroupId()) ) {
			 		//	continue; //don't process this one, go to next
			 		//}
			 		//create a new ReportData for each row
			 		ReportableData data = new ReportableData();
			 		//					add the data to the correct fields/columns
			 		
			 		data.addData(divisionField, divisionIdentifier );
			 		data.addData(groupField, groupIdentifier );
			 		data.addData(nameField, financeEntry.getUser().getName() );
			 		data.addData(personalIDField, financeEntry.getUser().getPersonalID().substring(0,6)+"-"+financeEntry.getUser().getPersonalID().substring(6,10) );
			 		data.addData(amountField, new Double(financeEntry.getAmount()) );
			 		data.addData(entryDateField, TextSoap.findAndCut((new IWTimestamp(financeEntry.getDateOfEntry())).getLocaleDate(currentLocale),"GMT") );
			 		data.addData(amountEqualizedField, new Double(financeEntry.getAmountEqualized()) );
			 		data.addData(infoField, financeEntry.getInfo() );
			 		
			 		//					get the stats
			 		//int playerCount = getWorkReportBusiness().getCountOfPlayersOfPlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(16, report, league);
			 		
			 		//data.addData(womenUnderAgeLimit, new Integer(getWorkReportBusiness().getCountOfFemalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(age, report, league)));
			 		//data.addData(womenOverOrEqualAgeLimit, new Integer(getWorkReportBusiness().getCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age, report, league)));
			 		//data.addData(menUnderAgeLimit,new Integer(getWorkReportBusiness().getCountOfMalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(age, report, league)));
			 		//data.addData(menOverOrEqualAgeLimit, new Integer(getWorkReportBusiness().getCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age, report, league)));
			 		
			 	//String leagueText = getLeagueIdentifier(league);
			 		
			 	//	data.addData(leagueString, leagueText);
			 		
			 		
			 		List statsForDivision = (List) financeEntriesByDivisions.get(division.getPrimaryKey());
			 		if (statsForDivision == null)
			 			statsForDivision = new Vector();
			 		statsForDivision.add(data);
			 		financeEntriesByDivisions.put(division.getPrimaryKey(), statsForDivision);			 	
			 }
			 catch (Exception e) {
			 	e.printStackTrace();
			 }
		 } 
		 // iterate through the ordered map and ordered lists and add to the final collection
		 Iterator statsDataIter = financeEntriesByDivisions.keySet().iterator();
		 while (statsDataIter.hasNext()) {
		 
		 	 List datas = (List) financeEntriesByDivisions.get(statsDataIter.next());
		  	 // don't forget to add the row to the collection
		 	 reportCollection.addAll(datas);
		 }
	
		 ReportableField[] sortFields = new ReportableField[] {groupField, divisionField};
		 Comparator comparator = new FieldsComparator(sortFields);
		 Collections.sort(reportCollection, comparator);
		 
		 //finished return the collection
		return reportCollection;
	}

}
