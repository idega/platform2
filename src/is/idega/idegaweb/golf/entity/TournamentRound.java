package is.idega.idegaweb.golf.entity;


public interface TournamentRound extends is.idega.idegaweb.golf.entity.GolfEntity
{
 public void delete()throws java.sql.SQLException;
 public boolean getDecreaseHandicap();
 public boolean getIncreaseHandicap();
 public java.lang.String getName(com.idega.idegaweb.IWResourceBundle p0);
 public java.lang.String getName();
 public java.sql.Timestamp getRoundDate();
 public java.sql.Timestamp getRoundEndDate();
 public int getRoundNumber();
 public int getStartingtees();
 public is.idega.idegaweb.golf.entity.Tournament getTournament();
 public int getTournamentID();
 public boolean getVisibleStartingtimes();
 public void setDecreaseHandicap(boolean p0);
 public void setDefaultValues();
 public void setIncreaseHandicap(boolean p0);
 public void setRoundDate(java.sql.Timestamp p0);
 public void setRoundEndDate(java.sql.Timestamp p0);
 public void setRoundNumber(int p0);
 public void setStartingtees(int p0);
 public void setTournament(is.idega.idegaweb.golf.entity.Tournament p0);
 public void setTournamentID(int p0);
 public void setVisibleStartingtimes(boolean p0);
}
