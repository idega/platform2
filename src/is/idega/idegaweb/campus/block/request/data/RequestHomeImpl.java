package is.idega.idegaweb.campus.block.request.data;


public class RequestHomeImpl extends com.idega.data.IDOFactory implements RequestHome
{
 protected Class getEntityInterfaceClass(){
  return Request.class;
 }


 public Request create() throws javax.ejb.CreateException{
  return (Request) super.createIDO();
 }


 public Request findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Request) super.findByPrimaryKeyIDO(pk);
 }
 
 public java.util.Collection findAll() throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((RequestBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
 }
 
 public java.util.Collection findByType(String type) throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((RequestBMPBean)entity).ejbFindByType(type);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
 }
 
 public java.util.Collection findByStatus(String status) throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((RequestBMPBean)entity).ejbFindByStatus(status);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
 }
 
 public java.util.Collection findByUser(Integer user) throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((RequestBMPBean)entity).ejbFindByUser(user);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
 }



}