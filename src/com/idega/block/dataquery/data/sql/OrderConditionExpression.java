package com.idega.block.dataquery.data.sql;

import com.idega.block.dataquery.data.xml.QueryOrderConditionPart;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Nov 13, 2003
 */



public class OrderConditionExpression implements Expression {
	
	public static final String DESCENDENT = "DESC";
	public static final char WHITE_SPACE = ' ';
	
	private boolean isAscendant = true;
	
	private String valueField = null;
	private String path = null;
	
  public OrderConditionExpression(QueryOrderConditionPart orderConditionPart, SQLQuery sqlQuery) {
  	initialize(orderConditionPart, sqlQuery);
  }
  
  protected void initialize(QueryOrderConditionPart orderConditionPart, SQLQuery sqlQuery)	{
  	String field = orderConditionPart.getField();
  	this.path = orderConditionPart.getPath();
  	this.isAscendant = orderConditionPart.isAscendant();
  	this.valueField = sqlQuery.getUniqueNameForField(this.path,field);
  }
  
   	
  public String toSQLString() {
  	StringBuffer buffer = new StringBuffer(this.valueField);
  	if (! this.isAscendant) {
  		buffer.append(WHITE_SPACE);
  		buffer.append(DESCENDENT);
  	}
  	return buffer.toString();
	}

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.data.sql.Expression#isValid()
	 */
	public boolean isValid() {
		// TODO Auto-generated method stub
		return true;
	}

	public String getPath()	{
		return this.path;
	}
}
