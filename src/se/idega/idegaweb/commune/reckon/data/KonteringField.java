package se.idega.idegaweb.commune.reckon.data;


public interface KonteringField extends com.idega.data.IDOEntity
{
 public java.lang.String getFieldTitle();
 public boolean getIsMandatory();
 public int getJustification();
 public int getKonteringStringId();
 public int getLength();
 public int getOrder();
 public char getPadChar();
 public void initializeAttributes();
 public void setFieldTitle(java.lang.String p0);
 public void setIsMandatory(boolean p0);
 public void setJustification(int p0);
 public void setKonteringStringId(int p0);
 public void setLength(int p0);
 public void setOrder(int p0);
 public void setPadChar(char p0);
}
