package is.idega.idegaweb.golf.entity;


public interface Payment extends is.idega.idegaweb.golf.entity.GolfEntity
{
 public int getAccountId();
 public int getCashierId();
 public java.lang.String getExtraInfo();
 public int getInstallmentNr();
 public java.sql.Timestamp getLastUpdated();
 public int getMemberId();
 public java.lang.String getName();
 public java.sql.Timestamp getPaymentDate();
 public int getPaymentTypeID();
 public int getPrice();
 public int getPriceCatalogueId();
 public int getRoundId();
 public boolean getStatus();
 public int getTotalInstallment();
 public int getUnionId();
 public void setAccountId(int p0);
 public void setAccountId(java.lang.Integer p0);
 public void setCashierId(java.lang.Integer p0);
 public void setCashierId(int p0);
 public void setExtraInfo(java.lang.String p0);
 public void setInstallmentNr(int p0);
 public void setInstallmentNr(java.lang.Integer p0);
 public void setLastUpdated(java.sql.Timestamp p0);
 public void setMemberId(java.lang.Integer p0);
 public void setMemberId(int p0);
 public void setName(java.lang.String p0);
 public void setPaymentDate(java.sql.Timestamp p0);
 public void setPaymentTypeID(int p0);
 public void setPaymentTypeID(java.lang.Integer p0);
 public void setPrice(int p0);
 public void setPrice(java.lang.Integer p0);
 public void setPriceCatalogueId(int p0);
 public void setPriceCatalogueId(java.lang.Integer p0);
 public void setRoundId(java.lang.Integer p0);
 public void setRoundId(int p0);
 public void setStatus(boolean p0);
 public void setTotalInstallment(java.lang.Integer p0);
 public void setTotalInstallment(int p0);
 public void setUnionId(java.lang.Integer p0);
 public void setUnionId(int p0);
}
