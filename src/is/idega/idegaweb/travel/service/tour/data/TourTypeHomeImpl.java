package is.idega.idegaweb.travel.service.tour.data;


public class TourTypeHomeImpl extends com.idega.data.IDOFactory implements TourTypeHome
{
 protected Class getEntityInterfaceClass(){
  return TourType.class;
 }


 public TourType create() throws javax.ejb.CreateException{
  return (TourType) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TourTypeBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByCategory(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TourTypeBMPBean)entity).ejbFindByCategory(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public TourType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TourType) super.findByPrimaryKeyIDO(pk);
 }



}