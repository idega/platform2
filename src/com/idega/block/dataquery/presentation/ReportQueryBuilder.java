/*
 * Created on May 21, 2003
 *
 * QueryBuilder is a wizard that constructs a ReportQuery from the user input.
 */
package com.idega.block.dataquery.presentation;
//import java.awt.Color;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.dataquery.business.QueryService;
import com.idega.block.dataquery.business.QuerySession;
import com.idega.block.dataquery.data.xml.QueryBooleanExpressionPart;
import com.idega.block.dataquery.data.xml.QueryConditionPart;
import com.idega.block.dataquery.data.xml.QueryEntityPart;
import com.idega.block.dataquery.data.xml.QueryFieldPart;
import com.idega.block.dataquery.data.xml.QueryHelper;
import com.idega.block.dataquery.data.xml.QueryOrderConditionPart;
import com.idega.block.dataquery.data.xml.QueryPart;
import com.idega.block.dataquery.data.xml.QueryXMLConstants;
import com.idega.business.IBOLookup;
import com.idega.core.data.ICTreeNode;
import com.idega.core.data.IWTreeNode;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.AbstractTreeViewer;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.SelectionDoubleBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.TreeViewer;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.StringHandler;
//import com.idega.util.IWColor;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author aron 
 * @version 1.0
 */
public class ReportQueryBuilder extends Block {
	private static final String PARAM_ASTEMPLATE = "astemplate";
	private IWBundle iwb = null;
	private IWResourceBundle iwrb = null;
	private static final String IWBUNDLE_IDENTIFIER = "com.idega.block.dataquery";
	private QueryHelper helper = null;
	private boolean hasEditPermission = false, hasTemplatePermission = false, hasCreatePermission = false;
	
	//thomas added:
	// this parameter describes the mode of the query builder
	// -1 means: simple mode
	// 0,1,2 means expert mode and sets the investigation level
	public static final String SHOW_WIZARD = "show_wizard";
	
	private static final String PARAM_STEP = "step";
	private static final String PARAM_NEXT = "next";
	private static final String PARAM_LAST = "last";
	private static final String PARAM_FINAL = "final";
	public static final String PARAM_CANCEL = "cancel";
	public static final String PARAM_SAVE = "save";
	private static final String PARAM_ADD = "add";
	private static final String PARAM_DROP = "drop";
	private static final String PARAM_SET_EXPRESSION = "setExpression";
	private static final String PARAM_DYNAMIC = "dynamic";
	public static final String PARAM_QUIT = "quit";
	private static final String PARAM_QUERY_AS_SOURCE = "source_query";
	private static final String PARAM_SOURCE = "source_entity";
	private static final String PARAM_RELATED = "related_entity";
	private static final String PARAM_FIELDS = "entity_fields";
	private static final String PARAM_ORDER_FIELDS = "order_fields";
	private static final String PARAM_ORDER_FUNCTION = "order_function";
	private static final String PARAM_CONDITION = "field_pattern";
	private static final String PARAM_COND_TYPE = "field_type";
	private static final String PARAM_COND_FIELD = "field";
	private static final String PARAM_COND_ENTITY = "entity";
	private static final String PARAM_COND_DESCRIPTION = "description";
	private static final String PARAM_BOOLEAN_EXPRESSION = "booleanExpression";
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
	private String layoutFolderID;
	private int queryID = -1;
	// thomas: not used
	//private int relationDepth = 4;
	private int investigationLevel = 6;
	private int tableBorder = 0;
	private String zebraColor1 = "#CCCC99";
	private String zebraColor2 = "#FFFFFF";
	private String stepTableColor = "#CDD6E6";
	private boolean allowEmptyConditions = true;
	private boolean showSourceEntityInSelectBox = true;
	private boolean showQueries = true;
	private boolean closeParentWindow = false;
	private boolean allowFunctions = true;
	private QuerySession sessionBean;
	private String defaultDynamicPattern = "";
	
	// added by thomas
	// the second step is skipped when expert mode is false 
	private boolean expertMode = true;
	
	
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
		
	// added by thomas
	public void setExpertMode(boolean expertMode, int investigationLevel)	{
		this.expertMode = expertMode;
		this.investigationLevel = investigationLevel;
	}
	
	
	public void control(IWContext iwc) {
		if (hasEditPermission || hasTemplatePermission || hasCreatePermission) {
			try {
				if (iwc.isParameterSet(SHOW_WIZARD))	{
					investigationLevel = Integer.parseInt(iwc.getParameter(SHOW_WIZARD));
					expertMode = (investigationLevel > -1);
				}

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
				form.addParameter(SHOW_WIZARD, Integer.toString(investigationLevel));
				if (iwc.isParameterSet(PARAM_LAYOUT_FOLDER_ID)) {
					layoutFolderID = iwc.getParameter(PARAM_LAYOUT_FOLDER_ID);
					form.addParameter(PARAM_LAYOUT_FOLDER_ID, layoutFolderID);
				}

				String width = getWidth();
				table.setWidth(width != null ? width : "300");
				table.setBorder(tableBorder);
				table.setColor(stepTableColor);
				table.setColor(1, 1, "#FFFFFF");

				Table headerTable = new Table(2, 2);
				headerTable.setWidth(Table.HUNDRED_PERCENT);
				// added by thomas
				// skip the second step
				int displayStep = step;
				if (! expertMode) {
					displayStep = (step == 1)? 1 : step -1;
				}
				headerTable.add(getStepText(iwrb.getLocalizedString("step", "Step") + " " + displayStep), 1, 1);
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
				form.add(table);
				add(form);
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			add(iwrb.getLocalizedString("no_permission", "You don't have permission !!"));
		}
	}
	private void processForm(IWContext iwc) throws IOException {
		//		destroy sessionbean and close window if set
		if (iwc.isParameterSet(PARAM_QUIT)) {
			//if(closeParentWindow)
			// try to close parent window
			// thomas: replaced by staic method
			// IBOLookup.removeSessionInstance(iwc, QuerySession.class);
			ReportQueryBuilder.cleanSession(iwc);
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
		else if (iwc.isParameterSet(PARAM_ORDER_FUNCTION))	{
			processFunctionForOrder(iwc);
		}
		else if (iwc.isParameterSet(PARAM_SET_EXPRESSION)) {
			processBooleanExpression(iwc);
		}
		else if (iwc.isParameterSet(PARAM_ADD)) {
			
			String field = iwc.getParameter(PARAM_COND_FIELD);
			String equator = iwc.getParameter(PARAM_COND_TYPE);
			String pattern = iwc.getParameter(PARAM_CONDITION);
			String description = iwc.getParameter(PARAM_COND_DESCRIPTION);
			if (!"".equals(pattern) || iwc.isParameterSet(PARAM_DYNAMIC)){
				QueryFieldPart fieldPart = QueryFieldPart.decode(field);
				helper.addHiddenField(fieldPart);
				if(pattern ==null || "".equals(pattern)){
					pattern = defaultDynamicPattern;
				}
				int id = helper.getNextIdForCondition();
				QueryConditionPart part = new QueryConditionPart(id, fieldPart.getEntity(), fieldPart.getPath(), fieldPart.getName(), equator, pattern, description);
				part.setLocked(iwc.isParameterSet(PARAM_LOCK));
				part.setDynamic(iwc.isParameterSet(PARAM_DYNAMIC));
				helper.addCondition(part);
				
			}
			// update boolean expression
			processBooleanExpression(iwc);
		}
		else if (iwc.isParameterSet(PARAM_DROP)) {
			String dropvalue = iwc.getParameter(PARAM_DROP);

			if (dropvalue != null && helper.hasConditions()) {
				List conditions = helper.getListOfConditions();
				for (int i = 0; i < conditions.size(); i++) {
					QueryConditionPart element = (QueryConditionPart) conditions.get(i);
					if (element.encode().equals(dropvalue)) {
						conditions.remove(i);
					}
				}
				// update boolean expression
				processBooleanExpression(iwc);
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
				// in the simple mode skip the second step
				if (! expertMode && step == 1) {
					step = 3;
				}
				else {
					step++;
				}
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
			processStep5(iwc);
			//else
			//add("Query was not saved");
		}

	}

	private boolean processNextStep(IWContext iwc) throws RemoteException  {
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
			case 5:
				return processStep5(iwc);
		}
		return false;
	}
	
	private boolean processStep5(IWContext iwc) {
		if (helper.hasConditions()) {
			// has conditions, ask for the corresponding boolean expression
			return processBooleanExpression(iwc);
		}
		// has no conditions: is that allowed?
		return this.allowEmptyConditions;
	}
	
	private boolean processBooleanExpression(IWContext iwc) {
		String booleanExpression = "";
		if (iwc.isParameterSet(PARAM_BOOLEAN_EXPRESSION)) {
			booleanExpression = iwc.getParameter(PARAM_BOOLEAN_EXPRESSION);
		}
		QueryBooleanExpressionPart booleanExpressionPart = helper.getBooleanExpressionForConditions();
		if (booleanExpressionPart == null) {
			booleanExpressionPart = new QueryBooleanExpressionPart();
			helper.setBooleanExpressionForConditions(booleanExpressionPart);
		}
		booleanExpressionPart.updateConditions(helper.getListOfConditions(), booleanExpression);
		return booleanExpressionPart.isBooleanExpressionValid();
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
	
	private boolean processStep4(IWContext iwc) {
		helper.clearOrderConditions();
		String[] orderConditions = null;
		if (iwc.isParameterSet(PARAM_ORDER_FIELDS))	{
			orderConditions = iwc.getParameterValues(PARAM_ORDER_FIELDS);
		}
		else if (iwc.isParameterSet(PARAM_ORDER_FIELDS + "_left"))	{
			orderConditions = iwc.getParameterValues(PARAM_ORDER_FIELDS + "_left");
		}
		if (orderConditions != null) {
			for (int i = 0; i < orderConditions.length; i++) {
				// thomas: this is not the best way to do this but at the moment I have no other ideas.
				// on the right side of the selection box are query order conditions or query fields if the 
				// order condition has not been created yet.
				QueryOrderConditionPart conditionPart = QueryOrderConditionPart.decode(orderConditions[i]);
				QueryFieldPart fieldPart = null;
				if (conditionPart == null) {
					fieldPart = QueryFieldPart.decode(orderConditions[i]);
				}
				if (fieldPart != null) {
					helper.addHiddenField(fieldPart);
					conditionPart = new QueryOrderConditionPart(fieldPart.getEntity(),fieldPart.getPath(),fieldPart.getColumns());
				}
				if (conditionPart != null) {
					helper.addOrderCondition(conditionPart);
				}
			}
		}
		// order conditions are not mandatory
		return true;
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
	private boolean processStep1(IWContext iwc) throws RemoteException{
		if (iwc.isParameterSet(PARAM_QUERY_AS_SOURCE))	{
			String queryId = iwc.getParameter(PARAM_QUERY_AS_SOURCE);
			QueryHelper queryHelperAsSource = sessionBean.getQueryService().getQueryHelper(Integer.parseInt(queryId));
			helper.addQuery(queryHelperAsSource);
		}
		// do not use else if 
		if (iwc.isParameterSet(PARAM_SOURCE)) {
			String sourceEntity = iwc.getParameter(PARAM_SOURCE);
			if (sourceEntity.length() > 0 && !sourceEntity.equalsIgnoreCase("empty")) {
				QueryEntityPart part = QueryEntityPart.decode(sourceEntity);
				helper.setSourceEntity(part);
				helper.getSourceEntity().setLocked(iwc.isParameterSet(PARAM_LOCK));
				// added by thomas: start
				if (! expertMode) {
					helper.clearRelatedEntities();
					List list = getQueryService(iwc).getRelatedEntities(helper,  investigationLevel);
					Iterator iterator = list.iterator();
					while (iterator.hasNext())	{
						QueryEntityPart entityPart = (QueryEntityPart) iterator.next();
						helper.addRelatedEntity(entityPart);
					}
				}
				// added by thomas: end
			}
		}
		// either an entity as source or a query as source
		if (expertMode) {
			return helper.hasSourceEntity();
		}
		else {
		 return helper.hasSourceEntity() || helper.hasPreviousQuery();
		}
	}
	
	private boolean processStep6(IWContext iwc) throws IOException	{
		helper.setTemplate(true);
		String name = iwc.getParameter(PARAM_QUERY_NAME);
		if (name == null) {
			name = iwrb.getLocalizedString("step_5_default_queryname", "My query");
		}
		ICFile q = sessionBean.storeQuery(name, queryFolderID);
		if (q != null) {
			queryID = ((Integer) q.getPrimaryKey()).intValue();
		}
		return true;
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

	private boolean processFunctionForOrder(IWContext iwc){
		//String type = QueryXMLConstants.TYPE_DESCENDANT;
		String[] fields = iwc.getParameterValues(PARAM_ORDER_FIELDS+"_left");
		if(fields !=null ){
			for (int i = 0; i < fields.length; i++) {
				String field = fields[i];
				System.out.println(field);
				QueryFieldPart fieldPart = QueryFieldPart.decode(field);
				helper.addHiddenField(fieldPart);
				QueryOrderConditionPart	conditionPart = new QueryOrderConditionPart(fieldPart.getEntity(),fieldPart.getPath(),fieldPart.getColumns());
				conditionPart.setDescendant(true);
				helper.addOrderCondition(conditionPart);
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
			// added by thomas
			// skip the second step
				if (! expertMode) {
					step--;
				}
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
	public PresentationObject getStep(IWContext iwc) throws RemoteException {
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
			case 6:
				return getStep6(iwc);
			default :
				return getStep1(iwc);
		}
	}

	public String getStepMessage() {
		switch (step) {
			case 1 :
				return iwrb.getLocalizedString("query_builder_step_1_msg", "Choose a source entity");
			case 2 :
				return iwrb.getLocalizedString("query_builder_step_2_msg", "Choose related entities");
			case 3 :
				return iwrb.getLocalizedString("query_builder_step_3_msg", "Choose entity fields");
			case 4:
				return iwrb.getLocalizedString("query_builder_step_4_msg", "Choose fields for order");
			case 5 :
				return iwrb.getLocalizedString("query_builder_step_5_msg", "Define field conditions");
			case 6 :
				return iwrb.getLocalizedString("query_builder_step_6_msg", "Take proper action");
		}
		return "";
	}

	public Table getStepTable() {
		Table T = new Table();
		T.setBorder(tableBorder);
		//T.setHeight(heightOfStepTable);
		T.setWidth(Table.HUNDRED_PERCENT);
		T.setVerticalAlignment(Table.VERTICAL_ALIGN_TOP);
		return T;
	}

	public PresentationObject getStep1(IWContext iwc) throws RemoteException {

		Table table = getStepTable();
		int row = 1;

		//TODO get available source entities with permissions !!!
		if (expertMode)	{
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
				table.add(select, 2, row++);
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
				table.add(drp, 2, row++);
	
			}
		}
		if (showQueries)	{
			SelectionBox select = new SelectionBox(PARAM_QUERY_AS_SOURCE);
			select.setMaximumChecked(1, iwrb.getLocalizedString("maximum_select_msg", "Select only one"));
			select.setHeight("20");
			select.setWidth("300");
			//ICTreeNode rootNode;

/// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	  	User currentUser = iwc.getCurrentUser();
	  	GroupBusiness groupBusiness = getGroupBusiness();
	  	UserBusiness userBusiness = getUserBusiness();
	  	//TODO: thi solve problems with group types
	  	String[] groupTypes = 
				{ "iwme_federation", "iwme_union", "iwme_regional_union",  "iwme_league", "iwme_club", "iwme_club_division"};
			Group topGroup = userBusiness.getUsersHighestTopGroupNode(currentUser, Arrays.asList(groupTypes), iwc);
			if (topGroup == null) {
				List groupType = new ArrayList();
				groupType.add("general");
				topGroup = userBusiness.getUsersHighestTopGroupNode(currentUser, groupType,iwc);
			}
	  	Collection parentGroups = new ArrayList();
	  	parentGroups.add(topGroup);
	  	try {
	  		// brilliant implementation in GroupBusiness!!!!!!
	  		// null is returned instead of an empty collection!!!! This is really brilliant.
	  		//TODO: implement a better version of that method
	  	Collection coll  = groupBusiness.getParentGroupsRecursive(topGroup);
	  	if (coll != null) {
	  		parentGroups.addAll(coll);
	  	}
	  	//TODO thi: handle exception in the right way
	  	}
	  	catch (Exception ex) {
	  		parentGroups = new ArrayList();
	  	}
	  	//List queryRepresentations = new ArrayList();
	  	Iterator parentGroupsIterator = parentGroups.iterator();
			while (parentGroupsIterator.hasNext()) {
	  		Group group = (Group) parentGroupsIterator.next();
	  		String groupName = group.getName();
	  		String groupId = group.getPrimaryKey().toString();
	  		StringBuffer buffer = new StringBuffer(groupId).append("_").append("public");
	  		ICFile folderFile = getFile(buffer.toString());
	  		if (folderFile != null) {
	  			// bad implementation:
	  			// if the children list is empty null is returned. 
	  			//TODO: thi: change the implementation
	  			Iterator iterator = folderFile.getChildren();
	  			if (iterator == null) {
	  				iterator = (new ArrayList(0)).iterator();
	  			}
					while (iterator.hasNext())	{
	  				ICTreeNode node = (ICTreeNode) iterator.next();
	  				int id = node.getNodeID();
	  				String name = node.getNodeName();
	  				String displayName = new StringBuffer(groupName).append(" - ").append(name).toString();
	  				select.addMenuElement(Integer.toString(id), displayName);
	//  				// show only the query with a specified id if desired 
	//  				if (showOnlyOneQueryWithId == -1 || id == showOnlyOneQueryWithId)	{
	//  					QueryRepresentation representation = new QueryRepresentation(id, name, groupName);
	//  					queryRepresentations.add(representation);
	  				}
					}
	  		}
	  		
				table.add(select, 1, (row == 1) ? 1 : row - 1 );
		}
		if (hasTemplatePermission) {
			CheckBox lockCheck = new CheckBox(PARAM_LOCK, "true");
			if (helper.hasSourceEntity())
				lockCheck.setChecked(helper.getSourceEntity().isLocked());
			table.add(getMsgText(iwrb.getLocalizedString("lock_source_entity", "Lock source entity")), 1, row);
			table.add(lockCheck, 1, row);
		}

		return table;
	}
	public PresentationObject getStep2(IWContext iwc) throws RemoteException {
		Table table = getStepTable();
		//table.add(getRelatedChoice(iwc),1,2);
		IWTreeNode root = getQueryService(iwc).getEntityTree(helper, investigationLevel);
		EntityChooserTree tree = new EntityChooserTree(root, iwc);

		tree.setUI(AbstractTreeViewer._UI_WIN);
		Link treeLink = new Link();
		treeLink.addParameter(PARAM_STEP, step);
		treeLink.addParameter(SHOW_WIZARD, Integer.toString(investigationLevel));
		treeLink.addParameter(PARAM_QUERY_FOLDER_ID, queryFolderID);
		treeLink.addParameter(PARAM_LAYOUT_FOLDER_ID, layoutFolderID);
		tree.setLinkOpenClosePrototype(treeLink);
		tree.addOpenCloseParameter(PARAM_STEP, Integer.toString(step));
		tree.addOpenCloseParameter(SHOW_WIZARD, Integer.toString(investigationLevel));
		tree.addOpenCloseParameter(PARAM_QUERY_FOLDER_ID, Integer.toString(queryFolderID));
		tree.addOpenCloseParameter(PARAM_LAYOUT_FOLDER_ID, layoutFolderID);
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
		
		List entities = helper.getListOfRelatedEntities();
		if (entities == null)
			entities = new Vector();
		Iterator iterator = entities.iterator();
		List listOfFields = helper.getListOfVisibleFields();
		if (listOfFields == null)
			listOfFields = new Vector();

		Map fieldMap = getQueryPartMap(listOfFields);
		SelectionDoubleBox box = new SelectionDoubleBox(PARAM_FIELDS + "_left", PARAM_FIELDS);
		
		// fill the right box with already chosen fields
		fillFieldSelectionBox(listOfFields, box);
		
		box.getLeftBox().setTextHeading(getMsgText(iwrb.getLocalizedString("available_fields", "Available fields")));
		box.getRightBox().setTextHeading(getMsgText(iwrb.getLocalizedString("chosen_fields", "Chosen fields")));
		box.getRightBox().addUpAndDownMovers();
		box.getRightBox().setWidth("400");
		box.getLeftBox().setWidth("400");
		box.getRightBox().setHeight("20");
		box.getLeftBox().setHeight("20");
		box.getRightBox().selectAllOnSubmit();
		
		// in simple mode source entity is not set
		QueryEntityPart entityPart;
		if (helper.hasSourceEntity()) {
			entityPart = helper.getSourceEntity();
			fillFieldSelectionBox(service, entityPart, fieldMap, box);
		}
		
		if (helper.hasPreviousQuery())	{
			List resultFields = new ArrayList();
			QueryHelper previousQuery = helper.previousQuery();
			String previousQueryName = previousQuery.getName();
			List fields = previousQuery.getListOfVisibleFields();
			Iterator fieldIterator = fields.iterator();
			while (fieldIterator.hasNext())	{
				QueryFieldPart fieldPart = (QueryFieldPart) fieldIterator.next();
				String display = fieldPart.getDisplay();
				String type = fieldPart.getTypeClass();
							
				QueryFieldPart newFieldPart = 
					new QueryFieldPart(display, previousQueryName, previousQueryName, display, null, display, type, false);
				resultFields.add(newFieldPart);
			}
			fillFieldSelectionBox(previousQueryName, resultFields, fieldMap,box);
		}
		
		
		
		
		
		
		while (iterator.hasNext()) {
			entityPart = (QueryEntityPart) iterator.next();
			fillFieldSelectionBox(service, entityPart, fieldMap, box);
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
	
	public PresentationObject getStep4(IWContext iwc) throws RemoteException {
		QueryService service = getQueryService(iwc);
		Table table = getStepTable();

		int row = 2;
		
		List entities = helper.getListOfRelatedEntities();
		if (entities == null)
			entities = new Vector();
		Iterator relatedEntitiesIterator = entities.iterator();
		List listOfFields = helper.getOrderConditions();
		if (listOfFields == null)
			listOfFields = new Vector();
		Map fieldMap = getQueryPartMap(listOfFields);
		SelectionDoubleBox box = new SelectionDoubleBox(PARAM_ORDER_FIELDS + "_left", PARAM_ORDER_FIELDS);
		
		// fill the right box with already chosen fields
		fillFieldSelectionBoxForOrder(listOfFields, box);
		
		box.getLeftBox().setTextHeading(getMsgText(iwrb.getLocalizedString("available_fields", "Available fields")));
		box.getRightBox().setTextHeading(getMsgText(iwrb.getLocalizedString("chosen_fields", "Chosen fields")));
		box.getRightBox().addUpAndDownMovers();
		box.getRightBox().setWidth("400");
		box.getLeftBox().setWidth("400");
		box.getRightBox().setHeight("20");
		box.getLeftBox().setHeight("20");
		box.getRightBox().selectAllOnSubmit();
		
		// in simple mode source entity is not set
		QueryEntityPart entityPart;
		if (helper.hasSourceEntity()) {
			entityPart = helper.getSourceEntity();
			// box is filled with values from the source entity
			fillFieldSelectionBox(service, entityPart, fieldMap, box);
		}
		
		// box is filled with results field from the previous query
		if (helper.hasPreviousQuery())	{
			List resultFields = new ArrayList();
			QueryHelper previousQuery = helper.previousQuery();
			String previousQueryName = previousQuery.getName();
			List fields = previousQuery.getListOfVisibleFields();
			Iterator fieldIterator = fields.iterator();
			while (fieldIterator.hasNext())	{
				QueryFieldPart fieldPart = (QueryFieldPart) fieldIterator.next();
				String display = fieldPart.getDisplay();
				String type = fieldPart.getTypeClass();
							
				QueryFieldPart newFieldPart = 
					new QueryFieldPart(display, previousQueryName, previousQueryName, display, null, display, type, false);
				resultFields.add(newFieldPart);
			}
			fillFieldSelectionBox(previousQueryName, resultFields, fieldMap,box);
		}

		// box is filled with values from the related entities
		while (relatedEntitiesIterator.hasNext()) {
			entityPart = (QueryEntityPart) relatedEntitiesIterator.next();
			fillFieldSelectionBox(service, entityPart, fieldMap, box);
		}
		table.add(box, 2, row);

		row++;
		table.add(getFunctionTableForOrder(),2,row);
	
		row++;
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
	
	private PresentationObject getFunctionTableForOrder()	{
		Table table = new Table(1,2);
		table.add(getMsgText(iwrb.getLocalizedString("step_4_choose_function","Select descendant for descendant ordering")),1,1);
		SubmitButton alias = new SubmitButton(iwrb.getLocalizedImageButton("btn_descendant","Descendant"),PARAM_ORDER_FUNCTION,QueryXMLConstants.TYPE_DESCENDANT);
		table.add(alias, 1,2);
		return table;
	}

	
	
	// fills the right and the left list of the specified box depending on values set in fieldMap
	// values are retrieved from the specified entityPart
	private void fillFieldSelectionBox(
		QueryService service,
		QueryEntityPart entityPart,
		Map fieldMap,
		SelectionDoubleBox box)
		throws RemoteException {
		//System.out.println("filling box with fields from " + entityPart.getName());
		Iterator iter = service.getListOfFieldParts(iwrb, entityPart, expertMode).iterator();
		while (iter.hasNext()) {
			QueryFieldPart part = (QueryFieldPart) iter.next();
			//System.out.println(" " + part.getName());
			String enc = part.encode();
			if (! fieldMap.containsKey(enc)) {
				box.getLeftBox().addElement(
				part.encode(),
				iwrb.getLocalizedString(entityPart.getName(), entityPart.getName()) + " -> " + part.getDisplay());
			}
		}
	}

//	// fills the right and the left list of the specified box depending on values set in fieldMap
//	// values are retrieved from the specified entityPart
//	private void fillFieldSelectionBoxForOrder(
//		QueryService service,
//		QueryEntityPart entityPart,
//		Map fieldMap,
//		SelectionDoubleBox box)
//		throws RemoteException {
//		//System.out.println("filling box with fields from " + entityPart.getName());
//		Iterator iter = service.getListOfOrderConditionParts(iwrb, entityPart, expertMode).iterator();
//		while (iter.hasNext()) {
//			QueryOrderConditionPart part = (QueryOrderConditionPart) iter.next();
//			//System.out.println(" " + part.getName());
//			String enc = part.encode();
//			if (fieldMap.containsKey(enc)) {
////				StringBuffer buffer = new StringBuffer(iwrb.getLocalizedString(entityPart.getName(), entityPart.getName()) + " -> " + part.getDisplay());
////				buffer.append(' ');
////				if (part.isAscendant()) {
////					buffer.append(iwrb.getLocalizedString(QueryXMLConstants.TYPE_ASCENDANT, QueryXMLConstants.TYPE_ASCENDANT));
////				}
////				else {
////					buffer.append(iwrb.getLocalizedString(QueryXMLConstants.TYPE_DESCENDANT, QueryXMLConstants.TYPE_DESCENDANT));
////				}
////				box.getRightBox().addElement(
////					part.encode(),
////					buffer.toString());
////					fieldMap.remove(enc);
//			}
//			else {
//				box.getLeftBox().addElement(
//					part.encode(),
//					iwrb.getLocalizedString(entityPart.getName(), entityPart.getName()) + " -> " + part.getDisplay());
//			}
//		}
//	}
	
	// fills the right and the left list of the specified box depending on values set in fieldMap
	// values are retrieved from the specified choiceFields
	private void fillFieldSelectionBox(
		String entityName,
		List choiceFields,
		Map fieldMap,
		SelectionDoubleBox box) {
		//System.out.println("filling box with fields from " + entityPart.getName());
		Iterator iter = choiceFields.iterator();
		while (iter.hasNext()) {
			QueryFieldPart part = (QueryFieldPart) iter.next();
			//System.out.println(" " + part.getName());
			String enc = part.encode();
			if (! fieldMap.containsKey(enc)) {
				box.getLeftBox().addElement(
				part.encode(),
				iwrb.getLocalizedString(entityName, entityName) + " -> " + part.getDisplay());
			}
		}
	}
	
	
	private void fillFieldSelectionBox(
		List choiceFields,
		SelectionDoubleBox box) {
		Iterator iter = choiceFields.iterator();
		while (iter.hasNext()) {
			QueryFieldPart part = (QueryFieldPart) iter.next();
			box.getRightBox().addElement(
			part.encode(),
			iwrb.getLocalizedString(part.getEntity(), part.getEntity()) + " -> " + part.getDisplay());
		}
	}
	
	
	private void fillFieldSelectionBoxForOrder(
		List choiceFields,
		SelectionDoubleBox box) {
		//System.out.println("filling box with fields from " + entityPart.getName());
		Iterator iter = choiceFields.iterator();
		while (iter.hasNext()) {
			QueryOrderConditionPart part = (QueryOrderConditionPart) iter.next();
			//System.out.println(" " + part.getName());
			StringBuffer buffer = new StringBuffer(iwrb.getLocalizedString(part.getEntity(), part.getEntity()) + " -> " + part.getField());
			buffer.append(' ');
			if (part.isDescendant()) {
				buffer.append(iwrb.getLocalizedString(QueryXMLConstants.TYPE_DESCENDANT, QueryXMLConstants.TYPE_DESCENDANT));
			}
			box.getRightBox().addElement(
			part.encode(),
			buffer.toString());
		}
	}

	public PresentationObject getStep5(IWContext iwc) throws RemoteException {
		Table table = getStepTable();
		int row = 1;
		table.add(getMsgText(iwrb.getLocalizedString("field_entity", "Entity")), 2, row);
		table.add(getMsgText(iwrb.getLocalizedString("field_display", "Display")), 3, row);
		table.add(getMsgText(iwrb.getLocalizedString("field_equator", "Equator")), 4, row);
		table.add(getMsgText(iwrb.getLocalizedString("field_pattern", "Pattern")), 5, row);
		table.add(getMsgText(iwrb.getLocalizedString("field_description", "Description")), 6, row);
		if (hasTemplatePermission) {
			table.add(getMsgText(iwrb.getLocalizedString("field_lock", "Lock")), 8, row);
			table.add(getMsgText(iwrb.getLocalizedString("field_dynamic","Dynamic")),9,row);
		}

		row++;
		List conditions = null;
		if (helper.hasConditions()) {
			conditions = helper.getListOfConditions();
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
				table.add(part.getDescription(), 6, row);
				
								
				if (hasTemplatePermission){ 
					if(	part.isLocked()) 
						table.add("x", 8, row);
					if(part.isDynamic())
						table.add("x",9,row);
					table.add(new SubmitButton(iwrb.getLocalizedImageButton("drop", "drop"), PARAM_DROP, part.encode()),	7,	row);
				}
				else if(!(part.isLocked() || part.isDynamic())){
					table.add(new SubmitButton(iwrb.getLocalizedImageButton("drop", "drop"), PARAM_DROP, part.encode()),	7,	row);
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
		TextInput description = new TextInput(PARAM_COND_DESCRIPTION);
		table.add(description, 6, row);
		table.add(new SubmitButton(iwrb.getLocalizedImageButton("add", "Add"), PARAM_ADD), 7, row);
		if(hasTemplatePermission){
		
			CheckBox lock = new CheckBox(PARAM_LOCK);	
			CheckBox dynamic = new CheckBox(PARAM_DYNAMIC);
			table.add(lock,8,row);
			table.add(dynamic,9,row);
		}
		
		// boolean expression
		QueryBooleanExpressionPart booleanExpressionPart = helper.getBooleanExpressionForConditions();
		String booleanExpression = "";
		if (booleanExpressionPart != null) {
			booleanExpression = (booleanExpressionPart.isSyntaxOfBooleanExpressionOkay()) ? 
			booleanExpressionPart.getBooleanExpression() :
			booleanExpressionPart.getBadSyntaxBooleanExpression();
		}
		TextInput textInput = new TextInput(PARAM_BOOLEAN_EXPRESSION, booleanExpression);
		textInput.setLength(50);
		row++;
		table.mergeCells(1, row, 6, row);
		table.add(textInput, 1 , row); 
		table.add(new SubmitButton(iwrb.getLocalizedImageButton("Set expression", "Set expression"), PARAM_SET_EXPRESSION, PARAM_SET_EXPRESSION),7 ,row);
		
		return table;
	}

	public PresentationObject getStep6(IWContext iwc) throws RemoteException {
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
		Table T = new Table(4, 1);
		T.setWidth(getWidth());
		T.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_RIGHT);
		T.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_LEFT);
		//T.setAlignment(T.HORIZONTAL_ALIGN_CENTER);
		if (currentStep < 6) {
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
		if (currentStep < 6) {
			SubmitButton finish =
				new SubmitButton(iwrb.getLocalizedImageButton("btn_finish", "finish"), PARAM_FINAL, "true");

			T.add(finish, 3, 1);
		}
		if (currentStep > 5) {
			SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("btn_save", "Save"), PARAM_SAVE, "true");
			T.add(save, 3, 1);
		}
		// thomas: removed
//		if (currentStep > 4) {
//			SubmitButton quit = new SubmitButton(iwrb.getLocalizedImageButton("btn_quit", "Quit"), PARAM_QUIT, "true");
//			T.add(quit, 5, 1);
//		}
		return T;
	}
	
	public void main(IWContext iwc) throws Exception {
		debugParameters(iwc);
		iwb = getBundle(iwc);
		iwrb = getResourceBundle(iwc);
		//TODO thi think about that, ask Aron
		hasEditPermission = true;
		hasTemplatePermission = true;
		hasCreatePermission = true;
//		hasEditPermission = hasEditPermission();
//		hasTemplatePermission = hasPermission(this.PERM_TEMPL_EDIT, this, iwc);
//		hasCreatePermission = hasPermission(this.PERM_CREATE, this, iwc);
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
		List fields = helper.getListOfVisibleFields();
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
			size = helper.getListOfVisibleFields().size();
		Map map = new HashMap(size);
		if (helper.hasFields()) {
			Iterator iter = helper.getListOfVisibleFields().iterator();
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
			List fields = helper.getListOfVisibleFields();
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
			Iterator iter  = helper.getListOfVisibleFields().iterator();
			while (iter.hasNext()) {
				QueryFieldPart part = (QueryFieldPart) iter.next();
				drpMap.put(part.encode(),part);
				
				drp.addMenuElement(part.encode(),	iwrb.getLocalizedString(part.getEntity(), part.getEntity()) + " -> " + part.getDisplay());
			}
		}

		List entities = helper.getListOfRelatedEntities();
		if (entities == null)
			entities = new Vector();
		Iterator iterator = entities.iterator();
		
		// in simple mode source entity is not set
		QueryEntityPart entityPart;
		if (helper.hasSourceEntity()) {
			entityPart = helper.getSourceEntity();
			filldropdown(service, entityPart,drpMap,drp);
		}
		while (iterator.hasNext()) {
			entityPart = (QueryEntityPart) iterator.next();
			filldropdown(service, entityPart,drpMap,drp);
		}
		return drp;
	}
	
	private void filldropdown(QueryService service,QueryEntityPart entityPart,Map drpMap,DropdownMenu drp)throws RemoteException {
		Iterator iter = service.getListOfFieldParts(iwrb, entityPart, expertMode).iterator();
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


  private ICFile getFile(int fileId) throws FinderException {
    try {
      ICFileHome home = (ICFileHome) IDOLookup.getHome(ICFile.class);
      ICFile file = (ICFile) home.findByPrimaryKey(new Integer(fileId));
      return file;
    }
    catch(RemoteException ex){
      throw new RuntimeException("[ReportBusiness]: Message was: " + ex.getMessage());
    }
  }     
  
  
  private ICFile getFile(String name)	{
  	try {
      ICFileHome home = (ICFileHome) IDOLookup.getHome(ICFile.class);
      ICFile file = (ICFile) home.findByFileName(name);
      return file;
    }
    catch(RemoteException ex){
      throw new RuntimeException("[ReportBusiness]: Message was: " + ex.getMessage());
    }
    catch (FinderException ex) {
			return null;
		}
  }	
 
  

	public UserBusiness getUserBusiness()	{
		try {
			return (UserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), UserBusiness.class);
		}
		catch (RemoteException ex)	{
      System.err.println("[ReportOverview]: Can't retrieve UserBusiness. Message is: " + ex.getMessage());
      throw new RuntimeException("[ReportOverview]: Can't retrieve UserBusiness");
		}
	}

	public GroupBusiness getGroupBusiness()	{
		try {
			return (GroupBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), GroupBusiness.class);
		}
		catch (RemoteException ex)	{
      System.err.println("[ReportOverview]: Can't retrieve GroupBusiness. Message is: " + ex.getMessage());
      throw new RuntimeException("[ReportOverview]: Can't retrieve GroupBusiness");
		}
	}
	
		


}
