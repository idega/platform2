package com.idega.block.trade.stockroom.presentation;

import java.rmi.RemoteException;

import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.data.Product;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.SendMail;

/**
 * @author gimmi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ProductItemProductInfoRequest extends ProductItem {

	private String PARAMETER_EMAIL = "pipir_eml";
	private String PARAMETER_ACTION = "pipir_act";
	private String PARAMETER_ACTION_SUBMIT = "pipir_actsb";

	private String SUBJECT_KEY = "trade.product_item_info_request_subject";
	private String BODY_KEY = "trade.product_item_info_request_body";
	
	private String mailserver = "mail.idega.is";
	private String toaddress;
	private String fromaddress;
	private boolean useInput = false;
	private boolean showProductNumber = true;
	

	public ProductItemProductInfoRequest() { }
	
	public ProductItemProductInfoRequest(int productId) throws RemoteException, FinderException{
		super(productId);
	}
	public ProductItemProductInfoRequest(Product product) throws RemoteException{
		super(product);
	}

	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		
		if (this._product != null) {
			String action = iwc.getParameter(this.PARAMETER_ACTION);
			
			if (action == null) {
				drawObject(iwc);
			}	else if (action.equals(this.PARAMETER_ACTION_SUBMIT)){
				if (send(iwc)) {
					sendSuccess();	
				}else {
					sendFailed();
				}
			}
		}else {
			add(getText("Info request form"));
		}	
	}
	
	private boolean send(IWContext iwc) throws RemoteException {
		if (this.useInput) {
			this.fromaddress = iwc.getParameter(this.PARAMETER_EMAIL);	
		}
		String text = this._iwrb.getLocalizedString(this.BODY_KEY, "Information is requested about the following product: "+getProductText(iwc));
		SendMail sm = new SendMail();
		try {
			SendMail.send(this.fromaddress, this.toaddress, null, null, this.mailserver, this._iwrb.getLocalizedString(this.SUBJECT_KEY, "Product information request"), text);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void sendSuccess() {
		add(super.getText(this._iwrb.getLocalizedString("trade.send_successful","Request has been sent.")));	
	}
	
	public void sendFailed() {
		add(super.getText(this._iwrb.getLocalizedString("trade.send_failed","Request was not sent, try again later.")));	
	}


	private void drawObject(IWContext iwc) throws RemoteException{
		Form form = new Form();
		Table table = new Table();
		form.add(table);
		form.maintainParameter(getProductBusiness(iwc).getProductIdParameter());
		
		TextInput email = new TextInput(this.PARAMETER_EMAIL);
		SubmitButton submit = new SubmitButton(this._iwrb.getLocalizedImageButton("trade.submit","Submit"), this.PARAMETER_ACTION, this.PARAMETER_ACTION_SUBMIT);
		
		
		table.add(this._iwrb.getLocalizedString("trade.request_more_info", "Request more information"), 1, 1);
		table.mergeCells(1, 1, 2, 1);
		table.add(this._iwrb.getLocalizedString("trade.email","Email"), 1, 2);
		table.add(email, 2, 2);
		table.add(submit, 2, 3);
		table.setAlignment(2, 3, Table.HORIZONTAL_ALIGN_RIGHT);
		
		add(form);
	}
	
	public String getProductText(IWContext iwc) throws RemoteException {
		if (this.showProductNumber) {
			return super.getProductBusiness(iwc).getProductNameWithNumber(this._product);
		}else {
			return this._product.getProductName(iwc.getCurrentLocaleId());
		}	
	}
	
	public void setMailserver(String mailserver) {
		this.mailserver = mailserver;
	}
	
	public void setToMailAddress(String toMailAddress) {
		this.toaddress = toMailAddress;	
	}
	
	public void setFromMailAddress(String fromMailAddress) {
		this.fromaddress = fromMailAddress;	
	}
	
	public void setFromMailAddressToFormInput(boolean useInputAsFromAddress) {
		this.useInput = useInputAsFromAddress;	
	}
	
	public void setShowProductNumber(boolean showProductNumber) {
		this.showProductNumber = showProductNumber;	
	}
}
