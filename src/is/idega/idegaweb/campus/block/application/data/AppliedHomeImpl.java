package is.idega.idegaweb.campus.block.application.data;


public class AppliedHomeImpl extends com.idega.data.IDOFactory implements AppliedHome
{
 protected Class getEntityInterfaceClass(){
  return Applied.class;
 }


 public Applied create() throws javax.ejb.CreateException{
  return (Applied) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AppliedBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByApplicationID(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AppliedBMPBean)entity).ejbFindByApplicationID(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySQL(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AppliedBMPBean)entity).ejbFindBySQL(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Applied findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Applied) super.findByPrimaryKeyIDO(pk);
 }



}