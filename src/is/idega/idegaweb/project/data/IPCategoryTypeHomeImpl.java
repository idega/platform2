package is.idega.idegaweb.project.data;


public class IPCategoryTypeHomeImpl extends com.idega.data.IDOFactory implements IPCategoryTypeHome
{
 protected Class getEntityInterfaceClass(){
  return IPCategoryType.class;
 }

 public IPCategoryType create() throws javax.ejb.CreateException{
  return (IPCategoryType) super.idoCreate();
 }

 public IPCategoryType createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public IPCategoryType findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (IPCategoryType) super.idoFindByPrimaryKey(id);
 }

 public IPCategoryType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (IPCategoryType) super.idoFindByPrimaryKey(pk);
 }

 public IPCategoryType findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}