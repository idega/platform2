package is.idega.idegaweb.campus.data;


public interface SystemProperties extends com.idega.data.IDOLegacyEntity
{
 public void delete()throws java.sql.SQLException;
 public java.lang.String getAdminEmail();
 public java.sql.Date getContractDate();
 public int getContractYears();
 public java.lang.String getCypherKey();
 public int getDefaultGroup();
 public java.lang.String getEmailHost();
 public long getTermOfNotice();
 public long getTermOfNoticeDays();
 public java.sql.Date getValidToDate();
 public void setAdminEmail(java.lang.String p0);
 public void setContractDate(java.sql.Date p0);
 public void setContractYears(int p0);
 public void setCypherKey(java.lang.String p0);
 public void setDefaultGroup(int p0);
 public void setEmailHost(java.lang.String p0);
 public void setTermOfNotice(long p0);
 public void setTermOfNoticeMonths(long p0);
 public long getTermOfNoticeMonths();
}
