package is.idega.idegaweb.golf.entity;


public class GolfEntityHomeImpl extends com.idega.data.IDOFactory implements GolfEntityHome
{
 protected Class getEntityInterfaceClass(){
  return GolfEntity.class;
 }

 public GolfEntity create() throws javax.ejb.CreateException{
  return (GolfEntity) super.idoCreate();
 }

 public GolfEntity createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public GolfEntity findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (GolfEntity) super.idoFindByPrimaryKey(id);
 }

 public GolfEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (GolfEntity) super.idoFindByPrimaryKey(pk);
 }

 public GolfEntity findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}