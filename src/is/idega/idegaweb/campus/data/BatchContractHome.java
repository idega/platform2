package is.idega.idegaweb.campus.data;


public interface BatchContractHome extends com.idega.data.IDOHome
{
 public BatchContract create() throws javax.ejb.CreateException;
 public BatchContract findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public BatchContract create(is.idega.idegaweb.campus.data.BatchContractKey p0)throws javax.ejb.CreateException;
 public BatchContract findByPrimaryKey(is.idega.idegaweb.campus.data.BatchContractKey p0)throws javax.ejb.FinderException;

}