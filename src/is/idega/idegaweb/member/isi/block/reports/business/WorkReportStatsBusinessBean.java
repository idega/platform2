package is.idega.idegaweb.member.isi.block.reports.business;

import is.idega.idegaweb.member.isi.block.reports.data.WorkReport;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportDivisionBoard;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportDivisionBoardHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup;
import is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler.ClubTypeDropDownMenu;
import is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler.WorkReportStatusDropDownMenu;
import is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler.YesNoDropDownMenu;
import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;
import is.idega.idegaweb.member.util.IWMemberConstants;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.ejb.FinderException;

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
	private static final String LOCALIZED_CLUB_NUMBER = "WorkReportStatsBusiness.club_number";
	private static final String LOCALIZED_CLUB_TYPE = "WorkReportStatsBusiness.club_type";
	private static final String LOCALIZED_REGIONAL_UNION_NAME = "WorkReportStatsBusiness.regional_union_name";
	private static final String LOCALIZED_LEAGUE_INFO = "WorkReportStatsBusiness.league_info";
	private static final String LOCALIZED_WOMEN_UNDER = "WorkReportStatsBusiness.womenUnderAgeLimit_";
	private static final String LOCALIZED_WOMEN_OVER_OR_EQUAL = "WorkReportStatsBusiness.womenOverOrEqualAgeLimit_";
	private static final String LOCALIZED_MEN_UNDER = "WorkReportStatsBusiness.menUnderAgeLimit_";
	private static final String LOCALIZED_MEN_OVER_OR_EQUAL = "WorkReportStatsBusiness.menOverOrEqualAgeLimit_";
	private static final String LOCALIZED_ALL_UNDER = "WorkReportStatsBusiness.bothGendersUnderAge_";
	private static final String LOCALIZED_ALL_EQUAL_OR_OVER = "WorkReportStatsBusiness.bothGendersEqualOverAge_";
	private static final String LOCALIZED_ALL = "WorkReportStatsBusiness.bothGenders";
	private static final String LOCALIZED_ALL_LAST_YEAR = "WorkReportStatsBusiness.bothGendersLastYear";
	private static final String LOCALIZED_IS_IN_UMFI = "WorkReportStatsBusiness.isInUMFI";
	private static final String LOCALIZED_CLUB_TYPE_MULTI_DIVISION = "WorkReportStatsBusiness.club_type_multi_division";
	private static final String LOCALIZED_CLUB_TYPE_SINGLE_DIVISION = "WorkReportStatsBusiness.club_type_single_division";
	private static final String LOCALIZED_CLUB_TYPE_NO_MEMBERS = "WorkReportStatsBusiness.club_type_no_members";
	private static final String LOCALIZED_CLUB_TYPE_IS_INACTIVE = "WorkReportStatsBusiness.club_type_inactive";
	private static final String LOCALIZED_CLUB_TYPE_IM_UMFI = "WorkReportStatsBusiness.club_type_in_umfi";
	private static final String LOCALIZED_YES = "WorkReportStatsBusiness.yes";
	private static final String LOCALIZED_WORK_REPORT_STATUS = "WorkReportStatsBusiness.workreport_status";
	private static final String LOCALIZED_WORK_REPORT_STATUS_INFO = "WorkReportStatsBusiness.workreport_info";
	private static final String LOCALIZED_WORK_REPORT_STATUS_REMARKS = "WorkReportStatsBusiness.workreport_remarks";
	private static final String LOCALIZED_DIVISION_NAME = "WorkReportStatsBusiness.division_name";
	private static final String LOCALIZED_ANNUAL_REPORT_STATUS = "WorkReportStatsBusiness.annual_report_status";
	private static final String LOCALIZED_ANNUAL_REPORT_IN = "WorkReportStatsBusiness.annual_report_in";
	private static final String LOCALIZED_ANNUAL_REPORT_MISSING = "WorkReportStatsBusiness.annual_report_missing";
	
	// names of reportable fields
	private static final String FIELD_NAME_COMPARING_YEAR = "comparing_year";
	private static final String FIELD_NAME_CLUB_NAME = "club_name";
	private static final String FIELD_NAME_CLUB_NUMBER = "club_number";
	private static final String FIELD_NAME_CLUB_TYPE = "club_type";
	private static final String FIELD_NAME_REGIONAL_UNION_NAME = "regional_union_name";
	private static final String FIELD_NAME_LEAGUE_NAME = "league_info";
	private static final String FIELD_NAME_WOMEN_UNDER_AGE = "womenUnderAgeLimit";
	private static final String FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE = "womenOverOrEqualAgeLimit";
	private static final String FIELD_NAME_MEN_UNDER_AGE = "menUnderAgeLimit";
	private static final String FIELD_NAME_MEN_OVER_OR_EQUAL_AGE = "menOverOrEqualAgeLimit";
	private static final String FIELD_NAME_ALL_UNDER_AGE = "bothGendersUnderAge";
	private static final String FIELD_NAME_ALL_OVER_OR_EQUAL_AGE = "bothGendersEqualOverAge";
	private static final String FIELD_NAME_ALL_AGES = "bothGendersAllAge";
	
	private static final String FIELD_NAME_WOMEN_MEMBERS_UNDER_AGE = "womenMembersUnderAgeLimit";
	private static final String FIELD_NAME_WOMEN_MEMBERS_OVER_OR_EQUAL_AGE = "womenMembersOverOrEqualAgeLimit";
	private static final String FIELD_NAME_MEN_MEMBERS_UNDER_AGE = "menMembersUnderAgeLimit";
	private static final String FIELD_NAME_MEN_MEMBERS_OVER_OR_EQUAL_AGE = "menMembersOverOrEqualAgeLimit";
	private static final String FIELD_NAME_ALL_MEMBERS_UNDER_AGE = "bothGendersMembersUnderAge";
	private static final String FIELD_NAME_ALL_MEMBERS_OVER_OR_EQUAL_AGE = "bothGendersMembersEqualOverAge";
	private static final String FIELD_NAME_ALL_MEMBERS_AGES = "bothGendersMembersAllAge";
	
	private static final String FIELD_NAME_WOMEN_UNDER_AGE_LAST_YEAR = "womenUnderAgeLimitLastYear";
	private static final String FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE_LAST_YEAR = "womenOverOrEqualAgeLimitLastYear";
	private static final String FIELD_NAME_MEN_UNDER_AGE_LAST_YEAR = "menUnderAgeLimitLastYear";
	private static final String FIELD_NAME_MEN_OVER_OR_EQUAL_AGE_LAST_YEAR = "menOverOrEqualAgeLimitLastYear";
	private static final String FIELD_NAME_ALL_UNDER_AGE_LAST_YEAR = "bothGendersUnderAgeLastYear";
	private static final String FIELD_NAME_ALL_OVER_OR_EQUAL_AGE_LAST_YEAR = "bothGendersEqualOverAgeLastYear";
	private static final String FIELD_NAME_ALL_AGES_LAST_YEAR = "bothGendersLastYear";
	private static final String FIELD_NAME_IS_IN_UMFI = "isInUMFI";
	private static final String FIELD_NAME_WORK_REPORT_STATUS = "workreport_status";
	private static final String FIELD_NAME_WORK_REPORT_STATUS_INFO = "workreport_info";
	private static final String FIELD_NAME_WORK_REPORT_STATUS_REMARKS = "workreport_remarks";
	private static final String FIELD_NAME_DIVISION_NAME = "division_name";
	private static final String FIELD_NAME_ANNUAL_REPORT_STATUS = "annual_report_status";


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
		ReportableField comparingYearStat = new ReportableField(FIELD_NAME_COMPARING_YEAR, Integer.class) {
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
		final ReportableField clubName = new ReportableField(FIELD_NAME_CLUB_NAME, String.class);
		clubName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_CLUB_NAME, "Club name"), currentLocale);
		reportCollection.addField(clubName);
		
		final ReportableField regionalUnionAbbreviation = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionAbbreviation.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionAbbreviation);
		
		//fake columns (data gotten by business methods)
		final ReportableField leagueString = new ReportableField(FIELD_NAME_LEAGUE_NAME, String.class);
		leagueString.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_LEAGUE_INFO, "League"), currentLocale);
		reportCollection.addField(leagueString);
		
		ReportableField womenUnderAgeLimit = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE, Integer.class);
		womenUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER + age, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAgeLimit);
		
		ReportableField womenOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL + age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
		
		ReportableField menUnderAgeLimit = new ReportableField(FIELD_NAME_MEN_UNDER_AGE, Integer.class);
		menUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_UNDER + age, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAgeLimit);
		
		ReportableField menOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL + age, "men "+age+"+"), currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
		
		
		ReportableField bothGendersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_UNDER + age, "all -"+age), currentLocale);
		reportCollection.addField(bothGendersUnderAge);
		
		ReportableField bothGendersEqualOverAge = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
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
		
		List leagueGroupIdList = getGroupIdListFromLeagueGroupCollection(year, leaguesFilter, false);
		
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
			String regUniIdentifier = this.getRegionalUnionIdentifier(report);
			
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
					
					if(lastYearReport!=null){//no point if it is
						WorkReportGroup lastYearLeague=null;
						try {
							lastYearLeague = getWorkReportBusiness().getWorkReportGroupHome().findWorkReportGroupByGroupIdAndYear(league.getGroupId().intValue(),year.intValue()-1);
			
							Integer lastYear = new Integer(getWorkReportBusiness().getCountOfPlayersByWorkReportAndWorkReportGroup(lastYearReport,lastYearLeague));
							data.addData(comparingYearStat,lastYear);
						}
						catch (FinderException e2) {
							System.err.println("WorkReportStatsBusiness : No report league for year before :"+year);
							data.addData(comparingYearStat,new Integer(0));
						}
					}
					else{
						data.addData(comparingYearStat,new Integer(0));
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

	private List getGroupIdListFromLeagueGroupCollection(final Integer year, Collection leaguesFilter, boolean returnWithMainBoard) throws RemoteException {
		//Don't display the main board
		WorkReportGroup mainBoard = getWorkReportBusiness().getMainBoardWorkReportGroup(year.intValue());
		Integer mainGroupId = mainBoard.getGroupId();
		
		List leagueGroupIdList = null;
		if (leaguesFilter != null && !leaguesFilter.isEmpty()) {
			leagueGroupIdList = new Vector();
			Iterator iter = leaguesFilter.iterator();
			while (iter.hasNext()) {
				Group group = (Group) iter.next();

				if( returnWithMainBoard ){
					leagueGroupIdList.add(group.getPrimaryKey());
				}
				else{
					if(!((Integer)group.getPrimaryKey()).equals(mainGroupId) ){
						leagueGroupIdList.add(group.getPrimaryKey());
					}
				}
			}
		}
		else{//because we need to remove ADA the main board
			Collection wrGroups = getWorkReportBusiness().getAllLeagueWorkReportGroupsForYear(year.intValue());
			leagueGroupIdList = new Vector();
			Iterator iter = wrGroups.iterator();
			while (iter.hasNext()) {
				WorkReportGroup wrGroup = (WorkReportGroup) iter.next();
		
				if( returnWithMainBoard ){
					leagueGroupIdList.add(wrGroup.getGroupId());
				}
				else{
					if(!((Integer)wrGroup.getGroupId()).equals(mainGroupId) ){
						leagueGroupIdList.add(wrGroup.getGroupId());
					}
				}
			}
		}
		return leagueGroupIdList;
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
		ReportableField regionalUnionAbbreviation = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionAbbreviation.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionAbbreviation);
		
		//fake columns (data gotten by business methods)
		ReportableField leagueString = new ReportableField(FIELD_NAME_LEAGUE_NAME, String.class);
		leagueString.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_LEAGUE_INFO, "League"), currentLocale);
		reportCollection.addField(leagueString);
		
		ReportableField womenUnderAgeLimit = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE, Integer.class);
		womenUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER+age, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAgeLimit);
		
		ReportableField womenOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
		
		ReportableField menUnderAgeLimit = new ReportableField(FIELD_NAME_MEN_UNDER_AGE, Integer.class);
		menUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_UNDER+age, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAgeLimit);
		
		ReportableField menOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL+age, "men "+age+"+"), currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
		
		ReportableField bothGendersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_UNDER+age, "all -"+age), currentLocale);
		reportCollection.addField(bothGendersUnderAge);
		
		ReportableField bothGendersEqualOverAge = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
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
		ReportableField leagueString = new ReportableField(FIELD_NAME_LEAGUE_NAME, String.class);
		leagueString.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_LEAGUE_INFO, "League"), currentLocale);
		reportCollection.addField(leagueString);
		
		ReportableField womenUnderAgeLimit = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE, Integer.class);
		womenUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER+age, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAgeLimit);
		
		ReportableField womenOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
		
		ReportableField menUnderAgeLimit = new ReportableField(FIELD_NAME_MEN_UNDER_AGE, Integer.class);
		menUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_UNDER+age, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAgeLimit);
		
		ReportableField menOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL+age, "men "+age+"+"), currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
		
		ReportableField bothGendersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_UNDER+age, "all -"+age), currentLocale);
		reportCollection.addField(bothGendersUnderAge);
		
		ReportableField bothGendersEqualOverAge = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
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
		List leagueGroupIdList = getGroupIdListFromLeagueGroupCollection(year,leaguesFilter,false);
		
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
					
					if (!leagueGroupIdList.contains(league.getGroupId())) {
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
		ReportableField comparingYearStat = new ReportableField(FIELD_NAME_COMPARING_YEAR, Integer.class) {
			public String getLocalizedName(Locale locale) {
				return Integer.toString(year.intValue() - 1);
			}
		};
		reportCollection.addField(comparingYearStat);
		
		ReportableField leagueString = new ReportableField(FIELD_NAME_LEAGUE_NAME, String.class);
		leagueString.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_LEAGUE_INFO, "League"), currentLocale);
		reportCollection.addField(leagueString);
		
		//Selected years parameters and fields
		ReportableField womenUnderAgeLimit = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE, Integer.class);
		womenUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER+age, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAgeLimit);
		
		ReportableField womenOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
		
		ReportableField menUnderAgeLimit = new ReportableField(FIELD_NAME_MEN_UNDER_AGE, Integer.class);
		menUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_UNDER+age, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAgeLimit);
		
		ReportableField menOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL+age, "men "+age+"+"), currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
		
		ReportableField bothGendersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_UNDER+age, "all -"+age), currentLocale);
		reportCollection.addField(bothGendersUnderAge);
		
		ReportableField bothGendersEqualOverAge = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
		bothGendersEqualOverAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER+age, "all "+age+"+"), currentLocale);
		reportCollection.addField(bothGendersEqualOverAge);
		
		//last years parameters and fields
		ReportableField womenUnderAgeLimitLastYear = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE_LAST_YEAR, Integer.class);
		womenUnderAgeLimitLastYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER+age, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAgeLimitLastYear);
		
		ReportableField womenOverOrEqualAgeLimitLastYear = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE_LAST_YEAR, Integer.class);
		womenOverOrEqualAgeLimitLastYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimitLastYear);
		
		ReportableField menUnderAgeLimitLastYear = new ReportableField(FIELD_NAME_MEN_UNDER_AGE_LAST_YEAR, Integer.class);
		menUnderAgeLimitLastYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_UNDER+age, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAgeLimitLastYear);
		
		ReportableField menOverOrEqualAgeLimitLastYear = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE_LAST_YEAR, Integer.class);
		menOverOrEqualAgeLimitLastYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL+age, "men "+age+"+"), currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimitLastYear);
		
		ReportableField bothGendersUnderAgeLastYear = new ReportableField(FIELD_NAME_ALL_UNDER_AGE_LAST_YEAR, Integer.class);
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
		List leagueGroupIdList = getGroupIdListFromLeagueGroupCollection(year,leaguesFilter,false);
		
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
					
					if (!leagueGroupIdList.contains(league.getGroupId())) {
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
	public ReportableCollection getCostPerPlayerStatisticsForLeaguesByYearAgeGenderAndLeaguesFiltering(final Integer year,Integer age,String gender, Collection leaguesFilter)throws RemoteException {
	
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
		ReportableField leagueString = new ReportableField(FIELD_NAME_LEAGUE_NAME, String.class);
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
		List leagueGroupIdList = getGroupIdListFromLeagueGroupCollection(year,leaguesFilter,false);
		
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
					
					if (!leagueGroupIdList.contains(league.getGroupId())) {
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
					
					int totalPlayerCount = 0;
					int cost = 0;
					
					totalPlayerCount = getWorkReportBusiness().getCountOfPlayersByWorkReportAndWorkReportGroup(report, league);
					leagueStatsData = addToIntegerCount(totalCountOfPlayersForLeague, leagueStatsData, totalPlayerCount);
					cost = getWorkReportBusiness().getWorkReportExpensesByWorkReportIdAndWorkReportGroupId(((Integer)report.getPrimaryKey()).intValue(),((Integer)league.getPrimaryKey()).intValue());
					leagueStatsData = addToIntegerCount(totalCost,leagueStatsData,cost);
					
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
		
		Iterator data = reportCollection.iterator();
		while (data.hasNext()) {
			ReportableData row = (ReportableData) data.next();
			Integer totalCostForleague = (Integer)row.getFieldValue(totalCost);
			Integer totalCountOfPlayers = (Integer)row.getFieldValue(totalCountOfPlayersForLeague);
			if(totalCountOfPlayers.intValue()!=0){
				Integer costPerPlayer = new Integer(totalCostForleague.intValue()/totalCountOfPlayers.intValue());
				row.addData(costPerPlayers,costPerPlayer);
			}
			
			
			
		}
		
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

		ReportableField regionalUnionName = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionName);

		ReportableField bothGendersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_UNDER+age, "all -"+age), currentLocale);
		reportCollection.addField(bothGendersUnderAge);

		ReportableField bothGendersEqualOverAge = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
		bothGendersEqualOverAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER+age, "all "+age+"+"), currentLocale);
		reportCollection.addField(bothGendersEqualOverAge);
		
		ReportableField bothGendersAllAge = new ReportableField(FIELD_NAME_ALL_AGES, Integer.class);
		bothGendersAllAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL, "all"), currentLocale);
		reportCollection.addField(bothGendersAllAge);

		//Real data stuff
		//Gathering data
		//Get all the workreports (actually more than needed)
		//then for each get its leagues and the count for
		//each age and create a row and insert into an ordered map by league
		//then iterate the map and insert into the final report collection.
		Collection clubs = getWorkReportBusiness().getWorkReportsForRegionalUnionCollection(year.intValue(), regionalUnionsFilter);
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
				regData.addData(regionalUnionName, regionalUnionIdentifier);
				/*String ruAbbrev = report.getRegionalUnionAbbreviation();
				regData.addData(regionalUnionAbbreviation, ruAbbrev==null?"":ruAbbrev);
				String ruName = report.getRegionalUnionName();
				regData.addData(regionalUnionName, ruName==null?"":ruName);*/
				regData.addData(bothGendersUnderAge, new Integer(0));
				regData.addData(bothGendersEqualOverAge, new Integer(0));
				regData.addData(bothGendersAllAge, new Integer(0));
			}

			//add to counts
			int membersUnder = getWorkReportBusiness().getCountOfMembersOfYoungerAgeAndByWorkReport(age, report);
			int membersEqualOrOver = getWorkReportBusiness().getCountOfMembersEqualOrOlderThanAgeAndByWorkReport(age, report);
			regData = addToIntegerCount(bothGendersUnderAge, regData, membersUnder);
			regData = addToIntegerCount(bothGendersEqualOverAge, regData, membersEqualOrOver);
			regData = addToIntegerCount(bothGendersAllAge, regData, membersEqualOrOver + membersUnder);

			//put it back again
			regionalUnionsStatsMap.put(regionalUnionIdentifier,regData);
		}

		// iterate through the ordered map and ordered lists and add to the final collection
		reportCollection.addAll(regionalUnionsStatsMap.values());

		ReportableField[] sortFields = new ReportableField[] {regionalUnionName};
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
		
		ReportableField regionalUnionName = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionName);
		
		ReportableField menUnderAge = new ReportableField("menUnderAge", Integer.class);
		menUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_UNDER+age, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAge);
		
		ReportableField womenUnderAge = new ReportableField("womenUnderAge", Integer.class);
		womenUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER+age, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAge);
		
		ReportableField womenOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
		
		ReportableField menOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL+age, "men "+age+"+"),currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
		
		ReportableField bothGendersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_UNDER+age, "all -"+age),currentLocale);
		reportCollection.addField(bothGendersUnderAge);
		
		ReportableField bothGendersEqualOrOver = new ReportableField("bothGendersEqualOrOverAge", Integer.class);
		bothGendersEqualOrOver.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER+age, "all "+age+"+"),currentLocale);
		reportCollection.addField(bothGendersEqualOrOver);
		
		ReportableField bothGendersAllAge = new ReportableField(FIELD_NAME_ALL_AGES, Integer.class);
		bothGendersAllAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL, "all"), currentLocale);
		reportCollection.addField(bothGendersAllAge);
		
		ReportableField bothGendersLastYear = new ReportableField(FIELD_NAME_ALL_AGES_LAST_YEAR, Integer.class);
		bothGendersLastYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_LAST_YEAR, "all "+age+"+ last year"),currentLocale);
		reportCollection.addField(bothGendersLastYear);
		
		//Real data stuff
		//Gathering data
		//Get all the workreports (actually more than needed)
		//then for each get its leagues and the count for
		//each age and create a row and insert into an ordered map by league
		//then iterate the map and insert into the final report collection.
		Collection clubs = getWorkReportBusiness().getWorkReportsForRegionalUnionCollection(year.intValue(), regionalUnionsFilter);
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
				regionalUnionsStatsMap.put(regionalUnionIdentifier,regData);
				regData.addData(regionalUnionName, regionalUnionIdentifier);
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
				regData.addData(bothGendersAllAge, new Integer(0));
				regData.addData(bothGendersEqualOrOver, new Integer(0));
				regData.addData(bothGendersUnderAge, new Integer(0));
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
			regData = addToIntegerCount(bothGendersEqualOrOver, regData, menMembersEqualOrOver + womenMembersEqualOrOver);
			regData = addToIntegerCount(bothGendersUnderAge, regData, menMembersUnder + womenMembersUnder);
			regData = addToIntegerCount(bothGendersAllAge, regData, menMembersUnder + womenMembersUnder + menMembersEqualOrOver + womenMembersEqualOrOver);
			if(lastYearReport!=null) {
				int lastYearMemberCount = getWorkReportBusiness().getCountOfMembersByWorkReport(lastYearReport);
				regData = addToIntegerCount(bothGendersLastYear, regData, lastYearMemberCount);
			}
		}
	
		// iterate through the ordered map and ordered lists and add to the final collection
		reportCollection.addAll(regionalUnionsStatsMap.values());
		
		ReportableField[] sortFields = new ReportableField[] {regionalUnionName};
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

		//initialize stuff
		int age = 16;
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();

		//PARAMETES

		//A way to set a static parameter
		ReportableField comparingYearStat = new ReportableField(FIELD_NAME_COMPARING_YEAR, Integer.class) {
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
		final ReportableField clubName = new ReportableField(FIELD_NAME_CLUB_NAME, String.class);
		clubName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_CLUB_NAME, "Club name"), currentLocale);
		reportCollection.addField(clubName);

		final ReportableField regionalUnionAbbreviation = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionAbbreviation.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionAbbreviation);

		//fake columns (data gotten by business methods)

		ReportableField womenUnderAgeLimit = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE, Integer.class);
		womenUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER + age, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAgeLimit);

		ReportableField womenOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL + age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);

		ReportableField menUnderAgeLimit = new ReportableField(FIELD_NAME_MEN_UNDER_AGE, Integer.class);
		menUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_UNDER + age, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAgeLimit);

		ReportableField menOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL + age, "men "+age+"+"), currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);

		ReportableField bothGendersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_UNDER + age, "all -"+age), currentLocale);
		reportCollection.addField(bothGendersUnderAge);
	
		ReportableField bothGendersEqualOrOver = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
		bothGendersEqualOrOver.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER+age, "all "+age+"+"),currentLocale);
		reportCollection.addField(bothGendersEqualOrOver);

		ReportableField bothGendersAllAge = new ReportableField(FIELD_NAME_ALL_AGES, Integer.class);
		bothGendersAllAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL, "all"), currentLocale);
		reportCollection.addField(bothGendersAllAge);

		//Real data stuff
		//Gathering data
		//Get all the workreports (actually more than needed)
		//then for each get its leagues and the count for
		//each age and create a row and insert into an ordered map by league
		//then iterate the map and insert into the final report collection.
		Collection clubs = getWorkReportBusiness().getWorkReportsForRegionalUnionCollection(year.intValue(), regionalUnionsFilter);

		Map clubMap = new TreeMap();
		//Iterating through workreports and creating report data 
		Iterator iter = clubs.iterator();
		while (iter.hasNext()) {
			WorkReport report = (WorkReport) iter.next();
			//get last years report for comparison

			String cName = report.getGroupName();
			if(cName==null) {
				System.out.println("Skipping club cause name is null");
				continue;
			}
			System.out.println("Procesing club \"" + cName + "\".");
			String regUniIdentifier = getRegionalUnionIdentifier(report);

			ReportableData regData = (ReportableData) clubMap.get(cName);
			// create a new ReportData for each row
			if(regData==null) {
				regData = new ReportableData();
				regData.addData(clubName, cName);
				regData.addData(regionalUnionAbbreviation, regUniIdentifier);
				regData.addData(womenUnderAgeLimit, new Integer(0));
				regData.addData(womenOverOrEqualAgeLimit, new Integer(0));
				regData.addData(menUnderAgeLimit, new Integer(0));
				regData.addData(menOverOrEqualAgeLimit, new Integer(0));
				regData.addData(bothGendersEqualOrOver, new Integer(0));
				regData.addData(bothGendersUnderAge, new Integer(0));
				regData.addData(bothGendersAllAge, new Integer(0));

				clubMap.put(cName, regData);
			}
			// add the data to the correct fields/columns

			try {
				Collection leagues = report.getLeagues();
				Iterator iterator = leagues.iterator();
				while (iterator.hasNext()) {
					WorkReportGroup league = (WorkReportGroup) iterator.next();

					int womenPlayersUnder = getWorkReportBusiness().getCountOfFemalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(age, report, league);
					int womenPlayersEqualOrOver = getWorkReportBusiness().getCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age, report, league);
					int menPlayersUnder = getWorkReportBusiness().getCountOfMalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(age, report, league);
					int menPlayersEqualOrOver = getWorkReportBusiness().getCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age, report, league);
					regData = addToIntegerCount(womenUnderAgeLimit, regData, womenPlayersUnder);
					regData = addToIntegerCount(womenOverOrEqualAgeLimit, regData, womenPlayersEqualOrOver);
					regData = addToIntegerCount(menUnderAgeLimit, regData, menPlayersUnder);
					regData = addToIntegerCount(menOverOrEqualAgeLimit, regData, menPlayersEqualOrOver);
					regData = addToIntegerCount(bothGendersEqualOrOver, regData, menPlayersEqualOrOver + womenPlayersEqualOrOver);
					regData = addToIntegerCount(bothGendersUnderAge, regData, menPlayersUnder + womenPlayersUnder);
					regData = addToIntegerCount(bothGendersAllAge, regData, menPlayersUnder + womenPlayersUnder + menPlayersEqualOrOver + womenPlayersEqualOrOver);
				}
			}
			catch (IDOException e) {
				e.printStackTrace();
			}
		}

		// iterate through the ordered map and ordered lists and add to the final collection
		reportCollection.addAll(clubMap.values());

		ReportableField[] sortFields = new ReportableField[] {regionalUnionAbbreviation, clubName};
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);

		// finished return the collection
		return reportCollection;
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

		ReportableField regionalUnionName = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionName);

		ReportableField clubName = new ReportableField(FIELD_NAME_CLUB_NAME, String.class);
		clubName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_CLUB_NAME, "Club name"), currentLocale);
		reportCollection.addField(clubName);

		ReportableField menUnderAge = new ReportableField(FIELD_NAME_MEN_UNDER_AGE, Integer.class);
		menUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_UNDER+age, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAge);

		ReportableField womenUnderAge = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE, Integer.class);
		womenUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER+age, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAge);

		ReportableField womenOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);

		ReportableField menOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL+age, "men "+age+"+"),currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);

		ReportableField bothGendersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_UNDER + age, "all -"+age), currentLocale);
		reportCollection.addField(bothGendersUnderAge);

		ReportableField bothGendersEqualOrOverAge = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
		bothGendersEqualOrOverAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER + age, "all "+age+"+"), currentLocale);
		reportCollection.addField(bothGendersEqualOrOverAge);

		ReportableField bothGendersAllAge = new ReportableField(FIELD_NAME_ALL_AGES, Integer.class);
		bothGendersAllAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL, "all "), currentLocale);
		reportCollection.addField(bothGendersAllAge);

		//Real data stuff
		//Gathering data
		//Get all the workreports (actually more than needed)
		//then for each get its leagues and the count for
		//each age and create a row and insert into an ordered map by league
		//then iterate the map and insert into the final report collection.
		Collection clubs = getWorkReportBusiness().getWorkReportsForRegionalUnionCollection(year.intValue(), regionalUnionsFilter);
		//Iterating through workreports and creating report data 
		Iterator iter = clubs.iterator();
		while (iter.hasNext()) {
			//the club
			WorkReport report = (WorkReport) iter.next();

			String regionalUnionIdentifier = getRegionalUnionIdentifier(report);
			String groupName = report.getGroupName();

			ReportableData regData = new ReportableData();
			regData.addData(regionalUnionName, regionalUnionIdentifier);
			regData.addData(clubName, groupName);

			int womenMembersUnder = getWorkReportBusiness().getCountOfFemaleMembersOfYoungerAgeAndByWorkReport(age, report);
			int womenMembersEqualOrOver = getWorkReportBusiness().getCountOfFemaleMembersEqualOrOlderThanAgeAndByWorkReport(age, report);
			int menMembersUnder = getWorkReportBusiness().getCountOfMaleMembersOfYoungerAgeAndByWorkReport(age, report);
			int menMembersEqualOrOver = getWorkReportBusiness().getCountOfMaleMembersEqualOrOlderThanAgeAndByWorkReport(age, report);

			regData.addData(womenUnderAge, new Integer(womenMembersUnder));
			regData.addData(womenOverOrEqualAgeLimit, new Integer(womenMembersEqualOrOver));
			regData.addData(menUnderAge, new Integer(menMembersUnder));
			regData.addData(menOverOrEqualAgeLimit, new Integer(menMembersEqualOrOver));
			regData.addData(bothGendersEqualOrOverAge, new Integer(menMembersEqualOrOver + womenMembersEqualOrOver));
			regData.addData(bothGendersUnderAge, new Integer(menMembersUnder + womenMembersUnder));
			regData.addData(bothGendersAllAge, new Integer(menMembersUnder + womenMembersUnder + menMembersEqualOrOver + womenMembersEqualOrOver));

			reportCollection.addElement(regData);
		}

		ReportableField[] sortFields = new ReportableField[] {regionalUnionName, clubName};
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
			
		ReportableField regionalUnionName = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionName);
	
		ReportableField menUnderAge = new ReportableField(FIELD_NAME_MEN_UNDER_AGE, Integer.class);
		menUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_UNDER+age, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAge);
	
		ReportableField womenUnderAge = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE, Integer.class);
		womenUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER+age, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAge);

		ReportableField womenOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);

		ReportableField menOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL+age, "men "+age+"+"),currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
	
		ReportableField bothGendersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_UNDER+age, "all -"+age),currentLocale);
		reportCollection.addField(bothGendersUnderAge);

		ReportableField bothGendersEqualOrOver = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
		bothGendersEqualOrOver.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER+age, "all "+age+"+"),currentLocale);
		reportCollection.addField(bothGendersEqualOrOver);

		ReportableField bothGendersLastYear = new ReportableField(FIELD_NAME_ALL_AGES_LAST_YEAR, Integer.class);
		bothGendersLastYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_LAST_YEAR, "all "+age+"+ last year"),currentLocale);
		reportCollection.addField(bothGendersLastYear);

		//Real data stuff
		//Gathering data
		//Get all the workreports (actually more than needed)
		//then for each get its leagues and the count for
		//each age and create a row and insert into an ordered map by league
		//then iterate the map and insert into the final report collection.
		Collection clubs = getWorkReportBusiness().getWorkReportsForRegionalUnionCollection(year.intValue(), regionalUnionsFilter);
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
				regData.addData(regionalUnionName, regionalUnionIdentifier);
				/*String ruAbbrev = report.getRegionalUnionAbbreviation();
				regData.addData(regionalUnionAbbreviation, ruAbbrev==null?"":ruAbbrev);
				String ruNumber = report.getRegionalUnionNumber();
				regData.addData(regionalUnionNumber, ruNumber==null?"":ruNumber);*/
			
				regData.addData(womenUnderAge, new Integer(0));
				regData.addData(womenOverOrEqualAgeLimit, new Integer(0));
				regData.addData(menUnderAge, new Integer(0));
				regData.addData(menOverOrEqualAgeLimit, new Integer(0));
				regData.addData(bothGendersLastYear, new Integer(0));
				regData.addData(bothGendersEqualOrOver, new Integer(0));
				regData.addData(bothGendersUnderAge, new Integer(0));
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
			regData = addToIntegerCount(bothGendersEqualOrOver, regData, menPlayersEqualOrOver + womenPlayersEqualOrOver); 
			regData = addToIntegerCount(bothGendersUnderAge, regData, womenPlayersUnder + menPlayersUnder);
			if(lastYearReport!=null) {
				//int lastYearMemberCount = getWorkReportBusiness().getCountOfPlayersByWorkReportAndWorkReportGroup(lastYearReport, null);
				int lastYearMemberCount = getWorkReportBusiness().getCountOfPlayersByWorkReport(lastYearReport);
				//all wrong
				regData = addToIntegerCount(bothGendersLastYear, regData, lastYearMemberCount);
			}
			//put it back again
			regionalUnionsStatsMap.put(regionalUnionIdentifier,regData);
		}

		// iterate through the ordered map and ordered lists and add to the final collection
		reportCollection.addAll(regionalUnionsStatsMap.values());
		
		ReportableField[] sortFields = new ReportableField[] {regionalUnionName};
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
		
		ReportableField regionalUnionName = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionName);
				
		ReportableField leagueString = new ReportableField(FIELD_NAME_LEAGUE_NAME, String.class);
		leagueString.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_LEAGUE_INFO, "League"), currentLocale);
		reportCollection.addField(leagueString);
	
		ReportableField womenUnderAgeLimit = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE, Integer.class);
		womenUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER+age, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAgeLimit);
	
		ReportableField womenOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
	
		ReportableField menUnderAgeLimit = new ReportableField(FIELD_NAME_MEN_UNDER_AGE, Integer.class);
		menUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_UNDER+age, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAgeLimit);
	
		ReportableField menOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL+age, "men "+age+"+"), currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
		
		ReportableField bothGendersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_UNDER + age, "all -"+age), currentLocale);
		reportCollection.addField(bothGendersUnderAge);

		ReportableField bothGendersEqualOrOverAge = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
		bothGendersEqualOrOverAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER + age, "all "+age+"+"), currentLocale);
		reportCollection.addField(bothGendersEqualOrOverAge);
	
		ReportableField bothGendersAllAge = new ReportableField(FIELD_NAME_ALL_AGES, Integer.class);
		bothGendersAllAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL, "all "), currentLocale);
		reportCollection.addField(bothGendersAllAge);
		
		//Real data stuff
		//Gathering data
		//Get all the workreports (actually more than needed)
		//then for each get its leagues and the count for
		//each age and create a row and insert into an ordered map by league
		//then insert into the final report collection.
		Collection clubs = getWorkReportBusiness().getWorkReportsForRegionalUnionCollection(year.intValue(), regionalUnionsFilter);
		Map leagueStatsMap = new TreeMap();
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
					Integer leagueKey = (Integer) league.getPrimaryKey();
				
					if (!leagueGroupIdList.contains(league.getGroupId())) {
						continue; //don't process this one, go to next
					}
				
					String leagueIdentifier = getLeagueIdentifier(league);
					//fetch the stats or initialize
					ReportableData leagueStatsData = (ReportableData) leagueStatsMap.get(leagueKey);
					if(leagueStatsData==null){//initialize
						leagueStatsData = new ReportableData();
						leagueStatsData.addData(regionalUnionName, regionalUnionIdentifier);
						leagueStatsData.addData(leagueString, leagueIdentifier);
						leagueStatsData.addData(womenUnderAgeLimit, new Integer(0));
						leagueStatsData.addData(womenOverOrEqualAgeLimit, new Integer(0));
						leagueStatsData.addData(menUnderAgeLimit,new Integer(0));
						leagueStatsData.addData(menOverOrEqualAgeLimit, new Integer(0));
						leagueStatsData.addData(bothGendersUnderAge, new Integer(0));
						leagueStatsData.addData(bothGendersEqualOrOverAge, new Integer(0));
						leagueStatsData.addData(bothGendersAllAge, new Integer(0));
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
					
					leagueStatsData = addToIntegerCount(bothGendersUnderAge, leagueStatsData, menUnder + womenUnder);
					leagueStatsData = addToIntegerCount(bothGendersEqualOrOverAge, leagueStatsData, menOver + womenOver);
					leagueStatsData = addToIntegerCount(bothGendersAllAge, leagueStatsData, menUnder + womenUnder + menOver + womenOver);				
				
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

		ReportableField[] sortFields = new ReportableField[] {regionalUnionName, leagueString};
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);
	
		//finished return the collection
		return reportCollection;

	}

	/*
	 * Report B12.2.7 of the ISI Specs
	 */
	public ReportableCollection getMemberStatisticsForRegionalUnionsByYearRegionalUnionsUMFIUnionsAndClubTypesFilter (
			final Integer year,
			Collection regionalUnionsFilter, 
			Collection umfiClubsFilter, 
			String type)
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
	
		ReportableField regionalUnionName = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionName);
	
		ReportableField menUnderAge = new ReportableField(FIELD_NAME_MEN_UNDER_AGE, Integer.class);
		menUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_UNDER+age, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAge);
	
		ReportableField womenUnderAge = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE, Integer.class);
		womenUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER+age, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAge);
	
		ReportableField womenOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
	
		ReportableField menOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL+age, "men "+age+"+"),currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
	
		ReportableField bothGendersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_UNDER+age, "all -"+age),currentLocale);
		reportCollection.addField(bothGendersUnderAge);
	
		ReportableField bothGendersEqualOrOver = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
		bothGendersEqualOrOver.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER+age, "all "+age+"+"),currentLocale);
		reportCollection.addField(bothGendersEqualOrOver);
		
		ReportableField bothGendersAllAge = new ReportableField(FIELD_NAME_ALL_AGES, Integer.class);
		bothGendersAllAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL, "all"), currentLocale);
		reportCollection.addField(bothGendersAllAge);

		ReportableField bothGendersLastYear = new ReportableField(FIELD_NAME_ALL_AGES_LAST_YEAR, Integer.class);
		bothGendersLastYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_LAST_YEAR, "all "+age+"+ last year"),currentLocale);
		reportCollection.addField(bothGendersLastYear);
	
		//Real data stuff
		//Gathering data
		//Get all the workreports (actually more than needed)
		//then for each get its leagues and the count for
		//each age and create a row and insert into an ordered map by league
		//then iterate the map and insert into the final report collection.
		Collection clubs = getWorkReportBusiness().getWorkReportsByYearRegionalUnionsAndClubs(year.intValue(), regionalUnionsFilter, umfiClubsFilter);
		Map regionalUnionsStatsMap = new TreeMap();
		//Iterating through workreports and creating report data 
		Iterator iter = clubs.iterator();
		while (iter.hasNext()) {
			//the club
			WorkReport report = (WorkReport) iter.next();
			
			String regionalUnionIdentifier = getRegionalUnionIdentifier(report);
			
			if(type!=null && type.length()>0) {
				boolean show = false;
				if(type.equals(ClubTypeDropDownMenu.TYPE_UMFI_CLUB) && report.isInUMFI()) {
					show = true;
				}
				if(type.equals(ClubTypeDropDownMenu.TYPE_INACTIVE_CLUB) && report.isInActive()) {
					show = true;
				}
				if(type.equals(report.getType())) {
					show = true;
				}
				if(!show) {
					continue;
				}
			}
			
			WorkReport lastYearReport=null;
			try {
				lastYearReport = getWorkReportBusiness().getWorkReportHome().findWorkReportByGroupIdAndYearOfReport(report.getGroupId().intValue(),year.intValue()-1);
			} catch (FinderException e1) {
				System.err.println("WorkReportStatsBusiness : No report for year before :"+year);
			}
	
			//fetch the stats or initialize for this regional union (i.e. the one associated with regionalUnionIdentifier)
			ReportableData regData = (ReportableData) regionalUnionsStatsMap.get(regionalUnionIdentifier);
			if(regData==null){//initialize
				regData = new ReportableData();
				regData.addData(regionalUnionName, regionalUnionIdentifier);
				regData.addData(womenUnderAge, new Integer(0));
				regData.addData(womenOverOrEqualAgeLimit, new Integer(0));
				regData.addData(menUnderAge, new Integer(0));
				regData.addData(menOverOrEqualAgeLimit, new Integer(0));
				regData.addData(bothGendersAllAge, new Integer(0));
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
			regData = addToIntegerCount(bothGendersEqualOrOver, regData, menMembersEqualOrOver + womenMembersEqualOrOver);
			regData = addToIntegerCount(bothGendersUnderAge, regData, menMembersUnder + womenMembersUnder);
			regData = addToIntegerCount(bothGendersAllAge, regData, menMembersUnder + womenMembersUnder + menMembersEqualOrOver + womenMembersEqualOrOver);
			if(lastYearReport!=null) {
				try {
					int lastYearMemberCount = getWorkReportBusiness().getCountOfMembersByWorkReport(lastYearReport);
					regData = addToIntegerCount(bothGendersLastYear, regData, lastYearMemberCount);
				} catch(Exception e) {
					System.out.println("Error getting member count for last year");
					e.printStackTrace();
				}
			}
			//put it back again
			regionalUnionsStatsMap.put(regionalUnionIdentifier,regData);
		}
	
		// iterate through the ordered map and ordered lists and add to the final collection
		reportCollection.addAll(regionalUnionsStatsMap.values());
	
		ReportableField[] sortFields = new ReportableField[] {regionalUnionName};
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);
	
		//finished return the collection
		return reportCollection;
	}
	
	/*
	 * Report B12.2.8 of the ISI Specs
	 */
	public ReportableCollection getPlayerStatisticsForRegionalUnionsByYearRegionalUnionsUMFIUnionsAndClubTypesFilter (
			final Integer year, 
			Collection regionalUnionsFilter, 
			Collection umfiClubsFilter, 
			String type)
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

		ReportableField regionalUnionName = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionName);

		ReportableField womenUnderAge = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE, Integer.class);
		womenUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER+age, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAge);

		ReportableField womenOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
		
		ReportableField menUnderAge = new ReportableField(FIELD_NAME_MEN_UNDER_AGE, Integer.class);
		menUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_UNDER+age, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAge);

		ReportableField menOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL+age, "men "+age+"+"),currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);

		ReportableField bothGendersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_UNDER+age, "all -"+age),currentLocale);
		reportCollection.addField(bothGendersUnderAge);

		ReportableField bothGendersEqualOrOver = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
		bothGendersEqualOrOver.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER+age, "all "+age+"+"),currentLocale);
		reportCollection.addField(bothGendersEqualOrOver);
	
		ReportableField bothGendersAllAge = new ReportableField(FIELD_NAME_ALL_AGES, Integer.class);
		bothGendersAllAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL, "all"), currentLocale);
		reportCollection.addField(bothGendersAllAge);

		ReportableField bothGendersLastYear = new ReportableField(FIELD_NAME_ALL_AGES_LAST_YEAR, Integer.class);
		bothGendersLastYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_LAST_YEAR, "all "+age+"+ last year"),currentLocale);
		reportCollection.addField(bothGendersLastYear);

		//Real data stuff
		//Gathering data
		//Get all the workreports (actually more than needed)
		//then for each get its leagues and the count for
		//each age and create a row and insert into an ordered map by league
		//then iterate the map and insert into the final report collection.
		Collection clubs = getWorkReportBusiness().getWorkReportsByYearRegionalUnionsAndClubs(year.intValue(), regionalUnionsFilter, umfiClubsFilter);
		Map regionalUnionsStatsMap = new TreeMap();
		//Iterating through workreports and creating report data 
		Iterator iter = clubs.iterator();
		while (iter.hasNext()) {
			//the club
			WorkReport report = (WorkReport) iter.next();
		
			String regionalUnionIdentifier = getRegionalUnionIdentifier(report);
		
			if(type!=null && type.length()>0) {
				boolean show = false;
				if(type.equals(ClubTypeDropDownMenu.TYPE_UMFI_CLUB) && report.isInUMFI()) {
					show = true;
				}
				if(type.equals(ClubTypeDropDownMenu.TYPE_INACTIVE_CLUB) && report.isInActive()) {
					show = true;
				}
				if(type.equals(report.getType())) {
					show = true;
				}
				if(!show) {
					continue;
				}
			}
		
			WorkReport lastYearReport=null;
			try {
				lastYearReport = getWorkReportBusiness().getWorkReportHome().findWorkReportByGroupIdAndYearOfReport(report.getGroupId().intValue(),year.intValue()-1);
			} catch (FinderException e1) {
				System.err.println("WorkReportStatsBusiness : No report for year before :"+year);
			}

			//fetch the stats or initialize for this regional union (i.e. the one associated with regionalUnionIdentifier)
			ReportableData regData = (ReportableData) regionalUnionsStatsMap.get(regionalUnionIdentifier);
			if(regData==null){//initialize
				regData = new ReportableData();
				regData.addData(regionalUnionName, regionalUnionIdentifier);
				regData.addData(womenUnderAge, new Integer(0));
				regData.addData(womenOverOrEqualAgeLimit, new Integer(0));
				regData.addData(menUnderAge, new Integer(0));
				regData.addData(menOverOrEqualAgeLimit, new Integer(0));
				regData.addData(bothGendersAllAge, new Integer(0));
				regData.addData(bothGendersLastYear, new Integer(0));
			}

			//add to counts
			int womenMembersUnder = getWorkReportBusiness().getCountOfFemalePlayersOfYoungerAgeAndByWorkReport(age, report);
			int womenMembersEqualOrOver = getWorkReportBusiness().getCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReport(age, report);
			int menMembersUnder = getWorkReportBusiness().getCountOfMalePlayersOfYoungerAgeAndByWorkReport(age, report);
			int menMembersEqualOrOver = getWorkReportBusiness().getCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReport(age, report);
			regData = addToIntegerCount(womenUnderAge, regData, womenMembersUnder);
			regData = addToIntegerCount(womenOverOrEqualAgeLimit, regData, womenMembersEqualOrOver);
			regData = addToIntegerCount(menUnderAge, regData, menMembersUnder);
			regData = addToIntegerCount(menOverOrEqualAgeLimit, regData, menMembersEqualOrOver);
			regData = addToIntegerCount(bothGendersEqualOrOver, regData, menMembersEqualOrOver + womenMembersEqualOrOver);
			regData = addToIntegerCount(bothGendersUnderAge, regData, menMembersUnder + womenMembersUnder);
			regData = addToIntegerCount(bothGendersAllAge, regData, menMembersUnder + womenMembersUnder + menMembersEqualOrOver + womenMembersEqualOrOver);
			if(lastYearReport!=null) {
				try {
					int lastYearMemberCount = getWorkReportBusiness().getCountOfMembersByWorkReport(lastYearReport);
					regData = addToIntegerCount(bothGendersLastYear, regData, lastYearMemberCount);
				} catch(Exception e) {
					System.out.println("Error getting member count for last year");
					e.printStackTrace();
				}
			}
			//put it back again
			regionalUnionsStatsMap.put(regionalUnionIdentifier,regData);
		}

		// iterate through the ordered map and ordered lists and add to the final collection
		reportCollection.addAll(regionalUnionsStatsMap.values());

		ReportableField[] sortFields = new ReportableField[] {regionalUnionName};
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);

		//finished return the collection
		return reportCollection;
	}

	/*
	 * Report B12.3.1 of the ISI Specs
	 */
	public ReportableCollection getMemberStatisticsForClubsByYearClubsUMFIClubsAndClubTypesFilter (
			final Integer year,
			Collection clubsFilter, 
			Collection umfiClubsFilter, 
			String type)
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

		ReportableField clubName = new ReportableField(FIELD_NAME_CLUB_NAME, String.class);
		clubName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_CLUB_NAME, "Club name"), currentLocale);
		reportCollection.addField(clubName);
		
		ReportableField clubNumber = new ReportableField(FIELD_NAME_CLUB_NUMBER, String.class);
		clubNumber.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_CLUB_NUMBER, "Club number"), currentLocale);
		reportCollection.addField(clubNumber);
		
		ReportableField regionalUnionAbbreviation = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionAbbreviation.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionAbbreviation);

		ReportableField clubIsInUMFI = new ReportableField(FIELD_NAME_IS_IN_UMFI, String.class);
		clubIsInUMFI.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_IS_IN_UMFI, "UMFI member"), currentLocale);
		reportCollection.addField(clubIsInUMFI);
		
		ReportableField clubType = new ReportableField(FIELD_NAME_CLUB_TYPE, String.class);
		clubType.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_CLUB_TYPE, "Club type"), currentLocale);
		reportCollection.addField(clubType);
		
		ReportableField womenUnderAge = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE, Integer.class);
		womenUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER+age, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAge);

		ReportableField womenOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
		
		ReportableField menUnderAge = new ReportableField(FIELD_NAME_MEN_UNDER_AGE, Integer.class);
		menUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_UNDER+age, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAge);

		ReportableField menOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL+age, "men "+age+"+"),currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);

		ReportableField bothGendersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_UNDER+age, "all -"+age),currentLocale);
		reportCollection.addField(bothGendersUnderAge);

		ReportableField bothGendersEqualOrOver = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
		bothGendersEqualOrOver.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER+age, "all "+age+"+"),currentLocale);
		reportCollection.addField(bothGendersEqualOrOver);
	
		ReportableField bothGendersAllAge = new ReportableField(FIELD_NAME_ALL_AGES, Integer.class);
		bothGendersAllAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL, "all"), currentLocale);
		reportCollection.addField(bothGendersAllAge);

		ReportableField bothGendersLastYear = new ReportableField(FIELD_NAME_ALL_AGES_LAST_YEAR, Integer.class);
		bothGendersLastYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_LAST_YEAR, "all last year"),currentLocale);
		reportCollection.addField(bothGendersLastYear);
		
		//Real data stuff
		//Gathering data
		//Get all the workreports (actually more than needed)
		//then for each get its leagues and the count for
		//each age and create a row and insert into an ordered map by league
		//then iterate the map and insert into the final report collection.
		HashSet clubSet = (clubsFilter==null)?(new HashSet()):(new HashSet(clubsFilter));
		if(umfiClubsFilter!=null) {
			clubSet.add(umfiClubsFilter);
		}
		if(clubSet.isEmpty()) {
			clubSet = null;
		}
		Collection clubs = getWorkReportBusiness().getWorkReportsByYearRegionalUnionsAndClubs(year.intValue(), null, clubSet);
		//Iterating through workreports and creating report data 
		Iterator iter = clubs.iterator();
		while (iter.hasNext()) {
			//the club
			WorkReport report = (WorkReport) iter.next();
		
			boolean showClub = showClubType(report, type);
			if(!showClub) {
				continue;
			}
		
			WorkReport lastYearReport=null;
			try {
				lastYearReport = getWorkReportBusiness().getWorkReportHome().findWorkReportByGroupIdAndYearOfReport(report.getGroupId().intValue(),year.intValue()-1);
			} catch (FinderException e1) {
				System.err.println("WorkReportStatsBusiness : No report for year before :"+year);
			}

			ReportableData regData = new ReportableData();
			// fetch club info

			regData.addData(clubName, report.getGroupName());
			regData.addData(clubNumber, report.getGroupNumber());
			regData.addData(regionalUnionAbbreviation, getRegionalUnionIdentifier(report));
			regData.addData(clubIsInUMFI, report.isInUMFI()?_iwrb.getLocalizedString(LOCALIZED_YES, "Yes"):"");
			regData.addData(clubType, getClubTypeString(report));

			//fetch member stats stats or initialize for this club
			int womenMembersUnder = getWorkReportBusiness().getCountOfFemaleMembersOfYoungerAgeAndByWorkReport(age, report);
			int womenMembersEqualOrOver = getWorkReportBusiness().getCountOfFemaleMembersEqualOrOlderThanAgeAndByWorkReport(age, report);
			int menMembersUnder = getWorkReportBusiness().getCountOfMaleMembersOfYoungerAgeAndByWorkReport(age, report);
			int menMembersEqualOrOver = getWorkReportBusiness().getCountOfMaleMembersEqualOrOlderThanAgeAndByWorkReport(age, report);
			int lastYearMemberCount = 0;
			if(lastYearReport!=null) {
				lastYearMemberCount = getWorkReportBusiness().getCountOfPlayersByWorkReport(lastYearReport);
			} 
			
			regData.addData(womenUnderAge, new Integer(womenMembersUnder));
			regData.addData(womenOverOrEqualAgeLimit, new Integer(womenMembersEqualOrOver));
			regData.addData(menUnderAge, new Integer(menMembersUnder));
			regData.addData(menOverOrEqualAgeLimit, new Integer(menMembersEqualOrOver));
			regData.addData(bothGendersUnderAge, new Integer(womenMembersUnder + menMembersUnder));
			regData.addData(bothGendersEqualOrOver, new Integer(womenMembersEqualOrOver + menMembersEqualOrOver));
			regData.addData(bothGendersAllAge, new Integer(womenMembersUnder + menMembersUnder + womenMembersEqualOrOver + menMembersEqualOrOver));
			regData.addData(bothGendersLastYear, new Integer(lastYearMemberCount));
			
			reportCollection.add(regData);
		}

		ReportableField[] sortFields = new ReportableField[] {clubNumber};
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);

		//finished return the collection
		return reportCollection;
	}
	
	/*
	 * Report B12.3.2 of the ISI Specs
	 */
	public ReportableCollection getPlayerStatisticsForClubsByYearClubsUMFIClubsAndClubTypesFilter (
			final Integer year,
			Collection clubsFilter, 
			Collection umfiClubsFilter, 
			String type)
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
	
		ReportableField clubName = new ReportableField(FIELD_NAME_CLUB_NAME, String.class);
		clubName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_CLUB_NAME, "Club name"), currentLocale);
		reportCollection.addField(clubName);
	
		ReportableField clubNumber = new ReportableField(FIELD_NAME_CLUB_NUMBER, String.class);
		clubNumber.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_CLUB_NUMBER, "Club number"), currentLocale);
		reportCollection.addField(clubNumber);
	
		ReportableField regionalUnionAbbreviation = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionAbbreviation.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionAbbreviation);
	
		ReportableField clubIsInUMFI = new ReportableField(FIELD_NAME_IS_IN_UMFI, String.class);
		clubIsInUMFI.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_IS_IN_UMFI, "UMFI member"), currentLocale);
		reportCollection.addField(clubIsInUMFI);
	
		ReportableField clubType = new ReportableField(FIELD_NAME_CLUB_TYPE, String.class);
		clubType.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_CLUB_TYPE, "Club type"), currentLocale);
		reportCollection.addField(clubType);
	
		ReportableField womenUnderAge = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE, Integer.class);
		womenUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER+age, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAge);
	
		ReportableField womenOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
	
		ReportableField menUnderAge = new ReportableField(FIELD_NAME_MEN_UNDER_AGE, Integer.class);
		menUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_UNDER+age, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAge);
	
		ReportableField menOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL+age, "men "+age+"+"),currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
	
		ReportableField bothGendersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_UNDER+age, "all -"+age),currentLocale);
		reportCollection.addField(bothGendersUnderAge);
	
		ReportableField bothGendersEqualOrOver = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
		bothGendersEqualOrOver.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER+age, "all "+age+"+"),currentLocale);
		reportCollection.addField(bothGendersEqualOrOver);
	
		ReportableField bothGendersAllAge = new ReportableField(FIELD_NAME_ALL_AGES, Integer.class);
		bothGendersAllAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL, "all"), currentLocale);
		reportCollection.addField(bothGendersAllAge);
	
		ReportableField bothGendersLastYear = new ReportableField(FIELD_NAME_ALL_AGES_LAST_YEAR, Integer.class);
		bothGendersLastYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_LAST_YEAR, "all last year"),currentLocale);
		reportCollection.addField(bothGendersLastYear);
	
		//Real data stuff
		//Gathering data
		//Get all the workreports (actually more than needed)
		//then for each get its leagues and the count for
		//each age and create a row and insert into an ordered map by league
		//then iterate the map and insert into the final report collection.
		HashSet clubSet = (clubsFilter==null)?(new HashSet()):(new HashSet(clubsFilter));
		if(umfiClubsFilter!=null) {
			clubSet.add(umfiClubsFilter);
		}
		if(clubSet.isEmpty()) {
			clubSet = null;
		}
		Collection clubs = getWorkReportBusiness().getWorkReportsByYearRegionalUnionsAndClubs(year.intValue(), null, clubSet);
		//Iterating through workreports and creating report data 
		Iterator iter = clubs.iterator();
		while (iter.hasNext()) {
			//the club
			WorkReport report = (WorkReport) iter.next();
		
			boolean showClub = showClubType(report, type);
			if(!showClub) {
				continue;
			}
		
			WorkReport lastYearReport=null;
			try {
				lastYearReport = getWorkReportBusiness().getWorkReportHome().findWorkReportByGroupIdAndYearOfReport(report.getGroupId().intValue(),year.intValue()-1);
			} catch (FinderException e1) {
				System.err.println("WorkReportStatsBusiness : No report for year before :"+year);
			}
		
			ReportableData regData = new ReportableData();
			// fetch club info
		
			regData.addData(clubName, report.getGroupName());
			regData.addData(clubNumber, report.getGroupNumber());
			regData.addData(regionalUnionAbbreviation, getRegionalUnionIdentifier(report));
			regData.addData(clubIsInUMFI, report.isInUMFI()?_iwrb.getLocalizedString(LOCALIZED_YES, "Yes"):"");
			regData.addData(clubType, getClubTypeString(report));
		
			//fetch member stats stats or initialize for this club
			int womenPlayersUnder = getWorkReportBusiness().getCountOfFemalePlayersOfYoungerAgeAndByWorkReport(age, report);
			int womenPlayersEqualOrOver = getWorkReportBusiness().getCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReport(age, report);
			int menPlayersUnder = getWorkReportBusiness().getCountOfMalePlayersOfYoungerAgeAndByWorkReport(age, report);
			int menPlayersEqualOrOver = getWorkReportBusiness().getCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReport(age, report);
			int lastYearPlayersCount = 0;
			if(lastYearReport!=null) {
				lastYearPlayersCount = getWorkReportBusiness().getCountOfPlayersByWorkReport(lastYearReport);
			} 
		
			regData.addData(womenUnderAge, new Integer(womenPlayersUnder));
			regData.addData(womenOverOrEqualAgeLimit, new Integer(womenPlayersEqualOrOver));
			regData.addData(menUnderAge, new Integer(menPlayersUnder));
			regData.addData(menOverOrEqualAgeLimit, new Integer(menPlayersEqualOrOver));
			regData.addData(bothGendersUnderAge, new Integer(womenPlayersUnder + menPlayersUnder));
			regData.addData(bothGendersEqualOrOver, new Integer(womenPlayersEqualOrOver + menPlayersEqualOrOver));
			regData.addData(bothGendersAllAge, new Integer(womenPlayersUnder + menPlayersUnder + womenPlayersEqualOrOver + menPlayersEqualOrOver));
			regData.addData(bothGendersLastYear, new Integer(lastYearPlayersCount));
		
			reportCollection.add(regData);
		}
	
		ReportableField[] sortFields = new ReportableField[] {clubNumber};
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);
	
		//finished return the collection
		return reportCollection;
	}
	
	/*
	 * Report B12.5.1 of the ISI Specs
	 */
	public ReportableCollection getWorkReportStatusForClubsByYearRegionalUnionsClubTypeAndStatus (
			final Integer year,
			Collection regionalUnionsFilter,
			String type,
			String status)
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

		ReportableField clubName = new ReportableField(FIELD_NAME_CLUB_NAME, String.class);
		clubName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_CLUB_NAME, "Club name"), currentLocale);
		reportCollection.addField(clubName);

		ReportableField clubNumber = new ReportableField(FIELD_NAME_CLUB_NUMBER, String.class);
		clubNumber.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_CLUB_NUMBER, "Club number"), currentLocale);
		reportCollection.addField(clubNumber);

		ReportableField regionalUnionAbbreviation = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionAbbreviation.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionAbbreviation);

		ReportableField workReportStatus = new ReportableField(FIELD_NAME_WORK_REPORT_STATUS, String.class);
		workReportStatus.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WORK_REPORT_STATUS, "Work Report Status"), currentLocale);
		reportCollection.addField(workReportStatus);
		
		ReportableField workReportStatusInfo = new ReportableField(FIELD_NAME_WORK_REPORT_STATUS_INFO, String.class);
		workReportStatusInfo.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WORK_REPORT_STATUS_INFO, "Work Report Status"), currentLocale);
		reportCollection.addField(workReportStatusInfo);
		
		ReportableField workReportStatusRemarks = new ReportableField(FIELD_NAME_WORK_REPORT_STATUS_REMARKS, String.class);
		workReportStatusRemarks.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WORK_REPORT_STATUS_REMARKS, "Work Report Status"), currentLocale);
		reportCollection.addField(workReportStatusRemarks);

		//Real data stuff
		//Gathering data
		//Get all the workreports (actually more than needed)
		//then for each get its leagues and the count for
		//each age and create a row and insert into an ordered map by league
		//then iterate the map and insert into the final report collection.
		Collection clubs = getWorkReportBusiness().getWorkReportsForRegionalUnionCollection(year.intValue(), regionalUnionsFilter);
		//Iterating through workreports and creating report data 
		Iterator iter = clubs.iterator();
		while (iter.hasNext()) {
			//the club
			WorkReport report = (WorkReport) iter.next();
			String cName = report.getGroupName();
			System.out.print("Processing club " + cName);
			
			boolean showClub = showClubType(report, type) && showClubStatus(report, type);
			if(!showClub) {
				System.out.println(" (skipped)");
				continue;
			}
			System.out.println();
			
			String reportStatus = report.getStatus();
			String statusString = "";
			if(WorkReportConstants.WR_STATUS_DONE.equals(reportStatus)) {
				statusString = _iwrb.getLocalizedString(WorkReportStatusDropDownMenu.LOCALIZED_STATUS_DONE, "Done");
			} else if(WorkReportConstants.WR_STATUS_NO_REPORT.equals(reportStatus)) {
				statusString = _iwrb.getLocalizedString(WorkReportStatusDropDownMenu.LOCALIZED_STATUS_NO_REPORT, "No Report");
			} else if(WorkReportConstants.WR_STATUS_NOT_DONE.equals(reportStatus)) {
				statusString = _iwrb.getLocalizedString(WorkReportStatusDropDownMenu.LOCALIZED_STATUS_NOT_DONE, "Not Done");
			} else if(WorkReportConstants.WR_STATUS_SOME_DONE.equals(reportStatus)) {
				statusString = _iwrb.getLocalizedString(WorkReportStatusDropDownMenu.LOCALIZED_STATUS_SOME_DONE, "Some Done");
			}
	
			ReportableData regData = new ReportableData();
			// fetch club info
			regData.addData(clubName, cName);
			regData.addData(clubNumber, report.getGroupNumber());
			regData.addData(regionalUnionAbbreviation, getRegionalUnionIdentifier(report));
			regData.addData(workReportStatus, statusString);
			String statusInfo = report.getContinuanceTill();
			if(statusInfo==null) {
				statusInfo = "";
			}
			String statusRemark = report.getSentReportText();
			if(statusRemark==null) {
				statusRemark = "";
			}
			regData.addData(workReportStatusInfo, statusInfo);
			regData.addData(workReportStatusRemarks, statusRemark);
	
			//fetch member stats stats or initialize for this club	
			reportCollection.add(regData);
		}

		ReportableField[] sortFields = new ReportableField[] {clubNumber};
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);

		//finished return the collection
		return reportCollection;
	}
	
	/*
	 * Report B12.5.2 of the ISI Specs
	 */
	public ReportableCollection getWorkReportStatusForClubsByYearRegionalUnionsAndClubs (
			final Integer year,
			Collection regionalUnionsFilter,
			Collection clubsFilter,
			String onlyTotals)
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

		ReportableField clubName = new ReportableField(FIELD_NAME_CLUB_NAME, String.class);
		clubName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_CLUB_NAME, "Club name"), currentLocale);
		reportCollection.addField(clubName);

		ReportableField divisionName = new ReportableField(FIELD_NAME_DIVISION_NAME, String.class);
		divisionName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_DIVISION_NAME, "Division"), currentLocale);
		reportCollection.addField(divisionName);

		ReportableField annualReportStatus = new ReportableField(FIELD_NAME_ANNUAL_REPORT_STATUS, String.class);
		annualReportStatus.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ANNUAL_REPORT_STATUS, "Annual Report Status"), currentLocale);
		reportCollection.addField(annualReportStatus);
	
		// Fields for member stats
		ReportableField womenMembersUnderAge = new ReportableField(FIELD_NAME_WOMEN_MEMBERS_UNDER_AGE, Integer.class);
		womenMembersUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER+age, "women -"+age), currentLocale);
		reportCollection.addField(womenMembersUnderAge);

		ReportableField womenMembersOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_MEMBERS_OVER_OR_EQUAL_AGE, Integer.class);
		womenMembersOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenMembersOverOrEqualAgeLimit);

		ReportableField menMembersUnderAge = new ReportableField(FIELD_NAME_MEN_MEMBERS_UNDER_AGE, Integer.class);
		menMembersUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_UNDER+age, "men -"+age), currentLocale);
		reportCollection.addField(menMembersUnderAge);

		ReportableField menMembersOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_MEMBERS_OVER_OR_EQUAL_AGE, Integer.class);
		menMembersOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL+age, "men "+age+"+"),currentLocale);
		reportCollection.addField(menMembersOverOrEqualAgeLimit);

		ReportableField bothGendersMembersUnderAge = new ReportableField(FIELD_NAME_ALL_MEMBERS_UNDER_AGE, Integer.class);
		bothGendersMembersUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_UNDER+age, "all -"+age),currentLocale);
		reportCollection.addField(bothGendersMembersUnderAge);

		ReportableField bothGendersMembersEqualOrOver = new ReportableField(FIELD_NAME_ALL_MEMBERS_OVER_OR_EQUAL_AGE, Integer.class);
		bothGendersMembersEqualOrOver.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER+age, "all "+age+"+"),currentLocale);
		reportCollection.addField(bothGendersMembersEqualOrOver);

		ReportableField bothGendersMembersAllAge = new ReportableField(FIELD_NAME_ALL_MEMBERS_AGES, Integer.class);
		bothGendersMembersAllAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL, "all"), currentLocale);
		reportCollection.addField(bothGendersMembersAllAge);
		
		// Fields for player stats
		ReportableField womenPlayersUnderAge = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE, Integer.class);
		womenPlayersUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER+age, "women -"+age), currentLocale);
		reportCollection.addField(womenPlayersUnderAge);
		
		ReportableField womenPlayersOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenPlayersOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenPlayersOverOrEqualAgeLimit);
		
		ReportableField menPlayersUnderAge = new ReportableField(FIELD_NAME_MEN_UNDER_AGE, Integer.class);
		menPlayersUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_UNDER+age, "men -"+age), currentLocale);
		reportCollection.addField(menPlayersUnderAge);
		
		ReportableField menPlayersOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menPlayersOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL+age, "men "+age+"+"),currentLocale);
		reportCollection.addField(menPlayersOverOrEqualAgeLimit);
		
		ReportableField bothGendersPlayersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersPlayersUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_UNDER+age, "all -"+age),currentLocale);
		reportCollection.addField(bothGendersPlayersUnderAge);
		
		ReportableField bothGendersPlayersEqualOrOver = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
		bothGendersPlayersEqualOrOver.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER+age, "all "+age+"+"),currentLocale);
		reportCollection.addField(bothGendersPlayersEqualOrOver);
		
		ReportableField bothGendersPlayersAllAge = new ReportableField(FIELD_NAME_ALL_AGES, Integer.class);
		bothGendersPlayersAllAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL, "all"), currentLocale);
		reportCollection.addField(bothGendersPlayersAllAge);
		
		// Fields for competitors stats
		ReportableField womenCompetitorsUnderAge = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE, Integer.class);
		womenCompetitorsUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER+age, "women -"+age), currentLocale);
		reportCollection.addField(womenCompetitorsUnderAge);
	
		ReportableField womenCompetitorsOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenCompetitorsOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenCompetitorsOverOrEqualAgeLimit);
	
		ReportableField menCompetitorsUnderAge = new ReportableField(FIELD_NAME_MEN_UNDER_AGE, Integer.class);
		menCompetitorsUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_UNDER+age, "men -"+age), currentLocale);
		reportCollection.addField(menCompetitorsUnderAge);
	
		ReportableField menCompetitorsOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menCompetitorsOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL+age, "men "+age+"+"),currentLocale);
		reportCollection.addField(menCompetitorsOverOrEqualAgeLimit);
	
		ReportableField bothGendersCompetitorsUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersCompetitorsUnderAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_UNDER+age, "all -"+age),currentLocale);
		reportCollection.addField(bothGendersCompetitorsUnderAge);
	
		ReportableField bothGendersCompetitorsEqualOrOver = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
		bothGendersCompetitorsEqualOrOver.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER+age, "all "+age+"+"),currentLocale);
		reportCollection.addField(bothGendersCompetitorsEqualOrOver);
	
		ReportableField bothGendersCompetitorsAllAge = new ReportableField(FIELD_NAME_ALL_AGES, Integer.class);
		bothGendersCompetitorsAllAge.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ALL, "all"), currentLocale);
		reportCollection.addField(bothGendersCompetitorsAllAge);
	
		//Real data stuff
		//Gathering data
		//Get all the workreports (actually more than needed)
		//then for each get its leagues and the count for
		//each age and create a row and insert into an ordered map by league
		//then iterate the map and insert into the final report collection.
		Collection clubs = getWorkReportBusiness().getWorkReportsForRegionalUnionCollection(year.intValue(), regionalUnionsFilter);
		//Iterating through workreports and creating report data 
		Iterator iter = clubs.iterator();
		while (iter.hasNext()) {
			//the club
			WorkReport report = (WorkReport) iter.next();
			String cName = report.getGroupName();
			
			ReportableData regData = new ReportableData();
			// fetch club info
			try {
				Collection leagues = report.getLeagues();
				Iterator iterator = leagues.iterator();
				while (iterator.hasNext()) {
					WorkReportGroup league = (WorkReportGroup) iterator.next();
					
					String leagueIdentifier = getLeagueIdentifier(league);
					regData.addData(clubName, cName);
					regData.addData(divisionName, leagueIdentifier);
					
					// Find if annual report is in or missing
					Collection records = getWorkReportBusiness().getWorkReportClubAccountRecordHome().findAllRecordsByWorkReportIdAndWorkReportGroupId(report.getGroupId().intValue(),league.getGroupId().intValue());
					boolean hasData = (records!=null && !records.isEmpty());
					String status;
					if(hasData) {
						status = _iwrb.getLocalizedString(LOCALIZED_ANNUAL_REPORT_IN, "In");
					} else {
						status = _iwrb.getLocalizedString(LOCALIZED_ANNUAL_REPORT_MISSING, "Missing");
					}
					regData.addData(annualReportStatus, status);
			
					//fetch member stats stats for this division	
					// @TODO (jonas) don't know how to fetch this data, have to ask eiki
					regData.addData(womenMembersUnderAge, new Integer(0));
					regData.addData(womenMembersOverOrEqualAgeLimit, new Integer(0));
					regData.addData(menMembersUnderAge, new Integer(0));
					regData.addData(menMembersOverOrEqualAgeLimit, new Integer(0));
					regData.addData(bothGendersMembersUnderAge, new Integer(0));
					regData.addData(bothGendersMembersEqualOrOver, new Integer(0));
					regData.addData(bothGendersMembersAllAge, new Integer(0));
					
					// fetch player stats stats for this division
					int womenPlayersUnder = getWorkReportBusiness().getCountOfFemalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(age, report, league);
					int womenPlayersEqualOrOver = getWorkReportBusiness().getCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age, report, league);
					int menPlayersUnder = getWorkReportBusiness().getCountOfMalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(age, report, league);
					int menPlayersEqualOrOver = getWorkReportBusiness().getCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age, report, league);
					regData.addData(womenPlayersUnderAge, new Integer(womenPlayersUnder));
					regData.addData(womenPlayersOverOrEqualAgeLimit, new Integer(womenPlayersEqualOrOver));
					regData.addData(menPlayersUnderAge, new Integer(menPlayersUnder));
					regData.addData(menPlayersOverOrEqualAgeLimit, new Integer(menPlayersEqualOrOver));
					regData.addData(bothGendersPlayersUnderAge, new Integer(womenPlayersUnder + menPlayersUnder));
					regData.addData(bothGendersPlayersEqualOrOver, new Integer(womenPlayersEqualOrOver + menPlayersEqualOrOver));
					regData.addData(bothGendersPlayersAllAge, new Integer(womenPlayersUnder + menPlayersUnder + womenPlayersEqualOrOver + menPlayersEqualOrOver));

					//fetch competitor stats stats for this division
//					@TODO (jonas) don't know how to fetch this data, have to ask eiki	
					regData.addData(womenCompetitorsUnderAge, new Integer(0));
					regData.addData(womenCompetitorsOverOrEqualAgeLimit, new Integer(0));
					regData.addData(menCompetitorsUnderAge, new Integer(0));
					regData.addData(menCompetitorsOverOrEqualAgeLimit, new Integer(0));
					regData.addData(bothGendersCompetitorsUnderAge, new Integer(0));
					regData.addData(bothGendersCompetitorsEqualOrOver, new Integer(0));
					regData.addData(bothGendersCompetitorsAllAge, new Integer(0));

					reportCollection.add(regData);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		ReportableField[] sortFields = new ReportableField[] {clubName, divisionName};
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);

		//finished return the collection
		return reportCollection;
	}
		
	private boolean showClubStatus(WorkReport report, String status) {
		String reportStatus = report.getStatus();
		boolean show = true;
		if(status!=null && status.length()>0) {
			show = false;
			if(WorkReportStatusDropDownMenu.STATUS_DONE.equals(status) && 
			   WorkReportConstants.WR_STATUS_DONE.equals(reportStatus)) {
				show = true;
			} else if(WorkReportStatusDropDownMenu.STATUS_NO_REPORT.equals(status) && 
					  WorkReportConstants.WR_STATUS_NO_REPORT.equals(reportStatus)) {
				show = true;
			} else if(WorkReportStatusDropDownMenu.STATUS_NOT_DONE.equals(status) && 
					  WorkReportConstants.WR_STATUS_NOT_DONE.equals(reportStatus)) {
				show = true;
			} else if(WorkReportStatusDropDownMenu.STATUS_SOME_DONE.equals(status) && 
					  WorkReportConstants.WR_STATUS_SOME_DONE.equals(reportStatus)) {
				show = true;
			} 
		}
		return show;
	}
	
	private boolean showClubType(WorkReport report, String type) {
		boolean show = true;
		if(type!=null && type.length()>0) {
			show = false;
			if(type.equals(ClubTypeDropDownMenu.TYPE_UMFI_CLUB) && report.isInUMFI()) {
				show = true;
			}
			if(type.equals(ClubTypeDropDownMenu.TYPE_INACTIVE_CLUB) && report.isInActive()) {
				show = true;
			}
			if(type.equals(report.getType())) {
				show = true;
			}
		}
		return show;
	}
	
private String getClubTypeString(WorkReport report) {
	String returnType;
	String cType = report.getType();
	
	if(IWMemberConstants.META_DATA_CLUB_STATUS_MULTI_DIVISION_CLUB.equals(cType)) {
		returnType = _iwrb.getLocalizedString(LOCALIZED_CLUB_TYPE_MULTI_DIVISION, "Multi Division");
	} else if(IWMemberConstants.META_DATA_CLUB_STATUS_SINGLE_DIVISION_CLUB.equals(cType)) {
		returnType = _iwrb.getLocalizedString(LOCALIZED_CLUB_TYPE_SINGLE_DIVISION, "Single Division");
	} else if(IWMemberConstants.META_DATA_CLUB_STATUS_NO_MEMBERS_CLUB.equals(cType)) {
		returnType = _iwrb.getLocalizedString(LOCALIZED_CLUB_TYPE_NO_MEMBERS, "No Members");
	} else if(report.isInUMFI()) {
		returnType = _iwrb.getLocalizedString(LOCALIZED_CLUB_TYPE_IM_UMFI, "UMFI member");
	} else if(report.isInActive()) {
		returnType = _iwrb.getLocalizedString(LOCALIZED_CLUB_TYPE_IS_INACTIVE, "Inactive");
	} else {
		returnType = "";
	}
	return returnType;
} 


private ReportableData addToIntegerCount(ReportableField reportableField, ReportableData reportableData, int intToAdd) {
	if(intToAdd>0 && reportableData!=null){//update count
		Integer count = (Integer)reportableData.getFieldValue(reportableField);
		if(count!=null) {
			count = new Integer(count.intValue()+intToAdd);
		} else {
			if(intToAdd>0){
				count = new Integer(intToAdd);
			}
			else{
				count = new Integer(0);
			}
		}
		reportableData.addData(reportableField,count);//swap
	}
	
	return reportableData;
}

private String getLeagueIdentifier(WorkReportGroup league) {
	//for the page separations
	StringBuffer leagueBuf = new StringBuffer();
	leagueBuf.append( (league.getNumber()!=null)? league.getNumber()+" " : "" )
	.append( (league.getShortName()!=null)? league.getShortName() : "");
	
	//.append( (league.getName()!=null)? league.getName() : "");
	String leagueText=league.toString();
	
	if("".equals(leagueText)){
		leagueText = (league.getName()!=null)? league.getName() : "No League name!";
	}
	return leagueText;
}

private String getRegionalUnionIdentifier(WorkReport report) {
	StringBuffer ruBuf = new StringBuffer();
	ruBuf.append( (report.getRegionalUnionNumber()!=null)? report.getRegionalUnionNumber()+" " : "" )
	.append( (report.getRegionalUnionAbbreviation()!=null)? report.getRegionalUnionAbbreviation() : "");
	//.append("  ")
	//.append( (report.getRegionalUnionName()!=null)? report.getRegionalUnionName() : "");
	String regText = ruBuf.toString();
	if("".equals(regText)){
		regText = (report.getRegionalUnionName()!=null)? report.getRegionalUnionName() : "No Reg.Un.name!";
	}
	return regText;
}

}
