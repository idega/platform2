package se.idega.idegaweb.commune.message.data;


public interface SystemArchivationMessage extends com.idega.data.IDOEntity,se.idega.idegaweb.commune.message.data.PrintMessage,com.idega.block.process.data.Case
{
 public com.idega.core.file.data.ICFile getAttachedFile();
 public int getAttachedFileID();
 public java.lang.String getBody();
 public java.lang.String getCaseCodeDescription();
 public java.lang.String getCaseCodeKey();
 public java.lang.String getDateString();
 public com.idega.core.file.data.ICFile getMessageData();
 public int getMessageDataFileID();
 public java.lang.String getMessageType();
 public java.lang.String getPrintType();
 public java.lang.String getSenderName();
 public java.lang.String getSubject();
 public void initializeAttributes();
 public void setAttachedFile(com.idega.core.file.data.ICFile p0);
 public void setAttachedFile(int p0);
 public void setBody(java.lang.String p0);
 public void setMessageData(com.idega.core.file.data.ICFile p0);
 public void setMessageData(int p0);
 public void setMessageType(java.lang.String p0);
 public void setSubject(java.lang.String p0);
}
