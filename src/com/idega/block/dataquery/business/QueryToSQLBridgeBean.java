package com.idega.block.dataquery.business;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import com.idega.block.dataquery.data.QueryResult;
import com.idega.block.dataquery.data.QueryResultCell;
import com.idega.block.dataquery.data.QueryResultField;
import com.idega.business.IBOServiceBean;
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
public class QueryToSQLBridgeBean extends IBOServiceBean implements QueryToSQLBridge {
  
  public String createSQLStatement(QueryHelper queryHelper)  {
    return "";
  } 
  
  public QueryResult executeStatement(String sqlQuery) throws SQLException {
    Connection connection = ConnectionBroker.getConnection();
    Statement statement = null;
    ResultSet resultSet = null;
    ResultSetMetaData metadata;
    QueryResult queryResult = new QueryResult();
    try {
      // get default connection
      statement = connection.createStatement();
      resultSet = statement.executeQuery(sqlQuery);
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
        queryResult.addField(field);
      }
      int numberOfRow = 1;
      while (resultSet.next())  {
        for (i=1 ; i <= numberOfColumns; i++)  {
          String columnValue = resultSet.getString(i);
          // store into QueryResultCell
          String numberRow = Integer.toString(numberOfRow++);
          String fieldId = Integer.toString(i);  
          QueryResultCell cell = new QueryResultCell(numberRow, fieldId, columnValue);
          queryResult.addCell(cell);
        }
      }
    }   
    catch (SQLException ex) {
      System.err.println("[QueryToSQLBridge] sql statement could not be executed. Message was: " + 
        ex.getMessage());
    }
    finally {
      if (resultSet != null) {
        resultSet.close();
      }
      if (statement != null)  {
        statement.close();
      } 
      ConnectionBroker.freeConnection(connection);
      return queryResult;
    }
  }

}