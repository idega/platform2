package is.idega.idegaweb.tracker.data;


public interface DomainStatistics extends com.idega.data.IDOLegacyEntity
{
 public java.sql.Timestamp getDate();
 public int getDomainId();
 public int getHits();
 public int getLocale();
 public int getSessions();
 public void setDomainId(int p0);
 public void setHits(int p0);
 public void setLocale(int p0);
 public void setModificationDate(java.sql.Timestamp p0);
 public void setSessions(int p0);
}
