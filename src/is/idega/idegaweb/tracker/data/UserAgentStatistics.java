package is.idega.idegaweb.tracker.data;


public interface UserAgentStatistics extends com.idega.data.IDOLegacyEntity
{
 public java.sql.Timestamp getDate();
 public int getSessions();
 public java.lang.String getUserAgent();
 public void setModificationDate(java.sql.Timestamp p0);
 public void setSessions(int p0);
 public void setUserAgent(java.lang.String p0);
}
