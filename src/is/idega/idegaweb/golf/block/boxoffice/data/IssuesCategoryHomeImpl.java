package is.idega.idegaweb.golf.block.boxoffice.data;


public class IssuesCategoryHomeImpl extends com.idega.data.IDOFactory implements IssuesCategoryHome
{
 protected Class getEntityInterfaceClass(){
  return IssuesCategory.class;
 }


 public IssuesCategory create() throws javax.ejb.CreateException{
  return (IssuesCategory) super.createIDO();
 }


 public IssuesCategory createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


 public IssuesCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (IssuesCategory) super.findByPrimaryKeyIDO(pk);
 }


 public IssuesCategory findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (IssuesCategory) super.findByPrimaryKeyIDO(id);
 }


 public IssuesCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



}