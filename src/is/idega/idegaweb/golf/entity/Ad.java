package is.idega.idegaweb.golf.entity;


public interface Ad extends is.idega.idegaweb.golf.entity.GolfEntity
{
 public void delete()throws java.sql.SQLException;
 public java.lang.String getBannerName();
 public java.sql.Date getBeginDate();
 public java.sql.Date getEndDate();
 public int getHits();
 public int getImageID();
 public int getImageID(boolean p0);
 public int getImpressions();
 public int getMaxHits();
 public int getMaxImpressions();
 public java.lang.String getUrl();
 public void setBannerName(java.lang.String p0);
 public void setBeginDate(java.sql.Date p0);
 public void setEndDate(java.sql.Date p0);
 public void setHits(int p0);
 public void setImageID(int p0);
 public void setImpressions(int p0);
 public void setMaxHits(int p0);
 public void setMaxImpressions(int p0);
 public void setUrl(java.lang.String p0);
}
