package is.idega.idegaweb.travel.service.hotel.presentation;

import is.idega.idegaweb.travel.block.search.presentation.AbstractSearchForm;
import is.idega.idegaweb.travel.service.hotel.data.Hotel;
import is.idega.idegaweb.travel.service.hotel.data.HotelHome;
import is.idega.idegaweb.travel.service.hotel.data.RoomType;
import is.idega.idegaweb.travel.service.hotel.data.RoomTypeHome;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.data.PriceCategory;
import com.idega.block.trade.stockroom.data.PriceCategoryHome;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;

/**
 * @author gimmi
 */
public class HotelSearch extends AbstractSearchForm {

	protected String PARAMETER_TYPE_COUNT = HotelBookingForm.parameterCountToCheck;

	
	private IWContext iwc;


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
	
	protected void getResults() throws RemoteException {
		String sPostalCode[] = iwc.getParameterValues(PARAMETER_POSTAL_CODE_NAME);
		String sFromDate = iwc.getParameter(PARAMETER_FROM_DATE);
		String sManyDays = iwc.getParameter(PARAMETER_MANY_DAYS);
		String sRoomType[] = iwc.getParameterValues(PARAMETER_TYPE);
		String sRoomTypeCount[] = iwc.getParameterValues(PARAMETER_TYPE_COUNT);
		
		try {
			Object[] roomTypeIds = null;
			if (sRoomType != null && sRoomType.length > 0) {
				roomTypeIds = new Object[sRoomType.length];
				for (int i = 0; i < roomTypeIds.length; i++) {
					//System.out.println("Adding roomtype to array roomTypeIds["+i+"] = "+roomTypeIds[i]+" ... "+i+" of "+sRoomType.length);
					roomTypeIds[i] = sRoomType[i];
				}
			}
		
			Object[] postalCodeIds = getSearchBusiness(iwc).getPostalCodeIds(iwc);

			HotelHome hHome = (HotelHome) IDOLookup.getHome(Hotel.class);

			Collection coll = hHome.find(null, null, roomTypeIds, postalCodeIds);

			PriceCategoryHome pcHome = (PriceCategoryHome) IDOLookup.getHome(PriceCategory.class);
			PriceCategory priceCat = pcHome.findByKey(HotelSetup.HOTEL_SEARCH_PRICE_CATEGORY_KEY);
			IWTimestamp from = from = new IWTimestamp(iwc.getParameter(AbstractSearchForm.PARAMETER_FROM_DATE));

			System.out.println("Before : ");
			showCollectionContent(coll);
			coll = getSearchBusiness(iwc).sortProducts(coll, priceCat, new IWTimestamp(sFromDate));
			System.out.println("After : ");
			showCollectionContent(coll);

			HashMap map = getSearchBusiness(iwc).checkResults(iwc, coll);
			int mapSize = map.size();
			String foundString = "";
			if (map != null) {
				foundString = "Found "+mapSize+" match";
				if (mapSize != 1) foundString += "es !<br>";
			} else {
				foundString = getText(iwrb.getLocalizedString("travel.search.no_matches","No matches"))+"<BR>";
			}
			
			if (mapSize > 0) {
				add(foundString);
			}
			listResults(iwc, coll, map);
			add(foundString);

			if (coll != null) {
				if (coll.isEmpty()) {
					STATE = 0;
					setupSearchForm();
				}
			} else {
				STATE = 0;
				setupSearchForm();
			}
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		
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
	
	
	protected void setupSearchForm() {
		addAreaCodeInput();
		
		IWTimestamp now = IWTimestamp.RightNow();
		
		DateInput fromDate = new DateInput(PARAMETER_FROM_DATE);
		fromDate.setDate(now.getDate());
		now.addDays(1);
		TextInput manyDays = new TextInput(PARAMETER_MANY_DAYS);
		manyDays.setContent("1");
		manyDays.setSize(3);
		addInputLine(new String[]{iwrb.getLocalizedString("travel.search.check_in","Check in"), iwrb.getLocalizedString("travel.search.number_of_days","Number of days")}, new PresentationObject[]{fromDate, manyDays});
		
		try {
			RoomTypeHome trh = (RoomTypeHome) IDOLookup.getHome(RoomType.class);
			Collection coll = trh.findAll();
			addInputLine(new String[]{iwrb.getLocalizedString("travel.search.type_of_rooms","Type of rooms"), iwrb.getLocalizedString("travel.search.number_of_rooms","Number of rooms")}, new PresentationObject[]{new DropdownMenu(coll, PARAMETER_TYPE), getRoomTypeCountDropdown()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected Link getBookingLink(int productId) {
		Link link = super.getBookingLink(productId);
		link.maintainParameter(PARAMETER_TYPE, iwc);
		link.maintainParameter(PARAMETER_TYPE_COUNT, iwc);
		return link;
	}

	private DropdownMenu getRoomTypeCountDropdown() {
		DropdownMenu menu = new DropdownMenu(PARAMETER_TYPE_COUNT);
		menu.addMenuElement(1, "1");
		menu.addMenuElement(2, "2");
		menu.addMenuElement(3, "3");
		menu.addMenuElement(4, "4");
		menu.addMenuElement(5, "5");
		menu.addMenuElement(6, "6");
		menu.addMenuElement(7, "7");
		
		return menu;
	}
	
	protected String getParameterTypeCountName() {
		return PARAMETER_TYPE_COUNT;
	}
	

}
