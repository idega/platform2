package com.idega.block.trade.presentation;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import com.idega.block.trade.business.CurrencyBusiness;
import com.idega.block.trade.business.CurrencyHolder;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Table;
import com.idega.presentation.remotescripting.RemoteScriptHandler;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.TextInput;
import com.idega.util.text.TextSoap;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class CurrencyCalculator extends PresentationObjectContainer {

	public static final String LAYER_ID = "calculated_price";

	public static  String PARAMETER_FROM_CURRENCY = "from";
	public static  String PARAMETER_TO_CURRENCY = "to";
	public static  String PARAMETER_PRICE = "price";
	private String parameterAll = "ALL";
	private String PARAMETER_REMOTE_CALL = "prm_rc";
	
	private IWResourceBundle iwrb;
	private IWBundle bundle;
	private List extraParameters;
	
	private boolean useRemoteScripting = false;
	private boolean calculateOnType = false;

	private String fontStyleClass = null;
	private String ioStyleClass = null;
	private String linkStyleClass = null;
	
	private int objectSpacing = 0;
	
	public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.trade";
	
	public CurrencyCalculator() {
	}
	
	public void main(IWContext iwc) {
		init(iwc);
		displayForm(iwc);
	}
		
	public String getBundleIdentifier(){
		return IW_BUNDLE_IDENTIFIER;
	}
	
	
	private void init(IWContext iwc) {
		bundle = getBundle(iwc);
		iwrb = bundle.getResourceBundle(iwc);
	}
	
	private void displayForm(IWContext iwc) {
		Form form = new Form();
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		form.add(table);
		if (extraParameters != null) {
			Iterator iter = extraParameters.iterator();
			while (iter.hasNext()) {
				form.maintainParameter(iter.next().toString());
			}
		}

		DropdownMenu from = new DropdownMenu(PARAMETER_FROM_CURRENCY);
		DropdownMenu to   = new DropdownMenu(PARAMETER_TO_CURRENCY);
		List currencyList = CurrencyBusiness.getCurrencyList();
		Iterator iter = currencyList.iterator();
		while (iter.hasNext()) {
			CurrencyHolder holder = (CurrencyHolder) iter.next();
			from.addMenuElement(holder.getCurrencyName(), holder.getCurrencyName());
			to.addMenuElement(holder.getCurrencyName(), holder.getCurrencyName());
		}
		from.setSelectedElement(CurrencyBusiness.defaultCurrency);
		
		if (!useRemoteScripting) {
			to.addMenuElement(this.parameterAll,"ALL");
		}
		TextInput price = new TextInput(PARAMETER_PRICE);

		TextInput res = new TextInput("resultOutput");
		res.setDisabled(true);
		
		Layer resultText = new Layer();
		resultText.setID("resultText");
		if (fontStyleClass != null) {
			resultText.setStyleClass(fontStyleClass);
		}
		
		RemoteScriptHandler rsh = null;
		
		if (useRemoteScripting) {
			rsh = new RemoteScriptHandler(price, resultText);
			try {
				rsh.setRemoteScriptCollectionClass(CurrencyCalculationCollectionHandler.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			rsh.setIsSourceTrigger(false);
			add(rsh);
		}
		
		String sFrom = iwc.getParameter(this.PARAMETER_FROM_CURRENCY);
		String sTo = iwc.getParameter(this.PARAMETER_TO_CURRENCY);
		String sPrice = iwc.getParameter(this.PARAMETER_PRICE);
		if (sPrice != null)
			sPrice = TextSoap.findAndReplace(sPrice, ',', '.');
		
		if (sTo == null || !sTo.equals(this.parameterAll)) {
			
			price.setSize(10);
			res.setSize(10);
			if (sPrice != null) price.setContent(sPrice);
			if (sTo != null) to.setSelectedElement(sTo);
			if (sFrom != null) from.setSelectedElement(sFrom);
			
			table.add(getText(iwrb.getLocalizedString("from", "From")), 1,1);
			table.add(getText(iwrb.getLocalizedString("to","To")), 2,1);
			table.add(getText(iwrb.getLocalizedString("price","Price")), 1,3);
			table.add(getText(iwrb.getLocalizedString("new_price","New price")), 3,3);
			table.add(from, 1,2);
			table.add(to, 2,2);
			table.add(price, 1,4);
			
			if (ioStyleClass != null) {
				from.setStyleClass(ioStyleClass);
				to.setStyleClass(ioStyleClass);
				price.setStyleClass(ioStyleClass);
				res.setStyleClass(ioStyleClass);
			}
			
			
			if (!useRemoteScripting) {
				table.add(getText(getNewPrice(iwc)), 3, 4);
			} else {
				from.setOnChange(rsh.getSubmitEvent(iwc));
				to.setOnChange  (rsh.getSubmitEvent(iwc));
				if (calculateOnType) {
					price.setOnKeyUp(rsh.getSubmitEvent(iwc));
				}
				table.add(resultText, 3, 4);
			}
			table.setAlignment(3,4,"right");
			
			Link calc = getLink(iwrb.getLocalizedString("calculate", "Calculate"));
			if (!useRemoteScripting) {
				calc.setToFormSubmit(form);
			} else {
				calc.setOnClick(rsh.getSubmitEvent(iwc));
			}
			table.add(Text.getNonBrakingSpace(), 2, 4);
			table.add(calc, 2, 4);

			if (objectSpacing > 0) {
				table.setCellpaddingBottom(1, 1, objectSpacing);
				table.setCellpaddingBottom(2, 1, objectSpacing);
				table.setCellpaddingBottom(1, 2, objectSpacing);
				table.setCellpaddingBottom(2, 2, objectSpacing);
				table.setCellpaddingBottom(1, 3, objectSpacing);
				table.setCellpaddingBottom(2, 3, objectSpacing);
				table.setCellpaddingBottom(3, 3, objectSpacing);
				table.setCellpaddingBottom(1, 4, objectSpacing);
				table.setCellpaddingBottom(2, 4, objectSpacing);
			}
			
			add(form);
		}else {
			List list = CurrencyBusiness.getCurrencyList();
			if (list != null) {
				int row = 1;
				table.add(getText(iwrb.getLocalizedString("from", "From")), 1,1);
				table.add(getText(iwrb.getLocalizedString("to","To")), 3,1);
				CurrencyHolder holder;
				for (int i = 0; i < list.size(); i++) {
					++row;
					holder = (CurrencyHolder) list.get(i);
					table.add(sPrice+Text.NON_BREAKING_SPACE+sFrom, 1,row);
					table.add(getText(" = "), 2,row);
					try {
						table.add(getText(TextSoap.decimalFormat(Float.toString(CurrencyBusiness.convertCurrency(sFrom, holder.getCurrencyName(), Float.parseFloat(sPrice))), 2)+Text.NON_BREAKING_SPACE+holder.getCurrencyName()), 3,row);
					}catch (NumberFormatException n) {
						table.add(getText("- "+holder.getCurrencyName()),3, row);
					}
				}
				++row;
				table.mergeCells(1, row, 3 ,row);
				table.setAlignment(1, row, "center");
				table.add(new BackButton(iwrb.getLocalizedImageButton("back", "Back")), 1, row);
			}
			add(table);
		}
		add(Text.BREAK);
		add(getDisclamer(iwc));
	}

	private Table getDisclamer(IWContext iwc) {
		Table table = new Table();
		table.setAlignment(1,1,Table.HORIZONTAL_ALIGN_LEFT);
		if (CurrencyBusiness.getCurrencyUrl() != null) {
			table.add(getText(iwrb.getLocalizedString("uses_latest_rates", "Uses latest available rates")+ ". ("+CurrencyBusiness.getCurrencyUrl()+")"));
		} else {
			table.add(getText(iwrb.getLocalizedString("displayed_without_guarantee","Displayed without guarantee")));
		}

		table.addBreak();
		table.add(getText(iwrb.getLocalizedString("last_update_at", "Last update at")+ " : "));
		if (CurrencyBusiness.getLastUpdate() != null) {
			table.add(getText(CurrencyBusiness.getLastUpdate().getLocaleDateAndTime(iwc.getLocale())));
		} else {
			table.add(getText(iwrb.getLocalizedString("unknown", "Unknown")));
		}
		
		return table;
	}
	
	private String getNewPrice(IWContext iwc) {
		String price = "";
		
		try {
			String sFrom = iwc.getParameter(this.PARAMETER_FROM_CURRENCY);
			String sTo = iwc.getParameter(this.PARAMETER_TO_CURRENCY);
			String sPrice = iwc.getParameter(this.PARAMETER_PRICE);
			
			if (sFrom != null && sTo != null && sPrice != null && !sPrice.equals("")) {
				sPrice = TextSoap.findAndReplace(sPrice, ',', '.');
				price = TextSoap.decimalFormat(Float.toString(CurrencyBusiness.convertCurrency(sFrom, sTo, Float.parseFloat(sPrice))), 2)+Text.NON_BREAKING_SPACE+sTo;
			}
			
		}catch (NumberFormatException n) {
			price = "-";
		}
		return price;
	}
	
	private Text getText(String content) {
		Text text = new Text(content);
		if (fontStyleClass != null) {
			text.setStyleClass(fontStyleClass);
		} else {
			text.setBold(true);
		}
		return text;
	}
	
	public Link getLink(String content) {
		Text text = new Text(content);
		if (linkStyleClass != null) {
			text.setStyleClass(linkStyleClass);
		}
		Link link = new Link(text);
		return link;
	}
	
	public void setLinkStyleClass(String linkStyleClass) {
		this.linkStyleClass = linkStyleClass;
	}

	public void setFontStyleClass(String fontStyleClass) {
		this.fontStyleClass = fontStyleClass;
	}
	
	public void setInterfaceObjectStyleClass(String styleClass) {
		this.ioStyleClass = styleClass;
	}
	
	public void setCalculateOnType(boolean calculateOnType) {
		this.calculateOnType = calculateOnType;
	}
	
	public void maintainParameter(String name) {
		if (extraParameters == null) {
			extraParameters = new Vector();
		}
		extraParameters.add(name);
	}
	
	public void setUseRemoteScripting(boolean useRemoteScripting) {
		this.useRemoteScripting = useRemoteScripting;
	}
}
