package com.idega.block.datareport.presentation;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.idega.block.dataquery.business.QueryHelper;
import com.idega.block.dataquery.business.QueryService;
import com.idega.block.dataquery.business.QueryToSQLBridge;
import com.idega.block.dataquery.data.QueryResult;
import com.idega.block.dataquery.data.sql.QuerySQL;
import com.idega.block.dataquery.presentation.QueryBuilder;
import com.idega.block.datareport.business.JasperReportBusiness;
import com.idega.business.IBOLookup;
import com.idega.core.data.ICTreeNode;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.AbstractTreeViewer;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.TreeViewerSelection;
import dori.jasper.engine.JasperPrint;
import dori.jasper.engine.design.JasperDesign;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jun 12, 2003
 */
public class ReportLayoutChooser extends Block {
  
  private static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.dataquery";
  
  // special init parameters
  public static final String SET_ID_OF_QUERY_FOLDER_KEY = "id_query_fold";
  public static final String SET_ID_OF_DESIGN_FOLDER_KEY = "id_design_fold";
  
  // restrictions
  private static final int REPORT_HEADLINE_MAX_LENGTH = 60;
  
  // session attributes
  private static final String QUERY_RESULT_SESSION_ATTRIBUTE = "query_result_session_attribute";
  
  private static final String EXCEL_REPORT_KEY = "xls_report_key";
  private static final String EXCEL_LINK_KEY = "xls_link";
  private static final String PDF_REPORT_KEY = "pdf_report_key";
  private static final String PDF_LINK_KEY = "pdf_link";
  private static final String HTML_REPORT_KEY = "html_report_key";
  private static final String HTML_LINK_KEY = "html_link";
  
  
  private static final String SELECTION_QUERY_KEY = "sel_query_key";
  private static final String SELECTION_REPORT_DESIGN_KEY = "sel_design_key"; 
  
  private static final String EXECUTE_QUERY_KEY = "exec_qu_key";
  private static final String GENERATE_REPORT_KEY = "gen_rep_key";
  private static final String PRINT_REPORT_KEY = "prn_rep_key";
  private static final String EXECUTE_QUERY_PRINT_REPORT_KEY = "exec_qu_prn_rep_key";
  
  private static final String REPORT_HEADLINE_KEY = "ReportTitle";
  
  private static final String SELECTED_VALUE = "sel_value";
  
  private static String EXECUTE_QUERY_ACTION = "exec_query_action";
  private static String GENERATE_REPORT_ACTION = "generate_report_action";
  private static String PRINT_REPORT_ACTION = "print_report_action";
  private static String EXECUTE_QUERY_PRINT_REPORT_ACTION = "exec_query_print_report_action";
  
  private static final String VIEW = "view_report";
  
  private ICFile queryFolder = null;
  private ICFile reportDesignFolder = null; 

  private Map parameterMap;
  
  private String answer = "";
  
  { parameterMap = new HashMap(8);
    parameterMap.put(EXCEL_REPORT_KEY,"");
    parameterMap.put(PDF_REPORT_KEY,"");
    parameterMap.put(HTML_REPORT_KEY,"");
    parameterMap.put(EXCEL_LINK_KEY, "");
    parameterMap.put(PDF_LINK_KEY, "");
    parameterMap.put(HTML_LINK_KEY,"");
    parameterMap.put(SELECTION_QUERY_KEY,"");
    parameterMap.put(SELECTION_REPORT_DESIGN_KEY,"");
    parameterMap.put(REPORT_HEADLINE_KEY,"");
    parameterMap.put(SET_ID_OF_QUERY_FOLDER_KEY,"");
    parameterMap.put(SET_ID_OF_DESIGN_FOLDER_KEY,"");
  }

  
  public String getBundleIdentifier() {
    return IW_BUNDLE_IDENTIFIER;
  } 
  
  public void setReportDesignFolder(ICFile reportDesignFolder)  {
    this.reportDesignFolder = reportDesignFolder;
  }
  
  public void setQueryFolder(ICFile queryFolder)  {
    this.queryFolder = queryFolder;
  }
  
  public void main(IWContext iwc) throws Exception {
    super.main(iwc);
    // get resource bundle
    IWResourceBundle resourceBundle = getResourceBundle(iwc);
    // get parameters
    parseParameters(resourceBundle, iwc);
    // parse action
    String action = parseAction(iwc);
    // do action
    if (action.equals(EXECUTE_QUERY_ACTION)) {
      answer = executeQuery(iwc,resourceBundle);
      answer = (answer.length() == 0) ?
        resourceBundle.getLocalizedString("report_la_sql_success", "Query was successfully executed.") : answer;
    }
    if (action.equals(PRINT_REPORT_ACTION)) {
      answer = printReport(iwc, resourceBundle);
    }
    if (action.equals(EXECUTE_QUERY_PRINT_REPORT_ACTION)) {
      answer = executeQuery(iwc, resourceBundle);
      if (answer.length() == 0) {
        answer = printReport(iwc, resourceBundle);
      }
    }
      
    add(getContent(resourceBundle));
  }
  
  private PresentationObject getContent(IWResourceBundle resourceBundle) {
    Table table = new Table(5,2);
    // create headlines
    Text queryHeadline = getHeadline(resourceBundle.getLocalizedString("report_la_headline_query", "Query"));
    Text designHeadline = getHeadline(resourceBundle.getLocalizedString("report_la_headline_design", "Design"));
    Text outputFormatHeadline = getHeadline(resourceBundle.getLocalizedString("report_la_headline_output_format","Output format"));
    Text resultHeadline = getHeadline(resourceBundle.getLocalizedString("report_la_headline_result","Result"));
    
    table.setRowVerticalAlignment(2, Table.VERTICAL_ALIGN_TOP);
    // query
    AbstractTreeViewer queryTree = getQueryTree();
    table.add(queryHeadline,1,1);
    table.add(queryTree,1,2);
    // design
    AbstractTreeViewer reportDesignTree = getReportDesignTree();
    table.add(designHeadline,2,1);
    table.add(reportDesignTree,2,2);
    // output format plus report headline input
    PresentationObject outputFormat = getOutputFormatArea(resourceBundle);
    table.add(outputFormatHeadline,3,1);
    Table outputTable = new Table(1,2);
    outputTable.add(outputFormat,1,1);
    outputTable.add(getReportHeadlineInput(resourceBundle),1,2); 
    table.add(outputTable, 3, 2);
    // buttons
    Table buttonTable = new Table(1,3);
//    buttonTable.add(getExecuteQueryButton(resourceBundle), 1,1);
//    buttonTable.add(getPrintReportButton(resourceBundle), 1, 2);
    buttonTable.add(getNewQueryBuilderButton(resourceBundle), 1, 1);
    buttonTable.add(getEditQueryBuilderButton(resourceBundle), 1,2 );
    buttonTable.add(getExecuteQueryAndPrintButton(resourceBundle),1,3);
    table.add(buttonTable,4,2);
    // results
    PresentationObject resultLinks = getResultLinks(resourceBundle);
    table.add(resultHeadline, 5, 1);
    table.add(resultLinks, 5, 2);

    // temporarily deactivated
    //buttonTable.add(getGenerateReportButton(resourceBundle), 2,2);
    
    Table mainTable = new Table(1,2);
    mainTable.add(table, 1,1);
    mainTable.add(getAnswer(resourceBundle), 1,2);
    // create form
    Form form = new Form();
    form.add(mainTable);
    addParametersToForm(form);  
    // finally add form 
    return form;
  }
    
  
  private String parseAction(IWContext iwc)  {
    // execute query?
    if (iwc.isParameterSet(EXECUTE_QUERY_KEY)) {
      return EXECUTE_QUERY_ACTION;
    }
    if (iwc.isParameterSet(PRINT_REPORT_KEY)) {
      return PRINT_REPORT_ACTION;
    }
    if (iwc.isParameterSet(EXECUTE_QUERY_PRINT_REPORT_KEY)) {
      return EXECUTE_QUERY_PRINT_REPORT_ACTION;
    }
    return "";
  }
  
     // report design
//    if (iwc.isParameterSet(SELECTION_REPORT_DESIGN_KEY)) {
//      String reportDesignId = iwc.getParameter(SELECTION_REPORT_DESIGN_KEY);
//      try {
//        selectedReportDesignId = Integer.parseInt(reportDesignId);
//      }
//      catch (NumberFormatException ex)  {
//        selectedReportDesignId = -1;
//      }
//    }
//    
//    if (iwc.isParameterSet(VIEW)) {
//
//    } 
//      return "";
//  }
  
//  private String createReport() {
//    return (selectedQueryId > -1 && selectedReportDesignId > -1 ) ? 
//      createReport( selectedQueryId, selectedReportDesignId) : null;
//  }
  
  private void addParametersToForm(Form form) {
    Iterator iterator = parameterMap.entrySet().iterator();
    while (iterator.hasNext())  {
      Map.Entry entry = (Map.Entry) iterator.next();
      // some values are Integer objects therefore use toString()
      String key = (String) entry.getKey();
      if (!(  key.equals(HTML_REPORT_KEY) || 
              key.equals(PDF_REPORT_KEY)  ||
              key.equals(EXCEL_REPORT_KEY) )) {
        // don't add the checkbox keys        
        form.addParameter( key, entry.getValue().toString());
      }
    }
  }    
  
  private void parseParameters(IWResourceBundle resourceBundle, IWContext iwc) {
    // check parameters
    Iterator iterator = parameterMap.entrySet().iterator();
    boolean isAnyParameterSet = false;
    while (iterator.hasNext())  {
      Map.Entry entry = (Map.Entry) iterator.next(); 
      String key = (String) entry.getKey();
      String value;
      if (iwc.isParameterSet(key)) {
        value = iwc.getParameter(key);
        // if there was already a parameter do not check again
        if (   isAnyParameterSet ||
            // but ignore the init parameter!
           ( (! SET_ID_OF_QUERY_FOLDER_KEY.equals(key)) &&  
             (! SET_ID_OF_DESIGN_FOLDER_KEY.equals(key))))  {
          isAnyParameterSet = true;
        }
      }
      else {
        value = "";
      }
      entry.setValue(value);
    }
    // if any parameters were set in the request the chooser was invoked the very first time
    // therefore initialize some parameters
    if (! isAnyParameterSet) {
      initializeParameters(resourceBundle);
    }
    // set special init paramters
    ICFile file;
    if ((file = getFileForParameter(SET_ID_OF_QUERY_FOLDER_KEY)) != null) {
      setQueryFolder(file);
    }
    if ((file =getFileForParameter(SET_ID_OF_DESIGN_FOLDER_KEY)) != null) {
      setReportDesignFolder(file);
    }
    // change type of some values
    // query id
    String queryId = (String) parameterMap.get(SELECTION_QUERY_KEY);
    parameterMap.put(SELECTION_QUERY_KEY, stringToInteger(queryId));
    // design id
    String designId = (String) parameterMap.get(SELECTION_REPORT_DESIGN_KEY);
    parameterMap.put(SELECTION_REPORT_DESIGN_KEY, stringToInteger(designId));
  }
  
  private ICFile getFileForParameter(String parameter)  {
    String idString = (String) parameterMap.get(parameter);
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
    
  private Integer stringToInteger(String integerString) {
    if (integerString.length() == 0)
      return new Integer(-1);
    try {
      return new Integer(integerString);
    }
    catch (NumberFormatException ex)  {
      return new Integer(-1);
    }
  }     
  
  private void resetLinksToFiles()  {
    parameterMap.put(HTML_LINK_KEY,"");
    parameterMap.put(PDF_LINK_KEY,"");
    parameterMap.put(EXCEL_LINK_KEY,"");
  }  
  
  private void initializeParameters(IWResourceBundle resourceBundle) {
    // preselect html page
    parameterMap.put(HTML_REPORT_KEY, SELECTED_VALUE);
    // predefine headline for report
    String defaultReportHeadline = resourceBundle.getLocalizedString("report_la_default_report_headline_value", "Report");
    parameterMap.put(REPORT_HEADLINE_KEY, defaultReportHeadline);
  }
  
  private Text getHeadline(String headline) {
    Text text = new Text(headline);
    text.setFontSize(2);
    text.setBold();
    return text;
  }
  
  private PresentationObject getAnswer(IWResourceBundle resourceBundle) {
    Text text = new Text(answer);
    text.setBold();
    return text;
  }
  
  private PresentationObject getEditQueryBuilderButton(IWResourceBundle resourceBundle) {
    Link queryBuilderLink = new Link(resourceBundle.getLocalizedImageButton("report_la_edit_query", "Edit Query..."));
    queryBuilderLink.setWindowToOpen(com.idega.user.presentation.QueryBuilderWindow.class);
    // add selected query
    Integer queryId = ((Integer) parameterMap.get(SELECTION_QUERY_KEY));
    if (queryId != null && queryFolder != null) {
      Integer queryFolderId = (Integer) queryFolder.getPrimaryKey();
      queryBuilderLink.addParameter(QueryBuilder.PARAM_QUERY_ID, queryId.toString());
      queryBuilderLink.addParameter(QueryBuilder.PARAM_QUERY_FOLDER_ID, queryFolderId.toString());
    }
    return queryBuilderLink;
  }
  
  private PresentationObject getNewQueryBuilderButton(IWResourceBundle resourceBundle)  {
    Link queryBuilderLink = new Link(resourceBundle.getLocalizedImageButton("report_la_new_query", "New Query..."));
    queryBuilderLink.setWindowToOpen(com.idega.user.presentation.QueryBuilderWindow.class);
    return queryBuilderLink;
  }
  
  private PresentationObject getExecuteQueryButton(IWResourceBundle resourceBundle)  {
    String name = resourceBundle.getLocalizedString("report_la_execute_query", "Execute query");
    SubmitButton button = new SubmitButton(EXECUTE_QUERY_KEY, name);
    button.setAsImageButton(true);
    return button;
  }   
  
  private PresentationObject getExecuteQueryAndPrintButton(IWResourceBundle resourceBundle) {
    String name = resourceBundle.getLocalizedString("report_la_execute_query_print_report", "Execute query and print report");
    SubmitButton button = new SubmitButton(EXECUTE_QUERY_PRINT_REPORT_KEY, name);
    button.setAsImageButton(true);
    return button;
  }       
 
  private PresentationObject getGenerateReportButton(IWResourceBundle resourceBundle)  {
    String name = resourceBundle.getLocalizedString("report_la_generate_query", "Generate report");
    SubmitButton button = new SubmitButton(GENERATE_REPORT_KEY, name);
    button.setAsImageButton(true);
    return button;
  }   
  
  private PresentationObject getPrintReportButton(IWResourceBundle resourceBundle)  {
    String name = resourceBundle.getLocalizedString("report_la_print_query", "Print report");
    SubmitButton button = new SubmitButton(PRINT_REPORT_KEY, name);
    button.setAsImageButton(true);
    return button;
  }   
  
  private PresentationObject  getOutputFormatArea(IWResourceBundle resourceBundle)  {
    Table table = new Table(2,3);
    // create checkbox html
    CheckBox html = new CheckBox(HTML_REPORT_KEY, SELECTED_VALUE);
    html.setChecked(parameterMap.get(HTML_REPORT_KEY).equals(SELECTED_VALUE));
    Text htmlText = new Text(resourceBundle.getLocalizedString("report_la_print_html_page", "Html page"));
    htmlText.setBold();
    table.add(html, 1,1);
    table.add(htmlText, 2, 1);
    // create checkbox pdf
    CheckBox pdf = new CheckBox(PDF_REPORT_KEY, SELECTED_VALUE);
    pdf.setChecked(parameterMap.get(PDF_REPORT_KEY).equals(SELECTED_VALUE));
    Text pdfText = new Text(resourceBundle.getLocalizedString("report_la_print_pdf_page", "PDF document"));
    pdfText.setBold();
    table.add(pdf, 1,2);
    table.add(pdfText, 2, 2);
    // create checkbox excel
    CheckBox excel = new CheckBox(EXCEL_REPORT_KEY, SELECTED_VALUE);
    excel.setChecked(parameterMap.get(EXCEL_REPORT_KEY).equals(SELECTED_VALUE));
    Text excelText = new Text(resourceBundle.getLocalizedString("report_la_print_excel_page", "excel document"));
    excelText.setBold();
    table.add(excel, 1,3);
    table.add(excelText, 2, 3);
    return table;
  }
  
  private PresentationObject getReportHeadlineInput(IWResourceBundle resourceBundle)  {
    Table table = new Table(1,2);
    // text input
    String headline = (String) parameterMap.get(REPORT_HEADLINE_KEY);
    TextInput reportHeadlineInput = new TextInput(REPORT_HEADLINE_KEY, headline);
    reportHeadlineInput.setLength(20);
    reportHeadlineInput.setMaxlength(REPORT_HEADLINE_MAX_LENGTH);
    Text text = new Text(resourceBundle.getLocalizedString("report_la_default_report_headline_input","Headline for report"));
    text.setBold();
    table.add(text, 1,1);
    table.add(reportHeadlineInput, 1 ,2);
    return table;
  }
  
  private PresentationObject getResultLinks(IWResourceBundle resourceBundle)  {
    Table table = new Table(1,3);
    String htmlUri = (String) parameterMap.get(HTML_LINK_KEY);
    if (htmlUri.length() != 0) {
      Text htmlUriText = new Text(resourceBundle.getLocalizedString("report_la_print_html_preview", "Preview"));
      Link htmlLink = new Link(htmlUriText, htmlUri);
      table.add(htmlLink, 1,1);
    }
    String pdfUri = (String) parameterMap.get(PDF_LINK_KEY);
    if (pdfUri.length() != 0) {
      Text pdfUriText = new Text(resourceBundle.getLocalizedString("report_la_print_pdf_download", "Download PDF file"));
      Link pdfLink = new Link(pdfUriText, pdfUri);
      table.add(pdfLink, 1,2);
    }
    String excelUri = (String) parameterMap.get(EXCEL_LINK_KEY);
    if (excelUri.length() != 0) {
      Text excelUriText = new Text(resourceBundle.getLocalizedString("report_la_print_excel_download", "Download Excel file"));
      Link excelLink = new Link(excelUriText, excelUri);
      table.add(excelLink, 1,3);
    }
    return table;
  }    

  private AbstractTreeViewer getQueryTree() {
    ICTreeNode root = (ICTreeNode) queryFolder;
    TreeViewerSelection tree = new TreeViewerSelection();
    tree.setSelectionKey(SELECTION_QUERY_KEY);
    Map maintainParameters = new HashMap(parameterMap);
    maintainParameters.remove(SELECTION_QUERY_KEY);
    tree.maintainParameters(maintainParameters);
    int selection = ((Integer) parameterMap.get(SELECTION_QUERY_KEY)).intValue();
    tree.setSelectedNode(selection);
    tree.setRootNode(root);
    tree.setDefaultOpenLevel(1);
    return tree;
  }
    
  
  private AbstractTreeViewer getReportDesignTree() {
    ICTreeNode root = (ICTreeNode) reportDesignFolder;
    TreeViewerSelection tree = new TreeViewerSelection();
    tree.setSelectionKey(SELECTION_REPORT_DESIGN_KEY);
    Map maintainParameters = new HashMap(parameterMap);
    maintainParameters.remove(SELECTION_REPORT_DESIGN_KEY);
    tree.maintainParameters(maintainParameters);
    int selection = ((Integer) parameterMap.get(SELECTION_REPORT_DESIGN_KEY)).intValue();
    tree.setSelectedNode(selection);
    tree.setRootNode(root);
    tree.setDefaultOpenLevel(1);
    return tree;
  }
  
  private String executeQuery(IWUserContext iwuc, IWResourceBundle resourceBundle)  {
    // destroy links to previous files
    resetLinksToFiles();
    int queryId = ((Integer) parameterMap.get(SELECTION_QUERY_KEY)).intValue();
    if (queryId < 0) {
      return resourceBundle.getLocalizedString("report_la_print_error_query_missing", "Choose a query, please");
    }
    // get the query from a file
    QueryResult queryResult;
    QueryHelper queryHelper;
    try {
      QueryService queryService = getQueryService();
      queryHelper = queryService.getQueryHelper(queryId);
    }
    catch (RemoteException rm) {
      System.err.println("[ReportLayoutChooser]: can't get QueryService. Mesage is: " + rm.getMessage());
      throw new RuntimeException("[ReportLayoutChooser]: Can't retrieve QueryService");
    }
    // get the sql statement
    QueryToSQLBridge bridge = getQueryToSQLBridge();
    try {
      QuerySQL query = bridge.createQuerySQL(queryHelper);
      String sqlStatement = query.getSQLStatement();
      if (sqlStatement.length() == 0) {
        return resourceBundle.getLocalizedString("report_la_print_error_query_problem", "A problem with the selected query occurred.");
      }
      // execute sql statement
      // sqlStatement = "SELECT FIRST_NAME FROM IC_USER";
      List displayNames = query.getDisplayNames();
      System.out.println("JASPERREPORT: "+sqlStatement);
      queryResult = bridge.executeStatement(sqlStatement, displayNames);
    }
    catch (RemoteException rm) {
      System.err.println("[ReportLayoutChooser]: can't get QueryToSqlBridge. Mesage is: " + rm.getMessage());
      throw new RuntimeException("[ReportLayoutChooser]: Can't retrieve QueryToSqlBridge");
    }
    catch (SQLException sql) {
      System.err.println("[ReportLayoutChooser]: can't execute sql statment. Mesage is: " + sql.getMessage());
      sql.printStackTrace(System.err);
      return resourceBundle.getLocalizedString("report_la_sql_error", "Execution of query failed.");
    }
      
    // store the result into the session
    iwuc.setSessionAttribute(QUERY_RESULT_SESSION_ATTRIBUTE, queryResult);
    return "";  
  }
            

  private String generateDesign(int designFileId) {
    return "";
  }
  
  private String printReport(IWUserContext iwuc, IWResourceBundle resourceBundle) {
    int designId = ((Integer) parameterMap.get(SELECTION_REPORT_DESIGN_KEY)).intValue();
    if (designId < 0) {
      return resourceBundle.getLocalizedString("report_la_print_error_design_missing", "Choose a design layout, please");
    }
    // get query from session
    QueryResult queryResult = (QueryResult) iwuc.getSessionAttribute(QUERY_RESULT_SESSION_ATTRIBUTE);
    if (queryResult == null)  {
      return resourceBundle.getLocalizedString("report_la_print_error_result_missing", "Excecute a query, please.");
    }
    if (queryResult.isEmpty())  {
      return resourceBundle.getLocalizedString("report_la_print_result_is_empty", "The result of the executed query is empty.");
    }
    JasperReportBusiness reportBusiness = getReportBusiness();
    try {
      JasperDesign design = reportBusiness.getDesign(designId);
      // synchronize design and result
      Map designParameters = new HashMap();
      designParameters.put(REPORT_HEADLINE_KEY, parameterMap.get(REPORT_HEADLINE_KEY));
      JasperPrint print = reportBusiness.printSynchronizedReport(queryResult, designParameters, design);
      if (parameterMap.get(HTML_REPORT_KEY).equals(SELECTED_VALUE)) {
        String uri = reportBusiness.getHtmlReport(print, "report");
        parameterMap.put(HTML_LINK_KEY, uri);
      }
      if (parameterMap.get(PDF_REPORT_KEY).equals(SELECTED_VALUE)) {
        String uri = reportBusiness.getPdfReport(print, "report");
        parameterMap.put(PDF_LINK_KEY, uri);
      }
      if (parameterMap.get(EXCEL_REPORT_KEY).equals(SELECTED_VALUE)) {
        String uri = reportBusiness.getExcelReport(print, "report");
        parameterMap.put(EXCEL_LINK_KEY, uri);
      }
    }
    catch (Exception ex)  {
      System.err.println("[ReportLayoutChooser]: can't print report file(s). Mesage is: " + ex.getMessage());
      ex.printStackTrace(System.err);
      return resourceBundle.getLocalizedString("report_la_print_error", "Can't print report file(s).");      
    }     
    return resourceBundle.getLocalizedString("report_la_print_success", "Desired files were successfully generated.");
  }


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

  private ICFile getFile(Integer fileId)  {
    try {
      ICFileHome home = (ICFileHome) IDOLookup.getHome(ICFile.class);
      ICFile file = (ICFile) home.findByPrimaryKey(fileId);
      return file;
    }
    // FinderException, RemoteException
    catch(Exception ex){
      throw new RuntimeException("[ReportBusiness]: Message was: " + ex.getMessage());
    }
  }     
  
}
