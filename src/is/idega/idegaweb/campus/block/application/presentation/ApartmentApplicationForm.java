package is.idega.idegaweb.campus.block.application.presentation;

import is.idega.idegaweb.campus.block.application.business.ApplicationService;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

public class ApartmentApplicationForm extends Block {
	private static final String LABEL_PROVIDE_ICELANDIC_SS = "provide_icelandic_ss";

	private static final String LABEL_APPLICATION_SUBJECT_TITLE = "applicationSubjectTitle";

	private static final String LABEL_OF = "of";

	private static final String LABEL_STAGE = "stage";

	private static final String LABEL_NEXT = "next";

	private static final String LABEL_BACK = "back";

	private static final String PARAM_APARTMENT_CATEGORY = "aprtCat";

	private static final String PARAM_SUBJECT = "subject";

	protected static final String LABEL_NO_SSN = "no_ssn";

	protected static final String IW_RESOURCE_BUNDLE = "is.idega.idegaweb.campus";

	protected static final int STATUS_TYPE = 1;

	protected static final int STATUS_PERSONAL_INFO = 3;

	protected static final int STATUS_FAMILY_INFO = 4;

	protected static final int STATUS_CAMPUS_INFO = 5;

	protected static final int STATUS_APPLIED_FOR = 6;

	protected static final int STATUS_SELECT_APPLICATION = 2;

	protected static final int STATUS_SAVE = 7;

	protected static final int NUMBER_OF_STAGES = 7;

	protected static final String STEP = "step";

	protected IWResourceBundle iwrb = null;

	protected static final String REQUIRED = ("* ");

	protected static final String LABEL_SSN = "ssn";

	private static final String LABEL_APPLICATION_SELECTION_TITLE = "select_between_applications";

	private static final String LABEL_PERSONAL_INFO_TITLE = "personal_info";

	private static final String LABEL_FAMILY_INFO_TITLE = "family_info";

	private static final String LABEL_CAMPUS_INFO_TITLE = "other_info";

	private static final String LABEL_APARTMENT_TYPE_SELECTION_TITLE = "aprtTypeSelection";

	private Text info = null;

	public void main(IWContext iwc) {
		iwrb = getResourceBundle(iwc);
		info = getHeader(iwrb.getLocalizedString("mustFillOut",
				"* Have to fill out fields marked with a star"));

		int action = parse(iwc);

		addStage(action);
		try {
			switch (action) {
			case STATUS_TYPE:
				addTypeSelection(iwc);
				break;
			case STATUS_SELECT_APPLICATION:
				addApplicationSelection(iwc);
				break;
			case STATUS_PERSONAL_INFO:
				addPersonalInfo(iwc);
				break;
			case STATUS_FAMILY_INFO:
				addFamilyInfo(iwc);
				break;
			case STATUS_CAMPUS_INFO:
				addCampusInfo(iwc);
				break;
			case STATUS_APPLIED_FOR:
				addApartmentTypeSelection(iwc);
				break;
			case STATUS_SAVE:
				add("Saved");
				break;
			}
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public String getBundleIdentifier() {
		return IW_RESOURCE_BUNDLE;
	}

	private int parse(IWContext iwc) {
		String selectedStep = iwc.getParameter(STEP);
		boolean check = true;
		if (selectedStep != null) {
			switch (Integer.parseInt(selectedStep)) {
			case STATUS_TYPE:
				break;
			case STATUS_SELECT_APPLICATION:
				check = checkAndSaveType(iwc);
				if (check) {
					return STATUS_PERSONAL_INFO;
					// return STATUS_SELECT_APPLICATION;
				} else {
					return STATUS_TYPE;
				}
			case STATUS_PERSONAL_INFO:
				check = checkAndSaveType(iwc);
				if (check) {
					return STATUS_PERSONAL_INFO;
				} else {
					return STATUS_SELECT_APPLICATION;
				}
			case STATUS_FAMILY_INFO:
				check = checkAndSaveType(iwc);
				if (check) {
					return STATUS_FAMILY_INFO;
				} else {
					return STATUS_PERSONAL_INFO;
				}
			case STATUS_CAMPUS_INFO:
				check = checkAndSaveType(iwc);
				if (check) {
					return STATUS_CAMPUS_INFO;
				} else {
					return STATUS_FAMILY_INFO;
				}
			case STATUS_APPLIED_FOR:
				check = checkAndSaveType(iwc);
				if (check) {
					return STATUS_APPLIED_FOR;
				} else {
					return STATUS_CAMPUS_INFO;
				}
			case STATUS_SAVE:
				check = checkAndSaveType(iwc);
				if (check) {
					return STATUS_SAVE;
				} else {
					return STATUS_APPLIED_FOR;
				}
			}
		}

		return STATUS_TYPE;
	}

	private boolean checkAndSaveType(IWContext iwc) {
		return true;
	}

	protected void addStage(int stage) {
		StringBuffer stageBuffer = new StringBuffer(iwrb.getLocalizedString(
				LABEL_STAGE, "Stage"));
		stageBuffer.append(" ");
		stageBuffer.append(Integer.toString(stage));
		stageBuffer.append(" ");
		stageBuffer.append(iwrb.getLocalizedString(LABEL_OF, LABEL_OF));
		stageBuffer.append(" ");
		stageBuffer.append(Integer.toString(NUMBER_OF_STAGES));
		Text stageText = new Text(stageBuffer.toString());
		stageText
				.setFontStyle("font-family:Verdana, Helvetica, Arial, sans-serif; font-size: 14px; font-weight: bold; color: #932a2b;");

		add(stageText);
		add(Text.getBreak());
	}

	protected void addTypeSelection(IWContext iwc) throws RemoteException,
			FinderException {
		Collection subjects = getApplicationService(iwc).getSubjectHome()
				.findNonExpired();

		Collection categories = getApplicationService(iwc).getBuildingService()
				.getApartmentCategoryHome().findAll();

		Form form = new Form();
		DataTable t = new DataTable();
		t.setUseBottom(false);

		String subjectLabel = iwrb.getLocalizedString(PARAM_SUBJECT,
				"Application subject");
		String aprtTypeLabel = iwrb.getLocalizedString(
				PARAM_APARTMENT_CATEGORY, "Apartment type");

		DropdownMenu subject = new DropdownMenu(subjects, PARAM_SUBJECT);

		DropdownMenu aprtCat = new DropdownMenu(categories,
				PARAM_APARTMENT_CATEGORY);

		SubmitButton ok = new SubmitButton(iwrb.getImage("next.gif", iwrb
				.getLocalizedString(LABEL_NEXT, "Continue")), STEP, Integer
				.toString(STATUS_SELECT_APPLICATION));

		form.add(t);

		t.addTitle(iwrb.getLocalizedString(LABEL_APPLICATION_SUBJECT_TITLE,
				"Select application type"));
		t.add(getHeader(subjectLabel), 1, 1);
		t.add(getHeader(REQUIRED), 1, 1);
		t.add(subject, 2, 1);

		t.add(getHeader(aprtTypeLabel), 1, 2);
		t.add(getHeader(REQUIRED), 1, 2);
		t.add(aprtCat, 2, 2);
		String ssnLabel = iwrb.getLocalizedString(LABEL_SSN,
				"Social security number");

		Table t2 = new Table(1, 2);

		TextInput ssn = new TextInput();
		ssn.setAsIcelandicSSNumber(iwrb.getLocalizedString(
				LABEL_PROVIDE_ICELANDIC_SS,
				"Please provide a valid Icelandic personal ID"));
		ssn.setName(LABEL_SSN);
		if (iwc.isParameterSet(LABEL_SSN)) {
			ssn.setContent(iwc.getParameter(LABEL_SSN));
		}
		ssn.setLength(10);
		ssn.setMaxlength(10);
		t.add(getHeader(ssnLabel), 1, 3);
		t.add(getHeader(REQUIRED), 1, 3);
		t.add(t2, 2, 3);

		t2.add(ssn, 1, 1);

		CheckBox noSSN = new CheckBox(LABEL_NO_SSN);
		String labelNoSSN = iwrb.getLocalizedString(LABEL_NO_SSN,
				"I do not have a valid Icelandic personal ID");

		noSSN.setToDisableWhenChecked(ssn);
		noSSN.setToEnableWhenUnchecked(ssn);

		t2.add(noSSN, 1, 2);
		t2.add(labelNoSSN, 1, 2);

		t.addButton(ok);

		add(form);
	}

	private void addApplicationSelection(IWContext iwc) {
		Form form = new Form();
		DataTable t = new DataTable();
		t.setUseBottom(false);

		SubmitButton back = getBackButton(STATUS_TYPE);
		SubmitButton ok = getOKButton(STATUS_PERSONAL_INFO);

		form.add(t);
		t.addTitle(iwrb.getLocalizedString(LABEL_APPLICATION_SELECTION_TITLE,
				"Select application"));
		t.addButton(back);
		t.addButton(ok);

		add(form);
	}

	private void addPersonalInfo(IWContext iwc) {
		Form form = new Form();
		DataTable t = new DataTable();
		t.setUseBottom(false);

		SubmitButton back = getBackButton(STATUS_SELECT_APPLICATION);
		SubmitButton ok = getOKButton(STATUS_FAMILY_INFO);

		form.add(t);
		t.addTitle(iwrb.getLocalizedString(LABEL_PERSONAL_INFO_TITLE,
				"Personal information"));
		t.addButton(back);
		t.addButton(ok);

/*		String firstNameLabel = iwrb.getLocalizedString(APP_FIRST_NAME,
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
		ssn.setAsIcelandicSSNumber(_iwrb.getLocalizedString(
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

		Text disclaimer = getHeader(_iwrb
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

		add(form);*/
	}

	private void addFamilyInfo(IWContext iwc) {
		Form form = new Form();
		DataTable t = new DataTable();
		t.setUseBottom(false);

		SubmitButton back = getBackButton(STATUS_PERSONAL_INFO);
		SubmitButton ok = getOKButton(STATUS_CAMPUS_INFO);

		form.add(t);
		t.addTitle(iwrb.getLocalizedString(LABEL_FAMILY_INFO_TITLE,
				"Family information"));
		t.addButton(back);
		t.addButton(ok);

		add(form);
	}

	private void addCampusInfo(IWContext iwc) {
		Form form = new Form();
		DataTable t = new DataTable();
		t.setUseBottom(false);

		SubmitButton back = getBackButton(STATUS_FAMILY_INFO);
		SubmitButton ok = getOKButton(STATUS_APPLIED_FOR);

		form.add(t);
		t.addTitle(iwrb.getLocalizedString(LABEL_CAMPUS_INFO_TITLE,
				"Other information"));
		t.addButton(back);
		t.addButton(ok);

		add(form);

	}

	private void addApartmentTypeSelection(IWContext iwc) {
		Form form = new Form();
		DataTable t = new DataTable();
		t.setUseBottom(false);

		SubmitButton back = getBackButton(STATUS_CAMPUS_INFO);
		SubmitButton ok = getOKButton(STATUS_SAVE);

		form.add(t);
		t.addTitle(iwrb.getLocalizedString(
				LABEL_APARTMENT_TYPE_SELECTION_TITLE, "Select apartment type"));
		t.addButton(back);
		t.addButton(ok);

		add(form);
	}

	private SubmitButton getBackButton(int step) {
		return new SubmitButton(iwrb.getImage("back.gif", iwrb
				.getLocalizedString(LABEL_BACK, "Back")), STEP, Integer
				.toString(step));
	}

	private SubmitButton getOKButton(int step) {
		return new SubmitButton(iwrb.getImage("next.gif", iwrb
				.getLocalizedString(LABEL_NEXT, "Next")), STEP, Integer
				.toString(step));
	}

	protected Text getHeader(String text) {
		Text t = new Text(text);
		t.setBold();
		return t;
	}

	protected Text getText(String text) {
		return new Text(text);
	}

	protected ApplicationService getApplicationService(IWContext iwc)
			throws RemoteException {
		return (ApplicationService) IBOLookup.getServiceInstance(iwc
				.getApplicationContext(), ApplicationService.class);
	}
}