package se.idega.idegaweb.commune.accounting.regulations.data;


public interface RegulationSpecType extends com.idega.data.IDOEntity
{
 public se.idega.idegaweb.commune.accounting.regulations.data.MainRule getMainRule();
 public java.lang.String getRegSpecType();
 public java.lang.String getTextKey();
 public void initializeAttributes();
 public void setMainRule(int p0);
 public void setRegSpecType(java.lang.String p0);
 public void setTextKey(java.lang.String p0);
}
