package se.idega.idegaweb.commune.accounting.regulations.data;


public class ActivityTypeHomeImpl extends com.idega.data.IDOFactory implements ActivityTypeHome
{
 protected Class getEntityInterfaceClass(){
  return ActivityType.class;
 }


 public ActivityType create() throws javax.ejb.CreateException{
  return (ActivityType) super.createIDO();
 }


public ActivityType findActivityType(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ActivityTypeBMPBean)entity).ejbFindActivityType(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findAllActivityTypes()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ActivityTypeBMPBean)entity).ejbFindAllActivityTypes();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public ActivityType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ActivityType) super.findByPrimaryKeyIDO(pk);
 }



}