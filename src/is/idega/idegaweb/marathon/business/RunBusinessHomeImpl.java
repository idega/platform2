package is.idega.idegaweb.marathon.business;


public class RunBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements RunBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return RunBusiness.class;
 }


 public RunBusiness create() throws javax.ejb.CreateException{
  return (RunBusiness) super.createIBO();
 }



}