package is.idega.idegaweb.tracker.data;


public interface ReferrerStatisticsHome extends com.idega.data.IDOHome
{
 public ReferrerStatistics create() throws javax.ejb.CreateException;
 public ReferrerStatistics createLegacy();
 public ReferrerStatistics findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ReferrerStatistics findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ReferrerStatistics findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}