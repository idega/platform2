package se.idega.idegaweb.commune.childcare.data;


public class ChangeChildCareHomeImpl extends com.idega.data.IDOFactory implements ChangeChildCareHome
{
 protected Class getEntityInterfaceClass(){
  return ChangeChildCare.class;
 }


 public ChangeChildCare create() throws javax.ejb.CreateException{
  return (ChangeChildCare) super.createIDO();
 }


 public ChangeChildCare findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ChangeChildCare) super.findByPrimaryKeyIDO(pk);
 }



}