package com.idega.block.datareport.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.FinderException;

import com.idega.block.dataquery.business.QueryService;
import com.idega.block.dataquery.business.QueryToSQLBridge;
import com.idega.block.dataquery.data.QueryConstants;
import com.idega.block.dataquery.data.UserQuery;
import com.idega.block.dataquery.data.UserQueryHome;
import com.idega.block.dataquery.presentation.ReportQueryBuilder;
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
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.StringAlphabeticalComparator;


/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Oct 13, 2003
 */
public class ReportQueryOverview extends Block {
	
  // special init parameters
  // replaced by parameters in class QueryBuilder

	public static final String SET_ID_OF_DESIGN_FOLDER_KEY = ReportQueryBuilder.PARAM_LAYOUT_FOLDER_ID;
  
  public static final String DELETE_KEY = "delete_key";
  public static final String PDF_KEY = "pdf_key";
  public static final String EXCEL_KEY = "excel_key";
  public static final String HTML_KEY = "html_key";
  public static final String EDIT_QUERY_SIMPLE_MODE_KEY = "edit_simple_mode_key";
  public static final String EDIT_QUERY_EXPERT_MODE_KEY ="edit_expert_mode_key";
  public static final String EDIT_NEW_QUERY = "edit_new_query"; 
  public static final String UPLOAD_QUERY = "upload_query";
  public static final String UPLOAD_LAYOUT = "upload_layout";

  public static final String DELETE_ITEMS_KEY = "delete_items_key";
  
  // not really used
  public static final String CLOSE_KEY = "close_key";
  
  public static final String DESIGN_LAYOUT_KEY = QueryResultViewer.DESIGN_CHOOSER_KEY;
  public static final String NAME_KEY = "name_key";
  public static final String GROUP_NAME_KEY = "group_name_key";
  public static final String IS_PRIVATE_KEY = "is_private_key";
  
  public static final String VALUES_COMMITTED_KEY = "value_committed_key";
  public static final String SHOW_LIST_KEY = "show_list_key";
  
  public static final String SHOW_SINGLE_QUERY = "show_single_query";
  
  public static final String SHOW_SINGLE_QUERY_CHECK_IF_DYNAMIC = "show_single_query_check_if_dynamic";
  
	public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.dataquery";
	
	//private static final String REPORT_HEADLINE_KEY = "ReportTitle";
	

	
	// sets the investigation level of the query builder (-1 means, that the query builder is shown in the simple mode)
	private static final int SIMPLE_MODE = -1; 
	
	// sets the investigation level of the query builder (0,1,2,3... means that the query builder is shown in the expert mode)
	// be careful: high numbers need much performance and time!
	public static final int EXPERT_MODE = 3;

	protected ICFile designFolder;
	
	private int showOnlyOneQueryWithId = -1;
	
	protected Map parameterMap = new HashMap();
	
	private boolean isAdmin = false;
	
	// values of the parameter map
	private static final String CURRENT_QUERY_ID = QueryResultViewer.QUERY_ID_KEY;
	private static final String CURRENT_LAYOUT_ID = QueryResultViewer.DESIGN_ID_KEY;
	private static final String CURRENT_OUTPUT_FORMAT = QueryResultViewer.OUTPUT_FORMAT_KEY;
	
	public void setShowOnlyOneQueryWithId(int showOnlyOneQueryWithId)	{
		this.showOnlyOneQueryWithId = showOnlyOneQueryWithId;
	}
	
	public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
	
  public void main(IWContext iwc) throws Exception {
		try {
			isAdmin = iwc.getAccessController().isAdmin(iwc);
		} 
		catch (Exception ex) {
			String message =
				"[ReportOverview]: Can't retrieve AccessController.";
			System.err.println(message + " Message is: " + ex.getMessage());
			ex.printStackTrace(System.err);
			isAdmin = false;
		}

  	IWBundle bundle = getBundle(iwc);
    IWResourceBundle resourceBundle = getResourceBundle(iwc);
    String action = parseAction(iwc);
    if (SHOW_SINGLE_QUERY.equals(action) || SHOW_SINGLE_QUERY_CHECK_IF_DYNAMIC.equals(action))	{
    	//getSingleQueryView(bundle, resourceBundle, action, iwc);
    }
    else {
    	getListOfQueries(bundle, resourceBundle, iwc);
    }
  }

	private String parseAction(IWContext iwc)	throws FinderException, RemoteException{
		String action = "";
		// get the file id of the query folder
		if (iwc.isParameterSet(SET_ID_OF_DESIGN_FOLDER_KEY))	{
			String designFolderKey = iwc.getParameter(SET_ID_OF_DESIGN_FOLDER_KEY);
			parameterMap.put(SET_ID_OF_DESIGN_FOLDER_KEY, designFolderKey);
			designFolder = getFileForId(designFolderKey);
		}
		if (iwc.isParameterSet(DELETE_ITEMS_KEY))	{
			List idsToDelete = CheckBoxConverter.getResultByParsing(iwc, DELETE_KEY);
			deleteQueries(idsToDelete, iwc);
			return "";
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

	public static Collection getQueries(IWContext iwc) throws RemoteException, FinderException {
		return ReportQueryOverview.getQueries(iwc, -1);
	}
	
  private static Collection getQueries(IWContext iwc , int showOnlyOneQueryWithId) throws RemoteException, FinderException {
  	User currentUser = iwc.getCurrentUser();
  	GroupBusiness groupBusiness = getGroupBusiness(iwc);
  	UserBusiness userBusiness = getUserBusiness(iwc);
  	//TODO: thi solve problem with the group types
  	String[] groupTypes = 
			{ "iwme_federation", "iwme_union", "iwme_regional_union",  "iwme_league", "iwme_club", "iwme_club_division"};
		Group topGroup = userBusiness.getUsersHighestTopGroupNode(currentUser, Arrays.asList(groupTypes), iwc);
		// special hack for damaged databases
		if (topGroup == null) {
			List groupType = new ArrayList();
			groupType.add("general");
			topGroup = userBusiness.getUsersHighestTopGroupNode(currentUser, groupType,iwc);
		}
  	Collection parentGroups = new ArrayList();
  	try {
  		// bad implementation in GroupBusiness
  		// null is returned instead of an empty collection
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
  	//To keep them ordered alphabetically
  	TreeMap queryRepresentations = new TreeMap(new StringAlphabeticalComparator(iwc.getCurrentLocale()));
		// add own queries
		getQueriesFromGroup(queryRepresentations, topGroup, true, true, showOnlyOneQueryWithId);
		getQueriesFromGroup(queryRepresentations, topGroup, false, true, showOnlyOneQueryWithId);
		// add public queries
  	Iterator parentGroupsIterator = parentGroups.iterator();
  	while (parentGroupsIterator.hasNext()) {
  		Group group = (Group) parentGroupsIterator.next();
  		getQueriesFromGroup(queryRepresentations, group, false, false, showOnlyOneQueryWithId);
  	}
  	return queryRepresentations.values();
  }
  	
  private void getListOfQueries(IWBundle bundle, IWResourceBundle resourceBundle, IWContext iwc) throws RemoteException, FinderException {
  	Collection queries = ReportQueryOverview.getQueries(iwc, showOnlyOneQueryWithId);
  	Form form = new Form();
  	form.setName("list_form");
  	EntityBrowser browser = getBrowser(new  ArrayList(queries), bundle, resourceBundle, form);
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
  	
//  private static void getQueriesFromFolder(ICFile folderFile, TreeMap queryRepresentations, String groupName, boolean isPrivate, boolean belongsToUser, int showOnlyOneQueryWithId) { 
//			// bad implementation:
//			// if the children list is empty null is returned. 
//			//TODO: thi: change the implementation
//			Iterator iterator = folderFile.getChildren();
//			if (iterator == null) {
//				iterator = (new ArrayList(0)).iterator();
//			}
//			while (iterator.hasNext())	{
//				ICTreeNode node = (ICTreeNode) iterator.next();
//				int id = node.getNodeID();
//				String name = node.getNodeName();
//				int countOfSameName = 2;
//				
//				boolean alreadyAddedKey = queryRepresentations.containsKey(name);
//				if(alreadyAddedKey){
//					String newName = name;
//					while(alreadyAddedKey){
//						//probably crappy code its 4am and i dead tired - Eiki
//						//query with the same name, cannot add to map directly until I change the key name a little to avoid overwrites
//						newName = new String(name+countOfSameName);
//						alreadyAddedKey = queryRepresentations.containsKey(newName);//if not we use that name	
//						countOfSameName++;
//					}
//					name = newName;
//				}
//				// show only the query with a specified id if desired 
//				if (showOnlyOneQueryWithId == -1 || id == showOnlyOneQueryWithId)	{
//					QueryRepresentation representation = new QueryRepresentation(id, name, groupName, isPrivate, belongsToUser);
//					queryRepresentations.put(name,representation);
//				}
//			}  
//  	}
  private static void getQueriesFromGroup(TreeMap queryRepresentations, Group group, boolean isPrivate, boolean belongsToUser, int showOnlyOneQueryWithId) throws FinderException { 
			// bad implementation:
			// if the children list is empty null is returned. 
			//TODO: thi: change the implementation
  		String groupName = group.getName();
  		UserQueryHome userQueryHome = getUserQueryHome();
  		String permission = (isPrivate) ? QueryConstants.PERMISSION_PRIVATE_QUERY : QueryConstants.PERMISSION_PUBLIC_QUERY;
  		Collection userQueries = userQueryHome.findByGroupAndPermission(group, permission);
			Iterator iterator = userQueries.iterator();
			while (iterator.hasNext())	{
				UserQuery userQuery = (UserQuery) iterator.next();
				int id = ((Integer) userQuery.getPrimaryKey()).intValue();
				String name = userQuery.getName();
				int countOfSameName = 2;
				
				boolean alreadyAddedKey = queryRepresentations.containsKey(name);
				if(alreadyAddedKey){
					String newName = name;
					while(alreadyAddedKey){
						//probably crappy code its 4am and i dead tired - Eiki
						//query with the same name, cannot add to map directly until I change the key name a little to avoid overwrites
						newName = new String(name+countOfSameName);
						alreadyAddedKey = queryRepresentations.containsKey(newName);//if not we use that name	
						countOfSameName++;
					}
					name = newName;
				}
				// show only the query with a specified id if desired 
				if (showOnlyOneQueryWithId == -1 || id == showOnlyOneQueryWithId)	{
					QueryRepresentation representation = new QueryRepresentation(id, name, groupName, isPrivate, belongsToUser);
					queryRepresentations.put(name,representation);
				}
			}  
  	}

 
	private PresentationObject getButtonBar(IWResourceBundle resourceBundle)	{
		Table table = new Table(7,1);
		// new button for query builder (simple mode)
		String simpleModeText = resourceBundle.getLocalizedString("ro_create_simple_mode", "New (simple mode)");
		Link simpleModeLink = new Link(simpleModeText);

		simpleModeLink.addParameter(ReportQueryBuilder.SHOW_WIZARD, Integer.toString(SIMPLE_MODE));
		simpleModeLink.addParameter(ReportQueryOverview.EDIT_NEW_QUERY, ReportQueryOverview.EDIT_NEW_QUERY);
		//simpleModeLink.addParameter(ReportQueryBuilder.PARAM_QUERY_FOLDER_ID, parameterMap.get(SET_ID_OF_QUERY_FOLDER_KEY).toString());
		simpleModeLink.addParameter(ReportQueryBuilder.PARAM_LAYOUT_FOLDER_ID, parameterMap.get(SET_ID_OF_DESIGN_FOLDER_KEY).toString());
		simpleModeLink.setAsImageButton(true);
		
  	int column = 1;
  	table.add(simpleModeLink,column++,1);
  	if (isAdmin) {
	  	// new button for query builder (expert mode)
			String expertModeText = resourceBundle.getLocalizedString("ro_create_expert_mode", "New (expert mode)");
			Link expertModeLink = new Link(expertModeText);
	
			expertModeLink.addParameter(ReportQueryBuilder.SHOW_WIZARD, Integer.toString(EXPERT_MODE));
			expertModeLink.addParameter(ReportQueryOverview.EDIT_NEW_QUERY, ReportQueryOverview.EDIT_NEW_QUERY);
		//	expertModeLink.addParameter(ReportQueryBuilder.PARAM_QUERY_FOLDER_ID, parameterMap.get(SET_ID_OF_QUERY_FOLDER_KEY).toString());
			expertModeLink.addParameter(ReportQueryBuilder.PARAM_LAYOUT_FOLDER_ID, parameterMap.get(SET_ID_OF_DESIGN_FOLDER_KEY).toString());
			expertModeLink.setAsImageButton(true);
			
	  	table.add(expertModeLink,column++,1);
  	}
  	
		// new button for query builder (simple mode)
		String uploadQueryText = resourceBundle.getLocalizedString("ro_upload_query", "Upload query");
		Link uploadQueryLink = new Link(uploadQueryText);
		uploadQueryLink.addParameter(ReportQueryOverview.UPLOAD_QUERY, ReportQueryOverview.UPLOAD_QUERY);
		//uploadQueryLink.addParameter(ReportQueryBuilder.PARAM_QUERY_FOLDER_ID, parameterMap.get(SET_ID_OF_QUERY_FOLDER_KEY).toString());
		uploadQueryLink.addParameter(ReportQueryBuilder.PARAM_LAYOUT_FOLDER_ID, parameterMap.get(SET_ID_OF_DESIGN_FOLDER_KEY).toString());
		uploadQueryLink.setAsImageButton(true);
		
		table.add(uploadQueryLink, column++, 1);

		// new button for query builder (simple mode)
		String uploadLayoutText = resourceBundle.getLocalizedString("ro_upload_layout", "Upload layout");
		Link uploadLayoutLink = new Link(uploadLayoutText);
		uploadLayoutLink.addParameter(ReportQueryOverview.UPLOAD_LAYOUT, ReportQueryOverview.UPLOAD_LAYOUT);
		//uploadQueryLink.addParameter(ReportQueryBuilder.PARAM_QUERY_FOLDER_ID, parameterMap.get(SET_ID_OF_QUERY_FOLDER_KEY).toString());
		uploadLayoutLink.addParameter(ReportQueryBuilder.PARAM_LAYOUT_FOLDER_ID, parameterMap.get(SET_ID_OF_DESIGN_FOLDER_KEY).toString());
		uploadLayoutLink.setAsImageButton(true);
		
		table.add(uploadLayoutLink, column++, 1);

		// delete button
		String deleteText = resourceBundle.getLocalizedString("ro_delete", "Delete");
  	SubmitButton delete = new SubmitButton(DELETE_ITEMS_KEY, deleteText);
  	// change target
  	//String click = getRemoveTargetScript();//ReportOverviewWindow.class);
  	//delete.setOnClick(click);
  	
  	delete.setAsImageButton(true);
  	// close button
  	String closeText = resourceBundle.getLocalizedString("ro_cancel", "Cancel");
  	Link close = new Link(closeText);
  	close.addParameter(CLOSE_KEY, CLOSE_KEY);
  	close.setAsImageButton(true);
  	close.setOnClick("window.close()");

  	table.add(delete,column++,1);
  	table.add(close, column++,1);
  	// special button if only one query was shown
  	if (showOnlyOneQueryWithId != -1)	{
  		PresentationObject goBack = getGoBackButton(resourceBundle);
  		table.add(goBack, column++,1);
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
		dropDownLayoutConverter.setAddEntryForNonExistingValue(false);
		dropDownLayoutConverter.setOptionProvider(getLayoutOptionProvider());
		dropDownLayoutConverter.setShowAlwaysDropDownMenu(true);
		// edit query converter
		String simpleModeDisplay = resourceBundle.getLocalizedString("ro_edit_query_simple_mode", "Edit (simple mode)");
		String expertModeDisplay = (isAdmin) ? resourceBundle.getLocalizedString("ro_edit_query_expert_mode", "Edit (expert mode)") : null;
		EditQueryConverter simpleEditQueryConverter = new EditQueryConverter(simpleModeDisplay, SIMPLE_MODE);
		EditQueryConverter expertEditQueryConverter = (isAdmin) ? new EditQueryConverter(expertModeDisplay, EXPERT_MODE) : null;
			
		// checkbox converter
		ButtonConverter htmlConverter = new ButtonConverter(bundle.getImage("/shared/txt.gif"));
		ButtonConverter pdfConverter = new ButtonConverter(bundle.getImage("/shared/pdf.gif"));
		ButtonConverter excelConverter = new ButtonConverter(bundle.getImage("/shared/xls.gif"));
		// change target
		String click = getChangeTargetScript(); //QueryResultViewerWindow.class);
		htmlConverter.setOnClick(click);
		pdfConverter.setOnClick(click);
		excelConverter.setOnClick(click);
		
		browser.setMandatoryColumnWithConverter(1, DELETE_KEY, new DeleteCheckBox(DELETE_KEY));
		browser.setMandatoryColumn(2, NAME_KEY);
		browser.setMandatoryColumn(3, GROUP_NAME_KEY);
		browser.setMandatoryColumn(4, IS_PRIVATE_KEY);
		browser.setMandatoryColumnWithConverter(5, DESIGN_LAYOUT_KEY, dropDownLayoutConverter);
		
		browser.setMandatoryColumnWithConverter(6, HTML_KEY, htmlConverter);
		browser.setMandatoryColumnWithConverter(7, PDF_KEY, pdfConverter);
		browser.setMandatoryColumnWithConverter(8, EXCEL_KEY, excelConverter);
		
		browser.setMandatoryColumnWithConverter(9, EDIT_QUERY_SIMPLE_MODE_KEY, simpleEditQueryConverter);
		if (isAdmin) {
			browser.setMandatoryColumnWithConverter(10, EDIT_QUERY_EXPERT_MODE_KEY, expertEditQueryConverter);
		}
		return browser;
	}		
  		
	private OptionProvider getLayoutOptionProvider()	{
		OptionProvider optionProvider = new OptionProvider() {
			
			Map optionMap = null;
			
			public Map getOptions(Object entity, EntityPath path, EntityBrowser browser, IWContext iwc)	{
				IWResourceBundle resourceBundle = getResourceBundle(iwc);
				String dynamicLayout = resourceBundle.getLocalizedString("ro_dynamic_layout","dynamic layout");
				if (optionMap == null) {
					optionMap = new LinkedHashMap();
					optionMap.put("-1", dynamicLayout);
					if(designFolder!=null){
		  				Iterator iterator = designFolder.getChildren();
		  				while (iterator.hasNext())	{
		  					ICTreeNode node = (ICTreeNode) iterator.next();
		  					String name = node.getNodeName();
		  					int id = node.getNodeID();
		  					String idAsString = Integer.toString(id);
		  					optionMap.put(idAsString, name);
		  				}
					}
				
				}
				return optionMap;
			}
			

				
		};
		return optionProvider;
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
      ICFile file = home.findByPrimaryKey(fileId);
      return file;
    }
    catch(RemoteException ex){
      throw new RuntimeException("[ReportBusiness]: Message was: " + ex.getMessage());
    }
  }     
  
	private void deleteQueries(List idsToDelete, IWContext iwc) throws RemoteException	{
		User currentUser = iwc.getCurrentUser();
		Iterator iterator = idsToDelete.iterator();
		QueryService queryService = getQueryService(iwc);
		while (iterator.hasNext())	{
			Integer id = (Integer) iterator.next();
			queryService.removeUserQuery(id, currentUser);
		}
	}
	

	// some get service methods

  public static QueryService getQueryService(IWContext iwc) {
    try {
      return (QueryService) IBOLookup.getServiceInstance( iwc.getApplicationContext() ,QueryService.class);
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

  public JasperReportBusiness getReportBusiness() {
    try {
      return (JasperReportBusiness) IBOLookup.getServiceInstance( getIWApplicationContext(), JasperReportBusiness.class);
    }
    catch (RemoteException ex) {
      System.err.println("[ReportOverview]: Can't retrieve JasperReportBusiness. Message is: " + ex.getMessage());
      throw new RuntimeException("[ReportOverview]: Can't retrieve ReportBusiness");
    }
  }
 

  
			
  
  // link to query builder converter
  class  EditQueryConverter implements EntityToPresentationObjectConverter	{
  	
  	private String display;
  	private int searchDepth;
  	
  	public EditQueryConverter(String display, int searchDepth)	{
  		this.display = display;
  		this.searchDepth = searchDepth;
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
			Object entity,
			EntityPath path,
			EntityBrowser browser,
			IWContext iwc) {
			//String shortKeyPath = path.getShortKey();
			if (((QueryRepresentation) entity).belongsToUser()) {
				EntityRepresentation idoEntity = (EntityRepresentation) entity;
				Link link = new Link(display);
				link.addParameter(ReportQueryBuilder.SHOW_WIZARD, Integer.toString(searchDepth));
				link.addParameter(ReportQueryBuilder.PARAM_QUERY_ID, idoEntity.getPrimaryKey().toString());
			//	link.addParameter(ReportQueryBuilder.PARAM_QUERY_FOLDER_ID, parameterMap.get(SET_ID_OF_QUERY_FOLDER_KEY).toString());
				link.addParameter(ReportQueryBuilder.PARAM_LAYOUT_FOLDER_ID,parameterMap.get(SET_ID_OF_DESIGN_FOLDER_KEY).toString());
				link.setAsImageButton(true);
				return link;
			}
			return Text.emptyString();
		}
  }
 	
  class DeleteCheckBox extends CheckBoxConverter {
  	
  	public DeleteCheckBox(String name) {
  		super(name);
  	}
  	
	  public PresentationObject getPresentationObject(
	    Object entity,
	    EntityPath path,
	    EntityBrowser browser,
	    IWContext iwc) { 
	  	if (((QueryRepresentation) entity).belongsToUser()) {
	  		return super.getPresentationObject(entity, path, browser, iwc);
	  	}
	  	// return checkbox if query  
	  	return Text.emptyString();
	  }
  }
  
  public static UserQueryHome getUserQueryHome(){
  	UserQueryHome userQueryHome; 
    try{
      userQueryHome = (UserQueryHome)IDOLookup.getHome(UserQuery.class);
    }
    catch(RemoteException rme){
      throw new RuntimeException(rme.getMessage());
    }
    return userQueryHome;
  }
  
  
  
  
	public static UserBusiness getUserBusiness(IWContext iwc)	{
		try {
			return (UserBusiness) IBOLookup.getServiceInstance(iwc.getApplicationContext(), UserBusiness.class);
		}
		catch (RemoteException ex)	{
      System.err.println("[ReportOverview]: Can't retrieve UserBusiness. Message is: " + ex.getMessage());
      throw new RuntimeException("[ReportOverview]: Can't retrieve UserBusiness");
		}
	}

	public static GroupBusiness getGroupBusiness(IWContext iwc)	{
		try {
			return (GroupBusiness) IBOLookup.getServiceInstance(iwc.getApplicationContext(), GroupBusiness.class);
		}
		catch (RemoteException ex)	{
      System.err.println("[ReportOverview]: Can't retrieve GroupBusiness. Message is: " + ex.getMessage());
      throw new RuntimeException("[ReportOverview]: Can't retrieve GroupBusiness");
		}
	}
	
	private String getChangeTargetScript() {//(Class windowClass) {
  //	String windowId = IWMainApplication.getEncryptedClassName(windowClass);
  	// note: the name "newTarget" is not important and arbitrary
//  	StringBuffer buffer = new StringBuffer("findObj('");
//  	buffer.append(Page.IW_FRAME_CLASS_PARAMETER);
//  	buffer.append("').value='");
//		buffer.append(windowId); 
  	StringBuffer buffer = new StringBuffer();
		buffer.append("var hello=window.name; this.form.target='newTarget'; openwindow('','newTarget','0','0','0','0','0','0','1','1','1024','768');this.form.submit(); this.form.target=hello;");
		buffer.append("changeValue(findObj('");
		buffer.append(ButtonConverter.SUBMIT_KEY);
		buffer.append("'),' ')");
		return buffer.toString();
	}

	
	// a representation of the query
	private static class QueryRepresentation implements EntityRepresentation {
			
			private int id;
			private String name;
			private String groupName;
			private boolean belongsToUser;
			private boolean isPrivate;
			
			public QueryRepresentation(int id, String name, String groupName, boolean isPrivate, boolean belongsToUser)	{
				this.id = id;
				this.name = name;
				this.groupName = groupName;
				this.belongsToUser = belongsToUser;
				this.isPrivate = isPrivate;
			}
			
			public Object getColumnValue(String columnName) {
				if (ReportQueryOverview.NAME_KEY.equals(columnName))	{
					return name;
				}
				else if (ReportQueryOverview.GROUP_NAME_KEY.equals(columnName))	{
					return groupName;
				} 
				else if (ReportQueryOverview.IS_PRIVATE_KEY.equals(columnName)) {
					return isPrivate ? "X" : "";
				}
				else if (ReportQueryOverview.DESIGN_LAYOUT_KEY.equals(columnName)) {
					//no preselection!
					return null;
				}
				return name;
			}
		
			public Object getPrimaryKey() {
				return new Integer(id);
			}
			
			public boolean belongsToUser() {
				return belongsToUser;
			}
		
	}
}


