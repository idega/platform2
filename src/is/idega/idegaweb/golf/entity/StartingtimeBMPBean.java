
/**
 * Title:        Golf<p>
 * Description:  <p>
 * Copyright:    Copyright (c) idega 2000 - idega team - gummi<p>
 * Company:      idega margmiðlun<p>
 * @author idega 2000 - idega team
 * @version 1.0
 */
package is.idega.idegaweb.golf.entity;


//import java.util.*;
import java.sql.Date;
import java.sql.SQLException;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;

public class StartingtimeBMPBean extends GenericEntity implements Startingtime{

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute("field_id","Numer vallar",true,true,"java.lang.Integer");
    addAttribute("member_id","Numer meðlims",true,true,"java.lang.Integer");
    addAttribute("startingtime_date","Dagsetning",true,true,"java.sql.Date");
    addAttribute("player_name","Nafn",true,true,"java.lang.String");
    addAttribute("handicap","Forgjöf",true,true,"java.lang.Float");
    addAttribute("club_name","Nafn klubbs",true,true,"java.lang.String");
    addAttribute("card_name","Nafn korts",true,true,"java.lang.String");
    addAttribute("card_num","Kortanumer",true,true,"java.lang.String");
    addAttribute("grup_num","Hopnumer",true,true,"java.lang.Integer");
    addAttribute("owner_id","Numer eiganda",true,true,"java.lang.Integer");
    // added 19.06.2001 by Gimmi
    addAttribute("tee_number","Númer teigs",true,true,"java.lang.Integer");
    addManyToManyRelationShip(TournamentRound.class,"tournament_round_startingtime");
  }

  public String getEntityName(){
    return "startingtime";
  }

  public void setDefaultValues(){
    this.setHandicap(Float.parseFloat("-1.0"));
    this.setMemberID(1);
    this.setOwnerID(1);
    this.setTeeNumber(1);
  }


  // ### get- & set-Föll ###


  public int getFieldID(){
    return getIntColumnValue("field_id");
  }

  public void setFieldID( Integer field_id){
    setColumn("field_id",field_id);
  }
  public void setFieldID( int field_id){
    setColumn("field_id",field_id);
  }

  public int getMemberID(){
    return getIntColumnValue("member_id");
  }

  public void setMemberID( Integer member_id){
    setColumn("member_id",member_id);
  }
  public void setMemberID( int member_id){
    setColumn("member_id",member_id);
  }

  public Date getStartingtimeDate(){
    return (Date)getColumnValue("startingtime_date");
  }

  public void setStartingtimeDate( Date startingtime_date ){
    setColumn("startingtime_date",startingtime_date);
  }


  public String getPlayerName(){
    return getStringColumnValue("player_name");
  }

  public void setPlayerName( String player_name){
    setColumn("player_name",player_name);
  }


  public float getHandicap(){
    return getFloatColumnValue("handicap");
  }

  public void setHandicap( Float handicap){
    setColumn("handicap",handicap);
  }
  public void setHandicap( float handicap){
    setColumn("handicap",handicap);
  }


  public String getClubName(){
    return getStringColumnValue("club_name");
  }

  public void setClubName( String club_name){
    setColumn("club_name",club_name);
  }


  public String getCardName(){
    return getStringColumnValue("card_name");
  }

  public void setCardName( String card_name){
    setColumn("card_name",card_name);
  }


  public String getCardNum(){
    return getStringColumnValue("card_num");
  }

  public void setCardNum( String card_num){
    setColumn("card_num",card_num);
  }


  public int getGroupNum(){
    return getIntColumnValue("grup_num");
  }

  public void setGroupNum( Integer group_num){
    setColumn("grup_num",group_num);
  }
  public void setGroupNum( int group_num){
    setColumn("grup_num",group_num);
  }


  public int getOwnerID(){
    return getIntColumnValue("owner_id");
  }

  public void setOwnerID( Integer group_num){
    setColumn("owner_id",group_num);
  }
  public void setOwnerID( int owner_id){
    setColumn("owner_id",owner_id);
  }


  public Member getMember()throws SQLException{
  	Member member = null;
	try{
          member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(getMemberID());
	}
	catch(FinderException e){
        }
	return member;
  }


	public void setTeeNumber(int teeNumber) {
		setColumn("tee_number",teeNumber);
	}

	public int getTeeNumber() {
		return getIntColumnValue("tee_number");
	}


}   // class Startingtime
