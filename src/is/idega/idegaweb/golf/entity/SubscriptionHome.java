package is.idega.idegaweb.golf.entity;


public interface SubscriptionHome extends com.idega.data.IDOHome
{
 public Subscription create() throws javax.ejb.CreateException;
 public Subscription createLegacy();
 public Subscription findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Subscription findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Subscription findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}