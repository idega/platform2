package com.idega.block.reports.business;



import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;

import com.idega.block.reports.data.ReportItem;
import com.idega.util.database.ConnectionBroker;



/**

 * Title:

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:      idega multimedia

 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>

 * @version 1.0

 */



public class ReportMaker {



  public ReportMaker(){



  }

  public List makeReport(String sql){

    List v = this.searchInDatabase(sql);

    return v;

  }



  public List makeReport(Vector conds){

    String sql = worker(conds);

    List v = this.searchInDatabase(sql);

    return v;

  }



  public List makeReport(List conds,ReportCondition RCx){

    conds.add(RCx);

    String sql = worker(conds);

    List v = this.searchInDatabase(sql);

    return v;

  }



  public String makeSQL(Vector conds){

    return worker(conds);

  }



  private String worker(List conds){

    ReportCondition cond;

    Vector vSelect = new Vector();

    Vector vTables = new Vector();

    Vector vJoin = new Vector();

    Vector vWhere = new Vector();

    Vector vOrder = new Vector();

		Vector vGroupBy = new Vector();

		Vector vHaving = new Vector();

    TreeMap orderMap = new TreeMap();



    ReportItem item;

    int length = conds.size();

    String field;

    for (int i = 0; i < length; i++) {

      cond = (ReportCondition) conds.get(i);

      field = cond.getField();

      item = cond.getItem();



      String c = cond.getCondition();

      boolean hascondition = c != null && c.length() > 0 ;



      if(cond.isSelect() || hascondition){

        if(cond.isSelect()){

          System.err.println("item "+item.getName()+" is select");

          if(field!= null){

            if(!vSelect.contains(field)){

              vSelect.add(field);

            }

          }

          if(!cond.isFunction() && !vGroupBy.contains(field)){

            vGroupBy.add(field);

          }

        }



        if(item.getMainTable()!=  null){

          if(!vTables.contains(item.getMainTable())){

            System.err.println("adding main table "+ item.getMainTable());

            vTables.add(item.getMainTable());

          }

        }



        String[] sa = item.getJoinTable();

        if(sa != null){

          for (int j = 0; j < sa.length; j++) {

            if(!vTables.contains(sa[j])){

              System.err.println("adding join table "+sa[j]);

              vTables.add(sa[j]);

            }

          }

        }

        String sJoin = item.getJoin();

        if(sJoin.length() > 1){

          StringTokenizer ST = new StringTokenizer(sJoin,",;");

          while(ST.hasMoreTokens()){

            String join = ST.nextToken();

            if(!vJoin.contains(join))

              vJoin.add(join);

          }

        }



        if(hascondition ){

          if(!cond.isFunction() && !vWhere.contains(c))

            vWhere.add(c);

          else if(!vHaving.contains(c))

            vHaving.add(c);

        }



        Integer order = cond.getOrder();

        if(order != null){

          orderMap.put(order,field);

        }

      }

    }

    if(orderMap.size() > 0){

      Collection C = orderMap.values();

      vOrder.addAll(C);

    }

    // check Group by

    if(vGroupBy.size() == length){

      vGroupBy.clear();

    }



    String sql = makeSQL(vSelect,vTables,vJoin,vWhere,vOrder,vGroupBy,vHaving);

    return sql;

  }



  private String makeSQL(List Select,List From,List Join,List Where,List Order,List GroupBy,List Having){

    ///////////////////////////

    //  Select part

    ///////////////////////////

    StringBuffer sql = new StringBuffer("SELECT  ");

    int len = Select.size();

    for(int i = 0; i < len ; i++){

      sql.append(Select.get(i));

      if(i < len-1)

        sql.append(",");

    }

    sql.append(" ");



    ///////////////////////

    //  Table part

    //////////////////////

    int flen = From.size();

    if(flen > 0){

      sql.append(" FROM ");

      for(int i = 0; i < flen ; i++){

        sql.append(From.get(i));

        if(i < flen-1)

          sql.append(",");

      }

      sql.append(" ");

    }



    /////////////////////

    //  Join Part

    ////////////////////

    boolean ifwhere = false;

    len = Join.size();

    //System.err.println("join length "+len );

    // use the join if  we have more than two tables

    //

    if(len > 0 && flen > 1){

      sql.append(" WHERE ");

      ifwhere = true;

      for(int i = 0; i < len ; i++){

        sql.append(Join.get(i));

        if(i < len-1)

          sql.append(" AND ");

      }

    }

    sql.append(" ");

    ////////////////////////

    //  Where part

    ///////////////////////

    len = Where.size();

    if(len > 0 ){

      if(ifwhere)

        sql.append(" AND ");

      else

        sql.append(" WHERE ");

      for(int i = 0; i < len ; i++){

        sql.append(Where.get(i));

        if( i < len-1)

          sql.append(" AND ");

      }

    }

		sql.append(" ");

    ////////////////////////

    //  Group by part

    ///////////////////////

    if(GroupBy != null){

      len = GroupBy.size();

      if(len > 0 ){

        sql.append( "GROUP BY ");

        for(int i = 0; i < len ; i++){

          sql.append(GroupBy.get(i));

          if(i < len-1)

            sql.append(",");

        }

      }

    }

		sql.append(" ");

    ////////////////////////

    //  Having part

    ///////////////////////

    if(Having != null){

      len = Having.size();

      if(len > 0 ){

        sql.append( " HAVING ");

        for(int i = 0; i < len ; i++){

          sql.append(Having.get(i));

          if(i < len-1)

            sql.append(" AND ");

        }

      }

    }

    sql.append(" ");

    ////////////////////////

    //  Order by part

    ///////////////////////

    if(Order != null){

      len = Order.size();

      if(len > 0 ){

        sql.append( "ORDER BY ");

        for(int i = 0; i < len ; i++){

          sql.append(Order.get(i));

          if(i < len-1)

            sql.append(",");

        }

      }

    }

    System.err.println(sql.toString());

    return sql.toString();

  }



  private Vector[] makeVectorArray(int size){

    Vector[] v = new Vector[size];

    for(int i = 0; i < size;i++){

      v[i] = new Vector();

    }

    return v;

  }

  private Vector searchInDatabase(String SQL){

    Vector v = new Vector();

    Connection Conn = null;
    Statement stmt = null;
    ResultSet RS = null;
    

    try{

      Conn = ConnectionBroker.getConnection();

      stmt = Conn.createStatement();

      RS = stmt.executeQuery(SQL);

      ResultSetMetaData MD = RS.getMetaData();

      int count = MD.getColumnCount();

      String temp;

      ReportContent RC;

      String[] s;

      while(RS.next()){

        s = new String[count];

        for(int i = 1; i <= count; i++){

          temp = RS.getString(i);

          s[i-1] = temp!=null?temp:"";

        }

        RC = new ReportContent(s);

        v.add(RC);

      }
    }

    catch(SQLException e){

      e.printStackTrace();

    }

    finally {
    	// do not hide an existing exception
    	try { 
    		if (RS != null) {
    			RS.close();
	      	}
    	}
	    catch (SQLException resultCloseEx) {
	    	System.err.println("[ReportMaker] result set could not be closed");
	     	resultCloseEx.printStackTrace(System.err);
	    }
	    // do not hide an existing exception
	    try {
	    	if (stmt != null)  {
	    		stmt.close();
	    	    com.idega.util.database.ConnectionBroker.freeConnection(Conn);
	    	}
	    }
 	    catch (SQLException statementCloseEx) {
	     	System.err.println("[ReportMaker] statement could not be closed");
	     	statementCloseEx.printStackTrace(System.err);
	    }
    }

    return v;

  }



}//class ReportMaker





