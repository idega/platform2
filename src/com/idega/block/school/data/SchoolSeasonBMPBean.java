package com.idega.block.school.data;

import com.idega.data.*;
import java.sql.Date;

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

  public String getEntityName() {
    return SCHOOLSEASON;
  }

  public void initializeAttributes() {
    this.addAttribute(getIDColumnName());
    this.addAttribute(START,"Start",true,true,Date.class);
    this.addAttribute(END,"End",true,true,Date.class);
  }

  public Date getSchoolSeasonEnd(){
    return (Date) this.getColumnValue(END);
  }

  public void setSchoolSeasonEnd(Date end){
    this.setColumn(END,end);
  }

  public Date getSchoolSeasonStart(){
    return (Date) this.getColumnValue(START);
  }

  public void setSchoolSeasonStart(Date start){
    this.setColumn(START,start);
  }


}