package is.idega.idegaweb.golf.entity;

import javax.ejb.*;

public interface Poll extends is.idega.idegaweb.golf.entity.GolfEntity
{
 public void delete()throws java.sql.SQLException;
 public is.idega.idegaweb.golf.entity.Poll_option[] findOptions()throws java.sql.SQLException;
 public java.sql.Timestamp getEndTime();
 public int getID();
 public boolean getIfInUse();
 public java.lang.String getName();
 public java.lang.String getQuestion();
 public java.sql.Timestamp getStartTime();
 public is.idega.idegaweb.golf.entity.Union getUnion();
 public int getUnionID();
 public void setDefaultValues();
 public void setInUse(boolean p0);
 public void setUnionID(int p0);
 public void setUnionID(java.lang.String p0);
}
