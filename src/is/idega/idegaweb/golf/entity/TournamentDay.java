package is.idega.idegaweb.golf.entity;


public interface TournamentDay extends is.idega.idegaweb.golf.entity.GolfEntity
{
 public java.sql.Date getDate();
 public java.lang.String getName();
 public is.idega.idegaweb.golf.entity.Tournament getTournament();
 public void setDate(java.sql.Date p0);
 public void setTournament(is.idega.idegaweb.golf.entity.Tournament p0);
}
