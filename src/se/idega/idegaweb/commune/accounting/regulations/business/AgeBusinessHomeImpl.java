package se.idega.idegaweb.commune.accounting.regulations.business;


public class AgeBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements AgeBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return AgeBusiness.class;
 }


 public AgeBusiness create() throws javax.ejb.CreateException{
  return (AgeBusiness) super.createIBO();
 }



}