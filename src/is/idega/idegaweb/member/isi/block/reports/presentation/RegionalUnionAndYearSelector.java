/*
 * Created on Jul 4, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusiness;
import is.idega.idegaweb.member.isi.block.reports.business.WorkReportImportBusiness;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;

/**
 * @author palli
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class RegionalUnionAndYearSelector extends Block {
	protected int regionalUnionId = -1;
	protected int year = -1;

	protected WorkReportBusiness reportBiz;
	protected WorkReportImportBusiness reportImportBiz;
	protected IWResourceBundle iwrb;
	
	protected List paramsToMaintain = null;
	protected List steps = null;
	protected Map localizedStepTexts = new HashMap(); 
		
	protected static final String PARAM_REGION_UNION_ID = "iwme_reg_sel_ru_id";	
	protected static final String PARAM_REGION_UNION_YEAR = "iwme_reg_sel_year";

	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	private static final String STEP_NAME_LOCALIZATION_KEY = "regselector.step_name";
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

	public RegionalUnionAndYearSelector() {
		super();
		this.setToDebugParameters(true);
		addToParametersToMaintainList(PARAM_REGION_UNION_ID);
		addToParametersToMaintainList(PARAM_REGION_UNION_YEAR);
		addToParametersToMaintainList(WorkReportWindow.ACTION);
		setStepNameLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
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

	/**
	 * @return the year of the report
	 */
	public int getYear() {
		return this.year;
	}

	/**
	 * @param year
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		this.reportBiz = getWorkReportBusiness(iwc);
		this.iwrb = getResourceBundle(iwc);
		//add breadcrumbs
		addStepsTable(iwc);
		addBreak();
		
		//sets this step as bold, if another class calls it this will be overridden
		setAsCurrentStepByStepLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
		
			if(iwc.isParameterSet(PARAM_REGION_UNION_ID)){
				this.regionalUnionId = Integer.parseInt(iwc.getParameter(PARAM_REGION_UNION_ID));
			}
		if( iwc.isParameterSet(PARAM_REGION_UNION_YEAR) ){
			this.year = Integer.parseInt(iwc.getParameter(PARAM_REGION_UNION_YEAR));
		}
			
						
			addRegionalSelectionForm();
	}
	
	protected void addStepsTable(IWContext iwc){
		if(this.steps!=null && !this.steps.isEmpty()){
			Table stepTable = new Table();
			
			Iterator iter = this.steps.iterator();
			int column = 1;
			
			while (iter.hasNext()) {
				String key = (String) iter.next();
				Text text = new Text(column+". "+this.iwrb.getLocalizedString(key,key));
				this.localizedStepTexts.put(key,text);
				stepTable.add(text,column++,1);		
			}
			
			add(stepTable);
		}	
	}

	protected void addRegionalSelectionForm() throws RemoteException {
		DropdownMenu regMenu = null;		
		Collection regionalUnions = null;
		
		Form regionalSelectorForm = new Form();
		
		regionalSelectorForm.maintainParameters(getParametersToMaintain());
		
		DropdownMenu dateSelector = new DropdownMenu(PARAM_REGION_UNION_YEAR);
		IWTimestamp stamp = IWTimestamp.RightNow();
		
		int currentYear = stamp.getYear();
		int beginningYear = 2004;
		
		for (int i = beginningYear; i <= currentYear; i++) {
			dateSelector.addMenuElement(i,Integer.toString(i));
		}
		
		if(this.year>0){
			dateSelector.setSelectedElement(this.year);
		}
		
		Table table = new Table(2,4);
		table.mergeCells(1,1,2,1);
		table.mergeCells(1,4,2,4);
		table.setAlignment(1,4,Table.HORIZONTAL_ALIGN_RIGHT);
		
			regionalUnions = this.reportBiz.getAllRegionalUnionGroups();
			regMenu = new DropdownMenu(regionalUnions,PARAM_REGION_UNION_ID);
			
			if(this.regionalUnionId>0){
				regMenu.setSelectedElement(this.regionalUnionId);
			}
		
		table.add(this.iwrb.getLocalizedString("regselector.select_regional_union","Select the desired union."),1,1);
		table.add(this.iwrb.getLocalizedString("regselector.regional_union","Regional union"),1,2);
		table.add(regMenu,2,2);		

		table.add(this.iwrb.getLocalizedString("regselector.year","Year"),1,3);
		table.add(dateSelector,2,3);		
		
		SubmitButton submit = new SubmitButton(this.iwrb.getLocalizedString("regselector.continue","continue"));
		submit.setAsImageButton(true);
		
		table.add(submit,1,4);
		
		regionalSelectorForm.add(table);
	
		add(regionalSelectorForm);
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
	
	public String getBundleIdentifier(){
		return RegionalUnionAndYearSelector.IW_BUNDLE_IDENTIFIER;
	}
	
	protected void setStepNameLocalizableKey(String stepInWizardNameLocalizedKey){
		if(this.steps==null){
			this.steps = new Vector();//to keep the order
		}
		
		this.steps.add(stepInWizardNameLocalizedKey);
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
}