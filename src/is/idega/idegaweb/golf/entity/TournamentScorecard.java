package is.idega.idegaweb.golf.entity;


public interface TournamentScorecard extends is.idega.idegaweb.golf.entity.GolfEntity
{
 public void delete();
 public int getDifference();
 public int getHolesPlayed();
 public int getMemberID();
 public float getRoundHandicap();
 public int getRoundNumber();
 public java.sql.Timestamp getScorecardDate();
 public int getScorecardID();
 public int getStrokesWithHandicap();
 public int getStrokesWithoutHandicap();
 public int getTotalPar();
 public int getTotalPoints();
 public int getTournamentRoundID();
}
