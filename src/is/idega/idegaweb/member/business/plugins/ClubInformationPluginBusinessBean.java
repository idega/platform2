/*
 * Created on Mar 11, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package is.idega.idegaweb.member.business.plugins;

import is.idega.idegaweb.member.presentation.ClubInformationTab;
import is.idega.idegaweb.member.util.IWMemberConstants;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;

import com.idega.business.IBOServiceBean;
import com.idega.core.accesscontrol.business.AccessController;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;

/**
 * @author palli
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ClubInformationPluginBusinessBean extends IBOServiceBean implements ClubInformationPluginBusiness, UserGroupPlugInBusiness {

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeUserRemove(com.idega.user.data.User)
	 */
	public void beforeUserRemove(User user) throws RemoveException, RemoteException {

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
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterGroupCreate(com.idega.user.data.Group)
	 */
	public void afterGroupCreate(Group group) throws CreateException, RemoteException {

	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getPresentationObjectClass()
	 */
	public Class getPresentationObjectClass() throws RemoteException {
		return null;
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
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getUserPropertiesTabs(com.idega.user.data.User)
	 */
	public List getUserPropertiesTabs(User user) throws RemoteException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupPropertiesTabs(com.idega.user.data.Group)
	 */
	public List getGroupPropertiesTabs(Group group) throws RemoteException {
		List list = new ArrayList();
		list.add(new ClubInformationTab(group));

		return list;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getListViewerFields()
	 */
	public Collection getListViewerFields() throws RemoteException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#findGroupsByFields(java.util.Collection, java.util.Collection, java.util.Collection)
	 */
	public Collection findGroupsByFields(Collection listViewerFields, Collection finderOperators, Collection listViewerFieldValues) throws RemoteException {
		return null;
	}

	public String isUserAssignableFromGroupToGroup(User user, Group sourceGroup, Group targetGroup) {
		return null;
	}

	public String isUserSuitedForGroup(User user, Group targetGroup) {
		return null;
	}

  public boolean createSpecialConnection(String connection, int parentGroupId, String clubName, IWContext iwc) {
		if (connection == null || connection.equals(""))
			return false;

		try {
			Group group = (Group) (((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(parentGroupId)));
			Group specialGroup = (Group) (((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(connection)));

			Group child = null;
			boolean foundIt = false;
			List children = specialGroup.getChildGroups();
			Iterator it = children.iterator();
			while (it.hasNext()) {
				child = (Group) it.next();
				if (child.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION_TEMPLATE)) {
					foundIt = true;
					break;
				}
			}

			if (foundIt && child != null) {
				Group newGroup = (Group) ((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).create();
				newGroup.setGroupType(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION);
				newGroup.setName(child.getName());
				newGroup.store();
				setCurrentUsersPrimaryGroupPermissionsForGroup(iwc,newGroup);
				
				

				group.addGroup(newGroup);

				insertCopyOfChild(newGroup, child, specialGroup, clubName,iwc);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean createSpecialConnectionDivision(String connection, int parentGroupId, String clubName, IWContext iwc) {
		if (connection == null || connection.equals(""))
			return false;

		try {
			Group group = (Group) (((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(parentGroupId)));
			Group specialGroup = (Group) (((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(connection)));

			Group child = null;
			boolean foundIt = false;
			List children = specialGroup.getChildGroups();
			Iterator it = children.iterator();
			while (it.hasNext()) {
				child = (Group) it.next();
				if (child.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION_TEMPLATE)) {
					foundIt = true;
					break;
				}
			}

			if (foundIt && child != null) {
				insertCopyOfChild(group, child, specialGroup, clubName,iwc);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	private void insertCopyOfChild(Group parent, Group templateParent, Group special, String clubName , IWContext iwc) {
		try {
			List child = templateParent.getChildGroups();
			Iterator it = child.iterator();
			while (it.hasNext()) {
				Group playerGroup = (Group) it.next();
				if (playerGroup.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER_TEMPLATE)) {
					Group newGroup = (Group) ((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).create();
					newGroup.setGroupType(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER);
					newGroup.setName(playerGroup.getName());
					newGroup.setAlias(playerGroup);

					java.util.Hashtable t = playerGroup.getMetaDataAttributes();
					if (t != null)
						newGroup.setMetaDataAttributes(t);

					newGroup.store();
					setCurrentUsersPrimaryGroupPermissionsForGroup(iwc,newGroup);

					parent.addGroup(newGroup);

					if (!updateSpecial(special, playerGroup, newGroup, clubName,iwc)) {
						Group newSpecialPlayerGroup = (Group) ((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).create();
						newSpecialPlayerGroup.setGroupType(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER);
						newSpecialPlayerGroup.setName(playerGroup.getName());
						newSpecialPlayerGroup.setAlias(playerGroup);
						newSpecialPlayerGroup.store();
						setCurrentUsersPrimaryGroupPermissionsForGroup(iwc,newSpecialPlayerGroup);

						special.addGroup(newSpecialPlayerGroup);

						Group newSpecialPlayerAliasGroup = (Group) ((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).create();
						newSpecialPlayerAliasGroup.setGroupType(IWMemberConstants.GROUP_TYPE_ALIAS);
						newSpecialPlayerAliasGroup.setAlias(newGroup);
						String name = newGroup.getName();
						if (clubName != null)
							name += " (" + clubName + ")";
						newSpecialPlayerAliasGroup.setName(name);
						newSpecialPlayerAliasGroup.store();
						setCurrentUsersPrimaryGroupPermissionsForGroup(iwc,newSpecialPlayerAliasGroup);
						

						newSpecialPlayerGroup.addGroup(newSpecialPlayerAliasGroup);
					}

					if (playerGroup.getChildCount() > 0)
						insertCopyOfChild(newGroup, playerGroup, special, clubName,iwc);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean updateSpecial(Group special, Group playerGroup, Group newGroup, String clubName, IWContext iwc) {
		try {
			List childs = special.getChildGroups();
			Iterator it = childs.iterator();
			while (it.hasNext()) {
				Group child = (Group) it.next();
				if (child.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER)) {
					if (child.getAliasID() == ((Integer) playerGroup.getPrimaryKey()).intValue()) {
						Group newSpecialPlayerAliasGroup = (Group) ((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).create();
						newSpecialPlayerAliasGroup.setGroupType(IWMemberConstants.GROUP_TYPE_ALIAS);
						newSpecialPlayerAliasGroup.setAlias(newGroup);
						String name = newGroup.getName();
						if (clubName != null)
							name = clubName;
						newSpecialPlayerAliasGroup.setName(name);
						newSpecialPlayerAliasGroup.store();
						setCurrentUsersPrimaryGroupPermissionsForGroup(iwc,newSpecialPlayerAliasGroup);
						child.addGroup(newSpecialPlayerAliasGroup);

						return true;
					}

					if (child.getChildCount() > 0) {
						if (updateSpecial(child, playerGroup, newGroup, clubName,iwc))
							return true;
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	private void setCurrentUsersPrimaryGroupPermissionsForGroup(IWUserContext iwc, Group group) {
		User user = iwc.getCurrentUser();
		AccessController access = iwc.getAccessController();
		try {
			Group primary = user.getPrimaryGroup();
			String primaryGroupId = primary.getPrimaryKey().toString();
			String newGroupId = group.getPrimaryKey().toString();
			//TDOD create methods for this in accesscontrol
			//create permission
			access.setPermission(AccessController.CATEGORY_GROUP_ID, iwc, primaryGroupId, newGroupId, access.PERMISSION_KEY_CREATE, Boolean.TRUE);
			//edit permission
			access.setPermission(AccessController.CATEGORY_GROUP_ID, iwc, primaryGroupId, newGroupId, access.PERMISSION_KEY_EDIT, Boolean.TRUE);
			//delete permission
			access.setPermission(AccessController.CATEGORY_GROUP_ID, iwc, primaryGroupId, newGroupId, access.PERMISSION_KEY_DELETE, Boolean.TRUE);
			//view permission
			access.setPermission(AccessController.CATEGORY_GROUP_ID, iwc, primaryGroupId, newGroupId, access.PERMISSION_KEY_VIEW, Boolean.TRUE);

		}
		catch (Exception ex) {
			ex.printStackTrace();

		}
	}

	public boolean updateConnectedToSpecial(Group special, IWContext iwc) {
		if (special.getGroupType().equals(IWMemberConstants.GROUP_TYPE_LEAGUE)) {
			Group child = null;
			boolean foundIt = false;
			List children = special.getChildGroups();
			Iterator it = children.iterator();
			while (it.hasNext()) {
				child = (Group) it.next();
				if (child.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION_TEMPLATE)) {
					foundIt = true;
					break;
				}
			}

			if (foundIt && child != null)
				special = child;
		}

		if (special.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION_TEMPLATE)) {
			List children = special.getChildGroups();
			Iterator it = children.iterator();
			while (it.hasNext()) {
				Group child = (Group) it.next();
				if (child.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER_TEMPLATE))
					updatePlayerGroupsConnectedTo(child,iwc);
			}

			return true;
		}

		return false;
	}

	private void updatePlayerGroupsConnectedTo(Group parent, IWContext iwc) {
		Collection connected = null;
		try {
			connected =((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findGroupsByType(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER);
			if (connected != null) {
				Hashtable metadata = parent.getMetaDataAttributes();
				int parent_id = ((Integer)parent.getPrimaryKey()).intValue();
				Iterator it = connected.iterator();
				while (it.hasNext()) {
					Group conn = (Group)it.next();
					if (conn.getAliasID() == parent_id) {
						conn.setMetaDataAttributes(metadata);
						conn.store();
						setCurrentUsersPrimaryGroupPermissionsForGroup(iwc,conn);
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		if (parent.getChildCount() > 0) {
			List children = parent.getChildGroups();
			Iterator it = children.iterator();
			while (it.hasNext()) {
				Group child = (Group) it.next();
				if (child.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER_TEMPLATE))
					updatePlayerGroupsConnectedTo(child,iwc);
			}
		}
	}
}