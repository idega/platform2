package se.idega.idegaweb.commune.accounting.regulations.business;


public class VATBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements VATBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return VATBusiness.class;
 }


 public VATBusiness create() throws javax.ejb.CreateException{
  return (VATBusiness) super.createIBO();
 }



}