/*
 * Created on Nov 13, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;
import is.idega.idegaweb.member.util.IWMemberConstants;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.ejb.FinderException;
import com.idega.presentation.IWContext;
import com.idega.util.ListUtil;

/**
 * @author jonas
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class UMFIClubSelectionBox extends GroupSelectionBox {
	/**
	 * Creates a new <code>RegionalUnionSelectionBox</code> with all regional unions.
	 * @param name	The name of the <code>RegionalUnionSelectionBox</code>
	 */
	public UMFIClubSelectionBox(String name) {
		super(name,IWMemberConstants.GROUP_TYPE_CLUB, metaDataMap);
	}
	
	/**
	 * 
	 */
	public UMFIClubSelectionBox() {
		super("", IWMemberConstants.GROUP_TYPE_CLUB, metaDataMap);
	}
	
	static private Map metaDataMap = getMetaDataMap();
	
	static private Map getMetaDataMap() {
		Map metaDataMap = new Hashtable();
		metaDataMap.put(IWMemberConstants.META_DATA_CLUB_IN_UMFI, "true");
		return metaDataMap;
	}
	
	//only allow this regionalunion to select itself
	protected Collection getGroups(IWContext iwc) throws RemoteException {
		Integer groupID = setUserTypeAndReturnGroupId(iwc);
		//don't show these groups for users
		if(groupID!=null && WorkReportConstants.WR_USER_TYPE_CLUB.equals(getUserType())){
			return ListUtil.getEmptyList();
		}
		else if(groupID!=null && WorkReportConstants.WR_USER_TYPE_REGIONAL_UNION.equals(getUserType())){
				
				List groups = new ArrayList();
				//only get the child clubs
				try {
					Collection clubGroups = getWorkReportBusiness(iwc).getClubGroupsForRegionUnionGroup(getGroupBusiness(iwc).getGroupByGroupID(groupID.intValue()));
					
					groups.addAll(clubGroups);
					
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
