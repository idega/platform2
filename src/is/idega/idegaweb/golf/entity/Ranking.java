package is.idega.idegaweb.golf.entity;


public interface Ranking extends is.idega.idegaweb.golf.entity.GolfEntity
{
 public java.lang.String getAbbrevation();
 public int getHole();
 public int getMemberID();
 public int getScore();
 public int getTournamentGroupID();
 public void setAbbrevation(java.lang.String p0);
 public void setHole(int p0);
 public void setMemberID(int p0);
 public void setScore(int p0);
 public void setTournamentGroupID(int p0);
}
