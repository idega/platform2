package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;
import is.idega.idegaweb.member.util.IWMemberConstants;
import java.rmi.RemoteException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.FinderException;
import com.idega.presentation.IWContext;
import com.idega.user.data.CachedGroup;
import com.idega.user.data.Group;

/**
 * @author Sigtryggur
 */
public class PlayerSelectionBox extends GroupSelectionBox  {

    private static String CACHE_PARENTS_APPLICATION_ATTRIBUTE = "CACHE_PARENTS";
	private static String CACHE_GROUPS_APPLICATION_ATTRIBUTE = "CACHE_GROUPS";
	private Map applicationCachedGroups = null;
	private Map applicationCachedParents = null;
	private Map cachedGroups = new HashMap();

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
	    if (group == null) return null;
	    
	    String groupName = group.getName();
	    String parentName = null;
	    try {
	        parentName = getParentName(group); 
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    if (parentName != null && !parentName.equals("")) {
	        groupName = groupName + " ("+parentName+")";
	    }
		return groupName;
	}
	
	private void getClubPlayers(Collection divisions, Group group) {
		if (divisions == null)
			divisions = new ArrayList();
		
		if (group.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER) ||
			group.getGroupType().equals(IWMemberConstants.GROUP_TYPE_GENERAL) ||
			group.getGroupType().equals(IWMemberConstants.GROUP_TYPE_TEMPORARY)   ) {
			divisions.add(group);
		}

		Iterator it = group.getChildrenIterator();
		if (it != null) {
			while (it.hasNext()) {
				Group child = (Group)it.next();
				getClubPlayers(divisions, child);
			}
		}
	}

	public String getParentName(Group group) throws RemoteException, FinderException {
	    String parentName = "";
	    CachedGroup cachedParentGroup = null;
	    Group parent = null;
		Integer groupId = (Integer)group.getPrimaryKey();
		if (applicationCachedParents.containsKey((groupId))) {
            Collection col = (Collection)applicationCachedParents.get(groupId);
            Iterator it = col.iterator();
            Integer parentId = null;
            
            if (it.hasNext()) {
                 parentId = (Integer)it.next();
                 String key = parentId.toString();
                 if (applicationCachedGroups.containsKey(key)) {
                     cachedParentGroup = (CachedGroup)applicationCachedGroups.get(key); 
                 }
                 else if (cachedGroups.containsKey(key)) {
                     parent = (Group)cachedGroups.get(key);
                     cachedParentGroup = new CachedGroup(parent);
                     applicationCachedGroups.put(key, cachedParentGroup);
                 }
                 else {
                     parent = (Group) groupBiz.getGroupByGroupID(parentId.intValue());
	                 cachedParentGroup = new CachedGroup(parent);
	                 cachedGroups.put(key,parent);
	                 applicationCachedGroups.put(key, cachedParentGroup);
                 }
            }       
        }
        else {
            Collection parents = group.getParentGroups(applicationCachedParents, cachedGroups);        
            Iterator parIt = parents.iterator();
	        if (parIt.hasNext()) {
	            parent = (Group)parIt.next();
	            if (parent!= null) {
	                cachedParentGroup = new CachedGroup(parent);
	            }
	        }
        }
		if (cachedParentGroup != null) {
		    parentName = cachedParentGroup.getName();
		}
		return parentName;
	}

	protected void sortList(IWContext iwc, List groups) throws RemoteException {
		PlayerComparator playerComparator = new PlayerComparator(iwc);
		Collections.sort(groups, playerComparator);//sort alphabetically
	}

	class PlayerComparator implements Comparator {
		
		private Locale locale;
    	private IWContext iwc;
		
		public PlayerComparator(IWContext _iwc) {
			iwc = _iwc;
		    locale = iwc.getLocale();
			applicationCachedGroups = (Map)iwc.getApplicationContext().getApplicationAttribute(CACHE_GROUPS_APPLICATION_ATTRIBUTE);
		    if(applicationCachedGroups == null){
		        applicationCachedGroups = new HashMap();
		        iwc.getApplicationContext().setApplicationAttribute(CACHE_GROUPS_APPLICATION_ATTRIBUTE, applicationCachedGroups);
		    }
			applicationCachedParents= (Map)iwc.getApplicationContext().getApplicationAttribute(CACHE_PARENTS_APPLICATION_ATTRIBUTE);
				if(applicationCachedParents == null){
				    applicationCachedParents = new HashMap();
				    iwc.getApplicationContext().setApplicationAttribute(CACHE_PARENTS_APPLICATION_ATTRIBUTE, applicationCachedParents);
				}
	
		}
		
		public int compare(Object arg0, Object arg1) {
			int comp = 0;
			try {
				Collator collator = Collator.getInstance(locale);
				Group group0 = (Group) arg0;
				Group group1 = (Group) arg1;
				String parentNode0 = getParentName(group0);
				String parentNode1 = getParentName(group1);
				comp = collator.compare(parentNode0, parentNode1);
								
				if(comp == 0) {
					String groupName0 = getNameForGroup(group0);
					String groupName1 = getNameForGroup(group1);
					comp = collator.compare(groupName0, groupName1);
				}
			} 
			catch(Exception e) {
				e.printStackTrace();
			}
			return comp;
		}
	}
}