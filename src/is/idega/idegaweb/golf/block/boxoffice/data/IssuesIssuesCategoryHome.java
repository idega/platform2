package is.idega.idegaweb.golf.block.boxoffice.data;


public interface IssuesIssuesCategoryHome extends com.idega.data.IDOHome
{
 public IssuesIssuesCategory create() throws javax.ejb.CreateException;
 public IssuesIssuesCategory createLegacy();
 public IssuesIssuesCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public IssuesIssuesCategory findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public IssuesIssuesCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}