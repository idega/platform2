package is.idega.idegaweb.campus.block.allocation.data;


public interface ContractTextHome extends com.idega.data.IDOHome
{
 public ContractText create() throws javax.ejb.CreateException;
 public ContractText findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByLanguage(java.lang.String p0)throws javax.ejb.FinderException;

}