package com.idega.block.dataquery.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jun 4, 2003
 */
public class SQLQueryExpression {
  
  private final String SELECT = "SELECT";
  private final String FROM = "FROM";
  private final String WHERE = "WHERE";
  private final String AND = "AND";
  private final char WHITE_SPACE = ' ';
  private final char COMMA = ',';
  
  private List fromClauses = new ArrayList();
  private List selectClauses = new ArrayList();
  private List whereClauses = new ArrayList();

  public void addFromClause(String clause, String alias) {
    StringBuffer buffer = new StringBuffer(clause);
    buffer.append(WHITE_SPACE);
    buffer.append(alias);
    fromClauses.add(buffer.toString());
  }
  
  public void addSelectClause(String clause) {
    selectClauses.add(clause);
  }

  public void addSelectClauses(Collection clauses) {
    selectClauses.addAll(clauses);
  }
     
  public void addWhereClause(SQLCriterionExpression criterion) {
    whereClauses.add(criterion);
  }
  
  public void addWhereClauses(Collection criteria)  {
    whereClauses.add(criteria);
  }
  
  public String toSQLString() {
    StringBuffer expression =  new StringBuffer();
    
    StringBuffer whiteSpaceCommaWhiteSpace =
      new StringBuffer(WHITE_SPACE).append(COMMA).append(WHITE_SPACE);
    
    StringBuffer spacing = new StringBuffer(SELECT).append(WHITE_SPACE);
    Iterator select = selectClauses.iterator();
    while (select.hasNext())  {
      String clause = (String) select.next();
      expression.append(spacing).append(clause);
      spacing = whiteSpaceCommaWhiteSpace;
    }

    spacing = new StringBuffer().append(WHITE_SPACE).append(FROM).append(WHITE_SPACE);
    Iterator from = fromClauses.iterator();
    while (from.hasNext())  {
      String clause = (String) from.next();
      expression.append(spacing).append(clause);
      spacing = whiteSpaceCommaWhiteSpace;
    }
    
    spacing = new StringBuffer().append(WHITE_SPACE).append(WHERE).append(WHITE_SPACE);
    StringBuffer and = new StringBuffer().append(WHITE_SPACE).append(AND).append(WHITE_SPACE);
    Iterator where = whereClauses.iterator();
    while (where.hasNext()) {
      SQLCriterionExpression clause = (SQLCriterionExpression) where.next();
      expression.append(spacing).append(clause.toSQLString());
      spacing = and;
    }
    
    return expression.toString();
  }
}   

