package se.idega.idegaweb.commune.accounting.invoice.business;


public class RegularPaymentBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements RegularPaymentBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return RegularPaymentBusiness.class;
 }


 public RegularPaymentBusiness create() throws javax.ejb.CreateException{
  return (RegularPaymentBusiness) super.createIBO();
 }



}