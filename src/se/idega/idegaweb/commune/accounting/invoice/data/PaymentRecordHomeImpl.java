package se.idega.idegaweb.commune.accounting.invoice.data;


public class PaymentRecordHomeImpl extends com.idega.data.IDOFactory implements PaymentRecordHome
{
 protected Class getEntityInterfaceClass(){
  return PaymentRecord.class;
 }


 public PaymentRecord create() throws javax.ejb.CreateException{
  return (PaymentRecord) super.createIDO();
 }


public java.util.Collection findByMonth(java.sql.Date p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PaymentRecordBMPBean)entity).ejbFindByMonth(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public PaymentRecord findByPaymentHeaderAndRuleSpecType(se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((PaymentRecordBMPBean)entity).ejbFindByPaymentHeaderAndRuleSpecType(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public PaymentRecord findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PaymentRecord) super.findByPrimaryKeyIDO(pk);
 }



}