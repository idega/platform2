/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation.plugin;

import is.idega.idegaweb.member.isi.block.accounting.business.FinanceExtraBasketInfo;
import is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRound;
import is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundHome;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryHome;
import is.idega.idegaweb.member.isi.block.accounting.presentation.CashierSubWindowTemplate;
import is.idega.idegaweb.member.isi.block.accounting.presentation.CheckoutPlugin;

import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.idega.block.basket.business.BasketBusiness;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;

/**
 * The simple default plugin for the payment types that do not specify a plugin.
 * Just displays a list of the entries paid and a print button.
 * 
 * @author palli
 *  
 */
public class DefaultCheckoutPlugin extends CashierSubWindowTemplate
		implements
			CheckoutPlugin {
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi.block.accounting";

	protected static final String LABEL_DIVISION = "isi_acc_dcp_div";
	protected static final String LABEL_GROUP = "isi_acc_dcp_group";
	protected static final String LABEL_INFO = "isi_acc_dcp_info";
	protected static final String LABEL_USER = "isi_acc_dcp_user";
	protected static final String LABEL_AMOUNT = "isi_acc_dcp_amount";
	protected static final String LABEL_AMOUNT_PAID = "isi_acc_dcp_amount_paid";
	protected static final String LABEL_SUM = "isi_acc_dcp_sum";
	
	protected static final String LABEL_RECEIPT = "isi_acc_dcp_receipt";

	private String backTableStyle = "back";
	private String borderTableStyle = "borderAll";

	/*
	 * (non-Javadoc)
	 * 
	 * @see is.idega.idegaweb.member.isi.block.accounting.presentation.CheckoutPlugin#checkOut(com.idega.presentation.IWContext,
	 *      java.lang.String, java.lang.String)
	 */
	public boolean checkOut(IWContext iwc, String type, String amount) {
		try {
			Map basket = getBasketBusiness(iwc).getBasket();
			getAccountingBusiness(iwc).insertPayment(type, amount,
					iwc.getCurrentUser(), basket, iwc);

			return true;
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	private FinanceEntryHome getFinanceEntryHome() {
		try {
			return (FinanceEntryHome) IDOLookup.getHome(FinanceEntry.class);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		}

		return null;
	}

	private AssessmentRoundHome getAssessmentRoundHome() {
		try {
			return (AssessmentRoundHome) IDOLookup
					.getHome(AssessmentRound.class);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see is.idega.idegaweb.member.isi.block.accounting.presentation.CheckoutPlugin#showPlugin(com.idega.presentation.IWContext)
	 */
	public PresentationObject showPlugin(IWContext iwc, String type, String amount) {
		Table returnObject = new Table();
		
		IWResourceBundle iwrb = getResourceBundle(iwc);
		Table t = new Table();
		t.setCellpadding(5);

		int row = 1;
		Text labelDiv = new Text(iwrb.getLocalizedString(LABEL_DIVISION,
				"Division"));
		labelDiv.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelGroup = new Text(iwrb
				.getLocalizedString(LABEL_GROUP, "Group"));
		labelGroup.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelUser = new Text(iwrb.getLocalizedString(LABEL_USER, "User"));
		labelUser.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelInfo = new Text(iwrb.getLocalizedString(LABEL_INFO, "Info"));
		labelInfo.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelAmount = new Text(iwrb.getLocalizedString(LABEL_AMOUNT,
				"Amount"));
		labelAmount.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelAmountPaid = new Text(iwrb.getLocalizedString(LABEL_AMOUNT_PAID,
		"Amount paid"));
		labelAmountPaid.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelSum = new Text(iwrb.getLocalizedString(LABEL_SUM, "Sum"));
		labelSum.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

		t.add(labelDiv, 1, row);
		t.add(labelGroup, 2, row);
		t.add(labelUser, 3, row);		
		t.add(labelInfo, 4, row);
		t.add(labelAmount, 5, row);
		t.add(labelAmountPaid, 6, row);
		row++;

		NumberFormat nf = NumberFormat.getInstance(iwc.getCurrentLocale());
		nf.setMaximumFractionDigits(0);

		try {
			List paid = null;
            try {
                paid = getBasketBusiness(iwc).getExtraInfo();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
            
            if (paid != null && !paid.isEmpty()) {
				Iterator it = paid.iterator();
				double sum = 0;
				while (it.hasNext()) {
					FinanceExtraBasketInfo info = (FinanceExtraBasketInfo) it.next();
					if (info.getDivision() != null) {
						t.add(info.getDivision().getName(), 1, row);
					}
					if (info.getGroup() != null) {
					    t.add(info.getGroup().getName(), 2, row);
					}
					t.add(info.getUser().getName(), 3, row);
					if (info.getInfo() != null) {
						t.add(info.getInfo(), 4, row);
					}
					t.add(nf.format(info.getAmount().doubleValue()), 5, row);
					t.setAlignment(5, row, "RIGHT");
					t.add(nf.format(info.getAmountPaid()), 6, row);
					t.setAlignment(6, row, "RIGHT");
					sum += info.getAmountPaid();
					row++;
				}
				t.mergeCells(1, row, 6, row);
				t.add("<hr>", 1, row++);
				t.add(labelSum, 5, row);
				t.add(nf.format(sum), 6, row);
				t.setAlignment(6, row, "RIGHT");
			}            
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		returnObject.add(t);
		Link receipt = new Link(iwrb.getLocalizedString(LABEL_RECEIPT,"Receipt"));
		receipt.setWindowToOpen(DefaultCheckoutReceiptWindow.class);
		returnObject.add(receipt);

		return returnObject;
	}
	
	//session business
	private BasketBusiness getBasketBusiness(IWContext iwc) {
		try {
			return (BasketBusiness) IBOLookup.getSessionInstance(iwc,
					BasketBusiness.class);
		} catch (IBOLookupException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}