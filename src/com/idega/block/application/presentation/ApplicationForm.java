/*
 * $Id: ApplicationForm.java,v 1.25.2.3 2009/02/06 15:43:54 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.application.presentation;
import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.application.business.ApplicationFormHelper;
import com.idega.block.application.data.ApplicationSubject;
import com.idega.block.application.data.ApplicationSubjectHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ApplicationForm extends PresentationObjectContainer {
	public static final String APP_GENINFO = "app_generalInfo";
	public static final String APP_FIRST_NAME = "app_firstName";
	public static final String APP_MIDDLE_NAME = "app_middleName";
	public static final String APP_LAST_NAME = "app_lastName";
	public static final String APP_SSN = "app_ssn";
	public static final String APP_LEGAL_RESIDENCE = "app_legalResidence";
	public static final String APP_LEGAL_RESIDENCE_PO = "app_legalResidencePO";
	public static final String APP_RESIDENCE = "app_residence";
	public static final String APP_PHONE = "app_residencePhone";
	public static final String APP_MOBILE = "app_mobilePhone";
	public static final String APP_PO = "app_po";
	public static final String APP_STATUS = "app_status";
	public static final String APP_MUST_FILL_OUT = "app_mustFillOut";
	
	protected static final int STATUS_ENTERING_PAGE = 0;
	protected static final int STATUS_SUBJECT = 1;
	protected static final int STATUS_GENERAL_INFO = 2;
	protected static final Text REQUIRED = new Text(" * ");
	
	private static final String IW_RESOURCE_BUNDLE = "com.idega.block.application";

	protected IWResourceBundle iwrb = null;
	protected Text info = null;
	
	public ApplicationForm() {
	}

	protected void control(IWContext iwc) {
		debugParameters(iwc);
		String statusString = iwc.getParameter(APP_STATUS);
		int status = 0;
		if (statusString == null) {
			status = this.STATUS_ENTERING_PAGE;
		}
		else {
			try {
				status = Integer.parseInt(statusString);
			}
			catch (NumberFormatException e) {
				status = this.STATUS_ENTERING_PAGE;
			}
		}
		if (status == this.STATUS_ENTERING_PAGE) {
			doSelectSubject();
		}
		else if (status == this.STATUS_SUBJECT) {
			try {
				ApplicationFormHelper.saveSubject(iwc);
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (CreateException e) {
				e.printStackTrace();
			}
			doGeneralInformation();
		}
		else if (status == this.STATUS_GENERAL_INFO) {
			try {
				ApplicationFormHelper.saveApplicantInformation(iwc);
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (CreateException e) {
				e.printStackTrace();
			}
			if (ApplicationFormHelper.saveDataToDB(iwc) != null) {
				doDone();
			}
			else {
				doError();
			}
		}
	}
	public String getBundleIdentifier() {
		return (IW_RESOURCE_BUNDLE);
	}
	protected void doSelectSubject() {
		Collection subjects = null;
		try {
			ApplicationSubjectHome aHome = (ApplicationSubjectHome) IDOLookup.getHome(ApplicationSubject.class);
			subjects = aHome.findNonExpired();
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		Text textTemplate = new Text();
		Form form = new Form();
		Table t = new Table(2, 3);
		t.setWidth(1, "50%");
		t.setWidth(2, "50%");
		Text heading = (Text) textTemplate.clone();
		heading.setStyle("headlinetext");
		heading.setText(this.iwrb.getLocalizedString("applicationSubjectTitle", "Veldu tegund ums?knar"));
		Text text1 = (Text) textTemplate.clone();
		text1.setStyle("bodytext");
		text1.setText(this.iwrb.getLocalizedString("applicationSubject", "Ums?kn um"));
		text1.setBold();
		DropdownMenu subject = new DropdownMenu(subjects, "subject");
		subject.setStyleClass("formstyle");
		BackButton back = new BackButton(this.iwrb.getImage("back.gif"));
		SubmitButton ok = new SubmitButton(this.iwrb.getImage("next.gif", this.iwrb.getLocalizedString("ok", "?fram")));
		form.add(heading);
		form.add(Text.getBreak());
		form.add(Text.getBreak());
		form.add(t);
		t.add(text1, 1, 1);
		t.add(this.REQUIRED, 1, 1);
		t.add(subject, 2, 1);
		t.add(back, 2, 3);
		t.add(ok, 2, 3);
		form.add(Text.getBreak());
		form.add(Text.getBreak());
		form.add(Text.getBreak());
		form.add(this.info);
		form.add(new HiddenInput(APP_STATUS, Integer.toString(this.STATUS_SUBJECT)));
		add(form);
	}
	protected void doGeneralInformation() {
		Text textTemplate = new Text();
		TextInput textInputTemplate = new TextInput();
		Form form = new Form();
		DataTable t = new DataTable();
		SubmitButton ok = new SubmitButton(this.iwrb.getImage("next.gif", this.iwrb.getLocalizedString("ok", "?fram")));
		Text heading = (Text) textTemplate.clone();
		heading.setStyle("headlinetext");
		heading.setText(this.iwrb.getLocalizedString("generalInfo", "Almennar uppl?singar um ums?kjanda"));
		Text text1 = (Text) textTemplate.clone();
		text1.setStyle("bodytext");
		text1.setText(this.iwrb.getLocalizedString("firstName", "Fornafn"));
		text1.setBold();
		Text text2 = (Text) textTemplate.clone();
		text2.setStyle("bodytext");
		text2.setText(this.iwrb.getLocalizedString("middleName", "Millinafn"));
		Text text3 = (Text) textTemplate.clone();
		text3.setStyle("bodytext");
		text3.setText(this.iwrb.getLocalizedString("lastName", "Eftirnafn"));
		text3.setBold();
		Text text4 = (Text) textTemplate.clone();
		text4.setStyle("bodytext");
		text4.setText(this.iwrb.getLocalizedString("ssn", "Kennitala"));
		text4.setBold();
		Text text5 = (Text) textTemplate.clone();
		text5.setStyle("bodytext");
		text5.setText(this.iwrb.getLocalizedString("legalResidence", "L?gheimili"));
		text5.setBold();
		Text text6 = (Text) textTemplate.clone();
		text6.setStyle("bodytext");
		text6.setText(this.iwrb.getLocalizedString("residence", "Dvalarsta?ur"));
		text6.setBold();
		Text text7 = (Text) textTemplate.clone();
		text7.setStyle("bodytext");
		text7.setText(this.iwrb.getLocalizedString("residencePhone", "S?man?mer ? dvalarsta?"));
		text7.setBold();
		Text text8 = (Text) textTemplate.clone();
		text8.setStyle("bodytext");
		text8.setText(this.iwrb.getLocalizedString("po", "P?stn?mer"));
		text8.setBold();
		TextInput input1 = (TextInput) textInputTemplate.clone();
		input1.setName("firstName");
		input1.setStyleClass("formstyle");
		input1.setLength(40);
		TextInput input2 = (TextInput) textInputTemplate.clone();
		input2.setName("middleName");
		input2.setStyleClass("formstyle");
		input2.setLength(40);
		TextInput input3 = (TextInput) textInputTemplate.clone();
		input3.setName("lastName");
		input3.setStyleClass("formstyle");
		input3.setLength(40);
		TextInput input4 = (TextInput) textInputTemplate.clone();
		input4.setName("ssn");
		input4.setLength(11);
		input4.setStyleClass("formstyle");
		TextInput input5 = (TextInput) textInputTemplate.clone();
		input5.setName("legalResidence");
		input5.setStyleClass("formstyle");
		input5.setLength(40);
		TextInput input6 = (TextInput) textInputTemplate.clone();
		input6.setName("residence");
		input6.setStyleClass("formstyle");
		input6.setLength(40);
		TextInput input7 = (TextInput) textInputTemplate.clone();
		input7.setName("residencePhone");
		input7.setLength(8);
		input7.setStyleClass("formstyle");
		TextInput input8 = (TextInput) textInputTemplate.clone();
		input8.setName("po");
		input8.setLength(3);
		input8.setStyleClass("formstyle");
		t.addTitle(heading);
		t.add(text1, 1, 1);
		t.add(this.REQUIRED, 1, 1);
		t.add(input1, 2, 1);
		t.add(text2, 1, 2);
		t.add(input2, 2, 2);
		t.add(text3, 1, 3);
		t.add(this.REQUIRED, 1, 3);
		t.add(input3, 2, 3);
		t.add(text4, 1, 4);
		t.add(this.REQUIRED, 1, 4);
		t.add(input4, 2, 4);
		t.add(text5, 1, 5);
		t.add(this.REQUIRED, 1, 5);
		t.add(input5, 2, 5);
		t.add(text6, 1, 6);
		t.add(this.REQUIRED, 1, 6);
		t.add(input6, 2, 6);
		t.add(text7, 1, 7);
		t.add(this.REQUIRED, 1, 7);
		t.add(input7, 2, 7);
		t.add(text8, 1, 8);
		t.add(this.REQUIRED, 1, 8);
		t.add(input8, 2, 8);
		t.addButton(ok);
		form.add(t);
		form.add(Text.getBreak());
		form.add(Text.getBreak());
		form.add(Text.getBreak());
		form.add(this.info);
		form.add(new HiddenInput(APP_STATUS, Integer.toString(this.STATUS_GENERAL_INFO)));
		add(form);
	}
	protected void doDone() {
		add(this.iwrb.getLocalizedString("applicationRegistered", "Ums?kn skr??"));
	}
	protected void doDone(String cypher) {
		Text cypherText = new Text(cypher);
		cypherText.setBold();
		add(
			this.iwrb.getLocalizedString("applicationRegistered", "Ums?kn skr??")
				+ ". "
				+ this.iwrb.getLocalizedString("applicationReferenceNumber", "Tilv?sunarn?mer ?itt er")
				+ ": ");
		add(cypherText);
		add(Text.getBreak());
		add(Text.getBreak());
		add(
			this.iwrb.getLocalizedString(
				"applicationFollowUp",
				"?? getur nota? tilv?sunarn?meri? til a? fylgjast me? st??u ums?knar ?innar")
				+ ".");
	}
	protected void doError() {
		add(this.iwrb.getLocalizedString("applicationDBError", "Gagnagrunnsvilla vi? skr?ningu ums?knar"));
	}
	public void main(IWContext iwc) {
		this.iwrb = getResourceBundle(iwc);
		this.info = new Text(this.iwrb.getLocalizedString(APP_MUST_FILL_OUT, "* Stj?rnumerkt sv??i ver?ur a? fylla ?t"));
		control(iwc);
	}
	protected void doSubjectError() {
		add(this.iwrb.getLocalizedString("applicationSubjectError", "Nothing to apply for"));
	}
}
