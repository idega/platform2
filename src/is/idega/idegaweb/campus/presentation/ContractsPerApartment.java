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

import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.presentation.ContractFiler;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.ejb.FinderException;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.ApplicantHome;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.ApartmentHome;
import com.idega.block.building.data.ApartmentView;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.Floor;
import com.idega.data.EntityFinder;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;

/**
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ContractsPerApartment extends CampusBlock {

	private int iSubjectId = -1;

	private String sGlobalStatus = "C";

	private String sCLBU = "-1";

	private String sCLFL = "-1";

	private String sCLCX = "-1";

	private String sCLTP = "-1";

	private String sCLCT = "-1";

	private String sORDER = "-1";

	private ListIterator iterator = null;

	private LinkedList linkedlist = null;

	private final String prmCx = "cl_clx";

	private final String prmBu = "cl_bu";

	private final String prmFl = "cl_fl";

	private final String prmCt = "cl_ct";

	private final String prmTp = "cl_tp";

	private final String prmOrder = "ct_or";

	private final String sessCx = "s_clx";

	private final String sessBu = "s_bu";

	private final String sessFl = "s_fl";

	private final String sessCt = "s_ct";

	private final String sessTp = "s_tp";

	private final String sessOrder = "s_or";

	private String[] prmArray = { prmCx, prmBu, prmFl, prmCt, prmTp, prmOrder };

	private String[] sessArray = { sessCx, sessBu, sessFl, sessCt, sessTp,
			sessOrder };

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
			} else if (iwc.getSessionAttribute(sessArray[i]) != null) {
				sValues[i] = ((String) iwc.getSessionAttribute(sessArray[i]));
			}
		}

		if (iwc.getParameter(conPrm) != null) {
			this.sGlobalStatus = (iwc.getParameter(conPrm));
			iwc.setSessionAttribute(sessConPrm, sGlobalStatus);
		} else if (iwc.getSessionAttribute(sessConPrm) != null) {
			this.sGlobalStatus = ((String) iwc.getSessionAttribute(sessConPrm));
		}

		if (iwc.isParameterSet("apartment")) {
			aprtName = iwc.getParameter("apartment");
		} else {
			aprtName = null;
		}

		if (isAdmin) {
			try {
				add(statusForm());

				add(getContractTable(iwc));
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			add(getNoAccessObject(iwc));
		}
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	private Form statusForm() throws RemoteException, FinderException {
		Form myForm = new Form();

		DropdownMenu complex = drpLodgings(Complex.class, prmArray[0], "--",
				sValues[0]);
		DropdownMenu building = drpLodgings(Building.class, prmArray[1], "--",
				sValues[1]);
		TextInput apartment = getTextInput("apartment");

		if (aprtName != null) {
			apartment.setValue(aprtName);
		}
		Edit.setStyle(complex);
		Edit.setStyle(building);
		Edit.setStyle(apartment);

		DataTable T = getDataTable();
		T.addTitle(localize("filter", "Filter"));
		T.setTitlesHorizontal(true);
		T.setBottomHeight(3);

		T.add(getHeader(localize("complex", "Complex")), 1, 1);
		T.add(getHeader(localize("building", "Building")), 2, 1);
		T.add(getHeader(localize("apartment", "Apartment")), 3, 1);

		T.add(complex, 1, 2);
		T.add(building, 2, 2);
		T.add(apartment, 3, 2);

		SubmitButton get = (SubmitButton) getSubmitButton("conget", "true",
				"Get", "get");

		T.add(get, 4, 2);

		myForm.add(T);
		return myForm;
	}

	private DropdownMenu drpLodgings(Class lodgings, String name,
			String display, String selected) {
		Collection lods = null;
		try {
			lods = EntityFinder.getInstance().findAll(lodgings);
		} catch (Exception e) {
		}
		DropdownMenu drp = new DropdownMenu(lods, name);

		if (!"".equals(display)) {
			drp.addMenuElement("-1", display);
		}

		/*for (Iterator iter = lods.iterator(); iter.hasNext();) {
			BuildingEntity entity = (BuildingEntity) iter.next();

			drp.addMenuElement(entity.getPrimaryKey().toString(), entity
					.getName());
		}*/
		if (!"".equalsIgnoreCase(selected)) {
			drp.setSelectedElement(selected);
		}

		return drp;
	}

	private PresentationObject getContractTable(IWContext iwc) throws Exception {
		Integer complex = Integer.valueOf(sValues[0]);
		Integer building = Integer.valueOf(sValues[1]);

		if (aprtName == null) {
			DataTable err1 = new DataTable();
			err1.add(iwrb.getLocalizedString("cpa_error_select_apartment", "You have to select an apartment"));

			return err1;
		}

		Collection L = null;
		if (complex != null && complex.intValue() != -1) {
			if (building != null && building.intValue() != -1) {
				L = getContractService(iwc).getContractHome()
				.findByComplexAndBuildingAndApartmentName(complex, building,
						aprtName);
			} else {
				DataTable err1 = new DataTable();
				err1.add(iwrb.getLocalizedString("cpa_error_select_building", "You have to select a building"));

				return err1;				
			}
		} else {
			if (building != null && building.intValue() != -1) {
				L = getContractService(iwc).getContractHome()
				.findByBuildingAndApartmentName(building,
						aprtName);
			} else {
				L = getContractService(iwc).getContractHome()
				.findByApartmentName(aprtName);
			}			
		}
		
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, iwc
				.getCurrentLocale());
		Contract C = null;
		User U = null;
		Applicant Ap = null;
		ApartmentView A = null;
		Building B = null;
		Floor F = null;
		Complex CX = null;

		Image keyImage = getBundle().getImage("/key.gif");
		Image nokeyImage = getBundle().getImage("/nokey.gif");

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
		T.add(getHeader(localize("rented", "Rented")), col++, 1);

		T.add(keyImage, col++, 1);
		row++;
		if (L != null) {
			int len = L.size();
			StringBuffer sbIDs = new StringBuffer();
			for (Iterator iter = L.iterator(); iter.hasNext();) {

				col = 1;
				try {
					C = (Contract) iter.next();
					String status = C.getStatus();
					sbIDs.append(C.getPrimaryKey().toString());
					sbIDs.append(ContractFiler.prmSeperator);
					U = ((UserHome) IDOLookup.getHome(User.class))
							.findByPrimaryKey(C.getUserId());
					Ap = ((ApplicantHome) IDOLookup.getHome(Applicant.class))
							.findByPrimaryKey(C.getApplicantId());
					Apartment Aprt = ((ApartmentHome) IDOLookup
							.getHome(Apartment.class)).findByPrimaryKey(C
							.getApartmentId());
					T.add(getText(Ap.getFullName()), col++, row);
					T.add(getText(Ap.getSSN()), col++, row);

					T.add((getApartmentTable(Aprt)), col++, row);
					T.add(getText(df.format(C.getValidFrom())), col++, row);
					T.add(getText(df.format(C.getValidTo())), col++, row);
					if (C.getIsRented()) {
						T.add(keyImage, col++, row);
					} else {
						T.add(nokeyImage, col++, row);						
					}
					row++;
					col = 1;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		return T;
	}

	private PresentationObject getApartmentTable(Apartment A) {
		Table T = new Table();

		Floor F = A.getFloor();
		Building B = F.getBuilding();
		Complex C = B.getComplex();
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