package is.idega.idegaweb.tracker.data;


public interface DomainStatisticsHome extends com.idega.data.IDOHome
{
 public DomainStatistics create() throws javax.ejb.CreateException;
 public DomainStatistics createLegacy();
 public DomainStatistics findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public DomainStatistics findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public DomainStatistics findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}