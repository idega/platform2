package se.idega.idegaweb.commune.accounting.regulations.data;


public interface RegulationSpecType extends com.idega.data.IDOEntity
{
 public java.lang.String getLocalizationKey();
 public se.idega.idegaweb.commune.accounting.regulations.data.MainRule getMainRule();
 public java.lang.String getRegSpecType();
 public void initializeAttributes();
 public void setLocalizationKey(java.lang.String p0);
 public void setMainRule(int p0);
 public void setRegSpecType(java.lang.String p0);
}
