/*
 * Created on Mar 11, 2003
 * 
 * To change this generated comment go to Window>Preferences>Java>Code
 * Generation>Code and Comments
 */
package is.idega.idegaweb.member.business.plugins;

import is.idega.idegaweb.member.presentation.ClubPlayerTab;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import com.idega.business.IBOServiceBean;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * @author palli
 * 
 * To change this generated comment go to Window>Preferences>Java>Code
 * Generation>Code and Comments
 */
public class FlokkarPluginBusinessBean extends IBOServiceBean implements FlokkarPluginBusiness, UserGroupPlugInBusiness {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeUserRemove(com.idega.user.data.User)
	 */
	public void beforeUserRemove(User user) throws RemoveException, RemoteException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterUserCreate(com.idega.user.data.User)
	 */
	public void afterUserCreateOrUpdate(User user) throws CreateException, RemoteException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeGroupRemove(com.idega.user.data.Group)
	 */
	public void beforeGroupRemove(Group group) throws RemoveException, RemoteException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterGroupCreate(com.idega.user.data.Group)
	 */
	public void afterGroupCreateOrUpdate(Group group) throws CreateException, RemoteException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateEditor(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateEditor(Group group) throws RemoteException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateViewer(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateViewer(Group group) throws RemoteException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getUserPropertiesTabs(com.idega.user.data.User)
	 */
	public List getUserPropertiesTabs(User user) throws RemoteException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupPropertiesTabs(com.idega.user.data.Group)
	 */
	public List getGroupPropertiesTabs(Group group) throws RemoteException {
		List list = new ArrayList();
		list.add(new ClubPlayerTab(group));
		return list;
	}

	public String isUserAssignableFromGroupToGroup(User user, Group sourceGroup, Group targetGroup) {
		return null;
	}

	public String isUserSuitedForGroup(User user, Group targetGroup) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getMainToolbarElements()
	 */
	public List getMainToolbarElements() throws RemoteException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupToolbarElements(com.idega.user.data.Group)
	 */
	public List getGroupToolbarElements(Group group) throws RemoteException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#canCreateSubGroup(com.idega.user.data.Group,java.lang.String)
	 */
	public String canCreateSubGroup(Group group, String groupTypeOfSubGroup) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
}