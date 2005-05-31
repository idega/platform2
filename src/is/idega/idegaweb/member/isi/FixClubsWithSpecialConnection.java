/*
 * $Id: FixClubsWithSpecialConnection.java,v 1.1 2005/05/31 10:00:32 palli Exp $ Created on Apr 28, 2005
 * 
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package is.idega.idegaweb.member.isi;

import is.idega.idegaweb.member.util.IWMemberConstants;
import java.util.Collection;
import java.util.Iterator;
import com.idega.data.IDOLookup;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;

/**
 * 
 * Last modified: $Date: 2005/05/31 10:00:32 $ by $Author: palli $
 * 
 * @author <a href="mailto:palli@idega.com">palli </a>
 * @version $Revision: 1.1 $
 */
public class FixClubsWithSpecialConnection extends Block {

	private static final String CLUB_CONNECTION = IWMemberConstants.META_DATA_CLUB_LEAGUE_CONNECTION;

	private static final String DIVISION_CONNECTION = IWMemberConstants.META_DATA_DIVISION_LEAGUE_CONNECTION;

	private static final String CLUB_MAKE = IWMemberConstants.META_DATA_CLUB_MAKE;

	private static final String MULTI_DIVISION_CONNECTION = IWMemberConstants.META_DATA_CLUB_STATUS_MULTI_DIVISION_CLUB;

	private static final String SINGLE_DIVISION_CONNECTION = IWMemberConstants.META_DATA_CLUB_STATUS_SINGLE_DIVISION_CLUB;

	private static final String TYPE_CLUB = IWMemberConstants.GROUP_TYPE_CLUB;
	
	private static final String TYPE_DIVISION = IWMemberConstants.GROUP_TYPE_CLUB_DIVISION;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		Collection specialConnectionClub = findAllClubsWithSpecialConnection(iwc);
		System.out.println("number of clubs = " + specialConnectionClub.size());
		Iterator it = specialConnectionClub.iterator();
		while (it.hasNext()) {
			Group club = (Group) it.next();
			updateClub(club);
		}
	}

	private Collection findAllClubsWithSpecialConnection(IWContext iwc) {
		Collection clubs = null;
		try {
			GroupHome gHome = (GroupHome) IDOLookup.getHome(Group.class);
			clubs = gHome.findGroupsByMetaData(CLUB_CONNECTION, null);
		}
		catch (Exception e) {
		}
		return clubs;
	}
	
	private Group findDivision(Group club) {
		Collection children = club.getChildren();
		Iterator it = children.iterator();
		while (it.hasNext()) {
			Group child = (Group) it.next();
			if (child.getGroupType().equals(TYPE_DIVISION)) {
				return child;
			}
		}
		return null;
	}
	
	private void updateClub(Group club) {
		Group division = findDivision(club);
		String connection = club.getMetaData(CLUB_CONNECTION);

		if (!club.getGroupType().equals(TYPE_CLUB)) {
			System.out.println("Group " + club.getName() + " is not a club");
			return;
		}
		
		if (division != null) {
			String divisionConnection = division.getMetaData(DIVISION_CONNECTION);
			if (divisionConnection != null && !"".equals(divisionConnection)) {
				System.out.println("Already a connection on division for club " + club.getName());
				return;
			}
			club.removeMetaData(CLUB_CONNECTION);
			club.setMetaData(CLUB_MAKE, MULTI_DIVISION_CONNECTION);
			club.store();
			
			if (connection != null && !"".equals(connection)) {
				division.setMetaData(DIVISION_CONNECTION, connection);
				division.store();
			} else {
				System.out.println("No connection defined for club " + club.getName());
			}
		} else {
			System.out.println("No division for club " + club.getName());
		}
	}
}