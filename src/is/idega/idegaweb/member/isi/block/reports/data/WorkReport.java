package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReport extends com.idega.data.IDOEntity
{
 public void addLeague(is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p0)throws com.idega.data.IDORelationshipException;
 public java.lang.Integer getAccountFileId();
 public java.lang.Integer getBoardFileId();
 public java.lang.Integer getGroupId();
 public java.lang.String getGroupName();
 public java.lang.String getGroupNumber();
 public java.lang.String getGroupShortName();
 public java.util.Collection getLeagues()throws com.idega.data.IDOException;
 public java.lang.Integer getMemberFileId();
 public java.lang.Integer getRegionalUnionGroupId();
 public java.lang.String getStatus();
 public java.lang.Integer getYearOfReport();
 public void initializeAttributes();
 public boolean isAccountPartDone();
 public boolean isBoardPartDone();
 public boolean isCreationFromDatabaseDone();
 public boolean isMembersPartDone();
 public boolean isSent();
 public void setAccountFileId(int p0);
 public void setAccountPartDone(boolean p0);
 public void setAsSent(boolean p0);
 public void setBoardFileId(int p0);
 public void setBoardPartDone(boolean p0);
 public void setCreationFromDatabaseDone(boolean p0);
 public void setGroupId(int p0);
 public void setGroupId(java.lang.Integer p0);
 public void setGroupName(java.lang.String p0);
 public void setGroupNumber(java.lang.String p0);
 public void setGroupShortName(java.lang.String p0);
 public void setMemberFileId(int p0);
 public void setMembersPartDone(boolean p0);
 public void setRegionalUnionGroupId(int p0);
 public void setRegionalUnionGroupId(java.lang.Integer p0);
 public void setStatus(java.lang.String p0);
 public void setYearOfReport(int p0);
}
