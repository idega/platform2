/*
 * Created on 6.8.2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package is.idega.idegaweb.travel.block.search.business;

import is.idega.idegaweb.travel.block.search.presentation.AbstractSearchForm;
import is.idega.idegaweb.travel.business.TravelSessionManager;
import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import is.idega.idegaweb.travel.service.business.ServiceHandler;
import is.idega.idegaweb.travel.service.presentation.BookingForm;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductHome;
import com.idega.block.trade.stockroom.data.ProductPrice;
import com.idega.business.IBOLookup;
import com.idega.business.IBOServiceBean;
import com.idega.core.data.PostalCode;
import com.idega.core.data.PostalCodeHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.util.IWTimestamp;

/**
 * @author root
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ServiceSearchBusinessBean extends IBOServiceBean implements ServiceSearchBusiness {

	private String PARAMETER_POSTAL_CODE_ICELAND = "post_ice";
	private String PARAMETER_POSTAL_CODE_REYKJAVIK = "post_rey";
	private String PARAMETER_POSTAL_CODE_REYKJAVIK_AREA = "post_reya";
	private String PARAMETER_POSTAL_CODE_WEST_ICELAND = "post_wice";
	private String PARAMETER_POSTAL_CODE_WEST_FJORDS = "post_fjo";
	private String PARAMETER_POSTAL_CODE_NORTH_WEST_ICELAND = "post_nwice";
	private String PARAMETER_POSTAL_CODE_NORTH_EAST_ICELAND = "post_neice";
	private String PARAMETER_POSTAL_CODE_EAST_ICELAND = "post_eice";
	private String PARAMETER_POSTAL_CODE_SOUTH_ICELAND = "post_sice";
	private String PARAMETER_POSTAL_CODE_WESTMAN_ISLANDS = "post_wmi";
	private String PARAMETER_POSTAL_CODE_SPACER = "post_space";
	
	public ServiceSearchBusinessBean() {
		super();
	}

	public DropdownMenu getPostalCodeDropdown(IWResourceBundle iwrb) throws RemoteException, FinderException {
		PostalCodeHome pch = (PostalCodeHome) IDOLookup.getHome(PostalCode.class);
		Collection coll = pch.findAllOrdererByCode();

		DropdownMenu menu = new DropdownMenu(AbstractSearchForm.PARAMETER_POSTAL_CODE_NAME);
		if (coll != null && !coll.isEmpty()) {
			PostalCode pc;
			Iterator iter = coll.iterator();
				
			menu.addMenuElement(PARAMETER_POSTAL_CODE_ICELAND, iwrb.getLocalizedString("travel.search.iceland", "Iceland"));
			menu.addMenuElement(PARAMETER_POSTAL_CODE_REYKJAVIK, iwrb.getLocalizedString("travel.search.reykjavik", "Reykjavik"));
			menu.addMenuElement(PARAMETER_POSTAL_CODE_REYKJAVIK_AREA, iwrb.getLocalizedString("travel.search.reykjavik_area", "Reykjav’k area"));
			menu.addMenuElement(PARAMETER_POSTAL_CODE_WEST_ICELAND, iwrb.getLocalizedString("travel.search.west_iceland", "West Iceland"));
			menu.addMenuElement(PARAMETER_POSTAL_CODE_WEST_FJORDS, iwrb.getLocalizedString("travel.search.westfjords", "Westfjords"));
			menu.addMenuElement(PARAMETER_POSTAL_CODE_NORTH_WEST_ICELAND, iwrb.getLocalizedString("travel.search.north_west_iceland", "North-west Iceland"));
			menu.addMenuElement(PARAMETER_POSTAL_CODE_NORTH_EAST_ICELAND, iwrb.getLocalizedString("travel.search.north_iceland", "North Iceland"));
			menu.addMenuElement(PARAMETER_POSTAL_CODE_EAST_ICELAND, iwrb.getLocalizedString("travel.search.east_iceland", "East Iceland"));
			menu.addMenuElement(PARAMETER_POSTAL_CODE_SOUTH_ICELAND, iwrb.getLocalizedString("travel.search.south_iceland", "South Iceland"));
			menu.addMenuElement(PARAMETER_POSTAL_CODE_WESTMAN_ISLANDS, iwrb.getLocalizedString("travel.search.westman_islands", "Westman islands"));
			menu.addMenuElement(PARAMETER_POSTAL_CODE_SPACER, "------------------------------");
				
			while (iter.hasNext()) {
				pc = (PostalCode) iter.next();
				menu.addMenuElement(pc.getPrimaryKey().toString(), pc.getPostalCode() + "  "+pc.getName());
			}
		}
		return menu;
	}
	
	public Object[] getPostalCodeIds(IWContext iwc) throws IDOLookupException, FinderException {
		String sPostalCode = iwc.getParameter(AbstractSearchForm.PARAMETER_POSTAL_CODE_NAME);
		Object[] postalCodeIds = null;

		if (sPostalCode != null) {
			Vector ids = new Vector();
			
			
			String from;
			String to;
			if ( sPostalCode.equals(PARAMETER_POSTAL_CODE_ICELAND) || sPostalCode.equals(PARAMETER_POSTAL_CODE_SPACER) ) {
				from = "100";
				to = "999";
			} else if (sPostalCode.equals(PARAMETER_POSTAL_CODE_REYKJAVIK)) {
				from = "100";
				to = "199";
			} else if (sPostalCode.equals(PARAMETER_POSTAL_CODE_REYKJAVIK_AREA)) {
				from = "100";
				to = "299";
			} else if (sPostalCode.equals(PARAMETER_POSTAL_CODE_WEST_ICELAND)) {
				from = "300";
				to = "399";
			} else if (sPostalCode.equals(PARAMETER_POSTAL_CODE_WEST_FJORDS)) {
				from = "400";
				to = "499";
			} else if (sPostalCode.equals(PARAMETER_POSTAL_CODE_NORTH_WEST_ICELAND)) {
				from = "500";
				to = "599";
			} else if (sPostalCode.equals(PARAMETER_POSTAL_CODE_NORTH_EAST_ICELAND)) {
				from = "600";
				to = "699";
			} else if (sPostalCode.equals(PARAMETER_POSTAL_CODE_EAST_ICELAND)) {
				from = "700";
				to = "799";
			} else if (sPostalCode.equals(PARAMETER_POSTAL_CODE_SOUTH_ICELAND)) {
				from = "800";
				to = "899";
			} else if (sPostalCode.equals(PARAMETER_POSTAL_CODE_WESTMAN_ISLANDS)) {
				from = "900";
				to = "999";
			} else {
				from = sPostalCode;
				to = null;
			}

			PostalCodeHome pcHome = (PostalCodeHome) IDOLookup.getHome(PostalCode.class);
			PostalCode tpc;
			Collection pks = null;
			if ( to == null ) {
				ids.add(new Integer(from));
			 	//pks = pcHome.findByPostalCode(from);
			} else {
				pks = pcHome.findByPostalCodeFromTo(from, to);
			}
			if (pks != null && !pks.isEmpty()) {
				Iterator iter = pks.iterator();
				while (iter.hasNext()) {
					ids.add(iter.next());
				}
			}
			
			/*			
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
			}*/
			
			postalCodeIds = ids.toArray();
		}
		return postalCodeIds;
	}

	public List getErrorFormFields(IWContext iwc) {
		List list = new Vector();
		String firstName = iwc.getParameter(AbstractSearchForm.PARAMETER_FIRST_NAME);
		String lastName = iwc.getParameter(AbstractSearchForm.PARAMETER_LAST_NAME);
		String street = iwc.getParameter(AbstractSearchForm.PARAMETER_STREET);
		String pc = iwc.getParameter(AbstractSearchForm.PARAMETER_POSTAL_CODE);
		String city = iwc.getParameter(AbstractSearchForm.PARAMETER_CITY);
		String country = iwc.getParameter(AbstractSearchForm.PARAMETER_COUNTRY);
		String email = iwc.getParameter(AbstractSearchForm.PARAMETER_EMAIL);
		String ccNum = iwc.getParameter(AbstractSearchForm.PARAMETER_CC_NUMBER);
		String ccMon = iwc.getParameter(AbstractSearchForm.PARAMETER_CC_MONTH);
		String ccYear = iwc.getParameter(AbstractSearchForm.PARAMETER_CC_YEAR);
		
		if (firstName == null || firstName.equals("")) {
			list.add(AbstractSearchForm.PARAMETER_FIRST_NAME);
		}
		if (lastName == null || lastName.equals("")) {
			list.add(AbstractSearchForm.PARAMETER_LAST_NAME);
		}
		if (street == null || street.equals("")) {
			list.add(AbstractSearchForm.PARAMETER_STREET);
		}
		if (pc == null || pc.equals("")) {
			list.add(AbstractSearchForm.PARAMETER_POSTAL_CODE);
		}
		if (city == null || city.equals("")) {
			list.add(AbstractSearchForm.PARAMETER_CITY);
		}
		if (country == null || country.equals("")) {
			list.add(AbstractSearchForm.PARAMETER_COUNTRY);
		}
		if (email == null || email.equals("")) {
			list.add(AbstractSearchForm.PARAMETER_EMAIL);
		}
		if (ccNum == null || ccNum.equals("")) {
			list.add(AbstractSearchForm.PARAMETER_CC_NUMBER);
		}
		if (ccMon == null || ccMon.equals("")) {
			list.add(AbstractSearchForm.PARAMETER_CC_MONTH);
		}
		if (ccYear == null || ccYear.equals("")) {
			list.add(AbstractSearchForm.PARAMETER_CC_YEAR);
		}
		String productId = iwc.getParameter(AbstractSearchForm.PARAMETER_PRODUCT_ID);
		
		ProductPrice[] pPrices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(Integer.parseInt(productId), -1, -1, true);
		int iMany = 0;
		for (int i = 0; i < pPrices.length; i++) {
		  try {
			iMany += Integer.parseInt(iwc.getParameter("priceCategory"+pPrices[i].getID()));
		  }catch (NumberFormatException n) {
		  	n.printStackTrace();
		  }
		}		
		
		if (iMany < 1) {
			list.add(AbstractSearchForm.ERROR_NO_BOOKING_COUNT);
		}
		
		return list;
	}

	public HashMap checkResults(IWContext iwc, Collection results) throws RemoteException {
		if (results != null && !results.isEmpty()) {
			HashMap map = new HashMap();
			Collection coll = new Vector();
			ProductHome pHome = (ProductHome) IDOLookup.getHome(Product.class);
			Product product;
			IWTimestamp from = null;
			IWTimestamp to = null;
			IWTimestamp tmp;
			try {
				from = new IWTimestamp(iwc.getParameter(AbstractSearchForm.PARAMETER_FROM_DATE));
				int betw = Integer.parseInt(iwc.getParameter(AbstractSearchForm.PARAMETER_MANY_DAYS));
				to = new IWTimestamp(from);
				to.addDays(betw);
				//to = new IWTimestamp(((IWContext) getIWApplicationContext()).getParameter(AbstractSearchForm.PARAMETER_TO_DATE));
			}catch (Exception e) {
				System.out.println("error getting stamps : "+e.getMessage());
				e.printStackTrace();
			}
			BookingForm bf;
			Iterator iter = results.iterator();
			boolean productIsValid = true;
			while (iter.hasNext() && from != null && to != null) {
				try {
					product = pHome.findByPrimaryKey(iter.next());
					bf = getServiceHandler().getBookingForm(iwc, product);
					//bus = getBusiness(product);
					tmp = new IWTimestamp(from);
					productIsValid = true;
					while ( tmp.isEarlierThan(to) && productIsValid) {
						productIsValid = (bf.checkBooking(iwc, false, true, true) >= 0);
						//productIsValid = bus.getIfDay(iwc, product, tmp);
						tmp.addDays(1);
					}
					if (productIsValid) {
						map.put(product.getPrimaryKey(), new Boolean(true));
//						coll.add(product.getPrimaryKey());
					} else {
						map.put(product.getPrimaryKey(), new Boolean(false));
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			return map;
		}
		return new HashMap();
	}


	public TravelSessionManager getTravelSessionManager(IWUserContext iwuc) throws RemoteException {
		return (TravelSessionManager) IBOLookup.getSessionInstance(iwuc, TravelSessionManager.class);
	}
	
	public TravelStockroomBusiness getBusiness(Product product) throws RemoteException, FinderException {
		return getServiceHandler().getServiceBusiness(product);
	}
	
	public ServiceHandler getServiceHandler() throws RemoteException {
		return (ServiceHandler) IBOLookup.getServiceInstance(getIWApplicationContext(), ServiceHandler.class);
	}

}
