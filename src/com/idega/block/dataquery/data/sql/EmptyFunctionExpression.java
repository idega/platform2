package com.idega.block.dataquery.data.sql;

import java.util.Iterator;
import java.util.List;

import com.idega.util.StringHandler;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Oct 7, 2003
 */
public class EmptyFunctionExpression extends FunctionExpression {

  private List fieldValues = null;
  
  protected void initialize() {
    fieldValues = querySQL.getUniqueNameForField(queryField);
  }
    
  
  public String toSQLString() {
    StringBuffer buffer = new StringBuffer();
    Iterator fieldValuesIterator = fieldValues.iterator();
    boolean useComma = false;
    while (fieldValuesIterator.hasNext()) {
      String value = (String) fieldValuesIterator.next();
      if (useComma)   {
        buffer.append(", ");
      }
      else {
        useComma = true;
      }
      buffer.append(value).append(' ');
    }
    return buffer.toString();  
  }
    
  public boolean isValid() {
    return StringHandler.elementsAreNotEmpty(fieldValues);
  } 
  

}
