package com.idega.block.trade.stockroom.business;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.idega.block.trade.stockroom.data.PriceCategory;
import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierStaffGroup;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.PermissionGroup;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.data.GenericGroup;
import com.idega.core.location.data.Address;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.user.business.UserGroupBusiness;
import com.idega.core.user.data.User;
import com.idega.data.EntityFinder;
import com.idega.util.IWTimestamp;


/**
 * Title:        IW Trade
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class SupplierManager {

  public static String PRICE_CATEGORY_FULL_PRICE_DEFAULT_NAME = "default full price";
  public static String SUPPLIER_ADMINISTRATOR_GROUP_DESCRIPTION = "Supplier administator group";

  public static String permissionGroupNameExtention = " - admins";

  public SupplierManager(){
  }


  public void deleteSupplier(int id)throws Exception{
    invalidateSupplier(((com.idega.block.trade.stockroom.data.SupplierHome)com.idega.data.IDOLookup.getHomeLegacy(Supplier.class)).findByPrimaryKeyLegacy(id));
  }

  public static Supplier updateSupplier(int supplierId, String name, String description, int[] addressIds, int[] phoneIds, int[] emailIds) throws Exception {
      return createSupplier(supplierId, name, null,null, description, addressIds, phoneIds, emailIds);
  }

  public static Supplier createSupplier(String name, String userName, String password, String description, int[] addressIds, int[] phoneIds, int[] emailIds) throws Exception {
    return createSupplier(-1, name, userName, password, description, addressIds, phoneIds, emailIds);
  }

  private static Supplier createSupplier(int supplierId,String name, String userName, String password, String description, int[] addressIds, int[] phoneIds, int[] emailIds) throws Exception {
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

      SupplierStaffGroup sGroup = ((com.idega.block.trade.stockroom.data.SupplierStaffGroupHome)com.idega.data.IDOLookup.getHomeLegacy(SupplierStaffGroup.class)).createLegacy();
      sGroup.setName(sName);
      sGroup.insert();




      UserBusiness uBus = new UserBusiness();
      User user = uBus.insertUser(name,"","- admin",name+" - admin","Supplier administrator",null,IWTimestamp.RightNow(),null);
      LoginDBHandler.createLogin(user.getID(), userName, password);

      sGroup.addUser(user);

      int[] userIDs = {user.getID()};

      AccessControl ac = new AccessControl();
      ac.createPermissionGroup(sName+permissionGroupNameExtention, SUPPLIER_ADMINISTRATOR_GROUP_DESCRIPTION, "", userIDs ,null);

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


      supp.setGroupId(sGroup.getID());
      supp.update();

      return supp;
    }
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
      return null;
    }
  }

  public static void invalidateSupplier(Supplier supplier) throws SQLException {
    supplier.setIsValid(false);
    supplier.update();
    List users = getUsers(supplier);
    if (users != null) {
      for (int i = 0; i < users.size(); i++) {
        LoginDBHandler.deleteUserLogin( ((User) users.get(i)).getID() );
      }
    }
    PermissionGroup pGroup = getPermissionGroup(supplier);
      pGroup.setName(pGroup.getName()+"_deleted");
      pGroup.update();

    SupplierStaffGroup sGroup = getSupplierStaffGroup(supplier);
      sGroup.setName(sGroup.getName()+"_deleted");
      sGroup.update();
  }

  public static void validateSupplier(Supplier supplier) throws SQLException {
    supplier.setIsValid(true);
    supplier.update();
  }



  public static PermissionGroup getPermissionGroup(Supplier supplier) throws SQLException{
    String name = supplier.getName()+"_"+supplier.getID() + permissionGroupNameExtention;
    String description = SUPPLIER_ADMINISTRATOR_GROUP_DESCRIPTION ;

    PermissionGroup pGroup = null;
    List listi = EntityFinder.findAllByColumn((PermissionGroup) com.idega.core.accesscontrol.data.PermissionGroupBMPBean.getStaticInstance(PermissionGroup.class), com.idega.core.accesscontrol.data.PermissionGroupBMPBean.getNameColumnName(), name, com.idega.core.accesscontrol.data.PermissionGroupBMPBean.getGroupDescriptionColumnName(), description);
    if (listi != null) {
      if (listi.size() > 0) {
        pGroup = (PermissionGroup) listi.get(listi.size()-1);
      }
    }

    if (listi == null) {
      listi = EntityFinder.findAllByColumn((PermissionGroup) com.idega.core.accesscontrol.data.PermissionGroupBMPBean.getStaticInstance(PermissionGroup.class), com.idega.core.accesscontrol.data.PermissionGroupBMPBean.getNameColumnName(), supplier.getName()+permissionGroupNameExtention, com.idega.core.accesscontrol.data.PermissionGroupBMPBean.getGroupDescriptionColumnName(), description);
      if (listi != null)
      if (listi.size() > 0) {
        pGroup = (PermissionGroup) listi.get(listi.size()-1);
      }
    }

    return pGroup;
  }

  public static SupplierStaffGroup getSupplierStaffGroup(Supplier supplier) throws SQLException {
    String name = supplier.getName()+"_"+supplier.getID();
    SupplierStaffGroup sGroup = null;
    List listi = EntityFinder.findAllByColumn((SupplierStaffGroup) com.idega.block.trade.stockroom.data.SupplierStaffGroupBMPBean.getStaticInstance(SupplierStaffGroup.class), com.idega.block.trade.stockroom.data.SupplierStaffGroupBMPBean.getNameColumnName(), name);
    if (listi != null) {
      if (listi.size() > 0) {
        sGroup = (SupplierStaffGroup) listi.get(listi.size()-1);
      }
    }
    if (listi == null) {
      listi = EntityFinder.findAllByColumn((SupplierStaffGroup) com.idega.block.trade.stockroom.data.SupplierStaffGroupBMPBean.getStaticInstance(SupplierStaffGroup.class), com.idega.block.trade.stockroom.data.SupplierStaffGroupBMPBean.getNameColumnName(), supplier.getName());
      if (listi != null)
      if (listi.size() > 0) {
        sGroup = (SupplierStaffGroup) listi.get(listi.size()-1);
      }
    }
    return sGroup;
  }

  public static void addUser(Supplier supplier, User user, boolean addToPermissionGroup) throws SQLException{
    PermissionGroup pGroup = getPermissionGroup(supplier);
    SupplierStaffGroup sGroup = getSupplierStaffGroup(supplier);
    if (addToPermissionGroup)
      pGroup.addUser(user);
    sGroup.addUser(user);
  }

  public static List getUsersInPermissionGroup(Supplier supplier) {
    try {
      PermissionGroup pGroup = getPermissionGroup(supplier);
      if (pGroup != null) {
        List users = UserBusiness.getUsersInGroup(pGroup);
        java.util.Collections.sort(users, new com.idega.util.GenericUserComparator(com.idega.util.GenericUserComparator.NAME));
        return users;
      }else {
        return null;
      }
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
      return null;
    }
  }

  public static List getUsersNotInPermissionGroup(Supplier supplier) {
    List allUsers = getUsers(supplier);
    List permUsers = getUsersInPermissionGroup(supplier);

    if (permUsers != null)
    allUsers.removeAll(permUsers);

    return allUsers;
  }

  public static List getUsers(Supplier supplier) {
    try {
      SupplierStaffGroup sGroup = getSupplierStaffGroup(supplier);
      List users = UserBusiness.getUsersInGroup(sGroup);
      if (users != null) {
        java.util.Collections.sort(users, new com.idega.util.GenericUserComparator(com.idega.util.GenericUserComparator.NAME));
      }
      return users;
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
      return null;
    }
  }

  public static List getUsersIncludingResellers(Supplier supplier) {
    return getUsersIncludingResellers(supplier, false);
  }

  public static List getUsersIncludingResellers(Supplier supplier, Object objBetweenResellers) {
    List users = getUsers(supplier);
    List temp;
    if (users == null) users = com.idega.util.ListUtil.getEmptyList();
    Iterator resellers = ResellerManager.getResellers(supplier, com.idega.block.trade.stockroom.data.ResellerBMPBean.getColumnNameName());
    while (resellers.hasNext()) {
      temp = ResellerManager.getUsersIncludingSubResellers((Reseller)resellers.next(), objBetweenResellers);
      if (temp != null)
      users.addAll(temp);
    }
    return users;
  }

  public static List getUsersIncludingResellers(Supplier supplier, boolean includeSupplierUsers) {
    List users = new Vector();
    if (includeSupplierUsers) {
      users = getUsers(supplier);
    }
    List temp;
    if (users == null) users = com.idega.util.ListUtil.getEmptyList();
    Iterator resellers = ResellerManager.getResellers(supplier, com.idega.block.trade.stockroom.data.ResellerBMPBean.getColumnNameName());
    while (resellers.hasNext()) {
      temp = ResellerManager.getUsers((Reseller)resellers.next());
//      temp = ResellerManager.getUsersIncludingSubResellers((Reseller)resellers.next());
      if (temp != null) {
        users.addAll(temp);
      }
    }
    return users;
  }

  public static Supplier getSupplier(User user) throws SQLException{
    List groups = UserBusiness.getUserGroups(user);
    boolean isSupplier = false;
    int number = 0;

    GenericGroup group;
    String type;
    if (groups != null) {
      for (int i = 0; i < groups.size(); i++) {
        group = (GenericGroup) groups.get(i);
        type = group.getGroupType();
        if (type != null && type.equals(com.idega.block.trade.stockroom.data.SupplierStaffGroupBMPBean.GROUP_TYPE_VALUE)) {
          isSupplier = true;
          number= i;
          break;
        }
      }
    }

    if (isSupplier) {
      Supplier[] supps = com.idega.block.trade.stockroom.data.SupplierBMPBean.getValidSuppliers();
      SupplierStaffGroup sGroup = (SupplierStaffGroup) groups.get(number);
      for (int i = 0; i < supps.length; i++) {
        if ((supps[i].getName()+"_"+supps[i].getID()).indexOf(sGroup.getName()) != -1) {
          return supps[i];
        }
      }

    }
    return null;
  }


  public static User getMainUser(Supplier supplier) throws SQLException  {
    List users = UserGroupBusiness.getUsersContained(((com.idega.core.data.GenericGroupHome)com.idega.data.IDOLookup.getHomeLegacy(GenericGroup.class)).findByPrimaryKeyLegacy(supplier.getGroupId()));
    if (users != null && users.size() > 0) {
      return (User) users.get(0);
    }else {
      return null;
    }
  }

} // Class SupplierManager
