package is.idega.idegaweb.campus.data;


public interface ContractAccountsHome extends com.idega.data.IDOHome
{
 public ContractAccounts create() throws javax.ejb.CreateException;
 public ContractAccounts createLegacy();
 public ContractAccounts findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ContractAccounts findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ContractAccounts findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}