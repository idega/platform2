package is.idega.idegaweb.golf.entity;


public class DismissalHomeImpl extends com.idega.data.IDOFactory implements DismissalHome
{
 protected Class getEntityInterfaceClass(){
  return Dismissal.class;
 }

 public Dismissal create() throws javax.ejb.CreateException{
  return (Dismissal) super.idoCreate();
 }

 public Dismissal createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Dismissal findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Dismissal) super.idoFindByPrimaryKey(id);
 }

 public Dismissal findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Dismissal) super.idoFindByPrimaryKey(pk);
 }

 public Dismissal findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}