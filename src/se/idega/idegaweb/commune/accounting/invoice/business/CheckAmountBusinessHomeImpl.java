package se.idega.idegaweb.commune.accounting.invoice.business;


public class CheckAmountBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements CheckAmountBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return CheckAmountBusiness.class;
 }


 public CheckAmountBusiness create() throws javax.ejb.CreateException{
  return (CheckAmountBusiness) super.createIBO();
 }



}