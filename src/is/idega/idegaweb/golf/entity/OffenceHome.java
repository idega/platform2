package is.idega.idegaweb.golf.entity;


public interface OffenceHome extends com.idega.data.IDOHome
{
 public Offence create() throws javax.ejb.CreateException;
 public Offence createLegacy();
 public Offence findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Offence findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Offence findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}