/*
 * $Id: TournamentRound.java,v 1.8 2001/07/03 16:28:28 laddi Exp $
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
import com.idega.idegaweb.IWResourceBundle;
import java.util.List;

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
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
		// added 19.06.2001
		addAttribute("startingtees","Fjöldi teiga",true,true,"java.lang.Integer");
		// added 28.06.2001
		addAttribute("visible_startingtimes","Sýnilegir rástimar",true,true,"java.lang.Boolean");

	}

	public String getEntityName(){
		return "tournament_round";
	}

	public void setDefaultValues() {
		setIncreaseHandicap(false);
		setDecreaseHandicap(false);
		setStartingtees(1);
                setVisibleStartingtimes(true);
	}


	public String getName(){
		return "Hringur "+getRoundNumber();
	}

	public String getName(IWResourceBundle iwrb){
		return iwrb.getLocalizedString("tournament.round","Round")+" "+getRoundNumber();
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

	/*
	public Startingtime[] getStartingtimes() throws SQLException {
		return (Startingtime[]) (Startingtime.getStaticInstance("com.idega.projects.golf.entity.Startingtime")).findRelated(this);
	}
	*/

  public void delete() throws SQLException {

    try {
      List scorecards = EntityFinder.findAllByColumn(new Scorecard(),"TOURNAMENT_ROUND_ID", this.getID());
      Scorecard scorecard = null;
      if (scorecards != null) {
        for (int j = 0; j < scorecards.size(); j++) {
          scorecard = (Scorecard) scorecards.get(j);
          scorecard.delete();
        }
      }
    }
    catch (java.sql.SQLException e) {
      e.printStackTrace();
    }

    try {
      Startingtime[] startingtimes = (Startingtime[]) (new Startingtime()).findAll("SELECT startingtime.* from startingtime s, tournament_round_startingtime trs where s.startingtime_id = trs.startingtime_id AND trs.tournament_round_id = "+this.getID());
      if (startingtimes != null) {
        for (int j = 0; j < startingtimes.length; j++) {
          startingtimes[j].removeFrom(this);
          startingtimes[j].delete();
        }
      }
    }
    catch (java.sql.SQLException e) {
      e.printStackTrace();
    }

    super.delete();
  }


    public void setStartingtees(int numberOfStartingtees) {
        setColumn("startingtees",numberOfStartingtees);
    }

    public int getStartingtees() {
        return getIntColumnValue("startingtees");
    }

    public void setVisibleStartingtimes(boolean visibleStartingtimes) {
        setColumn("visible_startingtimes",visibleStartingtimes);
    }

    public boolean getVisibleStartingtimes() {
        return getBooleanColumnValue("visible_startingtimes");
    }



}



