package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;
import is.idega.idegaweb.member.util.IWMemberConstants;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.presentation.IWContext;
 
/**
 * A presentation object for dynamic reports to choose Leagues from a selectionbox
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class LeagueSelectionBox extends GroupSelectionBox {

	/**
	 * Creates a new <code>LeagueSelectionBox</code> with all leagues.
	 * @param name	The name of the <code>LeagueSelectionBox</code>
	 */
	public LeagueSelectionBox(String name) {
		super(name,IWMemberConstants.GROUP_TYPE_LEAGUE);
	}
	
	/**
	 * 
	 */
	public LeagueSelectionBox() {
		super();
		setGroupType(IWMemberConstants.GROUP_TYPE_LEAGUE);
		setName(IWMemberConstants.GROUP_TYPE_LEAGUE);
	}
	
	//only allow this league to select itself
	protected Collection getGroups(IWContext iwc) throws RemoteException {
		Integer groupID = setUserTypeAndReturnGroupId(iwc);
		
		if(groupID!=null && WorkReportConstants.WR_USER_TYPE_LEAGUE.equals(getUserType())){
			
			List group = new Vector();
			try {
				group.add(getGroupBusiness(iwc).getGroupByGroupID(groupID.intValue()));
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
			return group;
		}
		else{
			return super.getGroups(iwc);
		}
		
		
	}
	
}
