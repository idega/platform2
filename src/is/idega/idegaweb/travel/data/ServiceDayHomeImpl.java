package is.idega.idegaweb.travel.data;


public class ServiceDayHomeImpl extends com.idega.data.IDOFactory implements ServiceDayHome
{
 protected Class getEntityInterfaceClass(){
  return ServiceDay.class;
 }


public ServiceDay create(is.idega.idegaweb.travel.data.ServiceDayPK p0)throws javax.ejb.CreateException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ServiceDayBMPBean)entity).ejbCreate(p0);
	//((ServiceDayBMPBean)entity).ejbPostCreate(p0);
	this.idoCheckInPooledEntity(entity);
	try{
		return this.findByPrimaryKey(pk);
	}
	catch(javax.ejb.FinderException fe){
		throw new com.idega.data.IDOCreateException(fe);
	}
	catch(Exception e){
		throw new com.idega.data.IDOCreateException(e);
	}
}


public ServiceDay findByPrimaryKey(is.idega.idegaweb.travel.data.ServiceDayPK p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ServiceDayBMPBean)entity).ejbFindByPrimaryKey(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public ServiceDay findByServiceAndDay(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ServiceDayBMPBean)entity).ejbFindByServiceAndDay(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public ServiceDay findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ServiceDay) super.findByPrimaryKeyIDO(pk);
 }


public boolean deleteService(int p0)throws java.rmi.RemoteException,javax.ejb.RemoveException,javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	boolean theReturn = ((ServiceDayBMPBean)entity).ejbHomeDeleteService(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int[] getDaysOfWeek(int p0)throws java.rmi.RemoteException,javax.ejb.RemoveException,javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int[] theReturn = ((ServiceDayBMPBean)entity).ejbHomeGetDaysOfWeek(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public boolean getIfDay(int p0,int p1){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	boolean theReturn = ((ServiceDayBMPBean)entity).ejbHomeGetIfDay(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public boolean setServiceWithNoDays(int p0)throws java.rmi.RemoteException,javax.ejb.RemoveException,javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	boolean theReturn = ((ServiceDayBMPBean)entity).ejbHomeSetServiceWithNoDays(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}