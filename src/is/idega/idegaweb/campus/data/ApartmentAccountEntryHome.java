package is.idega.idegaweb.campus.data;


public interface ApartmentAccountEntryHome extends com.idega.data.IDOHome
{
 public ApartmentAccountEntry create() throws javax.ejb.CreateException;
 public ApartmentAccountEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}