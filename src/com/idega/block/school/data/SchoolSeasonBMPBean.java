package com.idega.block.school.data;

import java.rmi.RemoteException;
import java.sql.Date;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class SchoolSeasonBMPBean extends GenericEntity implements SchoolSeason{

  public final static String SCHOOLSEASON = "sch_school_season";
  public final static String START = "season_start";
  public final static String END = "season_end";
  public final static String DUE_DATE = "due_date";
  public final static String NAME = "name";

  public String getEntityName() {
    return SCHOOLSEASON;
  }

  public void initializeAttributes() {
    this.addAttribute(getIDColumnName());
    this.addAttribute(NAME,"name",true,true,String.class);
    this.addAttribute(START,"Start",true,true,Date.class);
    this.addAttribute(END,"End",true,true,Date.class);
    this.addAttribute(DUE_DATE,"Duedate",true,true,Date.class);
  }

  public String getName(){
    return getSchoolSeasonName();
  }

  public String getSchoolSeasonName(){
    return this.getStringColumnValue(NAME);
  }

  public void setSchoolSeasonName(String name){
    this.setColumn(NAME,name);
  }


  public Date getSchoolSeasonEnd(){
    return (Date) this.getColumnValue(END);
  }

  public void setSchoolSeasonEnd(java.util.Date end){
    this.setColumn(END,end);
  }

  public Date getSchoolSeasonStart(){
    return (Date) this.getColumnValue(START);
  }

  public void setSchoolSeasonStart(java.util.Date start){
    this.setColumn(START,start);
  }

  public Date getSchoolSeasonDueDate(){
    return (Date) this.getColumnValue(DUE_DATE);
  }

  public void setSchoolSeasonDueDate(java.util.Date due){
    this.setColumn(DUE_DATE,due);
  }

  public java.util.Collection ejbFindAllSchoolSeasons()throws FinderException{
    return super.idoFindAllIDsBySQL();
  }

  public java.util.Collection ejbFindAllPreviousSchoolSeasons(SchoolSeason schoolSeason)throws FinderException, RemoteException {
    IDOQuery sql = idoQuery();
    sql.appendSelectAllFrom(this.getEntityName()).appendWhere().append(START).append("<").appendWithinSingleQuotes(schoolSeason.getSchoolSeasonStart().toString()).appendOrderBy(START);
    return super.idoFindPKsBySQL(sql.toString());
  }


}