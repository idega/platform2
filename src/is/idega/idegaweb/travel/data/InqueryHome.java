package is.idega.idegaweb.travel.data;


public interface InqueryHome extends com.idega.data.IDOHome
{
 public Inquery create() throws javax.ejb.CreateException;
 public Inquery createLegacy();
 public Inquery findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Inquery findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Inquery findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}