/*
 * $Id: CampusApplicationFinder.java,v 1.17 2004/05/24 14:21:42 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.application.business;

import is.idega.idegaweb.campus.block.allocation.business.ContractFinder;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.application.data.Applied;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import is.idega.idegaweb.campus.block.application.data.CurrentResidency;
import is.idega.idegaweb.campus.block.application.data.SpouseOccupation;
import is.idega.idegaweb.campus.block.application.data.WaitingList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.idega.block.application.business.ApplicationFinder;
import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.Application;
import com.idega.block.contract.data.ContractBMPBean;
import com.idega.data.EntityFinder;
import com.idega.data.IDOLegacyEntity;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public abstract class CampusApplicationFinder {
	public static List listOfApplicationInSubject(int subjectId) {
		try {
			return EntityFinder.findAll(((is.idega.idegaweb.campus.block.application.data.SpouseOccupationHome) com.idega.data.IDOLookup.getHomeLegacy(SpouseOccupation.class)).createLegacy());
		}
		catch (SQLException e) {
			return null;
		}
	}

	public static List listOfSpouseOccupations() {
		try {
			return EntityFinder.findAll(((is.idega.idegaweb.campus.block.application.data.SpouseOccupationHome) com.idega.data.IDOLookup.getHomeLegacy(SpouseOccupation.class)).createLegacy());
		}
		catch (SQLException e) {
			return null;
		}
	}
	public static List listOfResidences() {
		try {
			return EntityFinder.findAll(((is.idega.idegaweb.campus.block.application.data.CurrentResidencyHome) com.idega.data.IDOLookup.getHomeLegacy(CurrentResidency.class)).createLegacy());
		}
		catch (SQLException e) {
			return null;
		}
	}
	public static List listOfNewApplied() {
		try {
			return EntityFinder.findAll(((is.idega.idegaweb.campus.block.application.data.AppliedHome) com.idega.data.IDOLookup.getHomeLegacy(Applied.class)).createLegacy());
		}
		catch (SQLException e) {
			return null;
		}
	}

	public static List listOfNewCampusApplication() {
		try {
			return EntityFinder.findAll(((is.idega.idegaweb.campus.block.application.data.CampusApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(CampusApplication.class)).createLegacy());
		}
		catch (SQLException e) {
			return null;
		}
	}

	public static List listOfAppliedInApplication(int id) {
		try {
			Applied A = ((is.idega.idegaweb.campus.block.application.data.AppliedHome) com.idega.data.IDOLookup.getHomeLegacy(Applied.class)).createLegacy();
			return EntityFinder.findAllByColumn(A, A.getApplicationIdColumnName(), id);
		}
		catch (SQLException e) {
			return null;
		}
	}

	public static List listOfNewApplicationHolders() {
		List A = listOfNewApplied();
		List B = listOfNewCampusApplication();
		List C = ApplicationFinder.listOfNewApplications();
		List D = ApplicationFinder.listOfNewApplicants();

		return listOfCampusApplicationHolders(A, B, C, D);
	}

	public static List listOfApplicationHoldersInSubject(int id, String status) {
		List A = listOfNewApplied();
		List B = listOfNewCampusApplication();
		List C = ApplicationFinder.listOfApplicationInSubject(id, status);
		List D = ApplicationFinder.listOfNewApplicants();

		return listOfCampusApplicationHolders(A, B, C, D);
	}

	public static List listOfCampusApplicationHolders(List lApplied, List lCamApp, List lApp, List lApplicant) {
		List A = lApplied;
		List B = lCamApp;
		List C = lApp;
		List D = lApplicant;

		Vector V = null;
		try {
			java.util.Iterator it = null;
			if (B != null && C != null && D != null) {
				it = D.iterator();
				Hashtable HD = new Hashtable(D.size());
				while (it.hasNext()) {
					Applicant applicant = (Applicant) it.next();
					HD.put(new Integer(applicant.getID()), applicant);
				}

				it = C.iterator();
				Hashtable HC = new Hashtable(C.size());
				while (it.hasNext()) {
					Application application = (Application) it.next();
					HC.put(new Integer(application.getID()), application);
				}

				it = B.iterator();
				Hashtable HB = new Hashtable(B.size());
				while (it.hasNext()) {
					CampusApplication campusapplication = (CampusApplication) it.next();
					HB.put(new Integer(campusapplication.getID()), campusapplication);
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
			eApplicant = ((com.idega.block.application.data.ApplicantHome) com.idega.data.IDOLookup.getHomeLegacy(Applicant.class)).findByPrimaryKeyLegacy(iApplicantId);
			CAH = getApplicantInfo(eApplicant);
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}

		return CAH;
	}

	public static CampusApplicationHolder getCampusApplicationInfo(int campusApplicationId) {
		CampusApplicationHolder cah = null;
		CampusApplication ca = null;

		try {
			ca = ((is.idega.idegaweb.campus.block.application.data.CampusApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(CampusApplication.class)).findByPrimaryKeyLegacy(campusApplicationId);
			cah = getCampusApplicationInfo(ca);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		return (cah);
	}

	public static CampusApplicationHolder getCampusApplicationInfo(CampusApplication ca) {
		CampusApplicationHolder cah = null;
		List resultSet = null;
		if (ca != null) {
			try {
				Applied applied = ((is.idega.idegaweb.campus.block.application.data.AppliedHome) com.idega.data.IDOLookup.getHomeLegacy(Applied.class)).createLegacy();
				resultSet = EntityFinder.findAllByColumn(applied, applied.getApplicationIdColumnName(), ca.getID());
				Vector v = null;
				if (resultSet != null) {
					v = new Vector(resultSet.size());
					for (int i = 0; i < resultSet.size(); i++) {
						applied = (Applied) resultSet.get(i);
						v.add(applied);
					}
				}

				Application app = ((com.idega.block.application.data.ApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(Application.class)).findByPrimaryKeyLegacy(ca.getAppApplicationId().intValue());
				Applicant applicant = ((com.idega.block.application.data.ApplicantHome) com.idega.data.IDOLookup.getHomeLegacy(Applicant.class)).findByPrimaryKeyLegacy(app.getApplicantId());

				cah = new CampusApplicationHolder(app, applicant, ca, v);
			}
			catch (SQLException ex) {
				ex.printStackTrace();
			}
		}

		return (cah);
	}

	public static CampusApplicationHolder getApplicantInfo(Applicant eApplicant) {
		CampusApplicationHolder cah = null;
		if (eApplicant != null) {
			try {
				Application eApplication = ((com.idega.block.application.data.ApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(Application.class)).createLegacy();
				List l = EntityFinder.findAllByColumn(eApplication, com.idega.block.application.data.ApplicationBMPBean.getApplicantIdColumnName(), eApplicant.getID());
				if (l != null) {
					eApplication = (Application) l.get(0);
					CampusApplication eCampusApplication = ((is.idega.idegaweb.campus.block.application.data.CampusApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(CampusApplication.class)).createLegacy();
					l = EntityFinder.findAllByColumn(eCampusApplication, eCampusApplication.getApplicationIdColumnName(), eApplication.getID());
					if (l != null) {
						eCampusApplication = (CampusApplication) l.get(0);
						Applied eApplied = ((is.idega.idegaweb.campus.block.application.data.AppliedHome) com.idega.data.IDOLookup.getHomeLegacy(Applied.class)).createLegacy();
						l = EntityFinder.findAllByColumn(eApplied, eApplied.getApplicationIdColumnName(), eCampusApplication.getID());
						Vector v = null;
						if (l != null) {
							v = new Vector(l.size());
							for (int i = 0; i < l.size(); i++) {
								Applied a = (Applied) l.get(i);
								v.add(a);
							}
						}

						cah = new CampusApplicationHolder(eApplication, eApplicant, eCampusApplication, v);
					}
				}
			}
			catch (SQLException ex) {
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

	public static List listOfEntityInWaitingList(IDOLegacyEntity returntype, int aprtTypeId, int cmplxId) {
		List L = null;
		boolean bapplied = false;
		if (returntype instanceof Applied)
			bapplied = true;
		/** @todo  laga */
		boolean btype = true, bcmplx = true;

		if (aprtTypeId <= 0)
			btype = false;
		if (cmplxId <= 0)
			bcmplx = false;
		StringBuffer sql = new StringBuffer("select ");
		//if(!(btype && bcmplx))
		sql.append(" distinct ");
		sql.append(returntype.getEntityName());
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
		//System.err.println(sql.toString());
		try {
			L = EntityFinder.findAll(returntype, sql.toString());
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		return L;
	}

	public static List listOfCampusApplicationHoldersInWaitinglist(int aprtTypeId, int cmplxId) {
		Application A = ((com.idega.block.application.data.ApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(Application.class)).createLegacy();
		List lApplication = listOfEntityInWaitingList(A, aprtTypeId, cmplxId);
		List lApplicant = listOfEntityInWaitingList(((com.idega.block.application.data.ApplicantHome) com.idega.data.IDOLookup.getHomeLegacy(Applicant.class)).createLegacy(), aprtTypeId, cmplxId);
		List lCampusApplication = listOfEntityInWaitingList(((is.idega.idegaweb.campus.block.application.data.CampusApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(CampusApplication.class)).createLegacy(), aprtTypeId, cmplxId);
		List lApplied = listOfEntityInWaitingList(((is.idega.idegaweb.campus.block.application.data.AppliedHome) com.idega.data.IDOLookup.getHomeLegacy(Applied.class)).createLegacy(), aprtTypeId, cmplxId);

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
			count = ((is.idega.idegaweb.campus.block.application.data.CampusApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(CampusApplication.class)).createLegacy().getNumberOfRecords(sql.toString());
		}
		catch (SQLException ex) {
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
			count = ((is.idega.idegaweb.campus.block.application.data.AppliedHome) com.idega.data.IDOLookup.getHomeLegacy(Applied.class)).createLegacy().getNumberOfRecords(sql.toString());
		}
		catch (SQLException ex) {
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
			count = ((is.idega.idegaweb.campus.block.application.data.AppliedHome) com.idega.data.IDOLookup.getHomeLegacy(Applied.class)).createLegacy().getNumberOfRecords(sql.toString());
		}
		catch (SQLException ex) {
		}
		if (count < 0)
			count = 0;
		return count;
	}

	/**
	 *
	 */
	public static CampusApplicationHolder getApplicationInfo(int applicationId) {
		CampusApplicationHolder cah = null;
		Application a = null;

		try {
			a = ((com.idega.block.application.data.ApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(Application.class)).findByPrimaryKeyLegacy(applicationId);
			cah = getApplicationInfo(a);
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return (cah);
	}

	/**
	 *
	 */
	public static CampusApplicationHolder getApplicationInfo(Application a) {
		CampusApplicationHolder cah = null;
				CampusApplication ca = null;
				Collection resultSet = null;
				Vector applieds = null;
				Applicant applicant = null;
				Contract contract = null;
				Vector wl = null;
				if (a != null) {
					try {
						ca =((is.idega.idegaweb.campus.block.application.data.CampusApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(CampusApplication.class)).createLegacy();
						resultSet =  EntityFinder.findAllByColumn(ca, ca.getApplicationIdColumnName(), a.getID());
						if (resultSet != null && !resultSet.isEmpty()) {
							ca = (CampusApplication) resultSet.iterator().next();
				
							Applied applied = ((is.idega.idegaweb.campus.block.application.data.AppliedHome) com.idega.data.IDOLookup.getHomeLegacy(Applied.class)).createLegacy();
							resultSet = EntityFinder.findAllByColumnOrdered(applied, applied.getApplicationIdColumnName(), ca.getID(), applied.getOrderColumnName());
				
							if (resultSet != null && !resultSet.isEmpty()) {
								applieds = new Vector(resultSet);
							}
						}
						// Applicant
						applicant = ((com.idega.block.application.data.ApplicantHome) com.idega.data.IDOLookup.getHomeLegacy(Applicant.class)).findByPrimaryKeyLegacy(a.getApplicantId());
				
						// Contracts
						resultSet = ContractFinder.listOfApplicantContracts(applicant.getID(),ContractBMPBean.statusCreated);
						if (resultSet != null && !resultSet.isEmpty()) {
							contract = (Contract) resultSet.iterator().next();
						}
				
						// Waitinglist entries
						resultSet =  listOfWaitinglist(applicant.getID());
						if (resultSet != null){
							wl = new Vector(resultSet);
						}
						cah = new CampusApplicationHolder(a, applicant, ca, applieds, contract, wl);
						return cah;
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				return null;
		/*
		CampusApplicationHolder cah = null;
		List resultSet = null;
		if (a != null) {
			try {
				CampusApplication ca = ((is.idega.idegaweb.campus.block.application.data.CampusApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(CampusApplication.class)).createLegacy();
				resultSet = EntityFinder.findAllByColumn(ca, ca.getApplicationIdColumnName(), a.getID());
				if (resultSet != null)
					ca = (CampusApplication) resultSet.get(0);

				Applied applied = ((is.idega.idegaweb.campus.block.application.data.AppliedHome) com.idega.data.IDOLookup.getHomeLegacy(Applied.class)).createLegacy();
				resultSet = EntityFinder.findAllByColumnOrdered(applied, applied.getApplicationIdColumnName(), ca.getID(), applied.getOrderColumnName());
				Vector v = null;
				if (resultSet != null) {
					v = new Vector(resultSet.size());
					for (int i = 0; i < resultSet.size(); i++) {
						applied = (Applied) resultSet.get(i);
						v.add(applied);
					}
				}

				Applicant applicant = ((com.idega.block.application.data.ApplicantHome) com.idega.data.IDOLookup.getHomeLegacy(Applicant.class)).findByPrimaryKeyLegacy(a.getApplicantId());

				resultSet = ContractFinder.listOfApplicantContracts(applicant.getID());
				Vector contracts = null;
				if (resultSet != null)
					contracts = new Vector(resultSet);

				resultSet = listOfWaitinglist(applicant.getID());
				Vector wl = null;
				if (resultSet != null)
					wl = new Vector(resultSet);

				Contract contract = null;
				if (contracts != null && !contracts.isEmpty())
					contract = (Contract) contracts.elementAt(0);

				ListIterator it = v.listIterator(v.size());
				while (it.hasPrevious()) {
					WaitingList remove = null;
					Applied app = (Applied) it.previous();
					if (wl != null) {
						for (int j = 0; j < wl.size(); j++) {
							WaitingList wait = (WaitingList) wl.elementAt(j);
							if ((wait.getApartmentTypeId().intValue() == app.getApartmentTypeId().intValue()) && (wait.getComplexId().intValue() == app.getComplexId().intValue()))
								remove = wait;
						}
					}

					if (remove != null) {
						if (remove.getRemovedFromList()) {
							it.remove();
						}
					}
				}

				cah = new CampusApplicationHolder(a, applicant, ca, v, contract, wl);
			}
			catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		

		return (cah);
		*/
	}

	/**
	 *
	 */
	public static List listOfWaitinglist(int applicantId) {
		try {
			WaitingList WL = ((is.idega.idegaweb.campus.block.application.data.WaitingListHome) com.idega.data.IDOLookup.getHomeLegacy(WaitingList.class)).createLegacy();
			StringBuffer orderedBy = new StringBuffer(is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.getPriorityColumnName());
			orderedBy.append(", ");
			orderedBy.append(is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.getOrderColumnName());
			List li = EntityFinder.findAllByColumnOrdered(WL, is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.getApplicantIdColumnName(), String.valueOf(applicantId), orderedBy.toString());
			if (li != null) {
				updateWatingListToRightOrder(li);
			}
			return (li);
		}
		catch (SQLException e) {
			return (null);
		}
	}

	/**
	 *
	 */
	private static void updateWatingListToRightOrder(List li) {
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
					count = ((is.idega.idegaweb.campus.block.application.data.WaitingListHome) com.idega.data.IDOLookup.getHomeLegacy(WaitingList.class)).createLegacy().getNumberOfRecords(sql.toString());
				}
				catch (SQLException ex) {
					return;
				}

				if (count < 0)
					count = 1;

				wl.setOrder(count);
			}
		}
	}

	public static String[] getApplicantEmail(int iApplicantId) {
		/*
		  select b.email from cam_application b,app_application a
		  where b.app_application_id = a.app_application_id
		  and a.app_applicant_id = 819
		*/
		StringBuffer sql = new StringBuffer("select c.email from ");
		sql.append(((is.idega.idegaweb.campus.block.application.data.CampusApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(CampusApplication.class)).createLegacy().getEntityName()).append(" c,");
		sql.append(com.idega.block.application.data.ApplicationBMPBean.getEntityTableName()).append(" b ");
		sql.append(" where c.app_application_id = b.app_application_id ");
		sql.append(" and b.app_applicant_id = ");
		sql.append(iApplicantId);
		try {
			return com.idega.data.SimpleQuerier.executeStringQuery(sql.toString());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return new String[0];
	}

	public static Application getLastApprovedApplication(Applicant eApplicant) {
		Application eApplication = null;
		if (eApplicant != null) {
			try {
				eApplication = ((com.idega.block.application.data.ApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(Application.class)).createLegacy();
				List l = EntityFinder.findAllByColumn(eApplication, com.idega.block.application.data.ApplicantBMPBean.getStatusColumnName(), "A", com.idega.block.application.data.ApplicationBMPBean.getApplicantIdColumnName(), eApplicant.getID());
				if (l != null) {
					eApplication = (Application) l.get(l.size() - 1);
				}
			}
			catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return (eApplication);
	}

	public static List listOfApplicantsBySSN(String ssn) throws com.idega.data.IDOFinderException {
		return EntityFinder.getInstance().findAllByColumn(Applicant.class, "ssn", ssn);
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

	public static int findMaxTransferInWaitingList(int typeId, int cmplxId) {
		StringBuffer sql = new StringBuffer("select max(app.ordered) ");
		sql.append(" from cam_waiting_list app ");
		sql.append(" where app.bu_apartment_type_id = ");
		sql.append(typeId);
		sql.append(" and app.bu_complex_id =");
		sql.append(cmplxId);
		sql.append(" and app.list_type = 'T'");

		int maxId = 0;
		try {
			maxId = ((is.idega.idegaweb.campus.block.application.data.CampusApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(CampusApplication.class)).createLegacy().getNumberOfRecords(sql.toString());
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}

		if (maxId < 0)
			maxId = 0;
		return maxId;
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
	
	public static WaitingList findRightPlaceForTransfer(WaitingList wl) {
		
		int cmplx = wl.getComplexId().intValue();
		int aprttype = wl.getApartmentTypeId().intValue();
		
		int lastTransfer = findMaxTransferInWaitingList(aprttype,cmplx);

		java.util.Collection transfers = nextForTransfer(aprttype,cmplx,lastTransfer);
		
		if (transfers.size() > 4) {
			java.util.Iterator it = transfers.iterator();
			WaitingList wl2 = null;
			for (int i = 0; i < 4; i++)
				wl2 = (WaitingList)it.next();
				
			wl.setOrder(wl2.getOrder());
		}

		return wl;
	}
}