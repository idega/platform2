package se.idega.idegaweb.commune.accounting.regulations.data;


public class PaymentFlowTypeHomeImpl extends com.idega.data.IDOFactory implements PaymentFlowTypeHome
{
 protected Class getEntityInterfaceClass(){
  return PaymentFlowType.class;
 }


 public PaymentFlowType create() throws javax.ejb.CreateException{
  return (PaymentFlowType) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PaymentFlowTypeBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public PaymentFlowType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PaymentFlowType) super.findByPrimaryKeyIDO(pk);
 }



}