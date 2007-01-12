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
  
  private int currentRowNumber = 0;
  private int desiredNumberOfRows = -1;
  
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
        instance.addCell(cell.getId(), cell.getFieldId(), cell.getValue());
      }
    }    
    return instance;
  }
       
 
  public void addField(QueryResultField field) {
    String id = field.getId();
    this.fields.put(id, field);
    // store the order of the fields
    if (this.fieldOrder.contains(id))  {
      this.fieldOrder.remove(id);
    }
    this.fieldOrder.add(id);
  }
  
  public void addCell(Object id, Object fieldId, Object cellValue) {
  	this.cells.put(id,fieldId, cellValue);
  }
 
  public QueryResultField getField(String id) {
    return (QueryResultField) this.fields.get(id);
  }
  
  public QueryResultField getFieldByOrderNumber(int orderNumber) {
    orderNumber--;
    if (orderNumber < 0 || orderNumber >= this.fieldOrder.size()) {
      return null;
    }
    String id = (String) this.fieldOrder.get(orderNumber);
    return getField(id);
  }
  
//  public String getCell(String id, String fieldId)  {
//    return (String) cells.get(id, fieldId);
//  }
  
  public XMLElement convertToXML()  {
    XMLElement queryResult = new XMLElement(QUERY_RESULT);
    XMLElement definition = new XMLElement(DEFINITION);
    XMLElement content = new XMLElement(CONTENT);
    
    Iterator fieldsIterator = this.fieldOrder.iterator();
    while (fieldsIterator.hasNext())  {
      // store the order of the fields
      String id = (String) fieldsIterator.next();
      QueryResultField field = (QueryResultField) this.fields.get(id);
      XMLElement fieldElement = field.convertToXML();
      definition.addContent(fieldElement);
    }
    
    Iterator xKeyIterator = this.cells.firstKeySet().iterator();
    while (xKeyIterator.hasNext()) {
    	Object xKey = xKeyIterator.next();
    	Map yDimension = this.cells.get(xKey);
    	Iterator yKeyIterator = yDimension.keySet().iterator();
    	while (yKeyIterator.hasNext()) {
    		Object yKey = yKeyIterator.next();
    		Object cellValue = yDimension.get(yKey);
    		QueryResultCell cell = new QueryResultCell(xKey.toString(), yKey.toString(), cellValue);
    		XMLElement cellElement = cell.convertToXML();
    		content.addContent(cellElement);
    	}
    }
   
    queryResult.addContent(definition);
    queryResult.addContent(content);
    
    return queryResult;
  }
  
  
  public void mapDesignIdToFieldId(String designId, String fieldId)  {
    if (this.designIdFieldIdMapping == null)   {
      this.designIdFieldIdMapping = new HashMap();
    }
    this.designIdFieldIdMapping.put(designId, fieldId);
  }
  
  /** Returns true if there aren't any results stored.
   * Even if a QueryResult is empty you can use it as a JRDataSource,
   * that is an empty QueryResult causes no errors.
   * 
   * @return true if there aren't any results stored.
   */
  public boolean isEmpty()  {
    return this.cells.isEmpty();
  }
  
  public int getNumberOfRows() {
  	int realNumber = this.cells.sizeOfFirstKeySet();
  	return  (realNumber < this.desiredNumberOfRows || this.desiredNumberOfRows == -1) ? realNumber : this.desiredNumberOfRows;
  }
 
  /** @see net.sf.jasperreports.engine.JRDataSource#next()
   * 
   */
  public boolean next() throws JRException  {
  	
    // the very first time we have to initialize the iterator  
    if (this.cellIterator == null) {
    	this.currentRowNumber = 0;
    	this.cellIterator = this.cells.firstKeySet().iterator();
    }
    
    // now ask the iterator
    if (this.cellIterator.hasNext()) {
    	// compare with the behaviour of a result set...
    	this.currentCellId = (String) this.cellIterator.next();
    	this.currentRowNumber++;
      	if (this.currentRowNumber <= this.desiredNumberOfRows || this.desiredNumberOfRows == -1) {
      		return true;
      	}
    }
    return false;
  }
      
  public void resetDataSource() {
    this.cellIterator = null;
    this.currentCellId = null;
    this.currentRowNumber = 0;
   }

  /**
   *  @see net.sf.jasperreports.engine.JRDataSource#next()
   * 
   */
  public Object getFieldValue(JRField jrField) throws JRException {
    String fieldId = jrField.getName();
	
    // if mapping is required use the map
    if (this.designIdFieldIdMapping != null) {
      // if there is an entry use this one
      String id = (String) this.designIdFieldIdMapping.get(fieldId);
      if (id != null) {
        fieldId = id;
      }
    }
    // fetch the cellValue
    Object cellValue = this.cells.get(this.currentCellId, fieldId);
    // return the value of the cell
     return (cellValue == null ) ? "" : cellValue.toString();
    }
  
  public void setDesiredNumberOfRows(int desiredNumberOfRows) {
  	this.desiredNumberOfRows = desiredNumberOfRows;
  }
  
}
