package is.idega.idegaweb.golf.entity;


public class FamilyHomeImpl extends com.idega.data.IDOFactory implements FamilyHome
{
 protected Class getEntityInterfaceClass(){
  return Family.class;
 }

 public Family create() throws javax.ejb.CreateException{
  return (Family) super.idoCreate();
 }

 public Family createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Family findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Family) super.idoFindByPrimaryKey(id);
 }

 public Family findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Family) super.idoFindByPrimaryKey(pk);
 }

 public Family findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}