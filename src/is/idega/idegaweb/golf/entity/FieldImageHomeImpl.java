package is.idega.idegaweb.golf.entity;


public class FieldImageHomeImpl extends com.idega.data.IDOFactory implements FieldImageHome
{
 protected Class getEntityInterfaceClass(){
  return FieldImage.class;
 }

 public FieldImage create() throws javax.ejb.CreateException{
  return (FieldImage) super.idoCreate();
 }

 public FieldImage createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public FieldImage findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (FieldImage) super.idoFindByPrimaryKey(id);
 }

 public FieldImage findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (FieldImage) super.idoFindByPrimaryKey(pk);
 }

 public FieldImage findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }

 
 public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((FieldImageBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}
 
 

}