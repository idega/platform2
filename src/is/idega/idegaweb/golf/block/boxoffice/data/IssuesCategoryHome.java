package is.idega.idegaweb.golf.block.boxoffice.data;


public interface IssuesCategoryHome extends com.idega.data.IDOHome
{
 public IssuesCategory create() throws javax.ejb.CreateException;
 public IssuesCategory createLegacy();
 public IssuesCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public IssuesCategory findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public IssuesCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}