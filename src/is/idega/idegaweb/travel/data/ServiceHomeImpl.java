package is.idega.idegaweb.travel.data;


public class ServiceHomeImpl extends com.idega.data.IDOFactory implements ServiceHome
{
 protected Class getEntityInterfaceClass(){
  return Service.class;
 }


 public Service create() throws javax.ejb.CreateException{
  return (Service) super.createIDO();
 }


 public Service findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Service) super.findByPrimaryKeyIDO(pk);
 }



}