package is.idega.idegaweb.travel.block.search.presentation;

import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import is.idega.idegaweb.travel.data.GeneralBooking;
import is.idega.idegaweb.travel.presentation.LinkGenerator;
import is.idega.idegaweb.travel.presentation.TravelBlock;
import is.idega.idegaweb.travel.service.presentation.BookingForm;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import com.idega.block.basket.business.BasketBusiness;
import com.idega.block.basket.data.BasketEntry;
import com.idega.block.text.business.ContentFinder;
import com.idega.block.text.business.ContentHelper;
import com.idega.block.text.data.LocalizedText;
import com.idega.block.text.data.TxText;
import com.idega.block.text.presentation.TextEditorWindow;
import com.idega.block.trade.data.Currency;
import com.idega.block.trade.stockroom.data.PriceCategoryBMPBean;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductPrice;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierHome;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.block.trade.stockroom.data.TravelAddress;
import com.idega.block.trade.stockroom.data.TravelAddressHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.contact.data.PhoneType;
import com.idega.core.file.data.ICFile;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.util.IWTimestamp;


/**
 * @author gimmi
 */
public class ProductDetailFrame extends TravelBlock {

	Product product = null;
	Supplier supplier = null;
	TxText descriptionText = null;
	ContentHelper ch = null;
	Table contTable;
	String leftWidth = "200";
	IWTimestamp fromDate = null;
	List depAddresses = null;
	Timeframe timeframe = null;
	int columns = 3;
	int localeID = -1;
	protected DecimalFormat currencyFormat;
	
	String priceCategoryKey = null;
	int count = 0;
	
	IWResourceBundle iwrb = null;
	BookingForm bookingForm = null;
	Table productInfoDetailed = null;
	List leftAdd = new Vector();
	boolean showContactInfo = true;
	boolean useBasket = false;
	protected DecimalFormat df = new DecimalFormat("0.00");

//	public ProductDetailFrame(IWContext iwc, Product product) throws RemoteException {
//		this(iwc, 3, product);
//	}
	
	public ProductDetailFrame(IWContext iwc, int columns, Product product, boolean showContactInfo, boolean useBasket) throws RemoteException {
		String sProductId = iwc.getParameter(AbstractSearchForm.PARAMETER_PRODUCT_ID);
		if (sProductId == null) {
			sProductId = iwc.getParameter(LinkGenerator.parameterProductId);
		}
		this.useBasket = useBasket;
		this.showContactInfo = showContactInfo;
		iwrb = super.getResourceBundle(iwc);
		localeID = iwc.getCurrentLocaleId();
		currencyFormat = new DecimalFormat("0.00");

		this.columns = columns;
		if (product != null) {
			try {
//				ProductHome pHome = (ProductHome) IDOLookup.getHome(Product.class);
				SupplierHome sHome = (SupplierHome) IDOLookup.getHome(Supplier.class);
//				product = pHome.findByPrimaryKey(new Integer(sProductId));
				this.product = product;
				supplier = sHome.findByPrimaryKey(product.getSupplierId());

				descriptionText = product.getText();
				if (descriptionText != null) {
					ch = ContentFinder.getContentHelper(descriptionText.getContentId(), localeID, product.getDatasource());
				}
				bookingForm = getServiceHandler(iwc).getBookingForm(iwc, product);

				try {
					fromDate = new IWTimestamp(iwc.getParameter(AbstractSearchForm.PARAMETER_FROM_DATE));
				} catch (NullPointerException n) {
					fromDate = IWTimestamp.RightNow();
				}

				try {
					timeframe = getProductBusiness(iwc).getTimeframe(product, fromDate);
				} catch (Exception e) {e.printStackTrace();}
				
				try {
					Timeframe[] frames = new Timeframe[]{};
					if (timeframe != null) {
						frames = new Timeframe[]{timeframe};
					}
					depAddresses = getProductBusiness(iwc).getDepartureAddresses(product, fromDate, true, null, frames);
				} catch (Exception e) {e.printStackTrace();}
				
			} catch (Exception e ) {
				e.printStackTrace();
			}
		} else if (useBasket) {
			System.out.println("ProductDefailtFrame product is NULL, use Basket !!!");
		} else {
			System.out.println("ProductDefailtFrame product is NULL");
		}

		contTable = new Table(columns, 10);
		contTable.setBorder(0);
		//contTable.setBorderColor("GREEN");

		contTable.setCellpaddingAndCellspacing(0);
		contTable.setWidth("100%");
		contTable.setWidth(1, 1, leftWidth);
		if (columns == 3) {
			contTable.setWidth(columns, 1, "22");
		} else {
			contTable.setWidth(2, 1, "100%");
		}
	}
	
	public void main(IWContext iwc) throws RemoteException, EJBException, FinderException {
		super.add(getProductDetailFrame(iwc));
	}
	
	public void setPriceCategoryKey(String key) {
		this.priceCategoryKey = key;
	}
	
	public void add(PresentationObject po) {
		contTable.add(po, 2, 1);
	}
	
	public void addBottom(PresentationObject po) {
		contTable.add(po, 2, 7);
	}
	
	public void addLeft(PresentationObject po) {
		leftAdd.add(po);
	}
	
	protected Table getProductDetailFrame(IWContext iwc) throws RemoteException, EJBException, FinderException {

		Table table = new Table();
		table.setCellspacing(0);
		table.setCellpadding(0);
		table.setWidth("100%");
		table.setBorder(0);
		int row = 1;
		ContentHelper ch;
		LocalizedText locText = null;
		List files;
		Image image = null;
		Timeframe timeframe;
		
		if (useBasket) {
//			table.setBorder(1);
//			contTable.setBorder(1);

			Collection coll = getBasketBusiness(iwc).getBasket().values();
			Iterator iter = coll.iterator();
			Table bookingTable = new Table();
			bookingTable.setCellpaddingAndCellspacing(0);
//			bookingTable.setBorder(1);
			int bRow = 1;
			GeneralBooking booking;
			BasketEntry item;
			Currency currency = null;
			Product prod;
			bookingTable.setWidth("200");
//			contTable.setWidth(1, 1, "200");
			float totalPrice = 0;
			while (iter.hasNext()) {
				item = (BasketEntry) iter.next();
				booking = (GeneralBooking) item.getItem();
				float price = getBooker(iwc).getBookingPrice(booking);
				currency = getBooker(iwc).getBookingCurrency(booking);
				totalPrice += price;
				prod = booking.getService().getProduct();
				String name = prod.getProductName(localeID);
				String suppName = prod.getSupplier().getName();
				bookingTable.setStyleClass(1, bRow, getStyleName(BookingForm.STYLENAME_BACKGROUND_COLOR));
				bookingTable.setCellpaddingLeft(1, bRow, 10);
				bookingTable.setCellpaddingTop(1, bRow, 10);
				bookingTable.add(getText(suppName +" - "+name), 1, bRow++);
				bookingTable.setStyleClass(1, bRow, getStyleName(BookingForm.STYLENAME_BACKGROUND_COLOR));
				bookingTable.setCellpaddingTop(1, bRow, 5);
				bookingTable.setCellpaddingLeft(1, bRow, 10);
				bookingTable.setCellpaddingBottom(1, bRow, 10);
				bookingTable.add(getSmallText(iwrb.getLocalizedString("travel.price", "Price")+": "), 1, bRow);
				bookingTable.add(getOrangeText(df.format(price)+" "+currency.getCurrencyAbbreviation()), 1, bRow++);
				bookingTable.setHeight(bRow++, 2);
			}
			bookingTable.setStyleClass(1, bRow, getStyleName(BookingForm.STYLENAME_BACKGROUND_COLOR));
			bookingTable.setCellpadding(1, bRow, 10);
			if (getBasketBusiness(iwc).getBasket().isEmpty()) {
				bookingTable.add(getSmallText(iwrb.getLocalizedString("travel.basket_is_empty", "Basket is empty")), 1, bRow++);
			} else {
				bookingTable.add(getSmallText(iwrb.getLocalizedString("travel.total", "Total")+": "), 1, bRow);
				bookingTable.add(getOrangeText(df.format(totalPrice)+" "+currency.getCurrencyAbbreviation()), 1, bRow++);
			}
			
			contTable.add(bookingTable, 1, 1);
			contTable.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_TOP);
			contTable.setVerticalAlignment(2, 1, Table.VERTICAL_ALIGN_TOP);

			
			table.add(contTable, 1, row++);
		} else if (product != null) {
			TravelStockroomBusiness bus = null;
			try {
				bus = getServiceHandler(iwc).getServiceBusiness(product);
			}
			catch (FinderException e1) {
				e1.printStackTrace();
			}
			//IWTimestamp fromDate = null;
			
			String productPriceId = iwc.getParameter(AbstractSearchForm.PARAMETER_PRODUCT_PRICE_ID);
			String sAddressId = iwc.getParameter(AbstractSearchForm.PARAMETER_ADDRESS_ID);
			int addressId = -1;
			if (sAddressId != null) {
				try {
					addressId = Integer.parseInt(sAddressId);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			timeframe = getServiceHandler(iwc).getProductBusiness().getTimeframe(product, fromDate, addressId);
			int timeframeId = -1;
			if (timeframe != null) {
				timeframeId = timeframe.getID();
			}
						
			row = addProductHeader(product, supplier, table, row);
			
			table.mergeCells(1, row, 3, row);
			table.add(contTable, 1, row++);
			try {
				TxText descriptionText = product.getText();
				if (descriptionText != null) {
					ch = ContentFinder.getContentHelper(descriptionText.getContentId(), localeID, product.getDatasource());
					if (ch != null) {
						locText = ch.getLocalizedText();
						files = ch.getFiles();
						if (files != null && !files.isEmpty()) {
							ICFile imagefile = (ICFile) files.get(0);
							String att = imagefile.getMetaData(TextEditorWindow.imageAttributeKey);
	
							image = new Image(((Integer)imagefile.getPrimaryKey()).intValue());
							image.setMaxImageWidth(200);
							if (att != null) {
								image.addMarkupAttributes(getAttributeMap(att));
							}
						}
					}
				}
				else {
					ch = null;
					locText = null;
					files = null;
					image = null;
				}
				
				if (image != null) {
					contTable.add(image, 1, 1);
				}
				/*
				if (locText != null) {
					if (locText.getHeadline() != null) {
						contTable.add(getText(locText.getHeadline()), 2, 1);
						contTable.addBreak(2, 1);
					}
					if (locText.getBody() != null) {
						contTable.add(getSmallText(locText.getBody()), 2, 1);
					}
					contTable.setVerticalAlignment(2, 1, Table.VERTICAL_ALIGN_TOP);
					contTable.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_TOP);
					//contTable.setCellpaddingTop(1, 1, 5);
					contTable.setCellpadding(2, 1, 5);
				}
				*/
				contTable.setVerticalAlignment(2, 1, Table.VERTICAL_ALIGN_TOP);
				contTable.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_TOP);
				contTable.setHeight(1, 2, 2);
				contTable.setStyleClass(1, 3, getStyleName(BookingForm.STYLENAME_BACKGROUND_COLOR));
				contTable.setCellpadding(1, 3, 5);
				
				contTable.add(getSupplierInfo(product), 1, 3);
				
				contTable.setStyleClass(1, 4, getStyleName(BookingForm.STYLENAME_HEADER_BACKGROUND_COLOR));
				contTable.setHeight(1, 5, 2);

				contTable.setStyleClass(1, 6, getStyleName(BookingForm.STYLENAME_BACKGROUND_COLOR));
				contTable.setCellpadding(1, 6, 5);
				contTable.setVerticalAlignment(1, 6, Table.VERTICAL_ALIGN_TOP);
				if (productInfoDetailed != null) {
					contTable.add(productInfoDetailed, 1, 6);
				}
				if (depAddresses != null && !depAddresses.isEmpty() && addressId < 1) {
					Iterator iter = depAddresses.iterator();
					while (iter.hasNext()) {
						addPrices(contTable, 1, 6, bus, product, timeframeId, ((TravelAddress) iter.next()).getID(), getNumberOfDays(iwc, fromDate), Text.BREAK);
					}
				} else {
					addPrices(contTable, 1, 6, bus, product, timeframeId, addressId, getNumberOfDays(iwc, fromDate), Text.BREAK);
				}
				
				Iterator leftIter = leftAdd.iterator();
				while (leftIter.hasNext()) {
					contTable.add((PresentationObject) leftIter.next(), 1, 6);
				}

				contTable.setStyleClass(1, 7, getStyleName(BookingForm.STYLENAME_HEADER_BACKGROUND_COLOR));
				contTable.setHeight(1, 8, 2);
				contTable.setStyleClass(1, 9, getStyleName(BookingForm.STYLENAME_HEADER_BACKGROUND_COLOR));
				contTable.setHeight(1, 9, 2);
				
				contTable.mergeCells(2, 1, 2, 6);
				if (columns == 3) {
					contTable.mergeCells(3, 1, 3, 6);
					contTable.mergeCells(2, 7, 3, 10);
				}	else {
					contTable.mergeCells(2, 7, 2, 10);
				}
				contTable.setVerticalAlignment(2, 7, Table.VERTICAL_ALIGN_BOTTOM);
				contTable.setCellpaddingLeft(2, 7, 10);
				
				//contTable.add(getDetailLinks(product), 2, 7);
				

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			add("product == null");
		}
		
		return table;
	}
	
	


	protected Table getSupplierInfo(Product product) {
		Table table = new Table();
		table.setCellspacing(0);
		table.setCellpadding(1);
		int row = 1;
		Supplier supplier;
		try {
			supplier = product.getSupplier();
			table.add(getText(iwrb.getLocalizedString("travel.search.address", "Address")), 1, row++);
			table.add(getSmallText(supplier.getAddress().getStreetAddress()), 1, row++);
			
			if (showContactInfo) {
				List phones = supplier.getPhones();
				if (phones != null) {
					Iterator iter = phones.iterator();
					Phone phone;
					while (iter.hasNext()) {
						phone = (Phone) iter.next();
						if (!"".equals(phone.getNumber())) {
							switch (phone.getPhoneTypeId()) {
								case PhoneType.FAX_NUMBER_ID :
									table.add(getText(iwrb.getLocalizedString("travel.search.fax", "Fax")), 1, row++);
									break;
								case PhoneType.HOME_PHONE_ID :
									table.add(getText(iwrb.getLocalizedString("travel.search.telephone", "Telephone number")), 1, row++);
									break;
								case PhoneType.MOBILE_PHONE_ID :
									table.add(getText(iwrb.getLocalizedString("travel.search.mobile", "Mobile")), 1, row++);
									break;
								
							} 
							table.add(getSmallText(phone.getNumber()), 1, row++);
						}
					}
				}
				
				List emails = supplier.getEmails();
				if (emails != null) {
					Iterator iter = emails.iterator();
					Email email;
					while (iter.hasNext()) {
						email = (Email) iter.next();
						if (!"".equals(email.getEmailAddress())) {
							table.add(getText(iwrb.getLocalizedString("travel.search.email", "Email")), 1, row++);
							table.add(getSmallText(email.getEmailAddress()), 1, row++);
						}
					}
				}
			}
			
			String orgID = supplier.getOrganizationID();
			table.add(getText(iwrb.getLocalizedString("travel.organization_id", "Organization ID")), 1, row++);
			if (orgID != null) {
				table.add(getSmallText(orgID), 1, row++);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return table;
	}
	
	protected Text getText(String content) {
		Text text = new Text(content);
		if (getStyleName(BookingForm.STYLENAME_TEXT) != null) {
			text = getStyleText(text, BookingForm.STYLENAME_TEXT);
		}
		return text;
	}	
	
	protected Text getHeaderText(String content) {
		Text text = new Text(content);
		if (getStyleName(BookingForm.STYLENAME_HEADER_TEXT) != null) {
			text = getStyleText(text, BookingForm.STYLENAME_HEADER_TEXT);
		}		
		return text;
	}
	
	protected Text getSmallText(String content) {
		Text text = new Text(content);
		if (getStyleName(BookingForm.STYLENAME_SMALL_TEXT) != null) {
			text = getStyleText(text, BookingForm.STYLENAME_SMALL_TEXT);
		}
		return text;
	}	
	
	public void setCount(int count) {
		this.count = count;
	}
	
	/**
	 * @return
	 */
	protected int getNumberOfDays(IWContext iwc, IWTimestamp fromDate) {
		int	betw = 0;
		try {
			betw = Integer.parseInt(iwc.getParameter(AbstractSearchForm.PARAMETER_MANY_DAYS));
		} catch (NumberFormatException n) {
			String toParameter = iwc.getParameter(AbstractSearchForm.PARAMETER_TO_DATE);
			if (toParameter != null) {
				try {
					IWTimestamp toStamp = new IWTimestamp(toParameter);
					return IWTimestamp.getDaysBetween(fromDate, toStamp);
				} catch (Exception e) {
				}
			}
			logDebug("SearchForm : days set to 0");
		}
		return betw;
	}

	private int addProductHeader(Product product, Supplier supplier, Table table, int resultsRow) throws RemoteException {
		table.add(getHeaderText(supplier.getName()), 1, resultsRow);
		table.add(getSmallText(product.getProductName(localeID)), 3, resultsRow);
		table.setAlignment(3, resultsRow, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setCellpaddingLeft(1, resultsRow, 10);
		//table.setCellpadding(2, resultsRow, 5);
		table.setCellpaddingRight(3, resultsRow, 10);
		table.setHeight(resultsRow, 28);
		table.setRowStyleClass(resultsRow, super.getStyleName(BookingForm.STYLENAME_HEADER_BACKGROUND_COLOR));
		++resultsRow;
		table.setHeight(1, resultsRow, 1);
		++resultsRow;
		table.setRowStyleClass(resultsRow, super.getStyleName(BookingForm.STYLENAME_BLUE_BACKGROUND_COLOR));
		table.setHeight(1, resultsRow, 1);

		++resultsRow;
		table.setHeight(1, resultsRow, 3);

//		table.setRowStyleClass(++resultsRow, super.getStyleName(ServiceSearch.STYLENAME_BLUE_BACKGROUND_COLOR));
//		table.setHeight(1, resultsRow, 3);
		
		return ++resultsRow;
	}

	private Collection getProductPrices(Product usedProduct, int addressId, int timeframeId) throws RemoteException, FinderException {
		return getProductPriceHome().findProductPrices(usedProduct.getID(), timeframeId, addressId, new int[] {PriceCategoryBMPBean.PRICE_VISIBILITY_PUBLIC, PriceCategoryBMPBean.PRICE_VISIBILITY_BOTH_PRIVATE_AND_PUBLIC}, priceCategoryKey);
	}

	int addPrices(Table table, int column, int row, TravelStockroomBusiness bus, Product product, int timeframeId, int addressId, int days, String seperator) throws SQLException, RemoteException, FinderException {
		Collection prices = getProductPrices(product, addressId, timeframeId);
		ProductPrice price;
		Iterator iter = prices.iterator();
		int tmpPriceID = -1;
		while (iter.hasNext()) {
			price = (ProductPrice) iter.next();
			tmpPriceID = ((Integer)price.getPrimaryKey()).intValue();
			addPrices(table, column, row, bus, product.getID(), timeframeId, addressId, price, days, seperator);
		}
		return tmpPriceID;
	}
	
	private void addPrices(Table table, int column, int row, TravelStockroomBusiness bus, int productId, int timeframeId, int travelAddressId, ProductPrice pPrice, int days, String seperator) throws SQLException, RemoteException {
		float price = bus.getPrice(((Integer) pPrice.getPrimaryKey()).intValue(), productId ,pPrice.getPriceCategoryID() , pPrice.getCurrencyId(), IWTimestamp.getTimestampRightNow(), timeframeId, -1 );
		float total = -1;
		String returner = "";

		Currency currency;
		String currAbbr = "";
		try {
			currency = ((com.idega.block.trade.data.CurrencyHome)com.idega.data.IDOLookup.getHome(Currency.class)).findByPrimaryKeyLegacy(pPrice.getCurrencyId());
			currAbbr = currency.getCurrencyAbbreviation();
		}catch (Exception e) {
			currency = null;
		}
		TravelAddress tAddress = null;
		try {
			if (travelAddressId > 0) {
				tAddress = ((TravelAddressHome) IDOLookup.getHome(TravelAddress.class)).findByPrimaryKey(travelAddressId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		if (days == 0) {
			days = 1;
		}
		if (count == 0) {
			count = 1;
		}
		
		
		total = price * days * count;
		if (tAddress != null) {
			table.add(getText(tAddress.getName()+Text.BREAK), column, row);
		}
		table.add(getText(iwrb.getLocalizedString("travel.price","Price")+":"+seperator),column, row);
		table.add(getOrangeText(currencyFormat.format(price*count)+Text.NON_BREAKING_SPACE+currAbbr), column, row);
		if (days > 1) {
			table.add(getOrangeText(Text.NON_BREAKING_SPACE+bookingForm.getPerDayString()), column, row);
//			table.add(getOrangeText(Text.NON_BREAKING_SPACE+iwrb.getLocalizedString("travel.search.per_night","per night")), column, row);
		}
		
		if (count > 1) {
			table.add(getOrangeText(" ("+currencyFormat.format(price)+Text.NON_BREAKING_SPACE+currAbbr+" per "+bookingForm.getUnitName()+")"), column, row);
		}
		table.addBreak(column, row);
		table.add(getText(iwrb.getLocalizedString("travel.search.total","Total")+":"+seperator), column, row);
		table.add(getOrangeText(currencyFormat.format(total)+Text.NON_BREAKING_SPACE+currAbbr+seperator), column, row);
		table.addBreak(column, row);
	}	
	
	protected Text getOrangeText(String content) {
		Text text = new Text(content);
		if (getStyleName(BookingForm.STYLENAME_ORANGE_TEXT) != null) {
			text = getStyleText(text, BookingForm.STYLENAME_ORANGE_TEXT);
		}
		return text;
	}
		
	public void setProductInfoDetailed(Table table) {
		this.productInfoDetailed = table;
	}

	protected BasketBusiness getBasketBusiness(IWContext iwc) {
		try {
			return (BasketBusiness) IBOLookup.getSessionInstance(iwc, BasketBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	
}
