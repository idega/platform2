package com.idega.block.dataquery.business;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import org.doomdark.uuid.UUID;
import org.doomdark.uuid.UUIDGenerator;
import com.idega.block.dataquery.data.QueryLog;
import com.idega.block.dataquery.data.QueryLogHome;
import com.idega.block.dataquery.data.QueryResult;
import com.idega.block.dataquery.data.QueryResultCell;
import com.idega.block.dataquery.data.QueryResultField;
import com.idega.block.dataquery.data.sql.SQLQuery;
import com.idega.block.dataquery.data.xml.QueryHelper;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
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
  	String uniqueIdentifier = getUniqueIndentifier(iwc);
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
  	// we are not using transactions because some databases don't support transactions and rollbacks
  	String transactionID = Long.toString(System.currentTimeMillis());
  	QueryResult queryResult = null;
  	List postStatements = new ArrayList();
		Connection connection = ConnectionBroker.getConnection();
  	try {
  		queryResult = executeSQL(sqlQuery, numberOfRowsLimit, connection, postStatements, executedSQLStatements, transactionID);
  	}
  	catch (CreateException sqlEx) {
		logError("[QueryToSQLBridge] Entries for query log could not be created");
		log(sqlEx);
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
		  	try {
		  		removeFromQueryLog(transactionID, connection);
		  	}
		  	catch (EJBException ex) {
	  			logError("[QueryToSQLBridge] Problems with QueryLog.");
	  			log(ex);
		  	}
			catch (RemoveException ex) {
	  			logError("[QueryToSQLBridge] Problems with QueryLog.");
	  			log(ex);
			}
			catch (FinderException ex) {
	  			logError("[QueryToSQLBridge] Problems with QueryLog.");
	  			log(ex);
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
			logError("[QueryToSQLBridge]: Can't execute " + postStatement);
			log(ex);
			throw ex;
		}
  }
 
	private QueryResult executeSQL(SQLQuery sqlQuery, int numberOfRowsLimit, Connection connection, List postStatements, List executedSQLStatements, String transactionID) throws SQLException, CreateException	{
  	// go back to the very first query
  	SQLQuery currentQuery = sqlQuery;
  	while (currentQuery.hasPreviousQuery())	{
  		currentQuery = currentQuery.previousQuery();
  	}
  	do {
  		if (currentQuery.hasNextQuery())	{
   			// mark the generated table
  			String postStatement = executePreQuery(connection, currentQuery, executedSQLStatements, transactionID);
  			postStatements.add(postStatement);
  			currentQuery = currentQuery.nextQuery();
  		}
  		else {
  			return executeQuery(connection, currentQuery, numberOfRowsLimit, executedSQLStatements);
  		}
  	}
  	while (true);
  }
  			
 		
  
	private String executePreQuery(Connection connection, SQLQuery sqlQuery, List executedSQLStatements, String transactionID)	throws SQLException, CreateException {
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
			addToQueryLog(postStatement, transactionID);
			statement.execute(sqlStatement);
			executedSQLStatements.add(sqlStatement);
		}
		catch (CreateException crEx) {
			logError("[QueryToSQLBridge] An entry for the query log could not be created.");
			log(crEx);
			throw crEx;
		}
		catch (SQLException ex) {
			logError("[QueryToSQLBridge] sql statement could not be executed.");
			log(ex);
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
	      	logError("[QueryToSQLBridge] sql statement could not be executed."); 
	        log(sqlEx);
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
  
  private void addToQueryLog(String statement, String transactionID) throws CreateException {
  	QueryLogHome queryLogHome = getQueryLogHome();
  	QueryLog queryLog = queryLogHome.create();
  	queryLog.setStatement(statement);
  	queryLog.setTransactionID(transactionID);
  	queryLog.store();
  }
  
  private void removeFromQueryLog(String transactionID, Connection connection) throws EJBException, FinderException, RemoveException {
  	QueryLogHome queryLogHome = getQueryLogHome();
  	Collection queryLogs = queryLogHome.findAll();
  	Iterator iterator = queryLogs.iterator();
  	while (iterator.hasNext()) {
  		QueryLog queryLog = (QueryLog) iterator.next();
  		String queryLogTransactionID = queryLog.getTransactionID();
  		if (transactionID.equals(queryLogTransactionID)) {
  			queryLog.remove();
  		}
  		else {
  			// some logs found that don't bleong to this transaction
  			// how old are these entries?
  			long transactionTime = Long.parseLong(queryLogTransactionID);
  	    	long currentTime = System.currentTimeMillis();
  	    	// older than 10 minutes are being deleted
	    	if (currentTime - transactionTime > 600000)	{
	    		executeStatement(queryLog, connection);
	    	}
  		}
  	}
  }
  	
  private void executeStatement(QueryLog queryLog, Connection connection) {
  	String logStatement = queryLog.getStatement();
  	Statement statement;
	try {
		statement = connection.createStatement();
		try {
			statement.execute(logStatement);
		}
		catch (SQLException e) {
			// ignore, sometimes the statement is causing errors, e.g. dropping a view that already was dropped
		}
	  	queryLog.remove();
	}
	catch (SQLException e) {
		logError("[QueryToSQLBridge] Could not create statement");
		log(e);
	}
	catch (EJBException e) {
		logError("[QueryToSQLBridge] Could not remove query log");
		log(e);
	}
	catch (RemoveException e) {
		logError("[QueryToSQLBridge] Could not remove query log");
		log(e);
	}
  }
  
  private QueryLogHome getQueryLogHome() {
	try {
		return (QueryLogHome) IDOLookup.getHome(QueryLog.class);
	}
	catch (IDOLookupException e) {
		throw new RuntimeException("[QueryToSQLBridge] Could not look up QueryLogHome");
	}
  }
  
  private String getUniqueIndentifier(IWContext iwc) {
  	// user id plus random number (put user id at the beginning because the string is shortened)
	UUIDGenerator generator = UUIDGenerator.getInstance();
	UUID uuid = generator.generateRandomBasedUUID();
	int currentUserId = iwc.getCurrentUserId();
  	StringBuffer buffer = new StringBuffer();
  	buffer.append(currentUserId).append(uuid);
  	String identifier = buffer.toString();
  	return identifier.replaceAll("-", "");
  }
}