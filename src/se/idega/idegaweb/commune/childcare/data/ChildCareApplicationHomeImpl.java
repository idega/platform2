package se.idega.idegaweb.commune.childcare.data;


public class ChildCareApplicationHomeImpl extends com.idega.data.IDOFactory implements ChildCareApplicationHome
{
 protected Class getEntityInterfaceClass(){
  return ChildCareApplication.class;
 }


 public ChildCareApplication create() throws javax.ejb.CreateException{
  return (ChildCareApplication) super.createIDO();
 }


 public ChildCareApplication findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ChildCareApplication) super.findByPrimaryKeyIDO(pk);
 }



}