package is.idega.idegaweb.tracker.data;


public interface PageStatisticsHome extends com.idega.data.IDOHome
{
 public PageStatistics create() throws javax.ejb.CreateException;
 public PageStatistics createLegacy();
 public PageStatistics findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public PageStatistics findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public PageStatistics findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}