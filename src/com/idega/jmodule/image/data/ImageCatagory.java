package com.idega.jmodule.image.data;

import java.sql.*;
import com.idega.data.*;


public class ImageCatagory extends GenericEntity{

	public ImageCatagory(){
		super();
	}

	public ImageCatagory(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("image_catagory_name","Image Category Name",true,true,"java.lang.String");
                addAttribute("parent_id","Image Category parent",true,true,"java.lang.Integer");
	}

	public String getEntityName(){
		return "image_catagory";
	}


         public void setDefaultValues() {
          this.setParentId(-1);
        }

	public String getImageCatagoryName() {
          return (String) getColumnValue("image_catagory_name");
	}

        public String getName(){
          return getImageCatagoryName();
        }

        public void setParentId(int parent_id) {
          setColumn("parent_id",new Integer(parent_id));
        }

        public int getParentId() {
          return getIntColumnValue("parent_id");
        }


}
