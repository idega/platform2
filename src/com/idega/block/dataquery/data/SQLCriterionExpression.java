package com.idega.block.dataquery.data;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jun 4, 2003
 */
public class SQLCriterionExpression {
  
  public final String LIKE = "LIKE";
  
  public final char DOT = '.';
  public final char WHITE_SPACE = ' ';
  public final char APOSTROPHE = '\'';
  
  private String firstTable = null;
  private String firstColumn = null;
  private String pattern = null;
  private String comparison = null;
  
  public void add(String table, String column, String pattern, String type)  {
    firstTable = table;
    firstColumn = column;
    this.pattern = pattern;
    if (type != null && LIKE.equals(type.toUpperCase()))  {
      comparison = LIKE;
    }
  }
    
  public String toSQLString() {
    StringBuffer expression = 
      new StringBuffer(firstTable).append(DOT).append(firstColumn).append(WHITE_SPACE);
    expression.append(comparison);
    if (pattern != null)  {
      expression.append(WHITE_SPACE).append(APOSTROPHE).append(pattern).append(APOSTROPHE);
    }
    return expression.toString();
  }
    

}
