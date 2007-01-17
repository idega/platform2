/*
 * $Id: CampusApplicationFormHelper.java,v 1.23.4.3 2007/01/17 22:53:18 palli Exp $
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
import is.idega.idegaweb.campus.block.mailinglist.business.EntityHolder;
import is.idega.idegaweb.campus.block.mailinglist.business.LetterParser;
import is.idega.idegaweb.campus.block.mailinglist.business.MailingListService;
import is.idega.idegaweb.campus.business.CampusService;
import is.idega.idegaweb.campus.business.CampusSettings;
import is.idega.idegaweb.campus.presentation.CampusBlock;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.mail.MessagingException;

import com.idega.block.application.business.ApplicationFormHelper;
import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.ApplicantHome;
import com.idega.block.application.data.Application;
import com.idega.block.application.data.ApplicationHome;
import com.idega.block.building.business.ApartmentTypeComplexHelper;
import com.idega.block.building.data.ApartmentType;
import com.idega.block.building.data.ApartmentTypeHome;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;
import com.idega.util.SendMail;

/**
 * 
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class CampusApplicationFormHelper extends ApplicationFormHelper {

	/**
	 * 
	 */
	public static void saveAppliedFor(IWContext iwc) {
		String key1 = (String) iwc.getParameter("aprtType");
		String key2 = (String) iwc.getParameter("aprtType2");
		String key3 = (String) iwc.getParameter("aprtType3");

		Applied applied1 = null;
		Applied applied2 = null;
		Applied applied3 = null;

		try {
			applied1 = ((AppliedHome) IDOLookup.getHome(Applied.class))
					.create();
			int type = ApartmentTypeComplexHelper.getPartKey(key1, 1);
			int complex = ApartmentTypeComplexHelper.getPartKey(key1, 2);
			applied1.setApartmentTypeId(type);
			applied1.setComplexId(complex);
			applied1.setOrder(1);

			if ((key2 != null) && (!key2.equalsIgnoreCase("-1"))) {
				applied2 = ((AppliedHome) IDOLookup.getHome(Applied.class))
						.create();
				type = ApartmentTypeComplexHelper.getPartKey(key2, 1);
				complex = ApartmentTypeComplexHelper.getPartKey(key2, 2);
				applied2.setApartmentTypeId(type);
				applied2.setComplexId(complex);
				applied2.setOrder(2);
			}

			if ((key3 != null) && (!key3.equalsIgnoreCase("-1"))) {
				applied3 = ((AppliedHome) IDOLookup.getHome(Applied.class))
						.create();
				type = ApartmentTypeComplexHelper.getPartKey(key3, 1);
				complex = ApartmentTypeComplexHelper.getPartKey(key3, 2);
				applied3.setApartmentTypeId(type);
				applied3.setComplexId(complex);
				applied3.setOrder(3);
			}

			iwc.setSessionAttribute("applied1", applied1);
			if (applied2 != null)
				iwc.setSessionAttribute("applied2", applied2);
			if (applied3 != null)
				iwc.setSessionAttribute("applied3", applied3);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public static String saveDataToDB(IWContext iwc) {
		Applicant applicant = (Applicant) iwc.getSessionAttribute("applicant");
		Applicant spouse = (Applicant) iwc.getSessionAttribute("spouse");
		Vector childs = (Vector) iwc.getSessionAttribute("childs");
		Application application = (Application) iwc
				.getSessionAttribute("application");
		CampusApplication campusApplication = (CampusApplication) iwc
				.getSessionAttribute("campusapplication");
		Applied applied1 = (Applied) iwc.getSessionAttribute("applied1");
		Applied applied2 = (Applied) iwc.getSessionAttribute("applied2");
		Applied applied3 = (Applied) iwc.getSessionAttribute("applied3");

		String cypher = "";

		javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager
				.getInstance();
		if (applicant != null) {
			try {
				t.begin();
				applicant.store();
				applicant.addChild(applicant);

				if (spouse != null) {
					spouse.store();
					applicant.addChild(spouse);
				}

				if (childs != null && childs.size() > 0) {
					for (int i = 0; i < childs.size(); i++) {
						Applicant child = (Applicant) childs.get(i);
						child.store();
						applicant.addChild(child);
					}
				}

				application
						.setApplicantId(((Integer) applicant.getPrimaryKey())
								.intValue());
				application.store();

				campusApplication.setAppApplicationId(((Integer) application
						.getPrimaryKey()).intValue());
				campusApplication.store();

				applied1.setApplicationId(new Integer(campusApplication
						.getPrimaryKey().toString()));
				applied1.store();

				if (applied2 != null) {
					applied2.setApplicationId(new Integer(campusApplication
							.getPrimaryKey().toString()));
					applied2.store();
				}

				if (applied3 != null) {
					applied3.setApplicationId(new Integer(campusApplication
							.getPrimaryKey().toString()));
					applied3.store();
				}

				cypher = ReferenceNumberFinder.getInstance(iwc).lookup(
						((Integer) application.getPrimaryKey()).intValue());

				t.commit();
				iwc.removeSessionAttribute("applicant");
				iwc.removeSessionAttribute("spouse");
				iwc.removeSessionAttribute("childs");
				iwc.removeSessionAttribute("application");
				iwc.removeSessionAttribute("campusapplication");
				iwc.removeSessionAttribute("applied1");
				iwc.removeSessionAttribute("applied2");
				iwc.removeSessionAttribute("applied3");
				iwc.removeSessionAttribute("aprtCat");
			} catch (Exception e) {
				try {
					t.rollback();
				} catch (javax.transaction.SystemException ex) {
					ex.printStackTrace();
				}
				e.printStackTrace();
				return null;
			}
			
			String e_mail = campusApplication.getEmail();
			if (e_mail != null && applicant != null) {
				if (e_mail.length() > 0) {
					try {
						MailingListService MailingListBusiness = (MailingListService) IBOLookup
								.getServiceInstance(iwc, MailingListService.class);
						MailingListBusiness.processMailEvent(new EntityHolder(
								applicant), LetterParser.SUBMISSION);
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}
			}
		}

		return cypher;
	}

	/**
	 * 
	 */
	public static void saveCampusInformation(IWContext iwc) {
		int studyBeginMon = 0;
		int studyBeginYr = 0;
		int studyEndMo = 0;
		int studyEndYr = 0;
		int spouseStudyBeginMo = 0;
		int spouseStudyBeginYr = 0;
		int spouseStudyEndMo = 0;
		int spouseStudyEndYr = 0;
		int currentResidence = 0;
		int spouseOccupation = 0;
		int schoolID = 0;

		String faculty = iwc.getParameter("faculty");
		String school = iwc.getParameter("school");
		String studyTrack = iwc.getParameter("studyTrack");
		// String resInfo = iwc.getParameter("resInfo");
		String spouseName = iwc.getParameter("spouseName");
		String spouseSSN = iwc.getParameter("spouseSSN");
		String spouseSchool = iwc.getParameter("spouseSchool");
		String spouseStudyTrack = iwc.getParameter("spouseStudyTrack");
		String studyBegin = iwc.getParameter("studyBegin");
		String studyEnd = iwc.getParameter("studyEnd");
		String spouseStudyBegin = iwc.getParameter("spouseStudyBegin");
		String spouseStudyEnd = iwc.getParameter("spouseStudyEnd");

		// String children = iwc.getParameter("children");
		String wantHousingFrom = iwc.getParameter("wantHousingFrom");
		String waitingList = iwc.getParameter("waitingList");
		String furniture = iwc.getParameter("furniture");
		String contact = iwc.getParameter("contact");
		String email = iwc.getParameter("email");
		String info = iwc.getParameter("extra_info");

		CampusApplication application = null;
		Applicant spouse = null;
		try {
			application = ((CampusApplicationHome) IDOLookup
					.getHome(CampusApplication.class)).create();
			spouse = ((ApplicantHome) IDOLookup.getHome(Applicant.class))
					.create();
		} catch (IDOLookupException e1) {
			e1.printStackTrace();
		} catch (CreateException e1) {
			e1.printStackTrace();
		}
		Vector childs = new Vector();

		try {
			currentResidence = Integer.parseInt(iwc
					.getParameter("currentResidence"));
		} catch (java.lang.NumberFormatException e) {
		}

		try {
			spouseOccupation = Integer.parseInt(iwc
					.getParameter("spouseOccupation"));
		} catch (java.lang.NumberFormatException e) {
		}

		try {
			schoolID = new Integer(school).intValue();
		} catch (NumberFormatException e) {

		}

		if (studyBegin != null) {
			try {
				IWTimestamp stamp = new IWTimestamp(studyBegin);
				studyBeginMon = stamp.getMonth();
				studyBeginYr = stamp.getYear();
			} catch (Exception ex) {

			}
		}

		if (studyEnd != null) {
			try {
				IWTimestamp stamp = new IWTimestamp(studyEnd);
				studyEndMo = stamp.getMonth();
				studyEndYr = stamp.getYear();
			} catch (Exception ex) {

			}
		}

		if (spouseStudyBegin != null) {
			try {
				IWTimestamp stamp = new IWTimestamp(spouseStudyBegin);
				spouseStudyBeginMo = stamp.getMonth();
				spouseStudyBeginYr = stamp.getYear();
			} catch (Exception ex) {

			}
		}

		if (spouseStudyEnd != null) {
			try {
				IWTimestamp stamp = new IWTimestamp(spouseStudyEnd);
				spouseStudyEndMo = stamp.getMonth();
				spouseStudyEndYr = stamp.getYear();
			} catch (Exception ex) {

			}
		}

		application.setCurrentResidenceId(currentResidence);
		application.setSpouseOccupationId(spouseOccupation);
		application.setStudyBeginMonth(studyBeginMon);
		application.setStudyBeginYear(studyBeginYr);
		application.setStudyEndMonth(studyEndMo);
		application.setStudyEndYear(studyEndYr);
		if (faculty != null) {
			application.setFaculty(faculty);
		}
		if (school != null) {
			application.setSchoolID(schoolID);
		}
		application.setStudyTrack(studyTrack);
		application.setSpouseName(spouseName);
		application.setSpouseSSN(spouseSSN);
		application.setSpouseSchool(spouseSchool);
		application.setSpouseStudyTrack(spouseStudyTrack);
		application.setSpouseStudyBeginMonth(spouseStudyBeginMo);
		application.setSpouseStudyBeginYear(spouseStudyBeginYr);
		application.setSpouseStudyEndMonth(spouseStudyEndMo);
		application.setSpouseStudyEndYear(spouseStudyEndYr);

		IWTimestamp t = new IWTimestamp(wantHousingFrom);
		System.out.println("want housing from = " + t.toString());
		application.setHousingFrom(t.getDate());
		if (waitingList == null)
			application.setOnWaitinglist(false);
		else
			application.setOnWaitinglist(true);
		if (furniture == null)
			application.setWantFurniture(false);
		else
			application.setWantFurniture(true);
		application.setContactPhone(contact);
		application.setOtherInfo(info);
		application.setEmail(email);

		// spouse part
		if (spouseName.length() > 0) {
			spouse.setFullName(spouseName);
			spouse.setSSN(spouseSSN);
			spouse.setStatus("P");
			iwc.setSessionAttribute("spouse", spouse);
		}
		// Children part
		if (iwc.isParameterSet("children_count")) {
			int count = Integer.parseInt(iwc.getParameter("children_count"));
			String name, birth;
			for (int i = 0; i < count; i++) {
				try {
					Applicant child = ((ApplicantHome) IDOLookup
							.getHome(Applicant.class)).create();
					name = iwc.getParameter("childname" + i);
					birth = iwc.getParameter("childbirth" + i);
					if (name.length() > 0) {
						child.setFullName(name);
						child.setSSN(birth);
						child.setStatus("C");
						childs.add(child);
					}
				} catch (IDOLookupException e2) {
					e2.printStackTrace();
				} catch (CreateException e2) {
					e2.printStackTrace();
				}
			}
			iwc.setSessionAttribute("childs", childs);
		}
		iwc.setSessionAttribute("campusapplication", application);
	}

	/**
	 * 
	 */
	public static void saveSubject(IWContext iwc) {
		String subject = (String) iwc.getParameter("subject");
		String aprtCat = (String) iwc.getParameter("aprtCat");
		try {
			Application application = ((ApplicationHome) IDOLookup
					.getHome(Application.class)).create();
			application.setSubjectId(Integer.parseInt(subject));
			application.setSubmitted(IWTimestamp.getTimestampRightNow());
			application.setStatusSubmitted();
			application.setStatusChanged(IWTimestamp.getTimestampRightNow());
			iwc.setSessionAttribute("application", application);
			iwc.setSessionAttribute("aprtCat", aprtCat);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public static Vector checkAparmentTypesSelected(IWContext iwc) {
		String key1 = (String) iwc.getParameter("aprtType");
		String key2 = (String) iwc.getParameter("aprtType2");
		String key3 = (String) iwc.getParameter("aprtType3");

		Vector ret = new Vector(3);

		try {
			ApartmentTypeHome ath = (ApartmentTypeHome) IDOLookup
					.getHome(ApartmentType.class);
			int type = ApartmentTypeComplexHelper.getPartKey(key1, 1);

			ApartmentType room = ath.findByPrimaryKey(new Integer(type));

			int pic = room.getFloorPlanId();
			ret.add(0, new Integer(pic));

			if ((key2 != null) && (!key2.equalsIgnoreCase("-1"))) {
				type = ApartmentTypeComplexHelper.getPartKey(key2, 1);
				room = ath.findByPrimaryKey(new Integer(type));
				pic = room.getFloorPlanId();
			}
			ret.add(1, new Integer(pic));

			if ((key3 != null) && (!key3.equalsIgnoreCase("-1"))) {
				type = ApartmentTypeComplexHelper.getPartKey(key3, 1);

				room = ath.findByPrimaryKey(new Integer(type));
				pic = room.getFloorPlanId();
			}
			ret.add(2, new Integer(pic));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (ret);
	}

	public static void sendApplicationConfirmationReminderEmail(IWContext iwc) {
		Map map = CampusApplicationFinder
				.getEmailAndApplicationIdForApprovedApplications();

		Set keys = map.keySet();
		Iterator it = keys.iterator();

		IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(
				CampusBlock.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);

		String subject = iwrb.getLocalizedString("REMINDER_MAIL_SUBJECT",
				"Reminder mail subject");
		String body = iwrb.getLocalizedString("REMINDER_MAIL_BODY",
				"Reminder mail body [ref_num]");

		ReferenceNumberFinder instance = ReferenceNumberFinder.getInstance(iwc);

		CampusSettings settings = null;
		try {
			settings = getCampusService(iwc).getCampusSettings();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}

		if (settings != null && settings.getSendEventMail()) {
			while (it.hasNext()) {
				Integer id = (Integer) it.next();
				String email = (String) map.get(id);

				String cypher = instance.lookup(id.intValue());

				StringBuffer finalText = new StringBuffer();
				StringTokenizer st = new StringTokenizer(body, "[]");
				while (st.hasMoreTokens()) {
					String token = st.nextToken();
					if (token.equals("ref_num")) {
						finalText.append(cypher);
					} else {
						finalText.append(token);
					}
				}

				try {
					SendMail.send(settings.getAdminEmail(), email, null,
							"palli@idega.is", settings.getSmtpServer(),
							subject, finalText.toString());
//					SendMail.send(settings.getAdminEmail(), "palli@idega.is", null,
//							"palli@idega.is", settings.getSmtpServer(),
//							subject, finalText.toString());
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static CampusService getCampusService(IWContext iwc)
			throws RemoteException {
		return (CampusService) IBOLookup.getServiceInstance(iwc,
				CampusService.class);
	}
}