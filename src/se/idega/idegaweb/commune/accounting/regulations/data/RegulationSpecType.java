package se.idega.idegaweb.commune.accounting.regulations.data;


public interface RegulationSpecType extends com.idega.data.IDOEntity
{
 public java.lang.String getMainRule();
 public java.lang.String getRegSpecType();
 public java.lang.String getTextKey();
 public void initializeAttributes();
 public void setMainRule(java.lang.String p0);
 public void setRegSpecType(java.lang.String p0);
 public void setTextKey(java.lang.String p0);
}
