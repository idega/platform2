package is.idega.idegaweb.member.business.plugins;


public interface ClubInformationPluginBusiness extends com.idega.business.IBOService,com.idega.user.business.UserGroupPlugInBusiness
{
 public void afterGroupCreateOrUpdate(com.idega.user.data.Group p0)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void afterUserCreateOrUpdate(com.idega.user.data.User p0)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void beforeGroupRemove(com.idega.user.data.Group p0)throws javax.ejb.RemoveException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void beforeUserRemove(com.idega.user.data.User p0)throws javax.ejb.RemoveException,java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean createSpecialConnection(java.lang.String p0,int p1,java.lang.String p2,com.idega.presentation.IWContext p3) throws java.rmi.RemoteException;
 public java.util.Collection findGroupsByFields(java.util.Collection p0,java.util.Collection p1,java.util.Collection p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.List getGroupPropertiesTabs(com.idega.user.data.Group p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection getListViewerFields()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.Class getPresentationObjectClass()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.List getUserPropertiesTabs(com.idega.user.data.User p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.presentation.PresentationObject instanciateEditor(com.idega.user.data.Group p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.presentation.PresentationObject instanciateViewer(com.idega.user.data.Group p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String isUserAssignableFromGroupToGroup(com.idega.user.data.User p0,com.idega.user.data.Group p1,com.idega.user.data.Group p2);
 public java.lang.String isUserSuitedForGroup(com.idega.user.data.User p0,com.idega.user.data.Group p1);
 public boolean updateConnectedToSpecial(com.idega.user.data.Group p0,com.idega.presentation.IWContext p1) throws java.rmi.RemoteException;
}
