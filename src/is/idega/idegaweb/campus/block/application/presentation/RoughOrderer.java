package is.idega.idegaweb.campus.block.application.presentation;





import com.idega.block.building.business.ApartmentTypeComplexHelper;

import com.idega.block.application.data.Applicant;

import com.idega.block.application.data.Application;

import com.idega.block.application.data.ApplicationSubject;

import com.idega.block.building.business.BuildingFinder;

import is.idega.idegaweb.campus.block.application.data.Applied;

import is.idega.idegaweb.campus.block.application.data.WaitingList;

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

    query.append("select app_applicant_id, ordered ");

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

    Vector v2 = new Vector();



    try {

      Conn = com.idega.util.database.ConnectionBroker.getConnection();

      Statement stmt = Conn.createStatement();

      ResultSet RS  = stmt.executeQuery(query2.toString());



      while (RS.next()) {

        int applicant_id = RS.getInt("app_applicant_id");

        int ordered = RS.getInt("ordered");

        v.addElement(new Integer(applicant_id));

        v2.addElement(new Integer(ordered));

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

        Integer choice = (Integer)v2.elementAt(i);

        WaitingList wl = ((is.idega.idegaweb.campus.block.application.data.WaitingListHome)com.idega.data.IDOLookup.getHomeLegacy(WaitingList.class)).createLegacy();

        wl.setApartmentTypeId(aprt_type);

        wl.setComplexId(complex);

        wl.setType(new String("A"));

        wl.setApplicantId(applicant_id);

        wl.setOrder(i+1);

        wl.setChoiceNumber(choice);

        wl.insert();

      }

    }

    catch(SQLException e) {

      e.printStackTrace();

    }

  }

}

