/*
 * $Id: CashierQueueViewer.java,v 1.1 2005/07/07 02:59:05 gimmi Exp $
 * Created on Jul 6, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.block.search.business.SearchEventListener;
import is.idega.idegaweb.travel.block.search.business.ServiceSearchBusinessBean;
import is.idega.idegaweb.travel.data.CashierQueue;
import is.idega.idegaweb.travel.data.CashierQueueHome;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import com.idega.block.trade.stockroom.business.TradeConstants;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORuntimeException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;


public class CashierQueueViewer extends TravelManager {
	
	private static final String ACTION = "cqv_a";
	private static final String ACTION_DELETE = "cqv_ad";
	private static final String PARAMETER_QUEUE_ID = "cqv_qi";
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		
		delete(iwc);
		
		add(Text.BREAK);
		if (hasRole(iwc, TradeConstants.ROLE_SUPPLIER_MANAGER_CASHIER_STAFF)) {
			showList();
		}
	}
	
	private void delete(IWContext iwc) {
		String id = iwc.getParameter(PARAMETER_QUEUE_ID);
		if (id != null) {
			try {
				CashierQueue queue = getCashierQueueHome().findByPrimaryKey(new Integer(id));
				queue.removeAndDisableBookings();
			}
			catch (NumberFormatException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
			catch (EJBException e) {
				e.printStackTrace();
			}
			catch (RemoveException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void showList() throws RemoteException {
		
		try {
			IWResourceBundle iwrb = getResourceBundle();
			Collection queueItems = getCashierQueueHome().findAllBySupplierManager(getSupplierManager());
			Table table = getTable();
			table.setWidth("60%");
			int row = 1;
			table.add(getHeaderText(iwrb.getLocalizedString("travel.cashier_queue", "Cashier Queue")), 1, row);
			table.mergeCells(1, row, 4, row);
			table.setRowColor(row++, backgroundColor);
			
			if (queueItems == null || queueItems.isEmpty()) {
				table.add(getText(iwrb.getLocalizedString("travel.nothing_in_queue", "Nothing in queue")), 1, row);
				table.mergeCells(1, row, 4, row);
				table.setRowColor(row++, GRAY);
			} else {
				Iterator iter = queueItems.iterator();
				CashierQueue queueItem;
				Link book;
				Link delete;
				while (iter.hasNext()) {
					queueItem = (CashierQueue) iter.next();
					table.add(getText(queueItem.getPrimaryKey().toString()), 1, row);
					table.add(getText(queueItem.getClientName()), 2, row);
					
					book = new Link(getText(iwrb.getLocalizedString("travel.book", "Book")), SupplierBrowserBookingForm.class);
					book.setEventListener(SearchEventListener.class);
					book.addParameter(SupplierBrowserBookingForm.PARAMETER_CASHIER_QUEUE_ID, queueItem.getPrimaryKey().toString());
					Collection bookingIDs = queueItem.getBookingIDs();
					if (bookingIDs != null && !bookingIDs.isEmpty()) {
						Iterator biter = bookingIDs.iterator();
						while (biter.hasNext()) {
							book.addParameter(ServiceSearchBusinessBean.PARAMETER_BOOKING_IDS_FOR_BASKET, biter.next().toString());
						}
					}
					table.add(book, 3, row);
					
					delete = new Link(getText(iwrb.getLocalizedString("travel.link_delete", " Delete ")));
					delete.addParameter(ACTION, ACTION_DELETE);
					delete.setOnClick("return confirm('"+super.getResourceBundle().getLocalizedString("travel.are_you_sure","Are you sure")+"?');");
					delete.addParameter(PARAMETER_QUEUE_ID, queueItem.getPrimaryKey().toString());
					table.add(delete, 4, row);
					
					table.setRowColor(row++, GRAY);
				}
			}
			table.setWidth(1, "40");
			table.setWidth(3, "60");
			table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_RIGHT);
			table.setWidth(4, "60");
			table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_RIGHT);
			add(table);
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		
	}
	
	private CashierQueueHome getCashierQueueHome() {
		try {
			return (CashierQueueHome) IDOLookup.getHome(CashierQueue.class);
		}
		catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}
}
