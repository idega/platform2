package com.idega.block.news.business;


/**
 * Title:MediaBundleStarter
 * Description: MediaBundleStarter implements the IWBundleStartable interface. The start method of this
 * object is called during the Bundle loading when starting up a idegaWeb applications.
 * Copyright:    Copyright (c) 2001
 * Company:      idega software
 * @author Eirikur S. Hrafnsson eiki@idega.is
 * @version 1.0
 */

import com.idega.idegaweb.IWBundleStartable;
import com.idega.idegaweb.IWBundle;
import com.idega.data.EntityFinder;
import com.idega.block.category.business.*;
import com.idega.data.*;
import com.idega.core.data.ICCategory;
import com.idega.block.news.data.*;
import java.sql.SQLException;
import java.util.*;
import java.sql.*;

import java.util.HashMap;

public class NewsBundleStarter implements IWBundleStartable{

  public void start(IWBundle bundle){
    start();
  }

  public void start(){
    if(!testNews()){
      System.err.println("News bundle starter: Making categories");
      Map map = makeICCategories();
      if(map !=null){
        System.err.println("News bundle starter: Adding category reference to news ");
        if(addICCategoryField(new NwNews()))
          System.err.println("News bundle starter: Moving news to new categories ");
          moveDataToCategory(map);
      }
    }
    else
      System.err.println("did not need to do anything ");
  }

  private Map makeICCategories(){
    Hashtable hash = new Hashtable();

    String sql = "select * from nw_news_cat ";
    String sql2 = "select IC_OBJECT_INSTANCE_ID from  NW_NEWS_CAT_IC_OBJECT_INSTANCE where NW_NEWS_CAT_ID = ";
    String type = new NewsCategory().getCategoryType();
    try{
    Connection Conn = com.idega.util.database.ConnectionBroker.getConnection();
    Statement stmt = Conn.createStatement();
    ResultSet RS = null;
    RS = stmt.executeQuery(sql);
    String[] oinst;
    ICCategory CAT ;
    int objectinstance_id;
    while(RS.next()){
      objectinstance_id = -1;
      int id = RS.getInt("NW_NEWS_CAT_ID"); // first_name
      String name = RS.getString("NAME");
      String info = RS.getString("DESCRIPTION");
      Timestamp stamp = RS.getTimestamp("NEWS_DATE");
      String valid = RS.getString("VALID");
      oinst = SimpleQuerier.executeStringQuery(sql2+id,Conn);
      if(oinst !=null && oinst.length > 0)
        objectinstance_id = Integer.parseInt(oinst[0]);
      CAT = CategoryBusiness.saveCategory(-1,name,info,objectinstance_id,type);
      hash.put(new Integer(id),new Integer(CAT.getID()));
    }
    if(RS!=null)
        RS.close();
      stmt.close();

    com.idega.util.database.ConnectionBroker.freeConnection(Conn);

    }
    catch(Exception ex){
      ex.printStackTrace();
    }
    return hash;
  }

  private void moveDataToCategory(Map map){
    if(map!=null){
      Iterator iter = map.entrySet().iterator();
      Map.Entry me;
      Integer oldid,newid;
      while(iter.hasNext()){
        me = (Map.Entry) iter.next();
        oldid = (Integer)me.getKey();
        newid = (Integer)me.getValue();
        moveNewsBetweenCategories(oldid.intValue(),newid.intValue());
      }
    }
  }

  public static void moveNewsBetweenCategories(int fromCategoryId,int toCategoryId){
    if(fromCategoryId > 0 && toCategoryId > 0){
      NwNews news = (NwNews) NwNews.getStaticInstance(NwNews.class);
      StringBuffer sql = new StringBuffer("update ");
      sql.append(news.getEntityName());
      sql.append(" set ");
      sql.append(news.getColumnNameNewsCategoryId());
      sql.append(" = ");
      sql.append(toCategoryId);
      sql.append(" where ");
      sql.append("NW_NEWS_CAT_ID");
      sql.append(" = ");
      sql.append(fromCategoryId);
      //System.err.println(sql.toString());
      try {

      com.idega.data.SimpleQuerier.execute(sql.toString());
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    }
  }

  public boolean addICCategoryField(GenericEntity Entity){
    try{
    StringBuffer sql1 = new StringBuffer("ALTER TABLE ");
    sql1.append(Entity.getEntityName());
    sql1.append(" add IC_CATEGORY_ID INTEGER ");
    System.err.println();
      if(SimpleQuerier.execute(sql1.toString())){
        StringBuffer sql2 = new StringBuffer();
        sql2.append(" alter table ");
        sql2.append(Entity.getEntityName());
        sql2.append(" add constraint ");
        sql2.append(" FK_CAT_");
        sql2.append(Entity.getEntityName());
        sql2.append(" foreign key (IC_CATEGORY_ID) references IC_CATEGORY(IC_CATEGORY_ID)");
        return SimpleQuerier.execute(sql2.toString());
      }
    }
    catch(Exception ex){
      ex.printStackTrace();
    }
    return false;
  }

  public boolean testNews(){
    String sql = "select ic_category_id from nw_news where ic_category_id < -3";
    String sql2 = "select ic_category_id from nw_news where ic_category_id is null";
    try {
      return SimpleQuerier.execute(sql);
      /*  String[] s = SimpleQuerier.executeStringQuery(sql2);
        return !(s!=null && s.length > 0);
      }
*/
    }
    catch (Exception ex) {

      //ex.printStackTrace();
    }
    return false;
  }

  public static void main(String[] args) {
    System.out.println("Fixing News!"); //Display the string.
    new NewsBundleStarter().start();
  }




}