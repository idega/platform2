package is.idega.idegaweb.campus.block.building.data;


public class ApartmentTypePeriodsHomeImpl extends com.idega.data.IDOFactory implements ApartmentTypePeriodsHome
{
 protected Class getEntityInterfaceClass(){
  return ApartmentTypePeriods.class;
 }


 public ApartmentTypePeriods create() throws javax.ejb.CreateException{
  return (ApartmentTypePeriods) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ApartmentTypePeriodsBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByApartmentType(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ApartmentTypePeriodsBMPBean)entity).ejbFindByApartmentType(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public ApartmentTypePeriods findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ApartmentTypePeriods) super.findByPrimaryKeyIDO(pk);
 }



}