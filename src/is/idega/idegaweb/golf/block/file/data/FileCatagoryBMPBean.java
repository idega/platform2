package is.idega.idegaweb.golf.block.file.data;

import java.sql.*;
import com.idega.data.*;


public class FileCatagoryBMPBean extends GenericEntity{

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("file_catagory_name","Skjalaflokkur",true,true,"java.lang.String");
	}

	public String getEntityName(){
		return "file_catagory";
	}

	public String getFileCatagoryName() {
		return (String) getColumnValue("file_catagory_name");
	}
}