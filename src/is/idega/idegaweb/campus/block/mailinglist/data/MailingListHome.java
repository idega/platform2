package is.idega.idegaweb.campus.block.mailinglist.data;


public interface MailingListHome extends com.idega.data.IDOHome
{
 public MailingList create() throws javax.ejb.CreateException;
 public MailingList findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;

}