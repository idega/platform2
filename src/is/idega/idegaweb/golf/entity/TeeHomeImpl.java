package is.idega.idegaweb.golf.entity;


public class TeeHomeImpl extends com.idega.data.IDOFactory implements TeeHome
{
 protected Class getEntityInterfaceClass(){
  return Tee.class;
 }

 public Tee create() throws javax.ejb.CreateException{
  return (Tee) super.idoCreate();
 }

 public Tee createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Tee findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Tee) super.idoFindByPrimaryKey(id);
 }

 public Tee findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Tee) super.idoFindByPrimaryKey(pk);
 }

 public Tee findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}