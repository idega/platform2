//idega 2000 - Laddi

package com.idega.projects.golf.entity;

//import java.util.*;
import java.sql.*;


public class TournamentRoundParticipants extends TournamentParticipants{

	public TournamentRoundParticipants(){
		super();
	}

        public String getEntityName(){
		return "tournament_round_participants";
	}

	public void initializeAttributes(){
		addAttribute("member_id","númer meðlims",true,true,"java.lang.Integer");
		addAttribute("social_security_number","Kennitala",true,true,"java.lang.String");
		addAttribute("first_name","Fornafn",true,true,"java.lang.String");
		addAttribute("middle_name","Miðnafn",true,true,"java.lang.String");
		addAttribute("last_name","Eftirnafn",true,true,"java.lang.String");
		addAttribute("abbrevation", "Skammstöfun", true, true, "java.lang.String");
		addAttribute("tournament_id","Mót",true,true,"java.lang.Integer");
		addAttribute("tournament_group_id","Mótahópur",true,true,"java.lang.Integer");
		addAttribute("scorecard_id","Skorkort",true,true,"java.lang.Integer");
		addAttribute("scorecard_date", "Dagsetning", true, true, "java.sql.Timestamp");
		addAttribute("tournament_round_id","Mótahringur",true,true,"java.lang.Integer");
		addAttribute("round_number","Númer hrings",true,true,"java.lang.Integer");
		addAttribute("holes_played","Fjöldi hola",true,true,"java.lang.Integer");
		addAttribute("round_handicap","Leikforgjöf",true,true,"java.lang.Float");
		addAttribute("strokes_without_handicap","Högg án forgjafar",true, true , "java.lang.Integer");
		addAttribute("strokes_with_handicap","Högg með forgjöf",true, true , "java.lang.Integer");
		addAttribute("total_points","Heildarpunktar",true,true,"java.lang.Integer");
		addAttribute("total_par","Par vallarins",true, true , "java.lang.Integer");
		addAttribute("difference","par",true,true, "java.lang.Integer");
		addAttribute("grup_num","Ráshópur",true,true, "java.lang.Integer");
	}

	public int getGroupNumber() {
		return getIntColumnValue("grup_num");
	}

}