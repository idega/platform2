/*
 * $Id: CampusAllocator.java,v 1.58 2004/06/04 17:34:21 aron Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.allocation.presentation;

import is.idega.idegaweb.campus.block.allocation.business.ContractBusiness;
import is.idega.idegaweb.campus.block.allocation.business.ContractFinder;
import is.idega.idegaweb.campus.block.allocation.business.WaitingListFinder;
import is.idega.idegaweb.campus.block.allocation.data.AllocationView;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean;
import is.idega.idegaweb.campus.block.allocation.data.ContractHome;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationFinder;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationHolder;
import is.idega.idegaweb.campus.block.application.data.WaitingList;
import is.idega.idegaweb.campus.block.application.data.WaitingListHome;
import is.idega.idegaweb.campus.block.application.presentation.ApplicationFilerWindow;
import is.idega.idegaweb.campus.block.application.presentation.CampusApprover;
import is.idega.idegaweb.campus.block.building.data.ApartmentTypePeriods;
import is.idega.idegaweb.campus.business.CampusService;
import is.idega.idegaweb.campus.data.ApartmentContracts;
import is.idega.idegaweb.campus.presentation.CampusBlock;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.ApplicantHome;
import com.idega.block.application.data.Application;
import com.idega.block.building.business.ApartmentTypeComplexHelper;
import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.business.BuildingFinder;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.ApartmentCategory;
import com.idega.block.building.data.ApartmentType;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.Floor;
import com.idega.core.user.data.User;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOStoreException;
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
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;
import com.sun.rsasign.c;

/**
 * @author  <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */
public class CampusAllocator extends CampusBlock {
	protected final int ACT1 = 1, ACT2 = 2, ACT3 = 3, ACT4 = 4, ACT5 = 5;
	
	private String redColor = "#942829";
	private String blueColor = "#27324B", lightBlue = "#ECEEF0";
	private Integer subjectId = null;
	private int dayBuffer = 1;
	private int monthOverlap = 1;
	private String sGlobalStatus = "S";
	private ListIterator iterator = null;
	private LinkedList linkedlist = null;
	private Integer typeId, complexId;
	private ApartmentType apartmentType;
	private Complex complex;
	private Parameter pTypeId = null, pComplexId = null;
	
	private String bottomThickness = "8";
	protected boolean isAdmin = false;
	protected String MiddleColor, LightColor, DarkColor, WhiteColor, TextFontColor, HeaderFontColor, IndexFontColor;
	protected int fontSize = 2;
	protected boolean fontBold = false;
	protected String styleAttribute = "font-size: 8pt";
	private DateFormat dateFormat;
	private int allowedRejections = 3;
	private int acceptanceSeconds = 60*60*24*3; // default 3 days
	private CampusService campusService;

	public String getLocalizedNameKey() {
		return "allocator";
	}

	public String getLocalizedNameValue() {
		return "Allocator";
	}

	public CampusAllocator() {
		LightColor = "#D7DADF";
		MiddleColor = "#9fA9B3";
		DarkColor = "#27334B";
		WhiteColor = "#FFFFFF";
		TextFontColor = "#000000";
		HeaderFontColor = DarkColor;
		IndexFontColor = "#000000";
	}

	public void setDayBuffer(int buffer) {
		dayBuffer = buffer;
	}

	public void setOverlap(int overlap) {
		monthOverlap = overlap;
	}

	protected void control(IWContext iwc)throws RemoteException {
		campusService = getCampusService(iwc);
		dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT,iwc.getCurrentLocale());
	
		fontSize = 1;
/*		if (iwc.getParameter("list") != null) {
		}*/

		if (iwc.getParameter("type_id") != null) {
			pTypeId = new Parameter("type_id", iwc.getParameter("type_id"));
			typeId = Integer.valueOf(iwc.getParameter("type_id"));
			try {
				apartmentType = campusService.getBuildingService().getApartmentTypeHome().findByPrimaryKey(typeId);
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			}
			
		}

		if (iwc.getParameter("cplx_id") != null) {
			pComplexId = new Parameter("cplx_id", iwc.getParameter("cplx_id"));
			complexId = Integer.valueOf(iwc.getParameter("cplx_id"));
			try {
				complex = campusService.getBuildingService().getComplexHome().findByPrimaryKey(complexId);
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
			if (typeId.intValue() > 0 && complexId.intValue() > 0) {
				// Allocate apartment to an applicant
				if (iwc.getParameter("allocate") != null) {
					Integer applicantId = Integer.valueOf(iwc.getParameter("allocate"));
					Integer waitinglistID = null;
					if(iwc.isParameterSet("wl_id"))
						waitinglistID = Integer.valueOf(iwc.getParameter("wl_id"));
					Frame.add(getApartmentsForm( applicantId, null,waitinglistID), 3, row);
					Frame.add(getApplicantInfo(applicantId, iwc), 1, row);
				}
				// Change allocation
				else if (iwc.getParameter("change") != null) {
					Integer contractId = Integer.valueOf(iwc.getParameter("change"));
					Integer applicantId = Integer.valueOf(iwc.getParameter("applicant"));
					Integer waitinglistID = new Integer(-1);
					if(iwc.isParameterSet("wl_id"))
					 	waitinglistID = Integer.valueOf(iwc.getParameter("wl_id"));
					Frame.add(getWaitingList(contractId, iwc), 1, row);
					Frame.add(getApartmentsForm( applicantId, contractId,waitinglistID), 3, row);
					row++;
					Frame.mergeCells(1,row,3,row);
					Frame.add(getColorButtonInfo(),1,row);
				}
				// show all contracts for apartment
				else if (iwc.getParameter("view_aprtmnt") != null) {
					Integer apartmentId = Integer.valueOf(iwc.getParameter("view_aprtmnt"));
					Integer contractId = Integer.valueOf(iwc.getParameter("contract"));
					Integer applicantId = Integer.valueOf(iwc.getParameter("applicant"));
					IWTimestamp from = new IWTimestamp(iwc.getParameter("from"));
					Frame.add(getApplicantInfo(applicantId, iwc), 1, row);
					Frame.add(getContractsForm(apartmentId, applicantId, contractId, from,null), 3, row);
				}
				// save allocation
				else if (iwc.getParameter("save_allocation") != null && iwc.getParameter("save_allocation").equals("true")) {
					String msg = saveAllocation(iwc);
					//System.err.println(msg);
					Text Te = formatText(msg);
					Te.setFontSize(3);
					Te.setFontColor("#FF0000");
					Frame.add(Te, 1, row++);
					Frame.add(getWaitingList(null, iwc), 1, row);
					row++;
					Frame.mergeCells(1,row,3,row);
					Frame.add(getColorButtonInfo(),1,row);
				}
				// delete allocation
				else if (iwc.getParameter("delete_allocation") != null) {
					deleteAllocation(iwc);
					Frame.add(getWaitingList( null, iwc), 1, row);
					row++;
					Frame.mergeCells(1,row,3,row);
					Frame.add(getColorButtonInfo(),1,row);
				}
				else if(iwc.isParameterSet("offwaitinglist")){
					Integer wID = new Integer(iwc.getParameter("offwaitinglist"));
					Integer wAID = new Integer(iwc.getParameter("wl_appid"));
					Frame.add(getApplicantInfo(wAID, iwc), 1, row);
					Frame.add(getOffWaitingList(wID),3,row);
				}
				else if(iwc.isParameterSet("remove_waitinglist")){
					Integer wID = new Integer(iwc.getParameter("remove_waitinglist"));
					try {
						WaitingList wl = ((WaitingListHome) IDOLookup.getHome(WaitingList.class)).findByPrimaryKey(wID);
						wl.remove();
					}
					catch (IDOLookupException e) {
						e.printStackTrace();
					}
					catch (EJBException e) {
						e.printStackTrace();
					}
					catch (FinderException e) {
						e.printStackTrace();
					}
					catch (RemoveException e) {
						e.printStackTrace();
					}
					Frame.add(getWaitingList(null, iwc), 1, row);
					row++;
					Frame.mergeCells(1,row,3,row);
					Frame.add(getColorButtonInfo(),1,row);
				}
				else if(iwc.isParameterSet("reactivate_waitinglist")){
					Integer wID = new Integer(iwc.getParameter("reactivate_waitinglist"));
				try {
					
					WaitingList wl = ((WaitingListHome) IDOLookup.getHome(WaitingList.class)).findByPrimaryKey(wID);
					wl.setRemovedFromList(is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.NO);
					wl.store();
					
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
					Frame.add(getWaitingList(null, iwc), 1, row);
					row++;
					Frame.mergeCells(1,row,3,row);
					Frame.add(getColorButtonInfo(),1,row);
			}
				// get Waitinglist for this type and complex
				else{
					Frame.add(getWaitingList( null, iwc), 1, row);
					row++;
					Frame.mergeCells(1,row,3,row);
					Frame.add(getColorButtonInfo(),1,row);
				}
			}
			// get type and complex list
			else
				Frame.add(getCategoryLists(), 1, row);

			Frame.setRowVerticalAlignment(2, "top");
			add(Frame);
		}
		else
			add(getNoAccessObject(iwc));
	}

	private PresentationObject getCategoryLists() {
		DataTable T = new DataTable();
		T.setTitlesHorizontal(true);
		T.addTitle(localize("apartment_category", "Apartment category"));
		T.setWidth("100%");
		List Categories = BuildingFinder.listOfApartmentCategory();
		Hashtable allocationView = WaitingListFinder.getAllocationView();
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
				List L = (List)allocationView.get((Integer)AC.getPrimaryKey());//BuildingFinder.getApartmentTypesComplexForCategory(AC.getID());
				if (L != null) {
					int lLen = L.size();
					int catlist = 0, catfree = 0, catcont = 0, catapp = 0, catcnt1 = 0, catcnt2 = 0, catcnt3 = 0;
					T.add(boldText(AC.getName()), 1, row);
					row++;
					for (int j = 0; j < lLen; j++) {
//						ApartmentTypeComplexHelper eAprtType = (ApartmentTypeComplexHelper) L.get(j);
						AllocationView view = (AllocationView)L.get(j);
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

						T.add(getPDFLink(printImage), 1, row);
						T.add(getListLink(name.toString(),typeId.intValue(),complexId.intValue()), 2, row);
						T.add(formatText(listCount), 3, row);
						T.add(formatText(freeCount), 4, row);
						T.add(formatText(appliedCount), 5, row);
						T.add(formatText(appCnt1), 6, row);
						T.add(formatText(appCnt2), 7, row);
						T.add(formatText(appCnt3), 8, row);
						row++;
					}
					T.add(boldText(catlist), 3, row);
					T.add(boldText(catfree), 4, row);
					T.add(boldText(catapp), 5, row);
					T.add(boldText(catcnt1), 6, row);
					T.add(boldText(catcnt2), 7, row);
					T.add(boldText(catcnt3), 8, row);
					row++;
				}
			}

			T.add(boldText(localize("apartment_category", "Apartment category")), 1, 1);
			T.add(boldText(localize("apartments", "Apartments")), 3, 1);
			T.add(boldText(localize("available", "Available")), 4, 1);
			T.add(boldText(localize("applied", "Applied")), 5, 1);
			T.add(boldText(localize("choice1", "1.Choice")), 6, 1);
			T.add(boldText(localize("choice2", "2.Choice")), 7, 1);
			T.add(boldText(localize("choice3", "3.Choice")), 8, 1);
			T.add(getPDFLink(printImage), 1, row);
			T.add(boldText(totalCount), 3, row);
			T.add(boldText(totalFree), 4, row);
			T.add(boldText(totalApplied), 5, row);
			T.add(boldText(totApp1), 6, row);
			T.add(boldText(totApp2), 7, row);
			T.add(boldText(totApp3), 8, row);
			row++;
		}

		return T;
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
					T.add(boldText(AC.getName()), 1, row);
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
					T.add(boldText(catlist), 3, row);
					T.add(boldText(catfree), 4, row);
					T.add(boldText(catapp), 5, row);
					T.add(boldText(catcnt1), 6, row);
					T.add(boldText(catcnt2), 7, row);
					T.add(boldText(catcnt3), 8, row);
					row++;
				}
			}

			T.add(boldText(localize("apartment_category", "Apartment category")), 1, 1);
			T.add(boldText(localize("apartments", "Apartments")), 3, 1);
			T.add(boldText(localize("available", "Available")), 4, 1);
			T.add(boldText(localize("applied", "Applied")), 5, 1);
			T.add(boldText(localize("choice1", "1.Choice")), 6, 1);
			T.add(boldText(localize("choice2", "2.Choice")), 7, 1);
			T.add(boldText(localize("choice3", "3.Choice")), 8, 1);
			T.add(getPDFLink(printImage, -1, -1), 1, row);
			T.add(boldText(totalCount), 3, row);
			T.add(boldText(totalFree), 4, row);
			T.add(boldText(totalApplied), 5, row);
			T.add(boldText(totApp1), 6, row);
			T.add(boldText(totApp2), 7, row);
			T.add(boldText(totApp3), 8, row);
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

	private Link getListLink(String name,int type,int complex) {
		Link L = new Link(name);
		L.setFontSize(fontSize);
		L.addParameter("type_id", type);
		L.addParameter("cplx_id", complex);
		return L;
	}

	private PresentationObject getWaitingList(Integer contractId, IWContext iwc) {
		Image registerImage = getBundle().getImage("/pen.gif", localize("sign", "Sign"));
		DataTable Frame = new DataTable();
		ApartmentType apType = BuildingCacher.getApartmentType(typeId.intValue());
		Frame.addTitle(localize("applicants", "Applicants")+" "+apType.getName());
		Frame.setTitlesHorizontal(true);
		int row = 1;
		int col = 1;
		boolean ifLong = contractId.intValue() < 0 ? true : false;

		Frame.add(getHeader(localize("nr", "Nr")), col++, row);
		Frame.add(getHeader(localize("priority", "Pr")), col++, row);
		Frame.add(getHeader(localize("refnum", "Ref. num")), col++, row);
		Frame.add(getHeader(localize("a", "A")), col++, row);
		Frame.add(getHeader(localize("name", "Name")), col++, row);
		Frame.add(getHeader(localize("ssn", "Socialnumber")), col++, row);
		Frame.add(getHeader(localize("residence", "Residence")), col++, row);
		if (ifLong)
			Frame.add(formatText(localize("legal_residence", "Legal residence")), col++, row);
		Frame.add(formatText(localize("mobile_phone", "Mobile phone")), col++, row);
		Frame.add(formatText(localize("phone", "Phone")), col++, row);

		java.util.Collection L = CampusApplicationFinder.listOfWaitinglist(typeId.intValue(), complexId.intValue());
		java.util.Collection w_application = CampusApplicationFinder.listOfWaitinglistForTypeApplication(typeId.intValue(), complexId.intValue());
		java.util.Collection t_application = CampusApplicationFinder.listOfWaitinglistForTypeTransfer(typeId.intValue(), complexId.intValue());
		//Hashtable HT = ContractFinder.hashOfApplicantsContracts();
    	Map HT = ContractFinder.mapOfNewContractsByApplicantID();
    	//ContractHome cHome = (ContractHome) IDOLookup.getHome(Contract.class);
		boolean bcontracts = false;
		Contract C;
		if (HT != null)
			bcontracts = true;
		if (L != null) {
			int listCount = L.size();
			java.util.Iterator it = L.iterator();
			row = 2;
			String TempColor = "#000000";
			Integer con_id = null;
			boolean redColorSet = false;
			int numberOnList = 1;
			IWTimestamp now = IWTimestamp.RightNow();
			int wlID = -1;
			while (it.hasNext()) {
				col = 1;
				con_id =null;
				WaitingList WL = (WaitingList) it.next();
				wlID = ((Integer)WL.getPrimaryKey()).intValue();
				try {
					Applicant A = ((ApplicantHome) IDOLookup.getHomeLegacy(Applicant.class)).findByPrimaryKeyLegacy(WL.getApplicantId().intValue());

					Application app = CampusApplicationFinder.getLastApprovedApplication(A);
					TextFontColor = TempColor;
						
					if (WL.getType().equals("T")) {
						TextFontColor = "#0000CC";
					}
					
					if (app.getSubjectId() == 17) {
						TextFontColor = "#00CC00";
					}

					//Frame.add(formatText(numberOnList), col++, row);
					Frame.add(getWaitingListOrderLink(wlID,numberOnList,listCount), col++, row);
					numberOnList++;
					Frame.add(formatText(WL.getPriorityLevel()), col++, row);
					String cypher = null;
					if (app != null && app.getID() != -1) {
						com.idega.block.application.business.ReferenceNumberHandler h = new com.idega.block.application.business.ReferenceNumberHandler();
						String key = h.getCypherKey(iwc);
						com.idega.util.CypherText ct = new com.idega.util.CypherText();

						String id = Integer.toString(app.getID());
						while (id.length() < 6)
							id = "0" + id;

						cypher = ct.doCyper(id, key);
					}
					Frame.add(formatText(cypher), col++, row);

					// already has allocation
					if (bcontracts && HT.containsKey(new Integer(A.getID()))) {
						C = (Contract) HT.get(new Integer(A.getID()));
						if (((Integer)C.getPrimaryKey()).intValue() == contractId.intValue() ) {
							TempColor = TextFontColor;
							TextFontColor = "FF0000";
							redColorSet = true;
						}
						IWTimestamp allocated = null,accepted = null;
						boolean isOnTime = true;
						if(C.getStatusDate()!=null){
							allocated = new IWTimestamp(C.getStatusDate());
							allocated.addSeconds(acceptanceSeconds);
							if(WL.getAcceptedDate()!=null){
								accepted = new IWTimestamp(WL.getAcceptedDate());
								isOnTime = accepted.isEarlierThan(allocated);
							}
							else{
								isOnTime = now.isEarlierThan(allocated);
							}
							
						}
						
						if(WL.getNumberOfRejections()>0){
							Frame.add(getDenialChangeLink((Integer)C.getPrimaryKey(),WL,isOnTime),col++,row);
						}
						// if accepted allocation
						else{				
							Frame.add(getChangeLink((Integer)C.getPrimaryKey(), WL,isOnTime), col++, row);
						}
						con_id = (Integer)C.getPrimaryKey();
					}
					// if applicant has requested removal from list
					else if(WL.getRemovedFromList()){
						Frame.add(getOffWaitingListLink(WL),col++,row);
					}
					// if applicant has rejected allocations
					else if(WL.getNumberOfRejections()>0)
						Frame.add(getDenialChangeLink(null,WL,true),col++,row);
					else {
						Frame.add(getAllocateLink(A.getID()), col++, row);
					}

					Frame.add(formatText(A.getFullName()), col++, row);
					Frame.add(formatText(A.getSSN()), col++, row);
					Frame.add(formatText(A.getResidence()), col++, row);
					if (ifLong)
						Frame.add(formatText(A.getLegalResidence()), col++, row);
					Frame.add(formatText(A.getMobilePhone()), col++, row);
					Frame.add(formatText(A.getResidencePhone()), col++, row);

					if (redColorSet)
						TextFontColor = TempColor;
				}
				catch (SQLException sql) {
				}
				row++;
			}

		}
		else
			Frame.add(formatText(localize("not_to_allocate", "Nothing to Allocate!")));

		return Frame;
	}

	private Link getAllocateLink(int id) {
		Link L = new Link(getBundle().getImage("red.gif"));
		L.addParameter("allocate", String.valueOf(id));
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

	private Link getApartmentContractsLink(Text display, Integer applicantId, Integer contractId, Integer apartmentId, IWTimestamp from) {
		Link L = new Link(display);
		L.addParameter("view_aprtmnt", apartmentId.toString());
		L.addParameter("contract", contractId.toString());
		L.addParameter("applicant", applicantId.toString());
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
				L.addParameter("allocate", waitingList.getApplicantId().toString());
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

	private Link getChangeLink(Integer contractID,WaitingList waitingList,boolean onTime) {
		Image color = getBundle().getImage(onTime? "green.gif" :  "yellow.gif");
		if(waitingList.getAcceptedDate()!=null)
			color = getBundle().getImage(onTime?"purple.gif":"brown.gif");
		Link L = new Link(color);
		L.addParameter("change", contractID.toString());
		L.addParameter("applicant", waitingList.getApplicantId().toString());
		L.setToolTip(localize("tooltip_alloc_change","Change"));
		L.addParameter("wl_id",waitingList.getPrimaryKey().toString());
		if (pTypeId != null && pComplexId != null) {
			L.addParameter(pTypeId);
			L.addParameter(pComplexId);
		}
		return L;
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
	
	public PresentationObject getOffWaitingList(Integer wID){
		Form form = new Form();
		DataTable T = new DataTable();
		form.add(T);
		T.addTitle(localize("remove_from_waitinglist","Remove from waitinglist"));
		T.setTitlesVertical(true);
		try {
			WaitingList wl = ((WaitingListHome) IDOLookup.getHome(WaitingList.class)).findByPrimaryKey(wID);
			Applicant applicant = ((ApplicantHome)IDOLookup.getHome(Applicant.class)).findByPrimaryKey(wl.getApplicantId());
			ApartmentType type= BuildingCacher.getApartmentType(wl.getApartmentTypeId().intValue());
			int row = 1;
			T.add(formatText(localize("applicant","Applicant")),1,row);
			T.add(formatText(applicant.getName()),2,row);
			row++;
			T.add(formatText(localize("apartment_type","Apartment type")),1,row);
			T.add(formatText(type.getName()),2,row);
			row++;
			T.add(formatText(localize("last_confirmation","Last confirmation")),1,row);
			if(wl.getLastConfirmationDate()!=null)
				T.add(formatText(
				dateFormat.format(
				wl.getLastConfirmationDate())),2,row);
			row++;
			T.add(formatText(localize("allocation_denials","Allocation denials")),1,row);
			int count = wl.getNumberOfRejections();
			if(count<0)
				count = 0;
			T.add(formatText(String.valueOf(count)),2,row);
			row++;
			SubmitButton remove = new SubmitButton(getResourceBundle().getLocalizedImageButton("remove","Remove"),"remove_waitinglist",wID.toString());
			remove.setSubmitConfirm(localize("confirm_remove_waitinglist","Are you sure you want to remove this ?"));
			SubmitButton reactivate = new SubmitButton(getResourceBundle().getLocalizedImageButton("reactivate","Reactivate"),"reactivate_waitinglist",wID.toString());
			T.addButton(remove);
			if(wl.getRemovedFromList())
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
	
	public PresentationObject getColorButtonInfo(){
		Table T = new Table();
		T.setCellspacing(5);
		int col = 1;
		int row = 1;
		T.add(getBundle().getImage("green.gif"),col++,row);
		T.add(localize("greenbutton_info","Has been allocated"),col,row);
		T.add(Text.getNonBrakingSpace(),col,row);
		T.add(Text.getNonBrakingSpace(),col,row);
		
		col++;
		T.add(getBundle().getImage("yellow.gif"),col++,row);
		T.add(localize("yellowbutton_info","Has not accepted yet"),col,row);
		T.add(Text.getNonBrakingSpace(),col,row);
		T.add(Text.getNonBrakingSpace(),col,row);
			
		col++;
		T.add(getBundle().getImage("red.gif"),col++,row);
		T.add(localize("redbutton_info","Not been allocated"),col,row);
		T.add(Text.getNonBrakingSpace(),col,row);
		T.add(Text.getNonBrakingSpace(),col,row);
			
		col++;
		T.add(getBundle().getImage("blue.gif"),col++,row);
		T.add(localize("bluebutton_info","Wants off this list"),col,row);
		T.add(Text.getNonBrakingSpace(),col,row);
		T.add(Text.getNonBrakingSpace(),col,row);
		
		row++;
		col = 1;
		
		T.add(getBundle().getImage("purple.gif"),col++,row);
		T.add(localize("purplebutton_info","Allocation accepted"),col,row);
		T.add(Text.getNonBrakingSpace(),col,row);
		T.add(Text.getNonBrakingSpace(),col,row);
		
		col++;
		T.add(getBundle().getImage("brown.gif"),col++,row);
		T.add(localize("brownbutton_info","Allocation accepted late"),col,row);
		T.add(Text.getNonBrakingSpace(),col,row);
		T.add(Text.getNonBrakingSpace(),col,row);
			
		col++;
		T.add(getBundle().getImage("black.gif"),col++,row);
		T.add(localize("blackbutton_info","Denial"),col,row);
		T.add(Text.getNonBrakingSpace(),col,row);
		T.add(Text.getNonBrakingSpace(),col,row);
		
		col++;
		Text number12 = new Text(" 1 2 ");
		number12.setBold();
		number12.setFontColor("#00FF00");
		number12.setFontSize(Text.FONT_SIZE_14_HTML_4);
		Text number3 = new Text(" 3 ");
		number3.setBold();
		number3.setFontColor("#0000FF");
		number3.setFontSize(Text.FONT_SIZE_14_HTML_4);
		T.add(number12,col,row);
		T.add(number3,col++,row);
		T.add(localize("numberbutton_info","Number of denials"),col++,row);
			
		return T;
	}

	private Link getHomeLink() {
		Link L = new Link(getBundle().getImage("list.gif"));
		L.addParameter("list", "");
		return L;
	}

	public PresentationObject getApplicantInfo(Integer applicantId, IWContext iwc) {
		CampusApprover CA = new CampusApprover();
		PresentationObject MO = new Table();

		CampusApplicationHolder AH = CampusApplicationFinder.getApplicantInfo(applicantId.intValue());
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

			Frame.add(CA.getViewApartment(AH.getCampusApplication(), AH.getApplied(), iwc ), 1, 2);

			MO = Frame;
		}
		else
			add("er null");

		return MO;
	}

	private PresentationObject getApartmentsForm( Integer applicantId, Integer contractId,Integer waitingListID) {
		Form myForm = new Form();
		Contract C = null;
		try {
			C = campusService.getContractService().getContractHome().findByPrimaryKey(contractId);
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (FinderException e1) {
			e1.printStackTrace();
		}
		
		WaitingList waitingList = null;
		
		if(waitingListID.intValue()>0){
			
			try {
				waitingList = ((WaitingListHome)IDOLookup.getHome(WaitingList.class)).findByPrimaryKey(waitingListID); 
			}
			catch (IDOLookupException e) {
				e.printStackTrace();
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
		if (C != null)
			Frame.add(getContractMakingTable(C, applicantId,null,(Apartment) null,waitingList), 1, 1);
		else
			Frame.add(getFreeApartments( applicantId, C), 1, 1);
		myForm.add(Frame);

		myForm.add(new HiddenInput(pTypeId.getName(), pTypeId.getValueAsString()));
		myForm.add(new HiddenInput(pComplexId.getName(), pComplexId.getValueAsString()));
		return myForm;
	}

	private PresentationObject getContractsForm(Integer apartmentId, Integer applicantId, Integer contractId, IWTimestamp from,WaitingList waitingList)throws RemoteException {
		Contract C = null;
		ApartmentTypePeriods ATP = null;
		Apartment A = null;
	
		try {
			C = campusService.getContractService().getContractHome().findByPrimaryKey(contractId);
			A = C.getApartment();
			
			ATP = getPeriod();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

		Form myForm = new Form();
		Table Frame = new Table();

		Frame.add(getContractMakingTable(C, applicantId, from, A,waitingList));
		Frame.add(getApartmentContracts(apartmentId), 1, 3);
		myForm.add(Frame);

		myForm.add(new HiddenInput(pTypeId.getName(), pTypeId.getValueAsString()));
		myForm.add(new HiddenInput(pComplexId.getName(), pComplexId.getValueAsString()));
		return myForm;
	}

	private PresentationObject getApartmentContracts(Integer apartmentId) {
		/** @todo try to use jdbc cursors in sql queries */
		int iCount = Integer.parseInt(getBundle().getProperty("alloc_aprt_cntr_count", "10"));
		DataTable T = new DataTable();
		T.addTitle((localize("apartment_contracts", "Apartment contracts")));
		T.setTitlesHorizontal(true);
		int row = 1;
		T.add(formatText(localize("tenant", "Tenant")), 1, row);
		T.add(formatText(localize("valid_from", "From")), 2, row);
		T.add(formatText(localize("valid_to", "To")), 3, row);
		T.add(formatText(localize("status", "Status")), 4, row);
		List L = ContractFinder.listOfApartmentContracts(apartmentId.intValue());
		java.util.Map M = ContractFinder.mapOfApartmentUsersBy(ContractFinder.listOfApartmentUsers(apartmentId.intValue()));
		if (L != null) {
			java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT);
			java.util.Iterator I = L.iterator();
			Contract C;
			User user;
			Integer UID;
			IWTimestamp temp = null;
			row = 2;
			while (I.hasNext() && iCount-- > 0) {
				C = (Contract) I.next();
				UID = C.getUserId();
				if (M != null && M.containsKey(UID)) {
					user = (User) M.get(UID);
					T.add(formatText(user.getName()), 1, row);
				}
				T.add(formatText(df.format((java.util.Date) C.getValidFrom())), 2, row);
				T.add(formatText(df.format((java.util.Date) C.getValidTo())), 3, row);
				T.add(formatText(getStatus(C.getStatus())), 4, row);
				T.add(new HiddenInput("apartmentid", apartmentId.toString()));
				row++;
			}
		}
		return T;
	}

	private PresentationObject getFreeApartments( Integer applicantId, Contract C) {

		java.util.Map M = ContractFinder.mapOfAvailableApartmentContracts(typeId.intValue(), complexId.intValue());
		//List L = ContractFinder.listOfAvailable(ContractFinder.APARTMENT,AT.getID() ,CX.getID() );
		//List l = ContractFinder.listOfNonContractApartments(AT.getID() ,CX.getID());
		List L = BuildingFinder.listOfApartmentsInTypeAndComplex(typeId.intValue(), complexId.intValue());

		boolean hasContract = C != null ? true : false;
		Integer contractId = hasContract ? (Integer)C.getPrimaryKey() : new Integer(-1);
		Integer currentApartmentID = hasContract? C.getApartmentId():new Integer(-1);
		boolean bcontracts = false;
		if (M != null)
			bcontracts = true;
		DataTable T = new DataTable();
		T.setTitlesHorizontal(true);
		T.addTitle(apartmentType.getName() + " " + complex.getName());
		int row = 1;
		T.add(formatText(localize("name", "Name")), 2, row);
		T.add(formatText(localize("floor", "Floor")), 3, row);
		T.add(formatText(localize("building", "Building")), 4, row);
		T.add(formatText(localize("rented_to", "Rented until")), 5, row);
		if (L != null) {
			int len = L.size();
			Apartment A;
			Floor F;
			Building B;

			row = 2;
			RadioButton RB1, RB2;
			Integer apId;
			boolean Available = false;
			IWTimestamp nextAvailable;
			for (int i = 0; i < len; i++) {
				A = (Apartment) L.get(i);
				apId = (Integer)A.getPrimaryKey();

				RB1 = new RadioButton("apartmentid", apId.toString());
				//RB2 = new RadioButton("next_apartmentid",apId.toString());
				boolean isThis = false;

				// Mark current apartment
				if (hasContract && currentApartmentID.intValue() == apId.intValue()) {
					//if(C.getStatus().equals(C.statusCreated ) ||C.getStatus().equals(C.statusPrinted  ) )
					RB1.setSelected();

					isThis = true;
					TextFontColor = "#FF0000";
				}

				// List all apartments and show next availability ( date )
				nextAvailable = null;
				if (M != null && M.containsKey(apId)) {
					ApartmentContracts AC = (ApartmentContracts) M.get(apId);
					//System.err.println(" contractcount "+AC.getContracts().size());
					nextAvailable = AC.getNextDate();
					Available = false;
				}
				else {
					Available = true;
				}

				// If apartment is not in contract table:
				if (Available || isThis) {
					if (A.getUnavailableUntil() != null) {
						IWTimestamp it = new IWTimestamp(A.getUnavailableUntil());
            			nextAvailable = it;
						/*
            			if (!it.isLaterThan(IWTimestamp.RightNow())) {
							T.add(RB1, 1, row);
						}
            			*/
					}
					else {
            			nextAvailable = IWTimestamp.RightNow();
						//T.add(RB1, 1, row);
					}
				}

				T.add(formatText(A.getName()), 2, row);
				F = BuildingCacher.getFloor(A.getFloorId());
				T.add(formatText(F.getName()), 3, row);
				T.add(formatText((BuildingCacher.getBuilding(F.getBuildingId())).getName()), 4, row);
				if (nextAvailable != null) {
					Text text = formatText(nextAvailable.getLocaleDate(LocaleUtil.getIcelandicLocale()));
          		if(nextAvailable.equals(IWTimestamp.RightNow()))
            		text = formatText(localize("today","� dag"));
					T.add(getApartmentContractsLink(text, applicantId, contractId, (Integer)A.getPrimaryKey(), nextAvailable), 5, row);
				}

				if (isThis)
					TextFontColor = "#000000";
				row++;
			}
		}
		return T;
	}

	private PresentationObject getContractMakingTable(Contract C,  Integer applicantId, IWTimestamp from, Apartment apartment,WaitingList waitingList) {
		DataTable T = new DataTable();
		Integer apartmentId = null;
		if (apartment!=null) {
			apartmentId = (Integer)apartment.getPrimaryKey();
			Floor F = BuildingCacher.getFloor(apartment.getFloorId());
			T.addTitle(apartment.getName() + " " + F.getName() + " " + BuildingCacher.getBuilding(F.getBuildingId()).getName());
		}
		else
			T.addTitle(localize("contract_dates", "Contract dates"));

		int row = 1;
		SubmitButton save = new SubmitButton(getResourceBundle().getLocalizedImageButton("save", "Save"), "save_allocation", "true");

		setStyle(save);
		DateInput dateFrom = new DateInput("contract_date_from", true);
		DateInput dateTo = new DateInput("contract_date_to", true);

		IWTimestamp contractDateFrom = new IWTimestamp();
		IWTimestamp contractDateTo = new IWTimestamp();
		ApartmentTypePeriods ATP = getPeriod();
		if (C != null) {
			contractDateTo = new IWTimestamp(C.getValidTo());
			contractDateFrom = new IWTimestamp(C.getValidFrom());
		}
		else if (ATP != null) {
			// Period checking
			//System.err.println("ATP exists");
			IWTimestamp[] stamps = ContractBusiness.getContractStampsFromPeriod(ATP, monthOverlap);
			contractDateTo = stamps[1];
			contractDateFrom = stamps[0];

			if (dayBuffer > 0) {
				contractDateFrom.addDays(dayBuffer);
			}
			// end of Period checks
		}
		// are the System Properties set
		/*else if (SysProps != null) {
			contractDateTo = new IWTimestamp(SysProps.getValidToDate());
			contractDateFrom = new IWTimestamp();
		}*/
		else {
			contractDateTo = new IWTimestamp();
			contractDateFrom = new IWTimestamp();
		}
		if (from != null) {
			contractDateFrom = from;
			T.add(new HiddenInput("from", from.toString()));
		}

		int year = IWTimestamp.RightNow().getYear();
		dateFrom.setYearRange(year - 2, year + 5);
		dateTo.setYearRange(year - 2, year + 5);
		dateFrom.setDate(contractDateFrom.getSQLDate());
		dateTo.setDate(contractDateTo.getSQLDate());
		dateFrom.setStyleAttribute("style", styleAttribute);
		dateTo.setStyleAttribute("style", styleAttribute);
		if (applicantId!=null && applicantId.intValue() != -1) {
			HiddenInput Hid = new HiddenInput("applicantid", applicantId.toString());
			T.add(Hid);
		}
    if (apartmentId!=null && apartmentId.intValue() != -1) {
			HiddenInput Hid = new HiddenInput("apartmentid", apartmentId.toString());
			T.add(Hid);
		}
		T.add(formatText(localize("validfrom", "Valid from")), 1, row);
		T.add(dateFrom, 3, row);

		row++;
		T.add(formatText(localize("validto", "Valid to")), 1, row);
		T.add(dateTo, 3, row);

		row++;
		
		if (C != null) {
			SubmitButton delete =new SubmitButton(getResourceBundle().getLocalizedImageButton("delete", "Delete"), "delete_allocation", "true");
			setStyle(delete);
			//T.add(delete, 1, row);
			T.addButton(delete);
			T.add(new HiddenInput("contract_id", C.getPrimaryKey().toString()));
			CheckBox incrementRejections = new CheckBox("increment_rejections","true");
			incrementRejections.setChecked(true);
			
			if(waitingList!=null){
				T.add(new HiddenInput("wl_id",waitingList.getPrimaryKey().toString()));
				if(waitingList.getRejectFlag()){
					T.add(new HiddenInput("reset_reject_flag","true"));
					incrementRejections.setChecked(false);	
				}
				T.add(formatText(localize("increment_rejections","Increment rejections")),1,row);
				T.add(incrementRejections,3,row);
				
			}
			
			
			
		}
		//T.add(save, 3, row);
		T.addButton(save);

		return T;
	}

	private PresentationObject getContractTable(Integer contractId) {
		Table T = new Table();

		Form myForm = new Form();
		Contract eContract = null;
		try {
			eContract = campusService.getContractService().getContractHome().findByPrimaryKey(contractId);
			Apartment A = eContract.getApartment();
			Floor F = A.getFloor();
			Building B = F.getBuilding();
			Complex C = B.getComplex();
			T.add("Apartment", 1, 2);
			T.add(A.getName(), 1, 3);
			T.add(F.getName(), 2, 3);
			T.add(B.getName(), 3, 3);
			T.add(C.getName(), 4, 3);
			T.add(new SubmitButton("delete_allocation", "Delete"), 5, 3);
			T.add(new HiddenInput("contract_id", contractId.toString()));
		}
		catch (Exception ex) {

		}
		T.setRowColor(1, blueColor);
		T.setRowColor(4, redColor);
		T.mergeCells(1, 1, 5, 1);
		T.mergeCells(1, 2, 5, 2);
		T.mergeCells(1, 4, 5, 4);
		T.add(formatText(" "), 1, 1);
		T.setHeight(1, bottomThickness);
		T.add(formatText(" "), 1, 4);
		T.setHeight(4, bottomThickness);
		T.setCellpadding(1);
		T.setCellspacing(3);
		T.setBorder(0);
		myForm.add(T);

		myForm.add(new HiddenInput(pTypeId.getName(), pTypeId.getValueAsString()));
		myForm.add(new HiddenInput(pComplexId.getName(), pComplexId.getValueAsString()));

		return myForm;
	}

	private String saveAllocation(IWContext iwc) {
		String returner = localize("allocation_failure", "Allocation failure");
		String sContractId = iwc.getParameter("contract_id");
		String sApartmentId = iwc.getParameter("apartmentid");
		String sApplicantId = iwc.getParameter("applicantid");
		String sDateFrom = iwc.getParameter("contract_date_from");
		String sDateTo = iwc.getParameter("contract_date_to");
		String sMustFrom = iwc.getParameter("from");
		/** @todo  contract overlap */
		IWTimestamp mustBeFrom = sMustFrom != null ? new IWTimestamp(sMustFrom) : null;
		mustBeFrom.setHour(0);
		mustBeFrom.setMinute(0);
		mustBeFrom.setSecond(0);

		if (sDateFrom != null && sDateTo != null) {
			IWTimestamp from = new IWTimestamp(sDateFrom);
			IWTimestamp to = new IWTimestamp(sDateTo);

			System.err.println("Saving new contract : Applicant : " + sApplicantId);
			System.err.println("Must be from : " + mustBeFrom.toString() + " , is from " + from.toString());
			if (mustBeFrom != null && mustBeFrom.isLaterThan(from)) {
				returner = localize("alloc_contract_overlap", "Contracts overlap !!");
				System.err.println("Sorry contracts overlap");
				return returner;
			}
			if (sApplicantId != null ){
				Integer apartmentId = Integer.valueOf(sApartmentId);
        if( sApartmentId != null) {
          
          Integer applicantId = Integer.valueOf(sApplicantId);
          Collection L = null;
		Applicant applicant = null;
		try {
			applicant = getCampusService(iwc).getApplicationService().getApplicantHome().findByPrimaryKey(applicantId);
			  
			  L = getCampusService(iwc).getContractService().getContractHome().findByApplicantInCreatedStatus(applicantId);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

          if (L.isEmpty() && applicant != null) {
            User eUser = makeNewUser(applicant);
            if (eUser != null) {
              if (makeNewContract(iwc, eUser, applicant, apartmentId, from, to))
                returner = localize("alloc_was_saved", "Contract was saved");
              else
                returner = localize("alloc_not_saved", "Contract was not saved");
            }
            else
              returner = localize("no_user", "No user was made");
          }
          else
            returner = localize("has_contracts_or_no_applicant", "Has contracts or no applicant");
        }
        else if (sContractId != null) {

          Integer contractId = Integer.valueOf(sContractId);
          try {
			getCampusService(iwc).getContractService().updateAllocation(contractId,apartmentId,from.getDate(),to.getDate());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
        }
        else
            System.err.println("no Apartment id");

      }
      else
        System.err.println("no Applicant id");
		}
    else
      System.err.println("no from or to provided");

		return returner;
	}

	private boolean deleteAllocation(IWContext iwc) {
		String sContractId = iwc.getParameter("contract_id");
		int iContractId = Integer.parseInt(sContractId);
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
		return ContractBusiness.deleteAllocation(iContractId);
	}

	private User makeNewUser(Applicant A) {
		String[] emails = CampusApplicationFinder.getApplicantEmail(A.getID());
//		return service.makeNewUser(A, emails);
		return ContractBusiness.makeNewUser(A, emails);
	}

	private boolean makeNewContract(IWContext iwc, User eUser, Applicant eApplicant, Integer iApartmentId, IWTimestamp from, IWTimestamp to) {
		return ContractBusiness.makeNewContract(iwc, eUser, eApplicant, iApartmentId.intValue(), from, to);
	}

	public Text headerText(String text) {
		Text T = new Text(text);
		T.setBold();
		T.setFontColor(this.WhiteColor);
		T.setFontSize(1);
		return T;
	}

	private Text boldText(String text) {
		Text T = new Text(text);
		T.setBold();
		T.setFontColor(TextFontColor);
		T.setFontSize(this.fontSize);
		return T;
	}
	private Text boldText(int i) {
		return boldText(String.valueOf(i));
	}

	public Link getPDFLink(PresentationObject MO, int cam_app_id) {
		Link L = new Link(MO);
		L.setWindowToOpen(ApplicationFilerWindow.class);
		L.addParameter("cam_app_id", cam_app_id);
		return L;
	}
	public Link getPDFLink(PresentationObject MO) {
		Link L = new Link(MO);
		L.setWindowToOpen(ApplicationFilerWindow.class);
		L.addParameter("aprt_type_id", typeId.toString());
		L.addParameter("cmplx_id", complexId.toString());
		return L;
	}
	
	public Link getWaitingListOrderLink(int id,int order,int max){
		Link L = new Link(formatText(order));
		L.setWindowToOpen(WaitingListOrganizerWindow.class);
		L.addParameter(WaitingListOrganizerWindow.COMPLEX_ID,complexId.toString());
		L.addParameter(WaitingListOrganizerWindow.APARTMENT_TYPE_ID,typeId.toString());
		L.addParameter(WaitingListOrganizerWindow.NUMBER_ON_LIST,order);
		L.addParameter(WaitingListOrganizerWindow.WL_ID,id);
		L.addParameter(WaitingListOrganizerWindow.MAX_LIST,max);
		return L;
		
	}

	private ApartmentTypePeriods getPeriod() {
		return ContractFinder.getPeriod(typeId.intValue());
	}

	private String getApartmentString(Contract eContract) {
		Apartment A = BuildingCacher.getApartment(eContract.getApartmentId().intValue());
		Floor F = BuildingCacher.getFloor(A.getFloorId());
		Building B = BuildingCacher.getBuilding(F.getBuildingId());
		Complex C = BuildingCacher.getComplex(B.getComplexId());
		StringBuffer sb = new StringBuffer(A.getName());
		sb.append(" ");
		sb.append(F.getName());
		sb.append(" ");
		sb.append(B.getName());
		sb.append(" ");
		sb.append(C.getName());
		return sb.toString();
	}

	private String getStatus(String status) {
		return ContractBusiness.getLocalizedStatus(getResourceBundle(), status);
	}

	public Text formatText(String s) {
		Text T = new Text();
		if (s != null) {
			T = new Text(s);
			if (this.fontBold)
				T.setBold();
			T.setFontColor(this.TextFontColor);
			T.setFontSize(this.fontSize);
		}
		return T;
	}

	public Text formatText(int i) {
		return formatText(String.valueOf(i));
	}

	protected void setStyle(InterfaceObject O) {
		O.setMarkupAttribute("style", this.styleAttribute);
	}

	public void main(IWContext iwc) {
		//isStaff = com.idega.core.accesscontrol.business.AccessControl
		//debugParameters(iwc);
		isAdmin = iwc.hasEditPermission(this);
		try {
			control(iwc);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @return
	 */
	public int getAllowedRejections() {
		return allowedRejections;
	}

	/**
	 * @param i
	 */
	public void setAllowedRejections(int i) {
		allowedRejections = i;
	}

}