package com.idega.block.building.presentation;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;
import javax.faces.component.UIComponent;

import com.idega.block.building.business.BuildingService;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.ApartmentCategory;
import com.idega.block.building.data.ApartmentSubcategory;
import com.idega.block.building.data.ApartmentType;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.BuildingEntity;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.Floor;
import com.idega.block.media.presentation.ImageInserter;
import com.idega.block.text.presentation.TextChooser;
import com.idega.builder.presentation.IBPageChooser;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.builder.data.ICPage;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.Window;

/**
 * Title: Description: Copyright: Copyright (c) 2001 Company: idega multimedia
 * 
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class BuildingEditor extends com.idega.presentation.Block {

	private static final String PARAM_RENTABLE = "bm_rentable";

	private static final String PARAM_FLOOR = "bm_floor";

	private static final String PARAM_TYPE = "bm_type";

	private static final String PARAM_SERIE = "bm_serie";

	private static final String LABEL_DELETE = "del";

	private static final String LABEL_SAVE = "save";

	private static final String LABEL_PLAN = "plan";

	private static final String LABEL_PHOTO = "photo";

	private static final String LABEL_EXTRA_INFO = "extra_info";

	private static final String LABEL_INFO = "info";

	private static final String LABEL_LOCKED = "locked";

	private static final String LABEL_RENT = "rent";

	private static final String LABEL_BALCONY = "balcony";

	private static final String LABEL_FURNITURE = "furniture";

	private static final String LABEL_LOFT = "loft";

	private static final String LABEL_STUDY = "study";

	private static final String LABEL_STORAGE = "storage";

	private static final String LABEL_BATH = "bath";

	private static final String LABEL_KITCHEN = "kitchen";

	private static final String LABEL_AREA = "area";

	private static final String LABEL_ROOM_COUNT = "room_count";

	private static final String LABEL_CATEGORY = "category";

	private static final String LABEL_TEXT = "text";

	private static final String LABEL_ABBREVIATION = "abbreviation";

	private static final String LABEL_NAME = "name";

	private static final String LABEL_TYPE = "type";

	private static final String PARAM_CHOICE = "bm_choice";

	private static final String PARAM_SUBCATEGORY = "bm_subcategory";

	private static final String PARAM_CATEGORY = "bm_category";

	private static final String PARAM_FURNITURE = "bm_furni";

	private static final String PARAM_LOFT = "bm_loft";

	private static final String PARAM_STUDY = "bm_study";

	private static final String PARAM_BALCONY = "bm_balc";

	private static final String PARAM_STORAGE = "bm_stor";

	private static final String PARAM_BATH = "bm_bath";

	private static final String PARAM_KITCHEN = "bm_kitch";

	private static final String PARAM_RENT = "bm_rent";

	private static final String PARAM_AREA = "bm_area";

	private static final String PARAM_ROOMCOUNT = "bm_roomcount";

	private static final String PARAM_ID = "dr_id";
	
	private static final String LABEL_SUBCATEGORY = "subcategory";
	
	private static final String PARAM_SHOW_SPOUSE = "show_spouse";
	
	private static final String PARAM_SPOUSE_MANDATORY = "spouse_mandatory";
	
	private static final String PARAM_SHOW_CHILDREN = "show_children";
	
	private static final String PARAM_CHILDREN_MANDATORY = "children_mandatory";
	
	private static final String PARAM_MAX_CHOICES = "max_choices";
	
	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.building";

	public static final int ACTION_COMPLEX = 1;
	public static final int ACTION_BUILDING = 2;
	public static final int ACTION_FLOOR = 3;
	public static final int ACTION_APARTMENT = 4;
	public static final int ACTION_CATEGORY = 5;
	public static final int ACTION_TYPE = 6;
	public static final int ACTION_SUBCATEGORY = 7;

	private static final String PARAM_ABBREVATION = "abbrev";
	private static final String PARAM_INFO = "bm_info";
	private static final String PARAM_NAME = "bm_name";
	private static final String PARAM_APARTMENT_SERIAL_NUMBER = "ap_snr";
	private static final String PARAM_BUILDING_LOCKED = "bu_locked";
	private static final String PARAM_COMPLEX_LOCKED = "cp_locked";
	private static final String PARAM_TYPE_LOCKED = "tp_locked";
	private static final String PARAM_FLASH_PAGE = "flash_page";
	private static final String PARAM_ACTION = "be_action";
	private static final String PARAM_SAVE = LABEL_SAVE;
	private static final String PARAM_DELETE = LABEL_DELETE;

	protected boolean isAdmin = false;

	protected String TextFontColor = "#000000";

	private String styleAttribute = "font-family:arial; font-size:8pt; color:#000000; text-align: justify; border: 1 solid #000000";

	private String styleAttribute2 = "font-family:arial; font-size:8pt; color:#000000; text-align: justify;";

	private Integer eId = null;

	protected int fontSize = 1;

	protected boolean fontBold = false;

	private Table outerTable;

	private Integer textId = null;

	private BuildingService service = null;

	protected IWResourceBundle iwrb;

	protected IWBundle iwb;

	private boolean includeLinks = true;

	public void setToIncludeLinks(boolean include) {
		this.includeLinks = include;
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public String getLocalizedNameKey() {
		return "building_editor";
	}

	public String getLocalizedNameValue() {
		return "Buildings";
	}

	protected void control(IWContext iwc) throws RemoteException,
			FinderException {
		this.service = getBuildingService(iwc);
		this.outerTable = new Table(1, 2);
		this.outerTable.setCellpadding(0);
		this.outerTable.setCellspacing(0);
		this.outerTable.setWidth("100%");
		this.outerTable.setHeight("100%");
		this.outerTable.setHeight(2, "100%");

		int iAction = this.ACTION_BUILDING;
		if (iwc.getParameter(this.PARAM_ACTION) != null) {
			iAction = Integer.parseInt(iwc.getParameter(this.PARAM_ACTION));
		}

		if (iwc.getParameter(PARAM_ID) != null) {
			this.eId = Integer.valueOf(iwc.getParameter(PARAM_ID));
		} else if ((String) iwc.getSessionAttribute(PARAM_ID) != null) {
			this.eId = Integer.valueOf((String) iwc
					.getSessionAttribute(PARAM_ID));
			iwc.removeSessionAttribute(PARAM_ID);
		}

		if (iwc.getParameter(PARAM_SAVE) != null
				|| iwc.getParameter(PARAM_SAVE + ".x") != null) {
			if (iwc.getParameter(PARAM_CHOICE) != null) {
				int i = Integer.parseInt(iwc.getParameter(PARAM_CHOICE));
				if (iwc.isParameterSet("delete_text")) {
					this.textId = null;
				} else if (iwc.isParameterSet("txt_id")) {
					try {
						this.textId = Integer.valueOf(iwc
								.getParameter("txt_id"));
					} catch (Exception ex) {
						this.textId = null;
					}
				}

				switch (i) {
				case ACTION_COMPLEX:
					storeComplex(iwc);
					break;
				case ACTION_BUILDING:
					storeBuilding(iwc);
					break;
				case ACTION_FLOOR:
					storeFloor(iwc);
					break;
				case ACTION_APARTMENT:
					storeApartment(iwc);
					break;
				case ACTION_CATEGORY:
					storeApartmentCategory(iwc);
					break;
				case ACTION_TYPE:
					storeApartmentType(iwc);
					break;
				case ACTION_SUBCATEGORY:
					storeSubcategory(iwc);
				}
			}
		} else if (iwc.getParameter(PARAM_DELETE) != null
				|| iwc.getParameter(PARAM_DELETE + ".x") != null) {
			if (iwc.getParameter(PARAM_CHOICE) != null
					&& this.eId.intValue() > 0) {
				int i = Integer.parseInt(iwc.getParameter(PARAM_CHOICE));
				switch (i) {
				case ACTION_COMPLEX:
					this.service.removeComplex(this.eId);
					break;
				case ACTION_BUILDING:
					this.service.removeBuilding(this.eId);
					break;
				case ACTION_FLOOR:
					this.service.removeFloor(this.eId);
					break;
				case ACTION_APARTMENT:
					this.service.removeApartment(this.eId);
					break;
				case ACTION_CATEGORY:
					this.service.removeApartmentCategory(this.eId);
					break;
				case ACTION_TYPE:
					this.service.removeApartmentType(this.eId);
					break;
				case ACTION_SUBCATEGORY:
					this.service.removeSubcategory(this.eId);
					break;
				}
				this.eId = null;
			}
		}

		if (this.includeLinks) {
			this.outerTable.add(makeLinkTable(iAction), 1, 1);
		}

		switch (iAction) {
		case ACTION_COMPLEX:
			doComplex(iwc);
			break;
		case ACTION_BUILDING:
			doBuilding(iwc);
			break;
		case ACTION_FLOOR:
			doFloor(iwc);
			break;
		case ACTION_APARTMENT:
			doApartment(iwc);
			break;
		case ACTION_CATEGORY:
			doCategory(iwc);
			break;
		case ACTION_TYPE:
			doType(iwc);
			break;
		case ACTION_SUBCATEGORY:
			doSubcategory(iwc);
			break;
		}
		add(this.outerTable);
	}

	private void doMain(IWContext iwc, boolean ifMulti, int choice)
			throws RemoteException, FinderException {
		doBuilding(iwc);
	}

	private void doComplex(IWContext iwc) throws RemoteException,
			FinderException {
		Complex eComplex = (this.eId != null && this.eId.intValue() > 0) ? this.service
				.getComplexHome().findByPrimaryKey(this.eId)
				: null;
		this.outerTable.add(makeComplexFields(eComplex), 1, 2);
	}

	private void doBuilding(IWContext iwc) throws RemoteException,
			FinderException {
		Building eBuilding = (this.eId != null && this.eId.intValue() > 0) ? this.service
				.getBuildingHome().findByPrimaryKey(this.eId)
				: null;
		this.outerTable.add(makeBuildingFields(eBuilding), 1, 2);
	}

	private void doFloor(IWContext iwc) throws RemoteException, FinderException {
		Floor eFloor = (this.eId != null && this.eId.intValue() > 0) ? this.service
				.getFloorHome().findByPrimaryKey(this.eId)
				: null;
		this.outerTable.add(makeFloorFields(eFloor), 1, 2);
	}

	private void doApartment(IWContext iwc) throws RemoteException,
			FinderException {
		Apartment eApartment = (this.eId != null && this.eId.intValue() > 0) ? this.service
				.getApartmentHome().findByPrimaryKey(this.eId)
				: null;
		this.outerTable.add(makeApartmentFields(eApartment), 1, 2);
	}

	private void doType(IWContext iwc) {
		// Dirty job below
		int iPhotoId = 1, iPlanId = 1;
		boolean b1 = false, b2 = false;
		if (iwc.getSessionAttribute("tphotoid2") != null) {
			b1 = true;
			iPhotoId = Integer.parseInt((String) iwc
					.getSessionAttribute("tphotoid2"));
		}
		if (iwc.getSessionAttribute("tplanid2") != null) {
			b2 = true;
			iPlanId = Integer.parseInt((String) iwc
					.getSessionAttribute("tplanid2"));
		}
		if (b1 && b2) {
			iwc.removeSessionAttribute("tphotoid2");
			iwc.removeSessionAttribute("tplanid2");
		}
		try {

			ApartmentType eApartmentType = (this.eId != null && this.eId
					.intValue() > 0) ? this.service.getApartmentTypeHome()
					.findByPrimaryKey(this.eId) : null;

			this.outerTable.add(makeTypeFields(eApartmentType, iPhotoId,
					iPlanId), 1, 2);
		} catch (Exception sql) {
			sql.printStackTrace();
		}
		// add(getTypes());
	}

	private void doCategory(IWContext iwc) {
		try {
			ApartmentCategory eApartmentCategory = (this.eId != null && this.eId
					.intValue() > 0) ? this.service.getApartmentCategoryHome()
					.findByPrimaryKey(this.eId) : null;
			this.outerTable.add(makeCategoryFields(eApartmentCategory), 1, 2);
		} catch (Exception sql) {
		}
	}

	private void doSubcategory(IWContext iwc) {
		try {
			ApartmentSubcategory eApartmentSubcategory = (this.eId != null && this.eId
					.intValue() > 0) ? this.service
					.getApartmentSubcategoryHome().findByPrimaryKey(this.eId)
					: null;
			this.outerTable.add(makeSubcategoryFields(eApartmentSubcategory),
					1, 2);
		} catch (Exception sql) {
		}
	}

	private void doQuit(IWContext iwc) throws SQLException {
	}

	private void doSave(IWContext iwc) throws SQLException {
	}

	private void storeComplex(IWContext iwc) throws RemoteException {
		String sName = iwc.getParameter(PARAM_NAME).trim();
		String sInfo = iwc.getParameter(PARAM_INFO).trim();
		String sImageId = iwc.getParameter("mapid");
		String sId = iwc.getParameter(PARAM_ID);
		String sPageId = iwc.getParameter(PARAM_FLASH_PAGE);
		Boolean locked = Boolean
				.valueOf(iwc.getParameter(PARAM_COMPLEX_LOCKED));

		Integer imageid = null;
		Integer id = null;
		try {
			imageid = Integer.valueOf(sImageId);
		} catch (NumberFormatException ex) {
			imageid = null;
		}
		try {
			id = Integer.valueOf(sId);
		} catch (Exception ex) {
			id = null;
		}

		this.service.storeComplex(id, sName, sInfo, imageid, this.textId,
				sPageId, locked);

	}

	private void storeBuilding(IWContext iwc) throws RemoteException {
		String sName = iwc.getParameter(PARAM_NAME).trim();
		String sInfo = iwc.getParameter(PARAM_INFO).trim();
		String sAddress = iwc.getParameter("bm_address").trim();
		String sImageId = iwc.getParameter("photoid");
		String sComplexId = iwc.getParameter("dr_complex");
		String sId = iwc.getParameter(PARAM_ID);
		// String sSerie = iwc.getParameter("bm_serie");
		Boolean locked = Boolean.valueOf(iwc
				.getParameter(PARAM_BUILDING_LOCKED));
		Integer imageid = null;
		Integer id = null;
		Integer complexid = null;
		try {
			id = Integer.valueOf(sId);
		} catch (NumberFormatException ex) {
			id = null;
		}
		try {
			complexid = Integer.valueOf(sComplexId);
		} catch (NumberFormatException ex) {
			complexid = null;
		}
		try {
			imageid = Integer.valueOf(sImageId);
		} catch (NumberFormatException ex) {
			imageid = null;
		}

		this.service.storeBuilding(id, sName, sAddress, sInfo, imageid,
				complexid, this.textId, locked);
	}

	private void storeFloor(IWContext iwc) throws RemoteException {

		String sName = iwc.getParameter(PARAM_NAME).trim();
		String sInfo = iwc.getParameter(PARAM_INFO).trim();
		String sImageId = iwc.getParameter("photoid");
		String sBuildingId = iwc.getParameter("dr_building");
		String sId = iwc.getParameter(PARAM_ID);
		Integer imageid = null;
		Integer id = null;
		Integer buildingid = null;
		try {
			id = Integer.valueOf(sId);
		} catch (NumberFormatException ex) {
			id = null;
		}
		try {
			buildingid = Integer.valueOf(sBuildingId);
		} catch (NumberFormatException ex) {
			buildingid = null;
		}
		try {
			imageid = Integer.valueOf(sImageId);
		} catch (NumberFormatException ex) {
			imageid = null;
		}

		this.service.storeFloor(id, sName, buildingid, sInfo, imageid,
				this.textId);
	}

	private void storeApartmentCategory(IWContext iwc) throws RemoteException {
		String sName = iwc.getParameter(PARAM_NAME).trim();
		String sInfo = iwc.getParameter(PARAM_INFO).trim();
		String sImageId = iwc.getParameter("iconid");
		String sId = iwc.getParameter(PARAM_ID);
		
		Boolean showSpouse = Boolean.valueOf(iwc.isParameterSet(PARAM_SHOW_SPOUSE));
		Boolean spouseMandatory = Boolean.valueOf(iwc.isParameterSet(PARAM_SPOUSE_MANDATORY));
		Boolean showChildren = Boolean.valueOf(iwc.isParameterSet(PARAM_SHOW_CHILDREN));
		Boolean childrenMandatory = Boolean.valueOf(iwc.isParameterSet(PARAM_CHILDREN_MANDATORY));
		String numberOfChoices = iwc.getParameter(PARAM_MAX_CHOICES);
		
		Integer imageid = null;
		Integer id = null;
		Integer maxNumberOfChoices = null;
		try {
			imageid = Integer.valueOf(sImageId);
		} catch (NumberFormatException ex) {
			imageid = null;
		}
		try {
			id = Integer.valueOf(sId);
		} catch (NumberFormatException ex) {
			id = null;
		}

		try {
			maxNumberOfChoices = Integer.valueOf(numberOfChoices);
		} catch (NumberFormatException e) {
			maxNumberOfChoices = null;
		}
		
		this.service.storeApartmentCategory(id, sName, sInfo, imageid,
				this.textId, showSpouse.booleanValue(), spouseMandatory.booleanValue(), showChildren.booleanValue(), childrenMandatory.booleanValue(), maxNumberOfChoices.intValue());

	}

	private void storeSubcategory(IWContext iwc) throws RemoteException {
		String sName = iwc.getParameter(PARAM_NAME).trim();
		String sInfo = iwc.getParameter(PARAM_INFO).trim();
		String sImageId = iwc.getParameter("iconid");
		String sId = iwc.getParameter(PARAM_ID);
		String categoryID = iwc.getParameter(PARAM_CATEGORY);
		Integer imageid = null;
		Integer id = null;
		Integer catID = null;
		try {
			imageid = Integer.valueOf(sImageId);
		} catch (NumberFormatException ex) {
			imageid = null;
		}
		try {
			id = Integer.valueOf(sId);
		} catch (NumberFormatException ex) {
			id = null;
		}
		try {
			catID = Integer.valueOf(categoryID);
		} catch (NumberFormatException ex) {
			catID = null;
		}

		this.service.storeSubcategory(catID, id, sName, sInfo, imageid,
				this.textId);

	}

	private void storeApartmentType(IWContext iwc) throws RemoteException {

		String sName = iwc.getParameter(PARAM_NAME).trim();
		String sInfo = iwc.getParameter(PARAM_INFO).trim();
		String abbrev = iwc.getParameter(PARAM_ABBREVATION).trim();
		String sExtraInfo = iwc.getParameter(LABEL_EXTRA_INFO).trim();
		String sId = iwc.getParameter(PARAM_ID);
		String sRoomCount = iwc.getParameter(PARAM_ROOMCOUNT);
		// String sCategoryId = iwc.getParameter("bm_category");
		String subcategoryID = iwc.getParameter(PARAM_SUBCATEGORY);
		String sImageId = iwc.getParameter("tphotoid");
		String sPlanId = iwc.getParameter("tplanid");
		String sArea = iwc.getParameter(PARAM_AREA).trim();
		Boolean kitchen = Boolean.valueOf(iwc.isParameterSet(PARAM_KITCHEN));
		Boolean bath = Boolean.valueOf(iwc.isParameterSet(PARAM_BATH));
		Boolean storage = Boolean.valueOf(iwc.isParameterSet(PARAM_STORAGE));
		Boolean balcony = Boolean.valueOf(iwc.isParameterSet(PARAM_BALCONY));
		Boolean study = Boolean.valueOf(iwc.isParameterSet(PARAM_STUDY));
		Boolean loft = Boolean.valueOf(iwc.isParameterSet(PARAM_LOFT));
		Boolean furniture = Boolean
				.valueOf(iwc.isParameterSet(PARAM_FURNITURE));

		String sRent = iwc.getParameter(PARAM_RENT);
		Boolean locked = Boolean.valueOf(iwc.getParameter(PARAM_TYPE_LOCKED));

		Integer planid = null;
		Integer imageid = null;
		Integer id = null;
		Integer subcategory = null;
		Integer rent = null;
		Double area = null;
		Integer count = null;
		try {
			id = Integer.valueOf(sId);
		} catch (NumberFormatException ex) {
			id = null;
		}
		try {
			subcategory = Integer.valueOf(subcategoryID);
		} catch (NumberFormatException ex) {
			subcategory = null;
		}
		try {
			imageid = Integer.valueOf(sImageId);
		} catch (NumberFormatException ex) {
			imageid = null;
		}
		try {
			planid = Integer.valueOf(sPlanId);
		} catch (NumberFormatException ex) {
			planid = null;
		}
		try {
			rent = Integer.valueOf(sRent);
		} catch (NumberFormatException ex) {
			rent = null;
		}
		try {
			sArea = sArea.replace(',', '.');
			area = Double.valueOf(sArea);
		} catch (NumberFormatException ex) {
			area = null;
		}
		try {
			count = Integer.valueOf(sRoomCount);
		} catch (NumberFormatException ex) {
			count = null;
		}

		this.service
				.storeApartmentType(id, sName, sInfo, abbrev, sExtraInfo,
						planid, imageid, subcategory, this.textId, area, count,
						rent, balcony, bath, kitchen, storage, study,
						furniture, loft, locked);

	}

	private void storeApartment(IWContext iwc) throws RemoteException {

		String sName = iwc.getParameter(PARAM_NAME).trim();
		String sInfo = iwc.getParameter(PARAM_INFO).trim();
		String sId = iwc.getParameter(PARAM_ID);
		String sType = iwc.getParameter(PARAM_TYPE);
		String sFloorId = iwc.getParameter(PARAM_FLOOR);
		String sRentable = iwc.getParameter(PARAM_RENTABLE);
		String sImageId = iwc.getParameter("photoid");
		String apSnr = iwc.getParameter(PARAM_APARTMENT_SERIAL_NUMBER);
		Boolean bRentable = sRentable != null ? Boolean.TRUE : Boolean.FALSE;

		Integer id = null;
		Integer floorid = null;
		Integer imageid = null;
		Integer typeid = null;
		try {
			id = Integer.valueOf(sId);
		} catch (NumberFormatException ex) {
			id = null;
		}
		try {
			floorid = Integer.valueOf(sFloorId);
		} catch (NumberFormatException ex) {
			floorid = null;
		}
		try {
			imageid = Integer.valueOf(sImageId);
		} catch (NumberFormatException ex) {
			imageid = null;
		}
		try {
			typeid = Integer.valueOf(sType);
		} catch (NumberFormatException ex) {
			typeid = null;
		}

		this.service.storeApartment(id, sName, sInfo, floorid, typeid,
				bRentable, imageid, this.textId, apSnr);
	}

	public PresentationObject getLinkTable(IWContext iwc) {
		int iAct = this.ACTION_BUILDING;
		IWResourceBundle iwrb = getResourceBundle(iwc);
		if (iwc.getParameter(this.PARAM_ACTION) != null) {
			iAct = Integer.parseInt(iwc.getParameter(this.PARAM_ACTION));
		}

		Table LinkTable = new Table();
		LinkTable.setBorder(0);

		Link B1 = new Link(iwrb.getLocalizedString("complex", "Complex"));
		B1.setFontStyle("text-decoration: none");
		B1.setFontColor("#FFFFFF");
		B1.setBold();
		B1.addParameter(this.PARAM_ACTION, this.ACTION_COMPLEX);
		Link B2 = new Link(iwrb.getLocalizedString("building", "Building"));
		B2.setFontStyle("text-decoration: none");
		B2.setFontColor("#FFFFFF");
		B2.setBold();
		B2.addParameter(this.PARAM_ACTION, this.ACTION_BUILDING);
		Link B3 = new Link(iwrb.getLocalizedString("floor", "Floor"));
		B3.setFontStyle("text-decoration: none");
		B3.setFontColor("#FFFFFF");
		B3.setBold();
		B3.addParameter(this.PARAM_ACTION, this.ACTION_FLOOR);
		Link B4 = new Link(iwrb.getLocalizedString(LABEL_CATEGORY, "Category"));
		B4.setFontStyle("text-decoration: none");
		B4.setFontColor("#FFFFFF");
		B4.setBold();
		B4.addParameter(this.PARAM_ACTION, this.ACTION_CATEGORY);
		Link B5 = new Link(iwrb.getLocalizedString(LABEL_TYPE, "Type"));
		B5.setFontStyle("text-decoration: none");
		B5.setFontColor("#FFFFFF");
		B5.setBold();
		B5.addParameter(this.PARAM_ACTION, this.ACTION_TYPE);
		Link B6 = new Link(iwrb.getLocalizedString("apartment", "Apartment"));
		B6.setFontStyle("text-decoration: none");
		B6.setFontColor("#FFFFFF");
		B6.setBold();
		B6.addParameter(this.PARAM_ACTION, this.ACTION_APARTMENT);
		
		Link subcategory = new Link(iwrb.getLocalizedString(LABEL_SUBCATEGORY, "Subcategory"));
		subcategory.setFontStyle("text-decoration: none");
		subcategory.setFontColor("#FFFFFF");
		subcategory.setBold();
		subcategory.addParameter(this.PARAM_ACTION, this.ACTION_SUBCATEGORY);

		switch (iAct) {
		case ACTION_COMPLEX: {
			B1.setFontColor("#FF9933");
			break;
		}
		case ACTION_BUILDING: {
			B2.setFontColor("#FF9933");
			break;
		}
		case ACTION_FLOOR: {
			B3.setFontColor("#FF9933");
			break;
		}
		case ACTION_APARTMENT: {
			B6.setFontColor("#FF9933");
			break;
		}
		case ACTION_CATEGORY: {
			B4.setFontColor("#FF9933");
			break;
		}
		case ACTION_TYPE: {
			B5.setFontColor("#FF9933");
			break;
		}
		case ACTION_SUBCATEGORY: {
			subcategory.setFontColor("#FF9933");
			break;
		}
		}

		LinkTable.add(B1, 1, 1);
		LinkTable.add(B2, 2, 1);
		LinkTable.add(B3, 3, 1);
		LinkTable.add(B4, 4, 1);
		LinkTable.add(subcategory, 5, 1);
		LinkTable.add(B5, 6, 1);
		LinkTable.add(B6, 7, 1);
		return LinkTable;
	}

	protected PresentationObject makeLinkTable(int i) {
		Table headerTable = new Table(1, 2);
		headerTable.setCellpadding(0);
		headerTable.setCellspacing(0);
		headerTable.setWidth("100%");
		headerTable.setAlignment(1, 2, "center");

		String color = this.includeLinks ? "#000000" : "#FFFFFF";
		Table LinkTable = new Table();
		LinkTable.setBorder(0);
		LinkTable.setCellpadding(3);
		LinkTable.setCellspacing(3);
		headerTable.add(LinkTable, 1, 2);

		Link B1 = new Link(this.iwrb.getLocalizedString("complex", "Complex"));
		B1.setFontStyle("text-decoration: none");
		B1.setFontColor(color);
		B1.setBold();
		B1.addParameter(this.PARAM_ACTION, this.ACTION_COMPLEX);
		Link B2 = new Link(this.iwrb.getLocalizedString("building", "Building"));
		B2.setFontStyle("text-decoration: none");
		B2.setFontColor(color);
		B2.setBold();
		B2.addParameter(this.PARAM_ACTION, this.ACTION_BUILDING);
		Link B3 = new Link(this.iwrb.getLocalizedString("floor", "Floor"));
		B3.setFontStyle("text-decoration: none");
		B3.setFontColor(color);
		B3.setBold();
		B3.addParameter(this.PARAM_ACTION, this.ACTION_FLOOR);
		Link B4 = new Link(this.iwrb.getLocalizedString(LABEL_CATEGORY,
				"Category"));
		B4.setFontStyle("text-decoration: none");
		B4.setFontColor(color);
		B4.setBold();
		B4.addParameter(this.PARAM_ACTION, this.ACTION_CATEGORY);
		Link B5 = new Link(this.iwrb.getLocalizedString(LABEL_TYPE, "Type"));
		B5.setFontStyle("text-decoration: none");
		B5.setFontColor(color);
		B5.setBold();
		B5.addParameter(this.PARAM_ACTION, this.ACTION_TYPE);
		Link B6 = new Link(this.iwrb.getLocalizedString("apartment",
				"Apartment"));
		B6.setFontStyle("text-decoration: none");
		B6.setFontColor(color);
		B6.setBold();
		B6.addParameter(this.PARAM_ACTION, this.ACTION_APARTMENT);

		Link subcategory = new Link(iwrb.getLocalizedString(LABEL_SUBCATEGORY, "Subcategory"));
		subcategory.setFontStyle("text-decoration: none");
		subcategory.setFontColor(color);
		subcategory.setBold();
		subcategory.addParameter(this.PARAM_ACTION, this.ACTION_SUBCATEGORY);
		
		switch (i) {
		case ACTION_COMPLEX:
			B1.setFontColor("#FF9933");
			break;
		case ACTION_BUILDING:
			B2.setFontColor("#FF9933");
			break;
		case ACTION_FLOOR:
			B3.setFontColor("#FF9933");
			break;
		case ACTION_APARTMENT:
			B6.setFontColor("#FF9933");
			break;
		case ACTION_CATEGORY:
			B4.setFontColor("#FF9933");
			break;
		case ACTION_TYPE:
			B5.setFontColor("#FF9933");
			break;
		case ACTION_SUBCATEGORY : {
			subcategory.setFontColor("#FF9933");
			break;
		}
		}

		LinkTable.add(B1, 1, 1);
		LinkTable.add(B2, 2, 1);
		LinkTable.add(B3, 3, 1);
		LinkTable.add(B4, 4, 1);
		LinkTable.add(subcategory, 5, 1);
		LinkTable.add(B5, 6, 1);
		LinkTable.add(B6, 7, 1);
		return headerTable;
	}

	private PresentationObject makeTextArea(String sInit) {
		TextArea TA = new TextArea(PARAM_INFO);
		TA.setContent(sInit);
		TA.setWidth(90);
		TA.setHeight(12);
		setStyle(TA);
		return TA;
	}

	private PresentationObject makeTextArea(String name, String sInit) {
		TextArea TA = new TextArea(name);
		TA.setContent(sInit);
		TA.setWidth(90);
		TA.setHeight(12);
		setStyle(TA);
		return TA;
	}

	private PresentationObject makeImageInput(int id, String name) {
		PresentationObject imageObject = null;
		ImageInserter imageInsert = null;
		if (id > 1) {
			imageInsert = new ImageInserter(id, name);
		} else {
			imageInsert = new ImageInserter(name);
		}
		imageInsert.setHasUseBox(false);
		imageInsert.setMaxImageWidth(140);
		imageInsert.setHiddenInputName(name);
		imageObject = imageInsert;
		return imageObject;
	}

	private PresentationObject makeTextInput(int id) {
		Table T = new Table();

		TextChooser ans = new TextChooser("txt_id");
		T.add(ans, 1, 1);
		if (id < 0) {
			ans
					.setChooseImage(this.iwb.getImage("new.gif", this.iwrb
							.getLocalizedString("button_create_answer",
									"Create text")));
		} else {
			ans.setSelectedText(id);
			ans.setChooseImage(this.iwb.getImage("open.gif", this.iwrb
					.getLocalizedString("button_edit_answer", "Edit text")));
			CheckBox delete = new CheckBox("txt_del", String.valueOf(id));
			T.add(formatText(this.iwrb.getLocalizedString("delete_text",
					"Delete text:")), 3, 1);
			T.add(delete, 3, 1);
		}

		return T;
	}

	private UIComponent makeComplexFields(Complex eComplex)
			throws RemoteException, FinderException {
		boolean e = eComplex != null ? true : false;
		String sId = e ? eComplex.getPrimaryKey().toString() : "";
		String sName = e ? eComplex.getName() : "";
		String sInfo = e ? eComplex.getInfo() : "";
		int iMapId = e ? eComplex.getImageId() : 1;
		int iTextId = e ? eComplex.getTextId() : -1;
		int iFlashPage = e ? eComplex.getFlashPageID() : -1;
		boolean locked = e ? eComplex.getLocked() : false;

		Form form = new Form();
		Table Frame = new Table(2, 1);
		Frame.setRowVerticalAlignment(1, "top");
		Frame.setCellpadding(0);
		Frame.setCellspacing(0);
		Frame.setColor(2, 1, "#EFEFEF");
		Frame.setWidth("100%");
		Frame.setWidth(2, 1, "160");
		Frame.setHeight("100%");
		Table T = new Table(2, 9);
		T.setCellpadding(2);
		T.setWidth("100%");
		Table T2 = new Table(1, 2);
		T2.setCellpadding(8);
		T2.setHeight("100%");
		T2.setWidth("100%");
		T2.setVerticalAlignment(1, 1, "top");
		T2.setVerticalAlignment(1, 2, "bottom");
		T2.setAlignment(1, 2, "center");
		Frame.setAlignment(1, 1, "center");
		Frame.add(T, 1, 1);
		Frame.setAlignment(2, 1, "center");
		Frame.add(T2, 2, 1);

		TextInput name = new TextInput(PARAM_NAME, sName);
		DropdownMenu categories = drpLodgings(this.service.getComplexHome()
				.findAllIncludingLocked(), PARAM_ID, "Complex", sId);
		HiddenInput HI = new HiddenInput(PARAM_CHOICE, String
				.valueOf(this.ACTION_COMPLEX));
		HiddenInput HA = new HiddenInput(this.PARAM_ACTION, String
				.valueOf(this.ACTION_COMPLEX));
		setStyle(name);
		setStyle(categories);
		categories.setToSubmit();
		name.setLength(30);
		IBPageChooser pageChooser = new IBPageChooser(PARAM_FLASH_PAGE);
		setStyle(pageChooser, this.styleAttribute);
		if (iFlashPage > 0) {
			ICPage flashPage = this.service.getPage(iFlashPage);
			if (flashPage != null) {
				pageChooser.setSelectedPage(iFlashPage, flashPage.getName());
			}
		}
		CheckBox complexLocked = new CheckBox(PARAM_COMPLEX_LOCKED, "true");
		complexLocked.setChecked(locked);

		T.add(HI);
		T.add(HA);
		T.add(categories, 1, 1);
		T.add(formatText(this.iwrb.getLocalizedString(LABEL_NAME, "Name")), 1,
				2);
		T.add(name, 1, 3);
		T.add(formatText(this.iwrb.getLocalizedString(LABEL_TEXT, "Text")), 2,
				2);
		T.add(makeTextInput(iTextId), 2, 3);
		T.add(formatText(this.iwrb.getLocalizedString("flash", "Flash page")),
				1, 4);
		T.add(pageChooser, 1, 5);
		T.add(formatText(this.iwrb.getLocalizedString("complex_locked",
				"Complex locked")), 1, 6);
		T.add(complexLocked, 1, 7);
		T.add(formatText(this.iwrb.getLocalizedString(LABEL_INFO, "Info")), 1,
				8);
		T.mergeCells(1, 9, 2, 9);
		T.add(makeTextArea(sInfo), 1, 9);

		T2.add(formatText(this.iwrb.getLocalizedString("map", "Map")), 1, 1);
		T2.add(Text.getBreak(), 1, 1);
		T2.add(this.makeImageInput(iMapId, "mapid"), 1, 1);
		T2.add(new SubmitButton(this.iwrb.getImage("save.gif"), LABEL_SAVE), 1,
				2);
		T2
				.add(new SubmitButton(this.iwrb.getImage("delete.gif"),
						PARAM_DELETE), 1, 2);
		form.add(Frame);
		return form;
	}

	private UIComponent makeBuildingFields(Building eBuilding)
			throws RemoteException, FinderException {
		boolean e = eBuilding != null ? true : false;
		String sName = e ? eBuilding.getName() : "";
		String sInfo = e ? eBuilding.getInfo() : "";
		String sAddress = e ? eBuilding.getStreet() : "";
		String sId = e ? String.valueOf(eBuilding.getID()) : "";
		String sComplexId = e ? String.valueOf(eBuilding.getComplexId()) : "";
		String sSerie = e ? eBuilding.getSerie() : "";
		int iPhotoId = e ? eBuilding.getImageId() : 1;
		int iTextId = e ? eBuilding.getTextId() : -1;
		boolean locked = e ? eBuilding.getLocked() : false;

		Form form = new Form();
		Table Frame = new Table(2, 1);
		Frame.setRowVerticalAlignment(1, "top");
		Frame.setCellpadding(0);
		Frame.setCellspacing(0);
		Frame.setColor(2, 1, "#EFEFEF");
		Frame.setWidth("100%");
		Frame.setWidth(1, 1, "100%");
		Frame.setHeight("100%");
		Table T = new Table();
		T.setCellpadding(2);
		T.setWidth("100%");
		Table T2 = new Table(1, 2);
		T2.setCellpadding(8);
		T2.setHeight("100%");
		T2.setHeight(2, "100%");
		T2.setWidth("100%");
		T2.setVerticalAlignment(1, 1, "top");
		T2.setVerticalAlignment(1, 2, "bottom");
		T2.setAlignment(1, 2, "center");
		Frame.setAlignment(2, 1, "center");
		Frame.add(T2, 2, 1);
		Frame.setAlignment(1, 1, "center");
		Frame.add(T, 1, 1);
		TextInput name = new TextInput(PARAM_NAME, sName);
		TextInput address = new TextInput("bm_address", sAddress);
		TextInput serie = new TextInput(PARAM_SERIE, sSerie);
		HiddenInput HI = new HiddenInput(PARAM_CHOICE, String
				.valueOf(this.ACTION_BUILDING));

		DropdownMenu complex = drpLodgings(this.service.getComplexHome()
				.findAllIncludingLocked(), "dr_complex", "Complex", sComplexId);
		DropdownMenu houses = drpLodgings(this.service.getBuildingHome()
				.findAllIncludingLocked(), PARAM_ID, "Building", sId);
		CheckBox buildingLocked = new CheckBox(PARAM_BUILDING_LOCKED, "true");
		buildingLocked.setChecked(locked);

		houses.setToSubmit();
		setStyle(houses);
		setStyle(complex);
		setStyle(name);
		setStyle(address);
		setStyle(serie);
		name.setLength(30);
		address.setLength(30);
		serie.setLength(5);
		serie.setMaxlength(5);

		T.add(houses, 1, 1);
		T.add(formatText(this.iwrb.getLocalizedString(LABEL_NAME, "Name")), 1,
				2);
		T.add(name, 1, 3);
		T.add(formatText(this.iwrb.getLocalizedString(LABEL_TEXT, "Text")), 2,
				2);
		T.add(makeTextInput(iTextId), 2, 3);
		T.add(formatText(this.iwrb.getLocalizedString("address", "Address")),
				1, 4);

		T.add(address, 1, 5);
		T.add(formatText(this.iwrb.getLocalizedString("complex", "Complex")),
				1, 6);

		T.add(complex, 1, 7);
		// T.add(formatText(iwrb.getLocalizedString("serie","Serie")+" "),1,5);
		// T.add(serie,1,5);
		T.add(formatText(this.iwrb.getLocalizedString("building_locked",
				"Building locked")), 1, 8);
		T.add(buildingLocked, 1, 9);

		T.add(formatText(this.iwrb.getLocalizedString(LABEL_INFO, "Info")), 1,
				10);

		T.add(makeTextArea(sInfo), 1, 11);
		T.mergeCells(1, 11, 2, 11);

		T2.add(formatText(this.iwrb.getLocalizedString(LABEL_PHOTO, "Photo")),
				1, 1);
		T2.add(Text.getBreak(), 1, 1);
		T2.add(this.makeImageInput(iPhotoId, "photoid"), 1, 1);
		Frame.add(HI);
		T2.add(new SubmitButton(this.iwrb.getImage("save.gif"), PARAM_SAVE), 1,
				2);
		T2
				.add(new SubmitButton(this.iwrb.getImage("delete.gif"),
						PARAM_DELETE), 1, 2);
		form.add(Frame);
		return form;
	}

	private Form makeFloorFields(Floor eFloor) throws RemoteException,
			FinderException {
		boolean e = eFloor != null ? true : false;
		String sName = e ? eFloor.getName() : "";
		String sInfo = e ? eFloor.getInfo() : "";
		String sHouse = e ? String.valueOf(eFloor.getBuildingId()) : "";
		String sId = e ? String.valueOf(eFloor.getID()) : "";
		int iTextId = e ? eFloor.getTextId() : -1;
		Form form = new Form();
		Table Frame = new Table(2, 1);
		Frame.setRowVerticalAlignment(1, "top");
		Frame.setCellpadding(0);
		Frame.setCellspacing(0);
		Frame.setColor(2, 1, "#EFEFEF");
		Frame.setWidth("100%");
		Frame.setWidth(1, 1, "100%");
		Frame.setHeight("100%");
		Table T = new Table();
		T.setCellpadding(2);
		T.setWidth("100%");
		Table T2 = new Table(1, 2);
		T2.setCellpadding(8);
		T2.setHeight("100%");
		T2.setHeight(2, "100%");
		T2.setWidth("100%");
		T2.setVerticalAlignment(1, 1, "top");
		T2.setVerticalAlignment(1, 2, "bottom");
		T2.setAlignment(1, 2, "center");
		Frame.setAlignment(2, 1, "center");
		Frame.add(T2, 2, 1);
		Frame.setAlignment(1, 1, "center");
		Frame.add(T, 1, 1);
		TextInput name = new TextInput(PARAM_NAME, sName);
		DropdownMenu floors = this.drpFloors(PARAM_ID, "Floor", sId, true);
		floors.setToSubmit();

		DropdownMenu buildings = this.drpLodgings(this.service
				.getBuildingHome().findAllIncludingLocked(), "dr_building",
				"Building", sHouse);
		HiddenInput HI = new HiddenInput(PARAM_CHOICE, String
				.valueOf(this.ACTION_FLOOR));
		HiddenInput HA = new HiddenInput(this.PARAM_ACTION, String
				.valueOf(this.ACTION_FLOOR));
		setStyle(name);
		setStyle(floors);
		setStyle(buildings);
		name.setLength(30);

		T.add(floors, 1, 1);

		T.add(formatText(this.iwrb.getLocalizedString(LABEL_NAME, "Name")), 1,
				2);

		T.add(name, 1, 3);
		T.add(formatText(this.iwrb.getLocalizedString(LABEL_TEXT, "Text")), 2,
				2);

		T.add(makeTextInput(iTextId), 2, 3);
		T.add(formatText(this.iwrb.getLocalizedString("building", "Building")),
				1, 4);

		T.add(buildings, 1, 5);
		T.add(formatText(this.iwrb.getLocalizedString(LABEL_INFO, "Info")), 1,
				6);

		T.add(makeTextArea(sInfo), 1, 7);
		T.mergeCells(1, 7, 2, 7);
		T2.add(formatText(this.iwrb.getLocalizedString(LABEL_PHOTO, "Photo")),
				1, 1);
		T2.add(Text.getBreak(), 1, 1);
		T2.add(this.makeImageInput(1, "photoid"), 1, 1);
		Frame.add(HI);
		Frame.add(HA);
		T2.add(new SubmitButton(this.iwrb.getImage("save.gif"), PARAM_SAVE), 1,
				2);
		T2
				.add(new SubmitButton(this.iwrb.getImage("delete.gif"),
						PARAM_DELETE), 1, 2);
		form.add(Frame);
		return form;
	}

	private Form makeCategoryFields(ApartmentCategory eApartmentCategory)
			throws RemoteException, FinderException {
		boolean e = eApartmentCategory != null ? true : false;
		String sName = e ? eApartmentCategory.getName() : "";
		String sInfo = e ? eApartmentCategory.getInfo() : "";
		String sId = e ? String.valueOf(eApartmentCategory.getID()) : "";
		int iIconId = e ? eApartmentCategory.getImageId() : 1;
		int iTextId = e ? eApartmentCategory.getTextId() : -1;
		boolean showSpouse = e ? eApartmentCategory.getShowSpouse() : true;
		boolean spouseMandatory = e ? eApartmentCategory.getSpouseMandatory() : false;
		boolean showChildren = e ? eApartmentCategory.getShowChildren() : true;
		boolean childrenMandatory = e ? eApartmentCategory.getChildrenMandatory() : false;
		int maxNumberOfChoices = e ? eApartmentCategory.getMaxNumberOfChoices() : 3;
		
		Form form = new Form();
		Table Frame = new Table(2, 1);
		Frame.setRowVerticalAlignment(1, "top");
		Frame.setCellpadding(0);
		Frame.setCellspacing(0);
		Frame.setColor(2, 1, "#EFEFEF");
		Frame.setWidth("100%");
		Frame.setWidth(2, 1, "160");
		Frame.setHeight("100%");
		Table T = new Table();
		T.setCellpadding(2);
		T.setWidth("100%");
		Table T2 = new Table(1, 2);
		T2.setCellpadding(8);
		T2.setHeight("100%");
		T2.setWidth("100%");
		T2.setVerticalAlignment(1, 1, "top");
		T2.setVerticalAlignment(1, 2, "bottom");
		T2.setAlignment(1, 2, "center");
		Frame.setAlignment(1, 1, "center");
		Frame.add(T, 1, 1);
		Frame.setAlignment(2, 1, "center");
		Frame.add(T2, 2, 1);

		TextInput name = new TextInput(PARAM_NAME, sName);
		TextInput numberOfChoices = new TextInput(PARAM_MAX_CHOICES);
		numberOfChoices.setAsIntegers(iwrb.getLocalizedString("must_enter_integer", "Please enter an integer"));
		numberOfChoices.setValue(maxNumberOfChoices);

		DropdownMenu categories = drpLodgings(this.service
				.getApartmentCategoryHome().findAll(), PARAM_ID, "Category",
				sId);

		categories.setToSubmit();
		
		CheckBox showSpouseCheckBox = new CheckBox(PARAM_SHOW_SPOUSE);
		showSpouseCheckBox.setChecked(showSpouse);
		CheckBox spouseMandatoryCheckBox = new CheckBox(PARAM_SPOUSE_MANDATORY);
		spouseMandatoryCheckBox.setChecked(spouseMandatory);
		CheckBox showChildrenCheckBox = new CheckBox(PARAM_SHOW_CHILDREN);
		showChildrenCheckBox.setChecked(showChildren);
		CheckBox childrenMandatoryCheckBox = new CheckBox(PARAM_CHILDREN_MANDATORY);
		childrenMandatoryCheckBox.setChecked(childrenMandatory);
		
		HiddenInput HI = new HiddenInput(PARAM_CHOICE, String
				.valueOf(this.ACTION_CATEGORY));
		HiddenInput HA = new HiddenInput(this.PARAM_ACTION, String
				.valueOf(this.ACTION_CATEGORY));
		setStyle(name);
		setStyle(categories);
		name.setLength(30);
		T.add(HI);
		T.add(HA);
		T.add(categories, 1, 1);
		T.add(formatText(this.iwrb.getLocalizedString(LABEL_NAME, "Name")), 1,
				2);
		T.add(formatText(this.iwrb.getLocalizedString(LABEL_TEXT, "Text")), 2,
				2);
		T.add(name, 1, 3);
		T.add(makeTextInput(iTextId), 2, 3);
		
		T.add(formatText(this.iwrb.getLocalizedString(PARAM_SHOW_SPOUSE, "Show spouse")), 1,
				4);
		T.add(showSpouseCheckBox, 1, 5);
		T.add(formatText(this.iwrb.getLocalizedString(PARAM_SPOUSE_MANDATORY, "Spouse mandatory")), 1,
				6);
		T.add(spouseMandatoryCheckBox, 1, 7);
		T.add(formatText(this.iwrb.getLocalizedString(PARAM_SHOW_CHILDREN, "Show children")), 1,
				8);
		T.add(showChildrenCheckBox, 1, 9);
		T.add(formatText(this.iwrb.getLocalizedString(PARAM_CHILDREN_MANDATORY, "Children mandatory")), 1,
				10);
		T.add(childrenMandatoryCheckBox, 1, 11);
		T.add(formatText(this.iwrb.getLocalizedString(PARAM_MAX_CHOICES, "Max number of choices")), 1,
				12);
		T.add(numberOfChoices, 1, 13);
		
		T.add(formatText(this.iwrb.getLocalizedString(LABEL_INFO, "Info")), 1,
				14);

		T.add(makeTextArea(sInfo), 1, 15);
		T2.add(formatText(this.iwrb.getLocalizedString("icon", "Icon")), 1, 1);
		T2.add(Text.getBreak(), 1, 1);
		T2.add(this.makeImageInput(iIconId, "iconid"), 1, 1);
		T2.add(new SubmitButton(this.iwrb.getImage("save.gif"), PARAM_SAVE), 1,
				2);
		T2
				.add(new SubmitButton(this.iwrb.getImage("delete.gif"),
						PARAM_DELETE), 1, 2);
		form.add(Frame);
		return form;
	}

	private Form makeSubcategoryFields(
			ApartmentSubcategory eApartmentSubcategory) throws RemoteException,
			FinderException {
		boolean e = eApartmentSubcategory != null ? true : false;
		String sName = e ? eApartmentSubcategory.getName() : "";
		String sInfo = e ? eApartmentSubcategory.getInfo() : "";
		String sId = e ? String.valueOf((Integer) eApartmentSubcategory
				.getPrimaryKey()) : "";
		String sCategory = e ? String.valueOf((Integer) eApartmentSubcategory
				.getApartmentCategory().getPrimaryKey()) : "";
		int iIconId = e ? eApartmentSubcategory.getImage() : 1;
		int iTextId = e ? eApartmentSubcategory.getTextId() : -1;
		Form form = new Form();
		Table Frame = new Table(2, 1);
		Frame.setRowVerticalAlignment(1, "top");
		Frame.setCellpadding(0);
		Frame.setCellspacing(0);
		Frame.setColor(2, 1, "#EFEFEF");
		Frame.setWidth("100%");
		Frame.setWidth(2, 1, "160");
		Frame.setHeight("100%");
		Table T = new Table();
		T.setCellpadding(2);
		T.setWidth("100%");
		Table T2 = new Table(1, 2);
		T2.setCellpadding(8);
		T2.setHeight("100%");
		T2.setWidth("100%");
		T2.setVerticalAlignment(1, 1, "top");
		T2.setVerticalAlignment(1, 2, "bottom");
		T2.setAlignment(1, 2, "center");
		Frame.setAlignment(1, 1, "center");
		Frame.add(T, 1, 1);
		Frame.setAlignment(2, 1, "center");
		Frame.add(T2, 2, 1);

		TextInput name = new TextInput(PARAM_NAME, sName);

		DropdownMenu subcategories = drpSubcategory(this.service
				.getApartmentSubcategoryHome().findAll(), PARAM_ID,
				"Subcategory", sId);

		DropdownMenu categories = drpLodgings(this.service
				.getApartmentCategoryHome().findAll(), PARAM_CATEGORY,
				"Category", sCategory);

		subcategories.setToSubmit();
		HiddenInput HI = new HiddenInput(PARAM_CHOICE, String
				.valueOf(this.ACTION_SUBCATEGORY));
		HiddenInput HA = new HiddenInput(this.PARAM_ACTION, String
				.valueOf(this.ACTION_SUBCATEGORY));
		setStyle(name);
		setStyle(categories);
		name.setLength(30);
		T.add(HI);
		T.add(HA);
		T.add(subcategories, 1, 1);
		T.add(formatText(this.iwrb.getLocalizedString(LABEL_NAME, "Name")), 1,
				2);
		T.add(formatText(this.iwrb.getLocalizedString(LABEL_TEXT, "Text")), 2,
				2);
		T.add(name, 1, 3);
		T.add(makeTextInput(iTextId), 2, 3);
		T.add(formatText(this.iwrb.getLocalizedString(LABEL_CATEGORY, "Category")), 1, 4);
		T.add(categories, 1, 5);
		T.add(formatText(this.iwrb.getLocalizedString(LABEL_INFO, "Info")), 1,
				6);

		T.add(makeTextArea(sInfo), 1, 7);
		T2.add(formatText(this.iwrb.getLocalizedString("icon", "Icon")), 1, 1);
		T2.add(Text.getBreak(), 1, 1);
		T2.add(this.makeImageInput(iIconId, "iconid"), 1, 1);
		T2.add(new SubmitButton(this.iwrb.getImage("save.gif"), PARAM_SAVE), 1,
				2);
		T2
				.add(new SubmitButton(this.iwrb.getImage("delete.gif"),
						PARAM_DELETE), 1, 2);
		form.add(Frame);
		return form;
	}

	private Form makeTypeFields(ApartmentType eApartmentType, int iPhoto,
			int iPlan) throws FinderException, RemoteException {
		boolean e = eApartmentType != null ? true : false;
		String sName = e ? eApartmentType.getName() : "";
		String sInfo = e ? eApartmentType.getInfo() : "";
		String sAbbrev = e ? eApartmentType.getAbbreviation() : "";
		String sSubcategory = e ? String.valueOf(eApartmentType
				.getApartmentSubcategoryID()) : "";
		String sArea = e ? String.valueOf(eApartmentType.getArea()) : "";
		String sRoomCount = e ? String.valueOf(eApartmentType.getRoomCount())
				: "";
		String sId = e ? String.valueOf((Integer) eApartmentType
				.getPrimaryKey()) : "";
		String sExtraInfo = e ? eApartmentType.getExtraInfo() : "";
		String sRent = e ? String.valueOf(eApartmentType.getRent()) : "0";
		boolean locked = e ? eApartmentType.getLocked() : false;

		boolean bKitch = e ? eApartmentType.getKitchen() : false;
		boolean bBath = e ? eApartmentType.getBathRoom() : false;
		boolean bStor = e ? eApartmentType.getStorage() : false;
		boolean bBalc = e ? eApartmentType.getBalcony() : false;
		boolean bStud = e ? eApartmentType.getStudy() : false;
		boolean bLoft = e ? eApartmentType.getLoft() : false;
		boolean bFurniture = e ? eApartmentType.getFurniture() : false;
		int iImageId = e ? eApartmentType.getImageId() : iPhoto;
		int iPlanId = e ? eApartmentType.getFloorPlanId() : iPlan;
		int iTextId = e ? eApartmentType.getTextId() : -1;

		Form form = new Form();

		Table Frame = new Table(2, 1);
		Frame.setCellpadding(0);
		Frame.setCellspacing(0);
		Frame.setWidth("100%");
		Frame.setHeight("100%");
		Frame.setWidth(1, 1, "100%");
		Frame.setColor(2, 1, "#EFEFEF");
		Frame.setRowVerticalAlignment(1, "top");
		Table T = new Table();
		T.setCellpadding(2);
		T.setWidth("100%");
		Table T2 = new Table(1, 3);
		T2.setCellpadding(8);
		T2.setWidth("100%");
		T2.setHeight("100%");
		T2.setHeight(3, "100%");
		T2.setAlignment(1, 3, "center");
		T2.setVerticalAlignment(1, 3, "bottom");
		Frame.setAlignment(2, 1, "center");
		Frame.add(T2, 2, 1);
		Frame.setAlignment(1, 1, "center");
		Frame.add(T, 1, 1);
		Table InnerTable = new Table();
		// InnerTable.setWidth("100%");
		TextInput name = new TextInput(PARAM_NAME, sName);
		TextInput abbrev = new TextInput(PARAM_ABBREVATION, sAbbrev);
		DropdownMenu roomcount = drpCount(PARAM_ROOMCOUNT, "--", sRoomCount, 6);
		TextInput area = new TextInput(PARAM_AREA, sArea);
		area.setLength(4);
		TextInput rent = new TextInput(PARAM_RENT, sRent);
		rent.setLength(10);
		CheckBox kitch = new CheckBox(PARAM_KITCHEN, "true");
		if (bKitch) {
			kitch.setChecked(true);
		}
		CheckBox bath = new CheckBox(PARAM_BATH, "true");
		if (bBath) {
			bath.setChecked(true);
		}
		CheckBox stor = new CheckBox(PARAM_STORAGE, "true");
		if (bStor) {
			stor.setChecked(true);
		}
		CheckBox balc = new CheckBox(PARAM_BALCONY, "true");
		if (bBalc) {
			balc.setChecked(true);
		}
		CheckBox study = new CheckBox(PARAM_STUDY, "true");
		if (bStud) {
			study.setChecked(true);
		}
		CheckBox loft = new CheckBox(PARAM_LOFT, "true");
		if (bLoft) {
			loft.setChecked(true);
		}
		CheckBox furni = new CheckBox(PARAM_FURNITURE, "true");
		if (bFurniture) {
			furni.setChecked(true);
		}

		CheckBox typeLocked = new CheckBox(PARAM_TYPE_LOCKED, "true");
		typeLocked.setChecked(locked);

		DropdownMenu apartmenttypes = drpLodgings(this.service
				.getApartmentTypeHome().findAllIncludingLocked(), PARAM_ID,
				"Type", sId);
		apartmenttypes.setToSubmit();

		DropdownMenu subcategories = drpSubcategory(this.service
				.getApartmentSubcategoryHome().findAll(), PARAM_SUBCATEGORY,
				"Subcategory", sSubcategory);

		HiddenInput HI = new HiddenInput(PARAM_CHOICE, String
				.valueOf(this.ACTION_TYPE));
		HiddenInput HA = new HiddenInput(this.PARAM_ACTION, String
				.valueOf(this.ACTION_TYPE));
		name.setLength(30);
		setStyle(name);
		setStyle(abbrev);
		setStyle(area);
		setStyle(rent);
		setStyle(roomcount);
		setStyle2(kitch);
		setStyle2(bath);
		setStyle2(stor);
		setStyle2(balc);
		setStyle2(study);
		setStyle2(loft);
		setStyle2(furni);
		setStyle(apartmenttypes);
		setStyle(subcategories);
		T.add(HI);
		T.add(HA);

		T.add(formatText(this.iwrb.getLocalizedString(LABEL_TYPE, "Type")), 1,
				1);

		T.add(apartmenttypes, 1, 1);
		T.add(formatText(this.iwrb.getLocalizedString(LABEL_NAME, "Name")), 1,
				2);
		T.add(formatText(this.iwrb.getLocalizedString(LABEL_ABBREVIATION,
				"Abbreviation")), 1, 4);
		T.add(formatText(this.iwrb.getLocalizedString(LABEL_TEXT, "Text")), 2,
				2);
		T.add(name, 1, 3);
		T.add(makeTextInput(iTextId), 2, 3);
		T.add(abbrev, 1, 5);
		T.add(formatText(this.iwrb.getLocalizedString(LABEL_CATEGORY,
				"Category")
				+ " "), 1, 6);
		T.add(subcategories, 1, 7);
		InnerTable.add(formatText(this.iwrb.getLocalizedString(
				LABEL_ROOM_COUNT, "Room count")), 1, 1);
		InnerTable.add(roomcount, 2, 1);
		InnerTable.add(formatText(this.iwrb.getLocalizedString(LABEL_AREA,
				"Area(m2)")), 3, 1);
		InnerTable.add(area, 4, 1);
		InnerTable.add(formatText(this.iwrb.getLocalizedString(LABEL_KITCHEN,
				"Kitchen")), 1, 2);
		InnerTable.add(kitch, 2, 2);
		InnerTable.add(formatText(this.iwrb.getLocalizedString(LABEL_BATH,
				"Bath")), 3, 2);
		InnerTable.add(bath, 4, 2);
		InnerTable.add(formatText(this.iwrb.getLocalizedString(LABEL_STORAGE,
				"Storage")), 1, 3);
		InnerTable.add(stor, 2, 3);
		InnerTable.add(formatText(this.iwrb.getLocalizedString(LABEL_STUDY,
				"Study")), 3, 3);
		InnerTable.add(study, 4, 3);
		InnerTable.add(formatText(this.iwrb.getLocalizedString(LABEL_LOFT,
				"Loft")), 1, 4);
		InnerTable.add(loft, 2, 4);
		InnerTable.add(formatText(this.iwrb.getLocalizedString(LABEL_FURNITURE,
				"Furniture")), 3, 4);
		InnerTable.add(furni, 4, 4);
		InnerTable.add(formatText(this.iwrb.getLocalizedString(LABEL_BALCONY,
				"Balcony")), 1, 5);
		InnerTable.add(balc, 2, 5);
		InnerTable.add(formatText(this.iwrb.getLocalizedString(LABEL_RENT,
				"Rent")), 1, 6);
		InnerTable.add(rent, 2, 6);
		T.add(InnerTable, 1, 8);

		T.add(formatText(this.iwrb.getLocalizedString(LABEL_LOCKED, "Locked")),
				1, 9);
		T.add(typeLocked, 1, 10);

		T.add(formatText(this.iwrb.getLocalizedString(LABEL_INFO, "Info")), 1,
				11);

		T.add(makeTextArea(sInfo), 1, 12);
		T.add(formatText(this.iwrb.getLocalizedString(LABEL_EXTRA_INFO,
				"ExtraInfo")), 1, 13);

		T.add(makeTextArea(LABEL_EXTRA_INFO, sExtraInfo), 1, 14);
		T.mergeCells(1, 12, 2, 12);
		T.mergeCells(1, 14, 2, 14);
		T2.add(formatText(this.iwrb.getLocalizedString(LABEL_PHOTO, "Photo")),
				1, 1);
		T2.add(this.makeImageInput(iImageId, "tphotoid"), 1, 1);
		T2.add(formatText(this.iwrb.getLocalizedString(LABEL_PLAN, "Plan")), 1,
				2);
		T2.add(this.makeImageInput(iPlanId, "tplanid"), 1, 2);
		form.maintainParameter("tphotoid");
		form.maintainParameter("tplanid");
		Frame.add(HI);
		T2.add(new SubmitButton(LABEL_SAVE, "Save"), 1, 3);
		T2.add(new SubmitButton(LABEL_DELETE, "Delete"), 1, 3);
		form.add(Frame);
		return form;
	}

	private Form makeApartmentFields(Apartment eApartment)
			throws FinderException, RemoteException {
		boolean e = eApartment != null ? true : false;
		String sName = e ? eApartment.getName() : "";
		String sInfo = e ? eApartment.getInfo() : "";
		String sFloor = e ? String.valueOf(eApartment.getFloorId()) : "";
		String sType = e ? String.valueOf(eApartment.getApartmentTypeId()) : "";
		String sId = e ? String.valueOf((Integer) eApartment.getPrimaryKey())
				: "";
		String sSerie = e ? eApartment.getSerie() : "";
		String serialNumber = e ? eApartment.getSerialNumber() : "";

		int iTextId = e ? eApartment.getTextId() : -1;
		boolean bRentable = e ? eApartment.getRentable() : false;
		Form form = new Form();

		Table Frame = new Table(2, 1);
		Frame.setRowVerticalAlignment(1, "top");
		Frame.setCellpadding(0);
		Frame.setCellspacing(0);
		Frame.setColor(2, 1, "#EFEFEF");
		Frame.setWidth("100%");
		Frame.setWidth(1, 1, "100%");
		Frame.setHeight("100%");
		Table T = new Table(2, 12);
		T.setCellpadding(2);
		T.setWidth("100%");
		Table T2 = new Table(1, 2);
		T2.setCellpadding(8);
		T2.setHeight("100%");
		T2.setHeight(2, "100%");
		T2.setWidth("100%");
		T2.setVerticalAlignment(1, 1, "top");
		T2.setVerticalAlignment(1, 2, "bottom");
		T2.setAlignment(1, 2, "center");
		Frame.setAlignment(1, 1, "center");
		Frame.add(T, 1, 1);
		Frame.setAlignment(2, 1, "center");
		Frame.add(T2, 2, 1);

		TextInput name = new TextInput(PARAM_NAME, sName);
		TextInput serie = new TextInput(PARAM_SERIE, sSerie);
		TextInput serial = new TextInput(PARAM_APARTMENT_SERIAL_NUMBER,
				serialNumber);

		DropdownMenu apartments = drpLodgings(this.service.getApartmentHome()
				.findAll(), PARAM_ID, "Apartment", sId);
		apartments.setToSubmit();

		DropdownMenu types = this.drpLodgings(this.service
				.getApartmentTypeHome().findAll(), PARAM_TYPE, "Type", sType);
		DropdownMenu floors = this
				.drpFloors(PARAM_FLOOR, "Floor", sFloor, true);
		CheckBox rentable = new CheckBox(PARAM_RENTABLE, "true");
		if (bRentable) {
			rentable.setChecked(true);
		}
		HiddenInput HI = new HiddenInput(PARAM_CHOICE, String
				.valueOf(this.ACTION_APARTMENT));
		HiddenInput HA = new HiddenInput(this.PARAM_ACTION, String
				.valueOf(this.ACTION_APARTMENT));
		HiddenInput HID = new HiddenInput(PARAM_ID, sId);

		Window chooserWindow = new Window("b_editor", ApartmentChooser.class);
		chooserWindow.setWidth(550);
		chooserWindow.setHeight(500);
		chooserWindow.setResizable(true);
		Link chooser = new Link(this.iwb.getImage("/shared/list.gif", this.iwrb
				.getLocalizedString("select_apartment", "Select appartment"),
				13, 13));
		chooser.setWindowToOpen(ApartmentChooserWindow.class);

		form.add(HI);
		setStyle(name);
		setStyle(types);
		setStyle(floors);
		setStyle(serie);
		serie.setLength(5);
		serie.setMaxlength(5);
		name.setLength(30);
		// T.add(apartments,1,2);
		T.add(formatText(this.iwrb.getLocalizedString(LABEL_NAME, "Name")), 1,
				1);

		T.add(name, 1, 2);
		T.add(formatText(this.iwrb.getLocalizedString(LABEL_TEXT, "Text")), 2,
				2);

		T.add(makeTextInput(iTextId), 2, 2);
		T.add(formatText("&nbsp;&nbsp;"), 1, 2);
		T.add(chooser, 1, 2);

		T.add(formatText(this.iwrb
				.getLocalizedString("serial", "Serial number")), 1, 3);
		T.add(serial, 1, 4);

		T.add(formatText(this.iwrb.getLocalizedString("floor", "Floor")), 1, 5);
		T.add(floors, 1, 6);
		T.add(formatText(this.iwrb.getLocalizedString(LABEL_TYPE, "Type")), 1,
				7);
		T.add(types, 1, 8);
		// T.add(formatText(iwrb.getLocalizedString("serie","Serie")+" "),1,8);
		// T.add(serie,1,4);
		T.add(formatText(this.iwrb.getLocalizedString("rentable", "Rentable")
				+ " "), 1, 9);
		T.add(rentable, 1, 10);
		T.add(formatText(this.iwrb.getLocalizedString(LABEL_INFO, "Info")), 1,
				11);
		T.add(makeTextArea(sInfo), 1, 12);
		T.mergeCells(1, 12, 2, 12);
		T2.add(formatText(this.iwrb.getLocalizedString(LABEL_PHOTO, "Photo:")),
				1, 1);
		T2.add(this.makeImageInput(1, "photoid"), 1, 1);
		form.add(HI);
		form.add(HA);
		if (e) {
			form.add(HID);
		}
		T2.add(new SubmitButton(this.iwrb.getImage("save.gif"), PARAM_SAVE), 1,
				2);
		T2
				.add(new SubmitButton(this.iwrb.getImage("delete.gif"),
						PARAM_DELETE), 1, 2);
		form.add(Frame);
		return form;
	}

	private PresentationObject getApartments() throws FinderException,
			RemoteException {
		int border = 0;
		int padding = 6;
		int spacing = 1;
		Collection complexes = this.service.getComplexHome().findAll();

		int b = 1, f = 1;

		Table T = new Table();
		T.setRowVerticalAlignment(1, "top");
		T.setCellpadding(padding);
		T.setCellspacing(spacing);
		T.setVerticalZebraColored("#942829", "#21304a");
		T.setBorder(border);

		int i = 1;
		for (Iterator iter = complexes.iterator(); iter.hasNext();) {
			Complex complex = (Complex) iter.next();
			T.add(getHeaderText(complex.getName()), i, 1);
			Collection buildings = this.service.getBuildingHome()
					.findByComplex((Integer) complex.getPrimaryKey());

			Table BuildingTable = new Table();
			BuildingTable.setCellpadding(padding);
			BuildingTable.setCellspacing(spacing);
			BuildingTable.setBorder(border);
			T.add(BuildingTable, i, 2);
			b = 1;

			for (Iterator iter2 = buildings.iterator(); iter2.hasNext();) {
				Building building = (Building) iter2.next();

				BuildingTable.add(getHeaderText(building.getName()), 1, b++);
				Collection floors = this.service.getFloorHome().findByBuilding(
						(Integer) building.getPrimaryKey());

				Table FloorTable = new Table();
				FloorTable.setCellpadding(padding);
				FloorTable.setCellspacing(spacing);
				FloorTable.setBorder(border);
				BuildingTable.add(FloorTable, 1, b++);
				f = 1;

				for (Iterator iter3 = floors.iterator(); iter3.hasNext();) {
					Floor floor = (Floor) iter3.next();

					FloorTable.add(getHeaderText(floor.getName()), 1, f++);
					Collection apartments = this.service.getApartmentHome()
							.findByFloor((Integer) floor.getPrimaryKey());
					if (apartments != null && !apartments.isEmpty()) {

						Table ApartmentTable = new Table();
						ApartmentTable.setCellpadding(padding);
						ApartmentTable.setBorder(border);
						ApartmentTable.setCellspacing(spacing);
						FloorTable.add(ApartmentTable, 1, f++);

						int l = 1;
						for (Iterator iter4 = apartments.iterator(); iter4
								.hasNext();) {
							Apartment apartment = (Apartment) iter4.next();

							ApartmentTable.add(getApLink(apartment
									.getPrimaryKey().toString(), apartment
									.getName()), 1, l++);

						}
					}

				}

			}

		}
		T.setRowVerticalAlignment(2, "top");
		T.setVerticalZebraColored("#942829", "#21304a");
		return T;

	}

	private PresentationObject getTypes() throws RemoteException,
			FinderException {

		Collection types = this.service.getApartmentTypeHome().findAll();

		Table T = new Table();

		if (types != null && !types.isEmpty()) {
			T = new Table(10, types.size() + 1);

			T.setCellpadding(4);
			T.setCellspacing(2);

			int row = 1, col = 1;
			T.add(getHeaderText(this.iwrb
					.getLocalizedString(LABEL_NAME, "Name")), col++, row);
			T.add(getHeaderText(this.iwrb.getLocalizedString(LABEL_AREA,
					"Area(m2)")), col++, row);
			T.add(
					getHeaderText(this.iwrb
							.getLocalizedString("rooms", "Rooms")), col++, row);
			T.add(getHeaderText(this.iwrb.getLocalizedString(LABEL_KITCHEN,
					"Kitchen")), col++, row);
			T.add(getHeaderText(this.iwrb
					.getLocalizedString(LABEL_BATH, "Bath")), col++, row);
			T.add(getHeaderText(this.iwrb.getLocalizedString(LABEL_STORAGE,
					"Storage")), col++, row);
			T.add(getHeaderText(this.iwrb.getLocalizedString(LABEL_STUDY,
					"Study")), col++, row);
			T.add(getHeaderText(this.iwrb
					.getLocalizedString(LABEL_LOFT, "Loft")), col++, row);
			T.add(getHeaderText(this.iwrb.getLocalizedString(LABEL_FURNITURE,
					"Furniture")), col++, row);
			T.add(getHeaderText(this.iwrb.getLocalizedString(LABEL_BALCONY,
					"Balcony")), col++, row);
			T.setColumnAlignment(3, "center");
			T.setColumnAlignment(4, "center");
			T.setColumnAlignment(5, "center");
			T.setColumnAlignment(6, "center");
			T.setColumnAlignment(7, "center");
			T.setColumnAlignment(8, "center");
			T.setColumnAlignment(9, "center");
			T.setColumnAlignment(10, "center");

			for (Iterator iter = types.iterator(); iter.hasNext();) {
				ApartmentType type = (ApartmentType) iter.next();

				row += 2;

				col = 1;

				T.add(
						getATLink(type.getPrimaryKey().toString(), type
								.getName()), col++, row);
				T.add(getBodyText(String.valueOf(type.getArea())), col++, row);
				T.add(getBodyText(type.getRoomCount()), col++, row);
				T.add(getBodyText(type.getKitchen() ? "X" : "N"), col++, row);
				T.add(getBodyText(type.getBathRoom() ? "X" : "N"), col++, row);
				T.add(getBodyText(type.getStorage() ? "X" : "N"), col++, row);
				T.add(getBodyText(type.getStudy() ? "X" : "N"), col++, row);
				T.add(getBodyText(type.getLoft() ? "X" : "N"), col++, row);
				T.add(getBodyText(type.getFurniture() ? "X" : "N"), col++, row);
				T.add(getBodyText(type.getBalcony() ? "X" : "N"), col++, row);

			}
			T.setBorder(0);
			T.setVerticalZebraColored("#942829", "#21304a");

		}
		return T;

	}

	private Text getHeaderText(int i) {
		return getHeaderText(String.valueOf(i));
	}

	private Text getHeaderText(String s) {
		Text T = new Text(s);
		T.setBold();
		T.setFontColor("#FFFFFF");
		return T;
	}

	private Text getBodyText(int i) {
		return getHeaderText(String.valueOf(i));
	}

	private Text getBodyText(String s) {
		Text T = new Text(s);
		T.setFontColor("#FFFFFF");
		return T;
	}

	private Link getATLink(String id, String name) {
		Link L = new Link(name);

		L.setFontColor("#FFFFFF");
		L.addParameter(PARAM_ID, id);
		L.addParameter(this.PARAM_ACTION, this.ACTION_TYPE);
		L.addParameter(PARAM_CHOICE, this.ACTION_TYPE);
		return L;
	}

	private Link getApLink(String id, String name) {
		Link L = new Link(name);

		L.setFontColor("#FFFFFF");
		L.addParameter(PARAM_ID, id);
		L.addParameter(this.PARAM_ACTION, this.ACTION_APARTMENT);
		L.addParameter(PARAM_CHOICE, this.ACTION_APARTMENT);
		return L;
	}

	private DropdownMenu drpFloors(String name, String display,
			String selected, boolean withBuildingName) throws RemoteException,
			FinderException {
		Collection floors = this.service.getFloorHome().findAll();

		DropdownMenu drp = new DropdownMenu(name);

		drp.addDisabledMenuElement("0", display);
		for (Iterator iter = floors.iterator(); iter.hasNext();) {
			Floor floor = (Floor) iter.next();

			if (withBuildingName) {
				try {

					drp.addMenuElement(floor.getPrimaryKey().toString(), floor
							.getName()
							+ " "
							+ this.service.getBuildingHome().findByPrimaryKey(
									new Integer(floor.getBuildingId()))
									.getName());

				} catch (Exception e) {
				}
			} else {
				drp.addMenuElement(floor.getPrimaryKey().toString(), floor
						.getName());
			}
		}
		if (!selected.equalsIgnoreCase("")) {
			drp.setSelectedElement(selected);
		}
		return drp;
	}

	private DropdownMenu drpCount(String name, String display, String selected,
			int len) {
		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElement("0", display);
		for (int i = 1; i < len + 1; i++) {
			drp.addMenuElement(String.valueOf(i));
		}
		if (!selected.equalsIgnoreCase("")) {
			drp.setSelectedElement(selected);
		}
		return drp;
	}

	private DropdownMenu drpLodgings(Collection lodgings, String name,
			String display, String selected) {

		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElement("0", display);

		for (Iterator iter = lodgings.iterator(); iter.hasNext();) {
			BuildingEntity entity = (BuildingEntity) iter.next();

			drp.addMenuElement(entity.getPrimaryKey().toString(), entity
					.getName());
		}

		if (!selected.equalsIgnoreCase("")) {
			drp.setSelectedElement(selected);
		}

		return drp;
	}

	private DropdownMenu drpSubcategory(Collection subcategory, String name,
			String display, String selected) {

		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElement("0", display);

		for (Iterator iter = subcategory.iterator(); iter.hasNext();) {
			ApartmentSubcategory entity = (ApartmentSubcategory) iter.next();

			StringBuffer menuDisplay = new StringBuffer(entity.getName());
			menuDisplay.append("(");
			if (entity.getApartmentCategory() != null) {
				menuDisplay.append(entity.getApartmentCategory().getName());
			}
			menuDisplay.append(")");
			
			drp.addMenuElement(entity.getPrimaryKey().toString(), menuDisplay.toString());
		}

		if (!selected.equalsIgnoreCase("")) {
			drp.setSelectedElement(selected);
		}

		return drp;
	}

	
	public Text formatText(String s) {
		Text T = new Text();
		if (s != null) {
			T = new Text(s);
			// if(this.fontBold)
			T.setBold();
			T.setFontColor(this.TextFontColor);
			T.setFontSize(this.fontSize);
			T.setFontFace(Text.FONT_FACE_VERDANA);
		}
		return T;
	}

	public Text formatText(int i) {
		return formatText(String.valueOf(i));
	}

	public void main(IWContext iwc) throws Exception {
		this.iwrb = getResourceBundle(iwc);
		this.iwb = getBundle(iwc);

		this.isAdmin = iwc.hasEditPermission(this);
		this.getParentPage().setName("b_editor");
		this.getParentPage().setTitle(
				this.iwrb.getLocalizedString("buildingEditor",
						"Building Editor"));
		this.getParentPage().setAllMargins(0);

		/** @todo: fixa Admin */
		control(iwc);
	}

	protected void setStyle(InterfaceObject O) {
		O.setMarkupAttribute("style", this.styleAttribute);
	}

	protected void setStyle2(InterfaceObject O) {
		O.setMarkupAttribute("style", this.styleAttribute2);
	}

	protected BuildingService getBuildingService(IWContext iwc)
			throws IBOLookupException {
		return (BuildingService) IBOLookup.getServiceInstance(iwc,
				BuildingService.class);
	}
} // class BuildingEditor
