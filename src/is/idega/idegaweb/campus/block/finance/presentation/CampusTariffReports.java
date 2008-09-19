package is.idega.idegaweb.campus.block.finance.presentation;

import is.idega.idegaweb.campus.business.CampusSettings;
import is.idega.idegaweb.campus.data.AccountEntryReport;
import is.idega.idegaweb.campus.data.AccountEntryReportBMPBean;
import is.idega.idegaweb.campus.data.EntryReport;
import is.idega.idegaweb.campus.data.EntryReportBMPBean;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.block.building.data.Building;
import com.idega.block.building.data.BuildingHome;
import com.idega.block.finance.data.AccountKey;
import com.idega.block.finance.data.AccountKeyHome;
import com.idega.block.finance.data.EntryGroup;
import com.idega.block.finance.data.EntryGroupHome;
import com.idega.block.finance.presentation.Finance;
import com.idega.core.file.data.ICFile;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.GenericSelect;
import com.idega.presentation.ui.SelectDropdown;
import com.idega.presentation.ui.SelectOption;
import com.idega.presentation.ui.SelectPanel;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;
import com.idega.util.poi.POIUtility;

/**
 * Title: Description: Copyright: Copyright (c) 2000-2001 idega.is All Rights
 * Reserved Company: idega
 * 
 * @author <a href="mailto:aron@idega.is">Aron Birkir </a>
 * @version 1.1
 */
public class CampusTariffReports extends Finance {

	private static final String SHOW_BY_TARIFFKEY = "bytariffkey";

	private static final String SHOW_BY_APARTMENT = "byapartment";

	private static final String SHOW_BY_CONTRACT = "bycontract";

	private static final String CREATE_EXCEL_FILE = "createExcel";

	private IWResourceBundle iwrb;

	private String prmDateFrom = "ctr_date_from";

	private String prmDateTo = "ctr_date_to";

	private String prmBuilding = "ctr_building";

	private String prmAccountKey = "ctr_account_key";

	private String prmEntryGroup = "ctr_entry_group";

	private IWTimestamp from = null, to = null;

	Integer building = null, key = null, entryGroupId = null;

	String[] buildings = null, acckeys = null;
	String entryGroup = null;

	private List reports = null, reportsByApartment = null;

	private Collection accountReports = null;

	private Collection distinctKeys = null;

	private Collection keys = null;

	private Map keyMap = null;

	private boolean useBoxSelection = true, createExcelFile = false;

	public CampusTariffReports() {
		setWidth("100%");
	}

	public String getBundleIdentifier() {
		return CampusSettings.IW_BUNDLE_IDENTIFIER;
	}

	public void main(IWContext iwc) throws java.rmi.RemoteException {
		// debugParameters(iwc);
		iwb = getBundle(iwc);
		iwrb = getResourceBundle(iwc);
		if (iwc.isLoggedOn()) {
			if (iwc.isParameterSet("search"))
				parse(iwc);
			setLocalizedTitle("entry_reports", "Entry reports");
			setSearchPanel(getSearchForm(iwc));
			if (reports != null) {
				PresentationObject result = getResult(iwc);
				if (createExcelFile) {
					ICFile file = POIUtility.createICFileFromTable(((DataTable)result).getContentTable(), "bybuilding.xls", "By building");
					setMainPanel(getXLSLink(file));
				}
				setMainPanel(result);
			} else if (reportsByApartment != null) {
				PresentationObject result = getResultByApartment(iwc);
				if (createExcelFile) {
					ICFile file = POIUtility.createICFileFromTable(((DataTable)result).getContentTable(), "byapartment.xls", "By apartment");
					setMainPanel(getXLSLink(file));
				}
				setMainPanel(result);
			} else if (accountReports != null) {
				PresentationObject result = getAccountResult(iwc);
				if (createExcelFile) {
					ICFile file = POIUtility.createICFileFromTable((Table)result, "byaccount.xls", "By account");
					setMainPanel(getXLSLink(file));
				}

				setMainPanel(result);
			}
		}
	}

	protected Link getXLSLink(ICFile file) throws RemoteException {
		Link link = new Link(this.iwb.getImage("xls.gif"));
		link.setTarget(Link.TARGET_NEW_WINDOW);
		link.setFile(file);

		return link;
	}

	
	private void parse(IWContext iwc) {
		if (iwc.isParameterSet(prmDateFrom)) {
			try {
				from = new IWTimestamp(iwc.getParameter(prmDateFrom));
			} catch (Exception ex) {
			}
		}

		if (iwc.isParameterSet(prmDateTo)) {
			try {
				to = new IWTimestamp(iwc.getParameter(prmDateTo));
			} catch (Exception ex) {
			}
		}

		buildings = iwc.isParameterSet(prmBuilding) ? iwc
				.getParameterValues(prmBuilding) : null;
		acckeys = iwc.isParameterSet(prmAccountKey) ? iwc
				.getParameterValues(prmAccountKey) : null;
		entryGroup = iwc.isParameterSet(prmEntryGroup) ? iwc.getParameter(prmEntryGroup) : null;
		
		createExcelFile = iwc.isParameterSet(CREATE_EXCEL_FILE);
		
		try {
			Collection keys = ((AccountKeyHome) IDOLookup
					.getHome(AccountKey.class)).findByPrimaryKeys(acckeys);
			distinctKeys = new ArrayList(keys.size());
			keyMap = new HashMap(keys.size());
			for (Iterator iter = keys.iterator(); iter.hasNext();) {
				AccountKey element = (AccountKey) iter.next();
				distinctKeys.add((Integer) element.getPrimaryKey());
				keyMap
						.put((Integer) element.getPrimaryKey(), element
								.getInfo());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Integer entryGroupID = null;
		if (entryGroup != null) {
			entryGroupID = new Integer(entryGroup);
		}
		
		if (!iwc.isParameterSet(SHOW_BY_CONTRACT)) {
			boolean showByApartment = iwc.isParameterSet(SHOW_BY_APARTMENT);
			try {
				if (!showByApartment) {
					reports = EntryReportBMPBean.findAllBySearch(buildings,
							acckeys, from.getTimestamp(), to.getTimestamp(),
							false, entryGroupID);
				} else {
					reportsByApartment = EntryReportBMPBean.findAllBySearch(
							buildings, acckeys, from.getTimestamp(), to
									.getTimestamp(), true, entryGroupID);
				}
			} catch (java.sql.SQLException ex) {
				ex.printStackTrace();
			}
		} else {
			try {
				accountReports = AccountEntryReportBMPBean.findAllBySearch(
						buildings, acckeys, from.getTimestamp(), to
								.getTimestamp(), iwc
								.isParameterSet(SHOW_BY_TARIFFKEY));
			} catch (java.sql.SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	public PresentationObject getSearchForm(IWContext iwc) throws java.rmi.RemoteException {
		DataTable T = getDataTable();
		T.setUseBottom(false);
		T.setUseTop(false);
		T.setTitlesHorizontal(true);
		GenericSelect buildings = getBuildingsSelection(prmBuilding);
		buildings.setSelectedOption(String.valueOf(building));
		buildings.keepStatusOnAction(true);
		GenericSelect keys = getAccountKeysSelection(prmAccountKey);
		keys.setSelectedOption(String.valueOf(key));
		keys.keepStatusOnAction(true);
		IWTimestamp today = IWTimestamp.RightNow();
		DateInput inpFrom = new DateInput(prmDateFrom, true);
		if (from != null)
			inpFrom.setDate(from.getDate());
		else
			inpFrom.setDate(new IWTimestamp(1, today.getMonth(), today
					.getYear()).getDate());
		inpFrom.setYearRange(today.getYear() - 5, today.getYear() + 1);
		DateInput inpTo = new DateInput(prmDateTo, true);
		if (to != null)
			inpTo.setDate(to.getDate());
		else
			inpTo.setDate(today.getDate());
		inpTo.setYearRange(today.getYear() - 5, today.getYear() + 1);
		
		GenericSelect inpEntryGroup = getEntryGroupSelection(prmEntryGroup);
		inpEntryGroup.setSelectedOption(entryGroup);
		
		T.add(getHeader(iwrb.getLocalizedString("building", "Building")), 1, 1);
		T.add(getHeader(iwrb.getLocalizedString("account_key", "Account_key")),
				2, 1);
		T.add(getHeader(iwrb.getLocalizedString("date_from", "Due date from")),
				3, 1);
		T.add(getHeader(iwrb.getLocalizedString("date_to", "Due date to")), 4,
				1);
		T.add(getHeader(iwrb.getLocalizedString("entry_group", "Entry group")), 5,
				1);
		T.add(buildings, 1, 2);
		T.add(keys, 2, 2);
		T.add(inpFrom, 3, 2);
		T.add(inpTo, 4, 2);
		T.add(inpEntryGroup, 5, 2);

		CheckBox chkByContract = new CheckBox(SHOW_BY_CONTRACT, "true");
		CheckBox chkByTariffKey = new CheckBox(SHOW_BY_TARIFFKEY, "true");
		CheckBox chkByApartment = new CheckBox(SHOW_BY_APARTMENT, "true");
		CheckBox chkCreateExcel = new CheckBox(CREATE_EXCEL_FILE, "true");

		chkByContract.setToDisableWhenChecked(chkByApartment);
		chkByContract.setToEnableWhenUnchecked(chkByApartment);

		chkByApartment.setToDisableWhenChecked(chkByContract);
		chkByApartment.setToDisableWhenChecked(chkByTariffKey);
		chkByApartment.setToEnableWhenUnchecked(chkByContract);
		chkByApartment.setToEnableWhenUnchecked(chkByTariffKey);

		chkByContract.keepStatusOnAction();
		chkByTariffKey.keepStatusOnAction();
		chkByApartment.keepStatusOnAction();
		
		if (iwc.isParameterSet(SHOW_BY_APARTMENT)) {
			chkByContract.setChecked(false);
			chkByContract.setDisabled(true);
			chkByTariffKey.setDisabled(true);
		} else if (iwc.isParameterSet(SHOW_BY_CONTRACT)) {
			chkByApartment.setChecked(false);
			chkByApartment.setDisabled(true);
		}
		
		T.add(chkByContract, 1, 3);
		T.add(getSmallHeader(localize("show_by_accounts", "Show by accounts")),
				1, 3);
		T.add(chkByTariffKey, 1, 3);
		T.add(
				getSmallHeader(localize("show_by_tariffkey",
						"Show by tariff key")), 1, 3);
		T.add(chkByApartment, 1, 3);
		T.add(getSmallHeader(localize("show_by_apartments",
				"Show by apartments")), 1, 3);
		T.add(chkCreateExcel, 1, 3);
		T.add(getSmallHeader(localize("create_excel_file",
				"Create excel file")), 1, 3);
		T.getContentTable().mergeCells(1, 3, 4, 3);
		T.getContentTable()
				.setRowVerticalAlignment(2, Table.VERTICAL_ALIGN_TOP);
		SubmitButton search = new SubmitButton(iwrb.getLocalizedImageButton(
				"search", "Search"), "search", "true");
		T.addButton(search);
		return T;
	}

	public PresentationObject getResult(IWContext iwc)
			throws java.rmi.RemoteException {
		DataTable T = new DataTable();
		List L = reports;
		T.setUseBottom(false);
		T.setUseTop(false);
		T.setTitlesHorizontal(true);
		if (L != null && !L.isEmpty()) {
			java.util.Iterator iter = L.iterator();
			EntryReport entry;
			int iBuildingId = -1, row = 1, col = 1;
			float total = 0, Alltotal = 0;
			// boolean last = false;
			T.add(getHeader(iwrb.getLocalizedString("building", "Building")),
					1, row);
			T.add(getHeader(iwrb.getLocalizedString("account_key",
					"Account key")), 2, row);
			T.add(getHeader(iwrb.getLocalizedString("account_key_info",
					"Account key info")), 3, row);
			T.add(getHeader(iwrb.getLocalizedString("sum", "Sum")), 4, row);
			T.add(getHeader(iwrb.getLocalizedString("number", "Number")), 5,
					row);
			row++;
			while (iter.hasNext()) {
				col = 1;
				entry = (EntryReport) iter.next();
				if (iBuildingId != entry.getBuildingId()) {
					if (iBuildingId > 0) {
						T.add(getAmountText((total)), 4, row++);
						Alltotal += total;
					}
					T.add(getHeader(entry.getBuildingName()), 1, row++);
					total = 0;
				}
				col++;
				T.add(getText(entry.getKeyName()), 2, row);
				T.add(getText(entry.getKeyInfo()), 3, row);
				T.add(getAmountText((entry.getTotal())), 4, row);
				T.add(getText(" ( " + entry.getNumber() + " )"), 5, row);
				iBuildingId = entry.getBuildingId();
				total += entry.getTotal();
				row++;
			}
			if (iBuildingId > 0) {
				T.add(getAmountText((total)), 4, row++);
				Alltotal += total;
			}
			if (!(Alltotal == total))
				T.add(getAmountText((Alltotal)), 4, row);
			T.getContentTable().setColumnAlignment(4, "right");
			T.getContentTable().setColumnAlignment(5, "right");
		} else
			T.add(getHeader(iwrb.getLocalizedString("no_entries_found",
					"No entries where found")));
		
		return T;
	}

	public PresentationObject getResultByApartment(IWContext iwc)
			throws java.rmi.RemoteException {
		DataTable T = new DataTable();
		List L = reportsByApartment;
		T.setUseBottom(false);
		T.setUseTop(false);
		T.setTitlesHorizontal(true);
		if (L != null && !L.isEmpty()) {
			java.util.Iterator iter = L.iterator();
			EntryReport entry;
			int iBuildingId = -1, row = 1, col = 1;
			float total = 0, Alltotal = 0;
			// boolean last = false;
			T.add(getHeader(iwrb.getLocalizedString("building", "Building")),
					1, row);
			T.add(getHeader(iwrb.getLocalizedString("apartment", "Apartment")),
					2, row);
			T.add(getHeader(iwrb.getLocalizedString("account_key",
					"Account key")), 3, row);
			T.add(getHeader(iwrb.getLocalizedString("account_key_info",
					"Account key info")), 4, row);
			T.add(getHeader(iwrb.getLocalizedString("sum", "Sum")), 5, row);
			T.add(getHeader(iwrb.getLocalizedString("number", "Number")), 6,
					row);
			row++;
			while (iter.hasNext()) {
				col = 1;
				entry = (EntryReport) iter.next();
				if (iBuildingId != entry.getBuildingId()) {
					if (iBuildingId > 0) {
						T.add(getAmountText((total)), 4, row++);
						Alltotal += total;
					}
					T.add(getHeader(entry.getBuildingName()), 1, row++);
					total = 0;
				}
				col++;
				T.add(getText(entry.getApartmentName()), 2, row);
				T.add(getText(entry.getKeyName()), 3, row);
				T.add(getText(entry.getKeyInfo()), 4, row);
				T.add(getAmountText((entry.getTotal())), 5, row);
				T.add(getText(" ( " + entry.getNumber() + " )"), 6, row);
				iBuildingId = entry.getBuildingId();
				total += entry.getTotal();
				row++;
			}
			if (iBuildingId > 0) {
				T.add(getAmountText((total)), 4, row++);
				Alltotal += total;
			}
			if (!(Alltotal == total))
				T.add(getAmountText((Alltotal)), 4, row);
			T.getContentTable().setColumnAlignment(4, "right");
			T.getContentTable().setColumnAlignment(5, "right");
		} else
			T.add(getHeader(iwrb.getLocalizedString("no_entries_found",
					"No entries where found")));
		return T;
	}

	public PresentationObject getAccountResult(IWContext iwc) {
		Table T = new Table();
		T.setNoWrap();
		if (accountReports != null && !accountReports.isEmpty()) {
			Collection coll = groupReportsByContracts();
			Map contractMap;
			int row = 2;
			int offset = 4;
			int col = offset;
			int keySize = distinctKeys.size();
			T.add(getHeader(localize("name", "Name")), 2, 1);
			T.add(getHeader(localize("personal_id", "Personal ID")), 3, 1);
			for (Iterator iterator = distinctKeys.iterator(); iterator
					.hasNext();) {
				Integer keyId = (Integer) iterator.next();
				T.add(getHeader((String) keyMap.get(keyId)), col++, 1);
			}
			T.add(getHeader(localize("total", "Total")), col++, 1);
			GridResult result = new GridResult(keySize, coll.size());
			int resultRow = 0, resultCol = 0;
			int ind = 1;
			for (Iterator iter = coll.iterator(); iter.hasNext();) {
				resultCol = 0;
				T.add(getText(String.valueOf(ind++)), 1, row);
				contractMap = (Map) iter.next();
				boolean common = false;
				col = offset;
				// for every distinct account key
				// int index = 0;
				for (Iterator iterator = distinctKeys.iterator(); iterator
						.hasNext();) {
					Integer keyId = (Integer) iterator.next();
					AccountEntryReport report = (AccountEntryReport) contractMap
							.get(keyId);
					// print common info
					if (report != null) {
						if (!common) {
							T.add(getText(report.getName()), 2, row);
							T.add(getText(report.getPersonalID()), 3, row);
							common = true;
						}
						double lineKeyTotal = report.getTotal().doubleValue();
						result.add(lineKeyTotal, resultCol, resultRow);
						T.add(getAmountText(lineKeyTotal), col, row);
					}
					resultCol++;
					col++;
				}
				T.add(
						getAmountText(result.getRowTotal(resultRow)
								.doubleValue()), col, row);
				resultRow++;
				row++;
			}
			int ii = 0;
			for (int i = 0; i < keySize; i++) {
				ii = offset + i;
				T.add(getAmountText(result.getColumnTotal(i).doubleValue()),
						ii, row);
			}
			ii++;
			T.add(getAmountText(result.getTotal().doubleValue()), ii, row);
			T.setVerticalZebraColored(getZebraColor1(), getZebraColor2());
			T.setRowColor(1, getHeaderColor());
			T.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_RIGHT);
			T.setAlignment(3, 1, Table.HORIZONTAL_ALIGN_LEFT);
			for (int i = offset; i <= keySize + offset; i++) {
				T.setColumnAlignment(i, Table.HORIZONTAL_ALIGN_RIGHT);
				T.setAlignment(i, 1, Table.HORIZONTAL_ALIGN_LEFT);
			}
			T.setRowColor(row, getHeaderColor());
		}
		return T;
	}

	private class GridResult {

		private BigDecimal[][] grid;

		private int rowTotalCol;

		private int colTotalRow;

		public GridResult(int columns, int rows) {
			grid = new BigDecimal[columns + 1][rows + 1];
			rowTotalCol = columns;
			colTotalRow = rows;
		}

		public void add(float value, int xpos, int ypos) {
			add(new BigDecimal(value), xpos, ypos);
		}

		public void add(double value, int xpos, int ypos) {
			add(new BigDecimal(value), xpos, ypos);
		}

		public void add(BigDecimal value, int xpos, int ypos) {
			if (grid[xpos][ypos] == null)
				grid[xpos][ypos] = value;
			else
				grid[xpos][ypos] = grid[xpos][ypos].add(value);
			// update row result
			if (grid[rowTotalCol][ypos] == null)
				grid[rowTotalCol][ypos] = value;
			else
				grid[rowTotalCol][ypos] = grid[rowTotalCol][ypos].add(value);
			// update col result
			if (grid[xpos][colTotalRow] == null)
				grid[xpos][colTotalRow] = value;
			else
				grid[xpos][colTotalRow] = grid[xpos][colTotalRow].add(value);
		}

		public BigDecimal getRowTotal(int row) {
			if (grid[rowTotalCol][row] != null)
				return grid[rowTotalCol][row];
			return new BigDecimal(0.0d);
		}

		public BigDecimal getColumnTotal(int column) {
			if (grid[column][colTotalRow] != null)
				return grid[column][colTotalRow];
			return new BigDecimal(0.0d);
		}

		public BigDecimal getTotal() {
			BigDecimal total = new BigDecimal(0.0d);
			for (int i = 0; i < grid.length - 1; i++) {
				for (int j = 0; j < grid[i].length - 1; j++) {
					if (grid[i][j] != null) {
						total = total.add(grid[i][j]);
					}
				}
			}

			return total;
		}
	}

	private Collection groupReportsByContracts() {
		List collOfMaps = new ArrayList();
		Map mapOfIndexByContract = new HashMap();
		if (distinctKeys == null) {
			distinctKeys = new ArrayList();
		}
		if (keyMap == null) {
			keyMap = new HashMap();
		}
		for (Iterator iter = accountReports.iterator(); iter.hasNext();) {
			AccountEntryReport element = (AccountEntryReport) iter.next();
			Integer accountID = element.getAccountID();
			if (mapOfIndexByContract.containsKey(accountID)) {
				int index = ((Integer) mapOfIndexByContract.get(accountID))
						.intValue();
				Map m = ((Map) collOfMaps.get(index));
				// New: Check for previous entry and add up values if any
				AccountEntryReport previous = (AccountEntryReport) m
						.get(element.getKeyID());
				if (previous != null) {
					element.setTotal(new Float(element.getTotal().floatValue()
							+ previous.getTotal().floatValue()));
				}
				m.put(element.getKeyID(), element);
				collOfMaps.set(index, m);
			} else {
				Map map = new HashMap();
				map.put(element.getKeyID(), element);
				mapOfIndexByContract.put(accountID, new Integer(collOfMaps
						.size()));
				collOfMaps.add(map);
			}
			if (!distinctKeys.contains(element.getKeyID())) {
				distinctKeys.add(element.getKeyID());
				if (!keyMap.containsKey(element.getKeyID())) {
					keyMap.put(element.getKeyID(), element.getKeyInfo());
				}
			}
		}
		return collOfMaps;
	}

	public GenericSelect getBuildingsSelection(String name)
			throws java.rmi.RemoteException {
		GenericSelect drp = useBoxSelection ? (GenericSelect) new SelectPanel(
				name) : (GenericSelect) new SelectDropdown(name);
		try {
			Collection buildings = ((BuildingHome) IDOLookup
					.getHome(Building.class)).findAll();
			if (buildings != null && !buildings.isEmpty()) {
				Iterator iter = buildings.iterator();
				Building building;
				if (!useBoxSelection) {
					drp.addOption(new SelectOption(iwrb.getLocalizedString(
							"all_buildings", "All buildings"), ""));
				}
				while (iter.hasNext()) {
					building = (Building) iter.next();
					drp.addOption(new SelectOption(building.getName(), building
							.getPrimaryKey().toString()));
				}
			}
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (EJBException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

		return drp;
	}

	public GenericSelect getAccountKeysSelection(String name)
			throws java.rmi.RemoteException {
		GenericSelect drp = useBoxSelection ? (GenericSelect) new SelectPanel(
				name) : (GenericSelect) new SelectDropdown(name);
		Collection keys = null;
		try {
			keys = ((AccountKeyHome) IDOLookup.getHome(AccountKey.class))
					.findAll();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		if (keys != null && !keys.isEmpty()) {
			Iterator iter = keys.iterator();
			AccountKey key;
			if (!useBoxSelection) {
				drp.addOption(new SelectOption(iwrb.getLocalizedString(
						"all_account_keys", "All account keys"), ""));
			}
			while (iter.hasNext()) {
				key = (AccountKey) iter.next();
				drp.addOption(new SelectOption(key.getInfo(), key
						.getPrimaryKey().toString()));
			}
		}

		return drp;
	}

	public GenericSelect getEntryGroupSelection(String name)
			throws java.rmi.RemoteException {
		GenericSelect drp = (GenericSelect) new SelectDropdown(name);
		Collection entryGroups = null;
		try {
			entryGroups = ((EntryGroupHome) IDOLookup.getHome(EntryGroup.class))
					.findAll();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		if (entryGroups != null && !entryGroups.isEmpty()) {
			Iterator iter = entryGroups.iterator();
			EntryGroup entryGroup;
				drp.addOption(new SelectOption(iwrb.getLocalizedString(
						"use_date_filter", "Use date filter"), ""));
			while (iter.hasNext()) {
				entryGroup = (EntryGroup) iter.next();
				StringBuffer groupName = new StringBuffer(entryGroup.getPrimaryKey().toString());
				groupName.append(" (");
				groupName.append(new IWTimestamp(entryGroup.getGroupDate()).getDateString("dd.MM.yyyy"));
				groupName.append(")");
				drp.addOption(new SelectOption(groupName.toString(), entryGroup
						.getPrimaryKey().toString()));
			}
		}

		return drp;
	}

}