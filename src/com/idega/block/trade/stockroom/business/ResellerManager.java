package com.idega.block.trade.stockroom.business;

import com.idega.block.trade.stockroom.data.*;
import java.sql.SQLException;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.user.data.*;
import com.idega.util.idegaTimestamp;
import com.idega.core.accesscontrol.business.*;
import com.idega.core.data.*;
import com.idega.util.CypherText;
import com.idega.data.SimpleQuerier;

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

  public static Reseller updateReseller(int resellerId, String name, String description, int[] addressIds, int[] phoneIds, int[] emailIds) throws Exception {
      return createReseller(resellerId, name, null,null, description, addressIds, phoneIds, emailIds);
  }

  public static Reseller createReseller(String name, String userName, String password, String description, int[] addressIds, int[] phoneIds, int[] emailIds) throws Exception {
    return createReseller(-1, name, userName, password, description, addressIds, phoneIds, emailIds);
  }

  private static Reseller createReseller(int resellerId, String name, String userName, String password, String description, int[] addressIds, int[] phoneIds, int[] emailIds) throws Exception {
    boolean isUpdate = false;
    if (resellerId != -1) isUpdate = true;


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
          sql.append(com.idega.data.EntityControl.getManyToManyRelationShipTableName(Reseller.class, Supplier.class)+" rs");
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

  public static Reseller[] getResellers(int supplierId) {
    return getResellers(supplierId,"");
  }

  public static Reseller[] getResellers(int supplierId, String orderBy) {
    Reseller[] resellers = {};
    try {
        Reseller reseller = (Reseller) Reseller.getStaticInstance(Reseller.class);
        Supplier supplier = (Supplier) Supplier.getStaticInstance(Supplier.class);

        StringBuffer sql = new StringBuffer();
          sql.append("Select r.* from "+Reseller.getResellerTableName()+" r, "+Supplier.getSupplierTableName()+" s, ");
          sql.append(com.idega.data.EntityControl.getManyToManyRelationShipTableName(Reseller.class, Supplier.class)+" rs");
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

        resellers = (Reseller[]) (Reseller.getStaticInstance(Reseller.class)).findAll(sql.toString());

    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }

    return resellers;
  }


}
