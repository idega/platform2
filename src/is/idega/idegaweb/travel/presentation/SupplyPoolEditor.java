package is.idega.idegaweb.travel.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import com.idega.block.trade.stockroom.data.DayInfo;
import com.idega.block.trade.stockroom.data.DayInfoHome;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.SupplyPool;
import com.idega.block.trade.stockroom.data.SupplyPoolDay;
import com.idega.block.trade.stockroom.data.SupplyPoolDayBMPBean;
import com.idega.block.trade.stockroom.data.SupplyPoolDayHome;
import com.idega.block.trade.stockroom.data.SupplyPoolDayPK;
import com.idega.block.trade.stockroom.data.SupplyPoolHome;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORuntimeException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.CalendarParameters;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;


/**
 * @author gimmi
 */
public class SupplyPoolEditor extends TravelBlock {
	
  protected static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.trade";

  private static final String ACTION_PARAMETER = "a_p";
	private static final String PARAMETER_EDIT_POOL = "ae_p";
	private static final String PARAMETER_EDIT_POOL_DAYS = "ae_pd";
	private static final String PARAMETER_SAVE_POOL = "as_p";
	private static final String PARAMETER_SAVE_POOL_DAYS = "as_pd";
	private static final String PARAMETER_DELETE_POOL = "ad_p";
	private static final String PARAMETER_SHOW_POOLS = "ash_p";
	private static final String PARAMETER_EDIT_DAYS_INFO = "ae_di";
	private static final String PARAMETER_SAVE_DAYS_INFO = "as_di";
	
	private static final String PARAMETER_POOL_ID = "p_i";
	private static final String PARAMETER_NAME = "p_n";
	private static final String PARAMETER_DESCRIPTION = "p_d";
	private static final String PARAMETER_USE = "p_u_";
	private static final String PARAMETER_MAX = "p_ma_";
	private static final String PARAMETER_MIN = "p_mi_";
	private static final String PARAMETER_ESTIMATED = "p_es_";
	private static final String PARAMETER_EST_MONTH = "p_es_month_";
	
	private IWResourceBundle iwrb;
	private HashMap parameters = new HashMap();
	private String textStyle;
	
	private IWTimestamp timeStamp = null;
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	public void main(IWContext iwc) throws Exception {
		add(getForm(iwc));
	}
	
	public Form getForm(IWContext iwc) throws Exception {
		super.main(iwc);
		iwrb = getResourceBundle(iwc);
		
		String action = iwc.getParameter(ACTION_PARAMETER);

		if (super.getSupplier() == null) {
			Form form = new Form();
			form.add("error");
			return form;
		} else {
			Form form = new Form();
			if (action == null || "".equals(action)) {
				form = new Form();
				form.add(getPoolList(iwc));
			} else if (action.equals(PARAMETER_EDIT_POOL)) {
				form = getPoolEditor(iwc);
			} else if (action.equals(PARAMETER_SAVE_POOL)) {
				savePool(iwc);
				form = new Form();
				form.add(getPoolList(iwc));
			} else if (action.equals(PARAMETER_EDIT_POOL_DAYS)) {
				form = getPoolDaysEditor(iwc);
			} else if (action.equals(PARAMETER_SAVE_POOL_DAYS)) {
				savePoolDays(iwc);
				form = getPoolDaysEditor(iwc);
			} else if (action.equals(PARAMETER_DELETE_POOL)) {
				deletePool(iwc);
				form.add(getPoolList(iwc));
			}else if (action.equals(PARAMETER_EDIT_DAYS_INFO)) {
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
					form = getPoolMonthEditor(iwc, timeStamp);
				}	
			} else if(action.equals(PARAMETER_SAVE_DAYS_INFO)) {
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
				savePoolMonthDays(iwc, timeStamp);
				form = getPoolMonthEditor(iwc, timeStamp);
			
			}
			return form;
		}
	}
	
	private void deletePool(IWContext iwc) {
		String sPoolID = iwc.getParameter(PARAMETER_POOL_ID);
		SupplyPool pool = null;
		try {
			pool = getSupplyPoolHome().findByPrimaryKey(new Integer(sPoolID));
			pool.remove();
		} catch (Exception n) {
			System.out.println("Failed to delete supplyPool");
			n.printStackTrace();
		}
	}
	
	private void savePool(IWContext iwc) throws RemoteException {
		String sPoolID = iwc.getParameter(PARAMETER_POOL_ID);
		String name = iwc.getParameter(PARAMETER_NAME);
		String description = iwc.getParameter(PARAMETER_DESCRIPTION);
		SupplyPool pool = null;
		try {
			pool = getSupplyPoolHome().findByPrimaryKey(new Integer(sPoolID));
		} catch (Exception n) {
			try {
				pool = getSupplyPoolHome().create();
				pool.store();
			}
			catch (CreateException e) {
				e.printStackTrace();
			}
		}
		
		if (pool != null && name != null && !"".equals(name.trim())) {
			pool.setName(name);
			pool.setDescription(description);
			pool.setSupplier(super.getSupplier());
			pool.store();
		}
	}
	
	
	private void savePoolDays(IWContext iwc) throws RemoteException {
		String sPoolID = iwc.getParameter(PARAMETER_POOL_ID);
		try {
			SupplyPool pool = getSupplyPoolHome().findByPrimaryKey(new Integer(sPoolID));
			Object poolPK = pool.getPrimaryKey();
			
			String sMax;
			String sMin;
			String sEst;
			
			int iMax;
			int iMin;
			int iEst;
			boolean isUsed = false;
			
			SupplyPoolDay spDay;
			for (int i = SupplyPoolDayBMPBean.SUNDAY; i <= SupplyPoolDayBMPBean.SATURDAY; i++) {
				try {
					spDay = getSupplyPoolDayHome().findByPrimaryKey(new SupplyPoolDayPK(poolPK, new Integer(i)));
				} catch (FinderException e1) {
					spDay = getSupplyPoolDayHome().create(new SupplyPoolDayPK(poolPK, new Integer(i)));
				}
        
				sMax = iwc.getParameter(PARAMETER_MAX+i);
				sMin = iwc.getParameter(PARAMETER_MIN+i);
				sEst = iwc.getParameter(PARAMETER_ESTIMATED+i);
				isUsed = iwc.isParameterSet(PARAMETER_USE+i);
        if (!isUsed) {
	      	spDay.remove();
	      }	else {
	      	try {
						iMax = Integer.parseInt(sMax);
					} catch (NumberFormatException n) {
						iMax = -1;
					}

					try {
						iMin = Integer.parseInt(sMin);
					} catch (NumberFormatException n) {
						iMin = -1;
					}

					try {
						iEst = Integer.parseInt(sEst);
					} catch (NumberFormatException n) {
						iEst = -1;
					}
					
					spDay.setMax(iMax);
					spDay.setMin(iMin);
					spDay.setEstimated(iEst);
					spDay.store();
        } 

				
			
			}
			
			super.getTravelStockroomBusiness(iwc).removeServiceDayHashtable(iwc);
			super.getTravelStockroomBusiness(iwc).removeDepartureDaysApplication(iwc, null);
			super.getTravelStockroomBusiness(iwc).invalidateMaxDayCache(pool);

		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		catch (CreateException e) {
			e.printStackTrace();
		}
		catch (EJBException e) {
			e.printStackTrace();
		}
		catch (RemoveException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	private void savePoolMonthDays(IWContext iwc, IWTimestamp stamp) throws RemoteException {
		String sPoolID = iwc.getParameter(PARAMETER_POOL_ID);
		SupplyPool pool = null;
		Integer poolPK = null;

		if(sPoolID != null && !sPoolID.equals("")) {
			try {
				pool = getSupplyPoolHome().findByPrimaryKey(new Integer(sPoolID));
				poolPK = (Integer) pool.getPrimaryKey();
			} catch (FinderException e) {
				e.printStackTrace();
			}
		} 

		IWCalendar calendar = new IWCalendar();
		int daycount = calendar.getLengthOfMonth(stamp.getMonth(),stamp.getYear()); 
		int n = 1;
		if(pool != null && poolPK != null) {
			while(n <= daycount) {
				try {

					String countString = iwc.getParameter(PARAMETER_EST_MONTH + n); 
					IWTimestamp date = new IWTimestamp(n, stamp.getMonth(), stamp.getYear());
					if(countString != null && !countString.trim().equals("")) {
						int count = Integer.parseInt(countString);
						DayInfo dayInfo = null;
						try {
							dayInfo = getDayInfoHome().findBySupplyPoolIdAndDate(poolPK.intValue(), date.getDate());
						} catch (FinderException e1) {
							try {
								dayInfo = getDayInfoHome().create();
							}
							catch (CreateException e2) {
								e2.printStackTrace();
							}
						}
						dayInfo.setCount(count);
						dayInfo.setSupplyPoolId(poolPK.intValue());
						dayInfo.setDate(date.getDate());
						dayInfo.store();
					} else { // Removing
						try {
							DayInfo dayInfo = getDayInfoHome().findBySupplyPoolIdAndDate(poolPK.intValue(), date.getDate());
							try {
								dayInfo.remove();
							}
							catch (RemoveException e2) {
								e2.printStackTrace();
							}
						} catch (FinderException e1) {
							// Not logging FinderException because I dont care if nothing is here to delete
						}						
					}
				}
				catch (NumberFormatException e) {
					e.printStackTrace();
				}
				catch (EJBException e) {
					e.printStackTrace();
				}
				n++;
			}
			super.getTravelStockroomBusiness(iwc).removeServiceDayHashtable(iwc);
			super.getTravelStockroomBusiness(iwc).removeDepartureDaysApplication(iwc, null);
			super.getTravelStockroomBusiness(iwc).invalidateMaxDayCache(pool);
		}
	}
	
	private Form getPoolEditor(IWContext iwc) {
		Form form = new Form();
		Table table = new Table();
		table.setWidth("400");
		table.setColor(TravelManager.WHITE);
		table.setCellspacing(1);
		form.add(table);
		int row = 1;
		
		String sPoolID = iwc.getParameter(PARAMETER_POOL_ID);
		SupplyPool pool = null;
		try {
			pool = getSupplyPoolHome().findByPrimaryKey(new Integer(sPoolID)); 
		} catch (FinderException e) {
		} catch (NumberFormatException n) {
		}
		
		TextInput name = new TextInput(PARAMETER_NAME);
		TextInput description = new TextInput(PARAMETER_DESCRIPTION);
		
		if (pool != null) {
			table.add(new HiddenInput(PARAMETER_POOL_ID, pool.getPrimaryKey().toString()), 1, row);
			name.setContent(pool.getName());
			description.setContent(pool.getDescription());
		}
		
		table.add(getHeaderText(iwrb.getLocalizedString("name", "Name")), 1, row);
		table.add(getHeaderText(iwrb.getLocalizedString("description", "Desciption")), 2, row);
		table.setRowColor(row++, TravelManager.backgroundColor);
		table.add(name, 1, row);
		table.add(description, 2, row);
		table.setRowColor(row++, TravelManager.GRAY);
		
		SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("save", "Save"), ACTION_PARAMETER, PARAMETER_SAVE_POOL);
		BackButton back = new BackButton(iwrb.getLocalizedImageButton("back", "Back"));
		table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add(back, 1, row);
		table.add(save, 2, row);
		table.setRowColor(row++, TravelManager.GRAY);

		return form;
	}
	
	private Form getPoolDaysEditor(IWContext iwc) {
		String sPoolID = iwc.getParameter(PARAMETER_POOL_ID);
		Form form = new Form();
		Table table = new Table();
		table.setWidth("400");
		table.setColor(TravelManager.WHITE);
		table.setCellspacing(1);

		SupplyPool pool = null;
		int row = 1;
		try {
			Object poolPK = null;
			try {
				pool = getSupplyPoolHome().findByPrimaryKey(new Integer(sPoolID));
				poolPK = pool.getPrimaryKey();
			} catch (FinderException f) {
			} catch (NumberFormatException n) {}
		
			Link moreLink = new Link(iwrb.getLocalizedImageButton("travel.day_by_day", "Day by day"));
			moreLink.addParameter(ACTION_PARAMETER, PARAMETER_EDIT_DAYS_INFO);
			moreLink.addParameter(PARAMETER_POOL_ID, pool.getPrimaryKey().toString());
			moreLink.addParameter(InitialData.dropdownView,InitialData.PARAMETER_SUPPLY_POOL);
			
			IWCalendar calendar = new IWCalendar();
			form.add(table);
			form.maintainParameter(PARAMETER_POOL_ID);
			table.add(getHeaderText(pool.getName()), 1, row);
			table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_CENTER);
			table.add(getHeaderText(iwrb.getLocalizedString("available", "Available")), 2, row);
			table.add(getHeaderText(iwrb.getLocalizedString("maximum", "Maximum")), 3, row);
			table.add(getHeaderText(iwrb.getLocalizedString("minimum", "Minimum")), 4, row);
			table.add(getHeaderText(iwrb.getLocalizedString("estimated", "Estimated")), 5, row);
			table.setRowColor(row++, TravelManager.backgroundColor);
			TextInput max;
			TextInput min;
			TextInput est;
			CheckBox use;
			SupplyPoolDay spDay;
			for (int i = SupplyPoolDayBMPBean.SUNDAY; i <= SupplyPoolDayBMPBean.SATURDAY; i++) {
	      max = new TextInput(PARAMETER_MAX+i);
	      min = new TextInput(PARAMETER_MIN+i);
	      est = new TextInput(PARAMETER_ESTIMATED+i);
	      use = new CheckBox(PARAMETER_USE+i);
	      
	      max.setSize(4);
	      min.setSize(4);
	      est.setSize(4);
	      try {
	      	if (poolPK != null) {
						spDay = getSupplyPoolDayHome().findByPrimaryKey(new SupplyPoolDayPK(poolPK, new Integer(i)));
						use.setChecked(true);
					  if (spDay.getMax() > -1) {
					  	max.setContent(Integer.toString(spDay.getMax()));
					  }
					  if (spDay.getMin() > -1) {
					  	min.setContent(Integer.toString(spDay.getMin()));
					  }
					  if (spDay.getEstimated() > -1) {
					  	est.setContent(Integer.toString(spDay.getEstimated()));
					  }
	      	}
				}
				catch (FinderException e1) {
					//e1.printStackTrace();
				}
				
				table.add(getText(calendar.getDayName(i, iwc.getCurrentLocale(),IWCalendar.LONG)), 1, row);
				table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_CENTER);
				table.add(use, 2, row);
				table.add(max, 3, row);
				table.add(min, 4, row);
				table.add(est, 5, row);
				table.setRowColor(row++, TravelManager.GRAY);
			}
			SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("save", "Save"), ACTION_PARAMETER, PARAMETER_SAVE_POOL_DAYS);
			SubmitButton back = new SubmitButton(iwrb.getLocalizedImageButton("back", "Back"));

			table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_RIGHT);
			table.add(back, 1, row);
			table.mergeCells(2, row, 5, row);
			table.add(moreLink, 2, row);
			table.add(Text.NON_BREAKING_SPACE, 2, row);
			table.add(save, 2, row);
			table.setRowColor(row, TravelManager.GRAY);
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		

		getProductsUsingPool(iwc, form, pool);
		
		return form;
	}

	private Form getPoolMonthEditor(IWContext iwc, IWTimestamp stamp) {
		Form form = new Form();
		IWCalendar calendar = new IWCalendar();
		Table table = new Table();
		table.setWidth(400);
		table.setColor(TravelManager.WHITE);
		table.setCellspacing(1);
		table.setCellpadding(0);
		form.maintainParameter(CalendarParameters.PARAMETER_DAY);
		form.maintainParameter(CalendarParameters.PARAMETER_MONTH);
		form.maintainParameter(CalendarParameters.PARAMETER_YEAR);
		form.add(table);
		
		int row = 1;
		
		String sPoolID = iwc.getParameter(PARAMETER_POOL_ID);
		SupplyPool pool = null;
		SupplyPoolDay poolDay = null;
		Object poolPK = null;
		try {
			pool = getSupplyPoolHome().findByPrimaryKey(new Integer(sPoolID));
			poolPK = pool.getPrimaryKey();
		} catch (FinderException f) {
		} catch (NumberFormatException n) {
		}
				
		table.mergeCells(1,row,7,row);
		table.add(getHeaderText(pool.getName()), 1, row);
		table.setRowColor(row++, TravelManager.backgroundColor);
		
		table.mergeCells(1, row, 7, row);
		table.setRowColor(row, TravelManager.GRAY);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_CENTER);
		table.add(getLastMonthsLink(iwrb, pool, stamp), 1, row);
		table.add(Text.NON_BREAKING_SPACE, 1, row);
		table.add(new Text(stamp.getDateString("MMMMMMMM yyyy",iwc.getCurrentLocale())), 1, row); 
		table.add(Text.NON_BREAKING_SPACE, 1, row);
		table.add(getNextMonthsLink(iwrb, pool, stamp), 1, row++); 
		
		for(int i=1; i<=7; i++) {
			int max = -1; 
			try {
				if(poolPK != null) {
					poolDay = getSupplyPoolDayHome().findByPrimaryKey(new SupplyPoolDayPK(poolPK, new Integer(i)));
				  if (poolDay.getMax() > -1) {
				  		max = poolDay.getMax();
				  }
				}
			}
			catch (FinderException e) {
//				e.printStackTrace();
			}
			table.add(getText(calendar.getDayName(i, iwc.getCurrentLocale(),IWCalendar.SHORT)), i, row);
			if(max != -1) {
				table.add(" (" + max +")", i, row);
			}
			table.setAlignment(i, row, Table.HORIZONTAL_ALIGN_CENTER);
			table.setRowColor(row, TravelManager.GRAY);
		}
		
		int daycount = calendar.getLengthOfMonth(stamp.getMonth(),stamp.getYear());
		int column = calendar.getDayOfWeek(stamp.getYear(),stamp.getMonth(),1);	
		int n = 1;
		int poolId = ((Integer) poolPK).intValue();
		row++;
		while(n <= daycount) {
			TextInput input = new TextInput(PARAMETER_EST_MONTH + n);
			DayInfo dayInfo = null;
			IWTimestamp date = new IWTimestamp(n, stamp.getMonth(), stamp.getYear());
			try {
				dayInfo = getDayInfoHome().findBySupplyPoolIdAndDate(poolId, date.getDate());
			}
			catch (FinderException e) {
				dayInfo = null;
			}
			input.setSize(4);
			if(dayInfo != null) {
				input.setContent(Integer.toString(dayInfo.getCount()));
			}
			
			table.setRowColor(row, TravelManager.GRAY);
			table.add(new Text(String.valueOf(n)), column, row);
			table.add(Text.BREAK, column, row);
			table.add(input, column, row);
			
			column = column % 7 + 1;
			if (column == 1)
				row++;
			n++;
		}
		
		SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("save", "Save"), ACTION_PARAMETER, PARAMETER_SAVE_DAYS_INFO);
		SubmitButton back = new SubmitButton(iwrb.getLocalizedImageButton("back", "Back"));
		
		table.setRowColor(++row, TravelManager.GRAY);
		table.add(back, 1, row); 
		table.setAlignment(7, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add(save, 7, row); 

		getProductsUsingPool(iwc,form,pool);
		
		form.maintainParameter(PARAMETER_POOL_ID);
		
		return form;
	}
	/**
	 * @param iwc
	 * @param form
	 * @param pool
	 */
	private void getProductsUsingPool(IWContext iwc, Form form, SupplyPool pool) {
		try {
			if (pool != null) {
				Table table2 = new Table();
				table2.setWidth("400");
				table2.setColor(TravelManager.WHITE);
				table2.setCellspacing(1);
				form.add(Text.getBreak());
				form.add(table2);
				Collection products = getProductBusiness(iwc).getProductHome().findBySupplyPool(pool);
				if (products != null) {
					Iterator iter = products.iterator();
					int count = 0;
					Product product;
					while (iter.hasNext()) {
						product = (Product) iter.next();
						table2.add(getText(product.getProductName(iwc.getCurrentLocaleId())), 1, count+2);
						table2.setRowColor(count+2, TravelManager.GRAY);
						++count;
					}
					table2.add(getHeaderText(iwrb.getLocalizedString("travel.products_using_this_pool", "Products using this pool")+" : "+count), 1, 1);
					table2.setRowColor(1, TravelManager.backgroundColor);
				}
			}
		}
		catch (IDORelationshipException e1) {
			e1.printStackTrace();
		}
		catch (FinderException e1) {
			e1.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (IDOCompositePrimaryKeyException e) {
			e.printStackTrace();
		}
	}

	private Table getPoolList(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setWidth("400");
		table.setColor(TravelManager.WHITE);
		table.setCellspacing(1);
		int row = 1;
		
		table.add(getHeaderText(iwrb.getLocalizedString("name", "Name")), 1, row);
		table.add(getHeaderText(iwrb.getLocalizedString("description", "Desciption")), 2, row);
		table.add("", 4, row);
		table.setRowColor(row++, TravelManager.backgroundColor);
		try {
			Collection pools = getSupplyPoolHome().findBySupplier(super.getSupplier());
			if (pools != null && !pools.isEmpty()) {
				Iterator iter = pools.iterator();
				SupplyPool pool;
				Link link;
				Link edit;
				Link delete;
				while (iter.hasNext()) {
					pool = (SupplyPool) iter.next();
					link = getLink(getText(pool.getName()));
					link.addParameter(ACTION_PARAMETER, PARAMETER_EDIT_POOL_DAYS);
					link.addParameter(PARAMETER_POOL_ID, pool.getPrimaryKey().toString());
					
					edit = getLink(getText(iwrb.getLocalizedString("edit", "Edit")));
					edit.addParameter(ACTION_PARAMETER, PARAMETER_EDIT_POOL);
					edit.addParameter(PARAMETER_POOL_ID, pool.getPrimaryKey().toString());
					
					delete = getLink(getText(iwrb.getLocalizedString("delete", "Delete")));
					delete.addParameter(ACTION_PARAMETER, PARAMETER_DELETE_POOL);
					delete.addParameter(PARAMETER_POOL_ID, pool.getPrimaryKey().toString());

					table.add(link, 1, row);
					table.add(getText(pool.getDescription()), 2, row);
					table.add(edit, 3, row);
					table.add(delete, 4, row);
					table.setRowColor(row++, TravelManager.GRAY);
				}
			}
			Link link = getLink(getText("New Pool"));
			link.setImage(iwrb.getLocalizedImageButton("new", "New"));
			link.addParameter(ACTION_PARAMETER, PARAMETER_EDIT_POOL);

			table.add(link, 1, row);
			table.setRowColor(row, TravelManager.GRAY);
		} catch (FinderException e) {
		}
		
		return table;
	}
	
	private Text getHeaderText(String content) {
		Text text = new Text(content);
		text.setStyle(TravelManager.theBoldTextStyle);
		text.setFontColor(TravelManager.WHITE);
		return text;
	}
	
	private Text getText(String content) {
		Text text = new Text(content);
		if (textStyle != null) {
			text.setStyleClass(textStyle);
		} else {
			text.setStyle(TravelManager.theTextStyle);
		}
		return text;
	}
	
	private Link getLink(Text text) {
		Link link = new Link(text);
		Set set = parameters.keySet();
		if (set != null) {
			Iterator iter = set.iterator();
			String key;
			while (iter.hasNext()) {
				key = (String) iter.next();
				link.addParameter(key, (String) parameters.get(key));
			}
		}
		return link;
	}
	private Link getNextMonthsLink(IWResourceBundle iwrb, SupplyPool pool, IWTimestamp idts) {
		Link L = new Link(iwrb.getLocalizedString("right", "-->"));
		L.addParameter(ACTION_PARAMETER, PARAMETER_EDIT_DAYS_INFO);
		L.addParameter(PARAMETER_POOL_ID, pool.getPrimaryKey().toString());
		L.addParameter(InitialData.dropdownView,InitialData.PARAMETER_SUPPLY_POOL);
		L.addParameter(InitialData.dropdownView,InitialData.PARAMETER_SUPPLY_POOL);
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
	private Link getLastMonthsLink(IWResourceBundle iwrb, SupplyPool pool, IWTimestamp idts) {
		Link L = new Link(iwrb.getLocalizedString("left", "<--"));
		L.addParameter(ACTION_PARAMETER, PARAMETER_EDIT_DAYS_INFO);
		L.addParameter(PARAMETER_POOL_ID, pool.getPrimaryKey().toString());
		L.addParameter(InitialData.dropdownView,InitialData.PARAMETER_SUPPLY_POOL);
		L.addParameter(InitialData.dropdownView,InitialData.PARAMETER_SUPPLY_POOL);
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
	public void addParameter(String name, String value) {
		parameters.put(name, value);
	}
	
	public SupplyPoolHome getSupplyPoolHome() {
		try {
			return (SupplyPoolHome) IDOLookup.getHome(SupplyPool.class);
		}
		catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}

	public SupplyPoolDayHome getSupplyPoolDayHome() {
		try {
			return (SupplyPoolDayHome) IDOLookup.getHome(SupplyPoolDay.class);
		}
		catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}
	
	public DayInfoHome getDayInfoHome() {
		try {
			return (DayInfoHome) IDOLookup.getHome(DayInfo.class);
		}
		catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}

}
