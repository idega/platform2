/*
 * $Id: CampusApplicationForm.java,v 1.30.4.10 2009/02/06 15:43:53 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.application.presentation;

import is.idega.idegaweb.campus.block.application.business.ApplicationService;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationFormHelper;
import is.idega.idegaweb.campus.block.application.data.ApartmentCategoryCombination;
import is.idega.idegaweb.campus.block.application.data.CurrentResidency;
import is.idega.idegaweb.campus.block.application.data.SpouseOccupation;
import is.idega.idegaweb.campus.business.CampusService;
import is.idega.idegaweb.campus.presentation.Edit;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.ApplicationBMPBean;
import com.idega.block.application.data.ApplicationSubject;
import com.idega.block.application.presentation.ApplicationForm;
import com.idega.block.building.data.ApartmentCategory;
import com.idega.block.building.data.ApartmentSubcategory;
import com.idega.block.building.data.ApartmentSubcategoryHome;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWBundle;
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
public class CampusApplicationForm extends ApplicationForm {
	private static final String PARAM_EXTRA_INFO = "extra_info";

	private static final String PARAM_WANT_HOUSING_FROM = "wantHousingFrom";

	private static final String PARAM_CHILDBIRTH = "childbirth";

	private static final String PARAM_CHILDNAME = "childname";

	private static final String PARAM_EMAIL = "email";

	private static final String PARAM_CONTACT = "contact";

	private static final String PARAM_SPOUSE_STUDY_TRACK = "spouseStudyTrack";

	private static final String PARAM_SPOUSE_SCHOOL = "spouseSchool";

	private static final String PARAM_SPOUSE_SSN = "spouseSSN";

	private static final String PARAM_SPOUSE_NAME = "spouseName";

	private static final String PARAM_RES_INFO = "resInfo";

	private static final String PARAM_STUDY_TRACK = "studyTrack";

	private static final String PARAM_FACULTY = "faculty";

	private static final String PARAM_SPOUSE_STUDY_END = "spouseStudyEnd";

	private static final String PARAM_SPOUSE_STUDY_BEGIN = "spouseStudyBegin";

	private static final String PARAM_STUDY_END = "studyEnd";

	private static final String PARAM_STUDY_BEGIN = "studyBegin";

	private static final String PARAM_SPOUSE_OCCUPATION = "spouseOccupation";

	private static final String PARAM_CURRENT_RESIDENCE = "currentResidence";

	private static final String APPLICATION_PREFIX = "campus_application.";

	private static final String APPLICATION_FOR_PREFIX = APPLICATION_PREFIX
			+ "application_for.";

	private static final String APARTMENT_TYPE_PREFIX = APPLICATION_PREFIX
			+ "application_type.";

	private static final String CURRENT_RESIDENCE_PREFIX = APPLICATION_PREFIX
			+ "current_residence.";

	private static final String SPOUSE_IS_PREFIX = APPLICATION_PREFIX
			+ "spouse_is.";

	private static final String SUBCATEGORY_PREFIX = APPLICATION_PREFIX
			+ "subcategory.";

	public static final String PARAM_SUBCATEGORY1 = "subcat1";

	public static final String PARAM_SUBCATEGORY2 = "subcat2";

	public static final String PARAM_SUBCATEGORY3 = "subcat3";

	public static final String PARAM_CATEGORY = "aprtCat";

	protected final static int STATUS_ENTERING_PAGE = 0;

	protected final static int STATUS_SUBJECT = 1;

	protected final static int STATUS_GENERAL_INFO = 2;

	protected final static int STATUS_CAMPUS_INFO = 3;

	protected final static int STATUS_APPLIED_FOR = 4;

	protected final static int STATUS_SELECTING_SUBCATEGORY = 99;

	protected final static int NUMBER_OF_STAGES = 3;

	protected int apartment1 = -1;

	protected int apartment2 = -1;

	protected int apartment3 = -1;

	protected IWBundle iwb;

	protected static final String IW_RESOURCE_BUNDLE = "is.idega.idegaweb.campus";

	protected final static String REQUIRED = ("* ");

	protected Text info = null;

	/**
	 * 
	 */
	public CampusApplicationForm() {
	}

	/*
	 * 
	 */
	protected void control(IWContext iwc) {
		try {
			iwb = getBundle(iwc);
			List wrongParameters = new Vector();
			String statusString = iwc.getParameter(APP_STATUS);
			int status = 0;

			if (statusString == null) {
				status = STATUS_ENTERING_PAGE;
			} else {
				try {
					status = Integer.parseInt(statusString);
				} catch (NumberFormatException e) {
				}
			}

			if (status == STATUS_ENTERING_PAGE) {
				Collection subjects = null;
				try {
					subjects = getApplicationService(iwc).getSubjectHome()
							.findNonExpired();
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (FinderException e) {
					e.printStackTrace();
				}
				if (subjects == null) {
					doSubjectError();
					return;
				}

				addStage(1);
				doGeneralInformation(iwc, wrongParameters);
			} else if (status == STATUS_GENERAL_INFO) {
				wrongParameters = checkGeneral(iwc);
				if (wrongParameters.size() > 0) {
					addStage(1);
					doGeneralInformation(iwc, wrongParameters);
				} else {
					addStage(2);
					try {
						CampusApplicationFormHelper
								.saveApplicantInformation(iwc);
					} catch (RemoteException e) {
						e.printStackTrace();
					} catch (CreateException e) {
						e.printStackTrace();
					}

					doCampusInformation(iwc, wrongParameters);
				}
			} else if (status == STATUS_CAMPUS_INFO) {
				wrongParameters = checkCampusInfo(iwc);
				if (wrongParameters.size() > 0) {
					addStage(2);
					doCampusInformation(iwc, wrongParameters);
				} else {
					addStage(3);
					CampusApplicationFormHelper.saveSubject(iwc);
					CampusApplicationFormHelper.saveCampusInformation(iwc);
					doSelectAppliedFor(iwc, wrongParameters);
				}
			} else if (status == STATUS_SELECTING_SUBCATEGORY) {
				addStage(3);
				checkAparmentTypesSelected(iwc);
				doSelectAppliedFor(iwc, wrongParameters);
			} else if (status == STATUS_APPLIED_FOR) {
				wrongParameters = checkApplied(iwc);
				if (wrongParameters.size() > 0) {
					addStage(3);
					checkAparmentTypesSelected(iwc);
					doSelectAppliedFor(iwc, wrongParameters);
				} else {
					CampusApplicationFormHelper.saveAppliedFor(iwc);
					String cypher = CampusApplicationFormHelper
							.saveDataToDB(iwc);
					if (cypher != null)
						doDone(cypher);
					else
						doError();
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	protected void doSelectApplication(IWContext iwc) {

	}

	protected void doSelectAppliedFor(IWContext iwc, List wrongParameters)
			throws RemoteException {
		int size = 1;
		Integer categoryID[] = new Integer[size];
		String aprtCat = (String) iwc.getSessionAttribute(PARAM_CATEGORY);
		try {
			if (aprtCat.indexOf("&") > 0) {
				size = 2;
				int counter = 0;
				categoryID = new Integer[size];
				StringTokenizer tok = new StringTokenizer(aprtCat, "&");
				while (tok.hasMoreElements() && counter < size) {
					String tmp = (String) tok.nextElement();
					categoryID[counter] = Integer.valueOf(tmp);
					counter++;
				}
			} else {
				categoryID = new Integer[size];
				categoryID[0] = Integer.valueOf(aprtCat);
			}
		} catch (Exception e) {
			e.printStackTrace();
			categoryID = null;
		}

		try {
			Collection subcats = getCampusService(iwc).getBuildingService()
					.getApartmentSubcategoryHome().findByCategory(categoryID);
			DropdownMenu aprtType = new DropdownMenu(PARAM_SUBCATEGORY1);
			if (iwc.isParameterSet(PARAM_SUBCATEGORY1)) {
				aprtType.setSelectedElement(iwc
						.getParameter(PARAM_SUBCATEGORY1));
			}
			Edit.setStyle(aprtType);
			DropdownMenu aprtType2 = new DropdownMenu(PARAM_SUBCATEGORY2);
			if (iwc.isParameterSet(PARAM_SUBCATEGORY2)) {
				aprtType2.setSelectedElement(iwc
						.getParameter(PARAM_SUBCATEGORY2));
			}
			Edit.setStyle(aprtType2);
			DropdownMenu aprtType3 = new DropdownMenu(PARAM_SUBCATEGORY3);
			if (iwc.isParameterSet(PARAM_SUBCATEGORY3)) {
				aprtType3.setSelectedElement(iwc
						.getParameter(PARAM_SUBCATEGORY3));
			}
			Edit.setStyle(aprtType3);
			aprtType.addDisabledMenuElement("-1", "");
			aprtType2.addMenuElement("-1", "");
			aprtType3.addMenuElement("-1", "");

			for (Iterator iter = subcats.iterator(); iter.hasNext();) {
				ApartmentSubcategory eAprtSubcat = (ApartmentSubcategory) iter
						.next();
				boolean isLocked = false;

				if (!isLocked) {
					Integer id = (Integer) eAprtSubcat.getPrimaryKey();
					StringBuffer defaultString = new StringBuffer(eAprtSubcat
							.getName());
					defaultString.append(" (");
					defaultString.append(eAprtSubcat.getApartmentCategory().getName());
					defaultString.append(")");
					
					String locKey = SUBCATEGORY_PREFIX + id.toString();
					aprtType.addMenuElement(id.intValue(), iwrb.getLocalizedString(locKey, defaultString.toString()));
					aprtType2.addMenuElement(id.intValue(), iwrb.getLocalizedString(locKey, defaultString.toString()));
					aprtType3.addMenuElement(id.intValue(), iwrb.getLocalizedString(locKey, defaultString.toString()));
				}
			}

			Form form = new Form();
			DataTable t = new DataTable();
			Edit.setStyle(t);

			String text1 = iwrb.getLocalizedString("firstChoice",
					"First Choice");
			String text2 = iwrb.getLocalizedString("secondChoice",
					"Second Choice");
			String text3 = iwrb.getLocalizedString("thirdChoice",
					"Third Choice");

			Image back = iwrb.getImage("back.gif");
			back.setMarkupAttribute("onClick", "history.go(-1)");

			SubmitButton ok = new SubmitButton(iwrb.getImage("next.gif", iwrb
					.getLocalizedString("ok", "Next")));

			form.add(t);

			t.addTitle(iwrb.getLocalizedString("applied",
					"Apartment applied for"));
			Text label = Edit.formatText(text1);
			if (wrongParameters.contains(PARAM_SUBCATEGORY1))
				label.setFontColor("#ff0000");
			t.add(label, 1, 1);
			t.add(Edit.formatText(REQUIRED, true), 1, 1);
			t.add(aprtType, 2, 1);

			t.add(Edit.formatText(text2), 1, 2);
			t.add(aprtType2, 2, 2);

			t.add(Edit.formatText(text3), 1, 3);
			t.add(aprtType3, 2, 3);

			t.addButton(back);
			t.addButton(ok);

			HiddenInput hidden = new HiddenInput(APP_STATUS, Integer
					.toString(STATUS_SELECTING_SUBCATEGORY));

			ok
					.setValueOnClick(APP_STATUS, Integer
							.toString(STATUS_APPLIED_FOR));
			form.add(hidden);

			form.add(Text.getBreak());
			form.add(Text.getBreak());
			form.add(Text.getBreak());
			form.add(info);
			aprtType.setOnChange("this.form.app_status.value='"
					+ STATUS_SELECTING_SUBCATEGORY + "'");
			aprtType2.setOnChange("this.form.app_status.value='"
					+ STATUS_SELECTING_SUBCATEGORY + "'");
			aprtType3.setOnChange("this.form.app_status.value='"
					+ STATUS_SELECTING_SUBCATEGORY + "'");

			add(form);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void addStage(int stage) {
		Text stageText = new Text(iwrb.getLocalizedString("stage", "Stage")
				+ " " + Integer.toString(stage) + " "
				+ iwrb.getLocalizedString("of", "of") + " "
				+ Integer.toString(NUMBER_OF_STAGES));
		stageText
				.setFontStyle("font-family:Verdana, Helvetica, Arial, sans-serif; font-size: 14px; font-weight: bold; color: #932a2b;");

		add(stageText);
		add(Text.getBreak());
	}

	protected boolean checkForApplications(IWContext iwc, Applicant applicant) {
		if (applicant.getSSN().equals("9999999999")) {
			return false;
		}

		try {
			Collection applicants = CampusApplicationFormHelper
					.getApplicationService(iwc).getApplicantHome().findBySSN(
							applicant.getSSN());
			Iterator it = applicants.iterator();
			while (it.hasNext()) {
				Applicant a = (Applicant) it.next();
				try {
					Collection col = CampusApplicationFormHelper
							.getApplicationService(iwc).getApplicationHome()
							.findByApplicantAndStatus(
									(Integer) a.getPrimaryKey(),
									ApplicationBMPBean.STATUS_SUBMITTED);
					if (col != null && !col.isEmpty()) {
						return true;
					}
				} catch (FinderException e) {
				}

				try {
					Collection col = CampusApplicationFormHelper
							.getApplicationService(iwc).getApplicationHome()
							.findByApplicantAndStatus(
									(Integer) a.getPrimaryKey(),
									ApplicationBMPBean.STATUS_APPROVED);
					if (col != null && !col.isEmpty()) {
						return true;
					}
				} catch (FinderException e) {
				}

			}
			//			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
		}

		return false;
	}

	/*
	 * 
	 */
	protected void doCampusInformation(IWContext iwc, List wrongParameters) {
		Applicant applicant = (Applicant) iwc.getSessionAttribute("applicant");
		boolean hasManyApplications = checkForApplications(iwc, applicant);

		if (hasManyApplications) {
			add(new Text(
					iwrb
							.getLocalizedString(
									"have_many_application",
									"You have one or more applications in the system that have been submitted or approved. Please go in with your reference number and cancel them if you want to create a new application.")));
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

			DropdownMenu subject = new DropdownMenu("subject");
			if (subjects != null) {
				ApplicationSubject entity = null;
				Iterator iter = subjects.iterator();
				while (iter.hasNext()) {
					entity = (ApplicationSubject) iter.next();
					String id = entity.getPrimaryKey().toString();
					String locKey = APPLICATION_FOR_PREFIX + id;
					subject.addMenuElement(id, iwrb.getLocalizedString(locKey, entity.getName()));
				}
			}

			DropdownMenu aprtCat = new DropdownMenu(PARAM_CATEGORY);
			if (categories != null) {
				ApartmentCategory entity = null;
				Iterator iter = categories.iterator();
				while (iter.hasNext()) {
					entity = (ApartmentCategory) iter.next();
					String id = entity.getPrimaryKey().toString();
					String locKey = APARTMENT_TYPE_PREFIX + id;
					aprtCat.addMenuElement(id, iwrb.getLocalizedString(locKey, entity.getName()));
				}
			}

			Collection col = getCampusService(iwc)
					.getApartmentCategoryCombinationHome().findAll();
			if (col != null && !col.isEmpty()) {
				Iterator it = col.iterator();
				while (it.hasNext()) {
					ApartmentCategoryCombination comb = (ApartmentCategoryCombination) it
							.next();
					ApartmentCategory cat1 = comb.getCategory1();
					ApartmentCategory cat2 = comb.getCategory1();

					StringBuffer key = new StringBuffer(cat1.getPrimaryKey()
							.toString());
					key.append("&");
					key.append(cat2.getPrimaryKey().toString());

					StringBuffer name = new StringBuffer(cat1.getName());
					name.append(" & ");
					name.append(cat2.getName());

					String locKey = APARTMENT_TYPE_PREFIX + key.toString();
					aprtCat.addMenuElement(key.toString(), iwrb.getLocalizedString(locKey, name.toString()));
				}
			}

			Image back = iwrb.getImage("back.gif");
			back.setMarkupAttribute("onClick", "history.go(-1)");
			SubmitButton ok = new SubmitButton(iwrb.getImage("next.gif", iwrb
					.getLocalizedString("ok", "?fram")));

			form.add(t);

			t.addTitle(iwrb.getLocalizedString("applicationSubjectTitle",
					"Select application subject"));
			t.add(getHeader(text1), 1, 1);
			t.add(getHeader(REQUIRED), 1, 1);
			t.add(subject, 2, 1);
			t.add(getHeader(text2), 1, 2);
			t.add(getHeader(REQUIRED), 1, 2);
			t.add(aprtCat, 2, 2);

			Collection residences = null;
			Collection occupations = null;
			try {
				residences = getApplicationService(iwc).getResidencyHome()
						.findAll();
				occupations = getApplicationService(iwc)
						.getSpouseOccupationHome().findAll();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			} catch (FinderException e1) {
				e1.printStackTrace();
			}
			DropdownMenu resSelect = new DropdownMenu(PARAM_CURRENT_RESIDENCE);
			if (residences != null) {
				CurrentResidency entity = null;
				Iterator iter = residences.iterator();
				while (iter.hasNext()) {
					entity = (CurrentResidency) iter.next();
					String id = entity.getPrimaryKey().toString();
					String locKey = CURRENT_RESIDENCE_PREFIX + id;
					resSelect.addMenuElement(id, iwrb.getLocalizedString(locKey, entity.getName()));
				}
			}

			DropdownMenu occSelect = new DropdownMenu(PARAM_SPOUSE_OCCUPATION);
			if (occupations != null) {
				SpouseOccupation entity = null;
				Iterator iter = occupations.iterator();
				while (iter.hasNext()) {
					entity = (SpouseOccupation) iter.next();
					String id = entity.getPrimaryKey().toString();
					String locKey = SPOUSE_IS_PREFIX + id;
					occSelect.addMenuElement(id, iwrb.getLocalizedString(locKey, entity.getName()));
				}
			}

			DateInput studyBegin = new DateInput(PARAM_STUDY_BEGIN);

			studyBegin.setToShowDay(false);
			studyBegin.setYearRange(fromYear, toYear);
			DateInput studyEnd = new DateInput(PARAM_STUDY_END);

			studyEnd.setToShowDay(false);
			studyEnd.setYearRange(fromYear, toYear);
			DateInput spouseStudyBegin = new DateInput(PARAM_SPOUSE_STUDY_BEGIN);

			spouseStudyBegin.setToShowDay(false);
			spouseStudyBegin.setYearRange(fromYear, toYear);
			DateInput spouseStudyEnd = new DateInput(PARAM_SPOUSE_STUDY_END);

			spouseStudyEnd.setToShowDay(false);
			spouseStudyEnd.setYearRange(fromYear, toYear);

			String labelStudyBegin = iwrb.getLocalizedString(PARAM_STUDY_BEGIN,
					"Study begins");
			String labelStudyEnd = iwrb.getLocalizedString(PARAM_STUDY_END,
					"Estimated study end");
			String labelFaculty = iwrb.getLocalizedString(PARAM_FACULTY, "Faculty");
			String labelStudyTrack = iwrb.getLocalizedString(PARAM_STUDY_TRACK,
					"Study track");
			String labelCurrentRes = iwrb.getLocalizedString("currentRes",
					"Current residence");
			String labelSpouseName = iwrb.getLocalizedString(PARAM_SPOUSE_NAME,
					"Spouse name");
			String labelSpouseSSN = iwrb.getLocalizedString(PARAM_SPOUSE_SSN,
					"Spouse social security number");
			String labelSpouseSchool = iwrb.getLocalizedString(PARAM_SPOUSE_SCHOOL,
					"Spouse school");
			String labelSpouseTrack = iwrb.getLocalizedString(
					PARAM_SPOUSE_STUDY_TRACK, "Spouse study track");
			String labelSpouseBegin = iwrb.getLocalizedString(
					PARAM_SPOUSE_STUDY_BEGIN, "Spouse study begins");
			String labelSpouseEnd = iwrb.getLocalizedString(PARAM_SPOUSE_STUDY_END,
					"Spouse's estimated study end");
			String labelSpouseOcc = iwrb.getLocalizedString(
					PARAM_SPOUSE_OCCUPATION, "Spouse is");
			String labelChildren = iwrb.getLocalizedString("children",
					"Names and dates of birth of children living with applicant");
			String labelHousingFrom = iwrb.getLocalizedString(
					PARAM_WANT_HOUSING_FROM, "Want housing from");
			String labelContact = iwrb
					.getLocalizedString(
							PARAM_CONTACT,
							"Other phone number");
			String labelEmail = iwrb
					.getLocalizedString(PARAM_EMAIL, "Email");
			String labelInfo = iwrb.getLocalizedString("info",
					"Other information");

			TextInput textInputTemplate = new TextInput();

			TextInput inputFaculty = (TextInput) textInputTemplate.clone();
			inputFaculty.setName(PARAM_FACULTY);
			if (iwc.isParameterSet(PARAM_FACULTY))
				inputFaculty.setContent(iwc.getParameter(PARAM_FACULTY));

			TextInput inputTrack = (TextInput) textInputTemplate.clone();
			inputTrack.setName(PARAM_STUDY_TRACK);
			if (iwc.isParameterSet(PARAM_STUDY_TRACK))
				inputTrack.setContent(iwc.getParameter(PARAM_STUDY_TRACK));

			TextInput inputResInfo = (TextInput) textInputTemplate.clone();
			inputResInfo.setName(PARAM_RES_INFO);
			inputResInfo.setLength(10);
			if (iwc.isParameterSet(PARAM_RES_INFO))
				inputResInfo.setContent(iwc.getParameter(PARAM_RES_INFO));

			TextInput inputSpouseName = (TextInput) textInputTemplate.clone();
			inputSpouseName.setName(PARAM_SPOUSE_NAME);
			if (iwc.isParameterSet(PARAM_SPOUSE_NAME))
				inputSpouseName.setContent(iwc.getParameter(PARAM_SPOUSE_NAME));

			TextInput inputSpouseSSN = (TextInput) textInputTemplate.clone();
			inputSpouseSSN.setName(PARAM_SPOUSE_SSN);
			inputSpouseSSN.setMaxlength(10);
			inputSpouseSSN.setLength(12);
			if (iwc.isParameterSet(PARAM_SPOUSE_SSN))
				inputSpouseSSN.setContent(iwc.getParameter(PARAM_SPOUSE_SSN));

			TextInput inputSpouseSchool = (TextInput) textInputTemplate.clone();
			inputSpouseSchool.setName(PARAM_SPOUSE_SCHOOL);
			if (iwc.isParameterSet(PARAM_SPOUSE_SCHOOL))
				inputSpouseSchool.setContent(iwc.getParameter(PARAM_SPOUSE_SCHOOL));

			TextInput inputSpouseTrack = (TextInput) textInputTemplate.clone();
			inputSpouseTrack.setName(PARAM_SPOUSE_STUDY_TRACK);
			if (iwc.isParameterSet(PARAM_SPOUSE_STUDY_TRACK))
				inputSpouseTrack.setContent(iwc
						.getParameter(PARAM_SPOUSE_STUDY_TRACK));

			TextInput inputContact = (TextInput) textInputTemplate.clone();
			inputContact.setName(PARAM_CONTACT);
			inputContact.setLength(10);
			if (iwc.isParameterSet(PARAM_CONTACT))
				inputContact.setContent(iwc.getParameter(PARAM_CONTACT));

			TextInput inputEmail = (TextInput) textInputTemplate.clone();
			String needEmail = iwrb.getLocalizedString("enter_correct_email",
					"Please enter a valid email");
			inputEmail.setAsEmail(needEmail);
			inputEmail.setAsNotEmpty(needEmail);
			inputEmail.setName(PARAM_EMAIL);
			if (iwc.isParameterSet(PARAM_EMAIL))
				inputEmail.setContent(iwc.getParameter(PARAM_EMAIL));

			int children = 4;
			Table childrenTable = new Table(2, children);
			for (int i = 0; i < children; i++) {
				TextInput childName = new TextInput(PARAM_CHILDNAME + i);
				TextInput childBirth = new TextInput(PARAM_CHILDBIRTH + i);
				if (iwc.isParameterSet(PARAM_CHILDNAME + i))
					childName.setContent(iwc.getParameter(PARAM_CHILDNAME + i));
				if (iwc.isParameterSet(PARAM_CHILDBIRTH + i))
					childBirth.setContent(iwc.getParameter(PARAM_CHILDBIRTH + i));

				childName.setLength(40);
				childBirth.setLength(10);
				childBirth.setMaxlength(10);
				childrenTable.add(childName, 1, i + 1);
				childrenTable.add(childBirth, 2, i + 1);
			}
			childrenTable.add(new HiddenInput("children_count", String
					.valueOf(children)));

			TextArea inputExtraInfo = new TextArea(PARAM_EXTRA_INFO);

			Edit.setStyle(inputExtraInfo);
			inputExtraInfo.setRows(4);
			inputExtraInfo.setColumns(30);

			DateInput input16 = new DateInput(PARAM_WANT_HOUSING_FROM);
			if (iwc.isParameterSet(PARAM_WANT_HOUSING_FROM)) {
				String sdate = iwc.getParameter(PARAM_WANT_HOUSING_FROM);
				if (sdate != null && !"".equals(sdate))
					input16.setDate(new IWTimestamp(sdate).getDate());
			}
			input16.setToCurrentDate();

			DataTable t2 = new DataTable();

			form.add(t2);

			t2.addTitle(iwrb.getLocalizedString("otherInfo",
					"Other information"));
			int row = 1;
			Text label = getHeader(labelStudyBegin);
			if (wrongParameters.contains(PARAM_STUDY_BEGIN))
				label.setFontColor("#ff0000");
			t2.add(label, 1, row);
			t2.add(getHeader(REQUIRED), 1, row);
			t2.add(studyBegin, 2, row);
			row++;

			label = getHeader(labelStudyEnd);
			if (wrongParameters.contains(PARAM_STUDY_END))
				label.setFontColor("#ff0000");
			t2.add(label, 1, row);
			t2.add(getHeader(REQUIRED), 1, row);
			t2.add(studyEnd, 2, row);
			row++;

			label = getHeader(labelFaculty);
			if (wrongParameters.contains(PARAM_FACULTY))
				label.setFontColor("#ff0000");
			t2.add(label, 1, row);
			t2.add(getHeader(REQUIRED), 1, row);
			t2.add(inputFaculty, 2, row);
			row++;

			label = getHeader(labelStudyTrack);
			if (wrongParameters.contains(PARAM_STUDY_TRACK))
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
			if (wrongParameters.contains(PARAM_WANT_HOUSING_FROM))
				label.setFontColor("#ff0000");
			t2.add(label, 1, row);
			t2.add(getHeader(REQUIRED), 1, row);
			t2.add(input16, 2, row);
			row++;

			t2.add(getHeader(labelContact), 1, row);
			t2.add(inputContact, 2, row);
			row++;

			label = getHeader(labelEmail);
			if (wrongParameters.contains(PARAM_EMAIL))
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
	public String getBundleIdentifier() {
		return (IW_RESOURCE_BUNDLE);
	}

	/*
	 * 
	 */
	private void checkAparmentTypesSelected(IWContext iwc) {
		String key1 = (String) iwc.getParameter(PARAM_SUBCATEGORY1);
		String key2 = (String) iwc.getParameter(PARAM_SUBCATEGORY2);
		String key3 = (String) iwc.getParameter(PARAM_SUBCATEGORY3);

		try {
			ApartmentSubcategoryHome ath = getApplicationService(iwc)
					.getBuildingService().getApartmentSubcategoryHome();
			int type = Integer.parseInt(key1);

			ApartmentSubcategory room = ath.findByPrimaryKey(new Integer(type));
			apartment1 = ((Integer) room.getPrimaryKey()).intValue();

			if ((key2 != null) && (!key2.equalsIgnoreCase("-1"))) {
				type = Integer.parseInt(key2);

				room = ath.findByPrimaryKey(new Integer(type));
				apartment2 = ((Integer) room.getPrimaryKey()).intValue();

			}

			if ((key3 != null) && (!key3.equalsIgnoreCase("-1"))) {
				type = Integer.parseInt(key3);

				room = ath.findByPrimaryKey(new Integer(type));
				apartment3 = ((Integer) room.getPrimaryKey()).intValue();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void main(IWContext iwc) {
		iwrb = getResourceBundle(iwc);
		info = getHeader(iwrb.getLocalizedString("mustFillOut",
				"* Stj?rnumerkt sv??i ver?ur a? fylla ?t"));

		control(iwc);
	}

	/**
	 * 
	 */
	protected void doGeneralInformation(IWContext iwc, List wrongParameters) {
		TextInput textInputTemplate = new TextInput();
		Form form = new Form();
		DataTable t = new DataTable();
		// BackButton back = new BackButton(_iwrb.getImage("back.gif"));
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
		if (ssn == null
				|| !SocialSecurityNumber
						.isValidIcelandicSocialSecurityNumber(ssn)
				|| !this.isValidAge(ssn, iwc)) {
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

	/**
	 * If the Application property "campus_minimum_age" is set then it checks if
	 * the age of the applier is equal or more than that.
	 * 
	 * @param ssn
	 *            Icelandic social security number
	 * @return true if it doesn't need to check the age or returns the result of
	 *         (age>=minimumAge);
	 */
	protected boolean isValidAge(String ssn, IWContext iwc) {
		String mininumAgeString = iwc.getApplicationSettings().getProperty(
				"CAMPUS_MINIMUM_AGE");

		if (ssn == null || "".equals(ssn)) {
			return false;
		} else {
			int firstDigit = Integer.parseInt(ssn.substring(0, 2));
			if (firstDigit > 31) {
				return true;
			}
		}

		if (mininumAgeString != null && !"".equals(mininumAgeString)) {
			int minimumAge = Integer.parseInt(mininumAgeString);
			int age = SocialSecurityNumber.getAge(ssn);
			return (age >= minimumAge);
		} else
			return true;
	}

	public List checkCampusInfo(IWContext iwc) {
		Vector wrongParameters = new Vector();
		String studybegin = iwc.getParameter(PARAM_STUDY_BEGIN);
		String studyend = iwc.getParameter(PARAM_STUDY_END);
		String faculty = iwc.getParameter(PARAM_FACULTY);
		String studytrack = iwc.getParameter(PARAM_STUDY_TRACK);
		String wantingfrom = iwc.getParameter(PARAM_WANT_HOUSING_FROM);
		String email = iwc.getParameter(PARAM_EMAIL);

		if (studybegin == null || studybegin.length() == 0)
			wrongParameters.add(PARAM_STUDY_BEGIN);
		if (studyend == null || studyend.length() == 0)
			wrongParameters.add(PARAM_STUDY_END);
		if (faculty == null || faculty.length() == 0)
			wrongParameters.add(PARAM_FACULTY);
		if (studytrack == null || studytrack.length() == 0)
			wrongParameters.add(PARAM_STUDY_TRACK);
		if (wantingfrom == null || wantingfrom.length() == 0)
			wrongParameters.add(PARAM_WANT_HOUSING_FROM);
		if (email != null && email.length() > 0 && email.indexOf("@") != -1) {
			try {
				new javax.mail.internet.InternetAddress(email);
			} catch (Exception ex) {
				wrongParameters.add(PARAM_EMAIL);
			}
		} else {
			wrongParameters.add(PARAM_EMAIL);
		}

		return wrongParameters;
	}

	public List checkApplied(IWContext iwc) {

		Vector wrongParameters = new Vector();
		String aprt = iwc.getParameter(PARAM_SUBCATEGORY1);
		if (aprt == null || aprt.length() == 0 || aprt.equals("-1"))
			wrongParameters.add(PARAM_SUBCATEGORY1);
		return wrongParameters;
	}

	public ApplicationService getApplicationService(IWContext iwc)
			throws RemoteException {
		return (ApplicationService) IBOLookup.getServiceInstance(iwc
				.getApplicationContext(), ApplicationService.class);
	}

	public Text getHeader(String text) {
		Text t = new Text(text);
		t.setBold();
		return t;
	}

	public Text getText(String text) {
		return new Text(text);
	}

	public CampusService getCampusService(IWContext iwc) throws RemoteException {
		return (CampusService) IBOLookup.getServiceInstance(iwc,
				CampusService.class);
	}

}