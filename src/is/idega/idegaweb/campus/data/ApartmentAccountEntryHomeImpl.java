package is.idega.idegaweb.campus.data;


public class ApartmentAccountEntryHomeImpl extends com.idega.data.IDOFactory implements ApartmentAccountEntryHome
{
 protected Class getEntityInterfaceClass(){
  return ApartmentAccountEntry.class;
 }


public ApartmentAccountEntry create(is.idega.idegaweb.campus.data.ApartmentAccountEntryKey p0)throws javax.ejb.CreateException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ApartmentAccountEntryBMPBean)entity).ejbCreate(p0);
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

 public ApartmentAccountEntry create() throws javax.ejb.CreateException{
  return (ApartmentAccountEntry) super.createIDO();
 }


public ApartmentAccountEntry findByPrimaryKey(is.idega.idegaweb.campus.data.ApartmentAccountEntryKey p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ApartmentAccountEntryBMPBean)entity).ejbFindByPrimaryKey(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public ApartmentAccountEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ApartmentAccountEntry) super.findByPrimaryKeyIDO(pk);
 }



}