package is.idega.idegaweb.member.isi.block.accounting.data;


public interface AssessmentRound extends com.idega.data.IDOEntity
{
 public com.idega.user.data.Group getClub();
 public int getClubId();
 public com.idega.user.data.User getExecutedBy();
 public int getExecutedById();
 public java.sql.Timestamp getExecutionDate();
 public int getGroupId();
 public java.lang.String getName();
 public void initializeAttributes();
 public void setClub(com.idega.user.data.Group p0);
 public void setClubId(int p0);
 public void setExecutedBy(com.idega.user.data.User p0);
 public void setExecutedById(int p0);
 public void setExecutionDate(java.sql.Timestamp p0);
 public void setGroup(com.idega.user.data.Group p0);
 public com.idega.user.data.Group setGroup();
 public void setGroupId(int p0);
 public void setName(java.lang.String p0);
}
