package se.idega.idegaweb.commune.message.data;


public interface MessageHome extends com.idega.data.IDOHome
{
 public Message create() throws javax.ejb.CreateException;
 public Message findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findMessages(com.idega.user.data.User p0)throws javax.ejb.FinderException;
 public java.util.Collection findMessages(com.idega.user.data.User p0, String[] status)throws javax.ejb.FinderException;
}