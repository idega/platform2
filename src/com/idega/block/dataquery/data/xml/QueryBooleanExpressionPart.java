package com.idega.block.dataquery.data.xml;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import com.idega.util.StringHandler;
import com.idega.xml.XMLElement;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Nov 19, 2003
 */
public class QueryBooleanExpressionPart implements QueryPart {
	
	private static final List booleanOperators;
	
	static {
		booleanOperators = new ArrayList();
		booleanOperators.add("AND");
		booleanOperators.add("OR");
		booleanOperators.add("(");
		booleanOperators.add(")");
	}
	
	private static final String WHITESPACE = " ";
	private static final String AND = "AND";
	private static final String INVALID_ID_PREFIX = "[";
	private static final String INVALID_ID_SUFFIX = "]";
	
	// trivial case or default case:
	// trivial case (no conditions) <=> expressionElements is empty, badSyntaxBooleanExpression is null, booleanExpressionIsValid is true
	// following states are allowed:
	// bad syntax <=>  expressionElements is empty, badSyntaxBooleanExpression is not empty, booleanExpressionIsValid is false
	// syntax okay but not valid <=>  expressionElements is not empty, badSyntaxBooleanExpression is null, booleanExpressionIsValid is false
	// valid <=> expressionElements is not empty, badSyntaxBooleanExpression is null, booleanExpressionIsValid is true
	private List expressionElements = new ArrayList();
	private String badSyntaxBooleanExpression = null;
	private boolean booleanExpressionIsValid = true;
	
	private Set validIds = new HashSet();
	private Set invalidIds = new HashSet();
	
	public QueryBooleanExpressionPart()	{
	}
	
	public QueryBooleanExpressionPart(XMLElement xml) {
		this(xml.getTextTrim());
	}
	
	public QueryBooleanExpressionPart(String encoded) {
		StringTokenizer tokenizer = new StringTokenizer(encoded);
		while (tokenizer.hasMoreTokens()) {
			expressionElements.add(tokenizer.nextToken());
		}
	}
	
	public static QueryBooleanExpressionPart decode(String encoded)	{
		return new QueryBooleanExpressionPart(encoded);
	}

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.data.xml.QueryPart#getQueryElement()
	 */
	public XMLElement getQueryElement() {
		XMLElement el = new XMLElement(QueryXMLConstants.BOOLEAN_EXPRESSION);
		el.setText(getBooleanExpression());
		return el;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.data.xml.QueryPart#encode()
	 */
	public String encode() {
		return getBooleanExpression();
	}
	
	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.data.xml.QueryPart#isLocked()
	 */
	public boolean isLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.data.xml.QueryPart#setLocked(boolean)
	 */
	public void setLocked(boolean locked) {
		// TODO Auto-generated method stub

	}

	public String updateConditions(List conditionParts, String newBooleanExpression)	{
		// move all valid ids to the list of invalid ids
		invalidIds.addAll(validIds);
		validIds.clear();
		Iterator iteratorConditions = conditionParts.iterator();
		while (iteratorConditions.hasNext()) {
			QueryConditionPart conditionPart = (QueryConditionPart) iteratorConditions.next();
			String id = conditionPart.getId().toUpperCase();
			validIds.add(id);
			invalidIds.remove(id);
		}
		// prepare list of allowed elements
		List allowedElements = new ArrayList();
		allowedElements.addAll(QueryBooleanExpressionPart.booleanOperators);
		allowedElements.addAll(validIds);
		allowedElements.addAll(invalidIds);
		Iterator invalidElements = invalidIds.iterator();
		while (invalidElements.hasNext())	{
			StringBuffer buffer = new StringBuffer(QueryBooleanExpressionPart.INVALID_ID_PREFIX).append((String)invalidElements.next());
			buffer.append(QueryBooleanExpressionPart.INVALID_ID_SUFFIX);
			allowedElements.add(buffer.toString());
		}
		expressionElements = StringHandler.getElementsIgnoreCase(newBooleanExpression, allowedElements);
		// represents the newBooleanExpression a valid expression?
		if (expressionElements == null) {
			// store the invalid boolean expression
			badSyntaxBooleanExpression = newBooleanExpression;
			// clear expressionElements
			expressionElements = new ArrayList();
			// set invalid
			booleanExpressionIsValid = false;
			return null;
		}
		else {
			badSyntaxBooleanExpression = null;
			booleanExpressionIsValid = true;
		}
		int size = expressionElements.size();
		// change invalid elements
		for (int i = 0; i < size; i++) {
			String element = (String) expressionElements.get(i);
			if (invalidIds.contains(element)) {
				// okay: syntax is okay but it contains invalid ids
				booleanExpressionIsValid = false;
				StringBuffer buffer = new StringBuffer(QueryBooleanExpressionPart.INVALID_ID_PREFIX).append(element);
				buffer.append(QueryBooleanExpressionPart.INVALID_ID_SUFFIX);
				expressionElements.set(i, buffer.toString());
			}
		}
		// add new valids elements
		Iterator iterator = validIds.iterator();
		while (iterator.hasNext()) {
			String element = (String) iterator.next();
			if (! expressionElements.contains(element)) {
				if (! expressionElements.isEmpty()) {
					expressionElements.add(QueryBooleanExpressionPart.AND);
				}
				expressionElements.add(element);
			}
		}
		// return the new boolean expression
		return getBooleanExpression();
	}	
				
	public String getBadSyntaxBooleanExpression() {
		return badSyntaxBooleanExpression;
	}
		
					
	public String getBooleanExpression() {
		StringBuffer buffer = new StringBuffer();
		Iterator iterator = expressionElements.iterator();
		String whiteSpace = "";
		while (iterator.hasNext()) {
			buffer.append(whiteSpace).append((String) iterator.next());
			whiteSpace = QueryBooleanExpressionPart.WHITESPACE;
		}
		return buffer.toString();
	}
			
	public boolean isBooleanExpressionValid()	{
		return booleanExpressionIsValid; 
	}
	
	public boolean isSyntaxOfBooleanExpressionOkay()	{
		return badSyntaxBooleanExpression == null;
	}
	
				


}
