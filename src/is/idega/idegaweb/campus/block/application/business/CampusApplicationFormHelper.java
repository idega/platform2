/*
 * $Id: CampusApplicationFormHelper.java,v 1.21 2004/06/07 18:41:21 aron Exp $
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

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
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
			applied1 = ((AppliedHome) IDOLookup.getHome(Applied.class)).create();
			int type = ApartmentTypeComplexHelper.getPartKey(key1, 1);
			int complex = ApartmentTypeComplexHelper.getPartKey(key1, 2);
			applied1.setApartmentTypeId(type);
			applied1.setComplexId(complex);
			applied1.setOrder(1);
			
			if ((key2 != null) && (!key2.equalsIgnoreCase("-1"))) {
				applied2 = ((AppliedHome) IDOLookup.getHome(Applied.class)).create();
				type = ApartmentTypeComplexHelper.getPartKey(key2, 1);
				complex = ApartmentTypeComplexHelper.getPartKey(key2, 2);
				applied2.setApartmentTypeId(type);
				applied2.setComplexId(complex);
				applied2.setOrder(2);
			}
			
			if ((key3 != null) && (!key3.equalsIgnoreCase("-1"))) {
				applied3 = ((AppliedHome) IDOLookup.getHome(Applied.class)).create();
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
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (CreateException e) {
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
		Application application = (Application) iwc.getSessionAttribute("application");
		CampusApplication campusApplication = (CampusApplication) iwc.getSessionAttribute("campusapplication");
		Applied applied1 = (Applied) iwc.getSessionAttribute("applied1");
		Applied applied2 = (Applied) iwc.getSessionAttribute("applied2");
		Applied applied3 = (Applied) iwc.getSessionAttribute("applied3");

		String cypher = "";

		javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();

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

			application.setApplicantId(((Integer)applicant.getPrimaryKey()).intValue());
			application.store();

			campusApplication.setAppApplicationId(((Integer)application.getPrimaryKey()).intValue());
			campusApplication.store();

			applied1.setApplicationId(new Integer(campusApplication.getPrimaryKey().toString()));
			applied1.store();

			if (applied2 != null) {
				applied2.setApplicationId(new Integer(campusApplication.getPrimaryKey().toString()));
				applied2.store();
			}

			if (applied3 != null) {
				applied3.setApplicationId(new Integer(campusApplication.getPrimaryKey().toString()));
				applied3.store();
			}
			/*
			      ReferenceNumberHandler h = new ReferenceNumberHandler();
			      String key = h.getCypherKey(iwc);
			      CypherText ct = new CypherText();
			
			      String id = Integer.toString(application.getID());
			      while (id.length() < 6)
			        id = "0" + id;
			
			      cypher = ct.doCyper(id,key);
			*/

			cypher = ReferenceNumberFinder.getInstance(iwc).lookup(((Integer)application.getPrimaryKey()).intValue());

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
		}
		catch (Exception e) {
			try {
				t.rollback();
			}
			catch (javax.transaction.SystemException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
			return (null);
		}

		String e_mail = campusApplication.getEmail();
		if (e_mail != null) {
			if (e_mail.length() > 0) {
				try {
					MailingListService MailingListBusiness = (MailingListService) IBOLookup.getServiceInstance(iwc,MailingListService.class);
					MailingListBusiness.processMailEvent( new EntityHolder(applicant), LetterParser.SUBMISSION);
				}
				catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		}

		return (cypher);
	}

	/**
	 *
	 */
	public static void saveCampusInformation(IWContext iwc){
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

		String faculty = iwc.getParameter("faculty");
		String studyTrack = iwc.getParameter("studyTrack");
		//String resInfo = iwc.getParameter("resInfo");
		String spouseName = iwc.getParameter("spouseName");
		String spouseSSN = iwc.getParameter("spouseSSN");
		String spouseSchool = iwc.getParameter("spouseSchool");
		String spouseStudyTrack = iwc.getParameter("spouseStudyTrack");
		String studyBegin = iwc.getParameter("studyBegin");
		String studyEnd = iwc.getParameter("studyEnd");
		String spouseStudyBegin = iwc.getParameter("spouseStudyBegin");
		String spouseStudyEnd = iwc.getParameter("spouseStudyEnd");

		//String children = iwc.getParameter("children");
		String wantHousingFrom = iwc.getParameter("wantHousingFrom");
		String waitingList = iwc.getParameter("waitingList");
		String furniture = iwc.getParameter("furniture");
		String contact = iwc.getParameter("contact");
		String email = iwc.getParameter("email");
		String info = iwc.getParameter("extra_info");

		CampusApplication application = null;
		Applicant spouse = null;
		try {
			application = ((CampusApplicationHome) IDOLookup.getHome(CampusApplication.class)).create();
			spouse = ((ApplicantHome) IDOLookup.getHome(Applicant.class)).create();
		}
		catch (IDOLookupException e1) {
			e1.printStackTrace();
		}
		catch (CreateException e1) {
			e1.printStackTrace();
		}
		Vector childs = new Vector();

		try {
			currentResidence = Integer.parseInt(iwc.getParameter("currentResidence"));
		}
		catch (java.lang.NumberFormatException e) {
		}

		try {
			spouseOccupation = Integer.parseInt(iwc.getParameter("spouseOccupation"));
		}
		catch (java.lang.NumberFormatException e) {
		}

		if (studyBegin != null) {
			try {
				IWTimestamp stamp = new IWTimestamp(studyBegin);
				studyBeginMon = stamp.getMonth();
				studyBeginYr = stamp.getYear();
			}
			catch (Exception ex) {

			}
		}

		if (studyEnd != null) {
			try {
				IWTimestamp stamp = new IWTimestamp(studyEnd);
				studyEndMo = stamp.getMonth();
				studyEndYr = stamp.getYear();
			}
			catch (Exception ex) {

			}
		}

		if (spouseStudyBegin != null) {
			try {
				IWTimestamp stamp = new IWTimestamp(spouseStudyBegin);
				spouseStudyBeginMo = stamp.getMonth();
				spouseStudyBeginYr = stamp.getYear();
			}
			catch (Exception ex) {

			}
		}

		if (spouseStudyEnd != null) {
			try {
				IWTimestamp stamp = new IWTimestamp(spouseStudyEnd);
				spouseStudyEndMo = stamp.getMonth();
				spouseStudyEndYr = stamp.getYear();
			}
			catch (Exception ex) {

			}
		}

		application.setCurrentResidenceId(currentResidence);
		application.setSpouseOccupationId(spouseOccupation);
		application.setStudyBeginMonth(studyBeginMon);
		application.setStudyBeginYear(studyBeginYr);
		application.setStudyEndMonth(studyEndMo);
		application.setStudyEndYear(studyEndYr);
		application.setFaculty(faculty);
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
					Applicant child = ((ApplicantHome) IDOLookup.getHome(Applicant.class)).create();
					name = iwc.getParameter("childname" + i);
					birth = iwc.getParameter("childbirth" + i);
					if (name.length() > 0) {
						child.setFullName(name);
						child.setSSN(birth);
						child.setStatus("C");
						childs.add(child);
					}
				}
				catch (IDOLookupException e2) {
					e2.printStackTrace();
				}
				catch (CreateException e2) {
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
	public static void saveSubject(IWContext iwc){
		String subject = (String) iwc.getParameter("subject");
		String aprtCat = (String) iwc.getParameter("aprtCat");
		try {
			Application application = ((ApplicationHome) IDOLookup.getHome(Application.class)).create();
			application.setSubjectId(Integer.parseInt(subject));
			application.setSubmitted(IWTimestamp.getTimestampRightNow());
			application.setStatusSubmitted();
			application.setStatusChanged(IWTimestamp.getTimestampRightNow());
			iwc.setSessionAttribute("application", application);
			iwc.setSessionAttribute("aprtCat", aprtCat);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		catch (CreateException e) {
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
			ApartmentTypeHome ath = (ApartmentTypeHome)IDOLookup.getHome(ApartmentType.class);
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
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return (ret);
	}
	
	public static void sendApplicationConfirmationReminderEmail(IWContext iwc) {
//		select app.app_application_id, email from cam_application cam, app_application app where cam.app_application_id = app.app_application_id and app.status = 'A' 
		Map map = CampusApplicationFinder.getEmailAndApplicationIdForApprovedApplications();
		
		Set keys = map.keySet();
		Iterator it = keys.iterator();
		
		ReferenceNumberFinder instance = ReferenceNumberFinder.getInstance(iwc);
		StringBuffer body1 = new StringBuffer("English version follows\n\n");

		body1.append("Ágæti viðtakandi\n\n");
		body1.append("Þú átt inni umsókn um vist á Stúdentagörðum á skrifstofu okkar. Með því að nota tilvísunarnúmer getur þú fylgst með því hvar þú ert á biðlistanum. Farið er inná heimasíðuna okkar sem er: www.studentagardar.is og tilvísunarnúmerið slegið inn í viðeigandi reit og ýtt á get. Þá kemur upp staða þín á biðlistanum. Þar er þér einnig gefinn kostur á því að stafðfesta veru þína á biðlistanum með því að ýta á confirm. Staðfesta þarf á milli 1. og 5. hvers mánaðar, ef því er ekki sinnt dettur þú útaf biðlistanum.\n\n");
		body1.append("Tilvísunarnúmerið þitt er: ");

		StringBuffer tail1 = new StringBuffer("Skrifstofa Stúdentagarða\n");
		//tail1.append("Vilborg Sverrisdóttir\n\n");
		tail1.append("Þér er einnig bent á að setja inn netfang á þessari síðu og sjá til þess að það sem og símanúmerið sé alltaf rétt. Þetta er mjög mikilvægt svo hægt sé að ná sambandi við þig þegar þörf krefur.\n");


		StringBuffer body2 = new StringBuffer("To Whom It May Concern:\n\n");
		body2.append("You have an application at Student Housing, by using your reference number you can monitor where you are on the waiting list. To do that you go on our website: www.studenthousing.is and write your reference number in tilv?sunarnr. and push get. There you can see your status on the waiting list and there you also have to confirm that you want to stay on the waiting list, you have to do that between the 1st and the 5th of each month. If that is not done you will loose your place on the waiting list. At this site you can also update your phone number and e-mail address. It is very important that you keep these information always updated.\n\n");
		body2.append("Your reference number is: ");

		StringBuffer tail2 = new StringBuffer("Best regards,\n");
		tail2.append("Student Housing,\n");
		tail2.append("Tel: 5 700 800\n");
		tail2.append("E-mail: studentagardar@fs.is");

		while (it.hasNext()) {
			Integer id = (Integer)it.next();
			String email = (String)map.get(id);
			
			String cypher = instance.lookup(id.intValue());

			StringBuffer totalText = new StringBuffer(body1.toString());
			totalText.append(cypher);
			totalText.append("\n\n");
			totalText.append(tail1.toString());
			totalText.append("\n\n");
			totalText.append(body2.toString());
			totalText.append(cypher);
			totalText.append("\n\n");
			totalText.append(tail2.toString());
						
			try {
				SendMail.send("postmaster@studenthousing.is",email,null,"palli@idega.is","mail.idega.is","?minning/Reminder",totalText.toString());
			}
			catch (MessagingException e) {
				e.printStackTrace();
			}
			
		}
		
	}
}