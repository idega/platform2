package is.idega.idegaweb.golf.entity;


public interface Scorecard extends com.idega.data.IDOLegacyEntity
{
	public static final String COLUMN_SCORECARD = "scorecard_id";
	public static final String COLUMN_MEMBER = "member_id";
	public static final String COLUMN_TOURNAMENT_ROUND = "tournament_round_id";
	public static final String COLUMN_SCORECARD_DATE = "scorecard_date";
	public static final String COLUMN_TOTAL_POINTS = "total_points";
	public static final String COLUMN_HANDICAP_BEFORE = "handicap_before";
	public static final String COLUMN_HANDICAP_AFTER = "handicap_after";
	public static final String COLUMN_SLOPE = "slope";
	public static final String COLUMN_COURSE_RATING = "course_rating";
	public static final String COLUMN_FIELD = "field_id";
	public static final String COLUMN_TEE_COLOR = "tee_color_id";
	public static final String COLUMN_HANDICAP_CORRECTION = "handicap_correction";
	public static final String COLUMN_UPDATE_HANDICAP = "update_handicap";
	public static final String COLUMN_FOREIGN_ROUND = "foreign_round";
	public static final String COLUMN_FOREIGN_COURSE_NAME = "foreign_name";
	public static final String TABLE_NAME = "scorecard";
	
 public float getCourseRating();
 public int getFieldID();
 public Field getField();
 public java.lang.String getForeignCourseName();
 public boolean getForeignRound();
 public float getHandicapAfter();
 public float getHandicapBefore();
 public boolean getHandicapCorrection();
 public is.idega.idegaweb.golf.entity.Member getMember();
 public int getMemberId();
 public java.sql.Timestamp getScorecardDate();
 public int getSlope();
 public int getTeeColorID();
 public int getTotalPoints();
 public is.idega.idegaweb.golf.entity.TournamentRound getTournamentRound();
 public int getTournamentRoundId();
 public boolean getUpdateHandicap();
 public void setCourseRating(float p0);
 public void setFieldID(int p0);
 public void setForeignCourseName(java.lang.String p0);
 public void setForeignRound(boolean p0);
 public void setHandicapAfter(float p0);
 public void setHandicapBefore(float p0);
 public void setHandicapCorrection(boolean p0);
 public void setMember(is.idega.idegaweb.golf.entity.Member p0);
 public void setMemberId(int p0);
 public void setScorecardDate(java.sql.Timestamp p0);
 public void setSlope(int p0);
 public void setTeeColorID(int p0);
 public void setTotalPoints(int p0);
 public void setTournamentRound(is.idega.idegaweb.golf.entity.TournamentRound p0);
 public void setTournamentRoundId(int p0);
 public void setUpdateHandicap(boolean p0);
}
