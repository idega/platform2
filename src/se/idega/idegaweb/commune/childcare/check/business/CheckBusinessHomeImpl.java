package se.idega.idegaweb.commune.childcare.check.business;


public class CheckBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements CheckBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return CheckBusiness.class;
 }


 public CheckBusiness create() throws javax.ejb.CreateException{
  return (CheckBusiness) super.createIBO();
 }



}