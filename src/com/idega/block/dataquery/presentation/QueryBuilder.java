/*
 * Created on May 21, 2003
 *
 * QueryBuilder is a wizard that constructs a ReportQuery from the user input.
 */
package com.idega.block.dataquery.presentation;
//import java.awt.Color;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.ejb.RemoveException;

import com.idega.block.dataquery.business.QueryConditionPart;
import com.idega.block.dataquery.business.QueryEntityPart;
import com.idega.block.dataquery.business.QueryFieldPart;
import com.idega.block.dataquery.business.QueryHelper;
import com.idega.block.dataquery.business.QueryPart;
import com.idega.block.dataquery.business.QueryService;
import com.idega.block.dataquery.business.QuerySession;
import com.idega.block.dataquery.business.QueryXMLConstants;
import com.idega.business.IBOLookup;
import com.idega.core.data.ICTreeNode;
import com.idega.core.data.IWTreeNode;
import com.idega.core.file.data.ICFile;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.SelectionDoubleBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.TreeViewer;
//import com.idega.util.IWColor;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author aron 
 * @version 1.0
 */
public class QueryBuilder extends Block {
	private static final String PARAM_ASTEMPLATE = "astemplate";
	private IWBundle iwb = null;
	private IWResourceBundle iwrb = null;
	private static final String IWBUNDLE_IDENTIFIER = "com.idega.block.dataquery";
	private QueryHelper helper = null;
	private boolean hasEditPermission = false, hasTemplatePermission = false, hasCreatePermission = false;
	
	//thomas added:
	public static final String SHOW_WIZARD = "show_wizard";
	
	private static final String PARAM_STEP = "step";
	private static final String PARAM_NEXT = "next";
	private static final String PARAM_LAST = "last";
	private static final String PARAM_FINAL = "final";
	public static final String PARAM_CANCEL = "cancel";
	public static final String PARAM_SAVE = "save";
	private static final String PARAM_ADD = "add";
	private static final String PARAM_DROP = "drop";
	private static final String PARAM_DYNAMIC = "dynamic";
	public static final String PARAM_QUIT = "quit";
	private static final String PARAM_SOURCE = "source_entity";
	private static final String PARAM_RELATED = "related_entity";
	private static final String PARAM_FIELDS = "entity_fields";
	private static final String PARAM_CONDITION = "field_pattern";
	private static final String PARAM_COND_TYPE = "field_type";
	private static final String PARAM_COND_FIELD = "field";
	private static final String PARAM_COND_ENTITY = "entity";
	public static final String PARAM_QUERY_FOLDER_ID = "qb_fid";
	public static final String PARAM_LAYOUT_FOLDER_ID ="qb_layoutId";
	public static final String PARAM_QUERY_ID = "qb_qid";
	public static final String PARAM_QUERY_NAME = "q_name";
	private static final String PERM_TEMPL_EDIT = "template";
	private static final String PERM_CREATE = "create";
	private static final String PARAM_LOCK = "lock";
	private static final String PARAM_FUNCTION = "mkfunction";
	private static final String PARAM_FUNC_TYPE = "mkfunctype";
	private int heightOfStepTable = 300;
	private int step = 1;
	private int queryFolderID = -1;
	private int queryID = -1;
	private int relationDepth = 4;
	private int investigationLevel = 6;
	private int tableBorder = 0;
	private String zebraColor1 = "#CCCC99";
	private String zebraColor2 = "#FFFFFF";
	private String stepTableColor = "#CDD6E6";
	private boolean allowEmptyConditions = true;
	private boolean showSourceEntityInSelectBox = true;
	private boolean closeParentWindow = false;
	private boolean allowFunctions = true;
	private QuerySession sessionBean;
	private String defaultDynamicPattern = "";
	
	public static void cleanSession(IWContext iwc)	{
		try {
			IBOLookup.removeSessionInstance(iwc, QuerySession.class);
		}
		catch (RemoteException ex) {
			String message =
				"[QueryBuilder]: Can't retrieve IBOLookup.";
			System.err.println(message + " Message is: " + ex.getMessage());
			ex.printStackTrace(System.err);
			throw new RuntimeException(message);
		}
		catch (RemoveException rmEx) {
			String message =
				"[WorkReportBoardMemberEditor]: Can't remove SessionBean.";
			System.err.println(message + " Message is: " + rmEx.getMessage());
			rmEx.printStackTrace(System.err);
		}
	}
		
	
	public void control(IWContext iwc) {
		if (hasEditPermission || hasTemplatePermission || hasCreatePermission) {
			try {

				if (iwc.isParameterSet(PARAM_QUERY_FOLDER_ID)) {
					queryFolderID = Integer.parseInt(iwc.getParameter(PARAM_QUERY_FOLDER_ID));
				}
				if (iwc.isParameterSet(PARAM_QUERY_ID)) {
					queryID = Integer.parseInt(iwc.getParameter(PARAM_QUERY_ID));
				}

				sessionBean = (QuerySession) IBOLookup.getSessionInstance(iwc, QuerySession.class);
				if (queryID > 0)
					sessionBean.setXmlFileID(queryID);
				helper = sessionBean.getQueryHelper();

				// if not moving around we stay at entity tree
				if (iwc.isParameterSet("tree_action"))
					step = 2;
				else
					step =
						iwc.isParameterSet(PARAM_STEP)
							? Integer.parseInt(iwc.getParameter(PARAM_STEP))
							: helper.getStep();
				step = step == 0 ? 1 : step;
				//System.err.println("djokid");
				//System.err.println("helper step is " + helper.getStep());
				//System.err.println("this step is before process" + step);+
				processForm(iwc);
				//System.err.println("this step is after process" + step);
				Table table = new Table(1, 3);
				String width = getWidth();
				table.setWidth(width != null ? width : "300");
				table.setBorder(tableBorder);
				table.setColor(stepTableColor);
				table.setColor(1, 1, "#FFFFFF");

				Table headerTable = new Table(2, 2);
				headerTable.setWidth(Table.HUNDRED_PERCENT);
				headerTable.add(getStepText(iwrb.getLocalizedString("step", "Step") + " " + step), 1, 1);
				headerTable.add(getMsgText(getStepMessage()), 1, 2);
				headerTable.mergeCells(2, 1, 2, 2);
				headerTable.add(iwb.getImage("wizard.png"), 2, 1);
				headerTable.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_RIGHT);

				table.add(headerTable, 1, 1);

				table.add(getStep(iwc), 1, 2);
				table.setColor(1, 2, stepTableColor);
				table.setVerticalAlignment(1, 2, Table.VERTICAL_ALIGN_TOP);
				table.setAlignment(1, 3, Table.HORIZONTAL_ALIGN_RIGHT);
				table.setHeight(2, this.heightOfStepTable);
				Form form = new Form();
				// thomas changed: queryFolder  id is always set
				// if (queryFolderID > 0 && step < 5)
				if (queryFolderID > 0) {
					form.addParameter(PARAM_QUERY_FOLDER_ID, queryFolderID);
				}
				if (queryID > 0)
					form.addParameter(PARAM_QUERY_ID, queryID);
				table.add(getButtons(step), 1, 3);
				form.addParameter(PARAM_STEP, step);
				// thomas added:
				// this parameter serves as a flag for the outer window to continue showing the wizard
				// the outer window checks also if PARAM_CANCEL or PARAM_QUIT exist
				form.addParameter(SHOW_WIZARD, SHOW_WIZARD);
				if (iwc.isParameterSet(PARAM_LAYOUT_FOLDER_ID)) {
					String layoutFolderId = iwc.getParameter(PARAM_LAYOUT_FOLDER_ID);
					form.addParameter(PARAM_LAYOUT_FOLDER_ID, layoutFolderId);
				}
				form.add(table);
				add(form);
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			catch (RemoveException e) {
				e.printStackTrace();
			}
		}
		else {
			add(iwrb.getLocalizedString("no_permission", "You don't have permission !!"));
		}
	}
	private void processForm(IWContext iwc) throws ClassNotFoundException, RemoveException, IOException {
		//		destroy sessionbean and close window if set
		if (iwc.isParameterSet(PARAM_QUIT)) {
			//if(closeParentWindow)
			// try to close parent window
			// thomas: replaced by staic method
			// IBOLookup.removeSessionInstance(iwc, QuerySession.class);
			QueryBuilder.cleanSession(iwc);
			step = 1;
		}
		else if (iwc.isParameterSet(PARAM_CANCEL)) {
			helper.clearAll();
			step = 1;
		}
		else if (iwc.isParameterSet(PARAM_FINAL)) {
			processNextStep(iwc);
			step = 5;
		}
		else if (iwc.isParameterSet(PARAM_FUNCTION)){
			processFunction(iwc);
		}
		else if (iwc.isParameterSet(PARAM_ADD)) {
			
			String field = iwc.getParameter(PARAM_COND_FIELD);
			String equator = iwc.getParameter(PARAM_COND_TYPE);
			String pattern = iwc.getParameter(PARAM_CONDITION);
			if (!"".equals(pattern) || iwc.isParameterSet(PARAM_DYNAMIC)){
				QueryFieldPart fieldPart = QueryFieldPart.decode(field);
				helper.addField(fieldPart);
				if(pattern ==null || "".equals(pattern)){
					pattern = defaultDynamicPattern;
				}
				QueryConditionPart part = new QueryConditionPart(fieldPart.getEntity(),fieldPart.getName(), equator, pattern);
				part.setLocked(iwc.isParameterSet(PARAM_LOCK));
				part.setDynamic(iwc.isParameterSet(PARAM_DYNAMIC));
				helper.addCondition(part);
				
			}

		}
		else if (iwc.isParameterSet(PARAM_DROP)) {
			String dropvalue = iwc.getParameter(PARAM_DROP);

			if (dropvalue != null && helper.hasConditions()) {
				List conditions = helper.getListOfConditions();
				for (int i = 0; i < conditions.size(); i++) {
					QueryConditionPart element = (QueryConditionPart) conditions.get(i);
					if (element.encode().equals(dropvalue))
						conditions.remove(i);
				}

			}
		}
		else if (iwc.isParameterSet(PARAM_SAVE)) {
			helper.setTemplate(iwc.isParameterSet(PARAM_ASTEMPLATE));
			String name = iwc.getParameter(PARAM_QUERY_NAME);
			if (name == null)
				name = iwrb.getLocalizedString("step_5_default_queryname", "My query");
			ICFile q = sessionBean.storeQuery(name, queryFolderID);
			if (q != null) {
				queryID = ((Integer) q.getPrimaryKey()).intValue();
				//add("Query was saved with ID: "+queryID  );
			}
			//else
			//add("Query was not saved");
		}
		else if (iwc.isParameterSet(PARAM_NEXT)) {
			boolean proceed = processNextStep(iwc);
			if (proceed) {
				step++;
				//System.out.println(" proceed to next step");
			}
			//else
			//	System.out.println(" do not proceed to next step");
		}
		else if (iwc.isParameterSet(PARAM_LAST)) {
			processPreviousStep(iwc);
		}
		else if (iwc.isParameterSet(PARAM_CANCEL)) {
			helper.clearAll();
			step = 1;
		}
		else if (iwc.isParameterSet(PARAM_FINAL)) {
			processNextStep(iwc);
			step = 5;
		}
		else if (iwc.isParameterSet(PARAM_SAVE)) {
			helper.setTemplate(true);
			String name = iwc.getParameter(PARAM_QUERY_NAME);
			if (name == null)
				name = iwrb.getLocalizedString("step_5_default_queryname", "My query");
			ICFile q = sessionBean.storeQuery(name, queryFolderID);
			if (q != null) {
				queryID = ((Integer) q.getPrimaryKey()).intValue();
				//add("Query was saved with ID: "+queryID  );
			}
			//else
			//add("Query was not saved");
		}

	}

	private boolean processNextStep(IWContext iwc) throws ClassNotFoundException {
		int currentStep = iwc.isParameterSet(PARAM_STEP) ? Integer.parseInt(iwc.getParameter(PARAM_STEP)) : 1;
		//System.out.println("current processing step " + currentStep);
		switch (currentStep) {
			case 1 :
				return processStep1(iwc);
			case 2 :
				return processStep2(iwc);
			case 3 :
				return processStep3(iwc);
			case 4 :
				return processStep4(iwc);
		}
		return false;
	}
	private boolean processStep4(IWContext iwc) {
		/*
		helper.clearConditions();
		String[] conditions = iwc.getParameterValues(PARAM_CONDITION);
		String[] equators = iwc.getParameterValues(PARAM_COND_TYPE);
		if (conditions != null && equators != null) {
			List listOfFields = helper.getListOfFields();
			if (listOfFields.size() == conditions.length) {
				for (int i = 0; i < conditions.length; i++) {
					if (conditions[i].length() > 0) {
						QueryFieldPart fieldPart = (QueryFieldPart) listOfFields.get(i);
						QueryConditionPart part =
							new QueryConditionPart(fieldPart.getName(), equators[i], conditions[i]);
						helper.addCondition(part);
					}
				}
			}
			else {
				System.out.println("field count and condition count dont match !");
				return false;
			}
		}
		*/
		return helper.hasConditions() || this.allowEmptyConditions;
	}
	private boolean processStep3(IWContext iwc) {
		helper.clearFields();
		String[] fields = null;
		if (iwc.isParameterSet(PARAM_FIELDS)) {
			fields = iwc.getParameterValues(PARAM_FIELDS);
		}
		// allow to select from the left box only ( no ordering ), shortcut !
		else if (iwc.isParameterSet(PARAM_FIELDS + "_left")) {
			fields = iwc.getParameterValues(PARAM_FIELDS + "_left");
		}
		if (fields != null) {
			for (int i = 0; i < fields.length; i++) {
				QueryFieldPart part = QueryFieldPart.decode(fields[i]);
				if (part != null)
					helper.addField(part);
			}
			helper.setFieldsLock(iwc.isParameterSet(PARAM_LOCK));
			return helper.hasFields();
		}
		return false;
	}
	private boolean processStep2(IWContext iwc) {
		helper.clearRelatedEntities();
		if (iwc.isParameterSet(PARAM_RELATED)) {
			String[] entities = iwc.getParameterValues(PARAM_RELATED);
			for (int i = 0; i < entities.length; i++) {
				QueryEntityPart part = QueryEntityPart.decode(entities[i]);
				if (part != null)
					helper.addRelatedEntity(part);
			}
			helper.setEntitiesLock(iwc.isParameterSet(PARAM_LOCK));
			return helper.hasRelatedEntities();
		}
		// if we allow to  work with source entity fields alone
		else
			return helper.hasSourceEntity();
	}
	private boolean processStep1(IWContext iwc) {
		if (iwc.isParameterSet(PARAM_SOURCE)) {
			String sourceEntity = iwc.getParameter(PARAM_SOURCE);
			if (sourceEntity.length() > 0 && !sourceEntity.equalsIgnoreCase("empty")) {
				QueryEntityPart part = QueryEntityPart.decode(sourceEntity);
				helper.setSourceEntity(part);
				helper.getSourceEntity().setLocked(iwc.isParameterSet(PARAM_LOCK));
			}
			return helper.hasSourceEntity();
		}
		return false;
	}
	
	private boolean processFunction(IWContext iwc){
		String type = iwc.getParameter(PARAM_FUNCTION);
		String[] fields = iwc.getParameterValues(PARAM_FIELDS+"_left");
		String display = iwc.getParameter("display");
		if(fields !=null ){
			Vector dfields = new Vector();
			for (int i = 0; i < fields.length; i++) {
				String field = fields[i];
				System.out.println(field);
				QueryFieldPart part = QueryFieldPart.decode(field);
				dfields.add(part);
			}
			if(!dfields.isEmpty()){
				
				QueryFieldPart newPart = (QueryFieldPart) dfields.get(0);
				// concat fields
				if(type.equalsIgnoreCase(QueryXMLConstants.FUNC_CONCAT)){
					String partDisplay = newPart.getDisplay();
					for(int i=1;i<dfields.size();i++){
						QueryFieldPart part = (QueryFieldPart) dfields.get(i);
						// if from same entity, allow concat
						if(newPart.getEntity().equals(part.getEntity())){
							newPart.addColumn(part.getColumns());
							partDisplay+=","+ part.getDisplay();
						}
					}
					newPart.setFunction(type);
					if(display!=null && display.length()>0){
						newPart.setDisplay(display);
					}
					else{
						newPart.setDisplay(type+"("+partDisplay+")");
					}
					helper.addField(newPart);
					
				}
				// other functions
				else{
					newPart.setFunction(type);
					if(display!=null && display.length()>0){
						newPart.setDisplay(display);
					}
					else{
						newPart.setDisplay(type+"("+newPart.getDisplay()+")");
					}
					helper.addField(newPart);
					
				}
				// if another display name
				
				
			}
			else{
				System.out.println("is empty");
			}
		}
		
		return false;
	}
	
	private void processPreviousStep(IWContext iwc) {
		switch (step) {
			case 2 :
				//helper.clearRelatedEntities();
				//helper.clearSourceEntity(); 
				break;
			case 3 :
				//helper.clearRelatedEntities();
				break;
			case 4 :
				//helper.clearFields();
				break;
				// we are coming from the finish step, so maybe we have to 
				// set it to some other step than the previous to the  current
			case 5 :
				System.out.println("helper step is " + helper.getStep());
				step = helper.getStep() + 1;
				//helper.clearConditions();
				break;
		}
		step--;
		//step = helper.getStep()-1;
	}
	public PresentationObject getStep(IWContext iwc) throws ClassNotFoundException, RemoteException {
		switch (step) {
			case 1 :
				return getStep1(iwc);
			case 2 :
				return getStep2(iwc);
			case 3 :
				return getStep3(iwc);
			case 4 :
				return getStep4(iwc);
			case 5 :
				return getStep5(iwc);
			default :
				return getStep1(iwc);
		}
	}

	public String getStepMessage() {
		switch (step) {
			case 1 :
				return iwrb.getLocalizedString("step_1_msg", "Choose a source entity");
			case 2 :
				return iwrb.getLocalizedString("step_2_msg", "Choose related entities");
			case 3 :
				return iwrb.getLocalizedString("step_3_msg", "Choose entity fields and order ");
			case 4 :
				return iwrb.getLocalizedString("step_4_msg", "Define field conditions");
			case 5 :
				return iwrb.getLocalizedString("step_5_msg", "Take proper action");
		}
		return "";
	}

	public Table getStepTable() {
		Table T = new Table();
		T.setBorder(tableBorder);
		//T.setHeight(heightOfStepTable);
		T.setWidth(T.HUNDRED_PERCENT);
		T.setVerticalAlignment(Table.VERTICAL_ALIGN_TOP);
		return T;
	}

	public PresentationObject getStep1(IWContext iwc) throws RemoteException {

		Table table = getStepTable();
		int row = 1;

		//TODO get available source entities with permissions !!!
		Collection sourceEntities = getQueryService(iwc).getSourceQueryEntityParts();
		if (showSourceEntityInSelectBox) {
			SelectionBox select = new SelectionBox(PARAM_SOURCE);
			select.setMaximumChecked(1, iwrb.getLocalizedString("maximum_select_msg", "Select only one"));
			select.setHeight("20");
			select.setWidth("300");
			Iterator iter = sourceEntities.iterator();
			while (iter.hasNext()) {
				QueryEntityPart element = (QueryEntityPart) iter.next();
				select.addMenuElement(element.encode(), iwrb.getLocalizedString(element.getName(), element.getName()));
			}
			if (helper.hasSourceEntity()) {
				QueryEntityPart source = helper.getSourceEntity();
				select.setSelectedElement(source.encode());
			}
			table.add(select, 1, row);
		}
		else {
			DropdownMenu drp = new DropdownMenu(PARAM_SOURCE);
			drp.addMenuElement("empty", "Entity");
			Iterator iter = sourceEntities.iterator();
			while (iter.hasNext()) {
				QueryEntityPart element = (QueryEntityPart) iter.next();
				drp.addMenuElement(element.encode(), iwrb.getLocalizedString(element.getName(), element.getName()));
			}
			if (helper.hasSourceEntity()) {
				QueryEntityPart source = helper.getSourceEntity();
				drp.setSelectedElement(source.encode());
			}
			table.add(drp, 1, row);

		}
		row++;
		if (hasTemplatePermission) {
			CheckBox lockCheck = new CheckBox(PARAM_LOCK, "true");
			if (helper.hasSourceEntity())
				lockCheck.setChecked(helper.getSourceEntity().isLocked());
			table.add(getMsgText(iwrb.getLocalizedString("lock_source_entity", "Lock source entity")), 1, row);
			table.add(lockCheck, 1, row);
		}

		return table;
	}
	public PresentationObject getStep2(IWContext iwc) throws ClassNotFoundException, RemoteException {
		Table table = getStepTable();
		//table.add(getRelatedChoice(iwc),1,2);
		IWTreeNode root = getQueryService(iwc).getEntityTree(helper, investigationLevel);
		EntityChooserTree tree = new EntityChooserTree(root, iwc);

		tree.setUI(tree._UI_WIN);
		Link treeLink = new Link();
		treeLink.addParameter(PARAM_STEP, step);
		tree.setLinkOpenClosePrototype(treeLink);
		tree.addOpenCloseParameter(PARAM_STEP, "2");
		tree.setInitOpenLevel();
		tree.setNestLevelAtOpen(1);
		//tree.setToMaintainParameter(PARAM_STEP,iwc);
		//viewer.setNestLevelAtOpen(4);
		table.add(tree, 1, 1);
		if (hasTemplatePermission) {
			CheckBox lockCheck = new CheckBox(PARAM_LOCK, "true");
			lockCheck.setChecked(helper.isEntitiesLock());
			table.add(getMsgText(iwrb.getLocalizedString("lock_related_entities", "Lock entities")), 1, 2);
			table.add(lockCheck, 1, 2);
		}
		return table;
	}
	public PresentationObject getStep3(IWContext iwc) throws RemoteException {
		QueryService service = getQueryService(iwc);
		Table table = getStepTable();

		int row = 2;

		QueryEntityPart entityPart = helper.getSourceEntity();
		List entities = helper.getListOfRelatedEntities();
		if (entities == null)
			entities = new Vector();
		Iterator iterator = entities.iterator();
		List listOfFields = helper.getListOfFields();
		if (listOfFields == null)
			listOfFields = new Vector();
		Map fieldMap = getQueryPartMap(listOfFields);
		SelectionDoubleBox box = new SelectionDoubleBox(PARAM_FIELDS + "_left", PARAM_FIELDS);
		box.getLeftBox().setTextHeading(getMsgText(iwrb.getLocalizedString("available_fields", "Available fields")));
		box.getRightBox().setTextHeading(getMsgText(iwrb.getLocalizedString("chosen_fields", "Chosen fields")));
		box.getRightBox().addUpAndDownMovers();
		box.getRightBox().setWidth("300");
		box.getLeftBox().setWidth("300");
		box.getRightBox().setHeight("20");
		box.getLeftBox().setHeight("20");
		box.getRightBox().selectAllOnSubmit();
		fillFieldSelectionBox(service, entityPart, fieldMap, box);
		while (iterator.hasNext()) {
			entityPart = (QueryEntityPart) iterator.next();
			fillFieldSelectionBox(service, entityPart, fieldMap, box);
		}
		if(!fieldMap.isEmpty()){
			Iterator iter = fieldMap.values().iterator();
			while(iter.hasNext()){
				QueryFieldPart part = (QueryFieldPart) iter.next();
				box.getRightBox().addElement(part.encode(),iwrb.getLocalizedString(entityPart.getName(), entityPart.getName()) + " -> " + part.getDisplay());
			}
		}
		table.add(box, 2, row);
		
		row++;
		if(allowFunctions){
			table.add(getFunctionTable(),2,row);
		}
		if (hasTemplatePermission) {
			CheckBox lockCheck = new CheckBox(PARAM_LOCK, "true");
			lockCheck.setChecked(helper.isFieldsLock());
			table.add(getMsgText(iwrb.getLocalizedString("lock_fields", "Lock fields")), 2, row);
			table.add(lockCheck, 2, row);
		}
		
		
		return table;
	}
	
	private PresentationObject getFunctionTable(){
		Table table = new Table(8,2);
		int col = 1;
		table.mergeCells(1,1,8,1);
		table.add(getMsgText(iwrb.getLocalizedString("step_3_choose_function","Select function to apply on selected fields in the left box, and new display name if required")),1,1);
		TextInput display = new TextInput("display");
		SubmitButton count = new SubmitButton(iwrb.getLocalizedImageButton("btn_func.count","Count"),PARAM_FUNCTION,QueryXMLConstants.FUNC_COUNT);
		// concat not supported yet
		//SubmitButton concat = new SubmitButton(iwrb.getLocalizedImageButton("btn_func.concat","Concat"),PARAM_FUNCTION,QueryXMLConstants.FUNC_CONCAT);
		SubmitButton max = new SubmitButton(iwrb.getLocalizedImageButton("btn_func.max","Max"),PARAM_FUNCTION,QueryXMLConstants.FUNC_MAX);
		SubmitButton min = new SubmitButton(iwrb.getLocalizedImageButton("btn_func.min","Min"),PARAM_FUNCTION,QueryXMLConstants.FUNC_MIN);
		SubmitButton sum = new SubmitButton(iwrb.getLocalizedImageButton("btn_func.sum","Sum"),PARAM_FUNCTION,QueryXMLConstants.FUNC_SUM);
		SubmitButton avg = new SubmitButton(iwrb.getLocalizedImageButton("btn_func.avg","Avg"),PARAM_FUNCTION,QueryXMLConstants.FUNC_AVG);
		SubmitButton alias = new SubmitButton(iwrb.getLocalizedImageButton("btn_func.alias","Alias"),PARAM_FUNCTION,QueryXMLConstants.FUNC_ALIAS);
		table.add(display,col++,2);
		table.add(count,col++,2);
//		table.add(concat,col++,2);
		table.add(max,col++,2);
		table.add(min,col++,2);
		table.add(sum, col++,2);
		table.add(avg,col++,2);
		table.add(alias,col++,2);
		return table;
	}
	
	private void fillFieldSelectionBox(
		QueryService service,
		QueryEntityPart entityPart,
		Map fieldMap,
		SelectionDoubleBox box)
		throws RemoteException {
		//System.out.println("filling box with fields from " + entityPart.getName());
		Iterator iter = service.getListOfFieldParts(iwrb, entityPart).iterator();
		while (iter.hasNext()) {
			QueryFieldPart part = (QueryFieldPart) iter.next();
			//System.out.println(" " + part.getName());
			String enc = part.encode();
			if (fieldMap.containsKey(enc)) {
				box.getRightBox().addElement(
					part.encode(),
					iwrb.getLocalizedString(entityPart.getName(), entityPart.getName()) + " -> " + part.getDisplay());
					fieldMap.remove(enc);
			}
			else {
				box.getLeftBox().addElement(
					part.encode(),
					iwrb.getLocalizedString(entityPart.getName(), entityPart.getName()) + " -> " + part.getDisplay());
			}
		}
	}

	public PresentationObject getStep4Old(IWContext iwc) {
		Table table = getStepTable();
		int row = 1;
		table.add(getMsgText(iwrb.getLocalizedString("field_display", "Display")), 2, row);
		table.add(getMsgText(iwrb.getLocalizedString("field_entity", "Entity")), 3, row);
		table.add(getMsgText(iwrb.getLocalizedString("field_equator", "Equator")), 4, row);
		table.add(getMsgText(iwrb.getLocalizedString("field_pattern", "Pattern")), 5, row);
		row++;
		DropdownMenu drp = getConditionTypeDropdown();
		Iterator iter = helper.getListOfFields().iterator();
		Map mapOfFieldConditions = getConditionsMapByFieldName();
		while (iter.hasNext()) {
			QueryFieldPart part = (QueryFieldPart) iter.next();
			table.add(part.getDisplay(), 2, row);
			table.add(iwrb.getLocalizedString(part.getEntity(), part.getEntity()), 3, row);
			//table.add(iwrb.getLocalizedString(part.getTypeClass(), part.getTypeClass()), 4, row);
			table.add(drp, 4, row);
			TextInput conditionInput = new TextInput(PARAM_CONDITION);
			if (mapOfFieldConditions.containsKey(part.getName())) {
				conditionInput.setContent(((QueryConditionPart) mapOfFieldConditions.get(part.getName())).getPattern());
			}
			table.add(conditionInput, 5, row);
			row++;
		}
		return table;
	}

	public PresentationObject getStep4(IWContext iwc) throws RemoteException {
		Table table = getStepTable();
		int row = 1;
		table.add(getMsgText(iwrb.getLocalizedString("field_entity", "Entity")), 2, row);
		table.add(getMsgText(iwrb.getLocalizedString("field_display", "Display")), 3, row);
		table.add(getMsgText(iwrb.getLocalizedString("field_equator", "Equator")), 4, row);
		table.add(getMsgText(iwrb.getLocalizedString("field_pattern", "Pattern")), 5, row);
		if (hasTemplatePermission) {
			table.add(getMsgText(iwrb.getLocalizedString("field_lock", "Lock")), 7, row);
			table.add(getMsgText(iwrb.getLocalizedString("field_dynamic","Dynamic")),8,row);
		}

		row++;
		if (helper.hasConditions()) {
			List conditions = helper.getListOfConditions();
			Map mapOfFields = getMapOfFieldsByName();
			for (Iterator iter = conditions.iterator(); iter.hasNext();) {
				QueryConditionPart part = (QueryConditionPart) iter.next();
				QueryFieldPart field = (QueryFieldPart) mapOfFields.get(part.getField());
				if (field != null) {
					table.add(iwrb.getLocalizedString(field.getEntity(), field.getEntity()), 2, row);
					table.add(field.getDisplay(), 3, row);
				}
				table.add(iwrb.getLocalizedString("conditions." + part.getType(), part.getType()), 4, row);
				table.add(part.getPattern(), 5, row);
				
								
				if (hasTemplatePermission){ 
					if(	part.isLocked()) 
						table.add("x", 7, row);
					if(part.isDynamic())
						table.add("x",8,row);
					table.add(new SubmitButton(iwrb.getLocalizedImageButton("drop", "drop"), PARAM_DROP, part.encode()),	6,	row);
				}
				else if(!(part.isLocked() || part.isDynamic())){
					table.add(new SubmitButton(iwrb.getLocalizedImageButton("drop", "drop"), PARAM_DROP, part.encode()),	6,	row);
				}
				row++;
			}
		}
		table.add(Text.getBreak(), 1, row++);

		DropdownMenu equators = getConditionTypeDropdown();
		DropdownMenu chosenFields = getAvailableFieldsDropdown(iwc);//getFieldDropdown();
		table.add(chosenFields, 3, row);
		table.add(equators, 4, row);
		TextInput pattern = new TextInput(PARAM_CONDITION);
		table.add(pattern, 5, row);
		table.add(new SubmitButton(iwrb.getLocalizedImageButton("add", "Add"), PARAM_ADD), 6, row);
		if(hasTemplatePermission){
		
			CheckBox lock = new CheckBox(PARAM_LOCK);	
			CheckBox dynamic = new CheckBox(PARAM_DYNAMIC);
			table.add(lock,7,row);
			table.add(dynamic,8,row);
		}

		return table;
	}

	public PresentationObject getStep5(IWContext iwc) throws RemoteException {
		Table table = getStepTable();
		int row = 1;
		// thomas changed: do not use the FileChooser 
		// FileChooser folderChooser = new FileChooser(PARAM_FOLDER_ID);

		TextInput queryNameInput = new TextInput(PARAM_QUERY_NAME);
		queryNameInput.setLength(10);
		if (this.queryID > 0) {
			ICFile currentFile = sessionBean.getXMLFile(queryID);
			queryNameInput.setContent(currentFile.getName().toString());
			table.add(iwrb.getLocalizedString("step_5_change_queryname", "Change query name"), 1, row);
		}
		else {
			queryNameInput.setContent(iwrb.getLocalizedString("step_5_default_queryname", "My query"));
			table.add(iwrb.getLocalizedString("step_5_set_queryname", "Set query name"), 1, row);
		}
		table.add(queryNameInput, 2, row);
		row++;
//		if (this.queryFolderID > 0) {
//			ICFile currentFolder = sessionBean.getXMLFile(queryFolderID);
//			folderChooser.setSelectedFile(currentFolder);
//			table.add(iwrb.getLocalizedString("step_5_change_folder", "New folder"), 1, row);
//		}
//		else {
//			table.add(iwrb.getLocalizedString("step_5_choose_folder", "Choose folder to save in"), 1, row);
//		}
//		table.add(folderChooser, 2, row);
		row++;
		CheckBox templateCheck = new CheckBox(PARAM_ASTEMPLATE, "true");
		templateCheck.setChecked(helper.isTemplate());
		table.add(iwrb.getLocalizedString("step_5_check_template", "Save as template query ?"), 1, row);
		table.add(templateCheck, 2, row);

		return table;
	}

	private Table getButtons(int currentStep) {
		Table T = new Table(5, 1);
		T.setWidth(getWidth());
		T.setAlignment(1, 1, T.HORIZONTAL_ALIGN_RIGHT);
		T.setAlignment(2, 1, T.HORIZONTAL_ALIGN_LEFT);
		//T.setAlignment(T.HORIZONTAL_ALIGN_CENTER);
		if (currentStep <= 4) {
			SubmitButton next =
				new SubmitButton(iwrb.getLocalizedImageButton("btn_next", "next >>"), PARAM_NEXT, "true");
			T.add(next, 2, 1);
		}
		if (currentStep > 1) {
			SubmitButton last =
				new SubmitButton(iwrb.getLocalizedImageButton("btn_previous", "<< previous"), PARAM_LAST, "true");
			T.add(last, 1, 1);
		}
		if (currentStep > 0) {
			SubmitButton cancel =
				new SubmitButton(iwrb.getLocalizedImageButton("btn_cancel", "cancel"), PARAM_CANCEL, "true");
			T.add(cancel, 4, 1);
		}
		if (currentStep < 5) {
			SubmitButton finish =
				new SubmitButton(iwrb.getLocalizedImageButton("btn_finish", "finish"), PARAM_FINAL, "true");

			T.add(finish, 3, 1);
		}
		if (currentStep > 4) {
			SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("btn_save", "Save"), PARAM_SAVE, "true");
			T.add(save, 3, 1);
		}
		if (currentStep > 4) {
			SubmitButton quit = new SubmitButton(iwrb.getLocalizedImageButton("btn_quit", "Quit"), PARAM_QUIT, "true");
			T.add(quit, 5, 1);
		}
		return T;
	}
	private PresentationObject getRelatedChoice(IWContext iwc) throws ClassNotFoundException, RemoteException {
		Table T = new Table();
		T.setWidth(T.HUNDRED_PERCENT);
		Collection coll = getQueryService(iwc).getRelatedQueryEntityParts(helper.getSourceEntity(), relationDepth);
		Iterator iter = coll.iterator();
		int row = 1;
		CheckBox checkAll = new CheckBox("checkall");
		checkAll.setToCheckOnClick(PARAM_RELATED, "this.checked");
		T.add(checkAll, 1, row);
		T.add(iwrb.getLocalizedString("entity_name", "Entity name"), 2, row++);
		Map entityMap = getEntityMap(helper.getListOfRelatedEntities());
		while (iter.hasNext()) {
			QueryEntityPart entityPart = (QueryEntityPart) iter.next();
			CheckBox checkBox = new CheckBox(PARAM_RELATED, entityPart.encode());
			checkBox.setChecked(entityMap.containsKey(entityPart.getName()));
			T.add(checkBox, 1, row);
			T.add(
				iwrb.getLocalizedString(entityPart.getName(), entityPart.getName())
					+ " "
					+ entityPart.getBeanClassName(),
				2,
				row);
			row++;
		}
		return T;
	}
	public void main(IWContext iwc) throws Exception {
		debugParameters(iwc);
		iwb = getBundle(iwc);
		iwrb = getResourceBundle(iwc);
		hasEditPermission = hasEditPermission();
		hasTemplatePermission = hasPermission(this.PERM_TEMPL_EDIT, this, iwc);
		hasCreatePermission = hasPermission(this.PERM_CREATE, this, iwc);
		control(iwc);
	}
	public QueryService getQueryService(IWContext iwc) throws RemoteException {
		return (QueryService) IBOLookup.getServiceInstance(iwc, QueryService.class);
	}
	public String getBundleIdentifier() {
		return "com.idega.block.dataquery";
	}
	public Map getQueryPartMap(List listOfQueryParts) {
		int size = 0;
		if (listOfQueryParts != null)
			size = listOfQueryParts.size();
		Map map = new HashMap(size);
		if (listOfQueryParts != null) {
			Iterator iter = listOfQueryParts.iterator();
			while (iter.hasNext()) {
				QueryPart element = (QueryPart) iter.next();
				map.put(element.encode(), element);
				//System.out.println("putting into map : " + element.encode());
			}
		}
		return map;
	}
	public Map getEntityMap(List listOfEntityParts) {
		int size = 0;
		if (listOfEntityParts != null)
			size = listOfEntityParts.size();
		Map map = new HashMap(size);
		if (listOfEntityParts != null) {
			Iterator iter = listOfEntityParts.iterator();
			while (iter.hasNext()) {
				QueryEntityPart element = (QueryEntityPart) iter.next();
				if (element.getPath() != null)
					map.put(element.getPath(), element);
				else
					map.put(element.getName(), element);
			}
		}
		return map;
	}

	public Map getConditionsMapByFieldName() {
		int size = 0;
		if (helper.hasConditions())
			size = helper.getListOfConditions().size();
		Map map = new HashMap(size);
		List fields = helper.getListOfFields();
		if (helper.hasConditions()) {
			Iterator iter = helper.getListOfConditions().iterator();
			while (iter.hasNext()) {
				QueryConditionPart part = (QueryConditionPart) iter.next();
				for (Iterator iterator = fields.iterator(); iterator.hasNext();) {
					QueryFieldPart element = (QueryFieldPart) iterator.next();
					if (part.getField().equals(element.getName())) {
						map.put(part.getField(), part);
						break;
					}
				}
			}
		}
		return map;
	}

	public Map getMapOfFieldsByName() {
		int size = 0;
		if (helper.hasFields())
			size = helper.getListOfFields().size();
		Map map = new HashMap(size);
		if (helper.hasFields()) {
			Iterator iter = helper.getListOfFields().iterator();
			while (iter.hasNext()) {
				QueryFieldPart part = (QueryFieldPart) iter.next();
				map.put(part.getName(), part);
			}
		}
		return map;
	}
	/**
		 * @param i
		 */
	public void setInvestigationLevel(int i) {
		investigationLevel = i;
	}

	/**
	 * @return
	 */
	public int getQueryFolderID() {
		return queryFolderID;
	}

	/**
	 * @param i
	 */
	public void setQueryFolderID(int i) {
		queryFolderID = i;
	}

	public int getQueryId()	{
		return queryID;
	}


	private DropdownMenu getConditionTypeDropdown() {
		DropdownMenu drp = new DropdownMenu(PARAM_COND_TYPE);
		String[] types = QueryConditionPart.getConditionTypes();
		for (int i = 0; i < types.length; i++) {
			drp.addMenuElement(types[i], iwrb.getLocalizedString("conditions." + types[i], types[i]));
		}
		return drp;
	}

	private DropdownMenu getFieldDropdown() {
		DropdownMenu drp = new DropdownMenu(PARAM_COND_FIELD);
		if (helper.hasFields()) {
			List fields = helper.getListOfFields();
			for (Iterator iter = fields.iterator(); iter.hasNext();) {
				QueryFieldPart element = (QueryFieldPart) iter.next();
				drp.addMenuElement(element.getName(), element.getDisplay());
			}
		}
		return drp;
	}
	
	private DropdownMenu getAvailableFieldsDropdown(IWContext iwc)throws RemoteException {
		QueryService service = getQueryService(iwc);
		Map drpMap = new HashMap();
		DropdownMenu drp = new DropdownMenu(PARAM_COND_FIELD);
		if(helper.hasFields()){
			Iterator iter  = helper.getListOfFields().iterator();
			while (iter.hasNext()) {
				QueryFieldPart part = (QueryFieldPart) iter.next();
				drpMap.put(part.encode(),part);
				
				drp.addMenuElement(part.encode(),	iwrb.getLocalizedString(part.getEntity(), part.getEntity()) + " -> " + part.getDisplay());
			}
		}
		
		
		QueryEntityPart entityPart = helper.getSourceEntity();
		List entities = helper.getListOfRelatedEntities();
		if (entities == null)
			entities = new Vector();
		Iterator iterator = entities.iterator();
		
		filldropdown(service, entityPart,drpMap,drp);
		while (iterator.hasNext()) {
			entityPart = (QueryEntityPart) iterator.next();
			filldropdown(service, entityPart,drpMap,drp);
		}
		return drp;
	}
	
	private void filldropdown(QueryService service,QueryEntityPart entityPart,Map drpMap,DropdownMenu drp)throws RemoteException {
		Iterator iter = service.getListOfFieldParts(iwrb, entityPart).iterator();
		String enc;
		while (iter.hasNext()) {
			QueryFieldPart part = (QueryFieldPart) iter.next();
			enc = part.encode();
			if(!drpMap.containsKey(enc)){
				drp.addMenuElement(part.encode(),	iwrb.getLocalizedString(entityPart.getName(), entityPart.getName()) + " -> " + part.getDisplay());
			}
		}
	}

	private Text getStepText(String string) {
		Text text = new Text(string);
		text.setStyle(Text.FONT_FACE_ARIAL);
		text.setFontSize(Text.FONT_SIZE_14_HTML_4);
		text.setBold();
		return text;
	}

	private Text getMsgText(String string) {
		Text text = new Text(string);
		text.setStyle(Text.FONT_FACE_ARIAL);
		text.setFontSize(Text.FONT_SIZE_10_HTML_2);
		text.setBold();
		return text;
	}

	/* (non-Javadoc)
		 * @see com.idega.presentation.Block#registerPermissionKeys()
		 */
	public void registerPermissionKeys() {
		registerPermissionKey(PERM_TEMPL_EDIT);
		registerPermissionKey(PERM_CREATE);
	}

	public class EntityChooserTree extends TreeViewer {
		/**
		 * 
		 */
		Map entityMap = null;
		public EntityChooserTree() {
			super();
			setParallelExtraColumns(2);
			setWidth(Table.HUNDRED_PERCENT);
			setExtraColumnHorizontalAlignment(2, Table.HORIZONTAL_ALIGN_RIGHT);
			entityMap = getEntityMap(helper.getListOfRelatedEntities());
		}
		public EntityChooserTree(ICTreeNode node, IWContext iwc) {
			this();
			setRootNode(node);
		}
		/* (non-Javadoc)
		 * @see com.idega.presentation.ui.AbstractTreeViewer#getObjectToAddToParallelExtraColumn(int, com.idega.core.ICTreeNode, com.idega.presentation.IWContext, boolean, boolean, boolean)
		 */
		public PresentationObject getObjectToAddToParallelExtraColumn(
			int colIndex,
			ICTreeNode node,
			IWContext iwc,
			boolean nodesOpen,
			boolean nodeHasChild,
			boolean isRootNode) {
			QueryEntityPart entityNode = (QueryEntityPart) node;
			if (entityNode != null) {
				if (entityNode.getParentNode() != null) {
					switch (colIndex) {
						case 1 :
							return null; //return new Text(entityNode.getBeanClassName());
						case 2 :
							CheckBox checkBox = new CheckBox(PARAM_RELATED, entityNode.encode());
							if (entityNode.getPath() != null) {
								checkBox.setChecked(entityMap.containsKey(entityNode.getPath()));
								//System.out.println("using path " + entityNode.getPath());
							}
							else {
								checkBox.setChecked(entityMap.containsKey(entityNode.getName()));
								//System.out.println("using name " + entityNode.getName());
							}
							return checkBox;
					}
				}
			}
			return null;
		}
		/* (non-Javadoc)
		 * @see com.idega.presentation.ui.TreeViewer#getSecondColumnObject(com.idega.core.ICTreeNode, com.idega.presentation.IWContext, boolean)
		 */
		public PresentationObject getSecondColumnObject(ICTreeNode node, IWContext iwc, boolean fromEditor) {
			String nodeName = iwrb.getLocalizedString(node.getNodeName(), node.getNodeName());
			getShortenedNodeName(nodeName);
			return new Text(nodeName);
		}

	}

	/**
	 * @return
	 */
	public String getDefaultDynamicPattern() {
		return defaultDynamicPattern;
	}

	/**
	 * @param string
	 */
	public void setDefaultDynamicPattern(String string) {
		defaultDynamicPattern = string;
	}

}
