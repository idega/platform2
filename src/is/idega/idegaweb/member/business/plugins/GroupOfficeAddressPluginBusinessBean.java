package is.idega.idegaweb.member.business.plugins;

import is.idega.idegaweb.member.presentation.GroupOfficeAddressTab;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;

import com.idega.business.IBOServiceBean;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 *@author     <a href="mailto:thomas@idega.is">Thomas Hilbig</a>
 *@version    1.0
 */
public class GroupOfficeAddressPluginBusinessBean extends IBOServiceBean implements GroupOfficeAddressPluginBusiness, UserGroupPlugInBusiness {
	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterGroupCreateOrUpdate(com.idega.user.data.Group)
	 */
	public void afterGroupCreateOrUpdate(Group group) throws CreateException, RemoteException {
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterUserCreateOrUpdate(com.idega.user.data.User)
	 */
	public void afterUserCreateOrUpdate(User user) throws CreateException, RemoteException {
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeGroupRemove(com.idega.user.data.Group)
	 */
	public void beforeGroupRemove(Group group) throws RemoveException, RemoteException {
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeUserRemove(com.idega.user.data.User)
	 */
	public void beforeUserRemove(User user) throws RemoveException, RemoteException {
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#findGroupsByFields(java.util.Collection, java.util.Collection, java.util.Collection)
	 */
	public Collection findGroupsByFields(Collection listViewerFields, Collection finderOperators, Collection listViewerFieldValues) throws RemoteException {
		return null;
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupPropertiesTabs(com.idega.user.data.Group)
	 */
	public List getGroupPropertiesTabs(Group group) throws RemoteException {
    List list = new ArrayList();
    list.add(new GroupOfficeAddressTab(group));  
    return list;  
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getListViewerFields()
	 */
	public Collection getListViewerFields() throws RemoteException {
		return null;
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getPresentationObjectClass()
	 */
	public Class getPresentationObjectClass() throws RemoteException {
		return null;
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getUserPropertiesTabs(com.idega.user.data.User)
	 */
	public List getUserPropertiesTabs(User user) throws RemoteException {
		return null;
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateEditor(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateEditor(Group group) throws RemoteException {
		return null;
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateViewer(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateViewer(Group group) throws RemoteException {
		return null;
	}
  public String isUserAssignableFromGroupToGroup(User user, Group sourceGroup, Group targetGroup) {
    return null;
  }
  
  public String isUserSuitedForGroup(User user, Group targetGroup)  {
    return null;
  }

/* (non-Javadoc)
 * @see com.idega.user.business.UserGroupPlugInBusiness#getMainToolbarElements()
 */
public List getMainToolbarElements() throws RemoteException {
	return null;
}

/* (non-Javadoc)
 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupToolbarElements(com.idega.user.data.Group)
 */
public List getGroupToolbarElements(Group group) throws RemoteException {
	return null;
}

}
