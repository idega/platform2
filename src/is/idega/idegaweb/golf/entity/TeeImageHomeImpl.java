package is.idega.idegaweb.golf.entity;


public class TeeImageHomeImpl extends com.idega.data.IDOFactory implements TeeImageHome
{
 protected Class getEntityInterfaceClass(){
  return TeeImage.class;
 }

 public TeeImage create() throws javax.ejb.CreateException{
  return (TeeImage) super.idoCreate();
 }

 public TeeImage createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public TeeImage findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (TeeImage) super.idoFindByPrimaryKey(id);
 }

 public TeeImage findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TeeImage) super.idoFindByPrimaryKey(pk);
 }

 public TeeImage findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }

 public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TeeImageBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

}