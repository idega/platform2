package is.idega.idegaweb.member.isi.block.accounting.data;


public interface AssessmentRound extends com.idega.data.IDOEntity
{
 public void addTariffType(is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffType p0)throws com.idega.data.IDOAddRelationshipException;
 public com.idega.user.data.Group getClub();
 public int getClubId();
 public boolean getDeleted();
 public com.idega.user.data.Group getDivision();
 public int getDivisionId();
 public java.sql.Timestamp getEndTime();
 public com.idega.user.data.User getExecutedBy();
 public int getExecutedById();
 public java.sql.Timestamp getExecutionDate();
 public com.idega.user.data.Group getGroup();
 public int getGroupId();
 public boolean getIncludeChildren();
 public java.lang.String getName();
 public java.sql.Timestamp getPaymentDate();
 public java.sql.Timestamp getRunOnDate();
 public java.sql.Timestamp getStartTime();
 public void setClub(com.idega.user.data.Group p0);
 public void setClubId(int p0);
 public void setDeleted(boolean p0);
 public void setDivision(com.idega.user.data.Group p0);
 public void setDivisionId(int p0);
 public void setEndTime(java.sql.Timestamp p0);
 public void setExecutedBy(com.idega.user.data.User p0);
 public void setExecutedById(int p0);
 public void setExecutionDate(java.sql.Timestamp p0);
 public void setGroup(com.idega.user.data.Group p0);
 public void setGroupId(int p0);
 public void setIncludeChildren(boolean p0);
 public void setName(java.lang.String p0);
 public void setPaymentDate(java.sql.Timestamp p0);
 public void setRunOnDate(java.sql.Timestamp p0);
 public void setStartTime(java.sql.Timestamp p0);
}
