package is.idega.idegaweb.member.isi.block.reports.business;

import is.idega.idegaweb.member.isi.block.reports.data.WorkReport;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup;

import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
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
import com.idega.business.IBOLookup;
import com.idega.business.IBOSessionBean;
import com.idega.data.IDOEntityDefinition;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
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
	
	/**
	 * just some test crap, delete one day
	 */
	public ReportableCollection getClubMemberStatisticsForRegionalUnions(final Integer year, Collection regionalUnions) throws RemoteException {
		
		initializeBundlesIfNeeded();
		ReportableCollection reportData = new ReportableCollection();
		
		Collection clubs = getWorkReportBusiness().getWorkReportsForRegionalUnionCollection(year.intValue(), regionalUnions);
		
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		
		//initializing fields
		IDOEntityDefinition reportDef = IDOLookup.getEntityDefinitionForClass(WorkReport.class);
		
		//FIELDS
		ReportableField clubName = new ReportableField(reportDef.findFieldByUniqueName("GROUP_NAME"));
		clubName.setCustomMadeFieldName("club_name");
		clubName.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.club_name", "Club name"), currentLocale);
		reportData.addField(clubName);
		
		ReportableField regionalUnionAbbreviation = new ReportableField(reportDef.findFieldByUniqueName("REG_UNI_ABBR"));
		regionalUnionAbbreviation.setCustomMadeFieldName("regional_union_name");
		regionalUnionAbbreviation.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.regional_union_name", "Reg.U."), currentLocale);
		reportData.addField(regionalUnionAbbreviation);
		
		ReportableField leagueString = new ReportableField("league_info", String.class);
		leagueString.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.league_info", "League"), currentLocale);
		reportData.addField(leagueString);
		
		ReportableField womenUnderAgeLimit = new ReportableField("womenUnderAgeLimit", Integer.class);
		womenUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.womenUnderAgeLimit", "kvk -15"), currentLocale);
		reportData.addField(womenUnderAgeLimit);
		
		ReportableField womenOverOrEqualAgeLimit = new ReportableField("womenOverOrEqualAgeLimit", Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(
				_iwrb.getLocalizedString("WorkReportStatsBusiness.womenOverOrEqualAgeLimit", "kvk 16+"),
				currentLocale);
		reportData.addField(womenOverOrEqualAgeLimit);
		
		ReportableField menUnderAgeLimit = new ReportableField("menUnderAgeLimit", Integer.class);
		menUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.menUnderAgeLimit", "kk -15"), currentLocale);
		reportData.addField(menUnderAgeLimit);
		
		ReportableField menOverOrEqualAgeLimit = new ReportableField("menOverOrEqualAgeLimit", Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.menOverOrEqualAgeLimit", "kk 16+"), currentLocale);
		reportData.addField(menOverOrEqualAgeLimit);
		
		//A way to set the value for the parameter for good.
		ReportableField comparingYearStat = new ReportableField("comparing_year", Integer.class) {
			
			public String getLocalizedName(Locale locale) {
				return Integer.toString(year.intValue() - 1);
			}
			
		};
		
		reportData.addField(comparingYearStat);
		
		//DATA
		//Creating report data and adding to collection
		Iterator iter = clubs.iterator();
		while (iter.hasNext()) {
			WorkReport report = (WorkReport) iter.next();
			
			ReportableData data = new ReportableData();
			
			//WorkReport data
			data.addData(clubName, report.getGroupName());
			data.addData(
					regionalUnionAbbreviation,
					(report.getRegionalUnionAbbreviation() != null)
					? report.getRegionalUnionAbbreviation()
					: report.getRegionalUnionGroupId().toString());
			data.addData(leagueString, "league stuff");
			data.addData(womenUnderAgeLimit, new Integer(10));
			data.addData(womenOverOrEqualAgeLimit, new Integer(5));
			data.addData(menUnderAgeLimit, new Integer(3));
			data.addData(menOverOrEqualAgeLimit, new Integer(2));
			
			
			
			reportData.add(data);
			
		}
		
		reportData.addExtraHeaderParameter(
				"workreportreport",
				_iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				IWTimestamp.getTimestampRightNow().toGMTString());
		
		/*
		 * reportData.addExtraHeaderParameter( "report_year", _iwrb.getLocalizedString("WorkReportStatsBusiness.year", "Year"), "report_year_label",
		 * year.toString());
		 *  
		 */
		return reportData;
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
				_iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
				"label",
				IWTimestamp.getTimestampRightNow().toGMTString());
		
		//PARAMETERS that are also FIELDS
		//data from entity columns, can also be defined with an entity definition, see getClubMemberStatisticsForRegionalUnions method
		//The name you give the field/parameter must not contain spaces or special characters
		ReportableField clubName = new ReportableField("club_name", String.class);
		clubName.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.club_name", "Club name"), currentLocale);
		reportCollection.addField(clubName);
		
		ReportableField regionalUnionAbbreviation = new ReportableField("regional_union_name", String.class);
		regionalUnionAbbreviation.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.regional_union_name", "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionAbbreviation);
		
		//fake columns (data gotten by business methods)
		ReportableField leagueString = new ReportableField("league_info", String.class);
		leagueString.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.league_info", "League"), currentLocale);
		reportCollection.addField(leagueString);
		
		ReportableField womenUnderAgeLimit = new ReportableField("womenUnderAgeLimit", Integer.class);
		womenUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.womenUnderAgeLimit_"+age, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAgeLimit);
		
		ReportableField womenOverOrEqualAgeLimit = new ReportableField("womenOverOrEqualAgeLimit", Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.womenOverOrEqualAgeLimit_"+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
		
		ReportableField menUnderAgeLimit = new ReportableField("menUnderAgeLimit", Integer.class);
		menUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.menUnderAgeLimit_"+age, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAgeLimit);
		
		ReportableField menOverOrEqualAgeLimit = new ReportableField("menOverOrEqualAgeLimit", Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.menOverOrEqualAgeLimit_"+age, "men "+age+"+"), currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
		
		
		ReportableField bothGendersUnderAge = new ReportableField("bothGendersUnderAge", Integer.class);
		bothGendersUnderAge.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.bothGendersUnderAge"+age, "all -"+age), currentLocale);
		reportCollection.addField(bothGendersUnderAge);
		
		ReportableField bothGendersEqualOverAge = new ReportableField("bothGendersEqualOverAge", Integer.class);
		bothGendersEqualOverAge.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.bothGendersEqualOverAge"+age, "all "+age+"+"), currentLocale);
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
		regionalUnionAbbreviation.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.regional_union_name", "Reg.U."), currentLocale);
		reportCollection.addField(regionalUnionAbbreviation);
		
		//fake columns (data gotten by business methods)
		ReportableField leagueString = new ReportableField("league_info", String.class);
		leagueString.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.league_info", "League"), currentLocale);
		reportCollection.addField(leagueString);
		
		ReportableField womenUnderAgeLimit = new ReportableField("womenUnderAgeLimit", Integer.class);
		womenUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.womenUnderAgeLimit_"+age, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAgeLimit);
		
		ReportableField womenOverOrEqualAgeLimit = new ReportableField("womenOverOrEqualAgeLimit", Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.womenOverOrEqualAgeLimit_"+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
		
		ReportableField menUnderAgeLimit = new ReportableField("menUnderAgeLimit", Integer.class);
		menUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.menUnderAgeLimit_"+age, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAgeLimit);
		
		ReportableField menOverOrEqualAgeLimit = new ReportableField("menOverOrEqualAgeLimit", Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.menOverOrEqualAgeLimit_"+age, "men "+age+"+"), currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
		
		ReportableField bothGendersUnderAge = new ReportableField("bothGendersUnderAge", Integer.class);
		bothGendersUnderAge.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.bothGendersUnderAge"+age, "all -"+age), currentLocale);
		reportCollection.addField(bothGendersUnderAge);
		
		ReportableField bothGendersEqualOverAge = new ReportableField("bothGendersEqualOverAge", Integer.class);
		bothGendersEqualOverAge.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.bothGendersEqualOverAge"+age, "all "+age+"+"), currentLocale);
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
		
		//		iterate through the ordered map and ordered lists and add to the final collection
		Iterator statsDataIter = workReportsByLeagues.keySet().iterator();
		while (statsDataIter.hasNext()) {
			
			Map regMap = (Map) workReportsByLeagues.get(statsDataIter.next());
			
			//			don't forget to add the row to the collection
			reportCollection.addAll(regMap.values());
		}
		
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
		leagueString.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.league_info", "League"), currentLocale);
		reportCollection.addField(leagueString);
		
		ReportableField womenUnderAgeLimit = new ReportableField("womenUnderAgeLimit", Integer.class);
		womenUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.womenUnderAgeLimit_"+age, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAgeLimit);
		
		ReportableField womenOverOrEqualAgeLimit = new ReportableField("womenOverOrEqualAgeLimit", Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.womenOverOrEqualAgeLimit_"+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
		
		ReportableField menUnderAgeLimit = new ReportableField("menUnderAgeLimit", Integer.class);
		menUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.menUnderAgeLimit_"+age, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAgeLimit);
		
		ReportableField menOverOrEqualAgeLimit = new ReportableField("menOverOrEqualAgeLimit", Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.menOverOrEqualAgeLimit_"+age, "men "+age+"+"), currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
		
		ReportableField bothGendersUnderAge = new ReportableField("bothGendersUnderAge", Integer.class);
		bothGendersUnderAge.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.bothGendersUnderAge"+age, "all -"+age), currentLocale);
		reportCollection.addField(bothGendersUnderAge);
		
		ReportableField bothGendersEqualOverAge = new ReportableField("bothGendersEqualOverAge", Integer.class);
		bothGendersEqualOverAge.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.bothGendersEqualOverAge"+age, "all "+age+"+"), currentLocale);
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
		leagueString.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.league_info", "League"), currentLocale);
		reportCollection.addField(leagueString);
		
		//Selected years parameters and fields
		ReportableField womenUnderAgeLimit = new ReportableField("womenUnderAgeLimit", Integer.class);
		womenUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.womenUnderAgeLimit_"+age, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAgeLimit);
		
		ReportableField womenOverOrEqualAgeLimit = new ReportableField("womenOverOrEqualAgeLimit", Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.womenOverOrEqualAgeLimit_"+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);
		
		ReportableField menUnderAgeLimit = new ReportableField("menUnderAgeLimit", Integer.class);
		menUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.menUnderAgeLimit_"+age, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAgeLimit);
		
		ReportableField menOverOrEqualAgeLimit = new ReportableField("menOverOrEqualAgeLimit", Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.menOverOrEqualAgeLimit_"+age, "men "+age+"+"), currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);
		
		ReportableField bothGendersUnderAge = new ReportableField("bothGendersUnderAge", Integer.class);
		bothGendersUnderAge.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.bothGendersUnderAge"+age, "all -"+age), currentLocale);
		reportCollection.addField(bothGendersUnderAge);
		
		ReportableField bothGendersEqualOverAge = new ReportableField("bothGendersEqualOverAge", Integer.class);
		bothGendersEqualOverAge.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.bothGendersEqualOverAge"+age, "all "+age+"+"), currentLocale);
		reportCollection.addField(bothGendersEqualOverAge);
		
		//last years parameters and fields
		ReportableField womenUnderAgeLimitLastYear = new ReportableField("womenUnderAgeLimitLastYear", Integer.class);
		womenUnderAgeLimitLastYear.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.womenUnderAgeLimit_"+age, "women -"+age), currentLocale);
		reportCollection.addField(womenUnderAgeLimitLastYear);
		
		ReportableField womenOverOrEqualAgeLimitLastYear = new ReportableField("womenOverOrEqualAgeLimitLastYear", Integer.class);
		womenOverOrEqualAgeLimitLastYear.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.womenOverOrEqualAgeLimit_"+age, "women "+age+"+"),currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimitLastYear);
		
		ReportableField menUnderAgeLimitLastYear = new ReportableField("menUnderAgeLimitLastYear", Integer.class);
		menUnderAgeLimitLastYear.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.menUnderAgeLimit_"+age, "men -"+age), currentLocale);
		reportCollection.addField(menUnderAgeLimitLastYear);
		
		ReportableField menOverOrEqualAgeLimitLastYear = new ReportableField("menOverOrEqualAgeLimitLastYear", Integer.class);
		menOverOrEqualAgeLimitLastYear.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.menOverOrEqualAgeLimit_"+age, "men "+age+"+"), currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimitLastYear);
		
		ReportableField bothGendersUnderAgeLastYear = new ReportableField("bothGendersUnderAgeLastYear", Integer.class);
		bothGendersUnderAgeLastYear.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.bothGendersUnderAge"+age, "all -"+age), currentLocale);
		reportCollection.addField(bothGendersUnderAgeLastYear);
		
		ReportableField bothGendersEqualOverAgeLastYear = new ReportableField("bothGendersEqualOverAgeLastYear", Integer.class);
		bothGendersEqualOverAgeLastYear.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.bothGendersEqualOverAge"+age, "all "+age+"+"), currentLocale);
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
	if(intToAdd>0){//update count
		Integer count = (Integer)reportableData.getFieldValue(reportableField);
		count = new Integer(count.intValue()+intToAdd);
		reportableData.addData(reportableField,count);//swap
	}
	
	return reportableData;
}

private String getLeagueIdentifier(WorkReportGroup league) {
	//for the page separations
	StringBuffer leagueBuf = new StringBuffer();
	leagueBuf.append( (league.getNumber()!=null)? league.getNumber() : "" )
	.append("  ")
	.append( (league.getShortName()!=null)? league.getShortName() : "")
	.append("  ")
	.append( (league.getName()!=null)? league.getName() : "");
	String leagueText=league.toString();
	return leagueText;
}

private String getRegionalUnionIdentifier(WorkReport report) {
	String regUniAbbr = report.getRegionalUnionAbbreviation();
	if(regUniAbbr==null){
		regUniAbbr = report.getRegionalUnionNumber();
	}
	if(regUniAbbr==null){
		regUniAbbr = report.getRegionalUnionName();
	}
	if(regUniAbbr==null){
		Integer groupId=report.getRegionalUnionGroupId();
		if(groupId!=null){
			regUniAbbr = groupId.toString();
		}
		else{
			regUniAbbr="NONAME";
		}
	}
	return regUniAbbr;
}

}
