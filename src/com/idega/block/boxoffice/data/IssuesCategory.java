//idega 2000 - Laddi

package com.idega.block.boxoffice.data;

import java.sql.*;
import com.idega.data.*;

public class IssuesCategory extends GenericEntity{

	public IssuesCategory(){
		super();
	}

	public IssuesCategory(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("category_name", "Nafn", true, true, "java.lang.String");
		addAttribute("image_id", "Issue mynd", true, true, "java.lang.Integer");
	}

	public String getIDColumnName(){
		return "box_category_id";
	}

	public String getEntityName(){
		return "i_box_category";
	}

	public String getName(){
		return getCategoryName();
	}

	public String getCategoryName(){
		return getStringColumnValue("category_name");
	}

	public void setCategoryName(String category_name){
			setColumn("category_name", category_name);
	}

	public int getImageId(){
		return getIntColumnValue("image_id");
	}

	public void setImageId(int image_id){
			setColumn("image_id", image_id);
	}

}
