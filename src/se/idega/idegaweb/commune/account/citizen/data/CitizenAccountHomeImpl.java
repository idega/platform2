package se.idega.idegaweb.commune.account.citizen.data;


public class CitizenAccountHomeImpl extends com.idega.data.IDOFactory implements CitizenAccountHome
{
 protected Class getEntityInterfaceClass(){
  return CitizenAccount.class;
 }


 public CitizenAccount create() throws javax.ejb.CreateException{
  return (CitizenAccount) super.createIDO();
 }


 public CitizenAccount findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CitizenAccount) super.findByPrimaryKeyIDO(pk);
 }



}