package se.idega.idegaweb.commune.accounting.regulations.data;


public interface KeyMapping extends com.idega.data.IDOEntity,com.idega.data.IDOLegacyEntity
{
 public java.lang.String getCategory();
 public java.lang.String getKey();
 public int getValue();
 public void initializeAttributes();
 public void set(java.lang.String p0,java.lang.String p1,int p2);
 public void setCategory(java.lang.String p0);
 public void setKey(java.lang.String p0);
 public void setValue(int p0);
}
