package com.idega.block.trade.stockroom.business;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.block.trade.stockroom.data.ResellerStaffGroup;
import com.idega.block.trade.stockroom.data.Supplier;
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
import com.idega.data.EntityControl;
import com.idega.data.EntityFinder;
import com.idega.data.SimpleQuerier;
import com.idega.transaction.IdegaTransactionManager;
import com.idega.util.CypherText;
import com.idega.util.IWTimestamp;

//import is.idega.idegaweb.travel.data.Contract;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ResellerManager {

  public static String permissionGroupNameExtention = " - admins";
  private static String permissionGroupDescription = "Reseller administator group";

  public ResellerManager() {
  }

  /**
   * @deprecated
   */
  public boolean deleteReseller(int id) {
    try {
      return deleteReseller(((com.idega.block.trade.stockroom.data.ResellerHome)com.idega.data.IDOLookup.getHomeLegacy(Reseller.class)).findByPrimaryKeyLegacy(id));
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
      return false;
    }
  }

  public static boolean deleteReseller(Reseller reseller) {
    TransactionManager tm = IdegaTransactionManager.getInstance();
    try {
      tm.begin();
      reseller.setIsValid(false);
      reseller.update();
      List users = getUsers(reseller);
      if (users != null) {
        for (int i = 0; i < users.size(); i++) {
          LoginDBHandler.deleteUserLogin( ((User) users.get(i)).getID() );
        }
      }
      PermissionGroup pGroup = getPermissionGroup(reseller);
        pGroup.setName(pGroup.getName()+"_deleted");
        pGroup.update();

      ResellerStaffGroup sGroup = getResellerStaffGroup(reseller);
        sGroup.setName(sGroup.getName()+"_deleted");
        sGroup.update();
      tm.commit();
      return true;
    }catch (Exception e) {
      e.printStackTrace(System.err);
      try {
        tm.rollback();
      }catch (SystemException se) {
        se.printStackTrace(System.err);
      }
      return false;
    }
  }

  public static Reseller updateReseller(int resellerId, String name, String description, int[] addressIds, int[] phoneIds, int[] emailIds) throws Exception {
      return createReseller(resellerId, null, name, null,null, description, addressIds, phoneIds, emailIds);
  }

  public static Reseller createReseller(Reseller parentReseller, String name, String userName, String password, String description, int[] addressIds, int[] phoneIds, int[] emailIds) throws Exception {
    return createReseller(-1, parentReseller, name, userName, password, description, addressIds, phoneIds, emailIds);
  }

  private static Reseller createReseller(int resellerId, Reseller parentReseller, String name, String userName, String password, String description, int[] addressIds, int[] phoneIds, int[] emailIds) throws Exception {
    boolean isUpdate = false;
    if (resellerId != -1) isUpdate = true;

    if (description == null) description = "";


    if (isUpdate) {
      Reseller res = ((com.idega.block.trade.stockroom.data.ResellerHome)com.idega.data.IDOLookup.getHomeLegacy(Reseller.class)).findByPrimaryKeyLegacy(resellerId);
        res.setName(name);
        res.setDescription(description);
      res.update();

      res.removeFrom(com.idega.core.location.data.AddressBMPBean.getStaticInstance(Address.class));
      for (int i = 0; i < addressIds.length; i++) {
        res.addTo(Address.class, addressIds[i]);
      }

      res.removeFrom(com.idega.core.contact.data.PhoneBMPBean.getStaticInstance(Phone.class));
      for (int i = 0; i < phoneIds.length; i++) {
        res.addTo(Phone.class, phoneIds[i]);
      }

      res.removeFrom(com.idega.core.contact.data.EmailBMPBean.getStaticInstance(Email.class));
      for (int i = 0; i < emailIds.length; i++) {
        res.addTo(Email.class, emailIds[i]);
      }
      return res;
    }
    else {
      Reseller reseller = ((com.idega.block.trade.stockroom.data.ResellerHome)com.idega.data.IDOLookup.getHomeLegacy(Reseller.class)).createLegacy();
      CypherText cyph = new CypherText();
      String key = cyph.getKey(10);
      String[] check = SimpleQuerier.executeStringQuery("SELECT "+reseller.getIDColumnName()+" from "+com.idega.block.trade.stockroom.data.ResellerBMPBean.getResellerTableName()+" where "+com.idega.block.trade.stockroom.data.ResellerBMPBean.getColumnNameReferenceNumber()+" = '"+key+"'");
      while (check.length > 0) {
        cyph.getKey(10);
        check = SimpleQuerier.executeStringQuery("SELECT "+reseller.getIDColumnName()+" from "+com.idega.block.trade.stockroom.data.ResellerBMPBean.getResellerTableName()+" where "+com.idega.block.trade.stockroom.data.ResellerBMPBean.getColumnNameReferenceNumber()+" = '"+key+"'");
      }
      reseller.setName(name);
      reseller.setDescription(description);
      reseller.setIsValid(true);
      reseller.setReferenceNumber(key);
      reseller.insert();

      ResellerStaffGroup sGroup = ((com.idega.block.trade.stockroom.data.ResellerStaffGroupHome)com.idega.data.IDOLookup.getHomeLegacy(ResellerStaffGroup.class)).createLegacy();
      sGroup.setName(name+"_"+reseller.getID());
      sGroup.insert();


      if (parentReseller != null) {
        parentReseller.addChild(reseller);
      }

      UserBusiness uBus = new UserBusiness();
      User user = uBus.insertUser(name,"","- admin",name+" - admin","Reseller administrator",null,IWTimestamp.RightNow(),null);
      LoginDBHandler.createLogin(user.getID(), userName, password);

      sGroup.addUser(user);

      int[] userIDs = {user.getID()};


      AccessControl ac = new AccessControl();
      ac.createPermissionGroup(name+"_"+reseller.getID()+permissionGroupNameExtention, permissionGroupDescription, "", userIDs ,null);

      if(addressIds != null){
        for (int i = 0; i < addressIds.length; i++) {
          reseller.addTo(Address.class, addressIds[i]);
        }
      }

      if(phoneIds != null){
        for (int i = 0; i < phoneIds.length; i++) {
          reseller.addTo(Phone.class, phoneIds[i]);
        }
      }

      if(emailIds != null){
        for (int i = 0; i < emailIds.length; i++) {
          reseller.addTo(Email.class, emailIds[i]);
        }
      }

      reseller.setGroupId(sGroup.getID());
      reseller.update();

      return reseller;
    }
  }

  public static void invalidateReseller(Reseller reseller) throws SQLException {
    if (!deleteReseller(reseller)) {
      throw new SQLException("InvalidateReseller");
    }
  }


  public static void validateReseller(Reseller reseller) throws SQLException {
    reseller.setIsValid(true);
    reseller.update();
  }

  /**
   * @deprecated
   */
  public void updateSupplier(Supplier supplier, String name, String description, int[] addressIds, int[] phoneIds, int[] emailIds) throws Exception {
    supplier.setName(name);
    supplier.setDescription(description);
    supplier.update();
    /**
     * implementa
     */
/*
    supplier.reverseRemoveFrom(com.idega.core.data.AddressBMPBean.getStaticInstance(Address.class));

    if(addressIds != null){
      for (int i = 0; i < addressIds.length; i++) {
        supplier.addTo(Address.class, addressIds[i]);
      }
    }

    if(phoneIds != null){
      if (phoneIds.length == 0) {
        supplier.reverseRemoveFrom(com.idega.core.data.PhoneBMPBean.getStaticInstance(Phone.class));
      }
    }

    if(emailIds != null){
      if (emailIds.length == 0) {
        supplier.reverseRemoveFrom(com.idega.core.data.PhoneBMPBean.getStaticInstance(Email.class));
      }
    }
*/

  }



  public static Supplier[] getSuppliers(int resellerId) {
    return getSuppliers(resellerId,"");
  }

  public static Supplier[] getSuppliers(int resellerId, String orderBy) {
    Supplier[] suppliers = {};
    try {
        Reseller reseller = (Reseller) com.idega.block.trade.stockroom.data.ResellerBMPBean.getStaticInstance(Reseller.class);
        Supplier supplier = (Supplier) com.idega.block.trade.stockroom.data.SupplierBMPBean.getStaticInstance(Supplier.class);

        StringBuffer sql = new StringBuffer();
          sql.append("Select s.* from "+com.idega.block.trade.stockroom.data.ResellerBMPBean.getResellerTableName()+" r, "+com.idega.block.trade.stockroom.data.SupplierBMPBean.getSupplierTableName()+" s, ");
          sql.append(EntityControl.getManyToManyRelationShipTableName(Reseller.class, Supplier.class)+" rs");
          sql.append(" WHERE ");
          sql.append(" r."+reseller.getIDColumnName()+" = "+resellerId);
          sql.append(" AND ");
          sql.append(" s."+supplier.getIDColumnName()+" = rs."+supplier.getIDColumnName());
          sql.append(" AND ");
          sql.append(" r."+reseller.getIDColumnName()+" = rs."+reseller.getIDColumnName());
          sql.append(" AND ");
          sql.append("s."+com.idega.block.trade.stockroom.data.SupplierBMPBean.getColumnNameIsValid()+" = 'Y'");
          if (!orderBy.equals("")) {
            sql.append(" ORDER BY s."+orderBy);
          }

        suppliers = (Supplier[]) (com.idega.block.trade.stockroom.data.SupplierBMPBean.getStaticInstance(Supplier.class)).findAll(sql.toString());

    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }

    return suppliers;
  }

  public static Iterator getResellerChilds(Reseller reseller) {
    return getResellerChilds(reseller, "");
  }

  public static Iterator getResellerChilds(Reseller reseller, String orderBy) {
    Iterator iter = reseller.getChildrenIterator(orderBy);
    if (iter != null) {
    /*  List listi = new Vector();
      Reseller tempReseller;
      while (iter.hasNext()) {
        tempReseller = (Reseller) iter.next();
        if ((tempReseller.getID() != reseller.getID()) && (tempReseller.getID() != reseller.getParent().getID())) {
          listi.add(tempReseller);
        }
      }
      iter = listi.iterator();*/
    }else if (iter == null) {
      iter = com.idega.util.ListUtil.getEmptyList().iterator();
    }
    return iter;
  }

  public static List getResellersAvailable(Reseller reseller) throws SQLException {
    return getResellersAvailable(reseller, null);
  }
  public static List getResellersAvailable(Reseller reseller, String orderBy) throws SQLException {
    List list = null;
    if (reseller != null) {
      list = new Vector();
      int[] exclude = new int[0];
      if (reseller.getParent() != null) {
        exclude = new int[] {reseller.getID(), reseller.getParent().getID()};
      }else {
        exclude = new int[] {reseller.getID()};
      }

      StringBuffer buff = new StringBuffer();
        buff.append("SELECT * FROM "+com.idega.block.trade.stockroom.data.ResellerBMPBean.getResellerTableName());
        buff.append(" WHERE ");
        buff.append(com.idega.block.trade.stockroom.data.ResellerBMPBean.getColumnNameIsValid()+" = 'Y'");
        if (exclude.length > 0) {
          buff.append(" AND ");
          buff.append(reseller.getIDColumnName()+" not in (");
          for (int i = 0; i < exclude.length; i++) {
            if (i != 0)
              buff.append(", ");
            buff.append(exclude[i]);
          }
          buff.append(") ");
        }
        if (orderBy != null && !orderBy.equals("")) {
          buff.append(" ORDER BY "+orderBy);
        }

      list = EntityFinder.findAll(reseller, buff.toString());
    }
    return list;
  }

  public static Iterator getResellers(Supplier supplier) {
    return getResellers(supplier,"");
  }

  public static Iterator getResellers(Supplier supplier, String orderBy) {
    Iterator iter = null;
    try {
        Reseller reseller = (Reseller) com.idega.block.trade.stockroom.data.ResellerBMPBean.getStaticInstance(Reseller.class);
//        Supplier supplier = (Supplier) com.idega.block.trade.stockroom.data.SupplierBMPBean.getStaticInstance(Supplier.class);
        int supplierId = supplier.getID();

        StringBuffer sql = new StringBuffer();
          sql.append("Select r.* from "+com.idega.block.trade.stockroom.data.ResellerBMPBean.getResellerTableName()+" r, "+com.idega.block.trade.stockroom.data.SupplierBMPBean.getSupplierTableName()+" s, ");
          sql.append(EntityControl.getManyToManyRelationShipTableName(Reseller.class, Supplier.class)+" rs");
          sql.append(" WHERE ");
          sql.append(" s."+supplier.getIDColumnName()+" = "+supplierId);
          sql.append(" AND ");
          sql.append(" s."+supplier.getIDColumnName()+" = rs."+supplier.getIDColumnName());
          sql.append(" AND ");
          sql.append(" r."+reseller.getIDColumnName()+" = rs."+reseller.getIDColumnName());
          sql.append(" AND ");
          sql.append("r."+com.idega.block.trade.stockroom.data.ResellerBMPBean.getColumnNameIsValid()+" = 'Y'");
          if (!orderBy.equals("")) {
            sql.append(" ORDER BY r."+orderBy);
          }

        List list = EntityFinder.findAll(com.idega.block.trade.stockroom.data.ResellerBMPBean.getStaticInstance(Reseller.class), sql.toString());
//        resellers = (Reseller[]) (com.idega.block.trade.stockroom.data.ResellerBMPBean.getStaticInstance(Reseller.class)).findAll(sql.toString());
        if (list != null) {
          iter = list.iterator();
        }else {
          iter = com.idega.util.ListUtil.getEmptyList().iterator();
        }
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }

    return iter;
  }



/*
  public static Product[] getProductsWithContracts(Reseller ownerReseller, int contractedResellerId) {
    return getProductsWithContracts(ownerReseller, contractedResellerId,  null);
  }

  public static Product[] getProductsWithContracts(Reseller ownerReseller, int contractedResellerId, String orderBy) {
    return getProductsWithContracts(ownerReseller.getID(), contractedResellerId, -1, orderBy);
  }
*/

  public static PermissionGroup getPermissionGroup(Reseller reseller) throws SQLException{
    String name = reseller.getName()+"_"+reseller.getID() + permissionGroupNameExtention;
    String description = permissionGroupDescription;

    PermissionGroup pGroup = null;
    List listi = EntityFinder.findAllByColumn((PermissionGroup) com.idega.core.accesscontrol.data.PermissionGroupBMPBean.getStaticInstance(PermissionGroup.class), com.idega.core.accesscontrol.data.PermissionGroupBMPBean.getNameColumnName(), name, com.idega.core.accesscontrol.data.PermissionGroupBMPBean.getGroupDescriptionColumnName(), description  );
    if (listi != null) {
      if (listi.size() > 0) {
        pGroup = (PermissionGroup) listi.get(listi.size()-1);
      }
    }

    if (listi == null) {
      listi = EntityFinder.findAllByColumn((PermissionGroup) com.idega.core.accesscontrol.data.PermissionGroupBMPBean.getStaticInstance(PermissionGroup.class), com.idega.core.accesscontrol.data.PermissionGroupBMPBean.getNameColumnName(), reseller.getName()+ permissionGroupNameExtention, com.idega.core.accesscontrol.data.PermissionGroupBMPBean.getGroupDescriptionColumnName(), description  );
      if (listi != null) {
        if (listi.size() > 0) {
          pGroup = (PermissionGroup) listi.get(listi.size()-1);
        }
      }
    }
    return pGroup;
  }

  public static ResellerStaffGroup getResellerStaffGroup(Reseller reseller) throws SQLException {
    String name = reseller.getName()+"_"+reseller.getID();
    ResellerStaffGroup sGroup = null;

    List listi = EntityFinder.findAllByColumn((ResellerStaffGroup) com.idega.block.trade.stockroom.data.ResellerStaffGroupBMPBean.getStaticInstance(ResellerStaffGroup.class), com.idega.block.trade.stockroom.data.ResellerStaffGroupBMPBean.getNameColumnName(), name);
    if (listi != null) {
      if (listi.size() > 0) {
        sGroup = (ResellerStaffGroup) listi.get(listi.size()-1);
      }
    }

    if (listi == null) {
      listi = EntityFinder.findAllByColumn((ResellerStaffGroup) com.idega.block.trade.stockroom.data.ResellerStaffGroupBMPBean.getStaticInstance(ResellerStaffGroup.class), com.idega.block.trade.stockroom.data.ResellerStaffGroupBMPBean.getNameColumnName(), reseller.getName());
      if (listi != null) {
        if (listi.size() > 0) {
          sGroup = (ResellerStaffGroup) listi.get(listi.size()-1);
        }
      }
    }

    return sGroup;
  }

  public static void addUser(Reseller reseller, User user, boolean addToPermissionGroup) throws SQLException{
    PermissionGroup pGroup = getPermissionGroup(reseller);
    ResellerStaffGroup sGroup = getResellerStaffGroup(reseller);
    if (addToPermissionGroup)
      pGroup.addUser(user);
    sGroup.addUser(user);
  }


  public static List getUsersInPermissionGroup(Reseller reseller) {
    try {
      PermissionGroup pGroup = getPermissionGroup(reseller);
      List users = UserBusiness.getUsersInGroup(pGroup);
      if (users != null) {
        java.util.Collections.sort(users, new com.idega.util.GenericUserComparator(com.idega.util.GenericUserComparator.NAME));
      }
      return users;
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
      return null;
    }
  }

  public static List getUsersNotInPermissionGroup(Reseller reseller) {
      List allUsers = getUsers(reseller);
      List permUsers = getUsersInPermissionGroup(reseller);

      if (permUsers != null) {
        allUsers.removeAll(permUsers);
      }

      return allUsers;
  }

  public static List getUsers(Reseller reseller) {
    try {
      ResellerStaffGroup sGroup = getResellerStaffGroup(reseller);
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

  public static List getUsersIncludingSubResellers(Reseller reseller) {
    Iterator childs = ResellerManager.getResellerChilds(reseller, com.idega.block.trade.stockroom.data.ResellerBMPBean.getColumnNameName());
    List users = getUsers(reseller);
    List temp;

    while (childs.hasNext()) {
      temp = getUsers((Reseller) childs.next());
      if (temp != null) {
        users.addAll(temp);
      }
    }
    return users;
  }

  public static List getUsersIncludingSubResellers(Reseller reseller, Object objectBetweenResellers) {
    Iterator childs = ResellerManager.getResellerChilds(reseller, com.idega.block.trade.stockroom.data.ResellerBMPBean.getColumnNameName());
    List users = getUsers(reseller);
    List temp;

    while (childs.hasNext()) {
      if (objectBetweenResellers != null) {
        users.add(objectBetweenResellers);
      }

      temp = getUsers((Reseller) childs.next());
      if (temp != null) {
        users.addAll(temp);
      }
    }
//    System.err.println("Ur iter");
    return users;
  }

  public static Reseller getReseller(User user) throws SQLException{
    List groups = UserBusiness.getUserGroups(user);
    boolean isReseller = false;
    int number = 0;

    GenericGroup group;
    String type;
    if (groups != null) {
      for (int i = 0; i < groups.size(); i++) {
        group = (GenericGroup) groups.get(i);
        type = group.getGroupType();
        if (type != null && type.equals(com.idega.block.trade.stockroom.data.ResellerStaffGroupBMPBean.GROUP_TYPE_VALUE)) {
          isReseller = true;
          number= i;
          break;
        }
      }
    }

    if (isReseller) {
      Reseller[] resellers = com.idega.block.trade.stockroom.data.ResellerBMPBean.getValidResellers();
      GenericGroup rGroup = (GenericGroup) groups.get(number);
      for (int i = 0; i < resellers.length; i++) {
        if ((resellers[i].getName()+"_"+resellers[i].getID()).indexOf(rGroup.getName()) != -1) {
          return resellers[i];
        }
      }

    }

    return null;
  }

  public static User getMainUser(Reseller reseller) throws SQLException  {
    List users = UserGroupBusiness.getUsersContained(((com.idega.core.data.GenericGroupHome)com.idega.data.IDOLookup.getHomeLegacy(GenericGroup.class)).findByPrimaryKeyLegacy(reseller.getGroupId()));
    if (users != null && users.size() > 0) {
      return (User) users.get(0);
    }else {
      return null;
    }
  }

}
