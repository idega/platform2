//idega 2000 - Ægir

package com.idega.projects.golf.entity;

//import java.util.*;
import java.sql.*;
import com.idega.data.EntityFinder;
import java.util.List;

public class Scorecard extends GolfEntity{

	public Scorecard(){
		super();
	}

	public Scorecard(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("member_id", "Meðlimur id", true, true, "java.lang.Integer","one-to-many","com.idega.projects.golf.entity.Member");
		addAttribute("tournament_round_id", "Mót id", true, true, "java.lang.Integer","one-to-many","com.idega.projects.golf.entity.TournamentRound");
		addAttribute("scorecard_date", "Dagsetning", true, true, "java.sql.Timestamp");
		addAttribute("total_points", "Heildarstig", true, true, "java.lang.Integer");
		addAttribute("handicap_before", "Forgjöf áður", true, true, "java.lang.Float");
		addAttribute("handicap_after", "Forgjöf eftir", true, true, "java.lang.Float");
		addAttribute("slope", "Slope", true, true, "java.lang.Integer");
		addAttribute("course_rating", "CR", true, true, "java.lang.Float");
		addAttribute("field_id", "Völlur", true, true, "java.lang.Integer","one-to-many","com.idega.projects.golf.entity.Field");
		addAttribute("tee_color_id", "Tee litur", true, true, "java.lang.Integer");
		addAttribute("handicap_correction", "Leiðrétting forgjafar", true, true, "java.lang.String");
		addAttribute("update_handicap", "Uppfærsla forgjafar", true, true, "java.lang.String");
	}

	public String getEntityName(){
		return "scorecard";
	}

	public int getMemberId(){
		return getIntColumnValue("member_id");
	}

	public void setMemberId(int memberId){
		setColumn("member_id", memberId);
	}

	public int getTournamentRoundId(){
		return getIntColumnValue("tournament_round_id");
	}

	public void setTournamentRoundId(int tournamentRoundId){
		setColumn("tournament_round_id", tournamentRoundId);
	}

	public TournamentRound getTournamentRound(){
		return (TournamentRound) getColumnValue("tournament_round_id");
	}

	public void setTournamentRound(TournamentRound tournamentRound){
		setColumn("tournament_round_id", tournamentRound );
	}

	public java.sql.Timestamp getScorecardDate(){
		return (java.sql.Timestamp) getColumnValue("scorecard_date");
	}

	public void setScorecardDate(java.sql.Timestamp scorecardDate){
		setColumn("scorecard_date", scorecardDate);
	}

	public int getTotalPoints(){
		return getIntColumnValue("total_points");
	}

	public void setTotalPoints(int totalPoints){
		setColumn("total_points", totalPoints);
	}

	public float getHandicapBefore(){
		return getFloatColumnValue("handicap_before");
	}

	public void setHandicapBefore(float handicapBefore){
		setColumn("handicap_before", handicapBefore);
	}

	public float getHandicapAfter(){
		return getFloatColumnValue("handicap_after");
	}

	public void setHandicapAfter(float handicapAfter){
		setColumn("handicap_after", handicapAfter);
	}

	public void setMember(Member member){
		setColumn("member_id",member);
	}

	public Member getMember(){
		return (Member)getColumnValue("member_id");
	}

	public int getSlope(){
		return getIntColumnValue("slope");
	}

	public void setSlope(int slope){
		setColumn("slope", slope);
	}

	public float getCourseRating(){
		return getFloatColumnValue("course_rating");
	}

	public void setCourseRating(float course_rating){
		setColumn("course_rating", course_rating);
	}

	public int getFieldID(){
		return getIntColumnValue("field_id");
	}

	public void setFieldID(int field_id){
		setColumn("field_id", field_id);
	}

	public int getTeeColorID(){
		return getIntColumnValue("tee_color_id");
	}

	public void setTeeColorID(int tee_color_id){
		setColumn("tee_color_id", tee_color_id);
	}

        public String getHandicapCorrection(){
		return getStringColumnValue("handicap_correction");
	}

	public void setHandicapCorrection(String handicap_correction){
		setColumn("handicap_correction", handicap_correction);
	}

        public String getUpdateHandicap(){
		return getStringColumnValue("update_handicap");
	}

	public void setUpdateHandicap(String update_handicap){
		setColumn("update_handicap", update_handicap);
	}

	public void setUpdateHandicap(boolean handicap){
		String update_handicap = "Y";

                if ( handicap == true ) {
                  update_handicap = "Y";
                }
                if ( handicap == false ) {
                  update_handicap = "N";
                }

                setColumn("update_handicap", update_handicap);
	}

        public void delete() throws SQLException{
            try {
                List strokes = EntityFinder.findAll(Stroke.getStaticInstance("com.idega.projects.golf.entity.Stroke"),"Select * from stroke where scorecard_id = "+this.getID());
                //List strokes = EntityFinder.findAllByColumn(new Stroke(),"scorecard_id",this.getID());
                Stroke stroke;
                if (strokes != null) {
                    if (strokes.size() > 0) {
                        for (int i = 0; i < strokes.size(); i++) {
                            stroke = (Stroke) strokes.get(0);
                            stroke.delete();
                        }

                    }
                }
            }
            catch (SQLException sql) {
                System.err.println("Scorecard : delete : strokes");
                sql.printStackTrace(System.err);
            }

            try {
                List statistics = EntityFinder.findAll(Statistic.getStaticInstance("com.idega.projects.golf.entity.Statistic"),"Select * from statistic where scorecard_id = "+this.getID());
                //List statistics = EntityFinder.findAllByColumn(new Statistic(),"scorecard_id",this.getID());
                Statistic stat;
                if (statistics != null) {
                    if (statistics.size() > 0) {
                        for (int i = 0; i < statistics.size(); i++) {
                            stat = (Statistic) statistics.get(0);
                            stat.delete();
                        }

                    }
                }

            }
            catch (SQLException sql) {
                System.err.println("Scorecard : delete : strokes");
                sql.printStackTrace(System.err);
            }

            super.delete();
        }

}
