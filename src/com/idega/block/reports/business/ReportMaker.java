package com.idega.block.reports.business;

import com.idega.block.reports.data.*;
import java.util.Vector;
import java.util.Collection;
import java.util.TreeMap;
import java.util.List;
import java.util.Iterator;
import java.util.StringTokenizer;
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

  public ReportMaker(){

  }
  public Vector makeReport(String sql){
    Vector v = this.searchInDatabase(sql);
    return v;
  }

  public Vector makeReport(Vector conds){
    String sql = worker(conds);
    Vector v = this.searchInDatabase(sql);
    return v;
  }

  public Vector makeReport(Vector conds,ReportCondition RCx){
    conds.addElement(RCx);
    String sql = worker(conds);
    Vector v = this.searchInDatabase(sql);
    return v;
  }

  public String makeSQL(Vector conds){
    return worker(conds);
  }

  private String worker(Vector conds){
    ReportCondition cond;
    Vector vSelect = new Vector();
    Vector vTables = new Vector();
    Vector vJoin = new Vector();
    Vector vWhere = new Vector();
    Vector vOrder = new Vector();
		Vector vGroupBy = new Vector();
		Vector vHaving = new Vector();
    List LOrder = null;
    TreeMap orderMap = new TreeMap();

    ReportItem item;
    for (int i = 0; i < conds.size(); i++) {
      cond = (ReportCondition) conds.elementAt(i);
      item = cond.getItem();
      if(cond.isSelect()){
      if(item.getField()!= null){
        if(!vSelect.contains(item.getMainTable()+"."+item.getField()))
          vSelect.addElement(item.getMainTable()+"."+item.getField());
        }
      }
      if(item.getMainTable()!=  null){
        if(!vTables.contains(item.getMainTable()))
          vTables.addElement(item.getMainTable());
      }

      String[] sa = item.getJoinTable();
      if(sa != null){
        for (int j = 0; j < sa.length; j++) {
          if(!vTables.contains(sa[j])){
            vTables.addElement(sa[j]);
          }
        }
      }
      String sJoin = item.getJoin();
      if(sJoin.length() > 1){
        StringTokenizer ST = new StringTokenizer(sJoin,",;");
        while(ST.hasMoreTokens()){
          String join = ST.nextToken();
          if(!vJoin.contains(join))
            vJoin.addElement(join);
        }
      }
      String c = cond.getCondition();
      if(c != null && c.length() > 0 && !vWhere.contains(c))
        vWhere.addElement(c);

      Integer order = cond.getOrder();
      if(order != null){
        orderMap.put(order,item.getMainTable()+"."+item.getField());
      }
    }
    if(orderMap.size() > 0){
      Collection C = orderMap.values();
      vOrder.addAll(C);
    }
    String sql = makeSQL(vSelect,vTables,vJoin,vWhere,vOrder);
    return sql;
  }

  private String makeSQL(List Select,List From,List Join,List Where,List Order){
    ///////////////////////////
    //  Select part
    ///////////////////////////
    StringBuffer sql = new StringBuffer("select  ");
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
      sql.append(" from ");
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
      sql.append(" where ");
      ifwhere = true;
      for(int i = 0; i < len ; i++){
        sql.append(Join.get(i));
        if(i < len-1)
          sql.append(" and ");
      }
    }
    sql.append(" ");
    ////////////////////////
    //  Where part
    ///////////////////////
    len = Where.size();
    if(len > 0 ){
      if(ifwhere)
        sql.append(" and ");
      else
        sql.append(" where ");
      for(int i = 0; i < len ; i++){
        sql.append(Where.get(i));
        if( i < len-1)
          sql.append(" and ");
      }
    }
    sql.append(" ");
    ////////////////////////
    //  Order by part
    ///////////////////////
    if(Order != null){
      len = Order.size();
      if(len > 0 ){
        sql.append( "order by ");
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
    try{
      Conn = ConnectionBroker.getConnection();
      Statement stmt = Conn.createStatement();
      ResultSet RS = stmt.executeQuery(SQL);
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
      RS.close();
      stmt.close();
    }
    catch(SQLException e){
      e.printStackTrace();
    }
    finally {
      ConnectionBroker.freeConnection(Conn);
    }
    return v;
  }

}//class ReportMaker


