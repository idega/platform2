package is.idega.idegaweb.member.isi.block.accounting.data;


public interface ClubTariff extends com.idega.data.IDOEntity
{
 public double getAmount();
 public com.idega.user.data.Group getClub();
 public int getClubID();
 public boolean getDeleted();
 public com.idega.user.data.Group getDivision();
 public int getDivisionID();
 public com.idega.user.data.Group getGroup();
 public int getGroupId();
 public java.sql.Date getPeriodFrom();
 public java.sql.Date getPeriodTo();
 public is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffType getTariffType();
 public int getTariffTypeId();
 public java.lang.String getText();
 public void setAmount(double p0);
 public void setClub(com.idega.user.data.Group p0);
 public void setClubID(int p0);
 public void setDeleted(boolean p0);
 public void setDivision(com.idega.user.data.Group p0);
 public void setDivisionID(int p0);
 public void setGroup(com.idega.user.data.Group p0);
 public void setGroupId(int p0);
 public void setPeriodFrom(java.sql.Date p0);
 public void setPeriodTo(java.sql.Date p0);
 public void setTariffType(is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffType p0);
 public void setTariffTypeId(int p0);
 public void setText(java.lang.String p0);
}
