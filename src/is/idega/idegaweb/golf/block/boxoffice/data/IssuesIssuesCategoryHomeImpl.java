package is.idega.idegaweb.golf.block.boxoffice.data;


public class IssuesIssuesCategoryHomeImpl extends com.idega.data.IDOFactory implements IssuesIssuesCategoryHome
{
 protected Class getEntityInterfaceClass(){
  return IssuesIssuesCategory.class;
 }


 public IssuesIssuesCategory create() throws javax.ejb.CreateException{
  return (IssuesIssuesCategory) super.createIDO();
 }


 public IssuesIssuesCategory createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


 public IssuesIssuesCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (IssuesIssuesCategory) super.findByPrimaryKeyIDO(pk);
 }


 public IssuesIssuesCategory findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (IssuesIssuesCategory) super.findByPrimaryKeyIDO(id);
 }


 public IssuesIssuesCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



}