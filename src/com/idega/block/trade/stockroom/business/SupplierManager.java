package com.idega.block.trade.stockroom.business;

import com.idega.block.trade.stockroom.data.*;

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


  public int createSupplier(String name, String description, int group_id)throws Exception{
    throw new java.lang.UnsupportedOperationException("Method createSupplier(String name, String description, int group_id) not yet implemented.");
  }

  public void deleteSupplier(int id)throws Exception{
    throw new Exception("not implimented");
  }


  public void createSupplier(String name, String description, int[] addressIds, int[] phoneIds, int[] emailIds) throws Exception {

    boolean isUpdate = false;
    SupplierStaffGroup sGroup = new SupplierStaffGroup();
    Supplier supp = null;

    sGroup.setName(name);
    sGroup.insert();

    supp.setName(name);
    supp.setDescription(description);
    supp.setGroupId(sGroup.getID());

    supp.insert();

/*
    UserBusiness uBus = new UserBusiness();
    User user = uBus.insertUser(name,"","- admin",name+" - admin","Supplier administrator",null,idegaTimestamp.RightNow());

    user.addTo(eGroup);

    int[] userIDs = {user.getID()};

    AccessControl ac = new AccessControl();
    int permissionGroupID = ac.createPermissionGroup(name+" - admins", "Supplier administator group", "", userIDs ,null);

    //(new PermissionGroup(permissionGroupID)).addTo(eGroup);

    Address addr = new Address();
      addr.setStreetName(address);
    addr.insert();
    supp.addTo(addr);

    PhoneType[] phoneType = (PhoneType[]) (new PhoneType()).findAll();
    int homeID = 0;
    int faxID = 0;

    for (int i = 0; i < phoneType.length; i++) {
        if (phoneType[i].getUniqueName().equals(PhoneType.UNIQUE_NAME_HOME_PHONE)) {
          homeID = phoneType[i].getID();
        }else if (phoneType[i].getUniqueName().equals(PhoneType.UNIQUE_NAME_FAX_NUMBER)) {
          faxID = phoneType[i].getID();
        }
    }

    if (homeID != 0) {
        Phone ph1 = new Phone();
          ph1.setPhoneTypeId(homeID);
          ph1.setNumber(phone);
        ph1.insert();
        supp.addTo(ph1);
    }

    if (faxID != 0) {
        Phone ph2 = new Phone();
          ph2.setPhoneTypeId(faxID);
          ph2.setNumber(fax);
        ph2.insert();
        supp.addTo(ph2);
    }

*/


  }







} // Class SupplierManager