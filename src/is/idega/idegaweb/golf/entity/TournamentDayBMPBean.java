//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package is.idega.idegaweb.golf.entity;

import java.sql.Date;

import com.idega.data.GenericEntity;


/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.0
*/
public class TournamentDayBMPBean extends GenericEntity implements TournamentDay{

	public void initializeAttributes(){

	//par1: column name, par2: visible column name, par3-par4: editable/showable, par5 ...
		addAttribute(getIDColumnName());
		addAttribute("tournament_id","Mót",true,true,"java.lang.Integer","many-to-one","is.idega.idegaweb.golf.entity.Tournament");
		addAttribute("day_date","Dagsetning móts",true,true,"java.sql.Date");
		//addAttribute("startingtime_begin","Rástimar byrja",true,true,"java.sql.Time");
		//addAttribute("startingtime_end","Rástimar enda",true,true,"java.sql.Time");
	}


	public String getEntityName(){
		return "tournament_day";
	}


   public String getName(){
      return getDate().toString();
   }

	public Tournament getTournament(){
		return (Tournament)getColumnValue("tournament_id");
	}

	public void setTournament(Tournament tournament){
		setColumn("tournament_id",tournament);
	}


	public Date getDate(){
		return (Date)getColumnValue("day_date");
	}


	public void setDate(Date date){
		setColumn("day_date",date);
	}


/*	public Time getStartingtimeBegin(){
		return (Time)getColumnValue("startingtime_begin");
	}


	public void setStartingtimeBegin(Time time){
		setColumn("startingtime_begin",time);
	}

	public Time getStartingtimeEnd(){
		return (Time)getColumnValue("startingtime_begin");
	}

	public void setStartingtimeEnd(Time time){
		setColumn("startingtime_end",time);
	}*/
}
