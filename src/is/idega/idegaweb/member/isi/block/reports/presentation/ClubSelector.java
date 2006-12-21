package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusiness;
import is.idega.idegaweb.member.isi.block.reports.business.WorkReportImportBusiness;
import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;

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
import com.idega.presentation.PresentationObject;
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
	int stepCounter = 0;
	protected WorkReportBusiness reportBiz;
	protected WorkReportImportBusiness reportImportBiz;
	protected IWResourceBundle iwrb;
	
	protected List paramsToMaintain = null;
	protected List steps = null;
	protected Map localizedStepTexts = new HashMap(); 
	protected Map localizedStepKeyOrder = new HashMap(); 
	protected Table stepTable = null;

	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	protected static final String STEP_NAME_LOCALIZATION_KEY = "clubselector.step_name";
	
	/**
	 * @return
	 */
	public List getParametersToMaintain() {
		return this.paramsToMaintain;
	}

	/**
	 * @param paramsToMaintain
	 */
	public void setParametersToMaintain(List paramsToMaintain) {
		this.paramsToMaintain = paramsToMaintain;
	}
	
	public void addToParametersToMaintainList(String param){
		if(this.paramsToMaintain==null) {
			this.paramsToMaintain = new ArrayList();
		}
		
		this.paramsToMaintain.add(param);
		
	}

	public ClubSelector() {

		super();
		this.setToDebugParameters(true);
		addToParametersToMaintainList(WorkReportConstants.WR_SESSION_PARAM_CLUB_ID);
		addToParametersToMaintainList(WorkReportConstants.WR_SESSION_PARAM_REGIONAL_UNION_ID);
		addToParametersToMaintainList(WorkReportWindow.ACTION);
		setStepNameLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
	}

	/**
	 * @return The group id of the selected club.
	 */
	public int getClubId() {
		return this.clubId;
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
		return this.regionalUnionId;
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
		this.reportBiz = getWorkReportBusiness(iwc);
		this.iwrb = getResourceBundle(iwc);
		
		//add breadcrumbs
		addStepsTable(iwc);
		addBreakLine();
		
		
		
		//sets this step as bold, if another class calls it this will be overridden
		setAsCurrentStepByStepLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
		
		if (!initializeClubSelectionForm(iwc)) {	
		    addClubSelectionForm(iwc);
		}
	}
    
    // this method ruins the layout of all editors, therefore this method is overwritten by the editors
    protected void addBreakLine() {
      addBreak();      
    }  
  
	
	protected void addStepsTable(IWContext iwc){
		if(this.steps!=null && !this.steps.isEmpty()){
			this.stepTable = new Table();
			this.stepTable.setWidth(Table.HUNDRED_PERCENT);
			this.stepTable.setColor("#dfdfdf");//setHorizontalZebraColored("#dfdfdf","#efefef");   
			this.stepTable.setCellspacing(0);
			
			Iterator iter = this.steps.iterator();
			int column = 1;
			
			while (iter.hasNext()) {
				String key = (String) iter.next();
				Text text = new Text(column+". "+this.iwrb.getLocalizedString(key,key));
				
				this.localizedStepKeyOrder.put(key,new Integer(column));//for later lookup
				
				this.localizedStepTexts.put(key,text);
				this.stepTable.add(text,column,1);		
				this.stepTable.setWidth(column,200);
				column++;
			}
			
			add(this.stepTable);
		}
		
		
	}

	protected void addClubSelectionForm(IWContext iwc) throws RemoteException {
		
		Form clubSelectorForm = new Form();
		
		clubSelectorForm.maintainParameters(getParametersToMaintain());
		
		Table table = new Table(2,4);
		table.mergeCells(1,1,2,1);
		table.mergeCells(1,4,2,4);
		table.setAlignment(1,4,Table.HORIZONTAL_ALIGN_RIGHT);
		
		addContentToTable(table, iwc);
		
		SubmitButton submit = new SubmitButton(this.iwrb.getLocalizedString("clubselector.continue","continue"));
		submit.setAsImageButton(true);
		
		table.add(submit,1,4);
		
		clubSelectorForm.add(table);
	
		add(clubSelectorForm);
	}

	protected WorkReportBusiness getWorkReportBusiness(IWApplicationContext iwc) {
		if (this.reportBiz == null) {
			try {
				this.reportBiz = (WorkReportBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, WorkReportBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return this.reportBiz;
	}
	
	protected WorkReportImportBusiness getWorkReportImportBusiness(IWApplicationContext iwc) {
		if (this.reportImportBiz == null) {
			try {
				this.reportImportBiz = (WorkReportImportBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, WorkReportImportBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return this.reportImportBiz;
	}
		
	public String getBundleIdentifier(){
		return IW_BUNDLE_IDENTIFIER;
	}
	
	protected void setStepNameLocalizableKey(String stepInWizardNameLocalizedKey){
		if(this.steps==null){
			this.steps = new Vector();//to keep the order
		}

		
		this.steps.add(this.stepCounter++,stepInWizardNameLocalizedKey);
	}
	
	protected void addToStepsExtraInfo(String stepLocalizableKey, PresentationObject obj){
		Integer column = (Integer)this.localizedStepKeyOrder.get(stepLocalizableKey);
		this.stepTable.add(obj,column.intValue(),2);
		this.stepTable.setColor(column.intValue(),2,"#efefef");
		this.stepTable.setColor(column.intValue()+1,2,"#efefef");
		this.stepTable.setColor(column.intValue()+2,2,"#efefef");
		this.stepTable.setColor(column.intValue()+3,2,"#efefef");
	}
	
	protected List getSteps(){
		return this.steps;
	}
	

	protected void setAsCurrentStepByStepLocalizableKey(String key){
		Iterator iter = this.localizedStepTexts.keySet().iterator();
		while (iter.hasNext()) {
			String localizedKey = (String) iter.next();
			Text text = (Text) this.localizedStepTexts.get(localizedKey);
			if(localizedKey.equals(key)){
				text.setBold();
			}
			else{
				text.setBold(false);
			}
		}
	}
	
	
	/**
	 * This methods first checks for the parameter in session, if that is null then it checks the request.
	 * If the WorkReportConstants.WR_SESSION_CLEAR is set only params from requests are fetched. 
	 * @param iwc IWContext
	 * @param attributeName the name of the parameter
	 * @return
	 */
	protected String getParameterFromSessionOrRequest(IWContext iwc, String attributeName){
		String clear = iwc.getParameter(WorkReportConstants.WR_SESSION_CLEAR);
		if(clear!=null){
			iwc.setSessionAttribute(WorkReportConstants.WR_SESSION_CLEAR,"TRUE");
		}
		
		String temp = null;
		
		if(! (iwc.getSessionAttribute(WorkReportConstants.WR_SESSION_CLEAR) !=null)) {
			temp = (String) iwc.getSessionAttribute(attributeName);
		} 
		if( temp==null ){
			temp = iwc.getParameter(attributeName);
		}
		
		return temp;
	}
	
	protected boolean initializeClubSelectionForm(IWContext iwc) throws RemoteException, FinderException {
	    boolean parametersInitialized = false;
	    String paramRegionalUnionId = getParameterFromSessionOrRequest(iwc,WorkReportConstants.WR_SESSION_PARAM_REGIONAL_UNION_ID);
		String paramClubId = getParameterFromSessionOrRequest(iwc,WorkReportConstants.WR_SESSION_PARAM_CLUB_ID);
		
		if( paramClubId!=null || getClubId()!=-1 ){
			if( this.clubId == -1 ) {
				this.clubId = Integer.parseInt(paramClubId);
			}
			iwc.setSessionAttribute(WorkReportConstants.WR_SESSION_PARAM_CLUB_ID,paramClubId);
			
			if( paramRegionalUnionId != null && !paramRegionalUnionId.equals("") ){
				this.regionalUnionId = Integer.parseInt(paramRegionalUnionId);
				iwc.setSessionAttribute(WorkReportConstants.WR_SESSION_PARAM_REGIONAL_UNION_ID,paramRegionalUnionId);
			}
			
			if(this.regionalUnionId!=-1 && this.clubId!=this.regionalUnionId){
				Text regionalUnionText = new Text(this.getWorkReportBusiness(iwc).getGroupBusiness().getGroupByGroupID(this.regionalUnionId).getName(),true,true,false);
				addToStepsExtraInfo(STEP_NAME_LOCALIZATION_KEY,regionalUnionText);
				//add(" / ");
			}

			if(this.clubId!=-1 && this.regionalUnionId!=-1 && this.clubId!=this.regionalUnionId){
				addToStepsExtraInfo(STEP_NAME_LOCALIZATION_KEY,new Text(" / "));
			}

			if(this.clubId!=-1 && this.clubId!=this.regionalUnionId){
				Text clubText = new Text(this.getWorkReportBusiness(iwc).getGroupBusiness().getGroupByGroupID(this.clubId).getName(),true,true,false);
				addToStepsExtraInfo(STEP_NAME_LOCALIZATION_KEY,clubText);
			}
			parametersInitialized = true;
		}
		else{
			if(paramRegionalUnionId!=null){
				this.regionalUnionId = Integer.parseInt(paramRegionalUnionId);
				iwc.setSessionAttribute(WorkReportConstants.WR_SESSION_PARAM_REGIONAL_UNION_ID,paramRegionalUnionId);
				if(this.regionalUnionId!=-1 && this.clubId!=this.regionalUnionId){
					Text regionalUnionText = new Text(this.getWorkReportBusiness(iwc).getGroupBusiness().getGroupByGroupID(this.regionalUnionId).getName(),true,true,false);
					addToStepsExtraInfo(STEP_NAME_LOCALIZATION_KEY,regionalUnionText);
				}
				
			}
			parametersInitialized = false;
		}
	    return parametersInitialized;
	}
	
	protected void addContentToTable(Table table, IWContext iwc) throws RemoteException {	
		
	    DropdownMenu clubMenu = null;
		DropdownMenu regMenu = null;
		
		Collection clubs = null;
		Collection regionalUnions = null;
		if( getRegionalUnionId() !=-1 ){
			try {
				regionalUnions = new ArrayList();
				regionalUnions.add(this.reportBiz.getGroupBusiness().getGroupByGroupID(getRegionalUnionId()));
				regMenu = new DropdownMenu(regionalUnions,WorkReportConstants.WR_SESSION_PARAM_REGIONAL_UNION_ID);
				//regMenu.setDisabled(true);
				clubs = this.reportBiz.getClubGroupsForRegionUnionGroup(this.reportBiz.getGroupBusiness().getGroupByGroupID(getRegionalUnionId()));
				clubMenu = new DropdownMenu(clubs,WorkReportConstants.WR_SESSION_PARAM_CLUB_ID);
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
		}else{
			regionalUnions = this.reportBiz.getAllRegionalUnionGroups();
			regMenu = new DropdownMenu(regionalUnions,WorkReportConstants.WR_SESSION_PARAM_REGIONAL_UNION_ID);
		}
		
		table.add(this.iwrb.getLocalizedString("clubselector.select_regional_union_and_club","Select the desired group."),1,1);
		table.add(this.iwrb.getLocalizedString("clubselector.regional_union","Regional union"),1,2);
		table.add(regMenu,2,2);		

		if( clubMenu!=null ){
			table.add(this.iwrb.getLocalizedString("clubselector.club","Club"),1,3);
			table.add(clubMenu,2,3);
		}
	}
	
	
}
