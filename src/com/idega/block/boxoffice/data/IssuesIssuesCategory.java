//idega 2000 - Laddi

package com.idega.block.boxoffice.data;

import java.sql.*;
import com.idega.data.GenericEntity;

public class IssuesIssuesCategory extends GenericEntity{

	public IssuesIssuesCategory() {
		super();
	}

	public IssuesIssuesCategory(int id) throws SQLException {
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("issue_id", "Issue ID", true, true, "java.lang.Integer");
		addAttribute("issue_category_id", "Issue Category ID", true, true, "java.lang.Integer");
	}

	public String getIDColumnName(){
		return "box_issues_category_id";
	}

	public String getEntityName(){
		return "i_box_issues_category";
	}

	public int getIssueId(){
		return getIntColumnValue("issue_id");
	}

	public void setIssueId(int issue_id){
			setColumn("issue_id", issue_id);
	}

	public int getIssueCategoryId(){
		return getIntColumnValue("issue_category_id");
	}

	public void setIssueCategoryId(int issue_category_id){
			setColumn("issue_category_id", issue_category_id);
	}

	public String getIssueCategoryName()throws SQLException{
		String issue_category_name = "";
		try{
			IssuesCategory issues = new IssuesCategory(this.getIssueCategoryId());
			issue_category_name = issues.getCategoryName();
		}
		catch(SQLException ex){
		}
		return issue_category_name;
	}

	public int getIssueCategoryImage()throws SQLException{
		int image_id = 0;
		try{
			IssuesCategory issue_category = new IssuesCategory(this.getIssueCategoryId());
			image_id = issue_category.getImageId();
		}
		catch(SQLException ex){
		}
		return image_id;
	}

}
