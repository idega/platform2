package is.idega.idegaweb.golf.entity;


public interface StrokeHome extends com.idega.data.IDOHome
{
 public Stroke create() throws javax.ejb.CreateException;
 public Stroke createLegacy();
 public Stroke findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Stroke findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Stroke findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;
 public int getCountDifferenceByMember(int p0,int p1,java.lang.String p2)throws com.idega.data.IDOException;
 public int getCountOfHolesPlayedByMember(int p0)throws com.idega.data.IDOException;
 public int getSumOfStrokesByMember(int p0)throws com.idega.data.IDOException;

}