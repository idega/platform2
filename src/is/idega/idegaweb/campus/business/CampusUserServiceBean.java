/*
 * Created on Jan 25, 2004
 *
 */
package is.idega.idegaweb.campus.business;

import is.idega.idegaweb.member.business.MemberFamilyLogic;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWMainApplicationSettings;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusinessBean;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;

/**
 * CampusUserServiceBean
 * @author aron 
 * @version 1.0
 */
public class CampusUserServiceBean extends UserBusinessBean implements CampusUserService {
	private Group tenantGroup;
	private Group tenantFamilyGroup;
	private Group currentTenantGroup;
	private Group staffGroup;
	private Group adminGroup;
	
	public Group getTenantGroup()throws CreateException, FinderException, RemoteException{
		return getSpecialGroup(tenantGroup,"CAMPUS_TENANT_GROUP_ID","Tenant","Tenants that have at some point in time rented an apartment");
	}
	public Group getTenantFamilyGroup()throws CreateException, FinderException, RemoteException{
		return getSpecialGroup(tenantGroup,"CAMPUS_TENANT_FAMILY_GROUP_ID","Tenant families","Group for tenants families");
	}
	public Group getCurrentTenantGroup()throws CreateException, FinderException, RemoteException{
		return getSpecialGroup(tenantGroup,"CAMPUS_CURRENT_TENANT_GROUP_ID","Current tenants","Tenants currently renting apartments");
	}
	public Group getStaffGroup()throws CreateException, FinderException, RemoteException{
		return getSpecialGroup(tenantGroup,"CAMPUS_STAFF_GROUP_ID","Staff","Staff working with system");
	}
	public Group getAdminGroup()throws CreateException, FinderException, RemoteException{
		return getSpecialGroup(tenantGroup,"CAMPUS_ADMIN_GROUP_ID","Administrators","Super users for system");
	}

	private Group getSpecialGroup(Group specialGroup,String applicationPropertyName,String groupName,String groupDescription) throws CreateException, FinderException, RemoteException {
		//create the default group

		if (specialGroup != null)
			return specialGroup;

		final IWApplicationContext iwc = getIWApplicationContext();
		final IWMainApplicationSettings settings = iwc.getApplicationSettings();
		String groupId = settings.getProperty(applicationPropertyName);
		if (groupId != null) {
			final GroupHome groupHome = getGroupHome();
			specialGroup = groupHome.findByPrimaryKey(new Integer(groupId));
		}
		else {
			System.err.println("trying to store "+groupName);
			final GroupBusiness groupBusiness = getGroupBusiness();
			specialGroup = groupBusiness.createGroup(groupName, groupDescription);
			settings.setProperty(applicationPropertyName, specialGroup.getPrimaryKey());
		}
		return specialGroup;
	}
	
	public void setAsTenant(User tenant) throws CampusGroupException{
		try {
			getTenantGroup().addGroup(tenant);
		} catch (Exception e) {
			throw new CampusGroupException(e);
		}
	}
	
	public void setAsTenantSpouse(User tenant,User spouse)throws CampusGroupException{
		try {
			getFamilyService().setAsSpouseFor(spouse,tenant);
			getTenantFamilyGroup().addGroup(spouse);
		} catch (Exception e) {
			throw new CampusGroupException(e);
		}
	}
	
	public void setAsTenantChild(User tenant,User child)throws CampusGroupException{
		try {
			getFamilyService().setAsChildFor(child,tenant);
			getTenantFamilyGroup().addGroup(child);
		} catch (Exception e) {
			throw new CampusGroupException(e);
		}
	}
	
	public void setAsCurrentTenant(User tenant)throws CampusGroupException{
		try {
			getCurrentTenantGroup().addGroup(tenant);
		} catch (Exception e) {
			throw new CampusGroupException(e);
		}
	}
	
	public void removeAsCurrentTenant(User tenant,User currentUser)throws CampusGroupException{
		try {
			getCurrentTenantGroup().removeGroup(tenant,currentUser);
		} catch (Exception e) {
			throw new CampusGroupException(e);
		}
	}
	
	public MemberFamilyLogic getFamilyService()throws RemoteException{
		return (MemberFamilyLogic)getServiceInstance(MemberFamilyLogic.class);
	}
}
