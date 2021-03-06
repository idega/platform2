package com.idega.user.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.builder.data.ICPage;
import com.idega.core.builder.data.ICPageHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.event.IWActionListener;
import com.idega.event.IWPresentationEvent;
import com.idega.event.IWPresentationStateImpl;
import com.idega.idegaweb.IWException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.GroupType;
import com.idega.user.data.User;
import com.idega.user.event.CreateGroupEvent;
import com.idega.user.util.ICUserConstants;

/**
 * Title: CreateGroupWindowPS <br>
 * Description: This class handles creating new groups. <br>
 * Copyright: Idega Software Copyright (c) 2002 <br>
 * Company: Idega Software <br>
 * 
 * @author <a href="eiki@idega.is">Eirikur Hrafnsson </a>
 * @version 1.0
 */
public class CreateGroupWindowPS extends IWPresentationStateImpl implements IWActionListener {

	private boolean _close = false;

	private Integer groupId = null;

	private IWContext eventContext = null;

	private String _groupName = null;

	private String _groupDescription = null;

	private String _groupType = null;

	private IWResourceBundle iwrb;

	private static final String IW_BUNDLE_IDENTIFIER = "com.idega.user";

	private GroupBusiness groupBusiness;

	private boolean failedToCreateGroup;

	private Group _parentGroup;

	private int _aliasID = -1;

	private int _homePageID = -1;

	private List _errorMessages = null;

	public CreateGroupWindowPS() {
	}

	public void reset() {
		this._groupName = null;
		this._groupDescription = null;
		this._groupType = null;
		this._aliasID = -1;
		this._homePageID = -1;
		this._close = false;
		this.failedToCreateGroup = false;
		this._parentGroup = null;
		this.groupId = null;
		this._errorMessages = null;
		this.eventContext = null;
	}

	public String getGroupName() {
		return this._groupName;
	}

	public String getGroupDescription() {
		return this._groupDescription;
	}

	public String getGroupType() {
		return this._groupType;
	}

	public Integer getGroupId() {
		return this.groupId;
	}

	public IWContext getEventContext() {
		return this.eventContext;
	}

	public boolean doClose() {
		return this._close;
	}

	public void doneClosing() {
		this._close = false;
	}

	public void actionPerformed(IWPresentationEvent e) throws IWException {
		this.iwrb = e.getIWContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).getResourceBundle(
				e.getIWContext());
		if (e instanceof CreateGroupEvent) {
			CreateGroupEvent event = (CreateGroupEvent) e;
			//create group, subgroups and give permissions
			//TODO check permission for the alias id, check the allowed child group types
			//make a getErrorMessageLocalizedKey()  method
			this.eventContext = event.getIWContext();
			
			if (event.doCommit()) {
				try {
					this.groupBusiness = getGroupBusiness(this.eventContext);
					Group group = null;
					int parentGroupId = event.getParentID();
					this._groupName = event.getName().trim();//no leading or trailing white spaces thank you
					this._groupDescription = event.getDescription();
					this._groupType = event.getGroupType();
					this._aliasID = event.getAliasID();
					this._homePageID = event.getHomePageID();
					
					if (event.getParentType() == CreateGroupEvent.TYPE_DOMAIN) {
						// create the group under the default Domain (it's a
						// top node (as super user))
						//don't get the group by: _parentGroup = groupBusiness.getGroupByGroupID(parentGroupId);
						//because the id is a domain id not the group id
						this._parentGroup = null;
						group = this.groupBusiness.createGroup(this._groupName,this._groupDescription,this._groupType, this._homePageID, this._aliasID);
					}
					else if (event.getParentType() == CreateGroupEvent.TYPE_GROUP) {
						this._parentGroup = this.groupBusiness.getGroupByGroupID(parentGroupId);
						// create under the supplied parent group
						//if (false) {
						
						List errors = canCreateSubGroupPluginCheck(this._parentGroup,this._groupType,this.eventContext);
						
						if (errors.isEmpty() && this.eventContext.getAccessController().hasEditPermissionFor(this._parentGroup, this.eventContext)) {
							group = this.groupBusiness.createGroupUnder(this._groupName, this._groupDescription,
									this._groupType, this._homePageID, this._aliasID, this._parentGroup);
							copyGroupNumberFromParent(group, this._parentGroup);
						}
						else {
							//todo set error message key
							this.failedToCreateGroup = true;
							this._errorMessages = errors;
						}
						/////////////
					}
					else {
						//UNKNOWN PARENT TYPE
						System.err.println("[CreateGroupWindow]: parentGroupType " + event.getParentType()
								+ "not found. Use a proper parent type (0=domain, 1=group)");
					}
					if (group != null) {
						// store group id and context, so change listners are
						// able
						// to open windows (e.g. the group property window)
						this.groupId = (Integer) group.getPrimaryKey();
						
						User currentUser = this.eventContext.getCurrentUser();
						//Apply permission stuff
						this.groupBusiness.applyOwnerAndAllGroupPermissionsToNewlyCreatedGroupForUserAndHisPrimaryGroup(group, currentUser);
						// get groupType tree and iterate through it and create
						// default sub groups.
						createDefaultSubGroupsFromGroupTypeTreeAndApplyPermissions(group, this.groupBusiness,
								this.eventContext, currentUser);
						
						callAfterCreatePluginMethods(group,this.eventContext);
						
						
						//TODO fix this what is it doing? some caching stuff?
						e.getIWContext().getApplicationContext().removeApplicationAttribute("domain_group_tree");
						e.getIWContext().getApplicationContext().removeApplicationAttribute("group_tree");
					}
				}
				catch (CreateException ce) {
					throw new EJBException(ce);
				}
				catch (RemoteException ex) {
					throw new EJBException(ex);
				}
				catch (FinderException fe) {
					throw new EJBException(fe);
				}
				this.fireStateChanged();
				//forget everything
				if(!this.failedToCreateGroup){
					reset();
				}
			}
			else if (event.doCancel()) {
				this.reset();
				this._close = true;
				this.fireStateChanged();
			}
			else {
				//unknown behaviour OR the parent group changed
				try {
					this.groupBusiness = getGroupBusiness(this.eventContext);
					this._groupName = event.getName().trim();
					this._groupDescription = event.getDescription();
					this._groupType = event.getGroupType();
					this._aliasID = event.getAliasID();
					this._homePageID = event.getHomePageID();
					if(event.getParentID()!=-1 && event.getParentType()==CreateGroupEvent.TYPE_GROUP ){
						Group newParentGroup = this.groupBusiness.getGroupByGroupID(event.getParentID());
						if((this._parentGroup==null) || (newParentGroup!=null && !this._parentGroup.equals(newParentGroup)) ){
							this._parentGroup = newParentGroup;
							//_groupType = null;
							fireStateChanged();
						}
					}
					
				}
				catch (RemoteException e1) {
					e1.printStackTrace();
				}
				catch (FinderException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * Call after create or update plugin methods
	 * @param group
	 * @param eventContext2
	 */
	protected void callAfterCreatePluginMethods(Group group, IWContext iwc) {
		getGroupBusiness(iwc).callAllUserGroupPluginAfterGroupCreateOrUpdateMethod(group);
	}
	
	/**
	 * Call after create or update plugin methods
	 * @param group
	 * @param eventContext2
	 * @return A list of messages why we cannot create a sub group
	 */
	protected List canCreateSubGroupPluginCheck(Group parentGroup, String groupTypeOfNewGroup, IWContext iwc) {
		GroupBusiness groupBiz = getGroupBusiness(iwc);
		List errors = new ArrayList();
		try {
			Collection plugins = groupBiz.getUserGroupPluginsForGroup(parentGroup);
			Iterator iter = plugins.iterator();
			while(iter.hasNext()){
				UserGroupPlugInBusiness pluginBiz = (UserGroupPlugInBusiness)iter.next();
				String error = pluginBiz.canCreateSubGroup(parentGroup,groupTypeOfNewGroup);
				if(error!=null){
					errors.add(error);
				}
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		
		return errors;
	}

	/**
	 * @param group
	 * @param parentGroup
	 */
	private void copyGroupNumberFromParent(Group group, Group parentGroup) {
		//todo refactor this to a plugin
		//used to see the path to groups in the permission window
		String groupNumber = parentGroup.getMetaData(ICUserConstants.META_DATA_GROUP_NUMBER);
		if (groupNumber != null && !"".equals(groupNumber)) {
			if (!groupNumber.endsWith("-")) {
				groupNumber += "-";//add a - to thee number
			}
			group.setMetaData(ICUserConstants.META_DATA_GROUP_NUMBER, groupNumber);
			group.store();
		}
	}

	private GroupBusiness getGroupBusiness(IWContext iwc) {
		GroupBusiness business = null;
		try {
			business = (GroupBusiness) IBOLookup.getServiceInstance(iwc, GroupBusiness.class);
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
		}
		return business;
	}

	private UserBusiness getUserBusiness(IWContext iwc) {
		UserBusiness business = null;
		try {
			business = (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
		}
		return business;
	}

	/**
	 * Creates the child groups specified in the groups grouptypetree
	 * definition.
	 * 
	 * @param group
	 * @param business
	 * @param iWContext
	 */
	private void createDefaultSubGroupsFromGroupTypeTreeAndApplyPermissions(Group group, GroupBusiness business,
			IWContext iwc, User user) throws RemoteException {
		GroupType type;
		try {
			type = business.getGroupTypeHome().findByPrimaryKey(group.getGroupType());
		}
		catch (FinderException e) {
			e.printStackTrace();
			return;
		}
		Iterator iterator = type.getChildrenIterator();
		while (iterator != null && iterator.hasNext()) {
			GroupType gType = (GroupType) iterator.next();
			String name = gType.getDefaultGroupName();
			if (gType.getAutoCreate()) {
				Integer numberOfInstances = gType.getNumberOfInstancesToAutoCreate();
				int nrOfGroupsToCreate = 1;
				if ((numberOfInstances != null) && (numberOfInstances.intValue() > 1)) {
					nrOfGroupsToCreate = numberOfInstances.intValue();
				}
				for (int i = 1; i <= nrOfGroupsToCreate; i++) {
					String typeString = gType.getType();
					String typeLocalizingKey = "auto.create.name." + typeString;
					String defaultValue = gType.getDescription();
					if ((defaultValue == null) || ("".equals(defaultValue))) {
						defaultValue = typeString;
					}
					//to avoid circular reference with beginning type
					//if( this.getGroupType().equals(typeString) ) continue;
					// rather add all types to a map to check
					if (name == null) {
						if (nrOfGroupsToCreate > 1) {
							typeLocalizingKey = typeLocalizingKey + " " + i;
							defaultValue = defaultValue + " " + i;
						}
						name = this.iwrb.getLocalizedString(typeLocalizingKey, defaultValue);
					}
					else {
						if (nrOfGroupsToCreate > 1) {
							name = name + " " + i;
						}
					}
					//create group then call recursive
					try {
						
						List errors = canCreateSubGroupPluginCheck(group,typeString,iwc);
						
						if (errors.isEmpty()) {
							Group newGroup = business.createGroupUnder(name, "", typeString, group);
							copyGroupNumberFromParent(newGroup, group);
							this.groupBusiness.applyOwnerAndAllGroupPermissionsToNewlyCreatedGroupForUserAndHisPrimaryGroup(newGroup, user);
							if (!type.isLeaf()) {
								createDefaultSubGroupsFromGroupTypeTreeAndApplyPermissions(newGroup, business, iwc, user);
							}
						}
						else{
							//cannot create a subgroup of that type under the parent group, some plugin does not allow it
							System.err.println("[CreateGroupWindowPS] - Creating sub group of the type "+typeString+" was not allowed under a group "+group.getName()+" of type "+group.getGroupType()+". Reason/s :");
							for (Iterator err = errors.iterator(); err.hasNext();) {
								String error = (String) err.next();
								System.err.println("[CreateGroupWindowPS] -"+error);
								
							}
						}
					}
					catch (CreateException e) {
						e.printStackTrace();
						return;
					}
				}
			}
		}
	}

	/**
	 * @return Returns the failedToCreateGroup.
	 */
	public boolean hasFailedToCreateGroup() {
		return this.failedToCreateGroup;
	}
	/**
	 * @return Returns the _parentGroup.
	 */
	public Group getParentGroup() {
		return this._parentGroup;
	}
	/**
	 * @return Returns the _aliasID.
	 */
	public int getAliasID() {
		return this._aliasID;
	}
	/**
	 * @return Returns the _homePageID.
	 */
	public int getHomePageID() {
		return this._homePageID;
	}
	/**
	 * @param pageID The _homePageID to set.
	 */
	public void setHomePageID(int pageID) {
		this._homePageID = pageID;
	}

	/**
	 * @return
	 */
	public ICPage getHomePage() {
		ICPage homePage = null;
		//we could optimise by returning a cached object
		if(this._homePageID>0 ){
			ICPageHome home;
			try {
				home = (ICPageHome) IDOLookup.getHome(ICPage.class);
				homePage = home.findByPrimaryKey(this._homePageID);
			}
			catch (IDOLookupException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
			return homePage;
		}
		return homePage;
	}

	/**
	 * @return
	 */
	public List getFailedToCreateGroupErrorMessages() {
		return this._errorMessages;
	}
}