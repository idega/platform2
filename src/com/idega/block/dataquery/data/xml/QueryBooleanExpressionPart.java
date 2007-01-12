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
		booleanOperators.add("NOT");
	}
	
	private static final String WHITESPACE = " ";
	private static final String AND = "AND";
	
	// do not choose prefix and suffix that are similar to the allowed operators!
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
			this.expressionElements.add(tokenizer.nextToken());
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
		if (conditionParts == null) {
			conditionParts = new ArrayList(0);
		}
		// move all valid ids to the list of invalid ids
		this.invalidIds.addAll(this.validIds);
		this.validIds.clear();
		Iterator iteratorConditions = conditionParts.iterator();
		while (iteratorConditions.hasNext()) {
			QueryConditionPart conditionPart = (QueryConditionPart) iteratorConditions.next();
			String id = conditionPart.getId().toUpperCase();
			this.validIds.add(id);
			this.invalidIds.remove(id);
		}
		// prepare list of allowed elements
		List allowedElements = new ArrayList();
		allowedElements.addAll(QueryBooleanExpressionPart.booleanOperators);
		allowedElements.addAll(this.validIds);
		allowedElements.addAll(this.invalidIds);
		Iterator invalidElements = this.invalidIds.iterator();
		while (invalidElements.hasNext())	{
			StringBuffer buffer = new StringBuffer(QueryBooleanExpressionPart.INVALID_ID_PREFIX).append((String)invalidElements.next());
			buffer.append(QueryBooleanExpressionPart.INVALID_ID_SUFFIX);
			allowedElements.add(buffer.toString());
		}
		this.expressionElements = StringHandler.getElementsIgnoreCase(newBooleanExpression, allowedElements);
		// represents the newBooleanExpression a valid expression?
		if (this.expressionElements == null) {
			// store the invalid boolean expression
			this.badSyntaxBooleanExpression = newBooleanExpression;
			// clear expressionElements
			this.expressionElements = new ArrayList();
			// set invalid
			this.booleanExpressionIsValid = false;
			return null;
		}
		else {
			this.badSyntaxBooleanExpression = null;
			this.booleanExpressionIsValid = true;
		}
		int size = this.expressionElements.size();
		// change invalid elements
		for (int i = 0; i < size; i++) {
			String element = (String) this.expressionElements.get(i);
			if (this.invalidIds.contains(element)) {
				// okay: syntax is okay but it contains invalid ids
				this.booleanExpressionIsValid = false;
				StringBuffer buffer = new StringBuffer(QueryBooleanExpressionPart.INVALID_ID_PREFIX).append(element);
				buffer.append(QueryBooleanExpressionPart.INVALID_ID_SUFFIX);
				this.expressionElements.set(i, buffer.toString());
			}
			// there are already invalid elements
			else if (element.startsWith(INVALID_ID_PREFIX)) {
				this.booleanExpressionIsValid = false;
			}
		}
		// add new valids elements
		Iterator iterator = this.validIds.iterator();
		while (iterator.hasNext()) {
			String element = (String) iterator.next();
			if (! this.expressionElements.contains(element)) {
				if (! this.expressionElements.isEmpty()) {
					this.expressionElements.add(QueryBooleanExpressionPart.AND);
				}
				this.expressionElements.add(element);
			}
		}
		// return the new boolean expression
		return getBooleanExpression();
	}	
				
	public String getBadSyntaxBooleanExpression() {
		return this.badSyntaxBooleanExpression;
	}
		
					
	public String getBooleanExpression() {
		StringBuffer buffer = new StringBuffer();
		Iterator iterator = this.expressionElements.iterator();
		String whiteSpace = "";
		while (iterator.hasNext()) {
			buffer.append(whiteSpace).append((String) iterator.next());
			whiteSpace = QueryBooleanExpressionPart.WHITESPACE;
		}
		return buffer.toString();
	}
			
	public boolean isBooleanExpressionValid()	{
		return this.booleanExpressionIsValid; 
	}
	
	public boolean isSyntaxOfBooleanExpressionOkay()	{
		return this.badSyntaxBooleanExpression == null;
	}
	
				


}
