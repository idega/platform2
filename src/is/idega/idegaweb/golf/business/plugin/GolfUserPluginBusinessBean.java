/*
 * $Id: GolfUserPluginBusinessBean.java,v 1.5 2004/12/07 15:58:30 eiki Exp $
 * Created on Nov 15, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.golf.business.plugin;

import is.idega.idegaweb.golf.presentation.GolferTab;
import is.idega.idegaweb.golf.util.GolfConstants;
import is.idega.idegaweb.member.util.IWMemberConstants;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOServiceBean;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * A user application plugin for various golf specific stuff such as the Golfer Info tab.
 *  Last modified: $Date: 2004/12/07 15:58:30 $ by $Author: eiki $
 * 
 * @author <a href="mailto:eiki@idega.com">Eirikur S. Hrafnsson</a>
 * @version $Revision: 1.5 $
 */
public class GolfUserPluginBusinessBean extends IBOServiceBean implements UserGroupPlugInBusiness, GolfUserPluginBusiness{

	private Collection clubs;
	private GroupBusiness groupBiz;
	private UserBusiness userBiz;

	/**
	 * 
	 */
	public GolfUserPluginBusinessBean() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterGroupCreateOrUpdate(com.idega.user.data.Group)
	 */
	public void afterGroupCreateOrUpdate(Group group) throws CreateException, RemoteException {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterUserCreateOrUpdate(com.idega.user.data.User)
	 */
	public void afterUserCreateOrUpdate(User user) throws CreateException, RemoteException {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeGroupRemove(com.idega.user.data.Group)
	 */
	public void beforeGroupRemove(Group group) throws RemoveException, RemoteException {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeUserRemove(com.idega.user.data.User)
	 */
	public void beforeUserRemove(User user) throws RemoveException, RemoteException {
		// TODO Auto-generated method stub
	}

	
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupPropertiesTabs(com.idega.user.data.Group)
	 */
	public List getGroupPropertiesTabs(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupToolbarElements(com.idega.user.data.Group)
	 */
	public List getGroupToolbarElements(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getMainToolbarElements()
	 */
	public List getMainToolbarElements() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getUserPropertiesTabs(com.idega.user.data.User)
	 */
	public List getUserPropertiesTabs(User user) throws RemoteException {
		
		//only add the tab if superuser or a club admin (golfclub in top nodes) or a golf union admin
		IWContext iwc = IWContext.getInstance();
		
		if(iwc!=null){
			List tabs = new ArrayList();
			
			if(!iwc.isSuperAdmin()){
				//this might mean not all e.g. division admin might see this tab, if so then add a role like "Golf Admin"
				boolean showTab = false;
				UserBusiness userBiz = getUserBusiness();
				Collection groups = userBiz.getUsersTopGroupNodesByViewAndOwnerPermissions(iwc.getCurrentUser(), iwc);
				if(groups!=null && !groups.isEmpty()){
					Iterator iter = groups.iterator();
					
					while (iter.hasNext() && !showTab) {
						Group group = (Group) iter.next();
						String type = group.getGroupType();
						String name = group.getName();
						if( name.startsWith("Golf") && (type.equals(IWMemberConstants.GROUP_TYPE_CLUB) || type.equals(IWMemberConstants.GROUP_TYPE_LEAGUE))){
							showTab = true;
						}
					}
					
					if(showTab){
						tabs.add(new GolferTab());
					}
				}
			}
			else{
				tabs.add(new GolferTab());
			}
			
			return (tabs.isEmpty())?null:tabs;
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateEditor(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateEditor(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateViewer(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateViewer(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#isUserAssignableFromGroupToGroup(com.idega.user.data.User, com.idega.user.data.Group, com.idega.user.data.Group)
	 */
	public String isUserAssignableFromGroupToGroup(User user, Group sourceGroup, Group targetGroup)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#isUserSuitedForGroup(com.idega.user.data.User, com.idega.user.data.Group)
	 */
	public String isUserSuitedForGroup(User user, Group targetGroup) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	 * Clubs must start their name with "Golf".
	 * @return A list of golfclubs (Groups)
	 */
	public Collection getGolfClubs(){
		//if(clubs==null){
			try {
				clubs = getGroupBusiness().getGroupsByGroupTypeAndFirstPartOfName(GolfConstants.GROUP_TYPE_CLUB,"Golf");
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		//}
		return clubs;
	}
	
	public GroupBusiness getGroupBusiness(){
		if(groupBiz==null){
			try {
				groupBiz = (GroupBusiness)this.getServiceInstance(GroupBusiness.class);
			}
			catch (IBOLookupException e) {
				e.printStackTrace();
			}
		}
		return groupBiz;
	}
	
	public UserBusiness getUserBusiness(){
		if(userBiz==null){
			try {
				userBiz = (UserBusiness)this.getServiceInstance(UserBusiness.class);
			}
			catch (IBOLookupException e) {
				e.printStackTrace();
			}
		}
		return userBiz;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#canCreateSubGroup(com.idega.user.data.Group)
	 */
	public String canCreateSubGroup(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
