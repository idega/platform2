package is.idega.idegaweb.golf.entity;


public interface TournamentGroup extends Group
{
 public java.lang.String getDescription();
 public java.lang.String getExtraInfo();
 public char getGender();
 public java.lang.String getGenderString();
 public java.lang.String getGroupType();
 public int getMaxAge();
 public float getMaxHandicap();
 public int getMinAge();
 public float getMinHandicap();
 public java.lang.String getName();
 public int getRegistrationFee(is.idega.idegaweb.golf.entity.Tournament p0);
 public int getRegistrationFee(int p0);
 public is.idega.idegaweb.golf.entity.TeeColor getTeeColor();
 public int getTeeColorID();
 public java.util.List getTournamentGroupsForUnion(int p0)throws java.lang.Exception;
 public java.util.List getTournamentGroupsForUnion(is.idega.idegaweb.golf.entity.Union p0)throws java.lang.Exception;
 public is.idega.idegaweb.golf.entity.Union getUnion();
 public int getUnionID();
 public void setDefaultValues();
 public void setDescription(java.lang.String p0);
 public void setExtraInfo(java.lang.String p0);
 public void setGender(java.lang.String p0);
 public void setGroupType(java.lang.String p0);
 public void setMaxAge(int p0);
 public void setMaxHandicap(float p0);
 public void setMinAge(int p0);
 public void setMinHandicap(float p0);
 public void setName(java.lang.String p0);
 public void setTeeColor(is.idega.idegaweb.golf.entity.TeeColor p0);
 public void setTeeColor(int p0);
 public void setUnion(is.idega.idegaweb.golf.entity.Union p0);
 public void setUnionID(int p0);
}
