package com.idega.block.reports.business;

import com.idega.block.reports.data.*;
import java.util.Vector;
import java.sql.*;
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

  public static int STRING = 1,INTEGER = 2, AGE = 3;

  public ReportMaker(){

  }

  private Vector makeReport(ReportCondition[] conds){
    Vector vSelect = new Vector();
    Vector vTables = new Vector();
    Vector vJoin = new Vector();
    Vector vWhere = new Vector();
    Vector vOrder = new Vector();

    vSelect.addElement("select distinct ");
    vTables.addElement(" from ");
    vOrder.addElement(" order by ");
    vJoin.addElement(" where ");

    ReportItem item;
    for (int i = 0; i < conds.length; i++) {
      item = conds[i].getItem();
      if(!vSelect.contains(item.getField()))
        vSelect.addElement(item.getField());
      if(!vTables.contains(item.getMainTable()))
        vTables.addElement(item.getMainTable());
      if(!vJoin.contains(item.getJoin()))
        vJoin.addElement(item.getJoin());
      if(conds[i].getCondition() != null)
        vWhere.addElement(conds[i].getCondition());
    }
    String sql = makeSQL(vSelect,vTables,vJoin,vWhere,vOrder);
    Vector v = this.searchInDatabase(sql);
    return v;
  }

  private String makeSQL(Vector Select,Vector From,Vector Join,Vector Where,Vector Order){
    StringBuffer sql = new StringBuffer();
    int len = Select.size();
    for(int i = 0; i < len ; i++){
      sql.append(Select.elementAt(i));
      if(i > 0 && i < len-1)
        sql.append(",");
    }
    sql.append(" ");
    len = From.size();
    for(int i = 0; i < len ; i++){
      sql.append(From.elementAt(i));
      if(i > 0 && i < len-1)
        sql.append(",");
    }
    sql.append(" ");

    len = Join.size();
    int wlen = Where.size();
    if(len > 1 || wlen >0){
      for(int i = 0; i < len ; i++){
        sql.append(Join.elementAt(i));
        if(i > 1 && i < len-1)
          sql.append(" and ");
      }
    }

    int old = len;
    sql.append(" ");
    len = Where.size();
    if(len > 0 && old > 2)
    if(old > 2 && len > 0)    sql.append(" and ");
    for(int i = 0; i < len ; i++){
      sql.append(Where.elementAt(i));
      if( i < len-1)
        sql.append(" and ");
    }
    sql.append(" ");
    len = Order.size();
    for(int i = 0; i < len ; i++){
      sql.append(Order.elementAt(i));
      if(i > 0 && i < len-1 )
        sql.append(",");
    }
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
    Connection Conn = ConnectionBroker.getConnection();
    try{
      Statement stmt = Conn.createStatement();
      ResultSet RS = stmt.executeQuery(SQL);
      ResultSetMetaData MD = RS.getMetaData();
      int count = MD.getColumnCount();
      String temp;
      ReportContent RC;
      String[] s = new String[count];
      while(RS.next()){
        RC = new ReportContent(s);
        for(int i = 1; i <= count; i++){
          temp = RS.getString(i);
          s[i-1] = temp!=null?temp:"";
        }
        RC.setContent(s);
        v.addElement(RC);
      }
      RS.close();
      stmt.close();
      ConnectionBroker.freeConnection(Conn);
    }
    catch(SQLException e){
      e.printStackTrace();
    }
    return v;
  }

}//class ReportMaker


