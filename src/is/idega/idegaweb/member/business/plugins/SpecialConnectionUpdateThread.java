/*
 * $Id: SpecialConnectionUpdateThread.java,v 1.3 2005/01/13 12:52:26 palli Exp $ Created
 * on Jan 4, 2005
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
import com.idega.data.IDOLookupException;
import com.idega.presentation.IWContext;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.util.IWTimestamp;

/**
 * 
 * Last modified: $Date: 2005/01/13 12:52:26 $ by $Author: palli $
 * 
 * @author <a href="mailto:palli@idega.com">palli </a>
 * @version $Revision: 1.3 $
 */
public class SpecialConnectionUpdateThread extends Thread {

	private Group special = null;

	/**
	 * 
	 */
	public SpecialConnectionUpdateThread(Group special) {
		this.special = special;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		System.out.println("Starting SpecialConnectionUpdateThread : " + IWTimestamp.getTimestampRightNow());
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
			if (foundIt && child != null) {
				special = child;
			}
		}
		if (special.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION_TEMPLATE)) {
			List children = special.getChildGroups();
			Iterator it = children.iterator();
			while (it.hasNext()) {
				Group child = (Group) it.next();
				if (child.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER_TEMPLATE)) {
					updatePlayerGroupsConnectedTo(child);
				}
			}
		}
		System.out.println("SpecialConnectionUpdateThread done : " + IWTimestamp.getTimestampRightNow());
	}

	private void updatePlayerGroupsConnectedTo(Group parent) {
		Collection connected = null;
		try {
			connected = ((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findGroupsByType(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER);
			if (connected != null) {
				Map metadata = parent.getMetaDataAttributes();
				int parent_id = ((Integer) parent.getPrimaryKey()).intValue();
				Iterator it = connected.iterator();
				while (it.hasNext()) {
					Group conn = (Group) it.next();
					if (conn.getAliasID() == parent_id) {
						conn.setName(parent.getName());
						conn.setMetaDataAttributes(metadata);
						conn.store();
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
					updatePlayerGroupsConnectedTo(child);
			}
		}
	}

	/*
	 * 1. Find all groups conntected to the league 
	 * 2. Find the club/division 
	 * 3. Go through all division children and match to the player group templates
	 * 4. If any are missing, create them
	 * 5. If any aliases are missing to groups, then create them
	 */
	private void addMissingGroupsToClubs(Group league, Group divisionTemplate, IWContext iwc) {
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
						updateDivision(division, divisionTemplate, club, leaguePlayerGroup, iwc);
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
						updateDivision(division, divisionTemplate, club, leaguePlayerGroup, iwc);
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

	private void updateDivision(Group division, Group divisionTemplate, Group club,
			Group specialPlayerAliasGroupParent, IWContext iwc) {
		Collection divisionTemplateGroups = divisionTemplate.getChildren();
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
}