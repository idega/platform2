/*
 * $Id:$
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.presentation;


import is.idega.idegaweb.campus.block.allocation.business.ContractFinder;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.presentation.ContractFiler;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.idega.block.application.data.Applicant;
import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.Floor;
import com.idega.core.user.data.User;
import com.idega.data.EntityFinder;
import com.idega.data.IDOLegacyEntity;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * @author  <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ContractsPerApartment extends CampusBlock {
	protected final int ACT1 = 1, ACT2 = 2, ACT3 = 3, ACT4 = 4, ACT5 = 5;
	

	private int iSubjectId = -1;
	private String sGlobalStatus = "C", sCLBU = "-1", sCLFL = "-1", sCLCX = "-1", sCLTP = "-1", sCLCT = "-1", sORDER = "-1";
	private ListIterator iterator = null;
	private LinkedList linkedlist = null;

	private final String prmCx = "cl_clx", prmBu = "cl_bu", prmFl = "cl_fl", prmCt = "cl_ct", prmTp = "cl_tp", prmOrder = "ct_or";
	private final String sessCx = "s_clx", sessBu = "s_bu", sessFl = "s_fl", sessCt = "s_ct", sessTp = "s_tp", sessOrder = "s_or";
	private String[] prmArray = { prmCx, prmBu, prmFl, prmCt, prmTp, prmOrder };
	private String[] sessArray = { sessCx, sessBu, sessFl, sessCt, sessTp, sessOrder };
	private String[] sValues = { sCLBU, sCLFL, sCLCX, sCLCT, sCLTP, sORDER };
	protected boolean isAdmin = false;
	private String conPrm = "contract_status";
	private String sessConPrm = "sess_con_status";
	private String aprtName = null;

	public String getLocalizedNameKey() {
		return "contracts";
	}

	public String getLocalizedNameValue() {
		return "Contracts";
	}

	protected void control(IWContext iwc) {
	
		for (int i = 0; i < prmArray.length; i++) {
			if (iwc.getParameter(prmArray[i]) != null) {
				sValues[i] = (iwc.getParameter(prmArray[i]));
				iwc.setSessionAttribute(sessArray[i], sValues[i]);
			}
			else if (iwc.getSessionAttribute(sessArray[i]) != null) {
				sValues[i] = ((String) iwc.getSessionAttribute(sessArray[i]));
			}
		}

		if (iwc.getParameter(conPrm) != null) {
			this.sGlobalStatus = (iwc.getParameter(conPrm));
			iwc.setSessionAttribute(sessConPrm, sGlobalStatus);
		}
		else if (iwc.getSessionAttribute(sessConPrm) != null) {
			this.sGlobalStatus = ((String) iwc.getSessionAttribute(sessConPrm));
		}

		if (iwc.isParameterSet("apartment"))
			aprtName = iwc.getParameter("apartment");
		else
			aprtName = null;

		if (isAdmin) {
			add(statusForm());
			add(getContractTable(iwc));
		}
		else
			add(getNoAccessObject(iwc));
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	private Form statusForm() {
		Form myForm = new Form();
		DropdownMenu complex = drpLodgings(Complex.class, prmArray[0], "--", sValues[0]);
		DropdownMenu building = drpLodgings(Building.class, prmArray[1], "--", sValues[1]);
		TextInput apartment = getTextInput("apartment");
		if (aprtName != null)
			apartment.setValue(aprtName);
		
		DataTable T = getDataTable();
		T.addTitle(localize("filter", "Filter"));
		T.setTitlesHorizontal(true);
		T.setBottomHeight(3);
		T.add(getHeader(localize("complex", "Complex")), 1, 1);
		T.add(getHeader(localize("building", "Building")), 2, 1);
		T.add(getHeader(localize("apartment","Apartment")),3,1);
		T.add(complex, 1, 2);
		T.add(building, 2, 2);
		T.add(apartment,3,2);
		SubmitButton get = (SubmitButton)getSubmitButton("conget", "true","Get", "get");
		
		T.add(get, 4, 2);

		myForm.add(T);
		return myForm;
	}

	private DropdownMenu drpLodgings(Class lodgings, String name, String display, String selected) {
		Collection lods = null;
		try {
			lods = EntityFinder.getInstance().findAll(lodgings);
		}
		catch (Exception e) {
		}
		DropdownMenu drp = new DropdownMenu(lods,name);
		if (!"".equals(display))
			drp.addMenuElement("-1", display);
		
			if (!"".equalsIgnoreCase(selected)) {
				drp.setSelectedElement(selected);
			}
		
		return drp;
	}


	private PresentationObject getContractTable(IWContext iwc) {
/*		System.out.println("complex = " + sValues[0]);
		System.out.println("building  = " + sValues[1]);
		System.out.println("apartment = " + aprtName);*/

		String complex = sValues[0];
		String building = sValues[1];

		if (complex == null || complex.equals("-1")) {
			DataTable err1 = new DataTable();
			err1.add("Þú verður að velja Garð");

			return err1;
		}

		if (building == null || building.equals("-1")) {
			DataTable err1 = new DataTable();
			err1.add("Þú verður að velja Hús");

			return err1;
		}

		if (aprtName == null) {
			DataTable err1 = new DataTable();
			err1.add("Þú verður að velja Íbúð");

			return err1;
		}

//		List L = ContractFinder.listOfContracts(sValues[0], sValues[1], sValues[2], sValues[3], sValues[4], sGlobalStatus, order);
		List L = ContractFinder.listOfContracts(complex,building,aprtName);
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, iwc.getCurrentLocale());
		Contract C = null;
		User U = null;
		Applicant Ap = null;
		Apartment A = null;
		Building B = null;
		Floor F = null;
		Complex CX = null;
		Image printImage =getBundle().getImage("/print.gif");
		Image registerImage = getBundle().getImage("/pen.gif");
		Image resignImage = getBundle().getImage("/scissors.gif");
		Image keyImage = getBundle().getImage("/key.gif");
		Image nokeyImage = getBundle().getImage("/nokey.gif");
		Image garbageImage = getBundle().getImage("/trashcan.gif");
		Image renewImage = getBundle().getImage("/renew.gif");
		boolean garbage = false;
		int row = 1;
		int col = 1;
		DataTable T = new DataTable();
		T.addTitle(localize("contracts", "Contracts"));
		T.setTitlesHorizontal(true);
		T.setWidth("100%");
		T.add(getHeader(localize("name", "Name")), col++, 1);
		T.add(getHeader(localize("ssn", "Socialnumber")), col++, 1);
		T.add(getHeader(localize("apartment", "Apartment")), col++, 1);
		T.add(getHeader(localize("validfrom", "Valid from")), col++, 1);
		T.add(getHeader(localize("validto", "Valid To")), col++, 1);
		T.add(keyImage, col++, 1);
		row++;
		if (L != null) {
			int len = L.size();
			StringBuffer sbIDs = new StringBuffer();
			for (int i = 0; i < len; i++) {
				col = 1;
				try {
					C = (Contract) L.get(i);
					String status = C.getStatus();
					sbIDs.append(C.getPrimaryKey().toString());
					sbIDs.append(ContractFiler.prmSeperator);
					U = ((com.idega.user.data.UserHome) com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKey(C.getUserId());
					Ap = ((com.idega.block.application.data.ApplicantHome) com.idega.data.IDOLookup.getHomeLegacy(Applicant.class)).findByPrimaryKey(C.getApplicantId());
					A = ((com.idega.block.building.data.ApartmentHome) com.idega.data.IDOLookup.getHomeLegacy(Apartment.class)).findByPrimaryKey(C.getApartmentId());
					T.add(getText(Ap.getFullName()), col++, row);
					T.add(getText(Ap.getSSN()), col++, row);
					T.add((getApartmentTable(A)), col++, row);
					T.add(getText(df.format(C.getValidFrom())), col++, row);
					T.add(getText(df.format(C.getValidTo())), col++, row);
					row++;
					col = 1;
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		return T;
	}

	private PresentationObject getApartmentTable(Apartment A) {
		Table T = new Table();
		Floor F = BuildingCacher.getFloor(A.getFloorId());
		Building B = BuildingCacher.getBuilding(F.getBuildingId());
		Complex C = BuildingCacher.getComplex(B.getComplexId());
		T.add(getText(A.getName()), 1, 1);
		T.add(getText(F.getName()), 2, 1);
		T.add(getText(B.getName()), 3, 1);
		T.add(getText(C.getName()), 4, 1);
		return T;
	}

	public void main(IWContext iwc) {
		isAdmin = iwc.hasEditPermission(this);
		control(iwc);
	}
}