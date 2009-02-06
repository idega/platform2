/*
 * $Id: BNCampusApplicationForm.java,v 1.1.2.5 2009/02/06 15:43:53 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.bn.presentation;

import is.idega.idegaweb.campus.block.application.data.ApartmentCategoryCombination;
import is.idega.idegaweb.campus.block.application.data.CurrentResidency;
import is.idega.idegaweb.campus.block.application.data.SpouseOccupation;
import is.idega.idegaweb.campus.block.application.presentation.CampusApplicationForm;
import is.idega.idegaweb.campus.presentation.Edit;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.block.application.data.Applicant;
import com.idega.block.building.data.ApartmentCategory;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;
import com.idega.util.text.SocialSecurityNumber;

/**
 * 
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a> modified by <a
 *         href="mailto:aron@idega.is">
 * @version 1.0
 */
public class BNCampusApplicationForm extends CampusApplicationForm {
	private static final String PARAM_SCHOOL = "school";

	public BNCampusApplicationForm() {
		super();
	}

	/*
	 * 
	 */
	protected void doCampusInformation(IWContext iwc, List wrongParameters) {
		Applicant applicant = (Applicant) iwc.getSessionAttribute("applicant");
		boolean hasManyApplications = checkForApplications(iwc, applicant);

		if (hasManyApplications) {
			add(new Text(iwrb.getLocalizedString("have_many_application","You have one or more applications in the system that have been submitted or approved. Please go in with your reference number and cancel them if you want to create a new application.")));
			return;
		}

		Collection subjects = null;
		try {
			subjects = getApplicationService(iwc).getSubjectHome()
					.findNonExpired();

			Collection categories = getApplicationService(iwc)
					.getBuildingService().getApartmentCategoryHome().findAll();
			Text textTemplate = new Text();
			IWTimestamp today = IWTimestamp.RightNow();
			int fromYear = today.getYear() - 7;
			int toYear = today.getYear() + 7;

			Form form = new Form();
			DataTable t = new DataTable();
			t.setUseBottom(false);

			String text1 = iwrb.getLocalizedString("applicationSubject",
					"Application subject");
			String text2 = iwrb.getLocalizedString("apartmentType",
					"Apartment type");

			DropdownMenu subject = new DropdownMenu(subjects, "subject");

			DropdownMenu aprtCat = new DropdownMenu(categories, "aprtCat");
			Collection col = getCampusService(iwc).getApartmentCategoryCombinationHome().findAll();
			if (col != null && !col.isEmpty()) {
				Iterator it = col.iterator();
				while (it.hasNext()) {
					ApartmentCategoryCombination comb = (ApartmentCategoryCombination) it.next();
					ApartmentCategory cat1 = comb.getCategory1();
					ApartmentCategory cat2 = comb.getCategory2();
					
					StringBuffer key = new StringBuffer(cat1.getPrimaryKey().toString());
					key.append("&");
					key.append(cat2.getPrimaryKey().toString());
					
					StringBuffer name = new StringBuffer(cat1.getName());
					name.append(" & ");
					name.append(cat2.getName());
					
					aprtCat.addMenuElement(key.toString(), name.toString());
				}
			}


			Image back = iwrb.getImage("back.gif");
			back.setMarkupAttribute("onClick", "history.go(-1)");
			SubmitButton ok = new SubmitButton(iwrb.getImage("next.gif", iwrb
					.getLocalizedString("ok", "?fram")));

			form.add(t);

			t.addTitle(iwrb.getLocalizedString("applicationSubjectTitle",
					"Applicatin subject type"));
			t.add(getHeader(text1), 1, 1);
			t.add(getHeader(REQUIRED), 1, 1);
			t.add(subject, 2, 1);
			t.add(getHeader(text2), 1, 2);
			t.add(getHeader(REQUIRED), 1, 2);
			t.add(aprtCat, 2, 2);

			Collection residences = null;
			Collection occupations = null;
			Collection schools = null;
			try {
				residences = getApplicationService(iwc).getResidencyHome()
						.findAll();
				occupations = getApplicationService(iwc)
						.getSpouseOccupationHome().findAll();
				schools = getCampusService(iwc).getSchoolHome().findAll();				
			} catch (RemoteException e1) {
				e1.printStackTrace();
			} catch (FinderException e1) {
				e1.printStackTrace();
			}
			DropdownMenu resSelect = new DropdownMenu("currentResidence");
			if (residences != null) {
				Iterator it = residences.iterator();
				while (it.hasNext()) {
					CurrentResidency currRes = (CurrentResidency) it.next();
					resSelect.addMenuElement(currRes.getPrimaryKey().toString(), iwrb.getLocalizedString(currRes.getLocalizationKey(), currRes.getName()));
				}
			}

			DropdownMenu occSelect = new DropdownMenu("spouseOccupation");
			if (occupations != null) {
				Iterator it = occupations.iterator();
				while (it.hasNext()) {
					SpouseOccupation spouseOcc = (SpouseOccupation) it.next();
					occSelect.addMenuElement(spouseOcc.getPrimaryKey().toString(), iwrb.getLocalizedString(spouseOcc.getLocalizationKey(), spouseOcc.getName()));
				}
			}
			
			DropdownMenu schoolSelect = new DropdownMenu(schools, PARAM_SCHOOL);

			DateInput studyBegin = new DateInput("studyBegin");

			studyBegin.setToShowDay(false);
			studyBegin.setYearRange(fromYear, toYear);
			DateInput studyEnd = new DateInput("studyEnd");

			studyEnd.setToShowDay(false);
			studyEnd.setYearRange(fromYear, toYear);
			DateInput spouseStudyBegin = new DateInput("spouseStudyBegin");

			spouseStudyBegin.setToShowDay(false);
			spouseStudyBegin.setYearRange(fromYear, toYear);
			DateInput spouseStudyEnd = new DateInput("spouseStudyEnd");

			spouseStudyEnd.setToShowDay(false);
			spouseStudyEnd.setYearRange(fromYear, toYear);

			String labelStudyBegin = iwrb.getLocalizedString("studyBegin",
					"Study begin (month/year)");
			String labelStudyEnd = iwrb.getLocalizedString("studyEnd",
					"Study ends (month/year)");
			String labelSchool = iwrb.getLocalizedString("school", "School");
			String labelStudyTrack = iwrb.getLocalizedString("studyTrack",
					"Study track");
			String labelCurrentRes = iwrb.getLocalizedString("currentRes",
					"Current residency");
			String labelSpouseName = iwrb.getLocalizedString("spouseName",
					"Spouses name");
			String labelSpouseSSN = iwrb.getLocalizedString("spouseSSN",
					"Spouses ssn");
			String labelSpouseSchool = iwrb.getLocalizedString("spouseSchool",
					"Spouses school");
			String labelSpouseTrack = iwrb.getLocalizedString(
					"spouseStudyTrack", "Spouses study track");
			String labelSpouseBegin = iwrb.getLocalizedString(
					"spouseStudyBegin", "Spouse begins study (month/year)");
			String labelSpouseEnd = iwrb.getLocalizedString("spouseStudyEnd",
					"Spouse ends study (month/year)");
			String labelSpouseOcc = iwrb.getLocalizedString(
					"spouseOccupation", "Spouses occupation");
			String labelChildren = iwrb.getLocalizedString("children",
					"Children living with applicant");
			String labelHousingFrom = iwrb.getLocalizedString(
					"wantHousingFrom", "Want housing from");
			String labelContact = iwrb
					.getLocalizedString(
							"contact",
							"Other contact phone");
			String labelEmail = iwrb
					.getLocalizedString("email", "Email");
			String labelInfo = iwrb.getLocalizedString("info",
					"Other info");

			TextInput textInputTemplate = new TextInput();

/*			TextInput inputFaculty = (TextInput) textInputTemplate.clone();
			inputFaculty.setName(PARAM_FACULTY);
			if (iwc.isParameterSet(PARAM_FACULTY))
				inputFaculty.setContent(iwc.getParameter(PARAM_FACULTY));*/

			TextInput inputTrack = (TextInput) textInputTemplate.clone();
			inputTrack.setName("studyTrack");
			if (iwc.isParameterSet("studyTrack"))
				inputTrack.setContent(iwc.getParameter("studyTrack"));

			TextInput inputResInfo = (TextInput) textInputTemplate.clone();
			inputResInfo.setName("resInfo");
			inputResInfo.setLength(10);
			if (iwc.isParameterSet("resInfo"))
				inputResInfo.setContent(iwc.getParameter("resInfo"));

			TextInput inputSpouseName = (TextInput) textInputTemplate.clone();
			inputSpouseName.setName("spouseName");
			if (iwc.isParameterSet("spouseName"))
				inputSpouseName.setContent(iwc.getParameter("spouseName"));

			TextInput inputSpouseSSN = (TextInput) textInputTemplate.clone();
			inputSpouseSSN.setName("spouseSSN");
			inputSpouseSSN.setMaxlength(10);
			inputSpouseSSN.setLength(12);
			if (iwc.isParameterSet("spouseSSN"))
				inputSpouseSSN.setContent(iwc.getParameter("spouseSSN"));

			TextInput inputSpouseSchool = (TextInput) textInputTemplate.clone();
			inputSpouseSchool.setName("spouseSchool");
			if (iwc.isParameterSet("spouseSchool"))
				inputSpouseSchool.setContent(iwc.getParameter("spouseSchool"));

			TextInput inputSpouseTrack = (TextInput) textInputTemplate.clone();
			inputSpouseTrack.setName("spouseStudyTrack");
			if (iwc.isParameterSet("spouseStudyTrack"))
				inputSpouseTrack.setContent(iwc
						.getParameter("spouseStudyTrack"));

			TextInput inputContact = (TextInput) textInputTemplate.clone();
			inputContact.setName("contact");
			inputContact.setLength(10);
			if (iwc.isParameterSet("contact"))
				inputContact.setContent(iwc.getParameter("contact"));

			TextInput inputEmail = (TextInput) textInputTemplate.clone();
			String needEmail = iwrb.getLocalizedString("enter_correct_email",
					"Please enter a valid email");
			inputEmail.setAsEmail(needEmail);
			inputEmail.setAsNotEmpty(needEmail);
			inputEmail.setName("email");
			if (iwc.isParameterSet("email"))
				inputEmail.setContent(iwc.getParameter("email"));

			int children = 4;
			Table childrenTable = new Table(2, children);
			for (int i = 0; i < children; i++) {
				TextInput childName = new TextInput("childname" + i);
				TextInput childBirth = new TextInput("childbirth" + i);
				if (iwc.isParameterSet("childname" + i))
					childName.setContent(iwc.getParameter("childname" + i));
				if (iwc.isParameterSet("childbirth" + i))
					childBirth.setContent(iwc.getParameter("childbirth" + i));

				childName.setLength(40);
				childBirth.setLength(10);
				childBirth.setMaxlength(10);
				childrenTable.add(childName, 1, i + 1);
				childrenTable.add(childBirth, 2, i + 1);
			}
			childrenTable.add(new HiddenInput("children_count", String
					.valueOf(children)));

			TextArea inputExtraInfo = new TextArea("extra_info");

			Edit.setStyle(inputExtraInfo);
			inputExtraInfo.setRows(4);
			inputExtraInfo.setColumns(30);

			DateInput input16 = new DateInput("wantHousingFrom");
			if (iwc.isParameterSet("wantHousingFrom")) {
				String sdate = iwc.getParameter("wantHousingFrom");
				if (sdate != null && !"".equals(sdate))
					input16.setDate(new IWTimestamp(sdate).getDate());
			}
			input16.setToCurrentDate();

			DataTable t2 = new DataTable();

			form.add(t2);

			t2.addTitle(iwrb.getLocalizedString("otherInfo",
					"A?rar uppl?singar um ums?kjanda"));
			int row = 1;
			Text label = getHeader(labelStudyBegin);
			if (wrongParameters.contains("studyBegin"))
				label.setFontColor("#ff0000");
			t2.add(label, 1, row);
			t2.add(getHeader(REQUIRED), 1, row);
			t2.add(studyBegin, 2, row);
			row++;

			label = getHeader(labelStudyEnd);
			if (wrongParameters.contains("studyEnd"))
				label.setFontColor("#ff0000");
			t2.add(label, 1, row);
			t2.add(getHeader(REQUIRED), 1, row);
			t2.add(studyEnd, 2, row);
			row++;

			label = getHeader(labelSchool);
			if (wrongParameters.contains(PARAM_SCHOOL))
				label.setFontColor("#ff0000");
			t2.add(label, 1, row);
			t2.add(getHeader(REQUIRED), 1, row);
			t2.add(schoolSelect, 2, row);
			row++;

			label = getHeader(labelStudyTrack);
			if (wrongParameters.contains("studyTrack"))
				label.setFontColor("#ff0000");
			t2.add(label, 1, row);
			t2.add(getHeader(REQUIRED), 1, row);
			t2.add(inputTrack, 2, row);
			row++;

			t2.add(getHeader(labelCurrentRes), 1, row);
			t2.add(getHeader(REQUIRED), 1, row);
			t2.add(resSelect, 2, row);
			t2.add(inputResInfo, 2, row);
			row++;

			t2.add(getHeader(labelSpouseName), 1, row);
			t2.add(inputSpouseName, 2, row);
			row++;

			t2.add(getHeader(labelSpouseSSN), 1, row);
			t2.add(inputSpouseSSN, 2, row);
			row++;

			t2.add(getHeader(labelSpouseSchool), 1, row);
			t2.add(inputSpouseSchool, 2, row);
			row++;

			t2.add(getHeader(labelSpouseTrack), 1, row);
			t2.add(inputSpouseTrack, 2, row);
			row++;

			t2.add(getHeader(labelSpouseBegin), 1, row);
			t2.add(spouseStudyBegin, 2, row);
			row++;

			t2.add(getHeader(labelSpouseEnd), 1, row);
			t2.add(spouseStudyEnd, 2, row);
			row++;

			t2.add(getHeader(labelSpouseOcc), 1, row);
			t2.add(occSelect, 2, row);
			row++;
			t2.add(getHeader(labelChildren), 1, row);
			t2.add(childrenTable, 2, row);
			row++;

			label = getHeader(labelHousingFrom);
			if (wrongParameters.contains("wantHousingFrom"))
				label.setFontColor("#ff0000");
			t2.add(label, 1, row);
			t2.add(getHeader(REQUIRED), 1, row);
			t2.add(input16, 2, row);
			row++;

			t2.add(getHeader(labelContact), 1, row);
			t2.add(inputContact, 2, row);
			row++;

			label = getHeader(labelEmail);
			if (wrongParameters.contains("email"))
				label.setFontColor("#ff0000");
			t2.add(label, 1, row);
			t2.add(getHeader(REQUIRED), 1, row);
			t2.add(inputEmail, 2, row);
			row++;

			t2.add(getHeader(labelInfo), 1, row);
			t2.add(inputExtraInfo, 2, row);
			row++;

			t2.addButton(back);
			t2.addButton(ok);

			form.add(Text.getBreak());
			form.add(Text.getBreak());
			form.add(info);
			form.add(new HiddenInput(APP_STATUS, Integer
					.toString(STATUS_CAMPUS_INFO)));
			add(form);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 */
	protected void doGeneralInformation(IWContext iwc, List wrongParameters) {
		TextInput textInputTemplate = new TextInput();
		Form form = new Form();
		DataTable t = new DataTable();
		SubmitButton ok = new SubmitButton(iwrb.getImage("next.gif", iwrb
				.getLocalizedString("okk", "?fram")));
		ok.setName("ok");

		String heading = iwrb.getLocalizedString(APP_GENINFO,
				"General information about applicant");
		String firstNameLabel = iwrb.getLocalizedString(APP_FIRST_NAME,
				"First name");
		String middleNameLabel = iwrb.getLocalizedString(APP_MIDDLE_NAME,
				"Middle name");
		String lastNameLabel = iwrb.getLocalizedString(APP_LAST_NAME,
				"Last name");
		String ssnLabel = iwrb.getLocalizedString(APP_SSN,
				"Social security number");
		String legalResidenceLabel = iwrb.getLocalizedString(
				APP_LEGAL_RESIDENCE, "Legal residence");
		String residenceLabel = iwrb.getLocalizedString(APP_RESIDENCE,
				"Residence");
		String phoneLabel = iwrb.getLocalizedString(APP_PHONE,
				"Residence phone");
		String mobileLabel = iwrb.getLocalizedString(APP_MOBILE,
				"Mobile phone");
		String poLabel = iwrb.getLocalizedString(APP_PO, "PO");

		TextInput firstName = (TextInput) textInputTemplate.clone();
		firstName.setName(APP_FIRST_NAME);
		if (iwc.isParameterSet(APP_FIRST_NAME))
			firstName.setContent(iwc.getParameter(APP_FIRST_NAME));
		firstName.setLength(40);

		TextInput middleName = (TextInput) textInputTemplate.clone();
		middleName.setName(APP_MIDDLE_NAME);
		if (iwc.isParameterSet(APP_MIDDLE_NAME))
			middleName.setContent(iwc.getParameter(APP_MIDDLE_NAME));
		middleName.setLength(40);

		TextInput lastName = (TextInput) textInputTemplate.clone();
		lastName.setName(APP_LAST_NAME);
		if (iwc.isParameterSet(APP_LAST_NAME))
			lastName.setContent(iwc.getParameter(APP_LAST_NAME));
		lastName.setLength(40);

		TextInput ssn = (TextInput) textInputTemplate.clone();
		ssn.setAsIcelandicSSNumber(iwrb.getLocalizedString(
				"provide_icelandic_ss",
				"Please provide a valid Icelandic personal ID"));
		ssn.setName(APP_SSN);
		if (iwc.isParameterSet(APP_SSN))
			ssn.setContent(iwc.getParameter(APP_SSN));
		ssn.setLength(10);
		ssn.setMaxlength(10);

		TextInput legalResidence = (TextInput) textInputTemplate.clone();
		legalResidence.setName(APP_LEGAL_RESIDENCE);
		if (iwc.isParameterSet(APP_LEGAL_RESIDENCE))
			legalResidence.setContent(iwc.getParameter(APP_LEGAL_RESIDENCE));
		legalResidence.setLength(40);

		TextInput residence = (TextInput) textInputTemplate.clone();
		residence.setName(APP_RESIDENCE);
		if (iwc.isParameterSet(APP_RESIDENCE))
			residence.setContent(iwc.getParameter(APP_RESIDENCE));
		residence.setLength(40);

		TextInput phone = (TextInput) textInputTemplate.clone();
		phone.setName(APP_PHONE);
		if (iwc.isParameterSet(APP_PHONE))
			phone.setContent(iwc.getParameter(APP_PHONE));
		phone.setLength(8);

		TextInput po = (TextInput) textInputTemplate.clone();
		po.setName(APP_PO);
		if (iwc.isParameterSet(APP_PO))
			po.setContent(iwc.getParameter(APP_PO));
		po.setLength(3);

		TextInput mobile = (TextInput) textInputTemplate.clone();
		mobile.setName(APP_MOBILE);
		if (iwc.isParameterSet(APP_MOBILE))
			mobile.setContent(iwc.getParameter(APP_MOBILE));
		mobile.setLength(8);

		int row = 1;
		t.addTitle(heading);
		Text label = getHeader(firstNameLabel);
		if (wrongParameters.contains(APP_FIRST_NAME))
			label.setFontColor("#ff0000");
		t.add(label, 1, row);
		t.add(getHeader(REQUIRED), 1, row);
		t.add(firstName, 2, row);
		row++;
		label = getHeader(middleNameLabel);
		t.add(label, 1, row);
		t.add(middleName, 2, row);
		row++;
		label = getHeader(lastNameLabel);
		if (wrongParameters.contains(APP_LAST_NAME))
			label.setFontColor("#ff0000");
		t.add(label, 1, row);
		t.add(getHeader(REQUIRED), 1, row);
		t.add(lastName, 2, row);
		row++;
		label = getHeader(ssnLabel);
		if (wrongParameters.contains(APP_SSN))
			label.setFontColor("#ff0000");
		t.add(label, 1, row);
		t.add(getHeader(REQUIRED), 1, row);
		t.add(ssn, 2, row);
		row++;
		label = getHeader(legalResidenceLabel);
		if (wrongParameters.contains(APP_LEGAL_RESIDENCE))
			label.setFontColor("#ff0000");
		t.add(label, 1, row);
		t.add(getHeader(REQUIRED), 1, row);
		t.add(legalResidence, 2, row);
		row++;
		label = getHeader(residenceLabel);
		if (wrongParameters.contains(APP_RESIDENCE))
			label.setFontColor("#ff0000");
		t.add(label, 1, row);
		t.add(getHeader(REQUIRED), 1, row);
		t.add(residence, 2, row);
		row++;
		label = getHeader(phoneLabel);
		if (wrongParameters.contains(APP_PHONE))
			label.setFontColor("#ff0000");
		t.add(label, 1, row);
		t.add(getHeader(REQUIRED), 1, row);
		t.add(phone, 2, row);
		row++;
		label = getHeader(poLabel);
		if (wrongParameters.contains(APP_PO))
			label.setFontColor("#ff0000");
		t.add(label, 1, row);
		t.add(getHeader(REQUIRED), 1, row);
		t.add(po, 2, row);
		row++;
		label = getHeader(mobileLabel);
		t.add(label, 1, row);
		// t.add(_required,1,row);
		t.add(mobile, 2, row);
		row++;
		CheckBox acceptance = new CheckBox("acceptor");

		Text disclaimer = getHeader(iwrb
				.getLocalizedString(
						"disclaimer",
						"Ums?kjandi heimilar St?dentag?r?um a? s?kja uppl?singar um skr?ningu e?a n?msframvindu til H?sk?la ?slands, eignarst??u fasteigna til Fasteignarmats r?kisins og fj?lskyldust?r? e?a barnafj?lda til Hagstofu ?slands."));
		t.add(acceptance, 1, row);
		Text accReq = getHeader(REQUIRED);
		if (wrongParameters.contains("acceptor")) {
			accReq.setFontColor("#ff0000");
			accReq.setText("  *  ");
		}
		t.add(accReq, 1, row);
		t.getContentTable().setWidth(2, row, "150");
		t.getContentTable().setColor(2, row, "ff0000");
		t.add(disclaimer, 2, row);

		t.addButton(ok);

		Table frame = new Table(1, 4);

		frame.add(t, 1, 1);
		frame.add(info, 1, 4);
		form.add(frame);
		form.add(new HiddenInput(APP_STATUS, Integer
				.toString(STATUS_GENERAL_INFO)));
		add(form);
	}

	public List checkGeneral(IWContext iwc) throws RemoteException {
		List wrongParameters = new Vector();
		String first = iwc.getParameter(APP_FIRST_NAME);
		String last = iwc.getParameter(APP_LAST_NAME);
		String ssn = iwc.getParameter(APP_SSN);
		String legal = iwc.getParameter(APP_LEGAL_RESIDENCE);
		String res = iwc.getParameter(APP_RESIDENCE);
		String phone = iwc.getParameter(APP_PHONE);
		String zip = iwc.getParameter(APP_PO);
		String accept = iwc.getParameter("acceptor");
		if (first == null || first.length() == 0)
			wrongParameters.add(APP_FIRST_NAME);
		if (last == null || last.length() == 0)
			wrongParameters.add(APP_LAST_NAME);
		if (ssn == null|| !SocialSecurityNumber.isValidIcelandicSocialSecurityNumber(ssn) || !this.isValidAge(ssn,iwc) ){
			wrongParameters.add(APP_SSN);
		}
		if (legal == null || legal.length() == 0)
			wrongParameters.add(APP_LEGAL_RESIDENCE);
		if (res == null || res.length() == 0)
			wrongParameters.add(APP_RESIDENCE);
		if (phone == null || phone.length() == 0)
			wrongParameters.add(APP_PHONE);
		if (zip == null || zip.length() == 0)
			wrongParameters.add(APP_PO);
		if (accept == null)
			wrongParameters.add("acceptor");

		return wrongParameters;
	}

	public List checkCampusInfo(IWContext iwc) {
		Vector wrongParameters = new Vector();
		String studybegin = iwc.getParameter("studyBegin");
		String studyend = iwc.getParameter("studyEnd");
		String school = iwc.getParameter(PARAM_SCHOOL);
		String studytrack = iwc.getParameter("studyTrack");
		String wantingfrom = iwc.getParameter("wantHousingFrom");
		String email = iwc.getParameter("email");

		if (studybegin == null || studybegin.length() == 0)
			wrongParameters.add("studyBegin");
		if (studyend == null || studyend.length() == 0)
			wrongParameters.add("studyEnd");
		if (school == null || school.length() == 0)
			wrongParameters.add(PARAM_SCHOOL);
		if (studytrack == null || studytrack.length() == 0)
			wrongParameters.add("studyTrack");
		if (wantingfrom == null || wantingfrom.length() == 0)
			wrongParameters.add("wantHousingFrom");
		if (email != null && email.length() > 0 && email.indexOf("@") != -1) {
			try {
				new javax.mail.internet.InternetAddress(email);
			} catch (Exception ex) {
				wrongParameters.add("email");
			}
		} else {
			wrongParameters.add("email");
		}

		return wrongParameters;
	}
}