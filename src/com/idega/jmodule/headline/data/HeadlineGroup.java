//idega 2000 - "Laddi er sauður..." - Eiki

package com.idega.jmodule.headline.data;

import java.sql.*;
import com.idega.data.*;

public class HeadlineGroup extends GenericEntity{

  public HeadlineGroup(){
          super();
  }

  public HeadlineGroup(int id)throws SQLException{
          super(id);
  }

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute("name", "name", true, true, "java.lang.String");
    addAttribute("filename", "site", true, true, "java.lang.String");
  }

  public String getIDColumnName(){
          return "group_id";
  }

  public String getEntityName(){
          return "headline_group";
  }

  public String getName(){
          return getStringColumnValue("name");
  }

  public void setName(String name){
          setColumn("name", name);
  }
  public String getFileName(){
          return getStringColumnValue("filename");
  }

  public void setFileName(String filename){
          setColumn("filename", filename);
  }


}
