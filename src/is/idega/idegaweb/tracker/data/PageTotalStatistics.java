package is.idega.idegaweb.tracker.data;


public interface PageTotalStatistics extends com.idega.data.IDOLegacyEntity
{
 public java.sql.Timestamp getDate();
 public int getHits();
 public int getLocale();
 public int getPageId();
 public int getSessions();
 public int getUserId();
 public void setGenerationTime(int p0);
 public void setHits(int p0);
 public void setLocale(int p0);
 public void setModificationDate(java.sql.Timestamp p0);
 public void setPageId(int p0);
 public void setSessions(int p0);
 public void setUserId(int p0);
}
