package is.idega.idegaweb.tracker.data;


public interface ReferrerStatistics extends com.idega.data.IDOLegacyEntity
{
 public java.sql.Timestamp getDate();
 public java.lang.String getReferrerUrl();
 public int getSessions();
 public void setModificationDate(java.sql.Timestamp p0);
 public void setReferrerUrl(java.lang.String p0);
 public void setSessions(int p0);
}
