package is.idega.idegaweb.golf.block.poll.data;


public interface PollAttributesHome extends com.idega.data.IDOHome
{
 public PollAttributes create() throws javax.ejb.CreateException;
 public PollAttributes createLegacy();
 public PollAttributes findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public PollAttributes findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public PollAttributes findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}