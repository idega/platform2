//idega 2000 - Laddi

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import java.sql.SQLException;

import com.idega.data.EntityFinder;
import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;


public class TournamentScorecardBMPBean extends GenericEntity implements TournamentScorecard{

  public String getEntityName(){
		return "tournament_participants";
	}

	public void initializeAttributes(){
		addAttribute("scorecard_id","Skorkort",true,true,"java.lang.Integer");
		addAttribute("member_id","númer meðlims",true,true,"java.lang.Integer");
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
	}

	public int getScorecardID(){
		return getIntColumnValue("scorecard_id");
	}

	public int getMemberID() {
		return getIntColumnValue("member_id");
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

        public static TournamentScorecard getScorecard(int tournament_round_id,int member_id) {
            TournamentScorecard scorecard = null;
            try {
                java.util.List scoreCards = EntityFinder.findAllByColumn((TournamentScorecard) IDOLookup.instanciateEntity(TournamentScorecard.class),"tournament_round_id",tournament_round_id+"","member_id",member_id+"");
                if (scoreCards != null) {
                    if (scoreCards.size()  > 0) scorecard = (TournamentScorecard) scoreCards.get(0);
                }
            }
            catch (SQLException sq) {
                sq.printStackTrace(System.err);
            }

            return scorecard;
        }

        public void insert(){}
        public void update(){}
        public void delete(){}
}