//idega 2000 - Laddi

package com.idega.block.boxoffice.data;

import java.sql.*;
import com.idega.data.*;

public class Subject extends GenericEntity{

	public Subject(){
		super();
	}

	public Subject(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("issue_id", "Málaflokkur", true, true, "java.lang.Integer","many-to-one","com.idega.jmodule.boxoffice.data.Issues");
		addAttribute("issue_category_id", "Flokkur", true, true, "java.lang.Integer","many-to-one","com.idega.jmodule.boxoffice.data.IssuesCategory");
		addAttribute("subject_name", "Nafn skjals", true, true, "java.lang.String");
		addAttribute("content_id", "Gerð skjals", true, true, "java.lang.Integer","many-to-one","com.idega.jmodule.boxoffice.data.Content");
		addAttribute("subject_value", "Innihald skjals", true, true, "java.lang.String",4000);
		addAttribute("include_image", "Mynd?", true, true, "java.lang.String");
		addAttribute("image_id", "Mynd", true, true, "java.lang.Integer");
		addAttribute("subject_date", "Dagsetning", true, true, "java.sql.Timestamp");
		addAttribute("subject_author", "Höfundur", true, true, "java.lang.String");
	}

	public String getIDColumnName(){
		return "box_subject_id";
	}

	public String getEntityName(){
		return "i_box_subject";
	}

	public int getIssueId(){
		return getIntColumnValue("issue_id");
	}

	public void setIssueId(int issue_id){
			setColumn("issue_id", issue_id);
	}

	public String getIssueName()throws SQLException{
		String issue_name = "";
		try{
			Issues issues = new Issues(this.getIssueId());
			issue_name = issues.getIssueName();
		}
		catch(SQLException ex){
		}
		return issue_name;
	}

	public int getIssueImage()throws SQLException{
		int image_id = 0;
		try{
			Issues issue = new Issues(this.getIssueId());
			image_id = issue.getImageId();
		}
		catch(SQLException ex){
		}
		return image_id;
	}

	public int getIssueCategoryId(){
		return getIntColumnValue("issue_category_id");
	}

	public void setIssueCategoryId(int issue_category_id){
			setColumn("issue_category_id", issue_category_id);
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

	public String getSubjectName(){
		return getStringColumnValue("subject_name");
	}

	public void setSubjectName(String subject_name){
			setColumn("subject_name", subject_name);
	}

	public int getContentId(){
		return getIntColumnValue("content_id");
	}

	public void setContentId(int content_id){
			setColumn("content_id", content_id);
	}

	public int getContentImage()throws SQLException{
		int image_id = 0;
		try{
			Content content = new Content(this.getContentId());
			image_id = content.getImageId();
		}
		catch(SQLException ex){
		}
		return image_id;
	}

	public String getContentName()throws SQLException{
		String content_name = "";
		try{
			Content content = new Content(this.getContentId());
			content_name = content.getContentValue();
		}
		catch(SQLException ex){
		}
		return content_name;
	}

	public String getSubjectValue(){
		return getStringColumnValue("subject_value");
	}

	public void setSubjectValue(String subject_value){
			setColumn("subject_value", subject_value);
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

	public void setImageId(int image_id){
			setColumn("image_id", image_id);
	}

	public java.sql.Timestamp getSubjectDate(){
		return (java.sql.Timestamp) getColumnValue("subject_date");
	}

	public void setSubjectDate(java.sql.Timestamp subjectDate){
		setColumn("subject_date", subjectDate);
	}

        public String getSubjectAuthor(){
		return getStringColumnValue("subject_author");
	}

	public void setSubjectAuthor(String subject_author){
		setColumn("subject_author", subject_author);
	}
}
