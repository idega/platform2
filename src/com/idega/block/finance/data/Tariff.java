package com.idega.block.finance.data;

import java.sql.*;
import com.idega.data.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class Tariff extends GenericEntity {

  public Tariff() {
  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute("name","Heiti",true,true,"java.lang.String");
    addAttribute("price", "Verð", true, true, "java.lang.Integer");
    addAttribute("extra_info","Athugasemd",true,true,"java.lang.String");
    addAttribute("in_use","Gild",true,true,"java.lang.Boolean");
  }
  public String getEntityName() {
    return "tariff";
  }
  public String getName(){
    return getStringColumnValue("name");
  }
  public void setName(String name){
    setColumn("name", name);
  }
  public String getExtraInfo(){
    return getStringColumnValue("extra_info");
  }
  public void setExtraInfo(String extra_info){
    setColumn("extra_info", extra_info);
  }
}