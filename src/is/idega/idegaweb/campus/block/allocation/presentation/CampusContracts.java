package is.idega.idegaweb.campus.block.allocation.presentation;
import is.idega.idegaweb.campus.block.allocation.business.ContractBusiness;
import is.idega.idegaweb.campus.block.allocation.business.ContractFinder;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean;
import is.idega.idegaweb.campus.block.allocation.data.ContractHome;
import is.idega.idegaweb.campus.presentation.CampusBlock;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Iterator;

import com.idega.block.application.data.Applicant;
import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.Floor;
import com.idega.core.user.data.User;
import com.idega.data.EntityFinder;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
/**
 * Title: idegaclasses Description: Copyright: Copyright (c) 2001 Company:
 * 
 * @author <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */
public class CampusContracts extends CampusBlock {
	protected final int ACT1 = 1, ACT2 = 2, ACT3 = 3, ACT4 = 4, ACT5 = 5;
	private int iGlobalSize = 50;
	private String sGlobalStatus = "C";
	private Integer CLBU = new Integer(-1), CLFL = new Integer(-1), CLCX = new Integer(-1), CLTP = new Integer(-1),
			CLCT = new Integer(-1), ORDER = new Integer(-1);
	private Integer index = new Integer(0);
	private final String prmCx = "cl_clx", prmBu = "cl_bu", prmFl = "cl_fl", 
			prmOrder = "ct_or";
	private final String prmIdx = "cl_idx";
	protected boolean isAdmin = false;
	private String conPrm = "contract_status";
	//private String sessConPrm = "sess_con_status";
	private String sizePrm = "global_size";
	//private String sessSizePrm = "sess_global_size";
	public String getLocalizedNameKey() {
		return "contracts";
	}
	public String getLocalizedNameValue() {
		return "Contracts";
	}
	protected void control(IWContext iwc) {
		initFilter(iwc);
		if (isAdmin) {
			add(statusForm());
			if (iwc.getParameter("garbage") != null) {
				doGarbageContract(iwc);
			}
			add(getContractTable(iwc));
		} else
			add(getNoAccessObject(iwc));
		//add(String.valueOf(iSubjectId));
	}
	private void initFilter(IWContext iwc) {
		// complex check
		if (iwc.isParameterSet(prmIdx)) {
			this.index = Integer.valueOf(iwc.getParameter(prmIdx));
			iwc.setSessionAttribute(prmIdx, index);
		} else if (iwc.getSessionAttribute(prmIdx) != null) {
			index = ((Integer) iwc.getSessionAttribute(prmIdx));
		}
		if (iwc.isParameterSet(prmCx)) {
			this.CLCX = Integer.valueOf(iwc.getParameter(prmCx));
			iwc.setSessionAttribute(prmCx, CLCX);
			storeIndex(iwc, 0);
		} else if (iwc.getSessionAttribute(prmCx) != null) {
			CLCX = ((Integer) iwc.getSessionAttribute(prmCx));
		}
		if (iwc.isParameterSet(prmBu)) {
			this.CLBU = Integer.valueOf(iwc.getParameter(prmBu));
			iwc.setSessionAttribute(prmBu, CLBU);
			storeIndex(iwc, 0);
		} else if (iwc.getSessionAttribute(prmBu) != null) {
			CLBU = ((Integer) iwc.getSessionAttribute(prmBu));
		}
		if (iwc.isParameterSet(prmFl)) {
			this.CLFL = Integer.valueOf(iwc.getParameter(prmFl));
			iwc.setSessionAttribute(prmFl, CLFL);
			storeIndex(iwc, 0);
		} else if (iwc.getSessionAttribute(prmFl) != null) {
			CLFL = ((Integer) iwc.getSessionAttribute(prmFl));
		}
		if (iwc.isParameterSet(prmOrder)) {
			this.ORDER = Integer.valueOf(iwc.getParameter(prmOrder));
			iwc.setSessionAttribute(prmOrder, ORDER);
			storeIndex(iwc, 0);
		} else if (iwc.getSessionAttribute(prmOrder) != null) {
			ORDER = ((Integer) iwc.getSessionAttribute(prmOrder));
		}
		if (iwc.getParameter(conPrm) != null) {
			this.sGlobalStatus = (iwc.getParameter(conPrm));
			iwc.setSessionAttribute(conPrm, sGlobalStatus);
			storeIndex(iwc, 0);
		} else if (iwc.getSessionAttribute(conPrm) != null) {
			this.sGlobalStatus = ((String) iwc.getSessionAttribute(conPrm));
		}
		if (iwc.getParameter(sizePrm) != null) {
			this.iGlobalSize = Integer.parseInt(iwc.getParameter(sizePrm));
			iwc.setSessionAttribute(sizePrm, new Integer(iGlobalSize));
			storeIndex(iwc, 0);
		} else if (iwc.getSessionAttribute(sizePrm) != null) {
			this.iGlobalSize = ((Integer) iwc.getSessionAttribute(sizePrm)).intValue();
		}
	}
	private void storeIndex(IWContext iwc, int idx) {
		if (index.intValue() != idx) {
			index = new Integer(idx);
			iwc.setSessionAttribute(prmIdx, index);
		}
	}
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	public PresentationObject makeLinkTable(int menuNr) {
		Table LinkTable = new Table(6, 1);
		return LinkTable;
	}
	private PresentationObject statusForm() {
		Form form = new Form();
		DropdownMenu status = statusDrop(conPrm, sGlobalStatus);
		DropdownMenu complex = drpLodgings(Complex.class, prmCx, "--", CLCX);
		DropdownMenu building = drpLodgings(Building.class, prmBu, "--", CLBU);
		DropdownMenu floor = drpFloors(prmFl, "--", CLFL, true);
		//DropdownMenu cat =
		// drpLodgings(((com.idega.block.building.data.ApartmentCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(ApartmentCategory.class)).createLegacy(),prmArray[3],"--",sValues[3]);
		//DropdownMenu type =
		// drpLodgings(((com.idega.block.building.data.ApartmentTypeHome)com.idega.data.IDOLookup.getHomeLegacy(ApartmentType.class)).createLegacy(),prmArray[4],"--",sValues[4]);
		DropdownMenu order = orderDrop(prmOrder, "--", ORDER);
		DropdownMenu sizeMenu = sizeDrop(sizePrm, iGlobalSize);
		DataTable T = new DataTable();
		T.addTitle(localize("filter", "Filter"));
		T.setTitlesHorizontal(true);
		T.setBottomHeight(3);
		//Table T = new Table();
		T.add(getHeader(localize("status", "Status")), 1, 1);
		T.add(getHeader(localize("complex", "Complex")), 2, 1);
		T.add(getHeader(localize("building", "Building")), 3, 1);
		T.add(getHeader(localize("floor", "Floor")), 4, 1);
		// T.add(Edit.formatText(localize("category","Category")),4,1);
		// T.add(Edit.formatText(localize("type","Type")),5,1);
		T.add(getHeader(localize("order", "Order")), 5, 1);
		T.add(getHeader(localize("viewsize", "View size")), 6, 1);
		T.add(status, 1, 2);
		T.add(complex, 2, 2);
		T.add(building, 3, 2);
		T.add(floor, 4, 2);
		T.add(order, 5, 2);
		T.add(sizeMenu, 6, 2);
		//T.add(cat,4,2);
		//T.add(type,5,2);
		SubmitButton get = (SubmitButton) getSubmitButton("conget", null, "Get", "get");
		T.add(get, 7, 2);
		form.add(T);
		return (form);
	}
	private DropdownMenu sizeDrop(String name, int selected) {
		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElement("10");
		drp.addMenuElement("20");
		drp.addMenuElement("50");
		drp.addMenuElement("100");
		drp.addMenuElement("500");
		drp.addMenuElement("-1", "All");
		drp.setSelectedElement(selected);
		return drp;
	}
	private DropdownMenu drpLodgings(Class lodgings, String name, String display, Integer selected) {
		Collection lods = null;
		try {
			lods = EntityFinder.getInstance().findAll(lodgings);
		} catch (Exception e) {
		}
		DropdownMenu drp = new DropdownMenu(lods, name);
		if (!"".equals(display))
			drp.addMenuElementFirst("-1", display);
		drp.setSelectedElement(selected.toString());
		return drp;
	}
	private DropdownMenu drpFloors(String name, String display, Integer selected, boolean withBuildingName) {
		Collection lods = null;
		try {
			lods = EntityFinder.getInstance().findAll(Floor.class);
		} catch (Exception e) {
		}
		DropdownMenu drp = new DropdownMenu(name);
		if (!"".equals(display))
			drp.addMenuElement("-1", display);
		for (Iterator iter = lods.iterator(); iter.hasNext();) {
			Floor floor = (Floor) iter.next();
			if (withBuildingName) {
				try {
					drp.addMenuElement(floor.getPrimaryKey().toString(), floor.getName() + " "
							+ (floor.getBuilding().getName()));
				} catch (Exception e) {
				}
			} else
				drp.addMenuElement(floor.getPrimaryKey().toString(), floor.getName());
		}
		if (selected != null) {
			drp.setSelectedElement(selected.toString());
		}
		return drp;
	}
	private DropdownMenu orderDrop(String name, String display, Integer selected) {
		DropdownMenu drp = new DropdownMenu(name);
		if (!"".equals(display)) {
			drp.addDisabledMenuElement("-1", display);
		}
		drp.addMenuElement(ContractFinder.NAME, localize("name", "Name"));
		drp.addMenuElement(ContractFinder.SSN, localize("ssn", "Socialnumber"));
		drp.addMenuElement(ContractFinder.APARTMENT, localize("apartment", "Apartment"));
		drp.addMenuElement(ContractFinder.FLOOR, localize("floor", "Floor"));
		drp.addMenuElement(ContractFinder.BUILDING, localize("building", "Building"));
		drp.addMenuElement(ContractFinder.COMPLEX, localize("complex", "Complex"));
		drp.addMenuElement(ContractFinder.CATEGORY, localize("category", "Category"));
		drp.addMenuElement(ContractFinder.TYPE, localize("type", "Type"));
		if (selected != null)
			drp.setSelectedElement(selected.toString());
		return drp;
	}
	private PresentationObject getContractTable(IWContext iwc) {
		Collection contracts = null;
		int contractCount = 0;
		try {
			ContractHome cHome = getCampusService(iwc).getContractService().getContractHome();
			contracts = cHome.findBySearchConditions(sGlobalStatus, CLCX, CLBU, CLFL, CLTP, CLCT, null,
					this.iGlobalSize, index.intValue());
			contractCount = cHome.countBySearchConditions(sGlobalStatus, CLCX, CLBU, CLFL, CLTP, CLCT, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//List L =
		// ContractFinder.listOfContracts(sValues[0],sValues[1],sValues[2],sValues[3],sValues[4],sGlobalStatus,order);
		//List L = ContractFinder.listOfStatusContracts(this.sGlobalStatus);
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, iwc.getCurrentLocale());
		Contract C = null;
		User U = null;
		Applicant Ap = null;
		Apartment A = null;
		Building B = null;
		Floor F = null;
		Complex CX = null;
		Image printImage = getBundle().getImage("/print.gif");
		Image registerImage = getBundle().getImage("/pen.gif");
		Image resignImage = getBundle().getImage("/scissors.gif");
		Image keyImage = getBundle().getImage("/key.gif");
		Image nokeyImage = getBundle().getImage("/nokey.gif");
		Image garbageImage = getBundle().getImage("/trashcan.gif");
		Image renewImage = getBundle().getImage("/renew.gif");
		boolean garbage = false;
		int row = 1;
		int col = 1;
		DataTable T = getDataTable();
		T.setTitlesHorizontal(true);
		T.setWidth("100%");
		T.add(getHeader("#"), col++, 1);
		if (sGlobalStatus.equals(ContractBMPBean.statusEnded) || sGlobalStatus.equals(ContractBMPBean.statusResigned)
				|| sGlobalStatus.equals(ContractBMPBean.statusRejected)) {
			T.add((garbageImage), col++, 1);
			garbage = true;
		} else
			T.add((printImage), col++, 1);//Edit.formatText(localize("print","Print")
		T.add((resignImage), col++, 1);//Edit.formatText(localize("sign","Sign"))
		T.add((registerImage), col++, 1);//Edit.formatText(localize("sign","Sign"))
		T.add((renewImage), col++, 1);
		//col = 4;
		T.add(getHeader(localize("name", "Name")), col++, 1);
		T.add(getHeader(localize("ssn", "Socialnumber")), col++, 1);
		T.add(getHeader(localize("apartment", "Apartment")), col++, 1);
		T.add(getHeader(localize("validfrom", "Valid from")), col++, 1);
		T.add(getHeader(localize("validto", "Valid To")), col++, 1);
		T.add(keyImage, col++, 1);//Edit.titleText(localize("key","Key")),col++,1);
		/*
		 * Table T = new Table(); T.setCellspacing(0); T.setCellpadding(2);
		 */
		row++;
		if (contracts != null) {
			int len = contracts.size();
			if (iGlobalSize > 0 && iGlobalSize <= len) {
				len = iGlobalSize;
			}
			T.addTitle(localize("contracts", "Contracts") + " " + localize("showing", "showing") + " " + len + " "
					+ localize("of", "of") + " " + contractCount);
			StringBuffer sbIDs = new StringBuffer();
			int i = index.intValue();
			for (Iterator iter = contracts.iterator(); iter.hasNext();) {
				i++;
				col = 1;
				try {
					C = (Contract) iter.next();
					int contractID = ((Integer) C.getPrimaryKey()).intValue();
					String status = C.getStatus();
					sbIDs.append(contractID);
					sbIDs.append(ContractFiler.prmSeperator);
					U = C.getUser();
					Ap = C.getApplicant();
					A = C.getApartment();
					T.add(getEditLink(getText(String.valueOf(i)), contractID), col++, row);
					//if(C.getStatus().equalsIgnoreCase(ContractBMPBean.statusCreated)
					// ||
					// C.getStatus().equalsIgnoreCase(ContractBMPBean.statusPrinted)
					// )
					if (garbage)
						T.add(getGarbageLink(garbageImage, contractID), col++, row);
					else
						T.add(getPDFLink(printImage, contractID, Ap.getSSN()), col++, row);
					if (status.equalsIgnoreCase(ContractBMPBean.statusSigned))
						T.add(getReSignLink(resignImage, contractID), col, row);
					col++;
					if (status.equalsIgnoreCase(ContractBMPBean.statusPrinted)
							|| status.equalsIgnoreCase(ContractBMPBean.statusSigned))
						T.add(getSignedLink(registerImage, contractID, isAdmin), col, row);
					col++;
					if (status.equalsIgnoreCase(ContractBMPBean.statusEnded)
							|| status.equalsIgnoreCase(ContractBMPBean.statusResigned)
							|| status.equalsIgnoreCase(ContractBMPBean.statusSigned))
						T.add(getRenewLink(renewImage, contractID), col, row);
					col++;
					T.add(getText(Ap.getFullName()), col++, row);
					T.add(getText(Ap.getSSN()), col++, row);
					T.add((getApartmentTable(A)), col++, row);
					T.add(getText(df.format(C.getValidFrom())), col++, row);
					T.add(getText(df.format(C.getValidTo())), col++, row);
					if (C.getIsRented())
						T.add(getKeyLink(keyImage, contractID), col++, row);
					else
						T.add(getKeyLink(nokeyImage, contractID), col++, row);
					row++;
					col = 1;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			/*
			 * T.add(getPDFLink(printImage,sbIDs.toString()),1,row);
			 * 
			 * 
			 * T.setHorizontalZebraColored(Edit.colorLightBlue,Edit.colorWhite);
			 * T.setRowColor(1,Edit.colorBlue);
			 * T.setRowColor(row,Edit.colorRed); T.mergeCells(1,row,8,row);
			 * T.setWidth(1,"15"); T.add(Edit.formatText(" "),1,row);
			 * T.setColumnAlignment(1,"left");
			 * T.setHeight(row,Edit.bottomBarThickness);
			 */
		}
		//else
		// add(Edit.formatText(localize("no_contracts","No contracts")));
		Table T2 = new Table();
		T2.setCellpadding(0);
		T2.setCellspacing(0);
		T2.add(T, 1, 1);
		T2.add(getNavigationLinks(contractCount), 1, 3);
		return T2;
	}
	private PresentationObject getNavigationLinks(int totalSize) {
		Table T = new Table();
		int pages = totalSize / iGlobalSize;
		int remainder = totalSize % iGlobalSize;
		int col = 1;
		for (int i = 0; i < pages; i++) {
			int a = i * iGlobalSize;
			Link L = new Link((a + 1) + ".." + (a + iGlobalSize));
			L.addParameter(prmIdx, a);
			T.add(L, col++, 1);
		}
		if (remainder > 0) {
			int b = (pages * iGlobalSize);
			Link L = new Link((b + 1) + ".." + totalSize);
			L.addParameter(prmIdx, b);
			T.add(L, col, 1);
		}
		return T;
	}
	private void doGarbageContract(IWContext iwc) {
		int id = Integer.parseInt(iwc.getParameter("garbage"));
		ContractBusiness.doGarbageContract(id);
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
	private String getStatus(String status) {
		String r = "";
		char c = status.charAt(0);
		switch (c) {
			case 'C' :
				r = localize("created", "Created");
				break;
			case 'P' :
				r = localize("printed", "Printed");
				break;
			case 'S' :
				r = localize("signed", "Signed");
				break;
			case 'R' :
				r = localize("rejected", "Rejected");
				break;
			case 'T' :
				r = localize("terminated", "Terminated");
				break;
			case 'U' :
				r = localize("resigned", "Resigned");
				break;
			case 'E' :
				r = localize("ended", "Ended");
				break;
			case 'G' :
				r = localize("garbaged", "Canned");
				break;
		}
		return r;
	}
	private DropdownMenu statusDrop(String name, String selected) {
		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElement("C", getStatus("C"));
		drp.addMenuElement("P", getStatus("P"));
		drp.addMenuElement("S", getStatus("S"));
		drp.addMenuElement("R", getStatus("R"));
		drp.addMenuElement("U", getStatus("U"));
		drp.addMenuElement("E", getStatus("E"));
		drp.setSelectedElement(selected);
		return drp;
	}
	public static Link getSignedLink(PresentationObject MO, int contractId, boolean isAdmin) {
		Link L = new Link(MO);
		L.setWindowToOpen(ContractSignWindow.class);
		L.addParameter("signed_id", contractId);
		if (isAdmin) {
			L.addParameter(ContractSignWindow.prmAdmin, "true");
		}
		return L;
	}
	public static Link getEditLink(PresentationObject MO, int contractId) {
		Link L = new Link(MO);
		L.setWindowToOpen(ContractEditWindow.class);
		L.addParameter("contract_id", contractId);
		return L;
	}
	public static Link getRenewLink(PresentationObject MO, int contractId) {
		Link L = new Link(MO);
		L.setWindowToOpen(ContractRenewWindow.class);
		L.addParameter(ContractRenewWindow.prmContractId, contractId);
		return L;
	}
	public Link getReSignLink(PresentationObject MO, int contractId) {
		Link L = new Link(MO);
		L.setWindowToOpen(ContractResignWindow.class);
		if (isAdmin)
			L.addParameter(ContractResignWindow.prmAdmin, "true");
		L.addParameter("contract_id", contractId);
		return L;
	}
	public static Link getKeyLink(PresentationObject MO, int contractId) {
		Link L = new Link(MO);
		L.setWindowToOpen(ContractKeyWindow.class);
		L.addParameter(ContractKeyWindow.prmContractId, contractId);
		return L;
	}
	public Link getPDFLink(PresentationObject MO, int contractId) {
		Link L = new Link(MO);
		L.setWindowToOpen(ContractFilerWindow.class);
		L.addParameter(ContractFiler.prmOneId, contractId);
		return L;
	}
	public Link getPDFLink(PresentationObject MO, int contractId, String filename) {
		Link L = new Link(MO);
		L.setWindowToOpen(ContractFilerWindow.class);
		L.addParameter(ContractFiler.prmOneId, contractId);
		L.addParameter(ContractFiler.prmFileName, filename);
		return L;
	}
	public Link getPDFLink(PresentationObject MO, String ids) {
		Link L = new Link(MO);
		L.setWindowToOpen(ContractFilerWindow.class);
		L.addParameter(ContractFiler.prmManyIds, ids);
		return L;
	}
	public Link getGarbageLink(PresentationObject MO, int contractId) {
		Link L = new Link(MO);
		L.setWindowToOpen(ContractGarbageWindow.class);
		L.addParameter(ContractGarbageWindow.prmContractId, contractId);
		//L.addParameter("garbage",contractId);
		return L;
	}
	public void main(IWContext iwc) {
		isAdmin = iwc.hasEditPermission(this);
		control(iwc);
	}
}
