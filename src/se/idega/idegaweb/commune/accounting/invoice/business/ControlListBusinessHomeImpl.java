package se.idega.idegaweb.commune.accounting.invoice.business;


public class ControlListBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements ControlListBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return ControlListBusiness.class;
 }


 public ControlListBusiness create() throws javax.ejb.CreateException{
  return (ControlListBusiness) super.createIBO();
 }



}