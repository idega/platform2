package is.idega.idegaweb.golf.entity;


public interface DisplayScores extends is.idega.idegaweb.golf.entity.GolfEntity
{
 public void delete();
 public java.lang.String getAbbrevation();
 public int getDifference();
 public java.lang.String getFirstName();
 public int getHolesPlayed();
 public java.lang.String getLastName();
 public int getMemberID();
 public java.lang.String getMiddleName();
 public java.lang.String getName();
 public java.lang.String getSocialSecurityNumber();
 public int getStrokesWithHandicap();
 public int getStrokesWithoutHandicap();
 public int getTotalPoints();
 public int getTournamentGroupID();
 public float getTournamentHandicap();
 public int getTournamentID();
}
