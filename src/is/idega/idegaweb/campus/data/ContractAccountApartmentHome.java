package is.idega.idegaweb.campus.data;


public interface ContractAccountApartmentHome extends com.idega.data.IDOHome
{
 public ContractAccountApartment create() throws javax.ejb.CreateException;
 public ContractAccountApartment createLegacy();
 public ContractAccountApartment findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ContractAccountApartment findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ContractAccountApartment findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}