package com.idega.block.trade.stockroom.business;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.data.PriceCategory;
import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierStaffGroup;
import com.idega.block.trade.stockroom.data.SupplierStaffGroupHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWUserContext;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;


/**
 * @author gimmi
 */
public class SupplierManagerBusinessBean extends IBOServiceBean  implements SupplierManagerBusiness{

	private static String SUPPLIER_MANAGER_GROUP_TYPE_COLLECTION = "supplier_manager_coll";
	private static String SUPPLIER_MANAGER_GROUP_TYPE = "supplier_manager";
	private static String SUPPLIER_MANAGER_USER_GROUP_TYPE = "supplier_manager_user";
	private static String SUPPLIER_MANAGER_ADMIN_GROUP_TYPE = "supplier_manager_admin";
	private static String SUPPLIER_MANAGER_RESELLER_GROUP_TYPE = "supplier_manager_reseller";
	private static String SUPPLIER_MANAGER_SUPPLIER_GROUP_TYPE = "supplier_manager_supplier";
	public static String SUPPLIER_MANAGER_ROLE_KEY = "supp_man_edit_role";

	public Group updateSupplierManager(Object pk, String name, String description) throws IDOLookupException, FinderException {
		GroupHome gHome = (GroupHome) IDOLookup.getHome(Group.class);
		Group manager = gHome.findByPrimaryKey(pk);
		manager.setGroupType(SUPPLIER_MANAGER_GROUP_TYPE);
		manager.setName(name);
		manager.setDescription(description);
		manager.store();
		return manager;
	}
	
	public Group createSupplierManager(String name, String description, String adminName, String loginName, String password, IWUserContext iwuc) throws RemoteException, CreateException {
		try {
			// Making sure the group type exist
			getGroupBusiness().getGroupTypeFromString(SUPPLIER_MANAGER_GROUP_TYPE);
		} catch (FinderException e1) {
			System.out.println("TravelBlock : groupType not found, creating");
			getGroupBusiness().createVisibleGroupType(SUPPLIER_MANAGER_GROUP_TYPE);
		}
		try {
			// Making sure the group type exist
			getGroupBusiness().getGroupTypeFromString(SUPPLIER_MANAGER_USER_GROUP_TYPE);
		} catch (FinderException e1) {
			System.out.println("TravelBlock : groupType not found, creating");
			getGroupBusiness().createVisibleGroupType(SUPPLIER_MANAGER_USER_GROUP_TYPE);
		}
		try {
			// Making sure the group type exist
			getGroupBusiness().getGroupTypeFromString(SUPPLIER_MANAGER_ADMIN_GROUP_TYPE);
		} catch (FinderException e1) {
			System.out.println("TravelBlock : groupType not found, creating");
			getGroupBusiness().createVisibleGroupType(SUPPLIER_MANAGER_ADMIN_GROUP_TYPE);
		}
		try {
			// Making sure the group type exist
			getGroupBusiness().getGroupTypeFromString(SUPPLIER_MANAGER_SUPPLIER_GROUP_TYPE);
		} catch (FinderException e1) {
			System.out.println("TravelBlock : groupType not found, creating");
			getGroupBusiness().createVisibleGroupType(SUPPLIER_MANAGER_SUPPLIER_GROUP_TYPE);
		}
		try {
			// Making sure the group type exist
			getGroupBusiness().getGroupTypeFromString(SUPPLIER_MANAGER_RESELLER_GROUP_TYPE);
		} catch (FinderException e1) {
			System.out.println("TravelBlock : groupType not found, creating");
			getGroupBusiness().createVisibleGroupType(SUPPLIER_MANAGER_RESELLER_GROUP_TYPE);
		}

		User user;
  	UserBusiness ub = (UserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), UserBusiness.class);
  	
  	if (adminName == null || "".equals(adminName)) {
  		adminName = name;
  	}
  	
    user = ub.insertUser(adminName, "", "", adminName, "staff", null, null, null);
    LoginDBHandler.createLogin(user.getID(), loginName, password);

  	Group manager = getGroupBusiness().createGroup(name, description, SUPPLIER_MANAGER_GROUP_TYPE, false);
  	Group users = getGroupBusiness().createGroup("Users", "User group for "+name, SUPPLIER_MANAGER_USER_GROUP_TYPE, false);
  	Group admin = getGroupBusiness().createGroup("Managers", "Manager group for "+name, SUPPLIER_MANAGER_ADMIN_GROUP_TYPE, false);
  	Group resellers = getGroupBusiness().createGroup("Resellers", "Resellers belonging to "+name, SUPPLIER_MANAGER_RESELLER_GROUP_TYPE, false);
  	Group suppliers = getGroupBusiness().createGroup("Suppliers", "Suppliers belonging to "+name, SUPPLIER_MANAGER_SUPPLIER_GROUP_TYPE, false);

		getSupplierManagerGroup().addGroup(manager);
		
  	manager.addGroup(users);
  	manager.addGroup(suppliers);
  	manager.addGroup(resellers);
  	
  	users.addGroup(admin);
  	if (user != null) {
			admin.addGroup(user);
      user.setPrimaryGroup(admin);
      user.store();
			try {
				getIWMainApplication().getAccessController().setAsOwner(manager, Integer.parseInt(user.getPrimaryKey().toString()), iwuc);
			}
			catch (NumberFormatException e2) {
				e2.printStackTrace();
			}
			catch (EJBException e2) {
				e2.printStackTrace();
			}
			catch (Exception e2) {
				e2.printStackTrace();
			}
		}
  	getIWMainApplication().getAccessController().addRoleToGroup(SUPPLIER_MANAGER_ROLE_KEY, admin, iwuc);
  	return manager;
	}
	
  public Group getSupplierManagerGroup() {
  	try {
  		Collection coll = getGroupBusiness().getGroups(new String[] {SUPPLIER_MANAGER_GROUP_TYPE_COLLECTION}, true);
  		if (coll != null && coll.size() != 1) {
  			if (coll.isEmpty()) {
  				try {
  					getGroupBusiness().getGroupTypeFromString(SUPPLIER_MANAGER_GROUP_TYPE_COLLECTION);
  				} catch (FinderException e) {
  					System.out.println("TravelBlock : groupType not found, creating");
  					getGroupBusiness().createVisibleGroupType(SUPPLIER_MANAGER_GROUP_TYPE_COLLECTION);
  				}
  		  	return getGroupBusiness().createGroup("Supplier Managers", "Group containing group managers", SUPPLIER_MANAGER_GROUP_TYPE_COLLECTION, true);
  			} else {
  				System.err.println("TravelBlock : "+coll.size()+" supplier manager groups found !!! should only be 1");
    			Iterator iter = coll.iterator();
    			return (Group) iter.next();
  			}
  		} else if (coll != null) {
  			Iterator iter = coll.iterator();
  			return (Group) iter.next();
  		} else  {
				System.err.println("TravelBlock : NULL supplier manager groups found !!! should only be 1");
  			return null;
  		}
  	} catch (Exception e) {
  		e.printStackTrace();
		}
  	return null;
  }	
    
  public Group getSupplierManager(User user) throws RemoteException {
  	Collection coll = getGroupBusiness().getParentGroupsRecursive(user, new String[]{SUPPLIER_MANAGER_GROUP_TYPE}, true);
  	if (coll != null && !coll.isEmpty()) {
  		Iterator iter = coll.iterator();
  		return (Group) iter.next();
  	}
  	return null;
  }
	
	public GroupBusiness getGroupBusiness() {
		try {
			return (GroupBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), GroupBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	
// GAMLI SUPPLIER MANAGER
	
	public static String PRICE_CATEGORY_FULL_PRICE_DEFAULT_NAME = "default full price";
  public static String SUPPLIER_ADMINISTRATOR_GROUP_DESCRIPTION = "Supplier administator group";

  public static String permissionGroupNameExtention = " - admins";


  public void deleteSupplier(int id)throws Exception{
    invalidateSupplier(((com.idega.block.trade.stockroom.data.SupplierHome)com.idega.data.IDOLookup.getHomeLegacy(Supplier.class)).findByPrimaryKeyLegacy(id));
  }

  public Supplier updateSupplier(int supplierId, String name, String description, int[] addressIds, int[] phoneIds, int[] emailIds) throws Exception {
      return createSupplier(supplierId, name, null,null, description, addressIds, phoneIds, emailIds);
  }

  public Supplier createSupplier(String name, String userName, String password, String description, int[] addressIds, int[] phoneIds, int[] emailIds) throws Exception {
    return createSupplier(-1, name, userName, password, description, addressIds, phoneIds, emailIds);
  }

  private Supplier createSupplier(int supplierId,String name, String userName, String password, String description, int[] addressIds, int[] phoneIds, int[] emailIds) throws Exception {
    try {
    boolean isUpdate = false;
    if (supplierId != -1) isUpdate = true;

    if (isUpdate) {
      Supplier supp = ((com.idega.block.trade.stockroom.data.SupplierHome)com.idega.data.IDOLookup.getHomeLegacy(Supplier.class)).findByPrimaryKeyLegacy(supplierId);
        supp.setName(name);
        supp.setDescription(description);
      supp.update();

      supp.removeFrom(com.idega.core.location.data.AddressBMPBean.getStaticInstance(Address.class));
      for (int i = 0; i < addressIds.length; i++) {
        supp.addTo(Address.class, addressIds[i]);
      }

      supp.removeFrom(com.idega.core.contact.data.PhoneBMPBean.getStaticInstance(Phone.class));
      for (int i = 0; i < phoneIds.length; i++) {
        supp.addTo(Phone.class, phoneIds[i]);
      }

      supp.removeFrom(com.idega.core.contact.data.EmailBMPBean.getStaticInstance(Email.class));
      for (int i = 0; i < emailIds.length; i++) {
        supp.addTo(Email.class, emailIds[i]);
      }

      return supp;

    }else {
      Supplier supp = ((com.idega.block.trade.stockroom.data.SupplierHome)com.idega.data.IDOLookup.getHome(Supplier.class)).create();
      supp.setName(name);
      supp.setDescription(description);
      supp.setIsValid(true);
      supp.insert();

      String sName = name+"_"+supp.getID();

      SupplierStaffGroup sGroup = ((SupplierStaffGroupHome)com.idega.data.IDOLookup.getHome(SupplierStaffGroup.class)).create();
      sGroup.setName(sName);
      sGroup.store();

      UserBusiness uBus = getUserBusiness();
      User user = uBus.insertUser(name,"","- admin",name+" - admin","Supplier administrator",null,IWTimestamp.RightNow(),null);
      LoginDBHandler.createLogin(user.getID(), userName, password);

      Group pGroup = ((GroupHome) IDOLookup.getHome(Group.class)).create();
      pGroup.setName(sName+permissionGroupNameExtention);
      pGroup.setDescription(SUPPLIER_ADMINISTRATOR_GROUP_DESCRIPTION);
      pGroup.store();
      
      
      pGroup.addGroup(user);
      sGroup.addGroup(user);

//      int[] userIDs = {user.getID()};
//
//      AccessControl ac = new AccessControl();
//      ac.createPermissionGroup(sName+permissionGroupNameExtention, SUPPLIER_ADMINISTRATOR_GROUP_DESCRIPTION, "", userIDs ,null);

      //sGroup.addTo(PermissionGroup.class, permissionGroupID);

      if(addressIds != null){
        for (int i = 0; i < addressIds.length; i++) {
          supp.addTo(Address.class, addressIds[i]);
        }
      }

      if(phoneIds != null){
        for (int i = 0; i < phoneIds.length; i++) {
          supp.addTo(Phone.class, phoneIds[i]);
        }
      }

      if(emailIds != null){
        for (int i = 0; i < emailIds.length; i++) {
          supp.addTo(Email.class, emailIds[i]);
        }
      }

      PriceCategory pCategory = ((com.idega.block.trade.stockroom.data.PriceCategoryHome)com.idega.data.IDOLookup.getHome(PriceCategory.class)).create();
        pCategory.setSupplierId(supp.getID());
        pCategory.setType(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_PRICE);
        pCategory.setDescription(PRICE_CATEGORY_FULL_PRICE_DEFAULT_NAME);
        pCategory.setName("Price");
        pCategory.setCountAsPerson(true);
        pCategory.setExtraInfo("PriceCategory created at "+IWTimestamp.RightNow().toSQLString()+" when creating "+supp.getName());
      pCategory.insert();


      supp.setGroupId(((Integer)sGroup.getPrimaryKey()).intValue());
      supp.update();

      return supp;
    }
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
      return null;
    }
  }

  public void invalidateSupplier(Supplier supplier) throws FinderException, RemoteException{
    supplier.setIsValid(false);
    supplier.store();
    List users = getUsers(supplier);
    if (users != null) {
      for (int i = 0; i < users.size(); i++) {
      	try {
      		LoginDBHandler.deleteUserLogin( ((User) users.get(i)).getID() );
      	} catch (Exception e) {
      		throw new FinderException(e.getMessage());
      	}
      }
    }
    Group pGroup = getPermissionGroup(supplier);
      pGroup.setName(pGroup.getName()+"_deleted");
      pGroup.store();

    SupplierStaffGroup sGroup = getSupplierStaffGroup(supplier);
      sGroup.setName(sGroup.getName()+"_deleted");
      sGroup.store();
  }

  public void validateSupplier(Supplier supplier) throws SQLException {
    supplier.setIsValid(true);
    supplier.update();
  }



  public Group getPermissionGroup(Supplier supplier) throws FinderException, RemoteException {
    String name = supplier.getName()+"_"+supplier.getID() + permissionGroupNameExtention;
    String description = SUPPLIER_ADMINISTRATOR_GROUP_DESCRIPTION ;
    Group pGroup = null;
    Collection coll = getGroupBusiness().getGroupHome().findGroupsByNameAndDescription(name, description);
    //List listi = EntityFinder.findAllByColumn((Group) com.idega.core.accesscontrol.data.PermissionGroupBMPBean.getStaticInstance(PermissionGroup.class), com.idega.core.accesscontrol.data.PermissionGroupBMPBean.getNameColumnName(), name, com.idega.core.accesscontrol.data.PermissionGroupBMPBean.getGroupDescriptionColumnName(), description);
    
    if (coll != null) {
      if (!coll.isEmpty()) {
      	Iterator iter = coll.iterator();
      	pGroup = (Group) iter.next();
        //pGroup = (Group) listi.get(listi.size()-1);
      }
    }
    coll = getGroupBusiness().getGroupHome().findGroupsByNameAndDescription(supplier.getName()+permissionGroupNameExtention, description);
    if (coll != null) {
      if (!coll.isEmpty()) {
      	Iterator iter = coll.iterator();
      	pGroup = (Group) iter.next();
        //pGroup = (Group) listi.get(listi.size()-1);
      }
    }
//    if (listi == null) {
//      listi = EntityFinder.findAllByColumn((Group) com.idega.core.accesscontrol.data.PermissionGroupBMPBean.getStaticInstance(PermissionGroup.class), com.idega.core.accesscontrol.data.PermissionGroupBMPBean.getNameColumnName(), supplier.getName()+permissionGroupNameExtention, com.idega.core.accesscontrol.data.PermissionGroupBMPBean.getGroupDescriptionColumnName(), description);
//      if (listi != null)
//      if (listi.size() > 0) {
//        pGroup = (Group) listi.get(listi.size()-1);
//      }
//    }
    return pGroup;
  }

  public SupplierStaffGroup getSupplierStaffGroup(Supplier supplier) throws RemoteException, FinderException{
    String name = supplier.getName()+"_"+supplier.getID();
    SupplierStaffGroup sGroup = null;
    SupplierStaffGroupHome ssgh = (SupplierStaffGroupHome) IDOLookup.getHome(SupplierStaffGroup.class);
    Collection coll = ssgh.findGroupsByName(name); 
//    List listi = EntityFinder.findAllByColumn((SupplierStaffGroup) com.idega.block.trade.stockroom.data.SupplierStaffGroupBMPBean.getStaticInstance(SupplierStaffGroup.class), com.idega.block.trade.stockroom.data.SupplierStaffGroupBMPBean.getNameColumnName(), name);
    if (coll != null) {
      if (!coll.isEmpty()) {
      	Iterator iter = coll.iterator();
      	sGroup = (SupplierStaffGroup) iter.next();
//        sGroup = (SupplierStaffGroup) listi.get(listi.size()-1);
      }
    }
    
    coll = ssgh.findGroupsByName(supplier.getName()); 
	  if (coll != null) {
	    if (!coll.isEmpty()) {
	    	Iterator iter = coll.iterator();
	    	sGroup = (SupplierStaffGroup) iter.next();
	//      sGroup = (SupplierStaffGroup) listi.get(listi.size()-1);
	    }
	  }   
//    if (listi == null) {
//      listi = EntityFinder.findAllByColumn((SupplierStaffGroup) com.idega.block.trade.stockroom.data.SupplierStaffGroupBMPBean.getStaticInstance(SupplierStaffGroup.class), com.idega.block.trade.stockroom.data.SupplierStaffGroupBMPBean.getNameColumnName(), supplier.getName());
//      if (listi != null)
//      if (listi.size() > 0) {
//        sGroup = (SupplierStaffGroup) listi.get(listi.size()-1);
//      }
//    }
    return sGroup;
  }

  public void addUser(Supplier supplier, User user, boolean addToPermissionGroup) throws FinderException, RemoteException{
    Group pGroup = getPermissionGroup(supplier);
    SupplierStaffGroup sGroup = getSupplierStaffGroup(supplier);
    if (addToPermissionGroup)
    	pGroup.addGroup(user);
//      pGroup.addUser(user);
    ((Group) sGroup).addGroup(user);
  }

  public List getUsersInPermissionGroup(Supplier supplier) throws RemoteException, FinderException {
      Group pGroup = getPermissionGroup(supplier);
      if (pGroup != null) {
      	Collection coll = getUserBusiness().getUsersInGroup( pGroup );
//        List users = getUserBusiness().getUsersInGroup(pGroup);
      	List users = new Vector(coll);
        java.util.Collections.sort(users, new com.idega.util.GenericUserComparator(com.idega.util.GenericUserComparator.NAME));
        return users;
      }else {
        return null;
      }
  }

  public List getUsersNotInPermissionGroup(Supplier supplier) throws RemoteException, FinderException {
    List allUsers = getUsers(supplier);
    List permUsers = getUsersInPermissionGroup(supplier);

    if (permUsers != null)
    allUsers.removeAll(permUsers);

    return allUsers;
  }

  public List getUsers(Supplier supplier) throws RemoteException, FinderException{
	  SupplierStaffGroup sGroup = getSupplierStaffGroup(supplier);
	  Collection coll = getUserBusiness().getUsersInGroup((Group) sGroup);
	  List users = new Vector(coll);
	  if (users != null) {
	    java.util.Collections.sort(users, new com.idega.util.GenericUserComparator(com.idega.util.GenericUserComparator.NAME));
	  }
	  return users;
  }

  public List getUsersIncludingResellers(Supplier supplier) throws RemoteException, FinderException {
    return getUsersIncludingResellers(supplier, false);
  }

  public List getUsersIncludingResellers(Supplier supplier, Object objBetweenResellers) throws RemoteException, FinderException {
    List users = getUsers(supplier);
    List temp;
    if (users == null) users = com.idega.util.ListUtil.getEmptyList();
    Iterator resellers = getResellerManager().getResellers(supplier, com.idega.block.trade.stockroom.data.ResellerBMPBean.getColumnNameName());
    while (resellers.hasNext()) {
      temp = getResellerManager().getUsersIncludingSubResellers((Reseller)resellers.next(), objBetweenResellers);
      if (temp != null)
      users.addAll(temp);
    }
    return users;
  }

  public List getUsersIncludingResellers(Supplier supplier, boolean includeSupplierUsers) throws RemoteException, FinderException {
    List users = new Vector();
    if (includeSupplierUsers) {
      users = getUsers(supplier);
    }
    List temp;
    if (users == null) users = com.idega.util.ListUtil.getEmptyList();
    Iterator resellers = getResellerManager().getResellers(supplier, com.idega.block.trade.stockroom.data.ResellerBMPBean.getColumnNameName());
    while (resellers.hasNext()) {
      temp = getResellerManager().getUsers((Reseller)resellers.next());
//      temp = ResellerManager.getUsersIncludingSubResellers((Reseller)resellers.next());
      if (temp != null) {
        users.addAll(temp);
      }
    }
    return users;
  }


  public User getMainUser(Supplier supplier) throws RemoteException, FinderException {
  	if (supplier.getGroupId() == -1) {
  		return null;
  	}
  	Group group = getGroupBusiness().getGroupHome().findByPrimaryKey(new Integer(supplier.getGroupId()));
  	Collection coll = getUserBusiness().getUsersInGroup(group);
  	List users = new Vector(coll);
    //List users = UserGroupBusiness.getUsersContained(((com.idega.core.data.GenericGroupHome)com.idega.data.IDOLookup.getHomeLegacy(GenericGroup.class)).findByPrimaryKeyLegacy(supplier.getGroupId()));
    if (users != null && users.size() > 0) {
      return (User) users.get(0);
    }else {
      return null;
    }
  }

  protected UserBusiness getUserBusiness() {
  	try {
			return (UserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), UserBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
  }  
  
	protected ResellerManager getResellerManager() {
		try {
			return (ResellerManager) IBOLookup.getServiceInstance(getIWApplicationContext(), ResellerManager.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	
}
