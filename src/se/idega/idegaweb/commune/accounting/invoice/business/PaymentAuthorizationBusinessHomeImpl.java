package se.idega.idegaweb.commune.accounting.invoice.business;


public class PaymentAuthorizationBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements PaymentAuthorizationBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return PaymentAuthorizationBusiness.class;
 }


 public PaymentAuthorizationBusiness create() throws javax.ejb.CreateException{
  return (PaymentAuthorizationBusiness) super.createIBO();
 }



}