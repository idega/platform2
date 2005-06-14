//idega 2000 - Laddi

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import java.sql.SQLException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;


public class TournamentParticipantsBMPBean extends GenericEntity implements TournamentParticipants {

  public String getEntityName(){
		return "tournament_participants";
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
		addAttribute("group_name","Ráshópsnafn",true,true, "java.lang.String");
		// gimmi 10 JUNE 2003	
		addAttribute("paid", "Greitt", true, true, "java.lang.Boolean");
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
	
	public boolean getPaid() {
		return getBooleanColumnValue("paid");	
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

	public int getScorecardID(){
		return getIntColumnValue("scorecard_id");
	}

	public java.sql.Timestamp getScorecardDate(){
		return (java.sql.Timestamp) getColumnValue("scorecard_date");
	}

	public int getTournamentRoundID(){
		return getIntColumnValue("tournament_round_id");
	}

	public int getRoundNumber(){
		return getIntColumnValue("round_number");
	}

	public int getHolesPlayed(){
		return getIntColumnValue("holes_played");
	}

	public float getRoundHandicap(){
		return getFloatColumnValue("round_handicap");
	}

	public int getStrokesWithoutHandicap(){
		return getIntColumnValue("strokes_without_handicap");
	}

	public int getStrokesWithHandicap(){
		return getIntColumnValue("strokes_with_handicap");
	}

	public int getTotalPoints(){
		return getIntColumnValue("total_points");
	}

	public int getTotalPar(){
		return getIntColumnValue("total_par");
	}

	public int getDifference(){
		return getIntColumnValue("difference");
	}

        public String getGroupName() {
          return(getStringColumnValue("group_name"));
        }

        public static is.idega.idegaweb.golf.entity.TournamentParticipants getTournamentParticipants(int member_id,int tournament_id) {
            is.idega.idegaweb.golf.entity.TournamentParticipants returner = null;
            try {
                java.util.List members = com.idega.data.EntityFinder.findAllByColumnEquals((TournamentParticipants) IDOLookup.instanciateEntity(TournamentParticipants.class),"member_id",member_id+"","tournament_id",tournament_id+"");
                if (members != null) {
                    if (members.size()  > 0) returner = (is.idega.idegaweb.golf.entity.TournamentParticipants) members.get(0);
                }
            }
            catch (SQLException sq) {
                sq.printStackTrace(System.err);
            }

            return returner;
        }

        public void insert(){}
        public void update(){}
        public void delete(){}
}