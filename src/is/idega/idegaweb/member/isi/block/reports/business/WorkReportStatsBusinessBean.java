package is.idega.idegaweb.member.isi.block.reports.business;

import is.idega.idegaweb.member.isi.block.reports.data.WorkReport;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.block.datareport.util.ReportableCollection;
import com.idega.block.datareport.util.ReportableData;
import com.idega.block.datareport.util.ReportableField;
import com.idega.block.datareport.util.FieldsComparator;
import com.idega.business.IBOLookup;
import com.idega.business.IBOSessionBean;
import com.idega.data.IDOException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;

/**
 * Title: WorkReportStatsBusinessBean Description: The business bean for generating statistical report on the workreport data. Copyright: Copyright
 * (c) 2003 Company: idega Software
 * 
 * @author <br><a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a><br>
 * @version 1.0
 */
public class WorkReportStatsBusinessBean extends IBOSessionBean implements WorkReportStatsBusiness {
	
	private WorkReportBusiness workBiz = null;
	private IWBundle _iwb = null;
	private IWResourceBundle _iwrb = null;
	private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	private final long millisecondsInOneDay = 8640000;
	
	// keys for localized strings
	private static final String LOCALIZED_LABEL = "WorkReportStatsBusiness.label";
	private static final String LOCALIZED_CLUB_NAME = "WorkReportStatsBusiness.club_name";
	private static final String LOCALIZED_REGIONAL_UNION_NAME = "WorkReportStatsBusiness.regional_union_name";
	private static final String LOCALIZED_LEAGUE_INFO = "WorkReportStatsBusiness.league_info";
	private static final String LOCALIZED_WOMEN_UNDER = "WorkReportStatsBusiness.womenUnderAgeLimit_";
	private static final String LOCALIZED_WOMEN_OVER_OR_EQUAL = "WorkReportStatsBusiness.womenOverOrEqualAgeLimit_";
	private static final String LOCALIZED_MEN_UNDER = "WorkReportStatsBusiness.menUnderAgeLimit_";
	private static final String LOCALIZED_MEN_OVER_OR_EQUAL = "WorkReportStatsBusiness.menOverOrEqualAgeLimit_";
	private static final String LOCALIZED_ALL_UNDER = "WorkReportStatsBusiness.bothGendersUnderAge_";
	private static final String LOCALIZED_ALL_EQUAL_OR_OVER = "WorkReportStatsBusiness.bothGendersEqualOverAge_";
	private static final String LOCALIZED_ALL_LAST_YEAR = "WorkReportStatsBusiness.bothGendersLastYear";
	
	/**
	 *  
	 */
	public WorkReportStatsBusinessBean() {
		super();
	}
	
	private WorkReportBusiness getWorkReportBusiness() throws RemoteException {
		if (workBiz == null) {
			workBiz = (WorkReportBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), WorkReportBusiness.class);
		}
		
		return workBiz;
	}
	
	private void initializeBundlesIfNeeded() {
		if (_iwb == null) {
			_iwb = this.getIWApplicationContext().getApplication().getBundle(IW_BUNDLE_IDENTIFIER);
		}
		_iwrb = _iwb.getResourceBundle(this.getUserContext().getCurrentLocale());
	}
	
	/*
	 * Report B12.1.1 of the ISI Specs
	 */
	public ReportableCollection getStatisticsForLeaguesByYearRegionalUnionsClubsAndLeaguesFiltering(
			final Integer year,
			Collection regionalUnionsFilter,
			Collection clubsFilter,
			Collection leaguesFilter)
	throws RemoteException {
		
		
		//initialize stuff
		int age = 16;
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		
		//PARAMETES
		
		//A way to set a static parameter
		ReportableField comparingYearStat = new ReportableField("comparing_year", Integer.class) {
			public String getLocalizedName(Locale locale) {
				return Integer.toString(year.intValue() - 1);
			}
		};
		
		reportCollection.addField(comparingYearStat); //don't forget to add the param/field
		
		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				_iwrb.getLocalizedString(LOCALIZED_LABEL, "Current date"),
				"label",
				IWTimestamp.getTimestampRightNow().toGMTString());
		
		//PARAMETERS that are also FIELDS
		//data from entity columns, can also be defined with an entity definition, see getClubMemberStatisticsForRegionalUnions method
		//The name you give the field/parameter must not contain spaces or special characters
		final ReportableField clubName = new ReportableField("club_name", String.class);
		clubName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_CLUB_NAME, "Club name"), currentLocale);
		reportCollection.addField(clubName);
		
		final ReportableField regionalUnionAbbreviation = new ReportableField("regional_union_name", String.class);
		regionalUnionAbbreviation.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionAbbreviation);
		
		//fake columns (data gotten by business methods)
		final ReportableField leagueString = new ReportableField("league_info", String.class);
		leagueString.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_LEAGUE_INFO, "League"), currentLocale);
		reportCollection.addField(leagueString);
		
		ReportableField womenUnderAgeLimit = new ReportableField("womenUnderAgeLimit", Integer.class);
		womenUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER + age, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAgeLimit);
		
		ReportableField womenOverOrEqualAgeLimit = new ReportableField("womenOverOrEqualAgeLimit", Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL + age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
		
		ReportableField menUnderAgeLimit = new ReportableField("menUnderAgeLimit", Integer.class);
		menUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_UNDER + age, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAgeLimit);
		
		ReportableField menOverOrEqualAgeLimit = new ReportableField("menOverOrEqualAgeLimit", Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL + age, "men "+age+"+"), currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
		
		
		ReportableField bothGendersUnderAge = new ReportableField("bothGendersUnderAge", Integer.class);
		bothGendersUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_UNDER + age, "all -"+age), currentLocale);
		reportCollection.addField(bothGendersUnderAge);
		
		ReportableField bothGendersEqualOverAge = new ReportableField("bothGendersEqualOverAge", Integer.class);
		bothGendersEqualOverAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER + age, "all "+age+"+"), currentLocale);
		reportCollection.addField(bothGendersEqualOverAge);
		
		//Real data stuff
		//Gathering data
		//Get all the workreports (actually more than needed)
		//then for each get its leagues and the count for
		//each age and create a row and insert into an ordered map by league
		//then iterate the map and insert into the final report collection.
		Collection clubs = getWorkReportBusiness().getWorkReportsByYearRegionalUnionsAndClubs(year.intValue(), regionalUnionsFilter, clubsFilter);
		//Collection clubs = getWorkReportBusiness().getWorkReportsForRegionalUnionCollection(year.intValue(), regionalUnionsFilter);
		
		List leagueGroupIdList = null;
		if (leaguesFilter != null && !leaguesFilter.isEmpty()) {
			leagueGroupIdList = new Vector();
			Iterator iter = leaguesFilter.iterator();
			while (iter.hasNext()) {
				Group group = (Group) iter.next();
				leagueGroupIdList.add(group.getPrimaryKey());
			}
			
		}
		
		Map workReportsByLeagues = new TreeMap();
		//Iterating through workreports and creating report data 
		Iterator iter = clubs.iterator();
		while (iter.hasNext()) {
			WorkReport report = (WorkReport) iter.next();
			//get last years report for comparison
			WorkReport lastYearReport=null;
			try {
				lastYearReport = getWorkReportBusiness().getWorkReportHome().findWorkReportByGroupIdAndYearOfReport(report.getGroupId().intValue(),year.intValue()-1);
			}
			catch (FinderException e1) {
				//e1.printStackTrace();
				System.err.println("WorkReportStatsBusiness : No report for year before :"+year);
			}
			
			String cName = report.getGroupName();
			String regUniAbbr = report.getRegionalUnionAbbreviation();
			
			try {
				Collection leagues = report.getLeagues();
				Iterator iterator = leagues.iterator();
				while (iterator.hasNext()) {
					WorkReportGroup league = (WorkReportGroup) iterator.next();
					
					if (leagueGroupIdList != null && !leagueGroupIdList.contains(league.getGroupId())) {
						continue; //don't process this one, go to next
					}
					//create a new ReportData for each row
					ReportableData data = new ReportableData();
					//					add the data to the correct fields/columns
					
					data.addData(clubName, cName);
					
					if(regUniAbbr==null){
						regUniAbbr = report.getRegionalUnionNumber();
					}
					if(regUniAbbr==null){
						regUniAbbr = report.getRegionalUnionName();
					}
					if(regUniAbbr==null){
						regUniAbbr = report.getRegionalUnionGroupId().toString();
					}
					
					data.addData(regionalUnionAbbreviation, regUniAbbr );
					//					get the stats
					//int playerCount = getWorkReportBusiness().getCountOfPlayersOfPlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(16, report, league);
					
					data.addData(womenUnderAgeLimit, new Integer(getWorkReportBusiness().getCountOfFemalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(age, report, league)));
					data.addData(womenOverOrEqualAgeLimit, new Integer(getWorkReportBusiness().getCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age, report, league)));
					data.addData(menUnderAgeLimit,new Integer(getWorkReportBusiness().getCountOfMalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(age, report, league)));
					data.addData(menOverOrEqualAgeLimit, new Integer(getWorkReportBusiness().getCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age, report, league)));
					
					if(lastYearReport!=null){
						Integer lastYear = new Integer(getWorkReportBusiness().getCountOfPlayersByWorkReportAndWorkReportGroup(lastYearReport,league));
						data.addData(comparingYearStat,lastYear);
					}
					
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
		
		//		iterate through the ordered map and ordered lists and add to the final collection
	
		Iterator statsDataIter = workReportsByLeagues.keySet().iterator();
		while (statsDataIter.hasNext()) {
			
			List datas = (List) workReportsByLeagues.get(statsDataIter.next());
			//			don't forget to add the row to the collection
			reportCollection.addAll(datas);
		}
		
		ReportableField[] sortFields = new ReportableField[] {leagueString, regionalUnionAbbreviation, clubName};
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);
		
		//finished return the collection
		return reportCollection;
	}
	
	
	/*
	 * Report B12.1.2 of the ISI Specs
	 */
	public ReportableCollection getStatisticsForLeaguesByYearRegionalUnionsAndLeaguesFiltering(
			final Integer year,
			Collection regionalUnionsFilter,
			Collection leaguesFilter)
	throws RemoteException {
		
		
		//initialize stuff
		int age = 16;
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		
		//PARAMETES
		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				_iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				IWTimestamp.getTimestampRightNow().toGMTString());
		
		//PARAMETERS that are also FIELDS
		//data from entity columns, can also be defined with an entity definition, see getClubMemberStatisticsForRegionalUnions method
		//The name you give the field/parameter must not contain spaces or special characters		
		ReportableField regionalUnionAbbreviation = new ReportableField("regional_union_name", String.class);
		regionalUnionAbbreviation.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionAbbreviation);
		
		//fake columns (data gotten by business methods)
		ReportableField leagueString = new ReportableField("league_info", String.class);
		leagueString.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_LEAGUE_INFO, "League"), currentLocale);
		reportCollection.addField(leagueString);
		
		ReportableField womenUnderAgeLimit = new ReportableField("womenUnderAgeLimit", Integer.class);
		womenUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER+age, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAgeLimit);
		
		ReportableField womenOverOrEqualAgeLimit = new ReportableField("womenOverOrEqualAgeLimit", Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
		
		ReportableField menUnderAgeLimit = new ReportableField("menUnderAgeLimit", Integer.class);
		menUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_UNDER+age, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAgeLimit);
		
		ReportableField menOverOrEqualAgeLimit = new ReportableField("menOverOrEqualAgeLimit", Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL+age, "men "+age+"+"), currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
		
		ReportableField bothGendersUnderAge = new ReportableField("bothGendersUnderAge", Integer.class);
		bothGendersUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_UNDER+age, "all -"+age), currentLocale);
		reportCollection.addField(bothGendersUnderAge);
		
		ReportableField bothGendersEqualOverAge = new ReportableField("bothGendersEqualOverAge", Integer.class);
		bothGendersEqualOverAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER+age, "all "+age+"+"), currentLocale);
		reportCollection.addField(bothGendersEqualOverAge);
		
		
		//Real data stuff
		//Gathering data
		//Get all the workreports (actually more than needed)
		//then for each get its leagues and the count for
		//each age and create a row and insert into an ordered map by league
		//then iterate the map and insert into the final report collection.
		Collection clubs = getWorkReportBusiness().getWorkReportsByYearRegionalUnionsAndClubs(year.intValue(), regionalUnionsFilter, null);
		Map workReportsByLeagues = new TreeMap();
		List leagueGroupIdList = getGroupIdListFromWorkReportGroupCollection(leaguesFilter);
		
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
					
					if (leagueGroupIdList != null && !leagueGroupIdList.contains(league.getGroupId())) {
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
		
		//finished return the collection
		return reportCollection;
	}
	
	/*
	 * Report B12.1.3 of the ISI Specs
	 */
	public ReportableCollection getStatisticsForLeaguesByYearAndLeaguesFiltering(final Integer year,Collection leaguesFilter)throws RemoteException {
	
		//initialize stuff
		int age = 16;
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		
		//PARAMETES
		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				_iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				IWTimestamp.getTimestampRightNow().toGMTString());
		
		//PARAMETERS that are also FIELDS
		//data from entity columns, can also be defined with an entity definition, see getClubMemberStatisticsForRegionalUnions method
		//The name you give the field/parameter must not contain spaces or special characters
		
		//fake columns (data gotten by business methods)
		ReportableField leagueString = new ReportableField("league_info", String.class);
		leagueString.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_LEAGUE_INFO, "League"), currentLocale);
		reportCollection.addField(leagueString);
		
		ReportableField womenUnderAgeLimit = new ReportableField("womenUnderAgeLimit", Integer.class);
		womenUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER+age, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAgeLimit);
		
		ReportableField womenOverOrEqualAgeLimit = new ReportableField("womenOverOrEqualAgeLimit", Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
		
		ReportableField menUnderAgeLimit = new ReportableField("menUnderAgeLimit", Integer.class);
		menUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_UNDER+age, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAgeLimit);
		
		ReportableField menOverOrEqualAgeLimit = new ReportableField("menOverOrEqualAgeLimit", Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL+age, "men "+age+"+"), currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
		
		ReportableField bothGendersUnderAge = new ReportableField("bothGendersUnderAge", Integer.class);
		bothGendersUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_UNDER+age, "all -"+age), currentLocale);
		reportCollection.addField(bothGendersUnderAge);
		
		ReportableField bothGendersEqualOverAge = new ReportableField("bothGendersEqualOverAge", Integer.class);
		bothGendersEqualOverAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER+age, "all "+age+"+"), currentLocale);
		reportCollection.addField(bothGendersEqualOverAge);
		
		//Real data stuff
		//Gathering data
		//Get all the workreports (actually more than needed)
		//then for each get its leagues and the count for
		//each age and create a row and insert into an ordered map by league
		//then insert into the final report collection.
		Collection clubs = getWorkReportBusiness().getWorkReportsByYearRegionalUnionsAndClubs(year.intValue(), null, null);
		Map leagueStatsMap = new TreeMap();
		List leagueGroupIdList = getGroupIdListFromWorkReportGroupCollection(leaguesFilter);
		
		//Iterating through workreports and creating report data 
		Iterator iter = clubs.iterator();
		while (iter.hasNext()) {
			//the club
			WorkReport report = (WorkReport) iter.next();
			
			try {
				Collection leagues = report.getLeagues();
				Iterator iterator = leagues.iterator();
				while (iterator.hasNext()) {
					WorkReportGroup league = (WorkReportGroup) iterator.next();
					Integer leagueKey = (Integer) league.getPrimaryKey();
					
					if (leagueGroupIdList != null && !leagueGroupIdList.contains(league.getGroupId())) {
						continue; //don't process this one, go to next
					}
					
					String leagueIdentifier = getLeagueIdentifier(league);
					//fetch the stats or initialize
					ReportableData leagueStatsData = (ReportableData) leagueStatsMap.get(leagueKey);
					if(leagueStatsData==null){//initialize
						leagueStatsData = new ReportableData();
						leagueStatsData.addData(leagueString, leagueIdentifier);
						leagueStatsData.addData(womenUnderAgeLimit, new Integer(0));
						leagueStatsData.addData(womenOverOrEqualAgeLimit, new Integer(0));
						leagueStatsData.addData(menUnderAgeLimit,new Integer(0));
						leagueStatsData.addData(menOverOrEqualAgeLimit, new Integer(0));
					}
					
					//add to counts
					int womenUnder = getWorkReportBusiness().getCountOfFemalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(age, report, league);
					leagueStatsData = addToIntegerCount(womenUnderAgeLimit, leagueStatsData, womenUnder);
					
					int womenOver = getWorkReportBusiness().getCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age, report, league);
					leagueStatsData = addToIntegerCount(womenOverOrEqualAgeLimit, leagueStatsData, womenOver);
					
					int menUnder = getWorkReportBusiness().getCountOfMalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(age, report, league);				
					leagueStatsData = addToIntegerCount(menUnderAgeLimit, leagueStatsData, menUnder);
					
					int menOver = getWorkReportBusiness().getCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age, report, league);
					leagueStatsData = addToIntegerCount(menOverOrEqualAgeLimit, leagueStatsData, menOver);
					
					//put it back again
					leagueStatsMap.put(leagueKey,leagueStatsData);
					
				}
				
			}
			catch (IDOException e) {
				e.printStackTrace();
			}
			
		}
		
		//add the data to the collection
		reportCollection.addAll(leagueStatsMap.values());
		
		ReportableField[] sortFields = new ReportableField[] {leagueString};
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);
		
		//finished return the collection
		return reportCollection;
	}
	
	/*
	 * Report B12.1.4 of the ISI Specs
	 */
	public ReportableCollection getStatisticsForLeaguesByYearAndLeaguesFilteringComparedWithLastYear(final Integer year,Collection leaguesFilter)throws RemoteException {
	
		//initialize stuff
		int age = 16;
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		
		//PARAMETES
		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				_iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				IWTimestamp.getTimestampRightNow().toGMTString());
		
		//PARAMETERS that are also FIELDS
		//data from entity columns, can also be defined with an entity definition, see getClubMemberStatisticsForRegionalUnions method
		//The name you give the field/parameter must not contain spaces or special characters
		//fake columns (data gotten by business methods)
		//A way to set a static parameter
		ReportableField comparingYearStat = new ReportableField("comparing_year", Integer.class) {
			public String getLocalizedName(Locale locale) {
				return Integer.toString(year.intValue() - 1);
			}
		};
		reportCollection.addField(comparingYearStat);
		
		ReportableField leagueString = new ReportableField("league_info", String.class);
		leagueString.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_LEAGUE_INFO, "League"), currentLocale);
		reportCollection.addField(leagueString);
		
		//Selected years parameters and fields
		ReportableField womenUnderAgeLimit = new ReportableField("womenUnderAgeLimit", Integer.class);
		womenUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER+age, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAgeLimit);
		
		ReportableField womenOverOrEqualAgeLimit = new ReportableField("womenOverOrEqualAgeLimit", Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
		
		ReportableField menUnderAgeLimit = new ReportableField("menUnderAgeLimit", Integer.class);
		menUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_UNDER+age, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAgeLimit);
		
		ReportableField menOverOrEqualAgeLimit = new ReportableField("menOverOrEqualAgeLimit", Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL+age, "men "+age+"+"), currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
		
		ReportableField bothGendersUnderAge = new ReportableField("bothGendersUnderAge", Integer.class);
		bothGendersUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_UNDER+age, "all -"+age), currentLocale);
		reportCollection.addField(bothGendersUnderAge);
		
		ReportableField bothGendersEqualOverAge = new ReportableField("bothGendersEqualOverAge", Integer.class);
		bothGendersEqualOverAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER+age, "all "+age+"+"), currentLocale);
		reportCollection.addField(bothGendersEqualOverAge);
		
		//last years parameters and fields
		ReportableField womenUnderAgeLimitLastYear = new ReportableField("womenUnderAgeLimitLastYear", Integer.class);
		womenUnderAgeLimitLastYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER+age, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAgeLimitLastYear);
		
		ReportableField womenOverOrEqualAgeLimitLastYear = new ReportableField("womenOverOrEqualAgeLimitLastYear", Integer.class);
		womenOverOrEqualAgeLimitLastYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimitLastYear);
		
		ReportableField menUnderAgeLimitLastYear = new ReportableField("menUnderAgeLimitLastYear", Integer.class);
		menUnderAgeLimitLastYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_UNDER+age, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAgeLimitLastYear);
		
		ReportableField menOverOrEqualAgeLimitLastYear = new ReportableField("menOverOrEqualAgeLimitLastYear", Integer.class);
		menOverOrEqualAgeLimitLastYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL+age, "men "+age+"+"), currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimitLastYear);
		
		ReportableField bothGendersUnderAgeLastYear = new ReportableField("bothGendersUnderAgeLastYear", Integer.class);
		bothGendersUnderAgeLastYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_UNDER+age, "all -"+age), currentLocale);
		reportCollection.addField(bothGendersUnderAgeLastYear);
		
		ReportableField bothGendersEqualOverAgeLastYear = new ReportableField("bothGendersEqualOverAgeLastYear", Integer.class);
		bothGendersEqualOverAgeLastYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER+age, "all "+age+"+"), currentLocale);
		reportCollection.addField(bothGendersEqualOverAgeLastYear);
		
		//Real data stuff
		//Gathering data
		//Get all the workreports (actually more than needed)
		//then for each get its leagues and the count for
		//each age and create a row and insert into an ordered map by league
		//then insert into the final report collection.
		Collection clubs = getWorkReportBusiness().getWorkReportsByYearRegionalUnionsAndClubs(year.intValue(), null, null);
		
		Map leagueStatsMap = new TreeMap();
		List leagueGroupIdList = getGroupIdListFromWorkReportGroupCollection(leaguesFilter);
		
		//Iterating through workreports and creating report data 
		Iterator iter = clubs.iterator();
	
		while (iter.hasNext()) {
			//the club
			WorkReport report = (WorkReport) iter.next();
//			get last years report for comparison
			WorkReport lastYearReport=null;
			  try {
				  lastYearReport = getWorkReportBusiness().getWorkReportHome().findWorkReportByGroupIdAndYearOfReport(report.getGroupId().intValue(),year.intValue()-1);
			  }
			  catch (FinderException e1) {
				  //e1.printStackTrace();
				  System.err.println("WorkReportStatsBusiness : No report for year before :"+year);
			  }
			
			try {
				Collection leagues = report.getLeagues();
				Iterator iterator = leagues.iterator();
				while (iterator.hasNext()) {
					WorkReportGroup league = (WorkReportGroup) iterator.next();
					Integer leagueKey = (Integer) league.getGroupId();//for comparison this must be the same key both years
					
					if (leagueGroupIdList != null && !leagueGroupIdList.contains(league.getGroupId())) {
						continue; //don't process this one, go to next
					}
					
					WorkReportGroup lastYearLeague=null;
					if(lastYearReport!=null){//no point if it is
						try {
							lastYearLeague = getWorkReportBusiness().getWorkReportGroupHome().findWorkReportGroupByGroupIdAndYear(leagueKey.intValue(),year.intValue()-1);
						}
						catch (FinderException e2) {
							System.err.println("WorkReportStatsBusiness : No report for year before :"+year);
							//e2.printStackTrace();
						}
					}
					String leagueIdentifier = getLeagueIdentifier(league);
					//fetch the stats or initialize
					ReportableData leagueStatsData = (ReportableData) leagueStatsMap.get(leagueKey);
					if(leagueStatsData==null){//initialize
						leagueStatsData = new ReportableData();
						//Actually fetching a lot more than needed the layout sums up fields
						leagueStatsData.addData(leagueString, leagueIdentifier);
						leagueStatsData.addData(womenUnderAgeLimit, new Integer(0));
						leagueStatsData.addData(womenOverOrEqualAgeLimit, new Integer(0));
						leagueStatsData.addData(menUnderAgeLimit,new Integer(0));
						leagueStatsData.addData(menOverOrEqualAgeLimit, new Integer(0));
						//last year
						leagueStatsData.addData(womenUnderAgeLimitLastYear, new Integer(0));
						leagueStatsData.addData(womenOverOrEqualAgeLimitLastYear, new Integer(0));
						leagueStatsData.addData(menUnderAgeLimitLastYear,new Integer(0));
						leagueStatsData.addData(menOverOrEqualAgeLimitLastYear, new Integer(0));
					}
					
					//add to counts
					int womenUnder = getWorkReportBusiness().getCountOfFemalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(age, report, league);
					leagueStatsData = addToIntegerCount(womenUnderAgeLimit, leagueStatsData, womenUnder);
					
					int womenOver = getWorkReportBusiness().getCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age, report, league);
					leagueStatsData = addToIntegerCount(womenOverOrEqualAgeLimit, leagueStatsData, womenOver);
					
					int menUnder = getWorkReportBusiness().getCountOfMalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(age, report, league);				
					leagueStatsData = addToIntegerCount(menUnderAgeLimit, leagueStatsData, menUnder);
					
					int menOver = getWorkReportBusiness().getCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age, report, league);
					leagueStatsData = addToIntegerCount(menOverOrEqualAgeLimit, leagueStatsData, menOver);
					
					//last year stats
//					add to counts
					if(lastYearReport!=null && lastYearLeague!=null){
					  int womenUnderLastYear = getWorkReportBusiness().getCountOfFemalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(age, lastYearReport, lastYearLeague);
					  leagueStatsData = addToIntegerCount(womenUnderAgeLimitLastYear, leagueStatsData, womenUnderLastYear);
	
					  int womenOverLastYear = getWorkReportBusiness().getCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age, lastYearReport, lastYearLeague);
					  leagueStatsData = addToIntegerCount(womenOverOrEqualAgeLimitLastYear, leagueStatsData, womenOverLastYear);
	
					  int menUnderLastYear = getWorkReportBusiness().getCountOfMalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(age, lastYearReport, lastYearLeague);				
					  leagueStatsData = addToIntegerCount(menUnderAgeLimitLastYear, leagueStatsData, menUnderLastYear);
	
					  int menOverLastYear = getWorkReportBusiness().getCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age, lastYearReport, lastYearLeague);
					  leagueStatsData = addToIntegerCount(menOverOrEqualAgeLimitLastYear, leagueStatsData, menOverLastYear);
					}
									  
					//put it back again
					leagueStatsMap.put(leagueKey,leagueStatsData);
					
				}
				
			}
			catch (IDOException e) {
				e.printStackTrace();
			}
			
		}
		
		//add the data to the collection
		reportCollection.addAll(leagueStatsMap.values());
		
		ReportableField[] sortFields = new ReportableField[] {leagueString};
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);
		
		//finished return the collection
		return reportCollection;
	
	}
	
	/*
	 * Report B12.1.5 of the ISI Specs
	 * NOT FINISHED
	 */
	public ReportableCollection getCostPerPlayerStatisticsForLeaguesByYearAgeGenderAndLeaguesFilteringComparedWithLastYear(final Integer year,Integer age,String gender, Collection leaguesFilter)throws RemoteException {
	
		//initialize stuff
		int selectedAge = (age!=null)?age.intValue():-1;
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		
		//PARAMETES
		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				_iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				IWTimestamp.getTimestampRightNow().toGMTString());
		
		//PARAMETERS that are also FIELDS
		//data from entity columns, can also be defined with an entity definition, see getClubMemberStatisticsForRegionalUnions method
		//The name you give the field/parameter must not contain spaces or special characters
		//fake columns (data gotten by business methods)		
		ReportableField leagueString = new ReportableField("league_info", String.class);
		leagueString.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_LEAGUE_INFO, "League"), currentLocale);
		reportCollection.addField(leagueString);
		
		
		ReportableField totalCountOfPlayersForLeague = new ReportableField("totalCountOfPlayersForLeague", Integer.class);
		totalCountOfPlayersForLeague.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.totalCountOfPlayersForLeague", "Players"), currentLocale);
		reportCollection.addField(totalCountOfPlayersForLeague);
		
		ReportableField costPerPlayers = new ReportableField("costPerPlayers", Integer.class);
		costPerPlayers.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.costPerPlayers", "cost/player"),currentLocale);
		reportCollection.addField(costPerPlayers);
		
		ReportableField totalCost = new ReportableField("totalCost", Integer.class);
		totalCost.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.totalCost","Total cost"), currentLocale);
		reportCollection.addField(totalCost);
		
		
		//Real data stuff
		//Gathering data
		//Get all the workreports (actually more than needed)
		//then for each get its leagues and the count for
		//each age and create a row and insert into an ordered map by league
		//then insert into the final report collection.
		Collection clubs = getWorkReportBusiness().getWorkReportsByYearRegionalUnionsAndClubs(year.intValue(), null, null);
		
		Map leagueStatsMap = new TreeMap();
		List leagueGroupIdList = getGroupIdListFromWorkReportGroupCollection(leaguesFilter);
		
		//Iterating through workreports and creating report data 
		Iterator iter = clubs.iterator();
	
		while (iter.hasNext()) {
			//the club
			WorkReport report = (WorkReport) iter.next();
			
			try {
				Collection leagues = report.getLeagues();
				Iterator iterator = leagues.iterator();
				while (iterator.hasNext()) {
					WorkReportGroup league = (WorkReportGroup) iterator.next();
					Integer leagueKey = (Integer) league.getGroupId();//for comparison this must be the same key both years
					
					if (leagueGroupIdList != null && !leagueGroupIdList.contains(league.getGroupId())) {
						continue; //don't process this one, go to next
					}
					
					String leagueIdentifier = getLeagueIdentifier(league);
					//fetch the stats or initialize
					ReportableData leagueStatsData = (ReportableData) leagueStatsMap.get(leagueKey);
					if(leagueStatsData==null){//initialize
						leagueStatsData = new ReportableData();
						//Actually fetching a lot more than needed the layout sums up fields
						leagueStatsData.addData(leagueString, leagueIdentifier);
						leagueStatsData.addData(totalCountOfPlayersForLeague, new Integer(0));
						leagueStatsData.addData(costPerPlayers, new Integer(0));
						leagueStatsData.addData(totalCost,new Integer(0));	
					}
					
					//add to counts
					
					int playerCount = 0;
					//if(age!=null){
						playerCount = getWorkReportBusiness().getCountOfPlayersByWorkReportAndWorkReportGroup(report, league);
						
					//}
					//else{
						
					//}
					leagueStatsData = addToIntegerCount(totalCountOfPlayersForLeague, leagueStatsData, playerCount);
					
					//getWorkReportBusiness().getWorkReportClubAccountRecordHome().findAllRecordsByWorkReportIdAndWorkReportGroupId(report.getP)
									  
					//put it back again
					leagueStatsMap.put(leagueKey,leagueStatsData);
					
				}
				
			}
			catch (IDOException e) {
				e.printStackTrace();
			}
			
		}
		

		//add the data to the collection
		reportCollection.addAll(leagueStatsMap.values());
		
		//finished return the collection
		return reportCollection;
	
	}

	/*
	 * Report B12.2.1 of the ISI Specs
	 */
	public ReportableCollection getStatisticsForRegionalUnionsByYearAndRegionalUnionsFilter (
			final Integer year,
			Collection regionalUnionsFilter)
	throws RemoteException {


		//initialize stuff
		int age = 16;
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();

		//PARAMETES

		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				_iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				IWTimestamp.getTimestampRightNow().toGMTString());

		//PARAMETERS that are also FIELDS
		//data from entity columns, can also be defined with an entity definition, see getClubMemberStatisticsForRegionalUnions method
		//The name you give the field/parameter must not contain spaces or special characters
		/*ReportableField regionalUnionAbbreviation = new ReportableField("regionalUnionAbbrev", String.class);
		regionalUnionAbbreviation.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.regional_union_name_abbrev", "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionAbbreviation);

		ReportableField regionalUnionName = new ReportableField("regionalUnionName", String.class);
		regionalUnionName.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.regional_union_name", "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionName);*/
	
		ReportableField regionalUnionFiffName = new ReportableField("regional_union_name", String.class);
		regionalUnionFiffName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionFiffName);

		ReportableField bothGendersUnderAge = new ReportableField("bothGendersUnderAge", Integer.class);
		bothGendersUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_UNDER+age, "all -"+age), currentLocale);
		reportCollection.addField(bothGendersUnderAge);

		ReportableField bothGendersEqualOverAge = new ReportableField("bothGendersEqualOverAge", Integer.class);
		bothGendersEqualOverAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER+age, "all "+age+"+"), currentLocale);
		reportCollection.addField(bothGendersEqualOverAge);

		//Real data stuff
		//Gathering data
		//Get all the workreports (actually more than needed)
		//then for each get its leagues and the count for
		//each age and create a row and insert into an ordered map by league
		//then iterate the map and insert into the final report collection.
		Collection clubs = getWorkReportBusiness().getWorkReportsByYearRegionalUnionsAndClubs(year.intValue(), regionalUnionsFilter, null);
		Map regionalUnionsStatsMap = new TreeMap();
		//Iterating through workreports and creating report data 
		Iterator iter = clubs.iterator();
		while (iter.hasNext()) {
			//the club
			WorkReport report = (WorkReport) iter.next();

			//String cName = report.getGroupName();
			String regionalUnionIdentifier = getRegionalUnionIdentifier(report);
	
			//fetch the stats or initialize for this regional union (i.e. the one associated with regionalUnionIdentifier)
			ReportableData regData = (ReportableData) regionalUnionsStatsMap.get(regionalUnionIdentifier);
			if(regData==null){//initialize
				regData = new ReportableData();
				regData.addData(regionalUnionFiffName, regionalUnionIdentifier);
				/*String ruAbbrev = report.getRegionalUnionAbbreviation();
				regData.addData(regionalUnionAbbreviation, ruAbbrev==null?"":ruAbbrev);
				String ruName = report.getRegionalUnionName();
				regData.addData(regionalUnionName, ruName==null?"":ruName);*/
				regData.addData(bothGendersUnderAge, new Integer(0));
				regData.addData(bothGendersEqualOverAge, new Integer(0));
			}

			//add to counts
			int membersUnder = getWorkReportBusiness().getCountOfMembersOfYoungerAgeAndByWorkReport(age, report);
			int membersEqualOrOver = getWorkReportBusiness().getCountOfMembersEqualOrOlderThanAgeAndByWorkReport(age, report);
			regData = addToIntegerCount(bothGendersUnderAge, regData, membersUnder);
			regData = addToIntegerCount(bothGendersEqualOverAge, regData, membersEqualOrOver);

			//put it back again
			regionalUnionsStatsMap.put(regionalUnionIdentifier,regData);
		}

		// iterate through the ordered map and ordered lists and add to the final collection
		reportCollection.addAll(regionalUnionsStatsMap.values());

		ReportableField[] sortFields = new ReportableField[] {regionalUnionFiffName};
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);
		
		//finished return the collection
		return reportCollection;
	}
	
	/*
	 * Report B12.2.2 of the ISI Specs
	 */
	public ReportableCollection getGenderStatisticsForRegionalUnionsByYearAndRegionalUnionsFilter (
			final Integer year,
			Collection regionalUnionsFilter)
	throws RemoteException {
		//initialize stuff
		int age = 16;
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();

		//PARAMETES

		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				_iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				IWTimestamp.getTimestampRightNow().toGMTString());

		//PARAMETERS that are also FIELDS
		//data from entity columns, can also be defined with an entity definition, see getClubMemberStatisticsForRegionalUnions method
		//The name you give the field/parameter must not contain spaces or special characters
		/*ReportableField regionalUnionAbbreviation = new ReportableField("regionalUnionAbbrev", String.class);
		regionalUnionAbbreviation.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.regional_union_name_abbrev", "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionAbbreviation);

		ReportableField regionalUnionNumber = new ReportableField("regionalUnionNumber", String.class);
		regionalUnionNumber.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.regional_union_name", "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionNumber);*/
		
		ReportableField regionalUnionFiffName = new ReportableField("regionalUnionName", String.class);
		regionalUnionFiffName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionFiffName);

		ReportableField menUnderAge = new ReportableField("menUnderAge", Integer.class);
		menUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_UNDER+age, "all -"+age), currentLocale);
		reportCollection.addField(menUnderAge);
		
		ReportableField womenUnderAge = new ReportableField("womenUnderAge", Integer.class);
		womenUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER+age, "all -"+age), currentLocale);
		reportCollection.addField(womenUnderAge);

		ReportableField womenOverOrEqualAgeLimit = new ReportableField("womenOverOrEqualAgeLimit", Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
	
		ReportableField menOverOrEqualAgeLimit = new ReportableField("menOverOrEqualAgeLimit", Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL+age, "men "+age+"+"),currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
		
		ReportableField bothGendersLastYear = new ReportableField("bothGendersLastYear", Integer.class);
		bothGendersLastYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_LAST_YEAR, "all "+age+"+ last year"),currentLocale);
		reportCollection.addField(bothGendersLastYear);

		//Real data stuff
		//Gathering data
		//Get all the workreports (actually more than needed)
		//then for each get its leagues and the count for
		//each age and create a row and insert into an ordered map by league
		//then iterate the map and insert into the final report collection.
		Collection clubs = getWorkReportBusiness().getWorkReportsByYearRegionalUnionsAndClubs(year.intValue(), regionalUnionsFilter, null);
		Map regionalUnionsStatsMap = new TreeMap();
		//Iterating through workreports and creating report data 
		Iterator iter = clubs.iterator();
		while (iter.hasNext()) {
			//the club
			WorkReport report = (WorkReport) iter.next();
			
			WorkReport lastYearReport=null;
			try {
				lastYearReport = getWorkReportBusiness().getWorkReportHome().findWorkReportByGroupIdAndYearOfReport(report.getGroupId().intValue(),year.intValue()-1);
			} catch (FinderException e1) {
				System.err.println("WorkReportStatsBusiness : No report for year before :"+year);
			}
				
			//String cName = report.getGroupName();
			String regionalUnionIdentifier = getRegionalUnionIdentifier(report);
		
			//fetch the stats or initialize for this regional union (i.e. the one associated with regionalUnionIdentifier)
			ReportableData regData = (ReportableData) regionalUnionsStatsMap.get(regionalUnionIdentifier);
			if(regData==null){//initialize
				regData = new ReportableData();
				regData.addData(regionalUnionFiffName, regionalUnionIdentifier);
				/*String ruAbbrev = report.getRegionalUnionAbbreviation();
				regData.addData(regionalUnionAbbreviation, ruAbbrev==null?"":ruAbbrev);
				String ruNumber = report.getRegionalUnionNumber();
				regData.addData(regionalUnionNumber, ruNumber==null?"":ruNumber);*/
				
				// @TODO get the population somehow
				//regData.addData(regionalUnionPopulation, "10000");
				regData.addData(womenUnderAge, new Integer(0));
				regData.addData(womenOverOrEqualAgeLimit, new Integer(0));
				regData.addData(menUnderAge, new Integer(0));
				regData.addData(menOverOrEqualAgeLimit, new Integer(0));
				regData.addData(bothGendersLastYear, new Integer(0));
			}
	
			//add to counts
			int womenMembersUnder = getWorkReportBusiness().getCountOfFemaleMembersOfYoungerAgeAndByWorkReport(age, report);
			int womenMembersEqualOrOver = getWorkReportBusiness().getCountOfFemaleMembersEqualOrOlderThanAgeAndByWorkReport(age, report);
			int menMembersUnder = getWorkReportBusiness().getCountOfMaleMembersOfYoungerAgeAndByWorkReport(age, report);
			int menMembersEqualOrOver = getWorkReportBusiness().getCountOfMaleMembersEqualOrOlderThanAgeAndByWorkReport(age, report);
			regData = addToIntegerCount(womenUnderAge, regData, womenMembersUnder);
			regData = addToIntegerCount(womenOverOrEqualAgeLimit, regData, womenMembersEqualOrOver);
			regData = addToIntegerCount(menUnderAge, regData, menMembersUnder);
			regData = addToIntegerCount(menOverOrEqualAgeLimit, regData, menMembersEqualOrOver);
			if(lastYearReport!=null) {
				int lastYearMemberCount = getWorkReportBusiness().getCountOfMembersByWorkReport(lastYearReport);
				regData = addToIntegerCount(bothGendersLastYear, regData, lastYearMemberCount);
			}
			//put it back again
			regionalUnionsStatsMap.put(regionalUnionIdentifier,regData);
		}
	
		// iterate through the ordered map and ordered lists and add to the final collection
		reportCollection.addAll(regionalUnionsStatsMap.values());
		
		ReportableField[] sortFields = new ReportableField[] {regionalUnionFiffName};
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);
		
		//finished return the collection
		return reportCollection;
	}
	
	/*
	 * Report B12.2.3 of the ISI Specs
	*/
	public ReportableCollection getPlayerStatisticsForClubsByYearAndRegionalUnionsFiltering(
				final Integer year,
				Collection regionalUnionsFilter)
				throws RemoteException {
		return getStatisticsForLeaguesByYearRegionalUnionsClubsAndLeaguesFiltering(year, regionalUnionsFilter, null, null);
	}
	
	/*
	 * Report B12.2.4 of the ISI Specs
	 */
	public ReportableCollection getMemberStatisticsForClubsByYearAndRegionalUnionsFilter (
			final Integer year,
			Collection regionalUnionsFilter)
	throws RemoteException {
		//initialize stuff
		int age = 16;
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();

		//PARAMETES

		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				_iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				IWTimestamp.getTimestampRightNow().toGMTString());

		//PARAMETERS that are also FIELDS
		//data from entity columns, can also be defined with an entity definition, see getClubMemberStatisticsForRegionalUnions method
		//The name you give the field/parameter must not contain spaces or special characters
		/*ReportableField regionalUnionAbbreviation = new ReportableField("regionalUnionAbbrev", String.class);
		regionalUnionAbbreviation.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.regional_union_name_abbrev", "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionAbbreviation);

		ReportableField regionalUnionNumber = new ReportableField("regionalUnionNumber", String.class);
		regionalUnionNumber.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.regional_union_name", "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionNumber);*/
	
		ReportableField regionalUnionFiffName = new ReportableField("regionalUnionName", String.class);
		regionalUnionFiffName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionFiffName);
		
		ReportableField clubName = new ReportableField("club_name", String.class);
		clubName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_CLUB_NAME, "Club name"), currentLocale);
		reportCollection.addField(clubName);

		ReportableField menUnderAge = new ReportableField("menUnderAge", Integer.class);
		menUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_UNDER+age, "all -"+age), currentLocale);
		reportCollection.addField(menUnderAge);
	
		ReportableField womenUnderAge = new ReportableField("womenUnderAge", Integer.class);
		womenUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER+age, "all -"+age), currentLocale);
		reportCollection.addField(womenUnderAge);

		ReportableField womenOverOrEqualAgeLimit = new ReportableField("womenOverOrEqualAgeLimit", Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);

		ReportableField menOverOrEqualAgeLimit = new ReportableField("menOverOrEqualAgeLimit", Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL+age, "men "+age+"+"),currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
	
		//Real data stuff
		//Gathering data
		//Get all the workreports (actually more than needed)
		//then for each get its leagues and the count for
		//each age and create a row and insert into an ordered map by league
		//then iterate the map and insert into the final report collection.
		Collection clubs = getWorkReportBusiness().getWorkReportsByYearRegionalUnionsAndClubs(year.intValue(), regionalUnionsFilter, null);
		//Iterating through workreports and creating report data 
		Iterator iter = clubs.iterator();
		while (iter.hasNext()) {
			//the club
			WorkReport report = (WorkReport) iter.next();
		
			String regionalUnionIdentifier = getRegionalUnionIdentifier(report);
			String groupName = report.getGroupName();
			
			ReportableData regData = new ReportableData();
			regData.addData(regionalUnionFiffName, regionalUnionIdentifier);
			regData.addData(clubName, groupName);
			
			int womenMembersUnder = getWorkReportBusiness().getCountOfFemaleMembersOfYoungerAgeAndByWorkReport(age, report);
			int womenMembersEqualOrOver = getWorkReportBusiness().getCountOfFemaleMembersEqualOrOlderThanAgeAndByWorkReport(age, report);
			int menMembersUnder = getWorkReportBusiness().getCountOfMaleMembersOfYoungerAgeAndByWorkReport(age, report);
			int menMembersEqualOrOver = getWorkReportBusiness().getCountOfMaleMembersEqualOrOlderThanAgeAndByWorkReport(age, report);
			
			regData.addData(womenUnderAge, new Integer(womenMembersUnder));
			regData.addData(womenOverOrEqualAgeLimit, new Integer(womenMembersEqualOrOver));
			regData.addData(menUnderAge, new Integer(menMembersUnder));
			regData.addData(menOverOrEqualAgeLimit, new Integer(menMembersEqualOrOver));
			reportCollection.addElement(regData);
		}
	
		ReportableField[] sortFields = new ReportableField[] {regionalUnionFiffName, clubName};
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);
		
		
		//finished return the collection
		return reportCollection;
	}
	
	/*
	 * Report B12.2.5 of the ISI Specs
	 */
	public ReportableCollection getPlayersForRegionalUnionsByYearAndRegionalUnionsFilter (
			final Integer year,
			Collection regionalUnionsFilter)
	throws RemoteException {
		//initialize stuff
		int age = 16;
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();

		//PARAMETES

		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				_iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				IWTimestamp.getTimestampRightNow().toGMTString());

		//PARAMETERS that are also FIELDS
		//data from entity columns, can also be defined with an entity definition, see getClubMemberStatisticsForRegionalUnions method
		//The name you give the field/parameter must not contain spaces or special characters
		/*ReportableField regionalUnionAbbreviation = new ReportableField("regionalUnionAbbrev", String.class);
		regionalUnionAbbreviation.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.regional_union_name_abbrev", "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionAbbreviation);

		ReportableField regionalUnionNumber = new ReportableField("regionalUnionNumber", String.class);
		regionalUnionNumber.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.regional_union_name", "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionNumber);*/
	
		ReportableField regionalUnionFiffName = new ReportableField("regionalUnionName", String.class);
		regionalUnionFiffName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionFiffName);

		/*ReportableField regionalUnionPopulation = new ReportableField("regionalUnionPopulation", String.class);
		regionalUnionPopulation.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.regional_union_population", "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionPopulation);*/
	
		ReportableField menUnderAge = new ReportableField("menUnderAge", Integer.class);
		menUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_UNDER+age, "all -"+age), currentLocale);
		reportCollection.addField(menUnderAge);
	
		ReportableField womenUnderAge = new ReportableField("womenUnderAge", Integer.class);
		womenUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER+age, "all -"+age), currentLocale);
		reportCollection.addField(womenUnderAge);

		ReportableField womenOverOrEqualAgeLimit = new ReportableField("womenOverOrEqualAgeLimit", Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);

		ReportableField menOverOrEqualAgeLimit = new ReportableField("menOverOrEqualAgeLimit", Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
	
		ReportableField bothGendersLastYear = new ReportableField("bothGendersLastYear", Integer.class);
		bothGendersLastYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_LAST_YEAR+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(bothGendersLastYear);

		//Real data stuff
		//Gathering data
		//Get all the workreports (actually more than needed)
		//then for each get its leagues and the count for
		//each age and create a row and insert into an ordered map by league
		//then iterate the map and insert into the final report collection.
		Collection clubs = getWorkReportBusiness().getWorkReportsByYearRegionalUnionsAndClubs(year.intValue(), regionalUnionsFilter, null);
		Map regionalUnionsStatsMap = new TreeMap();
		//Iterating through workreports and creating report data 
		Iterator iter = clubs.iterator();
		while (iter.hasNext()) {
			//the club
			WorkReport report = (WorkReport) iter.next();
		
			WorkReport lastYearReport=null;
			try {
				lastYearReport = getWorkReportBusiness().getWorkReportHome().findWorkReportByGroupIdAndYearOfReport(report.getGroupId().intValue(),year.intValue()-1);
			} catch (FinderException e1) {
				System.err.println("WorkReportStatsBusiness : No report for year before :"+year);
			}
			
			//String cName = report.getGroupName();
			String regionalUnionIdentifier = getRegionalUnionIdentifier(report);
	
			//fetch the stats or initialize for this regional union (i.e. the one associated with regionalUnionIdentifier)
			ReportableData regData = (ReportableData) regionalUnionsStatsMap.get(regionalUnionIdentifier);
			if(regData==null){//initialize
				regData = new ReportableData();
				regData.addData(regionalUnionFiffName, regionalUnionIdentifier);
				/*String ruAbbrev = report.getRegionalUnionAbbreviation();
				regData.addData(regionalUnionAbbreviation, ruAbbrev==null?"":ruAbbrev);
				String ruNumber = report.getRegionalUnionNumber();
				regData.addData(regionalUnionNumber, ruNumber==null?"":ruNumber);*/
			
				// @TODO get the population somehow
				//regData.addData(regionalUnionPopulation, "10000");
				regData.addData(womenUnderAge, new Integer(0));
				regData.addData(womenOverOrEqualAgeLimit, new Integer(0));
				regData.addData(menUnderAge, new Integer(0));
				regData.addData(menOverOrEqualAgeLimit, new Integer(0));
				regData.addData(bothGendersLastYear, new Integer(0));
			}

			//add to counts
			int womenPlayersUnder = getWorkReportBusiness().getCountOfFemalePlayersOfYoungerAgeAndByWorkReport(age, report);
			int womenPlayersEqualOrOver = getWorkReportBusiness().getCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReport(age, report);
			int menPlayersUnder = getWorkReportBusiness().getCountOfMalePlayersOfYoungerAgeAndByWorkReport(age, report);
			int menPlayersEqualOrOver = getWorkReportBusiness().getCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReport(age, report);
			regData = addToIntegerCount(womenUnderAge, regData, womenPlayersUnder);
			regData = addToIntegerCount(womenOverOrEqualAgeLimit, regData, womenPlayersEqualOrOver);
			regData = addToIntegerCount(menUnderAge, regData, menPlayersUnder);
			regData = addToIntegerCount(menOverOrEqualAgeLimit, regData, menPlayersEqualOrOver);
			if(lastYearReport!=null) {
				int lastYearMemberCount = getWorkReportBusiness().getCountOfPlayersByWorkReportAndWorkReportGroup(lastYearReport, null);
				regData = addToIntegerCount(bothGendersLastYear, regData, lastYearMemberCount);
			}
			//put it back again
			regionalUnionsStatsMap.put(regionalUnionIdentifier,regData);
		}

		// iterate through the ordered map and ordered lists and add to the final collection
		reportCollection.addAll(regionalUnionsStatsMap.values());
		
		ReportableField[] sortFields = new ReportableField[] {regionalUnionFiffName};
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);
		
		//finished return the collection
		return reportCollection;
	}
	
	/*
	 * Report B12.2.6 of the ISI Specs
	 */
	public ReportableCollection getStatisticsForLeaguesByYearAndLeaguesAndRegionalUnionsFiltering(final Integer year,Collection regionalUnionsFilter,Collection leaguesFilter)throws RemoteException {

		//initialize stuff
		int age = 16;
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
	
		//PARAMETES
		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				_iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				IWTimestamp.getTimestampRightNow().toGMTString());
	
		//PARAMETERS that are also FIELDS
		//data from entity columns, can also be defined with an entity definition, see getClubMemberStatisticsForRegionalUnions method
		//The name you give the field/parameter must not contain spaces or special characters
	
		//fake columns (data gotten by business methods)
		
		ReportableField regionalUnionFiffName = new ReportableField("regionalUnionName", String.class);
		regionalUnionFiffName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionFiffName);
				
		ReportableField leagueString = new ReportableField("league_info", String.class);
		leagueString.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_LEAGUE_INFO, "League"), currentLocale);
		reportCollection.addField(leagueString);
	
		ReportableField womenUnderAgeLimit = new ReportableField("womenUnderAgeLimit", Integer.class);
		womenUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER+age, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAgeLimit);
	
		ReportableField womenOverOrEqualAgeLimit = new ReportableField("womenOverOrEqualAgeLimit", Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
	
		ReportableField menUnderAgeLimit = new ReportableField("menUnderAgeLimit", Integer.class);
		menUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_UNDER+age, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAgeLimit);
	
		ReportableField menOverOrEqualAgeLimit = new ReportableField("menOverOrEqualAgeLimit", Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL+age, "men "+age+"+"), currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
		
		//Real data stuff
		//Gathering data
		//Get all the workreports (actually more than needed)
		//then for each get its leagues and the count for
		//each age and create a row and insert into an ordered map by league
		//then insert into the final report collection.
		Collection clubs = getWorkReportBusiness().getWorkReportsByYearRegionalUnionsAndClubs(year.intValue(), regionalUnionsFilter, null);
		Map leagueStatsMap = new TreeMap();
		List leagueGroupIdList = getGroupIdListFromWorkReportGroupCollection(leaguesFilter);
	
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
					Integer leagueKey = (Integer) league.getPrimaryKey();
				
					if (leagueGroupIdList != null && !leagueGroupIdList.contains(league.getGroupId())) {
						continue; //don't process this one, go to next
					}
				
					String leagueIdentifier = getLeagueIdentifier(league);
					//fetch the stats or initialize
					ReportableData leagueStatsData = (ReportableData) leagueStatsMap.get(leagueKey);
					if(leagueStatsData==null){//initialize
						leagueStatsData = new ReportableData();
						leagueStatsData.addData(regionalUnionFiffName, regionalUnionIdentifier);
						leagueStatsData.addData(leagueString, leagueIdentifier);
						leagueStatsData.addData(womenUnderAgeLimit, new Integer(0));
						leagueStatsData.addData(womenOverOrEqualAgeLimit, new Integer(0));
						leagueStatsData.addData(menUnderAgeLimit,new Integer(0));
						leagueStatsData.addData(menOverOrEqualAgeLimit, new Integer(0));
					}
				
					//add to counts
					int womenUnder = getWorkReportBusiness().getCountOfFemalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(age, report, league);
					leagueStatsData = addToIntegerCount(womenUnderAgeLimit, leagueStatsData, womenUnder);
				
					int womenOver = getWorkReportBusiness().getCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age, report, league);
					leagueStatsData = addToIntegerCount(womenOverOrEqualAgeLimit, leagueStatsData, womenOver);
				
					int menUnder = getWorkReportBusiness().getCountOfMalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(age, report, league);				
					leagueStatsData = addToIntegerCount(menUnderAgeLimit, leagueStatsData, menUnder);
				
					int menOver = getWorkReportBusiness().getCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age, report, league);
					leagueStatsData = addToIntegerCount(menOverOrEqualAgeLimit, leagueStatsData, menOver);
				
					//put it back again
					leagueStatsMap.put(leagueKey,leagueStatsData);
				
				}
			
			}
			catch (IDOException e) {
				e.printStackTrace();
			}
		
		}
	
		//add the data to the collection
		reportCollection.addAll(leagueStatsMap.values());

		ReportableField[] sortFields = new ReportableField[] {regionalUnionFiffName, leagueString};
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);
	
		//finished return the collection
		return reportCollection;

	}

private List getGroupIdListFromWorkReportGroupCollection(Collection leaguesFilter) {
	List leagueGroupIdList = null;
	if (leaguesFilter != null && !leaguesFilter.isEmpty()) {
		leagueGroupIdList = new Vector();
		Iterator iter = leaguesFilter.iterator();
		while (iter.hasNext()) {
			Group group = (Group) iter.next();
			leagueGroupIdList.add(group.getPrimaryKey());
		}
		
	}
	return leagueGroupIdList;
}

private ReportableData addToIntegerCount(ReportableField reportableField, ReportableData reportableData, int intToAdd) {
	if(intToAdd>0 && reportableData!=null){//update count
		Integer count = (Integer)reportableData.getFieldValue(reportableField);
		if(count!=null) {
			count = new Integer(count.intValue()+intToAdd);
		} else {
			count = new Integer(0);
		}
		reportableData.addData(reportableField,count);//swap
	}
	
	return reportableData;
}

private String getLeagueIdentifier(WorkReportGroup league) {
	//for the page separations
	StringBuffer leagueBuf = new StringBuffer();
	leagueBuf.append( (league.getNumber()!=null)? league.getNumber() : "" )
	.append("  ")
	.append( (league.getShortName()!=null)? league.getShortName() : "");
	//.append("  ")
	//.append( (league.getName()!=null)? league.getName() : "");
	String leagueText=league.toString();
	return leagueText;
}

private String getRegionalUnionIdentifier(WorkReport report) {
	StringBuffer ruBuf = new StringBuffer();
	ruBuf.append( (report.getRegionalUnionNumber()!=null)? report.getRegionalUnionNumber() : "" )
	.append("  ")
	.append( (report.getRegionalUnionAbbreviation()!=null)? report.getRegionalUnionAbbreviation() : "");
	//.append("  ")
	//.append( (report.getRegionalUnionName()!=null)? report.getRegionalUnionName() : "");
	return ruBuf.toString();
}

}
