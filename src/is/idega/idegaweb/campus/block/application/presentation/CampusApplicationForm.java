/*
 * $Id: CampusApplicationForm.java,v 1.27 2004/06/09 17:07:36 aron Exp $
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
import is.idega.idegaweb.campus.presentation.CampusTypeWindow;
import is.idega.idegaweb.campus.presentation.Edit;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.application.presentation.ApplicationForm;
import com.idega.block.building.business.ApartmentTypeComplexHelper;
import com.idega.block.building.data.ApartmentType;
import com.idega.block.building.data.ApartmentTypeHome;
import com.idega.block.building.presentation.ApartmentTypeViewer;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
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

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * modified by <a href="mailto:aron@idega.is">
 * @version 1.0
 */
public class CampusApplicationForm extends ApplicationForm {
	protected final int _statusEnteringPage = 0;
	protected final int _statusSubject = 1;
	protected final int _statusGeneralInfo = 2;
	protected final int _statusCampusInfo = 3;
	protected final int _statusAppliedFor = 4;
	protected final int _statusSelectingApartmentTypes = 99;
	protected final int _numberOfStages = 3;

	private int _apartment1 = -1;
	private int _apartment2 = -1;
	private int _apartment3 = -1;

	protected IWBundle _iwb;

	private static final String IW_RESOURCE_BUNDLE = "is.idega.idegaweb.campus";

	protected String _required = ("* ");
	protected Text _info = null;

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
			//debugParameters(iwc);
			_iwb = getBundle(iwc);
			List wrongParameters = new Vector();
			String statusString = iwc.getParameter(APP_STATUS);
			int status = 0;

			if (statusString == null) {
				status = _statusEnteringPage;
			}
			else {
				try {
					status = Integer.parseInt(statusString);
				}
				catch (NumberFormatException e) {
				}
			}

			if (status == _statusEnteringPage) {
				Collection subjects = null;
				try {
					subjects = getApplicationService(iwc).getSubjectHome().findNonExpired();
				}
				catch (RemoteException e) {
					e.printStackTrace();
				}
				catch (FinderException e) {
					e.printStackTrace();
				}
				if (subjects == null) {
					doSubjectError();
					return;
				}

				addStage(1);
				doGeneralInformation(iwc, wrongParameters);
			}
			else if (status == _statusGeneralInfo) {
				wrongParameters = checkGeneral(iwc);
				if (wrongParameters.size() > 0) {
					addStage(1);
					doGeneralInformation(iwc, wrongParameters);
				}
				else {
					addStage(2);
					try {
						CampusApplicationFormHelper.saveApplicantInformation(iwc);
					}
					catch (RemoteException e) {
						e.printStackTrace();
					}
					catch (CreateException e) {
						e.printStackTrace();
					}
					doCampusInformation(iwc, wrongParameters);
				}
			}
			else if (status == _statusCampusInfo) {
				wrongParameters = checkCampusInfo(iwc);
				if (wrongParameters.size() > 0) {
					addStage(2);
					doCampusInformation(iwc, wrongParameters);
				}
				else {
					addStage(3);
					CampusApplicationFormHelper.saveSubject(iwc);
					CampusApplicationFormHelper.saveCampusInformation(iwc);
					doSelectAppliedFor(iwc, wrongParameters);
				}
			}
			else if (status == _statusSelectingApartmentTypes) {
				addStage(3);
				checkAparmentTypesSelected(iwc);
				doSelectAppliedFor(iwc, wrongParameters);
			}
			else if (status == _statusAppliedFor) {
				wrongParameters = checkApplied(iwc);
				if (wrongParameters.size() > 0) {
					addStage(3);
					checkAparmentTypesSelected(iwc);
					doSelectAppliedFor(iwc, wrongParameters);
				}
				else {
					CampusApplicationFormHelper.saveAppliedFor(iwc);
					String cypher = CampusApplicationFormHelper.saveDataToDB(iwc);
					if (cypher != null)
						doDone(cypher);
					else
						doError();
				}
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	/*
	/*
	 *
	 */
	protected void doSelectAppliedFor(IWContext iwc, List wrongParameters) throws RemoteException {
		Integer categoryID;
		String aprtCat = (String) iwc.getSessionAttribute("aprtCat");
		try {
			categoryID = Integer.valueOf(aprtCat);
		}
		catch (Exception e) {
			categoryID = null;
		}
		
		try {
			Collection typeHelpers = getApplicationService(iwc).getComplexTypeHelpersByCategory(categoryID);
			//Collection vAprtType = ((BuildingService)IDOLookup.getHome(ApartmentType.class)).findByCategory(categoryID);
			//java.util.Vector vAprtType = BuildingFinder.getApartmentTypesComplexForCategory(id);
			DropdownMenu aprtType = new DropdownMenu("aprtType");
			if (iwc.isParameterSet("aprtType"))
				aprtType.setSelectedElement(iwc.getParameter("aprtType"));
			Edit.setStyle(aprtType);
			DropdownMenu aprtType2 = new DropdownMenu("aprtType2");
			if (iwc.isParameterSet("aprtType2"))
				aprtType2.setSelectedElement(iwc.getParameter("aprtType2"));
			Edit.setStyle(aprtType2);
			DropdownMenu aprtType3 = new DropdownMenu("aprtType3");
			if (iwc.isParameterSet("aprtType3"))
				aprtType3.setSelectedElement(iwc.getParameter("aprtType3"));
			Edit.setStyle(aprtType3);
			aprtType.addDisabledMenuElement("-1", "");
			aprtType2.addMenuElement("-1", "");
			aprtType3.addMenuElement("-1", "");

			for (Iterator iter = typeHelpers.iterator(); iter.hasNext();) {
				ApartmentTypeComplexHelper eAprtType = (ApartmentTypeComplexHelper) iter.next();
				aprtType.addMenuElement(eAprtType.getKey(), eAprtType.getName());
				aprtType2.addMenuElement(eAprtType.getKey(), eAprtType.getName());
				aprtType3.addMenuElement(eAprtType.getKey(), eAprtType.getName());
			}

			Form form = new Form();
			DataTable t = new DataTable();
			Edit.setStyle(t);

			String text1 = _iwrb.getLocalizedString("firstChoice", "First Choice");
			String text2 = _iwrb.getLocalizedString("secondChoice", "Second Choice");
			String text3 = _iwrb.getLocalizedString("thirdChoice", "Third Choice");

			Image back = _iwrb.getImage("back.gif");
			back.setMarkupAttribute("onClick", "history.go(-1)");

		
		

//		SubmitButton ok = new SubmitButton(_iwrb.getImage("next.gif", _iwrb.getLocalizedString("ok", "?fram")), APP_STATUS, Integer.toString(_statusAppliedFor));
			SubmitButton ok = new SubmitButton(_iwrb.getImage("next.gif", _iwrb.getLocalizedString("ok", "Next")));//, APP_STATUS, Integer.toString(_statusAppliedFor));

			form.add(t);


			t.addTitle(_iwrb.getLocalizedString("applied", "Apartment applied for"));
			Text label = Edit.formatText(text1);
			if (wrongParameters.contains("aprtType"))
				label.setFontColor("#ff0000");
			t.add(label, 1, 1);
			t.add(Edit.formatText(_required, true), 1, 1);
			t.add(aprtType, 2, 1);

			Image apartmentImage = _iwb.getImage("list.gif", _iwrb.getLocalizedString("get_apartment", "Click for information about apartment"));
			apartmentImage.setAlignment("absmiddle");
			apartmentImage.setHorizontalSpacing(4);
			Link apartmentLink = new Link(apartmentImage);
			apartmentLink.setWindowToOpen(CampusTypeWindow.class);
			Text apartmentText = new Text(_iwrb.getLocalizedString("see_apartment", "view"));
			apartmentText.setFontStyle("font-family:arial; font-size:9px; color:#000000");
			Link apartmentLink2 = new Link(apartmentText);
			apartmentLink2.setWindowToOpen(CampusTypeWindow.class);

			if (_apartment1 > -1) {
				try {
					Link link1 = (Link) apartmentLink.clone();
					link1.addParameter(ApartmentTypeViewer.PARAMETER_STRING, _apartment1);
					Link link12 = (Link) apartmentLink2.clone();
					link12.addParameter(ApartmentTypeViewer.PARAMETER_STRING, _apartment1);
					t.add(link1, 3, 1);
					t.add(link12, 3, 1);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}

			t.add(Edit.formatText(text2), 1, 2);
			t.add(aprtType2, 2, 2);
			if (_apartment2 > -1) {
				try {
					Link link2 = (Link) apartmentLink.clone();
					link2.addParameter(ApartmentTypeViewer.PARAMETER_STRING, _apartment2);
					Link link22 = (Link) apartmentLink2.clone();
					link22.addParameter(ApartmentTypeViewer.PARAMETER_STRING, _apartment2);
					t.add(link2, 3, 2);
					t.add(link22, 3, 2);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}

			t.add(Edit.formatText(text3), 1, 3);
			t.add(aprtType3, 2, 3);
			if (_apartment3 > -1) {
				try {
					Link link3 = (Link) apartmentLink.clone();
					link3.addParameter(ApartmentTypeViewer.PARAMETER_STRING, _apartment3);
					Link link32 = (Link) apartmentLink2.clone();
					link32.addParameter(ApartmentTypeViewer.PARAMETER_STRING, _apartment3);
					t.add(link3, 3, 3);
					t.add(link32, 3, 3);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}

			t.addButton(back);
			t.addButton(ok);
			
			HiddenInput hidden = new HiddenInput(APP_STATUS,Integer.toString(_statusSelectingApartmentTypes));
			
			ok.setValueOnClick(APP_STATUS,Integer.toString(_statusAppliedFor));
			form.add(hidden);
			
			form.add(Text.getBreak());
			form.add(Text.getBreak());
			form.add(Text.getBreak());
			form.add(_info);
			aprtType.setOnChange("this.form.app_status.value='" + _statusSelectingApartmentTypes + "'");
			aprtType2.setOnChange("this.form.app_status.value='" + _statusSelectingApartmentTypes + "'");
			aprtType3.setOnChange("this.form.app_status.value='" + _statusSelectingApartmentTypes + "'");
			aprtType.setToSubmit();
			aprtType2.setToSubmit();
			aprtType3.setToSubmit();
			aprtType.keepStatusOnAction();
			aprtType2.keepStatusOnAction();
			aprtType3.keepStatusOnAction();
//		form.maintainParameter(APP_STATUS);
			add(form);

		
		}catch(Exception e){}
		
	}

	protected void addStage(int stage) {
		Text stageText = new Text(_iwrb.getLocalizedString("stage", "Stage") + " " + Integer.toString(stage) + " " + _iwrb.getLocalizedString("of", "of") + " " + Integer.toString(_numberOfStages));
		stageText.setFontStyle("font-family:Verdana, Helvetica, Arial, sans-serif; font-size: 14px; font-weight: bold; color: #932a2b;");

		add(stageText);
		add(Text.getBreak());
	}

	/*
	 *
	 */
	protected void doCampusInformation(IWContext iwc, List wrongParameters) {
		Collection subjects = null;
		try {
			subjects = getApplicationService(iwc).getSubjectHome().findNonExpired();
		
		Collection categories = getApplicationService(iwc).getBuildingService().getApartmentCategoryHome().findAll();
		Text textTemplate = new Text();
		IWTimestamp today = IWTimestamp.RightNow();
		int fromYear = today.getYear() - 7;
		int toYear = today.getYear() + 7;
		

		Form form = new Form();
		DataTable t = new DataTable();
		t.setUseBottom(false);

		String text1 = _iwrb.getLocalizedString("applicationSubject", "Application subject");
		String text2 = _iwrb.getLocalizedString("apartmentType", "Apartment type");

		DropdownMenu subject = new DropdownMenu(subjects, "subject");
		
		DropdownMenu aprtCat = new DropdownMenu(categories, "aprtCat");
	
		Image back = _iwrb.getImage("back.gif");
		back.setMarkupAttribute("onClick", "history.go(-1)");
		SubmitButton ok = new SubmitButton(_iwrb.getImage("next.gif", _iwrb.getLocalizedString("ok", "?fram")));

		form.add(t);

		t.addTitle(_iwrb.getLocalizedString("applicationSubjectTitle", "Veldu tegund ums?knar"));
		t.add(getHeader(text1), 1, 1);
		t.add(getHeader(_required), 1, 1);
		t.add(subject, 2, 1);
		t.add(getHeader(text2), 1, 2);
		t.add(getHeader(_required), 1, 2);
		t.add(aprtCat, 2, 2);

		Collection residences = null;
		Collection occupations = null;
		try {
			residences = getApplicationService(iwc).getResidencyHome().findAll();
			occupations = getApplicationService(iwc).getSpouseOccupationHome().findAll();
		}
		catch (RemoteException e1) {
			e1.printStackTrace();
		}
		catch (FinderException e1) {
			e1.printStackTrace();
		}
		DropdownMenu resSelect = new DropdownMenu(residences, "currentResidence");
		
		DropdownMenu occSelect = new DropdownMenu(occupations, "spouseOccupation");
		
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

		String labelStudyBegin = _iwrb.getLocalizedString("studyBegin", "N?m hafi? vi? H? (m?n./?r)");
		String labelStudyEnd = _iwrb.getLocalizedString("studyEnd", "??tlu? n?mslok (m?n./?r)");
		String labelFaculty = _iwrb.getLocalizedString("faculty", "Deild");
		String labelStudyTrack = _iwrb.getLocalizedString("studyTrack", "N?msbraut");
		String labelCurrentRes = _iwrb.getLocalizedString("currentRes", "N?verandi h?sn??i");
		String labelSpouseName = _iwrb.getLocalizedString("spouseName", "Nafn ums?kjanda/maka");
		String labelSpouseSSN = _iwrb.getLocalizedString("spouseSSN", "Kennitala");
		String labelSpouseSchool = _iwrb.getLocalizedString("spouseSchool", "Sk?li");
		String labelSpouseTrack = _iwrb.getLocalizedString("spouseStudyTrack", "N?msbraut");
		String labelSpouseBegin = _iwrb.getLocalizedString("spouseStudyBegin", "N?m hafi? (m?n./?r)");
		String labelSpouseEnd = _iwrb.getLocalizedString("spouseStudyEnd", "??tlu? n?mslok (m?n./?r)");
		String labelSpouseOcc = _iwrb.getLocalizedString("spouseOccupation", "Maki er");
		String labelChildren = _iwrb.getLocalizedString("children", "N?fn og f??ingardagur barna sem b?a hj? ums?kjanda");
		String labelHousingFrom = _iwrb.getLocalizedString("wantHousingFrom", "H?sn??i ?skast fr? og me?");
		String labelContact = _iwrb.getLocalizedString("contact", "Ef ekki n?st ? mig ? s?ma ? dvalarsta? m? n? ? mig e?a skilja eftir skilabo? ? sima");
		String labelEmail = _iwrb.getLocalizedString("email", "T?lvup?stur");
		String labelInfo = _iwrb.getLocalizedString("info", "A?rar uppl?singar");

		TextInput textInputTemplate = new TextInput();
		

		TextInput inputFaculty = (TextInput) textInputTemplate.clone();
		inputFaculty.setName("faculty");
		if (iwc.isParameterSet("faculty"))
			inputFaculty.setContent(iwc.getParameter("faculty"));

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
			inputSpouseTrack.setContent(iwc.getParameter("spouseStudyTrack"));

		TextInput inputContact = (TextInput) textInputTemplate.clone();
		inputContact.setName("contact");
		inputContact.setLength(10);
		if (iwc.isParameterSet("contact"))
			inputContact.setContent(iwc.getParameter("contact"));

		TextInput inputEmail = (TextInput) textInputTemplate.clone();
		String needEmail = _iwrb.getLocalizedString("enter_correct_email","Please enter a valid email");
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
		childrenTable.add(new HiddenInput("children_count", String.valueOf(children)));

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

		t2.addTitle(_iwrb.getLocalizedString("otherInfo", "A?rar uppl?singar um ums?kjanda"));
		int row = 1;
		Text label = getHeader(labelStudyBegin);
		if (wrongParameters.contains("studyBegin"))
			label.setFontColor("#ff0000");
		t2.add(label, 1, row);
		t2.add(getHeader(_required), 1, row);
		t2.add(studyBegin, 2, row);
		row++;

		label = getHeader(labelStudyEnd);
		if (wrongParameters.contains("studyEnd"))
			label.setFontColor("#ff0000");
		t2.add(label, 1, row);
		t2.add(getHeader(_required), 1, row);
		t2.add(studyEnd, 2, row);
		row++;

		label = getHeader(labelFaculty);
		if (wrongParameters.contains("faculty"))
			label.setFontColor("#ff0000");
		t2.add(label, 1, row);
		t2.add(getHeader(_required), 1, row);
		t2.add(inputFaculty, 2, row);
		row++;

		label = getHeader(labelStudyTrack);
		if (wrongParameters.contains("studyTrack"))
			label.setFontColor("#ff0000");
		t2.add(label, 1, row);
		t2.add(getHeader(_required), 1, row);
		t2.add(inputTrack, 2, row);
		row++;

		t2.add(getHeader(labelCurrentRes), 1, row);
		t2.add(getHeader(_required), 1, row);
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
		t2.add(getHeader(_required), 1, row);
		t2.add(input16, 2, row);
		row++;

		t2.add(getHeader(labelContact), 1, row);
		t2.add(inputContact, 2, row);
		row++;

		label = getHeader(labelEmail);
		if (wrongParameters.contains("email"))
			label.setFontColor("#ff0000");
		t2.add(label, 1, row);
		t2.add(getHeader(_required), 1, row);
		t2.add(inputEmail, 2, row);
		row++;

		t2.add(getHeader(labelInfo), 1, row);
		t2.add(inputExtraInfo, 2, row);
		row++;

		t2.addButton(back);
		t2.addButton(ok);

		form.add(Text.getBreak());
		form.add(Text.getBreak());
		form.add(_info);
		form.add(new HiddenInput(APP_STATUS, Integer.toString(_statusCampusInfo)));
		add(form);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
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
		String key1 = (String) iwc.getParameter("aprtType");
		String key2 = (String) iwc.getParameter("aprtType2");
		String key3 = (String) iwc.getParameter("aprtType3");

		try {
			ApartmentTypeHome ath =getApplicationService(iwc).getBuildingService().getApartmentTypeHome();
			int type = ApartmentTypeComplexHelper.getPartKey(key1, 1);

			ApartmentType room = ath.findByPrimaryKey(new Integer(type));
			_apartment1 = ((Integer) room.getPrimaryKey()).intValue();


			if ((key2 != null) && (!key2.equalsIgnoreCase("-1"))) {
				type = ApartmentTypeComplexHelper.getPartKey(key2, 1);

				room =  ath.findByPrimaryKey(new Integer(type));
				_apartment2 = ((Integer) room.getPrimaryKey()).intValue();

			}

			if ((key3 != null) && (!key3.equalsIgnoreCase("-1"))) {
				type = ApartmentTypeComplexHelper.getPartKey(key3, 1);

				room =  ath.findByPrimaryKey(new Integer(type));
				_apartment3 = ((Integer) room.getPrimaryKey()).intValue();

			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void main(IWContext iwc) {
		_iwrb = getResourceBundle(iwc);
		_info = getHeader(_iwrb.getLocalizedString("mustFillOut", "* Stj?rnumerkt sv??i ver?ur a? fylla ?t"));

		control(iwc);
	}

	/**
	 *
	 */
	protected void doGeneralInformation(IWContext iwc, List wrongParameters) {
		TextInput textInputTemplate = new TextInput();
		Form form = new Form();
		DataTable t = new DataTable();
		//BackButton back = new BackButton(_iwrb.getImage("back.gif"));
		SubmitButton ok = new SubmitButton(_iwrb.getImage("next.gif", _iwrb.getLocalizedString("okk", "?fram")));
		ok.setName("ok");

		String heading = _iwrb.getLocalizedString(APP_GENINFO, "General information about applicant");
		String firstNameLabel = _iwrb.getLocalizedString(APP_FIRST_NAME, "First name");
		String middleNameLabel = _iwrb.getLocalizedString(APP_MIDDLE_NAME, "Middle name");
		String lastNameLabel = _iwrb.getLocalizedString(APP_LAST_NAME, "Last name");
		String ssnLabel = _iwrb.getLocalizedString(APP_SSN, "Social security number");
		String legalResidenceLabel = _iwrb.getLocalizedString(APP_LEGAL_RESIDENCE, "Legal residence");
		String residenceLabel = _iwrb.getLocalizedString(APP_RESIDENCE, "Residence");
		String phoneLabel = _iwrb.getLocalizedString(APP_PHONE, "Residence phone");
		String mobileLabel = _iwrb.getLocalizedString(APP_MOBILE, "Mobile phone");
		String poLabel = _iwrb.getLocalizedString(APP_PO, "PO");

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
		ssn.setAsIcelandicSSNumber(_iwrb.getLocalizedString("provide_icelandic_ss","Please provide a valid Icelandic personal ID"));
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
		mobile.setName(APP_PHONE);
		if (iwc.isParameterSet(APP_PHONE))
			mobile.setContent(iwc.getParameter(APP_PHONE));
		mobile.setLength(8);
		

		int row = 1;
		t.addTitle(heading);
		Text label = getHeader(firstNameLabel);
		if (wrongParameters.contains(APP_FIRST_NAME))
			label.setFontColor("#ff0000");
		t.add(label, 1, row);
		t.add(getHeader(_required), 1, row);
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
		t.add(getHeader(_required), 1, row);
		t.add(lastName, 2, row);
		row++;
		label = getHeader(ssnLabel);
		if (wrongParameters.contains(APP_SSN))
			label.setFontColor("#ff0000");
		t.add(label, 1, row);
		t.add(getHeader(_required), 1, row);
		t.add(ssn, 2, row);
		row++;
		label = getHeader(legalResidenceLabel);
		if (wrongParameters.contains(APP_LEGAL_RESIDENCE))
			label.setFontColor("#ff0000");
		t.add(label, 1, row);
		t.add(getHeader(_required), 1, row);
		t.add(legalResidence, 2, row);
		row++;
		label = getHeader(residenceLabel);
		if (wrongParameters.contains(APP_RESIDENCE))
			label.setFontColor("#ff0000");
		t.add(label, 1, row);
		t.add(getHeader(_required), 1, row);
		t.add(residence, 2, row);
		row++;
		label = getHeader(phoneLabel);
		if (wrongParameters.contains(APP_PHONE))
			label.setFontColor("#ff0000");
		t.add(label, 1, row);
		t.add(getHeader(_required), 1, row);
		t.add(phone, 2, row);
		row++;
		label = getHeader(poLabel);
		if (wrongParameters.contains(APP_PO))
			label.setFontColor("#ff0000");
		t.add(label, 1, row);
		t.add(getHeader(_required), 1, row);
		t.add(po, 2, row);
		row++;
		label = getHeader(mobileLabel);
		t.add(label, 1, row);
		//t.add(_required,1,row);
		t.add(mobile, 2, row);
		row++;
		CheckBox acceptance = new CheckBox("acceptor");

		Text disclaimer = getHeader(_iwrb.getLocalizedString("disclaimer", "Ums?kjandi heimilar St?dentag?r?um a? s?kja uppl?singar um skr?ningu e?a n?msframvindu til H?sk?la ?slands, eignarst??u fasteigna til Fasteignarmats r?kisins og fj?lskyldust?r? e?a barnafj?lda til Hagstofu ?slands."));
		t.add(acceptance, 1, row);
		Text accReq = getHeader(_required);
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
		frame.add(_info, 1, 4);
		form.add(frame);
		form.add(new HiddenInput(APP_STATUS, Integer.toString(_statusGeneralInfo)));
		add(form);
	}

	public List checkGeneral(IWContext iwc) {
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
		if (ssn == null || !com.idega.util.text.SocialSecurityNumber.isValidIcelandicSocialSecurityNumber(ssn))
			wrongParameters.add(APP_SSN);
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
		String faculty = iwc.getParameter("faculty");
		String studytrack = iwc.getParameter("studyTrack");
		String wantingfrom = iwc.getParameter("wantHousingFrom");
		String email = iwc.getParameter("email");

		if (studybegin == null || studybegin.length() == 0)
			wrongParameters.add("studyBegin");
		if (studyend == null || studyend.length() == 0)
			wrongParameters.add("studyEnd");
		if (faculty == null || faculty.length() == 0)
			wrongParameters.add("faculty");
		if (studytrack == null || studytrack.length() == 0)
			wrongParameters.add("studyTrack");
		if (wantingfrom == null || wantingfrom.length() == 0)
			wrongParameters.add("wantHousingFrom");
		if (email != null && email.length() > 0 && email.indexOf("@") != -1) {
			try {
				new javax.mail.internet.InternetAddress(email);
			}
			catch (Exception ex) {
				wrongParameters.add("email");
			}
		}
		else {
			wrongParameters.add("email");
		}

		return wrongParameters;
	}

	public List checkApplied(IWContext iwc) {

		Vector wrongParameters = new Vector();
		String aprt = iwc.getParameter("aprtType");
		if (aprt == null || aprt.length() == 0 || aprt.equals("-1"))
			wrongParameters.add("aprtType");
		return wrongParameters;
	}
	
	public ApplicationService getApplicationService(IWContext iwc) throws RemoteException{
		return (ApplicationService) IBOLookup.getServiceInstance(iwc.getApplicationContext(),ApplicationService.class);
	}

	
	public Text getHeader(String text){
		Text t = new Text(text);
		t.setBold();
		return t;
	}
	
	public Text getText(String text){
		return new Text(text);
	}

}