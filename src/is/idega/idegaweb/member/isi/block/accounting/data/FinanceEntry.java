package is.idega.idegaweb.member.isi.block.accounting.data;


public interface FinanceEntry extends com.idega.data.IDOEntity
{
 public double getAmount();
 public is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRound getAssessmentRound();
 public int getAssessmentRoundID();
 public com.idega.user.data.Group getClub();
 public int getClubID();
 public java.sql.Timestamp getDateOfEntry();
 public com.idega.user.data.Group getGroup();
 public int getGroupID();
 public com.idega.user.data.User getUser();
 public int getUserID();
 public void setAmount(double p0);
 public void setAssessment(is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRound p0);
 public void setAssessmentRoundID(int p0);
 public void setClub(com.idega.user.data.Group p0);
 public void setClubID(int p0);
 public void setDateOfEntry(java.sql.Timestamp p0);
 public void setGroup(com.idega.user.data.Group p0);
 public void setGroupID(int p0);
 public void setUser(com.idega.user.data.User p0);
 public void setUserID(int p0);
}
