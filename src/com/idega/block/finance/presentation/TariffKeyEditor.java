package com.idega.block.finance.presentation;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.finance.data.TariffKey;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.util.Edit;
/**
 * Title: idegaclasses Description: Copyright: Copyright (c) 2001 Company:
 * 
 * @author <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */
public class TariffKeyEditor extends Finance {
	public String strAction = "tke_action";
	protected final int ACT1 = 1, ACT2 = 2, ACT3 = 3, ACT4 = 4, ACT5 = 5;
	public String getLocalizedNameKey() {
		return "tariffkey";
	}
	public String getLocalizedNameValue() {
		return "Tariffkey";
	}
	protected void control(IWContext iwc) {
		if (isAdmin) {
			try {
				PresentationObject MO = new Text();
				if (iwc.getParameter(strAction) == null) {
					MO = getMain(iwc);
				}
				if (iwc.getParameter(strAction) != null) {
					String sAct = iwc.getParameter(strAction);
					int iAct = Integer.parseInt(sAct);
					switch (iAct) {
						case ACT1 :
							MO = getMain(iwc);
							break;
						case ACT2 :
							MO = getChange(iwc);
							break;
						case ACT3 :
							MO = doUpdate(iwc);
							break;
						default :
							MO = getMain(iwc);
							break;
					}
				}
				Table T = new Table(1, 3);
				T.add(Edit.headerText(iwrb.getLocalizedString("tariff_key_editor", "Tariff key editor"), 3), 1, 1);
				T.add(makeLinkTable(1));
				T.add(MO);
				T.setWidth("100%");
				add(T);
			} catch (Exception S) {
				S.printStackTrace();
			}
		} else
			add(iwrb.getLocalizedString("access_denied", "Access denies"));
	}
	protected PresentationObject makeTabTable(int iCategoryId, int iTariffGroupId) {
		Table T = new Table();
		return T;
	}
	protected PresentationObject makeLinkTable(int menuNr) {
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
		Link1.addParameter(Finance.getCategoryParameter(getFinanceCategoryId()));
		Link Link2 = new Link(iwrb.getLocalizedString("change", "Change"));
		Link2.setFontColor(Edit.colorLight);
		Link2.addParameter(this.strAction, String.valueOf(this.ACT2));
		Link2.addParameter(Finance.getCategoryParameter(getFinanceCategoryId()));
		if (isAdmin) {
			LinkTable.add(Link1, 1, 1);
			LinkTable.add(Link2, 2, 1);
		}
		return LinkTable;
	}
	protected PresentationObject getMain(IWContext iwc) {
		Table keyTable = new Table();
		//List keys =
		// FinanceFinder.getInstance().listOfTariffKeys(iCategoryId);
		Collection keys = null;
		try {
			keys = getFinanceService().getTariffKeyHome().findByCategory(getFinanceCategoryId());
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		int count = 0;
		if (keys != null)
			count = keys.size();
		keyTable = new Table(3, count + 1);
		keyTable.setWidth("100%");
		keyTable.setHorizontalZebraColored(Edit.colorLight, Edit.colorWhite);
		keyTable.setRowColor(1, Edit.colorMiddle);
		keyTable.setCellpadding(2);
		keyTable.setCellspacing(1);
		//keyTable.setColumnAlignment(3, "right");
		keyTable.add(Edit.formatText("Nr"), 1, 1);
		keyTable.add(Edit.formatText(iwrb.getLocalizedString("name", "Name")), 2, 1);
		keyTable.add(Edit.formatText(iwrb.getLocalizedString("info", "Info")), 3, 1);
		if (isAdmin) {
			if (count > 0) {
				TariffKey key;
				int row = 2;
				int rowcount = 1;
				for (Iterator iter = keys.iterator(); iter.hasNext();) {
					key = (TariffKey) iter.next();
					keyTable.add(Edit.formatText(String.valueOf(rowcount++)), 1, row);
					keyTable.add(Edit.formatText(key.getName()), 2, row);
					keyTable.add(Edit.formatText(key.getInfo()), 3, row);
					row++;
				}
			}
		}
		return (keyTable);
	}
	protected PresentationObject getChange(IWContext iwc) throws SQLException {
		Form myForm = new Form();
		myForm.add(Finance.getCategoryParameter(getFinanceCategoryId().intValue()));
		//myForm.maintainAllParameters();
		Collection keys = null;
		try {
			keys = getFinanceService().getTariffKeyHome().findByCategory(getFinanceCategoryId());
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		//FinanceFinder.getInstance().listOfTariffKeys(iCategoryId);
		int count = 0;
		if (keys != null)
			count = keys.size();
		int inputcount = count + 5;
		Table inputTable = new Table(4, inputcount + 1);
		inputTable.setWidth("100%");
		inputTable.setCellpadding(2);
		inputTable.setCellspacing(1);
		// inputTable.setColumnAlignment(1,"right");
		inputTable.setHorizontalZebraColored(Edit.colorLight, Edit.colorWhite);
		inputTable.setRowColor(1, Edit.colorMiddle);
		inputTable.add(Edit.formatText("Nr"), 1, 1);
		inputTable.add(Edit.formatText(iwrb.getLocalizedString("name", "Name")), 2, 1);
		inputTable.add(Edit.formatText(iwrb.getLocalizedString("info", "Info")), 3, 1);
		inputTable.add(Edit.formatText(iwrb.getLocalizedString("delete", "Delete")), 4, 1);
		TariffKey key;
		Iterator iter = keys.iterator();
		for (int i = 1; i <= inputcount; i++) {
			String rownum = String.valueOf(i);
			TextInput nameInput, infoInput;
			HiddenInput idInput;
			CheckBox delCheck;
			int pos;
			if (i <= count && iter.hasNext()) {
				pos = i - 1;
				key = (TariffKey) iter.next();
				nameInput = new TextInput("tke_nameinput" + i, key.getName());
				infoInput = new TextInput("tke_infoinput" + i, key.getInfo());
				idInput = new HiddenInput("tke_idinput" + i, String.valueOf(key.getID()));
				delCheck = new CheckBox("tke_delcheck" + i, "true");
				Edit.setStyle(delCheck);
				inputTable.add(delCheck, 4, i + 1);
			} else {
				nameInput = new TextInput("tke_nameinput" + i);
				infoInput = new TextInput("tke_infoinput" + i);
				idInput = new HiddenInput("tke_idinput" + i, "-1");
			}
			nameInput.setSize(20);
			infoInput.setSize(40);
			Edit.setStyle(nameInput);
			Edit.setStyle(infoInput);
			inputTable.add(Edit.formatText(rownum), 1, i + 1);
			inputTable.add(nameInput, 2, i + 1);
			inputTable.add(infoInput, 3, i + 1);
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
	protected PresentationObject doUpdate(IWContext iwc) {
		int count = Integer.parseInt(iwc.getParameter("tke_count"));
		String sName, sInfo, sDel;
		Integer ID;
		for (int i = 1; i < count + 1; i++) {
			try {
				sName = iwc.getParameter("tke_nameinput" + i);
				sInfo = iwc.getParameter("tke_infoinput" + i);
				sDel = iwc.getParameter("tke_delcheck" + i);
				ID = Integer.valueOf(iwc.getParameter("tke_idinput" + i));
				if (sDel != null && sDel.equalsIgnoreCase("true")) {
					//FinanceBusiness.deleteTariffKey(ID);
					getFinanceService().removeTariffKey(ID);
				} else if (!"".equals(sName)) {
					//FinanceBusiness.saveTariffKey(ID,sName,sInfo,iCategoryId);
					getFinanceService().createOrUpdateTariffKey(ID, sName, sInfo, getFinanceCategoryId());
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			} catch (RemoveException e) {
				e.printStackTrace();
			} catch (CreateException e) {
				e.printStackTrace();
			}
		}// for loop
		return getMain(iwc);
	}
	public void main(IWContext iwc) {
		control(iwc);
	}
}// class TariffKeyEditor
