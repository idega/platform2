package is.idega.idegaweb.travel.block.search.data;


public interface ServiceSearchEngineHome extends com.idega.data.IDOHome
{
 public ServiceSearchEngine create() throws javax.ejb.CreateException;
 public ServiceSearchEngine findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
 public ServiceSearchEngine findByCode(java.lang.String p0)throws javax.ejb.FinderException;
 public ServiceSearchEngine findByGroupID(int p0)throws javax.ejb.FinderException;
 public ServiceSearchEngine findByName(java.lang.String p0)throws javax.ejb.FinderException;

}