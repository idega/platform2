package com.idega.block.dataquery.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import com.idega.util.datastructures.SortedHashMatrix;
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
public class QueryResult implements JRDataSource {
  private static final String QUERY_RESULT = "queryResult";
  
  private static final String DEFINITION = "definition";
  private static final String CONTENT = "content";
  
  private static final String DEFAULT_VALUE = "[not found]";
  
  private List fieldOrder = new ArrayList();  
  private Map fields = new HashMap();
  
  private Map designIdFieldIdMapping = null;
  
  private SortedHashMatrix cells = new SortedHashMatrix();
  
  private Iterator cellIterator = null;
  private String currentCellId = null;
  
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
    String id = field.getId();
    fields.put(id, field);
    // store the order of the fields
    if (fieldOrder.contains(id))  {
      fieldOrder.remove(id);
    }
    fieldOrder.add(id);
  }
  
  public void addCell(QueryResultCell cell) {
    cells.put(cell.getId(), cell.getFieldId(), cell);
  }
  
  public QueryResultField getField(String id) {
    return (QueryResultField) fields.get(id);
  }
  
  public QueryResultField getFieldByOrderNumber(int orderNumber) {
    orderNumber--;
    if (orderNumber < 0 || orderNumber >= fieldOrder.size()) {
      return null;
    }
    String id = (String) fieldOrder.get(orderNumber);
    return getField(id);
  }
  
  public String getCell(String id, String fieldId)  {
    return (String) cells.get(id, fieldId);
  }
  
  public XMLElement convertToXML()  {
    XMLElement queryResult = new XMLElement(QUERY_RESULT);
    XMLElement definition = new XMLElement(DEFINITION);
    XMLElement content = new XMLElement(CONTENT);
    
    Iterator fieldsIterator = fieldOrder.iterator();
    while (fieldsIterator.hasNext())  {
      // store the order of the fields
      String id = (String) fieldsIterator.next();
      QueryResultField field = (QueryResultField) fields.get(id);
      XMLElement fieldElement = field.convertToXML();
      definition.addContent(fieldElement);
    }
    Iterator cellIterator = cells.getCopiedListOfValues().iterator();
    while (cellIterator.hasNext())  {
      QueryResultCell cell = (QueryResultCell) cellIterator.next();
      XMLElement cellElement = cell.convertToXML();
      content.addContent(cellElement);
    }
    
    queryResult.addContent(definition);
    queryResult.addContent(content);
    
    return queryResult;
  }
  
  
  public void mapDesignIdToFieldId(String designId, String fieldId)  {
    if (designIdFieldIdMapping == null)   {
      designIdFieldIdMapping = new HashMap();
    }
    designIdFieldIdMapping.put(designId, fieldId);
  }
  
  /** Returns true if there aren't any results stored.
   * Even if a QueryResult is empty you can use it as a JRDataSource,
   * that is an empty QueryResult causes no errors.
   * 
   * @return true if there aren't any results stored.
   */
  public boolean isEmpty()  {
    return cells.isEmpty();
  }
  
  public int getNumberOfRows() {
  	return cells.sizeOfFirstKeySet();
  }
  
  /** @see net.sf.jasperreports.engine.JRDataSource#next()
   * 
   */
  public boolean next() throws JRException  {
  	
    // the very first time we have to initialize the iterator  
    if (cellIterator == null) {
      cellIterator = cells.firstKeySet().iterator();
    }
    
    // now ask the iterator
    if (cellIterator.hasNext()) {
      // compare with the behaviour of a result set...
      currentCellId = (String) cellIterator.next();
      return true;
    }
    
    return false;
  }
      
  public void resetDataSource() {
    cellIterator = null;
    currentCellId = null;
   }

  /**
   *  @see net.sf.jasperreports.engine.JRDataSource#next()
   * 
   */
  public Object getFieldValue(JRField jrField) throws JRException {
    String fieldId = jrField.getName();
	
    // if mapping is required use the map
    if (designIdFieldIdMapping != null) {
      // if there is an entry use this one
      String id = (String) designIdFieldIdMapping.get(fieldId);
      if (id != null) {
        fieldId = id;
      }
    }
    
       
    // fetch the cell
    QueryResultCell cell = (QueryResultCell) cells.get(currentCellId, fieldId);
    // return the value of the cell
    
    Object value;
    if (cell == null || (value = cell.getValue()) == null )  {
      return "";
    }
    else {
      return value.toString();
    }
  }
  
  
}
