//idega 2000 - ægir og eiki

package com.idega.block.news.data;

//import java.util.*;
import java.sql.*;
//import com.idega.data.*;
import com.idega.data.*;

public class News extends GenericEntity{

  public News(){
          super();
  }
  public News(int id)throws SQLException{
          super(id);
  }
  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getNewsCategoryIdColumnName(), "Category", true, true, Integer.class, "many-to-one",NewsCategory.class);
    addAttribute(getHeadLineColumnName(), "Headline", true, true, String.class);
    addAttribute(getNewsTextColumnName(), "Text", true, true, String.class,4000);
    addAttribute(getIncludeImageColumnName(), "Photo Included", true, true, String.class);
    addAttribute(getImageIdColumnName(), "Photo", true, true, Integer.class);
    addAttribute(getNewsDateColumnName(), "Date", true, true, java.sql.Timestamp.class);
    addAttribute(getAuthorColumnName(), "Author", true, true, String.class);
    addAttribute(getSourceColumnName(), "Source", true, true, String.class);
    addAttribute(getDaysShownColumnName(), "Days shown", true, true, String.class);
  }
  public String getEntityName(){
    return getNewsTableName();
  }
  public static String getNewsTableName(){return "NW_NEWS";}
  public static String getNewsCategoryIdColumnName(){return "NW_NEWS_CAT_ID";}
  public static String getHeadLineColumnName(){return "HEADLINE";}
  public static String getNewsTextColumnName(){return "NEWSTEXT";}
  public static String getIncludeImageColumnName(){return "INCLUDE_IMAGE";}
  public static String getNewsDateColumnName(){return "NEWS_DATE";}
  public static String getAuthorColumnName(){return "AUTHOR";}
  public static String getSourceColumnName(){return "SOURCE";}
  public static String getDaysShownColumnName(){return "DAYS_SHOWN";}
  public static String getImageIdColumnName(){return"IC_IMAGE_ID";}

  public String getName(){
    return getHeadline();
  }

  public void setDefaultValues() {
    this.setNewsCategoryId(1);
    this.setHeadline("");
    this.setIncludeImage("N");
    this.setText("");
    this.setImageId(-1);
    this.setDaysShown(-1);
    this.setSource("");
    this.setAuthor("");
    this.setDate(new com.idega.util.idegaTimestamp().getTimestampRightNow());
  }


  public int getNewsCategoryId(){
    return getIntColumnValue(getNewsCategoryIdColumnName());
  }
  public void setNewsCategoryId(Integer news_category_id){
    setColumn(getNewsCategoryIdColumnName(), news_category_id);
  }
  public void setNewsCategoryId(int news_category_id){
    setColumn(getNewsCategoryIdColumnName(), new Integer(news_category_id));
  }
  public String getHeadline(){
    return getStringColumnValue(getHeadLineColumnName());
  }
  public void setHeadline(String headline){
    setColumn(getHeadLineColumnName(), headline);
  }
  public String getText(){
    return getStringColumnValue(getNewsTextColumnName());
  }
  public void setText(String newstext){
    setColumn(getNewsTextColumnName(), newstext);
  }
  public String getIncludeImage(){
    return getStringColumnValue(getIncludeImageColumnName());
  }
  public void setIncludeImage(String include_image){
    setColumn(getIncludeImageColumnName(), include_image);
  }
  public int getImageId(){
    return getIntColumnValue(getImageIdColumnName());
  }
  public void setImageId(Integer image_id){
    setColumn(getImageIdColumnName(), image_id);
  }
  public void setImageId(int image_id){
    setColumn(getImageIdColumnName(),new Integer(image_id));
  }
  public java.sql.Timestamp getDate(){
    return (java.sql.Timestamp) getColumnValue(getNewsDateColumnName());
  }
  public java.sql.Timestamp getNewsDate(){
    return  getDate();
  }
  public void setDate(java.sql.Timestamp news_date){
    setColumn(getNewsDateColumnName(), news_date);
  }
  public void setNewsDate(java.sql.Timestamp news_date){
    setDate(news_date);
  }
  public String getAuthor(){
    return getStringColumnValue(getAuthorColumnName());
  }
  public void setAuthor(String author){
    setColumn(getAuthorColumnName(), author);
  }
  public String getSource(){
    return getStringColumnValue(getSourceColumnName());
  }
  public void setSource(String source){
    setColumn(getSourceColumnName(), source);
  }
  public int getDaysShown(){
    return getIntColumnValue(getDaysShownColumnName());
  }
  public void setDaysShown(Integer days_shown){
    setColumn(getDaysShownColumnName(), days_shown);
  }
  public void setDaysShown(int days_shown){
    setColumn(getDaysShownColumnName(), new Integer(days_shown));
  }
}
