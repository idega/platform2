package se.idega.idegaweb.commune.message.data;


public interface UserMessage extends com.idega.data.IDOEntity,se.idega.idegaweb.commune.message.data.Message,com.idega.block.process.data.Case
{
 public void setBody(java.lang.String p0);
 public java.lang.String getCaseCodeDescription() throws java.rmi.RemoteException;
 public void setSubject(java.lang.String p0);
 public java.lang.String getSubject();
 public java.lang.String getBody();
 public java.lang.String getCaseCodeKey();
}
