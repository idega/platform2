package is.idega.idegaweb.travel.service.carrental.data;


public class CarRentalHomeImpl extends com.idega.data.IDOFactory implements CarRentalHome
{
 protected Class getEntityInterfaceClass(){
  return CarRental.class;
 }


 public CarRental create() throws javax.ejb.CreateException{
  return (CarRental) super.createIDO();
 }


public java.util.Collection find(com.idega.util.IWTimestamp p0,com.idega.util.IWTimestamp p1,java.lang.Object[] p2,java.lang.Object[] p3,String p4)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CarRentalBMPBean)entity).ejbFind(p0,p1,p2,p3,p4);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public CarRental findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CarRental) super.findByPrimaryKeyIDO(pk);
 }



}