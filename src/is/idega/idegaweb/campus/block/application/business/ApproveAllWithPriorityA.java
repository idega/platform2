package is.idega.idegaweb.campus.block.application.business;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Idega hf
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */

import java.util.Collection;
import java.util.Vector;
import java.util.Iterator;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.block.application.data.*;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;

import is.idega.idegaweb.campus.block.application.data.*;
import is.idega.idegaweb.campus.block.mailinglist.business.*;

public class ApproveAllWithPriorityA {

  public ApproveAllWithPriorityA() {
  }

  public void approveAll(IWApplicationContext iwac) {
    StringBuffer query = new StringBuffer("select app_application_id ");
    query.append("from cam_application cam_app, app_application app ");
    query.append("where app.app_application_id = cam_app.app_application_id ");
    query.append("and app.status = 'S' ");
    query.append("and cam_app.priority_level = 'A' ");
    query.append("order by app.app_application_id");

    Connection Conn = null;
    Vector v = new Vector();

    try {
      Conn = com.idega.util.database.ConnectionBroker.getConnection();
      Statement stmt = Conn.createStatement();
      ResultSet RS  = stmt.executeQuery(query.toString());

      while (RS.next()) {
        int applicant_id = RS.getInt("app_application_id");
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
    MailingListService MailingListBusiness = (MailingListService) IBOLookup.getServiceInstance(iwac,MailingListService.class);
    if (v != null) {
    Iterator it2 = v.iterator();
    while (it2.hasNext()) {
      int id = ((Integer)it2.next()).intValue();
      Application A = ((ApplicationHome)com.idega.data.IDOLookup.getHome(Application.class)).findByPrimaryKey(new Integer(id));
      A.setStatus("A");
      A.store();
      Applicant Appli = ((ApplicantHome)com.idega.data.IDOLookup.getHome(Applicant.class)).findByPrimaryKey(new Integer(A.getApplicantId()));

      MailingListBusiness.processMailEvent(new EntityHolder(Appli),LetterParser.APPROVAL);

      CampusApplicationHome CAHome = null;
      CampusApplication CA = null;

      CAHome = (CampusApplicationHome)com.idega.data.IDOLookup.getHomeLegacy(CampusApplication.class);
      java.util.Collection coll = CAHome.findAllByApplicationId(((Integer)A.getPrimaryKey()).intValue());
      if (coll != null) {
        java.util.Iterator it = coll.iterator();
        if (it.hasNext())
          CA = (CampusApplication)it.next();//CAHome.findByPrimaryKeyLegacy(((Integer)it.next()).intValue());
      }

      if (CA != null) {
        Collection L = ((AppliedHome)IDOLookup.getHome(Applied.class)).findByApplicationID( ((Integer)CA.getPrimaryKey()));
        java.util.Iterator it = L.iterator();
        if (it != null) {
          while (it.hasNext()) {
            Applied applied = (Applied)it.next();

            WaitingList wl = ((WaitingListHome)com.idega.data.IDOLookup.getHome(WaitingList.class)).create();
            wl.setApartmentTypeId(applied.getApartmentTypeId());
            wl.setComplexId(applied.getComplexId().intValue());
//            wl.setType(new String("A"));
            wl.setTypeApplication();
            wl.setApplicantId(((Integer)Appli.getPrimaryKey()).intValue());
            wl.setOrder(0);
            wl.setChoiceNumber(applied.getOrder());
            wl.store();
            wl.setOrder(((Integer)wl.getPrimaryKey()).intValue());
            String level = CA.getPriorityLevel();
            if (level.equals("A"))
              wl.setPriorityLevelA();
            else if (level.equals("B"))
              wl.setPriorityLevelB();
            else if (level.equals("C"))
              wl.setPriorityLevelC();
            else if (level.equals("D"))
              wl.setPriorityLevelD();
            wl.store();
          }
        }
      }
    }}}
    catch(Exception e) {
      e.printStackTrace();
    }
  }
}