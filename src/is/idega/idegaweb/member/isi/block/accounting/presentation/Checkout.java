/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega software. Use is
 * subject to license terms.
 *  
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation;

import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry;
import is.idega.idegaweb.member.isi.block.accounting.data.PaymentType;
import is.idega.idegaweb.member.isi.block.accounting.data.PaymentTypeHome;

import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.FinderException;

import com.idega.block.basket.business.BasketBusiness;
import com.idega.block.basket.data.BasketEntry;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.user.business.UserBusiness;

/**
 * @author palli
 */
public class Checkout extends CashierSubWindowTemplate {

	protected static final String ACTION_PAY = "co_pay";

	protected static final String ACTION_REMOVE = "co_remove";

	protected static final String ACTION_UPDATE_DISCOUNT = "co_discount";

	private final static String LABEL_SELECTED_USER = "isi_acc_co_select_user";

	private final static String LABEL_USER = "isi_acc_co_user";

	private final static String LABEL_GROUP = "isi_acc_co_group";

	private final static String LABEL_DIVISION = "isi_acc_co_division";

	private final static String LABEL_INFO = "isi_acc_co_info";

	private final static String LABEL_AMOUNT = "isi_acc_co_amount";

	private final static String LABEL_AMOUNT_REMAINING = "isi_acc_co_remaining";

	private final static String LABEL_PAYMENT_TYPE = "isi_acc_co_payment_type";

	private final static String LABEL_DISCOUNT = "isi_acc_co_discount";

	private final static String LABEL_DISCOUNT_AMOUNT = "isi_acc_co_discount_amount";

	private final static String LABEL_DISCOUNT_INFO = "isi_acc_co_discount_info";

	private final static String LABEL_REMOVE_FROM_BASKET = "isi_acc_co_remove_from_basket";

	private final static String LABEL_TO_PAY = "isi_acc_co_to_pay";

	private final static int STATUS_VIEW_BASKET = 0;

	private final static int STATUS_INSERT_PAYMENT_INFO = 1;

	private final static int STATUS_ADD_TO_BASKET = 2;

	private final static int STATUS_REMOVE_ENTRIES = 10;

	private final static int STATUS_UPDATE_DISCOUNT = 20;

	private final static int STATUS_DONE = 99;

	private final static String DEFAULT_PLUGIN = "is.idega.idegaweb.member.isi.block.accounting.presentation.plugin.DefaultCheckoutPlugin";

	/**
	 *  
	 */
	public Checkout() {
		super();
	}

	private void addToBasket(IWContext iwc) {
		String basketCases[] = iwc
				.getParameterValues(SelectPayments.LABEL_ADD_TO_BASKET);

		try {
			if (basketCases.length != 0) {
				for (int i = 0; i < basketCases.length; i++) {
					FinanceEntry entry = getAccountingBusiness(iwc)
							.getFinanceEntryByPrimaryKey(
									new Integer(basketCases[i]));
					getBasketBusiness(iwc).addItem(entry);
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void updateDiscount(IWContext iwc) {
		try {
			Map entries = null;
			entries = getBasketBusiness(iwc).getBasket();
			if (entries != null && !entries.isEmpty()) {
				Iterator it = entries.keySet().iterator();
				int i = 0;

				while (it.hasNext()) {
					FinanceEntry entry = (FinanceEntry) ((BasketEntry) entries
							.get(it.next())).getItem();

					StringBuffer label = new StringBuffer(LABEL_DISCOUNT);
					label.append("_");
					label.append(entry.getPrimaryKey().toString());

					String disc = iwc.getParameter(label.toString());
					if (disc != null && !"".equals(disc)) {
						int perc = Integer.parseInt(disc);
						double discAmount = Math.round(entry.getAmount()
								* (double) perc / 100.0);

						StringBuffer info = new StringBuffer(
								LABEL_DISCOUNT_INFO);
						info.append("_");
						info.append(entry.getPrimaryKey().toString());

						String discInfo = iwc.getParameter(info.toString());

						entry.setDiscountPerc(perc);
						entry.setDiscountAmount(discAmount);
						entry.setDiscountInfo(discInfo);
						entry.store();
					}
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void removeFromBasket(IWContext iwc) {
		String basketCases[] = iwc.getParameterValues(LABEL_REMOVE_FROM_BASKET);

		try {
			if (getBasketBusiness(iwc).getBasket().size() == basketCases.length) {
				getBasketBusiness(iwc).emptyBasket();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		try {
			if (basketCases.length != 0) {
				for (int i = 0; i < basketCases.length; i++) {
					FinanceEntry entry = getAccountingBusiness(iwc)
							.getFinanceEntryByPrimaryKey(
									new Integer(basketCases[i]));
					getBasketBusiness(iwc).removeItem(entry);
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private int getCurrentStatus(IWContext iwc) {
		if (iwc.isParameterSet(ACTION_REMOVE)) {
			return STATUS_REMOVE_ENTRIES;
		} else if (iwc.isParameterSet(ACTION_PAY)) {
			return STATUS_INSERT_PAYMENT_INFO;
		} else if (iwc.isParameterSet(SelectPayments.ACTION_CHECKOUT)) {
			return STATUS_ADD_TO_BASKET;
		} else if (iwc.isParameterSet(ACTION_UPDATE_DISCOUNT)) {
			return STATUS_UPDATE_DISCOUNT;
		}

		return STATUS_VIEW_BASKET;
	}

	private void insertPayment(IWContext iwc) {
		String type = iwc.getParameter(LABEL_PAYMENT_TYPE);
		String amount = iwc.getParameter(LABEL_TO_PAY);

		PaymentType eType = null;
		if (type != null) {
			try {
				PaymentTypeHome pHome = (PaymentTypeHome) IDOLookup
						.getHome(PaymentType.class);
				eType = pHome.findByPrimaryKey(new Integer(type));
			} catch (IDOLookupException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			}
		}

		if (eType != null) {
			String pluginName = eType.getPlugin();
			if (pluginName == null || "".equals(pluginName)) {
				pluginName = DEFAULT_PLUGIN;
			}

			try {
				CheckoutPlugin plugin = (CheckoutPlugin) Class.forName(
						pluginName).newInstance();
				
				boolean checkoutCompleted = plugin.checkOut(iwc, type, amount);
				add(plugin.showPlugin(iwc));
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}

		}

	}

	/**
	 *  
	 */
	public void main(IWContext iwc) {
		int status = getCurrentStatus(iwc);

		switch (status) {
			case STATUS_VIEW_BASKET :
				viewBasket(iwc);
				break;
			case STATUS_INSERT_PAYMENT_INFO :
				//@TODO plugin stuff
				insertPayment(iwc);
				break;
			case STATUS_DONE :
				break;
			case STATUS_REMOVE_ENTRIES :
				removeFromBasket(iwc);
				viewBasket(iwc);
				break;
			case STATUS_ADD_TO_BASKET :
				addToBasket(iwc);
				viewBasket(iwc);
				break;
			case STATUS_UPDATE_DISCOUNT :
				updateDiscount(iwc);
				viewBasket(iwc);
				break;
			default :
				viewBasket(iwc);
				break;
		}
	}

	private void viewBasket(IWContext iwc) {
		Form f = new Form();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		Table inputTable = new Table();
		inputTable.setCellpadding(5);
		Table paymentTable = new Table();
		paymentTable.setCellpadding(5);

		Text labelUser = new Text(iwrb.getLocalizedString(LABEL_USER, "User"));
		labelUser.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelDiv = new Text(iwrb.getLocalizedString(LABEL_DIVISION,
				"Division"));
		labelDiv.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelGroup = new Text(iwrb
				.getLocalizedString(LABEL_GROUP, "Group"));
		labelGroup.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelInfo = new Text(iwrb.getLocalizedString(LABEL_INFO, "Info"));
		labelInfo.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelAmount = new Text(iwrb.getLocalizedString(LABEL_AMOUNT,
				"Amount"));
		labelAmount.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelRemaining = new Text(iwrb.getLocalizedString(
				LABEL_AMOUNT_REMAINING, "Remaining"));
		labelRemaining.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelDiscount = new Text(iwrb.getLocalizedString(LABEL_DISCOUNT,
				"Discount"));
		labelDiscount.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelDiscountInfo = new Text(iwrb.getLocalizedString(
				LABEL_DISCOUNT_INFO, "Discount info"));
		labelDiscountInfo.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelPaymentType = new Text(iwrb.getLocalizedString(
				LABEL_PAYMENT_TYPE, "Payment type"));
		labelPaymentType.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelDiscountAmount = new Text(iwrb.getLocalizedString(
				LABEL_DISCOUNT_AMOUNT, "Discount amount"));
		labelDiscountAmount.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelToPay = new Text(iwrb.getLocalizedString(LABEL_TO_PAY,
				"To pay"));
		labelToPay.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

		SubmitButton pay = new SubmitButton(iwrb.getLocalizedString(ACTION_PAY,
				"Pay"), ACTION_PAY, "pay");

		Map entries = null;
		try {
			entries = getBasketBusiness(iwc).getBasket();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		if (entries == null || entries.isEmpty()) {
			pay.setDisabled(true);
		}

		int row = 1;
		CheckBox checkAll = new CheckBox("checkall");
		checkAll.setToCheckOnClick(LABEL_REMOVE_FROM_BASKET, "this.checked");
		paymentTable.add(checkAll, 1, row);
		paymentTable.add(labelUser, 2, row);
		paymentTable.add(labelDiv, 3, row);
		paymentTable.add(labelGroup, 4, row);
		paymentTable.add(labelInfo, 5, row);
		paymentTable.add(labelAmount, 6, row);
		paymentTable.setAlignment(6, row, "RIGHT");
		paymentTable.add(labelRemaining, 7, row);
		paymentTable.setAlignment(7, row, "RIGHT");
		paymentTable.add(labelDiscount, 8, row);
		paymentTable.add(labelDiscountInfo, 9, row++);

		NumberFormat nf = NumberFormat.getInstance(iwc.getCurrentLocale());
		nf.setMaximumFractionDigits(0);
		double sumTotalRemaining = 0.0;
		double sumTotalDiscount = 0.0;

		if (entries != null && !entries.isEmpty()) {
			Iterator it = entries.keySet().iterator();
			int i = 0;

			while (it.hasNext()) {
				FinanceEntry entry = (FinanceEntry) ((BasketEntry) entries
						.get(it.next())).getItem();
				CheckBox removeFromBasket = new CheckBox(
						LABEL_REMOVE_FROM_BASKET, entry.getPrimaryKey()
								.toString());
				paymentTable.add(removeFromBasket, 1, row);
				if (entry.getUser() != null) {
					paymentTable.add(entry.getUser().getName(), 2, row);
				}
				if (entry.getDivision() != null) {
					paymentTable.add(entry.getDivision().getName(), 3, row);
				}
				if (entry.getGroup() != null) {
					paymentTable.add(entry.getGroup().getName(), 4, row);
				}
				paymentTable.add(entry.getInfo(), 5, row);
				paymentTable.add(nf.format(entry.getAmount()), 6, row);
				paymentTable.add(nf.format(entry.getItemPrice().doubleValue()),
						7, row);
				paymentTable.setAlignment(6, row, "RIGHT");
				paymentTable.setAlignment(7, row, "RIGHT");

				sumTotalRemaining += entry.getItemPrice().doubleValue();
				sumTotalDiscount += entry.getDiscountAmount();

				if (entry.getDiscountPerc() == 0.0
						|| entry.getAmountEqualized() == 0.0) {
					StringBuffer label = new StringBuffer(LABEL_DISCOUNT);
					label.append("_");
					label.append(entry.getPrimaryKey().toString());
					TextInput discInput = new TextInput(label.toString());
					discInput.setAsIntegers();
					if (entry.getDiscountPerc() != 0.0) {
						discInput.setValue((int) (Math.round(entry
								.getDiscountPerc())));
					}
					paymentTable.add(discInput, 8, row);
				} else {
					paymentTable.add(entry.getDiscountInfo(), 8, row);
				}

				if (entry.getDiscountInfo() == null
						|| entry.getAmountEqualized() == 0.0) {
					StringBuffer label = new StringBuffer(LABEL_DISCOUNT_INFO);
					label.append("_");
					label.append(entry.getPrimaryKey().toString());
					TextInput discInfoInput = new TextInput(label.toString());
					if (entry.getDiscountInfo() != null) {
						discInfoInput.setValue(entry.getDiscountInfo());
					}
					paymentTable.add(discInfoInput, 9, row);
				} else {
					paymentTable.add(entry.getDiscountInfo(), 9, row);
				}

				row++;
				i++;
			}

			SubmitButton removeFromBasket = new SubmitButton(iwrb
					.getLocalizedString(ACTION_REMOVE, "Remove from basket"),
					ACTION_REMOVE, "remove_from_basket");
			removeFromBasket.setToEnableWhenChecked(LABEL_REMOVE_FROM_BASKET);
			paymentTable.add(removeFromBasket, 8, row);
			paymentTable.setAlignment(8, row, "RIGHT");

			SubmitButton updateDiscount = new SubmitButton(iwrb
					.getLocalizedString(ACTION_UPDATE_DISCOUNT,
							"Update discount"), ACTION_UPDATE_DISCOUNT,
					"discount");
			//            removeFromBasket.setToEnableWhenChecked(LABEL_REMOVE_FROM_BASKET);
			paymentTable.add(updateDiscount, 9, row);
			paymentTable.setAlignment(9, row, "RIGHT");
		}

		row = 1;
		inputTable.add(labelRemaining, 1, row);
		inputTable.add(labelDiscountAmount, 2, row);
		inputTable.add(labelToPay, 3, row);
		inputTable.add(labelPaymentType, 4, row++);

		inputTable.add(nf.format(sumTotalRemaining), 1, row);
		inputTable.add(nf.format(sumTotalDiscount), 2, row);
		TextInput toPayInput = new TextInput(LABEL_TO_PAY);
		int value = (int) sumTotalRemaining;
		toPayInput.setValue(value);
		inputTable.add(toPayInput, 3, row);
		Collection types = null;
		try {
			types = getAccountingBusiness(iwc).findAllPaymentTypes();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		DropdownMenu typeInput = new DropdownMenu(LABEL_PAYMENT_TYPE);
		SelectorUtility util = new SelectorUtility();
		if (types != null && !types.isEmpty()) {
			typeInput = (DropdownMenu) util.getSelectorFromIDOEntities(
					typeInput, types, "getLocalizationKey", iwrb);
		}
		inputTable.add(typeInput, 4, row);
		inputTable.add(pay, 5, row);

		f.add(inputTable);
		f.add(paymentTable);
		f.maintainParameter(CashierWindow.ACTION);
		f.maintainParameter(CashierWindow.PARAMETER_GROUP_ID);
		f.maintainParameter(CashierWindow.PARAMETER_DIVISION_ID);
		f.maintainParameter(CashierWindow.PARAMETER_CLUB_ID);

		add(f);
	}

	// service method
	private UserBusiness getUserBusiness(IWContext iwc) {
		try {
			return (UserBusiness) IBOLookup.getServiceInstance(iwc,
					UserBusiness.class);
		} catch (RemoteException ex) {
			throw new RuntimeException(ex.getMessage());
		}
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