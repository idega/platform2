package is.idega.idegaweb.golf.entity;


public interface CurrentPosition extends is.idega.idegaweb.golf.entity.GolfEntity
{
 public int getHole();
 public int getMemberID();
 public int getScore();
 public int getTournamentRoundID();
 public void setHole(int p0);
 public void setMemberID(int p0);
 public void setScore(int p0);
 public void setTournamentRoundID(int p0);
}
