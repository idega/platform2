package is.idega.idegaweb.campus.block.application.data;


public class CurrentResidencyHomeImpl extends com.idega.data.IDOFactory implements CurrentResidencyHome
{
 protected Class getEntityInterfaceClass(){
  return CurrentResidency.class;
 }


 public CurrentResidency create() throws javax.ejb.CreateException{
  return (CurrentResidency) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CurrentResidencyBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public CurrentResidency findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CurrentResidency) super.findByPrimaryKeyIDO(pk);
 }



}