package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReport extends com.idega.data.IDOEntity
{
 public java.lang.Integer getClubId();
 public java.lang.String getClubName();
 public java.lang.String getClubNumber();
 public java.lang.String getClubShortName();
 public java.lang.Integer getYearOfReport();
 public void initializeAttributes();
 public boolean isAccountPartDone();
 public boolean isBoardPartDone();
 public boolean isMembersPartDone();
 public void setAccountPartDone(boolean p0);
 public void setBordPartDone(boolean p0);
 public void setClubId(int p0);
 public void setClubId(java.lang.Integer p0);
 public void setClubName(java.lang.String p0);
 public void setClubNumber(java.lang.String p0);
 public void setClubShortName(java.lang.String p0);
 public void setMembersPartDone(boolean p0);
 public void setYearOfReport(int p0);
}
