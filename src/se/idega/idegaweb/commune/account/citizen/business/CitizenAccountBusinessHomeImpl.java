package se.idega.idegaweb.commune.account.citizen.business;


public class CitizenAccountBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements CitizenAccountBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return CitizenAccountBusiness.class;
 }


 public CitizenAccountBusiness create() throws javax.ejb.CreateException{
  return (CitizenAccountBusiness) super.createIBO();
 }



}