package is.idega.idegaweb.golf.entity;


public interface TournamentType extends is.idega.idegaweb.golf.entity.GolfEntity
{
 public float getModifier();
 public java.lang.String getName();
 public java.lang.String getTournamentType();
 public java.util.List getVisibleTournamentTypes();
 public boolean getWithHandicap();
 public boolean getWithoutHandicap();
 public void setModifier(float p0);
 public void setName(java.lang.String p0);
 public void setTournamentType(java.lang.String p0);
 public void setWithHandicap(boolean p0);
 public void setWithoutHandicap(boolean p0);
}
