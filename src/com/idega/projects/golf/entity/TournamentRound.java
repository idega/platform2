/*
 * $Id: TournamentRound.java,v 1.5 2001/05/30 12:47:26 gimmi Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.projects.golf.entity;

import java.sql.*;
import com.idega.data.*;

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
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

  public void delete() {
    try {
      Scorecard[] scorecards = (Scorecard[]) this.findAllByColumn("TOURNAMENT_ROUND_ID",this.getID());
      if (scorecards != null) {
        for (int j = 0; j < scorecards.length; j++) {
          scorecards[j].delete();
        }
      }
    }
    catch (java.sql.SQLException e) {
      e.printStackTrace();
    }

    try {
        super.delete();
    }
    catch (java.sql.SQLException e) {
      e.printStackTrace();
    }
  }

}
