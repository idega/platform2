package is.idega.idegaweb.tracker.data;


public interface PageTotalStatisticsHome extends com.idega.data.IDOHome
{
 public PageTotalStatistics create() throws javax.ejb.CreateException;
 public PageTotalStatistics createLegacy();
 public PageTotalStatistics findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public PageTotalStatistics findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public PageTotalStatistics findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}