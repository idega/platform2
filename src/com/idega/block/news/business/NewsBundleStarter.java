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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.CreateException;

import com.idega.block.category.business.CategoryBusiness;
import com.idega.block.category.data.ICCategory;
import com.idega.block.news.data.NewsCategory;
import com.idega.block.news.data.NwNews;
import com.idega.block.news.data.NwNewsHome;
import com.idega.data.IDOLegacyEntity;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.SimpleQuerier;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;

public class NewsBundleStarter implements IWBundleStartable{

  public void start(IWBundle bundle){
    start();
  }

  public void start(){
    System.err.println("News bundle starter: starting");

    if(!testNews()){
      System.err.println("News bundle starter: Adding category reference to news ");
      try {
        addICCategoryField(((NwNewsHome)IDOLookup.getHome(NwNews.class)).create());
    } catch (IDOLookupException e) {
        e.printStackTrace();
    } catch (CreateException e) {
        e.printStackTrace();
    }
    }

    //
    if(testCategories()){
      System.err.println("News bundle starter: Making categories");
      Map map = makeICCategories();
      if(map !=null){
        System.err.println("News bundle starter: Moving news to new categories ");
        moveDataToCategory(map);
      }
    }
  }

  private Map makeICCategories(){
    Hashtable hash = new Hashtable();
    String sql = "select cat.*  from nw_news n, nw_news_cat cat " +
                  " where n.ic_category_id is null "+
                  " and n.nw_news_cat_id = cat.nw_news_cat_id ";
    //String sql = "select * from nw_news_cat ";
    String sql2 = "select IC_OBJECT_INSTANCE_ID from  NW_NEWS_CAT_IC_OBJECT_INSTANCE where NW_NEWS_CAT_ID = ";
    String type = ((com.idega.block.news.data.NewsCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(NewsCategory.class)).createLegacy().getCategoryType();
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
      oinst = SimpleQuerier.executeStringQuery(sql2+id,Conn);
      if(oinst !=null && oinst.length > 0)
        objectinstance_id = Integer.parseInt(oinst[0]);
      ICCategory cat = ((com.idega.block.category.data.ICCategoryHome)com.idega.data.IDOLookup.getHome(ICCategory.class)).create();
      cat.setName(name);
      cat.setDescription(info);
      cat.setType(type);

      CAT = CategoryBusiness.getInstance().saveCategory(cat,objectinstance_id,false);
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
      NwNews news = (NwNews) com.idega.block.news.data.NwNewsBMPBean.getStaticInstance(NwNews.class);
      StringBuffer sql = new StringBuffer("update ");
      sql.append(news.getEntityName());
      sql.append(" set ");
      sql.append(com.idega.block.news.data.NwNewsBMPBean.getColumnNameNewsCategoryId());
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

  public boolean addICCategoryField(IDOLegacyEntity Entity){
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

  // true if news have categories
   public boolean testCategories(){
    String sql = "select count(nw_news_id) from nw_news where ic_category_id is null";
    try {
      String[] s = SimpleQuerier.executeStringQuery(sql);
      // true if nw_news dont have category
      return (s!=null && s.length == 1 && Integer.parseInt(s[0]) > 0 );
    }
    catch (Exception ex) {

      ex.printStackTrace();
    }
    return false;
  }

  public static void main(String[] args) {
    System.out.println("Fixing News!"); //Display the string.
    new NewsBundleStarter().start();
  }

	/**
	 * @see com.idega.idegaweb.IWBundleStartable#stop(IWBundle)
	 */
	public void stop(IWBundle starterBundle) {
		//does nothing...
	}
}
