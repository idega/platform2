package is.idega.idegaweb.golf.entity;


public class UnionHomeImpl extends com.idega.data.IDOFactory implements UnionHome
{
 protected Class getEntityInterfaceClass(){
  return Union.class;
 }

 public Union create() throws javax.ejb.CreateException{
  return (Union) super.idoCreate();
 }

 public Union createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Union findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Union) super.idoFindByPrimaryKey(id);
 }

 public Union findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Union) super.idoFindByPrimaryKey(pk);
 }

 public Union findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}