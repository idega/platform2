 package com.idega.block.forum.data;

import com.idega.data.*;
import com.idega.block.forum.business.ForumService;
import java.sql.*;

/**
 * Title:        idegaForms
 * Description:
 * Copyright:    Copyright (c) 2000 idega margmiðlun hf.
 * Company:      idega margmiðlun hf.
 * @author idega 2000 - idega team - <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class ForumAttributes extends GenericEntity {

  public ForumAttributes() {
    super();
  }

  public ForumAttributes(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute("forum_id","ForumID",true,true,"java.lang.Integer");
    addAttribute("attribute_name","Attribute Name",true,true,"java.lang.String");
    addAttribute("attribute_id","Attribute ID",true,true,"java.lang.Integer");
  }

  public String getEntityName() {
    return "fo_forum_attributes";
  }


  public void insertStartData(){
    /*try {
      ForumAttributes fa = new ForumAttributes();
      ForumService fs = new ForumService();
      int id = fs.createForum("Name", "Description");
      fa.setForumID(new Integer(id));
      fa.insert();
    }
    catch (Exception ex) {

    }
    */
  }

  public int getForumID(){
    return getIntColumnValue("forum_id");
  }

  public void setForumID( Integer forum_id){
    setColumn("forum_id", forum_id);
  }



  public String getAttributeName(){
    return (String)getColumnValue("attribute_name");
  }

  public void setAttributeName( String attribute_name){
    setColumn("attribute_name",attribute_name);
  }


  public int getAttributeID(){
    return getIntColumnValue("attribute_id");
  }

  public void setAttributeID( Integer attribute_id){
    setColumn("attribute_id", attribute_id);
  }




} // class ForumAttributes
