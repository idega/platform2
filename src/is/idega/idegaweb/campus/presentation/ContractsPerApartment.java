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
import com.idega.data.IDOLegacyEntity;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
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
public class ContractsPerApartment extends Block {
	protected final int ACT1 = 1, ACT2 = 2, ACT3 = 3, ACT4 = 4, ACT5 = 5;
	private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.campus";
	protected IWResourceBundle iwrb;
	protected IWBundle iwb;

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
		is.idega.idegaweb.campus.presentation.SysPropsSetter.isSysPropsInMemoryElseLoad(iwc);
		iwrb = getResourceBundle(iwc);
		iwb = getBundle(iwc);
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
			add(Edit.formatText(iwrb.getLocalizedString("access_denied", "Access denied")));
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	private Form statusForm() {
		Form myForm = new Form();
		DropdownMenu complex = drpLodgings(((com.idega.block.building.data.ComplexHome) com.idega.data.IDOLookup.getHomeLegacy(Complex.class)).createLegacy(), prmArray[0], "--", sValues[0]);
		DropdownMenu building = drpLodgings(((com.idega.block.building.data.BuildingHome) com.idega.data.IDOLookup.getHomeLegacy(Building.class)).createLegacy(), prmArray[1], "--", sValues[1]);
		TextInput apartment = new TextInput("apartment");
		if (aprtName != null)
			apartment.setValue(aprtName);
		Edit.setStyle(complex);
		Edit.setStyle(building);
		Edit.setStyle(apartment);
		DataTable T = new DataTable();
		T.addTitle(iwrb.getLocalizedString("filter", "Filter"));
		T.setTitlesHorizontal(true);
		T.setBottomHeight(3);
		T.add(Edit.formatText(iwrb.getLocalizedString("complex", "Complex")), 1, 1);
		T.add(Edit.formatText(iwrb.getLocalizedString("building", "Building")), 2, 1);
		T.add(Edit.formatText(iwrb.getLocalizedString("apartment","Apartment")),3,1);
		T.add(complex, 1, 2);
		T.add(building, 2, 2);
		T.add(apartment,3,2);
		SubmitButton get = new SubmitButton("conget", iwrb.getLocalizedString("get", "Get"));
		Edit.setStyle(get);
		T.add(get, 4, 2);

		myForm.add(T);
		return myForm;
	}

	private DropdownMenu drpLodgings(IDOLegacyEntity lodgings, String name, String display, String selected) {
		IDOLegacyEntity[] lods = new IDOLegacyEntity[0];
		try {
			lods = (lodgings).findAll();
		}
		catch (SQLException e) {
		}
		DropdownMenu drp = new DropdownMenu(name);
		if (!"".equals(display))
			drp.addMenuElement("-1", display);
		int len = lods.length;
		if (len > 0) {
			for (int i = 0; i < len; i++) {
				drp.addMenuElement(lods[i].getID(), lods[i].getName());
			}
			if (!"".equalsIgnoreCase(selected)) {
				drp.setSelectedElement(selected);
			}
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
		Image printImage = iwb.getImage("/print.gif");
		Image registerImage = iwb.getImage("/pen.gif");
		Image resignImage = iwb.getImage("/scissors.gif");
		Image keyImage = iwb.getImage("/key.gif");
		Image nokeyImage = iwb.getImage("/nokey.gif");
		Image garbageImage = iwb.getImage("/trashcan.gif");
		Image renewImage = iwb.getImage("/renew.gif");
		boolean garbage = false;
		int row = 1;
		int col = 1;
		DataTable T = new DataTable();
		T.addTitle(iwrb.getLocalizedString("contracts", "Contracts"));
		T.setTitlesHorizontal(true);
		T.setWidth("100%");
		T.add(Edit.formatText(iwrb.getLocalizedString("name", "Name")), col++, 1);
		T.add(Edit.formatText(iwrb.getLocalizedString("ssn", "Socialnumber")), col++, 1);
		T.add(Edit.formatText(iwrb.getLocalizedString("apartment", "Apartment")), col++, 1);
		T.add(Edit.formatText(iwrb.getLocalizedString("validfrom", "Valid from")), col++, 1);
		T.add(Edit.formatText(iwrb.getLocalizedString("validto", "Valid To")), col++, 1);
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
					sbIDs.append(C.getID());
					sbIDs.append(ContractFiler.prmSeperator);
					U = ((com.idega.core.user.data.UserHome) com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(C.getUserId().intValue());
					Ap = ((com.idega.block.application.data.ApplicantHome) com.idega.data.IDOLookup.getHomeLegacy(Applicant.class)).findByPrimaryKeyLegacy(C.getApplicantId().intValue());
					A = ((com.idega.block.building.data.ApartmentHome) com.idega.data.IDOLookup.getHomeLegacy(Apartment.class)).findByPrimaryKeyLegacy(C.getApartmentId().intValue());
					T.add(Edit.formatText(Ap.getFullName()), col++, row);
					T.add(Edit.formatText(Ap.getSSN()), col++, row);
					T.add((getApartmentTable(A)), col++, row);
					T.add(Edit.formatText(df.format(C.getValidFrom())), col++, row);
					T.add(Edit.formatText(df.format(C.getValidTo())), col++, row);
					row++;
					col = 1;
				}
				catch (SQLException ex) {
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
		T.add(Edit.formatText(A.getName()), 1, 1);
		T.add(Edit.formatText(F.getName()), 2, 1);
		T.add(Edit.formatText(B.getName()), 3, 1);
		T.add(Edit.formatText(C.getName()), 4, 1);
		return T;
	}

	public void main(IWContext iwc) {
		isAdmin = iwc.hasEditPermission(this);
		control(iwc);
	}
}