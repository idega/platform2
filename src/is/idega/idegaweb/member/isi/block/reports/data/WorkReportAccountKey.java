package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReportAccountKey extends com.idega.data.IDOEntity
{
 public java.lang.String getKeyName();
 public java.lang.String getKeyNumber();
 public java.lang.String getKeyType();
 public java.lang.String getParentKeyNumber();
 public void initializeAttributes();
 public boolean isCredit();
 public boolean isDebet();
 public void setAsCredit();
 public void setAsDebet();
 public void setKeyName(java.lang.String p0);
 public void setKeyNumber(java.lang.String p0);
 public void setKeyType(java.lang.String p0);
 public void setParentKeyNumber(java.lang.String p0);
}
