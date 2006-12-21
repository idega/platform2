package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.util.text.TextSoap;


/**
 * Description: A generic block that forces the user to select a group to work with. Once the group is selected it maintance it's id in memory.<br>
 * If you extend it, you must call this objects main(iwc) method before your own and if you are using a form in your block you should do a <br>
 * yourForm.maintainParameters(getParamsToMaintain()) and use this.addToParamsToMaintain(string param) in your constructor if you need subclasses of your own class to work correctly.<br>
 * To display your "step" of a wizard created extending this class you should call the method setStepNameLocalizedKey(String stepInWizardNameLocalizedKey) with a localizable key in your constructor.<br>
 * Example: 1. Group 2. "Do my stuff" (gotten from iwrb.getLocalizedString(stepInWizardNameLocalizedKey,stepInWizardNameLocalizedKey) ).<br>
 * To highlight that your step is happening call setAsCurrentStepByStepLocalizableKey(STEP_NAME_LOCALIZATION_KEY) with your key in the main method.
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:sigtryggur@idega.is">Sigtryggur S�monarson</a>
 */
public class WorkReportGroupSelector extends WorkReportSelector {
	
	protected int groupId = -1;
		
	public WorkReportGroupSelector() {
		super();
	}

	public int getGroupId() {
		return this.groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	
	public int getClubId() {
		return this.groupId;
	}

	public void setClubId(int groupId) {
		this.groupId = groupId;
	}
	
	public void main(IWContext iwc) throws Exception { 
	    super.main(iwc);
	}

	protected boolean initializeClubSelectionForm(IWContext iwc) throws RemoteException, FinderException {
		boolean clubSelectionFormInitialized = false;
		iwc.setSessionAttribute(WorkReportConstants.WR_SESSION_PARAM_REGIONAL_UNION_ID, null);
		String paramGroupId = getParameterFromSessionOrRequest(iwc,WorkReportConstants.WR_SESSION_PARAM_CLUB_ID);
		if (paramGroupId != null || getGroupId()!=-1 ){
			if( this.groupId == -1 ) {
			    this.groupId = Integer.parseInt(paramGroupId);
			}
			iwc.setSessionAttribute(WorkReportConstants.WR_SESSION_PARAM_CLUB_ID,paramGroupId);
			Text groupText = new Text(this.getWorkReportBusiness(iwc).getGroupBusiness().getGroupByGroupID(this.groupId).getName(),true,true,false);
			addToStepsExtraInfo(ClubSelector.STEP_NAME_LOCALIZATION_KEY,groupText);
			clubSelectionFormInitialized = true;
		}
		return clubSelectionFormInitialized;
	}
	
	protected void addContentToTable(Table table, IWContext iwc) throws RemoteException {
	    DropdownMenu groupMenu = null;
		Collection groups = null;
		
		String paramGroupType = null;
		if( getGroupId() ==-1 ){
			paramGroupType = getParameterFromSessionOrRequest(iwc,WorkReportConstants.WR_SESSION_PARAM_GROUP_TYPE);
			groups = this.reportBiz.getAllGroupsByGroupType(paramGroupType);
			groupMenu = new DropdownMenu(groups,WorkReportConstants.WR_SESSION_PARAM_CLUB_ID);
		}
		
		String groupTypeString = this.iwrb.getLocalizedString(paramGroupType,paramGroupType);
		
		String modifiedGroupTypeString = paramGroupType.toLowerCase();
		if (modifiedGroupTypeString.substring(0,5).equals("iwme_")) {
		    modifiedGroupTypeString = modifiedGroupTypeString.substring(5,modifiedGroupTypeString.length());
		}
		modifiedGroupTypeString = TextSoap.findAndReplace(modifiedGroupTypeString,'_',' ');
		
		table.add(this.iwrb.getLocalizedString("workreportgroupselector.select_"+paramGroupType,"Select the desired " + modifiedGroupTypeString),1,1);

		if( groupMenu!=null ){
			table.add(groupTypeString,1,3);
			table.add(groupMenu,2,3);
		} 
	}
}