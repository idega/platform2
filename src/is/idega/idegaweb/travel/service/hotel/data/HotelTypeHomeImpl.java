package is.idega.idegaweb.travel.service.hotel.data;


public class HotelTypeHomeImpl extends com.idega.data.IDOFactory implements HotelTypeHome
{
 protected Class getEntityInterfaceClass(){
  return HotelType.class;
 }


 public HotelType create() throws javax.ejb.CreateException{
  return (HotelType) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((HotelTypeBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public HotelType findByLocalizationKey(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((HotelTypeBMPBean)entity).ejbFindByLocalizationKey(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public HotelType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (HotelType) super.findByPrimaryKeyIDO(pk);
 }



}