package is.idega.idegaweb.travel.service.hotel.presentation;

import is.idega.idegaweb.travel.block.search.presentation.AbstractSearchForm;
import is.idega.idegaweb.travel.service.hotel.data.Hotel;
import is.idega.idegaweb.travel.service.hotel.data.HotelHome;
import is.idega.idegaweb.travel.service.hotel.data.HotelType;
import is.idega.idegaweb.travel.service.hotel.data.HotelTypeHome;
import is.idega.idegaweb.travel.service.hotel.data.RoomType;
import is.idega.idegaweb.travel.service.hotel.data.RoomTypeHome;
import is.idega.idegaweb.travel.service.presentation.BookingForm;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.util.IWTimestamp;

/**
 * @author gimmi
 */
public class HotelSearch extends AbstractSearchForm {

	protected String PARAMETER_TYPE_COUNT = HotelBookingForm.parameterCountToCheck;
	protected String PARAMETER_HOTEL_TYPE = "hs_pht";
	protected String PARAMETER_MIN_RATING = "hs_mir";
	protected String PARAMETER_MAX_RATING = "hs_mar";
	
	private IWContext iwc;
	private static Image star = null;

	public HotelSearch() {
		super();
	}

	public void main(IWContext iwc) throws Exception {
		this.iwc = iwc;
		super.main(iwc);
	}

	protected String getServiceName(IWResourceBundle iwrb) {
		return iwrb.getLocalizedString("travel.search.accomodation","Accomodation");		
	}
	
	protected Image getHeaderImage(IWResourceBundle iwrb) {
		return iwrb.getImage("/search/accomodation.png");
	}
	
	protected String getPriceCategoryKey() {
		return HotelSetup.HOTEL_SEARCH_PRICE_CATEGORY_KEY;
	}
		
	protected Collection getResults() throws RemoteException {
		String sManyDays = iwc.getParameter(PARAMETER_MANY_DAYS);
		String sRoomType[] = iwc.getParameterValues(PARAMETER_TYPE);
		String sRoomTypeCount[] = iwc.getParameterValues(PARAMETER_TYPE_COUNT);
		String sHotelType[] = iwc.getParameterValues(PARAMETER_HOTEL_TYPE);
		String sMinRating = iwc.getParameter(PARAMETER_MIN_RATING);
		String sMaxRating = iwc.getParameter(PARAMETER_MAX_RATING);
		String supplierName = iwc.getParameter(PARAMETER_SUPPLIER_NAME);
		if (supplierName != null) {
			supplierName = supplierName.trim();
		}
		
		try {
			Object[] roomTypeIds = null;
			if (sRoomType != null && sRoomType.length > 0) {
				roomTypeIds = new Object[sRoomType.length];
				for (int i = 0; i < roomTypeIds.length; i++) {
					roomTypeIds[i] = sRoomType[i];
				}
			}
			
			Object[] hotelTypeIds = null;
			if (sHotelType != null && sHotelType.length > 0) {
				hotelTypeIds = new Object[sHotelType.length];
				for (int i = 0; i < hotelTypeIds.length; i++) {
					if ("-1".equals(sHotelType[i])) {
						hotelTypeIds = null;
						break;
					}
					hotelTypeIds[i] = sHotelType[i];
				}
			}
			
			float min = -1;
			float max = -1;
			
			try {
				min = Float.parseFloat(sMinRating);
			} catch (Exception e) {}
			try {
				max = Float.parseFloat(sMaxRating);
			} catch (Exception e) {}
		
			Collection postalCodes = getBookingForm().getPostalCodeIds(iwc);
			
			Object[] suppIds = getSupplierIDs();


			HotelHome hHome = (HotelHome) IDOLookup.getHome(Hotel.class);

			Collection coll = new Vector();
			
			if (suppIds.length > 0) {
				coll = hHome.find(null, null, roomTypeIds, hotelTypeIds, postalCodes, suppIds, min, max, supplierName);
			}
			
			return coll;
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void showCollectionContent(Collection coll) {
		try {
			Iterator iter = coll.iterator();
			while (iter.hasNext()) {
				System.out.print(iter.next().toString()+", ");
			}
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	protected void setupSearchForm() throws RemoteException {
		BookingForm bf = getBookingForm();
		
		int state = getSession(iwc).getState();
		Product prod = super.definedProduct;
		
		boolean defined = super.hasDefinedProduct();
		Hotel hotel = null;
		if (defined) {
			try {
				hotel = getHotelHome().findByPrimaryKey(definedProduct.getPrimaryKey());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		IWTimestamp now = IWTimestamp.RightNow();
		
		DateInput fromDate = new DateInput(PARAMETER_FROM_DATE);
		fromDate.setDate(now.getDate());
		now.addDays(1);
		DateInput toDate = new DateInput(PARAMETER_TO_DATE);
		toDate.setDate(now.getDate());

		Collection hotelTypes = new Vector();
		try {
			HotelTypeHome trh = (HotelTypeHome) IDOLookup.getHome(HotelType.class);
			hotelTypes = trh.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		DropdownMenu spHotelTypes = new DropdownMenu(PARAMETER_HOTEL_TYPE );
		SelectorUtility su = new SelectorUtility();
		spHotelTypes = (DropdownMenu) su.getSelectorFromIDOEntities(spHotelTypes, hotelTypes, "getLocalizationKey", iwrb);
		spHotelTypes.addMenuElementFirst("-1", iwrb.getLocalizedString("travel.any_types", "Any type"));
		spHotelTypes.setSelectedElement("-1");

		Collection roomTypes = new Vector();
		try {
			RoomTypeHome trh = (RoomTypeHome) IDOLookup.getHome(RoomType.class);
			roomTypes = trh.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		DropdownMenu min = getDropdownWithNumbers(PARAMETER_MIN_RATING, 1, 5);
		DropdownMenu max = getDropdownWithNumbers(PARAMETER_MAX_RATING, 1, 5);
		min.addMenuElementFirst("-1", iwrb.getLocalizedString("travel.search.any", "Any"));
		min.setSelectedElement("-1");
		max.addMenuElementFirst("-1", iwrb.getLocalizedString("travel.search.any", "Any"));
		max.setSelectedElement("-1");
		
		if (hotelTypes != null) {
			Iterator iter = hotelTypes.iterator();
			HotelType hotelType;
			boolean first = true;
			
			while (iter.hasNext()) {
				hotelType = (HotelType) iter.next();
				if (first) {
					min.setDisabled(!hotelType.getUseRating());
					max.setDisabled(!hotelType.getUseRating());
				}
				spHotelTypes.setToDisableWhenSelected(min, hotelType.getPrimaryKey().toString(), !hotelType.getUseRating(), true);
				spHotelTypes.setToDisableWhenSelected(max, hotelType.getPrimaryKey().toString(), !hotelType.getUseRating(), true);
				first = false;
			}
		}

		DropdownMenu roomTypeDrop = new DropdownMenu(roomTypes, PARAMETER_TYPE);
		boolean vertical = !isHorizontal();

		if (this.isHorizontal()) {
			if (!defined) {
				try {
					bf.addAreaCodeInput(null, engine.getCountries(), vertical);
					bf.getCurrentBookingPart().mergeCells(1, bf.getCurrentBookingPartRow()-1, 2, bf.getCurrentBookingPartRow()-1);
				}
				catch (IDORelationshipException e1) {
					e1.printStackTrace();
				}
//				bf.addSupplierNameInput();
			}
			bf.addInputLine(new String[]{iwrb.getLocalizedString("travel.search.check_in_date","Check in date")}, new PresentationObject[]{fromDate}, false, false, vertical);
			bf.getCurrentBookingPart().mergeCells(1, bf.getCurrentBookingPartRow()-1, 2, bf.getCurrentBookingPartRow()-1);
			bf.addInputLine(new String[]{iwrb.getLocalizedString("travel.search.check_out_date","Check out date")}, new PresentationObject[]{toDate}, vertical);
			bf.getCurrentBookingPart().mergeCells(1, bf.getCurrentBookingPartRow()-1, 2, bf.getCurrentBookingPartRow()-1);
			bf.addInputLine(new String[]{iwrb.getLocalizedString("travel.search.minimum_rating","Minimum rating"), iwrb.getLocalizedString("travel.search.maximum_rating","Maximum rating")},  new PresentationObject[] { min, max}, vertical);
		
			bf.addInputLine(new String[]{iwrb.getLocalizedString("travel.search.number_of_rooms","Number of rooms")}, new PresentationObject[]{getDropdownWithNumbers(PARAMETER_TYPE_COUNT, 1, 7)}, false, true, vertical);
			if ( !defined ) {
				bf.addInputLine(new String[]{iwrb.getLocalizedString("travel.search.type_of_accomodation","Type of accomodation")}, new PresentationObject[]{spHotelTypes}, vertical);
				bf.getCurrentBookingPart().mergeCells(1, bf.getCurrentBookingPartRow()-1, 2, bf.getCurrentBookingPartRow()-1);
				bf.addInputLine(new String[]{iwrb.getLocalizedString("travel.search.type_of_rooms","Type of rooms")}, new PresentationObject[]{roomTypeDrop}, vertical);
				bf.getCurrentBookingPart().mergeCells(1, bf.getCurrentBookingPartRow()-1, 2, bf.getCurrentBookingPartRow()-1);
			}

		} else {
			if (!defined) {
				try {
					bf.addAreaCodeInput(null, engine.getCountries(), vertical);
				}
				catch (IDORelationshipException e1) {
					e1.printStackTrace();
				}
				bf.addSupplierNameInput(vertical);
			}
	
			bf.addInputLine(new String[]{iwrb.getLocalizedString("travel.search.check_in_date","Check in date")}, new PresentationObject[]{fromDate}, false, true, vertical);
			bf.addInputLine(new String[]{iwrb.getLocalizedString("travel.search.check_out_date","Check out date")}, new PresentationObject[]{toDate}, vertical);
			
			bf.addInputLine(new String[]{iwrb.getLocalizedString("travel.search.number_of_rooms","Number of rooms")}, new PresentationObject[]{getDropdownWithNumbers(PARAMETER_TYPE_COUNT, 1, 7)}, false, true, vertical);
			bf.addInputLine(new String[]{iwrb.getLocalizedString("travel.search.minimum_rating","Minimum rating"), iwrb.getLocalizedString("travel.search.maximum_rating","Maximum rating")},  new PresentationObject[] { min, max}, vertical);
			if ( !defined ) {
				bf.addInputLine(new String[]{iwrb.getLocalizedString("travel.search.type_of_accomodation","Type of accomodation")}, new PresentationObject[]{spHotelTypes}, vertical);
				bf.getCurrentBookingPart().mergeCells(1, bf.getCurrentBookingPartRow()-1, 2, bf.getCurrentBookingPartRow()-1);
				bf.addInputLine(new String[]{iwrb.getLocalizedString("travel.search.type_of_rooms","Type of rooms")}, new PresentationObject[]{roomTypeDrop}, vertical);
				bf.getCurrentBookingPart().mergeCells(1, bf.getCurrentBookingPartRow()-1, 2, bf.getCurrentBookingPartRow()-1);
			}
		}
	}

	protected Link getBookingLink(int productId) {
		Link link = super.getBookingLink(productId);
		link.maintainParameter(PARAMETER_TYPE, iwc);
		link.maintainParameter(PARAMETER_TYPE_COUNT, iwc);
		return link;
	}

	protected String getParameterTypeCountName() {
		return PARAMETER_TYPE_COUNT;
	}
	protected void setupSpecialFieldsForBookingForm(Table table, int row, List errorFields) {
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.travel.block.search.presentation.AbstractSearchForm#getBookingFieldsToErrorCheck()
	 */
	protected List getErrorFormFields() {
		return null;
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.travel.block.search.presentation.AbstractSearchForm#getParametersInUse()
	 */
	protected Collection getParametersInUse() {
		Vector coll = new Vector();
		coll.add(PARAMETER_HOTEL_TYPE);
		coll.add(PARAMETER_MAX_RATING);
		coll.add(PARAMETER_MIN_RATING);
		coll.add(PARAMETER_TYPE_COUNT);
		coll.add(PARAMETER_TYPE);
		return coll;
	}
	
	protected HotelHome getHotelHome() throws RemoteException {
		return (HotelHome) IDOLookup.getHome(Hotel.class);
	}

	protected Image getStar() {
		if (star == null) {
			star = bundle.getImage("images/rating_01.gif");
		}
		return star;
	}
	
	protected void addProductInfo(Product product, Table contentTable, int column, int row) {
		try {
			Hotel hotel = getHotelHome().findByPrimaryKey(product.getPrimaryKey());
			int rating = (int) hotel.getRating();
	
			for (int i = 1; i <= rating; i++) {
				contentTable.add(getStar(), column, row);
			}
			//contentTable.add(this.getLinkText(" |", false), column, row);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected Table getProductInfoDetailed(Product product) {
		try {
			Table table = new Table(2, 1);
			table.setCellpaddingAndCellspacing(0);
			Hotel hotel = getHotelHome().findByPrimaryKey(product.getPrimaryKey());
			int rating = (int) hotel.getRating();
			if (rating > 0) {
				table.add(getText(iwrb.getLocalizedString("travel.search.hotel.rating", "Rating")), 1, 1);
				table.setWidth(1, 1, 50);
				table.setCellpaddingBottom(1, 1, 5);
				//addProductInfo(product, table, 2, 1);
				for (int i = 1; i <= rating; i++) {
					table.add(getStar(), 2, 1);
				}
				return table;
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return null;
	}

}
