package se.idega.idegaweb.commune.business;


public class CommuneFamilyServiceHomeImpl extends com.idega.business.IBOHomeImpl implements CommuneFamilyServiceHome
{
 protected Class getBeanInterfaceClass(){
  return CommuneFamilyService.class;
 }


 public CommuneFamilyService create() throws javax.ejb.CreateException{
  return (CommuneFamilyService) super.createIBO();
 }



}