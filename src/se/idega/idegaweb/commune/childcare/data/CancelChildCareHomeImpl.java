package se.idega.idegaweb.commune.childcare.data;


public class CancelChildCareHomeImpl extends com.idega.data.IDOFactory implements CancelChildCareHome
{
 protected Class getEntityInterfaceClass(){
  return CancelChildCare.class;
 }


 public CancelChildCare create() throws javax.ejb.CreateException{
  return (CancelChildCare) super.createIDO();
 }


 public CancelChildCare findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CancelChildCare) super.findByPrimaryKeyIDO(pk);
 }
}