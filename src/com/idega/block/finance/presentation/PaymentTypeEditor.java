package com.idega.block.finance.presentation;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.FinderException;
import javax.faces.component.UIComponent;
import com.idega.block.finance.data.PaymentType;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.util.Edit;
/**
 * 
 * Title: idegaclasses
 * 
 * Description:
 * 
 * Copyright: Copyright (c) 2001
 * 
 * Company:
 * 
 * @author <a href="mailto:aron@idega.is">aron@idega.is
 * 
 * @version 1.0
 *  
 */
public class PaymentTypeEditor extends Finance {
	public String strAction = "tke_action";
	protected final int ACT1 = 1, ACT2 = 2, ACT3 = 3, ACT4 = 4, ACT5 = 5;
	protected boolean isAdmin = false;
	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.finance";
	protected IWResourceBundle iwrb;
	protected IWBundle iwb;
	public String getLocalizedNameKey() {
		return "paymenttype";
	}
	public String getLocalizedNameValue() {
		return "Paymenttype";
	}
	protected void control(IWContext iwc) {
		if (isAdmin) {
			try {
				UIComponent MO = new Text();
				if (iwc.getParameter(strAction) == null) {
					MO = getMain(iwc, iCategoryId);
				}
				if (iwc.getParameter(strAction) != null) {
					String sAct = iwc.getParameter(strAction);
					int iAct = Integer.parseInt(sAct);
					switch (iAct) {
						case ACT1 :
							MO = getMain(iwc, iCategoryId);
							break;
						case ACT2 :
							MO = getChange(iwc, iCategoryId);
							break;
						case ACT3 :
							MO = doUpdate(iwc, iCategoryId);
							break;
						default :
							MO = getMain(iwc, iCategoryId);
							break;
					}
				}
				Table T = new Table(1, 3);
				T.add(Edit.headerText(iwrb.getLocalizedString("payment_type_editor", "Payment type editor"), 3), 1, 1);
				T.add(makeLinkTable(1, iCategoryId));
				T.add(MO);
				T.setWidth("100%");
				add(T);
			} catch (Exception S) {
				S.printStackTrace();
			}
		} else
			add(iwrb.getLocalizedString("access_denied", "Access denies"));
	}
	protected PresentationObject makeLinkTable(int menuNr, int iCategoryId) {
		Table LinkTable = new Table(3, 1);
		int last = 3;
		LinkTable.setWidth("100%");
		LinkTable.setCellpadding(2);
		LinkTable.setCellspacing(1);
		LinkTable.setColor(Edit.colorDark);
		LinkTable.setWidth(last, "100%");
		Link Link1 = new Link(iwrb.getLocalizedString("view", "View"));
		Link1.setFontColor(Edit.colorLight);
		Link1.addParameter(this.strAction, String.valueOf(this.ACT1));
		Link1.addParameter(Finance.getCategoryParameter(iCategoryId));
		Link Link2 = new Link(iwrb.getLocalizedString("change", "Change"));
		Link2.setFontColor(Edit.colorLight);
		Link2.addParameter(this.strAction, String.valueOf(this.ACT2));
		Link2.addParameter(Finance.getCategoryParameter(iCategoryId));
		if (isAdmin) {
			LinkTable.add(Link1, 1, 1);
			LinkTable.add(Link2, 2, 1);
		}
		return LinkTable;
	}
protected PresentationObject getMain(IWContext iwc,int iCategoryId){

    Table keyTable = new Table();
    Collection types = null;
    //List types =
	// FinanceFinder.getInstance().listOfPaymentTypes(iCategoryId);
	try {
		types = getFinanceService().getPaymentTypeHome().findAll();
	} 
	catch (RemoteException e) {
		e.printStackTrace();
	} 
	catch (FinderException e) {
		e.printStackTrace();
	}
	int count = 0;

    if(types !=null)

      count = types.size();

    keyTable = new Table(6,count+1);

    keyTable.setWidth("100%");

    keyTable.setHorizontalZebraColored(Edit.colorLight,Edit.colorWhite);

    keyTable.setRowColor(1,Edit.colorMiddle);

    keyTable.setCellpadding(2);

    keyTable.setCellspacing(1) ;

    //keyTable.setColumnAlignment(3, "right");

    keyTable.add(Edit.formatText("Nr"),1,1);

    keyTable.add(Edit.formatText(iwrb.getLocalizedString("name","Name")),2,1);

    keyTable.add(Edit.formatText(iwrb.getLocalizedString("info","Info")),3,1);

    keyTable.add(Edit.formatText(iwrb.getLocalizedString("payments","Payments")),4,1);

    keyTable.add(Edit.formatText(iwrb.getLocalizedString("amount_cost","Amount cost")),5,1);

    keyTable.add(Edit.formatText(iwrb.getLocalizedString("percent_cost","Percent cost")),6,1);

    if(isAdmin){

      if(count > 0){

        PaymentType type;
        int row  = 2;
        int rowcount = 1;
        for (Iterator iter = types.iterator(); iter.hasNext();) {
        	type = (PaymentType)  iter.next();
		

          keyTable.add(Edit.formatText( String.valueOf(rowcount++)),1,row);

          keyTable.add(Edit.formatText(type.getName()),2,row);

          keyTable.add(Edit.formatText(type.getInfo()),3,row);

          keyTable.add(Edit.formatText(type.getPayments()),4,row);

          keyTable.add(Edit.formatText(Float.toString(type.getAmountCost())),5,row);

          keyTable.add(Edit.formatText(Float.toString(type.getPercentCost())),6,row);
          row++;
        }

      }

    }

    return (keyTable);

  }	protected UIComponent getChange(IWContext iwc, int iCategoryId) throws SQLException {
		Form myForm = new Form();
		myForm.add(Finance.getCategoryParameter(iCategoryId));
		//myForm.maintainAllParameters();
		//List keys = FinanceFinder.getInstance().listOfPaymentTypes(iCategoryId);
		Collection types = null;
		try {
			types = getFinanceService().getPaymentTypeHome().findAll();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		} 
		catch (FinderException e) {
			e.printStackTrace();
		}
		int count = 0;
		if (types != null)
			count = types.size();
		int inputcount = count + 5;
		Table inputTable = new Table(7, inputcount + 1);
		inputTable.setWidth("100%");
		inputTable.setCellpadding(2);
		inputTable.setCellspacing(1);
		// inputTable.setColumnAlignment(1,"right");
		inputTable.setHorizontalZebraColored(Edit.colorLight, Edit.colorWhite);
		inputTable.setRowColor(1, Edit.colorMiddle);
		inputTable.add(Edit.formatText("Nr"), 1, 1);
		inputTable.add(Edit.formatText(iwrb.getLocalizedString("name", "Name")), 2, 1);
		inputTable.add(Edit.formatText(iwrb.getLocalizedString("info", "Info")), 3, 1);
		inputTable.add(Edit.formatText(iwrb.getLocalizedString("payments", "Payments")), 4, 1);
		inputTable.add(Edit.formatText(iwrb.getLocalizedString("amount_cost", "Amount cost")), 5, 1);
		inputTable.add(Edit.formatText(iwrb.getLocalizedString("percent_cost", "Percent cost")), 6, 1);
		inputTable.add(Edit.formatText(iwrb.getLocalizedString("delete", "Delete")), 7, 1);
		PaymentType key;
		TextInput nameInput, infoInput, cost;
		HiddenInput idInput;
		CheckBox delCheck;
		DropdownMenu payments, percent;
		Iterator iter = types.iterator();
		for (int i = 1; i <= inputcount; i++) {
			String rownum = String.valueOf(i);
			//int pos;
			nameInput = new TextInput("tke_nameinput" + i);
			infoInput = new TextInput("tke_infoinput" + i);
			payments = getIntDrop("tke_paym" + i, 1, 12, "");
			cost = new TextInput("tke_cost" + i);
			percent = getIntDrop("tke_percent" + i, 0, 100, "");
			int id = -1;
			if (i <= count && iter.hasNext()) {
				//pos = i - 1;
				key = (PaymentType) iter.next();
				nameInput.setContent(key.getName());
				infoInput.setContent(key.getInfo());
				payments.setSelectedElement(Integer.toString(key.getPayments()));
				cost.setContent(Float.toString(key.getAmountCost()));
				percent.setSelectedElement(Float.toString(key.getPercentCost()));
				idInput = new HiddenInput("tke_idinput" + i, key.getPrimaryKey().toString());
				delCheck = new CheckBox("tke_delcheck" + i, "true");
				Edit.setStyle(delCheck);
				inputTable.add(delCheck, 7, i + 1);
			}
			idInput = new HiddenInput("tke_idinput" + i, String.valueOf(id));
			nameInput.setSize(20);
			infoInput.setSize(40);
			Edit.setStyle(nameInput);
			Edit.setStyle(infoInput);
			inputTable.add(Edit.formatText(rownum), 1, i + 1);
			inputTable.add(nameInput, 2, i + 1);
			inputTable.add(infoInput, 3, i + 1);
			inputTable.add(payments, 4, i + 1);
			inputTable.add(cost, 5, i + 1);
			inputTable.add(percent, 6, i + 1);
			inputTable.add(idInput);
		}
		myForm.add(new HiddenInput("tke_count", String.valueOf(inputcount)));
		myForm.add(new HiddenInput(this.strAction, String.valueOf(this.ACT3)));
		myForm.add(inputTable);
		SubmitButton save = new SubmitButton(iwrb.getLocalizedString("save", "Save"));
		Edit.setStyle(save);
		myForm.add(save);
		return (myForm);
	}
	protected PresentationObject doUpdate(IWContext iwc, int iCategoryId) {
		/*int count = Integer.parseInt(iwc.getParameter("tke_count"));
		String sName, sDel, sCost, sPercent, sPayments;
		String sInfo;
		Float cost = null, percent = null;
		Integer payments = null;
		int ID;
		for (int i = 1; i < count + 1; i++) {
			sName = iwc.getParameter("tke_nameinput" + i);
			sInfo = iwc.getParameter("tke_infoinput" + i);
			sDel = iwc.getParameter("tke_delcheck" + i);
			sPayments = iwc.getParameter("tke_paym" + i);
			sCost = iwc.getParameter("tke_cost" + i);
			sPercent = iwc.getParameter("tke_percent" + i);
			ID = Integer.parseInt(iwc.getParameter("tke_idinput" + i));
			if (sDel != null && sDel.equalsIgnoreCase("true")) {
				//FinanceBusiness.deleteTariffKey(ID);
			} else if (!"".equals(sName)) {
				if (!"".equals(sPayments))
					payments = Integer.valueOf(sPayments);
				if (!"".equals(sCost))
					cost = Float.valueOf(sCost);
				if (!"".equals(sPercent)) {
					int p = Integer.parseInt(sPercent);
					percent = new Float((float) p / 100);
				}
				//FinanceBusiness.savePaymentType(ID, sName, sInfo, iCategoryId, payments, cost, percent);
			}
		}// for loop*/
		return getMain(iwc, iCategoryId);
	}
	private DropdownMenu getIntDrop(String name, int from, int to, String selected) {
		DropdownMenu drp = new DropdownMenu(name);
		for (int i = from; i <= to; i++) {
			drp.addMenuElement(String.valueOf(i));
		}
		drp.setSelectedElement(selected);
		return drp;
	}
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	public void main(IWContext iwc) {
		iwrb = getResourceBundle(iwc);
		iwb = getBundle(iwc);
		isAdmin = iwc.hasEditPermission(this);
		control(iwc);
	}
}// class TariffKeyEditor
