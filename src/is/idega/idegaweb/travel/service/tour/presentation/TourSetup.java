package is.idega.idegaweb.travel.service.tour.presentation;

import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import is.idega.idegaweb.travel.presentation.TravelManager;
import is.idega.idegaweb.travel.service.tour.data.TourCategory;
import is.idega.idegaweb.travel.service.tour.data.TourCategoryHome;
import is.idega.idegaweb.travel.service.tour.data.TourType;
import is.idega.idegaweb.travel.service.tour.data.TourTypeHome;

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
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.util.SelectorUtility;

/**
 * @author gimmi
 */
public class TourSetup extends TravelManager {

	public static final String TOUR_SEARCH_PRICE_CATEGORY_KEY = "tour_search";

	private static String ACTION = "ts_ac";
	private static String ACTION_SAVE = "ts_acs";
	
	private static String ACTION_PARAMETER = "ts_a";
	private static String PARAMETER_PRICE = "ts_p";
	private static String PARAMETER_TYPES = "ts_t";
	
	private static String PARAMETER_TYPE_NAME = "ts_tn";
	private static String PARAMETER_TYPE_LOCALIZATION_KEY = "ts_tlk";
	private static String PARAMETER_TYPE_ID = "ts_tid";
	private static String PARAMETER_CATEGORY_ID = "ts_cid";
	
	private static String PARAMETER_NAME = "ts_pcn";
	
	private IWResourceBundle iwrb;
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		add(Text.BREAK);
		iwrb = getTravelSessionManager(iwc).getIWResourceBundle();

//		typeMenu(iwc);
		
		String action = iwc.getParameter(ACTION_PARAMETER);
		if (action == null || "".equals(action)) {
			mainMenu();
		} else if (action.equals(PARAMETER_TYPES)) {
			typeMenu(iwc);
		} else if (action.equals(PARAMETER_PRICE)) {
			pricesSetup(iwc);
		}
		
	}
	
	private void handleInsert(IWContext iwc) {
		String[] names = iwc.getParameterValues(PARAMETER_TYPE_NAME);
		String[] locKeys = iwc.getParameterValues(PARAMETER_TYPE_LOCALIZATION_KEY);
		String[] catIds = iwc.getParameterValues(PARAMETER_CATEGORY_ID);
		String[] ids = iwc.getParameterValues(PARAMETER_TYPE_ID);
		
		if (ids != null) {
			try {
				TourType type;
				TourTypeHome typeHome = getTourTypeHome();
				for (int i = 0; i < ids.length; i++) {
					if (!ids[i].equals("-1")) {
						type = typeHome.findByPrimaryKey(ids[i]);
					} else {
						type = null;
					}

					if ("-1".equals(ids[i])) {
						if ( !"".equals(names[i]) && type == null) {
							type = typeHome.create();
							type.setName(names[i]);
							type.setLocalizationKey(locKeys[i]);
							type.setTourCategory(catIds[i]);
							type.store();
						}
					}else {
						if ("".equals(names[i])) {
							type.remove();
						}else {
							type.setName(names[i]);
							type.setLocalizationKey(locKeys[i]);
							type.setTourCategory(catIds[i]);
							type.store();
						}
					}

				}
			} catch (Exception e){
				e.printStackTrace(System.err);
			}
		}
		
		
	}
	
	private void typeMenu(IWContext iwc) {
		handleInsert(iwc);
		Form form = new Form();
		Table table = getTable();
		form.add(table);
		int row = 1;
		table.add(super.getHeaderText(iwrb.getLocalizedString("tour.category_name", "Category Name")), 1, row);
		table.add(super.getHeaderText(iwrb.getLocalizedString("tour.localization_key", "Localization Key")), 2, row);
		table.add(super.getHeaderText(iwrb.getLocalizedString("tour.category", "Category")), 3, row);
		table.setRowColor(row, backgroundColor);
		
		
		try {
			TourType type;
			TextInput catName;
			TextInput catLocKey;
			HiddenInput catId;
			Collection categories = getTourCategoryHome().findAll();
			
			DropdownMenu drTourCategoriesToClone = new DropdownMenu(PARAMETER_CATEGORY_ID );
			SelectorUtility su = new SelectorUtility();
			drTourCategoriesToClone = (DropdownMenu) su.getSelectorFromIDOEntities(drTourCategoriesToClone, categories, "getLocalizationKey", iwrb);
			
			DropdownMenu tourCategories;

			Collection types = getTourTypeHome().findAll();
			Iterator iter = types.iterator();
			while (iter.hasNext()) {
				++row;
				type = (TourType) iter.next();
				catName = new TextInput(PARAMETER_TYPE_NAME);
				catLocKey = new TextInput(PARAMETER_TYPE_LOCALIZATION_KEY);
				tourCategories = (DropdownMenu) drTourCategoriesToClone.clone();
				catId = new HiddenInput(PARAMETER_TYPE_ID);
				
				catId.setValue(type.getPrimaryKey().toString());
				catName.setContent(type.getName());
				catLocKey.setContent(type.getLocalizationKey());
				tourCategories.setSelectedElement(type.getTourCategory());
				
				table.add(catName, 1, row);
				table.add(catLocKey, 2, row);
				table.add(tourCategories, 3, row);
				table.add(catId, 1, row);
				table.setRowColor(row, GRAY);
			}

			++row;
			catName = new TextInput(PARAMETER_TYPE_NAME);
			catLocKey = new TextInput(PARAMETER_TYPE_LOCALIZATION_KEY);
			catId = new HiddenInput(PARAMETER_TYPE_ID, "-1");
			tourCategories = (DropdownMenu) drTourCategoriesToClone.clone();
			
			table.add(catName, 1, row);
			table.add(catLocKey, 2, row);
			table.add(tourCategories, 3, row);
			table.add(catId, 1, row);
			table.setRowColor(row, GRAY);
			
			++row;
			table.add(new SubmitButton(iwrb.getLocalizedImageButton("travel.update", "Update"), ACTION_PARAMETER, PARAMETER_TYPES), 3, row);
			table.add(new SubmitButton(iwrb.getLocalizedImageButton("travel.back", "Back"), ACTION_PARAMETER, ""), 1, row);
			//table.mergeCells(1, row, 3, row);
			table.setAlignment(3, row, Table.HORIZONTAL_ALIGN_RIGHT);
			table.setRowColor(row, GRAY);
			
		
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		add(form);
	}
	
	private void mainMenu() {
		Table table = getTable();
		table.add(super.getHeaderText(iwrb.getLocalizedString("tour.tour_setup", "Tour setup")), 1, 1);
		table.setRowColor(1, backgroundColor);
		
		Link categories = new Link(getText(iwrb.getLocalizedString("tour.price", "Price")));
		categories.addParameter(ACTION_PARAMETER, PARAMETER_PRICE);
		Link types = new Link(getText(iwrb.getLocalizedString("tour.tour_types", "Tour Types")));
		types.addParameter(ACTION_PARAMETER, PARAMETER_TYPES);
		
		table.add(categories, 1, 2);
		table.setRowColor(2, GRAY);
		table.add(types, 1, 3);
		table.setRowColor(3, GRAY);
		add(table);
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
					int iCategoryId = getStockroomBusiness(iwc).createPriceCategory(-1, name, "price category for tour search form", "", TOUR_SEARCH_PRICE_CATEGORY_KEY);
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
		form.maintainParameter(ACTION_PARAMETER);
		int row = 1;
		int empty = 1;
		
		
		
		table.add(getHeaderText(iwrb.getLocalizedString("travel.setup_price","Setup price")), 1, row);
		table.setRowColor(row, backgroundColor);

		try {
			++row;
			TextInput catName;
			PriceCategory[] categories = getStockroomBusiness(iwc).getPriceCategories(TOUR_SEARCH_PRICE_CATEGORY_KEY);
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
			table.add(new SubmitButton(iwrb.getLocalizedImageButton("travel.back", "Back"), ACTION_PARAMETER, ""), 1, row);
			table.add(new SubmitButton(iwrb.getImage("/buttons/save.gif"), ACTION, ACTION_SAVE), 1, row);
			table.setRowColor(row, GRAY);
		}catch (Exception fe) {
			++row;
			table.add(super.getText(iwrb.getLocalizedString("travel.error_getting_price","Error getting price")), 1, row);
		} 
		add(form);
	}		
	
	
	private TourCategoryHome getTourCategoryHome() throws IDOLookupException {
		return (TourCategoryHome) IDOLookup.getHome(TourCategory.class);
	}
	
	private TourTypeHome getTourTypeHome() throws IDOLookupException {
		return (TourTypeHome) IDOLookup.getHome(TourType.class);
	}	

	protected TravelStockroomBusiness getStockroomBusiness(IWContext iwc) throws RemoteException {
		return (TravelStockroomBusiness) IBOLookup.getServiceInstance(iwc, TravelStockroomBusiness.class);
	}

}
