package is.idega.idegaweb.golf.entity;


public interface StatisticHome extends com.idega.data.IDOHome
{
 public Statistic create() throws javax.ejb.CreateException;
 public Statistic createLegacy();
 public Statistic findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Statistic findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Statistic findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;
 public java.util.Collection findByTeeID(java.util.Collection p0)throws javax.ejb.FinderException;
 public int getCountByTeeId(java.util.Collection p0)throws com.idega.data.IDOException;
 public int getCountFairwaysByMember(int p0)throws com.idega.data.IDOException;
 public int getCountOnGreenByMember(int p0)throws com.idega.data.IDOException;
 public int getNumberOnFairwayByMember(int p0)throws com.idega.data.IDOException;
 public int getNumberOnFairwayByTeeID(java.util.Collection p0)throws com.idega.data.IDOException;
 public int getNumberOnGreenByMember(int p0)throws com.idega.data.IDOException;
 public int getNumberOnGreenByTeeID(java.util.Collection p0)throws com.idega.data.IDOException;
 public double getPuttAverageByMember(int p0)throws com.idega.data.IDOException;
 public double getPuttAverageByTeeID(java.util.Collection p0)throws com.idega.data.IDOException;
 public int getPuttSumByMember(int p0)throws com.idega.data.IDOException;

}