/*
 * Created on Jun 16, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package is.idega.idegaweb.travel.service.hotel.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;


import com.idega.block.trade.stockroom.data.PriceCategory;
import com.idega.block.trade.stockroom.data.PriceCategoryBMPBean;
import com.idega.block.trade.stockroom.data.PriceCategoryHome;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import is.idega.idegaweb.travel.presentation.TravelManager;
import is.idega.idegaweb.travel.service.hotel.data.RoomType;
import is.idega.idegaweb.travel.service.hotel.data.RoomTypeHome;

/**
 * @author gimmi
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class HotelSetup extends TravelManager {

	public static final String HOTEL_SEARCH_PRICE_CATEGORY_KEY = "hotel_search";

	private IWResourceBundle iwrb;
	
	private String ACTION = "hs_ac";
	private String ACTION_SAVE = "hs_asrt";
	private String PARAMETER_NAME = "hs_prm_n";
	private String PARAMETER_ROOM_TYPE_ID = "hs_prm_rtid";
	private String PARAMETER_CATEGORY_ID = "hs_prm_cat_id";

	private String PARAMETER_MENU = "hs_prm_menu";
	private String MENU_PARAMETER_ROOM_TYPES = "hs_mprm_rt";
	private String MENU_PARAMETER_PRICES = "hs_mprm_pr";
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);	
		iwrb = getTravelSessionManager(iwc).getIWResourceBundle();
		
		String menu = iwc.getParameter(PARAMETER_MENU);
		
		add(Text.BREAK);
		insertDropdown(iwc);
		if (menu == null || menu.equals(MENU_PARAMETER_ROOM_TYPES)) {
			roomTypeSetup(iwc);
		} else if (menu.equals(MENU_PARAMETER_PRICES)) {
			pricesSetup(iwc);
		}
	}

	private void insertDropdown(IWContext iwc) {
		Form form = new Form();
		
		DropdownMenu menu = new DropdownMenu(PARAMETER_MENU);
		menu.addMenuElement(MENU_PARAMETER_ROOM_TYPES, iwrb.getLocalizedString("travel.hotel.room_types","Room types"));
		menu.addMenuElement(MENU_PARAMETER_PRICES, iwrb.getLocalizedString("travel.hotel.prices","Prices"));
		menu.setToSubmit();
		
		menu.setSelectedElement(iwc.getParameter(PARAMETER_MENU));
		
		form.add(menu);
		add(form);
	}
	
	private void pricesSetup(IWContext iwc) throws RemoteException {
		String action = iwc.getParameter(ACTION);
		if (action != null && action.equals(ACTION_SAVE)) {
			handlePriceInsert(iwc);
		}
		
		drawPriceSetup(iwc);
		
	}
	
	private void handlePriceInsert(IWContext iwc) {
		String categoryId = iwc.getParameter(PARAMETER_CATEGORY_ID);
		String name = iwc.getParameter(PARAMETER_NAME);
		if (name != null && !name.equals("")) {
			PriceCategory category;
			try {
				PriceCategoryHome pcHome = (PriceCategoryHome) IDOLookup.getHome(PriceCategory.class);
				if (categoryId != null) {
					category = pcHome.findByPrimaryKey(new Integer(categoryId));
					category.setName(name);
//					category.setVisibility(PriceCategoryBMPBean.PRICE_VISIBILITY_BOTH_PRIVATE_AND_PUBLIC);
//					category.setCountAsPerson(true);
					category.store();
				} else {
					int iCategoryId = getStockroomBusiness(iwc).createPriceCategory(-1, name, "price category for hotel search form", "", HOTEL_SEARCH_PRICE_CATEGORY_KEY);
					category = pcHome.findByPrimaryKey(iCategoryId);
					category.setVisibility(PriceCategoryBMPBean.PRICE_VISIBILITY_BOTH_PRIVATE_AND_PUBLIC);
					category.setCountAsPerson(true);
					category.store();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void drawPriceSetup(IWContext iwc) throws RemoteException {
		Form form = new Form();
		Table table = getTable();
		form.add(table);
		form.maintainParameter(PARAMETER_MENU);
		int row = 1;
		int empty = 1;
		
		
		
		table.add(getHeaderText(iwrb.getLocalizedString("travel.setup_price","Setup price")), 1, row);
		table.setRowColor(row, backgroundColor);

		try {
			++row;
			TextInput catName;
			PriceCategory[] categories = getStockroomBusiness(iwc).getPriceCategories(HOTEL_SEARCH_PRICE_CATEGORY_KEY);
			if (categories != null && categories.length == 1) {
				catName = new TextInput(PARAMETER_NAME, categories[0].getName());
				table.add(new HiddenInput(PARAMETER_CATEGORY_ID, categories[0].getPrimaryKey().toString()));
			} else {
				catName = new TextInput(PARAMETER_NAME);
			}
			table.add(catName, 1, row);
			table.setRowColor(row, GRAY);
			++row;
			
			table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
			table.add(new SubmitButton(iwrb.getImage("/buttons/save.gif"), ACTION, ACTION_SAVE), 1, row);
			table.setRowColor(row, GRAY);
		}catch (Exception fe) {
			++row;
			table.add(super.getText(iwrb.getLocalizedString("travel.error_getting_price","Error getting price")), 1, row);
		} 
		add(form);
	}		
	
	private void roomTypeSetup(IWContext iwc) {
		String action = iwc.getParameter(ACTION);
		if (action != null && action.equals(ACTION_SAVE)) {
			handleRoomTypeInsert(iwc);
		}
		
		drawRoomTypeSetup();
	}
	
	private void handleRoomTypeInsert(IWContext iwc) {
		String[] name = iwc.getParameterValues(PARAMETER_NAME);
		String[] ids = iwc.getParameterValues(PARAMETER_ROOM_TYPE_ID);
		
		if (ids != null) {
			try {
				RoomTypeHome rth;
					rth = (RoomTypeHome) IDOLookup.getHome(RoomType.class);
				RoomType roomType;
				for (int i = 0 ; i < ids.length; i++) {
					if ("-1".equals(ids[i])) {
						if ( !"".equals(name[i]) ) {
							roomType = rth.create();
							roomType.setName(name[i]);
							roomType.setIsValid(true);
							roomType.store();
						}
					}else {
						roomType = rth.findByPrimaryKey(new Integer(ids[i]));
						if ("".equals(name[i])) {
							roomType.setIsValid(false);
						}else {
							roomType.setName(name[i]);	
							roomType.setIsValid(true);
						}
						roomType.store();
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}
	
	private void drawRoomTypeSetup() {
		Form form = new Form();
		Table table = getTable();
		form.add(table);
		int row = 1;
		int empty = 1;
		form.maintainParameter(PARAMETER_MENU);
		
		
		
		table.add(getHeaderText(iwrb.getLocalizedString("travel.setup_room_types","Setup room types")), 1, row);
		table.setRowColor(row, backgroundColor);

		try {
			RoomTypeHome rth = (RoomTypeHome) IDOLookup.getHome(RoomType.class);
			Collection roomTypes = rth.findAll();
			RoomType roomType;
			TextInput name;
			 
			if (roomTypes != null) {
				Iterator iter = roomTypes.iterator();
				while (iter.hasNext()) {
					++row;
					roomType = (RoomType) iter.next();
					table.add(new TextInput(PARAMETER_NAME, roomType.getName()), 1, row);
					table.add(new HiddenInput(PARAMETER_ROOM_TYPE_ID, roomType.getPrimaryKey().toString()), 1, row);
					table.setRowColor(row, GRAY);
				}	
			}
			++row;
			table.add(new TextInput(PARAMETER_NAME), 1, row);
			table.add(new HiddenInput(PARAMETER_ROOM_TYPE_ID, "-1"), 1, row);
			table.setRowColor(row, GRAY);
			
			++row;
			table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
			table.add(new SubmitButton(iwrb.getImage("/buttons/save.gif"), ACTION, ACTION_SAVE), 1, row);
			table.setRowColor(row, GRAY);
		}catch (Exception fe) {
			++row;
			table.add(super.getText(iwrb.getLocalizedString("travel.error_getting_room_types","Error getting room types")), 1, row);
		} 
		add(form);
	}
	
	protected TravelStockroomBusiness getStockroomBusiness(IWContext iwc) throws RemoteException {
		return (TravelStockroomBusiness) IBOLookup.getServiceInstance(iwc, TravelStockroomBusiness.class);
	}
}
