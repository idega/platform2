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
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

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
		if (this.isAdmin) {
			try {
				PresentationObject MO = new Text();
				if (iwc.getParameter(this.strAction) == null) {
					MO = getMain(iwc);
				}
				if (iwc.getParameter(this.strAction) != null) {
					String sAct = iwc.getParameter(this.strAction);
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
				
				setLocalizedTitle("tariff_key_editor", "Tariff key editor");
				setSearchPanel(makeLinkTable(1));
				setMainPanel(MO);
				
			} catch (Exception S) {
				S.printStackTrace();
			}
		}
		else {
			add(getErrorText(localize("access_denied", "Access denies")));
		}
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
		
		LinkTable.setWidth(last, "100%");
		Link Link1 = new Link(getHeader(localize("view", "View")));
		
		Link1.addParameter(this.strAction, String.valueOf(this.ACT1));
		//Link1.addParameter(Finance.getCategoryParameter(getFinanceCategoryId()));
		Link Link2 = new Link(getHeader(localize("change", "Change")));
		
		Link2.addParameter(this.strAction, String.valueOf(this.ACT2));
		//Link2.addParameter(Finance.getCategoryParameter(getFinanceCategoryId()));
		if (this.isAdmin) {
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
		if (keys != null) {
			count = keys.size();
		}
		keyTable = new Table(3, count + 1);
		keyTable.setWidth("100%");
		keyTable.setHorizontalZebraColored(getZebraColor1(), getZebraColor2());
		keyTable.setRowColor(1, getHeaderColor());
		keyTable.setCellpadding(2);
		keyTable.setCellspacing(1);
		//keyTable.setColumnAlignment(3, "right");
		keyTable.add(getHeader("Nr"), 1, 1);
		keyTable.add(getHeader(localize("name", "Name")), 2, 1);
		keyTable.add(getHeader(localize("info", "Info")), 3, 1);
		if (this.isAdmin) {
			if (count > 0) {
				TariffKey key;
				int row = 2;
				int rowcount = 1;
				for (Iterator iter = keys.iterator(); iter.hasNext();) {
					key = (TariffKey) iter.next();
					keyTable.add(getText(String.valueOf(rowcount++)), 1, row);
					keyTable.add(getText(key.getName()), 2, row);
					keyTable.add(getText(key.getInfo()), 3, row);
					row++;
				}
			}
		}
		return (keyTable);
	}
	protected PresentationObject getChange(IWContext iwc) throws SQLException {
		
		
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
		if (keys != null) {
			count = keys.size();
		}
		int inputcount = count + 5;
		DataTable inputTable = getDataTable();
		inputTable.setWidth("100%");
		inputTable.setUseBottom(false);
		inputTable.setUseTop(false);
		inputTable.setTitlesHorizontal(true);
		inputTable.add(getHeader("Nr"), 1, 1);
		inputTable.add(getHeader(localize("name", "Name")), 2, 1);
		inputTable.add(getHeader(localize("info", "Info")), 3, 1);
		inputTable.add(getHeader(localize("delete", "Delete")), 4, 1);
		TariffKey key;
		Iterator iter = keys.iterator();
		for (int i = 1; i <= inputcount; i++) {
			String rownum = String.valueOf(i);
			TextInput nameInput, infoInput;
			HiddenInput idInput;
			CheckBox delCheck;
			//int pos;
			if (i <= count && iter.hasNext()) {
				//pos = i - 1;
				key = (TariffKey) iter.next();
				nameInput = getTextInput("tke_nameinput" + i, key.getName());
				infoInput = getTextInput("tke_infoinput" + i, key.getInfo());
				idInput = new HiddenInput("tke_idinput" + i, String.valueOf(key.getID()));
				delCheck = getCheckBox("tke_delcheck" + i, "true");
				inputTable.add(delCheck, 4, i + 1);
			} else {
				nameInput = getTextInput("tke_nameinput" + i);
				infoInput = getTextInput("tke_infoinput" + i);
				idInput = new HiddenInput("tke_idinput" + i, "-1");
			}
			nameInput.setSize(20);
			infoInput.setSize(40);
			
			inputTable.add(getText(String.valueOf(rownum)), 1, i + 1);
			inputTable.add(nameInput, 2, i + 1);
			inputTable.add(infoInput, 3, i + 1);
			inputTable.add(idInput);
		}
		inputTable.add(Finance.getCategoryParameter(getFinanceCategoryId().intValue()));
		inputTable.add(new HiddenInput("tke_count", String.valueOf(inputcount)));
		inputTable.add(new HiddenInput(this.strAction, String.valueOf(this.ACT3)));
		
		SubmitButton save = new SubmitButton(localize("save", "Save"));
		save = (SubmitButton) setStyle(save,STYLENAME_INTERFACE_BUTTON);
		inputTable.addButton(save);
		return (inputTable);
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
