package is.idega.idegaweb.travel.data;


public class GeneralBookingHomeImpl extends com.idega.data.IDOFactory implements GeneralBookingHome
{
 protected Class getEntityInterfaceClass(){
  return GeneralBooking.class;
 }


 public GeneralBooking create() throws javax.ejb.CreateException{
  return (GeneralBooking) super.createIDO();
 }


public java.util.Collection findBookings(int p0,int p1,com.idega.util.idegaTimestamp p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((GeneralBookingBMPBean)entity).ejbFindBookings(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBookings(int[] p0,com.idega.util.idegaTimestamp p1,com.idega.util.idegaTimestamp p2,int[] p3,java.lang.String p4,java.lang.String p5)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((GeneralBookingBMPBean)entity).ejbFindBookings(p0,p1,p2,p3,p4,p5);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBookings(int[] p0,int p1,com.idega.util.idegaTimestamp p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((GeneralBookingBMPBean)entity).ejbFindBookings(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public GeneralBooking findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (GeneralBooking) super.findByPrimaryKeyIDO(pk);
 }


public java.util.List getMultibleBookings(is.idega.idegaweb.travel.data.GeneralBooking p0)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.List theReturn = ((GeneralBookingBMPBean)entity).ejbHomeGetMultibleBookings(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfBookings(int[] p0,int p1,com.idega.util.idegaTimestamp p2){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((GeneralBookingBMPBean)entity).ejbHomeGetNumberOfBookings(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfBookings(int p0,com.idega.util.idegaTimestamp p1,com.idega.util.idegaTimestamp p2,int p3,int[] p4){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((GeneralBookingBMPBean)entity).ejbHomeGetNumberOfBookings(p0,p1,p2,p3,p4);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}