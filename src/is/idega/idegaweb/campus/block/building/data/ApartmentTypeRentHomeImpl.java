package is.idega.idegaweb.campus.block.building.data;


public class ApartmentTypeRentHomeImpl extends com.idega.data.IDOFactory implements ApartmentTypeRentHome
{
 protected Class getEntityInterfaceClass(){
  return ApartmentTypeRent.class;
 }


 public ApartmentTypeRent create() throws javax.ejb.CreateException{
  return (ApartmentTypeRent) super.createIDO();
 }


public java.util.Collection findByType(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ApartmentTypeRentBMPBean)entity).ejbFindByType(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public ApartmentTypeRent findByTypeAndValidity(int p0,java.sql.Date p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ApartmentTypeRentBMPBean)entity).ejbFindByTypeAndValidity(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public ApartmentTypeRent findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ApartmentTypeRent) super.findByPrimaryKeyIDO(pk);
 }



}