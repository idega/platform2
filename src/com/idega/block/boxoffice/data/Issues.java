//idega 2000 - Laddi

package com.idega.block.boxoffice.data;

import java.sql.*;
import com.idega.data.*;

public class Issues extends GenericEntity{

	public Issues(){
		super();
	}

	public Issues(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("issue_name", "Nafn", true, true, "java.lang.String");
		addAttribute("image_id", "Issue mynd", true, true, "java.lang.Integer");
	}

	public String getIDColumnName(){
		return "box_issue_id";
	}

	public String getEntityName(){
		return "i_box_issues";
	}

        public void setDefaultValues() {
            this.setImageId(-1);
            this.setIssueName(" ");
        }

	public String getName(){
		return getIssueName();
	}

	public String getIssueName(){
		return getStringColumnValue("issue_name");
	}

	public void setIssueName(String issue_name){
			setColumn("issue_name", issue_name);
	}

	public int getImageId(){
		return getIntColumnValue("image_id");
	}

	public void setImageId(int image_id){
			setColumn("image_id", image_id);
	}


}
