package is.idega.idegaweb.member.isi.block.clubs.business;

import is.idega.idegaweb.member.util.IWMemberConstants;

import java.util.Collection;

import com.idega.business.IBOServiceBean;
import com.idega.user.data.Group;


public class ClubInfoBusinessBean extends IBOServiceBean implements ClubInfoBusiness
{
 public Collection getDivisionsForClub(Group club) {
 	return club.getChildGroups(new String[] {IWMemberConstants.GROUP_TYPE_CLUB_DIVISION}, true);
 }
 
 public Collection getFlocksForDivision(Group division) {
 	return division.getChildGroups(new String[] {IWMemberConstants.GROUP_TYPE_CLUB_PLAYER}, true);
 }
}
