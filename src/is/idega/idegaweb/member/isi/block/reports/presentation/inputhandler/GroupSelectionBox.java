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
	private boolean isInitialized = false; 
	protected boolean selectAllOnSubmitIfNoneSelected = true;
	protected boolean autoSelectIfOnlyOneGroup = true;
	
	private boolean addAllOptionToList = false;
	private String addAllOptionToListDisplayKey = null;
	private String addAllOptionToListDisplayValue = null;
	
	private final static String DEFAULT_ADD_ALL_DISPLAY_KEY = "all_groups";
	private final static String DEFAULT_ADD_ALL_DISPLAY_VALUE = "All groups";	

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

	private void initialize(IWContext iwc) {
		if (this.isInitialized) {
			return;
		}
		this.isInitialized = true;
		try {
			this.groupBiz = getGroupBusiness(iwc);

			Collection groupCollection = getGroups(iwc);
		


			if (groupCollection != null && !groupCollection.isEmpty()) {
				//stupid but neccesary
				List groups = ListUtil.convertCollectionToList(groupCollection);
				sortList(iwc, groups);
				
				if (this.addAllOptionToList) {
					if (this.addAllOptionToListDisplayKey == null || this.addAllOptionToListDisplayKey.equals("")) {
						this.addAllOptionToListDisplayKey = DEFAULT_ADD_ALL_DISPLAY_KEY;
					}
					
					if (this.addAllOptionToListDisplayValue == null || this.addAllOptionToListDisplayValue.equals("")) {
						this.addAllOptionToListDisplayValue = DEFAULT_ADD_ALL_DISPLAY_VALUE;
					}
					addMenuElement(-1, getResourceBundle(iwc).getLocalizedString(this.addAllOptionToListDisplayKey, this.addAllOptionToListDisplayValue));
				}
				
				Iterator iter = groups.iterator();
				int size = groups.size();
				while (iter.hasNext()) {
					Group group = (Group) iter.next();
					String name = null;
					name = getNameForGroup(group);

					boolean showGroup = true;
					if(this.metaDataMap!=null && !this.metaDataMap.isEmpty()) {
						showGroup = checkMetaData(group, this.metaDataMap);
					}
					if(showGroup) {
						String id = group.getPrimaryKey().toString();
						addMenuElement(id, name);
						if(size==1 && this.autoSelectIfOnlyOneGroup){//might this cause problems? add as an option to the interface if it does.
							setSelectedElement(id);
						//	setDisabled(true);//cannot change it
							
							//todo add a hidden input also if this does not cut it
						}
						
					}
				}
			}
		}
		catch (RemoteException e) {
			this.isInitialized = false;
			e.printStackTrace();
		}
	}

	protected void sortList(IWContext iwc, List groups) throws RemoteException {
		GroupComparator groupComparator = new GroupComparator(iwc);
		groupComparator.setGroupBusiness(this.getGroupBusiness(iwc));
		Collections.sort(groups, groupComparator);//sort alphabetically
	}

	protected Collection getGroups(IWContext iwc) throws RemoteException {
		Collection groups = null;

		if (this.groupType != null) {
			String[] type = { this.groupType };
			try {
				groups = this.groupBiz.getGroups(type, true);
			}
			catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		else {
			groups = this.groupBiz.getAllGroups();
		}
		
		return groups;
	}

	protected boolean checkMetaData(Group group, Map metaData) {
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
		initialize(iwc);
		this.setName(name);
		if (this.selectAllOnSubmitIfNoneSelected) {
			this.selectAllOnSubmitIfNoneSelected();
		}
		if (value != null) {
			this.setSelectedElement(value);
		}
		return this;
	}
	
	public void setResultAsString() {
		this._stringResults = true;
	}
	
	/**
	 * @return a Collection of Group's
	 *  
	 */
	public Object getResultingObject(String[] values, IWContext iwc) throws Exception {
		Collection groups = null;
		
		if(this._stringResults) {
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
			int totalNumberOfGroups = -1;
			String totalNumberOfGroupsParam = iwc.getParameter(PARAM_NUMBER_OF_ELEMENTS_IN_SELECTIONBOX+"_"+getClassName());
			if (totalNumberOfGroupsParam != null && !totalNumberOfGroupsParam.equals("")) {
			    try {
                    totalNumberOfGroups = Integer.parseInt(totalNumberOfGroupsParam);
                } catch (NumberFormatException e) {
					e.printStackTrace();
                }
			}
			if (numberOfGroups != -1) {
			    if (totalNumberOfGroups == numberOfGroups) {
					String shortClassName = this.getClassName().substring(this.getClassName().lastIndexOf(".")+1);
			        return this.getResourceBundle(iwc).getLocalizedString(shortClassName+".all_or_none_selected","All");
			    }
			}
			int counter = 0;

			while (iter.hasNext()) {
				Group group = (Group) iter.next();
				names.append(getNameForGroup(group));
				counter++;
				if (counter < numberOfGroups) {
					names.append(this.displayNameSeperator);
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
		return this.groupType;
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
		return this.displayNameSeperator;
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
				this.userType = WorkReportConstants.WR_USER_TYPE_FEDERATION;
				return null;
			}
			
			List union = getWorkReportBusiness(iwc).getUnionListForUserFromTopNodes(user, iwc); //should only be one
			if (!union.isEmpty()) {
				this.userType = WorkReportConstants.WR_USER_TYPE_UNION;
				return ((Integer) ((Group)union.iterator().next()).getPrimaryKey());	
			}

			List regional = getWorkReportBusiness(iwc).getRegionalUnionListForUserFromTopNodes(user, iwc); //should only be one
			if (!regional.isEmpty()) {
				this.userType = WorkReportConstants.WR_USER_TYPE_REGIONAL_UNION;
				return ((Integer) ((Group)regional.iterator().next()).getPrimaryKey());
			}

			List leagues = getWorkReportBusiness(iwc).getLeaguesListForUserFromTopNodes(user, iwc); //should only be one
			if (!leagues.isEmpty()) {
				this.userType = WorkReportConstants.WR_USER_TYPE_LEAGUE;
				return ((Integer) ((Group)leagues.iterator().next()).getPrimaryKey());
			}
			
			List club = getWorkReportBusiness(iwc).getClubListForUserFromTopNodes(user, iwc); //should only be one
			if (!club.isEmpty()) {
				this.userType = WorkReportConstants.WR_USER_TYPE_CLUB;
				return ((Integer) ((Group)club.iterator().next()).getPrimaryKey());
			}
			List division = getWorkReportBusiness(iwc).getDivisionListForUserFromTopNodes(user, iwc); //should only be one
			if (!division.isEmpty()) {
				this.userType = WorkReportConstants.WR_USER_TYPE_DIVISION;
				return ((Integer) ((Group)division.iterator().next()).getPrimaryKey());
			}

		}
		catch (RemoteException e) {
			e.printStackTrace();
		}

		return null;

	}
	
	protected String getUserType() {
		return this.userType;
	}
	
	protected WorkReportBusiness getWorkReportBusiness(IWApplicationContext iwc) {
		if (this.workBiz == null) {
			try {
				this.workBiz = (WorkReportBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, WorkReportBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return this.workBiz;
	}

	public PresentationObject getHandlerObject(String name, Collection values, IWContext iwc) {
		String value = (String) Collections.min(values);
		return getHandlerObject(name, value, iwc);
	}



	public Object convertSingleResultingObjectToType(Object value, String className) {
		return value;
	}

	public void setAddAllOptionToList(boolean addOption) {
		this.addAllOptionToList = addOption;
	}
	
	public void setAddAllOptionToListDisplayKey(String key) {
		this.addAllOptionToListDisplayKey = key;
	}
	
	public void setAddAllOptionToListDisplayValue(String value) {
		this.addAllOptionToListDisplayValue = value;
	}
}