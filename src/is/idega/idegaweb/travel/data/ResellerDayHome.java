package is.idega.idegaweb.travel.data;


public interface ResellerDayHome extends com.idega.data.IDOHome
{
 public ResellerDay create() throws javax.ejb.CreateException;
 public ResellerDay createLegacy();
 public ResellerDay findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ResellerDay findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ResellerDay findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}