package is.idega.idegaweb.golf.entity;


public class UserIdHomeImpl extends com.idega.data.IDOFactory implements UserIdHome
{
 protected Class getEntityInterfaceClass(){
  return UserId.class;
 }

 public UserId create() throws javax.ejb.CreateException{
  return (UserId) super.idoCreate();
 }

 public UserId createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public UserId findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (UserId) super.idoFindByPrimaryKey(id);
 }

 public UserId findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (UserId) super.idoFindByPrimaryKey(pk);
 }

 public UserId findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}