/*
 * $Id: SpecialConnectionUpdateThread.java,v 1.5 2005/06/21 22:49:57 palli Exp $
 * Created on Jan 4, 2005
 * 
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package is.idega.idegaweb.member.business.plugins;

import is.idega.idegaweb.member.util.IWMemberConstants;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

// import com.idega.util.IWTimestamp;

/**
 * 
 * Last modified: $Date: 2005/06/21 22:49:57 $ by $Author: palli $
 * 
 * @author <a href="mailto:palli@idega.com">palli </a>
 * @version $Revision: 1.5 $
 */
public class SpecialConnectionUpdateThread extends Thread {

	private Group special = null;

	private IWApplicationContext iwac = null;

	/**
	 * 
	 */
	public SpecialConnectionUpdateThread(Group special, IWApplicationContext iwac) {
		this.special = special;
		this.iwac = iwac;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		System.out.println("Starting update thread : " + IWTimestamp.getTimestampRightNow());
		Group league = null;
		Group template = null;
		if (special.getGroupType().equals(IWMemberConstants.GROUP_TYPE_LEAGUE)) {
			league = special;
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
			if (foundIt && child != null) {
				template = child;
			}
		}
		else if (special.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION_TEMPLATE)) {
			template = special;
			league = findLeagueForTemplate(template);
		}

		if (league != null && template != null) {
			updateOrAddMissingGroupsInClubs(league, template);
		}
		
		System.out.println("Update thread done : " + IWTimestamp.getTimestampRightNow());
	}

	private Group findLeagueForTemplate(Group template) {
		if (template == null) {
			return null;
		}

		Collection parents = template.getParentGroups();
		if (parents != null && !parents.isEmpty()) {
			Group parent = null;
			Iterator it = parents.iterator();
			while (it.hasNext()) {
				parent = (Group) it.next();
				if (parent.getGroupType().equals(IWMemberConstants.GROUP_TYPE_LEAGUE)) {
					return parent;
				}
			}
		}
		return null;
	}

	/*
	 * 1. Find all groups conntected to the league 2. Find the club/division 3.
	 * Go through all division children and match to the player group templates
	 * 4. If any are missing, create them 5. If any aliases are missing to
	 * groups, then create them
	 */
	private void updateOrAddMissingGroupsInClubs(Group league, Group divisionTemplate) {
		try {
			// Find all clubs connected to league. These just have one division.
			Collection clubs = ((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findGroupsByMetaData(
					IWMemberConstants.META_DATA_CLUB_LEAGUE_CONNECTION, ((Integer) league.getPrimaryKey()).toString());
			// Find all the division in the other clubs, that are connected to
			// the league.
			Collection divisions = ((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findGroupsByMetaData(
					IWMemberConstants.META_DATA_DIVISION_LEAGUE_CONNECTION,
					((Integer) league.getPrimaryKey()).toString());
			// Find the league club division group. If it's null, just use the
			// league group.
			Group leaguePlayerGroup = null;
			Collection leagueGroups = league.getChildren();
			if (leagueGroups != null && !leagueGroups.isEmpty()) {
				Iterator it = leagueGroups.iterator();
				while (it.hasNext() && leaguePlayerGroup == null) {
					Group leagueGroup = (Group) it.next();
					if (leagueGroup.getGroupType().equals(IWMemberConstants.GROUP_TYPE_LEAGUE_CLUB_DIVISION)) {
						leaguePlayerGroup = leagueGroup;
					}
				}
			}
			if (leaguePlayerGroup == null) {
				leaguePlayerGroup = league;
			}
			// Go through all the clubs. Find the division in them, and compare
			// it to the division template from the league.
			if (clubs != null && !clubs.isEmpty()) {
				Iterator it = clubs.iterator();
				while (it.hasNext()) {
					Group club = (Group) it.next();
					Group division = findDivisionForClub(club);
					if (division != null) {
						updateDivision(division, divisionTemplate, club, leaguePlayerGroup);
					}
				}
			}
			// Go through the divisions. Find the club they belong to and then
			// compare them to the division template from the league.
			if (divisions != null && !divisions.isEmpty()) {
				Iterator it = divisions.iterator();
				while (it.hasNext()) {
					Group division = (Group) it.next();
					Group club = findClubForGroup(division);
					if (club != null) {
						updateDivision(division, divisionTemplate, club, leaguePlayerGroup);
					}
				}
			}
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}

	private void updateDivision(Group division, Group divisionTemplate, Group club, Group specialPlayerAliasGroupParent) {
		try {
			Collection divisionTemplateGroups = divisionTemplate.getChildren();
			Collection templateOwners = getGroupBusiness().getOwnerUsersForGroup(specialPlayerAliasGroupParent);
			Iterator it = divisionTemplateGroups.iterator();
			while (it.hasNext()) {
				Group templateGroup = (Group) it.next();
				Group templateReferance = findTemplateReferanceInDivision(division, templateGroup);
				if (templateReferance == null) {
					// add group to the division
					Group newGroup = addGroupToDivision(division, templateGroup, templateOwners);
					if (newGroup != null) {
						addOrUpdateReferenceToGroupToLeague(specialPlayerAliasGroupParent, templateGroup, newGroup,
								club.getName(), templateOwners);
					}
				}
				else {
					// check for the alias group and add if missing.
					addOrUpdateReferenceToGroupToLeague(specialPlayerAliasGroupParent, templateGroup,
							templateReferance, club.getName(), templateOwners);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Group addGroupToDivision(Group division, Group templateGroup, Collection templateOwners) {
		try {
			if (templateGroup.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER_TEMPLATE)) {
				// Create a copy of the player group under the
				// club/division.
				Group newGroup = getGroupBusiness().createGroupUnder(templateGroup.getName(), "",
						IWMemberConstants.GROUP_TYPE_CLUB_PLAYER, division);
				// This is a hack to store the connection between the copy
				// and the original. Should maybe be replaced with some
				// metadata.
				newGroup.setAlias(templateGroup);
				// Copy the metadata
				java.util.Map t = templateGroup.getMetaDataAttributes();
				if (t != null) {
					newGroup.setMetaDataAttributes(t);
				}
				newGroup.store();
				// Setting the correct access controls for the group. Set
				// the owner of the template as the owner of the group, then
				// give the current user all permissions for the group.
				if (templateOwners != null && !templateOwners.isEmpty()) {
					Iterator owners = templateOwners.iterator();
					while (owners.hasNext()) {
						User owner = (User) owners.next();
						getGroupBusiness().applyOwnerAndAllGroupPermissionsToNewlyCreatedGroupForUserAndHisPrimaryGroup(
								newGroup, owner);
					}
				}
				
				Collection divisionOwners = getGroupBusiness().getOwnerUsersForGroup(division);
				if (divisionOwners != null && !divisionOwners.isEmpty()) {
					Iterator owners = divisionOwners.iterator();
					while (owners.hasNext()) {
						User owner = (User) owners.next();
						getGroupBusiness().applyAllGroupPermissionsForGroupToUsersPrimaryGroup(newGroup, owner);
					}
				}

				return newGroup;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private void addOrUpdateReferenceToGroupToLeague(Group leagueReferanceParent, Group divisionTemplate, Group group,
			String clubName, Collection templateOwners) {

		Group topGroup = findOrCreateTopReferanceGroupForDivision(leagueReferanceParent, divisionTemplate,
				templateOwners);
		if (topGroup != null) {
			try {
				// Create a link to the actual group in the club, with
				// a new name and put it under the group created above.
				String name = group.getName();
				if (clubName != null) {
					name += " (" + clubName + ")";
				}
				Group newSpecialPlayerAliasGroup = getGroupBusiness().createGroupUnder(name, "",
						IWMemberConstants.GROUP_TYPE_ALIAS, topGroup);
				newSpecialPlayerAliasGroup.setAlias(group);
				newSpecialPlayerAliasGroup.store();
				if (templateOwners != null && !templateOwners.isEmpty()) {
					Iterator owners = templateOwners.iterator();
					while (owners.hasNext()) {
						User owner = (User) owners.next();
						getGroupBusiness().applyOwnerAndAllGroupPermissionsToNewlyCreatedGroupForUserAndHisPrimaryGroup(
								newSpecialPlayerAliasGroup, owner);
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private Group findOrCreateTopReferanceGroupForDivision(Group leagueReferanceParent, Group divisionTemplate,
			Collection templateOwners) {
		Iterator it = leagueReferanceParent.getChildrenIterator();
		while (it.hasNext()) {
			Group child = (Group) it.next();
			if (child.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER)) {
				if (child.getAliasID() == ((Integer) divisionTemplate.getPrimaryKey()).intValue()) {
					return child;
				}
			}
		}

		try {
			// Create a new group under the league_club_division
			// group that links to the playerGroup in the league.
			Group topReferanceGroupForDivision = getGroupBusiness().createGroupUnder(divisionTemplate.getName(), "",
					IWMemberConstants.GROUP_TYPE_CLUB_PLAYER, leagueReferanceParent);
			// This is a hack to store the connection between the
			// copy and the original. Should maybe be replaced with
			// some metadata.
			topReferanceGroupForDivision.setAlias(divisionTemplate);
			topReferanceGroupForDivision.store();

			// Setting the correct access controls for the group.
			// Set the owner of the template as the owner of the
			// group.
			if (templateOwners != null && !templateOwners.isEmpty()) {
				Iterator owners = templateOwners.iterator();
				while (owners.hasNext()) {
					User owner = (User) owners.next();
					getGroupBusiness().applyOwnerAndAllGroupPermissionsToNewlyCreatedGroupForUserAndHisPrimaryGroup(
							topReferanceGroupForDivision, owner);
				}
			}

			return topReferanceGroupForDivision;
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/*
	 * Finds the group in the division that is connected to the templateGroup
	 * and updates the metadata for it.
	 */
	private Group findTemplateReferanceInDivision(Group division, Group templateGroup) {
		Iterator divisionGroups = division.getChildrenIterator();
		try {
			Map metadata = templateGroup.getMetaDataAttributes();
			int template_id = ((Integer) templateGroup.getPrimaryKey()).intValue();
			while (divisionGroups.hasNext()) {
				Group divGroup = (Group) divisionGroups.next();
				if (divGroup.getAliasID() == template_id) {
					divGroup.setName(templateGroup.getName());
					// Have to remove all the current metadata, since the
					// default values for age and gender are "stored" as no
					// value.
					Map currentMetadata = divGroup.getMetaDataAttributes();
					if (currentMetadata != null && !currentMetadata.isEmpty()) {
						Iterator currMetaIt = currentMetadata.keySet().iterator();
						while (currMetaIt.hasNext()) {
							divGroup.removeMetaData((String) currMetaIt.next());
						}
						
						divGroup.store();
					}
					divGroup.setMetaDataAttributes(metadata);
					divGroup.store();

					return divGroup;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private Group findClubForGroup(Group group) {
		if (group == null) {
			return null;
		}
		if (group.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB)) {
			return group;
		}
		List parents = group.getParentGroups();
		if (parents != null && !parents.isEmpty()) {
			Iterator it = parents.iterator();
			while (it.hasNext()) {
				Group parent = (Group) it.next();
				Group div = findClubForGroup(parent);
				if (div != null) {
					return div;
				}
			}
		}
		return null;
	}

	private Group findDivisionForClub(Group club) {
		Collection children = club.getChildren();
		if (children != null && !children.isEmpty()) {
			Iterator it = children.iterator();
			while (it.hasNext()) {
				Group child = (Group) it.next();
				if (child.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION)) {
					return child;
				}
			}
		}
		return null;
	}

	/*
	 * Get the GroupBusiness.
	 */
	private GroupBusiness getGroupBusiness() {
		GroupBusiness business = null;
		try {
			business = (GroupBusiness) IBOLookup.getServiceInstance(iwac, GroupBusiness.class);
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
		}
		return business;
	}
}