
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
 * Title:		WorkReportStatsBusinessBean
 * Description: The business bean for generating statistical report on the workreport data.
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		<br><a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a><br>
 * @version		1.0
 */
public class WorkReportStatsBusinessBean extends IBOSessionBean implements WorkReportStatsBusiness {
	
	private WorkReportBusiness workBiz = null;
	private MemberFamilyLogic _familyLogic = null;
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
	
	private WorkReportBusiness getWorkReportBusiness() throws RemoteException{
		if(workBiz==null){
			workBiz = (WorkReportBusiness)IBOLookup.getServiceInstance(this.getIWApplicationContext(),WorkReportBusiness.class);
		}
		
		return workBiz;
	}
	
	private void initializeBundlesIfNeeded(){
		if(_iwb==null){
			_iwb = this.getIWApplicationContext().getApplication().getBundle(IW_BUNDLE_IDENTIFIER);
		}
		_iwrb = _iwb.getResourceBundle(this.getUserContext().getCurrentLocale());
	}
	
	
	public ReportableCollection getAllMembersForWorkReportId(Integer reportId) throws RemoteException{
	
		initializeBundlesIfNeeded();
		
		WorkReport report = this.getWorkReportBusiness().getWorkReportById(reportId.intValue());
		Collection members = this.getWorkReportBusiness().getAllWorkReportMembersForWorkReportId(reportId.intValue());
		
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		
		ReportableCollection reportData = new ReportableCollection();
		
		
		
		//initializing fields
		IDOEntityDefinition reportDef = IDOLookup.getEntityDefinitionForClass(WorkReport.class);
		
		IDOEntityDefinition memberDef = IDOLookup.getEntityDefinitionForClass(WorkReportMember.class);
		
		
		//Child - Fields
		ReportableField personalId = new ReportableField(memberDef.findFieldByUniqueName("PERSONAL_ID"));
		personalId.setCustomMadeFieldName("member_ssn");
		personalId.setLocalizedName( _iwrb.getLocalizedString("WorkReportStatsBusiness.member_ssn","Personal ID"),currentLocale);
		reportData.addField(personalId);
		
		ReportableField name = new ReportableField(memberDef.findFieldByUniqueName("NAME"));
		name.setCustomMadeFieldName("child_last_name");
		name.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.name","Name"),currentLocale);
		reportData.addField(name);

		
		//reason
			ReportableField reasonField = new ReportableField("reason",String.class);
			reasonField.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.reason","Reason"),currentLocale);
			reportData.addField(reasonField);
		
			//date of action
			ReportableField actionDateField = new ReportableField("actionDate",String.class);
			actionDateField.setLocalizedName(_iwrb.getLocalizedString("WorkReportStatsBusiness.reg_date","Registration date"),currentLocale);
			reportData.addField(actionDateField);
	


		//Creating report data and adding to collection 
		Iterator iter = members.iterator();
		while (iter.hasNext()) {
			WorkReportMember member = (WorkReportMember)iter.next();
			ReportableData data = new ReportableData();
			
			
			//member data
			data.addData(personalId,member.getPersonalId());
			data.addData(name,name.getName());
			reportData.add(data);
			
		
				
			}
			
	
		reportData.addExtraHeaderParameter("workreportreport",_iwrb.getLocalizedString("WorkReportStatsBusiness.label","Current date"),"label",IWTimestamp.getTimestampRightNow().toGMTString());
		
		return reportData;
	}
	
	
	
	

	
	
	
	
	



	

}
