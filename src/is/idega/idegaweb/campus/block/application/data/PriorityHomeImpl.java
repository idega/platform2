package is.idega.idegaweb.campus.block.application.data;


public class PriorityHomeImpl extends com.idega.data.IDOFactory implements PriorityHome
{
 protected Class getEntityInterfaceClass(){
  return Priority.class;
 }


 public Priority create() throws javax.ejb.CreateException{
  return (Priority) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PriorityBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Priority findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Priority) super.findByPrimaryKeyIDO(pk);
 }



}