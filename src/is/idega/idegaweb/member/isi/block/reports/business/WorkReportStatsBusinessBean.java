package is.idega.idegaweb.member.isi.block.reports.business;

import is.idega.idegaweb.member.isi.block.reports.data.WorkReport;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup;
import is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler.ClubTypeDropDownMenu;
import is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler.WorkReportStatusDropDownMenu;
import is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler.YesNoDropDownMenu;
import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;
import is.idega.idegaweb.member.util.IWMemberConstants;

import java.rmi.RemoteException;
import java.text.DecimalFormat;
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
	private static final String LOCALIZED_MEMBERS_THIS_YEAR = "WorkReportStatsBusiness.members_this_year";
	private static final String LOCALIZED_MEMBERS_LAST_YEAR = "WorkReportStatsBusiness.members_last_year";
	private static final String LOCALIZED_PLAYERS_THIS_YEAR = "WorkReportStatsBusiness.players_this_year";
	private static final String LOCALIZED_PLAYERS_LAST_YEAR = "WorkReportStatsBusiness.players_last_year";
	private static final String LOCALIZED_COMPETITORS_THIS_YEAR = "WorkReportStatsBusiness.competitors_this_year";
	private static final String LOCALIZED_COMPETITORS_LAST_YEAR = "WorkReportStatsBusiness.competitors_last_year";
	private static final String LOCALIZED_CLUB_COUNT_INCACTIVE = "WorkReportStatsBusiness.club_count_inactive";
	private static final String LOCALIZED_CLUB_COUNT_THIS_YEAR = "WorkReportStatsBusiness.club_count_this_year";
	private static final String LOCALIZED_CLUB_COUNT_LAST_YEAR = "WorkReportStatsBusiness.club_count_last_year";
	private static final String LOCALIZED_TOTAL = "WorkReportStatsBusiness.total";
	private static final String LOCALIZED_MEMBERS_ANNUAL_CHANGE = "WorkReportStatsBusiness.member_annual_change";
	private static final String LOCALIZED_MEMBERS_ANNUAL_CHANGE_PERCENT = "WorkReportStatsBusiness.member_annual_change_percent";
	private static final String LOCALIZED_MEMBERS_ANNUAL_CHANGE_PERCENT_OF_TOTAL = "WorkReportStatsBusiness.member_annual_change_percent_of_total";
	private static final String LOCALIZED_PLAYERS_ANNUAL_CHANGE = "WorkReportStatsBusiness.player_annual_change";
	private static final String LOCALIZED_PLAYERS_ANNUAL_CHANGE_PERCENT = "WorkReportStatsBusiness.player_annual_change_percent";
	private static final String LOCALIZED_PLAYERS_ANNUAL_CHANGE_PERCENT_OF_TOTAL = "WorkReportStatsBusiness.player_annual_change_percent_of_total";
	private static final String LOCALIZED_PERCENT_REPORTS_DONE = "WorkReportStatsBusiness.percent_reports_done";
	private static final String LOCALIZED_COUNT = "WorkReportStatsBusiness.count";
	private static final String LOCALIZED_MEMBERS_SINGLE_DIVISION = "WorkReportStatsBusiness.single_division_members";
	private static final String LOCALIZED_MEMBERS_MULTI_DIVISION = "WorkReportStatsBusiness.multi_division_members";
	private static final String LOCALIZED_PLAYERS_SINGLE_DIVISION = "WorkReportStatsBusiness.single_division_players";
	private static final String LOCALIZED_PLAYERS_MULTI_DIVISION = "WorkReportStatsBusiness.multi_division_players";
	private static final String LOCALIZED_PERSON_NAME = "WorkReportStatsBusiness.person_name";
	private static final String LOCALIZED_PHONE = "WorkReportStatsBusiness.phone";
	private static final String LOCALIZED_ADDRESS = "WorkReportStatsBusiness.address";
	private static final String LOCALIZED_POSTALCODE = "WorkReportStatsBusiness.postalcode";
	private static final String LOCALIZED_EMAIL = "WorkReportStatsBusiness.email";
	
	
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
	private static final String FIELD_NAME_MEMBERS_THIS_YEAR = "members_this_year";
	private static final String FIELD_NAME_MEMBERS_LAST_YEAR = "members_last_year";
	private static final String FIELD_NAME_PLAYERS_THIS_YEAR = "players_this_year";
	private static final String FIELD_NAME_PLAYERS_LAST_YEAR = "players_last_year";
	private static final String FIELD_NAME_COMPETITORS_THIS_YEAR = "competitors_this_year";
	private static final String FIELD_NAME_COMPETITORS_LAST_YEAR = "competitors_last_year";
	private static final String FIELD_NAME_CLUB_COUNT_DONE = "club_count_done";
	private static final String FIELD_NAME_CLUB_COUNT_NOT_DONE = "club_count_not_done";
	private static final String FIELD_NAME_CLUB_COUNT_SOME_DONE = "club_count_some_done";
	private static final String FIELD_NAME_CLUB_COUNT_INACTIVE = "club_count_inactive";
	private static final String FIELD_NAME_CLUB_COUNT_THIS_YEAR = "club_count_this_year";
	private static final String FIELD_NAME_CLUB_COUNT_LAST_YEAR = "club_count_last_year";
	private static final String FIELD_NAME_MEMBERS_ANNUAL_CHANGE = "annual_member_change";
	private static final String FIELD_NAME_MEMBERS_ANNUAL_CHANGE_PERCENT = "annual_member_change_percent";
	private static final String FIELD_NAME_MEMBERS_ANNUAL_CHANGE_PERCENT_OF_TOTAL = "annual_member_change_percent_of_total";
	private static final String FIELD_NAME_PLAYERS_ANNUAL_CHANGE = "annual_player_change";
	private static final String FIELD_NAME_PLAYERS_ANNUAL_CHANGE_PERCENT = "annual_player_change_percent";
	private static final String FIELD_NAME_PLAYERS_ANNUAL_CHANGE_PERCENT_OF_TOTAL = "annual_player_change_percent_of_total";
	private static final String FIELD_NAME_PERCENT_REPORTS_DONE = "percent_reports_done";
	private static final String FIELD_NAME_COUNT = "count";
	private static final String FIELD_NAME_MEMBERS_SINGLE_DIVISION = "members_single_division";
	private static final String FIELD_NAME_MEMBERS_MULTI_DIVISION = "member_multi_division";
	private static final String FIELD_NAME_PLAYERS_SINGLE_DIVISION = "players_single_division";
	private static final String FIELD_NAME_PLAYERS_MULTI_DIVISION = "players_multi_division";
	private static final String FIELD_NAME_PERSON_NAME = "person_name";
	private static final String FIELD_NAME_PHONE = "phone";
	private static final String FIELD_NAME_ADDRESS = "address";
	private static final String FIELD_NAME_POSTALCODE = "postalcode";
	private static final String FIELD_NAME_EMAIL = "email";
	
	
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
	 */
	public ReportableCollection getCostPerPlayerStatisticsForLeaguesByYearAgeGenderAndLeaguesFiltering(final Integer year,Integer age,String gender, Collection leaguesFilter)throws RemoteException {
		
		//initialize stuff
		int selectedAge = (age!=null)?age.intValue():-1;
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		boolean ageAndOrGenderCompare = false;
		
		if(selectedAge>0 || gender!=null){
			ageAndOrGenderCompare = true;
		}
		
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
		
		ReportableField ageAndGenderCount = new ReportableField("ageAndGenderCount", Integer.class);
		reportCollection.addField(ageAndGenderCount);
		
		
		//Real data stuff
		//Gathering data
		//Get all the workreports (actually more than needed)
		//then for each get its leagues and the count for
		//each age and create a row and insert into an ordered map by league
		//then insert into the final report collection.
		Collection clubs = getWorkReportBusiness().getWorkReportsByYearRegionalUnionsAndClubs(year.intValue(), null, null );
		
		Map leagueStatsMap = new TreeMap();
		List leagueGroupIdList = getGroupIdListFromLeagueGroupCollection(year,leaguesFilter,true);
		
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
						leagueStatsData.addData(ageAndGenderCount,new Integer(0));
						
					}
					
					
					
					//add to counts
					
					int totalPlayerCount = 0;
					int cost = 0;
					
					int playerCountByAgeAndGender = 0;
					if(ageAndOrGenderCompare){	
						playerCountByAgeAndGender = getWorkReportBusiness().getCountOfPlayersOfEqualAgeAndGenderByWorkReportAndWorkReportGroup(selectedAge,gender,report,league);
						leagueStatsData = addToIntegerCount(ageAndGenderCount,leagueStatsData,playerCountByAgeAndGender);
					}
					
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
			Integer totalAgeAndGenderCount = (Integer)row.getFieldValue(ageAndGenderCount);
			Integer totalCostForleague = (Integer)row.getFieldValue(totalCost);
			Integer totalCountOfPlayers = (Integer)row.getFieldValue(totalCountOfPlayersForLeague);
			Integer costPerPlayer = new Integer(0);
			
			if(ageAndOrGenderCompare && totalAgeAndGenderCount.intValue()>0){
				costPerPlayer = new Integer( (totalCostForleague.intValue()*totalAgeAndGenderCount.intValue())/ (totalCountOfPlayers.intValue()*totalCountOfPlayers.intValue()));
			}
			else if(totalCountOfPlayers.intValue()!=0){
				costPerPlayer = new Integer(totalCostForleague.intValue()/totalCountOfPlayers.intValue());
			}
			
			row.addData(costPerPlayers,costPerPlayer);
			
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
				regData.addData(bothGendersEqualOrOver, new Integer(0));
				regData.addData(bothGendersUnderAge, new Integer(0));
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
				regData.addData(bothGendersEqualOrOver, new Integer(0));
				regData.addData(bothGendersUnderAge, new Integer(0));
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
		
		/*ReportableField clubNumber = new ReportableField(FIELD_NAME_CLUB_NUMBER, String.class);
		clubNumber.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_CLUB_NUMBER, "Club number"), currentLocale);
		reportCollection.addField(clubNumber);*/
		
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

			regData.addData(clubName, report.getGroupNumber() + " " + report.getGroupName());
			//regData.addData(clubNumber, report.getGroupNumber());
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

		ReportableField[] sortFields = new ReportableField[] {clubName};
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
	
		/*ReportableField clubNumber = new ReportableField(FIELD_NAME_CLUB_NUMBER, String.class);
		clubNumber.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_CLUB_NUMBER, "Club number"), currentLocale);
		reportCollection.addField(clubNumber);*/
	
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
		
			regData.addData(clubName, report.getGroupNumber() + " " + report.getGroupName());
			//regData.addData(clubNumber, report.getGroupNumber());
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
	
		ReportableField[] sortFields = new ReportableField[] {clubName};
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
		workReportStatusInfo.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WORK_REPORT_STATUS_INFO, "Work Report Status Info"), currentLocale);
		reportCollection.addField(workReportStatusInfo);
		
		ReportableField workReportStatusRemarks = new ReportableField(FIELD_NAME_WORK_REPORT_STATUS_REMARKS, String.class);
		workReportStatusRemarks.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_WORK_REPORT_STATUS_REMARKS, "Work Report Status Remarks"), currentLocale);
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
			System.out.println("Status of report is " + reportStatus);
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
		
		boolean showAll = true;
		if(YesNoDropDownMenu.YES.equals(onlyTotals)) {
			System.out.println("Showing only totals");
			showAll = false;
		} else {
			System.out.println("Showing all");
		}
		
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
		
		ReportableField orderField = new ReportableField("only_used_for_ordering", String.class);
		orderField.setLocalizedName("only_used_for_ordering", currentLocale);
		reportCollection.addField(orderField);

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
		
		
		WorkReportGroup mainBoardGroup = getWorkReportBusiness().getMainBoardWorkReportGroup(year.intValue());
		Integer mbId = mainBoardGroup.getGroupId();
		
		Collection clubs = getWorkReportBusiness().getWorkReportsForRegionalUnionCollection(year.intValue(), regionalUnionsFilter);
		Iterator iter = clubs.iterator();
		while (iter.hasNext()) {
			//the club
			WorkReport report = (WorkReport) iter.next();
			String cName = report.getGroupName();
			
			// fetch club info
			int totalWomenMembersUnder = 0;
			int totalWomenMembersEqualOrOver = 0;
			int totalMenMembersUnder = 0;
			int totalMenMembersEqualOrOver = 0;
			int totalMembersUnder = 0;
			int totalMembersEqualOrOver = 0;
			int totalAllMembers = 0;
			int totalWomenPlayersUnder = 0;
			int totalWomenPlayersEqualOrOver = 0;
			int totalMenPlayersUnder = 0;
			int totalMenPlayersEqualOrOver = 0;
			int totalPlayersUnder = 0;
			int totalPlayersEqualOrOver = 0;
			int totalAllPlayers = 0;
			try {
				Collection leagues = report.getLeagues();
				Iterator iterator = leagues.iterator();
				while (iterator.hasNext()) {
					WorkReportGroup league = (WorkReportGroup) iterator.next();
					
					boolean isMainBoard = mbId.equals(league.getGroupId());
					
					ReportableData regData = showAll?(new ReportableData()):null;
					
					String leagueIdentifier = getLeagueIdentifier(league);
					if(showAll) {
						regData.addData(clubName, cName);
						regData.addData(orderField, "a");
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
					}
			
					//fetch member stats for this division	
					// @TODO (jonas) don't know how to fetch this data, have to ask eiki
					int womenMembersUnder = 0;
					int womenMembersEqualOrOver = 0;
					int menMembersUnder = 0;
					int menMembersEqualOrOver = 0;
					int membersUnder = 0;
					int membersEqualOrOver = 0;
					int allMembers = 0;
					if(isMainBoard) {
						womenMembersUnder = getWorkReportBusiness().getCountOfFemalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(age, report, league);
						womenMembersEqualOrOver = getWorkReportBusiness().getCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age, report, league);
						menMembersUnder = getWorkReportBusiness().getCountOfMalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(age, report, league);
						menMembersEqualOrOver = getWorkReportBusiness().getCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age, report, league);
						membersUnder = womenMembersUnder + menMembersUnder;
						membersEqualOrOver = womenMembersEqualOrOver + menMembersEqualOrOver;
						allMembers = membersUnder + membersEqualOrOver;
						
						totalWomenMembersUnder += womenMembersUnder;
						totalWomenMembersEqualOrOver += womenMembersEqualOrOver;
						totalMenMembersUnder += menMembersUnder;
						totalMenMembersEqualOrOver += menMembersEqualOrOver;
						totalMembersUnder += membersUnder;
						totalMembersEqualOrOver += membersEqualOrOver;
						totalAllMembers += allMembers;
					}
					if(showAll) {
						regData.addData(womenMembersUnderAge, new Integer(womenMembersUnder));
						regData.addData(womenMembersOverOrEqualAgeLimit, new Integer(womenMembersEqualOrOver));
						regData.addData(menMembersUnderAge, new Integer(menMembersUnder));
						regData.addData(menMembersOverOrEqualAgeLimit, new Integer(menMembersEqualOrOver));
						regData.addData(bothGendersMembersUnderAge, new Integer(membersUnder));
						regData.addData(bothGendersMembersEqualOrOver, new Integer(membersEqualOrOver));
						regData.addData(bothGendersMembersAllAge, new Integer(allMembers));
					}
					
					// fetch player stats for this division
					int womenPlayersUnder = 0;
					int womenPlayersEqualOrOver = 0;
					int menPlayersUnder = 0;
					int menPlayersEqualOrOver = 0;
					int playersUnder = 0;
					int playersEqualOrOver = 0;
					int allPlayers = 0;
					if(!isMainBoard) {
						womenPlayersUnder = getWorkReportBusiness().getCountOfFemalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(age, report, league);
						womenPlayersEqualOrOver = getWorkReportBusiness().getCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age, report, league);
						menPlayersUnder = getWorkReportBusiness().getCountOfMalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(age, report, league);
						menPlayersEqualOrOver = getWorkReportBusiness().getCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age, report, league);
						playersUnder = womenPlayersUnder + menPlayersUnder;
						playersEqualOrOver = womenPlayersEqualOrOver + menPlayersEqualOrOver;
						allPlayers = playersUnder + playersEqualOrOver;
						
						totalWomenPlayersUnder += womenPlayersUnder;
						totalWomenPlayersEqualOrOver += womenPlayersEqualOrOver;
						totalMenPlayersUnder += menPlayersUnder;
						totalMenPlayersEqualOrOver += menPlayersEqualOrOver;
						totalPlayersUnder += playersUnder;
						totalPlayersEqualOrOver += playersEqualOrOver;
						totalAllPlayers += allPlayers;
					}
					if(showAll) {
						regData.addData(womenPlayersUnderAge, new Integer(womenPlayersUnder));
						regData.addData(womenPlayersOverOrEqualAgeLimit, new Integer(womenPlayersEqualOrOver));
						regData.addData(menPlayersUnderAge, new Integer(menPlayersUnder));
						regData.addData(menPlayersOverOrEqualAgeLimit, new Integer(menPlayersEqualOrOver));
						regData.addData(bothGendersPlayersUnderAge, new Integer(playersUnder));
						regData.addData(bothGendersPlayersEqualOrOver, new Integer(playersEqualOrOver));
						regData.addData(bothGendersPlayersAllAge, new Integer(allPlayers));
					}
					if(showAll) {
						reportCollection.add(regData);
					}
				}
				
				ReportableData totals = new ReportableData();
				
				totals.addData(clubName, cName);
				totals.addData(orderField, "b");
				totals.addData(divisionName, _iwrb.getLocalizedString(LOCALIZED_TOTAL, "TOTAL"));
				totals.addData(annualReportStatus, "");
				totals.addData(womenMembersUnderAge, new Integer(totalWomenMembersUnder));
				totals.addData(womenMembersOverOrEqualAgeLimit, new Integer(totalWomenMembersEqualOrOver));
				totals.addData(menMembersUnderAge, new Integer(totalMenMembersUnder));
				totals.addData(menMembersOverOrEqualAgeLimit, new Integer(totalMenMembersEqualOrOver));
				totals.addData(bothGendersMembersUnderAge, new Integer(totalMembersUnder));
				totals.addData(bothGendersMembersEqualOrOver, new Integer(totalMembersEqualOrOver));
				totals.addData(bothGendersMembersAllAge, new Integer(totalAllMembers));
				totals.addData(womenPlayersUnderAge, new Integer(totalWomenPlayersUnder));
				totals.addData(womenPlayersOverOrEqualAgeLimit, new Integer(totalWomenPlayersEqualOrOver));
				totals.addData(menPlayersUnderAge, new Integer(totalMenPlayersUnder));
				totals.addData(menPlayersOverOrEqualAgeLimit, new Integer(totalMenPlayersEqualOrOver));
				totals.addData(bothGendersPlayersUnderAge, new Integer(totalPlayersUnder));
				totals.addData(bothGendersPlayersEqualOrOver, new Integer(totalPlayersEqualOrOver));
				totals.addData(bothGendersPlayersAllAge, new Integer(totalAllPlayers));
				
				reportCollection.add(totals);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		ReportableField[] sortFields = new ReportableField[] {clubName, orderField, divisionName};
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);

		//finished return the collection
		return reportCollection;
	}
	
	/*
	 * Report B12.5.3 of the ISI Specs
	 */
	public ReportableCollection getAnnualChangeStatisticsForClubsByYearAndRegionalUnionsFilter (
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
		
		ReportableField regionalUnionName = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionName);

		ReportableField clubName = new ReportableField(FIELD_NAME_CLUB_NAME, String.class);
		clubName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_CLUB_NAME, "Club Name"), currentLocale);
		reportCollection.addField(clubName);

		// members
		ReportableField membersThisYear = new ReportableField(FIELD_NAME_MEMBERS_THIS_YEAR, Integer.class);
		membersThisYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEMBERS_THIS_YEAR, "Members this year"), currentLocale);
		reportCollection.addField(membersThisYear);
	
		ReportableField membersLastYear = new ReportableField(FIELD_NAME_MEMBERS_LAST_YEAR, Integer.class);
		membersLastYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEMBERS_LAST_YEAR, "Members last year"), currentLocale);
		reportCollection.addField(membersLastYear);
		
		ReportableField membersAnnualChangePercent = new ReportableField(FIELD_NAME_MEMBERS_ANNUAL_CHANGE_PERCENT, String.class);
		membersAnnualChangePercent.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEMBERS_ANNUAL_CHANGE_PERCENT, "Member Annual Change"), currentLocale);
		reportCollection.addField(membersAnnualChangePercent);
		
		// players
		ReportableField playersThisYear = new ReportableField(FIELD_NAME_PLAYERS_THIS_YEAR, Integer.class);
		playersThisYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_PLAYERS_THIS_YEAR, "Players this year"), currentLocale);
		reportCollection.addField(playersThisYear);

		ReportableField playersLastYear = new ReportableField(FIELD_NAME_PLAYERS_LAST_YEAR, Integer.class);
		playersLastYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_PLAYERS_LAST_YEAR, "Players last year"), currentLocale);
		reportCollection.addField(playersLastYear);
		
		ReportableField playersAnnualChangePercent = new ReportableField(FIELD_NAME_PLAYERS_ANNUAL_CHANGE_PERCENT, String.class);
		playersAnnualChangePercent.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_PLAYERS_ANNUAL_CHANGE_PERCENT, "Player Annual Change"), currentLocale);
		reportCollection.addField(playersAnnualChangePercent);
		
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
			WorkReport lastYearReport=null;
			try {
				lastYearReport = getWorkReportBusiness().getWorkReportHome().findWorkReportByGroupIdAndYearOfReport(report.getGroupId().intValue(),year.intValue()-1);
			}
			catch (FinderException e1) {
				System.err.println("WorkReportStatsBusiness : No report for year before :"+year);
			}
			//String cName = report.getGroupName();
			String regionalUnionIdentifier = getRegionalUnionIdentifier(report);

			//fetch the stats or initialize for this regional union (i.e. the one associated with regionalUnionIdentifier)
			ReportableData regData = new ReportableData();
			regData.addData(regionalUnionName, regionalUnionIdentifier);
			regData.addData(clubName, report.getGroupName());
			
			int mThisYear = getWorkReportBusiness().getCountOfMembersByWorkReport(report);
			int mLastYear = lastYearReport==null?0:getWorkReportBusiness().getCountOfMembersByWorkReport(lastYearReport);
			double mChange = (
			                  ((double) mThisYear) /
			                  ((double) (mLastYear>0?mLastYear:(mThisYear!=0?mThisYear:1)))
			                 ) * 100.0 
					           * (((mThisYear!=0 && mLastYear!=0) && (mThisYear>=mLastYear))?1.0:-1.0);
			
			
			/*new DecimalFormat("##0.#").format( 
					((($V{AllSum}!=null && $V{AllSum}.floatValue()>0)? $V{AllSum}.floatValue() : 0 )/
					 (($V{AllSumLastYear}!=null && $V{AllSumLastYear}.floatValue()>0)? $V{AllSumLastYear}.floatValue() : (($V{AllSum}!=null && $V{AllSum}.floatValue()>0)? $V{AllSum}.floatValue() : 1 )))
					* 100.0 * ( (($V{AllSum}!=null && $V{AllSumLastYear}!=null) && ($V{AllSum}.intValue()>= $V{AllSumLastYear}.intValue()) ) ? 1.0 : -1.0 ) 
			)*/
			
			int pThisYear = getWorkReportBusiness().getCountOfPlayersByWorkReport(report);
			int pLastYear = lastYearReport==null?0:getWorkReportBusiness().getCountOfPlayersByWorkReport(lastYearReport);
			double pChange = (
			                  ((double) pThisYear) /
			                  ((double) (pLastYear>0?pLastYear:(pThisYear!=0?pThisYear:1)))
			                 ) * 100.0 
			                   * (((pThisYear!=0 && pLastYear!=0) && (pThisYear>=pLastYear))?1.0:-1.0);
			
			regData.addData(membersThisYear, new Integer(mThisYear));
			regData.addData(membersLastYear, new Integer(mLastYear));
			regData.addData(membersAnnualChangePercent, (new DecimalFormat("##0.#")).format(mChange));
			regData.addData(playersThisYear, new Integer(pThisYear));
			regData.addData(playersLastYear, new Integer(pLastYear));
			regData.addData(playersAnnualChangePercent, (new DecimalFormat("##0.#")).format(pChange));
			
			reportCollection.add(regData);
		}

		ReportableField[] sortFields = new ReportableField[] {regionalUnionName, clubName};
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);
	
		//finished return the collection
		return reportCollection;
	}
	
	
	/*
	 * Report B12.5.4 of the ISI Specs
	 */
	public ReportableCollection getAnnualChangeStatisticsForClubsByYearAndRegionalUnionsFilter (
			final Integer year,
			Collection regionalUnionsFilter, 
			String umfiOnly)
	throws RemoteException {

		//initialize stuff
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
	
		ReportableField regionalUnionName = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionName);

		// stats for club work report status
		ReportableField clubCountDone = new ReportableField(FIELD_NAME_CLUB_COUNT_DONE, Integer.class);
		clubCountDone.setLocalizedName(_iwrb.getLocalizedString(WorkReportStatusDropDownMenu.LOCALIZED_STATUS_DONE, "Report done"), currentLocale);
		reportCollection.addField(clubCountDone);

		ReportableField clubCountNotDone = new ReportableField(FIELD_NAME_CLUB_COUNT_NOT_DONE, Integer.class);
		clubCountNotDone.setLocalizedName(_iwrb.getLocalizedString(WorkReportStatusDropDownMenu.LOCALIZED_STATUS_NOT_DONE, "Report not done"), currentLocale);
		reportCollection.addField(clubCountNotDone);

		ReportableField clubCountSomeDone = new ReportableField(FIELD_NAME_CLUB_COUNT_SOME_DONE, Integer.class);
		clubCountSomeDone.setLocalizedName(_iwrb.getLocalizedString(WorkReportStatusDropDownMenu.LOCALIZED_STATUS_SOME_DONE, "Report partialy done"), currentLocale);
		reportCollection.addField(clubCountSomeDone);
	
		ReportableField clubCountInactive = new ReportableField(FIELD_NAME_CLUB_COUNT_INACTIVE, Integer.class);
		clubCountInactive.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_CLUB_COUNT_INCACTIVE, "Inactive clubs"), currentLocale);
		reportCollection.addField(clubCountInactive);

		ReportableField percentReportsDone = new ReportableField(FIELD_NAME_PERCENT_REPORTS_DONE, String.class);
		percentReportsDone.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_PERCENT_REPORTS_DONE, "Percent reports done"), currentLocale);
		reportCollection.addField(percentReportsDone);

		// stats for members
		ReportableField membersThisYear = new ReportableField(FIELD_NAME_MEMBERS_THIS_YEAR, Integer.class);
		membersThisYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEMBERS_THIS_YEAR, "Members this year"), currentLocale);
		reportCollection.addField(membersThisYear);

		ReportableField membersLastYear = new ReportableField(FIELD_NAME_MEMBERS_LAST_YEAR, Integer.class);
		membersLastYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEMBERS_LAST_YEAR, "Members last year"), currentLocale);
		reportCollection.addField(membersLastYear);

		ReportableField membersAnnualChangePercent = new ReportableField(FIELD_NAME_MEMBERS_ANNUAL_CHANGE_PERCENT, String.class);
		membersAnnualChangePercent.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEMBERS_ANNUAL_CHANGE_PERCENT, "Member Annual Change"), currentLocale);
		reportCollection.addField(membersAnnualChangePercent);		

		// stats for players
		ReportableField playersThisYear = new ReportableField(FIELD_NAME_PLAYERS_THIS_YEAR, Integer.class);
		playersThisYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_PLAYERS_THIS_YEAR, "Players this year"), currentLocale);
		reportCollection.addField(playersThisYear);

		ReportableField playersLastYear = new ReportableField(FIELD_NAME_PLAYERS_LAST_YEAR, Integer.class);
		playersLastYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_PLAYERS_LAST_YEAR, "Players last year"), currentLocale);
		reportCollection.addField(playersLastYear);

		ReportableField playersAnnualChangePercent = new ReportableField(FIELD_NAME_PLAYERS_ANNUAL_CHANGE_PERCENT, String.class);
		playersAnnualChangePercent.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_PLAYERS_ANNUAL_CHANGE_PERCENT, "Player Annual Change"), currentLocale);
		reportCollection.addField(playersAnnualChangePercent);

		
		Collection clubs = getWorkReportBusiness().getWorkReportsForRegionalUnionCollection(year.intValue(), regionalUnionsFilter);
		Map regionalUnionsStatsMap = new TreeMap();
		//Iterating through workreports and creating report data 
		Iterator iter = clubs.iterator();
		int totalDone = 0;
		int totalNotDone = 0;
		int totalSomeDone = 0;
		int totalInactive = 0;
		int totalMembersThisYear = 0;
		int totalMembersLastYear = 0;
		int totalPlayersThisYear = 0;
		int totalPlayersLastYear = 0;
		DecimalFormat format = new DecimalFormat("##0.#");
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
				regData.addData(clubCountDone, new Integer(0));
				regData.addData(clubCountNotDone, new Integer(0));
				regData.addData(clubCountSomeDone, new Integer(0));
				regData.addData(clubCountInactive, new Integer(0));
				regData.addData(percentReportsDone, "");
				
				regData.addData(membersThisYear, new Integer(0));
				regData.addData(membersLastYear, new Integer(0));
				regData.addData(membersAnnualChangePercent, "");

				regData.addData(playersThisYear, new Integer(0));
				regData.addData(playersLastYear, new Integer(0));
				regData.addData(playersAnnualChangePercent, "");
			}
			
			String reportStatus = report.getStatus();
			ReportableField toAddTo = null;
			if(WorkReportConstants.WR_STATUS_DONE.equals(reportStatus)) {
				toAddTo = clubCountDone;
				totalDone++;
			} else if(WorkReportConstants.WR_STATUS_NOT_DONE.equals(reportStatus)) {
				toAddTo = clubCountNotDone;
				totalNotDone++;
			} else if(WorkReportConstants.WR_STATUS_SOME_DONE.equals(reportStatus)) {
				toAddTo = clubCountSomeDone;
				totalSomeDone++;
			}
			if(toAddTo!=null) {
				regData = addToIntegerCount(toAddTo, regData, 1);
			}
			
			if(report.isInActive()) {
				regData = addToIntegerCount(clubCountInactive, regData, 1);
				totalInactive++;
			}
			int done = ((Integer)regData.getFieldValue(clubCountDone)).intValue();
			int notDone = ((Integer)regData.getFieldValue(clubCountNotDone)).intValue();
			//int change = ((done+notDone)==0)?-1:((100*done)/(done+notDone));
			/*double change = (
			                  ((double) done) /
			                  ((double) (notDone>0?notDone:(done!=0?done:1)))
			                ) * 100.0 
			                  * (((done!=0 && notDone!=0) && (done>=notDone))?1.0:-1.0);*/
			double change = (done+notDone)==0?1.0:(done/(done+notDone));
			regData.addData(percentReportsDone, format.format(change));
			
			int mThisYear = getWorkReportBusiness().getCountOfMembersByWorkReport(report);
			int mLastYear = lastYearReport==null?0:getWorkReportBusiness().getCountOfMembersByWorkReport(lastYearReport);
			totalMembersThisYear += mThisYear;
			totalMembersLastYear += mLastYear;
			regData = addToIntegerCount(membersThisYear, regData, mThisYear);
			regData = addToIntegerCount(membersLastYear, regData, mLastYear);
			int now = ((Integer)regData.getFieldValue(membersThisYear)).intValue();
			int last = ((Integer)regData.getFieldValue(membersLastYear)).intValue();
			//change = (last==0)?-1:((100*now)/(last));
			change = (
			          ((double) now) /
			          ((double) (last>0?last:(now!=0?now:1)))
			         ) * 100.0 
			           * (((now!=0 && last!=0) && (now>=last))?1.0:-1.0);
			regData.addData(membersAnnualChangePercent, format.format(change));
						
			int pThisYear = getWorkReportBusiness().getCountOfPlayersByWorkReport(report);
			int pLastYear = lastYearReport==null?0:getWorkReportBusiness().getCountOfPlayersByWorkReport(lastYearReport);
			totalPlayersThisYear += pThisYear;
			totalPlayersLastYear += pLastYear;
			regData = addToIntegerCount(playersThisYear, regData, pThisYear);
			regData = addToIntegerCount(playersLastYear, regData, pLastYear);
			now = ((Integer)regData.getFieldValue(playersThisYear)).intValue();
			last = ((Integer)regData.getFieldValue(playersLastYear)).intValue();
			//change = (last==0)?-1:((100*now)/(last));
			change = (
			          ((double) now) /
			          ((double) (last>0?last:(now!=0?now:1)))
			         ) * 100.0 
			           * (((now!=0 && last!=0) && (now>=last))?1.0:-1.0);
			regData.addData(playersAnnualChangePercent, format.format(change));
		}

		// iterate through the ordered map and ordered lists and add to the final collection
		reportCollection.addAll(regionalUnionsStatsMap.values());

		ReportableField[] sortFields = new ReportableField[] {regionalUnionName};
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);

		/*ReportableData regData = new ReportableData();
		regData.addData(regionalUnionName, _iwrb.getLocalizedString(LOCALIZED_TOTAL, "TOTAL"));
		regData.addData(clubCountDone, new Integer(totalDone));
		regData.addData(clubCountNotDone, new Integer(totalNotDone));
		regData.addData(clubCountSomeDone, new Integer(totalSomeDone));
		regData.addData(clubCountInactive, new Integer(totalInactive));
		double change = (totalDone+totalNotDone)==0?1.0:(totalDone/(totalDone+totalNotDone));
		regData.addData(percentReportsDone, format.format(change));
		
		regData.addData(membersThisYear, new Integer(totalMembersThisYear));
		regData.addData(membersLastYear, new Integer(totalMembersLastYear));
		change = (
		          ((double) totalMembersThisYear) /
		          ((double) (totalMembersLastYear>0?totalMembersLastYear:(totalMembersThisYear!=0?totalMembersThisYear:1)))
		         ) * 100.0
		           * (((totalMembersThisYear!=0 && totalMembersLastYear!=0) && (totalMembersThisYear>=totalMembersLastYear))?1.0:-1.0);
		regData.addData(membersAnnualChangePercent, format.format(change));

		regData.addData(playersThisYear, new Integer(totalPlayersThisYear));
		regData.addData(playersLastYear, new Integer(totalPlayersLastYear));
		change = (
		          ((double) totalPlayersThisYear) /
		          ((double) (totalPlayersLastYear>0?totalPlayersLastYear:(totalPlayersThisYear!=0?totalPlayersThisYear:1)))
				 ) * 100.0
				   * (((totalPlayersThisYear!=0 && totalPlayersLastYear!=0) && (totalPlayersThisYear>=totalPlayersLastYear))?1.0:-1.0);
		regData.addData(playersAnnualChangePercent, format.format(change));
		
		reportCollection.add(regData);*/
		
		double change = (totalDone+totalNotDone)==0?1.0:(totalDone/(totalDone+totalNotDone));
		reportCollection.addExtraHeaderParameter(
				"percentReportsDoneAll",
				_iwrb.getLocalizedString(LOCALIZED_PERCENT_REPORTS_DONE, "Percent reports done"),
				"percentReportsDoneAll",
				format.format(change));
		
		//finished return the collection
		return reportCollection;
	}
	
	
	/*
	 * Report B12.5.5 of the ISI Specs
	 */
	public ReportableCollection getRegionalUnionsAnnualComparisonByYearAndRegionalUnionsFilter (
			final Integer year,
			Collection regionalUnionsFilter)
	throws RemoteException {

		//initialize stuff
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

		ReportableField regionalUnionName = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionName);

		// stats for members
		ReportableField membersThisYear = new ReportableField(FIELD_NAME_MEMBERS_THIS_YEAR, Integer.class);
		membersThisYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEMBERS_THIS_YEAR, "Members this year"), currentLocale);
		reportCollection.addField(membersThisYear);

		ReportableField membersLastYear = new ReportableField(FIELD_NAME_MEMBERS_LAST_YEAR, Integer.class);
		membersLastYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEMBERS_LAST_YEAR, "Members last year"), currentLocale);
		reportCollection.addField(membersLastYear);

		ReportableField membersAnnualChange = new ReportableField(FIELD_NAME_MEMBERS_ANNUAL_CHANGE, Integer.class);
		membersAnnualChange.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEMBERS_ANNUAL_CHANGE, "Member Annual Change"), currentLocale);
		reportCollection.addField(membersAnnualChange);
		
		ReportableField membersAnnualChangePercent = new ReportableField(FIELD_NAME_MEMBERS_ANNUAL_CHANGE_PERCENT, String.class);
		membersAnnualChangePercent.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEMBERS_ANNUAL_CHANGE_PERCENT, "Member Annual Change"), currentLocale);
		reportCollection.addField(membersAnnualChangePercent);
		
		ReportableField membersAnnualChangePercentOfTotal = new ReportableField(FIELD_NAME_MEMBERS_ANNUAL_CHANGE_PERCENT_OF_TOTAL, String.class);
		membersAnnualChangePercentOfTotal.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEMBERS_ANNUAL_CHANGE_PERCENT_OF_TOTAL, "Member Annual Change of Total"), currentLocale);
		reportCollection.addField(membersAnnualChangePercentOfTotal);

		// stats for players
		ReportableField playersThisYear = new ReportableField(FIELD_NAME_PLAYERS_THIS_YEAR, Integer.class);
		playersThisYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_PLAYERS_THIS_YEAR, "Players this year"), currentLocale);
		reportCollection.addField(playersThisYear);

		ReportableField playersLastYear = new ReportableField(FIELD_NAME_PLAYERS_LAST_YEAR, Integer.class);
		playersLastYear.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_PLAYERS_LAST_YEAR, "Players last year"), currentLocale);
		reportCollection.addField(playersLastYear);

		ReportableField playersAnnualChange = new ReportableField(FIELD_NAME_PLAYERS_ANNUAL_CHANGE, Integer.class);
		playersAnnualChange.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_PLAYERS_ANNUAL_CHANGE, "Players Annual Change"), currentLocale);
		reportCollection.addField(playersAnnualChange);
		
		ReportableField playersAnnualChangePercent = new ReportableField(FIELD_NAME_PLAYERS_ANNUAL_CHANGE_PERCENT, String.class);
		playersAnnualChangePercent.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_PLAYERS_ANNUAL_CHANGE_PERCENT, "Players Annual Change"), currentLocale);
		reportCollection.addField(playersAnnualChangePercent);
		
		ReportableField playersAnnualChangePercentOfTotal = new ReportableField(FIELD_NAME_PLAYERS_ANNUAL_CHANGE_PERCENT_OF_TOTAL, String.class);
		playersAnnualChangePercentOfTotal.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_PLAYERS_ANNUAL_CHANGE_PERCENT_OF_TOTAL, "Players Annual Change of Total"), currentLocale);
		reportCollection.addField(playersAnnualChangePercentOfTotal);

	
		Collection clubs = getWorkReportBusiness().getWorkReportsForRegionalUnionCollection(year.intValue(), regionalUnionsFilter);
		Map regionalUnionsStatsMap = new TreeMap();
		//Iterating through workreports and creating report data 
		Iterator iter = clubs.iterator();
		int mThisYearTotal = 0;
		int mLastYearTotal = 0;
		int pThisYearTotal = 0;
		int pLastYearTotal = 0;
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
			
				regData.addData(membersThisYear, new Integer(0));
				regData.addData(membersLastYear, new Integer(0));
				regData.addData(membersAnnualChange, new Integer(0));
				regData.addData(membersAnnualChangePercent, "");
				regData.addData(membersAnnualChangePercentOfTotal, "");
				
				regData.addData(playersThisYear, new Integer(0));
				regData.addData(playersLastYear, new Integer(0));
				regData.addData(playersAnnualChange, new Integer(0));
				regData.addData(playersAnnualChangePercent, "");
				regData.addData(playersAnnualChangePercentOfTotal, "");
				
				regionalUnionsStatsMap.put(regionalUnionIdentifier,regData);
			}
		
			int mThisYear = getWorkReportBusiness().getCountOfMembersByWorkReport(report);
			int mLastYear = lastYearReport==null?0:getWorkReportBusiness().getCountOfMembersByWorkReport(lastYearReport);
			mThisYearTotal += mThisYear;
			mLastYearTotal += mLastYear;
			regData = addToIntegerCount(membersThisYear, regData, mThisYear);
			regData = addToIntegerCount(membersLastYear, regData, mLastYear);
			regData = addToIntegerCount(membersAnnualChange, regData, mThisYear - mLastYear);
			int now = ((Integer)regData.getFieldValue(membersThisYear)).intValue();
			int last = ((Integer)regData.getFieldValue(membersLastYear)).intValue();
			int change = (last==0)?-1:((100*now)/(last));
			regData.addData(membersAnnualChangePercent, change==-1?"":Integer.toString(change));
					
			int pThisYear = getWorkReportBusiness().getCountOfPlayersByWorkReport(report);
			int pLastYear = lastYearReport==null?0:getWorkReportBusiness().getCountOfPlayersByWorkReport(lastYearReport);
			pThisYearTotal += pThisYear;
			pLastYearTotal += pLastYear;
			regData = addToIntegerCount(playersThisYear, regData, pThisYear);
			regData = addToIntegerCount(playersLastYear, regData, pLastYear);
			regData = addToIntegerCount(playersAnnualChange, regData, pThisYear - pLastYear);
			now = ((Integer)regData.getFieldValue(playersThisYear)).intValue();
			last = ((Integer)regData.getFieldValue(playersLastYear)).intValue();
			change = (last==0)?-1:((100*now)/(last));
			regData.addData(playersAnnualChangePercent, change==-1?"":Integer.toString(change));
		}

		Collection regDataCollection = regionalUnionsStatsMap.values();
		// iterate through the ordered map and ordered lists and add to the final collection
		reportCollection.addAll(regDataCollection);

		ReportableField[] sortFields = new ReportableField[] {regionalUnionName};
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);
		
		int mChangeTotal = mThisYearTotal - mLastYearTotal;
		int pChangeTotal = pThisYearTotal - pLastYearTotal;
		// get the percentage from total and create last row
		Iterator rData = regDataCollection.iterator();
		DecimalFormat format = new DecimalFormat("##0.#");
		double macptTotal = 0;
		double pacptTotal = 0;
		while(rData.hasNext()) {
			ReportableData rd = (ReportableData) rData.next();
			int mc = ((Integer)rd.getFieldValue(membersAnnualChange)).intValue();
//			double mwn = 100.0*((double)mc)/((double)mChangeTotal);

			double mwn = (
													((double)mc) /
													((double) (mThisYearTotal>0?mThisYearTotal:(mc!=0?mc:1)))
													) * 100.0
													* (((mc!=0 && mThisYearTotal!=0) && (mc>=mThisYearTotal))?1.0:-1.0);
			
			String value = format.format(mwn);
			rd.addData(membersAnnualChangePercentOfTotal, value);
			
			int pc = ((Integer)rd.getFieldValue(playersAnnualChange)).intValue();
	//		double pwn = 100.0*((double)pc)/((double)pChangeTotal);
			
			double pwn = (
													((double)pc) /
													((double) (pThisYearTotal>0?pThisYearTotal:(pc!=0?pc:1)))
													)  * 100.0
													* (((pc!=0 && pThisYearTotal!=0) && (pc>=pThisYearTotal))?1.0:-1.0);
			
			value = format.format(pwn);
			rd.addData(playersAnnualChangePercentOfTotal, value);
			macptTotal += mwn;
			pacptTotal += pwn;
		}
		
		reportCollection.addExtraHeaderParameter(
				"annualMemberChangePercentOfTotalAll",
				_iwrb.getLocalizedString(LOCALIZED_MEMBERS_ANNUAL_CHANGE_PERCENT_OF_TOTAL, "Members Annual Change of Total"),
				"annualMemberChangePercentOfTotalAll",
				format.format(macptTotal));
		
		reportCollection.addExtraHeaderParameter(
				"annualPlayerChangePercentOfTotalAll",
				_iwrb.getLocalizedString(LOCALIZED_PLAYERS_ANNUAL_CHANGE_PERCENT_OF_TOTAL, "Players Annual Change of Total"),
				"annualPlayerChangePercentOfTotalAll",
				format.format(pacptTotal));
		
		
		/*
		double mChangeTotalPercent = (mLastYearTotal==0)?-1:((100*((double)mThisYearTotal))/((double)mLastYearTotal));
		double pChangeTotalPercent = (pLastYearTotal==0)?-1:((100*((double)pThisYearTotal))/((double)pLastYearTotal));
		ReportableData regData = new ReportableData();
		regData.addData(regionalUnionName, _iwrb.getLocalizedString(LOCALIZED_TOTAL, "TOTAL"));
		regData.addData(membersThisYear, new Integer(mThisYearTotal));
		regData.addData(membersLastYear, new Integer(mLastYearTotal));
		regData.addData(membersAnnualChange, new Integer(mChangeTotal));
		regData.addData(membersAnnualChangePercent, (mChangeTotalPercent<0.0)?"":format.format(mChangeTotalPercent));
		regData.addData(membersAnnualChangePercentOfTotal, format.format(macptTotal));
		regData.addData(playersThisYear, new Integer(pThisYearTotal));
		regData.addData(playersLastYear, new Integer(pLastYearTotal));
		regData.addData(playersAnnualChange, new Integer(pChangeTotal));
		regData.addData(playersAnnualChangePercent, (pChangeTotalPercent<0.0)?"":format.format(pChangeTotalPercent));
		regData.addData(playersAnnualChangePercentOfTotal, format.format(pacptTotal));
		
		reportCollection.add(regData);*/

		//finished return the collection
		return reportCollection;
	}
	
	
	/*
	 * Report B12.6.1 of the ISI Specs
	 */
	public ReportableCollection sixDotOne () throws RemoteException {

		//initialize stuff
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
		
		ReportableField regionalUnionName = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionName);

		ReportableField clubName = new ReportableField(FIELD_NAME_CLUB_NAME, String.class);
		clubName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_CLUB_NAME, "Club name"), currentLocale);
		reportCollection.addField(clubName);
		
		ReportableField count = new ReportableField(FIELD_NAME_COUNT, String.class);
		count.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_COUNT, "Count"), currentLocale);
		reportCollection.addField(count);
				
		//finished return the collection
		return reportCollection;
	}
	
	/*
	 * Report B12.6.2 of the ISI Specs
	 */
	public ReportableCollection sixDotTwo () throws RemoteException {

		//initialize stuff
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
		
		ReportableField leagueString = new ReportableField(FIELD_NAME_LEAGUE_NAME, String.class);
		leagueString.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_LEAGUE_INFO, "League"), currentLocale);
		reportCollection.addField(leagueString);

		ReportableField clubName = new ReportableField(FIELD_NAME_CLUB_NAME, String.class);
		clubName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_CLUB_NAME, "Club name"), currentLocale);
		reportCollection.addField(clubName);
		
		ReportableField count = new ReportableField(FIELD_NAME_COUNT, String.class);
		count.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_COUNT, "Count"), currentLocale);
		reportCollection.addField(count);
		
		//finished return the collection
		return reportCollection;
	}
	
	/*
	 * Report B12.6.3 of the ISI Specs
	 */
	public ReportableCollection sixDotThree () throws RemoteException {

		//initialize stuff
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
		
		ReportableField regionalUnionName = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionName);

		ReportableField singleDivisionMembers = new ReportableField(FIELD_NAME_MEMBERS_SINGLE_DIVISION, String.class);
		singleDivisionMembers.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEMBERS_SINGLE_DIVISION, "Single Division Members"), currentLocale);
		reportCollection.addField(singleDivisionMembers);
		
		ReportableField multiDivisionMembers = new ReportableField(FIELD_NAME_MEMBERS_MULTI_DIVISION, String.class);
		multiDivisionMembers.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEMBERS_MULTI_DIVISION, "Multi Division Members"), currentLocale);
		reportCollection.addField(multiDivisionMembers);
		
		ReportableField singleDivisionPlayers = new ReportableField(FIELD_NAME_PLAYERS_SINGLE_DIVISION, String.class);
		singleDivisionPlayers.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_PLAYERS_SINGLE_DIVISION, "Single Division Players"), currentLocale);
		reportCollection.addField(singleDivisionPlayers);
		
		ReportableField multiDivisionPlayers = new ReportableField(FIELD_NAME_PLAYERS_MULTI_DIVISION, String.class);
		multiDivisionPlayers.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_PLAYERS_MULTI_DIVISION, "Multi Division Players"), currentLocale);
		reportCollection.addField(multiDivisionPlayers);
		
		
		//finished return the collection
		return reportCollection;
	}
	
	/*
	 * Report B12.6.4 of the ISI Specs
	 */
	public ReportableCollection sixDotFour () throws RemoteException {

		//initialize stuff
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
		
		ReportableField leagueString = new ReportableField(FIELD_NAME_LEAGUE_NAME, String.class);
		leagueString.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_LEAGUE_INFO, "League"), currentLocale);
		reportCollection.addField(leagueString);

		ReportableField singleDivisionMembers = new ReportableField(FIELD_NAME_MEMBERS_SINGLE_DIVISION, String.class);
		singleDivisionMembers.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEMBERS_SINGLE_DIVISION, "Single Division Members"), currentLocale);
		reportCollection.addField(singleDivisionMembers);
		
		ReportableField multiDivisionMembers = new ReportableField(FIELD_NAME_MEMBERS_MULTI_DIVISION, String.class);
		multiDivisionMembers.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_MEMBERS_MULTI_DIVISION, "Multi Division Members"), currentLocale);
		reportCollection.addField(multiDivisionMembers);
		
		ReportableField singleDivisionPlayers = new ReportableField(FIELD_NAME_PLAYERS_SINGLE_DIVISION, String.class);
		singleDivisionPlayers.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_PLAYERS_SINGLE_DIVISION, "Single Division Players"), currentLocale);
		reportCollection.addField(singleDivisionPlayers);
		
		ReportableField multiDivisionPlayers = new ReportableField(FIELD_NAME_PLAYERS_MULTI_DIVISION, String.class);
		multiDivisionPlayers.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_PLAYERS_MULTI_DIVISION, "Multi Division Players"), currentLocale);
		reportCollection.addField(multiDivisionPlayers);
		
		
		//finished return the collection
		return reportCollection;
	}

	/*
	 * Report B12.7.1 of the ISI Specs
	 */
	public ReportableCollection sevenDotOne () throws RemoteException {

		//initialize stuff
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
		
		ReportableField personName = new ReportableField(FIELD_NAME_PERSON_NAME, String.class);
		personName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_PERSON_NAME, "Name"), currentLocale);
		reportCollection.addField(personName);

		ReportableField phone = new ReportableField(FIELD_NAME_PHONE, String.class);
		phone.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_PHONE, "Phone"), currentLocale);
		reportCollection.addField(phone);
		
		ReportableField address = new ReportableField(FIELD_NAME_ADDRESS, String.class);
		address.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ADDRESS, "Address"), currentLocale);
		reportCollection.addField(address);
		
		ReportableField postalCode = new ReportableField(FIELD_NAME_POSTALCODE, String.class);
		postalCode.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_POSTALCODE, "Postal Code"), currentLocale);
		reportCollection.addField(postalCode);
		
		ReportableField email = new ReportableField(FIELD_NAME_EMAIL, String.class);
		email.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_EMAIL, "Email"), currentLocale);
		reportCollection.addField(email);


		//finished return the collection
		return reportCollection;
	}
	
	/*
	 * Report B12.7.2 of the ISI Specs
	 */
	public ReportableCollection sevenDotTwo () throws RemoteException {

		//initialize stuff
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
		
		ReportableField personName = new ReportableField(FIELD_NAME_PERSON_NAME, String.class);
		personName.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_PERSON_NAME, "Name"), currentLocale);
		reportCollection.addField(personName);
		
		ReportableField address = new ReportableField(FIELD_NAME_ADDRESS, String.class);
		address.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ADDRESS, "Address"), currentLocale);
		reportCollection.addField(address);
		
		ReportableField postalCode = new ReportableField(FIELD_NAME_POSTALCODE, String.class);
		postalCode.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_POSTALCODE, "Postal Code"), currentLocale);
		reportCollection.addField(postalCode);
		
		ReportableField email = new ReportableField(FIELD_NAME_EMAIL, String.class);
		email.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_EMAIL, "Email"), currentLocale);
		reportCollection.addField(email);
		

		

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
