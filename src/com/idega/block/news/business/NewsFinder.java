package com.idega.block.news.business;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import com.idega.block.category.business.CategoryFinder;
import com.idega.block.news.data.NewsCategory;
import com.idega.block.news.data.NwNews;
import com.idega.block.news.data.NwNewsBMPBean;
import com.idega.block.text.business.ContentFinder;
import com.idega.block.text.business.ContentHelper;
import com.idega.block.text.data.Content;
import com.idega.block.text.data.ContentBMPBean;
import com.idega.block.text.data.LocalizedText;
import com.idega.block.text.data.LocalizedTextBMPBean;
import com.idega.core.component.data.ICObjectInstance;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.data.EntityFinder;
import com.idega.data.IDORelationshipException;
import com.idega.data.query.AND;
import com.idega.data.query.InCriteria;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;
import com.idega.util.IWTimestamp;

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
    Table news = new Table(com.idega.block.news.data.NwNewsBMPBean.getEntityTableName(), "n");
    Table content = new Table(com.idega.block.text.data.ContentBMPBean.getEntityTableName(), "c");
    
    SelectQuery query = new SelectQuery(news);
    query.addColumn(new WildCardColumn(news));
    query.addColumn(new WildCardColumn(content));
    
    query.addJoin(news, NwNewsBMPBean.getColumnNameContentId(), content, ContentBMPBean.getEntityTableName()+"_ID");
    query.addCriteria(new MatchCriteria(news, NwNewsBMPBean.getColumnNameNewsCategoryId(), MatchCriteria.EQUALS, newsCategoryId));
    if (!ignorePublishingDates ) {
    	IWTimestamp today = IWTimestamp.RightNow();
      	MatchCriteria from = new MatchCriteria(content, ContentBMPBean.getColumnNamePublishFrom(), MatchCriteria.LESSEQUAL, today.getTimestamp());
      	MatchCriteria to = new MatchCriteria(content, ContentBMPBean.getColumnNamePublishTo(), MatchCriteria.GREATEREQUAL, today.getTimestamp());
      	query.addCriteria(new AND(from, to));
    }
    query.addOrder(content, ContentBMPBean.getColumnNameCreated(), false);

    /*StringBuffer sql = new StringBuffer("select * from ");
    sql.append(com.idega.block.news.data.NwNewsBMPBean.getEntityTableName());
    sql.append(" n,");
    sql.append(com.idega.block.text.data.ContentBMPBean.getEntityTableName());
    sql.append(" c where n.");
    sql.append(com.idega.block.news.data.NwNewsBMPBean.getColumnNameContentId());
    sql.append(" = c.");
    sql.append(com.idega.block.text.data.ContentBMPBean.getEntityTableName());
    sql.append("_id and ");
    sql.append(com.idega.block.news.data.NwNewsBMPBean.getColumnNameNewsCategoryId());
    sql.append(" = ");
    sql.append(newsCategoryId);*/
    // USE BETWEEN
    /*
    if(!ignorePublishingDates ){
      IWTimestamp today = IWTimestamp.RightNow();
      sql.append(" and '");
      sql.append(today.toSQLString());
      sql.append("' between ");
      sql.append(com.idega.block.text.data.ContentBMPBean.getColumnNamePublishFrom() );
      sql.append(" and ");
      sql.append(com.idega.block.text.data.ContentBMPBean.getColumnNamePublishTo());

    }
    */
    // USE OPERATORS <= AND >=

    /*if(!ignorePublishingDates ){
      IWTimestamp today = IWTimestamp.RightNow();
      sql.append(" and ");
      sql.append(com.idega.block.text.data.ContentBMPBean.getColumnNamePublishFrom() );
      sql.append(" <= '");
      sql.append(today.toSQLString());
      sql.append("' and ");
      sql.append(com.idega.block.text.data.ContentBMPBean.getColumnNamePublishTo());
      sql.append(" >= '");
      sql.append(today.toSQLString());
      sql.append("' ");
    }

    sql.append(" order by ");
    sql.append(com.idega.block.text.data.ContentBMPBean.getColumnNamePublishFrom());
    sql.append(" desc ");*/
    //System.err.println(sql.toString());
    try {
      return EntityFinder.findAll(((com.idega.block.news.data.NwNewsHome)com.idega.data.IDOLookup.getHomeLegacy(NwNews.class)).createLegacy(),query.toString());
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return null;
  }

    public static List listOfPublishingNews(int[] newsCategoryIds,int iLocaleId,boolean ignorePublishingDates){
      String middleTable = ((com.idega.block.text.data.ContentHome)com.idega.data.IDOLookup.getHomeLegacy(Content.class)).createLegacy().getLocalizedTextMiddleTableName(((com.idega.block.text.data.LocalizedTextHome)com.idega.data.IDOLookup.getHomeLegacy(LocalizedText.class)).createLegacy(),((com.idega.block.text.data.ContentHome)com.idega.data.IDOLookup.getHomeLegacy(Content.class)).createLegacy());
      Table news = new Table(com.idega.block.news.data.NwNewsBMPBean.getEntityTableName(), "n");
      Table content = new Table(com.idega.block.text.data.ContentBMPBean.getEntityTableName(), "c");
      Table text = new Table(com.idega.block.text.data.LocalizedTextBMPBean.getEntityTableName(), "t");
      Table middle = new Table(middleTable, "m");
      
      SelectQuery query = new SelectQuery(news);
      query.addColumn(new WildCardColumn(news));
      query.addColumn(new WildCardColumn(content));
      
      query.addJoin(news, NwNewsBMPBean.getColumnNameContentId(), content, ContentBMPBean.getEntityTableName()+"_ID");
      query.addJoin(content, ContentBMPBean.getEntityTableName()+"_ID", middle, ContentBMPBean.getEntityTableName()+"_ID");
      query.addJoin(content, ContentBMPBean.getEntityTableName()+"_ID", middle, ContentBMPBean.getEntityTableName()+"_ID");
      query.addJoin(middle, LocalizedTextBMPBean.getEntityTableName()+"_ID", text, LocalizedTextBMPBean.getEntityTableName()+"_ID");

      query.addCriteria(new InCriteria(news, NwNewsBMPBean.getColumnNameNewsCategoryId(), newsCategoryIds));
      query.addCriteria(new MatchCriteria(text, LocalizedTextBMPBean.getColumnNameLocaleId(), MatchCriteria.EQUALS, iLocaleId));
      if (!ignorePublishingDates ) {
      	IWTimestamp today = IWTimestamp.RightNow();
      	MatchCriteria from = new MatchCriteria(content, ContentBMPBean.getColumnNamePublishFrom(), MatchCriteria.LESSEQUAL, today.getTimestamp());
      	MatchCriteria to = new MatchCriteria(content, ContentBMPBean.getColumnNamePublishTo(), MatchCriteria.GREATEREQUAL, today.getTimestamp());
      	query.addCriteria(new AND(from, to));
      }
      query.addOrder(content, ContentBMPBean.getColumnNameCreated(), false);
      
      /*StringBuffer sql = new StringBuffer("SELECT N.*,C.* FROM ");
      sql.append(com.idega.block.news.data.NwNewsBMPBean.getEntityTableName());
      sql.append(" N, ");
      sql.append(com.idega.block.text.data.LocalizedTextBMPBean.getEntityTableName());
      sql.append(" T, ");
      sql.append(middleTable);
      sql.append(" M, ");
      sql.append(com.idega.block.text.data.ContentBMPBean.getEntityTableName());
      sql.append(" C ");
      sql.append("WHERE N.");
      sql.append(com.idega.block.news.data.NwNewsBMPBean.getColumnNameContentId());
      sql.append(" = C.");
      sql.append(com.idega.block.text.data.ContentBMPBean.getEntityTableName());
      sql.append("_ID AND ");
      sql.append(com.idega.block.news.data.NwNewsBMPBean.getColumnNameNewsCategoryId());

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
      sql.append(com.idega.block.text.data.ContentBMPBean.getEntityTableName());
      sql.append("_ID = M.");
      sql.append(com.idega.block.text.data.ContentBMPBean.getEntityTableName());
      sql.append("_ID AND M.");
      sql.append(com.idega.block.text.data.LocalizedTextBMPBean.getEntityTableName());
      sql.append("_ID = T.");
      sql.append(com.idega.block.text.data.LocalizedTextBMPBean.getEntityTableName());
      sql.append("_ID AND T.");
      sql.append(com.idega.block.text.data.LocalizedTextBMPBean.getColumnNameLocaleId());
      sql.append(" = ");
      sql.append(iLocaleId);

    // USE BETWEEN
    /*
    if(!ignorePublishingDates ){
      IWTimestamp today = IWTimestamp.RightNow();
      sql.append(" and '");
      sql.append(today.toSQLString());
      sql.append("' between ");
      sql.append(com.idega.block.text.data.ContentBMPBean.getColumnNamePublishFrom() );
      sql.append(" and ");
      sql.append(com.idega.block.text.data.ContentBMPBean.getColumnNamePublishTo());

    }
    */
    // USE OPERATORS <= AND >=

    /*if(!ignorePublishingDates ){
      IWTimestamp today = IWTimestamp.RightNow();
      sql.append(" and ");
      sql.append(com.idega.block.text.data.ContentBMPBean.getColumnNamePublishFrom() );
      sql.append(" <= '");
      sql.append(today.toSQLString());
      sql.append("' and ");
      sql.append(com.idega.block.text.data.ContentBMPBean.getColumnNamePublishTo());
      sql.append(" >= '");
      sql.append(today.toSQLString());
      sql.append("' ");
    }

    sql.append(" order by C.");
    sql.append(com.idega.block.text.data.ContentBMPBean.getColumnNameCreated());
    sql.append(" desc ");*/
    //
    //System.err.println(sql.toString());
    try {
      return EntityFinder.findAll(((com.idega.block.news.data.NwNewsHome)com.idega.data.IDOLookup.getHomeLegacy(NwNews.class)).createLegacy(),query.toString());
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
      return listOfNewsFiles(((com.idega.block.news.data.NwNewsHome)com.idega.data.IDOLookup.getHomeLegacy(NwNews.class)).findByPrimaryKeyLegacy(id));
    }
    catch (SQLException ex) {

    }
    return null;
  }


  public static List listOfNewsFiles(NwNews nwNews){
    try {
      return (List)nwNews.getRelatedFiles();
    }
    catch (IDORelationshipException e) {
		e.printStackTrace();
	}
    return null;
  }


  public static List listOfLocalizedText(int iNwNewsId){
    List L = null;
    try {
      NwNews tt = ((com.idega.block.news.data.NwNewsHome)com.idega.data.IDOLookup.getHomeLegacy(NwNews.class)).findByPrimaryKeyLegacy(iNwNewsId);
      LocalizedText lt = ((com.idega.block.text.data.LocalizedTextHome)com.idega.data.IDOLookup.getHomeLegacy(LocalizedText.class)).createLegacy();
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
        return ((com.idega.block.news.data.NwNewsHome)com.idega.data.IDOLookup.getHomeLegacy(NwNews.class)).findByPrimaryKeyLegacy(iNewsId);
      }
      catch (SQLException ex) {
        ex.printStackTrace();
        return null;
      }
    }

    public static int	countNewsInCategory(int iCategoryId){
      try {
              NwNews news = (NwNews)com.idega.block.news.data.NwNewsBMPBean.getStaticInstance(NwNews.class);
              return news.getNumberOfRecords(com.idega.block.news.data.NwNewsBMPBean.getColumnNameNewsCategoryId(),iCategoryId);
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
      return 0;
    }

    public static int countNewsInCategory(int iCategoryId, int PublishType){
      StringBuffer sql = new StringBuffer("select count(*) from ");
      sql.append(com.idega.block.news.data.NwNewsBMPBean.getEntityTableName());
      sql.append(" n,");
      sql.append(com.idega.block.text.data.ContentBMPBean.getEntityTableName());
      sql.append(" c where n.");
      sql.append(com.idega.block.news.data.NwNewsBMPBean.getColumnNameContentId());
      sql.append(" = c.");
      sql.append(com.idega.block.text.data.ContentBMPBean.getEntityTableName());
      sql.append("_id and ");
      sql.append(com.idega.block.news.data.NwNewsBMPBean.getColumnNameNewsCategoryId());
      sql.append(" = ");
      sql.append(iCategoryId);
      if(PublishType > 0){
              String today = IWTimestamp.RightNow().toSQLString();
      switch (PublishType) {
        case UNPUBLISHED :
                        sql.append(" and c.");
                        sql.append(com.idega.block.text.data.ContentBMPBean.getColumnNamePublishFrom() );
                        sql.append(" >= '");
                        sql.append(today);
                        sql.append("' ");
        break;
        case PUBLISHISING:
                        sql.append(" and c.");
                        sql.append(com.idega.block.text.data.ContentBMPBean.getColumnNamePublishFrom() );
                        sql.append(" <= '");
                        sql.append(today);
                        sql.append("' and c.");
                        sql.append(com.idega.block.text.data.ContentBMPBean.getColumnNamePublishTo());
                        sql.append(" >= '");
                        sql.append(today);
                        sql.append("' ");
        break;
        case PUBLISHED:
          sql.append(" and c.");
                sql.append(com.idega.block.text.data.ContentBMPBean.getColumnNamePublishTo());
                sql.append(" <= '");
                sql.append(today);
                sql.append("' ");
        break;
      }
    }
    NwNews ge = (NwNews)com.idega.block.news.data.NwNewsBMPBean.getStaticInstance(NwNews.class);
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
    return CategoryFinder.getInstance().listOfCategories(((com.idega.block.news.data.NewsCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(NewsCategory.class)).createLegacy().getCategoryType());
  }

  public static List listOfValidNewsCategories(){
    return CategoryFinder.getInstance().listOfValidCategories(((com.idega.block.news.data.NewsCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(NewsCategory.class)).createLegacy().getCategoryType());
  }

  public static List listOfInValidNewsCategories(){
    return CategoryFinder.getInstance().listOfInValidCategories(((com.idega.block.news.data.NewsCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(NewsCategory.class)).createLegacy().getCategoryType());
  }

  public static int getObjectInstanceIdFromNewsCategoryId(int iCategoryId){
    return CategoryFinder.getInstance().getObjectInstanceIdFromCategoryId(iCategoryId);
  }

  public static int getObjectInstanceCategoryId(int iObjectInstanceId,boolean CreateNew){
    return CategoryFinder.getInstance().getObjectInstanceCategoryId(iObjectInstanceId,CreateNew,((com.idega.block.news.data.NewsCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(NewsCategory.class)).createLegacy().getCategoryType());
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
