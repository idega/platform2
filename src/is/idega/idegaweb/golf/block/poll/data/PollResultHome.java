package is.idega.idegaweb.golf.block.poll.data;


public interface PollResultHome extends com.idega.data.IDOHome
{
 public PollResult create() throws javax.ejb.CreateException;
 public PollResult createLegacy();
 public PollResult findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public PollResult findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public PollResult findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}