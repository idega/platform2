/*
 * $Id: RoughOrderer.java,v 1.1 2001/07/13 09:33:29 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.allocation;

import com.idega.block.building.business.ApartmentTypeComplexHelper;
import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.Application;
import com.idega.block.application.data.ApplicationSubject;
import com.idega.block.building.business.BuildingFinder;
import is.idegaweb.campus.entity.Applied;
import is.idegaweb.campus.entity.WaitingList;
import java.util.Vector;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class RoughOrderer {
  private StringBuffer query = null;
  private StringBuffer delete = null;

  public RoughOrderer() {
    query = new StringBuffer();
    query.append("select app_applicant_id ");
    query.append("from cam_applied a, cam_application cam_app, app_application app, app_subject s, app_applicant aa ");
    query.append("where s.app_subject_id = app.app_subject_id ");
    query.append("and app.app_applicant_id = aa.app_applicant_id ");
    query.append("and app.app_application_id = cam_app.app_application_id ");
    query.append("and cam_app.cam_application_id = a.cam_application_id ");
    query.append("and app.status = 'A' ");

    delete = new StringBuffer("delete from cam_waiting_list");
  }

  public void createWaitingList(int subject_id) {
    deleteAll(subject_id);
    Vector types = BuildingFinder.getAllApartmentTypesComplex();
    for (int i = 0; i < types.size(); i++) {
      ApartmentTypeComplexHelper helper = (ApartmentTypeComplexHelper)types.elementAt(i);
      createWaitingListOfEachType(subject_id,helper.getKeyOne(),helper.getKeyTwo());
    }
  }

  private void deleteAll(int subject_id) {
    Connection Conn = null;
    Vector v = new Vector();

    try {
      Conn = com.idega.util.database.ConnectionBroker.getConnection();
      Statement stmt = Conn.createStatement();
      stmt.executeUpdate(delete.toString());

      stmt.close();
    }
    catch(SQLException e) {
      System.err.println(e.toString());
    }
    finally {
      com.idega.util.database.ConnectionBroker.freeConnection(Conn);
    }
  }

  private void createWaitingListOfEachType(int subject, int aprt_type, int complex) {
    StringBuffer query2 = new StringBuffer(query.toString());
    query2.append("and bu_aprt_type_id = " + aprt_type + " and bu_complex_id = "
                  + complex + " and s.app_subject_id = " + subject
                  + " order by ordered");

    Connection Conn = null;
    Vector v = new Vector();

    try {
      Conn = com.idega.util.database.ConnectionBroker.getConnection();
      Statement stmt = Conn.createStatement();
      ResultSet RS  = stmt.executeQuery(query2.toString());

      while (RS.next()) {
        int applicant_id = RS.getInt("app_applicant_id");
        v.addElement(new Integer(applicant_id));
      }

      RS.close();

      stmt.close();
    }
    catch(SQLException e) {
      System.err.println(e.toString());
    }
    finally {
      com.idega.util.database.ConnectionBroker.freeConnection(Conn);
    }

    try {
      for (int i = 0; i < v.size(); i++) {
        Integer applicant_id = (Integer)v.elementAt(i);
        WaitingList wl = new WaitingList();
        wl.setApartmentTypeId(aprt_type);
        wl.setComplexId(complex);
        wl.setType(new String("A"));
        wl.setApplicantId(applicant_id);
        wl.setOrder(i+1);
        wl.insert();
      }
    }
    catch(SQLException e) {
      e.printStackTrace();
    }
  }
}