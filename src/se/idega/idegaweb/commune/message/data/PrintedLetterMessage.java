package se.idega.idegaweb.commune.message.data;


public interface PrintedLetterMessage extends com.idega.data.IDOEntity,se.idega.idegaweb.commune.message.data.PrintMessage,com.idega.block.process.data.Case
{
 public java.lang.String getBody();
 public java.lang.String getCaseCodeDescription();
 public java.lang.String getCaseCodeKey();
 public java.lang.String getDateString();
 public java.lang.String getLetterType();
 public com.idega.core.file.data.ICFile getMessageBulkData();
 public int getMessageBulkDataFileID();
 public com.idega.core.file.data.ICFile getMessageData();
 public int getMessageDataFileID();
 public java.lang.String getMessageType();
 public java.lang.String getPrintType();
 public java.lang.String getPrintedCaseStatusForType(java.lang.String p0);
 public java.lang.String getSenderName();
 public java.lang.String getSubject();
 public java.lang.String getUnPrintedCaseStatusForType(java.lang.String p0);
 public void initializeAttributes();
 public void setAsPasswordLetter();
 public void setBody(java.lang.String p0);
 public void setLetterType(java.lang.String p0);
 public void setMessageBulkData(com.idega.core.file.data.ICFile p0);
 public void setMessageBulkData(int p0);
 public void setMessageData(com.idega.core.file.data.ICFile p0);
 public void setMessageData(int p0);
 public void setMessageType(java.lang.String p0);
 public void setSubject(java.lang.String p0);
}
