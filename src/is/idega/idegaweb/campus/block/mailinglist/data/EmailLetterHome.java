package is.idega.idegaweb.campus.block.mailinglist.data;


public interface EmailLetterHome extends com.idega.data.IDOHome
{
 public EmailLetter create() throws javax.ejb.CreateException;
 public EmailLetter findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
 public java.util.Collection findByType(java.lang.String p0)throws javax.ejb.FinderException;

}