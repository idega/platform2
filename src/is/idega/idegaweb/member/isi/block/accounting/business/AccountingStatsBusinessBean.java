package is.idega.idegaweb.member.isi.block.accounting.business;

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
import com.idega.business.IBOSessionBean;
import com.idega.data.IDOException;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

/**
 * @author Sigtryggur
 */
public class AccountingStatsBusinessBean extends IBOSessionBean implements AccountingStatsBusiness {
	
	/*
	 * Report A29.1 of the ISI Specs
	 */
	public ReportableCollection getPaymentStatusByLeaguesGroupsAndDateIntervalFiltering(
			Date dateFromFilter,
			Collection leaguesFilter,
			Collection regionalUnionsFilter)
	throws RemoteException {
		//initialize stuff
		//int age = splitAge==null?DEFAULT_SPLIT_AGE:splitAge.intValue();
		//initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		
		//PARAMETES
		//Add extra...because the inputhandlers supply the basic header texts
		/*reportCollection.addExtraHeaderParameter(
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
		 
		 //finished return the collection*/
		return reportCollection;
	}

}
