package is.idega.idegaweb.campus.data;


public interface ContractAccountsHome extends com.idega.data.IDOHome
{
 public ContractAccounts create() throws javax.ejb.CreateException;
 public ContractAccounts findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByPeriodOverLap(java.sql.Date p0,java.sql.Date p1)throws javax.ejb.FinderException;

}