package se.idega.idegaweb.commune.care.data;


public class ApplicationPriorityHomeImpl extends com.idega.data.IDOFactory implements ApplicationPriorityHome
{
 protected Class getEntityInterfaceClass(){
  return ApplicationPriority.class;
 }


 public ApplicationPriority create() throws javax.ejb.CreateException{
  return (ApplicationPriority) super.createIDO();
 }


public java.util.Collection findByPeriodAndProvider(java.sql.Date p0,java.sql.Date p1,int p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ApplicationPriorityBMPBean)entity).ejbFindByPeriodAndProvider(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public ApplicationPriority findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ApplicationPriority) super.findByPrimaryKeyIDO(pk);
 }



}