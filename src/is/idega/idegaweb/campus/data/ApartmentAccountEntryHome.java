package is.idega.idegaweb.campus.data;


public interface ApartmentAccountEntryHome extends com.idega.data.IDOHome
{
 public ApartmentAccountEntry create() throws javax.ejb.CreateException;
 public ApartmentAccountEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ApartmentAccountEntry create(is.idega.idegaweb.campus.data.ApartmentAccountEntryKey p0)throws javax.ejb.CreateException;
 public ApartmentAccountEntry findByPrimaryKey(is.idega.idegaweb.campus.data.ApartmentAccountEntryKey p0)throws javax.ejb.FinderException;

}