package is.idega.idegaweb.golf.entity;


public interface StatisticHome extends com.idega.data.IDOHome
{
 public Statistic create() throws javax.ejb.CreateException;
 public Statistic createLegacy();
 public Statistic findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Statistic findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Statistic findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}