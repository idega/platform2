package is.idega.idegaweb.golf.entity;


public interface DisplayScoresHome extends com.idega.data.IDOHome
{
 public DisplayScores create() throws javax.ejb.CreateException;
 public DisplayScores createLegacy();
 public DisplayScores findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public DisplayScores findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public DisplayScores findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}