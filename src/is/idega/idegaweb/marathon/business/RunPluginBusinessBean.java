/*
 * Created on Aug 17, 2004
 *
 */
package is.idega.idegaweb.marathon.business;

import is.idega.idegaweb.marathon.presentation.CreateYearWindowPlugin;
import is.idega.idegaweb.marathon.presentation.UserRunTab;
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
 * @author birna
 *
 */
public class RunPluginBusinessBean extends IBOServiceBean implements RunPluginBusiness, UserGroupPlugInBusiness {

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterGroupCreate(com.idega.user.data.Group)
	 */
	public void afterGroupCreateOrUpdate(Group group) throws CreateException, RemoteException {
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterUserCreate(com.idega.user.data.User)
	 */
	public void afterUserCreateOrUpdate(User user) throws CreateException, RemoteException {
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeGroupRemove(com.idega.user.data.Group)
	 */
	public void beforeGroupRemove(Group group) throws RemoveException, RemoteException {
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeUserRemove(com.idega.user.data.User)
	 */
	public void beforeUserRemove(User user) throws RemoveException, RemoteException {
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupPropertiesTabs(com.idega.user.data.Group)
	 */
	public List getGroupPropertiesTabs(Group group) throws RemoteException {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getUserPropertiesTabs(com.idega.user.data.User)
	 */
	public List getUserPropertiesTabs(User user) throws RemoteException {
		List list = new ArrayList();
		list.add(new UserRunTab());
		return list;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateEditor(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateEditor(Group group) throws RemoteException {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateViewer(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateViewer(Group group) throws RemoteException {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#isUserAssignableFromGroupToGroup(com.idega.user.data.User, com.idega.user.data.Group, com.idega.user.data.Group)
	 */
	public String isUserAssignableFromGroupToGroup(User user, Group sourceGroup, Group targetGroup) {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#isUserSuitedForGroup(com.idega.user.data.User, com.idega.user.data.Group)
	 */
	public String isUserSuitedForGroup(User user, Group targetGroup) {
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
		if ("iwma_run".equals(group.getGroupType())) {
			List list = new ArrayList(1);
			list.add(new CreateYearWindowPlugin());
			return list;
		}
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
