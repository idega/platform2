/*
 * Created on Aug 17, 2004
 *
 */
package is.idega.idegaweb.marathon.business;

import is.idega.idegaweb.marathon.presentation.UserRunTab;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.ejb.Handle;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOService;
import com.idega.business.IBOServiceBean;
import com.idega.core.accesscontrol.business.AccessController;
import com.idega.data.IDOHome;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWUserContext;
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
	public void afterGroupCreate(Group group) throws CreateException, RemoteException {
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterUserCreate(com.idega.user.data.User)
	 */
	public void afterUserCreate(User user) throws CreateException, RemoteException {
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
	 * @see com.idega.user.business.UserGroupPlugInBusiness#findGroupsByFields(java.util.Collection, java.util.Collection, java.util.Collection)
	 */
	public Collection findGroupsByFields(Collection listViewerFields, Collection finderOperators,
			Collection listViewerFieldValues) throws RemoteException {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupPropertiesTabs(com.idega.user.data.Group)
	 */
	public List getGroupPropertiesTabs(Group group) throws RemoteException {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getListViewerFields()
	 */
	public Collection getListViewerFields() throws RemoteException {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getPresentationObjectClass()
	 */
	public Class getPresentationObjectClass() throws RemoteException {
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
}
