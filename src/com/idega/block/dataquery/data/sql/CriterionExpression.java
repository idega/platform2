package com.idega.block.dataquery.data.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import com.idega.block.dataquery.data.QueryConstants;
import com.idega.block.dataquery.data.xml.QueryConditionPart;
import com.idega.business.InputHandler;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.util.StringHandler;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jun 4, 2003
 */
public class CriterionExpression implements DynamicExpression {
  
  public static final char DOT = '.';
  public static final char WHITE_SPACE = ' ';
  public static final char APOSTROPHE = '\'';
  public static final char BRACKET_OPEN = '(';
  public static final char BRACKET_CLOSE = ')';
  public static final String OR = "OR";
  
  private static final String INTEGER = Integer.class.getName(); 
  

  private Object identifier = null;
  private String id = null;

  private String valueField = null;
  private String firstColumnClass = null;
  private String comparison = null;
  
  private String patternField = null;
  
  private boolean isDynamic = false;
  private Map identifierValueMap = null;
  private Map identifierInputDescriptionMap = null;
  
  private String inputHandlerClass = null;
  private String inputHandlerDescription = null;
  
  private Map typeSQL = null;
  private IWContext iwc = null;
  
  { 
    typeSQL = new HashMap();
    typeSQL.put(QueryConditionPart.TYPE_LIKE, "LIKE");
    typeSQL.put(QueryConditionPart.TYPE_EQ, "=");
    typeSQL.put(QueryConditionPart.TYPE_NEQ, "<>");
    typeSQL.put(QueryConditionPart.TYPE_LT, "<");
    typeSQL.put(QueryConditionPart.TYPE_GT, ">");    
    typeSQL.put(QueryConditionPart.TYPE_GEQ, ">=");
    typeSQL.put(QueryConditionPart.TYPE_LEQ, "<=");
  }
     
	public CriterionExpression(QueryConditionPart condition, Object identifier, SQLQuery sqlQuery, IWContext iwc)	{
		this.id = condition.getId();
		this.identifier = identifier;
		this.iwc = iwc;
		initialize(condition, sqlQuery, iwc);
	}	
  
  protected void initialize(QueryConditionPart condition, SQLQuery sqlQuery, IWContext iwc)	{
  	String field = condition.getField();
  	String path = condition.getPath();
 
  	// set Handler 
  	inputHandlerDescription = sqlQuery.getHandlerDescriptionForField(path, field); 
  	inputHandlerClass = sqlQuery.getInputHandlerForField(path, field);
  	firstColumnClass = sqlQuery.getTypeClassForField(path, field);
  	valueField =  sqlQuery.getUniqueNameForField(path,field);
    String type = condition.getType();
    comparison = (String) typeSQL.get(type);
    // set pattern
    identifierValueMap = new HashMap();
    String patternFieldName = condition.getPatternField();
  	String patternPath = condition.getPatternPath();
  	if (patternFieldName == null && patternPath == null) {
		  Object pattern = null;
		  if (condition.hasMoreThanOnePattern()) {
		  	pattern = condition.getPatterns();
		  }
		  else {
		  	pattern = condition.getPattern();
		  }
		 	identifierValueMap.put(identifier, pattern);
  	}
  	else {
  		patternField = sqlQuery.getUniqueNameForField(patternPath,patternFieldName);
   	}
   	identifierInputDescriptionMap = new HashMap();
   	String description = condition.getDescription();
   	if (description == null || description.length() == 0)	{
   	IWResourceBundle iwrb = getResourceBundle(iwc);
   		// localization
   		String localizedType = iwrb.getLocalizedString(StringHandler.concat(QueryConstants.LOCALIZATION_CONDITION_TYPE_PREFIX, type), type);
   		String localizedField = iwrb.getLocalizedString(field, field);
  		StringBuffer buffer = new StringBuffer(localizedField).append(" ").append(localizedType);
  		description =  buffer.toString();
   	}
		identifierInputDescriptionMap.put(identifier, new InputDescription(description, inputHandlerClass, inputHandlerDescription));
		isDynamic = condition.isDynamic();
  }
  
  private IWResourceBundle getResourceBundle(IWContext iwc) {
		Locale locale = iwc.getApplicationSettings().getDefaultLocale();
		return iwc.getIWMainApplication().getBundle(QueryConstants.QUERY_BUNDLE_IDENTIFIER).getResourceBundle(locale);
	}

  	

  public String toSQLString() {
    StringBuffer expression = 
      new StringBuffer(valueField).append(WHITE_SPACE);
    expression.append(comparison);
    Object pattern = identifierValueMap.get(identifier);
    InputHandler inputHandler = getInputHandler();
    // if the pattern is a collection use OR operator
    if (pattern != null && pattern instanceof Collection) {
    	// if there is an inputhandler get the resulting objects
    	if (inputHandler != null)  {
    		String[] patternAsArray = (String[]) ((Collection)pattern).toArray(new String[0]);
    		try {
    			pattern = inputHandler.getResultingObject(patternAsArray, iwc);
    		}
    		catch (Exception ex) {};
    	}
    	Iterator iterator = ((Collection) pattern).iterator();
    	if (((Collection) pattern).size() == 1) {
				return getSingleCondition(iterator.next()).toString();
    	}
			StringBuffer buffer = new StringBuffer();
    	while (iterator.hasNext()) {
    		Object singlePattern = iterator.next();
    		buffer.append(BRACKET_OPEN).append(WHITE_SPACE);
    		StringBuffer singleCondition = getSingleCondition( singlePattern);
    		buffer.append(singleCondition);
    		buffer.append(WHITE_SPACE).append(BRACKET_CLOSE);
    		if (iterator.hasNext()) {
    			buffer.append(WHITE_SPACE).append(OR).append(WHITE_SPACE);
    		}
    	}
    	return buffer.toString();
    }    	
    else if (patternField != null) {
    	expression.append(WHITE_SPACE).append(patternField);
    	return expression.toString();
    }	
    // if there is an inputhandler get the resulting object
    String patternAsString = pattern.toString();
    if (inputHandler != null)  {
    	String[] patternAsArray = new String[] { patternAsString };
    	try {
    		pattern = inputHandler.getResultingObject(patternAsArray, iwc);
    	}
    	catch (Exception ex) {
    	}
    }
    return getSingleCondition(pattern).toString();
  }
  
  private StringBuffer getSingleCondition(Object patternObject) {
  	// change pattern to the corresponding string for the given type
  	if (inputHandlerClass != null && patternObject != null) {
  		InputHandler inputHandler = getInputHandler();
  		patternObject = inputHandler.convertSingleResultingObjectToType(patternObject, firstColumnClass).toString();
  	}
    StringBuffer expression = new StringBuffer(valueField).append(WHITE_SPACE);
    expression.append(comparison);
    if (patternObject != null)  {
      expression.append(WHITE_SPACE);
      // if it is an integer do not use apostrophe: age = 22
      if (INTEGER.equals(firstColumnClass)) {
        expression.append(patternObject);
      }
      // else use apostrophe: name = 'Thomas'
      else {
        expression.append(APOSTROPHE).append(patternObject).append(APOSTROPHE);
      }
    }
    return expression;
  }
    	
  public boolean isDynamic() {
  	return isDynamic;
  }
  
  public String getId()	{
  	return id;
  }
  
  public Map getIdentifierValueMap() {
  	return identifierValueMap;
  }
  
  public Map getIdentifierInputDescriptionMap()	{
  	return identifierInputDescriptionMap;
  }
  
  public void setIdentifierValueMap(Map identifierValueMap)	{
  	this.identifierValueMap = identifierValueMap;
  }
  
  public String getInputHandlerDescription() {
  	return inputHandlerDescription;
  }
  
  public InputHandler getInputHandler() {
  	// sometimes there isn't an inputhandler 
  	if (inputHandlerClass == null) {
  		return null;
  	}
  	InputHandler inputHandler = null;
  	try {
  		inputHandler = (InputHandler) Class.forName(inputHandlerClass).newInstance();
  	}
		catch (ClassNotFoundException ex) {
			//TODO: thi implement log 
//			log(ex);
//			logError("[CriterionExpression] Could not retrieve handler class");
		}
		catch (InstantiationException ex) {
//			log(ex);
//			logError("[CriterionExpression] Could not instanciate handler class");
		}
		catch (IllegalAccessException ex) {
//			log(ex);
//			logError("[CriterionExpression] Could not instanciate handler class");
		}
		return inputHandler;
  }
  
  public boolean isValid() {
    return true;
//    (
//       StringHandler.isNotEmpty(valueField) &&
//       StringHandler.isNotEmpty(pattern) &&
//       StringHandler.isNotEmpty(comparison));
  } 
    

}
