//idega 2000 - Laddi



package is.idega.idegaweb.golf.entity;



//import java.util.*;

import java.sql.*;





public class CurrentPositionBMPBean extends is.idega.idegaweb.golf.entity.GolfEntityBMPBean implements is.idega.idegaweb.golf.entity.CurrentPosition {



	public CurrentPositionBMPBean(){

		super();

	}



	public CurrentPositionBMPBean(int id)throws SQLException{

		super(id);

	}



		public String getEntityName(){

		return "current_position";

	}



	public void initializeAttributes(){

		addAttribute(getIDColumnName());

		addAttribute("member_id","númer meðlims",true,true,"java.lang.Integer");

		addAttribute("score","staða",true, true , "java.lang.Integer");

		addAttribute("hole","hola",true, true , "java.lang.Integer");

		addAttribute("tournament_round_id","hringnúmer",true,true, "java.lang.Integer");

	}



	public void setTournamentRoundID(int id) {

		setColumn("tournament_round_id",id);

	}



	public int getTournamentRoundID() {

		return getIntColumnValue("tournament_round_id");

	}





	public void setMemberID(int id) {

		setColumn("member_id",id);

	}



	public void setScore(int score) {

		setColumn("score",score);

	}



	public void setHole(int hole) {

		setColumn("hole",hole);

	}



	public int getMemberID() {

		return getIntColumnValue("member_id");

	}



	public int getScore() {

		return getIntColumnValue("score");

	}



	public int getHole() {

		return getIntColumnValue("hole");

	}









}

