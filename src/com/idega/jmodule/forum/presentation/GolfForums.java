package com.idega.jmodule.forum.presentation;

import java.sql.*;


/**
 * Title:        idegaForms
 * Description:
 * Copyright:    Copyright (c) 2000 idega margmiðlun hf.
 * Company:      idega margmiðlun hf.
 * @author idega 2000 - idega team - <a href="mailto:gummi@idega.is">gummi@idega.is</a>
 * @version 1.0
 */

public class GolfForums extends GrayForums {

  public GolfForums() throws Exception {
    super();
    setMenuColor("#669966");
    setItemColor("#99CC99");
  }


  public GolfForums(String user_name, int user_id) throws Exception {
    super(user_name, user_id);
    setMenuColor("#669966");
    setItemColor("#99CC99");
 }



  public void initBooleans(){
    setUseForums(true);
    setUseUserRegistration(false);
    setUseLogin(false);
    setUseNameField(true);
  }

  public void priset(){

  }

}