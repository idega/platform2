package is.idega.idegaweb.member.business.plugins;


public class DeildirPluginBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements DeildirPluginBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return DeildirPluginBusiness.class;
 }


 public DeildirPluginBusiness create() throws javax.ejb.CreateException{
  return (DeildirPluginBusiness) super.createIBO();
 }



}