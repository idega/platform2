package is.idega.idegaweb.campus.block.mailinglist.data;


public interface MailingListHome extends com.idega.data.IDOHome
{
 public MailingList create() throws javax.ejb.CreateException;
 public MailingList createLegacy();
 public MailingList findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public MailingList findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public MailingList findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}