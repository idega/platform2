package com.idega.block.news.business;

import com.idega.data.EntityFinder;
import com.idega.block.text.business.TextHelper;
import com.idega.util.LocaleUtil;
import com.idega.block.news.data.*;
import com.idega.block.text.data.*;
import java.util.List;
import java.util.Vector;
import java.util.Hashtable;
import java.sql.SQLException;
import java.util.Locale;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.data.ICObjectInstance;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
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

  public static List listOfNwNewsInCategory(int newsCategoryId){
    List L = null;
    try {
      L = EntityFinder.findAllByColumnDescendingOrdered(
                      new NwNews(),
                      NwNews.getColumnNameNewsCategoryId(),
                      String.valueOf(newsCategoryId),
                      NwNews.getColumnNameNewsDate());
    }
    catch (SQLException ex) {
      L = null;
    }
    return L;
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

  public static List listOfNewsHelpersInCategory(int newsCategoryId,int maxNumberOfNews,int iLocaleId){
    List L = listOfNwNewsInCategory(newsCategoryId);
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
      NH.setNews(N);
      NH.setLocalizedText( listOfLocalizedText(N.getID()));
      return NH;
    }
    else
      return null;
  }

  public static NewsHelper getNewsHelper(NwNews news,int iLocaleId){
    NewsHelper NH = new NewsHelper();
    NwNews N = news;
    if(N!=null){
      NH.setNews(N);
      NH.setLocalizedText(getLocalizedText(N.getID(),iLocaleId));
      return NH;
    }
    else
      return null;
  }

  public static NewsHelper getNewsHelper(int iNwNewsId){
    NewsHelper NH = new NewsHelper();
    NwNews N = getNews(iNwNewsId);
    return getNewsHelper(N);
  }

   public static NewsHelper getNewsHelper(int iNwNewsId,int iLocaleId){
    NewsHelper NH = new NewsHelper();
    NwNews N = getNews(iNwNewsId);
    return getNewsHelper(N,iLocaleId );
  }

  public static NewsHelper getNewsHelper(int iNwNewsId,Locale locale){
    NewsHelper NH = new NewsHelper();
    NwNews N = getNews(iNwNewsId);

    if(N!=null){
      NH.setNews(N);
      NH.setLocalizedText(getLocalizedText(iNwNewsId,locale));
      return NH;
    }
    else
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