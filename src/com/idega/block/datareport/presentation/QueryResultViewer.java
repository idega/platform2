package com.idega.block.datareport.presentation;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.idega.block.dataquery.business.QueryGenerationException;
import com.idega.block.dataquery.business.QueryService;
import com.idega.block.dataquery.business.QueryToSQLBridge;
import com.idega.block.dataquery.data.QueryResult;
import com.idega.block.dataquery.data.sql.DirectSQLStatement;
import com.idega.block.dataquery.data.sql.InputDescription;
import com.idega.block.dataquery.data.sql.SQLQuery;
import com.idega.block.dataquery.data.xml.QueryHelper;
import com.idega.block.datareport.business.JasperReportBusiness;
import com.idega.block.datareport.data.DesignBox;
import com.idega.block.entity.data.EntityPathValueContainer;
import com.idega.block.entity.presentation.converter.ButtonConverter;
import com.idega.block.entity.presentation.converter.editable.DropDownMenuConverter;
import com.idega.business.IBOLookup;
import com.idega.business.InputHandler;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;

import dori.jasper.engine.JRException;
import dori.jasper.engine.JasperPrint;


/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jan 21, 2004
 */
public class QueryResultViewer extends Block {

	public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.dataquery";
	public static final String QUERY_ID_KEY = "query_id_key";
	public static final String DESIGN_ID_KEY = "design_id_key";
	public static final String OUTPUT_FORMAT_KEY = "output_format_key";
	
	public static final String DESIGN_CHOOSER_KEY ="design_chooser_key";
	
	public static final String ACTION_EXECUTE_QUERY = "action_execute_query";
	public static final String ACTION_SHOW_QUERY = "execute_show_query";
	public static final String EXECUTE_QUERY_KEY = "execute_query_key";

	
	public static final String PDF_KEY = "pdf_key";
  public static final String EXCEL_KEY = "excel_key";
  public static final String HTML_KEY = "html_key";
  
  private static final String REPORT_HEADLINE_KEY = "ReportTitle";
 	
  private QueryToSQLBridge bridge;
	private SQLQuery query;
	private int queryId;
	// -1 is default value
	private int designId = -1;
	private String outputFormat = HTML_KEY;
	
	public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

	public void main(IWContext iwc) throws Exception {
		IWResourceBundle resourceBundle = getResourceBundle(iwc);
		String errorMessage = initializeByParsing(iwc, resourceBundle);
		if (errorMessage != null) {
			addErrorMessage(errorMessage);
			return;
		}
		List executedSQLStatements = new ArrayList();
		if (query.isDynamic()) {
				showInputFieldsOrExecuteQuery(executedSQLStatements, resourceBundle, iwc);
		}
		else {
			errorMessage = executeQueries(query, bridge, executedSQLStatements, resourceBundle, iwc);
			if (errorMessage != null) {
	  		addErrorMessage(errorMessage);
	  	}
		}
	}
	
	private String initializeByParsing(IWContext iwc, IWResourceBundle resourceBundle) throws RemoteException {
		// request from self
		if (iwc.isParameterSet(QUERY_ID_KEY)) {
			queryId = Integer.parseInt(iwc.getParameter(QUERY_ID_KEY));
			designId = Integer.parseInt(iwc.getParameter(DESIGN_ID_KEY));
			outputFormat = iwc.getParameter(OUTPUT_FORMAT_KEY);
		}
		else {
			// request from overview
			EntityPathValueContainer executeContainer = ButtonConverter.getResultByParsing(iwc);
			if (executeContainer.isValid())	{
				// get the chosen output format
				outputFormat = executeContainer.getEntityPathShortKey();
				// get the chosen query 
				Integer queryIdInteger = executeContainer.getEntityIdConvertToInteger(); 
				queryId = queryIdInteger.intValue();
				//get the chosen layout
				EntityPathValueContainer layoutContainer = 
					DropDownMenuConverter.getResultByEntityIdAndEntityPathShortKey(queryIdInteger, DESIGN_CHOOSER_KEY, iwc); 
				if (layoutContainer.isValid()){
					try {
						designId = Integer.parseInt(layoutContainer.getValue().toString());
					}
					catch (NumberFormatException ex) {
						logError("[ReportOverview]: Can't retrieve id of layout");
						log(ex);
						return resourceBundle.getLocalizedString("ro_design_id_could_not_be_fetched", "Design id  could not be fetched");
					}
				}
				else {
					return resourceBundle.getLocalizedString("ro_design_id_could_not_be_fetched", "Design id  could not be fetched");
				}
			}
			else {
				return resourceBundle.getLocalizedString("ro_query_id_could_not_be_fetched", "Query id  could not be fetched");
			}
		}
		QueryService queryService = getQueryService();
    QueryHelper queryHelper = queryService.getQueryHelper(queryId);
    bridge = getQueryToSQLBridge(); 
    query = null;
    try {
    	query = bridge.createQuerySQL(queryHelper, iwc);
    }
    catch (QueryGenerationException ex) {
    	logError("[QueryResultViewer] Can't create query");
    	log(ex);
    	return resourceBundle.getLocalizedString("ro_query_could_not_be_created", "Query could not be created");
		}
    return null;
	}

  public QueryService getQueryService() {
    try {
      return (QueryService) IBOLookup.getServiceInstance( getIWApplicationContext() ,QueryService.class);
    }
    catch (RemoteException ex)  {
      System.err.println("[ReportOverview]: Can't retrieve QueryService. Message is: " + ex.getMessage());
      throw new RuntimeException("[ReportOverview]: Can't retrieve QueryService");
    }
  }

  public QueryToSQLBridge getQueryToSQLBridge() {
    try {
      return (QueryToSQLBridge) IBOLookup.getServiceInstance( getIWApplicationContext() ,QueryToSQLBridge.class);
    }
    catch (RemoteException ex)  {
      System.err.println("[ReportOverview]: Can't retrieve QueryToSqlBridge. Message is: " + ex.getMessage());
      throw new RuntimeException("[ReportOverview]: Can't retrieve QueryToSQLBridge");
    }
  }

  private void showInputFieldsOrExecuteQuery(List executedSQLStatements, IWResourceBundle resourceBundle, IWContext iwc) throws RemoteException {
  	Map identifierValueMap = query.getIdentifierValueMap();
	  boolean calculateAccess = false;
	  boolean containsOnlyAccessVariable = 
	  	(	(calculateAccess = identifierValueMap.containsKey(DirectSQLStatement.USER_ACCESS_VARIABLE))  || 
	    (calculateAccess = identifierValueMap.containsKey(DirectSQLStatement.GROUP_ACCESS_VARIABLE))) && 
	    (identifierValueMap.size() == 1);
	  if (! (containsOnlyAccessVariable || iwc.isParameterSet(EXECUTE_QUERY_KEY))) {
	  	Map identifierInputDescriptionMap = query.getIdentifierInputDescriptionMap();
	  	showInputFields(query, identifierValueMap,  identifierInputDescriptionMap, resourceBundle, iwc);
	  }
	  else {
	  	// get the values of the input fields
	    Map modifiedValues = getModifiedIdentiferValueMapByParsingRequest(identifierValueMap, iwc);
	    if (calculateAccess) {
	    	setAccessCondition(modifiedValues, iwc);
	    }
	    query.setIdentifierValueMap(modifiedValues);
	  	String errorMessage = executeQueries(query, bridge, executedSQLStatements, resourceBundle, iwc);
	  	if (errorMessage != null) {
	  		addErrorMessage(errorMessage);
	  		if (! containsOnlyAccessVariable) {
			  	Map identifierInputDescriptionMap = query.getIdentifierInputDescriptionMap();
			  	showInputFields(query, identifierValueMap,  identifierInputDescriptionMap, resourceBundle, iwc);
	  		}
	  	}
	  }
  }  
	  
	  private void showInputFields(SQLQuery query, Map identifierValueMap, Map identifierInputDescriptionMap, IWResourceBundle resourceBundle, IWContext iwc)	{
	  	String name = query.getName();
	  	PresentationObject presentationObject = getInputFields(name, identifierValueMap, identifierInputDescriptionMap, resourceBundle, iwc);
	  	Form form = new Form();
	  	form.addParameter(QUERY_ID_KEY, Integer.toString(queryId));
	  	form.addParameter(DESIGN_ID_KEY, Integer.toString(designId));
	  	form.addParameter(OUTPUT_FORMAT_KEY, outputFormat);
	  	form.add(presentationObject);
	  	add(form);
	}

  private PresentationObject getInputFields(String queryName, Map identifierValueMap, Map identifierInputDescriptionMap, IWResourceBundle resourceBundle, IWContext iwc)	{

  	// create a nice headline for the confused user
  	String currentQuery = resourceBundle.getLocalizedString("ro_current_query", "Current Query");
  	StringBuffer buffer = new StringBuffer(currentQuery);
  	buffer.append(": ").append(queryName);
  	Text text = new Text(buffer.toString());
  	text.setBold();
  	add(text);

  	Table table = new Table (2, identifierValueMap.size() + 1);
  	Iterator iterator = identifierValueMap.entrySet().iterator();
  	int i = 1;
  	while (iterator.hasNext())	{
  		Map.Entry entry = (Map.Entry) iterator.next();
  		String key = (String) entry.getKey();
  		if (! DirectSQLStatement.USER_ACCESS_VARIABLE.equals(key) && 
  				! DirectSQLStatement.GROUP_ACCESS_VARIABLE.equals(key)) {
	  		InputDescription inputDescription = (InputDescription) identifierInputDescriptionMap.get(key);
	  		Object object = identifierValueMap.get(key);
	  		String value = null;
	  		if (object instanceof Collection) {
	  			Iterator objectIterator = ((Collection) object).iterator();
	  			//TODO thi remove hack, extend interface of inputhandler
	  			while (objectIterator.hasNext()) {
	  				value = (String) objectIterator.next();
	  			}
	  		}
	  		else {
	  			value = (String) object;
	  		}
	  		String inputHandlerClass = inputDescription.getInputHandler();
	  		String description = inputDescription.getDescription();
	  		InputHandler inputHandler = getInputHandler(inputHandlerClass);
	  		PresentationObject input = (inputHandler != null) ? inputHandler.getHandlerObject(key, value, iwc) : new TextInput(key, value);
	  		table.add(description, 1, i);
	  		table.add(input, 2, i++);
  		}
  	}
  	String okayText = resourceBundle.getLocalizedString("ro_okay", "ok");
  	SubmitButton okayButton = new SubmitButton(okayText, EXECUTE_QUERY_KEY, "default_value");
  	okayButton.setAsImageButton(true);
  	PresentationObject goBack = getGoBackButton(resourceBundle);
   	table.add(goBack, 1, i);
  	table.add(okayButton, 1, i);
  	return table;
  }

  private InputHandler getInputHandler(String className) {
  	if (className == null) {
  		return null;
  	}
  	InputHandler inputHandler = null;
		try {
			inputHandler = (InputHandler) Class.forName(className).newInstance();
		}
		catch (ClassNotFoundException ex) {
			log(ex);
			logError("[ReportOverview] Could not retrieve handler class");
		}
		catch (InstantiationException ex) {
			log(ex);
			logError("[ReportOverview] Could not instanciate handler class");
		}
		catch (IllegalAccessException ex) {
			log(ex);
			logError("[ReportOverview] Could not instanciate handler class");
		}
		return inputHandler;
  }

	private PresentationObject getGoBackButton(IWResourceBundle resourceBundle)	{
  	String goBackText = resourceBundle.getLocalizedString("ro_back_to_list", "Back to list");
  	Link goBack = new Link(goBackText);
  	goBack.setOnClick("window.close()");
  	goBack.setAsImageButton(true);
  	return goBack;
	}
		
	private String executeQueries(SQLQuery query, QueryToSQLBridge bridge, List executedSQLStatements, IWResourceBundle resourceBundle, IWContext iwc) throws RemoteException {
		QueryResult queryResult = bridge.executeQueries(query, executedSQLStatements);
		// check if everything is fine
		if (queryResult == null || queryResult.isEmpty())	{
			// nothing to do
			return resourceBundle.getLocalizedString("ro_result_of_query_is_empty", "Result of query is empty");
		}
		// get design
		JasperReportBusiness reportBusiness = getReportBusiness();
		DesignBox designBox = getDesignBox(query, reportBusiness, resourceBundle, iwc);
    // synchronize design and result
    Map designParameters = new HashMap();
    designParameters.put(REPORT_HEADLINE_KEY, query.getName());
    JasperPrint print = reportBusiness.printSynchronizedReport(queryResult, designParameters, designBox);
    if (print == null) {
    	return resourceBundle.getLocalizedString("ro_could_not_use_layout", "Layout can't be used");
    }
    // create html report
    String uri;
    if (PDF_KEY.equals(outputFormat)) {
    	uri = reportBusiness.getPdfReport(print, "report");
    }
    else if (EXCEL_KEY.equals(outputFormat))	{
    	uri = reportBusiness.getExcelReport(print, "report");
    }
    else {
    	uri = reportBusiness.getHtmlReport(print, "report");
    }
    Page parentPage = getParentPage();
    parentPage.setToRedirect(uri);
    // open an extra window with scrollbars
    //getParentPage().setOnLoad("window.open('" + uri + "' , 'newWin', 'width=600,height=400,scrollbars=yes')");
	//openwindow(Address,Name,ToolBar,Location,Directories,Status,Menubar,Titlebar,Scrollbars,Resizable,Width,Height)
    //getParentPage().setOnLoad(" openwindow('" + uri + "','IdegaWeb Generated Report','0','0','0','0','0','1','1','1','800','600') ");
    return null;
	}
	
	private DesignBox getDesignBox(SQLQuery query, JasperReportBusiness reportBusiness, IWResourceBundle resourceBundle, IWContext iwc) {
    DesignBox design = null;
    try {
    	if (designId > 0) {  
    		design = reportBusiness.getDesignBox(designId);
    	}
    	else {
    		design = reportBusiness.getDynamicDesignBox(query, resourceBundle, iwc);
    	}
    }
    catch (IOException ioEx) {
    	logError("[ReportQueryOverview] Couldn't retrieve design.");
    	log(ioEx);
    }
    catch (JRException jrEx) {
    	logError("[ReportQueryOverview] Couldn't retrieve design.");
    	log(jrEx);
    }
    return design;
	}

  public JasperReportBusiness getReportBusiness() {
    try {
      return (JasperReportBusiness) IBOLookup.getServiceInstance( getIWApplicationContext(), JasperReportBusiness.class);
    }
    catch (RemoteException ex) {
      System.err.println("[ReportOverview]: Can't retrieve JasperReportBusiness. Message is: " + ex.getMessage());
      throw new RuntimeException("[ReportOverview]: Can't retrieve ReportBusiness");
    }
  }

	private void setAccessCondition(Map identifierValueMap, IWContext iwc) throws RemoteException {
		List groupIds = new ArrayList();
		int userId = iwc.getCurrentUserId();
		UserBusiness userBusiness = getUserBusiness();
		GroupBusiness groupBusiness = getGroupBusiness();
		User user = userBusiness.getUser(userId);
		Collection topGroupNodes = userBusiness.getUsersTopGroupNodesByViewAndOwnerPermissions(user,iwc);
		Iterator iterator = topGroupNodes.iterator();
		while ( iterator.hasNext())	{
			Group topGroup = (Group) iterator.next();
			Collection childGroups = groupBusiness.getChildGroupsRecursiveResultFiltered(topGroup, new ArrayList(), true);
			Iterator childGroupsIterator = childGroups.iterator();
			while (childGroupsIterator.hasNext())	{
				Group group = (Group) childGroupsIterator.next();
				groupIds.add(group.getPrimaryKey());
			}
		}
		// create the where condition for user view
		StringBuffer userBuffer = new StringBuffer("(select related_ic_group_id from ic_group_relation where ic_group_id in ");
		Iterator groupIdsIterator = groupIds.iterator();
		StringBuffer buffer = new StringBuffer("( ");
		String separator = "";
		while (groupIdsIterator.hasNext()) {
			buffer.append(separator);
			Object groupId = groupIdsIterator.next();
			buffer.append(groupId.toString());
			separator = " , ";
		}
		buffer.append(" )");
		userBuffer.append(buffer).append(" and group_relation_status = 'ST_ACTIVE')");
		identifierValueMap.put(DirectSQLStatement.USER_ACCESS_VARIABLE, userBuffer.toString());
		// create the where condition for group view
		identifierValueMap.put(DirectSQLStatement.GROUP_ACCESS_VARIABLE, buffer.toString());
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

   // parsing of the request      	
	private Map getModifiedIdentiferValueMapByParsingRequest(Map identifierValueMap, IWContext iwc)	{
		Map result = new HashMap();
		Iterator iterator = identifierValueMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			if (iwc.isParameterSet(key))	{
				String[] value = iwc.getParameterValues(key);
				// change to collection-based API
				result.put(key, Arrays.asList(value));
			}
			else {
				result.put(key, "");
			}
		}
		return result;
	}
  
	private void addErrorMessage(String errorMessage)	{
		Text text = new Text(errorMessage);
	  text.setBold();
	  text.setFontColor("#FF0000");
	  add(text);
	  add(Text.getBreak());
	}
}
