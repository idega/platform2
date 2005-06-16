/*
 * $Id: CreditCardReport.java,v 1.1 2005/06/16 09:02:37 gimmi Exp $
 * Created on 8.6.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.presentation;

import java.rmi.RemoteException;
import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DatePicker;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;


public class CreditCardReport extends TravelManager {
	
	private static final String PARAMETER_FROM_DATE = "ccr_f";
	private static final String PARAMETER_TO_DATE = "ccr_t";
	private static final String PARAMETER_CLIENT_TYPE = "ccr_clt";
//	private static final String PARAMETER_MERCHANT = "ccr_m";
	private static final String PARAMETER_CARD_TYPE = "ccr_ct";
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		
		Form form = new Form();
		Table table = new Table();
		table.setCellpaddingAndCellspacing(0);
		
		int row = 1;
		table.add(getSearchTable(iwc), 1, row++);
		table.add(getResultsTable(iwc), 1, row++);
		add(form);
	}
	
	private Table getResultsTable(IWContext iwc) {
		Table table = new Table();
		table.setCellpaddingAndCellspacing(0);
		
		return table;
	}
	
	private Table getSearchTable(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setCellpaddingAndCellspacing(0);
		
		DatePicker from = new DatePicker(PARAMETER_FROM_DATE);
		String sFrom = iwc.getParameter(PARAMETER_FROM_DATE);
		IWTimestamp iwFromDate = null;
		if (sFrom != null) {
			iwFromDate = new IWTimestamp(sFrom);
		} else {
			iwFromDate = IWTimestamp.RightNow();
		}
		from.setDate(iwFromDate.getDate());
		
		DatePicker to = new DatePicker(PARAMETER_TO_DATE);
		String sTo = iwc.getParameter(PARAMETER_TO_DATE);
		IWTimestamp iwToDate = null;
		if (sTo != null) {
			iwToDate = new IWTimestamp(sTo);
		} else {
			iwToDate = new IWTimestamp(iwFromDate);
			iwToDate.addDays(31);
		}
		to.setDate(iwToDate.getDate());
		
		DropdownMenu clientType = new DropdownMenu(PARAMETER_CLIENT_TYPE);
		clientType.addMenuElement(CreditCardMerchant.MERCHANT_TYPE_TPOS);
		clientType.addMenuElement(CreditCardMerchant.MERCHANT_TYPE_KORTHATHJONUSTAN);
		clientType.keepStatusOnAction();
		
		int row = 1;
		table.add(getText(getResourceBundle().getLocalizedString("from", "From")+" :"), 1, row);
		table.add(from, 2, row++);
		table.add(getText(getResourceBundle().getLocalizedString("to", "To")+" :"), 1, row);
		table.add(to, 2, row++);
		table.add(getText(getResourceBundle().getLocalizedString("travel.client_type", "Client Type")+" :"), 1, row);
		table.add(clientType, 2, row++);
		table.add(new SubmitButton(getResourceBundle().getLocalizedString("search", "Search")), 2, row++);
		
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_RIGHT);
		
		return table;
	}
	
}
