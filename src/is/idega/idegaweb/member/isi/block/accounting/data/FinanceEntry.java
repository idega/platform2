package is.idega.idegaweb.member.isi.block.accounting.data;


public interface FinanceEntry extends com.idega.data.IDOEntity
{
 public double getAmount();
 public is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRound getAssessmentRound();
 public int getAssessmentRoundID();
 public com.idega.user.data.Group getClub();
 public int getClubID();
 public java.sql.Timestamp getDateOfEntry();
 public com.idega.user.data.Group getDivision();
 public int getDivisionID();
 public com.idega.user.data.Group getGroup();
 public int getGroupID();
 public java.lang.String getInfo();
 public is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffType getTariffType();
 public int getTariffTypeID();
 public com.idega.user.data.User getUser();
 public int getUserID();
 public void setAmount(double p0);
 public void setAssessment(is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRound p0);
 public void setAssessmentRoundID(int p0);
 public void setClub(com.idega.user.data.Group p0);
 public void setClubID(int p0);
 public void setDateOfEntry(java.sql.Timestamp p0);
 public void setDivision(com.idega.user.data.Group p0);
 public void setDivisionID(int p0);
 public void setGroup(com.idega.user.data.Group p0);
 public void setGroup(is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffType p0);
 public void setGroupID(int p0);
 public void setInfo(java.lang.String p0);
 public void setStatusCreated();
 public void setStatusReady();
 public void setStatusSent();
 public void setTariffTypeID(int p0);
 public void setTypeAssessment();
 public void setTypeManual();
 public void setTypePayment();
 public void setUser(com.idega.user.data.User p0);
 public void setUserID(int p0);
}
