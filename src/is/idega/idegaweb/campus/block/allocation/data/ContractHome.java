package is.idega.idegaweb.campus.block.allocation.data;


public interface ContractHome extends com.idega.data.IDOHome
{
 public Contract create() throws javax.ejb.CreateException;
 public Contract createLegacy();
 public Contract findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Contract findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Contract findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;
 public java.util.Collection findByApplicant(Integer id) throws javax.ejb.FinderException;

}