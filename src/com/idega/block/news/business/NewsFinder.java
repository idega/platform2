package com.idega.block.news.business;

import com.idega.data.EntityFinder;
import com.idega.block.text.business.*;
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

  public NewsFinder() {

  }

  public static NewsCategory getNewsCategory(int iCategoryId){
    try {
      return new NewsCategory(iCategoryId );
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static List listOfAllNwNewsInCategory(int newsCategoryId){
    return listOfPublishingNews(newsCategoryId,true);
  }

  public static List listOfNwNewsInCategory(int newsCategoryId){
    return listOfPublishingNews(newsCategoryId,false);
    /*
    try {
      return  EntityFinder.findAllByColumn( new NwNews(),NwNews.getColumnNameNewsCategoryId(),String.valueOf(newsCategoryId));
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }

    return null;
    */
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

  public static List listOfNewsHelpersInCategory(int newsCategoryId,int maxNumberOfNews,Locale locale){
    return listOfNewsHelpersInCategory(newsCategoryId,maxNumberOfNews,getLocaleId(locale));
  }

  public static List listOfAllNewsHelpersInCategory(int newsCategoryId,int maxNumberOfNews,Locale locale){
    List L = listOfAllNwNewsInCategory(newsCategoryId );
    return listOfNewsHelpersInCategory(L,newsCategoryId ,maxNumberOfNews ,getLocaleId(locale) );
  }

  public static List listOfNewsHelpersInCategory(int newsCategoryId,int maxNumberOfNews,int iLocaleId){
    List L = listOfNwNewsInCategory(newsCategoryId);
    return listOfNewsHelpersInCategory(L,newsCategoryId ,maxNumberOfNews ,iLocaleId );
  }

  private static List listOfNewsHelpersInCategory(List L,int newsCategoryId,int maxNumberOfNews,int iLocaleId){
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

  public static LocalizedText getLocalizedText(int iNwNewsId,int iLocaleId){
    LocalizedText LTX = null;
    List L =   listOfLocalizedText(iNwNewsId,iLocaleId);
    if(L!= null){
      LTX = (LocalizedText) L.get(0);
    }

    return LTX;
  }
  public static List listOfLocalizedText(int iNwNewsId,int iLocaleId){
    StringBuffer sql = new StringBuffer("select lt.* from tx_localized_text lt, nw_news n,NW_NEWS_TX_LOCALIZED_TEXT ttl ");
    sql.append(" where ttl.nw_news_id = n.nw_news_id  ");
    sql.append(" and ttl.tx_localized_text_id = lt.tx_localized_text_id ");
    sql.append(" and n.nw_news_id = ");
    sql.append(iNwNewsId);
    sql.append(" and lt.ic_locale_id =  ");
    sql.append(iLocaleId);
    try {
      return EntityFinder.findAll(new LocalizedText(),sql.toString());
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public static LocalizedText getLocalizedText(int iNwNewsId,Locale locale){
    int Lid = getLocaleId(locale);
    return getLocalizedText(iNwNewsId,Lid);
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
/*
  public static listOfObjectInstanceTexts(){

  }
*/
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
    try {
      return EntityFinder.findAll(new NewsCategory());
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static int getObjectInstanceIdFromNewsCategoryId(int iCategoryId){
    try {
      NewsCategory nw = new NewsCategory(iCategoryId);
      List L = EntityFinder.findRelated( nw,new ICObjectInstance());
      if(L!= null){
        return ((ICObjectInstance) L.get(0)).getID();
      }
      else
        return -1;
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return -2;
    }
  }

  public static int getObjectInstanceCategoryId(int iObjectInstanceId,boolean CreateNew){
    int id = -1;
    try {
      ICObjectInstance obj = new ICObjectInstance(iObjectInstanceId);
      id = getObjectInstanceCategoryId(obj);
      if(id <= 0 && CreateNew ){
        id = NewsBusiness.createNewsCategory(iObjectInstanceId );
      }
    }
    catch (Exception ex) {

    }
    return id;
  }

  public static int getObjectInstanceCategoryId(int iObjectInstanceId){
    try {
      ICObjectInstance obj = new ICObjectInstance(iObjectInstanceId);
      return getObjectInstanceCategoryId(obj);
    }
    catch (Exception ex) {

    }
    return -1;
  }

  public static int getObjectInstanceCategoryId(ICObjectInstance eObjectInstance){
    try {
      List L = EntityFinder.findRelated(eObjectInstance ,new NewsCategory());
      if(L!= null){
        return ((NewsCategory) L.get(0)).getID();
      }
      else
        return -1;
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return -2;
    }
  }

  public static List listOfNewsCategoryForObjectInstanceId(int instanceid){
    try {
      ICObjectInstance obj = new ICObjectInstance(instanceid );
      return listOfNewsCategoryForObjectInstanceId(obj);
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static List listOfNewsCategoryForObjectInstanceId( ICObjectInstance obj){
    try {
      List L = EntityFinder.findRelated(obj,new NewsCategory());
      return L;
    }
    catch (SQLException ex) {
      return null;
    }
  }
}