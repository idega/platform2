package com.idega.block.dataquery.business;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.idega.block.dataquery.data.QueryResult;
import com.idega.block.dataquery.data.QueryResultCell;
import com.idega.block.dataquery.data.QueryResultField;
import com.idega.block.dataquery.data.sql.SQLQuery;
import com.idega.block.dataquery.data.xml.QueryHelper;
import com.idega.business.IBOServiceBean;
import com.idega.presentation.IWContext;
import com.idega.util.database.ConnectionBroker;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on May 27, 2003
 */
public class QueryToSQLBridgeBean extends IBOServiceBean    implements QueryToSQLBridge{
	
  
  public SQLQuery createQuerySQL(QueryHelper queryHelper, IWContext iwc) throws QueryGenerationException  {
  	// avoid trouble with other users that use the query at the same time
  	if (! iwc.isLoggedOn()) {
  		throw new QueryGenerationException("User is not logged on");
  	}
  	String uniqueIdentifier = Integer.toString(iwc.getCurrentUserId());
    SQLQuery sqlQuery = SQLQuery.getInstance(queryHelper, uniqueIdentifier, iwc);
    return sqlQuery;
  }
  
  /**
   *  Use this method if you do not need to print or show the executed sql statements
   */
  public QueryResult executeQueries(SQLQuery sqlQuery)	{
  	// set -1 that is ignore limit of number of rows
  	return executeQueries(sqlQuery, -1,  new ArrayList());
  }

	/** 
	 * Use this method for printing or showing the executed sql statements
	 */
  public QueryResult executeQueries(SQLQuery sqlQuery, int numberOfRowsLimit, List executedSQLStatements) {
  	QueryResult queryResult = null;
  	List postStatements = new ArrayList();
		Connection connection = ConnectionBroker.getConnection();
  	try {
  		queryResult = executeSQL(sqlQuery, numberOfRowsLimit, connection, postStatements, executedSQLStatements);
  	}
  	catch (SQLException sqlEx) {
			logError("[QueryToSQLBridge] Statements could not be executed");
			log(sqlEx);
  	}
  	finally {
	  	// drop created views 
	  	// delete the view in reverse direction because of dependencies
  		try {
		  	for (int i = postStatements.size() - 1; i > -1 ; i--) {
		  		String postStatement = (String) postStatements.get(i);
		  		try {
		  			executePostStatement(postStatement,connection, executedSQLStatements);
		  		}
		  		catch (SQLException ex){
		  			logError("[QueryToSQLBridge] post sql statements could not be executed. ");
		  			log(ex);
		  			// go further to the next post statement
		  		}
		  	}
  		}
  		// do not catch any exceptions but try to close the connection if an exception has been thrown
  		finally {
  			ConnectionBroker.freeConnection(connection);
  		}
  	}
  	return queryResult;  			
  }
  
  private void executePostStatement(String postStatement, Connection connection, List executedSQLStatements) throws SQLException {
		Statement statement = connection.createStatement();
		try {
  		statement.execute(postStatement);
  		executedSQLStatements.add(postStatement);
		}
		catch (SQLException ex) {
			String message =
				"[QueryToSQLBridge]: Can't execute " + postStatement;
			System.err.println(message + " Message is: " + ex.getMessage());
			ex.printStackTrace(System.err);
			throw ex;
		}
  }
 
	private QueryResult executeSQL(SQLQuery sqlQuery, int numberOfRowsLimit, Connection connection, List postStatements, List executedSQLStatements) throws SQLException	{
  	// go back to the very first query
  	SQLQuery currentQuery = sqlQuery;
  	while (currentQuery.hasPreviousQuery())	{
  		currentQuery = currentQuery.previousQuery();
  	}
  	do {
  		if (currentQuery.hasNextQuery())	{
   			// mark the generated table
  			String postStatement = executePreQuery(connection, currentQuery, executedSQLStatements);
  			postStatements.add(postStatement);
  			currentQuery = currentQuery.nextQuery();
  		}
  		else {
  			return executeQuery(connection, currentQuery, numberOfRowsLimit, executedSQLStatements);
  		}
  	}
  	while (true);
  }
  			
 		
  
	private String executePreQuery(Connection connection, SQLQuery sqlQuery, List executedSQLStatements)	throws SQLException {
		String postStatement = null;
		String viewTableName = null;
		Statement statement = connection.createStatement();
		try {
			String sqlStatement = sqlQuery.toSQLString();
			viewTableName = sqlQuery.getMyTableName();
			// create view if desired
			if (sqlQuery.isUsableForCreatingAView()) {
				StringBuffer buffer = new StringBuffer("CREATE VIEW ");
				buffer.append(viewTableName);
				buffer.append(" ( ");
				List fieldNames = sqlQuery.getAliasFieldNames();
				Iterator fieldIterator = fieldNames.iterator();
				String separator = "";
				while (fieldIterator.hasNext())	{
					String fieldName = (String) fieldIterator.next();
					buffer.append(separator);
					buffer.append(fieldName);
					separator = " , ";
				}
				buffer.append(" )  AS ");
				buffer.append(sqlStatement); 
				sqlStatement = buffer.toString();
				// create postStatement
		  	StringBuffer postBuffer = new StringBuffer("DROP VIEW ").append(viewTableName);
		  	postStatement = postBuffer.toString();
			}
			else {
				postStatement = sqlQuery.getPostStatement();
			}
			// execute statement
	    statement.execute(sqlStatement);
	    executedSQLStatements.add(sqlStatement);
		}
    catch (SQLException ex) {
      System.err.println("[QueryToSQLBridge] sql statement could not be executed. Message was: " + 
        ex.getMessage());
        throw ex;
    }
    finally {
    	// do not hide an existing exception
    	try {
    		if (statement != null)  {
    			statement.close();
    		}
    	}
    	catch (SQLException statementCloseEx) {
    		logError("[QueryToSQLBridge] statement could not be closed");
    		log(statementCloseEx);
    	}
    }
    return postStatement;
	}

	private QueryResult executeQuery(Connection connection, SQLQuery sqlQuery, int numberOfRowsLimit, List executedSQLStatements) throws SQLException	{
		Statement statement = connection.createStatement();
		String sqlStatement = sqlQuery.toSQLString();
		List displayNames = sqlQuery.getDisplayNames();
    ResultSet resultSet = null;
    ResultSetMetaData metadata;
    QueryResult queryResult = new QueryResult();
    try {
      // get default connection
      statement = connection.createStatement();
      executedSQLStatements.add(sqlStatement);
      resultSet = statement.executeQuery(sqlStatement);
      metadata = resultSet.getMetaData();
      
      int numberOfColumns = metadata.getColumnCount();
      int i;
      for (i=1; i <= numberOfColumns; i++) {
        // String columnClass = metadata.getColumnClassName(i);
        String columnName = metadata.getColumnName(i);
        // store into QueryResultField
        QueryResultField field = new QueryResultField(Integer.toString(i));
        // field.setValue(QueryResultField.TYPE, columnClass);
        field.setValue(QueryResultField.COLUMN, columnName);
        // set display name
        setDisplayName(field, i, displayNames);
        queryResult.addField(field);
      }
      int numberOfRow = 1;
      // if number of rows is less than zero ignore the limit of rows, that is to get all rows choose -1 for example
       while (resultSet.next() && (numberOfRowsLimit < 0 || numberOfRow <= numberOfRowsLimit))  {
        String id = Integer.toString(numberOfRow++);
        for (i=1 ; i <= numberOfColumns; i++)  {
          Object columnValue = resultSet.getObject(i);
          // store into QueryResultCell
          String fieldId = Integer.toString(i);  
          QueryResultCell cell = new QueryResultCell(id, fieldId, columnValue);
		  // !!!!!!!!! do NOT use the following statement because the columnName is NOT necessarily unique if you use more than one table : 
          //QueryResultCell cell = new QueryResultCell(id, metadata.getColumnName(i), columnValue);
          queryResult.addCell(cell);
        }
      }
    }   
    catch (SQLException sqlEx) {
      	logError("[QueryToSQLBridge] sql statement could not be executed. Message was: " + 
        sqlEx.getMessage());
        throw sqlEx;
    }
    finally {
    	// do not hide an existing exception
    	try { 
	      if (resultSet != null) {
	        resultSet.close();
	      }
    	}
	    catch (SQLException resultCloseEx) {
	     	logError("[QueryToSQLBridge] result set could not be closed");
	     	log(resultCloseEx);
	    }
	    // do not hide an existing exception
	    try {
	     if (statement != null)  {
        statement.close();
	     }
	    }
 	    catch (SQLException statementCloseEx) {
	     	logError("[QueryToSQLBridge] statement could not be closed");
	     	log(statementCloseEx);
	    }
    }
    return queryResult;
  }		

  
  private void setDisplayName(QueryResultField field, int index, List displayNames)  {
    if (displayNames == null || index > displayNames.size())  {
      return;
    }
    // index within a list starts with 0 not 1
    String display = (String) displayNames.get(index - 1);
    if (display == null || display.length() == 0) {
      return;
    }
    field.setValue(QueryResultField.DISPLAY, display);
  }
}