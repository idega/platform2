package is.idega.idegaweb.marathon.business;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;

import com.idega.presentation.PresentationObject;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;


public interface RunPluginBusiness extends com.idega.business.IBOService, UserGroupPlugInBusiness {
	public void afterGroupCreate(Group group) throws CreateException, RemoteException;
	public void afterUserCreate(User user) throws CreateException, RemoteException;
	public void beforeGroupRemove(Group group) throws RemoveException, RemoteException;
	public void beforeUserRemove(User user) throws RemoveException, RemoteException;
	public Collection findGroupsByFields(Collection listViewerFields, Collection finderOperators,
			Collection listViewerFieldValues) throws RemoteException;
	public List getGroupPropertiesTabs(Group group) throws RemoteException;
	public Collection getListViewerFields() throws RemoteException;
	public Class getPresentationObjectClass() throws RemoteException;
	public List getUserPropertiesTabs(User user) throws RemoteException;
	public PresentationObject instanciateEditor(Group group) throws RemoteException;
	public PresentationObject instanciateViewer(Group group) throws RemoteException;
	public String isUserAssignableFromGroupToGroup(User user, Group sourceGroup, Group targetGroup);
	public String isUserSuitedForGroup(User user, Group targetGroup);
}
