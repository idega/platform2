package com.idega.block.finance.data;

import com.idega.block.finance.business.Key;
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

public class TariffKey extends GenericEntity implements Key{

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
  public String getInfo(){
    return getStringColumnValue("extra_info");
  }
  public void setInfo(String extra_info){
    setColumn("extra_info", extra_info);
  }
}