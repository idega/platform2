package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.FinderException;

import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolSeasonHome;
import com.idega.business.InputHandler;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.SelectionBox;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
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
	private boolean useShortName = false;
	private String displayNameSeperator = ",";

	protected static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";

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
		System.out.println("Metadata map set to: " + metaDataMap);
	}

	public GroupSelectionBox() {
		super();
	}

	public void main(IWContext iwc) {
		try {
			groupBiz = getGroupBusiness(iwc);

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

			if (groups != null) {
				Iterator iter = groups.iterator();
				while (iter.hasNext()) {
					Group group = (Group) iter.next();
					String name = null;
					name = getNameForGroup(group);

					boolean showGroup = true;
					if(metaDataMap!=null && metaDataMap.size()>0) {
						showGroup = checkMetaData(group, metaDataMap);
					}
					if(showGroup) {
						addMenuElement(group.getPrimaryKey().toString(), name);
					}
				}
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
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

	private String getNameForGroup(Group group) {
		String name;
		if (useShortName) {
			name = group.getShortName();
		}
		else {
			name = group.getName();
		}

		if( !"".equals(name) && (name == null) ) {
			name = group.getName();
		}
		if( !"".equals(name) && (name == null) ) {
			name = group.getShortName();
		}
		if( !"".equals(name) && (name == null) ) {
			name = group.getAbbrevation();
		}
		if( !"".equals(name) && (name == null) ) {
			name = group.getDescription();
		}
		if( !"".equals(name) && (name == null) ) {
			name = group.getPrimaryKey().toString();
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
	public PresentationObject getHandlerObject(String name, String stringValue, IWContext iwc) {
		this.setName(name);
		if (stringValue != null) {
			this.setContent(stringValue);
		}
		return this;
	}

	/**
	 * @return a Collection of Group's
	 *  
	 */
	public Object getResultingObject(String[] values, IWContext iwc) throws Exception {
		Collection groups = null;
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

		return groups;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.business.InputHandler#getDisplayNameOfValue(java.lang.String,
	 *      com.idega.presentation.IWContext)
	 */
	public String getDisplayNameOfValue(Object value, IWContext iwc) {
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
	protected boolean isSetToUseShortName() {
		return useShortName;
	}

	/**
	 * @param useShortName
	 */
	protected void setToUseShortName(boolean useShortName) {
		this.useShortName = useShortName;
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

}
