package is.idega.idegaweb.golf.entity;


public interface RankingHome extends com.idega.data.IDOHome
{
 public Ranking create() throws javax.ejb.CreateException;
 public Ranking createLegacy();
 public Ranking findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Ranking findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Ranking findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}