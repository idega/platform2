package is.idega.idegaweb.marathon.business;


public class RunPluginBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements RunPluginBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return RunPluginBusiness.class;
 }


 public RunPluginBusiness create() throws javax.ejb.CreateException{
  return (RunPluginBusiness) super.createIBO();
 }



}