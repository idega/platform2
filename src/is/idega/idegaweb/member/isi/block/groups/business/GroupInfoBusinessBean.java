/*
 * Created on Apr 7, 2006
 */
package is.idega.idegaweb.member.isi.block.groups.business;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOServiceBean;
import com.idega.core.location.data.Address;
import com.idega.presentation.IWContext;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;

/**
 * @author Sigtryggur
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class GroupInfoBusinessBean extends IBOServiceBean implements GroupInfoBusiness {
	
	private GroupBusiness _groupBiz = null;
	
	public static GroupInfoBusiness getGroupInfoBusiness(IWContext iwc) {
		try {
			return (GroupInfoBusiness) IBOLookup.getServiceInstance(iwc, GroupInfoBusiness.class);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}		
	
	/**
	 * Gets a list of groups, sorted using a given comparator. Takes two arrays of group ids, the returning list contains groups from both arrays with
	 * no duplicate values
	 * @param iwc The IWContext
	 * @param ids1 first array og group ids
	 * @param ids2 second array og group ids
	 * @param comparator The Comparator to use to compare group names, if null then list is not sorted.
	 * @return
	 */
	public List getGroups(IWContext iwc, String[] ids1, String[] ids2, final Comparator comparator) {
		Set groupIdsSet = new HashSet(); // must use a Set first to prevent duplicate values
		if(ids1!=null && ids1.length>0) {
			groupIdsSet.add(Arrays.asList(ids1));
		}
		if(ids2!=null && ids2.length>0) {
			groupIdsSet.add(Arrays.asList(ids2));
		}
		List groupIdsList = new ArrayList(groupIdsSet);
		List groups = new ArrayList();
		Iterator idsIter = groupIdsList.iterator();
		while(idsIter.hasNext()) {
			String id = (String) idsIter.next();
			Group group = getGroup(iwc, id);
			if(group!=null) {
				groups.add(group);
			}
		}
		
		if(comparator!=null) {
			Collections.sort(groups, new Comparator() {
				public int compare(Object arg0, Object arg1) {
					String name0 = ((Group) arg0).getName();
					String name1 = ((Group) arg1).getName();
					return comparator.compare(name0, name1);
				}
			});
		}
		
		return groups;
	}
		
	/**
	 * Gets a Group by id
	 * @param iwc IWContext
	 * @param groupId the Group's id
	 * @return The Group
	 */
	public Group getGroup(IWContext iwc, String groupId) {
		Group group = null;
		try {
			group = getGroupBusiness(iwc).getGroupByGroupID(Integer.parseInt(groupId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return group;
	}
	
	/**
	 * Gets a groups address
	 * @param iwc IWContext
	 * @param group The group to get address for
	 * @return The groups address
	 */
	public Address getGroupAddress(IWContext iwc, Group group) {
		Address address = null;
		try {
			address = getGroupBusiness(iwc).getGroupMainAddress(group);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return address;
	}
	
	private GroupBusiness getGroupBusiness(IWContext iwc) {
		if(_groupBiz == null) {
			try {
				_groupBiz = (GroupBusiness) IBOLookup.getServiceInstance(iwc.getApplicationContext(), GroupBusiness.class);
			} catch (IBOLookupException e) {
				e.printStackTrace();
			}
		}
		
		return _groupBiz;
	}
}