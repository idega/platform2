//idega 2000 - Laddi

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import java.sql.*;


public class MembersInTournamentBMPBean extends is.idega.idegaweb.golf.entity.GolfEntityBMPBean implements is.idega.idegaweb.golf.entity.MembersInTournament {

	public MembersInTournamentBMPBean(){
		super();
	}

        public String getEntityName(){
		return "display_scores";
	}

	public void initializeAttributes(){
		addAttribute("member_id","númer meðlims",true,true,"java.lang.Integer");
		addAttribute("social_security_number","Kennitala",true,true,"java.lang.String");
		addAttribute("first_name","Fornafn",true,true,"java.lang.String");
		addAttribute("middle_name","Miðnafn",true,true,"java.lang.String");
		addAttribute("last_name","Eftirnafn",true,true,"java.lang.String");
		addAttribute("abbrevation", "Skammstöfun", true, true, "java.lang.String");
		addAttribute("tournament_id","Mót",true,true,"java.lang.Integer");
		addAttribute("round_handicap","Leikforgjöf",true,true,"java.lang.Float");
	}

	public int getMemberID() {
		return getIntColumnValue("member_id");
	}

	public String getSocialSecurityNumber(){
		return (String) getColumnValue("social_security_number");
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

	public float getRoundHandicap(){
		return getFloatColumnValue("round_handicap");
	}


        public void insert(){}
        public void update(){}
        public void delete(){}
}
