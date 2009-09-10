package com.idega.block.dataquery.data.sql;

import java.util.HashMap;
import java.util.Map;

import com.idega.block.dataquery.data.xml.QueryFieldPart;
import com.idega.block.dataquery.data.xml.QueryXMLConstants;
import com.idega.repository.data.RefactorClassRegistry;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Oct 2, 2003
 */
public abstract class FunctionExpression implements Expression {
	
	protected QueryFieldPart queryField = null;
  
  private static final Map CLASS_FUNCTION;
  
  private static final String CONCATENATION_CLASS = ConcatenationExpression.class.getName();
  private static final String SIMPLE_FUNCTION_CLASS = SimpleFunctionExpression.class.getName();
   
  
  static {
    CLASS_FUNCTION = new HashMap();
    CLASS_FUNCTION.put(QueryXMLConstants.FUNC_CONCAT, CONCATENATION_CLASS);
    CLASS_FUNCTION.put(QueryXMLConstants.FUNC_COUNT,  SIMPLE_FUNCTION_CLASS);
    CLASS_FUNCTION.put(QueryXMLConstants.FUNC_COUNT_DISTINCT, SIMPLE_FUNCTION_CLASS);
    CLASS_FUNCTION.put(QueryXMLConstants.FUNC_MAX, SIMPLE_FUNCTION_CLASS);
    CLASS_FUNCTION.put(QueryXMLConstants.FUNC_MIN, SIMPLE_FUNCTION_CLASS);
    CLASS_FUNCTION.put(QueryXMLConstants.FUNC_SUM, SIMPLE_FUNCTION_CLASS);
    CLASS_FUNCTION.put(QueryXMLConstants.FUNC_AVG,   SIMPLE_FUNCTION_CLASS); 
  }

  public static FunctionExpression getInstance(QueryFieldPart queryField, SQLQuery sqlQuery) {
    FunctionExpression functionExpression = null;
    String function = queryField.getFunction();
    String className = (String) CLASS_FUNCTION.get(function);
    if (className != null)  {
      try {
        Class functionClass = RefactorClassRegistry.forName(className);
        functionExpression = (FunctionExpression) functionClass.newInstance();
      }
      catch (ClassNotFoundException ex) {
        String errorMessage = "[SQLFunctionExpression] Appropriate class for function " + function + " was not found";
        System.err.println(errorMessage);
        ex.printStackTrace(System.err);
        functionExpression = new EmptyFunctionExpression();
      }
      catch (InstantiationException instantEx) {
        String errorMessage = "[SQLFunctionExpression] Appropriate class for function " + function + " could not be instantiated";
        System.err.println(errorMessage);
        instantEx.printStackTrace(System.err);
        functionExpression = new EmptyFunctionExpression();
      }
      catch (IllegalAccessException illEx)  {
        String errorMessage = "[SQLFunctionExpression] Appropriate class for function " + function + " was not allowed to be created";
        System.err.println(errorMessage);
        illEx.printStackTrace(System.err);
        functionExpression = new EmptyFunctionExpression();
      }
    }
    else {
      functionExpression = new EmptyFunctionExpression();      
    }
    functionExpression.queryField = queryField;
    functionExpression.initialize(sqlQuery);
    return functionExpression;
  }
    
  protected abstract void initialize(SQLQuery sqlQuery);

}
