/*
 * $Id: CampusApprover.java,v 1.44 2003/04/07 11:20:46 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.application.presentation;

import is.idega.idegaweb.campus.block.application.business.CampusApplicationFinder;
import is.idega.idegaweb.campus.block.application.business.ReferenceNumberFinder;
import is.idega.idegaweb.campus.block.application.data.Applied;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import is.idega.idegaweb.campus.block.application.data.CampusApplicationHome;
import is.idega.idegaweb.campus.block.application.data.WaitingList;
import is.idega.idegaweb.campus.block.mailinglist.business.EntityHolder;
import is.idega.idegaweb.campus.block.mailinglist.business.LetterParser;
import is.idega.idegaweb.campus.block.mailinglist.business.MailingListBusiness;
import is.idega.idegaweb.campus.data.ApplicationSubjectInfo;
import is.idega.idegaweb.campus.presentation.Edit;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;
import java.util.Vector;

import com.idega.block.application.business.ApplicationFinder;
import com.idega.block.application.business.ApplicationHolder;
import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.Application;
import com.idega.block.application.data.ApplicationSubject;
import com.idega.block.building.business.ApartmentTypeComplexHelper;
import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.business.BuildingFinder;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
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
 * @author <a href="mailto:aron@idega.is">Aron</a>
 * @version 1.0
 */
public class CampusApprover extends Block {
	protected final int ACT1 = 1, ACT2 = 2, ACT3 = 3, ACT4 = 4, ACT5 = 5;
	private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.campus";
	protected IWResourceBundle iwrb;
	protected IWBundle iwb;
	private int iSubjectId = -1;
	private String sGlobalStatus = "S", sGlobalOrder = null;
	private ListIterator iterator = null;
	private LinkedList linkedlist = null;
	private final String sView = "app_view", sEdit = "app_edit";
	protected boolean isAdmin = false;
	private List listOfSubjects = null;

	/*
	Blár litur í topp # 27324B
	Hvítur litur fyrir neðan það # FFFFFF
	Ljósblár litur í töflu # ECEEF0
	Auka litur örlítið dekkri (í lagi að nota líka) # CBCFD3
	*/

	public String getLocalizedNameKey() {
		return "approver";
	}

	public String getLocalizedNameValue() {
		return "Approver";
	}

	protected void control(IWContext iwc) {
		//debugParameters(iwc);
		iwrb = getResourceBundle(iwc);
		iwb = getBundle(iwc);
		boolean infoCheck = true;
		listOfSubjects = ApplicationFinder.listOfSubject();
		if (iwc.getSessionAttribute("iterator") != null) {
			iterator = (ListIterator) iwc.getSessionAttribute("iterator");
		}
		if (iwc.getParameter("app_subject_id") != null) {
			infoCheck = false;
			this.iSubjectId = Integer.parseInt(iwc.getParameter("app_subject_id"));
			iwc.setSessionAttribute("subject_id", new Integer(iSubjectId));
		}
		else if (iwc.getSessionAttribute("subject_id") != null) {
			this.iSubjectId = ((Integer) iwc.getSessionAttribute("subject_id")).intValue();
		}
		if (iwc.getParameter("global_status") != null) {
			infoCheck = false;
			this.sGlobalStatus = (iwc.getParameter("global_status"));
			iwc.setSessionAttribute("gl_status", sGlobalStatus);
		}
		else if (iwc.getSessionAttribute("gl_status") != null) {
			this.sGlobalStatus = ((String) iwc.getSessionAttribute("gl_status"));
		}
		if (iwc.getParameter("global_order") != null) {
			infoCheck = false;
			this.sGlobalOrder = (iwc.getParameter("global_order"));
			if (sGlobalOrder != null)
				iwc.setSessionAttribute("gl_order", sGlobalOrder);
		}
		else if (iwc.getSessionAttribute("gl_order") != null) {
			this.sGlobalOrder = ((String) iwc.getSessionAttribute("gl_order"));
		}

		if (iwc.isParameterSet("subj_info") && iwc.getParameter("subj_info").equals("true"))
			infoCheck = true;

		if (isAdmin) {
			if (iwc.getParameter("cam_app_trash") != null) {
				int trashid = Integer.parseInt(iwc.getParameter("cam_app_trash"));
				trashApplication(trashid);
			}

			if (iwc.getParameter(sView) != null) {
				int id = Integer.parseInt(iwc.getParameter(sView));
				add(makeApplicationTable(id, false, iwc, iwrb));
			}
			else if (iwc.getParameter("application_id") != null) {
				int id = Integer.parseInt(iwc.getParameter("application_id"));
				boolean bEdit = false;
				if (iwc.getParameter("editor") != null) {
					bEdit = true;
				}
				else if (iwc.getParameter("viewer") != null) {
					bEdit = false;
				}

				if (iwc.getParameter("save") != null && !iwc.getParameter("save").equals("")) {
					bEdit = false;
					id = updateWholeApplication(iwc, id);
					if (iwc.isParameterSet("priority_drop")) {
						updatePriorityLevel(iwc, id);
					}
					if (iwc.isParameterSet("status_drop"))
						updateApplication(iwc, id);
				}
				/*
					else{
					  updateApplication(iwc,id);
					}
				*/
				if (bEdit) {
					add(makeApplicationForm(id, bEdit, iwc, iwrb));
				}
				else {
					add(makeApplicationTable(id, bEdit, iwc, iwrb));
				}
			}
			else if (iwc.getParameter("new_app") != null && iwc.getParameter("new_app").equals("true")) {
				add(makeApplicationForm(-1, true, iwc, iwrb));
			}
			else if (iwc.getParameter("new2") != null) {
				add(makeApplicationForm(-1, true, iwc, iwrb));
			}
			else if (infoCheck) {
				add(subjectForm());
				add(makeSubjectStatisticsTable());
			}
			else {
				add(subjectForm());
				add(makeApplicantTable(iwc, iwrb));
			}
		}

		else
			add(Edit.formatText(iwrb.getLocalizedString("access_denied", "Access denied")));
		//add(String.valueOf(iSubjectId));
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public PresentationObject makeLinkTable(int menuNr) {
		Table LinkTable = new Table(6, 1);

		return LinkTable;
	}

	private void updateApplication(IWContext iwc, int id) {
		String status = iwc.getParameter("status_drop");
		try {
			Application A = ((com.idega.block.application.data.ApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(Application.class)).findByPrimaryKeyLegacy(id);
			String oldStatus = A.getStatus();
			A.setStatus(status);
			A.update();
			Applicant Appli = ((com.idega.block.application.data.ApplicantHome) com.idega.data.IDOLookup.getHomeLegacy(Applicant.class)).findByPrimaryKeyLegacy(A.getApplicantId());

			if (((oldStatus == null) || (!oldStatus.equals("A"))) && status.equals("A")) {
				MailingListBusiness.processMailEvent(iwc, new EntityHolder(Appli), LetterParser.APPROVAL);

				CampusApplicationHome CAHome = null;
				CampusApplication CA = null;

				CAHome = (CampusApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(CampusApplication.class);
				java.util.Collection coll = CAHome.findAllByApplicationId(((Integer) A.getPrimaryKeyValue()).intValue());
				if (coll != null) {
					java.util.Iterator it = coll.iterator();
					if (it.hasNext())
						CA = (CampusApplication) it.next(); //CAHome.findByPrimaryKeyLegacy(((Integer)it.next()).intValue());
				}

				if (CA != null) {
					List L = CampusApplicationFinder.listOfAppliedInApplication(CA.getID());
					java.util.Iterator it = L.iterator();
					if (it != null) {
						while (it.hasNext()) {
							Applied applied = (Applied) it.next();

							WaitingList wl = ((is.idega.idegaweb.campus.block.application.data.WaitingListHome) com.idega.data.IDOLookup.getHomeLegacy(WaitingList.class)).createLegacy();
							wl.setApartmentTypeId(applied.getApartmentTypeId());
							wl.setComplexId(applied.getComplexId().intValue());
							wl.setTypeApplication();
							wl.setApplicantId(Appli.getID());
							wl.setOrder(0);
							wl.setChoiceNumber(applied.getOrder());
							wl.insert();
							wl.setOrder(wl.getID());
							String level = CA.getPriorityLevel();
							if (level.equals("A"))
								wl.setPriorityLevelA();
							else if (level.equals("B"))
								wl.setPriorityLevelB();
							else if (level.equals("C"))
								wl.setPriorityLevelC();
							else if (level.equals("D"))
								wl.setPriorityLevelD();
							else if (level.equals("E"))
								wl.setPriorityLevelE();
							else if (level.equals("T")) {
								wl.setPriorityLevelC();
								wl.setTypeTransfer();
								
								wl = CampusApplicationFinder.findRightPlaceForTransfer(wl);
							}
							wl.update();
						}
					}
				}
			}
			if (status.equals("R"))
				MailingListBusiness.processMailEvent(iwc, new EntityHolder(Appli), LetterParser.REJECTION);
		}
		catch (Exception e) {
			e.printStackTrace();

		}
	}

	private void updatePriorityLevel(IWContext iwc, int id) {
		//int id = Integer.parseInt(iwc.getParameter("application_id"));
		String level = iwc.getParameter("priority_drop");
		try {
			Application A = ((com.idega.block.application.data.ApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(Application.class)).findByPrimaryKeyLegacy(id);

			CampusApplicationHome CAHome = null;
			CampusApplication CA = null;

			CAHome = (CampusApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(CampusApplication.class);
			java.util.Collection coll = CAHome.findAllByApplicationId(((Integer) A.getPrimaryKeyValue()).intValue());
			if (coll != null) {
				java.util.Iterator it = coll.iterator();
				if (it.hasNext())
					CA = (CampusApplication) it.next(); //CAHome.findByPrimaryKeyLegacy(((Integer)it.next()).intValue());
			}

			if (CA != null) {
				CA.setPriorityLevel(level);
				CA.update();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void trashApplication(int id) {
		//int id = Integer.parseInt(iwc.getParameter("application_id"));

		try {
			Application A = ((com.idega.block.application.data.ApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(Application.class)).findByPrimaryKeyLegacy(id);
			A.setStatus(com.idega.block.application.data.ApplicationBMPBean.STATUS_GARBAGE);
			A.update();
		}
		catch (Exception e) {
			e.printStackTrace();

		}
	}

	private int updateWholeApplication(IWContext iwc, int id) {
		int returnid = id;
		javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();

		try {
			t.begin();

			Application eApplication = null;
			Applicant eApplicant = null;
			CampusApplication eCampusApplication = null;
			Applicant spouse = null;
			Vector children = null;
			if (id > 0) {
				eApplication = ((com.idega.block.application.data.ApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(Application.class)).findByPrimaryKeyLegacy(id);
				eApplicant = ((com.idega.block.application.data.ApplicantHome) com.idega.data.IDOLookup.getHomeLegacy(Applicant.class)).findByPrimaryKeyLegacy(eApplication.getApplicantId());
				java.util.Iterator iter = eApplicant.getChildren();
				if (iter != null) {
					Applicant a;
					while (iter.hasNext()) {
						a = (Applicant) iter.next();
						if (a.getStatus().equals("P")) {
							spouse = a;
						}
						else if (a.getStatus().equals("C")) {
							if (children == null)
								children = new Vector();
							children.add(a);
						}
					}
				}
				CampusApplication A = ((is.idega.idegaweb.campus.block.application.data.CampusApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(CampusApplication.class)).createLegacy();
				eCampusApplication = ((CampusApplication[]) (A.findAllByColumn(A.getApplicationIdColumnName(), id)))[0];
			}
			else {
				eApplicant = ((com.idega.block.application.data.ApplicantHome) com.idega.data.IDOLookup.getHomeLegacy(Applicant.class)).createLegacy();
				eApplicant.insert();
				eApplicant.addChild(eApplicant);
				eApplication = ((com.idega.block.application.data.ApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(Application.class)).createLegacy();
				eApplication.setApplicantId(eApplicant.getID());
				eApplication.setSubmitted(IWTimestamp.getTimestampRightNow());
				eApplication.setStatusSubmitted();
				eApplication.setSubjectId(iSubjectId);
				eApplication.insert();
				eCampusApplication = ((is.idega.idegaweb.campus.block.application.data.CampusApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(CampusApplication.class)).createLegacy();
				eCampusApplication.setAppApplicationId(new Integer(eApplication.getID()));
				eCampusApplication.setPriorityLevel("A");
				eCampusApplication.insert();
				returnid = eApplication.getID();
			}

			if (eApplication != null && eApplicant != null) {
				List L = CampusApplicationFinder.listOfAppliedInApplication(eCampusApplication.getID());
				updateApplicant(iwc, eApplicant, eCampusApplication);
				L = updateApartment(iwc, eCampusApplication, L);
				updateSpouse(iwc, eCampusApplication, eApplicant, spouse);
				updateChildren(iwc, eCampusApplication, eApplicant, children);

				eApplicant.update();
				eCampusApplication.update();
				for (int i = 0; i < L.size(); i++) {
					Applied applied = (Applied) L.get(i);
					int aid = applied.getID();
					if (aid == -1)
						applied.insert();
					else if (aid < -1)
						applied.delete();
					else if (aid > 0)
						applied.update();
				}

			}
			t.commit();
		}
		catch (Exception e) {
			try {
				t.rollback();
			}
			catch (javax.transaction.SystemException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		}
		return returnid;
	}

	public PresentationObject makeApplicantTable(IWContext iwc, IWResourceBundle iwrb) {

		DataTable T = new DataTable();
		T.setWidth("100%");
		T.addTitle(iwrb.getLocalizedString("applicants", "Applicants"));
		T.setTitlesHorizontal(true);

		int row = 1;
		int col = 1;
		Image printImage = iwb.getImage("print.gif");
		Image viewImage = iwb.getImage("view.gif");
		Image trashImage = iwb.getImage("trashcan.gif");
		T.add(headerText(iwrb.getLocalizedString("nr", "Nr")), col++, row);
		T.add(headerText(iwrb.getLocalizedString("prior", "Pr")), col++, row);
		T.add(headerText(iwrb.getLocalizedString("refnum", "Ref. num")), col++, row);
		T.add(headerText(iwrb.getLocalizedString("name", "Name")), col++, row);
		T.add(headerText(iwrb.getLocalizedString("ssn", "Socialnumber")), col++, row);
		T.add(headerText(iwrb.getLocalizedString("legal_residence", "Legal Residence")), col++, row);
		T.add(headerText(iwrb.getLocalizedString("residence", "Residence")), col++, row);
		T.add(headerText(iwrb.getLocalizedString("po", "PO")), col++, row);
		T.add(headerText(iwrb.getLocalizedString("phone", "Residence phone")), col++, row);
		T.add(headerText(iwrb.getLocalizedString("mobile_phone", "Mobile phone")), col++, row);
		//T.add(headerText(iwrb.getLocalizedString("v","V")),col++,row);
		//T.add(headerText(iwrb.getLocalizedString("p","P")),col++,row);
		/*
		Table T = new Table();
		  T.setCellpadding(2);
		  T.setCellspacing(1);
		*/
		List L = null;
		if (sGlobalOrder != null && !sGlobalOrder.equals("-1")) {
			L = ApplicationFinder.listOfApplicationHoldersInSubject(iSubjectId, sGlobalStatus, sGlobalOrder);
		}
		else {
			L = ApplicationFinder.listOfApplicationHoldersInSubject(iSubjectId, sGlobalStatus, null);
		}

		if (L != null) {
			ListIterator iterator = L.listIterator();
			iwc.setSessionAttribute("iterator", iterator);
			int len = L.size();

			boolean showcan = false;
			if (sGlobalStatus.equals(com.idega.block.application.data.ApplicationBMPBean.STATUS_REJECTED)) {
				T.add(headerText(iwrb.getLocalizedString("g", "g")), col++, row);
				showcan = true;
			}

			int lastcol = 1;
			for (int i = 0; i < len; i++) {
				row = i + 2;
				col = 1;
				ApplicationHolder AH = (ApplicationHolder) L.get(i);
				Applicant A = AH.getApplicant();
				Application a = AH.getApplication();
				CampusApplicationHome CAHome = null;
				CampusApplication CA = null;
				try {
					CAHome = (CampusApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(CampusApplication.class);
					java.util.Collection coll = CAHome.findAllByApplicationId(((Integer) a.getPrimaryKeyValue()).intValue());
					if (coll != null) {
						java.util.Iterator it = coll.iterator();
						if (it.hasNext())
							CA = (CampusApplication) it.next(); //CAHome.findByPrimaryKeyLegacy(((Integer)it.next()).intValue());
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}

				/*
				String cypher = null;
				if (a != null && a.getID() != -1) {
				com.idega.block.application.business.ReferenceNumberHandler h = new com.idega.block.application.business.ReferenceNumberHandler();
				String key = h.getCypherKey(iwc);
				com.idega.util.CypherText ct = new com.idega.util.CypherText();
				
				String id = Integer.toString(a.getID());
				while (id.length() < 6)
				id = "0" + id;
				
				cypher = ct.doCyper(id,key);
				}
				*/
				String cypher = null;
				if (a != null && a.getID() != -1)
					cypher = ReferenceNumberFinder.getInstance(iwc).lookup(a.getID());

				T.add(Edit.formatText(String.valueOf(i + 1)), col++, row);
				if (CA == null)
					T.add(Edit.formatText("A"), col++, row);
				else
					T.add(Edit.formatText(CA.getPriorityLevel()), col++, row);
				T.add(Edit.formatText(cypher), col++, row);
				String Name = A.getFirstName() + " " + A.getMiddleName() + " " + A.getLastName();
				T.add(Edit.formatText(Name), col++, row);
				T.add(Edit.formatText(A.getSSN() != null ? A.getSSN() : ""), col++, row);
				T.add(Edit.formatText(A.getLegalResidence() != null ? A.getLegalResidence() : ""), col++, row);
				T.add(Edit.formatText(A.getResidence() != null ? A.getResidence() : ""), col++, row);
				T.add(Edit.formatText(A.getPO() != null ? A.getPO() : ""), col++, row);
				T.add(Edit.formatText(A.getResidencePhone() != null ? A.getResidencePhone() : ""), col++, row);
				T.add(Edit.formatText(A.getMobilePhone() != null ? A.getMobilePhone() : ""), col++, row);
				T.add((getPDFLink(printImage, A.getID())), col++, row);
				T.add(getApplicationLink(viewImage, a.getID()), col++, row);
				T.add(getTrashLink(trashImage, a.getID()), col, row);
				if (lastcol < col)
					lastcol = col;
			}

			/*
			T.setHorizontalZebraColored(Edit.colorLightBlue,Edit.colorWhite);
			T.setRowColor(1,Edit.colorBlue);
			int lastrow = len+2;
			T.mergeCells(1,lastrow,lastcol,lastrow);
			T.setRowColor(lastrow,Edit.colorRed);
			T.add(Edit.formatText(" "),1,lastrow);
			T.setHeight(lastrow,Edit.bottomBarThickness);
			T.add(getPDFLink(printImage,sGlobalStatus,iSubjectId),1,++row);
			*/
		}
		else {
			T.add(Edit.formatText(iwrb.getLocalizedString("no_applications", "No applications in database")));
		}
		return T;
	}

	public PresentationObject makeSubjectStatisticsTable() {

		DataTable DT = new DataTable();
		DT.addTitle(iwrb.getLocalizedString("subject_info", "Subject Info"));
		DT.setTitlesHorizontal(true);
		int row = 1, col = 1;
		DT.add(Edit.formatText(iwrb.getLocalizedString("subject", "Subject")), col++, row);
		DT.add(Edit.formatText(iwrb.getLocalizedString("count", "Count")), col++, row);
		DT.add(Edit.formatText(iwrb.getLocalizedString("status", "Status")), col++, row);

		DT.add(Edit.formatText(iwrb.getLocalizedString("last_submission", "Last in")), col++, row);
		DT.add(Edit.formatText(iwrb.getLocalizedString("first_submission", "First in")), col++, row);
		DT.add(Edit.formatText(iwrb.getLocalizedString("last_changed", "Last change")), col++, row);
		DT.add(Edit.formatText(iwrb.getLocalizedString("first_change", "First Change")), col++, row);

		col = 1;
		row++;
		try {
			List infos = com.idega.data.EntityFinder.getInstance().findAll(ApplicationSubjectInfo.class);
			DateFormat df = DateFormat.getDateTimeInstance();
			if (infos != null) {
				java.util.Iterator iter = infos.iterator();
				ApplicationSubjectInfo info;
				while (iter.hasNext()) {

					info = (ApplicationSubjectInfo) iter.next();
					Link subjLink = new Link(Edit.formatText(info.getSubjectName()));
					subjLink.addParameter("app_subject_id", info.getSubjectId());
					subjLink.addParameter("global_status", info.getStatus());
					DT.add(subjLink, col++, row);
					DT.add(Edit.formatText(info.getNumber()), col++, row);
					DT.add(Edit.formatText(getStatus(info.getStatus())), col++, row);
					DT.add(Edit.formatText(df.format((Date) info.getLastSubmission())), col++, row);
					DT.add(Edit.formatText(df.format((Date) info.getFirstSubmission())), col++, row);
					DT.add(Edit.formatText(df.format((Date) info.getLastChange())), col++, row);
					DT.add(Edit.formatText(df.format((Date) info.getFirstChange())), col++, row);
					row++;
					col = 1;

				}

			}
		}
		catch (Exception fex) {
			fex.printStackTrace();
		}
		DT.getContentTable().setColumnAlignment(4, "right");
		DT.getContentTable().setColumnAlignment(5, "right");
		DT.getContentTable().setColumnAlignment(6, "right");
		DT.getContentTable().setColumnAlignment(7, "right");
		return DT;
	}

	public PresentationObject makeApplicationTable(int id, boolean bEdit, IWContext iwc, IWResourceBundle iwrb) {
		Form theForm = new Form();
		theForm.add(new HiddenInput("application_id", String.valueOf(id)));
		try {
			Application eApplication = null;
			Applicant eApplicant = null;
			if (id < -1 && iterator != null) {
				ApplicationHolder AS = null;
				if (id == -2 && iterator.hasPrevious()) {
					AS = (ApplicationHolder) iterator.previous();
				}
				else if (id == -4 && iterator.hasNext()) {
					AS = (ApplicationHolder) iterator.next();
				}
				if (AS != null) {
					eApplication = AS.getApplication();
					eApplicant = AS.getApplicant();
					id = eApplication.getID();
				}
			}
			else {
				eApplication = ((com.idega.block.application.data.ApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(Application.class)).findByPrimaryKeyLegacy(id);
				eApplicant = ((com.idega.block.application.data.ApplicantHome) com.idega.data.IDOLookup.getHomeLegacy(Applicant.class)).findByPrimaryKeyLegacy(eApplication.getApplicantId());
			}

			if (eApplication != null && eApplicant != null) {
				Applicant spouse = null;
				Vector children = null;
				java.util.Iterator iter = eApplicant.getChildren();
				if (iter != null) {
					Applicant a;
					while (iter.hasNext()) {
						a = (Applicant) iter.next();
						if (a.getStatus() != null) {
							if (a.getStatus().equals("P")) {
								spouse = a;
							}
							else if (a.getStatus().equals("C")) {
								if (children == null)
									children = new Vector();
								children.add(a);
							}
						}
					}
				}
				CampusApplication A = ((is.idega.idegaweb.campus.block.application.data.CampusApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(CampusApplication.class)).createLegacy();
				CampusApplication eCampusApplication = ((CampusApplication[]) (A.findAllByColumn(A.getApplicationIdColumnName(), id)))[0];
				List L = CampusApplicationFinder.listOfAppliedInApplication(eCampusApplication.getID());

				int border = 0;

				Table OuterFrame = new Table(3, 1);
				OuterFrame.setCellpadding(2);
				OuterFrame.setCellspacing(0);
				OuterFrame.setBorder(border);
				OuterFrame.setRowVerticalAlignment(1, "top");
				//OuterFrame.setWidth(1,"550");

				Table Left = new Table(1, 3);
				Left.add(getViewApplicant(eApplicant, eCampusApplication, iwrb), 1, 1);
				Left.add(getViewSpouse(spouse, eCampusApplication, iwrb), 1, 2);
				Left.add(getViewChildren(children, eCampusApplication, iwrb), 1, 3);

				Table Middle = new Table(1, 4);
				Middle.add(getViewApplication(eApplication), 1, 1);
				Middle.add(getViewApartment(eCampusApplication, L, iwc, iwrb), 1, 2);
				Middle.add(getViewApartmentExtra(eCampusApplication, iwc, iwrb), 1, 3);
				Middle.add(getOtherInfo(eCampusApplication, iwc, iwrb, false), 1, 3);

				Table Right = new Table(1, 3);
				//Right.add(getRemoteControl(iwrb),1,1);
				Right.add(getSubjectControl(iwrb, eApplication), 1, 1);
				Right.add(getKnobs(iwrb), 1, 2);
				Right.add(getButtons(eApplication, eApplication.getStatus(), eCampusApplication.getPriorityLevel(), bEdit, iwrb), 1, 3);

				OuterFrame.add(Left, 1, 1);
				OuterFrame.add(Middle, 2, 1);
				OuterFrame.add(Right, 3, 1);

				theForm.add(OuterFrame);

			}
		}
		catch (SQLException sql) {
			sql.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return theForm;
	}

	public PresentationObject makeApplicationForm(int id, boolean bEdit, IWContext iwc, IWResourceBundle iwrb) {
		Form theForm = new Form();
		theForm.add(new HiddenInput("application_id", String.valueOf(id)));
		try {
			Application eApplication = null;
			Applicant spouse = null;
			Vector children = null;
			Applicant eApplicant = null;
			if (id < -1 && iterator != null) {
				ApplicationHolder AS = null;
				if (id == -2 && iterator.hasPrevious()) {
					AS = (ApplicationHolder) iterator.previous();
				}
				else if (id == -4 && iterator.hasNext()) {
					AS = (ApplicationHolder) iterator.next();
				}
				if (AS != null) {
					eApplication = AS.getApplication();
					eApplicant = AS.getApplicant();
					id = eApplication.getID();
				}
			}
			else {
				if (id > 0) {
					eApplication = ((com.idega.block.application.data.ApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(Application.class)).findByPrimaryKeyLegacy(id);
					eApplicant = ((com.idega.block.application.data.ApplicantHome) com.idega.data.IDOLookup.getHomeLegacy(Applicant.class)).findByPrimaryKeyLegacy(eApplication.getApplicantId());
				}
			}

			CampusApplication A = null;
			CampusApplication eCampusApplication = null;
			List L = null;
			if (eApplication != null && eApplicant != null) {
				java.util.Iterator iter = eApplicant.getChildren();
				if (iter != null) {
					Applicant a;
					while (iter.hasNext()) {
						a = (Applicant) iter.next();
						if (a.getStatus().equals("P")) {
							spouse = a;
						}
						else if (a.getStatus().equals("C")) {
							if (children == null)
								children = new Vector();
							children.add(a);
						}
					}
				}

				A = ((is.idega.idegaweb.campus.block.application.data.CampusApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(CampusApplication.class)).createLegacy();
				eCampusApplication = ((CampusApplication[]) (A.findAllByColumn(A.getApplicationIdColumnName(), id)))[0];
				L = CampusApplicationFinder.listOfAppliedInApplication(eCampusApplication.getID());
			}

			int border = 0;

			Table OuterFrame = new Table(3, 1);
			OuterFrame.setCellpadding(2);
			OuterFrame.setCellspacing(0);
			OuterFrame.setBorder(border);
			OuterFrame.setRowVerticalAlignment(1, "top");
			//OuterFrame.setWidth(1,"550");

			Table Left = new Table(1, 3);
			Left.add(getFieldsApplicant(eApplicant, eCampusApplication, iwrb), 1, 1);
			Left.add(getFieldsSpouse(spouse, eCampusApplication, iwrb), 1, 2);
			Left.add(getFieldsChildren(children, eCampusApplication, iwrb), 1, 3);

			Table Middle = new Table(1, 4);
			Middle.add(getViewApplication(eApplication), 1, 1);
			Middle.add(getFieldsApartment(eCampusApplication, L, iwc, iwrb), 1, 2);
			Middle.add(getFieldsApartmentExtra(eCampusApplication, iwc, iwrb), 1, 3);
			Middle.add(getOtherInfo(eCampusApplication, iwc, iwrb, true), 1, 4);

			Table Right = new Table(1, 3);
			//Right.add(getRemoteControl(iwrb),1,1);
			Right.add(getSubjectControl(iwrb, eApplication), 1, 1);
			Right.add(getKnobs(iwrb), 1, 2);
			String status = eApplication != null ? eApplication.getStatus() : "";
			String pStatus = eCampusApplication != null ? eCampusApplication.getPriorityLevel() : "";
			Right.add(getButtons(eApplication, status, pStatus, bEdit, iwrb), 1, 3);

			OuterFrame.add(Left, 1, 1);
			OuterFrame.add(Middle, 2, 1);
			OuterFrame.add(Right, 3, 1);

			theForm.add(OuterFrame);
		}
		catch (SQLException sql) {
			sql.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return theForm;
	}

	public PresentationObject getViewApplicant(Applicant eApplicant, CampusApplication eCampusApplication, IWResourceBundle iwrb) {
		DataTable T = new DataTable();
		T.setWidth("100%");
		T.addTitle(iwrb.getLocalizedString("applicant", "Applicant"));
		int col = 1;
		int row = 1;
		T.add(Edit.formatText(iwrb.getLocalizedString("name", "Name")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("ssn", "Socialnumber")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("legal_residence", "Legal Residence")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("residence", "Residence")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("po", "PO")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("phone", "Residence phone")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("mobile_phone", "Mobile phone")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("email", "Email")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("faculty", "Faculty")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("studytrack", "Study Track")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("study_begins", "Study begins")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("study_ends", "Study ends")), col, row++);
		//T.add(Edit.formatText(iwrb.getLocalizedString("income","Income")),col,row++);

		col = 2;
		row = 1;
		T.add(Edit.formatText(eApplicant.getFullName()), col, row++);
		T.add(Edit.formatText(eApplicant.getSSN()), col, row++);
		T.add(Edit.formatText(eApplicant.getLegalResidence()), col, row++);
		T.add(Edit.formatText(eApplicant.getResidence()), col, row++);
		T.add(Edit.formatText(eApplicant.getPO()), col, row++);
		T.add(Edit.formatText(eApplicant.getResidencePhone()), col, row++);
		T.add(Edit.formatText(eApplicant.getMobilePhone()), col, row++);
		String email = eCampusApplication.getEmail();
		if (email == null)
			email = "";
		T.add(new Link(Edit.formatText(email), "mailto:" + email), col, row++);
		T.add(Edit.formatText(eCampusApplication.getFaculty()), col, row++);
		T.add(Edit.formatText(eCampusApplication.getStudyTrack()), col, row++);
		String beginMonth = (eCampusApplication.getStudyBeginMonth().toString());
		String endMonth = (eCampusApplication.getStudyEndMonth().toString());
		T.add(Edit.formatText(beginMonth + " " + eCampusApplication.getStudyBeginYear().intValue()), col, row++);
		T.add(Edit.formatText(endMonth + " " + eCampusApplication.getStudyEndYear().intValue()), col, row++);
		// T.add(Edit.formatText(eCampusApplication.getIncome().intValue()),col,row);

		return T;
	}

	public PresentationObject getFieldsApplicant(Applicant eApplicant, CampusApplication eCampusApplication, IWResourceBundle iwrb) {
		int year = IWTimestamp.RightNow().getYear();
		DataTable T = new DataTable();
		T.setWidth("100%");
		T.addTitle(iwrb.getLocalizedString("applicant", "Applicant"));

		int col = 1;
		int row = 1;
		T.add(Edit.formatText(iwrb.getLocalizedString("name", "Name")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("ssn", "Socialnumber")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("legal_residence", "Legal Residence")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("residence", "Residence")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("po", "PO")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("phone", "Residence phone")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("mobile_phone", "Mobile phone")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("email", "Email")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("faculty", "Faculty")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("studytrack", "Study Track")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("study_begins", "Study begins")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("study_ends", "Study ends")), col, row++);
		//T.add(Edit.formatText(iwrb.getLocalizedString("income","Income")),col,row++);

		col = 2;
		row = 1;

		TextInput tiFullName = new TextInput("ti_full");
		Edit.setStyle(tiFullName);
		TextInput tiSsn = new TextInput("ti_ssn");
		Edit.setStyle(tiSsn);
		TextInput tiLegRes = new TextInput("ti_legres");
		Edit.setStyle(tiLegRes);
		TextInput tiRes = new TextInput("ti_res");
		Edit.setStyle(tiRes);
		TextInput tiPo = new TextInput("ti_po");
		Edit.setStyle(tiPo);
		TextInput tiResPho = new TextInput("ti_respho");
		Edit.setStyle(tiResPho);
		TextInput tiMobPho = new TextInput("ti_mobpho");
		Edit.setStyle(tiMobPho);
		TextInput tiEmail = new TextInput("ti_email");
		Edit.setStyle(tiEmail);
		TextInput tiFac = new TextInput("ti_facult");
		Edit.setStyle(tiFac);
		TextInput tiTrack = new TextInput("ti_track");
		Edit.setStyle(tiTrack);
		/*
		TextInput tiIncome= new TextInput("ti_income");
		Edit.setStyle(tiIncome);
		tiIncome.setAsIntegers();
		*/

		IWTimestamp today = IWTimestamp.RightNow();
		String beginMonth = String.valueOf(today.getMonth());
		String beginYear = String.valueOf(today.getYear());

		String endMonth = String.valueOf(today.getMonth());
		String endYear = String.valueOf(today.getYear());

		if (eApplicant != null && eCampusApplication != null) {

			tiFullName.setContent(eApplicant.getFullName() != null ? eApplicant.getFullName() : "");
			tiSsn.setContent(eApplicant.getSSN() != null ? eApplicant.getSSN() : "");
			tiLegRes.setContent(eApplicant.getLegalResidence() != null ? eApplicant.getLegalResidence() : "");
			tiRes.setContent(eApplicant.getResidence() != null ? eApplicant.getResidence() : "");
			tiPo.setContent(eApplicant.getPO() != null ? eApplicant.getPO() : "");
			tiResPho.setContent(eApplicant.getResidencePhone() != null ? eApplicant.getResidencePhone() : "");
			tiMobPho.setContent(eApplicant.getMobilePhone() != null ? eApplicant.getMobilePhone() : "");
			tiEmail.setContent(eCampusApplication.getEmail() != null ? eCampusApplication.getEmail() : "");
			tiFac.setContent(eCampusApplication.getFaculty() != null ? eCampusApplication.getFaculty() : "");
			tiTrack.setContent(eCampusApplication.getStudyTrack() != null ? eCampusApplication.getStudyTrack() : "");
			//tiIncome.setContent(eCampusApplication.getIncome().toString());

			beginMonth = (eCampusApplication.getStudyBeginMonth().toString());
			endMonth = (eCampusApplication.getStudyEndMonth().toString());
			beginYear = eCampusApplication.getStudyBeginYear().toString();
			endYear = eCampusApplication.getStudyEndYear().toString();
		}

		T.add(tiFullName, col, row++);
		T.add(tiSsn, col, row++);
		T.add(tiLegRes, col, row++);
		T.add(tiRes, col, row++);
		T.add(tiPo, col, row++);
		T.add(tiResPho, col, row++);
		T.add(tiMobPho, col, row++);
		T.add(tiEmail, col, row++);
		T.add(tiFac, col, row++);
		T.add(tiTrack, col, row++);

		DropdownMenu drBM = intDrop("dr_bm", beginMonth, 1, 12);
		DropdownMenu drEM = intDrop("dr_em", endMonth, 1, 12);
		DropdownMenu drBY = intDrop("dr_by", beginYear, year - 10, year + 10);
		DropdownMenu drEY = intDrop("dr_ey", endYear, year - 10, year + 10);

		Edit.setStyle(drBM);
		Edit.setStyle(drEM);
		Edit.setStyle(drBY);
		Edit.setStyle(drEY);
		T.add(drBM, col, row);
		T.add(drBY, col, row++);
		T.add(drEM, col, row);
		T.add(drEY, col, row++);
		//T.add(tiIncome,col,row);

		return T;
	}

	private void updateApplicant(IWContext iwc, Applicant eApplicant, CampusApplication eCampusApplication) {
		String sFullName = iwc.getParameter("ti_full");
		String sSsn = iwc.getParameter("ti_ssn");
		String sLegRes = iwc.getParameter("ti_legres");
		String sRes = iwc.getParameter("ti_res");
		String sPo = iwc.getParameter("ti_po");
		String sResPho = iwc.getParameter("ti_respho");
		String sMobPho = iwc.getParameter("ti_mobpho");
		String sEmail = iwc.getParameter("ti_email");
		String sFac = iwc.getParameter("ti_facult");
		String sTrack = iwc.getParameter("ti_track");
		String sIncome = iwc.getParameter("ti_income");
		String sBM = iwc.getParameter("dr_bm");
		String sEM = iwc.getParameter("dr_em");
		String sBY = iwc.getParameter("dr_by");
		String sEY = iwc.getParameter("dr_ey");

		try {
			int iIncome = 0;
			if (sIncome != null && sIncome.length() > 0)
				iIncome = Integer.parseInt(sIncome);
			int iBM = sBM != null ? Integer.parseInt(sBM) : 0;
			int iEM = sEM != null ? Integer.parseInt(sEM) : 0;
			int iBY = sBY != null ? Integer.parseInt(sBY) : 0;
			int iEY = sEY != null ? Integer.parseInt(sEY) : 0;
			eCampusApplication.setIncome(iIncome);
			if (iBM != 0)
				eCampusApplication.setStudyBeginMonth(iBM);
			if (iBY != 0)
				eCampusApplication.setStudyBeginYear(iBY);
			if (iEM != 0)
				eCampusApplication.setStudyEndMonth(iEM);
			if (iEY != 0)
				eCampusApplication.setStudyEndYear(iEY);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		eCampusApplication.setEmail(sEmail);
		eCampusApplication.setFaculty(sFac);
		eCampusApplication.setStudyTrack(sTrack);
		eApplicant.setLegalResidence(sLegRes);
		eApplicant.setSSN(sSsn);
		eApplicant.setStatus("S");
		eApplicant.setPO(sPo);
		eApplicant.setResidencePhone(sResPho);
		eApplicant.setMobilePhone(sMobPho);
		eApplicant.setResidence(sRes);
		if (sFullName != null) {
			StringTokenizer st = new StringTokenizer(sFullName);
			if (st.hasMoreTokens()) {
				eApplicant.setFirstName(st.nextToken());
			}
			String mid = "";
			if (st.hasMoreTokens()) {
				mid = (st.nextToken());
			}

			if (st.hasMoreTokens()) {
				eApplicant.setLastName(st.nextToken());
				eApplicant.setMiddleName(mid);
			}
			else {
				eApplicant.setLastName(mid);
			}
		}
	}

	public PresentationObject getViewSpouse(Applicant spouse, CampusApplication eCampusApplication, IWResourceBundle iwrb) {
		int year = IWTimestamp.RightNow().getYear();
		DataTable T = new DataTable();
		T.setWidth("100%");
		T.addTitle(iwrb.getLocalizedString("spouse", "Spouse"));
		int col = 1;
		int row = 1;

		if (spouse != null) {
			T.add(Edit.formatText(iwrb.getLocalizedString("name", "Name")), col, row++);
			T.add(Edit.formatText(iwrb.getLocalizedString("ssn", "Socialnumber")), col, row++);
			T.add(Edit.formatText(iwrb.getLocalizedString("school", "School")), col, row++);
			T.add(Edit.formatText(iwrb.getLocalizedString("studytrack", "Study Track")), col, row++);
			T.add(Edit.formatText(iwrb.getLocalizedString("study_begins", "Study begins")), col, row++);
			T.add(Edit.formatText(iwrb.getLocalizedString("study_ends", "Study ends")), col, row++);
			//T.add(Edit.formatText(iwrb.getLocalizedString("income","Income")),col,row++);
			col = 2;
			row = 1;

			T.add(Edit.formatText(spouse.getName()), col, row++);
			T.add(Edit.formatText(spouse.getSSN()), col, row++);
			T.add(Edit.formatText(eCampusApplication.getSpouseSchool()), col, row++);
			T.add(Edit.formatText(eCampusApplication.getSpouseStudyTrack()), col, row++);
			String beginMonth = (eCampusApplication.getSpouseStudyBeginMonth().toString());
			String endMonth = (eCampusApplication.getSpouseStudyEndMonth().toString());
			T.add(Edit.formatText(beginMonth + " " + eCampusApplication.getSpouseStudyBeginYear().intValue()), col, row++);
			T.add(Edit.formatText(endMonth + " " + eCampusApplication.getSpouseStudyEndYear().intValue()), col, row++);
			//T.add(Edit.formatText(eCampusApplication.getSpouseIncome().intValue()),col,row);

		}

		return T;
	}

	public PresentationObject getFieldsSpouse(Applicant spouse, CampusApplication eCampusApplication, IWResourceBundle iwrb) {
		int year = IWTimestamp.RightNow().getYear();
		DataTable T = new DataTable();
		T.setWidth("100%");
		T.addTitle(iwrb.getLocalizedString("spouse", "Spouse"));
		int col = 1;
		int row = 1;
		T.add(Edit.formatText(iwrb.getLocalizedString("name", "Name")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("ssn", "Socialnumber")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("school", "School")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("studytrack", "Study Track")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("study_begins", "Study begins")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("study_ends", "Study ends")), col, row++);
		//T.add(Edit.formatText(iwrb.getLocalizedString("income","Income")),col,row++);
		col = 2;
		row = 1;

		TextInput tiSpName = new TextInput("ti_sp_name");
		TextInput tiSpSsn = new TextInput("ti_sp_ssn");
		TextInput tiSpSchl = new TextInput("ti_sp_schl");
		TextInput tiSpStTr = new TextInput("ti_sp_sttr");
		//TextInput tiSPIncome = new TextInput("ti_sp_income");

		IWTimestamp today = IWTimestamp.RightNow();
		String beginMonth = String.valueOf(today.getMonth());
		String beginYear = String.valueOf(today.getYear());
		String endMonth = String.valueOf(today.getMonth());
		String endYear = String.valueOf(today.getYear());
		if (eCampusApplication != null && spouse != null) {
			//	System.err.println("spouse "+spouse.getID());
			tiSpName.setContent(spouse.getName());
			tiSpSsn.setContent(spouse.getSSN());
			tiSpSchl.setContent(eCampusApplication.getSpouseSchool());
			tiSpStTr.setContent(eCampusApplication.getSpouseStudyTrack());
			//tiSPIncome.setContent(eCampusApplication.getSpouseIncome().toString());

			beginMonth = eCampusApplication.getSpouseStudyBeginMonth().toString();
			endMonth = eCampusApplication.getSpouseStudyEndMonth().toString();
			beginYear = eCampusApplication.getSpouseStudyBeginYear().toString();
			endYear = eCampusApplication.getSpouseStudyEndYear().toString();
			T.add(new HiddenInput("ti_sp_id", String.valueOf(spouse.getID())));
		}

		Edit.setStyle(tiSpName);
		Edit.setStyle(tiSpSsn);
		Edit.setStyle(tiSpSchl);
		Edit.setStyle(tiSpStTr);
		//Edit.setStyle(tiSPIncome);

		T.add(tiSpName, col, row++);
		T.add(tiSpSsn, col, row++);
		T.add(tiSpSchl, col, row++);
		T.add(tiSpStTr, col, row++);

		DropdownMenu drBM = intDrop("dr_sp_bm", beginMonth, 1, 12);
		DropdownMenu drEM = intDrop("dr_sp_em", endMonth, 1, 12);
		DropdownMenu drBY = intDrop("dr_sp_by", beginYear, year - 10, year + 10);
		DropdownMenu drEY = intDrop("dr_sp_ey", endYear, year - 10, year + 10);
		Edit.setStyle(drBM);
		Edit.setStyle(drEM);
		Edit.setStyle(drBY);
		Edit.setStyle(drEY);
		T.add(drBM, col, row);
		T.add(drBY, col, row++);
		T.add(drEM, col, row);
		T.add(drEY, col, row++);
		//T.add(tiSPIncome,col,row);

		return T;
	}

	private void updateSpouse(IWContext iwc, CampusApplication eCampusApplication, Applicant superApplicant, Applicant spouse) throws SQLException {
		String sSpId = iwc.getParameter("ti_sp_id");
		String sSpName = iwc.getParameter("ti_sp_name");
		String sSpSsn = iwc.getParameter("ti_sp_ssn");
		String sSpSchl = iwc.getParameter("ti_sp_schl");
		String sSpStTr = iwc.getParameter("ti_sp_sttr");
		String sSPIncome = iwc.getParameter("ti_sp_income");
		String sBM = iwc.getParameter("dr_sp_bm");
		String sEM = iwc.getParameter("dr_sp_em");
		String sBY = iwc.getParameter("dr_sp_by");
		String sEY = iwc.getParameter("dr_sp_ey");

		try {

			int iBM = Integer.parseInt(sBM);
			int iEM = Integer.parseInt(sEM);
			int iBY = Integer.parseInt(sBY);
			int iEY = Integer.parseInt(sEY);
			//eCampusApplication.setSpouseIncome(iIncome);
			if (iBM != 0)
				eCampusApplication.setSpouseStudyBeginMonth(iBM);
			if (iBY != 0)
				eCampusApplication.setSpouseStudyBeginYear(iBY);
			if (iEM != 0)
				eCampusApplication.setSpouseStudyEndMonth(iEM);
			if (iEY != 0)
				eCampusApplication.setSpouseStudyEndYear(iEY);
		}
		catch (Exception ex) {
			//ex.printStackTrace();
		}

		eCampusApplication.setSpouseStudyTrack(sSpStTr);
		eCampusApplication.setSpouseSchool(sSpSchl);

		if (sSpName != null && sSpName.length() > 0) {
			boolean update = true;
			if (spouse == null) {
				spouse = ((com.idega.block.application.data.ApplicantHome) com.idega.data.IDOLookup.getHomeLegacy(Applicant.class)).createLegacy();

				update = false;
			}

			if (!sSpName.equals(spouse.getName())) {
				spouse.setFullName(sSpName);
			}
			spouse.setSSN(sSpSsn);
			spouse.setStatus("P");
			if (update)
				spouse.update();
			else {
				spouse.insert();
				superApplicant.addChild(spouse);
			}

		}
	}

	public PresentationObject getViewChildren(Vector children, CampusApplication eCampusApplication, IWResourceBundle iwrb) {
		DataTable T = new DataTable();
		T.setWidth("100%");
		T.addTitle(iwrb.getLocalizedString("children", "Children"));
		T.setUseTitles(false);
		int col = 1;
		int row = 1;

		if (children != null) {
			Applicant child;
			for (int i = 0; i < children.size(); i++) {
				child = (Applicant) children.get(i);
				T.add(Edit.formatText(child.getName()), 1, row);
				T.add(Edit.formatText(child.getSSN()), 2, row++);
			}
		}
		return T;
	}

	public PresentationObject getFieldsChildren(Vector children, CampusApplication eCampusApplication, IWResourceBundle iwrb) {
		DataTable T = new DataTable();
		T.setWidth("100%");
		T.addTitle(iwrb.getLocalizedString("children", "Children"));
		T.setUseTitles(false);
		int col = 1;
		int row = 1;
		int count = 4;
		int childcount = children != null ? children.size() : 0;
		count = Math.max(count, childcount);
		for (int i = 0; i < count; i++) {
			TextInput childName = new TextInput("child_name" + i);
			TextInput childBirth = new TextInput("child_birth" + i);
			childName.setLength(30);
			childBirth.setLength(10);
			childBirth.setMaxlength(10);
			Edit.setStyle(childName);
			Edit.setStyle(childBirth);
			T.add(childName, 1, i + 1);
			T.add(childBirth, 2, i + 1);
			if (childcount > i) {
				Applicant child = (Applicant) children.get(i);
				childName.setContent(child.getName());
				childBirth.setContent(child.getSSN());
				T.add(new HiddenInput("ti_child_id" + i, String.valueOf(child.getID())));
			}
		}
		T.add(new HiddenInput("ti_child_count", String.valueOf(count)));
		return T;
	}

	private void updateChildren(IWContext iwc, CampusApplication eCampusApplication, Applicant superApplicant, Vector children) throws SQLException {
		if (iwc.isParameterSet("ti_child_count")) {
			int count = Integer.parseInt(iwc.getParameter("ti_child_count"));
			if (count > 0) {
				Hashtable chi = new Hashtable();
				if (children != null) {
					for (int i = 0; i < children.size(); i++) {
						Applicant child = (Applicant) children.get(i);
						chi.put(new Integer(child.getID()), child);
					}
				}
				for (int i = 0; i < count; i++) {
					String childName = iwc.getParameter("child_name" + i);
					String childSSN = iwc.getParameter("child_birth" + i);
					int childId = iwc.isParameterSet("ti_child_id" + i) ? Integer.parseInt(iwc.getParameter("ti_child_id" + i)) : -1;
					if (childName.length() > 0) {

						Applicant child = (Applicant) chi.get(new Integer(childId));
						boolean update = true;
						if (child == null) {
							child = ((com.idega.block.application.data.ApplicantHome) com.idega.data.IDOLookup.getHomeLegacy(Applicant.class)).createLegacy();
							update = false;
						}
						if (!childName.equals(child.getName())) {
							child.setFullName(childName);
						}
						child.setSSN(childSSN);
						child.setStatus("C");
						if (update)
							child.update();
						else {
							child.insert();
							superApplicant.addChild(child);
						}
					}
				}
			}
		}
	}

	public PresentationObject getViewApartment(CampusApplication eCampusApplication, List lApplied, IWContext iwc, IWResourceBundle iwrb) {
		DataTable T = new DataTable();
		T.setWidth("100%");
		T.addTitle(iwrb.getLocalizedString("applied", "Applied"));
		int col = 1;
		int row = 1;
		if (lApplied != null) {
			int len = lApplied.size();
			for (int i = 0; i < len; i++) {
				Applied A = (Applied) lApplied.get(i);
				T.add(Edit.formatText(i + 1), 1, row);
				T.add(Edit.formatText((BuildingCacher.getApartmentType(A.getApartmentTypeId().intValue()).getName())), 2, row++);
			}
		}
		return T;
	}

	public PresentationObject getViewApartmentExtra(CampusApplication eCampusApplication, IWContext iwc, IWResourceBundle iwrb) {
		DataTable T = new DataTable();
		T.setWidth("100%");
		T.addTitle(iwrb.getLocalizedString("requests", "Requests"));
		int col = 1;
		int row = 1;

		T.add(Edit.formatText(iwrb.getLocalizedString("housingfrom", "Housing from")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("wantfurniture", "Wants furniture")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("onwaitinglist", "On waitinglist")), col, row++);
		col = 2;
		row = 1;
		IWTimestamp iT = new IWTimestamp(eCampusApplication.getHousingFrom());
		T.add(Edit.formatText(iT.getLocaleDate(iwc)), col, row++);
		if (eCampusApplication.getWantFurniture())
			T.add(Edit.formatText("X"), col, row++);
		if (eCampusApplication.getOnWaitinglist())
			T.add(Edit.formatText("X"), col, row++);
		return T;
	}

	public PresentationObject getOtherInfo(CampusApplication eCampusApplication, IWContext iwc, IWResourceBundle iwrb, boolean editable) {
		DataTable T = new DataTable();
		T.setWidth("100%");
		T.addTitle(iwrb.getLocalizedString("otherinfo", "Other info"));
		int col = 1;
		int row = 1;
		T.add(Edit.formatText(iwrb.getLocalizedString("comment", "Comment")), col, row);

		String comment = null;

		if (eCampusApplication != null) {
			comment = eCampusApplication.getOtherInfo();
		}

		col = 2;
		row = 1;
		TextArea commentArea = new TextArea("ap_comment", comment);
		if (editable) {
			commentArea.setReadOnly(false);
			commentArea.setHeight(5);
			commentArea.setWidth(35);
		}
		else {
			commentArea.setReadOnly(true);
			commentArea.setHeight(5);
		}

		//    commentArea
		Edit.setStyle(commentArea);
		T.add(commentArea, col, row);

		return T;
	}

	public DropdownMenu drpTypes(Vector v, String name, String selected, boolean firstEmpty) {
		DropdownMenu drpTypes = new DropdownMenu(name);
		Edit.setStyle(drpTypes);
		if (firstEmpty)
			drpTypes.addMenuElementFirst("-1", "-");
		for (int i = 0; i < v.size(); i++) {
			ApartmentTypeComplexHelper eAprtType = (ApartmentTypeComplexHelper) v.elementAt(i);
			drpTypes.addMenuElement(eAprtType.getKey(), eAprtType.getName());
		}
		drpTypes.setSelectedElement(selected);
		return drpTypes;
	}

	public PresentationObject getFieldsApartment(CampusApplication eCampusApplication, List lApplied, IWContext iwc, IWResourceBundle iwrb) {
		DataTable T = new DataTable();
		T.setWidth("100%");
		T.addTitle(iwrb.getLocalizedString("applied", "Applied"));
		int col = 1;
		int row = 1;
		String sOne = "-1", sTwo = "-1", sThree = "-3";
		if (lApplied != null) {
			int len = lApplied.size();
			Applied A;
			ApartmentTypeComplexHelper ATCH;
			if (len >= 1) {
				A = (Applied) lApplied.get(0);
				ATCH = new ApartmentTypeComplexHelper(A.getApartmentTypeId().intValue(), A.getComplexId().intValue());
				sOne = ATCH.getKey();
			}
			if (len >= 2) {
				A = (Applied) lApplied.get(1);
				ATCH = new ApartmentTypeComplexHelper(A.getApartmentTypeId().intValue(), A.getComplexId().intValue());
				sTwo = ATCH.getKey();
			}
			if (len >= 3) {
				A = (Applied) lApplied.get(2);
				ATCH = new ApartmentTypeComplexHelper(A.getApartmentTypeId().intValue(), A.getComplexId().intValue());
				sThree = ATCH.getKey();
			}
		}

		java.util.Vector vAprtType = BuildingFinder.getAllApartmentTypesComplex();
		DropdownMenu drpOne = drpTypes(vAprtType, "drp_one", sOne, false);
		DropdownMenu drpTwo = drpTypes(vAprtType, "drp_two", sTwo, true);
		DropdownMenu drpThree = drpTypes(vAprtType, "drp_three", sThree, true);
		Edit.setStyle(drpOne);
		Edit.setStyle(drpTwo);
		Edit.setStyle(drpThree);

		T.add(Edit.formatText(1), 1, row);
		T.add(drpOne, 2, row++);
		T.add(Edit.formatText(2), 1, row);
		T.add(drpTwo, 2, row++);
		T.add(Edit.formatText(3), 1, row);
		T.add(drpThree, 2, row++);

		return T;
	}

	public PresentationObject getFieldsApartmentExtra(CampusApplication eCampusApplication, IWContext iwc, IWResourceBundle iwrb) {
		DataTable T = new DataTable();
		T.setWidth("100%");
		T.addTitle(iwrb.getLocalizedString("requests", "Requests"));
		int col = 1;
		int row = 1;
		T.add(Edit.formatText(iwrb.getLocalizedString("housingfrom", "Housing from")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("wantfurniture", "Wants furniture")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("onwaitinglist", "On waitinglist")), col, row++);

		IWTimestamp iT = new IWTimestamp();
		if (eCampusApplication != null) {
			iT = new IWTimestamp(eCampusApplication.getHousingFrom());
		}
		col = 2;
		row = 1;
		DateInput diRentFrom = new DateInput("ap_rentfrom", true);
		diRentFrom.setDate(iT.getSQLDate());
		diRentFrom.setStyleAttribute("style", Edit.styleAttribute);
		T.add(diRentFrom, col, row++);
		CheckBox chkFurni = new CheckBox("ap_furni", "true");
		Edit.setStyle(chkFurni);
		CheckBox chkWait = new CheckBox("ap_wait", "true");
		Edit.setStyle(chkWait);

		if (eCampusApplication != null) {
			chkFurni.setChecked(eCampusApplication.getWantFurniture());
			chkWait.setChecked(eCampusApplication.getOnWaitinglist());
		}
		T.add(chkFurni, col, row++);
		T.add(chkWait, col, row++);
		return T;
	}

	private List updateApartment(IWContext iwc, CampusApplication eCampusApplication, List lApplied) {
		String sRentFrom = iwc.getParameter("ap_rentfrom");
		String sFurni = iwc.getParameter("ap_furni");
		String sWait = iwc.getParameter("ap_wait");
		String comment = iwc.getParameter("ap_comment");
		Vector V = new Vector();
		if (eCampusApplication == null)
			eCampusApplication = ((is.idega.idegaweb.campus.block.application.data.CampusApplicationHome) com.idega.data.IDOLookup.getHomeLegacy(CampusApplication.class)).createLegacy();
		if (sRentFrom != null)
			eCampusApplication.setHousingFrom(new IWTimestamp(sRentFrom).getSQLDate());
		if ("true".equals(sFurni)) {
			eCampusApplication.setWantFurniture(true);
		}
		else
			eCampusApplication.setWantFurniture(false);
		if ("true".equals(sWait)) {
			eCampusApplication.setOnWaitinglist(true);
		}
		else
			eCampusApplication.setOnWaitinglist(false);

		if (comment != null)
			eCampusApplication.setOtherInfo(comment);

		String key1 = iwc.getParameter("drp_one");
		String key2 = iwc.getParameter("drp_two");
		String key3 = iwc.getParameter("drp_three");
		if (key1 != null && key2 != null && key3 != null) {
			Applied applied1 = null;
			Applied applied2 = null;
			Applied applied3 = null;
			if (lApplied != null) {
				applied1 = (Applied) lApplied.get(0);
			}
			else {
				applied1 = ((is.idega.idegaweb.campus.block.application.data.AppliedHome) com.idega.data.IDOLookup.getHomeLegacy(Applied.class)).createLegacy();
				lApplied = (List) new Vector();
				lApplied.add(applied1);
			}
			int type = ApartmentTypeComplexHelper.getPartKey(key1, 1);
			int complex = ApartmentTypeComplexHelper.getPartKey(key1, 2);
			applied1.setApartmentTypeId(type);
			applied1.setApplicationId(eCampusApplication.getID());
			applied1.setComplexId(complex);
			applied1.setOrder(1);

			if ((key2 != null)) {
				if (lApplied.size() >= 2) {
					applied2 = (Applied) lApplied.get(1);
				}
				else {
					applied2 = ((is.idega.idegaweb.campus.block.application.data.AppliedHome) com.idega.data.IDOLookup.getHomeLegacy(Applied.class)).createLegacy();
				}
				type = ApartmentTypeComplexHelper.getPartKey(key2, 1);
				complex = ApartmentTypeComplexHelper.getPartKey(key2, 2);
				applied2.setApartmentTypeId(type);
				applied2.setApplicationId(eCampusApplication.getID());
				applied2.setComplexId(complex);
				applied2.setOrder(2);

			}

			if ((key3 != null)) {
				if (lApplied.size() >= 3) {
					applied3 = (Applied) lApplied.get(2);
				}
				else {
					applied3 = ((is.idega.idegaweb.campus.block.application.data.AppliedHome) com.idega.data.IDOLookup.getHomeLegacy(Applied.class)).createLegacy();
				}
				type = ApartmentTypeComplexHelper.getPartKey(key3, 1);
				complex = ApartmentTypeComplexHelper.getPartKey(key3, 2);
				applied3.setApartmentTypeId(type);
				applied3.setApplicationId(eCampusApplication.getID());
				applied3.setComplexId(complex);
				applied3.setOrder(3);

			}

			/*
			if(applied3 == null && lApplied != null && lApplied.size() >= 3){
			((Applied)lApplied.get(2)).setID(-3);
			}
			if(applied2 == null && lApplied != null && lApplied.size() >= 2){
			((Applied)lApplied.get(1)).setID(-3);
			}
			*/
			if (applied1 != null && "-1".equals(key1)) {
				//        System.err.println("deleting 1");
				try {
					applied1.delete();
				}
				catch (SQLException ex) {
					ex.printStackTrace();
				}
				applied1 = null;
				if (applied2 != null) {
					applied2.setOrder(1);
				}
				if (applied3 != null) {
					applied3.setOrder(2);
				}

			}
			if (applied2 != null && "-1".equals(key2)) {

				try {
					applied2.delete();
				}
				catch (SQLException ex) {
					ex.printStackTrace();
				}
				applied2 = null;
				if (applied3 != null)
					applied3.setOrder(1);
			}
			if (applied3 != null && "-1".equals(key3)) {

				try {
					applied3.delete();
				}
				catch (SQLException ex) {
					ex.printStackTrace();
				}
				applied3 = null;
			}
			if (applied1 != null)
				V.add(applied1);
			if (applied2 != null)
				V.add(applied2);
			if (applied3 != null)
				V.add(applied3);

		}
		else {
			System.err.println("no key parameters for apartment");
		}
		return V;
	}

	public PresentationObject getViewApplication(Application eApplication) {
		DataTable T = new DataTable();
		T.setWidth("100%");
		T.addTitle(iwrb.getLocalizedString("application", "Application"));

		int row = 1;
		int col = 1;

		T.add(Edit.formatText(iwrb.getLocalizedString("submitted", "Submitted")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("changed", "Status change")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("status", "Status")), col, row++);
		col++;
		row = 1;
		if (eApplication != null) {
			T.add(Edit.formatText(eApplication.getSubmitted().toString()), col, row++);
			T.add(Edit.formatText(eApplication.getStatusChanged().toString()), col, row++);
			T.add(Edit.formatText(getStatus(eApplication.getStatus())), col, row++);
		}
		return T;
	}

	private PresentationObject getRemoteControl(IWResourceBundle iwrb) {
		DataTable T = new DataTable();
		T.setWidth("100%");
		T.addTitle(iwrb.getLocalizedString("extra", "Extra"));
		int col = 1;
		int row = 1;
		T.add(Edit.formatText(iwrb.getLocalizedString("tax_return", "Tax return")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("study_progress", "Study progress")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("choice1", "Choice 1")), col, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("choice2", "Choice 2")), col, row++);
		col++;
		row = 1;
		TextInput units = new TextInput("unit");
		units.setLength(1);
		Edit.setStyle(units);
		CheckBox choice1 = new CheckBox("choice1");
		Edit.setStyle(choice1);
		CheckBox choice2 = new CheckBox("choice2");
		Edit.setStyle(choice2);
		CheckBox choice3 = new CheckBox("choice3");
		Edit.setStyle(choice3);
		T.add(choice1, col, row++);
		T.add(units, col, row++);
		T.add(choice2, col, row++);
		T.add(choice3, col, row++);

		return T;
	}

	private PresentationObject getSubjectControl(IWResourceBundle iwrb, Application app) {
		DataTable T = new DataTable();
		T.setWidth("100%");
		T.addTitle(iwrb.getLocalizedString("subject", "Subject"));
		int col = 1;
		int row = 1;
		T.add(Edit.formatText(iwrb.getLocalizedString("current_subject", "Current Subject")), col, row++);

		col++;
		row = 1;

		DropdownMenu drp = subjectDrop("-1");
		if (app != null) {
			drp.setSelectedElement(String.valueOf(app.getSubjectId()));
		}
		drp.setName("app_subject_id");
		T.add(drp, col, row);

		return T;
	}

	private PresentationObject getButtons(Application eApplication, String sStatus, String sPriority, boolean bEdit, IWResourceBundle iwrb) {
		DataTable T = new DataTable();
		T.setWidth("100%");
		T.addTitle(iwrb.getLocalizedString("control", "Control"));
		int row = 1;
		int col = 1;
		if (eApplication != null) {
			DropdownMenu status = statusDrop("status_drop", sStatus);
			//status.setToSubmit();
			Edit.setStyle(status);
			T.add(status, col, row);

			DropdownMenu priority = priorityDrop("priority_drop", sPriority);
			Edit.setStyle(priority);
			T.add(priority, col, row);
		}
		if (bEdit) {
			SubmitButton view = new SubmitButton(iwrb.getLocalizedImageButton("view", "View"), "viewer", "View");
			T.add(view, col, row);
		}
		else {
			SubmitButton edit = new SubmitButton(iwrb.getLocalizedImageButton("edit", "Edit"), "editor", "Edit");
			T.add(edit, col, row);
		}
		SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("save", "Save"), "save", "Save");
		T.add(save, col, row);
		return T;
	}

	private PresentationObject getKnobs(IWResourceBundle iwrb) {
		Table T = new Table(5, 1);
		T.setAlignment("center");

		if (iterator != null) {
			if (iterator.hasPrevious()) {
				Link lLast = new Link(iwrb.getImage("back.gif"));
				lLast.addParameter(sView, "-2");
				T.add(lLast, 1, 1);
			}
			if (iterator.hasNext()) {
				Link lNext = new Link(iwrb.getImage("next.gif"));
				lNext.addParameter(sView, "-4");
				T.add(lNext, 5, 1);
			}
		}
		Link lList = new Link(iwb.getImage("list.gif"));
		T.add(lList, 3, 1);
		T.setCellpadding(1);
		T.setCellspacing(1);
		T.setBorder(0);
		return T;
	}

	public void doUpdate(IWContext iwc) {
		String sDesc = iwc.getParameter("app_subj_desc").trim();
		String sDate = iwc.getParameter("app_subj_xdate");
		if (sDesc.length() > 0) {
			ApplicationSubject AS = ((com.idega.block.application.data.ApplicationSubjectHome) com.idega.data.IDOLookup.getHomeLegacy(ApplicationSubject.class)).createLegacy();
			AS.setDescription(sDesc);
			AS.setExpires(new IWTimestamp(sDate).getSQLDate());
			try {
				AS.insert();
			}
			catch (SQLException ex) {

			}
		}
	}

	private Form subjectForm() {
		Form myForm = new Form();
		DropdownMenu drp = subjectDrop(String.valueOf(this.iSubjectId));
		DropdownMenu status = statusDrop("global_status", sGlobalStatus);
		DropdownMenu order = orderDrop("global_order", sGlobalOrder);
		SubmitButton New = new SubmitButton(iwrb.getLocalizedImageButton("new", "New"), "new_app", "true");
		SubmitButton Info = new SubmitButton(iwrb.getLocalizedImageButton("info", "Info"), "subj_info", "true");
		//SubmitButton New = new SubmitButton("new","New");
		//    SubmitButton New2 = new SubmitButton("new2","New transfer");
		drp.setToSubmit();
		status.setToSubmit();
		order.setToSubmit();
		Edit.setStyle(status);
		Edit.setStyle(order);
		Edit.setStyle(New);
		//    Edit.setStyle(New2);
		DataTable T = new DataTable();
		T.addTitle(iwrb.getLocalizedString("filter", "Filter"));
		T.setTitlesHorizontal(true);
		int col = 1;
		int row = 1;
		T.add(Edit.formatText(iwrb.getLocalizedString("subject", "Subject")), col++, row);
		T.add(Edit.formatText(iwrb.getLocalizedString("status", "Status")), col++, row);
		T.add(Edit.formatText(iwrb.getLocalizedString("order", "Order")), col++, row);
		row++;
		col = 1;
		T.add(drp, col++, row);
		T.add(status, col++, row);
		T.add(order, col++, row);
		T.add(Info, col++, row);
		if (iSubjectId > 0) {
			T.add(New, col++, row);
			//      T.add(New2,col++,row);
		}
		myForm.add(T);

		return myForm;
	}

	private DropdownMenu subjectDrop(String selected) {
		List L = listOfSubjects;
		DropdownMenu drp = new DropdownMenu("app_subject_id");
		drp.addMenuElement(-1, iwrb.getLocalizedString("subject", "Subject"));
		drp.addMenuElement(-99, iwrb.getLocalizedString("all_subject", "All"));
		if (L != null) {
			ApplicationSubject AS;
			int len = L.size();
			for (int i = 0; i < len; i++) {
				AS = (ApplicationSubject) L.get(i);
				drp.addMenuElement(AS.getID(), AS.getName());
			}
			Edit.setStyle(drp);
			if (selected.equals("-1")) {
				this.iSubjectId = ((ApplicationSubject) L.get(0)).getID();
				drp.setSelectedElement(String.valueOf(iSubjectId));
			}
			else
				drp.setSelectedElement(selected);
		}
		return drp;
	}

	private String getStatus(String status) {
		String r = "";
		char c = status.charAt(0);
		switch (c) {
			case 'S' :
				r = iwrb.getLocalizedString("submitted", "Submitted");
				break;
			case 'A' :
				r = iwrb.getLocalizedString("approved", "Approved");
				break;
			case 'R' :
				r = iwrb.getLocalizedString("rejected", "Rejected");
				break;
			case 'C' :
				r = iwrb.getLocalizedString("contracted", "Contract");
				break;
			case 'G' :
				r = iwrb.getLocalizedString("garbage", "Garbage");
				break;
		}
		return r;
	}

	private DropdownMenu intDrop(String name, String selected, int low, int high) {
		DropdownMenu drp = new DropdownMenu(name);
		for (int i = low; i <= high; i++) {
			drp.addMenuElement(String.valueOf(i));
		}
		drp.setSelectedElement(selected);
		return drp;
	}

	private DropdownMenu statusDrop(String name, String selected) {
		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElement("S", getStatus("S"));
		drp.addMenuElement("A", getStatus("A"));
		drp.addMenuElement("R", getStatus("R"));
		drp.setSelectedElement(selected);
		return drp;
	}

	private DropdownMenu priorityDrop(String name, String selected) {
		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElement("A", "A");
		drp.addMenuElement("B", "B");
		drp.addMenuElement("C", "C");
		drp.addMenuElement("D", "D");
		drp.addMenuElement("E", "E");
		drp.addMenuElement("T", "T");
		drp.setSelectedElement(selected);
		return drp;
	}

	private DropdownMenu orderDrop(String name, String selected) {
		DropdownMenu drp = new DropdownMenu(name);
		Applicant A = ((com.idega.block.application.data.ApplicantHome) com.idega.data.IDOLookup.getHomeLegacy(Applicant.class)).createLegacy();

		drp.addMenuElement("-1", iwrb.getLocalizedString("submitted", "Submitted"));
		drp.addMenuElement(com.idega.block.application.data.ApplicantBMPBean.getFullnameOrderValue(), iwrb.getLocalizedString("name", "Name"));
		drp.addMenuElement(com.idega.block.application.data.ApplicantBMPBean.getSSNColumnName(), iwrb.getLocalizedString("ssn", "Socialnumber"));
		drp.addMenuElement(com.idega.block.application.data.ApplicantBMPBean.getLegalResidenceColumnName(), iwrb.getLocalizedString("legal_residence", "Legal Residence"));
		drp.addMenuElement(com.idega.block.application.data.ApplicantBMPBean.getResidenceColumnName(), iwrb.getLocalizedString("residence", "Residence"));
		drp.addMenuElement(com.idega.block.application.data.ApplicantBMPBean.getResidenceColumnName(), iwrb.getLocalizedString("phone", "Residence phone"));
		drp.setSelectedElement(selected);
		return drp;
	}

	public Link getApplicationLink(PresentationObject MO, int id) {
		Link L = new Link(MO);
		L.setFontSize(1);
		L.addParameter(sView, id);
		return L;
	}

	public Text headerText(String text) {
		return Edit.formatText(text);
		/*
		Text T = new Text(text);
		T.setBold();
		//T.setFontColor(Edit.colorWhite);
		T.setFontSize(2);
		
		return T;
		*/
	}

	public Link getTrashLink(PresentationObject MO, int cam_app_id) {
		Link L = new Link(MO);
		L.addParameter("cam_app_trash", cam_app_id);
		return L;
	}

	public Link getPDFLink(PresentationObject MO, int cam_app_id) {
		Link L = new Link(MO);
		L.setWindowToOpen(ApplicationFilerWindow.class);
		L.addParameter("cam_app_id", cam_app_id);
		return L;
	}
	public Link getPDFLink(PresentationObject MO, String status, int subject_id) {
		Link L = new Link(MO);
		L.setWindowToOpen(ApplicationFilerWindow.class);
		L.addParameter("app_status", status);
		L.addParameter("app_sub_id", subject_id);
		return L;
	}

	public void main(IWContext iwc) {
		//isStaff = com.idega.core.accesscontrol.business.AccessControl
		isAdmin = iwc.hasEditPermission(this);
		control(iwc);
	}

}
