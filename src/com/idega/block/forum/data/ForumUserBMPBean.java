package com.idega.block.forum.data;



import com.idega.data.*;

import java.sql.*;



/**

 * Title:        JForums<p>

 * Description:  <p>

 * Copyright:    Copyright (c) idega margmiðlun hf.<p>

 * Company:      idega margmiðlun hf.<p>

 * @author idega 2000 - idega team - <a href="mailto:gummi@idega.is">gummi@idega.is</a>

 * @version 1.0

 */



public class ForumUserBMPBean extends com.idega.data.GenericEntity implements com.idega.block.forum.data.ForumUser {



  public ForumUserBMPBean() {

    super();

  }



  public ForumUserBMPBean(int id) throws SQLException{

    super(id);

  }



  public void initializeAttributes(){

    addAttribute(getIDColumnName());

    addAttribute("user_name","Notandanafn",true,true,"java.lang.String");

    addAttribute("user_id","Notendanumer",true,true,"java.lang.Integer");

  }



  public String getEntityName(){

    return "fo_forum_user";

  }





    // ### get- & set-Föll ###







  public String getUserName(){

    return (String)getColumnValue("user_name");

  }



  public void setName(String user_name){

    setColumn("user_name",user_name);

  }





  public int getUserID(){

    return getIntColumnValue("user_id");

  }



  public void setUserID(Integer user_id){

    setColumn("user_id",user_id);

  }







}

