/*
<<<<<<< CampusApplicationFinder.java
 * $Id: CampusApplicationFinder.java,v 1.18 2004/06/05 07:43:04 aron Exp $
=======
 * $Id: CampusApplicationFinder.java,v 1.18 2004/06/05 07:43:04 aron Exp $
>>>>>>> 1.17
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.application.business;

import is.idega.idegaweb.campus.block.application.data.Applied;
import is.idega.idegaweb.campus.block.application.data.AppliedHome;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import is.idega.idegaweb.campus.block.application.data.CampusApplicationHome;
import is.idega.idegaweb.campus.block.application.data.CurrentResidency;
import is.idega.idegaweb.campus.block.application.data.CurrentResidencyHome;
import is.idega.idegaweb.campus.block.application.data.SpouseOccupation;
import is.idega.idegaweb.campus.block.application.data.SpouseOccupationHome;
import is.idega.idegaweb.campus.block.application.data.WaitingList;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.ejb.CreateException;

import com.idega.block.application.business.ApplicationFinder;
import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.ApplicantHome;
import com.idega.block.application.data.Application;
import com.idega.block.application.data.ApplicationHome;
import com.idega.data.EntityFinder;
import com.idega.data.IDOEntityDefinition;
import com.idega.data.IDOLookup;
import com.idega.data.SimpleQuerier;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */

/** @deprecated use service beans instead*/
public abstract class CampusApplicationFinder {
	public static Collection listOfApplicationInSubject(int subjectId) {
		try {
			ApplicationHome sHome = (ApplicationHome) IDOLookup.getHome(Application.class);
			return sHome.findBySubject(new Integer(subjectId));
		}
		catch (Exception e) {
			return null;
		}
	}

	public static Collection listOfSpouseOccupations() {
		try {
			SpouseOccupationHome sHome = (SpouseOccupationHome) IDOLookup.getHome(SpouseOccupation.class);
			return sHome.findAll();
			//return EntityFinder.findAll(((is.idega.idegaweb.campus.block.application.data.SpouseOccupationHome) com.idega.data.IDOLookup.getHomeLegacy(SpouseOccupation.class)).createLegacy());
		}
		catch (Exception e) {
			return null;
		}
	}
	public static Collection listOfResidences() {
		try {
			CurrentResidencyHome rHome = (CurrentResidencyHome) IDOLookup.getHome(CurrentResidency.class);
			return rHome.findAll();
		}
		catch (Exception e) {
			return null;
		}
	}

	public static Collection listOfNewApplied() {
		try {
			AppliedHome aHome = (AppliedHome) IDOLookup.getHome(Applied.class);
			return aHome.findAll();
		}
		catch (Exception e) {
			return null;
		}
	}

	public static Collection listOfNewCampusApplication() {
		try {
			CampusApplicationHome aHome = (CampusApplicationHome) IDOLookup.getHome(CampusApplication.class);
			return aHome.findAll();
		}
		catch (Exception e) {
			return null;
		}
	}

	public static Collection listOfAppliedInApplication(int id) {
		try {
			AppliedHome aHome = (AppliedHome) IDOLookup.getHome(Applied.class);
			return aHome.findByApplicationID(new Integer(id));
		}
		catch (Exception e) {
			return null;
		}
	}

	public static Collection listOfApplicationHoldersInSubject(int id, String status) {
		Collection A = listOfNewApplied();
		Collection B = listOfNewCampusApplication();
		Collection C = ApplicationFinder.listOfApplicationInSubject(id, status);
		Collection D = ApplicationFinder.listOfNewApplicants();

		return listOfCampusApplicationHolders(A, B, C, D);
	}

	public static Collection listOfCampusApplicationHolders(Collection lApplied, Collection lCamApp, Collection lApp, Collection lApplicant) {
		Collection A = lApplied;
		Collection B = lCamApp;
		Collection C = lApp;
		Collection D = lApplicant;

		Vector V = null;
		try {
			java.util.Iterator it = null;
			if (B != null && C != null && D != null) {
				it = D.iterator();
				Hashtable HD = new Hashtable(D.size());
				while (it.hasNext()) {
					Applicant applicant = (Applicant) it.next();
					HD.put(new Integer(applicant.getPrimaryKey().toString()), applicant);
				}

				it = C.iterator();
				Hashtable HC = new Hashtable(C.size());
				while (it.hasNext()) {
					Application application = (Application) it.next();
					HC.put(new Integer(application.getPrimaryKey().toString()), application);
				}

				it = B.iterator();
				Hashtable HB = new Hashtable(B.size());
				while (it.hasNext()) {
					CampusApplication campusapplication = (CampusApplication) it.next();
					HB.put(new Integer(campusapplication.getPrimaryKey().toString()), campusapplication);
				}

				if (A != null) {
					it = A.iterator();
					Application application;
					Applicant applicant;
					CampusApplication campusApplication;
					Applied applied;
					CampusApplicationHolder AH;
					V = new Vector();
					Vector vApplied = null;
					int appliedAppId = -1;

					while (it.hasNext()) {
						applied = (Applied) it.next();

						if (appliedAppId == applied.getApplicationId().intValue()) {
							if (vApplied != null)
								vApplied.add(applied);
						}
						else {
							vApplied = new Vector();
							vApplied.add(applied);
							if (HB.containsKey(applied.getApplicationId())) {
								campusApplication = (CampusApplication) HB.get((applied.getApplicationId()));
								if (HC.containsKey(campusApplication.getAppApplicationId())) {
									application = (Application) HC.get((campusApplication.getAppApplicationId()));
									if (HD.containsKey(new Integer(application.getApplicantId()))) {
										applicant = (Applicant) HD.get(new Integer(application.getApplicantId()));
										AH = new CampusApplicationHolder(application, applicant, campusApplication, vApplied);
										V.add(AH);
									}
								}
							}
						}
						appliedAppId = (applied.getApplicationId()).intValue();
					}
				}
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return V;
	}

	public static CampusApplicationHolder getApplicantInfo(int iApplicantId) {
		CampusApplicationHolder CAH = null;
		Applicant eApplicant = null;

		try {
			eApplicant = ((ApplicantHome) IDOLookup.getHome(Applicant.class)).findByPrimaryKey(new Integer(iApplicantId));
			CAH = getApplicantInfo(eApplicant);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return CAH;
	}

	public static CampusApplicationHolder getCampusApplicationInfo(int campusApplicationId) {
		CampusApplicationHolder cah = null;
		CampusApplication ca = null;

		try {
			ca = ((CampusApplicationHome) IDOLookup.getHome(CampusApplication.class)).findByPrimaryKey(new Integer(campusApplicationId));
			cah = getCampusApplicationInfo(ca);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return (cah);
	}

	public static CampusApplicationHolder getCampusApplicationInfo(CampusApplication ca) {
		CampusApplicationHolder cah = null;
		Collection resultSet = null;
		if (ca != null) {
			try {
				AppliedHome aHome = (AppliedHome) IDOLookup.getHome(Applied.class);
				resultSet = aHome.findByApplicationID(new Integer(ca.getPrimaryKey().toString()));
				Vector v = null;
				if (resultSet != null) {
					v = new Vector(resultSet.size());
					for (Iterator iter = resultSet.iterator(); iter.hasNext();) {
						Applied applied = (Applied) iter.next();
						v.add(applied);
					}
				}

				Application app = ((ApplicationHome) IDOLookup.getHome(Application.class)).findByPrimaryKey(ca.getAppApplicationId());
				Applicant applicant = ((ApplicantHome) IDOLookup.getHome(Applicant.class)).findByPrimaryKey(new Integer(app.getApplicantId()));

				cah = new CampusApplicationHolder(app, applicant, ca, v);
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return (cah);
	}

	public static CampusApplicationHolder getApplicantInfo(Applicant eApplicant) {
		CampusApplicationHolder cah = null;
		if (eApplicant != null) {
			try {
				ApplicationHome aHome = (ApplicationHome) IDOLookup.getHome(Application.class);
				Collection l = aHome.findByApplicantID(new Integer(eApplicant.getPrimaryKey().toString()));
				if (l != null && !l.isEmpty()) {
					Application eApplication = (Application) l.iterator().next();
					CampusApplicationHome cHome = (CampusApplicationHome) IDOLookup.getHome(CampusApplication.class);
					l = cHome.findAllByApplicationId(new Integer(eApplication.getPrimaryKey().toString()).intValue());
					if (l != null && !l.isEmpty()) {
						CampusApplication eCampusApplication = (CampusApplication) l.iterator().next();
						AppliedHome lHome = (AppliedHome) IDOLookup.getHome(Applied.class);
						l = lHome.findByApplicationID((Integer)eCampusApplication.getPrimaryKey());
						Vector v = null;
						if (l != null) {
							v = new Vector(l.size());
							for (Iterator iter = l.iterator(); iter.hasNext();) {
								Applied a = (Applied) iter.next();
								v.add(a);
							}
						}

						cah = new CampusApplicationHolder(eApplication, eApplicant, eCampusApplication, v);
					}
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return (cah);
	}

	public static java.util.Collection listOfWaitinglist(int aprtTypeId, int cmplxId) {
		try {
			is.idega.idegaweb.campus.block.application.data.WaitingListHome WLHome = (is.idega.idegaweb.campus.block.application.data.WaitingListHome) com.idega.data.IDOLookup.getHomeLegacy(WaitingList.class);
			return WLHome.findByApartmentTypeAndComplex(aprtTypeId, cmplxId);
		}
		catch (Exception e) {
			return (null);
		}
	}

	public static java.util.Collection listOfWaitinglistForTypeApplication(int aprtTypeId, int cmplxId) {
		try {
			is.idega.idegaweb.campus.block.application.data.WaitingListHome WLHome = (is.idega.idegaweb.campus.block.application.data.WaitingListHome) com.idega.data.IDOLookup.getHomeLegacy(WaitingList.class);
			return WLHome.findByApartmentTypeAndComplexForApplicationType(aprtTypeId, cmplxId);
		}
		catch (Exception e) {
			return (null);
		}
	}

	public static java.util.Collection listOfWaitinglistForTypeTransfer(int aprtTypeId, int cmplxId) {
		try {
			is.idega.idegaweb.campus.block.application.data.WaitingListHome WLHome = (is.idega.idegaweb.campus.block.application.data.WaitingListHome) com.idega.data.IDOLookup.getHomeLegacy(WaitingList.class);
			return WLHome.findByApartmentTypeAndComplexForTransferType(aprtTypeId, cmplxId);
		}
		catch (Exception e) {
			return (null);
		}
	}

	public static Collection listOfEntityInWaitingList(Class entity, int aprtTypeId, int cmplxId) {
		Collection L = null;
		try {
		IDOEntityDefinition def = IDOLookup.getEntityDefinitionForClass(entity);
		
		boolean bapplied = false;
		if (def.getInterfaceClass().isAssignableFrom(Applied.class))
			bapplied = true;
		/** @todo  laga */
		StringBuffer sql = getApplicationEntityQuery(def.getUniqueEntityName(), aprtTypeId, cmplxId, bapplied);
		//System.err.println(sql.toString());
		
			L = EntityFinder.getInstance().findAll(def.getInterfaceClass(), sql.toString());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return L;
	}

	private static StringBuffer getApplicationEntityQuery(
		String entityName,
		int aprtTypeId,
		int cmplxId,
		boolean bapplied) {
		boolean btype = true, bcmplx = true;
		
		if (aprtTypeId <= 0)
			btype = false;
		if (cmplxId <= 0)
			bcmplx = false;
		StringBuffer sql = new StringBuffer("select ");
		//if(!(btype && bcmplx))
		sql.append(" distinct ");
		sql.append(entityName);
		sql.append(".* ");
		sql.append(" from cam_application ca,app_application an,app_applicant aa");
		sql.append(",cam_waiting_list wl ,cam_applied ad");
		sql.append(" where ca.app_application_id = an.app_application_id ");
		sql.append(" and an.app_applicant_id = aa.app_applicant_id ");
		sql.append(" and aa.app_applicant_id = wl.app_applicant_id ");
		if (bapplied) {
			sql.append(" and ad.bu_aprt_type_id =  wl.bu_apartment_type_id ");
			sql.append(" and ad.bu_complex_id = wl.bu_complex_id ");
		}
		if (btype) {
			sql.append(" and wl.bu_apartment_type_id =  ");
			sql.append(aprtTypeId);
		}
		if (bcmplx) {
			sql.append(" and wl.bu_complex_id =  ");
			sql.append(cmplxId);
		}
		return sql;
	}

	public static Collection listOfCampusApplicationHoldersInWaitinglist(int aprtTypeId, int cmplxId) throws RemoteException,CreateException {
		Collection lApplication = listOfEntityInWaitingList(Application.class, aprtTypeId, cmplxId);
		Collection lApplicant = listOfEntityInWaitingList(Applicant.class,aprtTypeId,cmplxId);
		Collection lCampusApplication = listOfEntityInWaitingList(CampusApplication.class, aprtTypeId, cmplxId);
		Collection lApplied = listOfEntityInWaitingList(Applied.class, aprtTypeId, cmplxId);

		return listOfCampusApplicationHolders(lApplied, lCampusApplication, lApplication, lApplicant);
	}

	public static int countAppliedInTypeAndComplex(int typeId, int cmplxId, int order) {
		StringBuffer sql = new StringBuffer("select count(*) ");
		sql.append(" from cam_applied app ");
		sql.append(" where app.bu_aprt_type_id = ");
		sql.append(typeId);
		sql.append(" and app.bu_complex_id =");
		sql.append(cmplxId);
		if (order > 0) {
			sql.append(" and app.ordered = ");
			sql.append(order);
		}
		int count = 0;
		try {
			count = SimpleQuerier.executeIntQuery(sql.toString());
			//count = ((is.idega.idegaweb.campus.block.application.data.CampusApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(CampusApplication.class)).createLegacy().getNumberOfRecords(sql.toString());
		}
		catch (Exception ex) {
		}

		if (count < 0)
			count = 0;
		return count;
	}

	public static int countWaitingInTypeAndComplex(int typeId, int cmplxId, int order) {
		StringBuffer sql = new StringBuffer("select count(*  ) ");
		sql.append(" from cam_waiting_list wl ");
		sql.append(" where wl.bu_apartment_type_id = ");
		sql.append(typeId);
		sql.append(" and wl.bu_complex_id = ");
		sql.append(cmplxId);
		//System.err.println(sql.toString());
		int count = 0;
		try {
			count = SimpleQuerier.executeIntQuery(sql.toString());
			//count = ((is.idega.idegaweb.campus.block.application.data.AppliedHome) com.idega.data.IDOLookup.getHomeLegacy(Applied.class)).createLegacy().getNumberOfRecords(sql.toString());
		}
		catch (Exception ex) {
		}
		if (count < 0)
			count = 0;
		return count;
	}
	public static int countWaitingWithTypeAndComplex(int typeId, int cmplxId, int order) {
		StringBuffer sql = new StringBuffer("select count(distinct cam_waiting_list_id) ");
		sql.append(" from cam_waiting_list wl ,cam_applied ad ");
		sql.append(" where wl.bu_apartment_type_id = ad.bu_aprt_type_id ");
		sql.append(" and wl.bu_complex_id = ad.bu_complex_id ");
		sql.append(" and wl.bu_apartment_type_id =  ");
		sql.append(typeId);
		sql.append(" and wl.bu_complex_id =  ");
		sql.append(cmplxId);
		if (order > 0) {
			sql.append(" and ad.ordered = ");
			sql.append(order);
		}
		int count = 0;
		//System.err.println(sql.toString());
		//System.err.println();
		try {
			count = SimpleQuerier.executeIntQuery(sql.toString());
			//count = ((is.idega.idegaweb.campus.block.application.data.AppliedHome) com.idega.data.IDOLookup.getHomeLegacy(Applied.class)).createLegacy().getNumberOfRecords(sql.toString());
		}
		catch (Exception ex) {
		}
		if (count < 0)
			count = 0;
		return count;
	}

	

	
	

	/**
	 *
	 */
	private static void updateWatingListToRightOrder(Collection li) {
		Iterator it = li.iterator();
		while (it.hasNext()) {
			WaitingList wl = (WaitingList) it.next();
			if ((wl.getApartmentTypeId() != null) && (wl.getComplexId() != null)) {
				StringBuffer sql = new StringBuffer("select count(*) from ");
				sql.append(is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.getEntityTableName());
				sql.append(" where ((");
				sql.append(is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.getOrderColumnName());
				sql.append(" <= ");
				sql.append(wl.getOrder().toString());
				sql.append(" and ");
				sql.append(is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.getPriorityColumnName());
				sql.append(" = '");
				sql.append(wl.getPriorityLevel());
				sql.append("') or (");
				sql.append(is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.getPriorityColumnName());
				sql.append(" < '");
				sql.append(wl.getPriorityLevel());
				sql.append("')) and ");
				sql.append(is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.getApartmentTypeIdColumnName());
				sql.append(" = ");
				sql.append(wl.getApartmentTypeId().toString());
				sql.append(" and ");
				sql.append(is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.getComplexIdColumnName());
				sql.append(" = ");
				sql.append(wl.getComplexId().toString());

				int count = 0;

				try {
					count = SimpleQuerier.executeIntQuery(sql.toString());
					//count = ((is.idega.idegaweb.campus.block.application.data.WaitingListHome) com.idega.data.IDOLookup.getHomeLegacy(WaitingList.class)).createLegacy().getNumberOfRecords(sql.toString());
				}
				catch (Exception ex) {
					return;
				}

				if (count < 0)
					count = 1;

				wl.setOrder(count);
			}
		}
	}

	

	public static Map getEmailAndApplicationIdForApprovedApplications() {
		Hashtable table = new Hashtable();
		StringBuffer sql = new StringBuffer("select app.app_application_id, email from cam_application cam, app_application app where cam.app_application_id = app.app_application_id and app.status = 'A'");
    Connection Conn = null;

    try {
      Conn = com.idega.util.database.ConnectionBroker.getConnection();
      Statement stmt = Conn.createStatement();
      ResultSet RS  = stmt.executeQuery(sql.toString());

      while (RS.next()) {
        int id = RS.getInt("app_application_id");
        String email = RS.getString("email");
        if (email != null && !email.equals(""))
	        table.put(new Integer(id),email);
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
				
		return table;
	}

//	public static int findNextTransferInWaitingList(int typeId, int cmplxId, int from) {
//		StringBuffer sql = new StringBuffer("select max(app.ordered) ");
//		sql.append(" from cam_applied app ");
//		sql.append(" where app.bu_aprt_type_id = ");
//		sql.append(typeId);
//		sql.append(" and app.bu_complex_id =");
//		sql.append(cmplxId);
//		sql.append(" and app.list_type = 'T'");
//
//		int maxId = 0;
//		try {
//			maxId = ((is.idega.idegaweb.campus.block.application.data.CampusApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(CampusApplication.class)).createLegacy().getNumberOfRecords(sql.toString());
//		}
//		catch (SQLException ex) {
//			ex.printStackTrace();
//		}
//
//		if (maxId < 0)
//			maxId = 0;
//		return maxId;
//	}

	protected static java.util.Collection nextForTransfer(int aprtTypeId, int cmplxId, int orderedFrom) {
		try {
			is.idega.idegaweb.campus.block.application.data.WaitingListHome WLHome = (is.idega.idegaweb.campus.block.application.data.WaitingListHome) com.idega.data.IDOLookup.getHomeLegacy(WaitingList.class);
			return WLHome.findNextForTransferByApartmentTypeAndComplex(aprtTypeId, cmplxId, orderedFrom);
		}
		catch (Exception e) {
			return (null);
		}
	}
	
}