package se.idega.idegaweb.commune.care.data;


public interface ProviderAccountingProperties extends com.idega.data.IDOEntity
{
 public java.lang.String getBankgiro();
 public java.lang.String getDoublePosting();
 public java.lang.String getIDColumnName();
 public java.lang.String getOwnPosting();
 public boolean getPaymentByInvoice();
 public java.lang.String getPostgiro();
 public se.idega.idegaweb.commune.care.data.ProviderType getProviderType();
 public int getProviderTypeId();
 public com.idega.block.school.data.School getSchool();
 public int getSchoolId();
 public boolean getStateSubsidyGrant();
 public java.lang.String getStatisticsType();
 public void initializeAttributes();
 public void setBankgiro(java.lang.String p0);
 public void setDoublePosting(java.lang.String p0);
 public void setOwnPosting(java.lang.String p0);
 public void setPaymentByInvoice(boolean p0);
 public void setPostgiro(java.lang.String p0);
 public void setProviderTypeId(int p0);
 public void setSchoolId(int p0);
 public void setStateSubsidyGrant(boolean p0);
 public void setStatisticsType(java.lang.String p0);
}
