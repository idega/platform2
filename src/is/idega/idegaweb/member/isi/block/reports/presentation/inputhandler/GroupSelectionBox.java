package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusiness;
import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;
import is.idega.idegaweb.member.util.IWMemberConstants;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.FinderException;

import com.idega.business.InputHandler;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.SelectionBox;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.GroupComparator;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.ListUtil;
/**
 * A presentation object for dynamic reports to choose groups. By default it
 * creates a selectionbox with all groups but subclassing it or using the
 * setGroupType method can filter the list to only show a desired type.
 * 
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class GroupSelectionBox extends SelectionBox implements InputHandler {

	private String groupType = null;
	private Map metaDataMap = null;
	protected GroupBusiness groupBiz = null;
	private String displayNameSeperator = ",";
	private boolean _stringResults = false;

	protected static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	private String userType;
	private WorkReportBusiness workBiz;

	/**
	 * Creates a new <code>GroupSelectionBox</code> with all groups.
	 * 
	 * @param name
	 *            The name of the <code>GroupSelectionBox</code>
	 */
	public GroupSelectionBox(String name) {
		super(name);
	}

	/**
	 * Creates a new <code>GroupSelectionBox</code> with all groups of
	 * specified type.
	 * 
	 * @param name
	 *            The name of the <code>GroupSelectionBox</code>
	 * @param groupType
	 *            The type of group to populate the selection box with
	 */
	public GroupSelectionBox(String name, String groupType) {
		super(name);
		this.groupType = groupType;
	}
	
	/**
	 * Creates a new <code>GroupSelectionBox</code> with all groups of
	 * specified type.
	 * 
	 * @param name
	 *            The name of the <code>GroupSelectionBox</code>
	 * @param groupType
	 *            The type of group to populate the selection box with
	 * @param metaDataMap
	 *            a map of key-values to match group metadata (the key is a string from IWMemberConstants)	 
	 */
	public GroupSelectionBox(String name, String groupType, Map metaDataMap) {
		super(name);
		this.groupType = groupType;
		this.metaDataMap = metaDataMap;
	}

	public GroupSelectionBox() {
		super();
	}

	public void main(IWContext iwc) {
		try {
			groupBiz = getGroupBusiness(iwc);

			Collection groupCollection = getGroups(iwc);
		


			if (groupCollection != null && !groupCollection.isEmpty()) {
				//stupid but neccesary
				List groups = ListUtil.convertCollectionToList(groupCollection);
				GroupComparator groupComparator = new GroupComparator(iwc.getCurrentLocale());
				groupComparator.setGroupBusiness(this.getGroupBusiness(iwc));
				Collections.sort(groups, groupComparator);//sort alphabetically
				
				Iterator iter = groups.iterator();
				int size = groups.size();
				while (iter.hasNext()) {
					Group group = (Group) iter.next();
					String name = null;
					name = getNameForGroup(group);

					boolean showGroup = true;
					if(metaDataMap!=null && !metaDataMap.isEmpty()) {
						showGroup = checkMetaData(group, metaDataMap);
					}
					if(showGroup) {
						String id = group.getPrimaryKey().toString();
						addMenuElement(id, name);
						if(size==1){//might this cause problems? add as an option to the interface if it does.
							setSelectedElement(id);
						//	setDisabled(true);//cannot change it
							
							//todo add a hidden input also if this does not cut it
						}
						
					}
				}
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	protected Collection getGroups(IWContext iwc) throws RemoteException {
		Collection groups = null;

		if (groupType != null) {
			String[] type = { groupType };
			try {
				groups = groupBiz.getGroups(type, true);
			}
			catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		else {
			groups = groupBiz.getAllGroups();
		}
		
		return groups;
	}

	private boolean checkMetaData(Group group, Map metaData) {
		Iterator keyIter = metaData.keySet().iterator();
		while(keyIter.hasNext()) {
			String key = (String) keyIter.next();
			String value = (String) metaData.get(key);
			String groupValue = group.getMetaData(key);
			boolean isEqual = groupValue==value || (groupValue!=null && groupValue.equalsIgnoreCase(value));
			if(!isEqual) {
				return false;
			}
		}
		return true;
	}

	protected String getNameForGroup(Group group) {
		StringBuffer groupName = new StringBuffer();
		String name;
		
		groupName.append( (group.getMetaData(IWMemberConstants.META_DATA_CLUB_NUMBER)!=null)? group.getMetaData(IWMemberConstants.META_DATA_CLUB_NUMBER)+" " : "" )
		.append( (group.getShortName()!=null)? group.getShortName() : "");

		name=group.toString();
		if("".equals(name)){
			if(group.getAbbrevation()!=null){
				name = group.getAbbrevation();
			}
			else{
				name = (group.getName()!=null)? group.getName() : "No name for group! (id:"+group.getPrimaryKey().toString()+")";
			}
		}
		
		return name;
	}

	public GroupBusiness getGroupBusiness(IWApplicationContext iwac) throws RemoteException {

		return (GroupBusiness) com.idega.business.IBOLookup.getServiceInstance(iwac, GroupBusiness.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.business.InputHandler#getHandlerObject(java.lang.String,
	 *      java.lang.String, com.idega.presentation.IWContext)
	 */
	public PresentationObject getHandlerObject(String name, String value, IWContext iwc) {
		this.setName(name);
		if (value != null) {
			this.setContent(value);
		}
		return this;
	}
	
	public void setResultAsString() {
		_stringResults = true;
	}
	
	/**
	 * @return a Collection of Group's
	 *  
	 */
	public Object getResultingObject(String[] values, IWContext iwc) throws Exception {
		Collection groups = null;
		
		if(_stringResults) {
			System.out.println("[GroupSelectionBox] returning Strings");
			if (values != null && values.length > 0) {
				groups = new ArrayList();
				
				for(int i=0; i<values.length; i++) {
					groups.add(values[i]);
				}
			}
		} else {
			System.out.println("[GroupSelectionBox] returning Groups");
			if (values != null && values.length > 0) {
				try {
					groups = getGroupBusiness(iwc).getGroups(values);
				}
				catch (IDOLookupException e) {
					e.printStackTrace();
				}
				catch (FinderException e) {
					e.printStackTrace();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return groups;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.business.InputHandler#getDisplayNameOfValue(java.lang.String,
	 *      com.idega.presentation.IWContext)
	 */
	public String getDisplayForResultingObject(Object value, IWContext iwc) {
		if (value != null) {
			Iterator iter = ((Collection) value).iterator();
			StringBuffer names = new StringBuffer();
			int numberOfGroups = ((Collection) value).size();
			int counter = 0;

			while (iter.hasNext()) {
				Group group = (Group) iter.next();
				names.append(getNameForGroup(group));
				counter++;
				if (counter < numberOfGroups) {
					names.append(displayNameSeperator);
				}
			}

			return names.toString();

		}
		return this.getResourceBundle(iwc).getLocalizedString("GroupSelectionBox.all_or_none_selected","All");
	}
	/**
	 * @return the set group type
	 */
	protected String getGroupType() {
		return groupType;
	}

	/**
	 * @param groupType
	 */
	protected void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	/**
	 * @return
	 */
	protected String getDisplayNameSeperator() {
		return displayNameSeperator;
	}

	/**
	 * @param displayNameSeperator
	 */
	protected void setDisplayNameSeperator(String displayNameSeperator) {
		this.displayNameSeperator = displayNameSeperator;
	}
	
	

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	protected Integer setUserTypeAndReturnGroupId(IWContext iwc) {
		User user = iwc.getCurrentUser();

		try {
			
			List federation = getWorkReportBusiness(iwc).getFederationListForUserFromTopNodes(user, iwc); //should only be one
			if (!federation.isEmpty()) {
				userType = WorkReportConstants.WR_USER_TYPE_FEDERATION;
				return null;
			}
			
			List union = getWorkReportBusiness(iwc).getUnionListForUserFromTopNodes(user, iwc); //should only be one
			if (!union.isEmpty()) {
				userType = WorkReportConstants.WR_USER_TYPE_UNION;
				return ((Integer) ((Group)union.iterator().next()).getPrimaryKey());	
			}

			List regional = getWorkReportBusiness(iwc).getRegionalUnionListForUserFromTopNodes(user, iwc); //should only be one
			if (!regional.isEmpty()) {
				userType = WorkReportConstants.WR_USER_TYPE_REGIONAL_UNION;
				return ((Integer) ((Group)regional.iterator().next()).getPrimaryKey());
			}

			List leagues = getWorkReportBusiness(iwc).getLeaguesListForUserFromTopNodes(user, iwc); //should only be one
			if (!leagues.isEmpty()) {
				userType = WorkReportConstants.WR_USER_TYPE_LEAGUE;
				return ((Integer) ((Group)leagues.iterator().next()).getPrimaryKey());
			}
			
			List club = getWorkReportBusiness(iwc).getClubListForUserFromTopNodes(user, iwc); //should only be one
			if (!club.isEmpty()) {
				userType = WorkReportConstants.WR_USER_TYPE_CLUB;
				return ((Integer) ((Group)club.iterator().next()).getPrimaryKey());
			}

		}
		catch (RemoteException e) {
			e.printStackTrace();
		}

		return null;

	}
	
	protected String getUserType() {
		return userType;
	}
	
	protected WorkReportBusiness getWorkReportBusiness(IWApplicationContext iwc) {
		if (workBiz == null) {
			try {
				workBiz = (WorkReportBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, WorkReportBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return workBiz;
	}

	public PresentationObject getHandlerObject(String name, Collection values, IWContext iwc) {
		String value = (String) Collections.min(values);
		return getHandlerObject(name, value, iwc);
	}



	public Object convertSingleResultingObjectToType(Object value, String className) {
		return value;
	}

}
