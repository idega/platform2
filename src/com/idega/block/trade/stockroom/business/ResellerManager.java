package com.idega.block.trade.stockroom.business;

import com.idega.block.trade.stockroom.data.*;
import java.sql.SQLException;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.user.data.*;
import com.idega.util.idegaTimestamp;
import com.idega.core.accesscontrol.business.*;
import com.idega.core.data.*;
import com.idega.data.*;
import com.idega.util.CypherText;
import com.idega.data.SimpleQuerier;
import java.util.*;

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

  public ResellerManager() {
  }

  public void deleteReseller(int id)throws Exception{
      Reseller res = new Reseller(id);
        res.delete();
  }
/*
  public static Reseller updateReseller(int resellerId, String name, String description, int[] addressIds, int[] phoneIds, int[] emailIds) throws Exception {
      return createReseller(resellerId, name, null,null, description, addressIds, phoneIds, emailIds);
  }

  public static Reseller createReseller(String name, String userName, String password, String description, int[] addressIds, int[] phoneIds, int[] emailIds) throws Exception {
    return createReseller(-1, name, userName, password, description, addressIds, phoneIds, emailIds);
  }
*/
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

      ResellerStaffGroup sGroup = new ResellerStaffGroup();

      sGroup.setName(name);
      sGroup.insert();

      reseller.setName(name);
      reseller.setDescription(description);
      reseller.setGroupId(sGroup.getID());
      reseller.setIsValid(true);
      reseller.setReferenceNumber(key);
      reseller.insert();

      if (parentReseller != null) {
        parentReseller.addChild(reseller);
      }

      UserBusiness uBus = new UserBusiness();
      User user = uBus.insertUser(name,"","- admin",name+" - admin","Reseller administrator",null,idegaTimestamp.RightNow(),null);
      LoginDBHandler.createLogin(user.getID(), userName, password);

      sGroup.addUser(user);

      int[] userIDs = {user.getID()};

      AccessControl ac = new AccessControl();
      int permissionGroupID = ac.createPermissionGroup(name+" - admins", "Reseller administator group", "", userIDs ,null);

      //sGroup.addTo(PermissionGroup.class, permissionGroupID);

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

      return reseller;
    }
  }

  public static void invalidateReseller(Reseller reseller) throws SQLException {
    reseller.setIsValid(false);
    reseller.update();
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

  public static Product[] getProductsForReseller(int resellerId) throws SQLException {
    Product[] products = {};

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
          sql.append(" ORDER BY p."+Product.getColumnNameProductName());

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

  public static Iterator getResellers(Reseller reseller) {
    return getResellers(reseller, "");
  }

  public static Iterator getResellers(Reseller reseller, String orderBy) {
    Iterator iter = reseller.getChildren(orderBy);
    if (iter == null) iter = com.idega.util.ListUtil.getEmptyList().iterator();
    return iter;
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
    Reseller parent = (Reseller) reseller.getParentEntity();
    if (parent == null) {
      System.err.println("parent == null : "+reseller.getID());
      return getProductsWithContracts(-1, reseller.getID(),-1, orderBy);
    }else {
      System.err.println("parent != null : "+reseller.getID());
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
          buffer.append(" ORDER BY p."+orderBy);
        }

      products = (Product[]) product.findAll(buffer.toString());
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
    return products;
  }

  public static boolean isActiveContract(int supplierId, int resellerId, int productId) {
    boolean returner = false;

    try {
      Product product = (Product) Product.getStaticInstance(Product.class);
      Contract contract = (Contract) Contract.getStaticInstance(Contract.class);

      StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT distinct(c.*) FROM  "+contract.getContractTableName() +" c, "+product.getEntityName()+" p");
        buffer.append(" WHERE ");
        buffer.append("c."+contract.getColumnNameResellerId()+" = "+resellerId);
        buffer.append(" AND ");
        buffer.append("c."+contract.getColumnNameServiceId()+" = p."+product.getIDColumnName());
        buffer.append(" AND ");
        buffer.append("p."+product.getColumnNameSupplierId()+" = "+supplierId);
        buffer.append(" AND ");
        buffer.append("p."+product.getIDColumnName()+" = "+productId);

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

}
