package com.idega.block.dataquery.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.idega.xml.XMLElement;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on May 26, 2003
 */
public class QueryResultField {
  
  private static final String FIELD = "field";
  private static final String ID = "id";
  
  public static final String ENTITY = "entity";
  public static final String COLUMN = "column";
  public static final String TYPE = "type";
  public static final String DISPLAY = "display"; 
  
  private Object id;
  
  private Map nameValue = new HashMap();
    
  public QueryResultField(Object id) {
    this.id = id;
  }    
  
  public void setValue(String name, String value) {
    nameValue.put(name, value);
  }
  
  public String getValue(String name) {
    return (String) nameValue.get(name);
  }
  
  public Object getId() {
    return id;
  }

  public XMLElement convertToXML()  {
    XMLElement fieldElement = new XMLElement(FIELD);
    addElement(fieldElement, ID, id.toString());
    Iterator iterator = nameValue.entrySet().iterator();
    while (iterator.hasNext())  {
      Map.Entry entry = (Map.Entry) iterator.next(); 
      addElement(fieldElement, (String) entry.getKey(), (String) entry.getValue());
    }
    return fieldElement;
  }
  
  
  private void addElement(XMLElement parent, String name, String value)  {
    XMLElement child = new XMLElement(name);
    value = (value == null) ? "" : value;
    child.addContent(value);
    parent.addContent(child);
  }
}