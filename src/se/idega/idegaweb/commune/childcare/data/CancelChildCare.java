package se.idega.idegaweb.commune.childcare.data;

import javax.ejb.*;

public interface CancelChildCare extends com.idega.data.IDOEntity,com.idega.block.process.data.Case
{
 public java.lang.String getReason();
 public void setCheckId(int p0);
 public java.lang.String getCaseCodeDescription();
 public void setReason(java.lang.String p0);
 public int getCheckId();
 public java.sql.Date getCancellationDate();
 public void initializeAttributes();
 public void setCheck(se.idega.idegaweb.commune.childcare.check.data.Check p0);
 public se.idega.idegaweb.commune.childcare.check.data.GrantedCheck getCheck();
 public void setCancellationDate(java.sql.Date p0);
 public java.lang.String getCaseCodeKey();
}
