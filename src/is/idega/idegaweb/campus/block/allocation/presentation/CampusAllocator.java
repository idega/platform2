/*
 * $Id: CampusAllocator.java,v 1.70 2004/07/12 11:52:23 aron Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.allocation.presentation;
import is.idega.idegaweb.campus.block.allocation.business.AllocationException;
import is.idega.idegaweb.campus.block.allocation.business.Period;
import is.idega.idegaweb.campus.block.allocation.business.WaitingListFinder;
import is.idega.idegaweb.campus.block.allocation.data.AllocationView;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractHome;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationHolder;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationWriter;
import is.idega.idegaweb.campus.block.application.data.WaitingList;
import is.idega.idegaweb.campus.block.application.data.WaitingListHome;
import is.idega.idegaweb.campus.block.application.presentation.CampusApprover;
import is.idega.idegaweb.campus.business.CampusService;
import is.idega.idegaweb.campus.data.SystemProperties;
import is.idega.idegaweb.campus.presentation.Campus;
import is.idega.idegaweb.campus.presentation.CampusBlock;
import is.idega.idegaweb.campus.presentation.CampusProperties;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.block.application.business.ReferenceNumberHandler;
import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.ApplicantHome;
import com.idega.block.application.data.Application;
import com.idega.block.application.data.ApplicationHome;
import com.idega.block.application.data.ApplicationSubject;
import com.idega.block.application.data.Status;
import com.idega.block.building.business.ApartmentTypeComplexHelper;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.ApartmentCategory;
import com.idega.block.building.data.ApartmentType;
import com.idega.block.building.data.ApartmentView;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.ComplexTypeView;
import com.idega.block.building.data.ComplexTypeViewHome;
import com.idega.block.building.data.Floor;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOStoreException;
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
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;
/**
 * @author  <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */
public class CampusAllocator extends CampusBlock implements Campus {
	
	private static final String PRM_ALLOCATE = "allocate";
	private static final String COMPLEX_ID = "cplx_id";
	private static final String TYPE_ID = "type_id";
	private int acceptanceSeconds = 60 * 60 * 24 * 3; // default 3 days
	protected final int ACT1 = 1, ACT2 = 2, ACT3 = 3, ACT4 = 4, ACT5 = 5;
	private int allowedRejections = 3;
	private String blueColor = "#27324B", lightBlue = "#ECEEF0";
	private String bottomThickness = "8";
	
	private DateFormat dateFormat;
	private Integer dayBuffer = new Integer(1);
	protected boolean fontBold = false;
	protected int fontSize = 2;
	protected boolean isAdmin = false;
	private Integer iSubjectId = new Integer(-1);
	private ListIterator iterator = null;
	private Integer typeID, complexID;
	private ApartmentType apartmentType = null;
	private Complex complex = null;
	private CampusService campusService = null;

	private LinkedList linkedlist = null;
	protected String MiddleColor, LightColor, DarkColor, WhiteColor, TextFontColor, HeaderFontColor, IndexFontColor;
	private Integer monthOverlap = new Integer(1);
	private Parameter pTypeId = null, pComplexId = null;
	private String redColor = "#942829";
	private String sGlobalStatus = "S";
	protected String styleAttribute = "font-size: 8pt";
	private SystemProperties SysProps = null;
	private Map subjectMap = null;
	
	public CampusAllocator() {
		LightColor = "#D7DADF";
		MiddleColor = "#9fA9B3";
		DarkColor = "#27334B";
		WhiteColor = "#FFFFFF";
		TextFontColor = "#000000";
		HeaderFontColor = DarkColor;
		IndexFontColor = "#000000";
	}
	
	protected void control(IWContext iwc) throws RemoteException, FinderException {
		campusService = getCampusService(iwc);
		dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, iwc.getCurrentLocale());
		
		fontSize = 1;
		/*		if (iwc.getParameter("list") != null) {
				}*/
		if (iwc.getParameter(TYPE_ID) != null) {
			pTypeId = new Parameter(TYPE_ID, iwc.getParameter(TYPE_ID));
			typeID = Integer.valueOf(iwc.getParameter(TYPE_ID));
			try {
				apartmentType = campusService.getBuildingService().getApartmentTypeHome().findByPrimaryKey(typeID);
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			}
		}
		if (iwc.getParameter(COMPLEX_ID) != null) {
			pComplexId = new Parameter(COMPLEX_ID, iwc.getParameter(COMPLEX_ID));
			complexID = Integer.valueOf(iwc.getParameter(COMPLEX_ID));
			try {
				complex = campusService.getBuildingService().getComplexHome().findByPrimaryKey(complexID);
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			}
		}
		
		Table Frame = new Table();
		Frame.add(getHomeLink(), 1, 1);
		int row = 2;
		if (isAdmin) {
			if (typeID!=null && complexID!=null && typeID.intValue() > 0 && complexID.intValue() > 0) {
				// Allocate apartment to an applicant
				if (iwc.getParameter(PRM_ALLOCATE) != null) {
					Integer applicantID = Integer.valueOf(iwc.getParameter(PRM_ALLOCATE));
					Integer waitinglistID = new Integer(-1);
					if (iwc.isParameterSet("wl_id"))
						waitinglistID = Integer.valueOf(iwc.getParameter("wl_id"));
					Frame.add(getApartmentsForm(iwc,applicantID, new Integer(-1), waitinglistID),
						3,
						row);
					Frame.add(getApplicantInfo(iwc, applicantID), 1, row);
				}
				// Change allocation
				else if (iwc.getParameter("change") != null) {
					Integer contractID = Integer.valueOf(iwc.getParameter("change"));
					Integer applicantID = Integer.valueOf(iwc.getParameter("applicant"));
					Integer waitinglistID = new Integer(-1);
					if (iwc.isParameterSet("wl_id"))
						waitinglistID = Integer.valueOf(iwc.getParameter("wl_id"));
					Frame.add(getWaitingList(iwc, contractID), 1, row);
					Frame.add(getApartmentsForm(iwc, applicantID, contractID, waitinglistID),3,row);
					row++;
					Frame.mergeCells(1, row, 3, row);
					Frame.add(getColorButtonInfo(), 1, row);
				}
				// show all contracts for apartment
				else if (iwc.getParameter("view_aprtmnt") != null) {
					Integer apartmentID = Integer.valueOf(iwc.getParameter("view_aprtmnt"));
					Integer contractID = Integer.valueOf(iwc.getParameter("contract"));
					Integer applicantID = Integer.valueOf(iwc.getParameter("applicant"));
					IWTimestamp from = new IWTimestamp(iwc.getParameter("from"));
					Frame.add(getApplicantInfo(iwc, applicantID), 1, row);
					Frame.add(getContractsForm(iwc, apartmentID, applicantID, contractID, from, null), 3, row);
				}
				// save allocation
				else if (
					iwc.getParameter("save_allocation") != null
						&& iwc.getParameter("save_allocation").equals("true")) {
					String msg = saveAllocation(iwc);
					//System.err.println(msg);
					Text Te = getText(msg);
					Te.setFontSize(3);
					Te.setFontColor("#FF0000");
					Frame.add(Te, 1, row++);
					Frame.add(getWaitingList(iwc, new Integer(-1)), 1, row);
					row++;
					Frame.mergeCells(1, row, 3, row);
					Frame.add(getColorButtonInfo(), 1, row);
				}
				// delete allocation
				else if (iwc.getParameter("delete_allocation") != null) {
					deleteAllocation(iwc);
					Frame.add(getWaitingList(iwc, new Integer(-1)), 1, row);
					row++;
					Frame.mergeCells(1, row, 3, row);
					Frame.add(getColorButtonInfo(), 1, row);
				}
				else if (iwc.isParameterSet("offwaitinglist")) {
					Integer wID = new Integer(iwc.getParameter("offwaitinglist"));
					Integer wAID = new Integer(iwc.getParameter("wl_appid"));
					Frame.add(getApplicantInfo(iwc, wAID), 1, row);
					Frame.add(getOffWaitingList(wID), 3, row);
				}
				else if (iwc.isParameterSet("remove_waitinglist")) {
					Integer wID = new Integer(iwc.getParameter("remove_waitinglist"));
					campusService.getContractService().removeWaitingList(wID);
					Frame.add(getWaitingList(iwc,  new Integer(-1)), 1, row);
					row++;
					Frame.mergeCells(1, row, 3, row);
					Frame.add(getColorButtonInfo(), 1, row);
				}
				else if (iwc.isParameterSet("reactivate_waitinglist")) {
					Integer wID = new Integer(iwc.getParameter("reactivate_waitinglist"));
					campusService.getContractService().reactivateWaitingList(wID);
					Frame.add(getWaitingList(iwc,  new Integer(-1)), 1, row);
					row++;
					Frame.mergeCells(1, row, 3, row);
					Frame.add(getColorButtonInfo(), 1, row);
				}
				// get Waitinglist for this type and complex
				else {
					Frame.add(getWaitingList(iwc, new Integer(-1)), 1, row);
					row++;
					Frame.mergeCells(1, row, 3, row);
					Frame.add(getColorButtonInfo(), 1, row);
				}
			}
			// get type and complex list
			else{
				Table table = new Table(3,1);
				table.setAlignment(3,1,Table.HORIZONTAL_ALIGN_RIGHT);
				Link statsLink = getLink(localize("allocation.show_statistic","Show statistics"));
				statsLink.addParameter("shw_stats","true");
				table.add(statsLink,3,1);
				table.setWidth(Table.HUNDRED_PERCENT);
				Frame.add(table,1,row++);
				Frame.add(getCategoryLists(iwc), 1, row);
				
			}
			Frame.setRowVerticalAlignment(2, "top");
			add(Frame);
		}
		else
			add(getNoAccessObject(iwc));
	}
	private boolean deleteAllocation(IWContext iwc) throws RemoteException, FinderException {
		String sContractId = iwc.getParameter("contract_id");

		Integer contractID = Integer.valueOf(sContractId);
		if(iwc.isParameterSet("reset_reject_flag") || iwc.isParameterSet("increment_rejections")){
			try {
				Integer wlID = new Integer(iwc.getParameter("wl_id"));
				WaitingList wl = ((WaitingListHome)IDOLookup.getHome(WaitingList.class)).findByPrimaryKey(wlID);
				if(iwc.isParameterSet("increment_rejections"))
					wl.incrementRejections(false);
				else if(iwc.isParameterSet("reset_reject_flag"))		
					wl.setRejectFlag(false);
				wl.store();
			}
			catch (NumberFormatException e) {
				e.printStackTrace();
			}
			catch (IDOLookupException e) {
				e.printStackTrace();
			}
			catch (IDOStoreException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}

		}
		return campusService.getContractService().deleteAllocation(contractID, iwc.getCurrentUser());
	}
	

	private Link getAcceptsLink(int Contractid, int iApplicantId,boolean onTime) {
		
			Image color = getBundle().getImage(onTime? "purple.gif" :  "brown.gif");
			Link L = new Link(color);
			L.addParameter("change", String.valueOf(Contractid));
			L.addParameter("applicant", String.valueOf(iApplicantId));
			L.setToolTip(localize("tooltip_alloc_change","Change"));
			if (pTypeId != null && pComplexId != null) {
				L.addParameter(pTypeId);
				L.addParameter(pComplexId);
			}
			return L;

	}
	private Link getAllocateLink(Applicant applicant) {
		Link L = new Link(getBundle().getImage("red.gif"));
		L.addParameter(PRM_ALLOCATE, applicant.getPrimaryKey().toString());
		L.setToolTip(localize("tooltip_nr_alloc_create", "Allocate"));
		if (pTypeId != null && pComplexId != null) {
			L.addParameter(pTypeId);
			L.addParameter(pComplexId);
		}
		return L;
	}
	/**
	 * @return
	 */
	public int getAllowedRejections() {
		return allowedRejections;
	}
	private PresentationObject getApartmentContracts(Integer apartmentID) throws RemoteException, FinderException {
		/** @todo try to use jdbc cursors in sql queries */
		int iCount = Integer.parseInt(getBundle().getProperty(CampusProperties.PROP_ALLOC_APRT_CON_COUNT, "10"));
		DataTable T = new DataTable();
		T.addTitle((localize("apartment_contracts", "Apartment contracts")));
		T.setTitlesHorizontal(true);
		int row = 1;
		T.add(getHeader(localize("tenant", "Tenant")), 1, row);
		T.add(getHeader(localize("valid_from", "From")), 2, row);
		T.add(getHeader(localize("valid_to", "To")), 3, row);
		T.add(getHeader(localize("status", "Status")), 4, row);
		Collection contracts = getContractHome().findByApartmentID(apartmentID);
		UserHome userHome = campusService.getContractService().getUserService().getUserHome();
		if (contracts != null && !contracts.isEmpty()) {
			java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT);
			java.util.Iterator I = contracts.iterator();
			Contract contract;
			User user;
			IWTimestamp temp = null;
			row = 2;
			while (I.hasNext() && iCount-- > 0) {
				contract = (Contract) I.next();
				user = userHome.findByPrimaryKey(contract.getUserId());
				T.add(getText(user.getName()), 1, row);
				T.add(getText(df.format((java.util.Date) contract.getValidFrom())), 2, row);
				T.add(getText(df.format((java.util.Date) contract.getValidTo())), 3, row);
				T.add(getText(getStatus(contract.getStatus())), 4, row);
				T.add(new HiddenInput("apartmentid", apartmentID.toString()));
				row++;
			}
		}
		return T;
	}
	private Link getApartmentContractsLink(
		Text display,
		Integer applicantID,
		Integer contractID,
		Integer apartmentID,
		IWTimestamp from) {
		Link L = new Link(display);
		L.addParameter("view_aprtmnt", apartmentID.toString());
		L.addParameter("contract", contractID.toString());
		L.addParameter("applicant", applicantID.toString());
		L.addParameter("from", from.toString());
		if (pTypeId != null && pComplexId != null) {
			L.addParameter(pTypeId);
			L.addParameter(pComplexId);
		}
		return L;
	}
	private PresentationObject getApartmentsForm(
		IWContext iwc,
		Integer applicantID,
		Integer contractID,
		Integer waitingListID)
		throws RemoteException, FinderException {
		Form myForm = new Form();
		ApartmentType apartmentType =campusService.getBuildingService().getApartmentTypeHome().findByPrimaryKey(typeID);
		Complex complex = campusService.getBuildingService().getComplexHome().findByPrimaryKey(complexID);
		Contract contract = null;
		if(contractID!=null && contractID.intValue()>0)
			contract = campusService.getContractService().getContractHome().findByPrimaryKey(contractID);
		WaitingList waitingList = null;
		if (waitingListID.intValue() > 0) {
			try {
				waitingList = getWaitingListHome().findByPrimaryKey((waitingListID));
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
		}
		Table Frame = new Table();
		/*
		Table Header = new Table();
		Header.add(headerText(AT.getName()+" "),1,1);
		Header.add(headerText(CX.getName()),1,1);
		Header.mergeCells(1,1,4,1);
		Header.setRowColor(1,blueColor);
		Header.setWidth("100%");
		Frame.add(Header,1,1);
		*/
		if (contract != null)
			Frame.add(getContractMakingTable(iwc, contract, applicantID, null, contract.getApartmentId(), waitingList),1,1);
		else
			Frame.add(getFreeApartments(iwc, applicantID, contract), 1, 1);
		myForm.add(Frame);

		myForm.add(new HiddenInput(pTypeId.getName(), pTypeId.getValueAsString()));
		myForm.add(new HiddenInput(pComplexId.getName(), pComplexId.getValueAsString()));

		return myForm;
	}
	private String getApartmentString(Contract eContract) throws RemoteException,FinderException{
		Apartment A = campusService.getBuildingService().getApartmentHome().findByPrimaryKey(eContract.getApartmentId());
		Floor F = campusService.getBuildingService().getFloorHome().findByPrimaryKey(new Integer(A.getFloorId()));
		Building B = campusService.getBuildingService().getBuildingHome().findByPrimaryKey(new Integer(F.getBuildingId()));
		Complex C = campusService.getBuildingService().getComplexHome().findByPrimaryKey(new Integer(B.getComplexId()));
		StringBuffer sb = new StringBuffer(A.getName());
		sb.append(" ");
		sb.append(F.getName());
		sb.append(" ");
		sb.append(B.getName());
		sb.append(" ");
		sb.append(C.getName());
		return sb.toString();
	}
	public PresentationObject getApplicantInfo(IWContext iwc, Integer applicantID) throws RemoteException {
		CampusApprover CA = new CampusApprover();
		PresentationObject MO = new Table();
		CampusApplicationHolder AH = getApplicationService(iwc).getApplicantInfo(applicantID.intValue());
		if (AH != null) {
			Table Frame = new Table(2, 2);
			Frame.mergeCells(1, 2, 2, 2);
			Frame.setVerticalAlignment(1, 2, "top");
			Frame.add(CA.getViewApplicant(AH.getApplicant(), AH.getCampusApplication()), 1, 1);
			String sSpouse = AH.getCampusApplication().getSpouseName();
			String sChildren = AH.getCampusApplication().getChildren();
			boolean bSpouse = false, bChildren = false;
			if (sSpouse != null && sSpouse.length() > 0) {
				Frame.add(CA.getViewSpouse(AH.getApplicant(), AH.getCampusApplication()), 2, 1);
				bSpouse = true;
			}
			if (sChildren != null && sChildren.length() > 0) {
				Frame.add(CA.getViewChildren(null, AH.getCampusApplication()), 2, 1);
				bChildren = true;
			}
			if (bChildren && bSpouse) {
				Frame.mergeCells(1, 1, 2, 1);
			}
			try {
				Frame.add(CA.getViewApartment(AH.getCampusApplication(), AH.getApplied(), iwc), 1, 2);
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
			MO = Frame;
		}
		else
			add("er null");
		return MO;
	}
	
	private PresentationObject getCategoryLists(IWContext iwc) throws RemoteException,FinderException{
		DataTable T = new DataTable();
		T.setTitlesHorizontal(true);
		T.addTitle(localize("apartment_category", "Apartment category"));
		T.setWidth("100%");
		boolean showStatistics = iwc.isParameterSet("shw_stats");
		Image printImage = getBundle().getImage("print.gif");
		Collection Categories = campusService.getBuildingService().getApartmentCategoryHome().findAll();
		if(showStatistics){
			Map allocationView = WaitingListFinder.getAllocationView();
			if (Categories != null && allocationView!=null) {
				int row = 2;
				int cLen = Categories.size();
				int listCount = 0, contractCount = 0, appliedCount = 0, appCnt1 = 0, appCnt2 = 0, appCnt3 = 0;
				int totalCount = 0, totalFree = 0, totalApplied = 0, totApp1 = 0, totApp2 = 0, totApp3 = 0;
				int freeCount = 0;
				int type, cmpx;
				
				for (Iterator iter = Categories.iterator(); iter.hasNext();) {
					ApartmentCategory AC = (ApartmentCategory) iter.next();
					
					List L = (List) allocationView.get(((Integer)AC.getPrimaryKey()));
				
					if (L != null) {
						int lLen = L.size();
						int catlist = 0, catfree = 0, catcont = 0, catapp = 0, catcnt1 = 0, catcnt2 = 0, catcnt3 = 0;
						T.add(getHeader(AC.getName()), 1, row);
						row++;
						for (int j = 0; j < lLen; j++) {
							
							AllocationView view = (AllocationView) L.get(j);
							type = view.getTypeId();
							cmpx = view.getComplexId();
							listCount = view.getTotalNumberOfApartments();
							freeCount = view.getNumberOfFreeApartments();
							appCnt1 = view.getNumberOfChoice1();
							appCnt2 = view.getNumberOfChoice2();
							appCnt3 = view.getNumberOfChoice3();
							appliedCount = appCnt1 + appCnt2 + appCnt3;
							totalCount += listCount;
							catlist += listCount;
							//						freeCount = listCount - contractCount;
							totalFree += freeCount;
							catfree += freeCount;
							totalApplied += appliedCount;
							catapp += appliedCount;
							totApp1 += appCnt1;
							totApp2 += appCnt2;
							totApp3 += appCnt3;
							catcnt1 += appCnt1;
							catcnt2 += appCnt2;
							catcnt3 += appCnt3;
							StringBuffer name = new StringBuffer(view.getTypeName());
							name.append(" (");
							name.append(view.getComplexName());
							name.append(")");
							T.add(getPDFLink(iwc,printImage, type, cmpx), 1, row);
							T.add(getListLink(name.toString(), type, cmpx), 2, row);
							T.add(getText(String.valueOf(listCount)), 3, row);
							T.add(getText(String.valueOf(freeCount)), 4, row);
							T.add(getText(String.valueOf(appliedCount)), 5, row);
							T.add(getText(String.valueOf(appCnt1)), 6, row);
							T.add(getText(String.valueOf(appCnt2)), 7, row);
							T.add(getText(String.valueOf(appCnt3)), 8, row);
							row++;
						}
						T.add(getHeader(catlist), 3, row);
						T.add(getHeader(catfree), 4, row);
						T.add(getHeader(catapp), 5, row);
						T.add(getHeader(catcnt1), 6, row);
						T.add(getHeader(catcnt2), 7, row);
						T.add(getHeader(catcnt3), 8, row);
						row++;
					}
				}
				T.add(getHeader(localize("apartment_category", "Apartment category")), 1, 1);
				T.add(getHeader(localize("apartments", "Apartments")), 3, 1);
				T.add(getHeader(localize("available", "Available")), 4, 1);
				T.add(getHeader(localize("applied", "Applied")), 5, 1);
				T.add(getHeader(localize("choice1", "1.Choice")), 6, 1);
				T.add(getHeader(localize("choice2", "2.Choice")), 7, 1);
				T.add(getHeader(localize("choice3", "3.Choice")), 8, 1);
				T.add(getPDFLink(iwc,printImage, -1, -1), 1, row);
				T.add(getHeader(totalCount), 3, row);
				T.add(getHeader(totalFree), 4, row);
				T.add(getHeader(totalApplied), 5, row);
				T.add(getHeader(totApp1), 6, row);
				T.add(getHeader(totApp2), 7, row);
				T.add(getHeader(totApp3), 8, row);
				row++;
			}
			
		}
		else{
			int row = 2;
			ComplexTypeViewHome ctHome = campusService.getBuildingService().getComplexTypeViewHome();
			if(Categories!=null && !Categories.isEmpty()){
				for (Iterator iter = Categories.iterator(); iter.hasNext();) {
					ApartmentCategory category = (ApartmentCategory) iter.next();
					Collection complexTypes = ctHome.findByCategory((Integer)category.getPrimaryKey());
					T.add(getHeader(category.getName()), 1, row);
					T.getContentTable().mergeCells(1,row,2,row);
					row++;
					for (Iterator iterator = complexTypes.iterator(); iterator.hasNext();) {
						ComplexTypeView complexType = (ComplexTypeView) iterator.next();
						StringBuffer name = new StringBuffer(complexType.getApartmentTypeName());
						name.append(" (");
						name.append(complexType.getComplexName());
						name.append(")");
						T.add(getPDFLink(iwc,printImage, complexType.getApartmentTypeID().intValue(), complexType.getComplexID().intValue()), 1, row);
						T.add(getListLink(name.toString(), complexType.getApartmentTypeID().intValue(), complexType.getComplexID().intValue()), 2, row);
						row++;
					}
					row++;
					
				}
				T.add(getHeader(localize("apartment_category", "Apartment category")), 1, 1);
				
				T.add(getPDFLink(iwc,printImage, -1, -1), 1, row);
			
			}
			
		}
		return T;
	}


	private Link getChangeLink(Integer contractID,WaitingList waitingList,boolean onTime) {
		Image color = getBundle().getImage(onTime? "green.gif" :  "yellow.gif");
		if(waitingList.getAcceptedDate()!=null)
			color = getBundle().getImage(onTime?"purple.gif":"brown.gif");

		Link L = new Link(color);
		L.addParameter(COMPLEX_ID,complexID.toString());
		L.addParameter(TYPE_ID,typeID.toString());
		L.addParameter("change", contractID.toString());
		L.addParameter("applicant", waitingList.getApplicantId().toString());
		L.setToolTip(localize("tooltip_alloc_change","Change"));
		return L;
	}
/*	private PresentationObject getCategoryLists() {
		DataTable T = new DataTable();
		T.setTitlesHorizontal(true);
		T.addTitle(localize("apartment_category", "Apartment category"));
		T.setWidth("100%");
		List Categories = BuildingFinder.listOfApartmentCategory();
		if (Categories != null) {
			int row = 2;
			int cLen = Categories.size();
			int listCount = 0, contractCount = 0, appliedCount = 0, appCnt1 = 0, appCnt2 = 0, appCnt3 = 0;

			int totalCount = 0, totalFree = 0, totalApplied = 0, totApp1 = 0, totApp2 = 0, totApp3 = 0;
			int freeCount = 0;
			int type, cmpx;
			Image printImage = getBundle().getImage("print.gif");
			for (int i = 0; i < cLen; i++) {
				ApartmentCategory AC = (ApartmentCategory) Categories.get(i);
				List L = BuildingFinder.getApartmentTypesComplexForCategory(AC.getID());
				if (L != null) {
					int lLen = L.size();
					int catlist = 0, catfree = 0, catcont = 0, catapp = 0, catcnt1 = 0, catcnt2 = 0, catcnt3 = 0;
					T.add(getHeader(AC.getName()), 1, row);
					row++;
					for (int j = 0; j < lLen; j++) {
						ApartmentTypeComplexHelper eAprtType = (ApartmentTypeComplexHelper) L.get(j);
						type = eAprtType.getKeyOne();
						cmpx = eAprtType.getKeyTwo();
						listCount = BuildingFinder.countApartmentsInTypeAndComplex(type, cmpx);
						contractCount = ContractFinder.countApartmentsInTypeAndComplex(type, cmpx, is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean.statusSigned);

						appliedCount = CampusApplicationFinder.countWaitingWithTypeAndComplex(type, cmpx, 0);
						appCnt1 = CampusApplicationFinder.countWaitingWithTypeAndComplex(type, cmpx, 1);
						appCnt2 = CampusApplicationFinder.countWaitingWithTypeAndComplex(type, cmpx, 2);
						appCnt3 = CampusApplicationFinder.countWaitingWithTypeAndComplex(type, cmpx, 3);

						totalCount += listCount;
						catlist += listCount;
						freeCount = listCount - contractCount;
						totalFree += freeCount;
						catfree += freeCount;
						totalApplied += appliedCount;
						catapp += appliedCount;
						totApp1 += appCnt1;
						totApp2 += appCnt2;
						totApp3 += appCnt3;
						catcnt1 += appCnt1;
						catcnt2 += appCnt2;
						catcnt3 += appCnt3;

						T.add(getPDFLink(printImage, type, cmpx), 1, row);
						T.add(getListLink(eAprtType), 2, row);
						T.add(formatText(listCount), 3, row);
						T.add(formatText(freeCount), 4, row);
						T.add(formatText(appliedCount), 5, row);
						T.add(formatText(appCnt1), 6, row);
						T.add(formatText(appCnt2), 7, row);
						T.add(formatText(appCnt3), 8, row);
						row++;
					}
					T.add(getHeader(catlist), 3, row);
					T.add(getHeader(catfree), 4, row);
					T.add(getHeader(catapp), 5, row);
					T.add(getHeader(catcnt1), 6, row);
					T.add(getHeader(catcnt2), 7, row);
					T.add(getHeader(catcnt3), 8, row);
					row++;
				}
			}

			T.add(getHeader(localize("apartment_category", "Apartment category")), 1, 1);
			T.add(getHeader(localize("apartments", "Apartments")), 3, 1);
			T.add(getHeader(localize("available", "Available")), 4, 1);
			T.add(getHeader(localize("applied", "Applied")), 5, 1);
			T.add(getHeader(localize("choice1", "1.Choice")), 6, 1);
			T.add(getHeader(localize("choice2", "2.Choice")), 7, 1);
			T.add(getHeader(localize("choice3", "3.Choice")), 8, 1);
			T.add(getPDFLink(printImage, -1, -1), 1, row);
			T.add(getHeader(totalCount), 3, row);
			T.add(getHeader(totalFree), 4, row);
			T.add(getHeader(totalApplied), 5, row);
			T.add(getHeader(totApp1), 6, row);
			T.add(getHeader(totApp2), 7, row);
			T.add(getHeader(totApp3), 8, row);
			row++;
		}

		return T;
	}*/

/*	private PresentationObject getWaitingLists(ApartmentCategory AC) {
		Table T = new Table();

		List L = BuildingFinder.getApartmentTypesComplexForCategory(AC.getID());
		int row = 2;
		if (L != null) {
			int lLen = L.size();
			int listCount = 0;
			for (int i = 0; i < lLen; i++) {
				ApartmentTypeComplexHelper eAprtType = (ApartmentTypeComplexHelper) L.get(i);
				listCount = BuildingFinder.countApartmentsInTypeAndComplex(eAprtType.getKeyOne(), eAprtType.getKeyTwo());
				T.add(getListLink(eAprtType), 2, row);
				T.add(formatText(listCount), 3, row);
				row++;
			}
			T.setHorizontalZebraColored(lightBlue, WhiteColor);
			T.setRowColor(1, blueColor);
			T.setRowColor(row, redColor);
			T.mergeCells(1, 1, 2, 1);
			T.mergeCells(1, row, 2, row);
			T.setWidth(1, "15");
			T.add(headerText(AC.getName()), 1, 1);
			T.add(formatText(" "), 1, row);
			T.setColumnAlignment(1, "left");
			T.setHeight(row, bottomThickness);
			T.setWidth("100%");
		}

		return T;
	}*/

	private Link getListLink(ApartmentTypeComplexHelper eAprtType) {
		return getListLink(eAprtType.getName(),eAprtType.getKeyOne(),eAprtType.getKeyTwo());
	}

	private Link getListLink(String name, int type_id, int complex_id) {
		Link L = getLink((name));
		//L.setFontSize(fontSize);
		L.addParameter(TYPE_ID, type_id);
		L.addParameter(COMPLEX_ID, complex_id);
		return L;
	}

	private Link getAllocateLink(int id) {
		Link L = new Link(getBundle().getImage("red.gif"));
		L.addParameter(PRM_ALLOCATE, String.valueOf(id));
		L.setToolTip(localize("tooltip_nr_alloc_create","Allocate"));
		if (pTypeId != null && pComplexId != null) {
			L.addParameter(pTypeId);
			L.addParameter(pComplexId);
		}
		return L;
	}
	
	private Link getOffWaitingListLink(WaitingList waitingList) {
		Link L = new Link(getBundle().getImage("blue.gif"));
		L.addParameter("offwaitinglist",waitingList.getPrimaryKey().toString());
		L.addParameter("wl_appid",waitingList.getApplicantId().toString());
		L.setToolTip(localize("tooltip_nr_alloc_offlist","Wants off list"));
		if (pTypeId != null && pComplexId != null) {
				L.addParameter(pTypeId);
				L.addParameter(pComplexId);
		}
		return L;
	}

	private Link getApartmentContractsLink(Text display, int applicant_id, int contract_id, int apartment_id, IWTimestamp from) {
		Link L = new Link(display);
		L.addParameter("view_aprtmnt", String.valueOf(apartment_id));
		L.addParameter("contract", String.valueOf(contract_id));
		L.addParameter("applicant", String.valueOf(applicant_id));
		L.addParameter("from", from.toString());
		if (pTypeId != null && pComplexId != null) {
			L.addParameter(pTypeId);
			L.addParameter(pComplexId);
		}
		return L;
	}
	
	private Link getDenialChangeLink(Integer contractID,WaitingList waitingList,boolean onTime){
		int rejections = waitingList.getNumberOfRejections();
		Text number = new Text(String.valueOf(rejections));
		number.setBold();
		number.setFontSize(Text.FONT_SIZE_14_HTML_4);
		Link L = new Link(number);
		L.setToolTip(rejections+". "+localize("tooltip_nr_alloc_denial","denial"));
		// still allowed to reject then allow allocation
		if(rejections < allowedRejections){
			if(contractID!=null){
				L.addParameter("change", contractID.toString());
				// if reject status on , handle by office
				if(waitingList.getAcceptedDate()!=null){
					if(onTime)
						number.setFontColor("#9900ff");
					else
						number.setFontColor("#996600");
				}
				else if(waitingList.getRejectFlag()){
					number.setFontColor("#000000");
				}
				else if(onTime){
					number.setFontColor("#00FF00");
				}
				else{ 
				
					number.setFontColor("#ffff00");
				}
			}
			else{
				L.addParameter(PRM_ALLOCATE, waitingList.getApplicantId().toString());
				number.setFontColor("#FF0000");
			}
			L.addParameter("applicant", waitingList.getApplicantId().toString());
			L.addParameter("wl_id", waitingList.getPrimaryKey().toString());
			
		}
		else{
			L.addParameter("offwaitinglist",waitingList.getPrimaryKey().toString());
			L.addParameter("wl_appid",waitingList.getApplicantId().toString());
			number.setFontColor("#0000FF");
		}
		if (pTypeId != null && pComplexId != null) {
			L.addParameter(pTypeId);
			L.addParameter(pComplexId);
		}
		return L;
	}

	
	public PresentationObject getColorButtonInfo() {
		Table T = new Table();
		T.setCellspacing(5);
		int col = 1;
		int row = 1;
		T.add(getBundle().getImage("green.gif"), col++, row);
		T.add(localize("greenbutton_info", "Has been allocated"), col, row);
		T.add(Text.getNonBrakingSpace(), col, row);
		T.add(Text.getNonBrakingSpace(), col, row);
		col++;
		T.add(getBundle().getImage("yellow.gif"), col++, row);
		T.add(localize("yellowbutton_info", "Has not accepted yet"), col, row);
		T.add(Text.getNonBrakingSpace(), col, row);
		T.add(Text.getNonBrakingSpace(), col, row);
		col++;
		T.add(getBundle().getImage("red.gif"), col++, row);
		T.add(localize("redbutton_info", "Not been allocated"), col, row);
		T.add(Text.getNonBrakingSpace(), col, row);
		T.add(Text.getNonBrakingSpace(), col, row);
		col++;
		T.add(getBundle().getImage("blue.gif"), col++, row);
		T.add(localize("bluebutton_info", "Wants off this list"), col, row);
		T.add(Text.getNonBrakingSpace(), col, row);
		T.add(Text.getNonBrakingSpace(), col, row);
		row++;
		col = 1;

		
		T.add(getBundle().getImage("purple.gif"),col++,row);
		T.add(localize("purplebutton_info","Allocation accepted"),col,row);
		T.add(Text.getNonBrakingSpace(),col,row);
		T.add(Text.getNonBrakingSpace(),col,row);
		

		col++;
		T.add(getBundle().getImage("brown.gif"), col++, row);
		T.add(localize("brownbutton_info", "Allocation accepted late"), col, row);
		T.add(Text.getNonBrakingSpace(), col, row);
		T.add(Text.getNonBrakingSpace(), col, row);
		col++;
		T.add(getBundle().getImage("black.gif"), col++, row);
		T.add(localize("blackbutton_info", "Denial"), col, row);
		T.add(Text.getNonBrakingSpace(), col, row);
		T.add(Text.getNonBrakingSpace(), col, row);
		col++;
		Text number12 = new Text(" 1 2 ");
		number12.setBold();
		number12.setFontColor("#00FF00");
		number12.setFontSize(Text.FONT_SIZE_14_HTML_4);
		Text number3 = new Text(" 3 ");
		number3.setBold();
		number3.setFontColor("#0000FF");
		number3.setFontSize(Text.FONT_SIZE_14_HTML_4);
		T.add(number12, col, row);
		T.add(number3, col++, row);
		T.add(localize("numberbutton_info", "Number of denials"), col++, row);
		return T;
	}
	public ContractHome getContractHome() throws RemoteException {
		return (ContractHome) IDOLookup.getHome(Contract.class);
	}
	private PresentationObject getContractMakingTable(
		IWContext iwc,
		Contract contract,
		Integer applicantID,
		IWTimestamp from,
		Integer apartmentID,
		WaitingList waitingList)
		throws RemoteException ,FinderException{
		DataTable T = getDataTable();
		Apartment apartment = null;
		if (apartmentID != null && apartmentID.intValue()>0) {
			apartment = campusService.getBuildingService().getApartmentHome().findByPrimaryKey(apartmentID);
			Floor F = apartment.getFloor();
			T.addTitle(
				apartment.getName()
					+ " "
					+ F.getName()
					+ " "
					+F.getBuilding().getName());
		}
		else
			T.addTitle(localize("contract_dates", "Contract dates"));
		int row = 1;
		SubmitButton save = new SubmitButton(getResourceBundle().getLocalizedImageButton("save", "Save"), "save_allocation", "true");
		setStyle(save);
		DateInput dateFrom = new DateInput("contract_date_from", true);
		DateInput dateTo = new DateInput("contract_date_to", true);
		Period validPeriod = campusService.getContractService().getValidPeriod(contract, apartment, dayBuffer, monthOverlap);
		int year = IWTimestamp.RightNow().getYear();
		dateFrom.setYearRange(year - 2, year + 5);
		dateTo.setYearRange(year - 2, year + 5);
		dateFrom.setDate((java.sql.Date) validPeriod.getFrom());
		dateTo.setDate((java.sql.Date) validPeriod.getTo());
		dateFrom.setStyleAttribute("style", styleAttribute);
		dateTo.setStyleAttribute("style", styleAttribute);
		if (applicantID.intValue() != -1) {
			HiddenInput Hid = new HiddenInput("applicantid", applicantID.toString());
			T.add(Hid);
		}
		if (apartmentID.intValue() != -1) {
			HiddenInput Hid = new HiddenInput("apartmentid", apartmentID.toString());
			T.add(Hid);
		}
		T.add(getHeader(localize("validfrom", "Valid from")), 1, row);
		T.add(dateFrom, 3, row);
		row++;
		T.add(getHeader(localize("validto", "Valid to")), 1, row);
		T.add(dateTo, 3, row);
		row++;
		if (contract != null) {
			SubmitButton delete =
				new SubmitButton(getResourceBundle().getLocalizedImageButton("delete", "Delete"), "delete_allocation", "true");
			setStyle(delete);
			//T.add(delete, 1, row);
			T.addButton(delete);

			T.add(new HiddenInput("contract_id", contract.getPrimaryKey().toString()));
			CheckBox incrementRejections = new CheckBox("increment_rejections","true");
			incrementRejections.setChecked(true);
			
			if(waitingList!=null){
				T.add(new HiddenInput("wl_id",waitingList.getPrimaryKey().toString()));
				if(waitingList.getRejectFlag()){
					T.add(new HiddenInput("reset_reject_flag","true"));
					incrementRejections.setChecked(false);	
				}

				T.add(getHeader(localize("increment_rejections","Increment rejections")),1,row);
				T.add(incrementRejections,3,row);
				

			}
			
			
			
		}
		//T.add(save, 3, row);
		T.addButton(save);
		return T;
	}
	
	private PresentationObject getContractsForm(
		IWContext iwc,
		Integer apartmentID,
		Integer applicantID,
		Integer contractID,
		IWTimestamp from,
		WaitingList waitingList)
		throws RemoteException, FinderException {
		Apartment apartment = campusService.getBuildingService().getApartmentHome().findByPrimaryKey(apartmentID);
		Contract contract =null;
		if(contractID!=null && contractID.intValue()>0)
			contract = campusService.getContractService().getContractHome().findByPrimaryKey(contractID);

		Form myForm = new Form();
		Table Frame = new Table();
		Frame.add(getContractMakingTable(iwc, contract, applicantID, from, apartmentID, waitingList));
		Frame.add(getApartmentContracts(apartmentID), 1, 3);
		myForm.add(Frame);

		myForm.add(new HiddenInput(pTypeId.getName(), pTypeId.getValueAsString()));
		myForm.add(new HiddenInput(pComplexId.getName(), pComplexId.getValueAsString()));

		return myForm;
	}
	private PresentationObject getContractTable(Integer contractID) {
		Table T = new Table();
		Form myForm = new Form();
		Contract eContract = null;
		try {
			eContract = getContractHome().findByPrimaryKey(contractID);
			Apartment A = campusService.getBuildingService().getApartmentHome().findByPrimaryKey(eContract.getApartmentId());
			Floor F = A.getFloor();
			Building B = F.getBuilding();
			Complex C = B.getComplex();
			T.add("Apartment", 1, 2);
			T.add(A.getName(), 1, 3);
			T.add(F.getName(), 2, 3);
			T.add(B.getName(), 3, 3);
			T.add(C.getName(), 4, 3);
			T.add(new SubmitButton("delete_allocation", "Delete"), 5, 3);
			T.add(new HiddenInput("contract_id", contractID.toString()));
		}
		catch (Exception ex) {
		}
		T.setRowColor(1, blueColor);
		T.setRowColor(4, redColor);
		T.mergeCells(1, 1, 5, 1);
		T.mergeCells(1, 2, 5, 2);
		T.mergeCells(1, 4, 5, 4);
		T.add(getText(" "), 1, 1);
		T.setHeight(1, bottomThickness);
		T.add(getText(" "), 1, 4);
		T.setHeight(4, bottomThickness);
		T.setCellpadding(1);
		T.setCellspacing(3);
		T.setBorder(0);
		myForm.add(T);


		myForm.add(new HiddenInput(pTypeId.getName(), pTypeId.getValueAsString()));
		myForm.add(new HiddenInput(pComplexId.getName(), pComplexId.getValueAsString()));

		return myForm;
	}
	
	private PresentationObject getFreeApartments(
		IWContext iwc,
		Integer applicantID,
		Contract contract)
		throws FinderException, RemoteException {
		//Map M = ContractFinder.mapOfAvailableApartmentContracts(type, complex);
		//List L = ContractFinder.listOfAvailable(ContractFinder.APARTMENT,AT.getID() ,CX.getID() );
		//List l = ContractFinder.listOfNonContractApartments(AT.getID() ,CX.getID());
//		Integer typeID = (Integer) apartmentType.getPrimaryKey();
//		Integer complexID = (Integer) complex.getPrimaryKey();
		//Map mapOfDates = contractService.getAvailableApartmentDates(typeID, cplxID);
		//Collection apartments = BuildingFinder.listOfApartmentsInTypeAndComplex(typeID.intValue(), cplxID.intValue());
		Collection apartments =campusService.getBuildingService().getApartmentViewHome().findByTypeAndComplex(typeID,complexID);
		boolean hasContract = contract != null ? true : false;
		Integer contractID = hasContract ? (Integer) contract.getPrimaryKey() : new Integer(-1);
		int currentApartmentID = hasContract ? contract.getApartmentId().intValue() : -1;
		boolean bcontracts = false;
		//if (M != null)
		//bcontracts = true;
		DataTable T = new DataTable();
		T.setTitlesHorizontal(true);
		T.addTitle(apartmentType.getName() + " " + complex.getName());
		int row = 1;
		T.add(getHeader(localize("name", "Name")), 2, row);
		T.add(getHeader(localize("floor", "Floor")), 3, row);
		T.add(getHeader(localize("building", "Building")), 4, row);
		T.add(getHeader(localize("rented_to", "Rented until")), 5, row);
		if (apartments != null && !apartments.isEmpty()) {
			int len = apartments.size();
			ApartmentView apartment;
			Floor floor;
			Building builder;
			row = 2;
			RadioButton RB1, RB2;
			Integer apartmentID;
			Date date;
			boolean Available = false;
			IWTimestamp nextAvailable;
			IWTimestamp today = IWTimestamp.RightNow();
			today.setAsDate();
			for (Iterator iter = apartments.iterator(); iter.hasNext();) {
				apartment = (ApartmentView) iter.next();
				apartmentID = (Integer) apartment.getPrimaryKey();
				boolean isThis = false;
				// Mark current apartment
				if (hasContract && currentApartmentID == apartmentID.intValue()) {
					isThis = true;
					TextFontColor = "#FF0000";
				}
				
				// List all apartments and show next availability ( date )
				nextAvailable = new IWTimestamp(campusService.getContractService().getNextAvailableDate(campusService.getBuildingService().getApartmentHome().findByPrimaryKey(apartmentID)).getTime());
				nextAvailable.setAsDate();
				
				
				T.add(getText(apartment.getApartmentName()), 2, row);
				T.add(getText(apartment.getFloorName()), 3, row);
				T.add(getText(apartment.getBuildingName()), 4, row);
				if (nextAvailable != null) {
					Text text = getText(nextAvailable.getLocaleDate(LocaleUtil.getIcelandicLocale()));
					if (nextAvailable.isEqualTo(IWTimestamp.RightNow()))
						text = getText(localize("today", "Today"));
					T.add(getApartmentContractsLink(text, applicantID, contractID, apartmentID, nextAvailable), 5, row);
				}
				if (isThis)
					TextFontColor = "#000000";
				row++;
			}
		}
		return T;
	}
	private Link getHomeLink() {
		Link L = new Link(getBundle().getImage("list.gif"));
		L.addParameter("list", "");
		return L;
	}
	
	
	public String getLocalizedNameKey() {
		return "allocator";
	}
	public String getLocalizedNameValue() {
		return "Allocator";
	}
	public PresentationObject getOffWaitingList(Integer wID) throws RemoteException{
		Form form = new Form();
		DataTable T = getDataTable();
		form.add(T);
		T.addTitle(localize("remove_from_waitinglist", "Remove from waitinglist"));
		T.setTitlesVertical(true);
		try {
			WaitingList wl = ((WaitingListHome) IDOLookup.getHome(WaitingList.class)).findByPrimaryKey(wID);
			Applicant applicant =((ApplicantHome) IDOLookup.getHome(Applicant.class)).findByPrimaryKey(wl.getApplicantId());
			ApartmentType type = campusService.getBuildingService().getApartmentTypeHome().findByPrimaryKey(wl.getApartmentTypeId());
			int row = 1;
			T.add(getHeader(localize("applicant", "Applicant")), 1, row);
			T.add(getText(applicant.getName()), 2, row);
			row++;
			T.add(getHeader(localize("apartment_type", "Apartment type")), 1, row);
			T.add(getText(type.getName()), 2, row);
			row++;
			T.add(getHeader(localize("last_confirmation", "Last confirmation")), 1, row);
			if (wl.getLastConfirmationDate() != null)
				T.add(getText(dateFormat.format(wl.getLastConfirmationDate())), 2, row);
			row++;
			T.add(getHeader(localize("allocation_denials", "Allocation denials")), 1, row);
			int count = wl.getNumberOfRejections();
			if (count < 0)
				count = 0;
			T.add(getText(String.valueOf(count)), 2, row);
			row++;
			SubmitButton remove =
				new SubmitButton(
					getResourceBundle().getLocalizedImageButton("remove", "Remove"),
					"remove_waitinglist",
					wID.toString());
			remove.setSubmitConfirm(
				localize("confirm_remove_waitinglist", "Are you sure you want to remove this ?"));
			SubmitButton reactivate =
				new SubmitButton(
					getResourceBundle().getLocalizedImageButton("reactivate", "Reactivate"),
					"reactivate_waitinglist",
					wID.toString());
			T.addButton(remove);
			if (wl.getRemovedFromList())
				T.addButton(reactivate);
			if (pTypeId != null && pComplexId != null) {
				form.addParameter(pTypeId.getName(),pTypeId.getValueAsString());
				form.addParameter(pComplexId.getName(),pComplexId.getValueAsString());
			}
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return form;
	}
	
	public Link getPDFLink(IWContext iwc,PresentationObject MO, int cam_app_id) {
		Link link = new Link(MO);
		link.setURL(iwc.getIWMainApplication().getMediaServletURI()+"application"+cam_app_id+".pdf");
	    link.addParameter(CampusApplicationWriter.PRM_WRITABLE_CLASS,IWMainApplication.getEncryptedClassName(CampusApplicationWriter.class));
		link.addParameter(CampusApplicationWriter.PRM_CAMPUS_APPLICATION_ID, cam_app_id);
		return link;
	}
	public Link getPDFLink(IWContext iwc,PresentationObject MO, int aprt_type_id, int cmplx_id) {
		Link link = new Link(MO);
		link.setURL(iwc.getIWMainApplication().getMediaServletURI()+"apps_type_"+aprt_type_id+"_complex_"+cmplx_id+".pdf");
	    link.addParameter(CampusApplicationWriter.PRM_WRITABLE_CLASS,IWMainApplication.getEncryptedClassName(CampusApplicationWriter.class));
	    link.addParameter(CampusApplicationWriter.PRM_APARTMENT_TYPE_ID, aprt_type_id);
	    link.addParameter(CampusApplicationWriter.PRM_COMPLEX_ID, cmplx_id);
		return link;
	}

	private String getStatus(String status) throws RemoteException {
		return campusService.getContractService().getLocalizedStatus(getResourceBundle(), status);
	}
	
	public Link getWaitingListOrderLink(String id,int order,int aprtTypeID,int complexID,int max){
		Link L = new Link(getText(String.valueOf(order)));
		L.setWindowToOpen(WaitingListOrganizerWindow.class);
		L.addParameter(WaitingListOrganizerWindow.COMPLEX_ID,complexID);
		L.addParameter(WaitingListOrganizerWindow.APARTMENT_TYPE_ID,aprtTypeID);
		L.addParameter(WaitingListOrganizerWindow.NUMBER_ON_LIST,order);
		L.addParameter(WaitingListOrganizerWindow.WL_ID,id);
		L.addParameter(WaitingListOrganizerWindow.MAX_LIST,max);
		return L;
		
	}

	private java.sql.Date getValidToDate(SystemProperties SysProps) {
		int years = SysProps.getContractYears();
		if (SysProps.getContractYears() > 0) {
			IWTimestamp now = IWTimestamp.RightNow();
			IWTimestamp iT = new IWTimestamp(1, now.getMonth(), now.getYear() + years);
			return iT.getDate();
		}
		else
			return SysProps.getContractDate();
	}
	
	private String getSubjectColor(Integer subjectId){
		try {
			if(subjectMap==null){
				Collection subjects = campusService.getApplicationService().getSubjectHome().findNonExpired();
				subjectMap = new HashMap(subjects.size());
				for (Iterator iter = subjects.iterator(); iter.hasNext();) {
					ApplicationSubject subject = (ApplicationSubject) iter.next();
					subjectMap.put((Integer)subject.getPrimaryKey(),subject);
				}
			}
			if(subjectMap.containsKey(subjectId)){
				return ((ApplicationSubject)subjectMap.get(subjectId)).getExtraAttribute();
			}
			else{
				ApplicationSubject subject = campusService.getApplicationService().getSubjectHome().findByPrimaryKey(subjectId);
				return subject.getExtraAttribute();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (EJBException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	private PresentationObject getWaitingList(IWContext iwc, Integer conID)
		throws FinderException, RemoteException {
		Image registerImage = getBundle().getImage("/pen.gif", localize("sign", "Sign"));
		Table Frame = new Table();
		Frame.setCellpadding(3);
	    Frame.setCellspacing(1);
		//Frame.addTitle(localize("applicants", "Applicants") + " " + apartmentType.getName());
		//Frame.setTitlesHorizontal(true);
		int row = 2;
		int col = 1;
		
		boolean ifLong = conID.intValue() < 0 ? true : false;
		Frame.add(getHeader(localize("nr", "Nr")), col++, row);
		Frame.add(getHeader(localize("priority", "Pr")), col++, row);
		Frame.add(getHeader(localize("refnum", "Ref. num")), col++, row);
		Frame.add(getHeader(localize("a", "A")), col++, row);
		Frame.add(getHeader(localize("name", "Name")), col++, row);
		Frame.add(getHeader(localize("ssn", "Socialnumber")), col++, row);
		Frame.add(getHeader(localize("residence", "Residence")), col++, row);
		if (ifLong)
			Frame.add(getHeader(localize("legal_residence", "Legal residence")), col++, row);
		Frame.add(getHeader(localize("mobile_phone", "Mobile phone")), col++, row);
		Frame.add(getHeader(localize("phone", "Phone")), col++, row);
	
		Collection waitingLists =
			getWaitingListHome().findByApartmentTypeAndComplex(typeID.intValue(), complexID.intValue());
		//java.util.Collection w_application = CampusApplicationFinder.listOfWaitinglistForTypeApplication(aprtTypeId, cmplxId);
		//java.util.Collection t_application = CampusApplicationFinder.listOfWaitinglistForTypeTransfer(aprtTypeId, cmplxId);
		//Hashtable HT = ContractFinder.hashOfApplicantsContracts();
		Map newApplicantContracts = campusService.getContractService().getNewApplicantContracts();
		Map priorityColorMap = campusService.getApplicationService().getPriorityColorMap();
		//ContractHome cHome = (ContractHome) IDOLookup.getHome(Contract.class);
		boolean bcontracts = false;
		Contract contract;
		if (newApplicantContracts != null)
			bcontracts = true;
		if (waitingLists != null && !waitingLists.isEmpty()) {
			int listSize = waitingLists.size();
			try {
				java.util.Iterator it = waitingLists.iterator();
				ApplicationHome applicationHome = (ApplicationHome) IDOLookup.getHome(Application.class);
				ApplicantHome applicantHome = (ApplicantHome) IDOLookup.getHome(Applicant.class);
				row++;
				String TempColor = "#000000";
				int con_id = -1;
				boolean redColorSet = false;
				int numberOnList = 1;
				IWTimestamp now = IWTimestamp.RightNow();
				
				ReferenceNumberHandler h =	new ReferenceNumberHandler();
				String key = ReferenceNumberHandler.getCypherKey(iwc);
				com.idega.util.CypherText ct = new com.idega.util.CypherText();
				
				Map colorMap = new HashMap();
				
				while (it.hasNext()) {
					col = 1;
					con_id = -1;
					WaitingList WL = (WaitingList) it.next();
					try {
						Applicant applicant = applicantHome.findByPrimaryKey(WL.getApplicantId());
						Application application = null;
						Integer applicantID = (Integer) applicant.getPrimaryKey();
						Integer applicationID = null;
						Collection approvedApplications =
							applicationHome.findByApplicantAndStatus(applicantID, Status.APPROVED.toString());
						if (approvedApplications != null && !approvedApplications.isEmpty()) {
							for (Iterator iter = approvedApplications.iterator(); iter.hasNext();) {
								application = (Application) iter.next();
							}
							applicationID = ((Integer) application.getPrimaryKey());
						}
						TextFontColor = TempColor;
						if (WL.getType().equals("T")) {
							TextFontColor = "#0000CC";
						}
						/* er thetta thitt skitamix Palli ??
						if (application.getSubjectId() == 15) {
							TextFontColor = "#00CC00";
						}*/
						if(application!=null){
							String subjectColor = getSubjectColor(new Integer(application.getSubjectId()));
							//System.out.println(application.getSubjectId()+ " subjectcolor = "+subjectColor);
							//if(subjectColor!=null)
							//	TextFontColor = subjectColor;
							colorMap.put(new Integer(row),subjectColor);
							//Frame.getContentTable().setRowColor(row,subjectColor);
						}
						//Frame.add(getText(String.valueOf(numberOnList)), col++, row);
						Frame.add(getWaitingListOrderLink(WL.getPrimaryKey().toString(),numberOnList,typeID.intValue(), complexID.intValue(),listSize), col++, row);
						numberOnList++;
						Frame.add(getText(WL.getPriorityLevel()), col++, row);
						if(priorityColorMap.containsKey(WL.getPriorityLevel()))
							colorMap.put(new Integer(row),priorityColorMap.get(WL.getPriorityLevel()));
						String cypher = null;
						if (application != null && applicationID.intValue() >0) {
							
							String id = application.getPrimaryKey().toString();
							while (id.length() < 6)
								id = "0" + id;
							cypher = ct.doCyper(id, key);
						}
						Frame.add(getText(cypher), col++, row);
						// already has allocation
						if (bcontracts && newApplicantContracts.containsKey(applicantID)) {
							contract = (Contract) newApplicantContracts.get(applicantID);
							Integer contractID = (Integer) contract.getPrimaryKey();
							if (contractID.intValue() == conID.intValue()) {
								TempColor = TextFontColor;
								TextFontColor = "FF0000";
								redColorSet = true;
							}
							IWTimestamp allocated = null, accepted = null;
							boolean isOnTime = true;
							if (contract.getStatusDate() != null) {
								allocated = new IWTimestamp(contract.getStatusDate());
								allocated.addSeconds(acceptanceSeconds);
								if (WL.getAcceptedDate() != null) {
									accepted = new IWTimestamp(WL.getAcceptedDate());
									isOnTime = accepted.isEarlierThan(allocated);
								}
								else {
									isOnTime = now.isEarlierThan(allocated);
								}
							}
							if (WL.getNumberOfRejections() > 0) {
								Frame.add(getDenialChangeLink(contractID, WL, isOnTime), col++, row);
							}
							// if accepted allocation
							else {
								Frame.add(getChangeLink(contractID, WL, isOnTime), col++, row);
							}
							con_id = contractID.intValue();
						}
						// if applicant has requested removal from list
						else if (WL.getRemovedFromList()) {
							Frame.add(getOffWaitingListLink(WL), col++, row);
						}
						// if applicant has rejected allocations
						else if (WL.getNumberOfRejections() > 0)
							Frame.add(getDenialChangeLink(null, WL, true), col++, row);
						else {
							Frame.add(getAllocateLink(applicant), col++, row);
						}
						Frame.add(getText(applicant.getFullName()), col++, row);
						Frame.add(getText(applicant.getSSN()), col++, row);
						Frame.add(getText(applicant.getResidence()), col++, row);
						if (ifLong)
							Frame.add(getText(applicant.getLegalResidence()), col++, row);
						Frame.add(getText(applicant.getMobilePhone()), col++, row);
						Frame.add(getText(applicant.getResidencePhone()), col++, row);
						if (redColorSet)
							TextFontColor = TempColor;
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					row++;
				}
				Frame.setHorizontalZebraColored(getZebraColor1(),getZebraColor2());
				Collection coloredRows = colorMap.entrySet();
				for (Iterator iter = coloredRows.iterator(); iter.hasNext();) {
					Map.Entry colorRow = (Map.Entry) iter.next();
					Frame.setRowColor(((Integer)colorRow.getKey()).intValue(),(String)colorRow.getValue());
					
					
				}
				  Text title = new Text(localize("applicants", "Applicants") + " " + apartmentType.getName(),true,false,false);
				    title.setFontColor("#FFFFFF");
				Frame.add(title,1,1);
				Frame.mergeCells(1,1,Frame.getColumns(),1);
				Frame.setRowColor(1,"#27334B");
				Frame.setRowColor(2,getHeaderColor());
				
			}
			catch (IDOLookupException e) {
				e.printStackTrace();
			}
		}
		else
			Frame.add(getHeader(localize("not_to_allocate", "Nothing to Allocate!")));
		return Frame;
	}
	public WaitingListHome getWaitingListHome() throws RemoteException {
		return (WaitingListHome) IDOLookup.getHome(WaitingList.class);
	}
	public Text headerText(String text) {
		Text T = new Text(text);
		T.setBold();
		T.setFontColor(this.WhiteColor);
		T.setFontSize(1);
		return T;
	}
	public void main(IWContext iwc) {
		//isStaff = com.idega.core.accesscontrol.business.AccessControl
		//debugParameters(iwc);
		isAdmin = iwc.hasEditPermission(this);
		try {
			control(iwc);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}
	private String saveAllocation(IWContext iwc) {
		String returner = localize("allocation_failure", "Allocation failure");
		String sContractId = iwc.getParameter("contract_id");
		String sApartmentId = iwc.getParameter("apartmentid");
		String sApplicantId = iwc.getParameter("applicantid");
		String sDateFrom = iwc.getParameter("contract_date_from");
		String sDateTo = iwc.getParameter("contract_date_to");
		String sMustFrom = iwc.getParameter("from");
		Integer contractID = null;
		Integer apartmentID = null;
		Integer applicantID = null;
		IWTimestamp validFrom = new IWTimestamp(iwc.getParameter("contract_date_from"));
		IWTimestamp validTo = new IWTimestamp(iwc.getParameter( "contract_date_to"));
		
		try {
			applicantID = Integer.valueOf(iwc.getParameter("applicantid"));
			apartmentID = Integer.valueOf(iwc.getParameter("apartmentid"));
			contractID = Integer.valueOf(iwc.getParameter("contract_id"));
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		/** @todo  contract overlap */
		IWTimestamp mustBeFrom = sMustFrom != null ? new IWTimestamp(sMustFrom) : null;
		try {
			campusService.getContractService().allocate(contractID,apartmentID,applicantID,validFrom.getDate(),validTo.getDate());
		}
		catch (RemoteException e1) {
			e1.printStackTrace();
			return e1.getMessage();
		}
		catch (AllocationException e1) {
			e1.printStackTrace();
			return e1.getMessage();
		}
		return localize("allocationSucceeded","Allocation succeeded");
	}
	/**
	 * @param i
	 */
	public void setAllowedRejections(int i) {
		allowedRejections = i;
	}
	public void setDayBuffer(int buffer) {
		dayBuffer = new Integer(buffer);
	}
	public void setOverlap(int overlap) {
		monthOverlap = new Integer(overlap);
	}
	protected void setStyle(InterfaceObject O) {
		O.setMarkupAttribute("style", this.styleAttribute);
	}
	
	public Text getHeader(int text){
		return getHeader(String.valueOf(text));
	}
	
	public Text getText(String text){
		Text t = super.getText(text);
		t.setFontColor(text);
		return t;
	}
}