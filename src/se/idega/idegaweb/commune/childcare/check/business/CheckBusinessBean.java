package se.idega.idegaweb.commune.childcare.check.business;

import se.idega.idegaweb.commune.childcare.check.data.*;

import com.idega.business.IBOServiceBean;

import java.util.*;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Anders Lindman
 * @version 1.0
 */

public class CheckBusinessBean extends IBOServiceBean implements CheckBusiness{

  public CheckBusinessBean() {
  }

  private CheckHome getCheckHome() throws java.rmi.RemoteException{
    return (CheckHome) com.idega.data.IDOLookup.getHome(Check.class);
  }

  public Check getCheck(int checkId) throws Exception {
    return getCheckHome().findByPrimaryKey(new Integer(checkId));
  }

  public Collection findChecks() throws Exception {
    return getCheckHome().findChecks();
  }

  public void createCheck(
      int childCareType,
      int workSituation1,
      int workSituation2,
      String motherTongueMotherChild,
      String motherTongueFatherChild,
      String motherTongueParents,
      int childId,
      int method,
      int amount,
      int checkFee
      )throws Exception{
    CheckHome home = (CheckHome)com.idega.data.IDOLookup.getHome(Check.class);
    Check check = home.create();
    check.setChildCareType(childCareType);
    check.setWorkSituation1(workSituation1);
    check.setWorkSituation2(workSituation2);
    check.setMotherTongueMotherChild(motherTongueMotherChild);
    check.setMotherTongueFatherChild(motherTongueFatherChild);
    check.setMotherTongueParents(motherTongueParents);
    check.setChildId(childId);
    check.setMethod(method);
    check.setAmount(amount);
    check.setCheckFee(checkFee);
//    check.setStatus();
    check.store();
  }
}
