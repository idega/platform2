package com.idega.block.news.data;

//import java.util.*;
import java.sql.*;
//import com.idega.data.*;
import com.idega.data.*;
import com.idega.block.text.data.Content;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class NwNews extends GenericEntity{

  public NwNews(){
          super();
  }
  public NwNews(int id)throws SQLException{
          super(id);
  }
  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameContentId(), "Content", true, true, Integer.class,"many-to-one",Content.class);
    addAttribute(getColumnNameNewsCategoryId(), "Category", true, true, Integer.class, "many-to-one",NewsCategory.class);
    addAttribute(getColumnNameAuthor(), "Author", true, true, String.class);
    addAttribute(getColumnNameSource(), "Source", true, true, String.class);

  }
  public String getEntityName(){
    return getEntityTableName();
  }
  public static String getEntityTableName(){return "NW_NEWS";}

  public static String getColumnNameNewsCategoryId(){return "NW_NEWS_CAT_ID";}
  public static String getColumnNameContentId(){ return "CONTENT_ID";}
  public static String getColumnNameAuthor(){return "AUTHOR";}
  public static String getColumnNameSource(){return "SOURCE";}

  public void setDefaultValues() {
    this.setNewsCategoryId(1);
    this.setSource("");
    this.setAuthor("");
  }


  public int getNewsCategoryId(){
    return getIntColumnValue(getColumnNameNewsCategoryId());
  }
  public void setNewsCategoryId(Integer news_category_id){
    setColumn(getColumnNameNewsCategoryId(), news_category_id);
  }
  public void setNewsCategoryId(int news_category_id){
    setColumn(getColumnNameNewsCategoryId(), new Integer(news_category_id));
  }
  public int getContentId(){
    return getIntColumnValue(getColumnNameContentId());
  }
  public void setContentId(int iContentId){
    setColumn(getColumnNameContentId(),iContentId);
  }
  public void setContentId(Integer iContentId){
    setColumn(getColumnNameContentId(),iContentId);
  }
  public String getAuthor(){
    return getStringColumnValue(getColumnNameAuthor());
  }
  public void setAuthor(String author){
    setColumn(getColumnNameAuthor(), author);
  }
  public String getSource(){
    return getStringColumnValue(getColumnNameSource());
  }
  public void setSource(String source){
    setColumn(getColumnNameSource(), source);
  }

}
