package com.idega.block.datareport.business;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.idega.block.dataquery.data.QueryResult;
import com.idega.block.dataquery.data.QueryResultField;
import com.idega.business.IBOServiceBean;
import com.idega.core.data.ICFile;
import com.idega.core.data.ICFileHome;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWMainApplication;
import com.idega.util.FileUtil;

import dori.jasper.engine.JRBand;
import dori.jasper.engine.JRDataSource;
import dori.jasper.engine.JRException;
import dori.jasper.engine.JRExporterParameter;
import dori.jasper.engine.JRField;
import dori.jasper.engine.JasperExportManager;
import dori.jasper.engine.JasperFillManager;
import dori.jasper.engine.JasperManager;
import dori.jasper.engine.JasperPrint;
import dori.jasper.engine.JasperReport;
import dori.jasper.engine.design.JasperDesign;
import dori.jasper.engine.export.JRXlsExporter;
import dori.jasper.engine.export.JRXlsExporterParameter;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jun 10, 2003
 */
public class JasperReportBusinessBean extends IBOServiceBean implements JasperReportBusiness {
  
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
  
  public JasperPrint printSynchronizedReport(QueryResult dataSource, Map parameterMap, JasperDesign design) {
    JasperPrint print;
    synchronizeResultAndDesign(dataSource, parameterMap, design);
    // henceforth we treat the QueryResult as a JRDataSource, 
    // therefore we reset the QueryResult to prepare it 
    dataSource.resetDataSource(); // resets only the DataSource functionality (sets the pointer to the first row)
    try {
      print = getReport(dataSource, parameterMap, design);
    }
    catch (JRException ex)  {
      System.err.println("[ReportBusiness]: Jasper print could not be generated.");
      ex.printStackTrace(System.err);
      return null;
    }
    return print;
  }
  
  public String getHtmlReport(JasperPrint print, String nameOfReport) {
    // prepare path
    String path = getRealPathToReportFile(nameOfReport, HTML_FILE_EXTENSION);
    try {
      JasperExportManager.exportReportToHtmlFile(print, path);
    }
    catch (JRException ex)  {
      System.err.println("[ReportBusiness]: Jasper print could not be generated.");
      ex.printStackTrace(System.err);
      return null;
    }
    return getURIToReport(nameOfReport, HTML_FILE_EXTENSION);
  }
  
  public String getPdfReport(JasperPrint print, String nameOfReport) {
    // prepare path
    String path = getRealPathToReportFile(nameOfReport, PDF_FILE_EXTENSION);
    try {
      JasperExportManager.exportReportToPdfFile(print, path);
    }
    catch (JRException ex)  {
      System.err.println("[ReportBusiness]: Jasper print could not be generated.");
      ex.printStackTrace(System.err);
      return null;
    }
    return getURIToReport(nameOfReport, PDF_FILE_EXTENSION);
  }  
  
  public String getExcelReport(JasperPrint print, String nameOfReport) {
    // prepare path
    String path = getRealPathToReportFile(nameOfReport, EXCEL_FILE_EXTENSION);
    // see samples of the jasper download package
    try {
      JRXlsExporter exporter = new JRXlsExporter();
      exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
      exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, path);
      exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
      exporter.exportReport();
    }
    catch (JRException ex)  {
      System.err.println("[ReportBusiness]: Jasper print could not be generated.");
      ex.printStackTrace(System.err);
      return null;
    }
    return getURIToReport(nameOfReport, EXCEL_FILE_EXTENSION);
  }  
    

  
  public String getRealPathToReportFile(String fileName, String extension) {
    IWMainApplication mainApp = getIWApplicationContext().getApplication();
    String separator = FileUtil.getFileSeparator();
    StringBuffer path = new StringBuffer(mainApp.getApplicationRealPath());
    path.append(mainApp.getIWCacheManager().IW_ROOT_CACHE_DIRECTORY)
      .append(separator)
      .append(REPORT_FOLDER);

    // check if the folder exists create it if necessary
    // usually the folder should be already be there.
    // the folder is never deleted by this class
    String folderPath = path.toString();
    FileUtil.createFolder(folderPath);
    path.append(separator)
      .append(fileName)
      .append(DOT)
      .append(extension);
    return path.toString();
  }
  
  private String getURIToReport(String reportName, String extension) {
    IWMainApplication mainApp = getIWApplicationContext().getApplication();
    String separator = FileUtil.getFileSeparator();
    StringBuffer uri = new StringBuffer(mainApp.getApplicationContextURI());
    uri.append(separator)
      .append(mainApp.getIWCacheManager().IW_ROOT_CACHE_DIRECTORY)
      .append(separator)
      .append(REPORT_FOLDER)
      .append(separator)
      .append(reportName)
      .append(DOT)
      .append(extension);
    return uri.toString();
  }

  public void synchronizeResultAndDesign(QueryResult result, Map parameterMap, JasperDesign reportDesign)  {
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
        StringBuffer buffer = new StringBuffer(REPORT_COLUMN_PARAMETER_NAME);
        buffer.append(orderNumber);
        parameterMap.put(buffer.toString(), display);
      }
      orderNumber++;
    }
    removeFields(designFieldsToRemove, reportDesign);
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
    
    JRBand detail = design.getDetail();
    List detailChildren = detail.getChildren();
    
    // remove the redundant design fields 
    Iterator removeIterator = fieldNames.iterator();
    int indexCorrection = 0;
    while (removeIterator.hasNext())  {
      String fieldId = (String) removeIterator.next();
      // remove field
      design.removeField(fieldId);
      // remove expression from header
      int orderNumber = fieldOrder.indexOf(fieldId) - indexCorrection;
      headerChildren.remove(orderNumber);
      detailChildren.remove(orderNumber); 
      // adjust the index because elements have been removed
      indexCorrection++; 
    }
  }


  public JasperDesign getDesign(int designFileId) {
    ICFile reportDesign = getFile(designFileId);
    InputStream inputStream = null;
    try {
      inputStream = reportDesign.getFileValue();
      JasperDesign design = JasperManager.loadXmlDesign(inputStream);
      inputStream.close();
      return design;
    }
    catch (Exception ex)  {
      System.err.println("[JasperReportBusiness]: File could not be read");
      ex.printStackTrace(System.err);
      try {
        inputStream.close();
      }
      catch (IOException streamEx)  {
      }
      return null;
    }
  }    

  private ICFile getFile(int fileId)  {
    try {
      ICFileHome home = (ICFileHome) IDOLookup.getHome(ICFile.class);
      ICFile file = (ICFile) home.findByPrimaryKey(new Integer(fileId));
      return file;
    }
    // FinderException, RemoteException
    catch(Exception ex){
      throw new RuntimeException("[JasperReportBusiness]: Message was: " + ex.getMessage());
    }
  }     
    
}
