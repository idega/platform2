package com.idega.block.dataquery.test;

//import java.rmi.RemoteException;
//
//import com.idega.block.dataquery.business.QueryConditionPart;
//import com.idega.block.dataquery.business.QueryEntityPart;
//import com.idega.block.dataquery.business.QueryFieldPart;
//import com.idega.block.dataquery.business.QueryHelper;
//import com.idega.block.dataquery.business.QueryToSQLBridge;
//import com.idega.block.dataquery.data.QueryResult;
//import com.idega.business.IBOLookup;
//import com.idega.presentation.Block;
//import com.idega.presentation.IWContext;
//import com.idega.util.xml.XMLData;
//import com.idega.xml.XMLDocument;
//import com.idega.xml.XMLElement;

import java.util.HashMap;

import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import dori.jasper.engine.JasperExportManager;
import dori.jasper.engine.JasperFillManager;
import dori.jasper.engine.JasperManager;
import dori.jasper.engine.JasperPrint;
import dori.jasper.engine.JasperReport;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on May 22, 2003
 */
public class CreatingAndReadingQueryResultTest extends Block {

  private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.dataquery";
  
  public String getBundleIdentifier(){
    return this.IW_BUNDLE_IDENTIFIER;
  }
  
  public void main(IWContext iwc) throws Exception {  
    // test 1 modifying existing data
//    XMLData data = XMLData.getInstanceForFile(18);
//    XMLDocument document = data.getDocument();
//    XMLElement element = document.getRootElement();
//    Collection coll = element.getChildren("field");
//    Iterator iterator = coll.iterator();
//    while (iterator.hasNext())  {
//      XMLElement childElement = (XMLElement) iterator.next();
//      childElement.setText("hello world");
//    }
//    data.store();
    // test 2 creating new data
//    XMLData newData = XMLData.getInstanceWithoutExistingFile();
//    XMLElement rootElement = new XMLElement("reykjavik");
//    XMLElement newElement = new XMLElement("husavik");
//    newElement.addContent("hello world");
//    rootElement.addContent(newElement);
//    XMLDocument newDocument = new XMLDocument(rootElement);
//    newData.setDocument(newDocument);
//    newData.store();
//    // test 3 executing sql statement
//    QueryToSQLBridge bridge = getQueryToSQLBridge(iwc);
//    QueryResult result = bridge.executeStatement("select first_name from ic_user");
//    XMLElement sqlelement = result.convertToXML();
//    XMLData sqldata = XMLData.getInstanceWithoutExistingFile();
//    XMLDocument sqldocument = new XMLDocument(sqlelement);
//    sqldata.setDocument(sqldocument);
//    sqldata.store();
//    // test 4 restoring a query result
//    XMLData data4 = XMLData.getInstanceForFile(55);
//    XMLDocument document4 = data4.getDocument();
//    result = QueryResult.getInstanceForDocument(document4);
//    // test 5 reading a query from querybuilder
//    QueryHelper helper = createExample();
//    String sql = bridge.createSQLStatement(helper);
    // creating jasper reports
    String classPath = System.getProperty("java.class.path");
    DataForStupidTest data = new DataForStupidTest();
    JasperReport report = JasperManager.compileReport("/home/thomas/jasperedit/jasperedit/samples/thi_test.xml");
    JasperPrint print = JasperFillManager.fillReport(report, new HashMap(), data);
    JasperExportManager.exportReportToPdfFile(print, "/home/thomas/exampleFiles/out.pdf");
    
   
    
    
    
    
  }
  
//  private QueryToSQLBridge getQueryToSQLBridge(IWContext iwc) throws RemoteException {
//    try {
//      return (QueryToSQLBridge) IBOLookup.getServiceInstance( iwc ,QueryToSQLBridge.class);
//    }
//    catch (RemoteException ex)  {
//      throw new RuntimeException(ex.getMessage());
//    }
//  }
//  
//  private QueryHelper createExample() {
//    QueryConditionPart condition = new QueryConditionPart("username", "like", "th");
//    QueryFieldPart field = new QueryFieldPart("username","user", new String[] {"first_name"},"concat", "name of user");
//    QueryEntityPart entity = new QueryEntityPart("user","com.idega.user.data.User");
//    QueryHelper helper = new QueryHelper();
//    helper.addCondition(condition);
//    helper.addField(field);
//    helper.setSourceEntity(entity);
//    return helper;
//  }
    
    
}
