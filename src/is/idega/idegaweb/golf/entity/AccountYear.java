package is.idega.idegaweb.golf.entity;


public interface AccountYear extends is.idega.idegaweb.golf.entity.GolfEntity
{
 public boolean getActive();
 public java.sql.Timestamp getCreationDate();
 public java.sql.Timestamp getFrom();
 public java.lang.String getInfo();
 public int getMainYear();
 public java.lang.String getName();
 public java.sql.Timestamp getTo();
 public int getUnionId();
 public void setActive(boolean p0);
 public void setCreationDate(java.sql.Timestamp p0);
 public void setFrom(java.sql.Timestamp p0);
 public void setInfo(java.lang.String p0);
 public void setMainYear(java.lang.Integer p0);
 public void setMainYear(int p0);
 public void setName(java.lang.String p0);
 public void setTo(java.sql.Timestamp p0);
 public void setUnionId(int p0);
 public void setUnionId(java.lang.Integer p0);
}
