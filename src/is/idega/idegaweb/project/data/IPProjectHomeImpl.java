package is.idega.idegaweb.project.data;


public class IPProjectHomeImpl extends com.idega.data.IDOFactory implements IPProjectHome
{
 protected Class getEntityInterfaceClass(){
  return IPProject.class;
 }

 public IPProject create() throws javax.ejb.CreateException{
  return (IPProject) super.idoCreate();
 }

 public IPProject createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public IPProject findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (IPProject) super.idoFindByPrimaryKey(id);
 }

 public IPProject findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (IPProject) super.idoFindByPrimaryKey(pk);
 }

 public IPProject findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}