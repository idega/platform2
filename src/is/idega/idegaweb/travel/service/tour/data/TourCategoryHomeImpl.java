package is.idega.idegaweb.travel.service.tour.data;


public class TourCategoryHomeImpl extends com.idega.data.IDOFactory implements TourCategoryHome
{
 protected Class getEntityInterfaceClass(){
  return TourCategory.class;
 }


 public TourCategory create() throws javax.ejb.CreateException{
  return (TourCategory) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TourCategoryBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public TourCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TourCategory) super.findByPrimaryKeyIDO(pk);
 }



}