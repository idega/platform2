package se.idega.idegaweb.commune.accounting.posting.data;


public interface PostingField extends com.idega.data.IDOEntity
{
 public java.lang.String getFieldTitle();
 public boolean getIsMandatory();
 public int getJustification();
 public int getLen();
 public int getOrderNr();
 public char getPadChar();
 public int getPostingStringId();
 public void initializeAttributes();
 public void setFieldTitle(java.lang.String p0);
 public void setIsMandatory(boolean p0);
 public void setJustification(int p0);
 public void setLen(int p0);
 public void setOrderNr(int p0);
 public void setPadChar(char p0);
 public void setPostingStringId(int p0);
}
