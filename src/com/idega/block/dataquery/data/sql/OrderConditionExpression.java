package com.idega.block.dataquery.data.sql;

import java.util.List;

import com.idega.block.dataquery.business.QueryOrderConditionPart;

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
	
	private int orderPriority = 0;
	
  public OrderConditionExpression(QueryOrderConditionPart orderConditionPart, QuerySQL querySQL) {
  	initialize(orderConditionPart, querySQL);
  }
  
  protected void initialize(QueryOrderConditionPart orderConditionPart, QuerySQL querySQL)	{
  	String field = orderConditionPart.getField();
  	path = orderConditionPart.getPath();
  	isAscendant = orderConditionPart.isAscendant();
  	List fieldValueList = querySQL.getUniqueNameForField(path,field);
  	if (fieldValueList.size() != 1)	{
  		// something wrong
  		return;
  	}
  	valueField = (String) fieldValueList.get(0);
  	orderPriority = orderConditionPart.getOrderPriority();
  }
  
   	
  public String toSQLString() {
  	StringBuffer buffer = new StringBuffer(valueField);
  	if (! isAscendant) {
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
		return path;
	}
}
