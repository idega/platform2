package is.idega.idegaweb.member.business.plugins;


public class GroupBoardPluginBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements GroupBoardPluginBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return GroupBoardPluginBusiness.class;
 }


 public GroupBoardPluginBusiness create() throws javax.ejb.CreateException{
  return (GroupBoardPluginBusiness) super.createIBO();
 }



}