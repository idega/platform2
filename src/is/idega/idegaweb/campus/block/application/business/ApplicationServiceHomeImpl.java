package is.idega.idegaweb.campus.block.application.business;


public class ApplicationServiceHomeImpl extends com.idega.business.IBOHomeImpl implements ApplicationServiceHome
{
 protected Class getBeanInterfaceClass(){
  return ApplicationService.class;
 }


 public ApplicationService create() throws javax.ejb.CreateException{
  return (ApplicationService) super.createIBO();
 }



}