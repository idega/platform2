package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import is.idega.idegaweb.travel.data.ServiceDay;
import is.idega.idegaweb.travel.data.ServiceDayBMPBean;
import is.idega.idegaweb.travel.data.ServiceDayHome;
import is.idega.idegaweb.travel.data.ServiceDayPK;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import com.idega.block.trade.data.Currency;
import com.idega.block.trade.data.CurrencyHome;
import com.idega.block.trade.stockroom.business.StockroomBusiness;
import com.idega.block.trade.stockroom.data.PriceCategory;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductDayInfoCount;
import com.idega.block.trade.stockroom.data.ProductDayInfoCountHome;
import com.idega.block.trade.stockroom.data.ProductHome;
import com.idega.block.trade.stockroom.data.ProductPrice;
import com.idega.block.trade.stockroom.data.ProductPriceHome;
import com.idega.block.trade.stockroom.data.Settings;
import com.idega.block.trade.stockroom.data.SupplyPool;
import com.idega.block.trade.stockroom.data.SupplyPoolDay;
import com.idega.block.trade.stockroom.data.SupplyPoolDayHome;
import com.idega.block.trade.stockroom.data.SupplyPoolDayPK;
import com.idega.block.trade.stockroom.data.SupplyPoolHome;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.block.trade.stockroom.data.TravelAddress;
import com.idega.business.IBOLookup;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOFinderException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.data.IDORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.CalendarParameters;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ServiceDaySetter extends TravelWindow {
	public static final String PARAMETER_SERVICE_ID = "sds_serv_id";
	private String ACTION = "sbs_action";
	private String ACTION_SAVE = "sbs_action_save";
	private String PARAMETER_UPDATE = "sbs_update";
	private String PARAMETER_AVAILABLE = "sbs_avail_";
	private String PARAMETER_MAX = "sbs_max_";
	private String PARAMETER_MIN = "sbs_min_";
	private String PARAMETER_ESTIMATED = "sbs_estimated_";
	private String PARAMETER_SUPPLY_POOL_ID = "sbs_pid";
	private String PARAMETER_DAY_EDITOR = "sbs_de";
	private String PARAMETER_SAVE_DAYS_INFO = "sbs_save_di"; 
	private String PARAMETER_COUNT = "sbs_count_";
	private String PARAMETER_PRICE = "sbs_price_";
	private String PARAMETER_SHOW_COUNT = "sbs_show_count";
	private String PARAMETER_SHOW_PRICE = "sbs_show_price";
	private String PARAMETER_DISPLAY_STATUS = "sbs_display_stat";
	private String PARAMETER_ADDRESS = "sbs_addrss";
	private String PARAMETER_TIMEFRAME = "sbs_timeframe";
	private String PARAMETER_PRICE_CATEGORY = "sbs_p_cat";
	private String PARAMETER_CURRENCY_ID = "sbs_currency_id";
	
	private IWResourceBundle _iwrb;
	private int _localeId;
	private Product _product;
	private ServiceDayHome _serviceDayHome;
	private SupplyPoolDayHome _supplyPoolDayHome;
	private IWCalendar _cal;
	private int[] _serviceDays;
	private int _textInputSize = 5;
	private SupplyPool pool = null;
	private IWTimestamp timeStamp = null;
	
	private TravelStockroomBusiness _tsBiz = null; 
	private StockroomBusiness _stockBiz = null;
	
	
	public ServiceDaySetter() {
		super.setWidth(700);
		super.setHeight(500);
		super.setTitle("idegaWeb Travel");
		super.setStatus(true);
		super.setResizable(true);
	}
	
	public void main(IWContext iwc) throws Exception{
		super.main(iwc);
		init(iwc);
		
		if (_product != null) {
			String actionSave = iwc.getParameter(ACTION_SAVE);
			if(actionSave != null && !actionSave.equals("")) {
				if(actionSave.equals(PARAMETER_SAVE_DAYS_INFO)) {
					if (timeStamp == null) {
						String day = iwc.getParameter(CalendarParameters.PARAMETER_DAY);
						String month = iwc.getParameter(CalendarParameters.PARAMETER_MONTH);
						String year = iwc.getParameter(CalendarParameters.PARAMETER_YEAR);

						if(month != null && !month.equals("") &&
								day != null && !day.equals("") &&
								year != null && !year.equals("")) {
							timeStamp = getTimestamp(day,month,year);
						}
						else {
							timeStamp = IWTimestamp.RightNow();
						}
					}	
					saveDayInfo(iwc, timeStamp);
				}
			}
			String action = iwc.getParameter(ACTION);
			if(action == null || action.equals("")) {
				drawForm(iwc);
			}
			else if (action.equals(PARAMETER_UPDATE)) {
				executeUpdate(iwc);
				drawForm(iwc);
			}
			else if(action.equals(PARAMETER_DAY_EDITOR)) {
				if (timeStamp == null) {
					String day = iwc.getParameter(CalendarParameters.PARAMETER_DAY);
					String month = iwc.getParameter(CalendarParameters.PARAMETER_MONTH);
					String year = iwc.getParameter(CalendarParameters.PARAMETER_YEAR);

					if(month != null && !month.equals("") &&
							day != null && !day.equals("") &&
							year != null && !year.equals("")) {
						timeStamp = getTimestamp(day,month,year);
					}
					else {
						timeStamp = IWTimestamp.RightNow();
					}
				}	
				drawDayEditorForm(iwc, timeStamp);
			}
			else {
				noProduct();
			}
		}
	}
	
	private void init(IWContext iwc) {
		_iwrb = super.getResourceBundle(iwc);
		_localeId = iwc.getCurrentLocaleId();
		_cal = new IWCalendar();
		String serviceId = iwc.getParameter(PARAMETER_SERVICE_ID);
		
		try {
			_tsBiz = getTravelStockroomBusiness(iwc);
		}
		catch (RemoteException e1) {
			e1.printStackTrace();
		}
		
		try {
			if (serviceId != null) {
				ProductHome productHome = (ProductHome)IDOLookup.getHome(Product.class);
				_serviceDayHome = (ServiceDayHome)IDOLookup.getHome(ServiceDay.class);
				_supplyPoolDayHome = (SupplyPoolDayHome) IDOLookup.getHome(SupplyPoolDay.class);
				_product = productHome.findByPrimaryKey(new Integer(serviceId));
				
				try {
					if (getServiceHandler(iwc).getServiceBusiness(_product).supportsSupplyPool()) {
						String sPoolID = iwc.getParameter(PARAMETER_SUPPLY_POOL_ID);
						SupplyPoolHome poolHome = (SupplyPoolHome) IDOLookup.getHome(SupplyPool.class);
						if (sPoolID == null) {
							pool = poolHome.findByProduct(_product);
						} else if ("-1".equals(sPoolID)) {
							pool = null;
						} else {
							pool = poolHome.findByPrimaryKey(new Integer(sPoolID));
						}
					}
				} catch (FinderException e) {
					pool = null;
					log("SupplyPool not used for product "+_product.getProductName(iwc.getCurrentLocaleId()));
				} 
				
				_serviceDays = _serviceDayHome.getDaysOfWeek(Integer.parseInt(serviceId));
			}
		}catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
	
	private void executeUpdate(IWContext iwc) {
		String avail;
		String max;
		String min;
		String est;
		int iMax;
		int iMin;
		int iEst;
		
		String sPoolID = iwc.getParameter(PARAMETER_SUPPLY_POOL_ID);
		System.out.println(sPoolID); 
		if (sPoolID != null && !"-1".equals(sPoolID)) {
			try {
				_product.removeAllFrom(SupplyPool.class);
			}
			catch (IDORemoveRelationshipException e1) {
				e1.printStackTrace();
			}
			try {
				pool.addProduct(_product);
			}
			catch (IDOAddRelationshipException e) {
				log("Can not add product to pool, possibly already added");
//				e.printStackTrace();
			}
		} else {
			try {
				SupplyPoolHome poolHome = (SupplyPoolHome) IDOLookup.getHome(SupplyPool.class);
				pool = poolHome.findByProduct(_product);
				pool.removeProduct(_product);
				pool = null;
			}
			catch (IDORelationshipException e1) {
				e1.printStackTrace();
			}
			catch (FinderException e1) {
				log("Can not find a pool for product");
//				e1.printStackTrace();
			}
			catch (IDOLookupException e) {
				e.printStackTrace();
			}
			
			try {
				_serviceDayHome.setServiceWithNoDays(_product.getID());
				ServiceDay sDay;
				
				for (int i = ServiceDayBMPBean.SUNDAY; i <= ServiceDayBMPBean.SATURDAY; i++) {
					avail = iwc.getParameter(PARAMETER_AVAILABLE+i);
					max = iwc.getParameter(PARAMETER_MAX+i);
					min = iwc.getParameter(PARAMETER_MIN+i);
					est = iwc.getParameter(PARAMETER_ESTIMATED+i);
					try {
						iMax = Integer.parseInt(max);
					}catch (NumberFormatException n) {
						iMax = -1;
					}
					try {
						iMin = Integer.parseInt(min);
					}catch (NumberFormatException n) {
						iMin = -1;
					}
					try {
						iEst = Integer.parseInt(est);
					}catch (NumberFormatException n) {
						iEst = -1;
					}
					
					if (avail != null) {
						sDay = _serviceDayHome.create(new ServiceDayPK(_product.getPrimaryKey(), new Integer(i)));
						sDay.setDayOfWeek(_product.getID(), i, iMax, iMin, iEst);
						sDay.store();
					}
				}
				
				_serviceDays = _serviceDayHome.getDaysOfWeek(_product.getID());
				
			}catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
	}
	
	private void saveDayInfo(IWContext iwc, IWTimestamp stamp) {
		IWCalendar calendar = new IWCalendar();
		int daycount = calendar.getLengthOfMonth(stamp.getMonth(),stamp.getYear());
		int n = 1; 
		while(n <= daycount) {
			String countString = iwc.getParameter(PARAMETER_COUNT + n);
			String priceString = iwc.getParameter(PARAMETER_PRICE + n);
			IWTimestamp date = new IWTimestamp(n, stamp.getMonth(), stamp.getYear());
			if(countString != null && !countString.trim().equals("")) {
				int count = Integer.parseInt(countString);
				ProductDayInfoCount productCount = null;
				try {
					productCount = getProductDayInfoCountHome().findByProductIdAndDate(_product.getID(), date.getDate());
				} catch (FinderException e1) {
					try {
						productCount = getProductDayInfoCountHome().create();
					}
					catch (CreateException e2) {
						e2.printStackTrace();
					}
				}
				productCount.setProductId(_product.getID());
				productCount.setCount(count);
				productCount.setDate(date.getDate());
				productCount.store();
			}
			if(priceString != null && !priceString.trim().equals("")) {
				float price = Float.parseFloat(priceString);
				ProductPrice productPrice = null;
				String timeframeIdString = iwc.getParameter(PARAMETER_TIMEFRAME);
				String travelAddressIdString = iwc.getParameter(PARAMETER_ADDRESS);
				String priceCategoryIdString = iwc.getParameter(PARAMETER_PRICE_CATEGORY);
				String currencyIdString = iwc.getParameter(PARAMETER_CURRENCY_ID);
				int timeframeId = -1;
				int addressId = -1; 
				int priceCategoryId = -1;
				int currencyId = -1;
				if(timeframeIdString != null && !timeframeIdString.equals("")) {
					timeframeId = Integer.parseInt(timeframeIdString);
				}
				if(travelAddressIdString != null && !travelAddressIdString.equals("")) {
					addressId = Integer.parseInt(travelAddressIdString);
				}
				if(priceCategoryIdString != null && !priceCategoryIdString.equals("")) {
					priceCategoryId = Integer.parseInt(priceCategoryIdString);
				}
				if(currencyIdString != null && !currencyIdString.equals("")) {
					currencyId = Integer.parseInt(currencyIdString);
				}
				try {
					productPrice = getProductPriceHome().findByData(_product.getID(), timeframeId, addressId, currencyId, priceCategoryId, date.getDate());

				}
				catch (FinderException e) {
					//e.printStackTrace();
					try {
						productPrice = getProductPriceHome().create();
					}
					catch (CreateException e1) {
						e1.printStackTrace();
					}
				}
				if(productPrice != null) {
					productPrice.setProductId(_product.getID());
					productPrice.setCurrencyId(currencyId);
					productPrice.setExactDate(date.getDate());
					productPrice.setPrice(price);
					if(priceCategoryId != -1) {
						productPrice.setPriceCategoryID(priceCategoryId);
					}
					productPrice.store();
					try {
						if(timeframeId != -1) {
							productPrice.addTo(Timeframe.class, timeframeId); 
						}
					}catch (SQLException e1) {
//						e1.printStackTrace();
					}
					try {
						if(addressId != -1) {
							productPrice.addTo(TravelAddress.class, addressId);
						}
						
					}
					catch (SQLException e1) {
//						e1.printStackTrace();
					}
				}
			}
			n++;
		}
	}
	
	private void noProduct() {
		add(getText(_iwrb.getLocalizedString("travel.no_product_selected","No product selected")));
	}
	
	private void drawForm(IWContext iwc) throws RemoteException{
		Form form = new Form();
		Table headerTable = TravelManager.getTable();
		headerTable.setAlignment("center");
		Table table = TravelManager.getTable();
		form.add(headerTable);
		form.add(table);
		form.maintainParameter(PARAMETER_SERVICE_ID);
		if (getBusiness(iwc, _product).supportsSupplyPool()) {
			DropdownMenu pools = new DropdownMenu(PARAMETER_SUPPLY_POOL_ID);
			pools.addMenuElement("-1", iwrb.getLocalizedString("do_not_use_supply_pool", "Do no use supply pool"));
			pools.setToSubmit();
			try {
				SupplyPoolHome poolHome = (SupplyPoolHome) IDOLookup.getHome(SupplyPool.class);
				Collection poolColl = poolHome.findBySupplier(super.getTravelSessionManager(iwc).getSupplier());
				pools.addMenuElements(poolColl);
				if (poolColl != null && !poolColl.isEmpty()) {
					headerTable.add(pools);
				}
				if (pool != null) {
					pools.setSelectedElement(pool.getPrimaryKey().toString());
				} else {
					pools.setSelectedElement("-1");
				}
			}
			catch (FinderException e) {
				e.printStackTrace();
		}
		}
		
		
		int row = 1;
		table.add(getTextHeader(_iwrb.getLocalizedString("travel.day","Day")), 1, row);
		table.add(getTextHeader(_iwrb.getLocalizedString("travel.available","Available")), 2, row);
		table.add(getTextHeader(_iwrb.getLocalizedString("travel.max","Max")), 3, row);
		table.add(getTextHeader(_iwrb.getLocalizedString("travel.min","Min")), 4, row);
		table.add(getTextHeader(_iwrb.getLocalizedString("travel.estimated","Estimated")), 5, row);
		table.setRowColor(row, TravelManager.backgroundColor);
		
		int arrayIndex = 0;
		if (_serviceDays.length == 0) {
			arrayIndex = -1;
		}
		
		CheckBox avail;
		TextInput max;
		TextInput min;
		TextInput estimated;
		int iMax = -1;
		int iMin = -1;
		int iEst = -1;
		SupplyPoolDay poolDay;
		ServiceDay sDay;
		
		for (int i = ServiceDayBMPBean.SUNDAY; i <= ServiceDayBMPBean.SATURDAY; i++) {
			++row;
			
			avail = new CheckBox(PARAMETER_AVAILABLE+i);
			max = new TextInput(PARAMETER_MAX+i);
			min = new TextInput(PARAMETER_MIN+i);
			estimated = new TextInput(PARAMETER_ESTIMATED+i);
			max.setSize(_textInputSize);
			min.setSize(_textInputSize);
			estimated.setSize(_textInputSize);
			
			if (pool == null) {
				for (int j = 0; j < _serviceDays.length; j++) {
					if (_serviceDays[j] == i) {
						try {
							sDay = _serviceDayHome.findByServiceAndDay(this._product.getID(), i);
							avail.setChecked(true);
							iMax = sDay.getMax();
							iMin = sDay.getMin();
							iEst = sDay.getEstimated();
							if (iMax != -1) {
								max.setContent(Integer.toString(iMax));
							}
							if (iMin != -1) {
								min.setContent(Integer.toString(iMin));
							}
							if (iEst != -1) {
								estimated.setContent(Integer.toString(iEst));
							}
							
							break;
						}catch (FinderException f) {
							f.printStackTrace();
						}
					}
				}
			} else {
				avail.setDisabled(true);
				max.setDisabled(true);
				min.setDisabled(true);
				estimated.setDisabled(true);

				try {
					poolDay = _supplyPoolDayHome.findByPrimaryKey(new SupplyPoolDayPK(pool.getPrimaryKey(), new Integer(i)));
					avail.setChecked(true);
					iMax = poolDay.getMax();
					iMin = poolDay.getMin();
					iEst = poolDay.getEstimated();
					if (iMax != -1) {
						max.setContent(Integer.toString(iMax));
					}
					if (iMin != -1) {
						min.setContent(Integer.toString(iMin));
					}
					if (iEst != -1) {
						estimated.setContent(Integer.toString(iEst));
					}
				}
				catch (FinderException e1) {
					//e1.printStackTrace();
				}
			}
			
			
			
			table.add(getText(_cal.getDayName(i, iwc.getCurrentLocale(),IWCalendar.LONG)), 1, row);
			table.add(avail, 2, row);
			table.add(max, 3, row);
			table.add(min, 4, row);
			table.add(estimated, 5, row);
			
			table.setRowColor(row, TravelManager.GRAY);
		}
		
		Link moreLink = new Link(iwrb.getLocalizedImageButton("travel.day_by_day", "Day by day"));
		moreLink.addParameter(ACTION, PARAMETER_DAY_EDITOR);
		moreLink.addParameter(PARAMETER_SERVICE_ID, _product.getPrimaryKey().toString());
		moreLink.addParameter(PARAMETER_DISPLAY_STATUS, "-1");
		if(pool != null) {
			moreLink.addParameter(PARAMETER_SUPPLY_POOL_ID, pool.getPrimaryKey().toString());
		}
		else {
			moreLink.addParameter(PARAMETER_SUPPLY_POOL_ID, "-1");
		}

		++row;
		table.mergeCells(1, row, 5, row);
		table.setRowColor(row, TravelManager.GRAY);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		//to get the day-by-day form, the more link has to be commited in
//		table.add(moreLink, 1, row);
		table.add(Text.BREAK);
		table.add(new SubmitButton(_iwrb.getLocalizedImageButton("travel.update","Update"), ACTION, PARAMETER_UPDATE), 1, row);
		
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
		table.setAlignment("center");
		add(Text.NON_BREAKING_SPACE);
		add(form);
	}
	
	private void drawDayEditorForm(IWContext iwc, IWTimestamp stamp)throws RemoteException{
		Form form = new Form();
		IWCalendar calendar = new IWCalendar();
		Table table = new Table();
		table.setWidth(500);
		table.setColor(TravelManager.WHITE);
		table.setAlignment(Table.HORIZONTAL_ALIGN_CENTER);
		table.setCellspacing(1);
		table.setCellpadding(0);
		form.maintainParameter(CalendarParameters.PARAMETER_DAY);
		form.maintainParameter(CalendarParameters.PARAMETER_MONTH);
		form.maintainParameter(CalendarParameters.PARAMETER_YEAR);
		form.maintainParameter(PARAMETER_SERVICE_ID);
		form.maintainParameter(PARAMETER_SUPPLY_POOL_ID);
		form.maintainParameter(PARAMETER_DISPLAY_STATUS);
		form.addParameter(ACTION, PARAMETER_DAY_EDITOR);
		form.add(table);
		add(form);
		
		DropdownMenu displayStatus = new DropdownMenu(PARAMETER_DISPLAY_STATUS);
		displayStatus.setToSubmit();
		displayStatus.addMenuElement("-1",iwrb.getLocalizedString("travel.count_and_price","Count and price"));
		displayStatus.addMenuElement(PARAMETER_SHOW_COUNT, iwrb.getLocalizedString("travel.show_count", "Count"));
		displayStatus.addMenuElement(PARAMETER_SHOW_PRICE, iwrb.getLocalizedString("travel.show_price", "Price"));	
//		int dropdownTableRow = 1;
		int dropdownTableColumn = 1;
		Table dropdownTable = new Table();
		dropdownTable.setCellpadding(0);
		dropdownTable.setCellspacing(1);
		dropdownTable.setWidth(Table.HUNDRED_PERCENT);
		dropdownTable.setRowColor(1, TravelManager.backgroundColor);
		dropdownTable.add(getTextHeader(iwrb.getLocalizedString("view", "View")),dropdownTableColumn,1);
		dropdownTable.setRowColor(2, TravelManager.GRAY);
		dropdownTable.add(displayStatus, dropdownTableColumn++, 2);

		Timeframe[] timeframes = null;
		try {
			timeframes = _product.getTimeframes();
		}
		catch (SQLException e2) {
			e2.printStackTrace();
		}
		List arrivalAddresses = null;
		try {
			arrivalAddresses = _product.getDepartureAddresses(true);
		}
		catch (IDOFinderException e3) {
			e3.printStackTrace();
		}
		int supplierId = _product.getSupplierId();
	
		if(iwc.getParameter(PARAMETER_DISPLAY_STATUS) != null && iwc.getParameter(PARAMETER_DISPLAY_STATUS).equals(PARAMETER_SHOW_COUNT)) {
			displayStatus.setSelectedElement(PARAMETER_SHOW_COUNT);
		}
		else if(iwc.getParameter(PARAMETER_DISPLAY_STATUS) != null) {
			if(iwc.getParameter(PARAMETER_DISPLAY_STATUS).equals(PARAMETER_SHOW_PRICE)) {
				displayStatus.setSelectedElement(PARAMETER_SHOW_PRICE);
			}
			 if(iwc.getParameter(PARAMETER_DISPLAY_STATUS).equals(PARAMETER_SHOW_PRICE) || iwc.getParameter(PARAMETER_DISPLAY_STATUS).equals("-1")) {
				if(timeframes != null) {
					DropdownMenu timeframeDD = new DropdownMenu(PARAMETER_TIMEFRAME);
					for(int i = 0; i<timeframes.length; i++) {
						timeframeDD.addMenuElement(timeframes[i].getPrimaryKey().toString(),timeframes[i].getName(iwc.getCurrentLocale()));		
					}
					String timeframeId = iwc.getParameter(PARAMETER_TIMEFRAME);
					if(timeframeId != null && !timeframeId.equals("")) {
						timeframeDD.setSelectedElement(timeframeId);
						form.addParameter(PARAMETER_TIMEFRAME, timeframeId);
					}
					else {
						form.addParameter(PARAMETER_TIMEFRAME, timeframes[0].getPrimaryKey().toString());
					}
					dropdownTable.setColor(dropdownTableColumn, 1, TravelManager.backgroundColor);
					dropdownTable.add(getTextHeader(iwrb.getLocalizedString("timeframe", "Timeframe")), dropdownTableColumn, 1);
					dropdownTable.setRowColor(2, TravelManager.GRAY);
					dropdownTable.add(timeframeDD, dropdownTableColumn++, 2);
				}
				if(arrivalAddresses != null && !arrivalAddresses.isEmpty()) {
					DropdownMenu addressDD = new DropdownMenu(PARAMETER_ADDRESS);
					Iterator addressIter = arrivalAddresses.iterator();
					while(addressIter.hasNext()) {
						TravelAddress address = (TravelAddress) addressIter.next();
						addressDD.addMenuElement(address.getPrimaryKey().toString(),address.getName());
					}
					dropdownTable.setColor(dropdownTableColumn, 1, TravelManager.backgroundColor);
					dropdownTable.add(getTextHeader(iwrb.getLocalizedString("travel_address", "Travel address")), dropdownTableColumn, 1);
					dropdownTable.setRowColor(2, TravelManager.GRAY);
					dropdownTable.add(addressDD, dropdownTableColumn++, 2);
					String addressId = iwc.getParameter(PARAMETER_ADDRESS);
					if(addressId != null && !addressId.equals("")) {
						addressDD.setSelectedElement(addressId);
						form.addParameter(PARAMETER_ADDRESS, addressId);
					}
				}
				PriceCategory[] priceCategories = _tsBiz.getPriceCategories(supplierId);
				if(priceCategories != null) {
					DropdownMenu pCategoriesDD = new DropdownMenu(PARAMETER_PRICE_CATEGORY);
					for (int i = 0; i < priceCategories.length; i++) {
						pCategoriesDD.addMenuElement(priceCategories[i].getPrimaryKey().toString(), priceCategories[i].getName());
					}
					dropdownTable.setColor(dropdownTableColumn, 1, TravelManager.backgroundColor);
					dropdownTable.add(getTextHeader(iwrb.getLocalizedString("category", "Price category")), dropdownTableColumn, 1);
					dropdownTable.setRowColor(2, TravelManager.GRAY);
					dropdownTable.add(pCategoriesDD, dropdownTableColumn, 2);
					String priceCategoryId = iwc.getParameter(PARAMETER_PRICE_CATEGORY);
					if(priceCategoryId != null && !priceCategoryId.equals("")) {
						pCategoriesDD.setSelectedElement(priceCategoryId);
						form.addParameter(PARAMETER_PRICE_CATEGORY, priceCategoryId);
					}
					else {
						form.addParameter(PARAMETER_PRICE_CATEGORY, priceCategories[0].getPrimaryKey().toString());
					}
				}
				dropdownTable.mergeCells(2, 3, dropdownTableColumn, 3);
				dropdownTable.setRowColor(3, TravelManager.GRAY);
				dropdownTable.setAlignment(2,3,Table.HORIZONTAL_ALIGN_RIGHT);
				dropdownTable.add(new SubmitButton(iwrb.getLocalizedImageButton("go", "Go")), 2, 3);
			}
		}
		
		int daycount = calendar.getLengthOfMonth(stamp.getMonth(),stamp.getYear());
		int column = calendar.getDayOfWeek(stamp.getYear(),stamp.getMonth(),1);	
		int row = 1;
		int n = 1;
		table.add(Text.BREAK, 1,1);
		table.mergeCells(1, row, 7, row);
		table.add(dropdownTable, 1, row);
		table.add(Text.BREAK, 1, row++);
		table.mergeCells(1, row, 7, row);
		table.setRowColor(row, TravelManager.backgroundColor);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_CENTER);
		table.add(getLastMonthsLink(iwc, iwrb, stamp), 1, row);
		table.add(Text.NON_BREAKING_SPACE, 1, row);
		table.add(getTextHeader(stamp.getDateString("MMMMMMMM yyyy",iwc.getCurrentLocale())), 1, row); 
		table.add(Text.NON_BREAKING_SPACE, 1, row);
		table.add(getNextMonthsLink(iwc, iwrb, stamp), 1, row++); 
		
		for (int i = ServiceDayBMPBean.SUNDAY; i <= ServiceDayBMPBean.SATURDAY; i++) {
			table.add(getTextHeader(calendar.getDayName(i, iwc.getCurrentLocale(),IWCalendar.SHORT)), i, row);
			table.setAlignment(i, row, Table.HORIZONTAL_ALIGN_CENTER);
			table.setRowColor(row, TravelManager.backgroundColor);
			SupplyPoolDay poolDay = null;
			int max = -1;
			try {
				if(pool != null) {
					poolDay = _supplyPoolDayHome.findByPrimaryKey(new SupplyPoolDayPK(pool.getPrimaryKey(), new Integer(i)));
				  if (poolDay.getMax() > -1) {
				  		max = poolDay.getMax();
				  }
				}
			}
			catch (EJBException e1) {
				e1.printStackTrace();
			}
			catch (FinderException e1) {
				e1.printStackTrace();
			}
			if(max != -1 && iwc.getParameter(PARAMETER_DISPLAY_STATUS) != null && !iwc.getParameter(PARAMETER_DISPLAY_STATUS).equals(PARAMETER_SHOW_PRICE)) {
				table.add(getTextHeader(" (" + max +")"), i, row);
			}
		}
		int currencyId = -1;
		Currency currency = null;
		CurrencyHome cHome = (CurrencyHome) IDOLookup.getHome(Currency.class);
		Settings settings = null;
		try {
			if(_product.getSupplier() != null) {
				settings = _product.getSupplier().getSettings();
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		catch (CreateException e) {
			e.printStackTrace();
		}
		if(settings != null) {
			currencyId = settings.getCurrencyId();
			if(currencyId != -1) {
				try {
					currency = cHome.findByPrimaryKey(currencyId);
				}
				catch (FinderException e1) {
					currency = null;
					e1.printStackTrace();
				}
				form.addParameter(PARAMETER_CURRENCY_ID, currencyId);
			}
		}
		String timeframeIdString = iwc.getParameter(PARAMETER_TIMEFRAME);
		String addressIdString = iwc.getParameter(PARAMETER_ADDRESS);
		String categoryIdString = iwc.getParameter(PARAMETER_PRICE_CATEGORY);
		int timeframeId = -1;
		int addressId = -1;
		int categoryId = -1;
		if(timeframeIdString != null && !timeframeIdString.equals("")) {
			timeframeId = Integer.parseInt(timeframeIdString);
		}
		if(addressIdString != null && !addressIdString.equals("")) {
			addressId = Integer.parseInt(addressIdString);
		}
		if(categoryIdString != null && !categoryIdString.equals("")) {
			categoryId = Integer.parseInt(categoryIdString);
		}
		Timeframe tFrame = getProductBusiness(iwc).getTimeframe(_product, stamp, addressId);
    row++;
    if(getStockroomBusiness(iwc).isInTimeframe( new IWTimestamp(tFrame.getFrom()) , new IWTimestamp(tFrame.getTo()), stamp, tFrame.getIfYearly() )) {
	  		while(n <= daycount) {
	  			Table dayTable = new Table();
	  			dayTable.setWidth(Table.HUNDRED_PERCENT);
	  			int dayTableRow = 1;
	  			IWTimestamp date = new IWTimestamp(n, stamp.getMonth(), stamp.getYear());
	  			TextInput countInput = new TextInput(PARAMETER_COUNT + n);
	  			countInput.setLength(4);
	  			ProductDayInfoCount productCount = null;
	  			try {
	  				productCount = getProductDayInfoCountHome().findByProductIdAndDate(_product.getID(), date.getDate());
	  			} catch (FinderException e1) {
	  				productCount = null;
	  			}
	  			if(productCount != null) {
	  				countInput.setContent(Integer.toString(productCount.getCount()));
	  			}
	  			
	  			TextInput priceInput = new TextInput(PARAMETER_PRICE + n);
	  			priceInput.setLength(5);
	  			ProductPrice productPrice = null;
	  			try {
	  				productPrice = getProductPriceHome().findByData(_product.getID(), timeframeId, addressId, currencyId, categoryId, date.getDate());
	  			}
	  			catch (FinderException e4) {
	  				productPrice = null;
	  			}
	  			if(productPrice != null) {
	  				priceInput.setContent(Float.toString(productPrice.getPrice()));
	  			}
	  			dayTable.setAlignment(2, dayTableRow, Table.HORIZONTAL_ALIGN_RIGHT);
	  			dayTable.add(String.valueOf(n),2,dayTableRow++);
	  			
	  			if(iwc.getParameter(PARAMETER_SUPPLY_POOL_ID) != null && !iwc.getParameter(PARAMETER_SUPPLY_POOL_ID).equals("-1")) {
	  				countInput.setDisabled(true);
	  			}
	  			if(iwc.getParameter(PARAMETER_DISPLAY_STATUS) != null && iwc.getParameter(PARAMETER_DISPLAY_STATUS).equals(PARAMETER_SHOW_COUNT)) {
	  				displayStatus.setSelectedElement(PARAMETER_SHOW_COUNT);
	  				dayTable.add(countInput,1,dayTableRow++);
	  			}
	  			else if(iwc.getParameter(PARAMETER_DISPLAY_STATUS) != null && iwc.getParameter(PARAMETER_DISPLAY_STATUS).equals(PARAMETER_SHOW_PRICE)) {
	  				displayStatus.setSelectedElement(PARAMETER_SHOW_PRICE);
	  				dayTable.add(priceInput,1,dayTableRow++);
	  				if(currency != null) {
	  					dayTable.add(currency.getCurrencyAbbreviation(), 1, dayTableRow++);
	  				}
	  			}
	  			else {
	  				dayTable.add(countInput,1,dayTableRow++);
	  				dayTable.add(priceInput,1,dayTableRow);
	  				if(currency != null) {
	  					dayTable.add(currency.getCurrencyAbbreviation(), 1, dayTableRow);
	  				}
	  			}
	  			table.setRowColor(row, TravelManager.GRAY);
	  			table.add(dayTable, column, row);
	  			column = column % 7 + 1;
	  			if (column == 1)
	  				row++;
	  			n++;
	  		}
    }
    else {
    		table.setRowColor(row, TravelManager.GRAY);
    		table.mergeCells(1,row,7,row);
    		table.setAlignment(1,row,Table.HORIZONTAL_ALIGN_CENTER);
    		table.setHeight(1,row,22);
    		Text t = new Text(iwrb.getLocalizedString("timeframe_does_not_fit_month", "The chosen timeframe does not fit the chosen month, please choose a different timeframe or a different month."));
			t.setBold();
    		table.add(t, 1, row);
		}

		
		SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("save", "Save"), ACTION_SAVE, PARAMETER_SAVE_DAYS_INFO);
		SubmitButton back = new SubmitButton(iwrb.getLocalizedImageButton("back", "Back"));
		
		table.setRowColor(++row, TravelManager.GRAY);
		table.add(back, 1, row);
		table.mergeCells(2, row, 7, row);
		table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add(save, 2, row);

	}
	private Link getNextMonthsLink(IWContext iwc, IWResourceBundle iwrb, IWTimestamp idts) {
		Link L = new Link(getTextHeader(iwrb.getLocalizedString("right", "-->")));
		L.addParameter(ACTION, PARAMETER_DAY_EDITOR);
		L.addParameter(PARAMETER_SERVICE_ID, _product.getPrimaryKey().toString());
		L.addParameter(InitialData.dropdownView,InitialData.PARAMETER_SUPPLY_POOL);
		L.addParameter(PARAMETER_ADDRESS, iwc.getParameter(PARAMETER_ADDRESS));
		L.addParameter(PARAMETER_PRICE_CATEGORY, iwc.getParameter(PARAMETER_PRICE_CATEGORY));
		L.addParameter(PARAMETER_TIMEFRAME, iwc.getParameter(PARAMETER_TIMEFRAME));
		L.addParameter(PARAMETER_DISPLAY_STATUS, iwc.getParameter(PARAMETER_DISPLAY_STATUS));
		if (idts.getMonth() == 12) {
			L.addParameter(CalendarParameters.PARAMETER_DAY, idts.getDay());
			L.addParameter(CalendarParameters.PARAMETER_MONTH, String.valueOf(1));
			L.addParameter(CalendarParameters.PARAMETER_YEAR, String.valueOf(idts.getYear() + 1));
		}
		else {
			L.addParameter(CalendarParameters.PARAMETER_DAY, idts.getDay());
			L.addParameter(CalendarParameters.PARAMETER_MONTH, String.valueOf(idts.getMonth() + 1));
			L.addParameter(CalendarParameters.PARAMETER_YEAR, String.valueOf(idts.getYear()));
		}
		return L;
	}
	private Link getLastMonthsLink(IWContext iwc, IWResourceBundle iwrb, IWTimestamp idts) {
		Link L = new Link(getTextHeader(iwrb.getLocalizedString("left", "<--")));
		L.addParameter(ACTION, PARAMETER_DAY_EDITOR);
		L.addParameter(PARAMETER_SERVICE_ID, _product.getPrimaryKey().toString());
		L.addParameter(InitialData.dropdownView,InitialData.PARAMETER_SUPPLY_POOL);
		L.addParameter(PARAMETER_ADDRESS, iwc.getParameter(PARAMETER_ADDRESS));
		L.addParameter(PARAMETER_PRICE_CATEGORY, iwc.getParameter(PARAMETER_PRICE_CATEGORY));
		L.addParameter(PARAMETER_TIMEFRAME, iwc.getParameter(PARAMETER_TIMEFRAME));
		L.addParameter(PARAMETER_DISPLAY_STATUS, iwc.getParameter(PARAMETER_DISPLAY_STATUS));
		if (idts.getMonth() == 1) {
			L.addParameter(CalendarParameters.PARAMETER_DAY,idts.getDay());
			L.addParameter(CalendarParameters.PARAMETER_MONTH, String.valueOf(12));
			L.addParameter(CalendarParameters.PARAMETER_YEAR, String.valueOf(idts.getYear() - 1));
		}
		else {
			L.addParameter(CalendarParameters.PARAMETER_DAY,idts.getDay());
			L.addParameter(CalendarParameters.PARAMETER_MONTH, String.valueOf(idts.getMonth() - 1));
			L.addParameter(CalendarParameters.PARAMETER_YEAR, String.valueOf(idts.getYear()));
		}
		return L; 
	}

	private static IWTimestamp getTimestamp(String day, String month, String year) {
		IWTimestamp stamp = new IWTimestamp();

		if (day != null) {
			stamp.setDay(Integer.parseInt(day));
		}
		if (month != null) {
			stamp.setMonth(Integer.parseInt(month));
		}
		if (year != null) {
			stamp.setYear(Integer.parseInt(year));
		}

		stamp.setHour(0);
		stamp.setMinute(0);
		stamp.setSecond(0);

		return stamp;
	}
	public SupplyPoolHome getSupplyPoolHome() {
		try {
			return (SupplyPoolHome) IDOLookup.getHome(SupplyPool.class);
		}
		catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}
	
	public ProductDayInfoCountHome getProductDayInfoCountHome() {
		try {
			return (ProductDayInfoCountHome) IDOLookup.getHome(ProductDayInfoCount.class);
		}
		catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}
	public ProductPriceHome getProductPriceHome() {
		try {
			return (ProductPriceHome) IDOLookup.getHome(ProductPrice.class);
		}
		catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}
	
  public TravelStockroomBusiness getTravelStockroomBusiness(IWApplicationContext iwac) throws RemoteException {
    return (TravelStockroomBusiness) IBOLookup.getServiceInstance(iwac, TravelStockroomBusiness.class);
  }
  
  public StockroomBusiness getStockroomBusiness(IWApplicationContext iwac) throws RemoteException {
    return (StockroomBusiness) IBOLookup.getServiceInstance(iwac, StockroomBusiness.class);
  }





	
}