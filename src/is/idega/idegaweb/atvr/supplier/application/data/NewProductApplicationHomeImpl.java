package is.idega.idegaweb.atvr.supplier.application.data;


public class NewProductApplicationHomeImpl extends com.idega.data.IDOFactory implements NewProductApplicationHome
{
 protected Class getEntityInterfaceClass(){
  return NewProductApplication.class;
 }


 public NewProductApplication create() throws javax.ejb.CreateException{
  return (NewProductApplication) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((NewProductApplicationBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public NewProductApplication findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (NewProductApplication) super.findByPrimaryKeyIDO(pk);
 }



}