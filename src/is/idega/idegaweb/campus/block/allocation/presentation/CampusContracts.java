package is.idega.idegaweb.campus.block.allocation.presentation;

import is.idega.idegaweb.campus.block.allocation.business.ContractFinder;
import is.idega.idegaweb.campus.block.allocation.data.AutomaticCharges;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean;
import is.idega.idegaweb.campus.block.allocation.data.ContractHome;
import is.idega.idegaweb.campus.presentation.CampusBlock;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.idega.block.application.data.Applicant;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.ApartmentType;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.Floor;
import com.idega.data.EntityFinder;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.CheckBox;
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
	private static final String SAVE = "save";
	
	private static final String CHARGE_FOR_UNLIMITED = "chargeForUnlimited";
	private static final String CHARGE_FOR_HANDLING = "chargeForHandling";
	private static final String CHARGE_FOR_TRANSFER = "chargeForTransfer";
	private static final String PARAM_GARBAGE = "garbage";
	
	protected final int ACT1 = 1;
	protected final int ACT2 = 2;
	protected final int ACT3 = 3;
	protected final int ACT4 = 4;
	protected final int ACT5 = 5;
	
	private int iGlobalSize = -1;
	
	private String sGlobalStatus = "C";
	
	private Integer ORDER = new Integer(-1);
	private Integer CLBU = new Integer(-1);
	private Integer CLFL = new Integer(-1);
	private Integer CLCX = new Integer(-1);
	private Integer CLTP = new Integer(-1);
	private Integer CLCT = new Integer(-1);
	private Integer index = new Integer(0);
	
	private final String prmCx = "cl_clx";
	private final String prmBu = "cl_bu";
	private final String prmFl = "cl_fl";
	private final String prmOrder = "ct_or";
	private final String prmIdx = "cl_idx";

	protected boolean isAdmin = false;
	
	private String conPrm = "contract_status";
	private String sizePrm = "global_size";

	public String getLocalizedNameKey() {
		return "contracts";
	}

	public String getLocalizedNameValue() {
		return "Contracts";
	}

	protected void control(IWContext iwc) {
		initFilter(iwc);
		if (isAdmin) {
			boolean showRenewal = iwc.getIWMainApplication().getSettings().getBoolean("SHOW_RENEWAL_STATUS", false);
			add(statusForm(showRenewal));
			if (iwc.isParameterSet(SAVE)) {
				handleAutomaticCharges(iwc);
			}

			if (iwc.getParameter(PARAM_GARBAGE) != null) {
				doGarbageContract(iwc);
			}
			add(getContractTable(iwc));
		} else {
			add(getNoAccessObject(iwc));
		}
	}

	private void handleAutomaticCharges(IWContext iwc) {
		String all = iwc.getParameter(SAVE);
		if (all != null && !all.equals("")) {
			StringTokenizer tok = new StringTokenizer(all, ";");
			while (tok.hasMoreElements()) {
				String removeId = (String) tok.nextElement();
				if (removeId != null && !"".equals(removeId)) {
					try {
						getContractService(iwc).removeAllAutomaticChargesForUser(removeId);
					} catch (RemoteException e) {
					}
				}
			}

			String ids[] = iwc.getParameterValues(CHARGE_FOR_UNLIMITED);
			if (ids != null) {
				for (int i = 0; i < ids.length; i++) {
					String id = ids[i];
					try {
						getContractService(iwc)
								.addChargeForUnlimitedDownloadToUser(id);
					} catch (RemoteException e) {
					}
				}
			} else {
				System.out.println("nothing selected");
			}

			ids = iwc.getParameterValues(CHARGE_FOR_HANDLING);
			if (ids != null) {
				for (int i = 0; i < ids.length; i++) {
					String id = ids[i];
					try {
						getContractService(iwc)
								.addChargeForHandlingToUser(id);
					} catch (RemoteException e) {
					}
				}
			} else {
				System.out.println("nothing selected");
			}

			ids = iwc.getParameterValues(CHARGE_FOR_TRANSFER);
			if (ids != null) {
				for (int i = 0; i < ids.length; i++) {
					String id = ids[i];
					try {
						getContractService(iwc)
								.addChargeForTransferToUser(id);
					} catch (RemoteException e) {
					}
				}
			} else {
				System.out.println("nothing selected");
			}
		}
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
			this.iGlobalSize = ((Integer) iwc.getSessionAttribute(sizePrm))
					.intValue();
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

	private PresentationObject statusForm(boolean showRenewal) {
		Form form = new Form();
		DropdownMenu status = statusDrop(conPrm, sGlobalStatus, showRenewal);
		DropdownMenu complex = drpLodgings(Complex.class, prmCx, "--", CLCX);
		DropdownMenu building = drpLodgings(Building.class, prmBu, "--", CLBU);
		DropdownMenu floor = drpFloors(prmFl, "--", CLFL, true);
		DropdownMenu order = orderDrop(prmOrder, "--", ORDER);
		DropdownMenu sizeMenu = sizeDrop(sizePrm, iGlobalSize);

		DataTable T = new DataTable();
		T.addTitle(localize("filter", "Filter"));
		T.setTitlesHorizontal(true);
		T.setBottomHeight(3);
		T.add(getHeader(localize("status", "Status")), 1, 1);
		T.add(getHeader(localize("complex", "Complex")), 2, 1);
		T.add(getHeader(localize("building", "Building")), 3, 1);
		T.add(getHeader(localize("floor", "Floor")), 4, 1);
		T.add(getHeader(localize("order", "Order")), 5, 1);
		T.add(getHeader(localize("viewsize", "View size")), 6, 1);
		T.add(status, 1, 2);
		T.add(complex, 2, 2);
		T.add(building, 3, 2);
		T.add(floor, 4, 2);
		T.add(order, 5, 2);
		T.add(sizeMenu, 6, 2);
		SubmitButton get = (SubmitButton) getSubmitButton("conget", null,
				"Get", "get");
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

	private DropdownMenu drpLodgings(Class lodgings, String name,
			String display, Integer selected) {
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

	private DropdownMenu drpFloors(String name, String display,
			Integer selected, boolean withBuildingName) {
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
					drp.addMenuElement(floor.getPrimaryKey().toString(), floor
							.getName()
							+ " " + (floor.getBuilding().getName()));
				} catch (Exception e) {
				}
			} else
				drp.addMenuElement(floor.getPrimaryKey().toString(), floor
						.getName());
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
		drp.addMenuElement(ContractFinder.APARTMENT, localize("apartment",
				"Apartment"));
		drp.addMenuElement(ContractFinder.FLOOR, localize("floor", "Floor"));
		drp.addMenuElement(ContractFinder.BUILDING, localize("building",
				"Building"));
		drp.addMenuElement(ContractFinder.COMPLEX, localize("complex",
				"Complex"));
		drp.addMenuElement(ContractFinder.CATEGORY, localize("category",
				"Category"));
		drp.addMenuElement(ContractFinder.TYPE, localize("type", "Type"));
		drp.addMenuElement(ContractFinder.START_DATE, localize("valid_from",
				"Valid from"));
		drp.addMenuElement(ContractFinder.END_DATE, localize("valid_to",
				"Valid to"));
		if (selected != null)
			drp.setSelectedElement(selected.toString());
		return drp;
	}

	private PresentationObject getContractTable(IWContext iwc) {
		Collection contracts = null;
		int contractCount = 0;
		try {
			ContractHome cHome = getContractService(iwc).getContractHome();
			contracts = cHome.findBySearchConditions(sGlobalStatus, CLCX, CLBU,
					CLFL, CLTP, CLCT, ORDER.intValue(), this.iGlobalSize, index
							.intValue());
			contractCount = cHome.countBySearchConditions(sGlobalStatus, CLCX,
					CLBU, CLFL, CLTP, CLCT, ORDER.intValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		boolean doAutomaticCharges = iwc.getApplicationSettings().getBoolean("EXECUTE_AUTOMATIC_CHARGES", false);
		boolean doChargeForUnlimitedDownload = iwc.getApplicationSettings().getBoolean("SHOW_DOWNLOAD_CHARGES", false);
		boolean showContractTariff = iwc.getApplicationSettings().getBoolean("SHOW_CONTRACT_TARIFF", false);
		
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, iwc
				.getCurrentLocale());
		Contract C = null;
		//User U = null;
		Applicant Ap = null;
		Apartment A = null;
		//Building B = null;
		//Floor F = null;
		//Complex CX = null;
		Image printImage = getBundle().getImage("/print.gif");
		Image registerImage = getBundle().getImage("/pen.gif");
		Image resignImage = getBundle().getImage("/scissors.gif");
		Image keyImage = getBundle().getImage("/key.gif");
		Image nokeyImage = getBundle().getImage("/nokey.gif");
		Image garbageImage = getBundle().getImage("/trashcan.gif");
		Image renewImage = getBundle().getImage("/renew.gif");
		Image contractTariff = getBundle().getImage("/dollar.gif");
		boolean garbage = false;
		boolean both = false;
		int row = 1;
		int col = 1;
		int maxCol = col;
		Form f = new Form();
		DataTable T = getDataTable();
		f.add(T);
		T.setTitlesHorizontal(true);
		T.setWidth("100%");
		T.add(getHeader("#"), col++, 1);
		if (sGlobalStatus.equals(ContractBMPBean.STATUS_ENDED)
				|| sGlobalStatus.equals(ContractBMPBean.STATUS_RESIGNED)
				|| sGlobalStatus.equals(ContractBMPBean.STATUS_REJECTED)) {
			T.add((garbageImage), col++, 1);
			garbage = true;
		} else {
			if (sGlobalStatus.equals(ContractBMPBean.STATUS_SIGNED) || sGlobalStatus.equals(ContractBMPBean.STATUS_CREATED)
					|| sGlobalStatus.equals(ContractBMPBean.STATUS_PRINTED)) {
				T.add((garbageImage), col++, 1);
				both = true;
			}
			T.add((printImage), col++, 1);// Edit.formatText(localize("print",
			// "Print")
		}
		T.add((resignImage), col++, 1);// Edit.formatText(localize("sign","Sign")
		// )
		T.add((registerImage), col++, 1);// Edit.formatText(localize("sign","Sign"
		// ))
		T.add((renewImage), col++, 1);
		// col = 4;
		if (showContractTariff) {
			T.add((contractTariff), col++, 1);			
		}
		
		T.add(getHeader(localize("name", "Name")), col++, 1);
		T.add(getHeader(localize("ssn", "Socialnumber")), col++, 1);
		T.add(getHeader(localize("apartment", "Apartment")), col++, 1);
		T.add(getHeader(localize("validfrom", "Valid from")), col++, 1);
		T.add(getHeader(localize("validto", "Valid To")), col++, 1);
		T.add(keyImage, col++, 1);
		if (doAutomaticCharges) {
			if (doChargeForUnlimitedDownload) {
				T.add(getHeader(localize("charge_download", "Download")), col++, 1);
			}
			T.add(getHeader(localize("charge_handling", "Handling")), col++, 1);
			T.add(getHeader(localize("charge_transfer", "Transfer")), col++, 1);
		}
		row++;

		StringBuffer listOfUsers = new StringBuffer();

		if (contracts != null) {
			int len = contracts.size();
			if (iGlobalSize > 0 && iGlobalSize <= len) {
				len = iGlobalSize;
			}
			T.addTitle(localize("contracts", "Contracts") + " "
					+ localize("showing", "showing") + " " + len + " "
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
					//U = C.getUser();
					Ap = C.getApplicant();
					A = C.getApartment();
					T.add(getEditLink(getText(String.valueOf(i)), contractID),
							col++, row);
					if (garbage)
						T.add(getGarbageLink(garbageImage, contractID), col++,
								row);
					else {
						if (both) {
							T.add(getGarbageLink(garbageImage, contractID),
									col++, row);
						}
						T.add(getPDFLink(printImage, contractID, Ap.getSSN()),
								col++, row);
					}
					if (status.equalsIgnoreCase(ContractBMPBean.STATUS_SIGNED))
						T.add(getResignLink(resignImage, contractID), col, row);
					col++;
					if (status.equalsIgnoreCase(ContractBMPBean.STATUS_PRINTED)
							|| status
									.equalsIgnoreCase(ContractBMPBean.STATUS_SIGNED))
						T.add(
								getSignedLink(registerImage, contractID,
										isAdmin), col, row);
					col++;
					if (status.equalsIgnoreCase(ContractBMPBean.STATUS_ENDED)
							|| status
									.equalsIgnoreCase(ContractBMPBean.STATUS_RESIGNED)
							|| status
									.equalsIgnoreCase(ContractBMPBean.STATUS_SIGNED))
						T.add(getRenewLink(renewImage, contractID), col, row);
					col++;
					if (showContractTariff) {
						T.add(getContractTariffLink(contractTariff, contractID), col++, row);						
					}
					T.add(getText(Ap.getFullName()), col++, row);
					T.add(getText(Ap.getSSN()), col++, row);
					T.add((getApartmentTable(A)), col++, row);
					T.add(getText(df.format(C.getValidFrom())), col++, row);
					T.add(getText(df.format(C.getValidTo())), col++, row);
					if (C.getIsRented()) {
						T.add(getKeyLink(keyImage, contractID), col++, row);
					} else {
						T.add(getKeyLink(nokeyImage, contractID), col++, row);
					}

					if (doAutomaticCharges) {
						AutomaticCharges unlimited = this
								.getCampusService(iwc).getContractService()
								.getAutomaticChargesByUser(C.getUser());
						boolean chargeDownload = false;
						boolean chargeHandling = false;
						boolean chargeTransfer = false;
						if (unlimited != null) {
							if (unlimited.getChargeForDownload()) {
								chargeDownload = true;
							}
							
							if (unlimited.getChargeForHandling()) {
								chargeHandling = true;
							}
							
							if (unlimited.getChargeForTransfer()) {
								chargeTransfer = true;
							}
						}

						listOfUsers.append(C.getUserId().toString());
						listOfUsers.append(";");

						if (doChargeForUnlimitedDownload) {
							CheckBox chargeForUnlimited = new CheckBox(
									CHARGE_FOR_UNLIMITED, C.getUserId().toString());
	
							chargeForUnlimited.setChecked(chargeDownload);
							T.add(chargeForUnlimited, col++, row);
						}
	
						CheckBox chargeForHandling = new CheckBox(
								CHARGE_FOR_HANDLING, C.getUserId().toString());
	
						chargeForHandling.setChecked(chargeHandling);
						T.add(chargeForHandling, col++, row);
	
						CheckBox chargeForTransfer = new CheckBox(
								CHARGE_FOR_TRANSFER, C.getUserId().toString());
						
						chargeForTransfer.setChecked(chargeTransfer);
						T.add(chargeForTransfer, col++, row);
					}
					
					row++;
					if (maxCol < col) {
						maxCol = col;
					}
					col = 1;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		maxCol--;

		if (doAutomaticCharges && maxCol > 0) {
			SubmitButton save = new SubmitButton(SAVE, SAVE, listOfUsers
					.toString());
			T.add(save, maxCol, row);
		}

		Table T2 = new Table();
		T2.setCellpadding(0);
		T2.setCellspacing(0);
		T2.add(f, 1, 1);
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
		try {
			Integer id = Integer.valueOf(iwc.getParameter(PARAM_GARBAGE));
			getContractService(iwc).doGarbageContract(id);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private PresentationObject getApartmentTable(Apartment A) {
		Table T = new Table();
		ApartmentType type = A.getApartmentType();
		Floor F = A.getFloor();
		Building B = F.getBuilding();
		Complex C = B.getComplex();
		T.add(getText(A.getName()), 1, 1);
		T.add(getText(F.getName()), 2, 1);
		T.add(getText(B.getName()), 3, 1);
		T.add(getText(C.getName()), 4, 1);
		if (type.getAbbreviation() != null)
			T.add(" (" + type.getAbbreviation() + ")", 5, 1);
		return T;
	}

	private String getStatus(String status) {
		String r = "";
		char c = status.charAt(0);
		switch (c) {
		case 'C':
			r = localize("created", "Created");
			break;
		case 'P':
			r = localize("printed", "Printed");
			break;
		case 'S':
			r = localize("signed", "Signed");
			break;
		case 'R':
			r = localize("rejected", "Rejected");
			break;
		case 'T':
			r = localize("terminated", "Terminated");
			break;
		case 'U':
			r = localize("resigned", "Resigned");
			break;
		case 'E':
			r = localize("ended", "Ended");
			break;
		case 'G':
			r = localize("garbaged", "Canned");
			break;
		case 'X':
			r = localize("renewal", "Renewal");
			break;
		}
		return r;
	}

	private DropdownMenu statusDrop(String name, String selected, boolean showRenewal) {
		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElement("C", getStatus("C"));
		drp.addMenuElement("P", getStatus("P"));
		drp.addMenuElement("S", getStatus("S"));
		drp.addMenuElement("R", getStatus("R"));
		drp.addMenuElement("U", getStatus("U"));
		drp.addMenuElement("E", getStatus("E"));
		drp.addMenuElement("G", getStatus("G"));
		if (showRenewal) {
			drp.addMenuElement("X", getStatus("X"));			
		}
		drp.setSelectedElement(selected);
		return drp;
	}

	public static Link getSignedLink(PresentationObject MO, int contractId,
			boolean isAdmin) {
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

	public static Link getContractTariffLink(PresentationObject MO, int contractId) {
		Link L = new Link(MO);
		L.setWindowToOpen(ContractTariffWindow.class);
		L.addParameter(ContractTariffWindow.prmContractId, contractId);
		return L;
	}

	
	public Link getResignLink(PresentationObject MO, int contractId) {
		Link L = new Link(MO);
		L.setWindowToOpen(ContractResignWindow.class);
		if (isAdmin) {
			L.addParameter(ContractResignWindow.PARAM_IS_ADMIN, "true");
		}
		L.addParameter("contract_id", contractId);

		return L;
	}

	public static Link getKeyLink(PresentationObject MO, int contractId) {
		Link L = new Link(MO);
		L.setWindowToOpen(ContractKeyWindow.class, "400", "260", true, true);
		L.addParameter(ContractKeyWindow.PARAM_CONTARACT, contractId);
		return L;
	}

	public Link getPDFLink(PresentationObject MO, int contractId) {
		Link L = new Link(MO);
		L.setWindowToOpen(ContractFilerWindow.class);
		L.addParameter(ContractFiler.prmOneId, contractId);
		return L;
	}

	public Link getPDFLink(PresentationObject MO, int contractId,
			String filename) {
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

	public static Link getGarbageLink(PresentationObject MO, int contractId) {
		Link L = new Link(MO);
		L.setWindowToOpen(ContractGarbageWindow.class);
		L.addParameter(ContractGarbageWindow.prmContractId, contractId);
		// L.addParameter("garbage",contractId);
		return L;
	}

	public void main(IWContext iwc) {
		isAdmin = iwc.hasEditPermission(this);
		/*
		try {
			buildingService = (BuildingService) IBOLookup.getServiceInstance(
					iwc, BuildingService.class);
		} catch (IBOLookupException e) {
			e.printStackTrace();
		}
		*/
		control(iwc);
	}
}