package is.idega.idegaweb.member.isi.block.accounting.data;


public class PaymentTypeHomeImpl extends com.idega.data.IDOFactory implements PaymentTypeHome
{
 protected Class getEntityInterfaceClass(){
  return PaymentType.class;
 }


 public PaymentType create() throws javax.ejb.CreateException{
  return (PaymentType) super.createIDO();
 }


public java.util.Collection findAllPaymentTypes()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PaymentTypeBMPBean)entity).ejbFindAllPaymentTypes();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public PaymentType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PaymentType) super.findByPrimaryKeyIDO(pk);
 }



}