/*
 * $Id: CampusApprover.java,v 1.60 2004/07/30 13:55:28 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.application.presentation;

import is.idega.idegaweb.campus.block.application.business.ApartmentInfo;
import is.idega.idegaweb.campus.block.application.business.ApplicantInfo;
import is.idega.idegaweb.campus.block.application.business.ApplicationService;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationWriter;
import is.idega.idegaweb.campus.block.application.business.ChildInfo;
import is.idega.idegaweb.campus.block.application.business.ReferenceNumberFinder;
import is.idega.idegaweb.campus.block.application.business.SpouseInfo;
import is.idega.idegaweb.campus.block.application.data.Applied;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import is.idega.idegaweb.campus.block.application.data.CampusApplicationHome;
import is.idega.idegaweb.campus.block.application.data.Priority;
import is.idega.idegaweb.campus.data.ApplicationSubjectInfo;
import is.idega.idegaweb.campus.data.ApplicationSubjectInfoHome;
import is.idega.idegaweb.campus.presentation.CampusBlock;
import is.idega.idegaweb.campus.presentation.Edit;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.Application;
import com.idega.block.application.data.ApplicationSubject;
import com.idega.block.application.data.Status;
import com.idega.block.building.business.ApartmentTypeComplexHelper;
import com.idega.block.building.data.ApartmentTypeHome;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWMainApplication;
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
public class CampusApprover extends CampusBlock {
	private static final String APP_SUBJECT_ID = "app_subject_id";
	private static final String PRM_PRIORITY = "priority_drop";
	private static final String PRM_STATUS = "status_drop";
	private static final String ACT_NEW_APPLICATION = "new_app";
	private static final String ACT_SAVE = "save";
	private static final String ACT_VIEWER = "viewer";
	private static final String ACT_EDITOR = "editor";
	private static final String PRM_CAM_APPLICATION_ID = "application_id";
	private static final String ACT_TRASH_APPLICATION = "cam_app_trash";
	
	private int iSubjectId = -1,iGlobalSize = 50,applicationIndex = 0;
	private String sGlobalStatus = "S", sGlobalOrder = null;
	private ListIterator iterator = null;
	private LinkedList linkedlist = null;
	private static final String ACT_VIEW = "app_view", ACT_EDIT = "app_edit";
	protected boolean isAdmin = false;
	private Collection listOfSubjects = null;
	private boolean infoCheck = true;
	boolean bEdit = false;
	private Integer applicationID =new Integer(-1);
	private static final String PRM_INDEX = "app_idx";
	private ApplicationService applicationService = null;
	

	/*
	Bl?r litur ? topp # 27324B
	Hv?tur litur fyrir ne?an ?a? # FFFFFF
	Lj?sbl?r litur ? t?flu # ECEEF0
	Auka litur ?rl?ti? dekkri (? lagi a? nota l?ka) # CBCFD3
	*/

	public String getLocalizedNameKey() {
		return "approver";
	}

	public String getLocalizedNameValue() {
		return "Approver";
	}

	protected void control(IWContext iwc) throws RemoteException {
		
		//debugParameters(iwc);
		init(iwc);

		if (isAdmin) {
			if (iwc.isParameterSet(ACT_TRASH_APPLICATION)) {
				int trashid = Integer.parseInt(iwc.getParameter(ACT_TRASH_APPLICATION));
				trashApplication(trashid);
			}
			if (iwc.isParameterSet(ACT_VIEW) ) {
				applicationID = Integer.valueOf(iwc.getParameter(ACT_VIEW));
				add(makeApplicationTable(iwc));
			}
			else if (iwc.isParameterSet(PRM_CAM_APPLICATION_ID)) {
				applicationID = Integer.valueOf(iwc.getParameter(PRM_CAM_APPLICATION_ID));
				
				if (iwc.isParameterSet(ACT_VIEWER)) {
					bEdit = false;
				}
				else if (iwc.isParameterSet(ACT_EDITOR)) {
					bEdit = true;
				}
				

				if (iwc.isParameterSet(ACT_SAVE)) {
					if (bEdit)
						updateWholeApplication(iwc);
					if (iwc.isParameterSet(PRM_PRIORITY)) {
						updatePriorityLevel(iwc);
					}
					if (iwc.isParameterSet(PRM_STATUS))
						updateApplicationStatus(iwc);
				}
				
				if (bEdit) {
					add(makeApplicationForm(iwc));
				}
				else {
					add(makeApplicationTable(iwc));
				}
			}
			else if (iwc.isParameterSet(ACT_NEW_APPLICATION)) {
				add(makeApplicationForm(iwc));
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
	
	private void init(IWContext iwc){
		try {
			applicationService = getApplicationService(iwc);
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			listOfSubjects = applicationService.getSubjectHome().findAll();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		if (iwc.getSessionAttribute("iterator") != null) {
			//iterator = (ListIterator) iwc.getSessionAttribute("iterator");
		}
		if (iwc.getParameter(APP_SUBJECT_ID) != null) {
			infoCheck = false;
			this.iSubjectId = Integer.parseInt(iwc.getParameter(APP_SUBJECT_ID));
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
		if(iwc.isParameterSet(PRM_INDEX))
			this.applicationIndex = Integer.parseInt(iwc.getParameter(PRM_INDEX));
		
	}

	public PresentationObject makeLinkTable(int menuNr) {
		Table LinkTable = new Table(6, 1);

		return LinkTable;
	}

	private void updateApplicationStatus(IWContext iwc) throws RemoteException {
		String status = iwc.getParameter(PRM_STATUS);
		applicationService.storeApplicationStatus( applicationID, status);
	}


	private void updatePriorityLevel(IWContext iwc)throws RemoteException {
		//int id = Integer.parseInt(iwc.getParameter("application_id"));
		String level = iwc.getParameter(PRM_PRIORITY);
		applicationService.storePriorityLevel(applicationID,level);
		
	}

	

	private void trashApplication(int id) {
		//int id = Integer.parseInt(iwc.getParameter("application_id"));

		
	}

	private void updateWholeApplication(IWContext iwc) {
		
		ApplicantInfo aInfo = getApplicantInfo(iwc);
		ApartmentInfo aprtInfo = getApartmentInfo(iwc);
		SpouseInfo spouseInfo = getSpouseInfo(iwc);
		List childInfo = getChildrenInfo(iwc);
		String newStatus = iwc.getParameter(PRM_STATUS);
		
		try {
			CampusApplication app = applicationService.storeWholeApplication(applicationID,new Integer(iSubjectId),aInfo,aprtInfo,spouseInfo,childInfo);
			applicationID =  ((Integer)app.getPrimaryKey());
		}
		catch (EJBException e) {
			applicationID = null;
			e.printStackTrace();
		}
		
	}
		
	public PresentationObject makeApplicantTable(IWContext iwc) {

		DataTable T = getDataTable();
		T.setWidth(Table.HUNDRED_PERCENT);
		
		T.setTitlesHorizontal(true);

		int row = 1;
		int col = 1;
		Image printImage = getBundle().getImage("print.gif");
		Image viewImage = getBundle().getImage("view.gif");
		Image trashImage = getBundle().getImage("trashcan.gif");
		T.add(getHeader(localize("nr", "Nr")), col++, row);
		T.add(getHeader(localize("prior", "Pr")), col++, row);
		T.add(getHeader(localize("refnum", "Ref. num")), col++, row);
		T.add(getHeader(localize("name", "Name")), col++, row);
		T.add(getHeader(localize("ssn", "Socialnumber")), col++, row);
		T.add(getHeader(localize("legal_residence", "Legal Residence")), col++, row);
		T.add(getHeader(localize("residence", "Residence")), col++, row);
		T.add(getHeader(localize("po", "PO")), col++, row);
		T.add(getHeader(localize("phone", "Residence phone")), col++, row);
		T.add(getHeader(localize("mobile_phone", "Mobile phone")), col++, row);
		
		try {
			//Collection L = ApplicationFinder.listOfApplicationHoldersInSubject(iSubjectId, sGlobalStatus, sGlobalOrder);
			if("-1".equals(sGlobalOrder))
				sGlobalOrder = null;
			CampusApplicationHome cappHome = applicationService.getCampusApplicationHome();
			int count = cappHome.getCountBySubjectAndStatus(new Integer(iSubjectId),sGlobalStatus);
			Collection L = cappHome.findBySubjectAndStatus(new Integer(iSubjectId),sGlobalStatus,sGlobalOrder,this.iGlobalSize,-1);

			if (L != null) {
				Iterator iterator = L.iterator();
				iwc.setSessionAttribute("iterator", iterator);
				int len = L.size();
				
				if(iGlobalSize>0 && iGlobalSize<=len){
					len = iGlobalSize;
				}
				T.addTitle(localize("applicants", "Applicants")+" "+localize("showing","showing")
				+" "+len+" "+localize("of","of")+" "+count);

				boolean showcan = false;
				if (sGlobalStatus.equals(com.idega.block.application.data.ApplicationBMPBean.STATUS_REJECTED)) {
					T.add(getHeader(localize("g", "g")), col++, row);
					showcan = true;
				}

				int lastcol = 1;
				int i = 0;
				for (Iterator iter = L.iterator(); iter.hasNext();) {
					//ApplicationHolder AH = (ApplicationHolder) iter.next();
					CampusApplication campusApplication = (CampusApplication) iter.next();
					row = i+2;
					col = 1;
					
					Application a = campusApplication.getApplication();//AH.getApplication();
					Applicant A = a.getApplicant();
				
					String cypher = null;
					if (a != null && ((Integer)a.getPrimaryKey()).intValue() != -1)
						cypher = ReferenceNumberFinder.getInstance(iwc).lookup(((Integer)a.getPrimaryKey()).intValue());

					T.add(getText(String.valueOf(i + 1)), col++, row);
					if (campusApplication.getPriorityLevel() != null)
						T.add(getText(campusApplication.getPriorityLevel()), col++, row);
					else
						col++;

						
					T.add(getText(cypher), col++, row);
					String Name = A.getFirstName() + " " + A.getMiddleName() + " " + A.getLastName();
					T.add(getText(Name), col++, row);
					T.add(getText(A.getSSN() != null ? A.getSSN() : ""), col++, row);
					T.add(getText(A.getLegalResidence() != null ? A.getLegalResidence() : ""), col++, row);
					T.add(getText(A.getResidence() != null ? A.getResidence() : ""), col++, row);
					T.add(getText(A.getPO() != null ? A.getPO() : ""), col++, row);
					T.add(getText(A.getResidencePhone() != null ? A.getResidencePhone() : ""), col++, row);
					T.add(getText(A.getMobilePhone() != null ? A.getMobilePhone() : ""), col++, row);
					T.add((getPDFLink(iwc,printImage, ( (Integer) campusApplication.getPrimaryKey() ).intValue())), col++, row);
					T.add(getCampusApplicationLink(viewImage, ((Integer)campusApplication.getPrimaryKey()),i), col++, row);
					T.add(getTrashLink(trashImage, ((Integer)campusApplication.getPrimaryKey())), col, row);
					if (lastcol < col)
						lastcol = col;
					
					i++;
				}

			}
			else {
				T.add(getHeader(localize("no_applications", "No applications in database")));
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (EJBException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (IDORelationshipException e) {
			e.printStackTrace();
		} catch (IDOException e) {
			e.printStackTrace();
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

			Collection infos = ((ApplicationSubjectInfoHome)IDOLookup.getHome(ApplicationSubjectInfo.class)).findAllNonExpired(IWTimestamp.RightNow().getDate());
			//List infos = com.idega.data.EntityFinder.getInstance().findAll(ApplicationSubjectInfo.class);
			DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT,iwc.getCurrentLocale());

			if (infos != null) {
				java.util.Iterator iter = infos.iterator();
				ApplicationSubjectInfo info;
				while (iter.hasNext()) {

					info = (ApplicationSubjectInfo) iter.next();
					Link subjLink = new Link(getText(info.getSubjectName()));
					subjLink.addParameter(APP_SUBJECT_ID, info.getSubjectId());
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

	public PresentationObject makeApplicationTable(  IWContext iwc) {
		Form theForm = new Form();
		theForm.add(new HiddenInput(PRM_CAM_APPLICATION_ID, String.valueOf(applicationID)));
		try {
			CampusApplication eCampusApplication = null;
			Application eApplication = null;
			Applicant eApplicant = null;
			/*
			if (applicationID!=null && applicationID.intValue() < -1 && iterator != null) {
				ApplicationHolder AS = null;
				if (applicationID.intValue() == -2 && iterator.hasPrevious()) {
					AS = (ApplicationHolder) iterator.previous();
				}
				else if (applicationID.intValue() == -4 && iterator.hasNext()) {
					AS = (ApplicationHolder) iterator.next();
				}
				if (AS != null) {
					eApplication = AS.getApplication();
					eApplicant = AS.getApplicant();
					applicationID = ((Integer)eApplication.getPrimaryKey());
				}
			}
			else {*/
				if(applicationID !=null && applicationID.intValue()>0){
					eCampusApplication = applicationService.getCampusApplicationHome().findByPrimaryKey(applicationID);
					eApplication = eCampusApplication.getApplication();
					eApplicant = eApplication.getApplicant();
				}
			//}

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
				
				if(eCampusApplication!=null){
					Collection L  = applicationService.getAppliedHome().findByApplicationID((Integer)eCampusApplication.getPrimaryKey());
				
	
				int border = 0;

				Table OuterFrame = new Table(3, 2);
				OuterFrame.setCellpadding(2);
				OuterFrame.setCellspacing(0);
				OuterFrame.setBorder(border);
				OuterFrame.setRowVerticalAlignment(2, Table.VERTICAL_ALIGN_TOP);
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
				//Right.add(getRemoteControl(iwrb),1,1);
				Right.add(getSubjectControl( eApplication), 1, 1);
				Right.add(getKnobs(), 1, 2);
				Right.add(
					getButtons(
						eApplication,
						eApplication.getStatus(),
						eCampusApplication.getPriorityLevel(),
						bEdit),
					1,
					3);

				OuterFrame.add(Left, 1, 2);
				OuterFrame.add(Middle, 2, 2);
				OuterFrame.add(Right, 3, 2);
				OuterFrame.mergeCells(1,1,3,1);
				OuterFrame.add(getPreviousAndNextApplicationsLinks(iwc,eCampusApplication),1,1);

				theForm.add(OuterFrame);
					}
						
				}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return theForm;
	}
	
	public PresentationObject getPreviousAndNextApplicationsLinks(IWContext iwc,CampusApplication application){
		Table T = new Table(4,1);
		T.setWidth(Table.HUNDRED_PERCENT);
		T.setWidth(1,Table.HUNDRED_PERCENT);
		try {
			Collection L = applicationService.getCampusApplicationHome().findBySubjectAndStatus(new Integer(iSubjectId),sGlobalStatus,sGlobalOrder,3,this.applicationIndex-1);
			int idx = 1;
			boolean next = false,prevAdded = false;
			boolean prev = false,nextAdded = false;;
			boolean currentEntered = false;
			for (Iterator iter = L.iterator(); iter.hasNext();) {
				CampusApplication app = (CampusApplication) iter.next();
				Integer ID = (Integer)app.getPrimaryKey();
				if(!(this.applicationID.intValue() == ID.intValue())){
					
					switch (idx) {
					case 1:
						prev = true;
						next = false;
						break;
					case 2: 
						if(currentEntered){
							prev = false;
							next = true;
						}
						else{
							prev = true;
							next = false;
						}
							
						break;
					case 3:
						prev = false;
						next = true;
						break;
					}
					
					if(prev && !prevAdded){
						Link prevLink = getCampusApplicationLink(getHeader(localize("previous_application","Previous")),ID,this.applicationIndex-1);
						T.add(prevLink,2,1);
						prevAdded = true;
					}
					else if(next && !nextAdded){
						Link nextLink = getCampusApplicationLink(getHeader(localize("next_application","Next")),ID,this.applicationIndex+1);
						T.add(nextLink,4,1);
						nextAdded = true;
					}
					
					
				}
				else{
					currentEntered = true;
				}
				
				
				
				idx++;
			}
			Link listLink = new Link(getHeader(localize("view_list","List")));
			listLink.addParameter(APP_SUBJECT_ID,this.iSubjectId);
			T.add(listLink,3,1);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
	
		return T;
	}

	public PresentationObject makeApplicationForm( IWContext iwc) {
		Form theForm = new Form();
		theForm.add(new HiddenInput(PRM_CAM_APPLICATION_ID, String.valueOf(applicationID)));
		if(!iwc.isParameterSet(ACT_VIEWER))
			theForm.add(new HiddenInput(ACT_EDITOR, "true"));
		try {
			//CampusApplication A = null;
			CampusApplication eCampusApplication = null;
			Application eApplication = null;
			Applicant spouse = null;
			Vector children = null;
			Applicant eApplicant = null;
			/*if ( applicationID!=null && applicationID.intValue() < -1 && iterator != null) {
				ApplicationHolder AS = null;
				if (applicationID.intValue() == -2 && iterator.hasPrevious()) {
					AS = (ApplicationHolder) iterator.previous();
				}
				else if (applicationID.intValue() == -4 && iterator.hasNext()) {
					AS = (ApplicationHolder) iterator.next();
				}
				if (AS != null) {
					eApplication = AS.getApplication();
					eApplicant = AS.getApplicant();
					applicationID = ((Integer)eApplication.getPrimaryKey());
				}
			}
			else {
				*/
				if (applicationID.intValue() > 0) {
					eCampusApplication = applicationService.getCampusApplicationHome().findByPrimaryKey(applicationID);
					eApplication = eCampusApplication.getApplication();
					eApplicant = eApplication.getApplicant();
				}
			//}

			
			Collection L = null;
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
				
				
				if(eCampusApplication!=null)
					L = applicationService.getAppliedHome().findByApplicationID((Integer)eCampusApplication.getPrimaryKey());
				
			}

			int border = 0;

			Table OuterFrame = new Table(3, 2);
			OuterFrame.setCellpadding(2);
			OuterFrame.setCellspacing(0);
			OuterFrame.setBorder(border);
			OuterFrame.setRowVerticalAlignment(2, "top");
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
			//Right.add(getRemoteControl(iwrb),1,1);
			Right.add(getSubjectControl( eApplication), 1, 1);
			Right.add(getKnobs(), 1, 2);
			String status = eApplication != null ? eApplication.getStatus() : "";
			String pStatus = eCampusApplication != null ? eCampusApplication.getPriorityLevel() : "";
			Right.add(getButtons(eApplication, status, pStatus, bEdit), 1, 3);

			OuterFrame.add(Left, 1, 2);
			OuterFrame.add(Middle, 2, 2);
			OuterFrame.add(Right, 3, 2);
			OuterFrame.mergeCells(1,1,3,1);
			OuterFrame.add(getPreviousAndNextApplicationsLinks(iwc,eCampusApplication),1,1);

			theForm.add(OuterFrame);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return theForm;
	}

	public PresentationObject getViewApplicant(
		Applicant eApplicant,
		CampusApplication eCampusApplication) {
		DataTable T = getDataTable();
		T.setUseBottom(false);
		T.setWidth(Table.HUNDRED_PERCENT);
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

	public PresentationObject getFieldsApplicant(
		Applicant eApplicant,
		CampusApplication eCampusApplication) {
		int year = IWTimestamp.RightNow().getYear();
		DataTable T = getDataTable();
		T.setWidth(Table.HUNDRED_PERCENT);
		T.setUseBottom(false);
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
		String needEmail = localize("warning_provide_email","No email address is supplied");
		tiEmail.setAsEmail(needEmail);
		tiEmail.setAsNotEmpty(needEmail);
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

	private ApplicantInfo getApplicantInfo(IWContext iwc) {
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

		return new ApplicantInfo(sFullName,sSsn,sLegRes,sRes,sPo,	sResPho,sMobPho,sEmail,sFac,sTrack,
		sIncome!=null?new Double(	sIncome):null,sBM!=null?new Integer(	sBM):null,sEM!=null?new Integer(	sEM):null,sBY!=null?new Integer(	sBY):null,sEY!=null?new Integer(sEY):null);
	}

	

	public PresentationObject getViewSpouse(
		Applicant spouse,
		CampusApplication eCampusApplication) {
		int year = IWTimestamp.RightNow().getYear();
		DataTable T = getDataTable();
		T.setWidth(Table.HUNDRED_PERCENT);
		T.addTitle(localize("spouse", "Spouse"));
		T.setUseBottom(false);
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
			T.add(
				getText(beginMonth + " " + eCampusApplication.getSpouseStudyBeginYear().intValue()),
				col,
				row++);
			T.add(getText(endMonth + " " + eCampusApplication.getSpouseStudyEndYear().intValue()), col, row++);
			//T.add(getText(eCampusApplication.getSpouseIncome().intValue()),col,row);

		}

		return T;
	}

	public PresentationObject getFieldsSpouse(
		Applicant spouse,
		CampusApplication eCampusApplication) {
		int year = IWTimestamp.RightNow().getYear();
		DataTable T = getDataTable();
		T.setWidth(Table.HUNDRED_PERCENT);
		T.setUseBottom(false);
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
			tiSpSchl.setContent(eCampusApplication.getSpouseSchool());
			tiSpStTr.setContent(eCampusApplication.getSpouseStudyTrack());
			//tiSPIncome.setContent(eCampusApplication.getSpouseIncome().toString());

			beginMonth = eCampusApplication.getSpouseStudyBeginMonth().toString();
			endMonth = eCampusApplication.getSpouseStudyEndMonth().toString();
			beginYear = eCampusApplication.getSpouseStudyBeginYear().toString();
			endYear = eCampusApplication.getSpouseStudyEndYear().toString();
			T.add(new HiddenInput("ti_sp_id", spouse.getPrimaryKey().toString()));
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

	private SpouseInfo getSpouseInfo(IWContext iwc){
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

			int iBM = Integer.parseInt(sBM);
			int iEM = Integer.parseInt(sEM);
			int iBY = Integer.parseInt(sBY);
			int iEY = Integer.parseInt(sEY);
	
		return new SpouseInfo(sSpId!=null?new Integer(sSpId):null,sSpName,sSpSsn,sSpSchl,sSpStTr,sSPIncome!=null?new Double(sSPIncome):null,sBM!=null?new Integer(sBM):null,sBY!=null?new Integer(sBY):null,sEM!=null?new Integer(sEM):null,sEY!=null?new Integer(sEY):null);
	}

	

	public PresentationObject getViewChildren(
		Vector children,
		CampusApplication eCampusApplication) {
		DataTable T = getDataTable();
		T.setWidth(Table.HUNDRED_PERCENT);
		T.setUseBottom(false);
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

	public PresentationObject getFieldsChildren(
		Vector children,
		CampusApplication eCampusApplication) {
		DataTable T = getDataTable();
		T.setWidth(Table.HUNDRED_PERCENT);
		T.setUseBottom(false);
		T.addTitle(localize("children", "Children"));
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
				T.add(new HiddenInput("ti_child_id" + i, child.getPrimaryKey().toString()));
			}
		}
		T.add(new HiddenInput("ti_child_count", String.valueOf(count)));
		return T;
	}

	private List getChildrenInfo(IWContext iwc)		 {
		if (iwc.isParameterSet("ti_child_count")) {
			int count = Integer.parseInt(iwc.getParameter("ti_child_count"));
			if (count > 0) {
				List l = new Vector(count);
				for (int i = 0; i < count; i++) {
					String childName = iwc.getParameter("child_name" + i);
					String childSSN = iwc.getParameter("child_birth" + i);
					int childId =iwc.isParameterSet("ti_child_id" + i)	? Integer.parseInt(iwc.getParameter("ti_child_id" + i))	: -1;
					if (childName.length() > 0) {
						l.add(new ChildInfo(new Integer(childId),childName,childSSN));
					}
				}
				return l;
			}
		}
		return null;
	}

	

	public PresentationObject getViewApartment(
		CampusApplication eCampusApplication,
		Collection lApplied,
		IWContext iwc) throws RemoteException,FinderException{
		DataTable T = getDataTable();
		T.setWidth(Table.HUNDRED_PERCENT);
		T.setUseBottom(false);
		T.addTitle(localize("applied", "Applied"));
		int col = 1;
		int row = 1;
		if (lApplied != null) {
			int i = 0;
			ApartmentTypeHome ath =getApplicationService(iwc).getBuildingService().getApartmentTypeHome();
			for (Iterator iter = lApplied.iterator(); iter.hasNext();) {
				Applied A = (Applied) iter.next();
				T.add(getText(String.valueOf(i + 1)), 1, row);
				T.add(getText((ath.findByPrimaryKey(A.getApartmentTypeId()).getName())),2,row++);
				i++;
			}
		}
		return T;
	}

	public PresentationObject getViewApartmentExtra(
		CampusApplication eCampusApplication,
		IWContext iwc) {
		DataTable T = getDataTable();
		T.setWidth(Table.HUNDRED_PERCENT);
		T.setUseBottom(false);
		T.addTitle(localize("requests", "Requests"));
		int col = 1;
		int row = 1;

		T.add(getHeader(localize("housingfrom", "Housing from")), col, row++);
		T.add(getHeader(localize("wantfurniture", "Wants furniture")), col, row++);
		T.add(getHeader(localize("onwaitinglist", "On waitinglist")), col, row++);
		col = 2;
		row = 1;
		IWTimestamp iT = new IWTimestamp(eCampusApplication.getHousingFrom());
		T.add(getText(iT.getLocaleDate(iwc.getCurrentLocale())), col, row++);
		if (eCampusApplication.getWantFurniture())
			T.add(getText("X"), col, row++);
		if (eCampusApplication.getOnWaitinglist())
			T.add(getText("X"), col, row++);
		return T;
	}

	public PresentationObject getOtherInfo(
		CampusApplication eCampusApplication,
		IWContext iwc,
		boolean editable) {
		DataTable T = getDataTable();
		T.setWidth(Table.HUNDRED_PERCENT);
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
		TextArea commentArea = new TextArea("ap_comment", comment);
		if (editable) {
			commentArea.setReadOnly(false);
			commentArea.setRows(5);
			commentArea.setColumns(35);
		}
		else {
			commentArea.setReadOnly(true);
			commentArea.setRows(5);
		}

		//    commentArea
		Edit.setStyle(commentArea);
		T.add(commentArea, col, row);

		return T;
	}

	public DropdownMenu drpTypes(Collection coll, String name, String selected, boolean firstEmpty) {
		DropdownMenu drpTypes = new DropdownMenu(name);
		Edit.setStyle(drpTypes);
		if (firstEmpty)
			drpTypes.addMenuElementFirst("-1", "-");
		for (Iterator iter = coll.iterator(); iter.hasNext();) {
			ApartmentTypeComplexHelper element = (ApartmentTypeComplexHelper) iter.next();
			drpTypes.addMenuElement(element.getKey(), element.getName());
		}
		drpTypes.setSelectedElement(selected);
		return drpTypes;
	}

	public PresentationObject getFieldsApartment(
		CampusApplication eCampusApplication,
		Collection lApplied,
		IWContext iwc) {
		DataTable T = getDataTable();
		T.setWidth(Table.HUNDRED_PERCENT);
		T.setUseBottom(false);
		T.addTitle(localize("applied", "Applied"));
		int col = 1;
		int row = 1;
		String sOne = "-1", sTwo = "-1", sThree = "-3";
		if (lApplied != null) {
			int len = lApplied.size();
			Applied A;
			ApartmentTypeComplexHelper ATCH;
			Iterator iter = lApplied.iterator();
			if (len >= 1) {
				A = (Applied) iter.next();
				ATCH = new ApartmentTypeComplexHelper(A.getApartmentTypeId().intValue(), A.getComplexId().intValue());
				sOne = ATCH.getKey();
			}
			if (len >= 2) {
				A = (Applied) iter.next();
				ATCH = new ApartmentTypeComplexHelper(A.getApartmentTypeId().intValue(), A.getComplexId().intValue());
				sTwo = ATCH.getKey();
			}
			if (len >= 3) {
				A = (Applied)iter.next();
				ATCH = new ApartmentTypeComplexHelper(A.getApartmentTypeId().intValue(), A.getComplexId().intValue());
				sThree = ATCH.getKey();
			}
		}

		
		try {
			Collection typeHelpers =applicationService.getComplexTypeHelpers();
			DropdownMenu drpOne = drpTypes(typeHelpers, "drp_one", sOne, false);
			DropdownMenu drpTwo = drpTypes(typeHelpers, "drp_two", sTwo, true);
			DropdownMenu drpThree = drpTypes(typeHelpers, "drp_three", sThree, true);
			drpOne = (DropdownMenu) getStyledInterface(drpOne);
			drpTwo = (DropdownMenu) getStyledInterface(drpTwo);
			drpThree = (DropdownMenu) getStyledInterface(drpThree);

			T.add(getText("1"), 1, row);
			T.add(drpOne, 2, row++);
			T.add(getText("2"), 1, row);
			T.add(drpTwo, 2, row++);
			T.add(getText("3"), 1, row);
			T.add(drpThree, 2, row++);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return T;
	}

	public PresentationObject getFieldsApartmentExtra(
		CampusApplication eCampusApplication,
		IWContext iwc) {
		DataTable T = getDataTable();
		T.setWidth(Table.HUNDRED_PERCENT);
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
		diRentFrom.setDate(iT.getDate());
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

	private ApartmentInfo getApartmentInfo(IWContext iwc)  {
		String sRentFrom = iwc.getParameter("ap_rentfrom");
		String sFurni = iwc.getParameter("ap_furni");
		String sWait = iwc.getParameter("ap_wait");
		String comment = iwc.getParameter("ap_comment");
		String key1 = iwc.getParameter("drp_one");
		String key2 = iwc.getParameter("drp_two");
		String key3 = iwc.getParameter("drp_three");
		return new ApartmentInfo(new IWTimestamp(sRentFrom),new Boolean(sFurni),new Boolean(sWait),comment,key1,key2,key3);
	}

	

	public PresentationObject getViewApplication(Application eApplication) {
		DataTable T = getDataTable();
		T.setWidth(Table.HUNDRED_PERCENT);
		T.setUseBottom(false);
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

	private PresentationObject getRemoteControl() {
		DataTable T = getDataTable();
		T.setWidth(Table.HUNDRED_PERCENT);
		T.addTitle(localize("extra", "Extra"));
		int col = 1;
		int row = 1;
		T.add(getHeader(localize("tax_return", "Tax return")), col, row++);
		T.add(getHeader(localize("study_progress", "Study progress")), col, row++);
		T.add(getHeader(localize("choice1", "Choice 1")), col, row++);
		T.add(getHeader(localize("choice2", "Choice 2")), col, row++);
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

	private PresentationObject getSubjectControl( Application app) {
		DataTable T = getDataTable();
		T.setWidth(Table.HUNDRED_PERCENT);
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
		drp.setName(APP_SUBJECT_ID);
		T.add(drp, col, row);

		return T;
	}

	private PresentationObject getButtons(
		Application eApplication,
		String sStatus,
		String sPriority,
		boolean bEdit) {
		DataTable T = getDataTable();
		T.setWidth(Table.HUNDRED_PERCENT);
		T.addTitle(localize("control", "Control"));
		int row = 1;
		int col = 1;
		if (eApplication != null) {
			DropdownMenu status = statusDrop(PRM_STATUS, sStatus);
			//status.setToSubmit();
			status = (DropdownMenu) getStyledInterface(status);
			T.add(status, col, row);

			DropdownMenu priority = priorityDrop(PRM_PRIORITY, sPriority);
			priority = (DropdownMenu) getStyledInterface(priority);
			T.add(priority, col, row);
		}
		row++;
		if (bEdit) {
			SubmitButton view = new SubmitButton(localize("view", "View"), ACT_VIEWER, "View");
			view = (SubmitButton) getStyledInterface(view);
			T.add(view, col, row);
		}
		else {
			SubmitButton edit = new SubmitButton(localize("edit", "Edit"), ACT_EDITOR, "Edit");
			edit = (SubmitButton) getStyledInterface(edit);
			T.add(edit, col, row);
		}
		SubmitButton save = new SubmitButton(localize(ACT_SAVE, "Save"), ACT_SAVE, "Save");
		save = (SubmitButton) getStyledInterface(save);
		T.add(save, col, row);
		return T;
	}

	private PresentationObject getKnobs() {
		Table T = new Table(5, 1);
		T.setHorizontalAlignment("center");
/*
		if (iterator != null) {
			if (iterator.hasPrevious()) {
				Link lLast = new Link(getResourceBundle().getImage("back.gif"));
				lLast.addParameter(ACT_VIEW, "-2");
				T.add(lLast, 1, 1);
			}
			if (iterator.hasNext()) {
				Link lNext = new Link(getResourceBundle().getImage("next.gif"));
				lNext.addParameter(ACT_VIEW, "-4");
				T.add(lNext, 5, 1);
			}
		}*/
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
		
		try {
			applicationService.createApplicationSubject(sDesc, sDate);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (CreateException e) {
			e.printStackTrace();
		}
	}

	

	private Form subjectForm() {
		Form myForm = new Form();
		DropdownMenu drp = subjectDrop(String.valueOf(this.iSubjectId));
		DropdownMenu status = statusDrop("global_status", sGlobalStatus);
		DropdownMenu order = orderDrop("global_order", sGlobalOrder);
		DropdownMenu sizeMenu = sizeDrop("global_size",iGlobalSize);
		SubmitButton New = new SubmitButton(localize("btn_new", "New"), ACT_NEW_APPLICATION, "true");
		New = (SubmitButton) getStyledInterface(New);
		SubmitButton Info = new SubmitButton(localize("btn_info", "Info"), "subj_info", "true");
		Info = (SubmitButton)getStyledInterface(Info);
		SubmitButton fetch = new SubmitButton(localize("btn_fetch", "Fetch"));
		fetch = (SubmitButton)getStyledInterface(fetch);
		
		//SubmitButton New = new SubmitButton("new","New");
		//    SubmitButton New2 = new SubmitButton("new2","New transfer");
		/*
		drp.setToSubmit();
		status.setToSubmit();
		order.setToSubmit();
		sizeMenu.setToSubmit();
		*/
		drp = (DropdownMenu) getStyledInterface(drp);
		status = (DropdownMenu) getStyledInterface(status);
		order = (DropdownMenu) getStyledInterface(order);
		sizeMenu = (DropdownMenu) getStyledInterface(sizeMenu);
		
		DataTable T = getDataTable();
		T.addTitle(localize("filter", "Filter"));
		T.setTitlesHorizontal(true);
		T.setUseBottom(false);
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
		T.add(fetch,col++,row);
		T.add(Info, col++, row);
		if (iSubjectId > 0) {
			T.add(New, col++, row);
			//      T.add(New2,col++,row);
		}
		myForm.add(T);

		return myForm;
	}

	private DropdownMenu subjectDrop(String selected) {
		Collection L = listOfSubjects;
		DropdownMenu drp = new DropdownMenu(APP_SUBJECT_ID);
		drp.addMenuElement(-1, localize("subject", "Subject"));
		drp.addMenuElement(-99, localize("all_subject", "All"));
		boolean setsel = true;
		if (L != null) {
			ApplicationSubject AS;
			int len = L.size();
			int i = 0;
			for (Iterator iter = L.iterator(); iter.hasNext();) {
				 AS = (ApplicationSubject) iter.next();
				drp.addMenuElement(AS.getPrimaryKey().toString(), AS.getName());
				if(i==0 && selected.equals("-1")){
					drp.setSelectedElement(AS.getPrimaryKey().toString());
					setsel = false;
					
				}
				i++;
			}
			Edit.setStyle(drp);
		
			if(setsel)
				drp.setSelectedElement(selected);
		}
		return drp;
	}

	private String getStatus(String status) {
		String r = "";
		Character stat = new Character( status.charAt(0));
		if(stat.equals(Status.SUBMITTED))
			r = localize("submitted", "Submitted");
		else if(stat.equals(Status.APPROVED))	
			r = localize("approved", "Approved");
		else if(stat.equals(Status.REJECTED))
			r = localize("rejected", "Rejected");
		else if(stat.equals(Status.SIGNED))
			r = localize("contracted", "Contract");
		else if(stat.equals(Status.GARBAGE))
			r = localize("garbage", "Garbage");
		
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
		boolean hasSelectedValue = false;
		DropdownMenu drp = new DropdownMenu(name);
		try {
			Collection priorities = this.applicationService.getPriorityHome().findAll();
			for (Iterator iter = priorities.iterator(); iter.hasNext();) {
				Priority prior = (Priority) iter.next();
				drp.addMenuElement(prior.getPriority(), prior.getDescription());
				hasSelectedValue |= prior.getPriority().equals(selected);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		if(!hasSelectedValue)
			drp.addMenuElement(selected,selected);
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
		
		drp.addMenuElement("app_applicant_id", localize("submitted", "Submitted"));
		drp.addMenuElement(
			com.idega.block.application.data.ApplicantBMPBean.getFullnameOrderValue(),
			localize("name", "Name"));
		drp.addMenuElement(
			com.idega.block.application.data.ApplicantBMPBean.getSSNColumnName(),
			localize("ssn", "Socialnumber"));
		drp.addMenuElement(
			com.idega.block.application.data.ApplicantBMPBean.getLegalResidenceColumnName(),
			localize("legal_residence", "Legal Residence"));
		drp.addMenuElement(
			com.idega.block.application.data.ApplicantBMPBean.getResidenceColumnName(),
			localize("residence", "Residence"));
		drp.addMenuElement(
			com.idega.block.application.data.ApplicantBMPBean.getResidenceColumnName(),
			localize("phone", "Residence phone"));
		drp.setSelectedElement(selected);
		return drp;
	}

	public Link getCampusApplicationLink(PresentationObject MO, Integer applicationID,int index) {
		Link L = new Link(MO);
		L.addParameter(ACT_VIEW, applicationID.toString());
		L.addParameter(PRM_INDEX ,index);
		return L;
	}

	

	public Link getTrashLink(PresentationObject MO, Integer cam_app_id) {
		Link L = new Link(MO);
		L.addParameter(ACT_TRASH_APPLICATION, cam_app_id.toString());
		return L;
	}

	public Link getPDFLink(IWContext iwc,PresentationObject MO, int cam_app_id) {
		Link link = new Link(MO);
		link.setURL(iwc.getIWMainApplication().getMediaServletURI()+"application"+cam_app_id+".pdf");
	    link.addParameter(CampusApplicationWriter.PRM_WRITABLE_CLASS,IWMainApplication.getEncryptedClassName(CampusApplicationWriter.class));
		link.addParameter(CampusApplicationWriter.PRM_CAMPUS_APPLICATION_ID, cam_app_id);
		return link;
	}
	public Link getPDFLink(IWContext iwc,PresentationObject MO, String status, int subject_id) {
		Link link = new Link(MO);
		link.setURL(iwc.getIWMainApplication().getMediaServletURI()+"apps_stat_"+status+"_subj_"+subject_id+".xls");
	    link.addParameter(CampusApplicationWriter.PRM_WRITABLE_CLASS,IWMainApplication.getEncryptedClassName(CampusApplicationWriter.class));
	    link.addParameter(CampusApplicationWriter.PRM_APPLICATION_STATUS, status);
	    link.addParameter(CampusApplicationWriter.PRM_SUBJECT_ID, subject_id);
		return link;
	}
	
	public void main(IWContext iwc) throws RemoteException{
		//isStaff = com.idega.core.accesscontrol.business.AccessControl
		isAdmin = iwc.hasEditPermission(this);
		control(iwc);
	}
}
