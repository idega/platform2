package is.idega.idegaweb.golf.course.data;


public class TeeColorHomeImpl extends com.idega.data.IDOFactory implements TeeColorHome
{
 protected Class getEntityInterfaceClass(){
  return TeeColor.class;
 }


 public TeeColor create() throws javax.ejb.CreateException{
  return (TeeColor) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TeeColorBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public TeeColor findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TeeColor) super.findByPrimaryKeyIDO(pk);
 }



}