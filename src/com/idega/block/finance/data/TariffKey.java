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

public class TariffKey extends GenericEntity {

  public TariffKey() {
    super();
  }
  public TariffKey(int id) throws SQLException {
    super(id);
  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute("name","Heiti",true,true,"java.lang.String");
    addAttribute("extra_info","Lýsing",true,true,"java.lang.String");
  }
  public String getEntityName() {
    return "tariff_key";
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