package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

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
 * A presentation object for dynamic reports to choose groups.
 * By default it creates a selectionbox with all groups but subclassing it or using 
 * the setGroupType method can filter the list to only show a desired type.
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class GroupSelectionBox extends SelectionBox implements InputHandler{

	private String groupType = null;
	protected GroupBusiness groupBiz = null;
	private boolean useShortName = true;
	private String displayNameSeperator = ",";
	/**
	 * Creates a new <code>GroupSelectionBox</code> with all groups.
	 * @param name	The name of the <code>GroupSelectionBox</code>
	 */
	public GroupSelectionBox(String name) {
		super(name);
	}
	
	/**
	 * Creates a new <code>GroupSelectionBox</code> with all groups of specified type.
	 * @param name	The name of the <code>GroupSelectionBox</code>
	 * @param groupType	The type of group to populate the selection box with
	 */
	public GroupSelectionBox(String name, String groupType) {
		super(name);
		this.groupType = groupType;
	}
	
	public GroupSelectionBox() {
		super();
	}
	
	public void main(IWContext iwc) {
		try {
			groupBiz = getGroupBusiness(iwc);
		

		
		Collection groups = null;
		
		if(groupType!=null) {
			String[] type = {groupType};
			groups = groupBiz.getGroups(type);
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
					
				addMenuElement(group.getPrimaryKey().toString(), name);
			}	
		}
	}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	private String getNameForGroup(Group group) {
		String name;
		if(useShortName) {
			name = group.getShortName();
		}
		else {
			name = group.getName();
		}

		while( name==null ) {
			name = group.getShortName();
			name = group.getAbbrevation();
			name = group.getDescription();
			name = group.getPrimaryKey().toString();
		}
		return name;
	}

	public GroupBusiness getGroupBusiness(IWApplicationContext iwac) throws RemoteException {
		
		return (GroupBusiness) com.idega.business.IBOLookup.getServiceInstance(iwac, GroupBusiness.class);
	}

	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getHandlerObject(java.lang.String, java.lang.String, com.idega.presentation.IWContext)
	 */
	public PresentationObject getHandlerObject(String name, String stringValue, IWContext iwc) {
		this.setName(name);
		if(stringValue != null){
			this.setContent(stringValue);
		}
		return this;
	}

	/**
	 * @return a Collection of Group's
	 * 
	 */
	public Object getResultingObject(String[] values, IWContext iwc) throws Exception {
		Collection  groups = null;
		if(values != null && values.length>0 ){
			try {
					groups = getGroupBusiness(iwc).getGroups(values);
				
			} catch (IDOLookupException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		
		return groups;
		
	}

	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getDisplayNameOfValue(java.lang.String, com.idega.presentation.IWContext)
	 */
	public String getDisplayNameOfValue(Object value, IWContext iwc) {
		if(value != null){
				Iterator iter = ((Collection) value).iterator();
				StringBuffer names = new StringBuffer();
				int numberOfGroups = ((Collection) value).size();
				int counter = 0;
				
				while (iter.hasNext()) {
					Group group = (Group) iter.next();
					names.append(getNameForGroup(group));
					counter++;
					if(counter<numberOfGroups) {
						names.append(displayNameSeperator);
					}
				}
				
				return names.toString();
				
		}		
		return "";
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

}
