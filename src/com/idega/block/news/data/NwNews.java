//idega 2000 - ægir og eiki

package com.idega.block.news.data;

//import java.util.*;
import java.sql.*;
//import com.idega.data.*;
import com.idega.data.*;

public class NwNews extends GenericEntity{

  public NwNews(){
          super();
  }
  public NwNews(int id)throws SQLException{
          super(id);
  }
  public void initializeAttributes(){
    addAttribute(getIDColumnName());
     addAttribute(getColumnNameUserId(),"User",true,true, java.lang.Integer.class,"many-to-one",com.idega.core.user.data.User.class);
    addAttribute(getColumnNameNewsCategoryId(), "Category", true, true, Integer.class, "many-to-one",NewsCategory.class);
    addAttribute(getColumnNameHeadLine(), "Headline", true, true, String.class);
    addAttribute(getColumnNameNewsText(), "Text", true, true, String.class,4000);
    addAttribute(getColumnNameIncludeImage(), "Photo Included", true, true, Boolean.class);
    addAttribute(getColumnNameImageId(), "Photo", true, true, Integer.class);
    addAttribute(getColumnNameNewsDate(), "Date", true, true, java.sql.Timestamp.class);
    addAttribute(getColumnNameAuthor(), "Author", true, true, String.class);
    addAttribute(getColumnNameSource(), "Source", true, true, String.class);
    addAttribute(getColumnNameDaysShown(), "Days shown", true, true, Integer.class);
    addAttribute(getColumnNameUpdated(), "Headline", true, true, java.sql.Timestamp.class);
    addManyToManyRelationShip(com.idega.block.text.data.LocalizedText.class,"NW_NEWS_TX_LOCALIZED_TEXT");
  }
  public String getEntityName(){
    return getEntityTableName();
  }
  public static String getEntityTableName(){return "NW_NEWS";}
  public static String getColumnNameNewsCategoryId(){return "NW_NEWS_CAT_ID";}
  public static String getColumnNameHeadLine(){return "HEADLINE";}
  public static String getColumnNameNewsText(){return "NEWSTEXT";}
  public static String getColumnNameIncludeImage(){return "INCLUDE_IMAGE";}
  public static String getColumnNameNewsDate(){return "NEWS_DATE";}
  public static String getColumnNameAuthor(){return "AUTHOR";}
  public static String getColumnNameSource(){return "SOURCE";}
  public static String getColumnNameUserId(){ return "IC_USER_ID";}
  public static String getColumnNameCreated(){ return "CREATED";}
  public static String getColumnNameUpdated(){ return "UPDATED";}
  public static String getColumnNameDaysShown(){return "DAYS_SHOWN";}
  public static String getColumnNameImageId(){return"IC_IMAGE_ID";}

  public String getName(){
    return getHeadline();
  }

  public void setDefaultValues() {
    this.setNewsCategoryId(1);
    this.setHeadline("");
    this.setIncludeImage(false);
    this.setText("");
    this.setImageId(-1);
    this.setDaysShown(-1);
    this.setSource("");
    this.setAuthor("");
    this.setDate(new com.idega.util.idegaTimestamp().getTimestampRightNow());
  }

  public int getUserId(){
    return getIntColumnValue(getColumnNameImageId());
  }
  public void setUserId(int id){
    setColumn(getColumnNameImageId(),id);
  }
  public void setUserId(Integer id){
    setColumn(getColumnNameImageId(),id);
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
  public String getHeadline(){
    return getStringColumnValue(getColumnNameHeadLine());
  }
  public void setHeadline(String headline){
    setColumn(getColumnNameHeadLine(), headline);
  }
  public String getText(){
    return getStringColumnValue(getColumnNameNewsText());
  }
  public void setText(String newstext){
    setColumn(getColumnNameNewsText(), newstext);
  }
  public boolean getIncludeImage(){
    return getBooleanColumnValue(getColumnNameIncludeImage());
  }
  public void setIncludeImage(boolean include_image){
    setColumn(getColumnNameIncludeImage(), include_image);
  }
  public int getImageId(){
    return getIntColumnValue(getColumnNameImageId());
  }
  public void setImageId(Integer image_id){
    setColumn(getColumnNameImageId(), image_id);
  }
  public void setImageId(int image_id){
    setColumn(getColumnNameImageId(),new Integer(image_id));
  }
  public java.sql.Timestamp getDate(){
    return (java.sql.Timestamp) getColumnValue(getColumnNameNewsDate());
  }
  public java.sql.Timestamp getNewsDate(){
    return  getDate();
  }
  public void setDate(java.sql.Timestamp news_date){
    setColumn(getColumnNameNewsDate(), news_date);
  }
  public void setNewsDate(java.sql.Timestamp news_date){
    setDate(news_date);
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
  public int getDaysShown(){
    return getIntColumnValue(getColumnNameDaysShown());
  }
  public void setDaysShown(Integer days_shown){
    setColumn(getColumnNameDaysShown(), days_shown);
  }
  public void setDaysShown(int days_shown){
    setColumn(getColumnNameDaysShown(), new Integer(days_shown));
  }
   public java.sql.Timestamp getCreated(){
    return (java.sql.Timestamp) getColumnValue(getColumnNameCreated());
  }
  public void setCreated(java.sql.Timestamp stamp){
    setColumn(getColumnNameCreated(), stamp);
  }
  public java.sql.Timestamp getUpdated(){
    return (java.sql.Timestamp) getColumnValue(getColumnNameUpdated());
  }
  public void setUpdated(java.sql.Timestamp stamp){
    setColumn(getColumnNameUpdated(), stamp);
  }
}
