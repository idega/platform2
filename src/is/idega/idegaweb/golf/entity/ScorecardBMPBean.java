//idega 2000 - Ægir

package is.idega.idegaweb.golf.entity;

import java.sql.SQLException;
import java.sql.Timestamp;

import com.idega.data.GenericEntity;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.query.Column;
import com.idega.data.query.CountColumn;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;


public class ScorecardBMPBean extends GenericEntity implements Scorecard {
  private final String FOREIGN_ROUND = "foreign_round";
  private final String FOREIGN_COURSE_NAME = "foreign_name";

  private final String YES = "Y";
  private final String NO = "N";

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute("member_id","Meðlimur id",true,true,"java.lang.Integer","one-to-many","is.idega.idegaweb.golf.entity.Member");
		addAttribute("tournament_round_id","Mót id",true,true,"java.lang.Integer","one-to-many","is.idega.idegaweb.golf.entity.TournamentRound");
		addAttribute("scorecard_date","Dagsetning",true,true,"java.sql.Timestamp");
		addAttribute("total_points","Heildarstig",true,true,"java.lang.Integer");
		addAttribute("handicap_before","Forgjöf áður",true,true,"java.lang.Float");
		addAttribute("handicap_after","Forgjöf eftir",true,true,"java.lang.Float");
		addAttribute("slope", "Slope",true,true,"java.lang.Integer");
		addAttribute("course_rating","CR",true,true,"java.lang.Float");
		addAttribute("field_id","Völlur",true,true,"java.lang.Integer","one-to-many","is.idega.idegaweb.golf.entity.Field");
		addAttribute("tee_color_id","Tee litur",true,true,"java.lang.Integer");
		addAttribute("handicap_correction","Leiðrétting forgjafar",true,true,"java.lang.String");
		addAttribute("update_handicap","Uppfærsla forgjafar",true,true,"java.lang.String");
    addAttribute(FOREIGN_ROUND,"Hringur á erlendum velli",true,true,"java.lang.String");
    addAttribute(FOREIGN_COURSE_NAME,"Nafn á erlendum velli",true,true,"java.lang.String");
    setMaxLength(FOREIGN_ROUND,1);
    setMaxLength(FOREIGN_COURSE_NAME,255);
	}

	public String getEntityName() {
		return "scorecard";
	}

	public int getMemberId() {
		return getIntColumnValue("member_id");
	}

	public void setMemberId(int memberId) {
		setColumn("member_id",memberId);
	}

	public int getTournamentRoundId() {
		return getIntColumnValue("tournament_round_id");
	}

	public void setTournamentRoundId(int tournamentRoundId) {
		setColumn("tournament_round_id",tournamentRoundId);
	}

	public TournamentRound getTournamentRound() {
		return (TournamentRound)getColumnValue("tournament_round_id");
	}

	public void setTournamentRound(TournamentRound tournamentRound) {
		setColumn("tournament_round_id",tournamentRound );
	}

	public java.sql.Timestamp getScorecardDate() {
		return (Timestamp)getColumnValue("scorecard_date");
	}

	public void setScorecardDate(Timestamp scorecardDate) {
		setColumn("scorecard_date",scorecardDate);
	}

	public int getTotalPoints() {
		return getIntColumnValue("total_points");
	}

	public void setTotalPoints(int totalPoints) {
		setColumn("total_points",totalPoints);
	}

	public float getHandicapBefore() {
		return getFloatColumnValue("handicap_before");
	}

	public void setHandicapBefore(float handicapBefore) {
		setColumn("handicap_before",handicapBefore);
	}

	public float getHandicapAfter() {
		return getFloatColumnValue("handicap_after");
	}

	public void setHandicapAfter(float handicapAfter) {
		setColumn("handicap_after",handicapAfter);
	}

	public void setMember(Member member) {
		setColumn("member_id",member);
	}

	public Member getMember() {
		return (Member)getColumnValue("member_id");
	}

	public int getSlope() {
		return getIntColumnValue("slope");
	}

	public void setSlope(int slope) {
		setColumn("slope",slope);
	}

	public float getCourseRating() {
		return getFloatColumnValue("course_rating");
	}

	public void setCourseRating(float course_rating) {
		setColumn("course_rating",course_rating);
	}

	public int getFieldID() {
		return getIntColumnValue("field_id");
	}

	public void setFieldID(int field_id) {
		setColumn("field_id",field_id);
	}

	public int getTeeColorID() {
		return getIntColumnValue("tee_color_id");
	}

	public void setTeeColorID(int tee_color_id) {
		setColumn("tee_color_id",tee_color_id);
	}

  public String getHandicapCorrection() {
		return getStringColumnValue("handicap_correction");
	}

  public boolean getHandicapCorrectionBoolean(){
		String correction = getStringColumnValue("handicap_correction");
    boolean handicapCorrection = true;

    if (correction.equalsIgnoreCase("n")) {
      handicapCorrection = false;
    }

    return handicapCorrection;
	}

	public void setHandicapCorrection(String handicap_correction){
		setColumn("handicap_correction", handicap_correction);
	}

	public void setHandicapCorrection(boolean correction){
		String handicap_correction = YES;

    if (correction == false) {
      handicap_correction = NO;
    }

    setColumn("handicap_correction", handicap_correction);
	}

  public String getUpdateHandicap() {
		return getStringColumnValue("update_handicap");
	}

  public boolean getUpdateHandicapBoolean() {
    String update = getStringColumnValue("update_handicap");
    boolean updateHandicap = true;

    if (update.equalsIgnoreCase("n")) {
      updateHandicap = false;
    }

    return updateHandicap;
  }

	public void setUpdateHandicap(String update_handicap) {
		setColumn("update_handicap", update_handicap);
	}

	public void setUpdateHandicap(boolean handicap) {
    String update_handicap = YES;

    if (handicap == false) {
      update_handicap = NO;
    }

    setColumn("update_handicap",update_handicap);
	}

  public void delete() throws SQLException {
    ((Stroke) IDOLookup.instanciateEntity(Stroke.class)).deleteMultiple("scorecard_id",Integer.toString(this.getID()));
    ((Statistic) IDOLookup.instanciateEntity(Statistic.class)).deleteMultiple("scorecard_id",Integer.toString(this.getID()));

    super.delete();
  }

  public void setForeignRound(boolean foreign) {
    String isForeign = NO;

    if (foreign)
      isForeign = YES;

    setColumn(FOREIGN_ROUND,isForeign);
  }

  public boolean getForeignRound() {
    String isForeign = getStringColumnValue(FOREIGN_ROUND);
    boolean foreign = true;

    if (isForeign == null || isForeign.equalsIgnoreCase("n")) {
      foreign = false;
    }

    return foreign;
  }

  public void setForeignCourseName(String name) {
    setColumn(FOREIGN_COURSE_NAME,name);
  }

  public String getForeignCourseName() {
    return this.getStringColumnValue(FOREIGN_COURSE_NAME);
  }
  
  public int ejbHomeGetCountRoundsPlayedByMember(int member) throws IDOException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		Column column = new Column(table, "scorecard_date");
		query.addColumn(new CountColumn(table, this.getIDColumnName()));
		query.addCriteria(new MatchCriteria(table, "member_id", MatchCriteria.EQUALS, member));
		query.addCriteria(new MatchCriteria(column, true));
		return this.idoGetNumberOfRecords(query.toString());
  }
}