package com.idega.block.trade.stockroom.business;

import javax.transaction.SystemException;
import com.idega.transaction.IdegaTransactionManager;
import javax.transaction.TransactionManager;
import com.idega.block.trade.stockroom.data.*;
import java.sql.SQLException;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.business.UserGroupBusiness;
import com.idega.core.user.data.*;
import com.idega.util.idegaTimestamp;
import com.idega.core.accesscontrol.business.*;
import com.idega.core.data.*;
import com.idega.core.accesscontrol.data.*;
import com.idega.data.*;
import com.idega.util.CypherText;
import com.idega.data.SimpleQuerier;
import java.util.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;

import is.idega.idegaweb.travel.data.Contract;

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
      return deleteReseller(new Reseller(id));
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
      Reseller res = new Reseller(resellerId);
        res.setName(name);
        res.setDescription(description);
      res.update();

      res.removeFrom(Address.getStaticInstance(Address.class));
      for (int i = 0; i < addressIds.length; i++) {
        res.addTo(Address.class, addressIds[i]);
      }

      res.removeFrom(Phone.getStaticInstance(Phone.class));
      for (int i = 0; i < phoneIds.length; i++) {
        res.addTo(Phone.class, phoneIds[i]);
      }

      res.removeFrom(Email.getStaticInstance(Email.class));
      for (int i = 0; i < emailIds.length; i++) {
        res.addTo(Email.class, emailIds[i]);
      }
      return res;
    }
    else {
      Reseller reseller = new Reseller();
      CypherText cyph = new CypherText();
      String key = cyph.getKey(10);
      String[] check = SimpleQuerier.executeStringQuery("SELECT "+reseller.getIDColumnName()+" from "+Reseller.getResellerTableName()+" where "+Reseller.getColumnNameReferenceNumber()+" = '"+key+"'");
      while (check.length > 0) {
        cyph.getKey(10);
        check = SimpleQuerier.executeStringQuery("SELECT "+reseller.getIDColumnName()+" from "+Reseller.getResellerTableName()+" where "+Reseller.getColumnNameReferenceNumber()+" = '"+key+"'");
      }
      reseller.setName(name);
      reseller.setDescription(description);
      reseller.setIsValid(true);
      reseller.setReferenceNumber(key);
      reseller.insert();

      ResellerStaffGroup sGroup = new ResellerStaffGroup();
      sGroup.setName(name+"_"+reseller.getID());
      sGroup.insert();


      if (parentReseller != null) {
        parentReseller.addChild(reseller);
      }

      UserBusiness uBus = new UserBusiness();
      User user = uBus.insertUser(name,"","- admin",name+" - admin","Reseller administrator",null,idegaTimestamp.RightNow(),null);
      LoginDBHandler.createLogin(user.getID(), userName, password);

      sGroup.addUser(user);

      int[] userIDs = {user.getID()};


      AccessControl ac = new AccessControl();
      int permissionGroupID = ac.createPermissionGroup(name+"_"+reseller.getID()+permissionGroupNameExtention, permissionGroupDescription, "", userIDs ,null);

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
    supplier.reverseRemoveFrom(Address.getStaticInstance(Address.class));

    if(addressIds != null){
      for (int i = 0; i < addressIds.length; i++) {
        supplier.addTo(Address.class, addressIds[i]);
      }
    }

    if(phoneIds != null){
      if (phoneIds.length == 0) {
        supplier.reverseRemoveFrom(Phone.getStaticInstance(Phone.class));
      }
    }

    if(emailIds != null){
      if (emailIds.length == 0) {
        supplier.reverseRemoveFrom(Phone.getStaticInstance(Email.class));
      }
    }
*/

  }

  public static Reseller[] getResellers(int serviceId, idegaTimestamp stamp) {
    Reseller[] returner = {};
    try {
        Reseller reseller = (Reseller) Reseller.getStaticInstance(Reseller.class);

        String[] many = {};
          StringBuffer sql = new StringBuffer();
            sql.append("Select r.* from "+Contract.getContractTableName()+" c, "+Reseller.getResellerTableName()+" r");
            sql.append(" where ");
            sql.append(" c."+Contract.getColumnNameServiceId()+"="+serviceId);
            sql.append(" and ");
            sql.append(" c."+Contract.getColumnNameFrom()+" <= '"+stamp.toSQLDateString()+"'");
            sql.append(" and ");
            sql.append(" c."+Contract.getColumnNameTo()+" >= '"+stamp.toSQLDateString()+"'");
            sql.append(" and ");
            sql.append(" c."+Contract.getColumnNameResellerId()+" = r."+reseller.getIDColumnName());

        returner = (Reseller[]) reseller.findAll(sql.toString());

    }catch (Exception e) {
        e.printStackTrace(System.err);
    }

    return returner;

  }

  public static DropdownMenu getDropdownMenuWithProducts(IWContext iwc, int resellerId) {
    try {
      Product[] list = getProductsForReseller(iwc, resellerId);
      DropdownMenu menu = new DropdownMenu(((Product)Product.getStaticInstance(Product.class)).getEntityName());
      Product product;
      if (list != null && list.length > 0) {
        for (int i = 0; i < list.length; i++) {
          menu.addMenuElement(list[i].getID(), ProductBusiness.getProductNameWithNumber(list[i]));
        }
      }
      return menu;
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
      return new DropdownMenu(Product.getProductEntityName());
    }
  }


  public static Product[] getProductsForReseller(IWContext iwc, int resellerId) throws SQLException {
    Product[] products = {};
    /**
     * @todo Cache...
     */

    try {
        Reseller reseller = (Reseller) (Reseller.getStaticInstance(Reseller.class));
        Product product = (Product) (Product.getStaticInstance(Product.class));

        StringBuffer sql = new StringBuffer();
          sql.append("SELECT p.* FROM "+Reseller.getResellerTableName()+" r, "+Product.getProductEntityName()+" p, "+Contract.getContractTableName()+" c");
          sql.append(" WHERE ");
          sql.append(" c."+Contract.getColumnNameResellerId()+" = r."+reseller.getIDColumnName());
          sql.append(" AND ");
          sql.append(" c."+Contract.getColumnNameServiceId()+" = p."+product.getIDColumnName());
          sql.append(" AND ");
          sql.append(" r."+reseller.getIDColumnName() +" = "+resellerId);
          sql.append(" AND ");
          sql.append(" p."+Product.getColumnNameIsValid()+" = 'Y'");
//          sql.append(" ORDER BY p."+Product.getColumnNameProductName());

        products = (Product[]) product.findAll(sql.toString());

    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }

    return products;
    //(Product[]) Product.getStaticInstance(Product.class).findAllByColumnOrdered(Service.getIsValidColumnName(),"Y",Supplier.getStaticInstance(Supplier.class).getIDColumnName() , Integer.toString(supplierId), Product.getColumnNameProductName());
  }

  public static Supplier[] getSuppliers(int resellerId) {
    return getSuppliers(resellerId,"");
  }

  public static Supplier[] getSuppliers(int resellerId, String orderBy) {
    Supplier[] suppliers = {};
    try {
        Reseller reseller = (Reseller) Reseller.getStaticInstance(Reseller.class);
        Supplier supplier = (Supplier) Supplier.getStaticInstance(Supplier.class);

        StringBuffer sql = new StringBuffer();
          sql.append("Select s.* from "+Reseller.getResellerTableName()+" r, "+Supplier.getSupplierTableName()+" s, ");
          sql.append(EntityControl.getManyToManyRelationShipTableName(Reseller.class, Supplier.class)+" rs");
          sql.append(" WHERE ");
          sql.append(" r."+reseller.getIDColumnName()+" = "+resellerId);
          sql.append(" AND ");
          sql.append(" s."+supplier.getIDColumnName()+" = rs."+supplier.getIDColumnName());
          sql.append(" AND ");
          sql.append(" r."+reseller.getIDColumnName()+" = rs."+reseller.getIDColumnName());
          sql.append(" AND ");
          sql.append("s."+Supplier.getColumnNameIsValid()+" = 'Y'");
          if (!orderBy.equals("")) {
            sql.append(" ORDER BY s."+orderBy);
          }

        suppliers = (Supplier[]) (Supplier.getStaticInstance(Supplier.class)).findAll(sql.toString());

    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }

    return suppliers;
  }

  public static Iterator getResellerChilds(Reseller reseller) {
    return getResellerChilds(reseller, "");
  }

  public static Iterator getResellerChilds(Reseller reseller, String orderBy) {
    Iterator iter = reseller.getChildren(orderBy);
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
        buff.append("SELECT * FROM "+reseller.getResellerTableName());
        buff.append(" WHERE ");
        buff.append(reseller.getColumnNameIsValid()+" = 'Y'");
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
        Reseller reseller = (Reseller) Reseller.getStaticInstance(Reseller.class);
//        Supplier supplier = (Supplier) Supplier.getStaticInstance(Supplier.class);
        int supplierId = supplier.getID();

        StringBuffer sql = new StringBuffer();
          sql.append("Select r.* from "+Reseller.getResellerTableName()+" r, "+Supplier.getSupplierTableName()+" s, ");
          sql.append(EntityControl.getManyToManyRelationShipTableName(Reseller.class, Supplier.class)+" rs");
          sql.append(" WHERE ");
          sql.append(" s."+supplier.getIDColumnName()+" = "+supplierId);
          sql.append(" AND ");
          sql.append(" s."+supplier.getIDColumnName()+" = rs."+supplier.getIDColumnName());
          sql.append(" AND ");
          sql.append(" r."+reseller.getIDColumnName()+" = rs."+reseller.getIDColumnName());
          sql.append(" AND ");
          sql.append("r."+Reseller.getColumnNameIsValid()+" = 'Y'");
          if (!orderBy.equals("")) {
            sql.append(" ORDER BY r."+orderBy);
          }

        List list = EntityFinder.findAll(Reseller.getStaticInstance(Reseller.class), sql.toString());
//        resellers = (Reseller[]) (Reseller.getStaticInstance(Reseller.class)).findAll(sql.toString());
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

  public static Supplier[] getSuppliersWithContracts(int resellerId) {
    return getSuppliersWithContracts(resellerId, null);
  }

  public static Supplier[] getSuppliersWithContracts(int resellerId, String orderBy) {
    Supplier[] suppliers =  {};
    try {
      Supplier supplier = (Supplier) Supplier.getStaticInstance(Supplier.class);
      Product product = (Product) Product.getStaticInstance(Product.class);
      Contract contract = (Contract) Contract.getStaticInstance(Contract.class);

      StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT distinct(s.*) FROM "+supplier.getSupplierTableName()+" s, "+contract.getContractTableName() +" c, "+product.getEntityName()+" p");
        buffer.append(" WHERE ");
        buffer.append("c."+contract.getColumnNameResellerId()+" = "+resellerId);
        buffer.append(" AND ");
        buffer.append("c."+contract.getColumnNameServiceId()+" = p."+product.getIDColumnName());
        buffer.append(" AND ");
        buffer.append("p."+product.getColumnNameSupplierId()+" = s."+supplier.getIDColumnName());
        if (orderBy != null && !orderBy.equals("")) {
        buffer.append(" ORDER BY s."+orderBy);
        }

      suppliers = (Supplier[]) supplier.findAll(buffer.toString());
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
    return suppliers;
  }

  public static Product[] getProductsWithContracts(Reseller reseller) {
    return getProductsWithContracts(reseller, null);
  }

  public static Product[] getProductsWithContracts(Reseller reseller, String orderBy) {
    Reseller parent = (Reseller) reseller.getParent();
    if (parent == null) {
      return getProductsWithContracts(-1, reseller.getID(),-1, orderBy);
    }else {
      return getProductsWithContracts(parent, orderBy);
    }
  }
/*
  public static Product[] getProductsWithContracts(Reseller ownerReseller, int contractedResellerId) {
    return getProductsWithContracts(ownerReseller, contractedResellerId,  null);
  }

  public static Product[] getProductsWithContracts(Reseller ownerReseller, int contractedResellerId, String orderBy) {
    return getProductsWithContracts(ownerReseller.getID(), contractedResellerId, -1, orderBy);
  }
*/
  public static Product[] getProductsWithContracts(int resellerId, int supplierId) {
    return getProductsWithContracts(resellerId, supplierId, null);
  }

  public static Product[] getProductsWithContracts(int resellerId, int supplierId, String orderBy) {
    return getProductsWithContracts(-1, resellerId ,supplierId, orderBy);
  }

  private static Product[] getProductsWithContracts(int ownerResellerId, int contractedResellerId, int supplierId, String orderBy) {
    Product[] products =  {};
    try {
      Product product = (Product) Product.getStaticInstance(Product.class);
      Contract contract = (Contract) Contract.getStaticInstance(Contract.class);
      Reseller reseller = (Reseller) Reseller.getStaticInstance(Reseller.class);

      StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT distinct(p.*) FROM  "+contract.getContractTableName() +" c");
        buffer.append(", "+product.getEntityName()+" p");
        if (ownerResellerId != -1) {
          buffer.append(", "+reseller.getTreeRelationshipTableName(reseller)+" r");
        }
        buffer.append(" WHERE ");
        buffer.append("c."+contract.getColumnNameResellerId()+" = "+contractedResellerId);
        buffer.append(" AND ");
        buffer.append("c."+contract.getColumnNameServiceId()+" = p."+product.getIDColumnName());
        if (supplierId != -1) {
          buffer.append(" AND ");
          buffer.append("p."+product.getColumnNameSupplierId()+" = "+supplierId);
        }else if (ownerResellerId != -1) {
          buffer.append(" AND ");
          buffer.append("r."+reseller.getTreeRelationshipChildColumnName(reseller)+"="+contractedResellerId);
          buffer.append(" AND ");
          buffer.append("r."+reseller.getIDColumnName()+"="+ownerResellerId);
        }
        if (orderBy != null && !orderBy.equals("")) {
//          buffer.append(" ORDER BY p."+orderBy);
        }

      products = (Product[]) product.findAll(buffer.toString());
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
    return products;
  }

  public static boolean isActiveContract(int resellerId, int productId) {
    return isActiveContract(-1, resellerId, productId);
  }

  public static boolean isActiveContract(int supplierId, int resellerId, int productId) {
    boolean returner = false;

    try {
      Product product = (Product) Product.getStaticInstance(Product.class);
      Contract contract = (Contract) Contract.getStaticInstance(Contract.class);

      StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT distinct(c.*) FROM  "+contract.getContractTableName() +" c");
        if (supplierId != -1) {
          buffer.append(", "+product.getEntityName()+" p");
        }
        buffer.append(" WHERE ");
        buffer.append("c."+contract.getColumnNameResellerId()+" = "+resellerId);
        buffer.append(" AND ");
        buffer.append("c."+contract.getColumnNameServiceId()+" = "+productId);
        if (supplierId != -1) {
          buffer.append(" AND ");
          buffer.append("p."+product.getIDColumnName()+" = c."+contract.getColumnNameServiceId());
          buffer.append(" AND ");
          buffer.append("p."+product.getColumnNameSupplierId()+" = "+supplierId);
        }

      String[] resuls = SimpleQuerier.executeStringQuery(buffer.toString());
      if (resuls != null && resuls.length > 0) {
        returner = true;
      }

      //products = (Product[]) product.findAll(buffer.toString());
    }catch (Exception sql) {
      sql.printStackTrace(System.err);
    }



    return returner;
  }

  public static PermissionGroup getPermissionGroup(Reseller reseller) throws SQLException{
    String name = reseller.getName()+"_"+reseller.getID() + permissionGroupNameExtention;
    String description = permissionGroupDescription;

    PermissionGroup pGroup = null;
    List listi = EntityFinder.findAllByColumn((PermissionGroup) PermissionGroup.getStaticInstance(PermissionGroup.class), PermissionGroup.getNameColumnName(), name, PermissionGroup.getGroupDescriptionColumnName(), description  );
    if (listi != null) {
      if (listi.size() > 0) {
        pGroup = (PermissionGroup) listi.get(listi.size()-1);
      }
    }
    if (listi == null) {
      listi = EntityFinder.findAllByColumn((PermissionGroup) PermissionGroup.getStaticInstance(PermissionGroup.class), PermissionGroup.getNameColumnName(), reseller.getName()+ permissionGroupNameExtention, PermissionGroup.getGroupDescriptionColumnName(), description  );
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

    List listi = EntityFinder.findAllByColumn((ResellerStaffGroup) ResellerStaffGroup.getStaticInstance(ResellerStaffGroup.class), ResellerStaffGroup.getNameColumnName(), name);
    if (listi != null) {
      if (listi.size() > 0) {
        sGroup = (ResellerStaffGroup) listi.get(listi.size()-1);
      }
    }

    if (listi == null) {
      listi = EntityFinder.findAllByColumn((ResellerStaffGroup) ResellerStaffGroup.getStaticInstance(ResellerStaffGroup.class), ResellerStaffGroup.getNameColumnName(), reseller.getName());
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
    try {
      List allUsers = getUsers(reseller);
      PermissionGroup pGroup = getPermissionGroup(reseller);
      List permUsers = getUsersInPermissionGroup(reseller);

      if (permUsers != null) {
        allUsers.removeAll(permUsers);
      }

      return allUsers;
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
      return null;
    }
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
    Iterator childs = ResellerManager.getResellerChilds(reseller, Reseller.getColumnNameName());
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
    Iterator childs = ResellerManager.getResellerChilds(reseller, Reseller.getColumnNameName());
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
    for (int i = 0; i < groups.size(); i++) {
      group = (GenericGroup) groups.get(i);
      type = group.getGroupType();
      if (type != null && type.equals(ResellerStaffGroup.GROUP_TYPE_VALUE)) {
        isReseller = true;
        number= i;
        break;
      }
    }

    if (isReseller) {
      Reseller[] resellers = Reseller.getValidResellers();
      GenericGroup rGroup = (GenericGroup) groups.get(number);
      String name;
      for (int i = 0; i < resellers.length; i++) {
        if ((resellers[i].getName()+"_"+resellers[i].getID()).indexOf(rGroup.getName()) != -1) {
          return resellers[i];
        }
      }

    }

    return null;
  }

  public static User getMainUser(Reseller reseller) throws SQLException  {
    List users = UserGroupBusiness.getUsersContained(new GenericGroup(reseller.getGroupId()));
    if (users != null && users.size() > 0) {
      return (User) users.get(0);
    }else {
      return null;
    }
  }

}
