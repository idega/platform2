package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.data.ServiceDay;
import is.idega.idegaweb.travel.data.ServiceDayBMPBean;
import is.idega.idegaweb.travel.data.ServiceDayHome;
import is.idega.idegaweb.travel.data.ServiceDayPK;
import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductHome;
import com.idega.block.trade.stockroom.data.SupplyPool;
import com.idega.block.trade.stockroom.data.SupplyPoolDay;
import com.idega.block.trade.stockroom.data.SupplyPoolDayHome;
import com.idega.block.trade.stockroom.data.SupplyPoolDayPK;
import com.idega.block.trade.stockroom.data.SupplyPoolHome;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWCalendar;

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
	private String PARAMETER_UPDATE = "sbs_update";
	private String PARAMETER_AVAILABLE = "sbs_avail_";
	private String PARAMETER_MAX = "sbs_max_";
	private String PARAMETER_MIN = "sbs_min_";
	private String PARAMETER_ESTIMATED = "sbs_estimated_";
	private String PARAMETER_SUPPLY_POOL_ID = "sbs_pid";
	
	
	private IWResourceBundle _iwrb;
	private int _localeId;
	private Product _product;
	private ServiceDayHome _serviceDayHome;
	private SupplyPoolDayHome _supplyPoolDayHome;
	private IWCalendar _cal;
	private int[] _serviceDays;
	private int _textInputSize = 5;
	private SupplyPool pool = null;
	
	
	public ServiceDaySetter() {
		super.setWidth(350);
		super.setHeight(400);
		super.setTitle("idegaWeb Travel");
		super.setStatus(true);
		super.setResizable(true);
	}
	
	public void main(IWContext iwc) throws Exception{
		super.main(iwc);
		init(iwc);
		
		if (_product != null) {
			String action = iwc.getParameter(ACTION);
			if (action != null && action.equals(PARAMETER_UPDATE)) {
				executeUpdate(iwc);
			}
			drawForm(iwc);
		}else {
			noProduct();
		}
	}
	
	private void init(IWContext iwc) {
		_iwrb = super.getResourceBundle(iwc);
		_localeId = iwc.getCurrentLocaleId();
		_cal = new IWCalendar();
		String serviceId = iwc.getParameter(PARAMETER_SERVICE_ID);
		
		try {
			if (serviceId != null) {
				ProductHome productHome = (ProductHome)IDOLookup.getHome(Product.class);
				_serviceDayHome = (ServiceDayHome)IDOLookup.getHome(ServiceDay.class);
				_supplyPoolDayHome = (SupplyPoolDayHome) IDOLookup.getHome(SupplyPoolDay.class);
				_product = productHome.findByPrimaryKey(new Integer(serviceId));
				
				try {
					String sPoolID = iwc.getParameter(PARAMETER_SUPPLY_POOL_ID);
					SupplyPoolHome poolHome = (SupplyPoolHome) IDOLookup.getHome(SupplyPool.class);
					if (sPoolID == null) {
						pool = poolHome.findByProduct(_product);
					} else if ("-1".equals(sPoolID)) {
						pool = null;
					} else {
						pool = poolHome.findByPrimaryKey(new Integer(sPoolID));
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
		if (sPoolID == null || !"-1".equals(sPoolID)) {
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
		
		++row;
		table.mergeCells(1, row, 5, row);
		table.setRowColor(row, TravelManager.GRAY);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add(new SubmitButton(_iwrb.getLocalizedImageButton("travel.update","Update"), ACTION, PARAMETER_UPDATE), 1, row);
		
		
		
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
		table.setAlignment("center");
		add(Text.NON_BREAKING_SPACE);
		add(form);
	}
	
}