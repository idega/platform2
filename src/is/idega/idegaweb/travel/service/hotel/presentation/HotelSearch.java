package is.idega.idegaweb.travel.service.hotel.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.data.Product;
import com.idega.business.IBOLookup;
import com.idega.core.data.PostalCode;
import com.idega.core.data.PostalCodeHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;

import is.idega.idegaweb.travel.block.search.presentation.AbstractSearchForm;
import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import is.idega.idegaweb.travel.service.hotel.business.HotelBusiness;
import is.idega.idegaweb.travel.service.hotel.data.Hotel;
import is.idega.idegaweb.travel.service.hotel.data.HotelHome;
import is.idega.idegaweb.travel.service.hotel.data.RoomType;
import is.idega.idegaweb.travel.service.hotel.data.RoomTypeHome;

/**
 * @author gimmi
 */
public class HotelSearch extends AbstractSearchForm {

	private String PARAMETER_ROOM_TYPE = "hs_rt";
	private String PARAMETER_ROOM_TYPE_COUNT = "hs_rtc";

	
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
	
	protected void getResults() throws RemoteException {
		String sPostalCode[] = iwc.getParameterValues(PARAMETER_POSTAL_CODE_NAME);
		String sFromDate = iwc.getParameter(PARAMETER_FROM_DATE);
		String sToDate = iwc.getParameter(PARAMETER_TO_DATE);
		String sRoomType[] = iwc.getParameterValues(PARAMETER_ROOM_TYPE);
		String sRoomTypeCount[] = iwc.getParameterValues(PARAMETER_ROOM_TYPE_COUNT);
		
		try {
			Object[] roomTypeIds = null;
			if (sRoomType != null && sRoomType.length > 0) {
				roomTypeIds = new Object[sRoomType.length];
				for (int i = 0; i < roomTypeIds.length; i++) {
					//System.out.println("Adding roomtype to array roomTypeIds["+i+"] = "+roomTypeIds[i]+" ... "+i+" of "+sRoomType.length);
					roomTypeIds[i] = sRoomType[i];
				}
			}
			
			Object[] postalCodeIds = null;
			if (sPostalCode != null && sPostalCode.length > 0) {
				Vector ids = new Vector();
				PostalCodeHome pcHome = (PostalCodeHome) IDOLookup.getHome(PostalCode.class);
				PostalCode tpc;
				Collection pks;
				for (int i = 0 ; i < sPostalCode.length; i++) {
					//System.out.println("postalCodeLength = "+sPostalCode.length+" ... currently working with "+i);
					pks = pcHome.findByName(sPostalCode[i]);
					if (pks != null && !pks.isEmpty()) {
						Iterator iter = pks.iterator();
						while (iter.hasNext()) {
							//System.out.println("Adding postalCode to vector");
							ids.add(iter.next());
						}
					}
				}
				postalCodeIds = ids.toArray();
			}
			
			
			HotelHome hHome = (HotelHome) IDOLookup.getHome(Hotel.class);
			Collection coll = hHome.find(null, null, roomTypeIds, postalCodeIds);
			coll = checkResults(iwc, coll);
			listResults(iwc, coll);
			if (coll != null) {
				add("Found "+coll.size()+" matches !<br>");
				if (coll.isEmpty()) {
					STATE = 0;
					setupSearchForm();
				}
			} else {
				add(getText(iwrb.getLocalizedString("travel.search.no_matches","No matches"))+"<BR>");
				STATE = 0;
				setupSearchForm();
			}
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		
	}
	
	
	protected void setupSearchForm() {
		try {
			PostalCodeHome pch = (PostalCodeHome) IDOLookup.getHome(PostalCode.class);
			Collection coll = pch.findAllUniqueNames();
			DropdownMenu menu = new DropdownMenu(PARAMETER_POSTAL_CODE_NAME);
			if (coll != null && !coll.isEmpty()) {
				PostalCode pc;
				Iterator iter = coll.iterator();
				while (iter.hasNext()) {
					pc = pch.findByPrimaryKey(iter.next());
					menu.addMenuElement(pc.getName(), pc.getName());
				}
			}
			addInputLine(new String[]{iwrb.getLocalizedString("travel.search.city","City")}, new PresentationObject[]{menu});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		IWTimestamp now = IWTimestamp.RightNow();
		
		DateInput fromDate = new DateInput(PARAMETER_FROM_DATE);
		fromDate.setDate(now.getDate());
		now.addDays(1);
		DateInput toDate = new DateInput(PARAMETER_TO_DATE);
		toDate.setDate(now.getDate());
		addInputLine(new String[]{iwrb.getLocalizedString("travel.search.check_in","Check in"), iwrb.getLocalizedString("travel.search.check_out","Check out")}, new PresentationObject[]{fromDate, toDate});
		
		try {
			RoomTypeHome trh = (RoomTypeHome) IDOLookup.getHome(RoomType.class);
			Collection coll = trh.findAll();
			addInputLine(new String[]{iwrb.getLocalizedString("travel.search.type_of_rooms","Type of rooms"), iwrb.getLocalizedString("travel.search.number_of_rooms","Number of rooms")}, new PresentationObject[]{new DropdownMenu(coll, PARAMETER_ROOM_TYPE), getRoomTypeCountDropdown()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected Link getBookingLink(int productId) {
		Link link = super.getBookingLink(productId);
		link.maintainParameter(PARAMETER_ROOM_TYPE, iwc);
		link.maintainParameter(PARAMETER_ROOM_TYPE_COUNT, iwc);
		return link;
	}

	private DropdownMenu getRoomTypeCountDropdown() {
		DropdownMenu menu = new DropdownMenu(PARAMETER_ROOM_TYPE_COUNT);
		menu.addMenuElement(1, "1");
		menu.addMenuElement(2, "2");
		menu.addMenuElement(3, "3");
		menu.addMenuElement(4, "4");
		menu.addMenuElement(5, "5");
		menu.addMenuElement(6, "6");
		menu.addMenuElement(7, "7");
		
		return menu;
	}
	
}
