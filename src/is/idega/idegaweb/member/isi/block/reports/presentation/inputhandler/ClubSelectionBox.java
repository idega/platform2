package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;
import is.idega.idegaweb.member.util.IWMemberConstants;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.business.InputHandler;
import com.idega.presentation.IWContext;
 
/**
 * A presentation object for dynamic reports to choose a club from a selectionbox
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class ClubSelectionBox extends GroupSelectionBox implements InputHandler{

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
		else{
			return super.getGroups(iwc);
		}
		
		
	}
	
}
