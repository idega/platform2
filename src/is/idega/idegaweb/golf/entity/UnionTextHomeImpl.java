package is.idega.idegaweb.golf.entity;


public class UnionTextHomeImpl extends com.idega.data.IDOFactory implements UnionTextHome
{
 protected Class getEntityInterfaceClass(){
  return UnionText.class;
 }

 public UnionText create() throws javax.ejb.CreateException{
  return (UnionText) super.idoCreate();
 }

 public UnionText createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public UnionText findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (UnionText) super.idoFindByPrimaryKey(id);
 }

 public UnionText findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (UnionText) super.idoFindByPrimaryKey(pk);
 }

 public UnionText findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}