package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusiness;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;

/**
 * Description: A generic block that forces the user to select a club to work with. Once the club is selected it maintance it's id in memory.<br>
 * If you extend it, you must call this objects main(iwc) method before your own and if you are using a form in your block you should do a <br>
 * yourForm.maintainParameters(getParamsToMaintain()) and use this.addToParamsToMaintain(string param) in your constructor if you need subclasses of your own class to work correctly.<br>
 * To display your "step" of a wizard created extending this class you should call the method setStepNameLocalizedKey(String stepInWizardNameLocalizedKey) with a localizable key in your constructor.<br>
 * Example: 1. Select regional union 2. Select club 3. "Do my stuff" (gotten from iwrb.getLocalizedString(stepInWizardNameLocalizedKey,stepInWizardNameLocalizedKey) ).<br>
 * To highlight that your step is happening call setAsCurrentStepByStepLocalizableKey(STEP_NAME_LOCALIZATION_KEY) with your key in the main method.
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class ClubSelector extends Block {
	
	protected int regionalUnionId = -1;
	protected int clubId = -1;
	protected WorkReportBusiness reportBiz;
	protected IWResourceBundle iwrb;
	
	protected List paramsToMaintain = null;
	protected List steps = null;
	protected Map localizedStepTexts = new HashMap(); 
		
	protected static final String PARAM_CLUB_ID = "iwme_club_sel_cl_id";
	protected static final String PARAM_REGION_UNION_ID = "iwme_club_sel_ru_id";	

	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	private static final String STEP_NAME_LOCALIZATION_KEY = "clubselector.step_name";
	/**
	 * @return
	 */
	public List getParametersToMaintain() {
		return paramsToMaintain;
	}

	/**
	 * @param paramsToMaintain
	 */
	public void setParametersToMaintain(List paramsToMaintain) {
		this.paramsToMaintain = paramsToMaintain;
	}
	
	public void addToParametersToMaintainList(String param){
		if(paramsToMaintain==null) paramsToMaintain = new ArrayList();
		
		paramsToMaintain.add(param);
		
	}

	public ClubSelector() {

		super();
		this.setToDebugParameters(true);
		addToParametersToMaintainList(PARAM_CLUB_ID);
		addToParametersToMaintainList(WorkReportWindow.ACTION);
		setStepNameLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
	}

	/**
	 * @return The group id of the selected club.
	 */
	public int getClubId() {
		return clubId;
	}

	/**
	 * @param clubId
	 */
	public void setClubId(int clubId) {
		this.clubId = clubId;
	}

	/**
	 * @return The group id of the selected regional union.
	 */
	public int getRegionalUnionId() {
		return regionalUnionId;
	}

	/**
	 * @param regionalUnionId
	 */
	public void setRegionalUnionId(int regionalUnionId) {
		this.regionalUnionId = regionalUnionId;
	}

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		reportBiz = getWorkReportBusiness(iwc);
		iwrb = getResourceBundle(iwc);
		//add breadcrumbs
		addStepsTable(iwc);
		addBreak();
		
		//sets this step as bold, if another class calls it this will be overridden
		setAsCurrentStepByStepLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
		
		if(iwc.isParameterSet(PARAM_CLUB_ID) || getClubId()!=-1 ){
			if( clubId == -1 ) clubId = Integer.parseInt(iwc.getParameter(PARAM_CLUB_ID));
			
			if( iwc.isParameterSet(PARAM_REGION_UNION_ID) ){
				regionalUnionId = Integer.parseInt(iwc.getParameter(PARAM_REGION_UNION_ID));
			}
		}
		else{
			if(iwc.isParameterSet(PARAM_REGION_UNION_ID)){
				regionalUnionId = Integer.parseInt(iwc.getParameter(PARAM_REGION_UNION_ID));
			}
						
			addClubSelectionForm();
		}
		
		
	}
	
	protected void addStepsTable(IWContext iwc){
		if(steps!=null && !steps.isEmpty()){
			Table stepTable = new Table();
			
			Iterator iter = steps.iterator();
			int column = 1;
			
			while (iter.hasNext()) {
				String key = (String) iter.next();
				Text text = new Text(column+". "+iwrb.getLocalizedString(key,key));
				localizedStepTexts.put(key,text);
				stepTable.add(text,column++,1);		
			}
			
			add(stepTable);
		}
		
		
	}

	protected void addClubSelectionForm() throws RemoteException {
		DropdownMenu clubMenu = null;
		DropdownMenu regMenu = null;
		
		Collection clubs = null;
		Collection regionalUnions = null;
		
		Form clubSelectorForm = new Form();
		
		clubSelectorForm.maintainParameters(getParametersToMaintain());
		
		Table table = new Table(2,4);
		table.mergeCells(1,1,2,1);
		table.mergeCells(1,4,2,4);
		table.setAlignment(1,4,Table.HORIZONTAL_ALIGN_RIGHT);
		
		if( getRegionalUnionId() !=-1 ){
			try {
				regionalUnions = new ArrayList();
				regionalUnions.add(reportBiz.getGroupBusiness().getGroupByGroupID(getRegionalUnionId()));
				regMenu = new DropdownMenu(regionalUnions,PARAM_REGION_UNION_ID);
				//regMenu.setDisabled(true);
				clubs = reportBiz.getClubGroupsForRegionUnionGroup(reportBiz.getGroupBusiness().getGroupByGroupID(getRegionalUnionId()));
				clubMenu = new DropdownMenu(clubs,PARAM_CLUB_ID);
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
		}else{
			regionalUnions = reportBiz.getAllRegionalUnionGroups();
			regMenu = new DropdownMenu(regionalUnions,PARAM_REGION_UNION_ID);
		}
		
		table.add(iwrb.getLocalizedString("clubselector.select_regional_union_and_club","Select the desired club."),1,1);
		table.add(iwrb.getLocalizedString("clubselector.regional_union","Regional union"),1,2);
		table.add(regMenu,2,2);		

		if( clubMenu!=null ){
			table.add(iwrb.getLocalizedString("clubselector.club","Club"),1,3);
			table.add(clubMenu,2,3);
		} 
		
		SubmitButton submit = new SubmitButton(iwrb.getLocalizedString("clubselector.continue","continue"));
		submit.setAsImageButton(true);
		
		table.add(submit,1,4);
		
		clubSelectorForm.add(table);
	
		add(clubSelectorForm);
	}

	protected WorkReportBusiness getWorkReportBusiness(IWApplicationContext iwc) {
		if (reportBiz == null) {
			try {
				reportBiz = (WorkReportBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, WorkReportBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return reportBiz;
	}
	
	
	
	public String getBundleIdentifier(){
		return this.IW_BUNDLE_IDENTIFIER;
	}
	
	protected void setStepNameLocalizableKey(String stepInWizardNameLocalizedKey){
		if(steps==null){
			steps = new Vector();//to keep the order
		}
		
		steps.add(stepInWizardNameLocalizedKey);
	}
	
	protected List getSteps(){
		return steps;
	}
	

	protected void setAsCurrentStepByStepLocalizableKey(String key){
		Iterator iter = localizedStepTexts.keySet().iterator();
		while (iter.hasNext()) {
			String localizedKey = (String) iter.next();
			Text text = (Text) localizedStepTexts.get(localizedKey);
			if(localizedKey.equals(key)){
				text.setBold();
			}
			else{
				text.setBold(false);
			}
		}
	}
	
	
	
	
}
