package is.idega.idegaweb.golf.entity;


public interface AccountEntry extends com.idega.data.IDOLegacyEntity
{
 public int getAccountId();
 public java.lang.String getAccountKey();
 public int getCashierId();
 public java.lang.String getEntryKey();
 public java.lang.String getInfo();
 public java.sql.Timestamp getLastUpdated();
 public java.lang.String getName();
 public java.sql.Timestamp getPaymentDate();
 public int getPrice();
 public java.lang.String getTariffKey();
 public void setAccountId(int p0);
 public void setAccountId(java.lang.Integer p0);
 public void setAccountKey(java.lang.String p0);
 public void setCashierId(int p0);
 public void setCashierId(java.lang.Integer p0);
 public void setEntryKey(java.lang.String p0);
 public void setInfo(java.lang.String p0);
 public void setLastUpdated(java.sql.Timestamp p0);
 public void setName(java.lang.String p0);
 public void setPaymentDate(java.sql.Timestamp p0);
 public void setPrice(int p0);
 public void setPrice(java.lang.Integer p0);
 public void setTariffKey(java.lang.String p0);
}
