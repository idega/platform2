package is.idega.idegaweb.golf.entity;


public interface Account extends is.idega.idegaweb.golf.entity.GolfEntity
{
 public void addDebet(int p0);
 public void addDebet(java.lang.Integer p0);
 public void addKredit(java.lang.Integer p0);
 public void addKredit(int p0);
 public void addToBalance(java.lang.Integer p0);
 public void addToBalance(int p0);
 public int getAccountYear();
 public int getBalance();
 public int getCashierId();
 public java.sql.Timestamp getCreationDate();
 public java.lang.String getExtraInfo();
 public java.sql.Timestamp getLastUpdated();
 public int getMemberId();
 public java.lang.String getName();
 public int getUnionId();
 public boolean getValid();
 public void setAccountYear(int p0);
 public void setBalance(java.lang.Integer p0);
 public void setBalance(int p0);
 public void setCashierId(java.lang.Integer p0);
 public void setCashierId(int p0);
 public void setCreationDate(java.sql.Timestamp p0);
 public void setExtraInfo(java.lang.String p0);
 public void setLastUpdated(java.sql.Timestamp p0);
 public void setMemberId(java.lang.Integer p0);
 public void setMemberId(int p0);
 public void setName(java.lang.String p0);
 public void setUnionId(java.lang.Integer p0);
 public void setUnionId(int p0);
 public void setValid(boolean p0);
}
