package is.idega.idegaweb.member.isi.block.reports.business;

import is.idega.idegaweb.member.business.MemberFamilyLogic;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReport;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import com.idega.block.datareport.util.ReportableCollection;
import com.idega.block.datareport.util.ReportableData;
import com.idega.block.datareport.util.ReportableField;
import com.idega.business.IBOLookup;
import com.idega.business.IBOSessionBean;
import com.idega.data.IDOEntityDefinition;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.util.IWTimestamp;

/**
 * Title: WorkReportStatsBusinessBean Description: The business bean for
 * generating statistical report on the workreport data. Copyright: Copyright
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
		womenUnderAgeLimit.setLocalizedName(
			_iwrb.getLocalizedString("WorkReportStatsBusiness.womenUnderAgeLimit", "kvk -15"),
			currentLocale);
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
		menOverOrEqualAgeLimit.setLocalizedName(
			_iwrb.getLocalizedString("WorkReportStatsBusiness.menOverOrEqualAgeLimit", "kk 16+"),
			currentLocale);
		reportData.addField(menOverOrEqualAgeLimit);
		
		//A way to set the value for the parameter for good.
		ReportableField comparingYearStat = new ReportableField("comparing_year", Integer.class){
			
		   public String getLocalizedName(Locale locale) {
			   return Integer.toString(year.intValue()-1);
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
				(report.getRegionalUnionAbbreviation() != null) ? report.getRegionalUnionAbbreviation() : report.getRegionalUnionGroupId().toString());
			data.addData(leagueString, "league stuff");
			data.addData(womenUnderAgeLimit,new Integer(10));
			data.addData(womenOverOrEqualAgeLimit,new Integer(5));
			data.addData(menUnderAgeLimit,new Integer(3));
			data.addData(menOverOrEqualAgeLimit,new Integer(2));
			
			
			reportData.add(data);

		}

		reportData.addExtraHeaderParameter(
			"workreportreport",
			_iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
			"label",
			IWTimestamp.getTimestampRightNow().toGMTString());
		
		/*reportData.addExtraHeaderParameter(
					"report_year",
					_iwrb.getLocalizedString("WorkReportStatsBusiness.year", "Year"),
					"report_year_label",
					year.toString());
		
		*/
		return reportData;
	}

	/*
	 * Report B12.1 of the ISI Specs
	 */
	public ReportableCollection getStatisticsForLeaguesByYearRegionalUnionsClubsAndLeaguesFiltering(final Integer year, Collection regionalUnionsFilter, Collection clubsFilter, Collection leaguesFilter) throws RemoteException {

		initializeBundlesIfNeeded();
		ReportableCollection reportData = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();

		//Gather data
		Collection clubs = getWorkReportBusiness().getWorkReportsByYearRegionalUnionsClubsAndLeaguesFiltering(year.intValue(), regionalUnionsFilter, clubsFilter, leaguesFilter);


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
		womenUnderAgeLimit.setLocalizedName(
			_iwrb.getLocalizedString("WorkReportStatsBusiness.womenUnderAgeLimit", "kvk -15"),
			currentLocale);
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
		menOverOrEqualAgeLimit.setLocalizedName(
			_iwrb.getLocalizedString("WorkReportStatsBusiness.menOverOrEqualAgeLimit", "kk 16+"),
			currentLocale);
		reportData.addField(menOverOrEqualAgeLimit);
		
		//A way to set the value for the parameter for good.
		ReportableField comparingYearStat = new ReportableField("comparing_year", Integer.class){
			
		   public String getLocalizedName(Locale locale) {
			   return Integer.toString(year.intValue()-1);
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
				(report.getRegionalUnionAbbreviation() != null) ? report.getRegionalUnionAbbreviation() : report.getRegionalUnionGroupId().toString());
			data.addData(leagueString, "league stuff");
			data.addData(womenUnderAgeLimit,new Integer(10));
			data.addData(womenOverOrEqualAgeLimit,new Integer(5));
			data.addData(menUnderAgeLimit,new Integer(3));
			data.addData(menOverOrEqualAgeLimit,new Integer(2));
			
			
			reportData.add(data);

		}

		reportData.addExtraHeaderParameter(
			"workreportreport",
			_iwrb.getLocalizedString("WorkReportStatsBusiness.label", "Current date"),
			"label",
			IWTimestamp.getTimestampRightNow().toGMTString());
		
		/*reportData.addExtraHeaderParameter(
					"report_year",
					_iwrb.getLocalizedString("WorkReportStatsBusiness.year", "Year"),
					"report_year_label",
					year.toString());
		
		*/
		return reportData;
	}
	
	
	
	
	
	

}
