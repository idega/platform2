package is.idega.idegaweb.golf.entity;


public class AdHomeImpl extends com.idega.data.IDOFactory implements AdHome
{
 protected Class getEntityInterfaceClass(){
  return Ad.class;
 }

 public Ad create() throws javax.ejb.CreateException{
  return (Ad) super.idoCreate();
 }

 public Ad createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Ad findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Ad) super.idoFindByPrimaryKey(id);
 }

 public Ad findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Ad) super.idoFindByPrimaryKey(pk);
 }

 public Ad findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}