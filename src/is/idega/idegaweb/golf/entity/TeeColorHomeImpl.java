package is.idega.idegaweb.golf.entity;


public class TeeColorHomeImpl extends com.idega.data.IDOFactory implements TeeColorHome
{
 protected Class getEntityInterfaceClass(){
  return TeeColor.class;
 }

 public TeeColor create() throws javax.ejb.CreateException{
  return (TeeColor) super.idoCreate();
 }

 public TeeColor createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public TeeColor findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (TeeColor) super.idoFindByPrimaryKey(id);
 }

 public TeeColor findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TeeColor) super.idoFindByPrimaryKey(pk);
 }

 public TeeColor findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}