package com.idega.block.dataquery.data;

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
  
  private final String CELL = "cell";
  private final String FIELD_ID = "fieldId";
  private final String ID = "rowId";
  private final String VALUE = "value";
  
  private Object fieldId;
  private Object id;
  
  private String value = null;
    
  public QueryResultCell(Object id, Object fieldId) {
    this.fieldId = fieldId;
    this.id = id;
  }
  
  
  public QueryResultCell(Object id, Object fieldId, String value) {
    this(id, fieldId);
    setValue(value);
  }
  
  public void setValue(String value) {
    this.value = value;
  }

  public XMLElement convertToXML()  {
    XMLElement cellElement = new XMLElement(CELL);
    addElement(cellElement, FIELD_ID, fieldId.toString());
    addElement(cellElement, ID, id.toString());
    addElement(cellElement, VALUE, value);
    return cellElement;
  }

  public Object getFieldId()  {
    return fieldId;
  }

  public Object getId() {
    return id;
  }

  public String getValue() {
    return value;
  }

  private void addElement(XMLElement parent, String name, String value)  {
    XMLElement child = new XMLElement(name);
    value = (value == null) ? "" : value;
    child.addContent(value);
    parent.addContent(child);
  }
}
