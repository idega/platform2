package is.idega.idegaweb.golf.entity;


public class CurrentPositionHomeImpl extends com.idega.data.IDOFactory implements CurrentPositionHome
{
 protected Class getEntityInterfaceClass(){
  return CurrentPosition.class;
 }

 public CurrentPosition create() throws javax.ejb.CreateException{
  return (CurrentPosition) super.idoCreate();
 }

 public CurrentPosition createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public CurrentPosition findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (CurrentPosition) super.idoFindByPrimaryKey(id);
 }

 public CurrentPosition findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CurrentPosition) super.idoFindByPrimaryKey(pk);
 }

 public CurrentPosition findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}