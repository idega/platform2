package com.idega.block.datareport.presentation;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.FinderException;

import com.idega.block.dataquery.business.QueryGenerationException;
import com.idega.block.dataquery.business.QueryHelper;
import com.idega.block.dataquery.business.QueryService;
import com.idega.block.dataquery.business.QueryToSQLBridge;
import com.idega.block.dataquery.data.QueryResult;
import com.idega.block.dataquery.data.sql.QuerySQL;
import com.idega.block.dataquery.presentation.QueryBuilder;
import com.idega.block.datareport.business.JasperReportBusiness;
import com.idega.block.entity.business.EntityToPresentationObjectConverter;
import com.idega.block.entity.data.EntityPath;
import com.idega.block.entity.data.EntityPathValueContainer;
import com.idega.block.entity.presentation.EntityBrowser;
import com.idega.block.entity.presentation.converter.ButtonConverter;
import com.idega.block.entity.presentation.converter.CheckBoxConverter;
import com.idega.block.entity.presentation.converter.editable.DropDownMenuConverter;
import com.idega.block.entity.presentation.converter.editable.OptionProvider;
import com.idega.business.IBOLookup;
import com.idega.core.data.ICTreeNode;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.EntityRepresentation;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

import dori.jasper.engine.JasperPrint;
import dori.jasper.engine.design.JasperDesign;


/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Oct 13, 2003
 */
public class ReportOverview extends Block {
	
  // special init parameters
  // replaced by parameters in class QueryBuilder
  public static final String SET_ID_OF_QUERY_FOLDER_KEY = QueryBuilder.PARAM_QUERY_FOLDER_ID;
	public static final String SET_ID_OF_DESIGN_FOLDER_KEY = QueryBuilder.PARAM_LAYOUT_FOLDER_ID;
  
  public static final String DELETE_KEY = "delete_key";
  public static final String PDF_KEY = "pdf_key";
  public static final String EXCEL_KEY = "excel_key";
  public static final String HTML_KEY = "html_key";
  public static final String EDIT_QUERY_KEY = "edit_key";

  public static final String DELETE_ITEMS_KEY = "delete_items_key";
  
  // not really used
  public static final String CLOSE_KEY = "close_key";
  
  public static final String DESIGN_LAYOUT_KEY = "design_layout_key";
  public static final String NAME_KEY = "name_key";
  
  public static final String VALUES_COMMITTED_KEY = "value_committed_key";
  public static final String SHOW_LIST_KEY = "show_list_key";
  
  public static final String SHOW_SINGLE_QUERY = "show_single_query";
  
  public static final String SHOW_SINGLE_QUERY_CHECK_IF_DYNAMIC = "show_single_query_check_if_dynamic";
  
	public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.dataquery";
	
	private static final String REPORT_HEADLINE_KEY = "ReportTitle";
	
	private ICFile queryFolder;
	private ICFile designFolder;
	
	private int showOnlyOneQueryWithId = -1;
	
	private Map parameterMap = new HashMap();
	
	// values of the parameter map
	private static final String CURRENT_QUERY_ID = "current_query_id";
	private static final String CURRENT_LAYOUT_ID = "current_layout_id";
	private static final String CURRENT_OUTPUT_FORMAT = "current_output_format";
	
	public void setShowOnlyOneQueryWithId(int showOnlyOneQueryWithId)	{
		this.showOnlyOneQueryWithId = showOnlyOneQueryWithId;
	}
	
	public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

	
  public void main(IWContext iwc) throws Exception {
  	IWBundle bundle = getBundle(iwc);
    IWResourceBundle resourceBundle = getResourceBundle(iwc);
    String action = parseAction(iwc);
    if (SHOW_SINGLE_QUERY.equals(action) || SHOW_SINGLE_QUERY_CHECK_IF_DYNAMIC.equals(action))	{
    	getSingleQueryView(bundle, resourceBundle, action, iwc);
    }
    else {
    	getListOfQueries(bundle, resourceBundle);
    }
  }

	private String parseAction(IWContext iwc)	throws FinderException{
		String action = "";
		// get the file id of the query folder
		if (iwc.isParameterSet(SET_ID_OF_QUERY_FOLDER_KEY))	{
			String queryFolderKey = iwc.getParameter(SET_ID_OF_QUERY_FOLDER_KEY);
			parameterMap.put(SET_ID_OF_QUERY_FOLDER_KEY, queryFolderKey);
				queryFolder = getFileForId(queryFolderKey);
		}
		if (iwc.isParameterSet(SET_ID_OF_DESIGN_FOLDER_KEY))	{
			String designFolderKey = iwc.getParameter(SET_ID_OF_DESIGN_FOLDER_KEY);
			parameterMap.put(SET_ID_OF_DESIGN_FOLDER_KEY, designFolderKey);
			designFolder = getFileForId(designFolderKey);
		}
		if (iwc.isParameterSet(DELETE_ITEMS_KEY))	{
			List idsToDelete = CheckBoxConverter.getResultByParsing(iwc, DELETE_KEY);
			deleteQueries(idsToDelete);
		}
		// check html, pdf and excel buttons
		EntityPathValueContainer executeContainer = ButtonConverter.getResultByParsing(iwc);
		if (executeContainer.isValid())	{
			// get the chosen output format
			parameterMap.put(CURRENT_OUTPUT_FORMAT,executeContainer.getEntityPathShortKey());
			// get the chosen query 
			try {
				parameterMap.put(CURRENT_QUERY_ID, executeContainer.getEntityIdConvertToInteger());
			}
			catch (NumberFormatException ex) {
				String message =
					"[ReportOverview]: Can't retrieve id of query";
				System.err.println(message + " Message is: " + ex.getMessage());
				ex.printStackTrace(System.err);
				return "";
			}
			// get the chosen layout
			EntityPathValueContainer layoutContainer = 
				DropDownMenuConverter.getResultByEntityIdAndEntityPathShortKey(parameterMap.get(CURRENT_QUERY_ID), DESIGN_LAYOUT_KEY, iwc); 
			if (layoutContainer.isValid())	{
				try {
					parameterMap.put(CURRENT_LAYOUT_ID,  new  Integer(layoutContainer.getValue().toString()));
				}
				catch (NumberFormatException ex) {
					String message = "[ReportOverview]: Can't retrieve id of layout";
					System.err.println(message + " Message is: " + ex.getMessage());
					ex.printStackTrace(System.err);
					return "";
				}
			}
			return SHOW_SINGLE_QUERY_CHECK_IF_DYNAMIC;
		}
		
		// dynamic field values were committed
		
		else if (iwc.isParameterSet(VALUES_COMMITTED_KEY))	{
			try {
				String id = iwc.getParameter(CURRENT_QUERY_ID);
				parameterMap.put(CURRENT_QUERY_ID, new Integer(id));
			}
			catch (NumberFormatException ex) {
				String message =
					"[ReportOverview]: Can't retrieve query id.";
				System.err.println(message + " Message is: " + ex.getMessage());
				ex.printStackTrace(System.err);
				return "";
			}
			try {
				String id = iwc.getParameter(CURRENT_LAYOUT_ID);
				parameterMap.put(CURRENT_LAYOUT_ID, new Integer(id));
			}
			catch (NumberFormatException ex) {
				String message =
					"[ReportOverview]: Can't retrieve layout id.";
				System.err.println(message + " Message is: " + ex.getMessage());
				ex.printStackTrace(System.err);
				return "";
			}
			String format = iwc.getParameter(CURRENT_OUTPUT_FORMAT);
			parameterMap.put(CURRENT_OUTPUT_FORMAT, format);
			return SHOW_SINGLE_QUERY;
		}
		return action;
	}
    
		
		    
    
    
  private void  getListOfQueries(IWBundle bundle, IWResourceBundle resourceBundle) {
  	List queryRepresentations = new ArrayList();
  	// bad implementation:
  	// if the children list is empty null is returned. 
  	//TODO: thi: change the implementation
  	Iterator iterator = queryFolder.getChildren();
  	if (iterator == null) {
  		iterator = (new ArrayList(0)).iterator();
  	}
  	while (iterator.hasNext())	{
  		ICTreeNode node = (ICTreeNode) iterator.next();
  		String name = node.getNodeName();
  		int id = node.getNodeID();
  		// show only the query with a specified id if desired 
  		if (showOnlyOneQueryWithId == -1 || id == showOnlyOneQueryWithId)	{
  			QueryRepresentation representation = new QueryRepresentation(id, name);
  			queryRepresentations.add(representation);
  		}
  	}
  	Form form = new Form();
  	EntityBrowser browser = getBrowser(queryRepresentations, bundle, resourceBundle, form);
  	addParametersToBrowser(browser);
  	addParametersToForm(form);
  	if (showOnlyOneQueryWithId != -1)	{
  		String newQueryWasCreatedOrModfied = 
  			resourceBundle.getLocalizedString("ro_query_was_created_or_modified", "Following query was created or modifed");
  		Text text = new Text(newQueryWasCreatedOrModfied);
  		text.setBold();
  		text.setFontColor("#FF0000");
  		add(text);
  	} 
  	Table table = new Table(1,2);
  	table.add(browser, 1,1);
  	table.add(getButtonBar(resourceBundle), 1,2);
  	form.add(table);
  	add(form);
  }
  	
	private PresentationObject getButtonBar(IWResourceBundle resourceBundle )	{
		Table table = new Table(4,1);
		// new button
		String newText = resourceBundle.getLocalizedString("ro_create", "New");
		Link newLink = new Link(newText);
		//newLink.setWindowToOpen(com.idega.user.presentation.QueryBuilderWindow.class);
		newLink.addParameter(QueryBuilder.SHOW_WIZARD, QueryBuilder.SHOW_WIZARD);
		newLink.addParameter(QueryBuilder.PARAM_QUERY_FOLDER_ID, parameterMap.get(SET_ID_OF_QUERY_FOLDER_KEY).toString());
		newLink.addParameter(QueryBuilder.PARAM_LAYOUT_FOLDER_ID, parameterMap.get(SET_ID_OF_DESIGN_FOLDER_KEY).toString());
		newLink.setAsImageButton(true);
		// delete button
		String deleteText = resourceBundle.getLocalizedString("ro_delete", "Delete");
  	SubmitButton delete = new SubmitButton(deleteText, DELETE_ITEMS_KEY, DELETE_ITEMS_KEY);
  	delete.setAsImageButton(true);
  	// close button
  	String closeText = resourceBundle.getLocalizedString("ro_cancel", "Cancel");
  	Link close = new Link(closeText);
  	close.addParameter(CLOSE_KEY, CLOSE_KEY);
  	close.setAsImageButton(true);
  	close.setOnClick("window.close()");
  	table.add(newLink,1,1);
  	table.add(delete,2,1);
  	table.add(close, 3,1);
  	// special button if only one query was shown
  	if (showOnlyOneQueryWithId != -1)	{
  		PresentationObject goBack = getGoBackButton(resourceBundle);
  		table.add(goBack, 4,1);
  	}
  	return table;
	}

		
  	
	private EntityBrowser getBrowser(List queryRepresentations, IWBundle bundle, IWResourceBundle resourceBundle, Form form)	{
		EntityBrowser browser = new EntityBrowser();
		browser.setAcceptUserSettingsShowUserSettingsButton(false, false);
		browser.setUseExternalForm(true);
		browser.setUseEventSystem(false);
		// browser gets confused because of some new children windows, therefore set artificial compoundId
		browser.setArtificialCompoundId("report_overview", null);
		browser.setLeadingEntityIsUndefined();
		// browser.setShowAllEntities("", queryRepresentations);
		browser.setEntities("", queryRepresentations, 10);
		// some design features
		browser.setCellpadding(2);
		// define some converters
		// drop down menu converter
		DropDownMenuConverter dropDownLayoutConverter = new DropDownMenuConverter(form);
		dropDownLayoutConverter.setOptionProvider(getLayoutOptionProvider());
		dropDownLayoutConverter.setShowAlwaysDropDownMenu(true);
		// edit query converter
		String display = resourceBundle.getLocalizedString("ro_edit_query", "Edit query");
		EditQueryConverter editQueryConverter = new EditQueryConverter(display);
		// checkbox converter
		ButtonConverter htmlConverter = new ButtonConverter(bundle.getImage("/shared/txt.gif"));
		ButtonConverter pdfConverter = new ButtonConverter(bundle.getImage("/shared/pdf.gif"));
		ButtonConverter excelConverter = new ButtonConverter(bundle.getImage("/shared/xls.gif"));

		browser.setMandatoryColumnWithConverter(1, DELETE_KEY, new CheckBoxConverter(DELETE_KEY));
		browser.setMandatoryColumn(2, NAME_KEY);
		browser.setMandatoryColumnWithConverter(3, DESIGN_LAYOUT_KEY, dropDownLayoutConverter);
		
		browser.setMandatoryColumnWithConverter(4, HTML_KEY, htmlConverter);
		browser.setMandatoryColumnWithConverter(5, PDF_KEY, pdfConverter);
		browser.setMandatoryColumnWithConverter(6, EXCEL_KEY, excelConverter);
		
		browser.setMandatoryColumnWithConverter(7, EDIT_QUERY_KEY, editQueryConverter);
		return browser;
	}		
  		
	private OptionProvider getLayoutOptionProvider()	{
		OptionProvider optionProvider = new OptionProvider() {
			
			Map optionMap = null;
			
			public Map getOptions(Object entity, EntityPath path, EntityBrowser browser, IWContext iwc)	{
				if (optionMap == null) {
					optionMap = new HashMap();
  				Iterator iterator = designFolder.getChildren();
  				while (iterator.hasNext())	{
  					ICTreeNode node = (ICTreeNode) iterator.next();
  					String name = node.getNodeName();
  					int id = node.getNodeID();
  					String idAsString = Integer.toString(id);
  					optionMap.put(idAsString, name);
  				}
				}
				return optionMap;
			}
		};
		return optionProvider;
	}			
		
  	
    
  
	private void getSingleQueryView(IWBundle bundle, IWResourceBundle resourceBundle, String action, IWContext iwc)	throws RemoteException {
		String errorMessage = null;
		QueryService queryService = getQueryService();
		int currentQueryId = ((Integer) parameterMap.get(CURRENT_QUERY_ID)).intValue();
    QueryHelper queryHelper = queryService.getQueryHelper(currentQueryId);
    QueryToSQLBridge bridge = getQueryToSQLBridge(); 
    QuerySQL query = null;
    try {
    	query = bridge.createQuerySQL(queryHelper, iwc);
    }
    catch (QueryGenerationException ex) {
			String message =
				"[ReportOverview]: Can't generate query.";
			System.err.println(message + " Message is: " + ex.getMessage());
			ex.printStackTrace(System.err);
			errorMessage = resourceBundle.getLocalizedString("ro_query_could not_be_created", "Query could not be created");
		}
		// execute query if the query was successfully created
		if (errorMessage == null) { 
			//
			// query is dynamic
			//
	    if (query.isDynamic()) {
	    	Map identifierValueMap = query.getIdentifierValueMap();
	    	if (SHOW_SINGLE_QUERY_CHECK_IF_DYNAMIC.equals(action)) {
	    		// show input fields
					showInputFields(query, identifierValueMap, resourceBundle);
	    	}
	    	else {
	    		// get the values of the input fields
	    		Map modifiedValues = getModifiedIdentiferValueMap(identifierValueMap, iwc);
	    		query.setIdentifierValueMap(modifiedValues);
	    		// show result of query
	    		List executedSQLStatements = new ArrayList();
	    		boolean isOkay = executeQueries(query, bridge, executedSQLStatements);
	    		addExecutedSQLQueries(executedSQLStatements);
	    		if (! isOkay)	{
	    			errorMessage = resourceBundle.getLocalizedString("ro_result_of_query_is_empty", "Result of query is empty");
	    		}	
	     		// show again the input fields
	     		if (errorMessage != null)	{
	     			addErrorMessage(errorMessage);
	     		}
	    		showInputFields(query, modifiedValues, resourceBundle);	
	    	}
	    	//
	    	// good bye - query is dynamic
	    	//
	    	return;
	    }
	    //
	    // query is not dynamic
	    //
	    else {
	    	List executedSQLStatements = new ArrayList();
	    	boolean isOkay = executeQueries(query, bridge, executedSQLStatements);
	    	addExecutedSQLQueries(executedSQLStatements);
	    	if (! isOkay)	{
	    		errorMessage = resourceBundle.getLocalizedString("ro_result_of_query_is_empty", "Result of query is empty");
	    	}
	    }
		}
		// show list if query is not dynamic and if an error occurred
		if (errorMessage != null) {
			addErrorMessage(errorMessage);
		}
		getListOfQueries(bundle, resourceBundle);
	}
	
	private void addErrorMessage(String errorMessage)	{
		Text text = new Text(errorMessage);
	  text.setBold();
	  text.setFontColor("#FF0000");
	  add(text);
	  add(Text.getBreak());
	}
	
	private void addExecutedSQLQueries(List executedSQLStatements) {
		Iterator iterator = executedSQLStatements.iterator();
		while (iterator.hasNext())	{
			String statement = (String) iterator.next();
			Text text = new Text(statement);
			text.setBold();
	  	text.setFontColor("#FF0000");
	  	add(text);
	  	add(Text.getBreak());
		}
	}
		
	
	private void showInputFields(QuerySQL query, Map identifierValueMap, IWResourceBundle resourceBundle)	{
    Map identifierDescriptionMap = query.getIdentifierDescriptionMap();
    String name = query.getName();
    PresentationObject presentationObject = getInputFields(name, identifierValueMap, identifierDescriptionMap, resourceBundle);
    Form form = new Form();
    addParametersToForm(form);
    form.add(presentationObject);
    add(form);
	}

	
	private boolean executeQueries(QuerySQL query, QueryToSQLBridge bridge, List executedSQLQueries) throws RemoteException {
		QueryResult queryResult = bridge.executeQueries(query, executedSQLQueries);
		// check if everything is fine
		if (queryResult == null || queryResult.isEmpty())	{
			// nothing to do
			return false;
		}
    // get the design of the report 
    JasperReportBusiness reportBusiness = getReportBusiness();
    int designId = ((Integer) parameterMap.get(CURRENT_LAYOUT_ID)).intValue();
    JasperDesign design = reportBusiness.getDesign(designId);
    // synchronize design and result
    Map designParameters = new HashMap();
    designParameters.put(REPORT_HEADLINE_KEY, query.getName());
    JasperPrint print = reportBusiness.printSynchronizedReport(queryResult, designParameters, design);
    // create html report
    String uri;
    String format = (String) parameterMap.get(CURRENT_OUTPUT_FORMAT);
    if (PDF_KEY.equals(format)) {
    	uri = reportBusiness.getPdfReport(print, "report");
    }
    else if (EXCEL_KEY.equals(format))	{
    	uri = reportBusiness.getExcelReport(print, "report");
    }
    else {
    	uri = reportBusiness.getHtmlReport(print, "report");
    }
    // open an extra window with scrollbars
    getParentPage().setOnLoad("window.open('" + uri + "' , 'newWin', 'width=600,height=400,scrollbars=yes')");
    return true;
	}
    
    
    			
    	
    	
	private Map getModifiedIdentiferValueMap(Map identifierValueMap, IWContext iwc)	{
		Map result = new HashMap();
		Iterator iterator = identifierValueMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			if (iwc.isParameterSet(key))	{
				String value = (String) iwc.getParameter(key);
				result.put(key, value);
			}
			else {
				result.put(key, "");
			}
		}
		return result;
	}
			
					    	
    	
    	
  private PresentationObject getInputFields(String queryName, Map identifierValueMap, Map identifierDescriptionMap, IWResourceBundle resourceBundle)	{

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
  		String description = (String) identifierDescriptionMap.get(key);
  		String value = (String) identifierValueMap.get(key);
  		TextInput textInput = new TextInput(key, value);
  		table.add(description, 1, i);
  		table.add(textInput, 2, i++);
  	}
  	String okayText = resourceBundle.getLocalizedString("ro_okay", "Okay");
  	SubmitButton okayButton = new SubmitButton(okayText, VALUES_COMMITTED_KEY, "default_value");
  	okayButton.setAsImageButton(true);
  	PresentationObject goBack = getGoBackButton(resourceBundle);
   	table.add(goBack, 1, i);
  	table.add(okayButton, 1, i);
  	return table;
  }
 
	private PresentationObject getGoBackButton(IWResourceBundle resourceBundle)	{
  	String goBackText = resourceBundle.getLocalizedString("ro_back_to_list", "Back to list");
  	Link goBack = new Link(goBackText);
  	goBack.addParameter(SHOW_LIST_KEY, SHOW_LIST_KEY);
  	addParametersToLink(goBack);
  	goBack.setAsImageButton(true);
  	return goBack;
	}
 
 
  		
	private void addParametersToForm(Form form)	{
		Iterator iterator = parameterMap.entrySet().iterator();
		while (iterator.hasNext())	{
			Map.Entry entry = (Map.Entry) iterator.next();
			String key = (String) entry.getKey();
			String value = entry.getValue().toString(); 
			form.addParameter(key, value);
		}
	}
	
	private void addParametersToLink(Link link)	{
		Iterator iterator = parameterMap.entrySet().iterator();
		while (iterator.hasNext())	{
			Map.Entry entry = (Map.Entry) iterator.next();
			String key = (String) entry.getKey();
			String value = entry.getValue().toString(); 
			link.addParameter(key, value);
		}
	}

	private void addParametersToBrowser(EntityBrowser browser)	{
		Iterator iterator = parameterMap.entrySet().iterator();
		while (iterator.hasNext())	{
			Map.Entry entry = (Map.Entry) iterator.next();
			String key = (String) entry.getKey();
			String value = entry.getValue().toString(); 
			browser.addMandatoryParameter(key, value);
		}
	}
	
	// some file methods
	
  private ICFile getFileForId( String idString) throws FinderException {
    if (idString.length() == 0) {
      return null;
    }
    Integer id = null;
    try {
      id = new Integer(idString);
    }  
    catch (NumberFormatException ex)  {
      System.err.println("[ReportLayoutChooser] Can't parse integer. Message is: "+ ex.getMessage());
      ex.printStackTrace(System.err);
      return null;
    }
    return getFile(id);
  }

  private ICFile getFile(Integer fileId) throws FinderException {
    try {
      ICFileHome home = (ICFileHome) IDOLookup.getHome(ICFile.class);
      ICFile file = (ICFile) home.findByPrimaryKey(fileId);
      return file;
    }
    catch(RemoteException ex){
      throw new RuntimeException("[ReportBusiness]: Message was: " + ex.getMessage());
    }
  }     

	private void deleteQueries(List idsToDelete)	{
		Iterator iterator = idsToDelete.iterator();
		while (iterator.hasNext())	{
			Integer id = (Integer) iterator.next();
			try {
				ICFile file = getFile(id);
				file.delete();
			}
			catch (FinderException ex) {
				String message =
					"[ReportOverview]: Can't find file with id " + id ;
				System.err.println(message + " Message is: " + ex.getMessage());
				ex.printStackTrace(System.err);
			}
			catch (SQLException sqlEx)	{
				String message =
					"[ReportOverview]: Can't delete file with id " + id ;
				System.err.println(message + " Message is: " + sqlEx.getMessage());
				sqlEx.printStackTrace(System.err);
			}
		}
	}

				
			 
	



	// some get service methods

  public QueryService getQueryService() {
    try {
      return (QueryService) IBOLookup.getServiceInstance( getIWApplicationContext() ,QueryService.class);
    }
    catch (RemoteException ex)  {
      System.err.println("[ReportlayoutChooser]: Can't retrieve QueryService. Message is: " + ex.getMessage());
      throw new RuntimeException("[ReportLayoutChooser]: Can't retrieve QueryService");
    }
  }



  public QueryToSQLBridge getQueryToSQLBridge() {
    try {
      return (QueryToSQLBridge) IBOLookup.getServiceInstance( getIWApplicationContext() ,QueryToSQLBridge.class);
    }
    catch (RemoteException ex)  {
      System.err.println("[ReportlayoutChooser]: Can't retrieve QueryToSqlBridge. Message is: " + ex.getMessage());
      throw new RuntimeException("[ReportLayoutChooser]: Can't retrieve QueryToSQLBridge");
    }
  }

  public JasperReportBusiness getReportBusiness() {
    try {
      return (JasperReportBusiness) IBOLookup.getServiceInstance( getIWApplicationContext(), JasperReportBusiness.class);
    }
    catch (RemoteException ex) {
      System.err.println("[ReportLayoutChooser]: Can't retrieve JasperReportBusiness. Message is: " + ex.getMessage());
      throw new RuntimeException("[ReportLayoutChooser]: Can't retrieve ReportBusiness");
    }
  }
 
  
  // a representation of the query
  class QueryRepresentation implements EntityRepresentation {
  	
  	private int id;
  	private String name;
  	
  	public QueryRepresentation(int id, String name)	{
  		this.id = id;
  		this.name = name;
  	}
  	
		public Object getColumnValue(String columnName) {
			return name;
		}
  
 		public Object getPrimaryKey() {
 			return new Integer(id);
 		}
  }
  
  // link to query builder converter
  class  EditQueryConverter implements EntityToPresentationObjectConverter	{
  	
  	private String display;
  	
  	public EditQueryConverter(String display)	{
  		this.display = display;
  	}
  	
		public PresentationObject getHeaderPresentationObject(
			EntityPath entityPath,
			EntityBrowser browser,
			IWContext iwc) {
			return browser.getDefaultConverter().getHeaderPresentationObject(entityPath, browser, iwc);
		}

		/* (non-Javadoc)
	 	* @see com.idega.block.entity.business.EntityToPresentationObjectConverter#getPresentationObject(java.lang.Object, com.idega.block.entity.data.EntityPath, com.idega.block.entity.presentation.EntityBrowser, com.idega.presentation.IWContext)
	 	*/
		public PresentationObject getPresentationObject(
			Object value,
			EntityPath path,
			EntityBrowser browser,
			IWContext iwc) {
			//String shortKeyPath = path.getShortKey();
			EntityRepresentation idoEntity = (EntityRepresentation) value;
			Link link = new Link(display);
			link.addParameter(QueryBuilder.SHOW_WIZARD, QueryBuilder.SHOW_WIZARD);
			link.addParameter(QueryBuilder.PARAM_QUERY_ID, idoEntity.getPrimaryKey().toString());
			link.addParameter(QueryBuilder.PARAM_QUERY_FOLDER_ID, parameterMap.get(SET_ID_OF_QUERY_FOLDER_KEY).toString());
			link.addParameter(QueryBuilder.PARAM_LAYOUT_FOLDER_ID,parameterMap.get(SET_ID_OF_DESIGN_FOLDER_KEY).toString());
			link.setAsImageButton(true);
			return link;
		}
  }
 	


}


