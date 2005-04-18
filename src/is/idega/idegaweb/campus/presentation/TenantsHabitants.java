package is.idega.idegaweb.campus.presentation;

import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import is.idega.idegaweb.campus.business.HabitantsCollector;
import is.idega.idegaweb.campus.business.HabitantsComparator;
import is.idega.idegaweb.campus.data.Habitant;
import is.idega.idegaweb.campus.data.HabitantHome;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import com.idega.block.application.data.Applicant;
import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.business.BuildingService;
import com.idega.block.building.data.ApartmentView;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.ComplexHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DataTable;
import com.idega.util.text.StyleConstants;
import com.idega.util.text.TextStyler;

/**
 * Title: Description: Copyright: Copyright (c) 2000-2001 idega.is All Rights
 * Reserved Company: idega
 * 
 * @author <a href="mailto:laddi@idega.is">??rhallur Helgason </a>
 * @version 1.1
 */
public class TenantsHabitants extends CampusBlock implements Campus {

	private static final String NAME_KEY = "cam_habitants_view";

	private static final String DEFAULT_VALUE = "Habitant list";

	private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.campus";

	private final static String PARAMETER_CAMPUS_ID = "campus_id";

	private final static String PARAMETER_ORDER_ID = "order_id";

	private final static String PRM_REFRESH = "refresh";

	private boolean isAdmin = false;

	private boolean isLoggedOn = false;

	private boolean isPublic = true;

	private int userID = -1;

	private int campusID = -1;

	private int orderID = -1;

	private TextStyler styler;

	private Image image;

	private BuildingService buildingService = null;

	public TenantsHabitants() {
	}

	public void main(IWContext iwc) throws RemoteException {
		isAdmin = iwc.hasEditPermission(this);
		isLoggedOn = iwc.isLoggedOn();
		buildingService = getBuildingService(iwc);
		if (isLoggedOn) {
			isPublic = false;
		}
		if (iwc.isParameterSet(TenantsProfile.PARAMETER_USER_ID)) {
			add(new TenantsProfile());
		}
		else {
			add(getHabitantsTable(iwc));
		}
	}

	private Table getHabitantsTable(IWContext iwc) throws RemoteException {
		styler = new TextStyler();
		styler.setStyleValue(StyleConstants.ATTRIBUTE_FONT_FAMILY, StyleConstants.FONT_FAMILY_ARIAL);
		styler.setStyleValue(StyleConstants.ATTRIBUTE_FONT_SIZE, "8pt");
		if (iwc.isParameterSet(PARAMETER_ORDER_ID)) {
			orderID = Integer.parseInt(iwc.getParameter(PARAMETER_ORDER_ID));
		}
		else {
			orderID = HabitantsComparator.NAME;
		}
		if (iwc.isParameterSet(PARAMETER_CAMPUS_ID)) {
			campusID = Integer.parseInt(iwc.getParameter(PARAMETER_CAMPUS_ID));
		}
		if (isAdmin || isLoggedOn) {
			userID = iwc.getUserId();
		}
		else {
			isPublic = true;
		}
		Table myTable = new Table(1, 2);
		myTable.setWidth("100%");
		myTable.add(getLinkTable(), 1, 1);
		myTable.add(getTenantsTable(iwc), 1, 2);
		image = Table.getTransparentCell(iwc);
		image.setHeight(6);
		return myTable;
	}

	public Table getLinkTable() {
		Table table = new Table();
		try {
			Collection complexes = buildingService.getComplexHome().findAll();
			int column = 1;
			Complex complex = null;
			Link link = null;
			if (complexes != null) {
				table.add(formatText("|"), column, 1);
				column++;
				for (Iterator iter = complexes.iterator(); iter.hasNext();) {
					complex = (Complex) iter.next();
					link = new Link(formatText(complex.getName(), "#000000", true));
					link.addParameter(PARAMETER_CAMPUS_ID, complex.getPrimaryKey().toString());
					table.add(link, column, 1);
					column++;
					table.add(formatText("|"), column, 1);
					column++;
				}
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return table;
	}

	public List listOfComplexHabitants(int iComplexId, IWContext iwc) {
		Vector vector = new Vector();
		Contract contract = null;
		Applicant applicant = null;
		HabitantsCollector collector = null;
		CampusApplication campusApplication = null;
		if (!isAdmin && !isPublic) {
			try {
				Collection contracts = getContractService(iwc).getContractHome().findByUserAndRented(
						new Integer(userID), Boolean.TRUE);
				contract = (Contract) contracts.iterator().next();
				if (contract != null) {
					applicant = contract.getApplicant();
				}
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
		}
		Collection habitants = null;
		try {
			HabitantHome hHome = (HabitantHome) IDOLookup.getHome(Habitant.class);
			habitants = hHome.findByComplex(new Integer(campusID));
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		Habitant hab;
		
		if (habitants != null) {
			for (Iterator iter = habitants.iterator(); iter.hasNext();) {
				hab = (Habitant) iter.next();
				collector = new HabitantsCollector();
				collector.setUserID(hab.getUserId());
				collector.setApartment(hab.getApartment());
				collector.setEmail(hab.getEmail());
				collector.setName(hab.getFullName());
				collector.setFloor(hab.getFloor());
				collector.setAddress(hab.getAddress());
				collector.setPhone(hab.getPhoneNumber());
				vector.add(collector);
			}
		}
		return vector;
	}

	public PresentationObject getTenantsTable(IWContext iwc) throws RemoteException {
		DataTable table = new DataTable();
		table.setTitlesHorizontal(true);
		table.getContentTable().setCellpadding(3);
		table.getContentTable().setCellspacing(1);
		table.setWidth("100%");
		Complex complex = null;
		ComplexHome cxh = getBuildingService(iwc).getComplexHome();
		try {
			if (campusID != -1) {
				complex = cxh.findByPrimaryKey(new Integer(campusID));
			}
			else if (!isAdmin && !isPublic) {
				BuildingCacher bc = new BuildingCacher();
				Collection contracts = getContractService(iwc).getContractHome().findByUserAndRented(
						new Integer(userID), Boolean.TRUE);
				Contract C = (Contract) contracts.iterator().next();
				// Contract C = ContractFinder.findByUser(_userID);
				if (C != null && ((Integer) C.getPrimaryKey()).intValue() > 0) {
					ApartmentView view = getBuildingService(iwc).getApartmentViewHome().findByPrimaryKey(
							C.getApartmentId());
					complex = cxh.findByPrimaryKey(view.getComplexID());
				}
			}
			else {
				complex = (Complex) cxh.findAll().iterator().next();
			}
		}
		catch (EJBException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		try {
			if (campusID != -1)
				complex = getBuildingService(iwc).getComplexHome().findByPrimaryKey(new Integer(campusID));
			else if (!isAdmin && !isPublic) {
				Collection contracts = getContractService(iwc).getContractHome().findByUserAndRented(
						new Integer(userID), Boolean.TRUE);
				Contract C = (Contract) contracts.iterator().next();// ContractFinder.findByUser(_userID);
				if (C != null)
					complex = C.getApartment().getFloor().getBuilding().getComplex();
			}
			else {
				Collection complexes = getBuildingService(iwc).getComplexHome().findAll();
				complex = (Complex) complexes.iterator().next();
			}
		}
		catch (RemoteException e1) {
			e1.printStackTrace();
		}
		catch (FinderException e1) {
			e1.printStackTrace();
		}
		if (campusID == -1 && complex != null) {
			campusID = ((Integer) complex.getPrimaryKey()).intValue();
		}
		if (complex != null)
			table.addTitle(complex.getName());
		Link nameLink = new Link(formatText(localize("name", "Name")));
		nameLink.addParameter(PARAMETER_ORDER_ID, HabitantsComparator.NAME);
		nameLink.addParameter(PARAMETER_CAMPUS_ID, campusID);
		table.add(nameLink, 1, 1);
		Link apartmentLink = new Link(formatText(localize("address", "Address")));
		apartmentLink.addParameter(PARAMETER_ORDER_ID, HabitantsComparator.ADDRESS);
		apartmentLink.addParameter(PARAMETER_CAMPUS_ID, campusID);
		table.add(apartmentLink, 2, 1);
		Link addressLink = new Link(formatText(localize("apartment", "Apartment")));
		addressLink.addParameter(PARAMETER_ORDER_ID, HabitantsComparator.APARTMENT);
		addressLink.addParameter(PARAMETER_CAMPUS_ID, campusID);
		table.add(addressLink, 3, 1);
		Link floorLink = new Link(formatText(localize("floor", "Floor")));
		floorLink.addParameter(PARAMETER_ORDER_ID, HabitantsComparator.FLOOR);
		floorLink.addParameter(PARAMETER_CAMPUS_ID, campusID);
		table.add(floorLink, 4, 1);
		table.add(formatText(localize("phone", "Residence phone")), 5, 1);
		table.add(formatText(localize("email", "e-mail")), 6, 1);
		int row = 2;
		List vector = listOfComplexHabitants(campusID, iwc);
		HabitantsComparator comparator = new HabitantsComparator(iwc.getCurrentLocale(), orderID);
		Collections.sort(vector, comparator);
		Link adminLink = null;
		int column = 1;
		String emailPrepend = "<a href=\"mailto:";
		String emailAppend1 = "\">";
		String emailAppend2 = "</a>";
		StringBuffer email = null;
		for (int a = 0; a < vector.size(); a++) {
			column = 1;
			HabitantsCollector collected = (HabitantsCollector) vector.get(a);
			if (isAdmin) {
				adminLink = new Link(formatText(collected.getName()));
				adminLink.addParameter(TenantsProfile.getUserParameter(collected.getUserID()));
				table.add(adminLink, column++, row);
			}
			else {
				table.add(formatText(collected.getName()), column++, row);
			}
			table.add(formatText(collected.getAddress()), column++, row);
			table.add(formatText(collected.getApartment()), column++, row);
			table.add(formatText(collected.getFloor()), column++, row);
			table.add(formatText(collected.getPhone()), column++, row);
			if (isAdmin) {
				String emailString = collected.getEmail();
				if (emailString != null && !"".equals(emailString.trim())) {
					email = new StringBuffer(emailPrepend);
					email.append(emailString);
					email.append(emailAppend1);
					email.append(emailString);
					email.append(emailAppend2);
					table.add(formatText(email.toString()), column++, row);
				}
				else {
					table.add(formatText(collected.getEmail()), column++, row);
				}
			}
			else { 
				table.add(formatText(collected.getEmail()), column++, row);
			}
			row++;
		}

		return table;
	}

	private Text formatText(String text) {
		return formatText(text, "#000000", false);
	}

	private Text formatText(String text, String color) {
		return formatText(text, color, false);
	}

	private Text formatText(String text, String color, boolean bold) {
		if (text == null)
			text = "";
		Text T = new Text(text);
		styler.setStyleValue(StyleConstants.ATTRIBUTE_COLOR, color);
		if (bold)
			styler.setStyleValue(StyleConstants.ATTRIBUTE_FONT_WEIGHT, StyleConstants.FONT_WEIGHT_BOLD);
		else
			styler.setStyleValue(StyleConstants.ATTRIBUTE_FONT_WEIGHT, StyleConstants.FONT_WEIGHT_NORMAL);
		T.setFontStyle(styler.getStyleString());
		return T;
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	/**
	 * 
	 */
	public String getLocalizedNameKey() {
		return (NAME_KEY);
	}

	/**
	 * 
	 */
	public String getLocalizedNameValue() {
		return (DEFAULT_VALUE);
	}
	
	/*
	 * public void setRefreshRate(long refreshRate){ this.refreshRate =
	 * refreshRate; }
	 */
}