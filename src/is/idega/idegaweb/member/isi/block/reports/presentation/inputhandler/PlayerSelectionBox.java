package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;
import is.idega.idegaweb.member.util.IWMemberConstants;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.presentation.IWContext;
import com.idega.user.data.Group;

/**
 * @author Sigtryggur
 */
public class PlayerSelectionBox extends GroupSelectionBox  {
	public PlayerSelectionBox(String name) {
		super(name,IWMemberConstants.GROUP_TYPE_CLUB_PLAYER);
	}

	public PlayerSelectionBox() {
		super();
		setGroupType(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER);
		setName(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER);
	}
	
	public String getDisplayForResultingObject(Object value, IWContext iwc) {
		if (value == null) {
			return this.getResourceBundle(iwc).getLocalizedString("PlayerSelectionBox.all_or_none_selected","All");
		}
		return super.getDisplayForResultingObject(value, iwc);
	}
	
	//only allow this league to select itself
	protected Collection getGroups(IWContext iwc) throws RemoteException {
		Integer groupID = setUserTypeAndReturnGroupId(iwc);
		Group club = null;
		if (groupID != null){
			try {
				club = getGroupBusiness(iwc).getGroupByGroupID(groupID.intValue());
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
		}
		if(club!=null && WorkReportConstants.WR_USER_TYPE_CLUB.equals(getUserType())){
			
			List players = new ArrayList();
			getClubPlayers(players, club);
			return players;
		}
		else{
			return super.getGroups(iwc);
		}	
	}
	
	protected String getNameForGroup(Group group) {
		return super.getNameForGroup(group)+" ("+group.getParentNode().getNodeName()+")";
	}
	
	private void getClubPlayers(Collection divisions, Group group) {
		if (divisions == null)
			divisions = new ArrayList();
		
		if (group.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER)) {
			divisions.add(group);
		}

		Iterator it = group.getChildren();
		if (it != null) {
			while (it.hasNext()) {
				Group child = (Group)it.next();
				getClubPlayers(divisions, child);
			}
		}
	}
}