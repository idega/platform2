package com.idega.projects.hackers.service;

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

public class Record extends GenericEntity {

  public Record() {
    super();
  }
  public Record(int id) throws SQLException {
    super(id);
  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute("score","Skor",true,true,"java.lang.Integer");
    addAttribute("date","dags",true,true,"java.sql.Timestamp");
    addAttribute("who","Hver / Hverjir",true,true,"java.lang.String");
    addAttribute("extra_info","Lýsing",true,true,"java.lang.String");
  }
  public String getEntityName() {
    return "record";
  }

  public int getScore(){
    return getIntColumnValue("score");
  }
  public void setScore(int score){
    setColumn("score",score);
  }
  public Timestamp getDate(){
    return (Timestamp) getColumnValue("date");
  }
  public void setDate(Timestamp date){
    setColumn("date",date);
  }

  public String getWho(){
    return getStringColumnValue("who");
  }
  public void setWho(String who){
    setColumn("who",who);
  }
  public String getExtraInfo(){
    return getStringColumnValue("extra_info");
  }
  public void setExtraInfo(String extra_info){
    setColumn("extra_info", extra_info);
  }
}