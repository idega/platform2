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
    addAttribute(getNameColumnName(),"Heiti",true,true,"java.lang.String");
    addAttribute(getInfoColumnName(),"Lýsing",true,true,"java.lang.String",4000);
  }
  public static String getAccountKeyEntityName(){return "FIN_TARIFF_KEY"; }
  public static String getNameColumnName(){ return "NAME"; }
  public static String getInfoColumnName(){return "INFO";}

  public String getEntityName() {
    return getAccountKeyEntityName();
  }
  public String getName(){
    return getStringColumnValue(getNameColumnName());
  }
  public void setName(String name){
    setColumn(getNameColumnName(), name);
  }
  public String getInfo(){
    return getStringColumnValue(getInfoColumnName());
  }
  public void setInfo(String info){
    setColumn(getInfoColumnName(), info);
  }
}