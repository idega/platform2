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


  public static Reseller createReseller(String name, String userName, String password, String description, int[] addressIds, int[] phoneIds, int[] emailIds) throws Exception {
    Reseller reseller = new Reseller();

    CypherText cyph = new CypherText();
    String key = cyph.getKey(10);
    String[] check = SimpleQuerier.executeStringQuery("SELECT "+reseller.getIDColumnName()+" from "+Reseller.getResellerTableName()+" where "+Reseller.getColumnNameReferenceNumber()+" = '"+key+"'");
    while (check.length > 0) {
      cyph.getKey(10);
      check = SimpleQuerier.executeStringQuery("SELECT "+reseller.getIDColumnName()+" from "+Reseller.getResellerTableName()+" where "+Reseller.getColumnNameReferenceNumber()+" = '"+key+"'");
    }

    boolean isUpdate = false;
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

    sGroup.addTo(user);

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




}