package is.idega.idegaweb.golf.entity;


public class UserIdsHomeImpl extends com.idega.data.IDOFactory implements UserIdsHome
{
 protected Class getEntityInterfaceClass(){
  return UserIds.class;
 }

 public UserIds create() throws javax.ejb.CreateException{
  return (UserIds) super.idoCreate();
 }

 public UserIds createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public UserIds findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (UserIds) super.idoFindByPrimaryKey(id);
 }

 public UserIds findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (UserIds) super.idoFindByPrimaryKey(pk);
 }

 public UserIds findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}