package is.idega.idegaweb.member.business.plugins;


public interface GroupBoardPluginBusiness extends com.idega.business.IBOService,com.idega.user.business.UserGroupPlugInBusiness
{
 public void afterGroupCreateOrUpdate(com.idega.user.data.Group p0)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void afterUserCreateOrUpdate(com.idega.user.data.User p0)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void beforeGroupRemove(com.idega.user.data.Group p0)throws javax.ejb.RemoveException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void beforeUserRemove(com.idega.user.data.User p0)throws javax.ejb.RemoveException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findGroupsByFields(java.util.Collection p0,java.util.Collection p1,java.util.Collection p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.List getGroupPropertiesTabs(com.idega.user.data.Group p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection getListViewerFields()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.Class getPresentationObjectClass()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.List getUserPropertiesTabs(com.idega.user.data.User p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.presentation.PresentationObject instanciateEditor(com.idega.user.data.Group p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.presentation.PresentationObject instanciateViewer(com.idega.user.data.Group p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
}
