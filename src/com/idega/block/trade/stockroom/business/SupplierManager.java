package com.idega.block.trade.stockroom.business;

import com.idega.block.trade.stockroom.data.*;
import com.idega.core.accesscontrol.data.PermissionGroup;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.user.data.User;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.data.*;
import com.idega.util.idegaTimestamp;
import com.idega.data.EntityFinder;
import java.util.*;
import java.sql.*;


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

  private static String permissionGroupNameExtention = " - admins";

  public SupplierManager(){
  }


  public void deleteSupplier(int id)throws Exception{
    throw new Exception("not implimented");
  }

  public static Supplier updateSupplier(int supplierId, String name, String description, int[] addressIds, int[] phoneIds, int[] emailIds) throws Exception {
      return createSupplier(supplierId, name, null,null, description, addressIds, phoneIds, emailIds);
  }

  public static Supplier createSupplier(String name, String userName, String password, String description, int[] addressIds, int[] phoneIds, int[] emailIds) throws Exception {
    return createSupplier(-1, name, userName, password, description, addressIds, phoneIds, emailIds);
  }

  private static Supplier createSupplier(int supplierId,String name, String userName, String password, String description, int[] addressIds, int[] phoneIds, int[] emailIds) throws Exception {

    boolean isUpdate = false;
    if (supplierId != -1) isUpdate = true;

    if (isUpdate) {
      Supplier supp = new Supplier(supplierId);
        supp.setName(name);
        supp.setDescription(description);
      supp.update();

      supp.removeFrom(Address.getStaticInstance(Address.class));
      for (int i = 0; i < addressIds.length; i++) {
        supp.addTo(Address.class, addressIds[i]);
      }

      supp.removeFrom(Phone.getStaticInstance(Phone.class));
      for (int i = 0; i < phoneIds.length; i++) {
        supp.addTo(Phone.class, phoneIds[i]);
      }

      supp.removeFrom(Email.getStaticInstance(Email.class));
      for (int i = 0; i < emailIds.length; i++) {
        supp.addTo(Email.class, emailIds[i]);
      }

      return supp;

    }else {
      SupplierStaffGroup sGroup = new SupplierStaffGroup();
      Supplier supp = new Supplier();

      sGroup.setName(name);
      sGroup.insert();

      supp.setName(name);
      supp.setDescription(description);
      supp.setGroupId(sGroup.getID());
      supp.setIsValid(true);

      supp.insert();

      UserBusiness uBus = new UserBusiness();
      User user = uBus.insertUser(name,"","- admin",name+" - admin","Supplier administrator",null,idegaTimestamp.RightNow(),null);
      LoginDBHandler.createLogin(user.getID(), userName, password);

      sGroup.addUser(user);

      int[] userIDs = {user.getID()};

      AccessControl ac = new AccessControl();
      int permissionGroupID = ac.createPermissionGroup(name+permissionGroupNameExtention, SUPPLIER_ADMINISTRATOR_GROUP_DESCRIPTION, "", userIDs ,null);

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

      PriceCategory pCategory = new PriceCategory();
        pCategory.setSupplierId(supp.getID());
        pCategory.setType(PriceCategory.PRICETYPE_PRICE);
        pCategory.setDescription(PRICE_CATEGORY_FULL_PRICE_DEFAULT_NAME);
        pCategory.setName("Price");
        pCategory.setExtraInfo("PriceCategory created at "+idegaTimestamp.RightNow().toSQLString()+" when creating "+supp.getName());
      pCategory.insert();

      return supp;
    }
  }

  /**
   * @deprecated
  public static PermissionGroup getPermissionGroup(Supplier supplier) {
    PermissionGroup pGroup = null;
    try {
      PermissionGroup pg = PermissionGroup.getStaticPermissionGroupInstance();
      GenericGroup group = new GenericGroup(supplier.getGroupId());
      if (group.getGroupType().equals(pg.getGroupTypeValue()) ) {
        pGroup = new PermissionGroup(group.getID());
      }

      if (pGroup == null && group != null) {
        GenericGroup[] gGroups = group.getAllGroupsContainingThis();
        for (int i = 0; i < gGroups.length; i++) {
          if (gGroups[i].getGroupType().equals(pg.getGroupTypeValue()) ) {
            pGroup = new PermissionGroup(group.getID());
            break;
          }
        }
      }

    }catch (Exception e) {
      e.printStackTrace();
    }
    return pGroup;
  }

   */

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
    String name = supplier.getName() + permissionGroupNameExtention;
    String description = SUPPLIER_ADMINISTRATOR_GROUP_DESCRIPTION ;

    PermissionGroup pGroup = null;
    List listi = EntityFinder.findAllByColumn((PermissionGroup) PermissionGroup.getStaticInstance(PermissionGroup.class), PermissionGroup.getNameColumnName(), name, PermissionGroup.getGroupDescriptionColumnName(), description);
    if (listi != null) {
      if (listi.size() > 0) {
        pGroup = (PermissionGroup) listi.get(listi.size()-1);
      }
    }

    return pGroup;
  }

  public static SupplierStaffGroup getSupplierStaffGroup(Supplier supplier) throws SQLException {
    String name = supplier.getName();
    SupplierStaffGroup sGroup = null;

    List listi = EntityFinder.findAllByColumn((SupplierStaffGroup) SupplierStaffGroup.getStaticInstance(SupplierStaffGroup.class), SupplierStaffGroup.getNameColumnName(), name);
    if (listi != null) {
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
      List users = UserBusiness.getUsersInGroup(pGroup);
      java.util.Collections.sort(users, new com.idega.util.GenericUserComparator(com.idega.util.GenericUserComparator.NAME));
      return users;
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
      return null;
    }
  }

  public static List getUsersNotInPermissionGroup(Supplier supplier) {
    try {
      List allUsers = getUsers(supplier);
      PermissionGroup pGroup = getPermissionGroup(supplier);
      List permUsers = getUsersInPermissionGroup(supplier);

      allUsers.removeAll(permUsers);

      return allUsers;
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
      return null;
    }
  }

  public static List getUsers(Supplier supplier) {
    try {
      SupplierStaffGroup sGroup = getSupplierStaffGroup(supplier);
      List users = UserBusiness.getUsersInGroup(sGroup);
      java.util.Collections.sort(users, new com.idega.util.GenericUserComparator(com.idega.util.GenericUserComparator.NAME));
      return users;
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
      return null;
    }
  }

} // Class SupplierManager