package is.idega.idegaweb.travel.presentation;


import is.idega.idegaweb.travel.business.ServiceNotFoundException;
import is.idega.idegaweb.travel.business.TimeframeNotFoundException;
import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import is.idega.idegaweb.travel.data.Contract;
import is.idega.idegaweb.travel.data.Service;
import is.idega.idegaweb.travel.service.presentation.BookingForm;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.CalendarParameters;
import com.idega.idegaweb.presentation.SmallCalendar;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;

/**
 * Title:        idegaWeb Travel
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href mailto:"gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class CalendarHandler extends TravelManager {
	private IWResourceBundle iwrb;
	private Supplier _supplier;
	
	private String backgroundStyle;// = super.backgroundColor;
	
	private String styleAvailableDay = "#FFFFFF";
	private String styleBackground = "";//BookingForm.STYLENAME_BACKGROUND_COLOR;
	private String styleInactiveDay = "";//BookingForm.STYLENAME_HEADER_BACKGROUND_COLOR;
	private String styleHeader;
	private String styleLink;
	private String styleText;
	private String styleSmallText;
	
	private String styleForAvailableDayText = null;//BLACK;
	private String styleForFullyBookedText = null;//BLACK;

	private String styleForAvailableDay = null;//"background: "+TravelManager.ORANGE;
	private String styleForInquery = null;//"background: "+TravelManager.YELLOW;
	private String styleForFullyBooked = null;//"background: "+TravelManager.RED;
	private String styleForToday = null;//"background: #71CBFB";

	private String colorForAvailableDay = TravelManager.ORANGE;
	private String colorForInquery = TravelManager.YELLOW;
	private String colorForFullyBooked = TravelManager.RED;
	private String colorForToday = "#71CBFB";
	private String colorForAvailableDayText = BLACK;
	private String colorForFullyBookedText = BLACK;
	private String colorForText = WHITE;
	int cellpadding = 4;
	//private String inactiveColor = "#666666";

	private IWCalendar cal = new IWCalendar();
	public SmallCalendar sm = new SmallCalendar();
	
	private Product _product;
	private Service _service;
	//  private Tour _tour;
	private Timeframe[] _timeframes;
	private Contract _contract;
	private Reseller _reseller;
	private IWTimestamp _fromStamp;
	private IWTimestamp _toStamp;
	private int addressId = -1;
	private Class _class = Booking.class;
	
	private int _productId;
	private int _resellerId;
	
	private Vector parameterName  = new Vector();
	private Vector parameterValue = new Vector();
	private IWTimestamp _stamp = IWTimestamp.RightNow();
	
	private String _fontColor;
	private boolean _viewInquiries = true;
	private boolean _showPast = false;
	
	
	public CalendarHandler(IWContext iwc) throws Exception{
		super.main(iwc);
		iwrb = super.getResourceBundle();
		_supplier = super.getSupplier();
		//handleInsert(iwc, supplier);
		sm = new SmallCalendar();
		setTimestampsAuto(iwc);
	}
	
	private void buildCalendar() {
		sm.T.setBorder(0);
		sm.T.setCellpadding(cellpadding);
		sm.T.setCellspacing(2);
		sm.useNextAndPreviousLinks(true);
		sm.setDaysAsLink(true);
		sm.showNameOfDays(true);
		
		sm.setLinkStyle(this.styleLink);
		
		sm.setHeaderFontStyleName(this.styleText);
		sm.setDayCellStyle(this.backgroundStyle);

		if (styleSmallText != null) {
			sm.setTextStyleName(styleSmallText);
		} else {
			sm.setTextColor(colorForText);
		}
		
		sm.setHeaderTextColor(WHITE);
		//sm.setDayTextColor(super.textColor);
		//sm.T.setBorderColor(this.backgroundColor);
		
		//      sm.setBackgroundStyleName(getStyleName(BookingForm.STYLENAME_BACKGROUND_COLOR));
		//sm.setBackgroundColor(this.backgroundColor);
		//sm.setHeaderColor(this.backgroundColor);
		sm.setDayCellColor(this.backgroundColor);
		//sm.setBodyColor("#8484D6");
		sm.setInActiveCellColor("#666666");
		if (styleForToday != null) {
			sm.setTodayBackgroundStyleName( styleForToday );
		}
		sm.setColorToday( colorForToday );
		sm.useColorToday(true);
		//      sm.setSelectedHighlighted(true);
		//      sm.setSelectedHighlightColor(colorForToday);
		
		//sm.setDayFontColor(IWTimestamp.RightNow(),this.backgroundColor);
	}
	
	private void timeframeCheck() throws RemoteException {
		if (_service != null) {
			try {
				if (_timeframes == null) {
					_timeframes =_product.getTimeframes();
				}
			}catch (SQLException s) {
				s.printStackTrace(System.err);
			}
		}
	}
	
	public Table getCalendarTable(IWContext iwc) throws RemoteException, FinderException{
		/**
		 * @todo Perform check of some sort
		 */
		return getCalendarTablePrivate(iwc);
	}
	
	private Table getCalendarTablePrivate(IWContext iwc) throws RemoteException, FinderException{
		int instanceId = -2;
		sm.setTimestamp(_fromStamp);
		sm.setICObjectInstanceID(instanceId);
		this.timeframeCheck();
		buildCalendar();

		Table table = new Table(4,7);
//		table.setBorder(1);
//		table.setBorderColor("RED");
		table.setWidth("100%");
		table.setCellspacing(0);
		if (backgroundStyle != null) {
			table.setStyleClass(backgroundStyle);
		}
		
		int row = 1;
		table.mergeCells(1, row, 4, row);
		table.setCellpaddingTop(1, row, 5);
		table.add(getText(iwrb.getLocalizedString("travel.select_date_from_calendar_below", "Select a date from the calendar below")), 1, row);
		++row;
		//      if (_timeframe.getIfYearly()) {
		Form form = new Form();
		for (int i = 0; i < parameterName.size(); i++) {
			form.maintainParameter((String) parameterName.get(i));
		}
		form.maintainParameter(CalendarParameters.PARAMETER_DAY);
		form.maintainParameter(CalendarParameters.PARAMETER_MONTH);
		
		Table yearTable = new Table(2,1);
		IWTimestamp tempSt = IWTimestamp.RightNow();
		DropdownMenu yearMenu = new DropdownMenu(CalendarParameters.PARAMETER_YEAR);
		for (int i = 2000; i < ( tempSt.getYear() +4 ); i++) {
			yearMenu.addMenuElement(i,""+i);
		}
		yearMenu.setSelectedElement(Integer.toString(this._stamp.getYear()));
		yearMenu.setToSubmit();
		
		Text yearTxt = getText(iwrb.getLocalizedString("travel.year","Year"));

		yearTable.add(yearTxt,1,1);
		yearTable.add(yearMenu,2,1);
		yearTable.setAlignment(1,1,"left");
		yearTable.setAlignment(2,1,"left");
		
		form.add(yearTable);
		table.mergeCells(1,row,4,row);
		//table.setAlignment(1,row,"center");
		table.add(form,1,row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_LEFT);
		++row;
		//      }
		
		Locale locale = iwc.getCurrentLocale();
		Text jan = getLinkText(cal.getMonthName(1,locale,IWCalendar.SHORT).substring(0,3));
		Text feb = getLinkText(cal.getMonthName(2,locale,IWCalendar.SHORT).substring(0,3));
		Text mar = getLinkText(cal.getMonthName(3,locale,IWCalendar.SHORT).substring(0,3));
		Text apr = getLinkText(cal.getMonthName(4,locale,IWCalendar.SHORT).substring(0,3));
		Text may = getLinkText(cal.getMonthName(5,locale,IWCalendar.SHORT).substring(0,3));
		Text jun = getLinkText(cal.getMonthName(6,locale,IWCalendar.SHORT).substring(0,3));
		Text jul = getLinkText(cal.getMonthName(7,locale,IWCalendar.SHORT).substring(0,3));
		Text aug = getLinkText(cal.getMonthName(8,locale,IWCalendar.SHORT).substring(0,3));
		Text sep = getLinkText(cal.getMonthName(9,locale,IWCalendar.SHORT).substring(0,3));
		Text oct = getLinkText(cal.getMonthName(10,locale,IWCalendar.SHORT).substring(0,3));
		Text nov = getLinkText(cal.getMonthName(11,locale,IWCalendar.SHORT).substring(0,3));
		Text dec = getLinkText(cal.getMonthName(12,locale,IWCalendar.SHORT).substring(0,3));
		
		Link lJan = new Link(jan,_class);
		lJan.setBold();
		lJan.addParameter(CalendarParameters.PARAMETER_YEAR,_stamp.getYear());
		lJan.addParameter(CalendarParameters.PARAMETER_MONTH,1);
		lJan.addParameter(CalendarParameters.PARAMETER_DAY,_stamp.getDay());
		lJan.setTargetObjectInstance(instanceId);
		Link lFeb = new Link(feb,_class);
		lFeb.setBold();
		lFeb.addParameter(CalendarParameters.PARAMETER_YEAR,_stamp.getYear());
		lFeb.addParameter(CalendarParameters.PARAMETER_MONTH,2);
		lFeb.addParameter(CalendarParameters.PARAMETER_DAY,_stamp.getDay());
		lFeb.setTargetObjectInstance(instanceId);
		Link lMar = new Link(mar,_class);
		lMar.setBold();
		lMar.addParameter(CalendarParameters.PARAMETER_YEAR,_stamp.getYear());
		lMar.addParameter(CalendarParameters.PARAMETER_MONTH,3);
		lMar.addParameter(CalendarParameters.PARAMETER_DAY,_stamp.getDay());
		lMar.setTargetObjectInstance(instanceId);
		Link lApr = new Link(apr,_class);
		lApr.setBold();
		lApr.addParameter(CalendarParameters.PARAMETER_YEAR,_stamp.getYear());
		lApr.addParameter(CalendarParameters.PARAMETER_MONTH,4);
		lApr.addParameter(CalendarParameters.PARAMETER_DAY,_stamp.getDay());
		lApr.setTargetObjectInstance(instanceId);
		Link lMay = new Link(may,_class);
		lMay.setBold();
		lMay.addParameter(CalendarParameters.PARAMETER_YEAR,_stamp.getYear());
		lMay.addParameter(CalendarParameters.PARAMETER_MONTH,5);
		lMay.addParameter(CalendarParameters.PARAMETER_DAY,_stamp.getDay());
		lMay.setTargetObjectInstance(instanceId);
		Link lJun = new Link(jun,_class);
		lJun.setBold();
		lJun.addParameter(CalendarParameters.PARAMETER_YEAR,_stamp.getYear());
		lJun.addParameter(CalendarParameters.PARAMETER_MONTH,6);
		lJun.addParameter(CalendarParameters.PARAMETER_DAY,_stamp.getDay());
		lJun.setTargetObjectInstance(instanceId);
		Link lJul = new Link(jul,_class);
		lJul.setBold();
		lJul.addParameter(CalendarParameters.PARAMETER_YEAR,_stamp.getYear());
		lJul.addParameter(CalendarParameters.PARAMETER_MONTH,7);
		lJul.addParameter(CalendarParameters.PARAMETER_DAY,_stamp.getDay());
		lJul.setTargetObjectInstance(instanceId);
		Link lAug = new Link(aug,_class);
		lAug.setBold();
		lAug.addParameter(CalendarParameters.PARAMETER_YEAR,_stamp.getYear());
		lAug.addParameter(CalendarParameters.PARAMETER_MONTH,8);
		lAug.addParameter(CalendarParameters.PARAMETER_DAY,_stamp.getDay());
		lAug.setTargetObjectInstance(instanceId);
		Link lSep = new Link(sep,_class);
		lSep.setBold();
		lSep.addParameter(CalendarParameters.PARAMETER_YEAR,_stamp.getYear());
		lSep.addParameter(CalendarParameters.PARAMETER_MONTH,9);
		lSep.addParameter(CalendarParameters.PARAMETER_DAY,_stamp.getDay());
		lSep.setTargetObjectInstance(instanceId);
		Link lOct = new Link(oct,_class);
		lOct.setBold();
		lOct.addParameter(CalendarParameters.PARAMETER_YEAR,_stamp.getYear());
		lOct.addParameter(CalendarParameters.PARAMETER_MONTH,10);
		lOct.addParameter(CalendarParameters.PARAMETER_DAY,_stamp.getDay());
		lOct.setTargetObjectInstance(instanceId);
		Link lNov = new Link(nov,_class);
		lNov.setBold();
		lNov.addParameter(CalendarParameters.PARAMETER_YEAR,_stamp.getYear());
		lNov.addParameter(CalendarParameters.PARAMETER_MONTH,11);
		lNov.addParameter(CalendarParameters.PARAMETER_DAY,_stamp.getDay());
		lNov.setTargetObjectInstance(instanceId);
		Link lDec = new Link(dec,_class);
		lDec.setBold();
		lDec.addParameter(CalendarParameters.PARAMETER_YEAR,_stamp.getYear());
		lDec.addParameter(CalendarParameters.PARAMETER_MONTH,12);
		lDec.addParameter(CalendarParameters.PARAMETER_DAY,_stamp.getDay());
		lDec.setTargetObjectInstance(instanceId);
		
		for (int i = 0; i < parameterName.size(); i++) {
			sm.addParameterToLink((String) parameterName.get(i), (String) parameterValue.get(i));
			lJan.addParameter((String) parameterName.get(i), (String) parameterValue.get(i));
			lFeb.addParameter((String) parameterName.get(i), (String) parameterValue.get(i));
			lMar.addParameter((String) parameterName.get(i), (String) parameterValue.get(i));
			lApr.addParameter((String) parameterName.get(i), (String) parameterValue.get(i));
			lMay.addParameter((String) parameterName.get(i), (String) parameterValue.get(i));
			lJun.addParameter((String) parameterName.get(i), (String) parameterValue.get(i));
			lJul.addParameter((String) parameterName.get(i), (String) parameterValue.get(i));
			lAug.addParameter((String) parameterName.get(i), (String) parameterValue.get(i));
			lSep.addParameter((String) parameterName.get(i), (String) parameterValue.get(i));
			lOct.addParameter((String) parameterName.get(i), (String) parameterValue.get(i));
			lNov.addParameter((String) parameterName.get(i), (String) parameterValue.get(i));
			lDec.addParameter((String) parameterName.get(i), (String) parameterValue.get(i));
		}
		
		
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_CENTER);
		table.setCellpaddingBottom(1, row, cellpadding);
		table.setCellpaddingBottom(2, row, cellpadding);
		table.setCellpaddingBottom(3, row, cellpadding);
		table.setCellpaddingBottom(4, row, cellpadding);
		table.add(lJan,1,row);
		table.add(lFeb,2,row);
		table.add(lMar,3,row);
		table.add(lApr,4,row);
		++row;
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_CENTER);
		table.setCellpaddingBottom(1, row, cellpadding);
		table.setCellpaddingBottom(2, row, cellpadding);
		table.setCellpaddingBottom(3, row, cellpadding);
		table.setCellpaddingBottom(4, row, cellpadding);
		table.add(lMay,1,row);
		table.add(lJun,2,row);
		table.add(lJul,3,row);
		table.add(lAug,4,row);
		++row;
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_CENTER);
		table.setCellpaddingBottom(1, row, cellpadding);
		table.setCellpaddingBottom(2, row, cellpadding);
		table.setCellpaddingBottom(3, row, cellpadding);
		table.setCellpaddingBottom(4, row, cellpadding);
		table.add(lSep,1,row);
		table.add(lOct,2,row);
		table.add(lNov,3,row);
		table.add(lDec,4,row);

		
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);
//		table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_RIGHT);
		
		int month = _fromStamp.getMonth();
		int year = _fromStamp.getYear();
		int lengthOfMonth = cal.getLengthOfMonth(month, year);
		
		IWTimestamp temp = new IWTimestamp(1, month , year);
		int iBookings = 0;
		
		List depDays = this.getDepartureDays(iwc, _showPast);
		
		TravelStockroomBusiness tsb = super.getServiceHandler(iwc).getServiceBusiness( this._product);
		int seats = tsb.getMaxBookings( _product, null);
		int minSeats = tsb.getMinBookings( _product, null);
		//      int seats = 0;
		//      int minSeats = 0;
		
		/*
		 * Er thetta FullyBooked dotariid ??		 if (_tour != null) {
		 seats = _tour.getTotalSeats();
		 }
		 */
		
		
		
		try {
			BookingForm bf = super.getServiceHandler(iwc).getBookingForm(iwc,  this._product);
			TravelStockroomBusiness sb = super.getServiceHandler(iwc).getServiceBusiness( _product);
			
			if (_contract != null) {
				for (int i = 0; i < depDays.size(); i++) {
					temp = (IWTimestamp) depDays.get(i);
					if (!sb.getIfExpired(_contract, temp))
						try {
							if (sb.getIfDay(iwc,_contract,_product,temp)) {
								if (bf.isFullyBooked( iwc, _product, temp) ) {
									//              if (seats > 0 && seats <= getBooker(iwc).getBookingsTotalCount(_productId, temp) ) {
									if (styleForFullyBooked != null) {
										sm.setDayStyleClass(temp, styleForFullyBooked);
									} else {
										sm.setDayColor(temp, colorForFullyBooked);
									}
									if (styleForFullyBookedText != null) {
										sm.setDayFontStyleClass(temp, styleForFullyBookedText);
									} else {
										sm.setDayFontColor(temp, colorForFullyBookedText);
									}
								}else {
									if (styleForAvailableDay != null) {
										sm.setDayStyleClass(temp, styleForAvailableDay);
									} else {
										sm.setDayColor(temp, colorForAvailableDay);
									}
									if (styleForAvailableDayText != null) {
										sm.setDayFontStyleClass(temp, styleForAvailableDayText);
									} else {
										sm.setDayFontColor(temp, colorForAvailableDayText);
									}
								}
							}
						}catch (SQLException sql) {
							sql.printStackTrace(System.err);
						}
				}
				int resellerId = _contract.getResellerId();
				if (this._viewInquiries) {
					temp = new IWTimestamp(1, month , year);
					for (int i = 1; i <= lengthOfMonth; i++) {
						if (getInquirer(iwc).getInquiryHome().getInqueredSeats(_productId, temp,resellerId, true) > 0) {
							if (styleForInquery != null) {
								sm.setDayStyleClass(temp, styleForInquery);
							} else {
								sm.setDayColor(temp, colorForInquery);
							}
							if (styleForAvailableDayText != null) {
								sm.setDayFontStyleClass(temp, styleForAvailableDayText);
							} else {
								sm.setDayFontColor(temp, colorForAvailableDayText);
							}
						}
						temp.addDays(1);
					}
				}
				
			}
			else if (_supplier != null) {
				for (int i = 0; i < depDays.size(); i++) {
					//System.err.println("trying");
					temp = (IWTimestamp) depDays.get(i);
					if (bf.isFullyBooked( iwc, _product, temp) ) {
						//            if (seats > 0 && seats <= getBooker(iwc).getBookingsTotalCount(_productId, temp) ) {
						//System.err.println("bookings...");
						if (styleForFullyBooked != null) {
							sm.setDayStyleClass(temp, styleForFullyBooked);
						} else {
							sm.setDayColor(temp, colorForFullyBooked);
						}
						if (styleForFullyBookedText != null) {
							sm.setDayFontStyleClass(temp, styleForFullyBookedText);
						} else {
							sm.setDayFontColor(temp, colorForFullyBookedText);
						}
					}else {
//						System.err.println("not.."+styleForAvailableDay);
						if (styleForAvailableDay != null) {
							sm.setDayStyleClass(temp, styleForAvailableDay);
						} else {
							sm.setDayColor(temp, colorForAvailableDay);
						}
						if (styleForAvailableDayText != null) {
							sm.setDayFontStyleClass(temp, styleForAvailableDayText);
						} else {
							sm.setDayFontColor(temp, colorForAvailableDayText);
						}
					}
				}
				if (this._viewInquiries) {
					temp = new IWTimestamp(1, month , year);
					for (int i = 1; i <= lengthOfMonth; i++) {
						if (getInquirer(iwc).getInqueredSeats(_productId, temp, true) > 0) {
							if (styleForInquery != null) {
								sm.setDayStyleClass(temp, styleForInquery);
							} else {
								sm.setDayColor(temp, colorForInquery);
							}
							if (styleForAvailableDayText != null) {
								sm.setDayFontStyleClass(temp, styleForAvailableDayText);
							} else {
								sm.setDayFontColor(temp, colorForAvailableDayText);
							}
						}
						temp.addDays(1);
					}
				}
			}
			else if (_reseller != null) {
				for (int i = 0; i < depDays.size(); i++) {
					temp = (IWTimestamp) depDays.get(i);
					iBookings = getBooker(iwc).getBookingsTotalCount(_productId, temp, addressId);
					if (seats > 0 && seats <= iBookings ) {
						if (styleForFullyBooked != null) {
							sm.setDayStyleClass(temp, styleForFullyBooked);
						} else {
							sm.setDayColor(temp, colorForFullyBooked);
						}
						if (styleForFullyBookedText != null) {
							sm.setDayFontStyleClass(temp, styleForFullyBookedText);
						} else {
							sm.setDayFontColor(temp, colorForFullyBookedText);
						}
					}else if (iBookings >= minSeats && iBookings <= seats) {
						if (styleForAvailableDay != null) {
							sm.setDayStyleClass(temp, styleForAvailableDay);
						} else {
							sm.setDayColor(temp, colorForAvailableDay);
						}
						if (styleForAvailableDayText != null) {
							sm.setDayFontStyleClass(temp, styleForAvailableDayText);
						} else {
							sm.setDayFontColor(temp, colorForAvailableDayText);
						}
					}
				}
				if (this._viewInquiries) {
					temp = new IWTimestamp(1, month , year);
					for (int i = 1; i <= lengthOfMonth; i++) {
						//              System.err.println("Reseller != null : "+Inquirer.getInqueredSeats(_productId, temp,_resellerId, true));
						if (getInquirer(iwc).getInquiryHome().getInqueredSeats(_productId, temp,_resellerId, true) > 0) {
							if (styleForInquery != null) {
								sm.setDayStyleClass(temp, styleForInquery);
							} else {
								sm.setDayColor(temp, colorForInquery);
							}
							if (styleForAvailableDayText != null) {
								sm.setDayFontStyleClass(temp, styleForAvailableDayText);
							} else {
								sm.setDayFontColor(temp, colorForAvailableDayText);
							}
						}
						temp.addDays(1);
					}
				}
			}
		}catch (ServiceNotFoundException snfe) {
			snfe.printStackTrace(System.err);
		}catch (TimeframeNotFoundException tfnfe) {
			tfnfe.printStackTrace(System.err);
		}catch (FinderException fe) {
			fe.printStackTrace(System.err);
		}catch (Exception ce) {
			ce.printStackTrace(System.err);	
		}
		
		Table legend = new Table();
		legend.setBorder(0);
		legend.setCellpadding(0);
		legend.setCellspacing(2);
		
		Text avail = getText(iwrb.getLocalizedString("travel.colorForAvailableDay","Available"));
		Text inq = getText(iwrb.getLocalizedString("travel.colorForInquiry","Inquiry"));
		Text today = getText(iwrb.getLocalizedString("travel.today","Today"));
		Text full = getText(iwrb.getLocalizedString("travel.fully_booked","Fully booked"));
		
		int lRow = 1;
		legend.add(avail,3,1);
		if (styleForAvailableDay != null) {
			legend.setStyleClass(1, 1, styleForAvailableDay);
		} else {
			legend.setColor(5,1,colorForAvailableDay);
		}
		legend.setWidth(1,1,"18");
		legend.setWidth(5,1,"18");
		legend.setHeight(1,"14");
		if (this._viewInquiries) {
			++lRow;
			legend.add(inq,3,lRow);
			if (styleForInquery != null) {
				legend.setStyleClass(1, lRow, styleForInquery);
			} else {
				legend.setColor(5,lRow,colorForInquery);
			}
			legend.setWidth(1,lRow,"18");
			legend.setHeight(lRow,"14");
		}
		++lRow;
		legend.add(today,3,lRow);
		if (styleForToday != null) {
			legend.setStyleClass(1, lRow, styleForToday);
		} else {
			legend.setColor(5,lRow,colorForToday);
		}
		legend.setWidth(1,lRow,"18");
		legend.setHeight(lRow,"14");
		++lRow;
		legend.add(full,3,lRow);
		if (styleForFullyBooked != null) {
			legend.setStyleClass(1, lRow, styleForFullyBooked);
		} else {
			legend.setColor(5,lRow,colorForFullyBooked);
		}
		legend.setWidth(1,lRow,"18");
		legend.setHeight(lRow,"14");
		
		++row;
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_CENTER);
		table.mergeCells(1,row,4,row);
		table.add(sm,1,row);
		++row;
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_LEFT);
		table.mergeCells(1,row,4,row);
		table.add(legend,1,row);
//		table.setAlignment(1,row,"center");
		
		return table;
	}
	
	
	public void setProduct(Product product) throws RemoteException{
		_product = product;
		_productId = product.getID();
		try {
			/** @todo fixa getInstance() */
			_supplier = ((com.idega.block.trade.stockroom.data.SupplierHome)com.idega.data.IDOLookup.getHomeLegacy(Supplier.class)).findByPrimaryKeyLegacy(product.getSupplierId());
			_service = getTravelStockroomBusiness(IWContext.getInstance()).getService(product);
			_timeframes = _product.getTimeframes();
			//      _timeframe = product.getTimeframe();
			//      try {
			//        _tour = ((is.idega.idegaweb.travel.service.tour.data.TourHome)com.idega.data.IDOLookup.getHome(Tour.class)).findByPrimaryKey(_product.getPrimaryKey());
			//      }catch (FinderException fe) {}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 public void setTour(Tour tour) throws RemoteException{
	 this._tour = tour;
	 try {
	 ProductHome home = (ProductHome) IDOLookup.getHome(Product.class);
	 setProduct(home.findByPrimaryKey(_tour.getPrimaryKey()));
	 //      setProduct(getProductBusiness(iwc).getProduct((Integer)tour.getPrimaryKey()));// Product(tour.getID() ));
	  }catch (FinderException s) {
	  s.printStackTrace(System.err);
	  }
	  }*/
	
	public void setContract(Contract contract) {
		_contract = contract;
	}
	
	public void setReseller(Reseller reseller) {
		_reseller = reseller;
		_resellerId = reseller.getID();
	}
	
	private void setTimestampsAuto(IWContext iwc) {
		IWTimestamp stamp = IWTimestamp.RightNow();
		
		String year = iwc.getParameter(CalendarParameters.PARAMETER_YEAR);
		String month = iwc.getParameter(CalendarParameters.PARAMETER_MONTH);
		if (year != null && month != null) {
			_fromStamp =  new IWTimestamp(1,Integer.parseInt(month), Integer.parseInt(year));
			_toStamp   = new IWTimestamp(cal.getLengthOfMonth(Integer.parseInt(month), Integer.parseInt(year)), Integer.parseInt(month), Integer.parseInt(year));
		}else {
			_fromStamp =  new IWTimestamp(1,stamp.getMonth(), stamp.getYear());
			_toStamp   = new IWTimestamp(cal.getLengthOfMonth(stamp.getMonth(), stamp.getYear()), stamp.getMonth(), stamp.getYear());
		}
		
	}
	
	public void setTimestamp(IWTimestamp stamp) {
		_stamp = new IWTimestamp(stamp);
		_fromStamp =  new IWTimestamp(1,stamp.getMonth(), stamp.getYear());
		_toStamp   = new IWTimestamp(cal.getLengthOfMonth(stamp.getMonth(), stamp.getYear()), stamp.getMonth(), stamp.getYear());
	}
	
	public void setTimeframe(IWTimestamp fromStamp, IWTimestamp toStamp) {
		_fromStamp = new IWTimestamp(fromStamp);
		_toStamp = new IWTimestamp(toStamp);
	}
	
	public void setClassToLinkTo(Class classToLinkTo) {
		_class = classToLinkTo;
	}
	
	public void addParameterToLink(String name, int value) {
		addParameterToLink(name, Integer.toString(value));
	}
	public void addParameterToLink(String name, String value) {
		parameterName.add(name);
		parameterValue.add(value);
	}
	
	public void setBackgroundStyle(String style) {
		this.backgroundStyle = style;
		//      sm.T.setBorderColor(this.backgroundColor);
		sm.setBackgroundStyleName(style);
		//      sm.setBackgroundColor(this.backgroundColor);
		//      sm.setHeaderColor(this.backgroundColor);
		//      sm.setDayCellColor(this.backgroundColor);
	}
	
	public void setInActiveCellStyle(String style) {
		sm.setInactiveBackgroundCellStyleName(style);
	}
	
	public void setFontColor(String color) {
		_fontColor = color;
		sm.setHeaderTextColor(_fontColor);
		sm.setTextColor(_fontColor);
		sm.setHeaderColor(_fontColor);
	}
	
	public void setFullyBookedStyle(String style) {
		this.styleForFullyBooked = style;
	}
	
	public void setAvailableDayStyle(String style) {
		this.styleForAvailableDay = style;
	}
	
	public void setAvailableDayFontStyle(String style) {
		this.styleForAvailableDayText = style;
	}
	
	public void setInquiryStyle(String style) {
		this.styleForInquery = style;
	}
	
	public void setTodayStyle(String style) {
		this.styleForToday = style;
	}
	
	public void showInquiries(boolean show) {
		_viewInquiries = show;
	}
	
	public void showPast(boolean showPast) {
		this._showPast = showPast;
	}
	
	public List getDepartureDays(IWContext iwc) throws RemoteException, FinderException{
		return getDepartureDays(iwc, _showPast);
	}
	
	public void setAddressId(int addressID) {
		this.addressId = addressID;
	}
	
	public List getDepartureDays(IWContext iwc, boolean showPast) throws RemoteException, FinderException {
		List depDays = new Vector();
		
		TravelStockroomBusiness tsb = super.getServiceHandler(iwc).getServiceBusiness( this._product);
		return tsb.getDepartureDays(iwc, _product, _fromStamp, _toStamp, showPast);
		
		/**
		 * @todo skoða betur, er bara tomt rugl
		 if (_tour != null) {
		 if (_tour.getNumberOfDays() > 1) {
		 depDays.addAll(getTourBusiness(iwc).getDepartureDays(iwc, _tour, _fromStamp, _toStamp, showPast));
		 }else {
		 depDays = getTourBusiness(iwc).getDepartureDays(iwc,_tour, _fromStamp, _toStamp, showPast);
		 }
		 }else {
		 depDays = getTravelStockroomBusiness(iwc).getDepartureDays(iwc, _product, _fromStamp, _toStamp, showPast);
		 }
		 
		 return depDays;
		 */
		
	}
	
	public Text getHeaderText(String content) {
		Text text = new Text(content);
		if (styleHeader != null) {
			text.setStyleClass(styleHeader);
		} else {
			text.setFontStyle(TravelManager.theBoldTextStyle);
			text.setFontColor("WHITE");
		}
		return text;
	}
	
	public Text getLinkText(String content) {
		Text text = new Text(content);
		if (styleLink != null) {
			text.setStyleClass(styleLink);
		} else {
			text.setFontStyle(TravelManager.theTextStyle);
			text.setFontColor("WHITE");
		}
		return text;
	}
	
	public Text getText(String content) {
		Text text = new Text(content);
		if (styleText != null) {
			text.setStyleClass(styleText);
		} else {
			text.setFontStyle(TravelManager.theTextStyle);
			text.setFontColor("WHITE");
		}
		return text;
	}
	
	public Text getSmallText(String content) {
		Text text = new Text(content);
		if (styleSmallText != null) {
			text.setStyleClass(styleSmallText);
		} else {
			text.setFontStyle(TravelManager.theTextStyle);
			text.setFontColor("WHITE");
		}
		return text;
	}
	
	public void setSmallStyle(String style) {
		this.styleSmallText = style;
	}
	
	public void setHeaderStyle(String style) {
		this.styleHeader = style;
	}
	
	public void setLinkStyle(String style) {
		this.styleLink = style;
	}
	public void setTextStyle(String style) {
		this.styleText = style;
	}
	public void setCellpadding(int padding) {
		this.cellpadding = padding;
	}
	
	
	/*
	 private TourBusiness getTourBusiness(IWApplicationContext iwac) throws RemoteException {
	 //  	super.getServiceHandler(iwac).
	  return (TourBusiness) IBOLookup.getServiceInstance(iwac, TourBusiness.class);
	  }
	  */
}
