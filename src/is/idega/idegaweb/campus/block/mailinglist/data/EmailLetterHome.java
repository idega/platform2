package is.idega.idegaweb.campus.block.mailinglist.data;


public interface EmailLetterHome extends com.idega.data.IDOHome
{
 public EmailLetter create() throws javax.ejb.CreateException;
 public EmailLetter createLegacy();
 public EmailLetter findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public EmailLetter findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public EmailLetter findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}