package com.idega.block.dataquery.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
public class QueryResultCell {
  
  private static final String CELL = "cell";
  
  private static final String FIELD_ID = "fieldId";
  private static final String ID = "rowId";
  private static final String VALUE = "value";
  
  private String fieldId;
  private String id;
  
  private Object value = null;
  
  public static List getInstancesForELement(XMLElement element) {
    List cells = new ArrayList();
    List children = element.getChildren(CELL);
    Iterator iterator = children.iterator();
    while (iterator.hasNext())  {
      XMLElement child = (XMLElement) iterator.next();
      QueryResultCell cell = QueryResultCell.getInstanceForElement(child);
      cells.add(cell);
    }
    return cells;
  }
      
  private static QueryResultCell getInstanceForElement(XMLElement element) {
    String fieldId = QueryResultCell.getChildTextTrim(element, FIELD_ID);
    if (fieldId.length() == 0) {
      return null;
    }
    String id = QueryResultCell.getChildTextTrim(element, ID);
    if (id.length() == 0) {
      return null;
    }
    String value = QueryResultCell.getChildTextTrim(element, VALUE);
    QueryResultCell instance = new QueryResultCell(id, fieldId, value);
    return instance;
  }  
  
  public QueryResultCell(String id, String fieldId) {
    this.fieldId = fieldId;
    this.id = id;
  }
  
  
  public QueryResultCell(String id, String fieldId, Object value) {
    this(id, fieldId);
    setValue(value);
  }
  
  public void setValue(Object value) {
    this.value = value;
  }

  public XMLElement convertToXML()  {
    XMLElement cellElement = new XMLElement(CELL);
    addElement(cellElement, FIELD_ID, this.fieldId.toString());
    addElement(cellElement, ID, this.id.toString());
    addElement(cellElement, VALUE, this.value);
    return cellElement;
  }

  public Object getFieldId()  {
    return this.fieldId;
  }

  public Object getId() {
    return this.id;
  }

  public Object getValue() {
    return this.value;
  }

  private void addElement(XMLElement parent, String name, Object value)  {
    XMLElement child = new XMLElement(name);
    value = (value == null) ? "" : value;
    child.addContent(value.toString());
    parent.addContent(child);
  }
  
  private static String getChildTextTrim(XMLElement element, String name)  {
    XMLElement child = element.getChild(name);
    if (child == null) {
      return "";
    }
    return child.getText().trim();
  }  
  
  public String toString(){
  	return this.getValue().toString();
  }

}
