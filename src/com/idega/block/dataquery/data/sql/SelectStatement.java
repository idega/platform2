package com.idega.block.dataquery.data.sql;

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
public class SelectStatement {
  
  private final String SELECT = "SELECT";
  private final String FROM = "FROM";
  private final String WHERE = "WHERE";
  private final String AND = "AND";
  private final char WHITE_SPACE = ' ';
  private final char COMMA = ',';
  
  private List innerClauses = new ArrayList();
  private List outerClauses = new ArrayList();
  private List selectClauses = new ArrayList();
  private List whereClauses = new ArrayList();

  public void addInnerJoin(Expression join) {
    innerClauses.add(join);
  }
  
  public void addOuterJoin(Expression join)	{
  	outerClauses.add(join);
  }
  
  public void addSelectClause(Expression clause) {
    selectClauses.add(clause);
  }

  public void addWhereClause(Expression criterion) {
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
      Expression clause = (Expression) select.next();
      expression.append(spacing).append(clause.toSQLString());
      spacing = whiteSpaceCommaWhiteSpace;
    }

    spacing = new StringBuffer().append(WHITE_SPACE).append(FROM).append(WHITE_SPACE);
    Iterator inner= innerClauses.iterator();
    while (inner.hasNext())  {
      Expression clause = (Expression) inner.next();
      expression.append(spacing).append(clause.toSQLString());
      spacing = whiteSpaceCommaWhiteSpace;
    }
    
    Iterator outer= outerClauses.iterator();
    while (outer.hasNext())  {
      Expression clause = (Expression) outer.next();
      expression.append(clause.toSQLString());
    }
    
    spacing = new StringBuffer().append(WHITE_SPACE).append(WHERE).append(WHITE_SPACE);
    StringBuffer and = new StringBuffer().append(WHITE_SPACE).append(AND).append(WHITE_SPACE);
    Iterator where = whereClauses.iterator();
    while (where.hasNext()) {
      Expression clause = (Expression) where.next();
      expression.append(spacing).append(clause.toSQLString());
      spacing = and;
    }
    
    return expression.toString();
  }
}   

