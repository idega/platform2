/*
 * Created on Feb 18, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.member.isi.block.clubs.business;

import is.idega.idegaweb.member.util.IWMemberConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.idega.business.IBOServiceBean;
import com.idega.user.data.Group;

/**
 * @author jonas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ClubInfoBusinessBean extends IBOServiceBean implements ClubInfoBusiness {
	
	public Collection getDivisionsForClub(Group club) {
		Collection clubChildren = club.getChildGroups();
		ArrayList divisions = new ArrayList();
		if(clubChildren!=null) {
			Iterator clubChildrenIter = clubChildren.iterator();
			while(clubChildrenIter.hasNext()) {
				Group child = (Group) clubChildrenIter.next();
				if(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION.equals(child.getGroupType())) {
					divisions.add(child);
				}
			}
		} else {
			System.out.println("no groups found under club " + club.getName());
		}
		return divisions;
	}
	
	public Collection getFlocksForDivision(Group division) {
		Collection divisionChildren = division.getChildGroups();
		ArrayList flocks = new ArrayList();
		if(divisionChildren!=null) {
			Iterator divisionChildrenIter = divisionChildren.iterator();
			while(divisionChildrenIter.hasNext()) {
				Group child = (Group) divisionChildrenIter.next();
				if(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER.equals(child.getGroupType())) {
					flocks.add(child);
				}
			}
		} else {
			System.out.println("no groups found under division " + division.getName());
		}
		return flocks;
	}
}
