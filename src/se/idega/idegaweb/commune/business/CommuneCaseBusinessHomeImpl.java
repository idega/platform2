package se.idega.idegaweb.commune.business;


public class CommuneCaseBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements CommuneCaseBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return CommuneCaseBusiness.class;
 }


 public CommuneCaseBusiness create() throws javax.ejb.CreateException{
  return (CommuneCaseBusiness) super.createIBO();
 }



}