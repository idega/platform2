package com.idega.block.trade.stockroom.business;

import com.idega.block.trade.stockroom.data.*;
import com.idega.core.accesscontrol.data.PermissionGroup;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.user.data.User;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.data.*;
import com.idega.util.idegaTimestamp;
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

  public SupplierManager(){
  }


  public void deleteSupplier(int id)throws Exception{
    throw new Exception("not implimented");
  }


  public static Supplier createSupplier(String name, String userName, String password, String description, int[] addressIds, int[] phoneIds, int[] emailIds) throws Exception {

    boolean isUpdate = false;
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
    User user = uBus.insertUser(name,"","- admin",name+" - admin","Supplier administrator",null,idegaTimestamp.RightNow());
    LoginDBHandler.createLogin(user.getID(), userName, password);

    sGroup.addTo(user);

    int[] userIDs = {user.getID()};

    AccessControl ac = new AccessControl();
    int permissionGroupID = ac.createPermissionGroup(name+" - admins", "Supplier administator group", "", userIDs ,null);

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

    return supp;

  }

  public static void invalidateSupplier(Supplier supplier) throws SQLException {
    supplier.setIsValid(false);
    supplier.update();
  }

  public static void validateSupplier(Supplier supplier) throws SQLException {
    supplier.setIsValid(true);
    supplier.update();
  }

  public void updateSupplier(Supplier supplier, String name, String description, int[] addressIds, int[] phoneIds, int[] emailIds) throws Exception {
    supplier.setName(name);
    supplier.setDescription(description);
    supplier.update();
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




} // Class SupplierManager