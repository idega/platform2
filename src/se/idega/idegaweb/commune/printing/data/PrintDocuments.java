package se.idega.idegaweb.commune.printing.data;


public interface PrintDocuments extends com.idega.data.IDOEntity
{
 public java.sql.Timestamp getCreated();
 public com.idega.user.data.User getCreator();
 public int getCreatorUserID();
 public com.idega.core.data.ICFile getDocument();
 public int getDocumentFileID();
 public java.sql.Timestamp getLastPrinted();
 public int getNumberOfSubDocuments();
 public java.lang.String getType();
 public void initializeAttributes();
 public boolean isIfPrinted();
 public void setAsPrintedLetter();
 public void setCreated(java.sql.Timestamp p0);
 public void setCreator(com.idega.user.data.User p0);
 public void setCreator(int p0);
 public void setDocument(com.idega.core.data.ICFile p0);
 public void setDocument(int p0);
 public void setIfPrinted(boolean p0);
 public void setLastPrinted(java.sql.Timestamp p0);
 public void setNumberOfSubDocuments(int p0);
 public void setType(java.lang.String p0);
}
