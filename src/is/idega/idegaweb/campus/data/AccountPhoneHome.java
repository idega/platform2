package is.idega.idegaweb.campus.data;


public interface AccountPhoneHome extends com.idega.data.IDOHome
{
 public AccountPhone create() throws javax.ejb.CreateException;
 public AccountPhone findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}