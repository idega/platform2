package is.idega.idegaweb.tracker.data;


public interface UserAgentStatisticsHome extends com.idega.data.IDOHome
{
 public UserAgentStatistics create() throws javax.ejb.CreateException;
 public UserAgentStatistics createLegacy();
 public UserAgentStatistics findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public UserAgentStatistics findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public UserAgentStatistics findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}