package is.idega.idegaweb.project.data;


public class IPCategoryHomeImpl extends com.idega.data.IDOFactory implements IPCategoryHome
{
 protected Class getEntityInterfaceClass(){
  return IPCategory.class;
 }

 public IPCategory create() throws javax.ejb.CreateException{
  return (IPCategory) super.idoCreate();
 }

 public IPCategory createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public IPCategory findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (IPCategory) super.idoFindByPrimaryKey(id);
 }

 public IPCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (IPCategory) super.idoFindByPrimaryKey(pk);
 }

 public IPCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}