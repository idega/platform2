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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;

import com.idega.business.IBOLookupException;
import com.idega.business.IBOServiceBean;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;

/**
 * @author palli
 * 
 * To change this generated comment go to Window>Preferences>Java>Code
 * Generation>Code and Comments
 */
public class ClubInformationPluginBusinessBean extends IBOServiceBean implements
        ClubInformationPluginBusiness, UserGroupPlugInBusiness {

    /*
     * (non-Javadoc)
     * 
     * @see com.idega.user.business.UserGroupPlugInBusiness#beforeUserRemove(com.idega.user.data.User)
     */
    public void beforeUserRemove(User user) throws RemoveException,
            RemoteException {

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.idega.user.business.UserGroupPlugInBusiness#afterUserCreate(com.idega.user.data.User)
     */
    public void afterUserCreate(User user) throws CreateException,
            RemoteException {

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.idega.user.business.UserGroupPlugInBusiness#beforeGroupRemove(com.idega.user.data.Group)
     */
    public void beforeGroupRemove(Group group) throws RemoveException,
            RemoteException {

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.idega.user.business.UserGroupPlugInBusiness#afterGroupCreate(com.idega.user.data.Group)
     */
    public void afterGroupCreate(Group group) throws CreateException,
            RemoteException {

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.idega.user.business.UserGroupPlugInBusiness#getPresentationObjectClass()
     */
    public Class getPresentationObjectClass() throws RemoteException {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateEditor(com.idega.user.data.Group)
     */
    public PresentationObject instanciateEditor(Group group)
            throws RemoteException {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateViewer(com.idega.user.data.Group)
     */
    public PresentationObject instanciateViewer(Group group)
            throws RemoteException {
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
        list.add(new ClubInformationTab(group));

        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.idega.user.business.UserGroupPlugInBusiness#getListViewerFields()
     */
    public Collection getListViewerFields() throws RemoteException {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.idega.user.business.UserGroupPlugInBusiness#findGroupsByFields(java.util.Collection,
     *      java.util.Collection, java.util.Collection)
     */
    public Collection findGroupsByFields(Collection listViewerFields,
            Collection finderOperators, Collection listViewerFieldValues)
            throws RemoteException {
        return null;
    }

    public String isUserAssignableFromGroupToGroup(User user,
            Group sourceGroup, Group targetGroup) {
        return null;
    }

    public String isUserSuitedForGroup(User user, Group targetGroup) {
        return null;
    }

    /**
     * A method to create a connection between a club and a league. Creates a
     * copy of the groups under the league template in under the club and
     * aliases to these groups under the league.
     * 
     * @param connection
     *            The primary key id of the league that the club/division is
     *            being connected to.
     * @param parentGroupId
     *            The primary key id of the club/division being connected to
     *            the league.
     * @param clubName
     *            The name of the club.
     * @param iwc
     *            The idegaWeb context object.
     * 
     * @return Returns true if the groups were created normally, false
     *         otherwise.
     */
    public boolean createSpecialConnection(String connection,
            int parentGroupId, String clubName, IWContext iwc) {

        //Are we connecting to a league.
        if (connection == null || connection.equals("")) { return false; }

        try {
            //Get the group that is connecting to the league.
            Group parentGroup = (Group) (((GroupHome) com.idega.data.IDOLookup
                    .getHome(Group.class)).findByPrimaryKey(new Integer(
                    parentGroupId)));
            //Get the league
            Group specialGroup = (Group) (((GroupHome) com.idega.data.IDOLookup
                    .getHome(Group.class)).findByPrimaryKey(new Integer(
                    connection)));

            /*
             * Going through the child groups of the league group
             * (specialGroup) and trying to find the groups there that are the
             * CLUB_DIVISION_TEMPLATE group and the LEAGUE_CLUB_DIVISION group.
             */
            Group child = null;
            Group clubDivisionGroup = null;
            Group clubDivisionTemplateGroup = null;
            boolean foundIt = false;
            boolean foundClubDivisionGroup = false;
            List children = specialGroup.getChildGroups();
            Iterator it = children.iterator();
            //Do this while there are still children under the specialGroup
            // and I haven't found the two groups I'm looking for.
            while (it.hasNext() && !(foundIt && foundClubDivisionGroup)) {
                child = (Group) it.next();
                if (child.getGroupType().equals(
                        IWMemberConstants.GROUP_TYPE_CLUB_DIVISION_TEMPLATE)) {
                    clubDivisionTemplateGroup = child;
                    foundIt = true;
                } else if (child.getGroupType().equals(
                        IWMemberConstants.GROUP_TYPE_LEAGUE_CLUB_DIVISION)) {
                    clubDivisionGroup = child;
                    foundClubDivisionGroup = true;
                }
            }

            //If we don't find the group to store the aliases then we'll just
            // store them directly under the league group.
            if (clubDivisionGroup == null) {
                clubDivisionGroup = specialGroup;
            }

            if (foundIt && clubDivisionTemplateGroup != null) {
                Group topNode = parentGroup;

                //If it's the club creating the connection we have to create a
                // group to put the copies under.
                if (parentGroup.getGroupType().equals(
                        IWMemberConstants.GROUP_TYPE_CLUB)) {
                    topNode = getGroupBusiness().createGroupUnder("Flokkar", "",
                            IWMemberConstants.GROUP_TYPE_CLUB_DIVISION,
                            parentGroup);
                    getGroupBusiness()
                            .applyOwnerAndAllGroupPermissionsToNewlyCreatedGroupForUserAndHisPrimaryGroup(
                                    iwc, topNode, iwc.getCurrentUser());
                }

                //Insert a copy of all the template groups under the
                // club/division and aliases under the league.
                insertCopyOfChild(topNode, clubDivisionTemplateGroup,
                        clubDivisionGroup, clubName, iwc);

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
     * A method that inserts copies of the leagues template groups under the
     * club/division. And also creates aliases back to the league from these
     * groups.
     * 
     * @param parent 
     * 			The group to create the copies under. 
     * @param templateParent 
     * 			The parent group of the template groups. These groups are copied. 
     * @param special 
     * 			The group to store the aliases under. 
     * @param clubName 
     * 			The name of the club. @param iwc The idegaWeb context object.
     */
    private void insertCopyOfChild(Group parent, Group templateParent,
            Group special, String clubName, IWContext iwc) {
        try {
            //Get all groups under the template parent group and iterate through them.
            List child = templateParent.getChildGroups();
            Collection templateOwners = getGroupBusiness().getOwnerUsersForGroup(templateParent);
            Iterator it = child.iterator();
            while (it.hasNext()) {
                //Get the group
                Group playerGroup = (Group) it.next();
                //If the groups type is CLUB_PLAYER_TEMPLATE then create a copy of it under the club/division.
                if (playerGroup.getGroupType().equals(
                        IWMemberConstants.GROUP_TYPE_CLUB_PLAYER_TEMPLATE)) {
                    //Create a copy of the player group under the club/division.
                    Group newGroup = getGroupBusiness().createGroupUnder(playerGroup.getName(), "", IWMemberConstants.GROUP_TYPE_CLUB_PLAYER, parent);
                    //This is a hack to store the connection between the copy and the original. Should maybe be replaced with some metadata.
                    newGroup.setAlias(playerGroup);

                    //Copy the metadata
                    java.util.Map t = playerGroup.getMetaDataAttributes();
                    if (t != null) {
                        newGroup.setMetaDataAttributes(t);
                    }

                    newGroup.store();
                    
                    //Setting the correct access controls for the group. Set the owner of the template as the owner of the group, the give the current user all permissions for the group.
                    if (templateOwners != null && !templateOwners.isEmpty()) {
                        Iterator owners = templateOwners.iterator();
                        while (owners.hasNext()) {
                            User owner = (User) owners.next();
                            getGroupBusiness().applyOwnerAndAllGroupPermissionsToNewlyCreatedGroupForUserAndHisPrimaryGroup(iwc, newGroup, owner);
                        }
                    }
                    getGroupBusiness().applyAllGroupPermissionsForGroupToUsersPrimaryGroup(iwc, newGroup, iwc.getCurrentUser());

                    //Try to update the connection to the league. If it does
                    // not exist, create a new connection.
                    if (!updateSpecial(special, playerGroup, newGroup,
                            clubName, iwc, templateOwners)) {
                        //Create a new group under the league_club_division
                        // group that links to the playerGroup in the league.
                        Group newSpecialPlayerGroup = getGroupBusiness().createGroupUnder(playerGroup.getName(), "", IWMemberConstants.GROUP_TYPE_CLUB_PLAYER, special);
                        newSpecialPlayerGroup.setAlias(playerGroup);
                        //This is a hack to store the connection between the copy and the original. Should maybe be replaced with some metadata.
                        newSpecialPlayerGroup.store();
                        //Setting the correct access controls for the group. Set the owner of the template as the owner of the group.
                        if (templateOwners != null && !templateOwners.isEmpty()) {
                            Iterator owners = templateOwners.iterator();
                            while (owners.hasNext()) {
                                User owner = (User) owners.next();
                                getGroupBusiness().applyOwnerAndAllGroupPermissionsToNewlyCreatedGroupForUserAndHisPrimaryGroup(iwc, newSpecialPlayerGroup, owner);
                            }
                        }

                        //Create a link to the actual group in the club, with
                        // a new name and put it under the group created above.
                        String name = newGroup.getName();
                        if (clubName != null) {
                            name += " (" + clubName + ")";
                        }
                        Group newSpecialPlayerAliasGroup = getGroupBusiness().createGroupUnder(name, "", IWMemberConstants.GROUP_TYPE_ALIAS, newSpecialPlayerGroup);
                        newSpecialPlayerAliasGroup.setAlias(newGroup);
                        newSpecialPlayerAliasGroup.store();

                        if (templateOwners != null && !templateOwners.isEmpty()) {
                            Iterator owners = templateOwners.iterator();
                            while (owners.hasNext()) {
                                User owner = (User) owners.next();
                                getGroupBusiness().applyOwnerAndAllGroupPermissionsToNewlyCreatedGroupForUserAndHisPrimaryGroup(iwc, newSpecialPlayerAliasGroup, owner);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * A method that adds a new alias connection between a club/division and a league, if some connection already exists in the 
     * league.
     * 
     * @param special 
     * 		A Group object representing the league.
     * @param playerGroup 
     * 		A Group object representing the template group being copied.
     * @param newGroup 
     * 		The copy of the template group.
     * @param clubName 
     * 		The name of the club.
     * @param iwc
     * 		The idegaWeb context object.
     * @param owners
     * 		A Collection representing the Users that are supposed to be the owners of the created aliases.
     * 
     * @return Returns true if the connection already exists and then the alias is created. False otherwise. 
     */
    private boolean updateSpecial(Group special, Group playerGroup,
            Group newGroup, String clubName, IWContext iwc, Collection owners) {
        try {
            //Get all groups under the template parent group and iterate through them.
            List childs = special.getChildGroups();
            Iterator it = childs.iterator();
            while (it.hasNext()) {
                Group child = (Group) it.next();
                if (child.getGroupType().equals(
                        IWMemberConstants.GROUP_TYPE_CLUB_PLAYER)) {
                    if (child.getAliasID() == ((Integer) playerGroup
                            .getPrimaryKey()).intValue()) {
                        String name = newGroup.getName();
                        if (clubName != null) {
                            name += " (" + clubName + ")";
                        }
                        Group newSpecialPlayerAliasGroup = getGroupBusiness().createGroupUnder(name, "", IWMemberConstants.GROUP_TYPE_ALIAS, child); 
                        newSpecialPlayerAliasGroup.setAlias(newGroup);
                        newSpecialPlayerAliasGroup.store();

                        if (owners != null && !owners.isEmpty()) {
                            Iterator o = owners.iterator();
                            while (o.hasNext()) {
                                User owner = (User) o.next();
                                getGroupBusiness().applyOwnerAndAllGroupPermissionsToNewlyCreatedGroupForUserAndHisPrimaryGroup(iwc, newSpecialPlayerAliasGroup, owner);
                            }
                        }

                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * A method to update the player groups created from the templates in a league.
     * 
     * @param special
     * 		The group under the league tree that the update method was executed from.
     * @param iwc
     * 		The idegaWeb context object.
     * 
     * @return True if all the groups are updated, false otherwise.
     */
    public boolean updateConnectedToSpecial(Group special, IWContext iwc) {
        if (special.getGroupType().equals(IWMemberConstants.GROUP_TYPE_LEAGUE)) {
            Group child = null;
            boolean foundIt = false;
            List children = special.getChildGroups();
            Iterator it = children.iterator();
            while (it.hasNext()) {
                child = (Group) it.next();
                if (child.getGroupType().equals(
                        IWMemberConstants.GROUP_TYPE_CLUB_DIVISION_TEMPLATE)) {
                    foundIt = true;
                    break;
                }
            }

            if (foundIt && child != null) {
                special = child;
            }
        }

        if (special.getGroupType().equals(
                IWMemberConstants.GROUP_TYPE_CLUB_DIVISION_TEMPLATE)) {
            List children = special.getChildGroups();
            Iterator it = children.iterator();
            while (it.hasNext()) {
                Group child = (Group) it.next();
                if (child.getGroupType().equals(
                        IWMemberConstants.GROUP_TYPE_CLUB_PLAYER_TEMPLATE)) {
                    updatePlayerGroupsConnectedTo(child, iwc);                 
                }
            }

            return true;
        }

        return false;
    }

    private void updatePlayerGroupsConnectedTo(Group parent, IWContext iwc) {
        Collection connected = null;
        try {
            connected = ((GroupHome) com.idega.data.IDOLookup
                    .getHome(Group.class))
                    .findGroupsByType(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER);
            if (connected != null) {
                Map metadata = parent.getMetaDataAttributes();
                int parent_id = ((Integer) parent.getPrimaryKey()).intValue();
                Iterator it = connected.iterator();
                while (it.hasNext()) {
                    Group conn = (Group) it.next();
                    if (conn.getAliasID() == parent_id) {
                        conn.setMetaDataAttributes(metadata);
                        conn.store();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (parent.getChildCount() > 0) {
            List children = parent.getChildGroups();
            Iterator it = children.iterator();
            while (it.hasNext()) {
                Group child = (Group) it.next();
                if (child.getGroupType().equals(
                        IWMemberConstants.GROUP_TYPE_CLUB_PLAYER_TEMPLATE))
                        updatePlayerGroupsConnectedTo(child, iwc);
            }
        }
    }

    /*
     * Get the GroupBusiness.
     */
    private GroupBusiness getGroupBusiness() {
        GroupBusiness business = null;
        try {
            business = (GroupBusiness) getServiceInstance(GroupBusiness.class);
        } catch (IBOLookupException e) {
            e.printStackTrace();
        }
        return business;
    }
}