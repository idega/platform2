package is.idega.idegaweb.golf.block.poll.data;


public interface PollOptionHome extends com.idega.data.IDOHome
{
 public PollOption create() throws javax.ejb.CreateException;
 public PollOption createLegacy();
 public PollOption findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public PollOption findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public PollOption findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}