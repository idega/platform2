package is.idega.idegaweb.travel.data;


public interface ContractHome extends com.idega.data.IDOHome
{
 public Contract create() throws javax.ejb.CreateException;
 public Contract findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Contract findByProductAndReseller(int p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findByProductId(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findByResellerId(int p0)throws javax.ejb.FinderException;

}