package is.idega.idegaweb.campus.block.application.data;


public class SpouseOccupationHomeImpl extends com.idega.data.IDOFactory implements SpouseOccupationHome
{
 protected Class getEntityInterfaceClass(){
  return SpouseOccupation.class;
 }


 public SpouseOccupation create() throws javax.ejb.CreateException{
  return (SpouseOccupation) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SpouseOccupationBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public SpouseOccupation findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SpouseOccupation) super.findByPrimaryKeyIDO(pk);
 }



}