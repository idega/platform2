package is.idega.idegaweb.member.business.plugins;


public class AgeGenderPluginBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements AgeGenderPluginBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return AgeGenderPluginBusiness.class;
 }


 public AgeGenderPluginBusiness create() throws javax.ejb.CreateException{
  return (AgeGenderPluginBusiness) super.createIBO();
 }



}