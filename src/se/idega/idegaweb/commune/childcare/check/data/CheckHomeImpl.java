package se.idega.idegaweb.commune.childcare.check.data;


public class CheckHomeImpl extends com.idega.data.IDOFactory implements CheckHome
{
 protected Class getEntityInterfaceClass(){
  return Check.class;
 }


 public Check create() throws javax.ejb.CreateException{
  return (Check) super.createIDO();
 }


public java.util.Collection findChecks()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CheckBMPBean)entity).ejbFindChecks();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Check findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Check) super.findByPrimaryKeyIDO(pk);
 }



}