package com.idega.block.dataquery.test;

import java.util.Collection;
import java.util.Iterator;

import com.idega.block.dataquery.business.QueryToSQLBridge;
import com.idega.block.dataquery.data.QueryResult;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.util.xml.XMLData;
import com.idega.xml.XMLDocument;
import com.idega.xml.XMLElement;

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
    XMLData data = XMLData.getInstanceForFile(18);
    XMLDocument document = data.getDocument();
    XMLElement element = document.getRootElement();
    Collection coll = element.getChildren("field");
    Iterator iterator = coll.iterator();
    while (iterator.hasNext())  {
      XMLElement childElement = (XMLElement) iterator.next();
      childElement.setText("hello world");
    }
    data.store();
    // test 2 creating new data
    XMLData newData = XMLData.getInstanceWithoutExistingFile();
    XMLElement rootElement = new XMLElement("reykjavik");
    XMLElement newElement = new XMLElement("husavik");
    newElement.addContent("hello world");
    rootElement.addContent(newElement);
    XMLDocument newDocument = new XMLDocument(rootElement);
    newData.setDocument(newDocument);
    newData.store();
    // test 3 executing sql statement
    QueryResult result = QueryToSQLBridge.executeStatement("select first_name from ic_user");
    XMLElement sqlelement = result.convertToXML();
    XMLData sqldata = XMLData.getInstanceWithoutExistingFile();
    XMLDocument sqldocument = new XMLDocument(sqlelement);
    sqldata.setDocument(sqldocument);
    sqldata.store();
    
  }
}
