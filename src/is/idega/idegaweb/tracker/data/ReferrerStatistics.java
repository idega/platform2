package com.idega.idegaweb.tracker.data;

import java.sql.Timestamp;
import java.sql.SQLException;
import com.idega.data.GenericEntity;

/**
 * Title:        com.idega.idegaweb.tracker.data.ReferrerStatistics
 * Description:  Keeps track of referrer urls
 * Copyright:    Copyright (c) 2002
 * Company:      idega
 * @author <a href="eiki@idega.is">Eirikur S. Hrafnsson</a>Eirikur S. Hrafnsson</a>
 * @version 1.0
 */

public class ReferrerStatistics extends GenericEntity {

  public ReferrerStatistics() {
    super();
  }

  public ReferrerStatistics(int id) throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameReferrerUrl(),"Referrer",true,true,String.class,500);
    addAttribute(getColumnNameSessions(),"Number of references",true,true,Integer.class);
    addAttribute(getColumnNameDate(),"Date of record",true,true, Timestamp.class);
  }

  public String getEntityName() {
    return getEntityTableName();
  }

  public static String getEntityTableName(){ return "TR_REFERRER_STATISTICS";}
  public static String getColumnNameReferrerUrl(){return "IB_REFERRER_URL";}
  public static String getColumnNameSessions(){return "SESSIONS";}
  public static String getColumnNameDate(){return "MODIFICATION_DATE";}

  public String getReferrerUrl(){
    return (String) getColumnValue(getColumnNameReferrerUrl());
  }

  public int getSessions(){
    return getIntColumnValue(getColumnNameSessions());
  }

  public Timestamp getDate(){
    return (Timestamp) getColumnValue(getColumnNameDate());
  }

  public void setReferrerUrl(String referrerUrl){
    setColumn(getColumnNameReferrerUrl(), referrerUrl);
  }

  public void setSessions(int sessionHits){
    setColumn(getColumnNameSessions(), sessionHits);
  }

  public void setModificationDate(Timestamp date){
    setColumn(getColumnNameDate(), date);
  }


}