
/**
 * Title:        Golf<p>
 * Description:  <p>
 * Copyright:    Copyright (c) idega 2000 - idega team - gummi<p>
 * Company:      idega margmiðlun<p>
 * @author idega 2000 - idega team - gummi
 * @version 1.0
 */
package is.idega.idegaweb.golf.entity;

import java.sql.*;

public class StartingtimeFieldConfig extends GolfEntity {

  public StartingtimeFieldConfig() {
  }

  public StartingtimeFieldConfig(int id) throws SQLException{
    super(id);
  }

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute("field_id","Numer vallar",true,true,"java.lang.Integer");
    addAttribute("begin_date","Tekur gildi",true,true,"java.sql.Timestamp");
    addAttribute("open_time","Opnar",true,true,"java.sql.Timestamp");
    addAttribute("close_time","Lokar",true,true,"java.sql.Timestamp");
    addAttribute("minutes_between_start","Bil milli holla",true,true,"java.lang.Integer");
    addAttribute("days_shown","Dagar i skraningu",true,true,"java.lang.Integer");
    addAttribute("days_kept","Hve lengi geymt",false,false,"java.lang.Integer");
    addAttribute("tournament_id","Mót",false,false,"java.lang.Integer");
    addAttribute("end_date","Hætti að gilda",true,true,"java.sql.Timestamp");
    addAttribute(getPublicRegistrationColumnName(),"skráning á neti",false,false,"java.lang.Boolean");
  }

  public String getEntityName(){
    return "startingtime_field_config";
  }


  public void setDefultValues(){
    setDaysShown( new Integer(0) );
  }



  public static String getPublicRegistrationColumnName(){
    return "public_registration";
  }



  // ### get- & set-Föll ###



  public int getTournamentID() {
      return getIntColumnValue("tournament_id");
  }

  public void setTournamentID(int tournament_id) {
      setColumn("tournament_id",tournament_id);
  }


  public int getFieldID(){
    return getIntColumnValue("field_id");
  }

  public void setFieldID( Integer field_id){
    setColumn("field_id",field_id);
  }


  public Timestamp getBeginDate(){
    return (Timestamp)getColumnValue("begin_date");
  }

  public void setBeginDate( Timestamp begin_date ){
    setColumn("begin_date",begin_date);
  }
  public Timestamp getEndDate(){
    return (Timestamp)getColumnValue("end_date");
  }

  public void setEndDate( Timestamp end_date ){
    setColumn("end_date",end_date);
  }


  public Timestamp getOpenTime(){
    return (Timestamp)getColumnValue("open_time");
  }

  public void setOpenTime( Timestamp open_time ){
    setColumn("open_time",open_time);
  }


  public Timestamp getCloseTime(){
    return (Timestamp)getColumnValue("close_time");
  }

  public void setCloseTime( Timestamp close_time ){
    setColumn("close_time",close_time);
  }


  public int getMinutesBetweenStart(){
    return getIntColumnValue("minutes_between_start");
  }

  public void setMinutesBetweenStart( Integer minutes_between_start){
    setColumn("minutes_between_start",minutes_between_start);
  }


  public int getDaysShown(){
    return getIntColumnValue("days_shown");
  }

  public void setDaysShown( Integer days_shown){
    setColumn("days_shown",days_shown);
  }


  public int getDaysKept(){
    return getIntColumnValue("days_kept");
  }

  public void setDaysKept( Integer days_kept){
    setColumn("days_kept",days_kept);
  }



  public boolean publicRegistration(){
    return getBooleanColumnValue(getPublicRegistrationColumnName());
  }

  public void setPublicRegistration(boolean value){
    setColumn(getPublicRegistrationColumnName(),value);
  }




}   // class  StartingtimeFieldConfig
