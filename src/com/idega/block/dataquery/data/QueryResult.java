package com.idega.block.dataquery.data;

import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import com.idega.xml.XMLDocument;
import com.idega.xml.XMLElement;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on May 27, 2003
 */
public class QueryResult {
  private static final String QUERY_RESULT = "queryResult";
  
  private static final String DEFINITION = "definition";
  private static final String CONTENT = "content";
    
  private SortedMap fields = new TreeMap();
  private SortedMap cells = new TreeMap();
  
  public static QueryResult getInstanceForDocument(XMLDocument document)  {
    QueryResult instance = new QueryResult(); 
    XMLElement root = document.getRootElement();
    // definition 
    XMLElement definition = root.getChild(DEFINITION);
    List fields = QueryResultField.getInstancesForELement(definition);
    Iterator fieldIterator = fields.iterator();
    while (fieldIterator.hasNext()) {
      QueryResultField field = (QueryResultField) fieldIterator.next();
      if (field != null) {
        instance.addField(field);
      }
    }
    // content
    XMLElement content = root.getChild(CONTENT);
    List cells = QueryResultCell.getInstancesForELement(content);
    Iterator cellIterator = cells.iterator();
    while (cellIterator.hasNext()) {
      QueryResultCell cell = (QueryResultCell) cellIterator.next();
      if (cell != null) {
        instance.addCell(cell);
      }
    }    
    return instance;
  }
       
 
  public void addField(QueryResultField field) {
    fields.put(field.getId(), field);
  }
  
  public void addCell(QueryResultCell cell) {
    cells.put(cell.getId(), cell);
  }
  
  public String getField(String id) {
    return (String) fields.get(id);
  }
  
  public String getCell(String id)  {
    return (String) cells.get(id);
  }
  
  public XMLElement convertToXML()  {
    XMLElement queryResult = new XMLElement(QUERY_RESULT);
    XMLElement definition = new XMLElement(DEFINITION);
    XMLElement content = new XMLElement(CONTENT);
    
    Iterator fieldsIterator = fields.values().iterator();
    while (fieldsIterator.hasNext())  {
      QueryResultField field = (QueryResultField) fieldsIterator.next();
      XMLElement fieldElement = field.convertToXML();
      definition.addContent(fieldElement);
    }
    Iterator cellIterator = cells.values().iterator();
    while (cellIterator.hasNext())  {
      QueryResultCell cell = (QueryResultCell) cellIterator.next();
      XMLElement cellElement = cell.convertToXML();
      content.addContent(cellElement);
    }
    
    queryResult.addContent(definition);
    queryResult.addContent(content);
    
    return queryResult;
  }
  
}
