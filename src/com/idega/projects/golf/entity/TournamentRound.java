//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/


/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
package com.idega.projects.golf.entity;

import java.sql.*;
import com.idega.data.*;

public class TournamentRound extends GolfEntity{

	public TournamentRound(){
		super();
	}

	public TournamentRound(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){

		addAttribute(getIDColumnName());
		addAttribute("tournament_id","Mót",true,true,"java.lang.Integer","one-to-one","com.idega.projects.golf.entity.Tournament");
		addAttribute("round_number","Hringur númer",true,true,"java.lang.Integer");
		addAttribute("round_date","Dagsetning hrings",true,true,"java.sql.Timestamp");
		addAttribute("increase_handicap","Spilað til hækkunar",true,true,"java.lang.Boolean");
		addAttribute("decrease_handicap","Spilað til lækkunar",true,true,"java.lang.Boolean");
                addAttribute("round_end_date","Hring lýkur",true,true,"java.sql.Timestamp");

	}

	public String getEntityName(){
		return "tournament_round";
	}

	public String getName(){
		return "Hringur "+getRoundNumber();
	}

	public void setTournament(Tournament tournament){
		setColumn("tournament_id",tournament);
	}

	public Tournament getTournament(){
		return (Tournament) getColumnValue("tournament_id");
	}

	public void setTournamentID(int id){
		setColumn("tournament_id",id);
	}

	public int getTournamentID(){
		return getIntColumnValue("tournament_id");
	}

	public void setRoundNumber(int number){
		setColumn("round_number",number);
	}

	public int getRoundNumber(){
		return getIntColumnValue("round_number");
	}

	public void setRoundDate(Timestamp date){
		setColumn("round_date",date);
	}

	public Timestamp getRoundDate(){
		return (Timestamp) getColumnValue("round_date");
	}

        public void setRoundEndDate(Timestamp endDate) {
            setColumn("round_end_date",endDate);
        }

        public Timestamp getRoundEndDate() {
            return (Timestamp) getColumnValue("round_end_date");
        }

        public boolean getIncreaseHandicap(){
		return getBooleanColumnValue("increase_handicap");
	}

	public void setIncreaseHandicap(boolean increase_handicap){
		setColumn("increase_handicap",new Boolean(increase_handicap));
	}

        public boolean getDecreaseHandicap(){
		return getBooleanColumnValue("decrease_handicap");
	}

	public void setDecreaseHandicap(boolean decrease_handicap){
		setColumn("decrease_handicap",new Boolean(decrease_handicap));
	}

}
