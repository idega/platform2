package com.idega.block.news.business;

import com.idega.core.business.CategoryBusiness;
import com.idega.core.business.CategoryFinder;
import com.idega.data.EntityFinder;
import com.idega.block.text.business.*;
import com.idega.block.text.data.*;
import com.idega.util.LocaleUtil;
import com.idega.block.news.data.*;
import com.idega.block.text.data.*;
import com.idega.util.idegaTimestamp;
import java.util.List;
import java.util.Vector;
import java.util.Hashtable;
import java.sql.SQLException;
import java.util.Locale;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.data.ICObjectInstance;
import com.idega.core.data.ICFile;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class NewsFinder {

  public static final int PUBLISHISING = 1,UNPUBLISHED = 2, PUBLISHED = 3;

  public NewsFinder() {

  }

  public static NewsCategory getNewsCategory(int iCategoryId){
    return (NewsCategory) CategoryFinder.getInstance().getCategory(iCategoryId);
  }

  public static List listOfAllNwNewsInCategory(int[] newsCategoryId,Locale locale){
    int iLocaleId = getLocaleId(locale);
    return listOfPublishingNews(newsCategoryId,iLocaleId ,true);
  }

  public static List listOfNwNewsInCategory(int[] newsCategoryId,Locale locale){
    int iLocaleId = getLocaleId(locale);
    return listOfPublishingNews(newsCategoryId,iLocaleId,false);
  }

  public static List listOfAllNwNewsInCategory(int[] newsCategoryId,int iLocaleId){
    return listOfPublishingNews(newsCategoryId,iLocaleId ,true);
  }

  public static List listOfNwNewsInCategory(int[] newsCategoryId,int iLocaleId){
    return listOfPublishingNews(newsCategoryId,iLocaleId,false);
  }

  public static List listOfAllNwNewsInCategory(int newsCategoryId){
    return listOfPublishingNews(newsCategoryId,true);
  }

  public static List listOfNwNewsInCategory(int newsCategoryId){
    return listOfPublishingNews(newsCategoryId,false);
  }

  public static List listOfPublishingNews(int newsCategoryId,boolean ignorePublishingDates){
    StringBuffer sql = new StringBuffer("select * from ");
    sql.append(NwNews.getEntityTableName());
    sql.append(" n,");
    sql.append(Content.getEntityTableName());
    sql.append(" c where n.");
    sql.append(NwNews.getColumnNameContentId());
    sql.append(" = c.");
    sql.append(Content.getEntityTableName());
    sql.append("_id and ");
    sql.append(NwNews.getColumnNameNewsCategoryId());
    sql.append(" = ");
    sql.append(newsCategoryId);
    // USE BETWEEN
    /*
    if(!ignorePublishingDates ){
      idegaTimestamp today = idegaTimestamp.RightNow();
      sql.append(" and '");
      sql.append(today.toSQLString());
      sql.append("' between ");
      sql.append(Content.getColumnNamePublishFrom() );
      sql.append(" and ");
      sql.append(Content.getColumnNamePublishTo());

    }
    */
    // USE OPERATORS <= AND >=

    if(!ignorePublishingDates ){
      idegaTimestamp today = idegaTimestamp.RightNow();
      sql.append(" and ");
      sql.append(Content.getColumnNamePublishFrom() );
      sql.append(" <= '");
      sql.append(today.toSQLString());
      sql.append("' and ");
      sql.append(Content.getColumnNamePublishTo());
      sql.append(" >= '");
      sql.append(today.toSQLString());
      sql.append("' ");
    }

    sql.append(" order by ");
    sql.append(Content.getColumnNamePublishFrom());
    sql.append(" desc ");
    //System.err.println(sql.toString());
    try {
      return EntityFinder.findAll(new NwNews(),sql.toString());
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return null;
  }

    public static List listOfPublishingNews(int[] newsCategoryIds,int iLocaleId,boolean ignorePublishingDates){
      String middleTable = new Content().getLocalizedTextMiddleTableName(new LocalizedText(),new Content());
      StringBuffer sql = new StringBuffer("SELECT N.*,C.* FROM ");
      sql.append(NwNews.getEntityTableName());
      sql.append(" N, ");
      sql.append(LocalizedText.getEntityTableName());
      sql.append(" T, ");
      sql.append(middleTable);
      sql.append(" M, ");
      sql.append(Content.getEntityTableName());
      sql.append(" C ");
      sql.append("WHERE N.");
      sql.append(NwNews.getColumnNameContentId());
      sql.append(" = C.");
      sql.append(Content.getEntityTableName());
      sql.append("_ID AND ");
      sql.append(NwNews.getColumnNameNewsCategoryId());

      sql.append(" in (");
      for (int i = 0; i < newsCategoryIds.length; i++) {
        if(i>0)
          sql.append(",");
        sql.append(newsCategoryIds[i]);
      }
      sql.append(" ) ");

      //sql.append(" = ");
      //sql.append(newsCategoryId);
      sql.append(" AND C.");
      sql.append(Content.getEntityTableName());
      sql.append("_ID = M.");
      sql.append(Content.getEntityTableName());
      sql.append("_ID AND M.");
      sql.append(LocalizedText.getEntityTableName());
      sql.append("_ID = T.");
      sql.append(LocalizedText.getEntityTableName());
      sql.append("_ID AND T.");
      sql.append(LocalizedText.getColumnNameLocaleId());
      sql.append(" = ");
      sql.append(iLocaleId);

    // USE BETWEEN
    /*
    if(!ignorePublishingDates ){
      idegaTimestamp today = idegaTimestamp.RightNow();
      sql.append(" and '");
      sql.append(today.toSQLString());
      sql.append("' between ");
      sql.append(Content.getColumnNamePublishFrom() );
      sql.append(" and ");
      sql.append(Content.getColumnNamePublishTo());

    }
    */
    // USE OPERATORS <= AND >=

    if(!ignorePublishingDates ){
      idegaTimestamp today = idegaTimestamp.RightNow();
      sql.append(" and ");
      sql.append(Content.getColumnNamePublishFrom() );
      sql.append(" <= '");
      sql.append(today.toSQLString());
      sql.append("' and ");
      sql.append(Content.getColumnNamePublishTo());
      sql.append(" >= '");
      sql.append(today.toSQLString());
      sql.append("' ");
    }

    sql.append(" order by C.");
    sql.append(Content.getColumnNameCreated());
    sql.append(" desc ");
    //
    //System.err.println(sql.toString());
    try {
      return EntityFinder.findAll(new NwNews(),sql.toString());
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public static List listOfNewsHelpersInCategory(int newsCategoryId,int maxNumberOfNews){
    List L = listOfNwNewsInCategory(newsCategoryId);
    if(L!= null){
      int len = L.size();
      Vector V = new Vector();
      for (int i = 0; i < len && i < maxNumberOfNews; i++) {
        NwNews news = (NwNews) L.get(i);
        NewsHelper nh = getNewsHelper(news);
        if(nh != null)
          V.add(nh);
      }
     return V;
    }
    return null;
  }

  public static List listOfNewsHelpersInCategory(int[] newsCategoryId,int maxNumberOfNews,Locale locale){
    return listOfNewsHelpersInCategory(newsCategoryId,maxNumberOfNews,getLocaleId(locale));
  }

  public static List listOfAllNewsHelpersInCategory(int[] newsCategoryId,int maxNumberOfNews,Locale locale){
    int iLocaleId = getLocaleId(locale);
    List L = listOfAllNwNewsInCategory(newsCategoryId ,iLocaleId);
    return listOfNewsHelpersInCategory(L,newsCategoryId ,maxNumberOfNews , iLocaleId);
  }

  public static List listOfNewsHelpersInCategory(int[] newsCategoryId,int maxNumberOfNews,int iLocaleId){
    List L = listOfNwNewsInCategory(newsCategoryId,iLocaleId);
    return listOfNewsHelpersInCategory(L,newsCategoryId ,maxNumberOfNews ,iLocaleId );
  }

  private static List listOfNewsHelpersInCategory(List L,int[] newsCategoryId,int maxNumberOfNews,int iLocaleId){
    if(L!= null){
      int len = L.size();
      Vector V = new Vector();
      for (int i = 0; i < len && i < maxNumberOfNews; i++) {
        NwNews news = (NwNews) L.get(i);
        NewsHelper nh = getNewsHelper(news,iLocaleId );
        if(nh != null)
          V.add(nh);
      }
     return V;
    }
    return null;
  }

  public static NewsHelper getNewsHelper(NwNews news){
    NewsHelper NH = new NewsHelper();
    NwNews N = news;
    if(N!=null){
      ContentHelper ch = ContentFinder.getContentHelper(N.getContentId());
      NH.setNews(N);
      NH.setContentHelper(ch);
      return NH;
    }
    else
      return null;
  }

  public static NewsHelper getNewsHelper(NwNews news,int iLocaleId){
    NewsHelper NH = new NewsHelper();
    NwNews N = news;
    if(N!=null){
      ContentHelper ch = ContentFinder.getContentHelper(N.getContentId(),iLocaleId );
      NH.setNews(N);
      NH.setContentHelper(ch);
      return NH;
    }
    else
      return null;
  }

  public static NewsHelper getNewsHelper(int iNwNewsId){
    NwNews N = getNews(iNwNewsId);
    return getNewsHelper(N);
  }

   public static NewsHelper getNewsHelper(int iNwNewsId,int iLocaleId){
    NwNews N = getNews(iNwNewsId);
    return getNewsHelper(N,iLocaleId );
  }

  public static NewsHelper getNewsHelper(int iNwNewsId,Locale locale){
    NewsHelper NH = new NewsHelper();
    NwNews N = getNews(iNwNewsId);

    if(N!=null){
      ContentHelper ch = ContentFinder.getContentHelper(N.getContentId(),locale );
      NH.setNews(N);
      NH.setContentHelper(ch);
      return NH;
    }
    else
      return null;
  }

  public static List listOfNewsFiles(int id){
    try {
      return listOfNewsFiles(new NwNews(id));
    }
    catch (SQLException ex) {

    }
    return null;
  }


  public static List listOfNewsFiles(NwNews nwNews){
    try {
      return EntityFinder.findRelated(nwNews,new ICFile());
    }
    catch (SQLException ex) {

    }
    return null;
  }


  public static List listOfLocalizedText(int iNwNewsId){
    List L = null;
    try {
      NwNews tt = new NwNews(iNwNewsId);
      LocalizedText lt = new LocalizedText();
      L = EntityFinder.findRelated(tt,lt);
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      L = null;
    }
    return L;
  }


    public static NwNews getNews(int iNewsId){
      try {
        return new NwNews(iNewsId);
      }
      catch (SQLException ex) {
        ex.printStackTrace();
        return null;
      }
    }

    public static int	countNewsInCategory(int iCategoryId){
      try {
              NwNews news = (NwNews)NwNews.getStaticInstance(NwNews.class);
              return news.getNumberOfRecords(news.getColumnNameNewsCategoryId(),iCategoryId);
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
      return 0;
    }

    public static int countNewsInCategory(int iCategoryId, int PublishType){
      StringBuffer sql = new StringBuffer("select count(*) from ");
      sql.append(NwNews.getEntityTableName());
      sql.append(" n,");
      sql.append(Content.getEntityTableName());
      sql.append(" c where n.");
      sql.append(NwNews.getColumnNameContentId());
      sql.append(" = c.");
      sql.append(Content.getEntityTableName());
      sql.append("_id and ");
      sql.append(NwNews.getColumnNameNewsCategoryId());
      sql.append(" = ");
      sql.append(iCategoryId);
      if(PublishType > 0){
              String today = idegaTimestamp.RightNow().toSQLString();
      switch (PublishType) {
        case UNPUBLISHED :
                        sql.append(" and c.");
                        sql.append(Content.getColumnNamePublishFrom() );
                        sql.append(" >= '");
                        sql.append(today);
                        sql.append("' ");
        break;
        case PUBLISHISING:
                        sql.append(" and c.");
                        sql.append(Content.getColumnNamePublishFrom() );
                        sql.append(" <= '");
                        sql.append(today);
                        sql.append("' and c.");
                        sql.append(Content.getColumnNamePublishTo());
                        sql.append(" >= '");
                        sql.append(today);
                        sql.append("' ");
        break;
        case PUBLISHED:
          sql.append(" and c.");
                sql.append(Content.getColumnNamePublishTo());
                sql.append(" <= '");
                sql.append(today);
                sql.append("' ");
        break;
      }
    }
    NwNews ge = (NwNews)NwNews.getStaticInstance(NwNews.class);
    try {
      //System.err.println(sql.toString());
      return ge.getIntTableValue(sql.toString());
    }
    catch (SQLException ex) {

    }
    return 0;
  }

  public static List listOfLocales(){
    return ICLocaleBusiness.listLocaleCreateIsEn();
  }

  public static int getLocaleId(Locale locale){
   return ICLocaleBusiness.getLocaleId(locale);
  }

  public static Locale getLocale(int iLocaleId){
    Locale L = ICLocaleBusiness.getLocale(iLocaleId);
    if(L==null)
      L = new Locale("is","IS");
    return L;
  }

  public static List listOfNewsCategories(){
    return CategoryFinder.getInstance().listOfCategories(new NewsCategory().getCategoryType());
  }

  public static List listOfValidNewsCategories(){
    return CategoryFinder.getInstance().listOfValidCategories(new NewsCategory().getCategoryType());
  }

  public static List listOfInValidNewsCategories(){
    return CategoryFinder.getInstance().listOfInValidCategories(new NewsCategory().getCategoryType());
  }

  public static int getObjectInstanceIdFromNewsCategoryId(int iCategoryId){
    return CategoryFinder.getInstance().getObjectInstanceIdFromCategoryId(iCategoryId);
  }

  public static int getObjectInstanceCategoryId(int iObjectInstanceId,boolean CreateNew){
    return CategoryFinder.getInstance().getObjectInstanceCategoryId(iObjectInstanceId,CreateNew,new NewsCategory().getCategoryType());
  }

  public static int getObjectInstanceCategoryId(int iObjectInstanceId){
    return CategoryFinder.getInstance().getObjectInstanceCategoryId(iObjectInstanceId);
  }

  public static int getObjectInstanceCategoryId(ICObjectInstance eObjectInstance){
    return CategoryFinder.getInstance().getObjectInstanceCategoryId(eObjectInstance);
  }

  public static List listOfNewsCategoryForObjectInstanceId(int instanceid){
    return CategoryFinder.getInstance().listOfCategoryForObjectInstanceId(instanceid);
  }

  public static List listOfNewsCategoryForObjectInstanceId( ICObjectInstance obj){
    return CategoryFinder.getInstance().listOfCategoryForObjectInstanceId(obj);
  }
}