package is.idega.idegaweb.golf.entity;


public class UnionImageHomeImpl extends com.idega.data.IDOFactory implements UnionImageHome
{
 protected Class getEntityInterfaceClass(){
  return UnionImage.class;
 }

 public UnionImage create() throws javax.ejb.CreateException{
  return (UnionImage) super.idoCreate();
 }

 public UnionImage createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public UnionImage findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (UnionImage) super.idoFindByPrimaryKey(id);
 }

 public UnionImage findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (UnionImage) super.idoFindByPrimaryKey(pk);
 }

 public UnionImage findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}