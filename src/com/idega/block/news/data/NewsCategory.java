//idega 2000 - ægir og eiki

package com.idega.block.news.data;

//import java.util.*;
import java.sql.*;
//import com.idega.data.*;
import com.idega.data.*;

public class NewsCategory extends GenericEntity{

	public NewsCategory(){
		super();
	}

	public NewsCategory(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("NEWS_CATEGORY_NAME", "Flokkur", true, true, "java.lang.String");
		addAttribute("DESCRIPTION", "Lýsing", true, true, "java.lang.String");
		addAttribute("NEWS_CATEGORY_DATE", "Dagsetning", true, true, "java.sql.Date");
		addAttribute("VALID", "Virkur", true, true, "java.lang.String",1);
	//	addAttribute("union_id", "Félag", true, true, "java.lang.Integer", "one-to-many","com.idega.projects.golf.entity.Union");

	}

	public String getIDColumnName(){
		return "NEWS_CATAGORY_ID";
	}

	public String getEntityName(){
		return "I_NEWS_CATEGORY";
	}


	public String getName(){
		return getNewsCategoryName();
	}

	public String getNewsCategoryName(){
		return getStringColumnValue("news_category_name");
	}

	public void setNewsCategoryName(String news_category_name){
			setColumn("news_category_name", news_category_name);
	}

	public String getDescription(){
		return getStringColumnValue("description");
	}

	public void setDescription(String description){
			setColumn("description", description);
	}

	public String getValid(){
		return getStringColumnValue("valid");
	}

	public void setValid(String valid){
		setColumn("valid", valid);
	}

	public java.sql.Date getDate(){
		return (java.sql.Date) getColumnValue("NEWS_CATEGORY_DATE");
	}

	public void setDate(java.sql.Date NEWS_CATEGORY_DATE){
			setColumn("NEWS_CATEGORY_DATE", NEWS_CATEGORY_DATE);
	}

	public int getUnionId(){
		return getIntColumnValue("union_id");
	}

	public void setUnionId(Integer union_id){
			setColumn("union_id", union_id);
	}




}
