package com.idega.block.dataquery.data.sql;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.idega.block.dataquery.data.xml.QueryBooleanExpressionPart;
import com.idega.util.StringHandler;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Nov 19, 2003
 */
public class CriteriaExpression implements DynamicExpression {
	
	private final static String LEFT_BRACKET = "(";
	private final static String RIGHT_BRACKET = ")";
	
	private boolean isDynamic = false;
	
	private String booleanExpression = null;
	
	private Map idCriterionMap = new LinkedHashMap(0);
	
	public CriteriaExpression(QueryBooleanExpressionPart booleanExpressionPart) {
		booleanExpression = booleanExpressionPart.getBooleanExpression();
	}
	
	public void add(CriterionExpression criterion)	{
		// criteria is dynamic if it contains at least one dynamic criterion
		isDynamic = isDynamic || criterion.isDynamic();
		idCriterionMap.put(criterion.getId(), criterion);
	}

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.data.sql.DynamicExpression#isDynamic()
	 */
	public boolean isDynamic() {
		return isDynamic;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.data.sql.DynamicExpression#getIdentifierValueMap()
	 */

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.data.sql.DynamicExpression#getIdentifierDescriptionMap()
	 */
	public Map getIdentifierInputDescriptionMap() {
		Map resultMap = new LinkedHashMap();
		Iterator iterator = idCriterionMap.values().iterator();
		while(iterator.hasNext())	{
			CriterionExpression expression = (CriterionExpression) iterator.next();
			if (expression.isDynamic())	{
				resultMap.putAll(expression.getIdentifierInputDescriptionMap());
			}
		}
		return resultMap;
	}

	public Map getIdentifierValueMap() {
		Map resultMap = new LinkedHashMap();
		Iterator iterator = idCriterionMap.values().iterator();
		while(iterator.hasNext())	{
			CriterionExpression expression = (CriterionExpression) iterator.next();
			if (expression.isDynamic())	{
				resultMap.putAll(expression.getIdentifierValueMap());
			}
		}
		return resultMap;
	}
	
	
	
	public void setIdentifierValueMap(Map identifierValueMap) {
		Iterator iterator = idCriterionMap.values().iterator();
		while(iterator.hasNext())	{
			CriterionExpression expression = (CriterionExpression) iterator.next();
			if (expression.isDynamic())	{
				expression.setIdentifierValueMap(identifierValueMap);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.data.sql.Expression#toSQLString()
	 */
	public String toSQLString() {
		Map replaceMap = new HashMap();
		Iterator iterator = idCriterionMap.entrySet().iterator();
		while (iterator.hasNext())	{
			Map.Entry entry = (Map.Entry) iterator.next();
			String key = (String) entry.getKey();
			CriterionExpression criterion = (CriterionExpression) entry.getValue();
			String value = criterion.toSQLString();
			StringBuffer buffer = new StringBuffer(LEFT_BRACKET);
			buffer.append(value).append(RIGHT_BRACKET);
			replaceMap.putAll(StringHandler.getReplaceMapIgnoreCase(booleanExpression, key, buffer.toString())); 
		}
		// replace the booleanExpression
		int index = 0;
		StringBuffer buffer = new StringBuffer();
		while (index < booleanExpression.length()) {
			Integer indexInt = new Integer(index);
			String replace = null;
			if (replaceMap.containsKey(indexInt)) {
				replace = (String) replaceMap.get(indexInt);
				index++;
			}
			else {
				replace = booleanExpression.substring(index, ++index);
			}
			buffer.append(replace);
		}
		return buffer.toString();
	}
		

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.data.sql.Expression#isValid()
	 */
	public boolean isValid() {
		return true;
	}

}
