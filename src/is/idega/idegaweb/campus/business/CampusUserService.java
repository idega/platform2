package is.idega.idegaweb.campus.business;

import is.idega.block.family.business.FamilyLogic;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;


public interface CampusUserService extends com.idega.business.IBOService,UserBusiness{
	
	public Group getTenantGroup()throws CreateException, FinderException, RemoteException;
	public Group getTenantFamilyGroup()throws CreateException, FinderException, RemoteException;
	public Group getCurrentTenantGroup()throws CreateException, FinderException, RemoteException;
	public Group getStaffGroup()throws CreateException, FinderException, RemoteException;
	public Group getAdminGroup()throws CreateException, FinderException, RemoteException;
	public FamilyLogic getFamilyService()throws RemoteException;
	public void setAsTenant(User tenant) throws CampusGroupException,RemoteException;
	public void setAsTenantSpouse(User tenant,User spouse)throws CampusGroupException,RemoteException;
	public void setAsTenantChild(User tenant,User child)throws CampusGroupException,RemoteException;
	public void setAsCurrentTenant(User tenant)throws CampusGroupException,RemoteException;
	public void removeAsCurrentTenant(User tenant,User currentUser)throws CampusGroupException,RemoteException;
	
}
