package is.idega.idegaweb.travel.block.search.business;


public class ServiceSearchBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements ServiceSearchBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return ServiceSearchBusiness.class;
 }


 public ServiceSearchBusiness create() throws javax.ejb.CreateException{
  return (ServiceSearchBusiness) super.createIBO();
 }



}