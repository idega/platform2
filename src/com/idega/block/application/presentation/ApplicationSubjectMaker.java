package com.idega.block.application.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;
import javax.faces.component.UIComponent;

import com.idega.block.application.business.ApplicationService;
import com.idega.block.application.data.ApplicationSubject;
import com.idega.business.IBOLookup;
import com.idega.core.builder.presentation.ICPropertyHandler;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.util.Edit;
import com.idega.util.IWTimestamp;

/**
 * Title: Description: Copyright: Copyright (c) 2001 Company: idega.is
 * 
 * @author 2000 - idega team - <br>
 *         <a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class ApplicationSubjectMaker extends Block {
	private static final String PARAM_EXTRA = "app_subj_extra";
	private static final String PARAM_DESCRIPTION = "app_subj_desc";
	private static final String PARAM_EXPIRE_DATE = "app_subj_xdate";
	private static final String PARAM_STARTS_DATE = "app_subj_startdate";
	protected final int ACT1 = 1, ACT2 = 2, ACT3 = 3, ACT4 = 4, ACT5 = 5;
	private final String strAction = "appsmaction";
	protected boolean isAdmin = false;
	private final static String IW_BUNDLE_IDENTIFIER = "com.block.allocation";
	protected IWResourceBundle iwrb;
	protected IWBundle iwb, core;
	private Class attributeHandlerClass = null;

	public ApplicationSubjectMaker() {

	}

	public String getLocalizedNameKey() {
		return "subjects";
	}

	public String getLocalizedNameValue() {
		return "Subjects";
	}

	protected void control(IWContext iwc) {

		if (this.isAdmin) {
			ApplicationSubject subject = null;
			if (iwc.isParameterSet("app_subject_id")) {
				try {
					subject = getApplicationService(iwc).getSubjectHome()
							.findByPrimaryKey(
									new Integer(iwc
											.getParameter("app_subject_id")));
				} catch (Exception ex) {

				}

			}

			if (iwc.isParameterSet("save") || iwc.isParameterSet("save.x")) {
				doUpdate(iwc, subject);
			} else if (iwc.isParameterSet("delete")) {
				doDelete(iwc);
			}

			Table T = new Table();
			T.setVerticalAlignment(1, 1, "top");
			T.setVerticalAlignment(2, 1, "top");
			T.add(getSubjectFormTable(iwc, subject), 1, 1);
			T.add(getSubjectTable(iwc, subject), 2, 1);
			add(T);

		} else {
			this.add(new Text(this.iwrb.getLocalizedString("access_denied",
					"Access denied")));
		}

	}

	public PresentationObject makeLinkTable(int menuNr) {
		Table LinkTable = new Table(6, 1);

		return LinkTable;
	}

	private PresentationObject getSubjectTable(IWContext iwc,
			ApplicationSubject subject) {

		Collection L = null;
		try {
			L = getApplicationService(iwc).getSubjectHome().findAll();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		DataTable dTable = new DataTable();
		dTable.setTitlesHorizontal(true);
		dTable.addTitle(this.iwrb.getLocalizedString("subjects", "Subjects"));
		dTable.add(Edit.formatText(this.iwrb.getLocalizedString("description",
				"Description")), 1, 1);
		dTable.add(Edit.formatText(this.iwrb.getLocalizedString("startdate",
				"Startdate")), 2, 1);
		dTable.add(Edit.formatText(this.iwrb.getLocalizedString("expiredate",
				"Expiredate")), 3, 1);
		dTable.add(Edit.formatText(this.iwrb.getLocalizedString(
				"extra_attribute", "Extra attribute")), 4, 1);

		if (L != null) {
			int row = 2;
			for (Iterator iter = L.iterator(); iter.hasNext();) {
				ApplicationSubject AS = (ApplicationSubject) iter.next();

				dTable.add(getSubjectLink(AS), 1, row);
				if (AS.getStarts() != null) {
					dTable.add(Edit.formatText(new IWTimestamp(AS.getStarts())
							.getDateString("dd.MM.yyyy")), 2, row);

				} else {
					dTable.add(Edit.formatText(""), 2, row);
				}
				dTable.add(Edit.formatText(new IWTimestamp(AS.getExpires())
						.getDateString("dd.MM.yyyy")), 3, row);
				if (AS.getExtraAttribute() != null) {
					dTable.add(Edit.formatText(AS.getExtraAttribute()), 4, row);
				}
				dTable.add((getDeleteLink(AS)), 5, row);
				row++;
			}
		}
		return dTable;
	}

	private UIComponent getSubjectFormTable(IWContext iwc,
			ApplicationSubject subject) {
		DataTable dTable = new DataTable();
		dTable.setTitlesHorizontal(true);
		dTable.addTitle(this.iwrb.getLocalizedString("new_subject",
				"New subject"));

		TextInput description = new TextInput(PARAM_DESCRIPTION);
		Edit.setStyle(description);
		DateInput expireDate = new DateInput(PARAM_EXPIRE_DATE, true);
		expireDate.setStyleAttribute("style", Edit.styleAttribute);
		expireDate.setDate(IWTimestamp.RightNow().getDate());
		DateInput startDate = new DateInput(PARAM_STARTS_DATE, true);
		startDate.setStyleAttribute("style", Edit.styleAttribute);
		startDate.setDate(IWTimestamp.RightNow().getDate());
		TextInput extra = new TextInput(PARAM_EXTRA);

		if (subject != null) {
			description.setContent(subject.getDescription());
			expireDate.setDate(subject.getExpires());
			if (subject.getStarts() != null) {
				startDate.setDate(subject.getStarts());
			}
			if (subject.getExtraAttribute() != null) {
				extra.setContent(subject.getExtraAttribute());
			}
			dTable.add(new HiddenInput("app_subject_id", subject
					.getPrimaryKey().toString()));
		}
		dTable.add(Edit.formatText(this.iwrb.getLocalizedString("description",
				"Description")), 1, 1);
		dTable.add(Edit.formatText(this.iwrb.getLocalizedString("startdate",
				"Startdate")), 2, 1);
		dTable.add(Edit.formatText(this.iwrb.getLocalizedString("expiredate",
				"Expiredate")), 3, 1);
		dTable.add(Edit.formatText(this.iwrb.getLocalizedString(
				"extra_attribute", "Extra attribute")), 4, 1);
		dTable.add(description, 1, 2);
		dTable.add(startDate, 2, 2);
		dTable.add(expireDate, 3, 2);

		if (this.attributeHandlerClass != null) {
			try {
				ICPropertyHandler handler = (ICPropertyHandler) this.attributeHandlerClass
						.newInstance();
				String attribute = "";
				if (subject != null) {
					attribute = subject.getExtraAttribute() != null ? subject
							.getExtraAttribute() : "";
				}
				PresentationObject handlerObject = handler.getHandlerObject(
						PARAM_EXTRA, attribute, iwc);
				dTable.add(handlerObject, 4, 2);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} else {
			dTable.add(extra, 4, 2);
		}

		dTable.addButton(new SubmitButton(this.iwrb.getLocalizedImageButton(
				"save", "Save"), "save"));

		Form f = new Form();
		f.add(dTable);
		return f;
	}

	public Link getDeleteLink(ApplicationSubject AS) {
		Link L = new Link("X");
		L.addParameter("delete", AS.getPrimaryKey().toString());
		return L;
	}

	public Link getSubjectLink(ApplicationSubject AS) {
		Link L = new Link(AS.getDescription());
		L.addParameter("app_subject_id", AS.getPrimaryKey().toString());
		return L;
	}

	public void doDelete(IWContext iwc) {
		Integer id = new Integer(iwc.getParameter("delete"));
		try {
			getApplicationService(iwc).removeApplicationSubject(id);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void doUpdate(IWContext iwc, ApplicationSubject subject) {
		String description = iwc.getParameter(PARAM_DESCRIPTION).trim();
		String expires = iwc.getParameter(PARAM_EXPIRE_DATE);
		String extra = iwc.getParameter(PARAM_EXTRA);
		String starts = iwc.getParameter(PARAM_STARTS_DATE);
		Integer id = subject != null ? (Integer) subject.getPrimaryKey()
				: new Integer(-1);
		if (description.length() > 0) {
			try {
				getApplicationService(iwc).storeApplicationSubject(id, description,
						new IWTimestamp(starts).getDate(), new IWTimestamp(expires).getDate(), extra);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	public void main(IWContext iwc) {
		this.isAdmin = iwc.hasEditPermission(this);
		this.iwrb = getResourceBundle(iwc);
		this.iwb = getBundle(iwc);
		this.core = iwc.getIWMainApplication().getBundle(
				IWMainApplication.CORE_BUNDLE_IDENTIFIER);
		control(iwc);
	}

	public ApplicationService getApplicationService(IWApplicationContext iwac)
			throws RemoteException {
		return (ApplicationService) IBOLookup.getServiceInstance(iwac,
				ApplicationService.class);
	}

	/**
	 * @param attributeHandlerClass
	 *            The attributeHandlerClass to set.
	 */
	public void setAttributeHandlerClass(Class attributeHandlerClass) {
		this.attributeHandlerClass = attributeHandlerClass;
	}

	/**
	 * @param attributeHandlerClass
	 *            The attributeHandlerClass to set.
	 */
	public void setAttributeHandlerClassName(String attributeHandlerClassName) {
		try {
			this.attributeHandlerClass = Class
					.forName(attributeHandlerClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}