package se.idega.idegaweb.commune.accounting.export.data;


public interface ExportDataMapping extends com.idega.data.IDOEntity
{
 public java.lang.String getAccount();
 public int getAccountSettlementType();
 public boolean getCashFlowIn();
 public boolean getCashFlowOut();
 public java.lang.String getCounterAccount();
 public java.lang.String getCustomerClaimAccount();
 public java.lang.String getIDColumnName();
 public java.lang.String getJournalNumber();
 public com.idega.block.school.data.SchoolCategory getOperationalField();
 public java.lang.String getPayableAccount();
 public java.lang.Class getPrimaryKeyClass();
 public boolean getProviderAuthorization();
 public int getStandardPaymentDay();
 public void initializeAttributes();
 public void setAccount(java.lang.String p0);
 public void setAccountSettlementType(int p0);
 public void setCashFlowIn(boolean p0);
 public void setCashFlowOut(boolean p0);
 public void setCounterAccount(java.lang.String p0);
 public void setCustomerClaimAccount(java.lang.String p0);
 public void setJournalNumber(java.lang.String p0);
 public void setOperationalField(java.lang.String p0);
 public void setOperationalField(com.idega.block.school.data.SchoolCategory p0);
 public void setPayableAccount(java.lang.String p0);
 public void setProviderAuthorization(boolean p0);
 public void setStandardPaymentDay(int p0);
}
