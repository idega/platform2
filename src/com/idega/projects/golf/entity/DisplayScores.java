//idega 2000 - Laddi

package com.idega.projects.golf.entity;

//import java.util.*;
import java.sql.*;


public class DisplayScores extends GolfEntity{

	public DisplayScores(){
		super();
	}

        public String getEntityName(){
		return "display_scores";
	}

	public void initializeAttributes(){
		addAttribute("member_id","númer meðlims",true,true,"java.lang.Integer");
		addAttribute("first_name","Fornafn",true,true,"java.lang.String");
		addAttribute("middle_name","Miðnafn",true,true,"java.lang.String");
		addAttribute("last_name","Eftirnafn",true,true,"java.lang.String");
		addAttribute("abbrevation", "Skammstöfun", true, true, "java.lang.String");
		addAttribute("tournament_id","Mót",true,true,"java.lang.Integer");
		addAttribute("tournament_group_id","Mótahópur",true,true,"java.lang.Integer");
		addAttribute("total_points","Heildarpunktar",true,true,"java.lang.Integer");
		addAttribute("holes_played","Spilaðar holur",true, true , "java.lang.Integer");
		addAttribute("total_strokes","Heildar högg",true, true , "java.lang.Integer");
		addAttribute("difference","par",true,true, "java.lang.Integer");
	}

	public int getMemberID() {
		return getIntColumnValue("member_id");
	}

	public String getName(){
          StringBuffer nameBuffer = new StringBuffer();
		if ((getFirstName() != null) && (getMiddleName() != null) && (getLastName() != null)){
			 nameBuffer.append(getFirstName());
                          nameBuffer.append(" ");
                           nameBuffer.append(getMiddleName());
                            nameBuffer.append(" ");
                             nameBuffer.append(getLastName());
		}
		else if ((getFirstName() != null) && (getLastName() != null)){
                   nameBuffer.append(getFirstName());
                    nameBuffer.append(" ");
                     nameBuffer.append(getLastName());
		}
		else if(getLastName() != null){
                  nameBuffer.append(getLastName());
		}
		else if (getFirstName() != null){
                  nameBuffer.append(getFirstName());
		}
		return  nameBuffer.toString();
	}

	public String getFirstName(){
		return (String) getColumnValue("first_name");
	}

	public String getMiddleName(){
		return (String) getColumnValue("middle_name");
	}

	public String getLastName(){
		return (String) getColumnValue("last_name");
	}

	public String getAbbrevation(){
		return getStringColumnValue("abbrevation");
	}

	public int getTournamentID(){
		return getIntColumnValue("tournament_id");
	}

	public int getTournamentGroupID(){
		return getIntColumnValue("tournament_group_id");
	}

	public int getTotalPoints(){
		return getIntColumnValue("total_points");
	}

	public int getHolesPlayed(){
		return getIntColumnValue("holes_played");
	}

	public int getTotalStrokes(){
		return getIntColumnValue("total_strokes");
	}

	public int getDifference(){
		return getIntColumnValue("difference");
	}

        public void insert(){}
        public void update(){}
        public void delete(){}
}
