package is.idega.idegaweb.golf.entity;


public interface ScorecardHome extends com.idega.data.IDOHome
{
 public Scorecard create() throws javax.ejb.CreateException;
 public Scorecard createLegacy();
 public Scorecard findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Scorecard findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Scorecard findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;
 public int getCountRoundsPlayedByMember(int p0)throws com.idega.data.IDOException;
 public int getSumPointsByMember(int p0)throws com.idega.data.IDOException;

}