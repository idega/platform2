/*
 * $Id: CampusApprover.java,v 1.50 2004/06/04 17:37:15 aron Exp $
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
import is.idega.idegaweb.campus.presentation.CampusBlock;


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
import com.idega.data.IDOLookup;
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
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;

/**
 *
 * @author <a href="mail

/**
 *
 * @author <a href="mailto:aron@idega.is">Aron</a>
 * @version 1.0
 */
public class CampusApprover extends CampusBlock {
	protected final int ACT1 = 1, ACT2 = 2, ACT3 = 3, ACT4 = 4, ACT5 = 5;
	
	private int iSubjectId = -1,iGlobalSize = 50;
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
		if (iwc.getParameter("global_size") != null) {
			infoCheck = false;
			this.iGlobalSize = Integer.parseInt(iwc.getParameter("global_size"));
			iwc.setSessionAttribute("gl_size",new Integer( iGlobalSize));
		}
		else if (iwc.getSessionAttribute("gl_size") != null) {
			this.iGlobalSize = ((Integer) iwc.getSessionAttribute("gl_size")).intValue();
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
				add(makeApplicationTable(id, false, iwc));
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
					add(makeApplicationForm(id, bEdit, iwc));
				}
				else {
					add(makeApplicationTable(id, bEdit, iwc));
				}
			}
			else if (iwc.getParameter("new_app") != null && iwc.getParameter("new_app").equals("true")) {
				add(makeApplicationForm(-1, true, iwc));
			}
			else if (iwc.getParameter("new2") != null) {
				add(makeApplicationForm(-1, true, iwc));
			}
			else if (infoCheck) {
				add(subjectForm());
				add(makeSubjectStatisticsTable(iwc));
			}
			else {
				add(subjectForm());
				add(makeApplicantTable(iwc));
			}
		}

		else
			add(getNoAccessObject(iwc));
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
							wl.setLastConfirmationDate(IWTimestamp.getTimestampRightNow());
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

			CAHome = (CampusApplicationHome) IDOLookup.getHomeLegacy(CampusApplication.class);
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

	public PresentationObject makeApplicantTable(IWContext iwc) {

		DataTable T = getDataTable();
		T.setWidth("100%");
		
		T.setTitlesHorizontal(true);

		int row = 1;
		int col = 1;
		Image printImage = getBundle().getImage("print.gif");
		Image viewImage = getBundle().getImage("view.gif");
		Image trashImage = getBundle().getImage("trashcan.gif");
		T.add(headerText(localize("nr", "Nr")), col++, row);
		T.add(headerText(localize("prior", "Pr")), col++, row);
		T.add(headerText(localize("refnum", "Ref. num")), col++, row);
		T.add(headerText(localize("name", "Name")), col++, row);
		T.add(headerText(localize("ssn", "Socialnumber")), col++, row);
		T.add(headerText(localize("legal_residence", "Legal Residence")), col++, row);
		T.add(headerText(localize("residence", "Residence")), col++, row);
		T.add(headerText(localize("po", "PO")), col++, row);
		T.add(headerText(localize("phone", "Residence phone")), col++, row);
		T.add(headerText(localize("mobile_phone", "Mobile phone")), col++, row);
		//T.add(headerText(localize("v","V")),col++,row);
		//T.add(headerText(localize("p","P")),col++,row);
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
			
			if(iGlobalSize>0 && iGlobalSize<=len){
				len = iGlobalSize;
			}
			T.addTitle(localize("applicants", "Applicants")+" "+localize("showing","showing")
			+" "+len+" "+localize("of","of")+" "+L.size());

			boolean showcan = false;
			if (sGlobalStatus.equals(com.idega.block.application.data.ApplicationBMPBean.STATUS_REJECTED)) {
				T.add(headerText(localize("g", "g")), col++, row);
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

				T.add(getText(String.valueOf(i + 1)), col++, row);
				if (CA == null)
					T.add(getText("A"), col++, row);
				else if(CA.getPriorityLevel()!=null){
					T.add(getText(CA.getPriorityLevel()), col++, row);
				}
				else {
					col++;
				}
					
				T.add(getText(cypher), col++, row);
				String Name = A.getFirstName() + " " + A.getMiddleName() + " " + A.getLastName();
				T.add(getText(Name), col++, row);
				T.add(getText(A.getSSN() != null ? A.getSSN() : ""), col++, row);
				T.add(getText(A.getLegalResidence() != null ? A.getLegalResidence() : ""), col++, row);
				T.add(getText(A.getResidence() != null ? A.getResidence() : ""), col++, row);
				T.add(getText(A.getPO() != null ? A.getPO() : ""), col++, row);
				T.add(getText(A.getResidencePhone() != null ? A.getResidencePhone() : ""), col++, row);
				T.add(getText(A.getMobilePhone() != null ? A.getMobilePhone() : ""), col++, row);
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
			T.add(getText(" "),1,lastrow);
			T.setHeight(lastrow,Edit.bottomBarThickness);
			T.add(getPDFLink(printImage,sGlobalStatus,iSubjectId),1,++row);
			*/
		}
		else {
			T.add(getHeader(localize("no_applications", "No applications in database")));
		}
		return T;
	}

	public PresentationObject makeSubjectStatisticsTable(IWContext iwc) {

		DataTable DT = getDataTable();
		DT.addTitle(localize("subject_info", "Subject Info"));
		DT.setTitlesHorizontal(true);
		int row = 1, col = 1;
		DT.add(getHeader(localize("subject", "Subject")), col++, row);
		DT.add(getHeader(localize("count", "Count")), col++, row);
		DT.add(getHeader(localize("status", "Status")), col++, row);

		DT.add(getHeader(localize("last_submission", "Last in")), col++, row);
		DT.add(getHeader(localize("first_submission", "First in")), col++, row);
		DT.add(getHeader(localize("last_changed", "Last change")), col++, row);
		DT.add(getHeader(localize("first_change", "First Change")), col++, row);

		col = 1;
		row++;
		try {
			List infos = com.idega.data.EntityFinder.getInstance().findAll(ApplicationSubjectInfo.class);
			DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT,iwc.getCurrentLocale());
			if (infos != null) {
				java.util.Iterator iter = infos.iterator();
				ApplicationSubjectInfo info;
				while (iter.hasNext()) {

					info = (ApplicationSubjectInfo) iter.next();
					Link subjLink = new Link(getText(info.getSubjectName()));
					subjLink.addParameter("app_subject_id", info.getSubjectId());
					subjLink.addParameter("global_status", info.getStatus());
					DT.add(subjLink, col++, row);
					DT.add(getText(String.valueOf(info.getNumber())), col++, row);
					DT.add(getText(getStatus(info.getStatus())), col++, row);
					DT.add(getText(df.format((Date) info.getLastSubmission())), col++, row);
					DT.add(getText(df.format((Date) info.getFirstSubmission())), col++, row);
					DT.add(getText(df.format((Date) info.getLastChange())), col++, row);
					DT.add(getText(df.format((Date) info.getFirstChange())), col++, row);
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

	public PresentationObject makeApplicationTable(int id, boolean bEdit, IWContext iwc) {
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
				Left.add(getViewApplicant(eApplicant, eCampusApplication), 1, 1);
				Left.add(getViewSpouse(spouse, eCampusApplication), 1, 2);
				Left.add(getViewChildren(children, eCampusApplication), 1, 3);

				Table Middle = new Table(1, 4);
				Middle.add(getViewApplication(eApplication), 1, 1);
				Middle.add(getViewApartment(eCampusApplication, L, iwc), 1, 2);
				Middle.add(getViewApartmentExtra(eCampusApplication, iwc), 1, 3);
				Middle.add(getOtherInfo(eCampusApplication, iwc, false), 1, 3);

				Table Right = new Table(1, 3);
				//Right.add(getRemoteControl(getResourceBundle()),1,1);
				Right.add(getSubjectControl(getResourceBundle(), eApplication), 1, 1);
				Right.add(getKnobs(getResourceBundle()), 1, 2);
				Right.add(getButtons(eApplication, eApplication.getStatus(), eCampusApplication.getPriorityLevel(), bEdit), 1, 3);

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

	public PresentationObject makeApplicationForm(int id, boolean bEdit, IWContext iwc) {
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
			Left.add(getFieldsApplicant(eApplicant, eCampusApplication), 1, 1);
			Left.add(getFieldsSpouse(spouse, eCampusApplication), 1, 2);
			Left.add(getFieldsChildren(children, eCampusApplication), 1, 3);

			Table Middle = new Table(1, 4);
			Middle.add(getViewApplication(eApplication), 1, 1);
			Middle.add(getFieldsApartment(eCampusApplication, L, iwc), 1, 2);
			Middle.add(getFieldsApartmentExtra(eCampusApplication, iwc), 1, 3);
			Middle.add(getOtherInfo(eCampusApplication, iwc, true), 1, 4);

			Table Right = new Table(1, 3);
			//Right.add(getRemoteControl(getResourceBundle()),1,1);
			Right.add(getSubjectControl(getResourceBundle(), eApplication), 1, 1);
			Right.add(getKnobs(getResourceBundle()), 1, 2);
			String status = eApplication != null ? eApplication.getStatus() : "";
			String pStatus = eCampusApplication != null ? eCampusApplication.getPriorityLevel() : "";
			Right.add(getButtons(eApplication, status, pStatus, bEdit), 1, 3);

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

	public PresentationObject getViewApplicant(Applicant eApplicant, CampusApplication eCampusApplication) {
		DataTable T = getDataTable();
		T.setWidth("100%");
		T.addTitle(localize("applicant", "Applicant"));
		int col = 1;
		int row = 1;
		T.add(getHeader(localize("name", "Name")), col, row++);
		T.add(getHeader(localize("ssn", "Socialnumber")), col, row++);
		T.add(getHeader(localize("legal_residence", "Legal Residence")), col, row++);
		T.add(getHeader(localize("residence", "Residence")), col, row++);
		T.add(getHeader(localize("po", "PO")), col, row++);
		T.add(getHeader(localize("phone", "Residence phone")), col, row++);
		T.add(getHeader(localize("mobile_phone", "Mobile phone")), col, row++);
		T.add(getHeader(localize("email", "Email")), col, row++);
		T.add(getHeader(localize("faculty", "Faculty")), col, row++);
		T.add(getHeader(localize("studytrack", "Study Track")), col, row++);
		T.add(getHeader(localize("study_begins", "Study begins")), col, row++);
		T.add(getHeader(localize("study_ends", "Study ends")), col, row++);
		//T.add(getHeader(localize("income","Income")),col,row++);

		col = 2;
		row = 1;
		T.add(getText(eApplicant.getFullName()), col, row++);
		T.add(getText(eApplicant.getSSN()), col, row++);
		T.add(getText(eApplicant.getLegalResidence()), col, row++);
		T.add(getText(eApplicant.getResidence()), col, row++);
		T.add(getText(eApplicant.getPO()), col, row++);
		T.add(getText(eApplicant.getResidencePhone()), col, row++);
		T.add(getText(eApplicant.getMobilePhone()), col, row++);
		String email = eCampusApplication.getEmail();
		try{
			javax.mail.internet.InternetAddress emailAddress = new javax.mail.internet.InternetAddress(email);
		}
		catch( Exception e){
			email = null;
		}
		if(email!=null){	
			T.add(new Link(getText(email), "mailto:" + email), col, row++);
		}
		else{
			Text noEmailText = new Text(localize("no_email","No email address"));
			noEmailText.setFontColor("#FF0000");
			noEmailText.setBold();
			T.add(noEmailText, col, row++);
		}
		T.add(getText(eCampusApplication.getFaculty()), col, row++);
		T.add(getText(eCampusApplication.getStudyTrack()), col, row++);
		String beginMonth = (eCampusApplication.getStudyBeginMonth().toString());
		String endMonth = (eCampusApplication.getStudyEndMonth().toString());
		T.add(getText(beginMonth + " " + eCampusApplication.getStudyBeginYear().intValue()), col, row++);
		T.add(getText(endMonth + " " + eCampusApplication.getStudyEndYear().intValue()), col, row++);
		// T.add(getText(eCampusApplication.getIncome().intValue()),col,row);

		return T;
	}

	public PresentationObject getFieldsApplicant(Applicant eApplicant, CampusApplication eCampusApplication) {
		int year = IWTimestamp.RightNow().getYear();
		DataTable T = getDataTable();
		T.setWidth("100%");
		T.addTitle(localize("applicant", "Applicant"));

		int col = 1;
		int row = 1;
		T.add(getHeader(localize("name", "Name")), col, row++);
		T.add(getHeader(localize("ssn", "Socialnumber")), col, row++);
		T.add(getHeader(localize("legal_residence", "Legal Residence")), col, row++);
		T.add(getHeader(localize("residence", "Residence")), col, row++);
		T.add(getHeader(localize("po", "PO")), col, row++);
		T.add(getHeader(localize("phone", "Residence phone")), col, row++);
		T.add(getHeader(localize("mobile_phone", "Mobile phone")), col, row++);
		T.add(getHeader(localize("email", "Email")), col, row++);
		T.add(getHeader(localize("faculty", "Faculty")), col, row++);
		T.add(getHeader(localize("studytrack", "Study Track")), col, row++);
		T.add(getHeader(localize("study_begins", "Study begins")), col, row++);
		T.add(getHeader(localize("study_ends", "Study ends")), col, row++);
		//T.add(getHeader(localize("income","Income")),col,row++);

		col = 2;
		row = 1;

		TextInput tiFullName = getTextInput("ti_full");
		
		TextInput tiSsn = getTextInput("ti_ssn");
		TextInput tiLegRes = getTextInput("ti_legres");
		
		TextInput tiRes =getTextInput("ti_res");
		
		TextInput tiPo = getTextInput("ti_po");
		
		TextInput tiResPho = getTextInput("ti_respho");
		
		TextInput tiMobPho = getTextInput("ti_mobpho");
		
		TextInput tiEmail = getTextInput("ti_email");
		String needEmail = localize("warning_provide_email","No email address is supplied");
		tiEmail.setAsEmail(needEmail);
		tiEmail.setAsNotEmpty(needEmail);
	
		TextInput tiFac = getTextInput("ti_facult");
		
		TextInput tiTrack = getTextInput("ti_track");
		
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

	public PresentationObject getViewSpouse(Applicant spouse, CampusApplication eCampusApplication) {
		int year = IWTimestamp.RightNow().getYear();
		DataTable T = getDataTable();
		T.setWidth("100%");
		T.addTitle(localize("spouse", "Spouse"));
		int col = 1;
		int row = 1;

		if (spouse != null) {
			T.add(getHeader(localize("name", "Name")), col, row++);
			T.add(getHeader(localize("ssn", "Socialnumber")), col, row++);
			T.add(getHeader(localize("school", "School")), col, row++);
			T.add(getHeader(localize("studytrack", "Study Track")), col, row++);
			T.add(getHeader(localize("study_begins", "Study begins")), col, row++);
			T.add(getHeader(localize("study_ends", "Study ends")), col, row++);
			//T.add(getHeader(localize("income","Income")),col,row++);
			col = 2;
			row = 1;

			T.add(getText(spouse.getName()), col, row++);
			T.add(getText(spouse.getSSN()), col, row++);
			T.add(getText(eCampusApplication.getSpouseSchool()), col, row++);
			T.add(getText(eCampusApplication.getSpouseStudyTrack()), col, row++);
			String beginMonth = (eCampusApplication.getSpouseStudyBeginMonth().toString());
			String endMonth = (eCampusApplication.getSpouseStudyEndMonth().toString());
			T.add(getText(beginMonth + " " + eCampusApplication.getSpouseStudyBeginYear().intValue()), col, row++);
			T.add(getText(endMonth + " " + eCampusApplication.getSpouseStudyEndYear().intValue()), col, row++);
			//T.add(getText(eCampusApplication.getSpouseIncome().intValue()),col,row);

		}

		return T;
	}

	public PresentationObject getFieldsSpouse(Applicant spouse, CampusApplication eCampusApplication) {
		int year = IWTimestamp.RightNow().getYear();
		DataTable T = getDataTable();
		T.setWidth("100%");
		T.addTitle(localize("spouse", "Spouse"));
		int col = 1;
		int row = 1;
		T.add(getHeader(localize("name", "Name")), col, row++);
		T.add(getHeader(localize("ssn", "Socialnumber")), col, row++);
		T.add(getHeader(localize("school", "School")), col, row++);
		T.add(getHeader(localize("studytrack", "Study Track")), col, row++);
		T.add(getHeader(localize("study_begins", "Study begins")), col, row++);
		T.add(getHeader(localize("study_ends", "Study ends")), col, row++);
		//T.add(getHeader(localize("income","Income")),col,row++);
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
			if(eCampusApplication.getSpouseSchool()!=null)
			tiSpSchl.setContent(eCampusApplication.getSpouseSchool());
			if(eCampusApplication.getSpouseStudyTrack()!=null)
			tiSpStTr.setContent(eCampusApplication.getSpouseStudyTrack());
			//tiSPIncome.setContent(eCampusApplication.getSpouseIncome().toString());
			
			beginMonth = eCampusApplication.getSpouseStudyBeginMonth().toString();
			endMonth = eCampusApplication.getSpouseStudyEndMonth().toString();
			beginYear = eCampusApplication.getSpouseStudyBeginYear().toString();
			endYear = eCampusApplication.getSpouseStudyEndYear().toString();
			T.add(new HiddenInput("ti_sp_id", String.valueOf(spouse.getID())));
		}

	
		//Edit.setStyle(tiSPIncome);

		T.add(tiSpName, col, row++);
		T.add(tiSpSsn, col, row++);
		T.add(tiSpSchl, col, row++);
		T.add(tiSpStTr, col, row++);

		DropdownMenu drBM = intDrop("dr_sp_bm", beginMonth, 1, 12);
		DropdownMenu drEM = intDrop("dr_sp_em", endMonth, 1, 12);
		DropdownMenu drBY = intDrop("dr_sp_by", beginYear, year - 10, year + 10);
		DropdownMenu drEY = intDrop("dr_sp_ey", endYear, year - 10, year + 10);
		
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

	public PresentationObject getViewChildren(Vector children, CampusApplication eCampusApplication) {
		DataTable T = getDataTable();
		T.setWidth("100%");
		T.addTitle(localize("children", "Children"));
		T.setUseTitles(false);
		int col = 1;
		int row = 1;

		if (children != null) {
			Applicant child;
			for (int i = 0; i < children.size(); i++) {
				child = (Applicant) children.get(i);
				T.add(getText(child.getName()), 1, row);
				T.add(getText(child.getSSN()), 2, row++);
			}
		}
		return T;
	}

	public PresentationObject getFieldsChildren(Vector children, CampusApplication eCampusApplication) {
		DataTable T = getDataTable();
		T.setWidth("100%");
		T.addTitle(localize("children", "Children"));
		T.setUseTitles(false);
		int col = 1;
		int row = 1;
		int count = 4;
		int childcount = children != null ? children.size() : 0;
		count = Math.max(count, childcount);
		for (int i = 0; i < count; i++) {
			TextInput childName = getTextInput("child_name" + i);
			TextInput childBirth = getTextInput("child_birth" + i);
			childName.setLength(30);
			childBirth.setLength(10);
			childBirth.setMaxlength(10);
			
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

	public PresentationObject getViewApartment(CampusApplication eCampusApplication, List lApplied, IWContext iwc) {
		DataTable T = getDataTable();
		T.setWidth("100%");
		T.addTitle(localize("applied", "Applied"));
		int col = 1;
		int row = 1;
		if (lApplied != null) {
			int len = lApplied.size();
			for (int i = 0; i < len; i++) {
				Applied A = (Applied) lApplied.get(i);
				T.add(getText(String.valueOf(i + 1)), 1, row);
				T.add(getText((BuildingCacher.getApartmentType(A.getApartmentTypeId().intValue()).getName())), 2, row++);
			}
		}
		return T;
	}

	public PresentationObject getViewApartmentExtra(CampusApplication eCampusApplication, IWContext iwc) {
		DataTable T = getDataTable();
		T.setWidth("100%");
		T.addTitle(localize("requests", "Requests"));
		int col = 1;
		int row = 1;

		T.add(getHeader(localize("housingfrom", "Housing from")), col, row++);
		T.add(getHeader(localize("wantfurniture", "Wants furniture")), col, row++);
		T.add(getHeader(localize("onwaitinglist", "On waitinglist")), col, row++);
		col = 2;
		row = 1;
		IWTimestamp iT = new IWTimestamp(eCampusApplication.getHousingFrom());
		T.add(getHeader(iT.getLocaleDate(iwc)), col, row++);
		if (eCampusApplication.getWantFurniture())
			T.add(getHeader("X"), col, row++);
		if (eCampusApplication.getOnWaitinglist())
			T.add(getHeader("X"), col, row++);
		return T;
	}

	public PresentationObject getOtherInfo(CampusApplication eCampusApplication, IWContext iwc, boolean editable) {
		DataTable T = getDataTable();
		T.setWidth("100%");
		T.addTitle(localize("otherinfo", "Other info"));
		int col = 1;
		int row = 1;
		T.add(getHeader(localize("comment", "Comment")), col, row);

		String comment = null;

		if (eCampusApplication != null) {
			comment = eCampusApplication.getOtherInfo();
		}

		col = 2;
		row = 1;
		TextArea commentArea = getTextArea("ap_comment", comment,35,5);
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
		
		T.add(commentArea, col, row);

		return T;
	}

	public DropdownMenu drpTypes(Vector v, String name, String selected, boolean firstEmpty) {
		DropdownMenu drpTypes = new DropdownMenu(name);
		
		if (firstEmpty)
			drpTypes.addMenuElementFirst("-1", "-");
		for (int i = 0; i < v.size(); i++) {
			ApartmentTypeComplexHelper eAprtType = (ApartmentTypeComplexHelper) v.elementAt(i);
			drpTypes.addMenuElement(eAprtType.getKey(), eAprtType.getName());
		}
		drpTypes.setSelectedElement(selected);
		return drpTypes;
	}

	public PresentationObject getFieldsApartment(CampusApplication eCampusApplication, List lApplied, IWContext iwc) {
		DataTable T = getDataTable();
		T.setWidth("100%");
		T.addTitle(localize("applied", "Applied"));
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
		
		T.add(getHeader(String.valueOf(1)), 1, row);
		T.add(drpOne, 2, row++);
		T.add(getHeader(String.valueOf(2)), 1, row);
		T.add(drpTwo, 2, row++);
		T.add(getHeader(String.valueOf(3)), 1, row);
		T.add(drpThree, 2, row++);

		return T;
	}

	public PresentationObject getFieldsApartmentExtra(CampusApplication eCampusApplication, IWContext iwc) {
		DataTable T = getDataTable();
		T.setWidth("100%");
		T.addTitle(localize("requests", "Requests"));
		int col = 1;
		int row = 1;
		T.add(getHeader(localize("housingfrom", "Housing from")), col, row++);
		T.add(getHeader(localize("wantfurniture", "Wants furniture")), col, row++);
		T.add(getHeader(localize("onwaitinglist", "On waitinglist")), col, row++);

		IWTimestamp iT = new IWTimestamp();
		if (eCampusApplication != null) {
			iT = new IWTimestamp(eCampusApplication.getHousingFrom());
		}
		col = 2;
		row = 1;
		DateInput diRentFrom = new DateInput("ap_rentfrom", true);
		diRentFrom.setDate(iT.getSQLDate());
		
		T.add(diRentFrom, col, row++);
		CheckBox chkFurni = getCheckBox("ap_furni", "true");
		CheckBox chkWait = getCheckBox("ap_wait", "true");

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
		DataTable T = getDataTable();
		T.setWidth("100%");
		T.addTitle(localize("application", "Application"));

		int row = 1;
		int col = 1;

		T.add(getHeader(localize("submitted", "Submitted")), col, row++);
		T.add(getHeader(localize("changed", "Status change")), col, row++);
		T.add(getHeader(localize("status", "Status")), col, row++);
		col++;
		row = 1;
		if (eApplication != null) {
			T.add(getText(eApplication.getSubmitted().toString()), col, row++);
			T.add(getText(eApplication.getStatusChanged().toString()), col, row++);
			T.add(getText(getStatus(eApplication.getStatus())), col, row++);
		}
		return T;
	}

	private PresentationObject getRemoteControl(IWResourceBundle iwrb) {
		DataTable T = getDataTable();
		T.setWidth("100%");
		T.addTitle(localize("extra", "Extra"));
		int col = 1;
		int row = 1;
		T.add(getHeader(localize("tax_return", "Tax return")), col, row++);
		T.add(getHeader(localize("study_progress", "Study progress")), col, row++);
		T.add(getHeader(localize("choice1", "Choice 1")), col, row++);
		T.add(getHeader(localize("choice2", "Choice 2")), col, row++);
		col++;
		row = 1;
		TextInput units = getTextInput("unit");
		units.setLength(1);
		
		CheckBox choice1 = getCheckBox("choice1","true");
		CheckBox choice2 = getCheckBox("choice2","true");
		CheckBox choice3 = getCheckBox("choice3","true");
		
		T.add(choice1, col, row++);
		T.add(units, col, row++);
		T.add(choice2, col, row++);
		T.add(choice3, col, row++);

		return T;
	}

	private PresentationObject getSubjectControl(IWResourceBundle iwrb, Application app) {
		DataTable T = getDataTable();
		T.setWidth("100%");
		T.addTitle(localize("subject", "Subject"));
		int col = 1;
		int row = 1;
		T.add(getHeader(localize("current_subject", "Current Subject")), col, row++);

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

	private PresentationObject getButtons(Application eApplication, String sStatus, String sPriority, boolean bEdit) {
		DataTable T = getDataTable();
		T.setWidth("100%");
		T.addTitle(localize("control", "Control"));
		int row = 1;
		int col = 1;
		if (eApplication != null) {
			
			T.add(headerText(localize("priority","Priority")),col,row++);
			Table letterTable = new Table(6,2);
			String[] priorities = {"A","B","C","D","E","T"};
			for (int i = 0; i < priorities.length; i++) {
				RadioButton rb = new RadioButton("priority_drop",priorities[i]);
				if(priorities[i].equalsIgnoreCase(sPriority)){
					rb.setSelected();
				}
				letterTable.add(headerText(priorities[i]),i+1,1);
				letterTable.add(rb,i+1,2);
			}
				
			T.add(letterTable,col,row++);
			
			//DropdownMenu status = statusDrop("status_drop", sStatus);
			//status.setToSubmit();
			//Edit.setStyle(status);
			//T.add(status, col, row);

			//DropdownMenu priority = priorityDrop("priority_drop", sPriority);
			//Edit.setStyle(priority);
			//T.add(priority, col, row);
		}
		if (bEdit) {
			SubmitButton view = new SubmitButton(getResourceBundle().getLocalizedImageButton("view", "View"), "viewer", "View");
			T.add(view, col, row++);
		}
		else {
			SubmitButton edit = new SubmitButton(getResourceBundle().getLocalizedImageButton("edit", "Edit"), "editor", "Edit");
			T.add(edit, col, row++);
		}
		SubmitButton save = new SubmitButton(getResourceBundle().getLocalizedImageButton("save", "Save"), "save", "Save");
		T.add(save, col, row++);
		return T;
	}

	private PresentationObject getKnobs(IWResourceBundle iwrb) {
		Table T = new Table(5, 1);
		T.setAlignment("center");

		if (iterator != null) {
			if (iterator.hasPrevious()) {
				Link lLast = new Link(getResourceBundle().getImage("back.gif"));
				lLast.addParameter(sView, "-2");
				T.add(lLast, 1, 1);
			}
			if (iterator.hasNext()) {
				Link lNext = new Link(getResourceBundle().getImage("next.gif"));
				lNext.addParameter(sView, "-4");
				T.add(lNext, 5, 1);
			}
		}
		Link lList = new Link(getBundle().getImage("list.gif"));
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
		DropdownMenu sizeMenu = sizeDrop("global_size",iGlobalSize);
		SubmitButton New = (SubmitButton) getSubmitButton("new_app", "true","New","new");
		SubmitButton Info = (SubmitButton) getSubmitButton("subj_info", "true","Info","info");
		//SubmitButton New = new SubmitButton("new","New");
		//    SubmitButton New2 = new SubmitButton("new2","New transfer");
		drp.setToSubmit();
		status.setToSubmit();
		order.setToSubmit();
		sizeMenu.setToSubmit();
		
		
		DataTable T = getDataTable();
		T.addTitle(localize("filter", "Filter"));
		T.setTitlesHorizontal(true);
		int col = 1;
		int row = 1;
		T.add(getHeader(localize("subject", "Subject")), col++, row);
		T.add(getHeader(localize("status", "Status")), col++, row);
		T.add(getHeader(localize("order", "Order")), col++, row);
		T.add(getHeader(localize("viewsize", "View size")), col++, row);
		row++;
		col = 1;
		T.add(drp, col++, row);
		
		T.add(status, col++, row);
		T.add(order, col++, row);
		T.add(sizeMenu, col++, row);
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
		drp.addMenuElement(-1, localize("subject", "Subject"));
		drp.addMenuElement(-99, localize("all_subject", "All"));
		if (L != null) {
			ApplicationSubject AS;
			int len = L.size();
			for (int i = 0; i < len; i++) {
				AS = (ApplicationSubject) L.get(i);
				drp.addMenuElement(AS.getID(), AS.getName());
			}
		
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
				r = localize("submitted", "Submitted");
				break;
			case 'A' :
				r = localize("approved", "Approved");
				break;
			case 'R' :
				r = localize("rejected", "Rejected");
				break;
			case 'C' :
				r = localize("contracted", "Contract");
				break;
			case 'G' :
				r = localize("garbage", "Garbage");
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
	
	private DropdownMenu sizeDrop(String name, int  selected) {
			DropdownMenu drp = new DropdownMenu(name);
			drp.addMenuElement("10");
			drp.addMenuElement("20");
			drp.addMenuElement("50");
			drp.addMenuElement("100");
			drp.addMenuElement("500");
			drp.addMenuElement("-1","All");
			drp.setSelectedElement(selected);
			return drp;
		}


	private DropdownMenu orderDrop(String name, String selected) {
		DropdownMenu drp = new DropdownMenu(name);
		Applicant A = ((com.idega.block.application.data.ApplicantHome) com.idega.data.IDOLookup.getHomeLegacy(Applicant.class)).createLegacy();

		drp.addMenuElement("-1", localize("submitted", "Submitted"));
		drp.addMenuElement(com.idega.block.application.data.ApplicantBMPBean.getFullnameOrderValue(), localize("name", "Name"));
		drp.addMenuElement(com.idega.block.application.data.ApplicantBMPBean.getSSNColumnName(), localize("ssn", "Socialnumber"));
		drp.addMenuElement(com.idega.block.application.data.ApplicantBMPBean.getLegalResidenceColumnName(), localize("legal_residence", "Legal Residence"));
		drp.addMenuElement(com.idega.block.application.data.ApplicantBMPBean.getResidenceColumnName(), localize("residence", "Residence"));
		drp.addMenuElement(com.idega.block.application.data.ApplicantBMPBean.getResidenceColumnName(), localize("phone", "Residence phone"));
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
		return getHeader(text);
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
