package is.idega.idegaweb.golf.entity;


public interface Scorecard extends com.idega.data.IDOLegacyEntity
{
 public float getCourseRating();
 public int getFieldID();
 public java.lang.String getForeignCourseName();
 public boolean getForeignRound();
 public float getHandicapAfter();
 public float getHandicapBefore();
 public java.lang.String getHandicapCorrection();
 public boolean getHandicapCorrectionBoolean();
 public is.idega.idegaweb.golf.entity.Member getMember();
 public int getMemberId();
 public java.sql.Timestamp getScorecardDate();
 public int getSlope();
 public int getTeeColorID();
 public int getTotalPoints();
 public is.idega.idegaweb.golf.entity.TournamentRound getTournamentRound();
 public int getTournamentRoundId();
 public java.lang.String getUpdateHandicap();
 public boolean getUpdateHandicapBoolean();
 public void setCourseRating(float p0);
 public void setFieldID(int p0);
 public void setForeignCourseName(java.lang.String p0);
 public void setForeignRound(boolean p0);
 public void setHandicapAfter(float p0);
 public void setHandicapBefore(float p0);
 public void setHandicapCorrection(java.lang.String p0);
 public void setHandicapCorrection(boolean p0);
 public void setMember(is.idega.idegaweb.golf.entity.Member p0);
 public void setMemberId(int p0);
 public void setScorecardDate(java.sql.Timestamp p0);
 public void setSlope(int p0);
 public void setTeeColorID(int p0);
 public void setTotalPoints(int p0);
 public void setTournamentRound(is.idega.idegaweb.golf.entity.TournamentRound p0);
 public void setTournamentRoundId(int p0);
 public void setUpdateHandicap(java.lang.String p0);
 public void setUpdateHandicap(boolean p0);
}
