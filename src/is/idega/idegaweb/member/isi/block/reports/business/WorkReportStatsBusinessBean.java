package is.idega.idegaweb.member.isi.block.reports.business;

import is.idega.idegaweb.member.isi.block.reports.data.WorkReport;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

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
	 * Report B12.1 of the ISI Specs
	 */
	public ReportableCollection getStatisticsForLeaguesByYearRegionalUnionsClubsAndLeaguesFiltering(
		final Integer year,
		Collection regionalUnionsFilter,
		Collection clubsFilter,
		Collection leaguesFilter)
		throws RemoteException {

		//initialize stuff
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
		womenUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.womenUnderAgeLimit", "kvk -15"), currentLocale);
		reportCollection.addField(womenUnderAgeLimit);

		ReportableField womenOverOrEqualAgeLimit = new ReportableField("womenOverOrEqualAgeLimit", Integer.class);
		womenOverOrEqualAgeLimit.setLocalizedName(
			_iwrb.getLocalizedString("WorkReportStatsBusiness.womenOverOrEqualAgeLimit", "kvk 16+"),
			currentLocale);
		reportCollection.addField(womenOverOrEqualAgeLimit);

		ReportableField menUnderAgeLimit = new ReportableField("menUnderAgeLimit", Integer.class);
		menUnderAgeLimit.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.menUnderAgeLimit", "kk -15"), currentLocale);
		reportCollection.addField(menUnderAgeLimit);

		ReportableField menOverOrEqualAgeLimit = new ReportableField("menOverOrEqualAgeLimit", Integer.class);
		menOverOrEqualAgeLimit.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.menOverOrEqualAgeLimit", "kk 16+"), currentLocale);
		reportCollection.addField(menOverOrEqualAgeLimit);

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

		Map workReportsByLeagues = new HashMap();
		//Iterating through workreports and creating report data 
		Iterator iter = clubs.iterator();
		while (iter.hasNext()) {
			WorkReport report = (WorkReport) iter.next();
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
					data.addData(regionalUnionAbbreviation, (regUniAbbr != null) ? regUniAbbr : report.getRegionalUnionGroupId().toString());
//					get the stats
					int playerCount = getWorkReportBusiness().getCountOfPlayersOfPlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(16, report, league);
								  
					data.addData(womenUnderAgeLimit, new Integer(5));
					data.addData(womenOverOrEqualAgeLimit, new Integer(playerCount));
					data.addData(menUnderAgeLimit, new Integer(0));
					data.addData(menOverOrEqualAgeLimit, new Integer(playerCount));
					//for the page separations
					data.addData(leagueString, league.getPrimaryKey());
					//TODO order by the number
					//data.addData(leagueString, league.getNumber());

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
			ReportableData data = (ReportableData) statsDataIter.next();
			//			don't forget to add the row to the collection
			reportCollection.add(data);
		}

		//finished return the collection
		return reportCollection;
	}

}
