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
		addAttribute("news_category_id", "Flokkur", true, true, "java.lang.Integer", "many-to-one","com.idega.jmodule.news.data.NewsCategory");
		//addAttribute("union_id", "Félag", true, true, "java.lang.Integer", "one-to-many","com.idega.projects.golf.entity.Union");
		addAttribute("headline", "Fyrirsögn", true, true, "java.lang.String");
		addAttribute("newstext", "Texti", true, true, "java.lang.String",4000);
		addAttribute("include_image", "Mynd meðfylgjandi", true, true, "java.lang.String");
		addAttribute("image_id", "Mynd", true, true, "java.lang.Integer");
		//addAttribute("image_id", "Mynd", true, true, "java.lang.Integer", "one-to-one","com.idega.projects.golf.entity.ImageEntity");
		addAttribute("news_date", "Dagsetning", true, true, "java.sql.Timestamp");
               // addAttribute("news_date", "Dagsetning", true, true, "java.sql.Date");
		addAttribute("author", "Höfundur", true, true, "java.lang.String");
		addAttribute("source", "Heimild", true, true, "java.lang.String");
		addAttribute("days_shown", "Líftími - dagar", true, true, "java.lang.Integer");
	}

        public String getIDColumnName(){
          return "news_id";
        }

	public String getEntityName(){
          return "i_news";
	}

	public String getName(){
		return getHeadline();
	}

        public void setDefaultValues() {
          this.setNewsCategoryId(1);
          this.setUnionId(3);
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
		return getIntColumnValue("news_category_id");
	}

	public void setNewsCategoryId(Integer news_category_id){
			setColumn("news_category_id", news_category_id);
	}
	public void setNewsCategoryId(int news_category_id){
			setColumn("news_category_id", new Integer(news_category_id));
	}

	public int getUnionId(){
		return getIntColumnValue("union_id");
	}

	public void setUnionId(Integer union_id){
			setColumn("union_id", union_id);
	}
	public void setUnionId(int union_id){
			setColumn("union_id", new Integer(union_id));
	}

	public String getHeadline(){
		return getStringColumnValue("headline");
	}

	public void setHeadline(String headline){
			setColumn("headline", headline);
	}

	public String getText(){
		return getStringColumnValue("newstext");
	}

	public void setText(String newstext){
			setColumn("newstext", newstext);
	}

	public String getIncludeImage(){
		return getStringColumnValue("include_image");
	}

	public void setIncludeImage(String include_image){
			setColumn("include_image", include_image);
	}

	public int getImageId(){
		return getIntColumnValue("image_id");
	}

	public void setImageId(Integer image_id){
			setColumn("image_id", image_id);
	}
	public void setImageId(int image_id){
			setColumn("image_id",new Integer(image_id));
	}
//modified 02/11/00
	public java.sql.Timestamp getDate(){
          return (java.sql.Timestamp) getColumnValue("news_date");
	}

        public java.sql.Timestamp getNewsDate(){
          return  getDate();
        }

	public void setDate(java.sql.Timestamp news_date){
          setColumn("news_date", news_date);
	}

        public void setNewsDate(java.sql.Timestamp news_date){
          setDate(news_date);
	}

	public String getAuthor(){
		return getStringColumnValue("author");
	}

	public void setAuthor(String author){
			setColumn("author", author);
	}

	public String getSource(){
		return getStringColumnValue("source");
	}

	public void setSource(String source){
		setColumn("source", source);
	}

	public int getDaysShown(){
		return getIntColumnValue("days_shown");
	}

	public void setDaysShown(Integer days_shown){
			setColumn("days_shown", days_shown);
	}
	public void setDaysShown(int days_shown){
			setColumn("days_shown", new Integer(days_shown));
	}
}
