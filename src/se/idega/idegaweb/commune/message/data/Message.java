package se.idega.idegaweb.commune.message.data;

import com.idega.user.data.User;

public interface Message extends com.idega.data.IDOEntity,com.idega.block.process.data.Case
{
 public void setBody(java.lang.String p0);
 public void setSubject(java.lang.String p0);
 public java.lang.String getSubject();
 public java.lang.String getBody();
 public void setSenderID(int userID);
 public void setSender(User sender);
 public int getSenderID();
 public User getSender();
}
