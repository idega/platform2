package is.idega.idegaweb.golf.entity;


public interface TournamentParticipants extends is.idega.idegaweb.golf.entity.GolfEntity
{
 public void delete();
 public java.lang.String getAbbrevation();
 public int getDifference();
 public java.lang.String getFirstName();
 public java.lang.String getGroupName();
 public int getHolesPlayed();
 public java.lang.String getLastName();
 public int getMemberID();
 public java.lang.String getMiddleName();
 public java.lang.String getName();
 public float getRoundHandicap();
 public int getRoundNumber();
 public java.sql.Timestamp getScorecardDate();
 public int getScorecardID();
 public java.lang.String getSocialSecurityNumber();
 public int getStrokesWithHandicap();
 public int getStrokesWithoutHandicap();
 public int getTotalPar();
 public int getTotalPoints();
 public int getTournamentGroupID();
 public int getTournamentID();
 public int getTournamentRoundID();
}
