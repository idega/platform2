/*
 * $Id: ApplicantHome.java,v 1.3 2003/04/03 07:05:44 laddi Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.application.business;

import java.util.List;

import com.idega.block.application.data.Applicant;
import com.idega.data.EntityFinder;
import com.idega.data.IDOFinderException;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ApplicantHome {
  private static ApplicantHome _instance = null;

  /**
   *
   */
  private ApplicantHome() {
  }

  /**
   *
   */
  public static ApplicantHome getInstance() {
    if (_instance == null)
      _instance = new ApplicantHome();

    return(_instance);
  }

  /**
   *
   */
  public Applicant getNewApplicant() {
//    ApplicantBean applicant = new ApplicantBean();
//    return(applicant);
    return(null);
  }

  /**
   *
   */
  public boolean update(Applicant applicant) {
/*    ApplicantBean bean = (ApplicantBean)applicant;
    try {
      bean.update();
    }
    catch(SQLException e) {
      return(false);
    }*/

    return(true);
  }

  /**
   *
   */
  public boolean insert(Applicant applicant) {
/*    ApplicantBean bean = (ApplicantBean)applicant;
    try {
      bean.insert();
    }
    catch(SQLException e) {
      return(false);
    }*/

    return(true);
  }

  /**
   *
   */
  public boolean delete(Applicant applicant) {
/*    ApplicantBean bean = (ApplicantBean)applicant;
    try {
      bean.delete();
    }
    catch(SQLException e) {
      return(false);
    }*/

    return(true);
  }

  /**
   *
   */
  public List listOfNewApplicants() {
    return(listOfApplicants(null,com.idega.block.application.data.ApplicationBMPBean.STATUS_SUBMITTED));
  }

  /**
   *
   */
  public List listOfNewApplicantsOrdered(String order) {
    return(listOfApplicants(null,com.idega.block.application.data.ApplicationBMPBean.STATUS_SUBMITTED));
  }

  /**
   *
   */
  public List listOfApplicantsWithStatus(String status) {
    return(listOfApplicants(null,status));
  }

  /**
   *
   */
  public List listOfApplicants(String order,String status) {
    try {
      StringBuffer sql = new StringBuffer("select distinct a.* from ");
/*      sql.append(ApplicantBean.getEntityTableName());
      sql.append("a, ");
      sql.append(com.idega.block.application.data.ApplicationBMPBean.getEntityTableName());
      sql.append("b where ");
      sql.append("a.");
//      sql.append(ApplicantBean.getIDColumnName());
      sql.append(" = ");
      sql.append("b.");
      sql.append(com.idega.block.application.data.ApplicationBMPBean.getApplicantIdColumnName());
      sql.append(" and ");
      if (status != null) {
        sql.append("b.");
        sql.append(com.idega.block.application.data.ApplicationBMPBean.getStatusColumnName());
        sql.append(" = '");
        sql.append(status);
        sql.append("' ");
      }
      if (order != null && order.length() > 0) {
        sql.append(" order by ");
        sql.append( order);
      }*/

//      List L = EntityFinder.getInstance().findAll(ApplicantBean.class,sql.toString());
      List L = EntityFinder.getInstance().findAll(Applicant.class,sql.toString());
      return(L);
    }
    catch(IDOFinderException e) {
      e.printStackTrace();
      return(null);
    }
  }
}
