package is.idega.idegaweb.travel.block.search.data;


public interface ServiceSearchEngineStaffGroupHome extends com.idega.data.IDOHome
{
 public ServiceSearchEngineStaffGroup create() throws javax.ejb.CreateException;
 public ServiceSearchEngineStaffGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}