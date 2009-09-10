package com.idega.block.dataquery.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
  
  private Map nameValue = new HashMap();
  
  // set default values
  { this.nameValue.put(ENTITY,"");
    this.nameValue.put(COLUMN,"");
    this.nameValue.put(TYPE,"");
    this.nameValue.put(DISPLAY,"");
  }

  private String id;
     
  public static List getInstancesForELement(XMLElement element) {
    List fields = new ArrayList();
    List children = element.getChildren(FIELD);
    Iterator iterator = children.iterator();
    while (iterator.hasNext())  {
      XMLElement child = (XMLElement) iterator.next();
      QueryResultField field = QueryResultField.getInstanceForElement(child);
      fields.add(field);
    }
    return fields;
  }
      
  private static QueryResultField getInstanceForElement(XMLElement element) {
    String id = QueryResultField.getChildTextTrim(element, ID);
    if (id.length() == 0) {
      return null;
    }
    QueryResultField instance = new QueryResultField(id);
    Iterator iterator = instance.nameValue.entrySet().iterator();
    while (iterator.hasNext())  {
      Map.Entry entry = (Map.Entry) iterator.next();
      String name = (String) entry.getKey();
      String value = QueryResultField.getChildTextTrim(element, name);
      entry.setValue(value);
    }
    return instance;
  }
  
  public QueryResultField(String id) {
    this.id = id;
  }    
  
  public void setValue(String name, String value) {
    this.nameValue.put(name, value);
  }
  
  public String getValue(String name) {
    return (String) this.nameValue.get(name);
  }
  
  public String getId() {
    return this.id;
  }

  public XMLElement convertToXML()  {
    XMLElement fieldElement = new XMLElement(FIELD);
    addElement(fieldElement, ID, this.id.toString());
    Iterator iterator = this.nameValue.entrySet().iterator();
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
  
  private static String getChildTextTrim(XMLElement element, String name)  {
    XMLElement child = element.getChild(name);
    if (child == null) {
      return "";
    }
    return child.getText().trim();
  }
}