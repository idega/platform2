package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;
import is.idega.idegaweb.member.util.IWMemberConstants;

import com.idega.business.InputHandler;
import com.idega.presentation.IWContext;
 
/**
 * A presentation object for dynamic reports to choose Leagues from a selectionbox
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class RegionalUnionSelectionBox extends GroupSelectionBox implements InputHandler{

	
	/**
	 * Creates a new <code>RegionalUnionSelectionBox</code> with all regional unions.
	 * @param name	The name of the <code>RegionalUnionSelectionBox</code>
	 */
	public RegionalUnionSelectionBox(String name) {
		super(name,IWMemberConstants.GROUP_TYPE_REGIONAL_UNION);
	}
	
	/**
	 * 
	 */
	public RegionalUnionSelectionBox() {
		super();
		setGroupType(IWMemberConstants.GROUP_TYPE_REGIONAL_UNION);
		setName(IWMemberConstants.GROUP_TYPE_REGIONAL_UNION);
	}
	
	//only allow this regionalunion to select itself
	protected Collection getGroups(IWContext iwc) throws RemoteException {
		Integer groupID = setUserTypeAndReturnGroupId(iwc);
		
		if(groupID!=null && WorkReportConstants.WR_USER_TYPE_REGIONAL_UNION.equals(getUserType())){
			
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
