/*
 * Created on May 21, 2003
 *
 * QueryBuilder is a wizard that constructs a ReportQuery from the user input.
 */
package com.idega.block.dataquery.presentation;
//import java.awt.Color;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.dataquery.business.QueryService;
import com.idega.block.dataquery.business.QuerySession;
import com.idega.block.dataquery.data.QueryConstants;
import com.idega.block.dataquery.data.QueryRepresentation;
import com.idega.block.dataquery.data.UserQuery;
import com.idega.block.dataquery.data.xml.QueryBooleanExpressionPart;
import com.idega.block.dataquery.data.xml.QueryConditionPart;
import com.idega.block.dataquery.data.xml.QueryEntityPart;
import com.idega.block.dataquery.data.xml.QueryFieldPart;
import com.idega.block.dataquery.data.xml.QueryHelper;
import com.idega.block.dataquery.data.xml.QueryOrderConditionPart;
import com.idega.block.dataquery.data.xml.QueryPart;
import com.idega.block.dataquery.data.xml.QueryXMLConstants;
import com.idega.business.IBOLookup;
import com.idega.business.InputHandler;
import com.idega.core.data.ICTreeNode;
import com.idega.core.data.IWTreeNode;
import com.idega.data.EntityRepresentation;
import com.idega.data.IDOStoreException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.help.presentation.Help;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.AbstractTreeViewer;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.RadioGroup;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.SelectionDoubleBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.TreeViewer;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.util.datastructures.HashMatrix;

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
	protected IWResourceBundle iwrb = null;
	protected QueryHelper helper = null;
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
	public static final String PARAM_SAVE_MODE ="save_mode";
	private static final String PARAM_SET_EXPRESSION = "setExpression";
	/** disabled input handler chooser (don't delete this code) 
	private static final String PARAM_SET_HANDLER = "setHandler";
	*/
	private static final String PARAM_DYNAMIC = "dynamic";
//public static final String PARAM_QUIT = "quit";
	private static final String PARAM_QUERY_AS_SOURCE = "source_query";
	private static final String PARAM_SOURCE = "source_entity";
	private static final String PARAM_RELATED = "related_entity";
	private static final String PARAM_FIELDS = "entity_fields";
	private static final String PARAM_ORDER_FIELDS = "order_fields";
	private static final String PARAM_ORDER_FUNCTION = "order_function";
	private static final String PARAM_DISTINCT = "distinct";
	private static final String PARAM_QUERY_DESCRIPTION = "query_description";
	public static final String PARAM_LAYOUT_FOLDER_ID ="qb_layoutId";
	public static final String PARAM_QUERY_ID = "qb_qid";
	public static final String PARAM_QUERY_NAME = "q_name";
	private static final String PERM_TEMPL_EDIT = "template";
	private static final String PERM_CREATE = "create";
	private static final String PARAM_LOCK = "lock";
	private static final String PARAM_FUNCTION = "mkfunction";
	// parameters used for field selection step (step 3)
	private static final String PARAM_DISPLAY = "display";
	
	// parameters used for condition step (step 5)
	// input fields
	private static final String PARAM_COND_PATTERN = "field_pattern";
	private static final String PARAM_COND_TYPE = "field_type";
	private static final String PARAM_COND_FIELD = "field";
	private static final String PARAM_COND_DESCRIPTION = "cond_description";
	private static final String PARAM_COND_FIELD_AS_CONDITION = "setFieldsAsDynamicPattern";
	private static final String PARAM_COND_BOOLEAN_EXPRESSION = "booleanExpression";
	
	private static final String PARAM_COND_SET_ON_CHANGE_PARAMETER = "setOnChaPara";
	private static final String PARAM_COND_SET_ON_CHANGE_NOT_CLICKED = "setOnChaNotClicked";
	private static final String PARAM_COND_SET_ON_CHANGE_CLICKED = "setOnChaClicked";
	private static final String SET_ON_CHANGE_SCRIPT = "this.form."+PARAM_COND_SET_ON_CHANGE_PARAMETER+".value='"+PARAM_COND_SET_ON_CHANGE_CLICKED+"';this.form.submit()";
	
	// buttons
	private static final String PARAM_COND_ADD = "add";
	private static final String PARAM_COND_DROP = "drop";
	private static final String PARAM_COND_EDIT_START = "edit_condition";
	private static final String PARAM_COND_EDIT_SAVE = "save_edit_condition";
	private static final String PARAM_COND_EDIT_CANCEL = "cancel_edit_condition";
	private static final String PARAM_COND_EDIT_MODE = "edit_mode_condition";
	
	private static final String VALUE_DO_NOT_USE_FIELD_AS_CONDITION = "do_not_use_a_field";
	

	
	
	public static final int MAX_INVESTIGATIONS_LEVEL = 6;
	public static final String DEFAULT_PATTERN = "";
	public static final int NEW_INSTANCE = -1;
	
	private static final String PARAM_IS_PRIVATE_QUERY = "param_private_query";
	
	private static final String PRIVATE = "private";
	private static final String PUBLIC = "public";
	private static final String SAVE_AS_NEW_QUERY = "save_as_new_query";
	private static final String OVERWRITE_QUERY = "overwrite_query";
	
	private int heightOfStepTable = 300;
	private int step = 1;
	private int queryFolderID = -1;
	private String layoutFolderID;
	private int userQueryID = -1;

	private int investigationLevel = 6;
	private int tableBorder = 0;

	private String stepTableColor = "#ffffff";

	private String stepFontStyle = "headingFont";
	private String messageFontStyle = "messageFont";
	private boolean allowEmptyConditions = true;
	private boolean showSourceEntityInSelectBox = true;
	private boolean showQueries = true;
	private boolean allowFunctions = true;
	private int editId = NEW_INSTANCE;
	private QueryService queryService = null;
	private QuerySession sessionBean;
	private Form form = null;

	
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
	
	
	public void control(IWContext iwc) throws IDOStoreException, CreateException, SQLException, FinderException {
		if (hasEditPermission || hasTemplatePermission || hasCreatePermission) {
			try {

				if (iwc.isParameterSet(PARAM_QUERY_ID)) {
					userQueryID = Integer.parseInt(iwc.getParameter(PARAM_QUERY_ID));
				}

				sessionBean = (QuerySession) IBOLookup.getSessionInstance(iwc, QuerySession.class);
				if (userQueryID > 0) {
					sessionBean.setUserQueryID(userQueryID);
				}
				helper = sessionBean.getQueryHelper(iwc);
				if (iwc.isParameterSet(SHOW_WIZARD))	{
					investigationLevel = Integer.parseInt(iwc.getParameter(SHOW_WIZARD));
					// if we are editing an existing query that has an entity choose the maximum investigation level
					if (helper.hasSourceEntity()) {
						investigationLevel = MAX_INVESTIGATIONS_LEVEL;
					}
					expertMode = (investigationLevel > -1);
				}

				
				
				
				step = iwc.isParameterSet(PARAM_STEP) ? Integer.parseInt(iwc.getParameter(PARAM_STEP)) : 1;
/*---------------------------------------------------------------------------------------------------
				// if not moving around we stay at entity tree
				if (iwc.isParameterSet("tree_action"))
					step = 2;
				else
					step =
						iwc.isParameterSet(PARAM_STEP)
							? Integer.parseInt(iwc.getParameter(PARAM_STEP))
							: helper.getStep();
				step = step == 0 ? 1 : step;
------------------------------------------------------------------------------------------------------*/				
				//System.err.println("djokid");
				//System.err.println("helper step is " + helper.getStep());
				//System.err.println("this step is before process" + step);+
				processForm(iwc);
				//System.err.println("this step is after process" + step);
				Table table = new Table();
				table.setStyleClass("main");
				form = new Form();
				// thomas changed: queryFolder  id is always set
				// if (queryFolderID > 0 && step < 5)
				if (userQueryID > 0) {
					form.addParameter(PARAM_QUERY_ID, userQueryID);
				}
				table.mergeCells(1,1,2,1);
				table.mergeCells(1,2,2,2);
				table.add(getHelpTable(step,expertMode),1,3);
				table.add(getButtons(step), 2, 3);
				form.addParameter(PARAM_STEP, step);
				// thomas added:
				// this parameter serves as a flag for the outer window to continue showing the wizard
				// the outer window checks also if PARAM_CANCEL or PARAM_QUIT exist
				form.addParameter(SHOW_WIZARD, Integer.toString(investigationLevel));
				if (iwc.isParameterSet(PARAM_LAYOUT_FOLDER_ID)) {
					layoutFolderID = iwc.getParameter(PARAM_LAYOUT_FOLDER_ID);
					form.addParameter(PARAM_LAYOUT_FOLDER_ID, layoutFolderID);
				}

				table.setWidth("300");
				table.setBorder(tableBorder);
				table.setColor(stepTableColor);
				//table.setStyleClass(stepTableStyle);
				table.setColor(1, 1, "#FFFFFF");

				Table headerTable = new Table(2, 2);
				headerTable.setWidth(Table.HUNDRED_PERCENT);
				// added by thomas
				// skip the second step
				int displayStep = step;
				if (! expertMode) {
					displayStep = (step == 1)? 1 : step -1;
				}
				String queryName = (userQueryID > 0) ? helper.getUserQuery().getName() : iwrb.getLocalizedString("step_all_new_query", "New Query");
				Text queryNameText = new Text(queryName);
				queryNameText.setBold();
				StringBuffer buffer = new StringBuffer(iwrb.getLocalizedString("step", "Step"));
				buffer.append(' ').append(displayStep);
				Text stepText = getStepText(buffer.toString());
				headerTable.add(stepText, 1, 1);
				headerTable.setAlignment(1,1, Table.HORIZONTAL_ALIGN_LEFT);
				headerTable.add(queryNameText, 2, 1);
				headerTable.setAlignment(2,1, Table.HORIZONTAL_ALIGN_RIGHT);
				headerTable.mergeCells(1, 2, 2, 2);
				headerTable.add(getMsgText(getStepMessage()), 1, 2);
				headerTable.setAlignment(1, 2, Table.HORIZONTAL_ALIGN_LEFT);

				table.add(headerTable, 1, 1);

				table.add(getStep(iwc), 1, 2);
				table.setColor(1, 2, stepTableColor);
				table.setVerticalAlignment(1, 2, Table.VERTICAL_ALIGN_TOP);
				table.setAlignment(2, 3, Table.HORIZONTAL_ALIGN_RIGHT);
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
	private void processForm(IWContext iwc) throws IDOStoreException, IOException, CreateException, SQLException, RemoteException, FinderException {
/** disabled input handler chooser (don't delete that code) (start)
 *  search this file for "InputHandlerChooser".
		// first of all set new handler classes
		if (iwc.isParameterSet(PARAM_SET_HANDLER)) {
			String handlerClass = InputHandlerChooser.parseInputHandler(iwc);
			if (handlerClass != null && iwc.isParameterSet(PARAM_COND_FIELD)) {
				String field = iwc.getParameter(PARAM_COND_FIELD);
				QueryFieldPart fieldPart = QueryFieldPart.decode(field);
				helper.setInputHandler(fieldPart.getPath(), fieldPart.getName(), handlerClass);
			}
		}
		disabled input handler chooser (end)
*/
//		//		destroy sessionbean and close window if set
//		if (iwc.isParameterSet(PARAM_QUIT)) {
//			//if(closeParentWindow)
//			// try to close parent window
//			// thomas: replaced by staic method
//			// IBOLookup.removeSessionInstance(iwc, QuerySession.class);
//			ReportQueryBuilder.cleanSession(iwc);
//			step = 1;
//		}
		if (iwc.isParameterSet(PARAM_CANCEL)) {
			helper.clearAll();
			step = 1;
		}
		else if (iwc.isParameterSet(PARAM_FINAL)) {
			boolean proceed = processNextStep(iwc);
			if (proceed) {
				step = 6;
			}
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
		// handle step 5, conditions
		else if (iwc.isParameterSet(PARAM_COND_ADD)) {
			saveCondition(iwc);
		}
		else if (iwc.isParameterSet(PARAM_COND_EDIT_START)) {
			editId = Integer.parseInt(iwc.getParameter(PARAM_COND_EDIT_START));
		}
		else if (iwc.isParameterSet(PARAM_COND_EDIT_SAVE)) {
			editId = Integer.parseInt(iwc.getParameter(PARAM_COND_EDIT_SAVE));
			saveCondition(iwc);
			editId = NEW_INSTANCE;
		}
		else if (iwc.isParameterSet(PARAM_COND_EDIT_CANCEL)) {
			editId = NEW_INSTANCE;
		}
		else if (iwc.isParameterSet(PARAM_COND_DROP)) {
			int dropId = Integer.parseInt(iwc.getParameter(PARAM_COND_DROP));
			if (helper.hasConditions()) {
				List conditions = helper.getListOfConditions();
				for (int i = 0; i < conditions.size(); i++) {
					QueryConditionPart element = (QueryConditionPart) conditions.get(i);
					if (element.getIdNumber() == dropId) {
						conditions.remove(i);
					}
				}
				// update boolean expression
				processBooleanExpression(iwc);
				editId = NEW_INSTANCE;
			}
		}
		else if (iwc.isParameterSet(PARAM_COND_EDIT_MODE)) {
			editId = Integer.parseInt(iwc.getParameter(PARAM_COND_EDIT_MODE));
		}
		else if (iwc.isParameterSet(PARAM_SAVE)) {
			storeQuery(iwc);
		}
		else if (iwc.isParameterSet(PARAM_NEXT)) {
			boolean proceed = processNextStep(iwc);
			if (proceed) {
				// in the simple mode skip the second step
				// in the expert mode skip the second step when no source entity was chosen
				if ((! expertMode && step == 1) || 
						(expertMode && step == 1 && ! helper.hasSourceEntity())) {
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
//		else if (iwc.isParameterSet(PARAM_SAVE)) {
//			processStep5(iwc);
//			//else
//			//add("Query was not saved");
//		}

	}

	/**
	 * @param iwc
	 * @throws RemoteException
	 * @throws IOException
	 * @throws CreateException
	 * @throws SQLException
	 * @throws FinderException
	 */
	private void storeQuery(IWContext iwc) throws RemoteException, IOException, CreateException, SQLException, FinderException {
		String description = iwc.getParameter(PARAM_QUERY_DESCRIPTION);
		helper.setDescription(description);
		helper.setTemplate(iwc.isParameterSet(PARAM_ASTEMPLATE));
		String name = iwc.getParameter(PARAM_QUERY_NAME);
		String saveMode = iwc.getParameter(PARAM_SAVE_MODE);
		boolean isPrivate = PRIVATE.equals(iwc.getParameter(PARAM_IS_PRIVATE_QUERY));
		boolean overwriteQuery = OVERWRITE_QUERY.equals(saveMode);
		if (name == null) {
			name = iwrb.getLocalizedString("step_6_default_name", "My query");
		}
		UserQuery userQuery = sessionBean.storeQuery(name, isPrivate, overwriteQuery);
		if (userQuery != null) {
			userQueryID = ((Integer) userQuery.getPrimaryKey()).intValue();
			helper.setUserQuery(userQuery);
		}
	}

	private boolean processNextStep(IWContext iwc) throws NumberFormatException, RemoteException, FinderException, IOException  {
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

	private void saveCondition(IWContext iwc) { 
		String field = iwc.getParameter(PARAM_COND_FIELD);
		String equator = iwc.getParameter(PARAM_COND_TYPE);
		String description = iwc.getParameter(PARAM_COND_DESCRIPTION);
		QueryFieldPart fieldPart = QueryFieldPart.decode(field);
		helper.addHiddenField(fieldPart);
		QueryConditionPart part = null; 
		if (editId == NEW_INSTANCE) {
			part = new QueryConditionPart();
			int id = helper.getNextIdForCondition();
			part.setIdUsingPrefix(id);
		}
		else {
			if (helper.hasConditions()) {
				List conditions = helper.getListOfConditions();
					for (int i = 0; i < conditions.size(); i++) {
					QueryConditionPart element = (QueryConditionPart) conditions.get(i);
					if (element.getIdNumber() == editId) {
						part = element;
					}
				}
			}
		}
		part.setEntity(fieldPart.getEntity());
		part.setPath(fieldPart.getPath());
		part.setField(fieldPart.getName());
		part.setType(equator);
		part.setDescription(description);
		if (isFieldAsPatternSet(iwc)) {
			// ignore user settings if a pattern as field was chosen
			part.setDynamic(false);
			setFieldAsPatternByParsing(iwc, part);
		}
		else {
			part.setDynamic(iwc.isParameterSet(PARAM_DYNAMIC));
			setPatternByParsing(iwc, part);
		}
		helper.addCondition(part);
		processBooleanExpression(iwc);
	}
	
	/** 
	 * @param iwc
	 * @param part
	 */
	private void setPatternByParsing(IWContext iwc, QueryConditionPart part) {
		// call method isFieldAsPatternSet before calling this method
		String[] patterns = iwc.getParameterValues(PARAM_COND_PATTERN);
		if (patterns == null || "".equals(patterns)){
			part.setPattern(DEFAULT_PATTERN);
		}
		else if (patterns.length == 1) {
			part.setPattern(patterns[0]);
		}
		else { 
			// change to collection-based API
			List patternsAsList = Arrays.asList(patterns);
			part.setPatterns(patternsAsList);
		}
	}
		
	/** Returns true if the a field is used as pattern else false */
	private boolean isFieldAsPatternSet(IWContext iwc) {
		String fieldAsPattern = iwc.getParameter(PARAM_COND_FIELD_AS_CONDITION);
		return !(fieldAsPattern == null || VALUE_DO_NOT_USE_FIELD_AS_CONDITION.equals(fieldAsPattern)); 
	}
		
	private void setFieldAsPatternByParsing(IWContext iwc, QueryConditionPart part) {
		// call method isFieldAsPatternSet before
		// field is used as pattern
		String fieldAsPattern = iwc.getParameter(PARAM_COND_FIELD_AS_CONDITION);
		QueryFieldPart queryFieldAsPattern = QueryFieldPart.decode(fieldAsPattern);
		helper.addHiddenField(queryFieldAsPattern);
		part.setPatternPath(queryFieldAsPattern.getPath());
		part.setPatternField(queryFieldAsPattern.getName());
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
		if (iwc.isParameterSet(PARAM_COND_BOOLEAN_EXPRESSION)) {
			booleanExpression = iwc.getParameter(PARAM_COND_BOOLEAN_EXPRESSION);
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
		helper.setSelectDistinct(iwc.isParameterSet(PARAM_DISTINCT));

		String[] fields = null;
		if (iwc.isParameterSet(PARAM_FIELDS)) {
			fields = iwc.getParameterValues(PARAM_FIELDS);
		}
		// allow to select from the left box only ( no ordering ), shortcut !
		else if (iwc.isParameterSet(PARAM_FIELDS + "_left")) {
			fields = iwc.getParameterValues(PARAM_FIELDS + "_left");
		}
		return setFields(fields);
	}
	
	private boolean processStep4(IWContext iwc) {
		String[] orderConditions = null;
		if (iwc.isParameterSet(PARAM_ORDER_FIELDS))	{
			orderConditions = iwc.getParameterValues(PARAM_ORDER_FIELDS);
		}
		else if (iwc.isParameterSet(PARAM_ORDER_FIELDS + "_left"))	{
			orderConditions = iwc.getParameterValues(PARAM_ORDER_FIELDS + "_left");
		}
		return setOrderConditions(orderConditions);	
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
		return helper.hasSourceEntity();
	}
	private boolean processStep1(IWContext iwc) throws NumberFormatException, RemoteException, FinderException, IOException {
		if (iwc.isParameterSet(PARAM_QUERY_AS_SOURCE))	{
			String[] userQueryId = iwc.getParameterValues(PARAM_QUERY_AS_SOURCE);
			for (int i = 0; i < userQueryId.length; i++) {
				QueryHelper queryHelperAsSource = sessionBean.getQueryService().getQueryHelper(Integer.parseInt(userQueryId[i]), iwc);
				helper.addQuery(queryHelperAsSource);
			}
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
			return helper.hasSourceEntity()  || helper.hasPreviousQuery();
		}
		return helper.hasPreviousQuery();
	}
	

	
	private boolean processFunction(IWContext iwc){
		//++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		String[] fields = null;
		if (iwc.isParameterSet(PARAM_FIELDS)) {
			fields = iwc.getParameterValues(PARAM_FIELDS);
		}
		setFields(fields);
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++	
		String type = iwc.getParameter(PARAM_FUNCTION);
		fields = iwc.getParameterValues(PARAM_FIELDS+"_left");
		String display = iwc.getParameter(PARAM_DISPLAY);
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
//					String partDisplay = newPart.getDisplay();
//					for(int i=1;i<dfields.size();i++){
//						QueryFieldPart part = (QueryFieldPart) dfields.get(i);
//						// if from same entity, allow concat
//						if(newPart.getEntity().equals(part.getEntity())){
//							newPart.addColumn(part.getColumns());
//							partDisplay+=","+ part.getDisplay();
//						}
//					}
//					newPart.setFunction(type);
//					if(display!=null && display.length()>0){
//						newPart.setDisplay(display);
//					}
//					else{
//						newPart.setDisplay(type+"("+partDisplay+")");
//					}
//					helper.addField(newPart);
//					
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
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		String[] orderConditions = null;
		if (iwc.isParameterSet(PARAM_ORDER_FIELDS))	{
			orderConditions = iwc.getParameterValues(PARAM_ORDER_FIELDS);
		}
		setOrderConditions(orderConditions);
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		String[] fields = iwc.getParameterValues(PARAM_ORDER_FIELDS+"_left");
		if(fields !=null ){
			for (int i = 0; i < fields.length; i++) {
				String field = fields[i];
				System.out.println(field);
				QueryFieldPart fieldPart = QueryFieldPart.decode(field);
				helper.addHiddenField(fieldPart);
				QueryOrderConditionPart	conditionPart = new QueryOrderConditionPart(fieldPart.getEntity(),fieldPart.getPath(),fieldPart.getName());
				conditionPart.setDescendant(true);
				helper.addOrderCondition(conditionPart);
			}
		}
		return false;
	}
	
	private boolean setOrderConditions(String[] orderConditions) {
		helper.clearOrderConditions();
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
					conditionPart = new QueryOrderConditionPart(fieldPart.getEntity(),fieldPart.getPath(),fieldPart.getName());
				}
				if (conditionPart != null) {
					helper.addOrderCondition(conditionPart);
				}
			}
		}
		// order conditions are not mandatory
		return true;
	}
	
	private boolean setFields(String[] fields) {
		helper.clearFields();
		if (fields != null) {
			for (int i = 0; i < fields.length; i++) {
				QueryFieldPart part = QueryFieldPart.decode(fields[i]);
				if (part != null)
					helper.addField(part);
			}
//LOCK			helper.setFieldsLock(iwc.isParameterSet(PARAM_LOCK));
			return helper.hasFields();
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
			// in the simple mode skip the second step
			// in the expert mode skip the second step when no source entity was chosen	
				processStep3(iwc);
				if (! expertMode || (expertMode && ! helper.hasSourceEntity())) {
					step--;
				}
				//helper.clearRelatedEntities();
				break;
			case 4 :
				processStep4(iwc);
				//helper.clearFields();
				break;
				// we are coming from the finish step, so maybe we have to 
				// set it to some other step than the previous to the  current
			case 5 :
				processStep5(iwc);
				//System.out.println("helper step is " + helper.getStep());
				//step = helper.getStep() + 1;
				//helper.clearConditions();
				break;
		}
		step--;
		//step = helper.getStep()-1;
	}
	public PresentationObject getStep(IWContext iwc) throws RemoteException, FinderException {
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
				return getStep6();
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
		return T;
	}

	public PresentationObject getStep1(IWContext iwc) throws RemoteException, FinderException {

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
			select.setMultiple(true);
			select.setMaximumChecked(1, iwrb.getLocalizedString("maximum_select_msg", "Select only one"));
			select.setHeight("20");
			select.setWidth("300");
			// get queries
			Collection queries = getQueryService(iwc).getQueries(iwc);
			Iterator iterator = queries.iterator();
			while (iterator.hasNext()) {
				EntityRepresentation representation = (EntityRepresentation) iterator.next();
				String id = representation.getPrimaryKey().toString();
				String name = (String) representation.getColumnValue(QueryRepresentation.NAME_KEY);
				String groupName = (String) representation.getColumnValue(QueryRepresentation.GROUP_NAME_KEY);
				StringBuffer displayName = new StringBuffer(groupName).append(" - ").append(name);
				String isPrivate = (String) representation.getColumnValue(QueryRepresentation.IS_PRIVATE_KEY);
				if (isPrivate.length() != 0) {
					displayName.append(" - ");
					displayName.append(iwrb.getLocalizedString("query_builder_private", "private"));
				}
				// do not show the query that you are editing in the drop down menu
				if (! id.equals(Integer.toString(userQueryID))) {
					select.addMenuElement(id, displayName.toString());
				}
			}
  		if (helper.hasPreviousQuery()) {
  			List previousQueries = helper.previousQueries();
  			Iterator previousIterator = previousQueries.iterator();
  			while (previousIterator.hasNext()) {
  				QueryHelper previousQuery = (QueryHelper) previousIterator.next();
  				UserQuery userQuery = previousQuery.getUserQuery();
  				if (userQuery != null) {
  					String id = userQuery.getPrimaryKey().toString();
  					select.setSelectedElement(id);
  				}
  			}
  		}
			table.add(select, 1, (row == 1) ? 1 : row - 1 );
		}
		if (hasTemplatePermission) {
			CheckBox lockCheck = new CheckBox(PARAM_LOCK, "true");
			if (helper.hasSourceEntity())
				lockCheck.setChecked(helper.getSourceEntity().isLocked());
//LOCK			table.add(getMsgText(iwrb.getLocalizedString("lock_source_entity", "Lock source entity")), 1, row);
//LOCK			table.add(lockCheck, 1, row);
		}

		return table;
	}
	public PresentationObject getStep2(IWContext iwc) throws RemoteException {
		Table table = getStepTable();
		//table.add(getRelatedChoice(iwc),1,2);
		IWTreeNode root = getQueryService(iwc).getEntityTree(helper, investigationLevel);
		EntityChooserTree tree = new EntityChooserTree(root);

		tree.setUI(AbstractTreeViewer._UI_WIN);
		Link treeLink = new Link();
		treeLink.addParameter(PARAM_STEP, step);
		treeLink.addParameter(SHOW_WIZARD, Integer.toString(investigationLevel));
		treeLink.addParameter(PARAM_LAYOUT_FOLDER_ID, layoutFolderID);
		tree.setLinkOpenClosePrototype(treeLink);
		tree.addOpenCloseParameter(PARAM_STEP, Integer.toString(step));
		tree.addOpenCloseParameter(SHOW_WIZARD, Integer.toString(investigationLevel));
		tree.addOpenCloseParameter(PARAM_LAYOUT_FOLDER_ID, layoutFolderID);
		tree.setInitOpenLevel();
		tree.setNestLevelAtOpen(1);
		//tree.setToMaintainParameter(PARAM_STEP,iwc);
		//viewer.setNestLevelAtOpen(4);
		table.add(tree, 1, 1);
		if (hasTemplatePermission) {
			CheckBox lockCheck = new CheckBox(PARAM_LOCK, "true");
			lockCheck.setChecked(helper.isEntitiesLock());
//LOCK			table.add(getMsgText(iwrb.getLocalizedString("lock_related_entities", "Lock entities")), 1, 2);
//LOCK			table.add(lockCheck, 1, 2);
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
		
		fillFieldsFromPreviousQuery(fieldMap, box);
		
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
//LOCK			table.add(getMsgText(iwrb.getLocalizedString("lock_fields", "Lock fields")), 2, row);
//LOCK			table.add(lockCheck, 2, row);
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
		fillFieldsFromPreviousQuery(fieldMap, box);

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
//LOCK			table.add(getMsgText(iwrb.getLocalizedString("lock_fields", "Lock fields")), 2, row);
//LOCK			table.add(lockCheck, 2, row);
		}
		
		
		return table;
	}
	
	
	
	
	private PresentationObject getFunctionTable(){
		Table table = new Table(9,2);
		int col = 1;
		table.mergeCells(1,1,8,1);
		table.add(getMsgText(iwrb.getLocalizedString("step_3_choose_function","Select function to apply on selected fields in the left box, and new display name if required")),1,1);
		TextInput display = new TextInput(PARAM_DISPLAY);
		SubmitButton count = new SubmitButton(iwrb.getLocalizedImageButton("btn_func.count","Count"),PARAM_FUNCTION,QueryXMLConstants.FUNC_COUNT);
		SubmitButton count_distinct = new SubmitButton(iwrb.getLocalizedImageButton("btn_func.count_distinct", "Count distinct"), PARAM_FUNCTION, QueryXMLConstants.FUNC_COUNT_DISTINCT);
		// concat not supported yet
		//SubmitButton concat = new SubmitButton(iwrb.getLocalizedImageButton("btn_func.concat","Concat"),PARAM_FUNCTION,QueryXMLConstants.FUNC_CONCAT);
		SubmitButton max = new SubmitButton(iwrb.getLocalizedImageButton("btn_func.max","Max"),PARAM_FUNCTION,QueryXMLConstants.FUNC_MAX);
		SubmitButton min = new SubmitButton(iwrb.getLocalizedImageButton("btn_func.min","Min"),PARAM_FUNCTION,QueryXMLConstants.FUNC_MIN);
		SubmitButton sum = new SubmitButton(iwrb.getLocalizedImageButton("btn_func.sum","Sum"),PARAM_FUNCTION,QueryXMLConstants.FUNC_SUM);
		SubmitButton avg = new SubmitButton(iwrb.getLocalizedImageButton("btn_func.avg","Avg"),PARAM_FUNCTION,QueryXMLConstants.FUNC_AVG);
		SubmitButton alias = new SubmitButton(iwrb.getLocalizedImageButton("btn_func.alias","Alias"),PARAM_FUNCTION,QueryXMLConstants.FUNC_ALIAS);
		//distinct checkbox
		Table distinctTable = new Table(2,1);
		CheckBox distinct = new CheckBox(PARAM_DISTINCT);
		distinct.setChecked(helper.isSelectDistinct());
		distinctTable.add(iwrb.getLocalizedString("btn_func.distinct","Select distinct"),1,1);
		distinctTable.add(distinct,2,1);

		table.add(display,col++,2);
		table.add(count,col++,2);
		table.add(count_distinct, col++,2);
//		table.add(concat,col++,2);
		table.add(max,col++,2);
		table.add(min,col++,2);
		table.add(sum, col++,2);
		table.add(avg,col++,2);
		table.add(alias,col++,2);
		table.add(distinctTable, col++,2);
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
				getDisplay(part));
			}
		}
	}

	// fills the right and the left list of the specified box depending on values set in fieldMap
	// values are retrieved from the specified choiceFields
	private void fillFieldSelectionBox(
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
				box.getLeftBox().addElement(part.encode(), getDisplay(part));
			}
		}
	}
	
	
	private void fillFieldSelectionBox(
		List choiceFields,
		SelectionDoubleBox box) {
		Iterator iter = choiceFields.iterator();
		while (iter.hasNext()) {
			QueryFieldPart part = (QueryFieldPart) iter.next();
			box.getRightBox().addElement(part.encode(), getDisplay(part));
		}
	}
	
		// used by step 4 (order by)
	private void fillFieldSelectionBoxForOrder(
		List choiceFields,
		SelectionDoubleBox box) {
		//System.out.println("filling box with fields from " + entityPart.getName());
		Iterator iter = choiceFields.iterator();
		while (iter.hasNext()) {
			QueryOrderConditionPart part = (QueryOrderConditionPart) iter.next();
			//System.out.println(" " + part.getName());
			String entityName = part.getEntity();
			String fieldName = part.getField();
			StringBuffer buffer = new StringBuffer(iwrb.getLocalizedString(entityName, entityName));
			buffer.append(" -> ");
			buffer.append(iwrb.getLocalizedString(fieldName, fieldName));
			buffer.append(" (");
			buffer.append(fieldName);
			buffer.append(") ");
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
		HashMatrix mapOfFields = getMapOfFields();
		int row = 1;
		addHeaderToConditionTable(table, row++);
		List conditions = null;
		if (helper.hasConditions()) {
			conditions = helper.getListOfConditions();
			for (Iterator iter = conditions.iterator(); iter.hasNext();) {
				QueryConditionPart part = (QueryConditionPart) iter.next();
				if (editId == part.getIdNumber() && ! iwc.isParameterSet(PARAM_COND_EDIT_START)) {
					addInputForNewInstanceToConditionTable(iwc, table, row, mapOfFields, editId);
				}
				else {
					addEntryToConditionTable(iwc, table, row, part, mapOfFields, null);
				}
				addButtonsToConditionTable(table, row, part);
				row = (editId == part.getIdNumber()) ? row+ 2 : row + 1;
			}
		}
		table.add(Text.getBreak(), 1, row++);
		if (editId == NEW_INSTANCE) {
			addInputForNewInstanceToConditionTable(iwc, table, row, mapOfFields, editId);
			table.add(new SubmitButton(iwrb.getLocalizedImageButton("add", "Add"), PARAM_COND_ADD), 8, row);
		}
		// set fields as source
		row++;
		row = addBooleanExpressionToConditionTable(table, row);
/** disabled input handler chooser (don't delete that code) (start)
 * 	search this file for "InputHandlerChooser".
		// add input handler chooser
//		if (expertMode) {
//			row++;
//			InputHandlerChooser inputHandlerChooser = new InputHandlerChooser();
//			if (fieldPart != null) {
//				inputHandlerChooser.setField(fieldPart.getName());
//				inputHandlerChooser.setEntity(fieldPart.getEntity());
//			}
//			table.mergeCells(3, row, 6, row);
//			table.add(inputHandlerChooser, 3, row);
//			table.add(new SubmitButton(iwrb.getLocalizedImageButton("Set handler", "Set handler"), PARAM_SET_HANDLER, PARAM_SET_HANDLER),7 ,row);
//		}
 *  disabled input handler chooser (end)
 */
		return table;
	}

	/**
	 * @param iwc
	 * @param table
	 * @param row
	 * @param mapOfFields
	 * @throws RemoteException
	 */
	private void addInputForNewInstanceToConditionTable(IWContext iwc, Table table, int row, HashMatrix mapOfFields, int localEditId) throws RemoteException {
		Map temporaryParameterMap = new HashMap(1);
		QueryConditionPart newInstance = new QueryConditionPart();
		if (	! iwc.isParameterSet(PARAM_COND_EDIT_SAVE) && 
					! iwc.isParameterSet(PARAM_COND_EDIT_CANCEL) &&
					! iwc.isParameterSet(PARAM_COND_DROP) &&
					! iwc.isParameterSet(PARAM_COND_ADD)) {
			String equator = iwc.getParameter(PARAM_COND_TYPE);
			String description = iwc.getParameter(PARAM_COND_DESCRIPTION);
			newInstance.setDescription(description);
			newInstance.setType(equator);
			newInstance.setDynamic(iwc.isParameterSet(PARAM_DYNAMIC));
			if (! isFieldAsPatternSet(iwc)) {
				setPatternByParsing(iwc, newInstance);
			}
			temporaryParameterMap.put(PARAM_COND_FIELD, iwc.getParameter(PARAM_COND_FIELD));
			temporaryParameterMap.put(PARAM_COND_FIELD_AS_CONDITION, iwc.getParameter(PARAM_COND_FIELD_AS_CONDITION));
		}
		newInstance.setIdUsingPrefix(localEditId);
		addEntryToConditionTable(iwc, table, row, newInstance, mapOfFields, temporaryParameterMap);
	}

	/**
	 * @param iwc
	 * @param table
	 * @param row
	 * @param part
	 * @param mapOfFields
	 * @throws RemoteException
	 */
	private void addEntryToConditionTable(IWContext iwc, Table table, int row, QueryConditionPart part, HashMatrix mapOfFields, Map temporaryParameterMap) throws RemoteException {
		addIdentifierToConditionTable(table, row, part);
		addFieldToConditionTable(iwc, table, row, part, mapOfFields, temporaryParameterMap);
		addCondtionTypeToCondtionTable(table, row, part);
		// display the pattern
		addPatternToConditionTable(iwc, table, row, part, mapOfFields, temporaryParameterMap);
		addDescriptionToConditionTable(table, row, part);
		addDynamicPropertyToConditionTable(table, row, part);
		addFieldAsPatternToConditionTable(iwc, table, row, part, mapOfFields, temporaryParameterMap);
	}

	/**
	 * @param table
	 * @param row
	 * @return
	 */
	private int addBooleanExpressionToConditionTable(Table table, int row) {
		// boolean expression
		QueryBooleanExpressionPart booleanExpressionPart = helper.getBooleanExpressionForConditions();
		String booleanExpression = "";
		if (booleanExpressionPart != null) {
			booleanExpression = (booleanExpressionPart.isSyntaxOfBooleanExpressionOkay()) ? 
			booleanExpressionPart.getBooleanExpression() :
			booleanExpressionPart.getBadSyntaxBooleanExpression();
		}
		TextInput textInput = new TextInput(PARAM_COND_BOOLEAN_EXPRESSION, booleanExpression);
		textInput.setLength(100);
		row++;
		table.mergeCells(3, row, 6, row);
		table.add(textInput, 3 , row); 
		table.add(new SubmitButton(iwrb.getLocalizedImageButton("Set expression", "Set expression"), PARAM_SET_EXPRESSION, PARAM_SET_EXPRESSION),3 ,row);
		row++;
		table.mergeCells(3, row, 6, row);
		StringBuffer buffer = new StringBuffer(iwrb.getLocalizedString("Example", "Example"));
		buffer.append(": ( Cond1 or Cond2 ) and ( Cond3 or ( not Cond4 ) )");
		table.add(buffer.toString(), 3, row);
		return row;
	}

	/**
	 * @param table
	 * @param row
	 * @param part
	 */
	private void addButtonsToConditionTable(Table table, int row, QueryConditionPart part) {
		String partId = Integer.toString(part.getIdNumber());
		if (editId == NEW_INSTANCE) {
			table.add(new SubmitButton(iwrb.getLocalizedImageButton("drop", "drop"), PARAM_COND_DROP, partId), 8,	row);
			table.add(new SubmitButton(iwrb.getLocalizedImageButton("step_5_edit_condition", "Edit"), PARAM_COND_EDIT_START, partId), 9, row);
		}
		else if (part.getIdNumber() == editId) {
			form.addParameter(PARAM_COND_EDIT_MODE, partId);
			table.add(new SubmitButton(iwrb.getLocalizedImageButton("step_5_save", "Save"), PARAM_COND_EDIT_SAVE, partId), 8, row);
			table.add(new SubmitButton(iwrb.getLocalizedImageButton("step_5_cancel", "Cancel"), PARAM_COND_EDIT_CANCEL, partId), 9, row);
		}
	}

	/**
	 * @param table
	 * @param row
	 * @param part
	 */
	private void addCondtionTypeToCondtionTable(Table table, int row, QueryConditionPart part) {
		String type = part.getType();
		if (part.getIdNumber() == editId) {
			DropdownMenu equators = getConditionTypeDropdown();
			if (type != null) {
				equators.setSelectedElement(type);
			}
			table.add(equators, 4, row);
		}
		else {
			table.add(iwrb.getLocalizedString(QueryConstants.LOCALIZATION_CONDITION_TYPE_PREFIX + part.getType(), part.getType()), 4, row);
		}
	}

	/**
	 * @param table
	 * @param row
	 * @param field
	 */
	private void addFieldToConditionTable(IWContext iwc, Table table, int row, QueryConditionPart part, HashMatrix mapOfFields, Map temporaryParameterMap) throws RemoteException {
		String path = part.getPath();
		String fieldName= part.getField();
		if (part.getIdNumber() == editId) {
			// add a hidden parameter to the form that says that the selection of the hasn't changed
			form.addParameter(PARAM_COND_SET_ON_CHANGE_PARAMETER, PARAM_COND_SET_ON_CHANGE_NOT_CLICKED);
			// change the value of the parameter if the selection has changed
			DropdownMenu chosenFields = getAvailableFieldsDropdown(iwc, PARAM_COND_FIELD);
			chosenFields.setOnChange(SET_ON_CHANGE_SCRIPT);
			// set the prior selected field
			String fieldEncoded = null;
			if (path != null && fieldName != null) {
				QueryFieldPart field = (QueryFieldPart) mapOfFields.get(path, fieldName);
				fieldEncoded = field.encode();
			}
			else {
				fieldEncoded = (temporaryParameterMap == null) ? null : (String) temporaryParameterMap.get(PARAM_COND_FIELD);
			}
			if (fieldEncoded != null) {
				chosenFields.setSelectedElement(fieldEncoded);
			}
			else {
				// put into the temporaryParameterMap to be able to set the right inputhandler
				String selectedFieldEncoded = chosenFields.getSelectedElementValue();
				temporaryParameterMap.put(PARAM_COND_FIELD, selectedFieldEncoded);
			}
			table.add(chosenFields, 3,row);
		}
		else {
			QueryFieldPart field = (QueryFieldPart) mapOfFields.get(path, fieldName);
			table.add(getDisplay(field), 3, row);
		}
	}
	
	private void addFieldAsPatternToConditionTable(IWContext iwc, Table table, int row, QueryConditionPart part, HashMatrix mapOfFields, Map temporaryParameterMap) throws RemoteException {
		String path = part.getPatternPath();
		String fieldName= part.getPatternField();
		if (part.getIdNumber() == editId) {
			DropdownMenu chosenFields = getAvailableFieldsDropdown(iwc, PARAM_COND_FIELD_AS_CONDITION);
			String doNotUseAField = iwrb.getLocalizedString("step_5_do_not_use_field","don't use a field");
			chosenFields.addMenuElementFirst(VALUE_DO_NOT_USE_FIELD_AS_CONDITION, doNotUseAField );
			// set the prior selected field
			String fieldEncoded = null;
			if (path != null && fieldName != null) {
				QueryFieldPart field = (QueryFieldPart) mapOfFields.get(path, fieldName);
				fieldEncoded = field.encode();
			}
			else {
				fieldEncoded = (temporaryParameterMap == null) ? null: (String) temporaryParameterMap.get(PARAM_COND_FIELD_AS_CONDITION);
			}
			if (fieldEncoded != null) {
				chosenFields.setSelectedElement(fieldEncoded);
			}
			table.add(chosenFields, 3 ,row + 1);
		}
		else if (path != null && fieldName != null) {
			QueryFieldPart field = (QueryFieldPart) mapOfFields.get(path, fieldName);
			table.add(getDisplay(field), 5 , row);
		}
	}

	/**
	 * @param table
	 * @param row
	 * @param part
	 */
	private void addIdentifierToConditionTable(Table table, int row, QueryConditionPart part) {
		// do not show the id number "-1"
		if (NEW_INSTANCE != part.getIdNumber()) {
			table.add(part.getId(), 2, row);
		}
	}

	/**
	 * @param table
	 * @param row
	 * @param part
	 */
	private void addDescriptionToConditionTable(Table table, int row, QueryConditionPart part) {
		String description = part.getDescription();
		if (part.getIdNumber() ==  editId) {
			TextInput descriptionInput = new TextInput(PARAM_COND_DESCRIPTION);
			descriptionInput.setValue(description);
			table.add(descriptionInput, 6, row);
		}
		else {
			table.add(part.getDescription(), 6, row);
		}
	}

	/**
	 * @param table
	 * @param row
	 * @param part
	 */
	private void addDynamicPropertyToConditionTable(Table table, int row, QueryConditionPart part) {
		boolean isDynamic =part.isDynamic();
		if (part.getIdNumber() == editId) {
			CheckBox dynamic = new CheckBox(PARAM_DYNAMIC);
			dynamic.setChecked(isDynamic);
			table.add(dynamic,7,row);
		}
		else if (isDynamic) {
			table.add("x", 7, row);
		}
	}

	/**
	 * @param iwc
	 * @param table
	 * @param row
	 * @param part
	 * @param fieldInputHandler
	 */
	private void addPatternToConditionTable(IWContext iwc, Table table, int row, QueryConditionPart part, HashMatrix mapOfFields, Map temporaryParameterMap) {
		String path = part.getPath();
		String field = part.getField();
		QueryFieldPart fieldPart = null;
		if (path == null && field == null) {
			String fieldEncoded = (temporaryParameterMap == null) ? null : (String) temporaryParameterMap.get(PARAM_COND_FIELD);
			fieldPart = (fieldEncoded == null) ? null : QueryFieldPart.decode(fieldEncoded);
		}
		else {
			fieldPart = (QueryFieldPart) mapOfFields.get(part.getPath(), part.getField());
		}
		boolean hasMoreThanOnePattern = part.hasMoreThanOnePattern();
		String singlePattern = (hasMoreThanOnePattern) ? null : part.getPattern();
		Collection patterns = (hasMoreThanOnePattern) ? part.getPatterns() : null;

		InputHandler inputHandler = (fieldPart ==null) ? null : getInputHandler(fieldPart);
		
		// display with editing options
		if (part.getIdNumber() == editId) {
				// if another field was chosen ignore the value of the pattern field
			if (PARAM_COND_SET_ON_CHANGE_CLICKED.equals(iwc.getParameter(PARAM_COND_SET_ON_CHANGE_PARAMETER))) {
				singlePattern = null;
				patterns = null;
			}
			PresentationObject inputWidget;

			if (inputHandler == null) {
				inputWidget = new TextInput(PARAM_COND_PATTERN);
				if (singlePattern != null) {
					// type casting is necessary to prevent calling the method 
					// PresentationObject>>setValue(Object)
					// The right method is setValue(String)
					((TextInput)inputWidget).setValue(singlePattern);
				}
			}
			else if (patterns != null) {
				inputWidget = inputHandler.getHandlerObject(PARAM_COND_PATTERN, patterns, iwc);
			}
			else {
				inputWidget = inputHandler.getHandlerObject(PARAM_COND_PATTERN, singlePattern, iwc);
			}
			table.add(inputWidget, 5, row);
		}
		else if (singlePattern == null && patterns == null) {
			// nothing to display!!!!
			return;
		}
		// normal display without any inputs
		else if (inputHandler != null) {
			String[] patternArray = (hasMoreThanOnePattern) ? (String[]) patterns.toArray(new String[0]) : new String[]  { singlePattern };
			Object resultingObject = null;
			String display = null;
			try {
				resultingObject = inputHandler.getResultingObject(patternArray, iwc);
				display = inputHandler.getDisplayForResultingObject(resultingObject, iwc);
			} 	
			catch (Exception e)	 {
				log(e);
				display = "";
			}
			table.add(display, 5, row);
		}
		else {
			table.add(singlePattern, 5, row);
		}
	}


	
	
	
	/**
	 * @param table
	 * @param row
	 */
	private void addHeaderToConditionTable(Table table, int row) {
		table.add(getMsgText(iwrb.getLocalizedString("field_id","Id")), 2, row);
		table.setColor(2,row, "#dfdfdf");
		table.add(getMsgText(iwrb.getLocalizedString("field_name", "Name")), 3, row);
		table.setColor(3,row,"#dfdfdf");
		table.add(getMsgText(iwrb.getLocalizedString("field_operator", "Operator")), 4, row);
		table.setColor(4,row,"#dfdfdf");
		table.add(getMsgText(iwrb.getLocalizedString("field_pattern", "Pattern")), 5, row);
		table.setColor(5,row,"#dfdfdf");
		table.add(getMsgText(iwrb.getLocalizedString("field_description", "Description")), 6, row);
		table.setColor(6,row,"#dfdfdf");
		table.add(getMsgText(iwrb.getLocalizedString("field_dynamic","Dynamic")), 7 ,row);
		table.setColor(7 ,row,"#dfdfdf");
	}

	public PresentationObject getStep6() {
		Table table = getStepTable();
		int row = 1;
		// thomas changed: do not use the FileChooser 
		// FileChooser folderChooser = new FileChooser(PARAM_FOLDER_ID);

		TextInput queryNameInput = new TextInput(PARAM_QUERY_NAME);
		queryNameInput.setWidth("250");
		
		
		queryNameInput.setLength(20);
		if (userQueryID > 0) {
			String queryName = helper.getUserQuery().getName();
			queryNameInput.setContent(queryName);
			table.add(iwrb.getLocalizedString("step_5_change_queryname", "Change query name"), 1, row++);
		}
		else {
			queryNameInput.setContent(iwrb.getLocalizedString("step_6_choose_name_for_query", "Choose a name for the query"));
			table.add(iwrb.getLocalizedString("step_5_set_queryname", "Set query name"), 1, row++);
		}
		table.add(queryNameInput, 1 , row++);
		// description
		table.add(iwrb.getLocalizedString("step_5_set_query_description", "Query description"), 1, row++);
		table.setAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
		String descriptionText = helper.getDescription();
		descriptionText = (descriptionText == null) ? "" : descriptionText;
		TextArea descriptionEditor = new TextArea(PARAM_QUERY_DESCRIPTION, descriptionText );
		descriptionEditor.setWidth("250");
		descriptionEditor.setHeight("60");
		table.add(descriptionEditor, 1, row++);
//LOCK		table.add(iwrb.getLocalizedString("step_5_check_template", "Save as template query ?"), 1, row);
//LOCK		table.add(templateCheck, 2, row);
		// add checkbox private or public
		row++;
		RadioGroup radioGroup = new RadioGroup(PARAM_IS_PRIVATE_QUERY);
		radioGroup.setWidth(1);
		boolean isPrivate = true;
		if (userQueryID > 0) {
			String permission = helper.getUserQuery().getPermisson();
			isPrivate = QueryConstants.PERMISSION_PRIVATE_QUERY.equals(permission);
		}
		radioGroup.addRadioButton(PRIVATE, new Text(iwrb.getLocalizedString("step_5_private_query", "private")), isPrivate);
		radioGroup.addRadioButton(PUBLIC, new Text(iwrb.getLocalizedString("step_5_public_query", "public")), ! isPrivate);
		table.add(radioGroup, 1, row);
		row++;
		if (userQueryID > 0) {
			RadioGroup radioGroupCopy = new RadioGroup(PARAM_SAVE_MODE);
			radioGroupCopy.setWidth(1);
			radioGroupCopy.addRadioButton(SAVE_AS_NEW_QUERY, new Text(iwrb.getLocalizedString("step_5_save_query_as_new", "save as new query")), true);
			radioGroupCopy.addRadioButton(OVERWRITE_QUERY, new Text(iwrb.getLocalizedString("step_5_overwrite_query", "overwrite existing one")));
			table.add(radioGroupCopy, 1, row);
		}

		return table;
	}

	private Table getButtons(int currentStep) {
		Table T = new Table(4, 1);
		// T.setWidth("300");
		// T.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_RIGHT);
		// T.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_LEFT);
		// T.setAlignment(T.HORIZONTAL_ALIGN_CENTER);
		int column = 1;
		if (currentStep > 1) {
			SubmitButton last =
				new SubmitButton(iwrb.getLocalizedImageButton("btn_previous", "<< previous"), PARAM_LAST, "true");
			T.add(last, column++, 1);
		}
		if (currentStep < 6) {
			SubmitButton next =
				new SubmitButton(iwrb.getLocalizedImageButton("btn_next", "next >>"), PARAM_NEXT, "true");
			T.add(next, column++, 1);
		}
		if (currentStep == 3 || currentStep ==  4 ||  currentStep == 5) {
			SubmitButton finish =
				new SubmitButton(iwrb.getLocalizedImageButton("btn_finish", "finish"), PARAM_FINAL, "true");

			T.add(finish, column++, 1);
		}
		if (currentStep > 5) {
			SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("btn_save", "Save"), PARAM_SAVE, "true");
			T.add(save, column++, 1);
		}
		if (currentStep > 0) {
			SubmitButton cancel =
				new SubmitButton(iwrb.getLocalizedImageButton("btn_cancel", "cancel"), PARAM_CANCEL, "true");
			T.add(cancel, column++, 1);
		}
		// thomas: removed
//		if (currentStep > 4) {
//			SubmitButton quit = new SubmitButton(iwrb.getLocalizedImageButton("btn_quit", "Quit"), PARAM_QUIT, "true");
//			T.add(quit, 5, 1);
//		}
		return T;
	}
	private Table getHelpTable(int currentStep, boolean isExpertMode) {
		Table helpTable = new Table();
		helpTable.setCellpadding(5);
		helpTable.setCellspacing(0);
		Help help = null;
		String expert = "";
		if(isExpertMode) {
			expert = "expert";
		}
		else {
			expert = "simple";
		}
		help = getHelp("help." + currentStep + expert);
		
		helpTable.add(help,1,1);
		
		return helpTable;
		
	}
	
	public void main(IWContext iwc) throws Exception {
		debugParameters(iwc);
		//iwb = getBundle(iwc);
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
	private QueryService getQueryService(IWContext iwc) throws RemoteException {
		if (queryService == null) {
			queryService = (QueryService) IBOLookup.getServiceInstance(iwc, QueryService.class);
		}
		return queryService;
	}
	public String getBundleIdentifier() {
		return QueryConstants.QUERY_BUNDLE_IDENTIFIER;
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

	public HashMatrix getMapOfFields() {
		HashMatrix map = new HashMatrix();
		if (helper.hasFields()) {
			Iterator iter = helper.getListOfFields().iterator();
			while (iter.hasNext()) {
				QueryFieldPart part = (QueryFieldPart) iter.next();
				map.put(part.getPath(), part.getName(), part);
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
		return userQueryID;
	}


	private DropdownMenu getConditionTypeDropdown() {
		DropdownMenu drp = new DropdownMenu(PARAM_COND_TYPE);
		String[] types = QueryConditionPart.getConditionTypes();
		for (int i = 0; i < types.length; i++) {
			drp.addMenuElement(types[i], iwrb.getLocalizedString("conditions." + types[i], types[i]));
		}
		return drp;
	}

	
	private DropdownMenu getAvailableFieldsDropdown(IWContext iwc, String keyName) throws RemoteException {
		QueryService service = getQueryService(iwc);
		Map drpMap = new HashMap();
		DropdownMenu drp = new DropdownMenu(keyName);

		if (helper.hasPreviousQuery())	{
			List previousQueries = helper.previousQueries();
			Iterator iterator = previousQueries.iterator();
			while (iterator.hasNext()) {
				QueryHelper previousQuery = (QueryHelper) iterator.next();
				String previousQueryName = previousQuery.getName();
				String previousQueryPath = previousQuery.getPath();
				List fields = previousQuery.getListOfVisibleFields();
				Iterator fieldIterator = fields.iterator();
				while (fieldIterator.hasNext())	{
					QueryFieldPart fieldPart = (QueryFieldPart) fieldIterator.next();
					String aliasName = fieldPart.getAliasName();
					String display = fieldPart.getDisplay();
					String type = fieldPart.getTypeClass();
					String handlerClass = fieldPart.getHandlerClass();
					String handlerDescription = fieldPart.getHandlerDescription();
					// see also method fillFieldsFromPreviousQuery
					QueryFieldPart newFieldPart = 
						new QueryFieldPart(aliasName, null, previousQueryName, previousQueryPath, aliasName, null, display, type, handlerClass, handlerDescription);
					drpMap.put(newFieldPart.encode(), newFieldPart);
					addMenuElement(drp, newFieldPart);
				}
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
			fillDropDown(service, entityPart,drpMap,drp);
		}
		while (iterator.hasNext()) {
			entityPart = (QueryEntityPart) iterator.next();
			fillDropDown(service, entityPart,drpMap,drp);
		}
		return drp;
	}
	
	private void fillDropDown(QueryService service,QueryEntityPart entityPart,Map drpMap,DropdownMenu drp)throws RemoteException {
		Iterator iter = service.getListOfFieldParts(iwrb, entityPart, expertMode).iterator();
		while (iter.hasNext()) {
			QueryFieldPart part = (QueryFieldPart) iter.next();
			String enc = part.encode();
			if(!drpMap.containsKey(enc)){
				addMenuElement(drp, part);
			}
		}
	}
	
	
	private void addMenuElement(DropdownMenu dropdownMenu, QueryFieldPart part) {
		dropdownMenu.addMenuElement(part.encode(),	getDisplay(part));
	}
	
	private String getDisplay(QueryFieldPart part) {
		if (part == null) {
			return "";
		}
		String entity = part.getEntity();
		String displayName = part.getDisplay();//localizable key
		String fieldName = part.getName();//the real database field name
		String functionName = part.getFunction(); // function name
		if (displayName == null || displayName.length() == 0) {
			displayName = iwrb.getLocalizedString(fieldName,fieldName);
		}
		StringBuffer buffer = new StringBuffer(iwrb.getLocalizedString(entity, entity));
		buffer.append(" -> ")
		.append(displayName)
		.append(" ( ")
		.append(fieldName)
		.append(" )");
		if (functionName != null && functionName.length() > 0)	{
			buffer.append(" [ ").append(functionName).append(" ] ");
		}
		return buffer.toString();
	}



	private Text getStepText(String string) {
		Text text = new Text(string);
		text.setStyleClass(stepFontStyle);
//		text.setStyle(Text.FONT_FACE_ARIAL);
//		text.setFontSize(Text.FONT_SIZE_14_HTML_4);
		text.setBold();
		return text;
	}

	private Text getMsgText(String string) {
		Text text = new Text(string);
		text.setStyleClass(messageFontStyle);
//		text.setStyle(Text.FONT_FACE_ARIAL);
//		text.setFontSize(Text.FONT_SIZE_10_HTML_2);
//		text.setBold();
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
		public EntityChooserTree(ICTreeNode node) {
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
	public Help getHelp(String helpTextKey) {
		IWContext iwc = IWContext.getInstance();
		IWBundle iwb = iwc.getIWMainApplication().getBundle("com.idega.block.dataquery");
	 	Help help = new Help();
	 	Image helpImage = iwb.getImage("help.gif");//.setSrc("/idegaweb/bundles/com.idega.user.bundle/resources/help.gif");
 	  help.setHelpTextBundle("com.idega.block.dataquery");
	  help.setHelpTextKey(helpTextKey);
	  help.setImage(helpImage);
	  return help;
	}


  private InputHandler getInputHandler(QueryFieldPart fieldPart) {
  	InputHandler inputHandler = null;
  	String predefinedClassName = fieldPart.getHandlerClass();
  	String currentDefinedClassName = helper.getInputHandler(fieldPart.getPath(), fieldPart.getName());
  	String className = null;
  	if (currentDefinedClassName != null) {
  		className = currentDefinedClassName;
  	}
  	else if (predefinedClassName != null) {
  		className = predefinedClassName;
  	}
  	else {
  		return null;
  	}
		try {
			inputHandler = (InputHandler) Class.forName(className).newInstance();
		}
		catch (ClassNotFoundException ex) {
			log(ex);
			ex.printStackTrace();
			logError("[ReportQueryBuilder] Could not retrieve handler class");
		}
		catch (InstantiationException ex) {
			log(ex);
			ex.printStackTrace();
			logError("[ReportQueryBuilder] Could not instanciate handler class");
		}
		catch (IllegalAccessException ex) {
			log(ex);
			ex.printStackTrace();
			logError("[ReportQueryBuilder] Could not instanciate handler class");
		}
		return inputHandler;
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
	
	// used by step 3 (field choice) and 4 (order by)
	private  void fillFieldsFromPreviousQuery(Map fieldMap, SelectionDoubleBox box) {
		if (helper.hasPreviousQuery())	{
			List previousQueries = helper.previousQueries();
			Iterator iterator = previousQueries.iterator();
			while (iterator.hasNext()) {
				List resultFields = new ArrayList();
				QueryHelper previousQuery = (QueryHelper) iterator.next();
				String previousQueryName = previousQuery.getName();
				String previousQueryPath = previousQuery.getPath();
				List fields = previousQuery.getListOfVisibleFields();
				Iterator fieldIterator = fields.iterator();
				while (fieldIterator.hasNext())	{
					QueryFieldPart fieldPart = (QueryFieldPart) fieldIterator.next();
					String aliasName = fieldPart.getAliasName();
					String display = fieldPart.getDisplay();
					String type = fieldPart.getTypeClass();
					// name, aliasName, entity, path, column,function, display, typeClass, handlerClass, handlerDescription
					QueryFieldPart newFieldPart = 
						new QueryFieldPart(aliasName, null,  previousQueryName, previousQueryPath, aliasName, null, display, type, null, null);
					resultFields.add(newFieldPart);
				}
				fillFieldSelectionBox(resultFields, fieldMap,box);
			}
		}
	}

}
