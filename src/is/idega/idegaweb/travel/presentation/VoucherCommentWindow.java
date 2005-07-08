/*
 * $Id: VoucherCommentWindow.java,v 1.1 2005/07/08 14:11:33 gimmi Exp $
 * Created on Jul 8, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.presentation;

import java.rmi.RemoteException;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;


public class VoucherCommentWindow extends TravelWindow {
	
	private static final String ACTION = "vcw_a";
	private static final String ACTION_SAVE = "vcw_as";
	static final String PARAMETER_PRODUCT_ID = "vcw_pid";
	private static final String PARAMETER_COMMENT = "vcw_vc";
	
	private IWResourceBundle iwrb = null;
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		iwrb = super.getResourceBundle(iwc);
		String pID = iwc.getParameter(PARAMETER_PRODUCT_ID);
		
		add(Text.BREAK);
		if (pID == null) {
			add(getText(iwrb.getLocalizedString("travel.no_product_selected", "No product selected")));
		} else {
			String action = iwc.getParameter(ACTION);
			if (action != null && action.equals(ACTION_SAVE)) {
				saveComment(iwc);
			}
			addForm(iwc);
		}
	}
	
	private void saveComment(IWContext iwc) throws NumberFormatException, RemoteException, FinderException {
		Product product = getProductBusiness(iwc).getProduct(new Integer(iwc.getParameter(PARAMETER_PRODUCT_ID)));
		String comment = iwc.getParameter(PARAMETER_COMMENT);
		if (comment != null) {
			product.setVoucherComment(comment);
			product.store();
		}
	}
	
	public void addForm(IWContext iwc) throws NumberFormatException, RemoteException, FinderException {
		Product product = getProductBusiness(iwc).getProduct(new Integer(iwc.getParameter(PARAMETER_PRODUCT_ID)));
		
		Form form = new Form();
		form.maintainParameter(PARAMETER_PRODUCT_ID);
		Table table = TravelManager.getTable();
		table.setAlignment(Table.HORIZONTAL_ALIGN_CENTER);
		form.add(table);
		int row = 1;
		
		table.add(TravelManager.getHeaderText(iwrb.getLocalizedString("travel.voucher_comment", "Voucher Comment")), 1, row);
		table.setRowColor(row++, TravelManager.backgroundColor);
		
		TextArea comment = new TextArea(PARAMETER_COMMENT);
		comment.setWidth("300");
		comment.setHeight("70");
		comment.setMaximumCharacters(1000);
		if (product.getVoucherComment() != null) {
			comment.setContent(product.getVoucherComment());
		}
		table.add(comment, 1, row);
		table.setRowColor(row++, TravelManager.GRAY);
		
		SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("travel.save", "Save"), ACTION, ACTION_SAVE);
		table.add(save, 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setRowColor(row++, TravelManager.GRAY);
		add(form);
	}
	
	
}
