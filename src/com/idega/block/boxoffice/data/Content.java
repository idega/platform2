//idega 2000 - Laddi

package com.idega.block.boxoffice.data;

import java.sql.*;
import com.idega.data.*;

public class Content extends GenericEntity{

	public Content(){
		super();
	}

	public Content(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("content_value", "Flokkur", true, true, "java.lang.String");
		addAttribute("image_id", "ImageID", true, true, "java.lang.Integer");
	}

	public String getIDColumnName(){
		return "box_content_id";
	}

	public String getEntityName(){
		return "i_box_content";
	}

	public String getValue(){
		return getContentValue();
	}

	public String getName(){
		return getContentValue();
	}

	public String getContentValue(){
		return getStringColumnValue("content_value");
	}

	public void setContentValue(String content_value){
			setColumn("content_value", content_value);
	}

	public int getImageId(){
		return getIntColumnValue("image_id");
	}

	public void setImageId(Integer image_id){
			setColumn("image_id", image_id);
	}

	public void setImageId(int image_id){
			setColumn("image_id", image_id);
	}

}
