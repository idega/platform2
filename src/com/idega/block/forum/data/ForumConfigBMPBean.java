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



public class ForumConfigBMPBean extends com.idega.data.GenericEntity implements com.idega.block.forum.data.ForumConfig {



  public ForumConfigBMPBean() {

    super();

  }





  public ForumConfigBMPBean(int id) throws SQLException{

    super(id);

  }



  public void initializeAttributes(){

    addAttribute(getIDColumnName());

  }



  public String getEntityName(){

    return "fo_forum_config";

  }









}   // class ForumConfig

