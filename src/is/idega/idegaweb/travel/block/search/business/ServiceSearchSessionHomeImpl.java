package is.idega.idegaweb.travel.block.search.business;


public class ServiceSearchSessionHomeImpl extends com.idega.business.IBOHomeImpl implements ServiceSearchSessionHome
{
 protected Class getBeanInterfaceClass(){
  return ServiceSearchSession.class;
 }


 public ServiceSearchSession create() throws javax.ejb.CreateException{
  return (ServiceSearchSession) super.createIBO();
 }



}