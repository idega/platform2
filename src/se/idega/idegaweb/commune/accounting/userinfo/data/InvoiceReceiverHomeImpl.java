package se.idega.idegaweb.commune.accounting.userinfo.data;


public class InvoiceReceiverHomeImpl extends com.idega.data.IDOFactory implements InvoiceReceiverHome
{
 protected Class getEntityInterfaceClass(){
  return InvoiceReceiver.class;
 }


 public InvoiceReceiver create() throws javax.ejb.CreateException{
  return (InvoiceReceiver) super.createIDO();
 }


public InvoiceReceiver findByUser(com.idega.user.data.User p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((InvoiceReceiverBMPBean)entity).ejbFindByUser(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public InvoiceReceiver findByUser(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((InvoiceReceiverBMPBean)entity).ejbFindByUser(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public InvoiceReceiver findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (InvoiceReceiver) super.findByPrimaryKeyIDO(pk);
 }



}