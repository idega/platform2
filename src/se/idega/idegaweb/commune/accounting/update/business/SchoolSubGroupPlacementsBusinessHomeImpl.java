package se.idega.idegaweb.commune.accounting.update.business;


public class SchoolSubGroupPlacementsBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements SchoolSubGroupPlacementsBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return SchoolSubGroupPlacementsBusiness.class;
 }


 public SchoolSubGroupPlacementsBusiness create() throws javax.ejb.CreateException{
  return (SchoolSubGroupPlacementsBusiness) super.createIBO();
 }



}