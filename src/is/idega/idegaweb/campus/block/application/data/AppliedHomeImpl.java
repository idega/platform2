package is.idega.idegaweb.campus.block.application.data;


public class AppliedHomeImpl extends com.idega.data.IDOFactory implements AppliedHome
{
 protected Class getEntityInterfaceClass(){
  return Applied.class;
 }

 public Applied create() throws javax.ejb.CreateException{
  return (Applied) super.idoCreate();
 }

 public Applied createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Applied findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Applied) super.idoFindByPrimaryKey(id);
 }

 public Applied findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Applied) super.idoFindByPrimaryKey(pk);
 }

 public Applied findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}