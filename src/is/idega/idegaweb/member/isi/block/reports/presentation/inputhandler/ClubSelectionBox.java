package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;
import is.idega.idegaweb.member.util.IWMemberConstants;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.presentation.IWContext;
import com.idega.user.data.Group;

/**
 * A presentation object for dynamic reports to choose a club from a selectionbox
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class ClubSelectionBox extends GroupSelectionBox {
	
	/**
	 * Creates a new <code>ClubSelectionBox</code> with all clubs.
	 * @param name	The name of the <code>ClubSelectionBox</code>
	 */
	public ClubSelectionBox(String name) {
		super(name,IWMemberConstants.GROUP_TYPE_CLUB);
	}
	
	/**
	 * 
	 */
	public ClubSelectionBox() {
		super();
		setGroupType(IWMemberConstants.GROUP_TYPE_CLUB);
		setName(IWMemberConstants.GROUP_TYPE_CLUB);
	}
	
	//only allow this club to select itself
	protected Collection getGroups(IWContext iwc) throws RemoteException {
		Integer groupID = setUserTypeAndReturnGroupId(iwc);
		
		if(groupID!=null && WorkReportConstants.WR_USER_TYPE_CLUB.equals(getUserType())){
			
			List group = new Vector();
			try {
				group.add(getGroupBusiness(iwc).getGroupByGroupID(groupID.intValue()));
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
			return group;
		}
		else if(groupID!=null && WorkReportConstants.WR_USER_TYPE_LEAGUE.equals(getUserType())){
			
			List groups = new Vector();
			//only get the connected clubs
			try {
				Collection clubGroups = getGroupBusiness(iwc).getGroupHome().findGroupsByMetaData(IWMemberConstants.META_DATA_CLUB_LEAGUE_CONNECTION,groupID.toString());
				
				if(clubGroups!=null && !clubGroups.isEmpty()){
					groups.addAll(clubGroups);
				}
				
			}
			catch (FinderException e) {
				//nothing found, don't care
			}
			
			try{
				Collection divGroups = getGroupBusiness(iwc).getGroupHome().findGroupsByMetaData(IWMemberConstants.META_DATA_DIVISION_LEAGUE_CONNECTION,groupID.toString());
				
				if(divGroups!=null && !divGroups.isEmpty()){
					Iterator iter = divGroups.iterator();
					while (iter.hasNext()) {
						Group div = (Group) iter.next();
						List parentClub = div.getParentGroups();
						if(parentClub!=null && !parentClub.isEmpty()){
							Group club = (Group) parentClub.iterator().next();
							groups.add(club);
						}
						else{
							System.err.println("The Division "+div.getName()+" id: "+div.getPrimaryKey()+" does not have a club parent!");
						}
					}
				}
				
			}
			catch (FinderException e) {
				//nothing found, don't care
			}
			
			return groups;
			
		}
		else if(groupID!=null && WorkReportConstants.WR_USER_TYPE_REGIONAL_UNION.equals(getUserType())){
			
			List groups = new Vector();
			//only get the child clubs
			try {
				Collection clubGroups = getWorkReportBusiness(iwc).getClubGroupsForRegionUnionGroup(getGroupBusiness(iwc).getGroupByGroupID(groupID.intValue()));
				
				if(clubGroups!=null && !clubGroups.isEmpty()){
					groups.addAll(clubGroups);
				}
				
			}
			catch (FinderException e) {
				//nothing found, don't care
			}			
			return groups;
			
		}
		else if(groupID!=null && WorkReportConstants.WR_USER_TYPE_UNION.equals(getUserType())){
			
			List groups = new Vector();
			//only get the connected clubs
			try {
				Collection clubGroups = getGroupBusiness(iwc).getGroupHome().findGroupsByMetaData(IWMemberConstants.META_DATA_CLUB_IN_UMFI,"true");
				
				if(clubGroups!=null && !clubGroups.isEmpty()){
					Iterator iter = clubGroups.iterator();
					while (iter.hasNext()) {
						Group group = (Group) iter.next();
						if(IWMemberConstants.GROUP_TYPE_CLUB.equals(group.getGroupType())){
							groups.add(group);
						}
					}
				}
			}
			catch (FinderException e) {
				//nothing found, don't care
			}
			
			return groups;
			
		}
		else{
			return super.getGroups(iwc);
		}
		
		
	}
	
}
