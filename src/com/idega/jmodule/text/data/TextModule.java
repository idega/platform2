//idega 2000 - Laddi

package com.idega.jmodule.text.data;

import java.sql.*;
import com.idega.data.*;
//import com.idega.projects.*;

public class TextModule extends GenericEntity{

	public TextModule(){
		super();
	}

	public TextModule(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("text_headline", "Fyrirsögn", true, true, "java.lang.String");
		addAttribute("text_body", "Text", true, true, "java.lang.String",20000);
		addAttribute("include_image", "Á að setja inn mynd?", true, true, "java.lang.String");
		addAttribute("image_id", "Mynd", true, true, "java.lang.Integer");
		//addAttribute("text_date", "Dagsetning", true, true, "java.sql.Date");
                addAttribute("text_date", "Dagsetning", true, true, "java.sql.Timestamp");
	}

	/*public void initializeColumnNames(){
		addColumnName(getIDColumnName());
		addColumnName("text_headline", "Fyrirsögn", true, true, "java.lang.String");
		addColumnName("text_body", "Text", true, true, "java.lang.String");
		addColumnName("include_image", "Á að setja inn mynd?", true, true, "java.lang.String");
		addColumnName("image_id", "Mynd", true, true, "java.lang.Integer");
		addColumnName("text_date", "Dagsetning", true, true, "java.sql.Date");
	}*/

	/*public String getIDColumnName(){
		return "text_id";
	}*/

        public void setDefaultValues() {
          com.idega.util.idegaTimestamp stamp = new com.idega.util.idegaTimestamp();
          this.setTextDate(stamp.getTimestampRightNow());
          this.setImageId(-1);
          this.setIncludeImage("N");
          this.setTextHeadline("");
          this.setTextBody("");
        }

	public String getEntityName(){
		return "text";
	}

	/*public String getTableName(){
		return "Text";
	}*/

	public String getTextHeadline(){
		return getStringColumnValue("text_headline");
	}

	public void setTextHeadline(String text_headline){
			setColumn("text_headline", text_headline);
	}

	public String getTextBody(){
		return getStringColumnValue("text_body");
	}

	public void setTextBody(String text_body){
			setColumn("text_body", text_body);
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

	public java.sql.Timestamp getTextDate(){
		return (java.sql.Timestamp) getColumnValue("text_date");
	}

	public void setTextDate(java.sql.Timestamp textDate){
		setColumn("text_date", textDate);
	}

}
