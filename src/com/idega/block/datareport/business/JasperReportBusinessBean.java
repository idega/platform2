package com.idega.block.datareport.business;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import com.idega.block.dataquery.data.QueryResult;
import com.idega.block.dataquery.data.QueryResultField;
import com.idega.block.dataquery.data.sql.DirectSQLStatement;
import com.idega.block.dataquery.data.sql.InputDescription;
import com.idega.block.dataquery.data.sql.SQLQuery;
import com.idega.block.dataquery.data.xml.QueryFieldPart;
import com.idega.block.datareport.data.DesignBox;
import com.idega.block.datareport.presentation.ReportOverviewWindowPlugin;
import com.idega.block.datareport.util.ReportDescription;
import com.idega.block.datareport.util.ReportableCollection;
import com.idega.block.datareport.util.ReportableField;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOServiceBean;
import com.idega.business.InputHandler;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWCacheManager;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.repository.data.RefactorClassRegistry;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.FileUtil;
import com.idega.util.StringHandler;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jun 10, 2003
 */
public class JasperReportBusinessBean extends IBOServiceBean implements JasperReportBusiness,UserGroupPlugInBusiness { 
  
  private static String REPORT_FOLDER = "reports";
  private static String HTML_FILE_EXTENSION = "html";
  private static String PDF_FILE_EXTENSION = "pdf";
  private static String EXCEL_FILE_EXTENSION = "xls";
  
  private static String REPORT_COLUMN_PARAMETER_NAME = "Column_";
  private static char DOT = '.';
  
  public JasperPrint getReport(JRDataSource dataSource, Map parameterMap, JasperDesign design) throws JRException {
    //System.out.println("JASPERREPORT: "+parameterMap.toString());
    JasperReport report = JasperManager.compileReport(design);
    return JasperFillManager.fillReport(report, parameterMap, dataSource);
  }
  
  private void synchronizeAndReset(QueryResult dataSource, DesignBox designBox) {
    synchronizeResultAndDesign(dataSource, designBox);
    // henceforth we treat the QueryResult as a JRDataSource, 
    // therefore we reset the QueryResult to prepare it 
    dataSource.resetDataSource(); // resets only the DataSource functionality (sets the pointer to the first row)
  }
  
  public JasperPrint printSynchronizedReport(QueryResult dataSource, DesignBox designBox) {
  	synchronizeAndReset(dataSource, designBox);
    JasperPrint print = null;
    try {
    	Map map = designBox.getParameterMap();
    	JasperDesign design = designBox.getDesign();
    	print = getReport(dataSource, map, design);
    }
    catch (JRException ex)  {
     logError("[ReportBusiness]: Jasper print could not be generated.");
     log(ex);
      return null;
    }
    return print;
  }
  
  public String getHtmlReport(JasperPrint print, String nameOfReport) {
    // prepare path
    long folderIdentifier = System.currentTimeMillis();
    String path = getRealPathToReportFile(nameOfReport, HTML_FILE_EXTENSION,folderIdentifier);
    try {
		JasperExportManager.exportReportToHtmlFile(print, path);
		/* Not needed since jasper 0.5 if you use utf-8
		 * UNLESS APACHE ALWAYS OUTPUTS ISO AS THE ENCODING
		 */
		  JRHtmlExporter exporter = new JRHtmlExporter();
			//to handle icelandic in html
			//saw this in the JRHTMLExport of jasperreports	
		  exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING,"iso-8859-1");
		  exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
		  exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, path);
		    
		  exporter.exportReport();
    }
    catch (JRException ex)  {
      logError("[ReportBusiness]: Jasper print could not be generated.");
      log(ex);
      return null;
    }
    return getURIToReport(nameOfReport, HTML_FILE_EXTENSION,folderIdentifier);
  }
  
  public String getPdfReport(JasperPrint print, String nameOfReport) {
    // prepare path
    long folderIdentifier = System.currentTimeMillis();
    String path = getRealPathToReportFile(nameOfReport, PDF_FILE_EXTENSION,folderIdentifier);
    try {
    	JasperExportManager.exportReportToPdfFile(print, path);
    }
    catch (JRException ex)  {
    	logError("[ReportBusiness]: Jasper print could not be generated.");
    	log(ex);
    	return null;
    }
    return getURIToReport(nameOfReport, PDF_FILE_EXTENSION,folderIdentifier);
  }  
  
  public String getExcelReport(JasperPrint print, String nameOfReport) {
    // prepare path
    long folderIdentifier = System.currentTimeMillis();
    String path = getRealPathToReportFile(nameOfReport, EXCEL_FILE_EXTENSION,folderIdentifier);
    // see samples of the jasper download package
    try {
      JRXlsExporter exporter = new JRXlsExporter();
      exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
      exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, path);
      exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
      exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
      exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
      exporter.exportReport();
    }
    catch (JRException ex)  {
      logError("[ReportBusiness]: Jasper print could not be generated.");
      log(ex);
      return null;
    }
    return getURIToReport(nameOfReport, EXCEL_FILE_EXTENSION,folderIdentifier);
  }  
  
  public String getSynchronizedSimpleExcelReport(QueryResult dataSource, DesignBox designBox, String nameOfReport) {
  	synchronizeAndReset(dataSource, designBox);
  	Map designerMap = designBox.getParameterMap();
  	ReportDescription reportDescription = new ReportDescription(); 
  	Iterator iterator = designBox.getDesign().getFieldsList().iterator();
  	int columnNumber = 1;
  	while (iterator.hasNext()) {
  		JRField jrField = (JRField) iterator.next();
  	    String designFieldId = jrField.getName();
  	    ReportableField reportField = new ReportableField(designFieldId, String.class);
  	    // now the customMadeFieldName of the reportField is the designFieldId
  	    // do not get confused: customMadeFieldName is an identifier, not a display string
  	    
  	    // get the already localized display name from the parameter map 
  	    String columnKey = getColumnParameter(columnNumber++);
  	    String displayName = (String) designerMap.get(columnKey);
  	    // avoid trouble
  	    if (displayName == null) {
  	    	// could be also set to ""
  	    	displayName = Integer.toString(columnNumber);
  	    }
  	    // set the header display for the column
  	    reportDescription.put(designFieldId, displayName);

  	    reportDescription.addField(reportField);
  	}
  	String reportTitle = (String) designerMap.get(DesignBox.REPORT_HEADLINE_KEY); 
  	return getSimpleExcelReportWithFileName(dataSource, reportTitle, reportDescription, nameOfReport);
  }
    
  public String getSimpleExcelReport(JRDataSource reportData, String nameOfReport, ReportDescription description) {
  	return getSimpleExcelReportWithFileName(reportData, nameOfReport, description, "report");
  }
  	
  	
  private String getSimpleExcelReportWithFileName(JRDataSource reportData, String nameOfReport, ReportDescription description, String fileName) {
    // prepare path
    long folderIdentifier = System.currentTimeMillis();
    String path = getRealPathToReportFile(fileName, EXCEL_FILE_EXTENSION,folderIdentifier);

    try {
		SimpleReportBusiness srBusiness = (SimpleReportBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(),SimpleReportBusiness.class);
		srBusiness.writeSimpleExcelFile(reportData,nameOfReport,path, description);
	}
	catch (IBOLookupException e) {
	      logError("[ReportBusiness]: The Simple Report could not be generated.");
	      log(e);
	      return null;
	}
	catch (IOException e) {
	      logError("[ReportBusiness]: The Simple Report could not be generated.");
	      log(e);
	      return null;
	}    
    
    return getURIToReport(fileName, EXCEL_FILE_EXTENSION,folderIdentifier);
  }  

  
  private String getRealPathToReportFile(String fileName, String extension, long folderIdentifier) {
    IWMainApplication mainApp = getIWApplicationContext().getIWMainApplication();
    String separator = FileUtil.getFileSeparator();
    StringBuffer path = new StringBuffer(mainApp.getApplicationRealPath());
    path.append(IWCacheManager.IW_ROOT_CACHE_DIRECTORY)
      .append(separator)
      .append(REPORT_FOLDER);

    // check if the folder exists create it if necessary
    // usually the folder should be already be there.
    // the folder is never deleted by this class
    String folderPath = path.toString();
    File[] files = FileUtil.getAllFilesInDirectory(folderPath);
    
    if(files!=null){
    	long currentTime = System.currentTimeMillis();
	    for (int i = 0; i < files.length; i++) {
	    	File file = files[i];
	    	long modifiedFile = file.lastModified();
	    	if (currentTime - modifiedFile > 300000)	{
	    		FileUtil.deleteFileAndChildren(file);
	    	}
	    }
    }
    
		path.append(separator);
		path.append(folderIdentifier);
		folderPath = path.toString();
		FileUtil.createFolder(folderPath);
		path.append(separator)
      .append(fileName)
      .append(DOT)
      .append(extension);
    return path.toString();
  }
  
  private String getURIToReport(String reportName, String extension, long folderIdentifier) {
    IWMainApplication mainApp = getIWApplicationContext().getIWMainApplication();
    String separator = "/";
    String appContextURI = mainApp.getApplicationContextURI();
    StringBuffer uri = new StringBuffer(appContextURI);
    if(!appContextURI.endsWith(separator)){
			uri.append(separator);
    }
      uri.append(IWCacheManager.IW_ROOT_CACHE_DIRECTORY)
      .append(separator)
      .append(REPORT_FOLDER)
      .append(separator)
      .append(folderIdentifier)
      .append(separator)
      .append(reportName)
      .append(DOT)
      .append(extension);
    return uri.toString();
  }

  private void synchronizeResultAndDesign(QueryResult result, DesignBox designBox)  {
  	JasperDesign reportDesign = designBox.getDesign();
  	Map designParameterMap = designBox.getParameterMap();
    List designFieldsToRemove = new ArrayList();
    // get fields of the report design
    List fields = reportDesign.getFieldsList();
    int orderNumber = 1;
    Iterator iterator = fields.iterator();
    while (iterator.hasNext())  {
      JRField jrField = (JRField) iterator.next();
      String designFieldId = jrField.getName();
      QueryResultField field = result.getFieldByOrderNumber(orderNumber);
      // sometimes the design provides more fields than necessary
      if (field == null) {
        // note the design field
        designFieldsToRemove.add(designFieldId);
      }
      else {
        result.mapDesignIdToFieldId(designFieldId, field.getId());
        String display = field.getValue(QueryResultField.DISPLAY);
        String displayKey = getColumnParameter(orderNumber);
        // be sure that the display key is set. Usually a default value is set by the design box! If not, set it now.
        // e.g. see method getDynamicDesignBox (there the designParameterMap is set)
        if (display == null || display.length() == 0)  {
        	// only set if the displayKey was not set by the design that is only set if there is no default value
        	if (! designParameterMap.containsKey(displayKey))	{
        		designParameterMap.put(displayKey, "");
        	}
        }
        else {
        	// ignore the default display that was set by the design use the display that is suggested by the query 
        	designParameterMap.put(displayKey, display);
        }
      }
      orderNumber++;
    }
    removeFields(designFieldsToRemove, reportDesign);
  }
  
  private String getColumnParameter(int orderNumber) {
  	 StringBuffer buffer = new StringBuffer(REPORT_COLUMN_PARAMETER_NAME);
     buffer.append(orderNumber);
     return buffer.toString();
  }
    
    
  private void removeFields(Collection fieldNames, JasperDesign design) {
    // prepare (get the order of the fields)
    List fieldOrder = new ArrayList();
    List fields = design.getFieldsList();
    Iterator iterator = fields.iterator();
    while (iterator.hasNext())  {
      JRField field = (JRField) iterator.next();
      fieldOrder.add(field.getName());
    }
    JRBand header = design.getColumnHeader();
    List headerChildren = header.getChildren();
    int headerChildrenSize = headerChildren.size();
    
    JRBand detail = design.getDetail();
    List detailChildren = detail.getChildren();
    int detailChildrenSize = detailChildren.size();
    
    // remove the redundant design fields 
    Iterator removeIterator = fieldNames.iterator();
    int indexCorrection = 0;
    while (removeIterator.hasNext())  {
      String fieldId = (String) removeIterator.next();
      // remove field
      design.removeField(fieldId);
      // remove expression from header
      int orderNumber = fieldOrder.indexOf(fieldId) - indexCorrection;
      if (headerChildrenSize > orderNumber) {
      	headerChildren.remove(orderNumber);
      	headerChildrenSize--;
      }
      if (detailChildrenSize > orderNumber) {
      	detailChildren.remove(orderNumber);
      	detailChildrenSize--;
      }
      // adjust the index because elements have been removed
      indexCorrection++; 
    }
  }


  public DesignBox getDesignBox(int designFileId) {
    ICFile reportDesign = getFile(designFileId);
    InputStream inputStream = null;
    try {
      inputStream = reportDesign.getFileValue();
      JasperDesign design = JasperManager.loadXmlDesign(inputStream);
      DesignBox designBox = new DesignBox();
      designBox.setDesign(design);
      return designBox;
    }
    catch (JRException jrEx) {
    	logError("[JasperReportBusiness] Problems loading design");
    	log(jrEx);
    }
    finally {
      try {
        inputStream.close();
      }
      catch (IOException streamEx)  {
      	//do not hide an existing exception 
      }
    }
    return null;
  }    
  
  public DesignBox getDynamicDesignBox(SQLQuery query, IWResourceBundle resourceBundle, IWContext iwc) throws IOException, JRException {
  	final int columnSpacing = 15;
  	int columnWidth = 120;
  	final int labelWidth = 95;
  	final int valueWidth = 55;
  	
  	Map parameterMap = new HashMap();
  	
  	DynamicReportDesign design = DynamicReportDesign.getInstanceThatShowsDateAndUser("GeneratedDesign");
  	// add date 
  	Date date = new Date();
  	String pattern = "dd.MM.yyyy, HH:mm";
  	SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
  	String formatedDate = dateFormat.format(date);
  	parameterMap.put(DynamicReportDesign.PRM_DATE, formatedDate);
  	// add user
  	User user = iwc.getCurrentUser();
  	String userName = user.getName();
  	parameterMap.put(DynamicReportDesign.PRM_USER, userName);
  	//set title
  	String queryName = query.getName();
  	parameterMap.put(DynamicReportDesign.PRM_REPORT_NAME, queryName);
  	// set total width
  	List fields = query.getFields();
  	int totalWidth = 0;
  	if (! fields.isEmpty()) {
  	  	//TODO thi: solve problem with the width of columns avoiding merging of vertical cells in excel outputs
  		// stretch with overflow merges two vertical cells, excel file can't be sorted
  		// see also and fix also ReportGenerator
  		// this is a hack ---------------------------------------------------------------------------------------------------------------------------------------------------------------
  		int numberOfFields = fields.size();
  		columnWidth = DynamicReportDesign.PAGE_WIDTH_WITHOUT_MARGINS_PORTRAIT_A4 / numberOfFields;
  		columnWidth -= columnSpacing;
  		// end of hack ------------------------------------------------------------------------------------------------------------------------------------------------------------------
  		totalWidth = ((columnSpacing + columnWidth) * fields.size())  - columnSpacing;
  	}
  	design.setColumnWidth(totalWidth);
  	// does the width fit the page width?
  	if (totalWidth > DynamicReportDesign.PAGE_WIDTH_WITHOUT_MARGINS_PORTRAIT_A4) {
  		// change to landscape!
  		design.setOrientationLandscape();
  		// does the the width now fit the page width?
  		int landscapeWidth = (totalWidth > DynamicReportDesign.PAGE_WIDTH_WITHOUT_MARGINS_LANDSCAPE_A4) ?
					totalWidth + DynamicReportDesign.PAGE_LEFT_MARGIN + DynamicReportDesign.PAGE_RIGHT_MARGIN :
					DynamicReportDesign.PAGE_WIDTH_LANDSCAPE_A4;
  		design.setPageWidth(landscapeWidth);
  		design.setPageHeight(DynamicReportDesign.PAGE_HEIGHT_LANDSCAPE_A4);
  	}
  	// set fields
  	int orderNumber = 1;
  	Iterator fieldIterator = fields.iterator();
  	while (fieldIterator.hasNext()) {
  		QueryFieldPart fieldPart = (QueryFieldPart) fieldIterator.next();
  		String fieldName =  fieldPart.getName();
  		String localizedFieldName = resourceBundle.getLocalizedString(fieldName, fieldName);
  		//String type = fieldPart.getTypeClass();
  		//TODO: thi think about that
  		String type = String.class.getName();
      String displayParameter = getColumnParameter(orderNumber);
      // !!! do not store it with the key field name but with the alias name
      String aliasName = fieldPart.getAliasName();
  		parameterMap.put(displayParameter, localizedFieldName);
  		design.addField(aliasName, displayParameter, type, columnWidth);
  		orderNumber++;
  	}
  	// add fields for values and descriptions of the dynamic fields to header
  	Map identifierInputDescriptionMap = query.getIdentifierInputDescriptionMap();
  	Map identifierValueMap = query.getIdentifierValueMap();
  	Iterator iterator = identifierInputDescriptionMap.entrySet().iterator();
  	while (iterator.hasNext()) {
  		Map.Entry entry = (Map.Entry) iterator.next();
  		String identifier = entry.getKey().toString();
  		if ( ! (DirectSQLStatement.USER_ACCESS_VARIABLE.equals(identifier) ||
  				DirectSQLStatement.GROUP_ACCESS_VARIABLE.equals(identifier) ||
					DirectSQLStatement.USER_GROUP_ACCESS_VARIABLE.equals(identifier)	) ) {
	   		InputDescription inputDescription = (InputDescription) entry.getValue();
	  		List inputValues = (List) identifierValueMap.get(identifier);
	  		// get handler
	  		String inputHandlerClassName = inputDescription.getHandlerDescription();
	  		String inputValueDisplay = null;
	  		if (inputHandlerClassName != null) { 
	  			try {
	  				Class inputHandlerClass = RefactorClassRegistry.forName(inputHandlerClassName);
	  				InputHandler inputHandler = (InputHandler) inputHandlerClass.newInstance();
	  				String[] inputValuesAsArray =  (String[]) inputValues.toArray(new String[0]);
	  				// method below throws Exception 
	  				Object resultingObject = inputHandler.getResultingObject(inputValuesAsArray, iwc);
	  				inputValueDisplay = inputHandler.getDisplayForResultingObject(resultingObject, iwc);
	  			}
	  			catch (ClassNotFoundException classEx) {
	  				logError("[JasperReportBusiness] Inputhandler class could not be found");
	  				log(classEx);
	  				inputValueDisplay = inputValues.toString();
	  			}
	  			catch (InstantiationException insEx) {
	  				logError("[JasperReportBusiness] Inputhandler class could not be instanciated");
	  				log(insEx);
	  				inputValueDisplay = inputValues.toString();
	  			}
	  			catch (IllegalAccessException accEx) {
	  				logError("[JasperReportBusiness] Inputhandler class could not be instanciated");
	  				log(accEx);
	  				inputValueDisplay = inputValues.toString();
	  			}	
	  			catch (Exception ex) {
	  				logError("[JasperReportBusiness] Resulting object could not be fetched");
	  				log(ex);
	  				inputValueDisplay = inputValues.toString();
	  			}
	  		}
	  		else {
	  			StringBuffer buffer = new StringBuffer();
	  			Iterator inputIterator = inputValues.iterator();
	  			boolean firstTime = true;
	  			while (inputIterator.hasNext()) {
	  				if (! firstTime) {
	  					buffer.append(", ");
	  				}
	  				else {
	  					firstTime = false;
	  				}
	  				buffer.append(inputIterator.next().toString());
	  			}
	  			inputValueDisplay = buffer.toString();
	  		}
	  		String description = inputDescription.getDescription();

	  		int descriptionWidth = (description == null) ? labelWidth : calculateTextFieldWidthForString(description, DynamicReportDesign.FONT_SIZE_PAGE_HEADER );
	  		int inputValueDisplayWidth = (inputValueDisplay == null) ? valueWidth : calculateTextFieldWidthForString(inputValueDisplay, DynamicReportDesign.FONT_SIZE_PAGE_HEADER);
	  		String descriptionWithPrefix = StringHandler.concat(SQLQuery.DYNAMIC_FIELD_DESCRIPTION_PREFIX, identifier);
	  		String inputValueDisplayWithPrefix = StringHandler.concat(SQLQuery.DYNAMIC_FIELD_VALUE_PREFIX, identifier);
	  		parameterMap.put(descriptionWithPrefix, description);
	  		parameterMap.put(inputValueDisplayWithPrefix, inputValueDisplay);
	  		design.addHeaderParameter(descriptionWithPrefix, descriptionWidth, inputValueDisplayWithPrefix, String.class, inputValueDisplayWidth);
  		}
  	}
  	design.close();
  	JasperDesign jasperDesign = design.getJasperDesign(iwc);
  	DesignBox designBox = new DesignBox();
  	designBox.setDesign(jasperDesign);
  	designBox.setParameterMap(parameterMap);
  	return designBox;
  }
  	
  		
  		
  	
  
  
  
  public JasperDesign getDesignFromBundle(IWBundle bundle, String layoutXMLFileName) {
	FileInputStream inputStream = null;
  	try {
	  inputStream = new FileInputStream(new File(bundle.getRealPathWithFileNameString(layoutXMLFileName)));

	  JasperDesign design = JasperManager.loadXmlDesign(inputStream);
	  return design;
	}
	catch (JRException ex)  {
	  logError("[JasperReportBusiness]: File could not be loaded");
	  log(ex);
	}
	catch (FileNotFoundException fileEx) {
	  logError("[JasperReportBusiness]: File could not be loaded");
	  log(fileEx);
	}
	finally {	
	  try {
	  	if(inputStream!=null){
	  		inputStream.close();
	  	}
	  }
	  catch (IOException streamEx)  {
	  	// do not hide an existing exception
	  }
	}
	return null;
  }    

  private ICFile getFile(int fileId)  {
    try {
      ICFileHome home = (ICFileHome) IDOLookup.getHome(ICFile.class);
      ICFile file = home.findByPrimaryKey(new Integer(fileId));
      return file;
    }
    // FinderException, RemoteException
    catch(Exception ex){
      throw new RuntimeException("[JasperReportBusiness]: Message was: " + ex.getMessage());
    }
  }     
    
    
    
    
	public JasperDesign generateLayout(JRDataSource _dataSource) throws IOException, JRException{
		int columnWidth = 120;
			
		DynamicReportDesign designTemplate = new DynamicReportDesign("GeneratedReport");
		
		ReportDescription description = null;
		if(_dataSource != null && _dataSource instanceof ReportableCollection){
			ReportableCollection rcSource = ((ReportableCollection)_dataSource);
			description = rcSource.getReportDescription();			
		}
		
		if(description != null ){
			Iterator keyIter = description.getListOfHeaderParameterLabelKeys().iterator();
			Iterator valueIter = description.getListOfHeaderParameterKeys().iterator();
			while (keyIter.hasNext()) {
				String labelKey = (String)keyIter.next();
				String valueKey = (String)valueIter.next();
				designTemplate.addHeaderParameter(labelKey,description.getWithOfParameterOrLabel(labelKey),valueKey,String.class,description.getWithOfParameterOrLabel(valueKey));
			}
			
			
			
			List allFields = description.getListOfFields();
			if(allFields != null && allFields.size() > 0){
				//TMP
				//TODO get columnspacing (15) and it to columnsWidth;
				int columnsWidth = columnWidth*allFields.size()+15*(allFields.size()-1);
				//TMP
				//TODO get page Margins (20) and add them to pageWidth;
				designTemplate.setPageWidth(columnsWidth+20+20);
				designTemplate.setColumnWidth(columnsWidth);

				//
				Iterator iter = allFields.iterator();
				while (iter.hasNext()) {
					ReportableField field = (ReportableField)iter.next();
					String name = field.getName();
					designTemplate.addField(name,field.getValueClass(),columnWidth);
				} 	
			}

		}
	
		designTemplate.close();
		return designTemplate.getJasperDesign(this.getIWApplicationContext());
	}

    
	private int calculateTextFieldWidthForString(String str, int fontSize){
		return (int)( 10 +(str.length()* fontSize *0.58));
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeUserRemove(com.idega.user.data.User)
	 */
	public void beforeUserRemove(User user, Group parentGroup) throws RemoveException, RemoteException {
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterUserCreate(com.idega.user.data.User)
	 */
	public void afterUserCreateOrUpdate(User user, Group parentGroup) throws CreateException, RemoteException {
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeGroupRemove(com.idega.user.data.Group)
	 */
	public void beforeGroupRemove(Group group, Group parentGroup) throws RemoveException, RemoteException {
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterGroupCreate(com.idega.user.data.Group)
	 */
	public void afterGroupCreateOrUpdate(Group group, Group parentGroup) throws CreateException, RemoteException {
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateEditor(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateEditor(Group group) throws RemoteException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateViewer(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateViewer(Group group) throws RemoteException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getUserPropertiesTabs(com.idega.user.data.User)
	 */
	public List getUserPropertiesTabs(User user) throws RemoteException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupPropertiesTabs(com.idega.user.data.Group)
	 */
	public List getGroupPropertiesTabs(Group group) throws RemoteException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getMainToolbarElements()
	 */
	public List getMainToolbarElements() throws RemoteException {
		List list = new ArrayList(1);
		list.add(new ReportOverviewWindowPlugin());
		return list;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupToolbarElements(com.idega.user.data.Group)
	 */
	public List getGroupToolbarElements(Group group) throws RemoteException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#isUserAssignableFromGroupToGroup(com.idega.user.data.User, com.idega.user.data.Group, com.idega.user.data.Group)
	 */
	public String isUserAssignableFromGroupToGroup(User user, Group sourceGroup, Group targetGroup) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#isUserSuitedForGroup(com.idega.user.data.User, com.idega.user.data.Group)
	 */
	public String isUserSuitedForGroup(User user, Group targetGroup) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#canCreateSubGroup(com.idega.user.data.Group,java.lang.String)
	 */
	public String canCreateSubGroup(Group group, String groupTypeOfSubGroup) throws RemoteException {
		return null;
	}
    
    
}
