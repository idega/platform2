package se.idega.idegaweb.commune.business;


public class CommuneUserBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements CommuneUserBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return CommuneUserBusiness.class;
 }


 public CommuneUserBusiness create() throws javax.ejb.CreateException{
  return (CommuneUserBusiness) super.createIBO();
 }



}