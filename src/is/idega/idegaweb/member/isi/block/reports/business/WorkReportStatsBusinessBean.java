package is.idega.idegaweb.member.isi.block.reports.business;

import is.idega.idegaweb.member.isi.block.reports.data.WorkReport;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportDivisionBoard;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember;
import is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler.ClubTypeDropDownMenu;
import is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler.WorkReportStatusDropDownMenu;
import is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler.YesNoDropDownMenu;
import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;
import is.idega.idegaweb.member.util.IWMemberConstants;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

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
	
	// keys for localized strings
	private static final String LOCALIZED_LABEL = "WorkReportStatsBusiness.label";
	private static final String LOCALIZED_CLUB_NAME = "WorkReportStatsBusiness.club_name";
	private static final String LOCALIZED_CLUB_NUMBER = "WorkReportStatsBusiness.club_number";
	private static final String LOCALIZED_CLUB_TYPE = "WorkReportStatsBusiness.club_type";
	private static final String LOCALIZED_REGIONAL_UNION_NAME = "WorkReportStatsBusiness.regional_union_name";
	private static final String LOCALIZED_LEAGUE_INFO = "WorkReportStatsBusiness.league_info";
	private static final String LOCALIZED_WOMEN_UNDER = "WorkReportStatsBusiness.womenUnderAgeLimit_16";
	private static final String LOCALIZED_WOMEN_OVER_OR_EQUAL = "WorkReportStatsBusiness.womenOverOrEqualAgeLimit_16";
	private static final String LOCALIZED_MEN_UNDER = "WorkReportStatsBusiness.menUnderAgeLimit_16";
	private static final String LOCALIZED_MEN_OVER_OR_EQUAL = "WorkReportStatsBusiness.menOverOrEqualAgeLimit_16";
	private static final String LOCALIZED_ALL_UNDER = "WorkReportStatsBusiness.bothGendersUnderAge_16";
	private static final String LOCALIZED_ALL_EQUAL_OR_OVER = "WorkReportStatsBusiness.bothGendersEqualOverAge_16";
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
	private static final String LOCALIZED_CLUB_COUNT_INCACTIVE = "WorkReportStatsBusiness.club_count_inactive";
	private static final String LOCALIZED_TOTAL = "WorkReportStatsBusiness.total";
	private static final String LOCALIZED_MEMBERS_ANNUAL_CHANGE = "WorkReportStatsBusiness.member_annual_change";
	private static final String LOCALIZED_MEMBERS_ANNUAL_CHANGE_PERCENT = "WorkReportStatsBusiness.member_annual_change_percent";
	private static final String LOCALIZED_MEMBERS_ANNUAL_CHANGE_PERCENT_OF_TOTAL = "WorkReportStatsBusiness.member_annual_change_percent_of_total";
	private static final String LOCALIZED_PLAYERS_ANNUAL_CHANGE = "WorkReportStatsBusiness.player_annual_change";
	private static final String LOCALIZED_PLAYERS_ANNUAL_CHANGE_PERCENT = "WorkReportStatsBusiness.player_annual_change_percent";
	private static final String LOCALIZED_PLAYERS_ANNUAL_CHANGE_PERCENT_OF_TOTAL = "WorkReportStatsBusiness.player_annual_change_percent_of_total";
	private static final String LOCALIZED_PERCENT_REPORTS_DONE = "WorkReportStatsBusiness.percent_reports_done";
	private static final String LOCALIZED_MEMBERS_SINGLE_DIVISION = "WorkReportStatsBusiness.single_division_members";
	private static final String LOCALIZED_MEMBERS_MULTI_DIVISION = "WorkReportStatsBusiness.multi_division_members";
	private static final String LOCALIZED_PLAYERS_SINGLE_DIVISION = "WorkReportStatsBusiness.single_division_players";
	private static final String LOCALIZED_PLAYERS_MULTI_DIVISION = "WorkReportStatsBusiness.multi_division_players";
	private static final String LOCALIZED_PERSON_NAME = "WorkReportStatsBusiness.person_name";
	private static final String LOCALIZED_SSN = "WorkReportStatsBusiness.ssn";
	private static final String LOCALIZED_PHONE = "WorkReportStatsBusiness.phone";
	private static final String LOCALIZED_ADDRESS = "WorkReportStatsBusiness.address";
	private static final String LOCALIZED_POSTALCODE = "WorkReportStatsBusiness.postalcode";
	private static final String LOCALIZED_EMAIL = "WorkReportStatsBusiness.email";
	private static final String LOCALIZED_MEMBERS = "WorkReportStatsBusiness.members";
	private static final String LOCALIZED_PLAYERS = "WorkReportStatsBusiness.players";
	private static final String LOCALIZED_YEAR = "WorkReportStatsBusiness.year";
	private static final String LOCALIZED_NO_REGIONAL_UNION_NAME = "WorkReportStatsBusiness.no_reg_un_name";
	private static final String LOCALIZED_ORDERING = "WorkReportStatsBusiness.ordering";
	
	
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
	private static final String FIELD_NAME_CLUB_COUNT_DONE = "club_count_done";
	private static final String FIELD_NAME_CLUB_COUNT_NOT_DONE = "club_count_not_done";
	private static final String FIELD_NAME_CLUB_COUNT_SOME_DONE = "club_count_some_done";
	private static final String FIELD_NAME_CLUB_COUNT_INACTIVE = "club_count_inactive";
	private static final String FIELD_NAME_MEMBERS_ANNUAL_CHANGE = "annual_member_change";
	private static final String FIELD_NAME_MEMBERS_ANNUAL_CHANGE_PERCENT = "annual_member_change_percent";
	private static final String FIELD_NAME_MEMBERS_ANNUAL_CHANGE_PERCENT_OF_TOTAL = "annual_member_change_percent_of_total";
	private static final String FIELD_NAME_PLAYERS_ANNUAL_CHANGE = "annual_player_change";
	private static final String FIELD_NAME_PLAYERS_ANNUAL_CHANGE_PERCENT = "annual_player_change_percent";
	private static final String FIELD_NAME_PLAYERS_ANNUAL_CHANGE_PERCENT_OF_TOTAL = "annual_player_change_percent_of_total";
	private static final String FIELD_NAME_PERCENT_REPORTS_DONE = "percent_reports_done";
	private static final String FIELD_NAME_MEMBERS_SINGLE_DIVISION = "members_single_division";
	private static final String FIELD_NAME_MEMBERS_MULTI_DIVISION = "member_multi_division";
	private static final String FIELD_NAME_PLAYERS_SINGLE_DIVISION = "players_single_division";
	private static final String FIELD_NAME_PLAYERS_MULTI_DIVISION = "players_multi_division";
	private static final String FIELD_NAME_PERSON_NAME = "person_name";
	private static final String FIELD_NAME_SSN = "ssn";
	private static final String FIELD_NAME_PHONE = "phone";
	private static final String FIELD_NAME_ADDRESS = "address";
	private static final String FIELD_NAME_POSTALCODE = "postalcode";
	private static final String FIELD_NAME_EMAIL = "email";
	private static final String FIELD_NAME_MEMBERS = "members";
	private static final String FIELD_NAME_PLAYERS = "players";
	private static final String FIELD_NAME_YEAR = "year";
	private static final String FIELD_NAME_ORDERING = "ordering";
	
	private static final int DEFAULT_SPLIT_AGE = 16;
	
	/**
	 *  
	 */
	public WorkReportStatsBusinessBean() {
		super();
	}
	
	private WorkReportBusiness getWorkReportBusiness() throws RemoteException {
		if (this.workBiz == null) {
			this.workBiz = (WorkReportBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), WorkReportBusiness.class);
		}
		
		return this.workBiz;
	}
	
	private void initializeBundlesIfNeeded() {
		if (this._iwb == null) {
			this._iwb = this.getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
		}
		this._iwrb = this._iwb.getResourceBundle(this.getUserContext().getCurrentLocale());
	}
	
	/*
	 * Report B12.1.1 of the ISI Specs
	 */
	public ReportableCollection getStatisticsForLeaguesByYearRegionalUnionsClubsAndLeaguesFiltering(
			final Integer year,
			Collection regionalUnionsFilter,
			Collection clubsFilter,
			Collection leaguesFilter,
			Integer splitAge)
	throws RemoteException {
		
		
		//initialize stuff
		int age = splitAge==null?DEFAULT_SPLIT_AGE:splitAge.intValue();
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
				this._iwrb.getLocalizedString(LOCALIZED_LABEL, "Current date"),
				"label",
				TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale),"GMT"));
		
		//PARAMETERS that are also FIELDS
		//data from entity columns, can also be defined with an entity definition, see getClubMemberStatisticsForRegionalUnions method
		//The name you give the field/parameter must not contain spaces or special characters
		final ReportableField clubName = new ReportableField(FIELD_NAME_CLUB_NAME, String.class);
		clubName.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_CLUB_NAME, "Club name"), currentLocale);
		reportCollection.addField(clubName);
		
		final ReportableField regionalUnionAbbreviation = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionAbbreviation.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionAbbreviation);
		
		//fake columns (data gotten by business methods)
		final ReportableField leagueString = new ReportableField(FIELD_NAME_LEAGUE_NAME, String.class);
		leagueString.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_LEAGUE_INFO, "League"), currentLocale);
		reportCollection.addField(leagueString);
		
		ReportableField womenUnderAgeLimit = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE, Integer.class);
		womenUnderAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAgeLimit);
		
		ReportableField womenOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
		
		ReportableField menUnderAgeLimit = new ReportableField(FIELD_NAME_MEN_UNDER_AGE, Integer.class);
		menUnderAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_UNDER, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAgeLimit);
		
		ReportableField menOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL, "men "+age+"+"), currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
		
		
		ReportableField bothGendersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_UNDER, "all -"+age), currentLocale);
		reportCollection.addField(bothGendersUnderAge);
		
		ReportableField bothGendersEqualOverAge = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
		bothGendersEqualOverAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER, "all "+age+"+"), currentLocale);
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
					if (statsForLeague == null) {
						statsForLeague = new Vector();
					}
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
		Integer mainGroupId = null;
		if(mainBoard!=null) {
			mainGroupId = mainBoard.getGroupId();
		}
		
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
					if(!wrGroup.getGroupId().equals(mainGroupId) ){
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
			Collection leaguesFilter,
			Integer splitAge)
	throws RemoteException {
		
		//initialize stuff
		int age = splitAge==null?DEFAULT_SPLIT_AGE:splitAge.intValue();
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		
		//PARAMETES
		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				this._iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale),"GMT"));
		
		//PARAMETERS that are also FIELDS
		//data from entity columns, can also be defined with an entity definition, see getClubMemberStatisticsForRegionalUnions method
		//The name you give the field/parameter must not contain spaces or special characters		
		ReportableField regionalUnionAbbreviation = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionAbbreviation.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionAbbreviation);
		
		//fake columns (data gotten by business methods)
		ReportableField leagueString = new ReportableField(FIELD_NAME_LEAGUE_NAME, String.class);
		leagueString.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_LEAGUE_INFO, "League"), currentLocale);
		reportCollection.addField(leagueString);
		
		ReportableField womenUnderAgeLimit = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE, Integer.class);
		womenUnderAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAgeLimit);
		
		ReportableField womenOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
		
		ReportableField menUnderAgeLimit = new ReportableField(FIELD_NAME_MEN_UNDER_AGE, Integer.class);
		menUnderAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_UNDER, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAgeLimit);
		
		ReportableField menOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL, "men "+age+"+"), currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
		
		ReportableField bothGendersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_UNDER, "all -"+age), currentLocale);
		reportCollection.addField(bothGendersUnderAge);
		
		ReportableField bothGendersEqualOverAge = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
		bothGendersEqualOverAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER, "all "+age+"+"), currentLocale);
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
	public ReportableCollection getStatisticsForLeaguesByYearAndLeaguesFiltering(final Integer year,Collection leaguesFilter, Integer splitAge)throws RemoteException {
	
		//initialize stuff
		int age = splitAge==null?DEFAULT_SPLIT_AGE:splitAge.intValue();
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		
		//PARAMETES
		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				this._iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale),"GMT"));
		
		//PARAMETERS that are also FIELDS
		//data from entity columns, can also be defined with an entity definition, see getClubMemberStatisticsForRegionalUnions method
		//The name you give the field/parameter must not contain spaces or special characters
		
		//fake columns (data gotten by business methods)
		ReportableField leagueString = new ReportableField(FIELD_NAME_LEAGUE_NAME, String.class);
		leagueString.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_LEAGUE_INFO, "League"), currentLocale);
		reportCollection.addField(leagueString);
		
		ReportableField womenUnderAgeLimit = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE, Integer.class);
		womenUnderAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAgeLimit);
		
		ReportableField womenOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
		
		ReportableField menUnderAgeLimit = new ReportableField(FIELD_NAME_MEN_UNDER_AGE, Integer.class);
		menUnderAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_UNDER, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAgeLimit);
		
		ReportableField menOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL, "men "+age+"+"), currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
		
		ReportableField bothGendersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_UNDER, "all -"+age), currentLocale);
		reportCollection.addField(bothGendersUnderAge);
		
		ReportableField bothGendersEqualOverAge = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
		bothGendersEqualOverAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER, "all "+age+"+"), currentLocale);
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
	public ReportableCollection getStatisticsForLeaguesByYearAndLeaguesFilteringComparedWithLastYear(final Integer year,Collection leaguesFilter, Integer splitAge)throws RemoteException {
	
		//initialize stuff
		int age = splitAge==null?DEFAULT_SPLIT_AGE:splitAge.intValue();
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		
		//PARAMETES
		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				this._iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale),"GMT"));
		
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
		leagueString.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_LEAGUE_INFO, "League"), currentLocale);
		reportCollection.addField(leagueString);
		
		//Selected years parameters and fields
		ReportableField womenUnderAgeLimit = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE, Integer.class);
		womenUnderAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAgeLimit);
		
		ReportableField womenOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
		
		ReportableField menUnderAgeLimit = new ReportableField(FIELD_NAME_MEN_UNDER_AGE, Integer.class);
		menUnderAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_UNDER, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAgeLimit);
		
		ReportableField menOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL, "men "+age+"+"), currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
		
		ReportableField bothGendersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_UNDER, "all -"+age), currentLocale);
		reportCollection.addField(bothGendersUnderAge);
		
		ReportableField bothGendersEqualOverAge = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
		bothGendersEqualOverAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER, "all "+age+"+"), currentLocale);
		reportCollection.addField(bothGendersEqualOverAge);
		
		//last years parameters and fields
		ReportableField womenUnderAgeLimitLastYear = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE_LAST_YEAR, Integer.class);
		womenUnderAgeLimitLastYear.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAgeLimitLastYear);
		
		ReportableField womenOverOrEqualAgeLimitLastYear = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE_LAST_YEAR, Integer.class);
		womenOverOrEqualAgeLimitLastYear.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimitLastYear);
		
		ReportableField menUnderAgeLimitLastYear = new ReportableField(FIELD_NAME_MEN_UNDER_AGE_LAST_YEAR, Integer.class);
		menUnderAgeLimitLastYear.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_UNDER, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAgeLimitLastYear);
		
		ReportableField menOverOrEqualAgeLimitLastYear = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE_LAST_YEAR, Integer.class);
		menOverOrEqualAgeLimitLastYear.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL, "men "+age+"+"), currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimitLastYear);
		
		ReportableField bothGendersUnderAgeLastYear = new ReportableField(FIELD_NAME_ALL_UNDER_AGE_LAST_YEAR, Integer.class);
		bothGendersUnderAgeLastYear.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_UNDER, "all -"+age), currentLocale);
		reportCollection.addField(bothGendersUnderAgeLastYear);
		
		ReportableField bothGendersEqualOverAgeLastYear = new ReportableField("bothGendersEqualOverAgeLastYear", Integer.class);
		bothGendersEqualOverAgeLastYear.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER, "all "+age+"+"), currentLocale);
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
					Integer leagueKey = league.getGroupId();//for comparison this must be the same key both years
					
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
				this._iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale),"GMT"));
		
		//PARAMETERS that are also FIELDS
		//data from entity columns, can also be defined with an entity definition, see getClubMemberStatisticsForRegionalUnions method
		//The name you give the field/parameter must not contain spaces or special characters
		//fake columns (data gotten by business methods)		
		ReportableField leagueString = new ReportableField(FIELD_NAME_LEAGUE_NAME, String.class);
		leagueString.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_LEAGUE_INFO, "League"), currentLocale);
		reportCollection.addField(leagueString);
		
		
		ReportableField totalCountOfPlayersForLeague = new ReportableField("totalCountOfPlayersForLeague", Integer.class);
		totalCountOfPlayersForLeague.setLocalizedName(this._iwrb.getLocalizedString("WorkReportStatsBusiness.totalCountOfPlayersForLeague", "Players"), currentLocale);
		reportCollection.addField(totalCountOfPlayersForLeague);
		
		ReportableField costPerPlayers = new ReportableField("costPerPlayers", Integer.class);
		costPerPlayers.setLocalizedName(this._iwrb.getLocalizedString("WorkReportStatsBusiness.costPerPlayers", "cost/player"),currentLocale);
		reportCollection.addField(costPerPlayers);
		
		ReportableField totalCost = new ReportableField("totalCost", Integer.class);
		totalCost.setLocalizedName(this._iwrb.getLocalizedString("WorkReportStatsBusiness.totalCost","Total cost"), currentLocale);
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
					Integer leagueKey = league.getGroupId();//for comparison this must be the same key both years
					
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
						leagueStatsData = addToIntegerCount(totalCountOfPlayersForLeague,leagueStatsData,playerCountByAgeAndGender);
					} else {
					
					totalPlayerCount = getWorkReportBusiness().getCountOfPlayersByWorkReportAndWorkReportGroup(report, league);
					leagueStatsData = addToIntegerCount(totalCountOfPlayersForLeague, leagueStatsData, totalPlayerCount);
					}
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
		
		ReportableField[] sortFields = new ReportableField[] {leagueString};
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);
		
		//finished return the collection
		return reportCollection;
	
	}

	/*
	 * Report B12.2.1 of the ISI Specs
	 */
	public ReportableCollection getStatisticsForRegionalUnionsByYearAndRegionalUnionsFilter (
			final Integer year,
			Collection regionalUnionsFilter,
			Integer splitAge)
	throws RemoteException {

		//initialize stuff
		int age = splitAge==null?DEFAULT_SPLIT_AGE:splitAge.intValue();
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();

		//PARAMETES

		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				this._iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale),"GMT"));

		//PARAMETERS that are also FIELDS
		//data from entity columns, can also be defined with an entity definition, see getClubMemberStatisticsForRegionalUnions method
		//The name you give the field/parameter must not contain spaces or special characters

		ReportableField regionalUnionName = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionName.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionName);

		ReportableField bothGendersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_UNDER, "all -"+age), currentLocale);
		reportCollection.addField(bothGendersUnderAge);

		ReportableField bothGendersEqualOverAge = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
		bothGendersEqualOverAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER, "all "+age+"+"), currentLocale);
		reportCollection.addField(bothGendersEqualOverAge);
		
		ReportableField bothGendersAllAge = new ReportableField(FIELD_NAME_ALL_AGES, Integer.class);
		bothGendersAllAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL, "all"), currentLocale);
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
			Collection regionalUnionsFilter,
			Integer splitAge)
	throws RemoteException {
		//initialize stuff
		int age = splitAge==null?DEFAULT_SPLIT_AGE:splitAge.intValue();
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();

		//PARAMETES

		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				this._iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale),"GMT"));

		//PARAMETERS that are also FIELDS
		//data from entity columns, can also be defined with an entity definition, see getClubMemberStatisticsForRegionalUnions method
		//The name you give the field/parameter must not contain spaces or special characters
		
		ReportableField regionalUnionName = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionName.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionName);
		
		ReportableField menUnderAge = new ReportableField("menUnderAge", Integer.class);
		menUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_UNDER, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAge);
		
		ReportableField womenUnderAge = new ReportableField("womenUnderAge", Integer.class);
		womenUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAge);
		
		ReportableField womenOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
		
		ReportableField menOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL, "men "+age+"+"),currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
		
		ReportableField bothGendersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_UNDER, "all -"+age),currentLocale);
		reportCollection.addField(bothGendersUnderAge);
		
		ReportableField bothGendersEqualOrOver = new ReportableField("bothGendersEqualOrOverAge", Integer.class);
		bothGendersEqualOrOver.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER, "all "+age+"+"),currentLocale);
		reportCollection.addField(bothGendersEqualOrOver);
		
		ReportableField bothGendersAllAge = new ReportableField(FIELD_NAME_ALL_AGES, Integer.class);
		bothGendersAllAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL, "all"), currentLocale);
		reportCollection.addField(bothGendersAllAge);
		
		ReportableField bothGendersLastYear = new ReportableField(FIELD_NAME_ALL_AGES_LAST_YEAR, Integer.class);
		bothGendersLastYear.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_LAST_YEAR, "all "+age+"+ last year"),currentLocale);
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
				
				//
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
			Collection regionalUnionsFilter,
			Integer splitAge)
			throws RemoteException {

		//initialize stuff
		int age = splitAge==null?DEFAULT_SPLIT_AGE:splitAge.intValue();
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();

		WorkReportGroup mainBoardGroup = getWorkReportBusiness().getMainBoardWorkReportGroup(year.intValue());
		Integer mbId = mainBoardGroup==null?(new Integer(-1)):mainBoardGroup.getGroupId();
		
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
				this._iwrb.getLocalizedString(LOCALIZED_LABEL, "Current date"),
				"label",
				TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale),"GMT"));

		//PARAMETERS that are also FIELDS
		//data from entity columns, can also be defined with an entity definition, see getClubMemberStatisticsForRegionalUnions method
		//The name you give the field/parameter must not contain spaces or special characters
		final ReportableField clubName = new ReportableField(FIELD_NAME_CLUB_NAME, String.class);
		clubName.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_CLUB_NAME, "Club name"), currentLocale);
		reportCollection.addField(clubName);

		final ReportableField regionalUnionAbbreviation = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionAbbreviation.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionAbbreviation);

		//fake columns (data gotten by business methods)

		ReportableField womenUnderAgeLimit = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE, Integer.class);
		womenUnderAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAgeLimit);

		ReportableField womenOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);

		ReportableField menUnderAgeLimit = new ReportableField(FIELD_NAME_MEN_UNDER_AGE, Integer.class);
		menUnderAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_UNDER, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAgeLimit);

		ReportableField menOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL, "men "+age+"+"), currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);

		ReportableField bothGendersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_UNDER, "all -"+age), currentLocale);
		reportCollection.addField(bothGendersUnderAge);
	
		ReportableField bothGendersEqualOrOver = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
		bothGendersEqualOrOver.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER, "all "+age+"+"),currentLocale);
		reportCollection.addField(bothGendersEqualOrOver);

		ReportableField bothGendersAllAge = new ReportableField(FIELD_NAME_ALL_AGES, Integer.class);
		bothGendersAllAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL, "all"), currentLocale);
		reportCollection.addField(bothGendersAllAge);

		//Real data stuff
		//Gathering data
		//Get all the workreports (actually more than needed)
		//then for each get its leagues and the count for
		//each age and create a row and insert into an ordered map by league
		//then iterate the map and insert into the final report collection.
		Collection clubs = getWorkReportBusiness().getWorkReportsForRegionalUnionCollection(year.intValue(), regionalUnionsFilter);
		
		Map ruMap = new TreeMap();
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
			
			String regUniIdentifier = getRegionalUnionIdentifier(report);

			Map clubMap = (Map) ruMap.get(regUniIdentifier);
			if(clubMap==null) {
				clubMap = new TreeMap();
				ruMap.put(regUniIdentifier, clubMap);
			}
			
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
					
					boolean isMainBoard = mbId.equals(league.getGroupId());
					if(isMainBoard) {
						continue;
					}
					
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
					
					System.out.println("Counting players for club " + cName + " in regional union " + regUniIdentifier + " and league " + league.getName() + " total is "
					                   + (menPlayersUnder + womenPlayersUnder + menPlayersEqualOrOver + womenPlayersEqualOrOver));
				}
			}
			catch (IDOException e) {
				e.printStackTrace();
			}
		}

		// iterate through the ordered map and ordered lists and add to the final collection
		Iterator ruIter = ruMap.values().iterator();
		while(ruIter.hasNext()) {
			Map clubMap = (Map) ruIter.next();
			reportCollection.addAll(clubMap.values());
		}
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
			Collection regionalUnionsFilter,
			Integer splitAge)
	throws RemoteException {
		//initialize stuff
		int age = splitAge==null?DEFAULT_SPLIT_AGE:splitAge.intValue();
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();

		//PARAMETES

		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				this._iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale),"GMT"));

		//PARAMETERS that are also FIELDS
		//data from entity columns, can also be defined with an entity definition, see getClubMemberStatisticsForRegionalUnions method
		//The name you give the field/parameter must not contain spaces or special characters

		ReportableField regionalUnionName = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionName.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionName);

		ReportableField clubName = new ReportableField(FIELD_NAME_CLUB_NAME, String.class);
		clubName.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_CLUB_NAME, "Club name"), currentLocale);
		reportCollection.addField(clubName);

		ReportableField menUnderAge = new ReportableField(FIELD_NAME_MEN_UNDER_AGE, Integer.class);
		menUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_UNDER, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAge);

		ReportableField womenUnderAge = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE, Integer.class);
		womenUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAge);

		ReportableField womenOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);

		ReportableField menOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL, "men "+age+"+"),currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);

		ReportableField bothGendersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_UNDER, "all -"+age), currentLocale);
		reportCollection.addField(bothGendersUnderAge);

		ReportableField bothGendersEqualOrOverAge = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
		bothGendersEqualOrOverAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER, "all "+age+"+"), currentLocale);
		reportCollection.addField(bothGendersEqualOrOverAge);

		ReportableField bothGendersAllAge = new ReportableField(FIELD_NAME_ALL_AGES, Integer.class);
		bothGendersAllAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL, "all "), currentLocale);
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
			Collection regionalUnionsFilter,
			Integer splitAge)
	throws RemoteException {
		//initialize stuff
		int age = splitAge==null?DEFAULT_SPLIT_AGE:splitAge.intValue();
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();

		//PARAMETES

		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				this._iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale),"GMT"));

		//PARAMETERS that are also FIELDS
		//data from entity columns, can also be defined with an entity definition, see getClubMemberStatisticsForRegionalUnions method
		//The name you give the field/parameter must not contain spaces or special characters
			
		ReportableField regionalUnionName = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionName.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionName);
	
		ReportableField menUnderAge = new ReportableField(FIELD_NAME_MEN_UNDER_AGE, Integer.class);
		menUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_UNDER, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAge);
	
		ReportableField womenUnderAge = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE, Integer.class);
		womenUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAge);

		ReportableField womenOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);

		ReportableField menOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL, "men "+age+"+"),currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
	
		ReportableField bothGendersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_UNDER, "all -"+age),currentLocale);
		reportCollection.addField(bothGendersUnderAge);

		ReportableField bothGendersEqualOrOver = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
		bothGendersEqualOrOver.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER, "all "+age+"+"),currentLocale);
		reportCollection.addField(bothGendersEqualOrOver);
		
		ReportableField bothGendersAllAge = new ReportableField(FIELD_NAME_ALL_AGES, Integer.class);
		bothGendersAllAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL, "all "),currentLocale);
		reportCollection.addField(bothGendersAllAge);

		ReportableField bothGendersLastYear = new ReportableField(FIELD_NAME_ALL_AGES_LAST_YEAR, Integer.class);
		bothGendersLastYear.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_LAST_YEAR, "all "+age+"+ last year"),currentLocale);
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
				regData.addData(bothGendersAllAge, new Integer(0));
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
			regData = addToIntegerCount(bothGendersAllAge, regData, womenPlayersUnder + menPlayersUnder + womenPlayersEqualOrOver + menPlayersEqualOrOver);
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
	public ReportableCollection getStatisticsForLeaguesByYearAndLeaguesAndRegionalUnionsFiltering(final Integer year,Collection regionalUnionsFilter,Collection leaguesFilter,Integer splitAge)throws RemoteException {

		//initialize stuff
		int age = splitAge==null?DEFAULT_SPLIT_AGE:splitAge.intValue();
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
	
		//PARAMETES
		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				this._iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale),"GMT"));
	
		//PARAMETERS that are also FIELDS
		//data from entity columns, can also be defined with an entity definition, see getClubMemberStatisticsForRegionalUnions method
		//The name you give the field/parameter must not contain spaces or special characters
	
		//fake columns (data gotten by business methods)
		
		ReportableField regionalUnionName = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionName.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionName);
				
		ReportableField leagueString = new ReportableField(FIELD_NAME_LEAGUE_NAME, String.class);
		leagueString.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_LEAGUE_INFO, "League"), currentLocale);
		reportCollection.addField(leagueString);
	
		ReportableField womenUnderAgeLimit = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE, Integer.class);
		womenUnderAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAgeLimit);
	
		ReportableField womenOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
	
		ReportableField menUnderAgeLimit = new ReportableField(FIELD_NAME_MEN_UNDER_AGE, Integer.class);
		menUnderAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_UNDER, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAgeLimit);
	
		ReportableField menOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL, "men "+age+"+"), currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
		
		ReportableField bothGendersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_UNDER, "all -"+age), currentLocale);
		reportCollection.addField(bothGendersUnderAge);

		ReportableField bothGendersEqualOrOverAge = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
		bothGendersEqualOrOverAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER, "all "+age+"+"), currentLocale);
		reportCollection.addField(bothGendersEqualOrOverAge);
	
		ReportableField bothGendersAllAge = new ReportableField(FIELD_NAME_ALL_AGES, Integer.class);
		bothGendersAllAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL, "all "), currentLocale);
		reportCollection.addField(bothGendersAllAge);
		
		//Real data stuff
		//Gathering data
		//Get all the workreports (actually more than needed)
		//then for each get its leagues and the count for
		//each age and create a row and insert into an ordered map by league
		//then insert into the final report collection.
		Collection clubs = getWorkReportBusiness().getWorkReportsForRegionalUnionCollection(year.intValue(), regionalUnionsFilter);
		Map leaguesByRegionalUnion = new TreeMap();
		Map leagueStatsMap = new TreeMap();
		List leagueGroupIdList = getGroupIdListFromLeagueGroupCollection(year,leaguesFilter,false);
	
		//Iterating through workreports and creating report data 
		Iterator iter = clubs.iterator();
		while (iter.hasNext()) {
			//the club
			WorkReport report = (WorkReport) iter.next();
			
			String regionalUnionIdentifier = getRegionalUnionIdentifier(report);
			
			leagueStatsMap = (Map) leaguesByRegionalUnion.get(regionalUnionIdentifier);
			if (leagueStatsMap == null) {
			    leagueStatsMap = new TreeMap();
			}
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
				leaguesByRegionalUnion.put(regionalUnionIdentifier, leagueStatsMap);
			}
			catch (IDOException e) {
				e.printStackTrace();
			}
		
		}
	
		//add the data to the collection
		Iterator regionsIter = leaguesByRegionalUnion.keySet().iterator();
		while (regionsIter.hasNext()) {		
			// don't forget to add the row to the collection
			reportCollection.addAll(((Map)leaguesByRegionalUnion.get(regionsIter.next())).values());
		}

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
			String type,
			Integer splitAge)
	throws RemoteException {
		//initialize stuff
		int age = splitAge==null?DEFAULT_SPLIT_AGE:splitAge.intValue();
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		//PARAMETES

		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				this._iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale),"GMT"));

		//PARAMETERS that are also FIELDS
		//data from entity columns, can also be defined with an entity definition, see getClubMemberStatisticsForRegionalUnions method
		//The name you give the field/parameter must not contain spaces or special characters
	
		ReportableField regionalUnionName = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionName.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionName);
	
		ReportableField menUnderAge = new ReportableField(FIELD_NAME_MEN_UNDER_AGE, Integer.class);
		menUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_UNDER, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAge);
	
		ReportableField womenUnderAge = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE, Integer.class);
		womenUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAge);
	
		ReportableField womenOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
	
		ReportableField menOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL, "men "+age+"+"),currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
	
		ReportableField bothGendersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_UNDER, "all -"+age),currentLocale);
		reportCollection.addField(bothGendersUnderAge);
	
		ReportableField bothGendersEqualOrOver = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
		bothGendersEqualOrOver.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER, "all "+age+"+"),currentLocale);
		reportCollection.addField(bothGendersEqualOrOver);
		
		ReportableField bothGendersAllAge = new ReportableField(FIELD_NAME_ALL_AGES, Integer.class);
		bothGendersAllAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL, "all"), currentLocale);
		reportCollection.addField(bothGendersAllAge);

		ReportableField bothGendersLastYear = new ReportableField(FIELD_NAME_ALL_AGES_LAST_YEAR, Integer.class);
		bothGendersLastYear.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_LAST_YEAR, "all "+age+"+ last year"),currentLocale);
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
			String type,
			Integer splitAge)
	throws RemoteException {
		//initialize stuff
		int age = splitAge==null?DEFAULT_SPLIT_AGE:splitAge.intValue();
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		//PARAMETES

		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				this._iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale),"GMT"));

		//PARAMETERS that are also FIELDS
		//data from entity columns, can also be defined with an entity definition, see getClubMemberStatisticsForRegionalUnions method
		//The name you give the field/parameter must not contain spaces or special characters

		ReportableField regionalUnionName = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionName.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionName);

		ReportableField womenUnderAge = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE, Integer.class);
		womenUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAge);

		ReportableField womenOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
		
		ReportableField menUnderAge = new ReportableField(FIELD_NAME_MEN_UNDER_AGE, Integer.class);
		menUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_UNDER, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAge);

		ReportableField menOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL, "men "+age+"+"),currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);

		ReportableField bothGendersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_UNDER, "all -"+age),currentLocale);
		reportCollection.addField(bothGendersUnderAge);

		ReportableField bothGendersEqualOrOver = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
		bothGendersEqualOrOver.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER, "all "+age+"+"),currentLocale);
		reportCollection.addField(bothGendersEqualOrOver);
	
		ReportableField bothGendersAllAge = new ReportableField(FIELD_NAME_ALL_AGES, Integer.class);
		bothGendersAllAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL, "all"), currentLocale);
		reportCollection.addField(bothGendersAllAge);

		ReportableField bothGendersLastYear = new ReportableField(FIELD_NAME_ALL_AGES_LAST_YEAR, Integer.class);
		bothGendersLastYear.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_LAST_YEAR, "all "+age+"+ last year"),currentLocale);
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
					int lastYearPlayerCount = getWorkReportBusiness().getCountOfPlayersByWorkReport(lastYearReport);
					regData = addToIntegerCount(bothGendersLastYear, regData, lastYearPlayerCount);
				} catch(Exception e) {
					System.out.println("Error getting player count for last year");
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
			String type,
			Integer splitAge)
	throws RemoteException {
		//initialize stuff
		int age = splitAge==null?DEFAULT_SPLIT_AGE:splitAge.intValue();
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		//PARAMETES

		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				this._iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale),"GMT"));

		//PARAMETERS that are also FIELDS
		//data from entity columns, can also be defined with an entity definition, see getClubMemberStatisticsForRegionalUnions method
		//The name you give the field/parameter must not contain spaces or special characters

		ReportableField clubName = new ReportableField(FIELD_NAME_CLUB_NAME, String.class);
		clubName.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_CLUB_NAME, "Club name"), currentLocale);
		reportCollection.addField(clubName);
		
		/*ReportableField clubNumber = new ReportableField(FIELD_NAME_CLUB_NUMBER, String.class);
		clubNumber.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_CLUB_NUMBER, "Club number"), currentLocale);
		reportCollection.addField(clubNumber);*/
		
		ReportableField regionalUnionAbbreviation = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionAbbreviation.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionAbbreviation);

		ReportableField clubIsInUMFI = new ReportableField(FIELD_NAME_IS_IN_UMFI, String.class);
		clubIsInUMFI.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_IS_IN_UMFI, "UMFI member"), currentLocale);
		reportCollection.addField(clubIsInUMFI);
		
		ReportableField clubType = new ReportableField(FIELD_NAME_CLUB_TYPE, String.class);
		clubType.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_CLUB_TYPE, "Club type"), currentLocale);
		reportCollection.addField(clubType);
		
		ReportableField womenUnderAge = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE, Integer.class);
		womenUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAge);

		ReportableField womenOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
		
		ReportableField menUnderAge = new ReportableField(FIELD_NAME_MEN_UNDER_AGE, Integer.class);
		menUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_UNDER, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAge);

		ReportableField menOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL, "men "+age+"+"),currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);

		ReportableField bothGendersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_UNDER, "all -"+age),currentLocale);
		reportCollection.addField(bothGendersUnderAge);

		ReportableField bothGendersEqualOrOver = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
		bothGendersEqualOrOver.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER, "all "+age+"+"),currentLocale);
		reportCollection.addField(bothGendersEqualOrOver);
	
		ReportableField bothGendersAllAge = new ReportableField(FIELD_NAME_ALL_AGES, Integer.class);
		bothGendersAllAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL, "all"), currentLocale);
		reportCollection.addField(bothGendersAllAge);

		ReportableField bothGendersLastYear = new ReportableField(FIELD_NAME_ALL_AGES_LAST_YEAR, Integer.class);
		bothGendersLastYear.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_LAST_YEAR, "all last year"),currentLocale);
		reportCollection.addField(bothGendersLastYear);
		
		//Real data stuff
		//Gathering data
		//Get all the workreports (actually more than needed)
		//then for each get its leagues and the count for
		//each age and create a row and insert into an ordered map by league
		//then iterate the map and insert into the final report collection.
		HashSet clubSet = (clubsFilter==null)?(new HashSet()):(new HashSet(clubsFilter));
		if(umfiClubsFilter!=null) {
			clubSet.addAll(umfiClubsFilter);
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
			regData.addData(clubIsInUMFI, report.isInUMFI()?this._iwrb.getLocalizedString(LOCALIZED_YES, "Yes"):"");
			regData.addData(clubType, getClubTypeString(report));

			//fetch member stats stats or initialize for this club
			int womenMembersUnder = getWorkReportBusiness().getCountOfFemaleMembersOfYoungerAgeAndByWorkReport(age, report);
			int womenMembersEqualOrOver = getWorkReportBusiness().getCountOfFemaleMembersEqualOrOlderThanAgeAndByWorkReport(age, report);
			int menMembersUnder = getWorkReportBusiness().getCountOfMaleMembersOfYoungerAgeAndByWorkReport(age, report);
			int menMembersEqualOrOver = getWorkReportBusiness().getCountOfMaleMembersEqualOrOlderThanAgeAndByWorkReport(age, report);
			int lastYearMemberCount = 0;
			if(lastYearReport!=null) {
				lastYearMemberCount = getWorkReportBusiness().getCountOfMembersByWorkReport(lastYearReport);
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
			String type,
			Integer splitAge)
	throws RemoteException {
		//initialize stuff
		int age = splitAge==null?DEFAULT_SPLIT_AGE:splitAge.intValue();
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		//PARAMETES
	
		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				this._iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale),"GMT"));
	
		ReportableField clubName = new ReportableField(FIELD_NAME_CLUB_NAME, String.class);
		clubName.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_CLUB_NAME, "Club name"), currentLocale);
		reportCollection.addField(clubName);
	
		/*ReportableField clubNumber = new ReportableField(FIELD_NAME_CLUB_NUMBER, String.class);
		clubNumber.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_CLUB_NUMBER, "Club number"), currentLocale);
		reportCollection.addField(clubNumber);*/
	
		ReportableField regionalUnionAbbreviation = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionAbbreviation.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionAbbreviation);
	
		ReportableField clubIsInUMFI = new ReportableField(FIELD_NAME_IS_IN_UMFI, String.class);
		clubIsInUMFI.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_IS_IN_UMFI, "UMFI member"), currentLocale);
		reportCollection.addField(clubIsInUMFI);
	
		ReportableField clubType = new ReportableField(FIELD_NAME_CLUB_TYPE, String.class);
		clubType.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_CLUB_TYPE, "Club type"), currentLocale);
		reportCollection.addField(clubType);
	
		ReportableField womenUnderAge = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE, Integer.class);
		womenUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAge);
	
		ReportableField womenOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
	
		ReportableField menUnderAge = new ReportableField(FIELD_NAME_MEN_UNDER_AGE, Integer.class);
		menUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_UNDER, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAge);
	
		ReportableField menOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL, "men "+age+"+"),currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
	
		ReportableField bothGendersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_UNDER, "all -"+age),currentLocale);
		reportCollection.addField(bothGendersUnderAge);
	
		ReportableField bothGendersEqualOrOver = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
		bothGendersEqualOrOver.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER, "all "+age+"+"),currentLocale);
		reportCollection.addField(bothGendersEqualOrOver);
	
		ReportableField bothGendersAllAge = new ReportableField(FIELD_NAME_ALL_AGES, Integer.class);
		bothGendersAllAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL, "all"), currentLocale);
		reportCollection.addField(bothGendersAllAge);
	
		ReportableField bothGendersLastYear = new ReportableField(FIELD_NAME_ALL_AGES_LAST_YEAR, Integer.class);
		bothGendersLastYear.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_LAST_YEAR, "all last year"),currentLocale);
		reportCollection.addField(bothGendersLastYear);
	
		//Real data stuff
		//Gathering data
		//Get all the workreports (actually more than needed)
		//then for each get its leagues and the count for
		//each age and create a row and insert into an ordered map by league
		//then iterate the map and insert into the final report collection.
		HashSet clubSet = (clubsFilter==null)?(new HashSet()):(new HashSet(clubsFilter));
		if(umfiClubsFilter!=null) {
			clubSet.addAll(umfiClubsFilter);
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
			regData.addData(clubIsInUMFI, report.isInUMFI()?this._iwrb.getLocalizedString(LOCALIZED_YES, "Yes"):"");
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
	 * Report B12.4.1 and B12.4.2 of the ISI Specs rolled into one report.
	 */
	public ReportableCollection getYearlyAccountsStatistic(final Integer year
			, final Integer comparingYear
			,Collection regionalUnionsFilter
			,Collection clubsFilter
			,Collection leaguesFilter
			,String theClubType) throws RemoteException{
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		
		//PARAMETERS and FIELDS
		//		Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				this._iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				TextSoap.findAndCut(TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale),"GMT"),"GMT"));
		
		//PARAMETERS and FIELDS
		ReportableField leagueString = new ReportableField(FIELD_NAME_LEAGUE_NAME, String.class);
		leagueString.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_LEAGUE_INFO, "League"), currentLocale);
		reportCollection.addField(leagueString);
		
		ReportableField clubName = new ReportableField(FIELD_NAME_CLUB_NAME, String.class);
		clubName.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_CLUB_NAME, "Club name"), currentLocale);
		reportCollection.addField(clubName);
		
		ReportableField regionalUnionAbbreviation = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionAbbreviation.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionAbbreviation);
		
		ReportableField clubType = new ReportableField(FIELD_NAME_CLUB_TYPE, String.class);
		clubType.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_CLUB_TYPE, "Club type"), currentLocale);
		reportCollection.addField(clubType);
		
		ReportableField dummyOrderingField = new ReportableField(FIELD_NAME_ORDERING, String.class);
		dummyOrderingField.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ORDERING, "Ordering"), currentLocale);
		reportCollection.addField(dummyOrderingField);
		
		//selected year stuff
		//tekjur
		ReportableField income = new ReportableField("income", Long.class);
		income.setLocalizedName(this._iwrb.getLocalizedString("WorkReportStatsBusiness.income", "Income"), currentLocale);
		reportCollection.addField(income);
		//gjold
		ReportableField expenses = new ReportableField("expenses", Long.class);
		expenses.setLocalizedName(this._iwrb.getLocalizedString("WorkReportStatsBusiness.expenses", "Expenses"), currentLocale);
		reportCollection.addField(expenses);
		//afkoma (tekjur-gjold)
		ReportableField incomeMinusExpenses = new ReportableField("incMexp", Long.class);
		incomeMinusExpenses.setLocalizedName(this._iwrb.getLocalizedString("WorkReportStatsBusiness.incMexp", "incMexp"), currentLocale);
		reportCollection.addField(incomeMinusExpenses);
		//veltufjarmunir
		ReportableField rollingMoney = new ReportableField("rollingmoney", Long.class);
		rollingMoney.setLocalizedName(this._iwrb.getLocalizedString("WorkReportStatsBusiness.rollingmoney", "Rollingmoney"), currentLocale);
		reportCollection.addField(rollingMoney);
		//fastafjarmunir
		ReportableField rigidMoney = new ReportableField("rigidMoney", Long.class);
		rigidMoney.setLocalizedName(this._iwrb.getLocalizedString("WorkReportStatsBusiness.rigidMoney", "Rigidmoney"), currentLocale);
		reportCollection.addField(rigidMoney);
		//skuldir
		ReportableField debts = new ReportableField("debts", Long.class);
		debts.setLocalizedName(this._iwrb.getLocalizedString("WorkReportStatsBusiness.debts", "Debts"), currentLocale);
		reportCollection.addField(debts);
		//peningaleg stada (veltufjarmunir-skuldir)
		ReportableField rollingMoneyMinusDebts = new ReportableField("rollingMDebts", Long.class);
		rollingMoneyMinusDebts.setLocalizedName(this._iwrb.getLocalizedString("WorkReportStatsBusiness.rollingMDebts", "RollingMDebts"), currentLocale);
		reportCollection.addField(rollingMoneyMinusDebts);
		
		////////////////////////////////////////////////////
		//comparing year stuff
		ReportableField comparingIncome = new ReportableField("Cincome", Long.class);
		comparingIncome.setLocalizedName(this._iwrb.getLocalizedString("WorkReportStatsBusiness.income", "Income"), currentLocale);
		reportCollection.addField(comparingIncome);
		//gjold
		ReportableField comparingExpenses = new ReportableField("Cexpenses", Long.class);
		comparingExpenses.setLocalizedName(this._iwrb.getLocalizedString("WorkReportStatsBusiness.expenses", "Expenses"), currentLocale);
		reportCollection.addField(comparingExpenses);
		//afkoma (tekjur-gjold)
		ReportableField comparingIncomeMinusExpenses = new ReportableField("CincMexp", Long.class);
		comparingIncomeMinusExpenses.setLocalizedName(this._iwrb.getLocalizedString("WorkReportStatsBusiness.incMexp", "incMexp"), currentLocale);
		reportCollection.addField(comparingIncomeMinusExpenses);
		//veltufjarmunir
		ReportableField comparingRollingMoney = new ReportableField("Crollingmoney", Long.class);
		comparingRollingMoney.setLocalizedName(this._iwrb.getLocalizedString("WorkReportStatsBusiness.rollingmoney", "Rollingmoney"), currentLocale);
		reportCollection.addField(comparingRollingMoney);
		//fastafjarmunir
		ReportableField comparingRigidMoney = new ReportableField("CrigidMoney", Long.class);
		comparingRigidMoney.setLocalizedName(this._iwrb.getLocalizedString("WorkReportStatsBusiness.rigidMoney", "Rigidmoney"), currentLocale);
		reportCollection.addField(comparingRigidMoney);
		//skuldir
		ReportableField comparingDebts = new ReportableField("Cdebts", Long.class);
		comparingDebts.setLocalizedName(this._iwrb.getLocalizedString("WorkReportStatsBusiness.debts", "Debts"), currentLocale);
		reportCollection.addField(comparingDebts);
		//peningaleg stada (veltufjarmunir-skuldir)
		ReportableField comparingRollingMoneyMinusDebts = new ReportableField("CrollingMDebts", Long.class);
		comparingRollingMoneyMinusDebts.setLocalizedName(this._iwrb.getLocalizedString("WorkReportStatsBusiness.rollingMDebts", "RollingMDebts"), currentLocale);
		reportCollection.addField(comparingRollingMoneyMinusDebts);
		
		
		////////////////THE Sum-hidden fields
		//selected year stuff
		//tekjur
		ReportableField incomeSum = new ReportableField("incomeSum", Long.class);
		
		//gjold
		ReportableField expensesSum  = new ReportableField("expensesSum", Long.class);
		reportCollection.addField(expenses);
		//afkoma (tekjur-gjold)
		ReportableField incomeMinusExpensesSum  = new ReportableField("incMexpSum", Long.class);
		reportCollection.addField(incomeMinusExpenses);
		//veltufjarmunir
		ReportableField rollingMoneySum  = new ReportableField("rollingmoneySum", Long.class);
		reportCollection.addField(rollingMoney);
		//fastafjarmunir
		ReportableField rigidMoneySum  = new ReportableField("rigidMoneySum", Long.class);
		reportCollection.addField(rigidMoney);
		//skuldir
		ReportableField debtsSum  = new ReportableField("debtsSum", Long.class);
		reportCollection.addField(debts);
		//peningaleg stada (veltufjarmunir-skuldir)
		ReportableField rollingMoneyMinusDebtsSum  = new ReportableField("rollingMDebtsSum", Long.class);
		reportCollection.addField(rollingMoneyMinusDebts);
		
		////////////////////////////////////////////////////
		//comparing year stuff
		ReportableField comparingIncomeSum  = new ReportableField("CincomeSum", Long.class);
		reportCollection.addField(comparingIncome);
		//gjold
		ReportableField comparingExpensesSum  = new ReportableField("CexpensesSum", Long.class);
		reportCollection.addField(comparingExpenses);
		//afkoma (tekjur-gjold)
		ReportableField comparingIncomeMinusExpensesSum  = new ReportableField("CincMexpSum", Long.class);
		reportCollection.addField(comparingIncomeMinusExpenses);
		//veltufjarmunir
		ReportableField comparingRollingMoneySum  = new ReportableField("CrollingmoneySum", Long.class);
		reportCollection.addField(comparingRollingMoney);
		//fastafjarmunir
		ReportableField comparingRigidMoneySum  = new ReportableField("CrigidMoneySum", Long.class);
		reportCollection.addField(comparingRigidMoney);
		//skuldir
		ReportableField comparingDebtsSum  = new ReportableField("CdebtsSum", Long.class);
		reportCollection.addField(comparingDebts);
		//peningaleg stada (veltufjarmunir-skuldir)
		ReportableField comparingRollingMoneyMinusDebtsSum  = new ReportableField("CrollingMDebtsSum", Long.class);
		reportCollection.addField(comparingRollingMoneyMinusDebts);
		
		//end sum stuff
		
		///////////////////////////////////////////////////////////////
		//get the data
		Collection reports = getWorkReportBusiness().getWorkReportsByYearRegionalUnionsAndClubs(year.intValue(), regionalUnionsFilter, clubsFilter);
		List leagueGroupIDList = getGroupIdListFromLeagueGroupCollection(year, leaguesFilter, true);
		
		Map recordsMapKeyedByLeagueIdentifierAndClubsName = new TreeMap();
		
		
		
		Iterator iter = reports.iterator();
		try {
			while (iter.hasNext()) {
				WorkReport report = (WorkReport) iter.next();
				int reportId = ((Integer)report.getPrimaryKey()).intValue();
				WorkReport comparingReport=null;
				int comparingReportId = -1;
				try {
					comparingReport = getWorkReportBusiness().getWorkReportHome().findWorkReportByGroupIdAndYearOfReport(report.getGroupId().intValue(),comparingYear.intValue());
					comparingReportId = ((Integer)comparingReport.getPrimaryKey()).intValue();
				}
				catch (FinderException e1) {
					//no report that year
				}
				
				
				String regionalUnionIdentifier = getRegionalUnionIdentifier(report);
				String groupName = report.getGroupNumber() + " " + report.getGroupName();
				
				Collection leagues = report.getLeagues();
				Iterator iterator = leagues.iterator();
				while (iterator.hasNext()) {
					WorkReportGroup league = (WorkReportGroup) iterator.next();
					
					if (!leagueGroupIDList.contains(league.getGroupId()) && showClubType(report, theClubType)) {
						continue; //don't process this one, go to next
					}
					
					String leagueIdentifier = getLeagueIdentifier(league);
					int wrGroupId = ((Integer)league.getPrimaryKey()).intValue();
					WorkReportGroup comparingLeague=null;
					int comparingWrGroupId = -1;
					try {
						comparingLeague = getWorkReportBusiness().getWorkReportGroupHome().findWorkReportGroupByGroupIdAndYear(league.getGroupId().intValue(),comparingYear.intValue());
						comparingWrGroupId =  ((Integer)comparingLeague.getPrimaryKey()).intValue();
					}
					catch (FinderException e2) {
						//no league that year or data missing
					}
					
					
					String mapKey = leagueIdentifier+groupName;
					//add the data					
					//fetch the stats or initialize
					ReportableData regData = (ReportableData) recordsMapKeyedByLeagueIdentifierAndClubsName.get(mapKey);
					if(regData==null){//initialize
						regData = new ReportableData();
						regData.addData(leagueString, leagueIdentifier);
						regData.addData(clubName, groupName);
						regData.addData(regionalUnionAbbreviation, regionalUnionIdentifier);
						regData.addData(clubType, getClubTypeString(report));
						
						regData.addData(income, new Long(0));
						regData.addData(expenses, new Long(0));
						regData.addData(incomeMinusExpenses,new Long(0));
						regData.addData(rollingMoney, new Long(0));
						regData.addData(rigidMoney, new Long(0));
						regData.addData(debts, new Long(0));
						regData.addData(rollingMoneyMinusDebts,new Long(0));
						
						regData.addData(comparingIncome, new Long(0));
						regData.addData(comparingExpenses, new Long(0));
						regData.addData(comparingIncomeMinusExpenses,new Long(0));
						regData.addData(comparingRollingMoney, new Long(0));
						regData.addData(comparingRigidMoney, new Long(0));
						regData.addData(comparingDebts, new Long(0));
						regData.addData(comparingRollingMoneyMinusDebts,new Long(0));
						
						regData.addData(dummyOrderingField,"A");
					}
					
					//getWorkReportBusiness().getWorkReportClubAccountRecordHome().
					
					//Amounts for selected year
					//income is account key collection
					Collection incomeKeys = getWorkReportBusiness().getWorkReportAccountKeyHome().findIncomeAccountKeysWithoutSubKeys();
					long incomeTotal = getWorkReportBusiness().getTotalAmmountOfAccountRecordsByWorkReportIdWorkReportGroupIdAndWorkReportAccountKeyCollection(reportId,wrGroupId,incomeKeys);
					addToLongCount(income,regData,incomeTotal);
					//expenses is account key collection
					Collection expensesKeys = getWorkReportBusiness().getWorkReportAccountKeyHome().findExpensesAccountKeysWithoutSubKeys();
					long expensesTotal = getWorkReportBusiness().getTotalAmmountOfAccountRecordsByWorkReportIdWorkReportGroupIdAndWorkReportAccountKeyCollection(reportId,wrGroupId,expensesKeys);
					addToLongCount(expenses,regData,expensesTotal);
					//income minus expenses
					addToLongCount(incomeMinusExpenses,regData,incomeTotal - expensesTotal);
					//rollingMoney FIN_89998
					long rollingMoneyTotal = getWorkReportBusiness().getAmmountOfAccountRecordByWorkReportIdWorkReportGroupIdAndWorkReportAccountKeyName(reportId,wrGroupId,"FIN_89998");
					addToLongCount(rollingMoney,regData,rollingMoneyTotal);
					//rigidMoney FIN_84999
					long rigidMoneyTotal = getWorkReportBusiness().getAmmountOfAccountRecordByWorkReportIdWorkReportGroupIdAndWorkReportAccountKeyName(reportId,wrGroupId,"FIN_84999");
					addToLongCount(rigidMoney,regData,rigidMoneyTotal);
					//debts FIN_99998
					long debtsTotal = getWorkReportBusiness().getAmmountOfAccountRecordByWorkReportIdWorkReportGroupIdAndWorkReportAccountKeyName(reportId,wrGroupId,"FIN_99998");
					addToLongCount(debts,regData,debtsTotal);
					//rollingmoney minus debts
					addToLongCount(rollingMoneyMinusDebts,regData,(rollingMoneyTotal-debtsTotal));
					
					//Amounts for comparingYear
					if(comparingReportId!=-1 && comparingWrGroupId!=-1){//just zero's otherwise
						//income is account key collection
						long comparingIncomeTotal = getWorkReportBusiness().getTotalAmmountOfAccountRecordsByWorkReportIdWorkReportGroupIdAndWorkReportAccountKeyCollection(comparingReportId,comparingWrGroupId,incomeKeys);
						addToLongCount(comparingIncome,regData,comparingIncomeTotal);
						//expenses is account key collection
						long comparingExpensesTotal = getWorkReportBusiness().getTotalAmmountOfAccountRecordsByWorkReportIdWorkReportGroupIdAndWorkReportAccountKeyCollection(comparingReportId,comparingWrGroupId,expensesKeys);
						addToLongCount(comparingExpenses,regData,comparingExpensesTotal);
						//income minus expenses
						addToLongCount(comparingIncomeMinusExpenses,regData,comparingIncomeTotal - comparingExpensesTotal);
						//rollingMoney FIN_89998
						long comparingRollingMoneyTotal = getWorkReportBusiness().getAmmountOfAccountRecordByWorkReportIdWorkReportGroupIdAndWorkReportAccountKeyName(comparingReportId,comparingWrGroupId,"FIN_89998");
						addToLongCount(comparingRollingMoney,regData,comparingRollingMoneyTotal);
						//rigidMoney FIN_84999
						long comparingRigidMoneyTotal = getWorkReportBusiness().getAmmountOfAccountRecordByWorkReportIdWorkReportGroupIdAndWorkReportAccountKeyName(comparingReportId,comparingWrGroupId,"FIN_84999");
						addToLongCount(comparingRigidMoney,regData,comparingRigidMoneyTotal);
						//debts FIN_99998
						long comparingDebtsTotal = getWorkReportBusiness().getAmmountOfAccountRecordByWorkReportIdWorkReportGroupIdAndWorkReportAccountKeyName(comparingReportId,comparingWrGroupId,"FIN_99998");
						addToLongCount(comparingDebts,regData,comparingDebtsTotal);
						//rollingmoney minus debts
						addToLongCount(comparingRollingMoneyMinusDebts,regData,(comparingRollingMoneyTotal-comparingDebtsTotal));
					}
					
					recordsMapKeyedByLeagueIdentifierAndClubsName.put(mapKey,regData);
					
					////////////////////////////////////////////////////////////////////////////////////////////////
					//Get and display the report for the league itself
					String leagueKey = leagueIdentifier+"leaguesowndata";
					ReportableData leagueData = (ReportableData) recordsMapKeyedByLeagueIdentifierAndClubsName.get(leagueKey);
					if(leagueData==null){
						int yearOfReport = year.intValue();
						int comparingYearInt = comparingYear.intValue();
						WorkReport leagueReport = null;
						WorkReport comparingleagueReport=null;
						int comparingLeagueReportId = -1;
						int leagueReportId = -1;
						
						try{
							leagueReport = getWorkReportBusiness().getWorkReportHome().findWorkReportByGroupIdAndYearOfReport(league.getGroupId().intValue(),yearOfReport);
							leagueReportId = ((Integer)leagueReport.getPrimaryKey()).intValue();
						}
						catch (FinderException e) {
							//no report for the league itself
						}
						
						try {
							comparingleagueReport = getWorkReportBusiness().getWorkReportHome().findWorkReportByGroupIdAndYearOfReport(league.getGroupId().intValue(),comparingYearInt);
							comparingLeagueReportId = ((Integer)comparingleagueReport.getPrimaryKey()).intValue();
						}
						catch (FinderException e1) {
							//no report that year
						}
						
						WorkReportGroup mainBoardWRGroup = this.getWorkReportBusiness().getMainBoardWorkReportGroup(yearOfReport);
						WorkReportGroup comparingMainBoardWRGroup = this.getWorkReportBusiness().getMainBoardWorkReportGroup(comparingYearInt);
						int mainBoardWRGroupId = -1;
						int comparingMainBoardWRGroupId = -1;
						
						if(mainBoardWRGroup!=null){
							mainBoardWRGroupId = ((Integer)mainBoardWRGroup.getPrimaryKey()).intValue();
						}	
						if(comparingMainBoardWRGroup!=null){
							comparingMainBoardWRGroupId = ((Integer)comparingMainBoardWRGroup.getPrimaryKey()).intValue();
						}
						
						//add the data					
						//fetch the stats or initialize
						
						//fill with data -> only do this once!
						leagueData = new ReportableData();
						leagueData.addData(leagueString, leagueIdentifier);
						leagueData.addData(clubName, "");
						leagueData.addData(regionalUnionAbbreviation, "Samtals");
						leagueData.addData(clubType, "");
						
						leagueData.addData(income, new Long(0));
						leagueData.addData(expenses, new Long(0));
						leagueData.addData(incomeMinusExpenses,new Long(0));
						leagueData.addData(rollingMoney, new Long(0));
						leagueData.addData(rigidMoney, new Long(0));
						leagueData.addData(debts, new Long(0));
						leagueData.addData(rollingMoneyMinusDebts,new Long(0));
						
						leagueData.addData(comparingIncome, new Long(0));
						leagueData.addData(comparingExpenses, new Long(0));
						leagueData.addData(comparingIncomeMinusExpenses,new Long(0));
						leagueData.addData(comparingRollingMoney, new Long(0));
						leagueData.addData(comparingRigidMoney, new Long(0));
						leagueData.addData(comparingDebts, new Long(0));
						leagueData.addData(comparingRollingMoneyMinusDebts,new Long(0));
						
						leagueData.addData(dummyOrderingField,"B");
						
						
						if(leagueReportId>0 && mainBoardWRGroupId>0){
							//Amounts for selected year
							//income is account key collection
							
							incomeTotal = getWorkReportBusiness().getTotalAmmountOfAccountRecordsByWorkReportIdWorkReportGroupIdAndWorkReportAccountKeyCollection(leagueReportId,mainBoardWRGroupId,incomeKeys);
							addToLongCount(income,leagueData,incomeTotal);
							//expenses is account key collection
							expensesTotal = getWorkReportBusiness().getTotalAmmountOfAccountRecordsByWorkReportIdWorkReportGroupIdAndWorkReportAccountKeyCollection(leagueReportId,mainBoardWRGroupId,expensesKeys);
							addToLongCount(expenses,leagueData,expensesTotal);
							//income minus expenses
							addToLongCount(incomeMinusExpenses,leagueData,incomeTotal - expensesTotal);
							//rollingMoney FIN_89998
							rollingMoneyTotal = getWorkReportBusiness().getAmmountOfAccountRecordByWorkReportIdWorkReportGroupIdAndWorkReportAccountKeyName(leagueReportId,mainBoardWRGroupId,"FIN_89998");
							addToLongCount(rollingMoney,leagueData,rollingMoneyTotal);
							//rigidMoney FIN_84999
							rigidMoneyTotal = getWorkReportBusiness().getAmmountOfAccountRecordByWorkReportIdWorkReportGroupIdAndWorkReportAccountKeyName(leagueReportId,mainBoardWRGroupId,"FIN_84999");
							addToLongCount(rigidMoney,leagueData,rigidMoneyTotal);
							//debts FIN_99998
							debtsTotal = getWorkReportBusiness().getAmmountOfAccountRecordByWorkReportIdWorkReportGroupIdAndWorkReportAccountKeyName(leagueReportId,mainBoardWRGroupId,"FIN_99998");
							addToLongCount(debts,leagueData,debtsTotal);
							//rollingmoney minus debts
							addToLongCount(rollingMoneyMinusDebts,leagueData,(rollingMoneyTotal-debtsTotal));
							
						}
						
						
						if(comparingLeagueReportId>0 && comparingMainBoardWRGroupId>0){	
							//Amounts for comparingYear
							
							//income is account key collection
							long comparingIncomeTotal = getWorkReportBusiness().getTotalAmmountOfAccountRecordsByWorkReportIdWorkReportGroupIdAndWorkReportAccountKeyCollection(comparingLeagueReportId,comparingMainBoardWRGroupId,incomeKeys);
							addToLongCount(comparingIncome,leagueData,comparingIncomeTotal);
							//expenses is account key collection
							long comparingExpensesTotal = getWorkReportBusiness().getTotalAmmountOfAccountRecordsByWorkReportIdWorkReportGroupIdAndWorkReportAccountKeyCollection(comparingLeagueReportId,comparingMainBoardWRGroupId,expensesKeys);
							addToLongCount(comparingExpenses,leagueData,comparingExpensesTotal);
							//income minus expenses
							addToLongCount(comparingIncomeMinusExpenses,leagueData,comparingIncomeTotal - comparingExpensesTotal);
							//rollingMoney FIN_89998
							long comparingRollingMoneyTotal = getWorkReportBusiness().getAmmountOfAccountRecordByWorkReportIdWorkReportGroupIdAndWorkReportAccountKeyName(comparingLeagueReportId,comparingMainBoardWRGroupId,"FIN_89998");
							addToLongCount(comparingRollingMoney,leagueData,comparingRollingMoneyTotal);
							//rigidMoney FIN_84999
							long comparingRigidMoneyTotal = getWorkReportBusiness().getAmmountOfAccountRecordByWorkReportIdWorkReportGroupIdAndWorkReportAccountKeyName(comparingLeagueReportId,comparingMainBoardWRGroupId,"FIN_84999");
							addToLongCount(comparingRigidMoney,leagueData,comparingRigidMoneyTotal);
							//debts FIN_99998
							long comparingDebtsTotal = getWorkReportBusiness().getAmmountOfAccountRecordByWorkReportIdWorkReportGroupIdAndWorkReportAccountKeyName(comparingLeagueReportId,comparingMainBoardWRGroupId,"FIN_99998");
							addToLongCount(comparingDebts,leagueData,comparingDebtsTotal);
							//rollingmoney minus debts
							addToLongCount(comparingRollingMoneyMinusDebts,leagueData,(comparingRollingMoneyTotal-comparingDebtsTotal));
						}
						
						
						//put it in		
						recordsMapKeyedByLeagueIdentifierAndClubsName.put(leagueKey,leagueData);
					}
					
					
					//SUM DATA STARTS
					WorkReportDivisionBoard division = null;
					try{
						division = getWorkReportBusiness().getWorkReportDivisionBoardHome().findWorkReportDivisionBoardByWorkReportIdAndWorkReportGroupId(reportId,wrGroupId);
					}
					catch (FinderException e) {
						System.out.println("Division not found for workReport: "+report.getGroupName()+"/"+report.getYearOfReport()+ " and workReportGroup: "+league.getName()+"/"+league.getYearOfReport());
					    //e.printStackTrace();
					}
					
					if(division!=null && division.hasNationalLeague()){
						String divMapKey = leagueIdentifier+"hasNationalLeagueSum";
						ReportableData divDataSum = (ReportableData) recordsMapKeyedByLeagueIdentifierAndClubsName.get(divMapKey);
						if(divDataSum==null){//initialize
							divDataSum = new ReportableData();
							divDataSum.addData(leagueString, leagueIdentifier);
							
							divDataSum.addData(clubName, this._iwrb.getLocalizedString("WorkReportStatsBusiness.clubs_with_national_league","Clubs with national league"));
							
							divDataSum.addData(regionalUnionAbbreviation, "Samtals");
							divDataSum.addData(clubType,"");
							
							divDataSum.addData(income, new Long(0));
							divDataSum.addData(expenses, new Long(0));
							divDataSum.addData(incomeMinusExpenses,new Long(0));
							divDataSum.addData(rollingMoney, new Long(0));
							divDataSum.addData(rigidMoney, new Long(0));
							divDataSum.addData(debts, new Long(0));
							divDataSum.addData(rollingMoneyMinusDebts,new Long(0));
							
							divDataSum.addData(comparingIncome, new Long(0));
							divDataSum.addData(comparingExpenses, new Long(0));
							divDataSum.addData(comparingIncomeMinusExpenses,new Long(0));
							divDataSum.addData(comparingRollingMoney, new Long(0));
							divDataSum.addData(comparingRigidMoney, new Long(0));
							divDataSum.addData(comparingDebts, new Long(0));
							divDataSum.addData(comparingRollingMoneyMinusDebts,new Long(0));
							
							
							
							
							
							divDataSum.addData(dummyOrderingField,"C");
						}
						
						addToLongCountFromFieldInAnotherReportableData(income,divDataSum,regData);
						addToLongCountFromFieldInAnotherReportableData(expenses,divDataSum,regData);
						addToLongCountFromFieldInAnotherReportableData(incomeMinusExpenses,divDataSum,regData);
						addToLongCountFromFieldInAnotherReportableData(rollingMoney,divDataSum,regData);
						addToLongCountFromFieldInAnotherReportableData(rigidMoney,divDataSum,regData);
						addToLongCountFromFieldInAnotherReportableData(debts,divDataSum,regData);
						addToLongCountFromFieldInAnotherReportableData(rollingMoneyMinusDebts,divDataSum,regData);
						addToLongCountFromFieldInAnotherReportableData(comparingIncome,divDataSum,regData);
						addToLongCountFromFieldInAnotherReportableData(comparingExpenses,divDataSum,regData);
						addToLongCountFromFieldInAnotherReportableData(comparingIncomeMinusExpenses,divDataSum,regData);
						addToLongCountFromFieldInAnotherReportableData(comparingRollingMoney,divDataSum,regData);
						addToLongCountFromFieldInAnotherReportableData(comparingRigidMoney,divDataSum,regData);
						addToLongCountFromFieldInAnotherReportableData(comparingDebts,divDataSum,regData);
						addToLongCountFromFieldInAnotherReportableData(comparingRollingMoneyMinusDebts,divDataSum,regData);
						
						
						recordsMapKeyedByLeagueIdentifierAndClubsName.put(divMapKey,divDataSum);
						
					}
					else{//add to the total
						String noDivMapKey = leagueIdentifier+"DoesNotHaveNationalLeagueSum";
						ReportableData divDataSum = (ReportableData) recordsMapKeyedByLeagueIdentifierAndClubsName.get(noDivMapKey);
						if(divDataSum==null){//initialize
							divDataSum = new ReportableData();
							divDataSum.addData(leagueString, leagueIdentifier);
							divDataSum.addData(clubName,this._iwrb.getLocalizedString("WorkReportStatsBusiness.clubs_without_national_league","Clubs without a national league"));
							divDataSum.addData(regionalUnionAbbreviation, "Samtals");
							divDataSum.addData(clubType,"");
							
							divDataSum.addData(income, new Long(0));
							divDataSum.addData(expenses, new Long(0));
							divDataSum.addData(incomeMinusExpenses,new Long(0));
							divDataSum.addData(rollingMoney, new Long(0));
							divDataSum.addData(rigidMoney, new Long(0));
							divDataSum.addData(debts, new Long(0));
							divDataSum.addData(rollingMoneyMinusDebts,new Long(0));
							
							divDataSum.addData(comparingIncome, new Long(0));
							divDataSum.addData(comparingExpenses, new Long(0));
							divDataSum.addData(comparingIncomeMinusExpenses,new Long(0));
							divDataSum.addData(comparingRollingMoney, new Long(0));
							divDataSum.addData(comparingRigidMoney, new Long(0));
							divDataSum.addData(comparingDebts, new Long(0));
							divDataSum.addData(comparingRollingMoneyMinusDebts,new Long(0));
							
							divDataSum.addData(dummyOrderingField,"D");
						}
						
						addToLongCountFromFieldInAnotherReportableData(income,divDataSum,regData);
						addToLongCountFromFieldInAnotherReportableData(expenses,divDataSum,regData);
						addToLongCountFromFieldInAnotherReportableData(incomeMinusExpenses,divDataSum,regData);
						addToLongCountFromFieldInAnotherReportableData(rollingMoney,divDataSum,regData);
						addToLongCountFromFieldInAnotherReportableData(rigidMoney,divDataSum,regData);
						addToLongCountFromFieldInAnotherReportableData(debts,divDataSum,regData);
						addToLongCountFromFieldInAnotherReportableData(rollingMoneyMinusDebts,divDataSum,regData);
						
						addToLongCountFromFieldInAnotherReportableData(comparingIncome,divDataSum,regData);
						addToLongCountFromFieldInAnotherReportableData(comparingExpenses,divDataSum,regData);
						addToLongCountFromFieldInAnotherReportableData(comparingIncomeMinusExpenses,divDataSum,regData);
						addToLongCountFromFieldInAnotherReportableData(comparingRollingMoney,divDataSum,regData);
						addToLongCountFromFieldInAnotherReportableData(comparingRigidMoney,divDataSum,regData);
						addToLongCountFromFieldInAnotherReportableData(comparingDebts,divDataSum,regData);
						addToLongCountFromFieldInAnotherReportableData(comparingRollingMoneyMinusDebts,divDataSum,regData);
						
						
						recordsMapKeyedByLeagueIdentifierAndClubsName.put(noDivMapKey,divDataSum);
						
					}
					
					//ALL SUM
					String allSumKey = leagueIdentifier+"ALLSUM";
					ReportableData divDataSum = (ReportableData) recordsMapKeyedByLeagueIdentifierAndClubsName.get(allSumKey);
					if(divDataSum==null){
						//initialize
						divDataSum = new ReportableData();
						divDataSum.addData(leagueString, leagueIdentifier);
						divDataSum.addData(clubName, this._iwrb.getLocalizedString("WorkReportStatsBusiness.all_clubs","All clubs"));
						divDataSum.addData(regionalUnionAbbreviation, "Samtals");
						divDataSum.addData(clubType,"");
						
						divDataSum.addData(income, new Long(0));
						divDataSum.addData(expenses, new Long(0));
						divDataSum.addData(incomeMinusExpenses,new Long(0));
						divDataSum.addData(rollingMoney, new Long(0));
						divDataSum.addData(rigidMoney, new Long(0));
						divDataSum.addData(debts, new Long(0));
						divDataSum.addData(rollingMoneyMinusDebts,new Long(0));
						
						divDataSum.addData(comparingIncome, new Long(0));
						divDataSum.addData(comparingExpenses, new Long(0));
						divDataSum.addData(comparingIncomeMinusExpenses,new Long(0));
						divDataSum.addData(comparingRollingMoney, new Long(0));
						divDataSum.addData(comparingRigidMoney, new Long(0));
						divDataSum.addData(comparingDebts, new Long(0));
						divDataSum.addData(comparingRollingMoneyMinusDebts,new Long(0));
						
						//sum for layout
						
						divDataSum.addData(incomeSum, new Long(0));
						divDataSum.addData(expensesSum, new Long(0));
						divDataSum.addData(incomeMinusExpensesSum,new Long(0));
						divDataSum.addData(rollingMoneySum, new Long(0));
						divDataSum.addData(rigidMoneySum, new Long(0));
						divDataSum.addData(debtsSum, new Long(0));
						divDataSum.addData(rollingMoneyMinusDebtsSum,new Long(0));
						
						divDataSum.addData(comparingIncomeSum, new Long(0));
						divDataSum.addData(comparingExpensesSum, new Long(0));
						divDataSum.addData(comparingIncomeMinusExpensesSum,new Long(0));
						divDataSum.addData(comparingRollingMoneySum, new Long(0));
						divDataSum.addData(comparingRigidMoneySum, new Long(0));
						divDataSum.addData(comparingDebtsSum, new Long(0));
						divDataSum.addData(comparingRollingMoneyMinusDebtsSum,new Long(0));
						
						
						divDataSum.addData(dummyOrderingField,"E");
					}
					
					addToLongCountFromFieldInAnotherReportableData(income,divDataSum,regData);					
					addToLongCountFromFieldInAnotherReportableData(expenses,divDataSum,regData);
					addToLongCountFromFieldInAnotherReportableData(incomeMinusExpenses,divDataSum,regData);
					addToLongCountFromFieldInAnotherReportableData(rollingMoney,divDataSum,regData);
					addToLongCountFromFieldInAnotherReportableData(rigidMoney,divDataSum,regData);
					addToLongCountFromFieldInAnotherReportableData(debts,divDataSum,regData);
					addToLongCountFromFieldInAnotherReportableData(rollingMoneyMinusDebts,divDataSum,regData);
					
					addToLongCountFromFieldInAnotherReportableData(comparingIncome,divDataSum,regData);
					addToLongCountFromFieldInAnotherReportableData(comparingExpenses,divDataSum,regData);
					addToLongCountFromFieldInAnotherReportableData(comparingIncomeMinusExpenses,divDataSum,regData);
					addToLongCountFromFieldInAnotherReportableData(comparingRollingMoney,divDataSum,regData);
					addToLongCountFromFieldInAnotherReportableData(comparingRigidMoney,divDataSum,regData);
					addToLongCountFromFieldInAnotherReportableData(comparingDebts,divDataSum,regData);
					addToLongCountFromFieldInAnotherReportableData(comparingRollingMoneyMinusDebts,divDataSum,regData);
					
					//sum stuff
					addToLongCountFromFieldInAnotherReportableData(income, incomeSum,divDataSum,regData);
					addToLongCountFromFieldInAnotherReportableData(expenses,expensesSum,divDataSum,regData);
					addToLongCountFromFieldInAnotherReportableData(incomeMinusExpenses, incomeMinusExpensesSum,divDataSum,regData);
					addToLongCountFromFieldInAnotherReportableData(rollingMoney, rollingMoneySum,divDataSum,regData);
					addToLongCountFromFieldInAnotherReportableData(rigidMoney,rigidMoneySum,divDataSum,regData);
					addToLongCountFromFieldInAnotherReportableData(debts,debtsSum,divDataSum,regData);
					addToLongCountFromFieldInAnotherReportableData(rollingMoneyMinusDebts,rollingMoneyMinusDebtsSum,divDataSum,regData);
					
					addToLongCountFromFieldInAnotherReportableData(comparingIncome,comparingIncomeSum,divDataSum,regData);
					addToLongCountFromFieldInAnotherReportableData(comparingExpenses,comparingExpensesSum,divDataSum,regData);
					addToLongCountFromFieldInAnotherReportableData(comparingIncomeMinusExpenses,comparingIncomeMinusExpensesSum,divDataSum,regData);
					addToLongCountFromFieldInAnotherReportableData(comparingRollingMoney,comparingRollingMoneySum,divDataSum,regData);
					addToLongCountFromFieldInAnotherReportableData(comparingRigidMoney,comparingRigidMoneySum,divDataSum,regData);
					addToLongCountFromFieldInAnotherReportableData(comparingDebts,comparingDebtsSum,divDataSum,regData);
					addToLongCountFromFieldInAnotherReportableData(comparingRollingMoneyMinusDebts,comparingRollingMoneyMinusDebtsSum,divDataSum,regData);
					
					
					recordsMapKeyedByLeagueIdentifierAndClubsName.put(allSumKey,divDataSum);
				}
				
			}
		}
		catch (IDOException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		
		reportCollection.addAll(recordsMapKeyedByLeagueIdentifierAndClubsName.values());
		
		ReportableField[] sortFields = new ReportableField[] {leagueString,regionalUnionAbbreviation,clubName,dummyOrderingField};
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);
		
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
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		//PARAMETES

		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				this._iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale),"GMT"));

		ReportableField clubName = new ReportableField(FIELD_NAME_CLUB_NAME, String.class);
		clubName.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_CLUB_NAME, "Club name"), currentLocale);
		reportCollection.addField(clubName);

		ReportableField clubNumber = new ReportableField(FIELD_NAME_CLUB_NUMBER, String.class);
		clubNumber.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_CLUB_NUMBER, "Club number"), currentLocale);
		reportCollection.addField(clubNumber);

		ReportableField regionalUnionAbbreviation = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionAbbreviation.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionAbbreviation);

		ReportableField workReportStatus = new ReportableField(FIELD_NAME_WORK_REPORT_STATUS, String.class);
		workReportStatus.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WORK_REPORT_STATUS, "Work Report Status"), currentLocale);
		reportCollection.addField(workReportStatus);
		
		ReportableField workReportStatusInfo = new ReportableField(FIELD_NAME_WORK_REPORT_STATUS_INFO, String.class);
		workReportStatusInfo.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WORK_REPORT_STATUS_INFO, "Work Report Status Info"), currentLocale);
		reportCollection.addField(workReportStatusInfo);
		
		ReportableField workReportStatusRemarks = new ReportableField(FIELD_NAME_WORK_REPORT_STATUS_REMARKS, String.class);
		workReportStatusRemarks.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WORK_REPORT_STATUS_REMARKS, "Work Report Status Remarks"), currentLocale);
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
			//System.out.print("Processing club " + cName);

			boolean showClub = showClubType(report, type) && showClubStatus(report, status);
			if(!showClub) {
				//System.out.println(" (skipped)");
				continue;
			}
			if(IWMemberConstants.GROUP_TYPE_LEAGUE.equals(report.getGroupType())) {
				// don't show leagues
				continue;
			}
			//System.out.println();
			
			String reportStatus = report.getStatus();
			//System.out.println("Status of report is " + reportStatus);
			String statusString = "";
			if(WorkReportConstants.WR_STATUS_DONE.equals(reportStatus)) {
				statusString = this._iwrb.getLocalizedString(WorkReportStatusDropDownMenu.LOCALIZED_STATUS_DONE, "Done");
			} else if(WorkReportConstants.WR_STATUS_NO_REPORT.equals(reportStatus)) {
				statusString = this._iwrb.getLocalizedString(WorkReportStatusDropDownMenu.LOCALIZED_STATUS_NO_REPORT, "No Report");
			} else if(WorkReportConstants.WR_STATUS_NOT_DONE.equals(reportStatus)) {
				statusString = this._iwrb.getLocalizedString(WorkReportStatusDropDownMenu.LOCALIZED_STATUS_NOT_DONE, "Not Done");
			} else if(WorkReportConstants.WR_STATUS_SOME_DONE.equals(reportStatus)) {
				statusString = this._iwrb.getLocalizedString(WorkReportStatusDropDownMenu.LOCALIZED_STATUS_SOME_DONE, "Some Done");
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
			String onlyTotals,
			Integer splitAge)
	throws RemoteException {
		
		boolean showAll = true;
		if(YesNoDropDownMenu.YES.equals(onlyTotals)) {
			System.out.println("Showing only totals");
			showAll = false;
		} else {
			System.out.println("Showing all");
		}
		
		//initialize stuff
		int age = splitAge==null?DEFAULT_SPLIT_AGE:splitAge.intValue();
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		//PARAMETES

		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				this._iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale),"GMT"));

		ReportableField clubName = new ReportableField(FIELD_NAME_CLUB_NAME, String.class);
		clubName.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_CLUB_NAME, "Club name"), currentLocale);
		reportCollection.addField(clubName);
		
		ReportableField orderField = new ReportableField(FIELD_NAME_ORDERING, String.class);
		orderField.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ORDERING, "Ordering"), currentLocale);
		reportCollection.addField(orderField);

		ReportableField divisionName = new ReportableField(FIELD_NAME_DIVISION_NAME, String.class);
		divisionName.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_DIVISION_NAME, "Division"), currentLocale);
		reportCollection.addField(divisionName);

		ReportableField annualReportStatus = new ReportableField(FIELD_NAME_ANNUAL_REPORT_STATUS, String.class);
		annualReportStatus.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ANNUAL_REPORT_STATUS, "Annual Report Status"), currentLocale);
		reportCollection.addField(annualReportStatus);
		
		// Fields for member stats
		ReportableField womenMembersUnderAge = new ReportableField(FIELD_NAME_WOMEN_MEMBERS_UNDER_AGE, Integer.class);
		womenMembersUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER, "women -"+age), currentLocale);
		reportCollection.addField(womenMembersUnderAge);

		ReportableField womenMembersOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_MEMBERS_OVER_OR_EQUAL_AGE, Integer.class);
		womenMembersOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenMembersOverOrEqualAgeLimit);

		ReportableField menMembersUnderAge = new ReportableField(FIELD_NAME_MEN_MEMBERS_UNDER_AGE, Integer.class);
		menMembersUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_UNDER, "men -"+age), currentLocale);
		reportCollection.addField(menMembersUnderAge);

		ReportableField menMembersOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_MEMBERS_OVER_OR_EQUAL_AGE, Integer.class);
		menMembersOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL, "men "+age+"+"),currentLocale);
		reportCollection.addField(menMembersOverOrEqualAgeLimit);

		ReportableField bothGendersMembersUnderAge = new ReportableField(FIELD_NAME_ALL_MEMBERS_UNDER_AGE, Integer.class);
		bothGendersMembersUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_UNDER, "all -"+age),currentLocale);
		reportCollection.addField(bothGendersMembersUnderAge);

		ReportableField bothGendersMembersEqualOrOver = new ReportableField(FIELD_NAME_ALL_MEMBERS_OVER_OR_EQUAL_AGE, Integer.class);
		bothGendersMembersEqualOrOver.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER, "all "+age+"+"),currentLocale);
		reportCollection.addField(bothGendersMembersEqualOrOver);

		ReportableField bothGendersMembersAllAge = new ReportableField(FIELD_NAME_ALL_MEMBERS_AGES, Integer.class);
		bothGendersMembersAllAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL, "all"), currentLocale);
		reportCollection.addField(bothGendersMembersAllAge);
		
		// Fields for member stats totals
		ReportableField womenMembersUnderAgeTot = new ReportableField(FIELD_NAME_WOMEN_MEMBERS_UNDER_AGE + "_tot", Integer.class);
		womenMembersUnderAgeTot.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER, "women -"+age), currentLocale);
		reportCollection.addField(womenMembersUnderAgeTot);

		ReportableField womenMembersOverOrEqualAgeLimitTot = new ReportableField(FIELD_NAME_WOMEN_MEMBERS_OVER_OR_EQUAL_AGE + "_tot", Integer.class);
		womenMembersOverOrEqualAgeLimitTot.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenMembersOverOrEqualAgeLimitTot);

		ReportableField menMembersUnderAgeTot = new ReportableField(FIELD_NAME_MEN_MEMBERS_UNDER_AGE + "_tot", Integer.class);
		menMembersUnderAgeTot.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_UNDER, "men -"+age), currentLocale);
		reportCollection.addField(menMembersUnderAgeTot);

		ReportableField menMembersOverOrEqualAgeLimitTot = new ReportableField(FIELD_NAME_MEN_MEMBERS_OVER_OR_EQUAL_AGE + "_tot", Integer.class);
		menMembersOverOrEqualAgeLimitTot.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL, "men "+age+"+"),currentLocale);
		reportCollection.addField(menMembersOverOrEqualAgeLimitTot);

		ReportableField bothGendersMembersUnderAgeTot = new ReportableField(FIELD_NAME_ALL_MEMBERS_UNDER_AGE + "_tot", Integer.class);
		bothGendersMembersUnderAgeTot.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_UNDER, "all -"+age),currentLocale);
		reportCollection.addField(bothGendersMembersUnderAgeTot);

		ReportableField bothGendersMembersEqualOrOverTot = new ReportableField(FIELD_NAME_ALL_MEMBERS_OVER_OR_EQUAL_AGE + "_tot", Integer.class);
		bothGendersMembersEqualOrOverTot.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER, "all "+age+"+"),currentLocale);
		reportCollection.addField(bothGendersMembersEqualOrOverTot);

		ReportableField bothGendersMembersAllAgeTot = new ReportableField(FIELD_NAME_ALL_MEMBERS_AGES + "_tot", Integer.class);
		bothGendersMembersAllAgeTot.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL, "all"), currentLocale);
		reportCollection.addField(bothGendersMembersAllAgeTot);
		
		// Fields for player stats
		ReportableField womenPlayersUnderAge = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE, Integer.class);
		womenPlayersUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER, "women -"+age), currentLocale);
		reportCollection.addField(womenPlayersUnderAge);
		
		ReportableField womenPlayersOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE, Integer.class);
		womenPlayersOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenPlayersOverOrEqualAgeLimit);
		
		ReportableField menPlayersUnderAge = new ReportableField(FIELD_NAME_MEN_UNDER_AGE, Integer.class);
		menPlayersUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_UNDER, "men -"+age), currentLocale);
		reportCollection.addField(menPlayersUnderAge);
		
		ReportableField menPlayersOverOrEqualAgeLimit = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE, Integer.class);
		menPlayersOverOrEqualAgeLimit.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL, "men "+age+"+"),currentLocale);
		reportCollection.addField(menPlayersOverOrEqualAgeLimit);
		
		ReportableField bothGendersPlayersUnderAge = new ReportableField(FIELD_NAME_ALL_UNDER_AGE, Integer.class);
		bothGendersPlayersUnderAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_UNDER, "all -"+age),currentLocale);
		reportCollection.addField(bothGendersPlayersUnderAge);
		
		ReportableField bothGendersPlayersEqualOrOver = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE, Integer.class);
		bothGendersPlayersEqualOrOver.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER, "all "+age+"+"),currentLocale);
		reportCollection.addField(bothGendersPlayersEqualOrOver);
		
		ReportableField bothGendersPlayersAllAge = new ReportableField(FIELD_NAME_ALL_AGES, Integer.class);
		bothGendersPlayersAllAge.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL, "all"), currentLocale);
		reportCollection.addField(bothGendersPlayersAllAge);
		
		// Fields for player stats totals
		ReportableField womenPlayersUnderAgeTot = new ReportableField(FIELD_NAME_WOMEN_UNDER_AGE + "_tot", Integer.class);
		womenPlayersUnderAgeTot.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_UNDER, "women -"+age), currentLocale);
		reportCollection.addField(womenPlayersUnderAgeTot);
		
		ReportableField womenPlayersOverOrEqualAgeLimitTot = new ReportableField(FIELD_NAME_WOMEN_OVER_OR_EQUAL_AGE + "_tot", Integer.class);
		womenPlayersOverOrEqualAgeLimitTot.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_WOMEN_OVER_OR_EQUAL, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenPlayersOverOrEqualAgeLimitTot);
		
		ReportableField menPlayersUnderAgeTot = new ReportableField(FIELD_NAME_MEN_UNDER_AGE + "_tot", Integer.class);
		menPlayersUnderAgeTot.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_UNDER, "men -"+age), currentLocale);
		reportCollection.addField(menPlayersUnderAgeTot);
		
		ReportableField menPlayersOverOrEqualAgeLimitTot = new ReportableField(FIELD_NAME_MEN_OVER_OR_EQUAL_AGE + "_tot", Integer.class);
		menPlayersOverOrEqualAgeLimitTot.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEN_OVER_OR_EQUAL, "men "+age+"+"),currentLocale);
		reportCollection.addField(menPlayersOverOrEqualAgeLimitTot);
		
		ReportableField bothGendersPlayersUnderAgeTot = new ReportableField(FIELD_NAME_ALL_UNDER_AGE + "_tot", Integer.class);
		bothGendersPlayersUnderAgeTot.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_UNDER, "all -"+age),currentLocale);
		reportCollection.addField(bothGendersPlayersUnderAgeTot);
		
		ReportableField bothGendersPlayersEqualOrOverTot = new ReportableField(FIELD_NAME_ALL_OVER_OR_EQUAL_AGE + "_tot", Integer.class);
		bothGendersPlayersEqualOrOverTot.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL_EQUAL_OR_OVER, "all "+age+"+"),currentLocale);
		reportCollection.addField(bothGendersPlayersEqualOrOverTot);
		
		ReportableField bothGendersPlayersAllAgeTot = new ReportableField(FIELD_NAME_ALL_AGES + "_tot", Integer.class);
		bothGendersPlayersAllAgeTot.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ALL, "all"), currentLocale);
		reportCollection.addField(bothGendersPlayersAllAgeTot);
		
		WorkReportGroup mainBoardGroup = getWorkReportBusiness().getMainBoardWorkReportGroup(year.intValue());
		Integer mbId = mainBoardGroup==null?(new Integer(-1)):mainBoardGroup.getGroupId();
		
		Collection clubs = getWorkReportBusiness().getWorkReportsByYearRegionalUnionsAndClubs(year.intValue(), regionalUnionsFilter, clubsFilter);
		Iterator iter = clubs.iterator();
		while (iter.hasNext()) {
			//the club
			WorkReport report = (WorkReport) iter.next();
			String cName = report.getGroupNumber() +" "+ report.getGroupName();
			
			if(IWMemberConstants.GROUP_TYPE_LEAGUE.equals(report.getGroupType())) {
				// don't show leagues
				continue;
			}
			
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
						Collection records = getWorkReportBusiness().getWorkReportClubAccountRecordHome().findAllRecordsByWorkReportIdAndWorkReportGroupId(((Integer)report.getPrimaryKey()).intValue(),((Integer)league.getPrimaryKey()).intValue());
						boolean hasData = (records!=null && !records.isEmpty());
						String status;
						if(hasData) {
							status = this._iwrb.getLocalizedString(LOCALIZED_ANNUAL_REPORT_IN, "In");
						} else {
							status = this._iwrb.getLocalizedString(LOCALIZED_ANNUAL_REPORT_MISSING, "Missing");
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
						
						regData.addData(womenMembersUnderAgeTot, new Integer(0));
						regData.addData(womenMembersOverOrEqualAgeLimitTot, new Integer(0));
						regData.addData(menMembersUnderAgeTot, new Integer(0));
						regData.addData(menMembersOverOrEqualAgeLimitTot, new Integer(0));
						regData.addData(bothGendersMembersUnderAgeTot, new Integer(0));
						regData.addData(bothGendersMembersEqualOrOverTot, new Integer(0));
						regData.addData(bothGendersMembersAllAgeTot, new Integer(0));
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
						
						regData.addData(womenPlayersUnderAgeTot, new Integer(0));
						regData.addData(womenPlayersOverOrEqualAgeLimitTot, new Integer(0));
						regData.addData(menPlayersUnderAgeTot, new Integer(0));
						regData.addData(menPlayersOverOrEqualAgeLimitTot, new Integer(0));
						regData.addData(bothGendersPlayersUnderAgeTot, new Integer(0));
						regData.addData(bothGendersPlayersEqualOrOverTot, new Integer(0));
						regData.addData(bothGendersPlayersAllAgeTot, new Integer(0));
					}
					if(showAll) {
						reportCollection.add(regData);
					}
				}
				
				ReportableData totals = new ReportableData();
				
				totals.addData(clubName, cName);
				totals.addData(orderField, "b");
				totals.addData(divisionName, "");
				totals.addData(annualReportStatus, this._iwrb.getLocalizedString(LOCALIZED_TOTAL, "TOTAL"));
				
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
				
				totals.addData(womenMembersUnderAgeTot, new Integer(totalWomenMembersUnder));
				totals.addData(womenMembersOverOrEqualAgeLimitTot, new Integer(totalWomenMembersEqualOrOver));
				totals.addData(menMembersUnderAgeTot, new Integer(totalMenMembersUnder));
				totals.addData(menMembersOverOrEqualAgeLimitTot, new Integer(totalMenMembersEqualOrOver));
				totals.addData(bothGendersMembersUnderAgeTot, new Integer(totalMembersUnder));
				totals.addData(bothGendersMembersEqualOrOverTot, new Integer(totalMembersEqualOrOver));
				totals.addData(bothGendersMembersAllAgeTot, new Integer(totalAllMembers));
				totals.addData(womenPlayersUnderAgeTot, new Integer(totalWomenPlayersUnder));
				totals.addData(womenPlayersOverOrEqualAgeLimitTot, new Integer(totalWomenPlayersEqualOrOver));
				totals.addData(menPlayersUnderAgeTot, new Integer(totalMenPlayersUnder));
				totals.addData(menPlayersOverOrEqualAgeLimitTot, new Integer(totalMenPlayersEqualOrOver));
				totals.addData(bothGendersPlayersUnderAgeTot, new Integer(totalPlayersUnder));
				totals.addData(bothGendersPlayersEqualOrOverTot, new Integer(totalPlayersEqualOrOver));
				totals.addData(bothGendersPlayersAllAgeTot, new Integer(totalAllPlayers));
				
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
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();

		//PARAMETES

		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				this._iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale),"GMT"));

		//PARAMETERS that are also FIELDS
		
		ReportableField regionalUnionName = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionName.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionName);

		ReportableField clubName = new ReportableField(FIELD_NAME_CLUB_NAME, String.class);
		clubName.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_CLUB_NAME, "Club Name"), currentLocale);
		reportCollection.addField(clubName);

		// members
		ReportableField membersThisYear = new ReportableField(FIELD_NAME_MEMBERS_THIS_YEAR, Integer.class);
		membersThisYear.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEMBERS_THIS_YEAR, "Members this year"), currentLocale);
		reportCollection.addField(membersThisYear);
	
		ReportableField membersLastYear = new ReportableField(FIELD_NAME_MEMBERS_LAST_YEAR, Integer.class);
		membersLastYear.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEMBERS_LAST_YEAR, "Members last year"), currentLocale);
		reportCollection.addField(membersLastYear);
		
		ReportableField membersAnnualChangePercent = new ReportableField(FIELD_NAME_MEMBERS_ANNUAL_CHANGE_PERCENT, String.class);
		membersAnnualChangePercent.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEMBERS_ANNUAL_CHANGE_PERCENT, "Member Annual Change"), currentLocale);
		reportCollection.addField(membersAnnualChangePercent);
		
		// players
		ReportableField playersThisYear = new ReportableField(FIELD_NAME_PLAYERS_THIS_YEAR, Integer.class);
		playersThisYear.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_PLAYERS_THIS_YEAR, "Players this year"), currentLocale);
		reportCollection.addField(playersThisYear);

		ReportableField playersLastYear = new ReportableField(FIELD_NAME_PLAYERS_LAST_YEAR, Integer.class);
		playersLastYear.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_PLAYERS_LAST_YEAR, "Players last year"), currentLocale);
		reportCollection.addField(playersLastYear);
		
		ReportableField playersAnnualChangePercent = new ReportableField(FIELD_NAME_PLAYERS_ANNUAL_CHANGE_PERCENT, String.class);
		playersAnnualChangePercent.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_PLAYERS_ANNUAL_CHANGE_PERCENT, "Player Annual Change"), currentLocale);
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
			                  ((double) (mThisYear - mLastYear) /
			                  ((double) (mThisYear>0?mThisYear:(((mThisYear - mLastYear)!=0)?(mThisYear - mLastYear):1))))
			                 ) * 100.0 ;
					           //* ((((mThisYear-mLastYear)!=0 && mThisYear!=0) && (mThisYear>=mLastYear))?1.0:-1.0);
			
			
			/*new DecimalFormat("##0.#").format( 
					((($V{AllSum}!=null && $V{AllSum}.floatValue()>0)? $V{AllSum}.floatValue() : 0 )/
					 (($V{AllSumLastYear}!=null && $V{AllSumLastYear}.floatValue()>0)? $V{AllSumLastYear}.floatValue() : (($V{AllSum}!=null && $V{AllSum}.floatValue()>0)? $V{AllSum}.floatValue() : 1 )))
					* 100.0 * ( (($V{AllSum}!=null && $V{AllSumLastYear}!=null) && ($V{AllSum}.intValue()>= $V{AllSumLastYear}.intValue()) ) ? 1.0 : -1.0 ) 
			)*/
			
			int pThisYear = getWorkReportBusiness().getCountOfPlayersByWorkReport(report);
			int pLastYear = lastYearReport==null?0:getWorkReportBusiness().getCountOfPlayersByWorkReport(lastYearReport);
			double pChange = (
			                  ((double) (pThisYear - pLastYear) /
			                  ((double) (pThisYear>0?pThisYear:(((pThisYear - pLastYear)!=0)?(pThisYear - pLastYear):1))))
			                 ) * 100.0 ;
					           //* ((((pThisYear-pLastYear)!=0 && pThisYear!=0) && (pThisYear>=pLastYear))?1.0:-1.0);
			/*double pChange = (
			                  ((double) pThisYear) /
			                  ((double) (pLastYear>0?pLastYear:((pThisYear-pLastYear)!=0?(pThisYear-pLastYear):1)))
			                 ) * 100.0 
			                   * ((((pThisYear-pLastYear)!=0 && pThisYear!=0) && (pThisYear>=pLastYear))?1.0:-1.0);*/
			
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
				this._iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale),"GMT"));

		//PARAMETERS that are also FIELDS
	
		ReportableField regionalUnionName = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionName.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionName);

		// stats for club work report status
		ReportableField clubCountDone = new ReportableField(FIELD_NAME_CLUB_COUNT_DONE, Integer.class);
		clubCountDone.setLocalizedName(this._iwrb.getLocalizedString(WorkReportStatusDropDownMenu.LOCALIZED_STATUS_DONE, "Report done"), currentLocale);
		reportCollection.addField(clubCountDone);

		ReportableField clubCountNotDone = new ReportableField(FIELD_NAME_CLUB_COUNT_NOT_DONE, Integer.class);
		clubCountNotDone.setLocalizedName(this._iwrb.getLocalizedString(WorkReportStatusDropDownMenu.LOCALIZED_STATUS_NOT_DONE, "Report not done"), currentLocale);
		reportCollection.addField(clubCountNotDone);

		ReportableField clubCountSomeDone = new ReportableField(FIELD_NAME_CLUB_COUNT_SOME_DONE, Integer.class);
		clubCountSomeDone.setLocalizedName(this._iwrb.getLocalizedString(WorkReportStatusDropDownMenu.LOCALIZED_STATUS_SOME_DONE, "Report partialy done"), currentLocale);
		reportCollection.addField(clubCountSomeDone);
	
		ReportableField clubCountInactive = new ReportableField(FIELD_NAME_CLUB_COUNT_INACTIVE, Integer.class);
		clubCountInactive.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_CLUB_COUNT_INCACTIVE, "Inactive clubs"), currentLocale);
		reportCollection.addField(clubCountInactive);

		ReportableField percentReportsDone = new ReportableField(FIELD_NAME_PERCENT_REPORTS_DONE, String.class);
		percentReportsDone.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_PERCENT_REPORTS_DONE, "Percent reports done"), currentLocale);
		reportCollection.addField(percentReportsDone);

		// stats for members
		ReportableField membersThisYear = new ReportableField(FIELD_NAME_MEMBERS_THIS_YEAR, Integer.class);
		membersThisYear.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEMBERS_THIS_YEAR, "Members this year"), currentLocale);
		reportCollection.addField(membersThisYear);

		ReportableField membersLastYear = new ReportableField(FIELD_NAME_MEMBERS_LAST_YEAR, Integer.class);
		membersLastYear.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEMBERS_LAST_YEAR, "Members last year"), currentLocale);
		reportCollection.addField(membersLastYear);

		ReportableField membersAnnualChangePercent = new ReportableField(FIELD_NAME_MEMBERS_ANNUAL_CHANGE_PERCENT, String.class);
		membersAnnualChangePercent.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEMBERS_ANNUAL_CHANGE_PERCENT, "Member Annual Change"), currentLocale);
		reportCollection.addField(membersAnnualChangePercent);		

		// stats for players
		ReportableField playersThisYear = new ReportableField(FIELD_NAME_PLAYERS_THIS_YEAR, Integer.class);
		playersThisYear.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_PLAYERS_THIS_YEAR, "Players this year"), currentLocale);
		reportCollection.addField(playersThisYear);

		ReportableField playersLastYear = new ReportableField(FIELD_NAME_PLAYERS_LAST_YEAR, Integer.class);
		playersLastYear.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_PLAYERS_LAST_YEAR, "Players last year"), currentLocale);
		reportCollection.addField(playersLastYear);

		ReportableField playersAnnualChangePercent = new ReportableField(FIELD_NAME_PLAYERS_ANNUAL_CHANGE_PERCENT, String.class);
		playersAnnualChangePercent.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_PLAYERS_ANNUAL_CHANGE_PERCENT, "Player Annual Change"), currentLocale);
		reportCollection.addField(playersAnnualChangePercent);

		
		Collection reports = getWorkReportBusiness().getWorkReportsForRegionalUnionCollection(year.intValue(), regionalUnionsFilter);
		Map regionalUnionsStatsMap = new TreeMap();
		//Iterating through workreports and creating report data 
		Iterator iter = reports.iterator();
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
			double change = (done==0)?0.0:((double)done)/((double)(done+notDone))*100.0;
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
					   ((double) (now - last) /
					   ((double) (now>0?now:(((now - last)!=0)?(now - last):1))))
					 ) * 100.0 ;
					  //* ((((now-last)!=0 && now!=0) && (now>=last))?1.0:-1.0);
			
			regData.addData(membersAnnualChangePercent, format.format(change));
						
			int pThisYear = getWorkReportBusiness().getCountOfPlayersByWorkReport(report);//, mainBoardReportGroup);
			int pLastYear = lastYearReport==null?0:getWorkReportBusiness().getCountOfPlayersByWorkReport(lastYearReport);
			totalPlayersThisYear += pThisYear;
			totalPlayersLastYear += pLastYear;
			regData = addToIntegerCount(playersThisYear, regData, pThisYear);
			regData = addToIntegerCount(playersLastYear, regData, pLastYear);
			now = ((Integer)regData.getFieldValue(playersThisYear)).intValue();
			last = ((Integer)regData.getFieldValue(playersLastYear)).intValue();
			change = (
					   ((double) (now - last) /
					   ((double) (now>0?now:(((now - last)!=0)?(now - last):1))))
					 ) * 100.0 ;
					  // * ((((now-last)!=0 && now!=0) && (now>=last))?1.0:-1.0);
			
			regData.addData(playersAnnualChangePercent, format.format(change));
		}

		// iterate through the ordered map and ordered lists and add to the final collection
		reportCollection.addAll(regionalUnionsStatsMap.values());

		ReportableField[] sortFields = new ReportableField[] {regionalUnionName};
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);
		
		double change = (totalDone+totalNotDone)==0?1.0:(totalDone/(totalDone+totalNotDone));
		reportCollection.addExtraHeaderParameter(
				"percentReportsDoneAll",
				this._iwrb.getLocalizedString(LOCALIZED_PERCENT_REPORTS_DONE, "Percent reports done"),
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
				this._iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale),"GMT"));

		//PARAMETERS that are also FIELDS

		ReportableField regionalUnionName = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionName.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionName);

		// stats for members
		ReportableField membersThisYear = new ReportableField(FIELD_NAME_MEMBERS_THIS_YEAR, Integer.class);
		membersThisYear.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEMBERS_THIS_YEAR, "Members this year"), currentLocale);
		reportCollection.addField(membersThisYear);

		ReportableField membersLastYear = new ReportableField(FIELD_NAME_MEMBERS_LAST_YEAR, Integer.class);
		membersLastYear.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEMBERS_LAST_YEAR, "Members last year"), currentLocale);
		reportCollection.addField(membersLastYear);

		ReportableField membersAnnualChange = new ReportableField(FIELD_NAME_MEMBERS_ANNUAL_CHANGE, Integer.class);
		membersAnnualChange.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEMBERS_ANNUAL_CHANGE, "Member Annual Change"), currentLocale);
		reportCollection.addField(membersAnnualChange);
		
		ReportableField membersAnnualChangePercent = new ReportableField(FIELD_NAME_MEMBERS_ANNUAL_CHANGE_PERCENT, String.class);
		membersAnnualChangePercent.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEMBERS_ANNUAL_CHANGE_PERCENT, "Member Annual Percentage Change"), currentLocale);
		reportCollection.addField(membersAnnualChangePercent);
		
		ReportableField membersAnnualChangePercentOfTotal = new ReportableField(FIELD_NAME_MEMBERS_ANNUAL_CHANGE_PERCENT_OF_TOTAL, String.class);
		membersAnnualChangePercentOfTotal.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEMBERS_ANNUAL_CHANGE_PERCENT_OF_TOTAL, "Member Annual Change of Total"), currentLocale);
		reportCollection.addField(membersAnnualChangePercentOfTotal);

		// stats for players
		ReportableField playersThisYear = new ReportableField(FIELD_NAME_PLAYERS_THIS_YEAR, Integer.class);
		playersThisYear.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_PLAYERS_THIS_YEAR, "Players this year"), currentLocale);
		reportCollection.addField(playersThisYear);

		ReportableField playersLastYear = new ReportableField(FIELD_NAME_PLAYERS_LAST_YEAR, Integer.class);
		playersLastYear.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_PLAYERS_LAST_YEAR, "Players last year"), currentLocale);
		reportCollection.addField(playersLastYear);

		ReportableField playersAnnualChange = new ReportableField(FIELD_NAME_PLAYERS_ANNUAL_CHANGE, Integer.class);
		playersAnnualChange.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_PLAYERS_ANNUAL_CHANGE, "Players Annual Change"), currentLocale);
		reportCollection.addField(playersAnnualChange);
		
		ReportableField playersAnnualChangePercent = new ReportableField(FIELD_NAME_PLAYERS_ANNUAL_CHANGE_PERCENT, String.class);
		playersAnnualChangePercent.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_PLAYERS_ANNUAL_CHANGE_PERCENT, "Players Annual Percentage Change"), currentLocale);
		reportCollection.addField(playersAnnualChangePercent);
		
		ReportableField playersAnnualChangePercentOfTotal = new ReportableField(FIELD_NAME_PLAYERS_ANNUAL_CHANGE_PERCENT_OF_TOTAL, String.class);
		playersAnnualChangePercentOfTotal.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_PLAYERS_ANNUAL_CHANGE_PERCENT_OF_TOTAL, "Players Annual Change of Total"), currentLocale);
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
			
					
			int pThisYear = getWorkReportBusiness().getCountOfPlayersByWorkReport(report);
			int pLastYear = lastYearReport==null?0:getWorkReportBusiness().getCountOfPlayersByWorkReport(lastYearReport);
			pThisYearTotal += pThisYear;
			pLastYearTotal += pLastYear;
			regData = addToIntegerCount(playersThisYear, regData, pThisYear);
			regData = addToIntegerCount(playersLastYear, regData, pLastYear);
		}

		Collection regDataCollection = regionalUnionsStatsMap.values();
		// iterate through the ordered map and ordered lists and add to the final collection
		reportCollection.addAll(regDataCollection);
		
		// get the percentage from total and create last row
		Iterator rData = regDataCollection.iterator();
		DecimalFormat format = new DecimalFormat("##0.#");
		double macptTotal = 0;
		double pacptTotal = 0;
		while(rData.hasNext()) {
			ReportableData rd = (ReportableData) rData.next();
			
			// memebers percentage stats
			int mNow = ((Integer)rd.getFieldValue(membersThisYear)).intValue();
			int mLast = ((Integer)rd.getFieldValue(membersLastYear)).intValue();
			int mMissing = mLast - mNow;
			rd.addData(membersAnnualChange, new Integer(mMissing));
			double mChange = getChange(mNow, mLast);
			rd.addData(membersAnnualChangePercent, mChange<0.0?"":Integer.toString((int)mChange));
			double mwn = ((double)mMissing)/((double)mLastYearTotal)*100.0;
			
			String value = (mwn<0.0)?"":format.format(mwn);
			rd.addData(membersAnnualChangePercentOfTotal, value);
			
			// players percentage stats
			int pNow = ((Integer)rd.getFieldValue(playersThisYear)).intValue();
			int pLast = ((Integer)rd.getFieldValue(playersLastYear)).intValue();
			int pMissing = pLast - pNow;
			rd.addData(playersAnnualChange, new Integer(pMissing));
			double pChange = getChange(pNow, pLast);
			rd.addData(playersAnnualChangePercent, pChange<0.0?"":Integer.toString((int)pChange));
			double pwn = ((double)pMissing)/((double)pLastYearTotal)*100.0;
			
			value = (pwn<0.0)?"":format.format(pwn);
			rd.addData(playersAnnualChangePercentOfTotal, value);
			
			macptTotal += mwn;
			pacptTotal += pwn;
		}
		
		reportCollection.addExtraHeaderParameter(
				"annualMemberChangePercentOfTotalAll",
				this._iwrb.getLocalizedString(LOCALIZED_MEMBERS_ANNUAL_CHANGE_PERCENT_OF_TOTAL, "Members Annual Change of Total"),
				"annualMemberChangePercentOfTotalAll",
				format.format(macptTotal));
		
		reportCollection.addExtraHeaderParameter(
				"annualPlayerChangePercentOfTotalAll",
				this._iwrb.getLocalizedString(LOCALIZED_PLAYERS_ANNUAL_CHANGE_PERCENT_OF_TOTAL, "Players Annual Change of Total"),
				"annualPlayerChangePercentOfTotalAll",
				format.format(pacptTotal));
			
		ReportableField[] sortFields = new ReportableField[] {regionalUnionName};
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);
		
		return reportCollection;
	}
	
	private double getChange(double now, double last) {
		return last==0?(now==0?0:-1):(now/last*100.0);
	}
	
	/*
	 * Report B12.6.1 of the ISI Specs
	 */
	public ReportableCollection getAgeStatisticsMemberTypeGenderRegionalUnionsFilterAndAge (
			Integer year,
			String gender,
			Collection regionalUnionsFilter,
			Integer ageFrom,
			Integer ageTo,
			String strShowClubs) throws RemoteException {

		boolean showClubs = strShowClubs!=null && strShowClubs.equals(YesNoDropDownMenu.YES);
		
		int age1 = ageFrom==null?0:ageFrom.intValue();
		int age2 = ageTo==null?123:ageTo.intValue();
		if(age1>age2) {
			age2 =age1;
		}
		
		boolean filterByAge = !(age1==0 && age2==123);
	
		//initialize stuff
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();

		//PARAMETES

		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				this._iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale),"GMT"));

		//PARAMETERS that are also FIELDS

		ReportableField regionalUnionName = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionName.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionName);
		
		ReportableField orderField = new ReportableField(FIELD_NAME_ORDERING, String.class);
		orderField.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ORDERING, "Ordering"), currentLocale);
		reportCollection.addField(orderField);

		ReportableField clubName = new ReportableField(FIELD_NAME_CLUB_NAME, String.class);
		clubName.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_CLUB_NAME, "Club name"), currentLocale);
		reportCollection.addField(clubName);
		
		ReportableField memberCount = new ReportableField(FIELD_NAME_MEMBERS, Integer.class);
		memberCount.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEMBERS, "members"), currentLocale);
		reportCollection.addField(memberCount);
		
		ReportableField memberCountTot = new ReportableField(FIELD_NAME_MEMBERS + "_tot", Integer.class);
		memberCountTot.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEMBERS, "members"), currentLocale);
		reportCollection.addField(memberCountTot);

		ReportableField playerCount = new ReportableField(FIELD_NAME_PLAYERS, Integer.class);
		playerCount.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_PLAYERS, "players"), currentLocale);
		reportCollection.addField(playerCount);
		
		ReportableField playerCountTot = new ReportableField(FIELD_NAME_PLAYERS + "_tot", Integer.class);
		playerCountTot.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_PLAYERS, "players"), currentLocale);
		reportCollection.addField(playerCountTot);
		
		Collection clubs = getWorkReportBusiness().getWorkReportsForRegionalUnionCollection(year.intValue(), regionalUnionsFilter);
		Map regionalUnionsStatsMap = new TreeMap();
		//Iterating through workreports and creating report data 
		Iterator iter = clubs.iterator();
		while (iter.hasNext()) {
			//the club
			WorkReport report = (WorkReport) iter.next();
			
			//String cName = report.getGroupName();
			String regionalUnionIdentifier = getRegionalUnionIdentifier(report);
			String cName = report.getGroupName();

			//fetch the stats or initialize for this regional union (i.e. the one associated with regionalUnionIdentifier)
			//Map clubMap = (Map) regionalUnionsClubMap.get(regionalUnionIdentifier);
			ReportableData regData = (ReportableData) regionalUnionsStatsMap.get(regionalUnionIdentifier);
			if(regData == null) {
				regData = new ReportableData();
				regionalUnionsStatsMap.put(regionalUnionIdentifier, regData);
				regData.addData(regionalUnionName, regionalUnionIdentifier);
				regData.addData(orderField, "b");
				regData.addData(clubName, this._iwrb.getLocalizedString(LOCALIZED_TOTAL, "TOTAL"));
				regData.addData(memberCount, new Integer(0));
				regData.addData(playerCount, new Integer(0));
				regData.addData(memberCountTot, new Integer(0));
				regData.addData(playerCountTot, new Integer(0));
				reportCollection.add(regData);
			}

			int members;
			int players;
			if(filterByAge) {
				if(gender==null) {
					members = getWorkReportBusiness().getCountOfMembersEqualOrOlderThanAgeAndByWorkReport(age1, report) -
					          getWorkReportBusiness().getCountOfMembersEqualOrOlderThanAgeAndByWorkReport(age2 + 1, report);
					players = getWorkReportBusiness().getCountOfPlayersEqualOrOlderThanAgeAndByWorkReport(age1, report) -
					          getWorkReportBusiness().getCountOfPlayersEqualOrOlderThanAgeAndByWorkReport(age2 + 1, report);
				} else if(gender.equals("m")) {
					members = getWorkReportBusiness().getCountOfMaleMembersEqualOrOlderThanAgeAndByWorkReport(age1, report) -
					          getWorkReportBusiness().getCountOfMaleMembersEqualOrOlderThanAgeAndByWorkReport(age2 + 1, report);
					players = getWorkReportBusiness().getCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReport(age1, report) -
					          getWorkReportBusiness().getCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReport(age2 + 1, report);
				} else {
					members = getWorkReportBusiness().getCountOfFemaleMembersEqualOrOlderThanAgeAndByWorkReport(age1, report) -
					          getWorkReportBusiness().getCountOfFemaleMembersEqualOrOlderThanAgeAndByWorkReport(age2 + 1, report);
					players = getWorkReportBusiness().getCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReport(age1, report) -
					          getWorkReportBusiness().getCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReport(age2 + 1, report);
				}
			} else {
				if (gender==null) {
					members = getWorkReportBusiness().getCountOfMembersByWorkReport(report);
					players = getWorkReportBusiness().getCountOfPlayersByWorkReport(report);
				} else if(gender.equals("m")) {
					members = getWorkReportBusiness().getCountOfMaleMembersByWorkReport(report);
					players = getWorkReportBusiness().getCountOfMalePlayersByWorkReport(report);
				} else {
					members = getWorkReportBusiness().getCountOfFemaleMembersByWorkReport(report);
					players = getWorkReportBusiness().getCountOfFemalePlayersByWorkReport(report);
				}
			}
			regData = addToIntegerCount(memberCount, regData, members);
			regData = addToIntegerCount(playerCount, regData, players);
			regData = addToIntegerCount(memberCountTot, regData, members);
			regData = addToIntegerCount(playerCountTot, regData, players);
			
			if(showClubs) {
				ReportableData rdClub = new ReportableData();
				rdClub.addData(regionalUnionName, regionalUnionIdentifier);
				rdClub.addData(orderField, "a");
				rdClub.addData(clubName, cName);
				rdClub.addData(memberCount, new Integer(members));
				rdClub.addData(playerCount, new Integer(players));
				rdClub.addData(memberCountTot, new Integer(0));
				rdClub.addData(playerCountTot, new Integer(0));
				reportCollection.add(rdClub);
			}
		}

		ReportableField[] sortFields = new ReportableField[] {regionalUnionName, orderField, clubName};
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);
		
		//finished return the collection
		return reportCollection;
	}
	
	/*
	 * Report B12.6.2 of the ISI Specs
	 */
	public ReportableCollection getAgeStatisticsMemberTypeGenderLeaguesFilterAndAge (
			Integer year,
			String gender,
			Collection leaguesFilter,
			Integer ageFrom,
			Integer ageTo, 
			String strShowClubs) throws RemoteException {

		boolean showClubs = strShowClubs!=null && strShowClubs.equals(YesNoDropDownMenu.YES);
		
		int age1 = ageFrom==null?0:ageFrom.intValue();
		int age2 = ageTo==null?123:ageTo.intValue();
		if(age1>age2) {
			age2 =age1;
		}
		
		boolean filterByAge = !(age1==0 && age2==123);

		//initialize stuff
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();

		//PARAMETES

		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				this._iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale),"GMT"));

		//PARAMETERS that are also FIELDS

		ReportableField leagueString = new ReportableField(FIELD_NAME_LEAGUE_NAME, String.class);
		leagueString.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_LEAGUE_INFO, "League"), currentLocale);
		reportCollection.addField(leagueString);
		
		ReportableField orderField = new ReportableField(FIELD_NAME_ORDERING, String.class);
		orderField.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ORDERING, "Ordering"), currentLocale);
		reportCollection.addField(orderField);

		ReportableField clubName = new ReportableField(FIELD_NAME_CLUB_NAME, String.class);
		clubName.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_CLUB_NAME, "Club name"), currentLocale);
		reportCollection.addField(clubName);

		ReportableField memberCount = new ReportableField(FIELD_NAME_MEMBERS, Integer.class);
		memberCount.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEMBERS, "members"), currentLocale);
		reportCollection.addField(memberCount);

		ReportableField memberCountTot = new ReportableField(FIELD_NAME_MEMBERS + "_tot", Integer.class);
		memberCountTot.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEMBERS, "members"), currentLocale);
		reportCollection.addField(memberCountTot);
		
		ReportableField playerCount = new ReportableField(FIELD_NAME_PLAYERS, Integer.class);
		playerCount.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_PLAYERS, "players"), currentLocale);
		reportCollection.addField(playerCount);
		
		ReportableField playerCountTot = new ReportableField(FIELD_NAME_PLAYERS + "_tot", Integer.class);
		playerCountTot.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_PLAYERS, "players"), currentLocale);
		reportCollection.addField(playerCountTot);

		Collection clubs = getWorkReportBusiness().getWorkReportsForRegionalUnionCollection(year.intValue(), null);
		Map leagueStatsMap = new TreeMap();
		List leagueGroupIdList = getGroupIdListFromLeagueGroupCollection(year,leaguesFilter,false);
		//Iterating through workreports and creating report data
		Iterator iter = clubs.iterator();
		while (iter.hasNext()) {
			//the club
			WorkReport report = (WorkReport) iter.next();
			String cName = report.getGroupName();
			
			Collection leagues;
			try {
				leagues = report.getLeagues();
			} catch (IDOException e) {
				System.out.println("Exception getting leagues for club " + cName);
				e.printStackTrace();
				continue;
			}
			Iterator iterator = leagues.iterator();
			while (iterator.hasNext()) {
				WorkReportGroup league = (WorkReportGroup) iterator.next();
				Integer leagueKey = league.getGroupId();//for comparison this must be the same key both years
				
				if (!leagueGroupIdList.contains(league.getGroupId())) {
					continue; //don't process this one, go to next
				}
				
				String leagueIdentifier = getLeagueIdentifier(league);
				//fetch the stats or initialize
				ReportableData leagueStatsData = (ReportableData) leagueStatsMap.get(leagueKey);
				if(leagueStatsData==null) {
					leagueStatsData = new ReportableData();
					leagueStatsMap.put(leagueKey, leagueStatsData);
					leagueStatsData.addData(leagueString, leagueIdentifier);
					leagueStatsData.addData(orderField, "b");
					leagueStatsData.addData(clubName, this._iwrb.getLocalizedString(LOCALIZED_TOTAL, "TOTAL"));
					leagueStatsData.addData(memberCount, new Integer(0));
					leagueStatsData.addData(playerCount, new Integer(0));
					leagueStatsData.addData(memberCountTot, new Integer(0));
					leagueStatsData.addData(playerCountTot, new Integer(0));
					reportCollection.add(leagueStatsData);
				}
				
				int members;
				int players;
				if(filterByAge) {
					if(gender==null) {
						members = getWorkReportBusiness().getCountOfMembersEqualOrOlderThanAgeAndByWorkReport(age1, report) -
						          getWorkReportBusiness().getCountOfMembersEqualOrOlderThanAgeAndByWorkReport(age2 + 1, report);
						players = getWorkReportBusiness().getCountOfPlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age1, report, league) -
						          getWorkReportBusiness().getCountOfPlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age2 + 1, report, league);
					} else if(gender.equals("m")) {
						members = getWorkReportBusiness().getCountOfMaleMembersEqualOrOlderThanAgeAndByWorkReport(age1, report) -
						          getWorkReportBusiness().getCountOfMaleMembersEqualOrOlderThanAgeAndByWorkReport(age2 + 1, report);
						players = getWorkReportBusiness().getCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age1, report, league) -
						          getWorkReportBusiness().getCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age2 + 1, report, league);
					} else {
						members = getWorkReportBusiness().getCountOfFemaleMembersEqualOrOlderThanAgeAndByWorkReport(age1, report) -
						          getWorkReportBusiness().getCountOfFemaleMembersEqualOrOlderThanAgeAndByWorkReport(age2 + 1, report);
						players = getWorkReportBusiness().getCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age1, report, league) -
						          getWorkReportBusiness().getCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age2 + 1, report, league);
					}
				} else {
					if (gender==null) {
						members = getWorkReportBusiness().getCountOfMembersByWorkReport(report);
						players = getWorkReportBusiness().getCountOfPlayersByWorkReportAndWorkReportGroup(report, league);
					} else if(gender.equals("m")) {
						members = getWorkReportBusiness().getCountOfMaleMembersByWorkReport(report);
						players = getWorkReportBusiness().getCountOfMalePlayersByWorkReportAndWorkReportGroup(report, league);
					} else {
						members = getWorkReportBusiness().getCountOfFemaleMembersByWorkReport(report);
						players = getWorkReportBusiness().getCountOfFemalePlayersByWorkReportAndWorkReportGroup(report, league);
					}
				}
				leagueStatsData = addToIntegerCount(memberCount, leagueStatsData, members);
				leagueStatsData = addToIntegerCount(playerCount, leagueStatsData, players);
				leagueStatsData = addToIntegerCount(memberCountTot, leagueStatsData, members);
				leagueStatsData = addToIntegerCount(playerCountTot, leagueStatsData, players);
				if(showClubs) {
					ReportableData rdClubPlayers = new ReportableData();
					rdClubPlayers.addData(leagueString, leagueIdentifier);
					rdClubPlayers.addData(orderField, "a");
					rdClubPlayers.addData(clubName, cName);
					rdClubPlayers.addData(memberCount, new Integer(members));
					rdClubPlayers.addData(playerCount, new Integer(players));
					rdClubPlayers.addData(memberCountTot, new Integer(0));
					rdClubPlayers.addData(playerCountTot, new Integer(0));
					reportCollection.add(rdClubPlayers);
				}
			}
		}

		ReportableField[] sortFields = new ReportableField[] {leagueString, orderField, clubName};
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);
		
		//finished return the collection
		return reportCollection;
	}
	
	/*
	 * Report B12.6.3 of the ISI Specs
	 */
	public ReportableCollection getDivisionTypeStatsByYearAndRegionalUnionsFilter (
	Collection  years,
	Collection regionalUnionsFilter) throws RemoteException {

		//initialize stuff
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();

		//PARAMETES

		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				this._iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale),"GMT"));

		//PARAMETERS that are also FIELDS
		
		ReportableField reportYear = new ReportableField(FIELD_NAME_YEAR, String.class);
		reportYear.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_YEAR, "Year"), currentLocale);
		reportCollection.addField(reportYear);
		
		ReportableField regionalUnionName = new ReportableField(FIELD_NAME_REGIONAL_UNION_NAME, String.class);
		regionalUnionName.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_REGIONAL_UNION_NAME, "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionName);

		ReportableField singleDivisionMembers = new ReportableField(FIELD_NAME_MEMBERS_SINGLE_DIVISION, String.class);
		singleDivisionMembers.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEMBERS_SINGLE_DIVISION, "Single Division Members"), currentLocale);
		reportCollection.addField(singleDivisionMembers);
		
		ReportableField multiDivisionMembers = new ReportableField(FIELD_NAME_MEMBERS_MULTI_DIVISION, String.class);
		multiDivisionMembers.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEMBERS_MULTI_DIVISION, "Multi Division Members"), currentLocale);
		reportCollection.addField(multiDivisionMembers);
		
		ReportableField singleDivisionPlayers = new ReportableField(FIELD_NAME_PLAYERS_SINGLE_DIVISION, String.class);
		singleDivisionPlayers.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_PLAYERS_SINGLE_DIVISION, "Single Division Players"), currentLocale);
		reportCollection.addField(singleDivisionPlayers);
		
		ReportableField multiDivisionPlayers = new ReportableField(FIELD_NAME_PLAYERS_MULTI_DIVISION, String.class);
		multiDivisionPlayers.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_PLAYERS_MULTI_DIVISION, "Multi Division Players"), currentLocale);
		reportCollection.addField(multiDivisionPlayers);
		
		Iterator yearIter = (years==null?Collections.EMPTY_LIST:years).iterator();
		while(yearIter.hasNext()) {
			String year = (String) yearIter.next();
			Collection clubs = getWorkReportBusiness().getWorkReportsForRegionalUnionCollection(Integer.parseInt(year), regionalUnionsFilter);
			Map regionalUnionsStatsMap = new TreeMap();
			
			//Iterating through workreports and creating report data 
			Iterator iter = clubs.iterator();
			while (iter.hasNext()) {
				//the club
				WorkReport report = (WorkReport) iter.next();
				
				//String cName = report.getGroupName();
				String regionalUnionIdentifier = getRegionalUnionIdentifier(report);
				
				ReportableData regData = (ReportableData) regionalUnionsStatsMap.get(regionalUnionIdentifier);
				if(regData==null) {
					regData = new ReportableData();
					regionalUnionsStatsMap.put(regionalUnionIdentifier, regData);
					regData.addData(reportYear, year);
					regData.addData(regionalUnionName, regionalUnionIdentifier);
					regData.addData(singleDivisionMembers, new Integer(0));
					regData.addData(multiDivisionMembers, new Integer(0));
					regData.addData(singleDivisionPlayers, new Integer(0));
					regData.addData(multiDivisionPlayers, new Integer(0));
					reportCollection.add(regData);
				}
				
				String cType = report.getType();
				
				int members = getWorkReportBusiness().getCountOfMembersByWorkReport(report);
				int players = getWorkReportBusiness().getCountOfPlayersByWorkReport(report);
				
				if(IWMemberConstants.META_DATA_CLUB_STATUS_MULTI_DIVISION_CLUB.equals(cType)) {
					regData = addToIntegerCount(multiDivisionMembers, regData, members);
					regData = addToIntegerCount(multiDivisionPlayers, regData, players);
				} else if(IWMemberConstants.META_DATA_CLUB_STATUS_SINGLE_DIVISION_CLUB.equals(cType)) {
					regData = addToIntegerCount(singleDivisionMembers, regData, members);
					regData = addToIntegerCount(singleDivisionPlayers, regData, players);
				}
			}
		}
		
		ReportableField[] sortFields = new ReportableField[] {reportYear, regionalUnionName};
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);
		
		//finished return the collection
		return reportCollection;
	}
	
	/*
	 * Report B12.6.4 of the ISI Specs
	 */
	public ReportableCollection getDivisionTypeStatsByYearAndLeaguesFilter (
	Collection years,	
	Collection leaguesFilter) throws RemoteException {

		//initialize stuff
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();

		//PARAMETES

		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				this._iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale),"GMT"));

		//PARAMETERS that are also FIELDS
		
		ReportableField reportYear = new ReportableField(FIELD_NAME_YEAR, String.class);
		reportYear.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_YEAR, "Year"), currentLocale);
		reportCollection.addField(reportYear);
		
		ReportableField leagueString = new ReportableField(FIELD_NAME_LEAGUE_NAME, String.class);
		leagueString.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_LEAGUE_INFO, "League"), currentLocale);
		reportCollection.addField(leagueString);

		ReportableField singleDivisionMembers = new ReportableField(FIELD_NAME_MEMBERS_SINGLE_DIVISION, String.class);
		singleDivisionMembers.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEMBERS_SINGLE_DIVISION, "Single Division Members"), currentLocale);
		reportCollection.addField(singleDivisionMembers);
		
		ReportableField multiDivisionMembers = new ReportableField(FIELD_NAME_MEMBERS_MULTI_DIVISION, String.class);
		multiDivisionMembers.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_MEMBERS_MULTI_DIVISION, "Multi Division Members"), currentLocale);
		reportCollection.addField(multiDivisionMembers);
		
		ReportableField singleDivisionPlayers = new ReportableField(FIELD_NAME_PLAYERS_SINGLE_DIVISION, String.class);
		singleDivisionPlayers.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_PLAYERS_SINGLE_DIVISION, "Single Division Players"), currentLocale);
		reportCollection.addField(singleDivisionPlayers);
		
		ReportableField multiDivisionPlayers = new ReportableField(FIELD_NAME_PLAYERS_MULTI_DIVISION, String.class);
		multiDivisionPlayers.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_PLAYERS_MULTI_DIVISION, "Multi Division Players"), currentLocale);
		reportCollection.addField(multiDivisionPlayers);
		
		Iterator yearIter = (years==null?Collections.EMPTY_LIST:years).iterator();
		while(yearIter.hasNext()) {
			String year = (String) yearIter.next();
			
			Collection clubs = getWorkReportBusiness().getWorkReportsForRegionalUnionCollection(Integer.parseInt(year), null);
			Map leagueStatsMap = new TreeMap();
			List leagueGroupIdList = getGroupIdListFromLeagueGroupCollection(new Integer(year),leaguesFilter,false);
			//Iterating through workreports and creating report data 
			Iterator iter = clubs.iterator();
			while (iter.hasNext()) {
				//the club
				WorkReport report = (WorkReport) iter.next();
				String cName = report.getGroupName();
				
				Collection leagues;
				try {
					leagues = report.getLeagues();
				} catch (IDOException e) {
					System.out.println("Exception getting leagues for club " + cName);
					e.printStackTrace();
					continue;
				}
				Iterator iterator = leagues.iterator();
				while (iterator.hasNext()) {
					WorkReportGroup league = (WorkReportGroup) iterator.next();
					Integer leagueKey = league.getGroupId();//for comparison this must be the same key both years
					if (!leagueGroupIdList.contains(league.getGroupId())) {
						continue; //don't process this one, go to next
					}
					
					String leagueIdentifier = getLeagueIdentifier(league);
					//fetch the stats or initialize
					ReportableData regData = (ReportableData) leagueStatsMap.get(leagueKey);
					if(regData==null) {
						regData = new ReportableData();
						leagueStatsMap.put(leagueKey, regData);
						regData.addData(reportYear, year);
						regData.addData(leagueString, leagueIdentifier);
						regData.addData(singleDivisionMembers, new Integer(0));
						regData.addData(multiDivisionMembers, new Integer(0));
						regData.addData(singleDivisionPlayers, new Integer(0));
						regData.addData(multiDivisionPlayers, new Integer(0));
						reportCollection.add(regData);
					}
					
					String cType = report.getType();
					
					int members = getWorkReportBusiness().getCountOfMembersByWorkReport(report);
					int players = getWorkReportBusiness().getCountOfPlayersByWorkReportAndWorkReportGroup(report, league);
					
					if(IWMemberConstants.META_DATA_CLUB_STATUS_MULTI_DIVISION_CLUB.equals(cType)) {
						regData = addToIntegerCount(multiDivisionMembers, regData, members);
						regData = addToIntegerCount(multiDivisionPlayers, regData, players);
					} else if(IWMemberConstants.META_DATA_CLUB_STATUS_SINGLE_DIVISION_CLUB.equals(cType)) {
						regData = addToIntegerCount(singleDivisionMembers, regData, members);
						regData = addToIntegerCount(singleDivisionPlayers, regData, players);
					}
				}
			}
		}
		
		ReportableField[] sortFields = new ReportableField[] {reportYear, leagueString};
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);
		
		//finished return the collection
		return reportCollection;
	}

	/*
	 * Report B12.7.1 of the ISI Specs
	 */
	public ReportableCollection sevenDotOne (
	Integer year,
	Collection leagueFilter,
	Collection regionalUnionFilter,
	Collection clubFilter,
	String playersOrMembers,
	String gender,
	Integer birthYear,
	Collection postalCodes,
	String order) throws RemoteException {

		//initialize stuff
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();

		//PARAMETES

		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				this._iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale),"GMT"));

		//PARAMETERS that are also FIELDS
		
		ReportableField ssn = new ReportableField(FIELD_NAME_SSN, String.class);
		ssn.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_SSN, "ssn"), currentLocale);
		reportCollection.addField(ssn);
		
		ReportableField personName = new ReportableField(FIELD_NAME_PERSON_NAME, String.class);
		personName.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_PERSON_NAME, "Name"), currentLocale);
		reportCollection.addField(personName);

		ReportableField phone = new ReportableField(FIELD_NAME_PHONE, String.class);
		phone.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_PHONE, "Phone"), currentLocale);
		reportCollection.addField(phone);
		
		ReportableField address = new ReportableField(FIELD_NAME_ADDRESS, String.class);
		address.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ADDRESS, "Address"), currentLocale);
		reportCollection.addField(address);
		
		ReportableField postalCode = new ReportableField(FIELD_NAME_POSTALCODE, String.class);
		postalCode.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_POSTALCODE, "Postal Code"), currentLocale);
		reportCollection.addField(postalCode);
		
		ReportableField email = new ReportableField(FIELD_NAME_EMAIL, String.class);
		email.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_EMAIL, "Email"), currentLocale);
		reportCollection.addField(email);

		Collection members = getWorkReportBusiness().getWorkReportsByYearRegionalUnionsAndClubs(year.intValue(), regionalUnionFilter, clubFilter);
		List leagueGroupIDList = getGroupIdListFromLeagueGroupCollection(year, leagueFilter, false);

		Iterator iter = members.iterator();
		HashSet userSet = new HashSet();
		while (iter.hasNext()) {
			WorkReport report = (WorkReport) iter.next();
			try {
				Collection leagues = report.getLeagues();
				Iterator iterator = leagues.iterator();
				while (iterator.hasNext()) {
					WorkReportGroup league = (WorkReportGroup) iterator.next();
					
					if (!leagueGroupIDList.contains(league.getGroupId()) ) {
						continue; //don't process this one, go to next
					}
					
					try {
						Collection users = null;
						if (playersOrMembers.equals(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER)) {
							users = getWorkReportBusiness().getWorkReportMemberHome().findAllWorkReportMembersByWorkReportIdAndWorkReportGroup(((Integer) report.getPrimaryKey()).intValue(), league);
						}
						else if (playersOrMembers.equals(IWMemberConstants.GROUP_TYPE_CLUB_MEMBER)) {
							users = getWorkReportBusiness().getWorkReportMemberHome().findAllWorkReportMembersByWorkReportIdOrderedByMemberName(((Integer) report.getPrimaryKey()).intValue());
						}
						
						if (users != null) {
							Iterator itor = users.iterator();
							while (itor.hasNext()) {
								WorkReportMember element = (WorkReportMember) itor.next();
								if(userSet.contains(element.getPrimaryKey())) {
									continue;
								}
								userSet.add(element.getPrimaryKey());
								
								if (birthYear != null && birthYear.intValue() > 0) {
									Timestamp dateOfBirth = element.getDateOfBirth();
									IWTimestamp birthThingie = new IWTimestamp(dateOfBirth);
									if (birthYear.intValue() != birthThingie.getYear()) {
										continue;
									}
								}
								if (gender != null) {
									if (gender.equalsIgnoreCase("m") && element.isFemale()) {
										continue;
									}
									else if (gender.equalsIgnoreCase("f") && element.isMale()) {
										continue;
									}
								}
								
								PostalCode code = null;
								try {
									code = element.getPostalCode();
								}
								catch (SQLException e1) {
									code = null;
								}
								
								if (postalCodes != null && !postalCodes.isEmpty()) {
									if (code != null) {
										if (!postalCodes.contains(code.getPostalCode())) {
											continue;
										}
									}
									else {
										continue;
									}
								}
								
								ReportableData regData = new ReportableData();
								regData.addData(ssn, element.getPersonalId());
								regData.addData(personName, element.getName());
								regData.addData(phone, element.getHomePhone());
								regData.addData(address, element.getStreetName());
								if (code != null) {
									regData.addData(postalCode, code.getPostalAddress());
								}
								regData.addData(email, element.getEmail());
								reportCollection.add(regData);
							}
						}
					}
					catch (FinderException fe) {
						fe.printStackTrace();
					}
				}
			}
			catch (IDOException e) {
				e.printStackTrace();
			}
		}
		
		ReportableField[] sortFields = null;
		if (order.equals(IWMemberConstants.ORDER_BY_NAME)) {
			sortFields = new ReportableField[] {personName, address, postalCode};
		}
		else if (order.equals(IWMemberConstants.ORDER_BY_ADDRESS)) {
			sortFields = new ReportableField[] {address, postalCode, personName};
		}
		else if (order.equals(IWMemberConstants.ORDER_BY_POSTAL_CODE)) {
			sortFields = new ReportableField[] {postalCode, address, personName};
		}
		
		if (sortFields != null) {
			Comparator comparator = new FieldsComparator(sortFields);
			Collections.sort(reportCollection, comparator);
		}
		
		//finished return the collection
		return reportCollection;
	}
	
	/*
	 * Report B12.7.2 of the ISI Specs
	 */
	public ReportableCollection sevenDotTwo (
	Integer year,
	Collection leagueFilter,
	Collection regionalUnionFilter,
	Collection clubFilter,
	String playersOrMembers,
	String gender,
	Integer birthYear,
	Collection postalCodes,
	String order) throws RemoteException {

		//initialize stuff
		initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();

		//PARAMETES

		//Add extra...because the inputhandlers supply the basic header texts
		reportCollection.addExtraHeaderParameter(
				"workreportreport",
				this._iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale),"GMT"));

		//PARAMETERS that are also FIELDS
		
		ReportableField personName = new ReportableField(FIELD_NAME_PERSON_NAME, String.class);
		personName.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_PERSON_NAME, "Name"), currentLocale);
		reportCollection.addField(personName);
		
		ReportableField address = new ReportableField(FIELD_NAME_ADDRESS, String.class);
		address.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_ADDRESS, "Address"), currentLocale);
		reportCollection.addField(address);
		
		ReportableField postalCode = new ReportableField(FIELD_NAME_POSTALCODE, String.class);
		postalCode.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_POSTALCODE, "Postal Code"), currentLocale);
		reportCollection.addField(postalCode);
		
		ReportableField email = new ReportableField(FIELD_NAME_EMAIL, String.class);
		email.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_EMAIL, "Email"), currentLocale);
		reportCollection.addField(email);
		
		Collection members = getWorkReportBusiness().getWorkReportsByYearRegionalUnionsAndClubs(year.intValue(), regionalUnionFilter, clubFilter);
		List leagueGroupIDList = getGroupIdListFromLeagueGroupCollection(year, leagueFilter, false);

		Iterator iter = members.iterator();
		HashSet userSet = new HashSet();
		while (iter.hasNext()) {
			WorkReport report = (WorkReport) iter.next();
			try {
				Collection leagues = report.getLeagues();
				Iterator iterator = leagues.iterator();
				while (iterator.hasNext()) {
					WorkReportGroup league = (WorkReportGroup) iterator.next();
					
					if (!leagueGroupIDList.contains(league.getGroupId()) ) {
						continue; //don't process this one, go to next
					}
					
					try {
						Collection users = null;
						if (playersOrMembers.equals(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER)) {
							users = getWorkReportBusiness().getWorkReportMemberHome().findAllWorkReportMembersByWorkReportIdAndWorkReportGroup(((Integer) report.getPrimaryKey()).intValue(), league);
						}
						else if (playersOrMembers.equals(IWMemberConstants.GROUP_TYPE_CLUB_MEMBER)) {
							users = getWorkReportBusiness().getWorkReportMemberHome().findAllWorkReportMembersByWorkReportIdOrderedByMemberName(((Integer) report.getPrimaryKey()).intValue());
						}
						
						if (users != null) {
							Iterator itor = users.iterator();
							
							while (itor.hasNext()) {
								WorkReportMember element = (WorkReportMember) itor.next();
								if(userSet.contains(element.getPrimaryKey())) {
									continue;
								}
								userSet.add(element.getPrimaryKey());
								if (birthYear != null && birthYear.intValue() > 0) {
									
									Timestamp dateOfBirth = element.getDateOfBirth();
									if (dateOfBirth == null) {
										continue;
									}
									IWTimestamp birthThingie = new IWTimestamp(dateOfBirth);
									if (birthYear.intValue() != birthThingie.getYear()) {
										continue;
									}
								}
								if (gender != null) {
									if (gender.equalsIgnoreCase("m") && element.isFemale()) {
										continue;
									}
									else if (gender.equalsIgnoreCase("f") && element.isMale()) {
										continue;
									}
								}
								
								PostalCode code = null;
								try {
									code = element.getPostalCode();
								}
								catch (SQLException e1) {
									code = null;
								}
								
								if (postalCodes != null && !postalCodes.isEmpty()) {
									if (code != null) {
										if (!postalCodes.contains(code.getPostalCode())) {
											continue;
										}
									}
									else {
										continue;
									}
								}
								
								ReportableData regData = new ReportableData();
								regData.addData(personName, element.getName());
								regData.addData(address, element.getStreetName());
								if (code != null) {
									regData.addData(postalCode, code.getPostalAddress());
								}
								regData.addData(email, element.getEmail());
								reportCollection.add(regData);
							}
						}
					}
					catch (FinderException fe) {
						fe.printStackTrace();
					}
				}
			}
			catch (IDOException e) {
				e.printStackTrace();
			}
		}
		
		ReportableField[] sortFields = null;
		if (order.equals(IWMemberConstants.ORDER_BY_NAME)) {
			sortFields = new ReportableField[] {personName, address, postalCode};
		}
		else if (order.equals(IWMemberConstants.ORDER_BY_ADDRESS)) {
			sortFields = new ReportableField[] {address, postalCode, personName};
		}
		else if (order.equals(IWMemberConstants.ORDER_BY_POSTAL_CODE)) {
			sortFields = new ReportableField[] {postalCode, address, personName};
		}
		
		if (sortFields != null) {
			Comparator comparator = new FieldsComparator(sortFields);
			Collections.sort(reportCollection, comparator);
		}
		
		//finished return the collection
		return reportCollection;
	}
	
	private boolean showClubStatus(WorkReport report, String status) {
		String reportStatus = report.getStatus();
		boolean show = true;
		if(status!=null && status.trim().length()>0 && !status.equals(WorkReportStatusDropDownMenu.STATUS_ALL)) {
			show = false;
			if(WorkReportStatusDropDownMenu.STATUS_DONE.equals(status)) {
				show = WorkReportConstants.WR_STATUS_DONE.equals(reportStatus);
			} else if(WorkReportStatusDropDownMenu.STATUS_NO_REPORT.equals(status)) {
				show = WorkReportConstants.WR_STATUS_NO_REPORT.equals(reportStatus);
			} else if(WorkReportStatusDropDownMenu.STATUS_NOT_DONE.equals(status)) {
				show = WorkReportConstants.WR_STATUS_NOT_DONE.equals(reportStatus);
			} else if(WorkReportStatusDropDownMenu.STATUS_SOME_DONE.equals(status)) {
				show = WorkReportConstants.WR_STATUS_SOME_DONE.equals(reportStatus);
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
		returnType = this._iwrb.getLocalizedString(LOCALIZED_CLUB_TYPE_MULTI_DIVISION, "Multi Division");
	} else if(IWMemberConstants.META_DATA_CLUB_STATUS_SINGLE_DIVISION_CLUB.equals(cType)) {
		returnType = this._iwrb.getLocalizedString(LOCALIZED_CLUB_TYPE_SINGLE_DIVISION, "Single Division");
	} else if(IWMemberConstants.META_DATA_CLUB_STATUS_NO_MEMBERS_CLUB.equals(cType)) {
		returnType = this._iwrb.getLocalizedString(LOCALIZED_CLUB_TYPE_NO_MEMBERS, "No Members");
	} else if(report.isInUMFI()) {
		returnType = this._iwrb.getLocalizedString(LOCALIZED_CLUB_TYPE_IM_UMFI, "UMFI member");
	} else if(report.isInActive()) {
		returnType = this._iwrb.getLocalizedString(LOCALIZED_CLUB_TYPE_IS_INACTIVE, "Inactive");
	} else {
		returnType = "";
	}
	return returnType;
} 


private ReportableData addToIntegerCount(ReportableField reportableField, ReportableData reportableData, int intToAdd) {
	if(reportableData!=null){//update count
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

private ReportableData addToLongCount(ReportableField reportableField, ReportableData reportableData, long longToAdd) {
	if(reportableData!=null){//update count
		Long count = (Long)reportableData.getFieldValue(reportableField);
		if(count!=null) {
			count = new Long(count.longValue()+longToAdd);
		} else {
			if(longToAdd>0){
				count = new Long(longToAdd);
			}
			else{
				count = new Long(0);
			}
		}
		reportableData.addData(reportableField,count);//swap
	}
	
	return reportableData;
}

private ReportableData addToLongCountFromFieldInAnotherReportableData(ReportableField reportableField, ReportableData reportableData, ReportableData dataToGetCountFrom) {
	if(reportableData!=null && dataToGetCountFrom!=null){//update count
		Long count = (Long)dataToGetCountFrom.getFieldValue(reportableField);
		Long oldCount = (Long)reportableData.getFieldValue(reportableField);
		if(count!=null && oldCount!=null) {
			count = new Long(oldCount.longValue()+count.longValue());
		} 
		
		reportableData.addData(reportableField,count);//swap
	}
	
	return reportableData;
}

private ReportableData addToLongCountFromFieldInAnotherReportableData(ReportableField reportableFieldFrom,ReportableField reportableFieldTo, ReportableData reportableData, ReportableData dataToGetCountFrom) {
	if(reportableData!=null && dataToGetCountFrom!=null){//update count
		Long count = (Long)dataToGetCountFrom.getFieldValue(reportableFieldFrom);
		Long oldCount = (Long)reportableData.getFieldValue(reportableFieldTo);
		if(count!=null && oldCount!=null) {
			count = new Long(oldCount.longValue()+count.longValue());
		} 
		
		reportableData.addData(reportableFieldTo,count);//swap
	}
	
	return reportableData;
}

private String getLeagueIdentifier(WorkReportGroup league) {
	StringBuffer leagueBuf = new StringBuffer();
	String number = league.getNumber();
	if(number==null || "".equals(number)) {
		number = "";
	} else {
		number += " ";
	}
	String shortName = league.getShortName();
	if(shortName==null) {
		shortName = "";
	}
	leagueBuf.append(number).append(shortName);
	
	String leagueText=leagueBuf.toString();
	
	if("".equals(leagueText)){
		String name = league.getName();
		if(name==null) {
			name = "Nafn ekki sett";
		}
		leagueText = name;
	}
	return leagueText;
}

private String getRegionalUnionIdentifier(WorkReport report) {
	StringBuffer ruBuf = new StringBuffer();
	ruBuf.append( ( ( (report.getRegionalUnionNumber()!=null) && (!"".equals(report.getRegionalUnionNumber()))) )? report.getRegionalUnionNumber()+" " : "" )
	.append( ( ( (report.getRegionalUnionAbbreviation()!=null) && (!"".equals(report.getRegionalUnionAbbreviation()))) )? report.getRegionalUnionAbbreviation() : "");
	String regText = ruBuf.toString();
	if("".equals(regText)){
		regText = (report.getRegionalUnionName()!=null)? report.getRegionalUnionName() : this._iwrb.getLocalizedString(LOCALIZED_NO_REGIONAL_UNION_NAME, "No Reg.Un. name");
	}
	return regText;
}

}
