package is.idega.idegaweb.travel.service.business;


public class ServiceHandlerHomeImpl extends com.idega.business.IBOHomeImpl implements ServiceHandlerHome
{
 protected Class getBeanInterfaceClass(){
  return ServiceHandler.class;
 }


 public ServiceHandler create() throws javax.ejb.CreateException{
  return (ServiceHandler) super.createIBO();
 }



}