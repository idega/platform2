package is.idega.idegaweb.member.business.plugins;


public class FlokkarPluginBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements FlokkarPluginBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return FlokkarPluginBusiness.class;
 }


 public FlokkarPluginBusiness create() throws javax.ejb.CreateException{
  return (FlokkarPluginBusiness) super.createIBO();
 }



}