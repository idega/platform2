package com.idega.block.finance.data;

import java.sql.SQLException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class EntryKeyBMPBean extends com.idega.data.GenericEntity implements com.idega.block.finance.data.EntryKey {

  public EntryKeyBMPBean() {
    super();
  }
  public EntryKeyBMPBean(int id) throws SQLException {
    super(id);
  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute("name","Heiti",true,true,"java.lang.String");
    addAttribute("positive","Færslugerð",true,true,"java.lang.Boolean");
    addAttribute("extra_info","Lýsing",true,true,"java.lang.String");
  }
  public String getEntityName() {
    return "fin_entry_key";
  }
  public String getName(){
    return getStringColumnValue("name");
  }
  public void setName(String name){
    setColumn("name", name);
  }
  public boolean getPositive(){
    return getBooleanColumnValue("positive");
  }
  public void setPositive(boolean positive){
    setColumn("positive",positive);
  }
  public String getExtraInfo(){
    return getStringColumnValue("extra_info");
  }
  public void setExtraInfo(String extra_info){
    setColumn("extra_info", extra_info);
  }
}
