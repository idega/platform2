package se.idega.idegaweb.commune.accounting.invoice.data;


public class PaymentRecordHomeImpl extends com.idega.data.IDOFactory implements PaymentRecordHome
{
 protected Class getEntityInterfaceClass(){
  return PaymentRecord.class;
 }


 public PaymentRecord create() throws javax.ejb.CreateException{
  return (PaymentRecord) super.createIDO();
 }


 public PaymentRecord findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PaymentRecord) super.findByPrimaryKeyIDO(pk);
 }



}