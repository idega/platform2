package is.idega.idegaweb.golf.entity;


public interface PaymentHome extends com.idega.data.IDOHome
{
 public Payment create() throws javax.ejb.CreateException;
 public Payment createLegacy();
 public Payment findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Payment findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Payment findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}