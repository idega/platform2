package com.idega.block.finance.data;



/**

 * Title:

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:      idega multimedia

 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>

 * @version 1.0

 */



public interface Entry {



  public static String typeFinancial = "FINANCE";

  public static String typePhone = "PHONE";



  public java.sql.Timestamp getLastUpdated();

  public String getType();

  public String getFieldNameLastUpdated();

  public String getFieldNameAccountId();

  public String getTableName();

  public String getFieldNameStatus();



}
