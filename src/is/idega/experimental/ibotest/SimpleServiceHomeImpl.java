package is.idega.experimental.ibotest;


public class SimpleServiceHomeImpl extends com.idega.business.IBOHomeImpl implements SimpleServiceHome
{
 protected Class getBeanInterfaceClass(){
  return SimpleService.class;
 }


 public SimpleService create() throws javax.ejb.CreateException{
  return (SimpleService) super.createIBO();
 }



}